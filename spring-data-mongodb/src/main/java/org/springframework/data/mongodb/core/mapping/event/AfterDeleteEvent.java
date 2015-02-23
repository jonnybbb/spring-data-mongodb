<<<<<<< Temporary merge branch 1
package org.springframework.data.mongodb.core.mapping.event;

/**
 * Event invoked after document is deleted with methods:
 * <ul>
 *     <li>{@link org.springframework.data.mongodb.core.MongoOperations#remove(Object)}</li>
 *     <li>{@link org.springframework.data.mongodb.core.MongoOperations#remove(Object, String)}</li>
 * </ul>
 *
 * @param <T> removed object
 *
 * @author Maciej Walkowiak
 */
public class AfterDeleteEvent<T> extends MongoMappingEvent<T> {
	public AfterDeleteEvent(T source) {
		super(source, null);
=======
/*
 * Copyright 2013 by the original author(s).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mongodb.core.mapping.event;

import com.mongodb.DBObject;

/**
 * Event being thrown after a single or a set of documents has/have been deleted. The {@link DBObject} held in the event
 * will be the query document <em>after</am> it has been mapped onto the domain type handled.
 * 
 * @author Martin Baumgartner
 */
public class AfterDeleteEvent<T> extends AbstractDeleteEvent<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new {@link AfterDeleteEvent} for the given {@link DBObject} and type.
	 * 
	 * @param dbo must not be {@literal null}.
	 * @param type can be {@literal null}.
	 */
	public AfterDeleteEvent(DBObject dbo, Class<T> type) {
		super(dbo, type);
	}
}
