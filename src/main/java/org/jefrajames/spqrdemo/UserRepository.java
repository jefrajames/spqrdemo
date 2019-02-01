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

import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

/**
 * MongoDB repository for User entity.
 * 
 * @author JF James
 */

@ApplicationScoped
public class UserRepository {

	private MongoCollection<Document> users;
	private MongoClient mongoClient;

	@PostConstruct
	private void setUp() {
		mongoClient = new MongoClient();
		MongoDatabase mongo = mongoClient.getDatabase("hackernews");
		users = mongo.getCollection("users");
	}

	@PreDestroy
	public void shutdown() {
		if (mongoClient != null)
			mongoClient.close();
	}

	public User findByEmail(String email) {
		Document doc = users.find(eq("email", email)).first();
		return user(doc);
	}

	public User findById(String id) {
		System.out.println("Calling UserRepository#findById, id=" + id);
		try {
			Document doc = users.find(eq("_id", new ObjectId(id))).first();
			System.out.println("Mongo doc=" + doc);
			return user(doc);
		} catch (IllegalArgumentException e) {
			// Authorization HTTP header may not be a proper Document id
			return null;
		}

	}
	
	public List<User> findAll() {
		List<User> allUsers = new ArrayList<>();
		for (Document doc : users.find()) {
			allUsers.add(user(doc));
		}
		return allUsers;
	}

	public User saveUser(User user) {
		Document doc = new Document();
		doc.append("name", user.getName());
		doc.append("email", user.getEmail());
		doc.append("password", user.getPassword());
		users.insertOne(doc);
		return new User(doc.get("_id").toString(), user.getName(), user.getEmail(), user.getPassword());
	}

	private User user(Document doc) {
		if (doc == null) {
			return null;
		}
		return new User(doc.get("_id").toString(), doc.getString("name"), doc.getString("email"),
				doc.getString("password"));
	}
	


}