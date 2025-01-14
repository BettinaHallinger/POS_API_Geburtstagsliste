package com.example.accessingmongodbdatarest;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;

@Document(collection="geburtstage")
@CrossOrigin
public class Person {
	private @Id @GeneratedValue String id;
	private String vorname;
	private String nachname;
	private String geburtstag;
	private int tag;
	private int monat;
	private int jahr;
	private int alter;


	public String getId() { return id; }

	public void setId(String id) { this.id = id; }

	public String getVorname() { return vorname; }

	public  void setVorname(String vorname) { this.vorname = vorname; }

	public String getNachname() { return nachname; }

	public void setNachname(String nachname) { this.nachname = nachname; }

	public String getGeburtstag() { return geburtstag; }

	public void setGeburtstag(String geburtstag) { this.geburtstag = geburtstag; }

	public int getTag() { return tag; }

	public void setTag(int tag) { this.tag = tag; }

	public int getMonat() { return monat; }

	public void setMonat(int monat) { this.monat = monat; }

	public int getJahr() { return jahr; }

	public void setJahr(int jahr) { this.jahr = jahr; }

	public int getAlter() { return alter; }

	public void setAlter(int alter) { this.alter = alter; }
}
