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

package com.netflix.spinnaker.clouddriver.docker.registry.controllers

import com.netflix.spinnaker.cats.cache.Cache
import com.netflix.spinnaker.cats.cache.CacheData
import com.netflix.spinnaker.clouddriver.docker.registry.cache.Keys
import com.netflix.spinnaker.clouddriver.docker.registry.provider.DockerRegistryProviderUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/dockerRegistry/images")
class DockerRegistryImageLookupController {
  @Autowired
  private final Cache cacheView

  @RequestMapping(value = '/find', method = RequestMethod.GET)
  List<Map> find(LookupOptions lookupOptions) {
    def account = ""
    def image = ""
    def tag = ""

    if (lookupOptions.account) {
      account = lookupOptions.account
    }

    if (lookupOptions.q) {
      def lastColon = lookupOptions.q.lastIndexOf(':')
      if (lastColon != -1) {
        image = lookupOptions.q.substring(0, lastColon)
        tag = lookupOptions.q.(lastColon + 1)
      } else {
        image = lookupOptions.q
      }
    }

    image = image ?: '*'
    account = account ?: '*'
    tag = tag ?: '*'

    print ",, image = $image, account = $account, tag = $tag\n"

    def key = Keys.getTaggedImageKey(account, image, tag)

    print ",, key = $key\n"

    Set<CacheData> images = DockerRegistryProviderUtils.getAllMatchingKeyPattern(cacheView, Keys.Namespace.TAGGED_IMAGE.ns, key)

    return images.collect({
      [imageName: (String) it.attributes.name,
       account: it.attributes.account]
    })
  }

  private static class LookupOptions {
    String q
    String account
    String region
  }
}
