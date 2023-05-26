package com.example.accessingmongodbdatarest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
public class PersonController {

    @Autowired
    private PersonService personService;
    private final PersonRepository repository;
    PersonController(PersonRepository repository) {this.repository = repository;}

    private String calculateBirtday(int d, int m, int y){
        try {
            String s = (d + "." + m + "." + y);
            if (m < 10) {
                s = (d + ".0" + m + "." + y);
            }
            if (d < 10) {
                s = "0" + s;
            }
            return s;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return "";
    }

    private int calculateAge(int d, int m, int y){
        try {
            String toParse = y + "-" + m + "-" + d;
            if (m < 10) {
                toParse = y + "-0" + m + "-" + d;
            }
            if (d < 10) {
                toParse = toParse.substring(0, toParse.length() - 1);
                toParse += "0" + d;
            }
            // Calculate age
            LocalDate now = LocalDate.now();
            LocalDate then = LocalDate.parse(toParse);
            Period period = then.until(now);
            int yearsBetween = period.getYears();
            return yearsBetween;
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return 0;
    }

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }


    @PostMapping("/add")
    @ResponseBody
    Person newPerson(@RequestBody Person newPerson) {
        try{
            newPerson.setGeburtstag(calculateBirtday(newPerson.getTag(), newPerson.getMonat(), newPerson.getJahr()));
            newPerson.setAlter(calculateAge(newPerson.getTag(), newPerson.getMonat(), newPerson.getJahr()));

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return repository.save(newPerson);
    }

    @GetMapping(path="/geburtstage")
    public @ResponseBody Iterable<Person> getAll() {
        return repository.findAll();
    }

    @GetMapping("/id/{id}")
    @ResponseBody
    Optional<Person> one(@PathVariable String id) {
        return repository.findById(id);
    }

    @RequestMapping("/find")
    @ResponseBody
    public List<Person> find(@RequestParam("vorname") Optional<String> vorname,
                                        @RequestParam("nachname") Optional<String> nachname,
                                        @RequestParam("tag") Optional<Integer> tag,
                                        @RequestParam("monat") Optional<Integer> monat,
                                        @RequestParam("jahr") Optional<Integer> jahr,
                                        @RequestParam("alter") Optional<Integer> alter) {
        return personService.find(vorname, nachname, tag, monat, jahr, alter);
    }

    @PutMapping("/update/{id}")
    @ResponseBody
    Person update(@RequestBody Person newPerson, @PathVariable String id) {
        System.out.println( "OK");
        return repository.findById(id)
                .map(employee -> {
                    employee.setVorname(newPerson.getVorname());
                    employee.setNachname(newPerson.getNachname());
                    employee.setTag(newPerson.getTag());
                    employee.setMonat(newPerson.getMonat());
                    employee.setJahr(newPerson.getJahr());
                    employee.setGeburtstag(calculateBirtday(newPerson.getTag(), newPerson.getMonat(), newPerson.getJahr()));
                    employee.setAlter(calculateAge(newPerson.getTag(), newPerson.getMonat(), newPerson.getJahr()));

                    return repository.save(employee);
                })
                .orElseGet(() -> {
                    newPerson.setId(id);
                    return repository.save(newPerson);
                });
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    void deletePerson(@PathVariable String id) {
        repository.deleteById(id);
    }

}

