--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

create table tenant_server_connections(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`schema_server` VARCHAR(100) NOT NULL,
    `schema_name` VARCHAR(100) NOT NULL,
	`schema_server_port` VARCHAR(10) NOT NULL,
	`schema_username` VARCHAR(100) NOT NULL,
	`schema_password` VARCHAR(100) NOT NULL,
	`auto_update` TINYINT(1) NOT NULL,
	PRIMARY KEY (`id`)
	)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

LOCK TABLES `tenant_server_connections` WRITE;
/*!40000 ALTER TABLE `tenant_server_connections` DISABLE KEYS */;
INSERT INTO `tenant_server_connections` (`id`, `schema_server`, `schema_name`, `schema_server_port`, `schema_username`, `schema_password`, `auto_update`) VALUES (1, '${dbHost}', 'tn01', '${dbPort}', '${dbUserName}', '${dbPassword}', 1);
INSERT INTO `tenant_server_connections` (`id`, `schema_server`, `schema_name`, `schema_server_port`, `schema_username`, `schema_password`, `auto_update`) VALUES (2, '${dbHost}', 'tn02', '${dbPort}', '${dbUserName}', '${dbPassword}', 1);
/*!40000 ALTER TABLE `tenant_server_connections` ENABLE KEYS */;
UNLOCK TABLES;