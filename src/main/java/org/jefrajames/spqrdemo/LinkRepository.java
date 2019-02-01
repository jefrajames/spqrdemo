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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * MongoDB repository for the Link entity.
 *
 * @author JF James
 */
@ApplicationScoped
public class LinkRepository {

    private MongoCollection<Document> links;
    private MongoClient mongoClient;

    @PostConstruct
    private void setUp() {
        mongoClient = new MongoClient();
        MongoDatabase mongo = mongoClient.getDatabase("hackernews");
        links = mongo.getCollection("links");
    }

    @PreDestroy
    public void shutdown() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public Link findById(String id) {
        Document doc = links.find(eq("_id", new ObjectId(id))).first();
        return link(doc);
    }

    public List<Link> findAll() {
        List<Link> allLinks = new ArrayList<>();
        for (Document doc : links.find()) {
            allLinks.add(link(doc));
        }
        return allLinks;
    }

    public void saveLink(Link link) {
        Document doc = new Document();
        doc.append("url", link.getUrl());
        doc.append("description", link.getDescription());
        doc.append("postedBy", link.getUserId());
        links.insertOne(doc);
    }

    private Link link(Document doc) {
        return new Link(doc.get("_id").toString(), doc.getString("url"), doc.getString("description"),
                doc.getString("postedBy"));
    }

}
