/*
 * Copyright 2016 Google, Inc.
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

package com.netflix.spinnaker.clouddriver.docker.registry.api.v2.auth

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

class DockerBearerTokenServiceSpec extends Specification {
  private static final REALM1 = "https://auth.docker.io"
  private static final PATH1 = "token"
  private static final SERVICE1 = "registry.docker.io"
  private static final SCOPE1 = "repository:library/ubuntu:push,pull"
  private static final SCOPE2 = "repository:library/ubuntu:push"
  private static final REPOSITORY1 = "library/ubuntu"

  @Shared
  DockerBearerTokenService tokenService

  def setupSpec() {
    tokenService = new DockerBearerTokenService(null, null)
  }

  void "DockerBearerTokenService should parse Www-Authenticate header with full privileges and path."() {
    setup:
      def input = "Bearer realm=\"${REALM1}/${PATH1}\",service=\"${SERVICE1}\",scope=\"${SCOPE1}\""
    when:
      def result = tokenService.parseBearerAuthenticateHeader(input)

    then:
      result.path == PATH1
      result.realm == REALM1
      result.service == SERVICE1
      result.scope == SCOPE1
  }

  void "DockerBearerTokenService should parse Www-Authenticate header with some privileges and path."() {
    setup:
      def input = "Bearer realm=\"${REALM1}/${PATH1}\",service=\"${SERVICE1}\",scope=\"${SCOPE2}\""
    when:
      def result = tokenService.parseBearerAuthenticateHeader(input)

    then:
      result.path == PATH1
      result.realm == REALM1
      result.service == SERVICE1
      result.scope == SCOPE2
  }

  void "DockerBearerTokenService should parse Www-Authenticate header with some privileges and no path."() {
    setup:
      def input = "Bearer realm=\"${REALM1}\",service=\"${SERVICE1}\",scope=\"${SCOPE2}\""
    when:
      def result = tokenService.parseBearerAuthenticateHeader(input)

    then:
      !result.path
      result.realm == REALM1
      result.service == SERVICE1
      result.scope == SCOPE2
  }

  void "DockerBearerTokenService should parse unquoted Www-Authenticate header with some privileges and path."() {
    setup:
      def input = "Bearer realm=${REALM1}/${PATH1},service=${SERVICE1},scope=${SCOPE2}"
    when:
      def result = tokenService.parseBearerAuthenticateHeader(input)

    then:
      result.path == PATH1
      result.realm == REALM1
      result.service == SERVICE1
      result.scope == SCOPE2
  }

  void "DockerBearerTokenService should request a real token from Dockerhub's token registry."() {
    setup:
      def header = [:]
      header.value = "Bearer realm=\"${REALM1}/${PATH1}\",service=\"${SERVICE1}\",scope=\"${SCOPE1}\""
      header.name = "Www-Authenticate"
      def headers = [header]
    when:
      DockerBearerToken token = tokenService.getToken(REPOSITORY1, headers)

    then:
      token.token.length() > 0
  }

  void "DockerBearerTokenService should request a real token from Dockerhub's token registry, and supply a cached one."() {
    setup:
      def header = [:]
      header.value = "Bearer realm=\"${REALM1}/${PATH1}\",service=\"${SERVICE1}\",scope=\"${SCOPE1}\""
      header.name = "Www-Authenticate"
      def headers = [header]
    when:
      tokenService.getToken(REPOSITORY1, headers)
      DockerBearerToken token = tokenService.getToken(REPOSITORY1)

    then:
      token.token.length() > 0
  }
}
