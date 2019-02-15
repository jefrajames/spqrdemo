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

import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import lombok.extern.java.Log;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;
import org.jnosql.diana.api.document.DocumentConfiguration;
import org.jnosql.diana.mongodb.document.MongoDBDocumentConfiguration;

/**
 *
 * This class is a CDI producer for DocumentCollectionManager.
 * 
 * Is is required to inject Repository and Template objects.
 * 
 * A WELD-001334 error occurs if it doesn't exist : Unsatisfied dependencies for type DocumentCollectionManager with qualifiers @Default
 * 
 * @author JF James
 */
@ApplicationScoped
@Log
public class DocumentCollectionManagerProducer {
    
    private static final String DATABASE = "hackernews";
    
    private DocumentConfiguration configuration;
    
    private DocumentCollectionManagerFactory managerFactory;
    
    @PostConstruct
    public void init() {
        configuration = new MongoDBDocumentConfiguration();
        managerFactory = configuration.get(); // Properties coming from diana-mongodb.properties
        log.log(Level.INFO, "calling init, managerFactory={0}", managerFactory);
    }
    
    // This enables to inject DocumentTemplate and repository objects
    @Produces
    public DocumentCollectionManager getManager() {
        return managerFactory.get(DATABASE);
    }
    

    @PreDestroy
    public void closeFactory() {
        if ( managerFactory!=null )
            managerFactory.close();
    }
    
}
