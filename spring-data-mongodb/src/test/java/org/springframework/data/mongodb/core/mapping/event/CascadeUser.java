package org.springframework.data.mongodb.core.mapping.event;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.CascadeType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CascadeUser {
	@Id
	private ObjectId id;
	private String name;

	@DBRef(cascadeType = CascadeType.ALL)
	private Address address;

	public CascadeUser(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CascadeUser user = (CascadeUser) o;

		if (id != null ? !id.equals(user.id) : user.id != null) return false;
		if (!name.equals(user.name)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + name.hashCode();
		return result;
	}
}
