package com.example.accessingmongodbdatarest;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        try {
            /*System.out.println( newEmployee.getGeburtstag());
            String[] s = newEmployee.getGeburtstag().split("-");
            newEmployee.setJahr(Integer.parseInt(s[0]));
            newEmployee.setMonat(Integer.parseInt(s[1]));
            newEmployee.setTag(Integer.parseInt(s[2]));*/

            //LocalDate d = LocalDate.parse(s[0] + "-" + s[1] + "-" + s[2]);
            int day = newEmployee.getTag();
            int month = newEmployee.getMonat();
            int year = newEmployee.getJahr();

            String geburtstag = year + "-" + month + "-" + day;
            String g = day + "." + month + "." + year;
            if(newEmployee.getMonat()<10){
                geburtstag = year + "-0" + month + "-" + day;
                g = day + ".0" + month + "." + year;
            }
            if(newEmployee.getTag()<10){
                geburtstag = geburtstag.substring(0, geburtstag.length()-1) + "0" + day;
                g = g.substring(1, 1) + "0" + g;
                System.out.println(g);
            }
            newEmployee.setGeburtstag(g);
            //LocalDate d = LocalDate.parse(newEmployee.getJahr() + "-" + newEmployee.getMonat() + "-" + newEmployee.getTag());
            LocalDate d = LocalDate.parse(geburtstag);
            System.out.println( d + " " + newEmployee.getJahr());
            LocalDate now = LocalDate.now();
            long y = ChronoUnit.YEARS.between(d, now);
            //long m = ChronoUnit.MONTHS.between(d, now);
            //long t = ChronoUnit.DAYS.between(d, now);
            //System.out.println(y + " " + m + " " + t);
            int i = Integer.parseInt(String.valueOf(y));
            newEmployee.setAlter(String.valueOf(i));
            /*if((m/12)<y){
                newEmployee.setAlter(i-1);
            }
            else{
                if(newEmployee.getTag()<=now.getDayOfMonth()){
                    newEmployee.setAlter(i);
                }
                else{
                    newEmployee.setAlter(i-1);
                }
            }*/
            System.out.println(newEmployee.getAlter());

        } catch(Exception e){
            System.out.println(e.getMessage());
        }
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

    @GetMapping(path="/tag/{tag}")
    public ResponseEntity<Optional<List<Person>>> getPersonByTag(@PathVariable int tag){
        return new ResponseEntity<Optional<List <Person>>>(repository.findByTag(tag), HttpStatus.OK);
    }

    @GetMapping(path="/monat/{monat}")
    public ResponseEntity<Optional<List<Person>>> getPersonByMonat(@PathVariable int monat){
        return new ResponseEntity<Optional<List <Person>>>(repository.findByMonat(monat), HttpStatus.OK);
    }

    @GetMapping(path="/jahr/{jahr}")
    public ResponseEntity<Optional<List<Person>>> getPersonByJahr(@PathVariable int jahr){
        return new ResponseEntity<Optional<List <Person>>>(repository.findByJahr(jahr), HttpStatus.OK);
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

