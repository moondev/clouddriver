/*
 * Copyright 2014 Netflix, Inc.
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

package com.netflix.spinnaker.clouddriver.model

import com.netflix.spinnaker.clouddriver.model.securitygroups.Rule

/**
 * A representation of a security group
 */
interface SecurityGroup {

  /**
   * The type of this security group. May reference the cloud provider to which it is associated
   *
   * @return
   */
  String getType()

  /**
   * The ID associated with this security group
   *
   * @return
   */
  String getId()

  /**
   * The name representing this security group
   *
   * @return
   */
  String getName()

  /**
   * The application associated with this security group
   *
   * @return
   */
  String getApplication()

  /**
   * The account associated with this security group
   *
   * @return
   */
  String getAccountName()

  /**
   * The region associated with this security group
   *
   * @return
   */
  String getRegion()

  /**
   * A representation of the inbound securityRules
   *
   * @return
   */
  Set<Rule> getInboundRules()

  Set<Rule> getOutboundRules()

  SecurityGroupSummary getSummary()
}
