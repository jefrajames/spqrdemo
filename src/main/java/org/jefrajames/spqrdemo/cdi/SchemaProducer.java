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

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;
import io.leangen.graphql.metadata.strategy.query.AnnotatedResolverBuilder;
import io.leangen.graphql.metadata.strategy.value.jackson.JacksonValueMapperFactory;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import lombok.extern.java.Log;

/**
 * This class generates the GraphQL schema from GraphQLConfig previously initialized by GraphQLExtension.
 * 
 * The schema is eagerly generated by observing @Initialized(ApplicationScoped.class) CDI event.
 * 
 * @author JF James
 */
@Log
@ApplicationScoped
public class SchemaProducer {

    @Inject
    private BeanManager beanManager;

    // This CDI bean is dynamically generated by GraphQLExtension
    @Inject
    private GraphQLConfig graphQLConfig;

    private GraphQLSchema schema;
    
    private void generateSchemaFromConfig() {
        
        GraphQLSchemaGenerator schemaGen = new GraphQLSchemaGenerator()
                .withResolverBuilders(new AnnotatedResolverBuilder())
                .withValueMapperFactory(new JacksonValueMapperFactory());
        
        for (Bean<?> bean : graphQLConfig.getGraphQLComponents()) {
            schemaGen.withOperationsFromSingleton(
                    beanManager.getReference(bean, bean.getBeanClass(), beanManager.createCreationalContext(bean)), 
                    bean.getBeanClass());
        }
        
        schema = schemaGen.generate();
    }

    @PostConstruct
    private void createSchema() {
        generateSchemaFromConfig();
        log.info("GraphQL schema created!");
    }

    @Produces
    public GraphQLSchema getSchema() {
        return schema;
    }

    // Enables to build the schema just after the application context is initialized
    // See CDI 2.0 Spec 6.7.3
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        log.info("GraphQL schema ready!");
    }

}
