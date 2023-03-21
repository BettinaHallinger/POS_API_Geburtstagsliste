package com.example.accessingmongodbdatarest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
public class PersonController {

    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @RequestMapping("/home")
    public String serviceTest() { return "Schenes Service!"; }

    @PostMapping("/add")
    Person newEmployee(@RequestBody Person newEmployee) {
        return repository.save(newEmployee);
    }

    @GetMapping(path="/geburtstage")
    public @ResponseBody Iterable<Person> getAllUsers() {
        // This returns a JSON or XML with the users
        return repository.findAll();
    }

    @GetMapping(path="/vorname/{vorname}")
    public ResponseEntity<Optional<List<Person>>> getPersonByVorname(@PathVariable String vorname){
        return new ResponseEntity<Optional<List<Person>>>(repository.findByVorname(vorname), HttpStatus.OK);
    }

    @GetMapping(path="/nachname/{nachname}")
    public ResponseEntity<Optional<List<Person>>> getPersonByNachname(@PathVariable String nachname){
        return new ResponseEntity<Optional<List <Person>>>(repository.findByNachname(nachname), HttpStatus.OK);
    }

    @GetMapping(path="/datum/{geburtstag}")
    public ResponseEntity<Optional<List<Person>>> getPersonByGeburtstag(@PathVariable String geburtstag){
        return new ResponseEntity<Optional<List<Person>>>(repository.findByGeburtstag(geburtstag), HttpStatus.OK);
    }

    @GetMapping(path="/abteilung/{abteilung}")
    public ResponseEntity<Optional<List<Person>>> getPersonByAbteilung(@PathVariable String abteilung){
        return new ResponseEntity<Optional<List<Person>>>(repository.findByAbteilung(abteilung), HttpStatus.OK);
    }

    @PutMapping("/person/{id}")
    public Person replacePerson(@RequestBody Person newEmployee, @PathVariable String id) {

        return repository.findById(id) //
                .map(employee -> {
                    employee.setVorname(newEmployee.getVorname());
                    employee.setNachname(newEmployee.getNachname());
                    employee.setGeburtstag(newEmployee.getGeburtstag());
                    employee.setAbteilung(newEmployee.getAbteilung());
                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setId(id);
                    return repository.save(newEmployee);
                });
    }

    @DeleteMapping("/remove/{id}")
    void deletePerson(@PathVariable String id) {
        repository.deleteById(id);
    }

}

