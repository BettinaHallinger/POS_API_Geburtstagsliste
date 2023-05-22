package com.example.accessingmongodbdatarest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.GeneratedValue;

@Document(collection="geburtstage")
@CrossOrigin
public class Person {

	private @Id @GeneratedValue String id;

	private String vorname;
	private String nachname;
	private String geburtstag;
	private String abteilung;
	private int tag;
	private int monat;
	private int jahr;
	private String alter;


	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getVorname() { return vorname; }

	public void setVorname(String vorname) { this.vorname = vorname; }

	public String getNachname() { return nachname; }

	public void setNachname(String nachname) { this.nachname = nachname; }

	public String getGeburtstag() { return geburtstag; }

	public void setGeburtstag(String geburtstag) { this.geburtstag = geburtstag; }

	public String getAbteilung() { return abteilung; }

	public void setAbteilung(String abteilung) { this.abteilung = abteilung; }

	public int getTag() { return tag; }

	public void setTag(int tag) { this.tag = tag; }

	public int getMonat() { return monat; }

	public void setMonat(int monat) { this.monat = monat; }

	public int getJahr() { return jahr; }

	public void setJahr(int jahr) { this.jahr = jahr; }

	public String getAlter() { return alter; }

	public void setAlter(String alter) { this.alter = alter; }

	//private String firstName;
	//private String lastName;

	/*public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}*/
}
