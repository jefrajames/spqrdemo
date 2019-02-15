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
import io.leangen.graphql.annotations.GraphQLContext;
import io.leangen.graphql.annotations.GraphQLQuery;
import java.util.Optional;
import lombok.extern.java.Log;
import org.jefrajames.spqrdemo.cdi.GraphQLComponent;

/**
 * A GraphQL resolver to retrieve the User and Link data for a vote.
 *
 * @author JF James
 */
@GraphQLComponent
@Log
public class VoteResolver {

    @Inject
    private LinkRepository linkRepo;

    @Inject
    private UserRepository userRepo;

    @GraphQLQuery
    public Optional<User> user(@GraphQLContext Vote vote) {
        return userRepo.findById(vote.getUserId());
    }

    @GraphQLQuery
    public Optional<Link> link(@GraphQLContext Vote vote) {
        return linkRepo.findById(vote.getLinkId());
    }

}
