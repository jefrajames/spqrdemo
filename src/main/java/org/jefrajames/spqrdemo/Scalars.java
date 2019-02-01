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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

/**
 * 
 * Specific scalar type.
 * 
 * @author JF James
 */
public class Scalars {

	// Definition of a specific dateTime type
	public static GraphQLScalarType dateTime = new GraphQLScalarType("DateTime", "DataTime scalar", new Coercing<Object, Object>() {
		@Override
		public String serialize(Object input) {
			// serialize the ZonedDateTime into string on the way out
			return ((ZonedDateTime) input).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
		}

		@Override
		public Object parseValue(Object input) {
			return serialize(input);
		}

		@Override
		public ZonedDateTime parseLiteral(Object input) {
			// parse the string values coming in
			if (input instanceof StringValue) {
				return ZonedDateTime.parse(((StringValue) input).getValue());
			} else {
				return null;
			}
		}
	});
}
