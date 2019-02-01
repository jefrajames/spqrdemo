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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User domain object (aka Entity).
 * 
 * @author JF James
 */
@Data
@NoArgsConstructor // Required by JNoSQL => no final attributes
@AllArgsConstructor
public class User {

	public User(String name, String email, String password) {
		this(null, name, email, password);
	}


	private String id;
	
	private String name;
	
	private String email;
	
	private String password;

}
