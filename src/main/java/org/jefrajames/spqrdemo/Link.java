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
import org.jnosql.artemis.Column;
import org.jnosql.artemis.Entity;
import org.jnosql.artemis.Id;

/**
 * Link domain object (aka Entity).
 *
 * @author JF James
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity("links")
public class Link {

    @Id("_id")
    private String id;

    @Column
    private String url;

    @Column
    private String description;

    @Column
    private String userId;

    public Link(String url, String description, String userId) {
        this(null, url, description, userId);
    }

}
