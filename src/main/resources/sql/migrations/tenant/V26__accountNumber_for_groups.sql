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

ALTER TABLE m_group ADD COLUMN `account_no` VARCHAR(20) NOT NULL;
UPDATE m_group set account_no = lpad(id,9,0);

ALTER TABLE `m_code_value` ADD COLUMN `is_active` TINYINT(1) NOT NULL DEFAULT '1' AFTER `code_score`;

alter table `m_code_value` add `is_mandatory` tinyint(1) not null default '0';