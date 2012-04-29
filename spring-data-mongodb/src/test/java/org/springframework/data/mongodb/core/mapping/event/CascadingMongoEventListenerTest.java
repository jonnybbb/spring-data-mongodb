package org.springframework.data.mongodb.core.mapping.event;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CascadingMongoEventListenerTest {
	@Autowired
	private MongoOperations mongoOperations;

	@Test
	public void testOnBeforeConvert() throws Exception {
		mongoOperations.dropCollection(CascadeUser.class);
		mongoOperations.dropCollection(Address.class);

		CascadeUser cascadeUser = new CascadeUser("john");
		cascadeUser.setAddress(new Address("London"));

		mongoOperations.save(cascadeUser);
	}

	@Test
	public void testOnAfterDelete() {
		CascadeUser cascadeUser = mongoOperations.findAll(CascadeUser.class).get(0);

		mongoOperations.remove(cascadeUser);

		assertEquals(0, mongoOperations.findAll(Address.class).size());
	}
}
