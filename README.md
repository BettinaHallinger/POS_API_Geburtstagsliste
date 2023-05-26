# POS Semesterprojekt API - Geburtstagsliste

   Dieses Projekt bietet eine Geburtstagsliste, in welcher man diverse
    Geburtstage eintragen kann. Zusätzlich kann man die Personen nach Namen bzw. Geburtstag filtern, außerdem wird das jeweilige Alter der Personenangezeigt.
   Das Projekt ist folgendermaßen aufgebaut: Die Daten liegen in einer
    MongoDB Atlas-Datenbank gespeichert, auf welche über eine Spring REST-API zugegriffen wird. Die Geburtstagsliste verfügt sowohl über eine Web-GUI, die auf dem Broser läuft, als auch über eine WPF-GUI. Für den Zugriff über der Website wird im Backend JavaScript verwendet, welches Daten mittels fetch() von der Datenbank ausliest bzw. speichert. Das Backend der WPF-App ist in C# codiert.
Der genaue Aufbau des Projekts sowie Probleme und Lösungswege werden in der folgenden Dokumentation angeführt.
 
# Diagramme
## Verteilungsdiagramm
![alt Verteilungsdiagramm](https://raw.githubusercontent.com/BettinaHallinger/POS_API_Geburtstagsliste/main/images/diagramm1.png?token=GHSAT0AAAAAACBJUE6QKYCYRZ2TH4ILJ6KMZDQJFGA)
## Klassendiagramm Spring REST-Server
![alt Klassendiagramm](https://github.com/BettinaHallinger/POS_API_Geburtstagsliste/blob/main/images/klassendiagramm.png?raw=true)
# Spring REST API
Das Backend besteht aus einem Spring REST-Server, welcher die Verbindung zur MongoDB Atlas Datenbank herstellt. 
Das Projekt besteht aus folgelnden Dateien:
![Projekt-Dateistruktur](https://github.com/BettinaHallinger/POS_API_Geburtstagsliste/blob/main/images/ordnerstruktur.png?raw=true)
Die HTML-Page ist direkt in das Spring REST-Projekt integriert. Sie kann nach Starten des Servers mit "localhost:3001" im Browser aufgerufen werden.

## Verbindung zur Datenbank
### MongoDB Atlas
Zuerst muss eine neue Collection "geburtstage" angelegegt werden. Anschließend wird ein neue Benutzer "user" erstellt, der die Berechtigungen read und write zugeteilt bekommt. Dies ist erforderlich, damit vom REST-Server aus auf die Datenbank zugegriffen werden darf. Im nächsten Schritt erfolgt der Zugriff über den REST-Server.

### application.properties
Hier wird die Verbindung zur Datenbank hergestellt. Der Port wird auf 3001 gesetzt und es wird der Link zur MongoDB Atlas-Datenbank inkl. Benutzer angegeben. 
```java
server.port=3001  
spring.data.mongodb.uri=mongodb+srv://user:IDszkWfqMmPNYihj@cluster0.gsuywxw.mongodb.net/geburtstage?retryWrites=true&w=majority
```

### pom.xml
Das Spring REST-Projekt verfügt über folgenden Dependencies:
```xml
<dependencies>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-thymeleaf</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-devtools</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-actuator</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-data-mongodb</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-web</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>org.projectlombok</groupId>  
      <artifactId>lombok</artifactId>  
      <optional>true</optional>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-test</artifactId>  
      <scope>test</scope>  
   </dependency>  
   <dependency>  
      <groupId>org.springframework.boot</groupId>  
      <artifactId>spring-boot-starter-data-rest</artifactId>  
   </dependency>  
   <dependency>  
      <groupId>jakarta.persistence</groupId>  
      <artifactId>jakarta.persistence-api</artifactId>  
      <version>2.2.3</version>  
   </dependency>  
</dependencies>
```

### Person.java
In der Datatenbank sind Instanzen der Klasse Person gespeichert. Diese enthält folgende Variablen inkl. Getter und Setter:
```java
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
```
Die Variable geburtstag ist hier nicht zwingend notwendig, dient aber zur einfacheren und schöneren Ausgabe des Geburtsdatums  (zusammengesetzt aus tag, monat und jahr).

Da die Web-GUI direkt im Spring REST-Projekt integriert ist, müssen folgende Änderungen in der Controller-Klasse vorgenommen werden: @RestController wird durch @Controller ersetzt und alle Endpoints müssen mit @ResponseBody erweitert werden.

### PersonController.java
```java
@Controller  
public class PersonController {  
  
    @Autowired  
  private PersonService personService;  
    private final PersonRepository repository;  
    PersonController(PersonRepository repository) {this.repository = repository;}
```
**Aufrufen der Web-GUI**

Beim Aufruf von "localhost:3001" im Browser wird die Seite "index.html" geladen.
```java
@RequestMapping("/")  
public String welcome() {  
    return "index";  
}
```
**Eintrag in die Datenbank hinzufügen:**

Hier wird eine neue Insanz der Klasse Person erstellt und zur Datenbank hinzugefügt. Tag, Monat und Jahr des Geburtstages werden hier als Integer-Werte übergeben. Nun wird direkt in der Funktion der String geburtstag aus den drei Integer zusammengesetzt (zur einfacheren und schöneren Ausgabe). Außerdem wird das Datum als LocalDate geparsed, um direkt das Alter der hinzugefügten Person zu berechnen. 
```java
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
```
**Alle Einträge aus der Datenbank abfragen:**

Hier werden alle Datenbankeinträge als JSON-Objekte zurückgegeben.
```java
@GetMapping(path="/geburtstage")  
public @ResponseBody Iterable<Person> getAll() {  
  return repository.findAll();  
}}
```
**Suche nach beliebigen Parametern:**

Hier kann man Datenbankeinträge nach beliebigen Parametern filtern. Die Schwierigkeit hierbei war, dass man keine "findBy...()" Funktionen verwenden kann, wo nach den mitgegebenen Parameter gesucht wird, da man bei diesen nur nach einem Parameter suchen kann. Bei diesem Projekt würde die Suche nach einem einzelnen Parameter wenig Sinn machen, da man sich z.B. alle Personen anzeigen lassen will, die in einem bestimmten Monat in einem bestimmten Jahr Geburtstag haben. Deshalb war es notwendig, ein Query zu erstellen, bei welcher alle Parameter optional mitgegeben werden können. Dieser Teil des Projekts zwar sehr zeitaufwändig. Die Funktion find() ist mit sämtlichen anderen Klassen verbunden, die später noch erklärt werden.
```java
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
```
**Objekt nach ID suchen**

Ein Objekt kann nach der ID gesucht werden, dies ist für die Update-Funktion notwendig (siehe JavaScript - Daten bearbeiten).
```java
@GetMapping("/id/{id}")  
@ResponseBody  
Optional<Person> one(@PathVariable String id) {  
    return repository.findById(id);  
}
```
**Eintrag löschen:**

Ein Einträg kann gelöscht werden, indem die ID des Objekts mitgegeben wird. 
```java
@DeleteMapping("/remove/{id}")  
@ResponseBody
void deletePerson(@PathVariable String id) {  
    repository.deleteById(id);  
}
```

### PersonRepository.java (Interface)
In dieser Klasse befindet sich lediglich das zuvor erwähnte Query, das nach den optionalen Parametern in der Datenbank sucht.
Die Query konvertiert alle Integer Werte für Tag, Monat und Jahr zu Strings und sucht anschließend mit einer Regex, welche im @RequestParam angegebn wird, nach diesen Werten.
```java 
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
```
### PersonService.java (Interface)
Die Service-Klasse ist im Normalfall nicht zwingend notwendig, doch aufgrund der find()-Methode mit dem Query ist sie schon notwendig.
```java
public interface PersonService {  
  
    List<Person> find(Optional<String> vorname, Optional<String> nachname, Optional<Integer> tag, Optional<Integer> monat, Optional<Integer> jahr, Optional<Integer> alter);  
}
```

### PersonServiceImpl.java
Diese Klasse implementiert das Interface PersonService und überschreibt die Methode find(). Hier werden die optionalen Parameter übergeben und es wird überprüft, ob die jweiligen Felder belegt sind, ansonsten wird ein leerer String übergeben.
Des nimmt oafoch de Optional-Werte (de a leer sei kennan) und konvertierts fois gsetzt sand vo int zu string und fois leer sand setztes aus leeren string damit de regex quasi olle matched (weil "" aus regex is imma true)

```java
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
```

# GUI mit HTML/CSS und JavaScript
![](https://github.com/BettinaHallinger/POS_API_Geburtstagsliste/blob/main/images/frontend.png?raw=true)
Das Graphical User Interface der API besteht aus einer HTML-Website, die mit CSS gestylt wird. Auf die Daten wird mit JavaScript durch fetch() auf dem REST-Server zugegriffen. 

## HTML
Die HTML-Page besteht aus einem MainContainer, der wiederum zwei Container enthält. Der erste Container beinhaltet die Tabelle, die mit JavaScript mit Daten befüllt wird.
Der zweite Container beinhaltet einen Container mit Feldern zum Hinzufügen von Daten und einen weiteren Container mit Feldern zum Suchen von Daten.
```html 
<div  class="mainContainer">

	<div class="container1">
		<h1>Geburtstagsliste</h1>
		<div id="tbl"></div>
	</div>
	
	<div  class="container2">
		<div  class="addPerson">
			<div  class="buttons">
				<h3>Geburtstag suchen</h3>
			</div>
			<label  class="lbl">Vorname:</label>
			<input  type="text"  name="vorname"  class="big"  id="vorname"  placeholder="Vorname">
			<label  class="lbl">Nachname:</label>
			<input  type="text"  name="nachname"  class="big"  id="nachname"  placeholder="Nachname">
			<label  class="lbl">Geburtstag:</label>
			<div  class="num">
				<input  type="text"  name="tag"  id="tag"  placeholder="Tag"  class="numInput">
				<input  type="text"  name="monat"  id="monat"  placeholder="Monat"  class="numInput">
				<input  type="text"  name="jahr"  id="jahr"  placeholder="Jahr"  class="numInput">
			</div>
			<div  class="buttons">
				<button  class="b1"  id="addP"  type="button"  onclick="addPerson()"><b>Hinzufügen</b></button>
			</div>
		</div>
		<div  class="addPerson">
			<div  class="buttons">
				<h3>Geburtstag suchen</h3>
			</div>
			<label  class="lbl">Vorname:</label>
			<input  type="text"  name="vorname"  class="big"  id="findVorname"  placeholder="Vorname">
			<label  class="lbl">Nachname:</label>
			<input  type="text"  name="nachname"  class="big"  id="findNachname"  placeholder="Nachname">
			<label  class="lbl">Geburtstag:</label>
			<div  class="num">
				<input  type="text"  name="tag"  id="findTag"  placeholder="Tag"  class="numInput">
				<input  type="text"  name="monat"  id="findMonat"  placeholder="Monat"  class="numInput">
				<input  type="text"  name="jahr"  id="findJahr"  placeholder="Jahr"  class="numInput">
			</div>
			<label  class="lbl">Alter:</label>
			<input  type="text"  name="alter"  class="big"  id="findAlter"  placeholder="25">
			<div  class="buttons">
				<button  class="b1"  id="addP"  type="button"  onclick="find()"><b>Suchen</b></button>
				<button  class="b1"  id="addP"  type="button"  onclick="load()"><b>Reset</b></button>
			</div>
		</div>
	</div>
</div>
```

## CSS
MainContainer, container1 und container2 sind Flex-Container, in denen ihre Elemente zentriert angeordnet sind. AddPerson ist jeweils ein Grid, wo die Suchfelder und Buttons angeordnet sind.
Die Tabelle ist scrollbar, d.h. wenn die Liste länger ist, scrollt man nicht die gesamte Seite, sondern nur die Tabelle, der Rest ist statisch.

## JavaScript
JavaScript dient zur Kommunikation zwischen der HTML-Seite und dem REST-Server, auf welchem sich die Daten befinden. 
Als erstes wird die load()-Funktion aufgerufen, um alle Daten 

**Daten laden**

Die Funktion load() wird gleich beim Laden der Seite aufgerufen. Sie holt mittels fetch() über die URL alle verfügbaren Daten vom Spring REST-Server. Die Daten werden als JSON-Objekte übergeben, deshalb müssen diese noch in einen String umgewandelt werden.
```javascript 
function  load() {
	fetch('http://localhost:3001/geburtstage', {
	method:  'GET',
	headers: {
		'Accept':  'application/json',
	},
	})
	.then((response) =>  response.json())
	.then((response) => {
		var  birthdays = JSON.parse(JSON.stringify(response));
		appendData(birthdays);
	});
}
``` 

**Daten zur Datenbank hinzufügen**

Die Funktion addPerson() wird aufgerufen, wenn der Button "Hinzufügen" geklickt wird. Bevor ein neuer Datensatz erstellt wird, werden die einzelnen Felder auf ihre Gültigkeit überprüft (kein Feld darf leer sein, Geburtstdatum muss gültig sein). Anschließend wird ein POST-Request mit den eingegebenen Werten an den Server geschickt und die Daten werden in der Datenbank hinzugefügt. 
```javascript
function  addPerson() {
	// Überprüfen, ob Daten gültig sind; wenn ja:
	if (ok) {
		async  function  postData(url = "", data = {}) {
			const  response = await  fetch(url, {
			method:  "POST", 
			mode:  "cors", 
			cache:  "no-cache", 
			credentials:  "same-origin", 
			Allow:  "POST",
			headers: {
				"Content-Type":  "application/json",
		},
		redirect:  "follow", 
		referrerPolicy:  "no-referrer", 
		body:  JSON.stringify(data), 
	});
	return  response.json(); 
	}
	postData("http://localhost:3001/add", {
		vorname:  vorname,
		nachname:  nachname,
		tag:  tag,
		monat:  monat,
		jahr:  jahr
	}).then((data) => {});
	}
}
```


**Daten zur Tabelle in HTML hinzufügen**

Mit der Funktion appendData() werden die Objekte nun zur Tabelle im HTML-Code hinzugefügt. Dazu wrid für jedes Objekt eine eigene Zeile und für jedes Attribut eines Obejektes eine eigene Spalte erstellt. Diese Funktion wird immer aufgerufen, wenn neue Daten hinzugefügt bzw. gelöscht oder gefiltert werden.
```javascript
function  appendData(data) {
	var  mainContainer = document.getElementById("tbl");
	let  text = "<table><tr><th>Vorname</th><th>Nachname</th><th>Geburtstag</th><th>Alter</th></tr>";
	for (let  i  in  data) {
	text += "<tr><td>" + data[i].vorname + "</td>";
	text += "<td>" + data[i].nachname + "</td>";
	text += "<td>" + data[i].geburtstag + "</td>";
	text += "<td>" + data[i].alter + "</td>";
	text += "<td><button class=\"bttnrm\" onclick=\"deletePerson('" + data[i].id + "')\"><img class=\"imgrm\" src=\"delete.svg\"></button></td></tr>";
	}
	text += "</table>";
	document.getElementById("tbl").innerHTML = text;
}
```

**Daten löschen**

Die Funktion deletePerson() wird aufgerufen, wenn man auf den Löschen-Button neben dem Datensatz klickt. Hierbei wird die ID des Objekts übergeben. 
```javascript
function  deletePerson(id) {
	fetch('http://localhost:3001/remove/' + id, {
		method:  'DELETE',
	})
	.then(res  =>  res.text()) 
	.then(res  =>  console.log(res));
	load();
}
```

**Daten bearbeiten**

Mit einem PUT-Request, bei dem die ID des zu bearbeitenden Objekts übergeben wird, kann ein Datensatz bearbeitet werden. Wird auf den "Edit" Button neben dem gewünschten Objekt in der Tabelle geklickt, wird zuerst dieses Objekt aus der Datenbank geladen, um dessen Feldwerte zu lesen. Die Feldwerte werden anschließend in die fürs Hinzufügen vorgesehenen Felder eingefügt und der "Hinzufügen" Button ändert seine Funktion zu einem "Ändern" Button, der dann den PUT-Request an den Server schickt. Nach dieser Transaktion hat der Button wieder seine ursprüngliche Funktion "Hinzufügen".
```javascript
function  updatePerson(id) {
	let  vorname  =  document.getElementById("vorname").value;
	let  nachname  =  document.getElementById("nachname").value;
	let  tag  =  document.getElementById("tag").value;
	let  monat  =  document.getElementById("monat").value;
	let  jahr  =  document.getElementById("jahr").value;

	async  function  put(url  =  "", data  = {}) {
		const  response  =  await  fetch(url, {
		method:  "PUT",
		mode:  "cors",
		cache:  "no-cache",
		credentials:  "same-origin",
		Allow:  "PUT",
		headers: {
			"Content-Type":  "application/json",
	},
		redirect:  "follow",
		referrerPolicy:  "no-referrer",
		body:  JSON.stringify(data),
	});
		return  response.json();
	}

	put("http://localhost:3001/update/"  +  id, {
		vorname:  vorname,
		nachname:  nachname,
		tag:  tag,
		monat:  monat,
		jahr:  jahr,
		}).then((data) => {
		console.log(data);
	});

	clearTextInput1();
	document.getElementById("btnChange").innerHTML  =  "";
	document.getElementById("btnChange").innerHTML  ='<button class="b1" id="addP" type="button" onclick="addPerson()"><b>Hinzufügen</b></button>';
	setTimeout(20000);
	load();
}
```

**Daten filtern**

Damit die Daten nach beliebigen Feldern gefiltert werden können, muss zuerst die richtige URL zusammengesetzt werden. Hierfür wird jedes Feld überprüft, ob es nicht leer ist. Wenn das der Fall ist, wird &feldname=feldwert hinzugefügt, bzw. wenn das Feld der erste übergebene Parameter ist, wird ?feldname=feldwert hinzugefügt. Dies wird bei allen Feldern geprüft und anschließend erhält man die URL, die dann mit einem GET-Request an den Server geschickt wird und die gefilterten Daten zurückgibt. 
```javascript
function  find() {
var  month = document.getElementById("findMonat").value;
var  day = document.getElementById("findTag").value;
var  year = document.getElementById("findJahr").value;
var  firstname = document.getElementById("findVorname").value;
var  lastname = document.getElementById("findNachname").value;
var  age = document.getElementById("findAlter").value;

var  url = "http://localhost:3001/find";
var  isFirst = true;
if (firstname != "") {
	if (isFirst) {
		url += "?vorname=" + firstname;
		isFirst = false;
	}
	else  url += "&vorname=" + firstname;
}

if (lastname != "") {
	if (isFirst) {
		url += "?nachname=" + lastname;
		isFirst = false;
	}
	else  url += "&nachname=" + lastname;
}
// Dieser Code wird auch für alle restlichen Felder angewendet (Tag, Monat, Jahr, Alter)
  
fetch((url), {
	method:  'GET',
	headers: {
	'Accept':  'application/json',
},
})
.then((response) =>  response.json())
.then((response) => {
	var  birthdays = JSON.parse(JSON.stringify(response));
	appendData(birthdays);
});
}
```
# WPF-Client mit C#
Als zweiter Client wurde eine WPF-App mit C# implementiert. Diese bietet dieselben Funktionen wie die HTML-Seite (Daten anzeigen, hinzufügen, löschen und filtern). Es gibt eine Main-Datei und eine Klasse Person. Die Instanzen dieser Klasse werden in der Tabelle angezeigt.

Das MainWindow besteht aus einer GridView mit zwei Zeilen und drei Spalten. Zum Anzeigen der Daten dient eine ListView. 
![](https://github.com/BettinaHallinger/POS_API_Geburtstagsliste/blob/main/images/wpf-frontend.png?raw=true)

**Daten laden**

Hier werden die Daten mit einem GET-Request vom Server geholt, direkt in ein Objekt der Klasse Person geparsed und zur ListView hinzugefügt. 
```csharp
 private async void load()
	 {
	     HttpClient client = new HttpClient();
	     string data = client.GetStringAsync("http://localhost:3001/geburtstage").Result;
         var list = JsonSerializer.Deserialize<List<Person>>(data);
         appendData(list);
     }
```

**Daten in die ListView einfügen**

Gleich wie bei JavaSript gibt es hier die Funkiton appendData(), die die Daten von einer Liste von der Klasse Person in die ListView schreibt. 
```csharp
private void appendData(dynamic list)
        {
            List<Person> items = new List<Person>();
            if (list != null)
            {
                foreach (Person people in list)
                {
                    var date = new DateTime();
                    Button btnRemove = new Button();
                    Person p = new Person() { vorname = people.vorname, nachname = people.nachname, geburtstag = people.geburtstag, alter = people.alter, id = people.id };
                    items.Add(p);
                    try
                    {
                        string dateInput = people.geburtstag;
                        date = DateTime.Parse(dateInput);
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine(ex.Message);
                    }
                }
            }
            lvUsers.ItemsSource = items;
        }
```

**Daten zur Datenbank hinzufügen**

Hierzu werden ähnlich wie bei JavaScript die Feldwerte entnommen und anschließend mit einem POST-Request an den Server geschickt. Bei der Eingabe wird zuvor noch geprüft, ob alle Felder ausgefüllt sind und ob das Datum gültig ist. Ist dies nicht der Fall, wird eine Fehlermeldung ausgegeben und die Funktion wird abgebrochen.
```csharp
 private async void btnAdd_Click(object sender, RoutedEventArgs e)
        {
            if(string.IsNullOrEmpty(txtVorname.Text) || string.IsNullOrEmpty(txtNachname.Text) || string.IsNullOrEmpty(txtTag.Text) || string.IsNullOrEmpty(txtMonat.Text) || string.IsNullOrEmpty(txtJahr.Text))
            {
                MessageBox.Show("Bitte füllen Sie alle Felder aus!");
                return;
            }

            if (!checkDate(txtTag.Text, txtMonat.Text, txtJahr.Text))
            {
                ClearInput();
                return;
            }
            else
            {
                var httpWebRequest = (HttpWebRequest)WebRequest.Create("http://localhost:3001/add");
                httpWebRequest.ContentType = "application/json";
                httpWebRequest.Method = "POST";

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {
                    string json = JsonSerializer.Serialize(new
                    {
                        vorname = txtVorname.Text,
                        nachname = txtNachname.Text,
                        tag = txtTag.Text,
                        monat = txtMonat.Text,
                        jahr = txtJahr.Text
                    });

                    streamWriter.Write(json);
                }
                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
                {
                    var result = streamReader.ReadToEnd();
                }
                ClearInput();
                load();
            } 
        }
```


**Daten löschen**

```csharp 
private async void btnRemove_Click(object sender, RoutedEventArgs e)
        {
            try
            {
                HttpClient httpClient = new HttpClient();

                Person s = (Person)lvUsers.SelectedItems[0];
                string id = s.id;
                string url = "http://localhost:3001/remove/" + id;
                HttpResponseMessage response = await httpClient.DeleteAsync(url);
                httpClient.Dispose();
                load();
            } catch(Exception ex)
            {
                MessageBox.Show("Bitte wählen Sie ein Element aus, das sie löschen wollen!");
            }
        }
```

**Button "Löschen" enablen wenn ein Listenelement ausgewählt ist**

Da beim Löschen (gleich wie beim Bearbeiten) eines Objekts die ID übergeben wird, muss sichergestellt werden, dass immer ein Objekt in der Liste ausgewählt ist und somit dessen ID bekannt ist. Ist das nicht der Fall, kann kein Objekt gelöscht werden und der Button wird auf disable gesetzt.
```csharp
private void lvUsers_Click(object sender, RoutedEventArgs e)
        {
            var item = (sender as ListView).SelectedItem;
            if (item != null)
            { 
                btnRemove.IsEnabled = true;
                btnChange.IsEnabled = true;
                Person s = (Person)lvUsers.SelectedItems[0];
            }
        }

private void lvUsers_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            var item = (sender as ListView).SelectedItem;
            if (item == null)
            {
		            btnChange.isEnabled = false;
                btnRemove.IsEnabled = false;
            }
        }
```

**Daten bearbeiten**

Mit einem PUT-Request an den Server, bei dem die ID übergeben wird, wird kann ein Datensatz verändert werden. Gleich wie bei JavaScript dienen die zum Hinzufügen vorgesehenen Felder zum Bearbeiten der Werte. Wird ein Element in der Liste ausgewählt und der "Bearbeiten" Button geklickt, werden die Daten des Objekts unten in die Input-Felder eingefügt und der Button "Änderungen speichern" wird auf enable gesetzt. Wird dieser dann geklickt, wird  die Funktion btnUpdate_Click aufgerufen und der PUT-Request mit den neuen Daten an den Server geschickt.
```csharp
private void btnUpdate_Click(object sender, RoutedEventArgs e)
        {
			Person p = (Person)lvUsers.SelectedItems[0];
            string data = client.GetStringAsync("http://localhost:3001/id/" + p.id).Result;
            Person person = JsonSerializer.Deserialize<Person>(data);

            if (!checkDate(txtTag.Text, txtMonat.Text, txtJahr.Text))
            {
                ClearInput();
                return;
            }
            else
            {

                var httpWebRequest = (HttpWebRequest)WebRequest.Create("http://localhost:3001/update/" + p.id);
                httpWebRequest.ContentType = "application/json";
                httpWebRequest.Method = "PUT";

                using (var streamWriter = new StreamWriter(httpWebRequest.GetRequestStream()))
                {
                    string json = JsonSerializer.Serialize(new
                    {
                        vorname = txtVorname.Text,
                        nachname = txtNachname.Text,
                        tag = txtTag.Text,
                        monat = txtMonat.Text,
                        jahr = txtJahr.Text
                    });
                    streamWriter.Write(json);
                }
                var httpResponse = (HttpWebResponse)httpWebRequest.GetResponse();
                using (var streamReader = new StreamReader(httpResponse.GetResponseStream()))
                {
                    var result = streamReader.ReadToEnd();
                }
                load();
            }
        }
```
 **Daten filtern**
 
Diese Methode funktioniert gleich wie in JavaScript - alle Felder werden überprüft und wenn sie nicht leer sind, werden sie zur URL hinzugefügt und anschließend mit einem GET-Request vom Server geladen.
```csharp
private void btnFind_Click(object sender, RoutedEventArgs e)
        {
            string url = "http://localhost:3001/find";
            
// Hier wird wieder die URL zusammengesetzt.

            HttpClient client = new HttpClient();
            string data = client.GetStringAsync(url).Result;
            var list = JsonSerializer.Deserialize<List<Person>>(data);
            appendData(list);
        }
```

### Klasse Person.cs
Alle Datensätze werden bei jedem GET-Request in ein Person-Objekt geparst und in eine Liste gespeichert. Die Klasse Person enthält einen Default-Konstruktur und folgende Attribute:
```csharp
public class Person
    {
        public string vorname { get; set; }
        public string nachname { get; set; }
        public string geburtstag { get; set; }
        public int tag { get; set; }
        public int monat { get; set; }
        public int jahr { get; set; }
        public int alter { get; set; }
        public string id { get; set; }
        public Person()
        {   
        }
    }
```
