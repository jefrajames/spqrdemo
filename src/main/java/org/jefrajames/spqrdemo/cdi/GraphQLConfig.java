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

import java.util.Set;
import javax.enterprise.inject.spi.Bean;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * This class is completed and defined as a CDI bean by GraphQLExtension.
 * It provides to SchemaProducer all CDI beans qualified as GraphQLComponent.
 * 
 * @author JF James
 */
public class GraphQLConfig { 
    
    private Set<Bean<?>> graphQLComponents;
    
    
    public void setGraphQLConfig(Set<Bean<?>> graphQLComponents) {
        this.graphQLComponents = graphQLComponents;
    }
    
    public Set<Bean<?>> getGraphQLComponents() {
        return graphQLComponents;
    }
    
}
