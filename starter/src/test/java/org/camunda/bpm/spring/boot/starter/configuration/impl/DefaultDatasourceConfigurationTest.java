/*
 * Copyright © 2015 - 2018 camunda services GmbH and various authors (info@camunda.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.spring.boot.starter.configuration.impl;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDatasourceConfigurationTest {

  @Mock
  private PlatformTransactionManager platformTransactionManager;

  private CamundaBpmProperties camundaBpmProperties;

  @InjectMocks
  private DefaultDatasourceConfiguration defaultDatasourceConfiguration;

  private SpringProcessEngineConfiguration configuration;

  @Before
  public void before() {
    configuration = new SpringProcessEngineConfiguration();
    camundaBpmProperties = new CamundaBpmProperties();
    defaultDatasourceConfiguration.camundaBpmProperties = camundaBpmProperties;
  }

  @Test
  public void transactionManagerTest() {
    defaultDatasourceConfiguration.dataSource = mock(DataSource.class);
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(platformTransactionManager, configuration.getTransactionManager());
  }

  @Test
  public void defaultDataSourceTest() {
    DataSource datasourceMock = mock(DataSource.class);
    defaultDatasourceConfiguration.dataSource = datasourceMock;
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(datasourceMock, getDataSourceFromConfiguration());
  }

  @Test
  public void camundaDataSourceTest() {
    DataSource camundaDatasourceMock = mock(DataSource.class);
    defaultDatasourceConfiguration.camundaDataSource = camundaDatasourceMock;
    defaultDatasourceConfiguration.dataSource = mock(DataSource.class);
    defaultDatasourceConfiguration.preInit(configuration);
    assertSame(camundaDatasourceMock, getDataSourceFromConfiguration());
  }

  private DataSource getDataSourceFromConfiguration() {
    return ((TransactionAwareDataSourceProxy) configuration.getDataSource()).getTargetDataSource();
  }
}
