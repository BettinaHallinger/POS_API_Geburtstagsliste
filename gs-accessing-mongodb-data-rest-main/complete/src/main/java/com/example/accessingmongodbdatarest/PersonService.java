package com.example.accessingmongodbdatarest;

//import com.Person.information.system.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> find(Optional<String> vorname, Optional<String> nachname, Optional<Integer> tag, Optional<Integer> monat, Optional<Integer> jahr, Optional<Integer> alter);

}