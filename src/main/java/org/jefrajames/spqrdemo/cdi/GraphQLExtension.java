/*
 * Copyright 2019 JF James.
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
package org.jefrajames.spqrdemo.cdi;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.util.AnnotationLiteral;
import lombok.extern.java.Log;

/**
 * This is a CDI extension that detects GraphQL components and generate a CDI
 * GraphQLConfig bean used by SchemaProducer.
 *
 * @author JF James
 */
@Log
public class GraphQLExtension implements Extension {

    private final Set<Bean<?>> graphQLComponents = new LinkedHashSet<>();

    // Add a GraphQLConfig CDI bean 
    public void addGraphQLConfig(@Observes AfterBeanDiscovery abd) {
        log.info("calling GraphQLExtension.addGraphQLConfig");

        abd
                .addBean()
                .types(GraphQLConfig.class)
                .qualifiers(new AnnotationLiteral<Any>() {}, new AnnotationLiteral<Default>() {})
                .scope(Dependent.class)
                .name(GraphQLConfig.class.getName())
                .beanClass(GraphQLConfig.class)
                .createWith(creationalContext -> {
                    GraphQLConfig instance = new GraphQLConfig();
                    instance.setGraphQLConfig(graphQLComponents);
                    return instance;
                });

    }

    // Detect and store GraphQLcomponents
    <X> void detectGraphQLComponent(@Observes ProcessBean<X> event) {
        if (event.getAnnotated().isAnnotationPresent(GraphQLComponent.class)) {
            graphQLComponents.add(event.getBean());
        }
    }

}
