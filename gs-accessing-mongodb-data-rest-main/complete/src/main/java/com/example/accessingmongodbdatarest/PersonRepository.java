
package com.example.accessingmongodbdatarest;

import java.util.List;
import java.util.Optional;

import com.jayway.jsonpath.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.criteria.CriteriaBuilder;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "geburtstage", path = "geburtstage")
public interface PersonRepository extends MongoRepository<Person, String> {

	@Query("{ vorname: { $regex: ?0 }, $expr: { $and: [" +
			"{ $regexMatch: { input: { $toString :$nachname }, regex: ?1 } }," +
			"{ $regexMatch: { input: { $toString :$tag }, regex: ?2 } }," +
			"{ $regexMatch: { input: { $toString: $monat }, regex: ?3 } }," +
			"{ $regexMatch: { input: { $toString :$jahr }, regex: ?4 } }," +
			"{ $regexMatch: { input: { $toString: $alter }, regex: ?5 } } ] } }")
	List<Person> find(String vorname, String nachname, String tag, String monat, String jahr, String alter);

}

