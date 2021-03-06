/*
 * Copyright 2015 Pivotal Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.cf.model

import com.netflix.spectator.api.Registry
import com.netflix.spectator.api.Timer
import com.netflix.spinnaker.clouddriver.model.Application
import com.netflix.spinnaker.clouddriver.model.ApplicationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct
import java.util.concurrent.Callable

@Component
class CloudFoundryApplicationProvider implements ApplicationProvider {

  @Autowired
  Registry registry

  @Autowired
  CloudFoundryResourceRetriever cloudFoundryResourceRetriever

  Timer applications

  Timer applicationByName

  @PostConstruct
  void init() {
    String[] tags = ['className', this.class.simpleName]
    applications = registry.timer('applications', tags)
    applicationByName = registry.timer('applicationByName', tags)
  }

  @Override
  Set<? extends Application> getApplications(boolean expand) {
    applications.record({
      Collections.unmodifiableSet(
        cloudFoundryResourceRetriever.applicationByName.values() as Set)
    } as Callable<Set<? extends Application>>)
  }

  @Override
  Application getApplication(String name) {
    applicationByName.record({
      cloudFoundryResourceRetriever.applicationByName[name]
    } as Callable<Application>)
  }

}
