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
package com.netflix.spinnaker.kato.model.aws
import groovy.transform.Canonical

@Canonical
class TagsNotCreatedException<T> extends RuntimeException {

  final T objectToTag

  static TagsNotCreatedException<T> of(Throwable cause, T objectToTag) {
    String msg = "Failed to create tags for ${objectToTag}."
    new TagsNotCreatedException(cause, msg, objectToTag)
  }

  private TagsNotCreatedException(Throwable cause, String msg, T objectToTag) {
    super(msg, cause)
    this.objectToTag = objectToTag
  }
}