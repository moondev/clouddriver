/*
 * Copyright 2016 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.google.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.netflix.spinnaker.clouddriver.google.model.callbacks.Utils
import com.netflix.spinnaker.clouddriver.model.HealthState
import com.netflix.spinnaker.clouddriver.model.Instance

class GoogleInstance2 implements Instance, Serializable {

  String name
  String instanceId
  String instanceType
  Long launchTime
  String zone
  List<GoogleHealth> healths

  private Map<String, Object> dynamicProperties = new HashMap<String, Object>()

  @Override
  @JsonIgnore
  List<Map<String, String>> getHealth() {
    ObjectMapper mapper = new ObjectMapper()
    return healths.collect { mapper.convertValue(it, new TypeReference<Map<String, String>>() {}) }
  }

  @JsonAnyGetter
  public Map<String, Object> anyProperty() {
    return dynamicProperties;
  }

  @JsonAnySetter
  public void set(String name, Object value) {
    dynamicProperties.put(name, value);
  }

  @Override
  HealthState getHealthState() {
    someUpRemainingUnknown(health) ? HealthState.Up :
        anyStarting(health) ? HealthState.Starting :
            anyDown(health) ? HealthState.Down :
                anyOutOfService(health) ? HealthState.OutOfService :
                    HealthState.Unknown
  }

  private static boolean anyDown(List<Map<String, String>> healthList) {
    healthList.any { it.state == HealthState.Down }
  }

  private static boolean someUpRemainingUnknown(List<Map<String, String>> healthList) {
    List<Map<String, String>> knownHealthList = healthList.findAll { it.state != HealthState.Unknown }
    knownHealthList ? knownHealthList.every { it.state == HealthState.Up } : false
  }

  private static boolean anyStarting(List<Map<String, String>> healthList) {
    healthList.any { it.state == HealthState.Starting }
  }

  private static boolean anyOutOfService(List<Map<String, String>> healthList) {
    healthList.any { it.state == HealthState.OutOfService }
  }

  @Override
  boolean equals(Object o) {
    if (o instanceof GoogleInstance2) {
      o.name.equals(name)
    }
  }

  @Override
  int hashCode() {
    return name.hashCode()
  }
}
