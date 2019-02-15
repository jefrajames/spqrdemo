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
import javax.inject.Inject;
import org.bson.types.ObjectId;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.diana.api.document.DocumentQuery;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;

/**
 * JNoSQL repository for Vote entity.
 *
 * @author JF James
 */
@ApplicationScoped
public class VoteRepository {

    private static final String COLLECTION = "votes";

    @Inject
    protected DocumentTemplate template;

    public List<Vote> findByUserId(String userId) {
        DocumentQuery query = select().from(COLLECTION).where("userId").eq(new ObjectId(userId)).build();
        return template.select(query);
    }

    public List<Vote> findByLinkId(String linkId) {
        DocumentQuery query = select().from(COLLECTION).where("linkId").eq(new ObjectId(linkId)).build();
        return template.select(query);
    }

    public List<Vote> findAll() {
        DocumentQuery query = select().from(COLLECTION).build();
        return template.select(query);
    }

    public Vote save(Vote vote) {
        return template.insert(vote);
    }

}
