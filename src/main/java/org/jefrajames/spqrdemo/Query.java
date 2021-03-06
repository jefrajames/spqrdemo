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

import java.util.List;
import javax.inject.Inject;
import io.leangen.graphql.annotations.GraphQLQuery;
import org.jefrajames.spqrdemo.cdi.GraphQLComponent;

/**
 * A facade for all GraphQL queries.
 *
 * @author JF James
 */
@GraphQLComponent
public class Query {

    @Inject
    private LinkRepository linkRepo;

    @Inject
    private UserRepository userRepo;

    @Inject
    private VoteRepository voteRepo;

    @GraphQLQuery(name = "allLinks", description = "Get all links")
    public List<Link> findAllLinks() {
        return linkRepo.findAll();
    }

    @GraphQLQuery(name = "allUsers", description = "Get all users")
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }

    @GraphQLQuery(name = "allVotes", description = "Get all votes")
    public List<Vote> findAllVotes() {
        return voteRepo.findAll();
    }

}
