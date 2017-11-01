/*
 * Copyright 2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.boothub.template

import org.boothub.Initializr
import org.boothub.gradle.GradleTemplateBuilder
import org.boothub.gradle.OutputChecker
import spock.lang.Specification

import java.nio.file.Paths

class SimpleJavaSpec extends Specification {
    private static final String BASE_PATH = 'org/bizarre_soft/weird_app'

    private static final String TEMPLATE_DIR = getPath("/template")
    private static final String SAMPLE_CONTEXT = getPath("/sample-context.yml")

    private static final APP_MAIN_CLASS = 'WeirdAppMain'
    private static final APP_MAIN_CLASS_PATH = "$BASE_PATH/${APP_MAIN_CLASS}.class"

    private static String getPath(String resourcePath) {
        def resource = SimpleJavaSpec.class.getResource(resourcePath)
        assert resource : "Resource not available: $resourcePath"
        Paths.get(resource.toURI()).toAbsolutePath().toString()
    }

    def "should create a valid artifact"() {
        when:
        def artifacts = new GradleTemplateBuilder(TEMPLATE_DIR)
                .withContextFile(SAMPLE_CONTEXT)
                .runGradleBuild()
                .artifacts
        def jars = artifacts['jar']

        then:
        jars.size() == 1
        jars[0].getEntry(APP_MAIN_CLASS_PATH) != null
    }

    def "should create a valid application"() {
        when:
        def context = new Initializr(TEMPLATE_DIR).createContext(SAMPLE_CONTEXT)

        then:
        new OutputChecker(TEMPLATE_DIR, context)
                .checkOutput("Hello from $context.appMainClass!")
    }
}
