package org.springframework.data.mongodb.core.mapping.event;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {
	@Id
	private ObjectId id;
	private String city;

	public Address(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Address address = (Address) o;

		if (!city.equals(address.city)) return false;
		if (id != null ? !id.equals(address.id) : address.id != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + city.hashCode();
		return result;
	}
}
