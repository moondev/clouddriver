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

package com.netflix.spinnaker.clouddriver.kubernetes.deploy.converters.loadbalancer

import com.netflix.spinnaker.clouddriver.kubernetes.KubernetesOperation
import com.netflix.spinnaker.clouddriver.kubernetes.deploy.converters.KubernetesAtomicOperationConverterHelper
import com.netflix.spinnaker.clouddriver.kubernetes.deploy.description.loadbalancer.UpsertKubernetesLoadBalancerAtomicOperationDescription
import com.netflix.spinnaker.clouddriver.kubernetes.deploy.ops.loadbalancer.UpsertKubernetesLoadBalancerAtomicOperation
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperation
import com.netflix.spinnaker.clouddriver.orchestration.AtomicOperations
import com.netflix.spinnaker.clouddriver.security.AbstractAtomicOperationsCredentialsSupport
import org.springframework.stereotype.Component

@KubernetesOperation(AtomicOperations.UPSERT_LOAD_BALANCER)
@Component
class UpsertKubernetesLoadBalancerAtomicOperationConverter extends AbstractAtomicOperationsCredentialsSupport {
  AtomicOperation convertOperation(Map input) {
    new UpsertKubernetesLoadBalancerAtomicOperation(convertDescription(input))
  }

  UpsertKubernetesLoadBalancerAtomicOperationDescription convertDescription(Map input) {
    KubernetesAtomicOperationConverterHelper.convertDescription(input, this, UpsertKubernetesLoadBalancerAtomicOperationDescription)
  }
}

