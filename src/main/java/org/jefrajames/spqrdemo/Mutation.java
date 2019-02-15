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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import javax.inject.Inject;
import graphql.GraphQLException;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import java.util.Optional;
import lombok.extern.java.Log;
import org.jefrajames.spqrdemo.cdi.GraphQLComponent;

/**
 * A facade for all GraphQL mutations.
 *
 * @author JF James
 */
@Log
@GraphQLComponent
public class Mutation {

    @Inject
    private LinkRepository linkRepo;

    @Inject
    private UserRepository userRepo;

    @Inject
    private VoteRepository voteRepo;

    @Inject
    private AuthContext authContext;

    @GraphQLMutation(name = "createLink", description = "Create a link")
    public Link createLink(@GraphQLArgument(name = "url") String url,
            @GraphQLArgument(name = "description") String description) {

        if (authContext.getUser() == null) {
            throw new GraphQLException("Authentication required to create a Link");
        }

        Link newLink = new Link(url, description, authContext.getUser().getId());
        return linkRepo.save(newLink);
    }

    @GraphQLMutation(name = "createUser", description = "Create a user")
    public User createUser(@GraphQLArgument(name = "name") String name,
            @GraphQLArgument(name = "authData") AuthData authData) {
        User newUser = new User(name, authData.getEmail(), authData.getPassword());
        return userRepo.save(newUser);
    }

    @GraphQLMutation(name = "signinUser", description = "Signin a created user")
    public SigninPayload signinUser(@GraphQLArgument(name = "authData") AuthData auth) throws IllegalAccessException {
        Optional<User> user = userRepo.findByEmail(auth.getEmail());
        if (user.isPresent() && user.get().getPassword().equals(auth.getPassword())) {
            return new SigninPayload(user.get().getId(), user.get());
        }

        throw new GraphQLException("Invalid credentials");
    }

    @GraphQLMutation(name = "createVote", description = "Vote for a link")
    public Vote createVote(@GraphQLArgument(name = "linkId") String linkId) {

        if (authContext.getUser() == null) {
            throw new GraphQLException("Authentication required to vote");
        }

        ZonedDateTime now = Instant.now().atZone(ZoneOffset.UTC);

        if (!linkRepo.findById(linkId).isPresent()) {
            throw new GraphQLException("Cannot vote for an inexistant link");
        }

        Vote newVote = new Vote(now, authContext.getUser().getId(), linkId);
        return voteRepo.save(newVote);
    }

}
