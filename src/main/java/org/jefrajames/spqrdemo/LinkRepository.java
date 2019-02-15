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

import javax.enterprise.context.ApplicationScoped;

import org.bson.types.ObjectId;

import java.util.Optional;
import javax.inject.Inject;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.diana.api.document.DocumentQuery;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;

/**
 * JNoSQL repository for the Link entity.
 *
 * @author JF James
 */
@ApplicationScoped
public class LinkRepository {

    private static final String COLLECTION = "links";

    @Inject
    protected DocumentTemplate template;

    public List<Link> findAll() {
        DocumentQuery query = select().from(COLLECTION).build();
        return template.select(query);
    }

    public Optional<Link> findById(String id) {
        DocumentQuery query = select().from(COLLECTION).where("_id").eq(new ObjectId(id)).build();
        return template.singleResult(query);
    }

    public Link save(Link link) {
        return template.insert(link);
    }

}
