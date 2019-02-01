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
package org.jefrajames.spqrdemo;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLHttpServlet;
import lombok.extern.java.Log;

/**
 *
 * A WebListener that initializes a SimpleGraphQLHttpServlet 
 * with a GraphQL schema.
 *
 * Check URL is: 
 * - http://localhost:9080/graphql/schema.json on OpenLiberty
 * - http://localhost:8080/spqrdemo/graphql/schema.json on Payara
 *
 * @author JF James
 *
 */
@WebListener
@Log
public class GraphQLListener implements ServletContextListener {

    @Inject
    private GraphQLSchema schema;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        // How to replace the deprecated build method?
        SimpleGraphQLHttpServlet graphQLServlet = SimpleGraphQLHttpServlet.newBuilder(schema).build();

        ServletContext context = sce.getServletContext();
        ServletRegistration.Dynamic servlet = context.addServlet(SERVLET_NAME, graphQLServlet);
        servlet.addMapping(SERVLET_URL);

        log.info("Application (re)started ...");
    }

    private static final String SERVLET_NAME = "GraphQLServlet";
    private static final String[] SERVLET_URL = new String[]{"/graphql/*"};

}
