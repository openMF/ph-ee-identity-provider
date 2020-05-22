/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.core.service;

import com.googlecode.flyway.core.Flyway;
import org.apache.fineract.organisation.tenant.TenantServerConnection;
import org.apache.fineract.organisation.tenant.TenantServerConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import static org.apache.fineract.config.ResourceServerConfig.IDENTITY_PROVIDER_RESOURCE_ID;

@Service
public class TenantDatabaseUpgradeService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TenantServerConnectionRepository repository;

    @Autowired
    private DataSourcePerTenantService dataSourcePerTenantService;

    @Value("${fineract.datasource.core.host}")
    private String hostname;

    @Value("${fineract.datasource.core.port}")
    private int port;

    @Value("${fineract.datasource.core.username}")
    private String username;

    @Value("${fineract.datasource.core.password}")
    private String password;

    @Value("${fineract.datasource.common.protocol}")
    private String jdbcProtocol;

    @Value("${fineract.datasource.common.subprotocol}")
    private String jdbcSubprotocol;

    @Value("${fineract.datasource.common.driverclass_name}")
    private String driverClass;

    @Value("${token.access.validity-seconds}")
    private String tokenAccessValiditySeconds;

    @Value("${token.refresh.validity-seconds}")
    private String tokenRefreshValiditySeconds;

    @PostConstruct
    public void upgradeAllTenants() {
        upgradeDefaultSchema();
        for (TenantServerConnection tenant : repository.findAll()) {
            if (tenant.isAutoUpdateEnabled()) {
                try {
                    ThreadLocalContextUtil.setTenant(tenant);
                    final Flyway flyway = new Flyway();
                    flyway.setDataSource(dataSourcePerTenantService.retrieveDataSource());
                    flyway.setLocations("sql/migrations/tenant");
                    flyway.setOutOfOrder(true);
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("tenantDatabase", tenant.getSchemaName());
                    placeholders.put("accessTokenValidity", tokenAccessValiditySeconds);
                    placeholders.put("refreshTokenValidity", tokenRefreshValiditySeconds);
                    placeholders.put("identityProviderResourceId", IDENTITY_PROVIDER_RESOURCE_ID);
                    flyway.setPlaceholders(placeholders);
                    flyway.migrate();
                } catch (Exception e) {
                    logger.error("Error when running flyway on tenant: {}", tenant.getSchemaName(), e);
                } finally {
                    ThreadLocalContextUtil.clear();
                }
            }
        }
    }

    private void upgradeDefaultSchema() {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourcePerTenantService.retrieveDataSource());
        flyway.setLocations("sql/migrations/core");
        flyway.setOutOfOrder(true);
        // flyway variables
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("dbHost", hostname);
        placeholders.put("dbPort", String.valueOf(port));
        placeholders.put("dbUserName", username);
        placeholders.put("dbPassword", password);
        flyway.setPlaceholders(placeholders);
        flyway.migrate();
    }
}