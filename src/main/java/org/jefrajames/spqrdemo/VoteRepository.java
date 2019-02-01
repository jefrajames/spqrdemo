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

import static com.mongodb.client.model.Filters.eq;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB repository for Vote entity.
 *
 * @author JF James
 */
@ApplicationScoped
public class VoteRepository {

    private MongoCollection<Document> votes;
    private MongoClient mongoClient;

    @PostConstruct
    private void setUp() {
        mongoClient = new MongoClient();
        MongoDatabase mongo = mongoClient.getDatabase("hackernews");
        votes = mongo.getCollection("votes");
    }

    @PreDestroy
    public void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public List<Vote> findByUserId(String userId) {
        List<Vote> list = new ArrayList<>();
        for (Document doc : votes.find(eq("userId", userId))) {
            list.add(vote(doc));
        }
        return list;
    }

    public List<Vote> findByLinkId(String linkId) {
        List<Vote> list = new ArrayList<>();
        for (Document doc : votes.find(eq("linkId", linkId))) {
            list.add(vote(doc));
        }
        return list;
    }

    public List<Vote> findAll() {
        List<Vote> allVotes = new ArrayList<>();
        for (Document doc : votes.find()) {
            allVotes.add(vote(doc));
        }
        return allVotes;
    }

    public Vote saveVote(Vote vote) {
        Document doc = new Document();
        doc.append("userId", vote.getUserId());
        doc.append("linkId", vote.getLinkId());
        doc.append("createdAt", Scalars.dateTime.getCoercing().serialize(vote.getCreatedAt()));
        votes.insertOne(doc);
        return new Vote(doc.get("_id").toString(), vote.getCreatedAt(), vote.getUserId(), vote.getLinkId());
    }

    private Vote vote(Document doc) {
        return new Vote(doc.get("_id").toString(), ZonedDateTime.parse(doc.getString("createdAt")),
                doc.getString("userId"), doc.getString("linkId"));
    }
}
