/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.impl.history.handler.CompositeDbHistoryEventHandler;
import org.camunda.bpm.engine.impl.history.handler.CompositeHistoryEventHandler;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.property.CamundaBpmProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultHistoryConfigurationTest {

  @Mock
  private SpringProcessEngineConfiguration springProcessEngineConfiguration;

  private CamundaBpmProperties camundaBpmProperties;

  private DefaultHistoryConfiguration defaultHistoryConfiguration;

  @Before
  public void before() {
    camundaBpmProperties = new CamundaBpmProperties();
    defaultHistoryConfiguration = new DefaultHistoryConfiguration();
    defaultHistoryConfiguration.camundaBpmProperties = camundaBpmProperties;
  }

  @Test
  public void defaultHistoryLevelTest() {
    defaultHistoryConfiguration.preInit(springProcessEngineConfiguration);
    verify(springProcessEngineConfiguration, times(1)).setHistory(HistoryLevel.HISTORY_LEVEL_FULL.getName());
  }

  @Test
  public void historyLevelTest() {
    camundaBpmProperties.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_AUDIT.getName());
    defaultHistoryConfiguration.preInit(springProcessEngineConfiguration);
    verify(springProcessEngineConfiguration).setHistory(HistoryLevel.HISTORY_LEVEL_AUDIT.getName());
  }

  @Test
  public void noHistoryEventHandlerTest() {
    defaultHistoryConfiguration.preInit(springProcessEngineConfiguration);
    verify(springProcessEngineConfiguration, times(1)).setHistoryEventHandler(Mockito.any(CompositeDbHistoryEventHandler.class));
  }

  @Test
  public void historyEventHandlerTest() {
    HistoryEventHandler historyEventHandlerMock = mock(HistoryEventHandler.class);
    defaultHistoryConfiguration.historyEventHandler = historyEventHandlerMock;
    defaultHistoryConfiguration.preInit(springProcessEngineConfiguration);
    verify(springProcessEngineConfiguration).setHistoryEventHandler(any(CompositeHistoryEventHandler.class));
  }
}
