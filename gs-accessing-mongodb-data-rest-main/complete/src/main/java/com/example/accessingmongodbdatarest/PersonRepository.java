
package com.example.accessingmongodbdatarest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "geburtstage", path = "geburtstage")
public interface PersonRepository extends MongoRepository<Person, String> {

	Optional<List<Person>>findByVorname(@Param("vorname") String vorname);

	Optional<List<Person>>findByNachname(@Param("nachname") String nachname);

	Optional<List<Person>>findByTag(@Param("nachname") int tag);

	Optional<List<Person>>findByMonat(@Param("nachname") int monat);

	Optional<List<Person>>findByJahr(@Param("nachname") int jahr);

	Optional<List<Person>>findByGeburtstag(@Param("geburtstag") String geburtstag);

	Optional<List<Person>>findByAbteilung(@Param("abteilung") String abteilung);

	Optional<Person>findById(@Param("id") String id);

}
