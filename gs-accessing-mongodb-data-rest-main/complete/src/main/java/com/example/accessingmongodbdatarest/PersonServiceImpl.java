package com.example.accessingmongodbdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public List<Person> find(Optional<String> vorname, Optional<String> nachname, Optional<Integer> tag, Optional<Integer> monat, Optional<Integer> jahr, Optional<Integer> alter) {
        String vornameString = vorname.orElse("");
        String nachnameString = nachname.orElse("");
        String tagString = tag.map(v -> v.toString()).orElse("");
        String monatString = monat.map(v -> v.toString()).orElse("");
        String jahrString = jahr.map(v -> v.toString()).orElse("");
        String alterString = alter.map(v -> v.toString()).orElse("");

        return this.personRepository.find(vornameString, nachnameString, tagString, monatString, jahrString, alterString);
    }
}