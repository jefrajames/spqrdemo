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

import java.io.IOException;
import java.util.Optional;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;

/**
 * A Web Filter to check the user authentication.
 *
 * @author JF James
 */
@WebFilter(filterName = "AuthContextFilter", urlPatterns = "/*")
@Log
public class AuthContextFilter implements Filter {

    @Inject
    private UserRepository userRepo;

    @Inject
    private AuthContext authContext;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authHeader = httpRequest.getHeader("Authorization");

        User user = Optional.of(httpRequest)
                .map(req -> req.getHeader("Authorization"))
                .filter(id -> !id.isEmpty())
                .map(id -> id.replace("Bearer ", ""))
                .map(userRepo::findById)
                .orElse(null);

        if (user == null) {
            log.warning("Unauthenticated user, bad authorization token");
        }

        authContext.setUser(user);

        chain.doFilter(httpRequest, response);
    }

}
