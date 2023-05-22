load();

function load() {
  fetch("http://localhost:3001/geburtstage", {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
    .then((response) => response.json())
    .then((response) => {
      var birthdays = JSON.parse(JSON.stringify(response));
      appendData(birthdays);
    });
}

function appendData(data) {
  var mainContainer = document.getElementById("tbl");
  let text =
    "<table><tr><th>Vorname</th><th>Nachname</th><th>Geburtstag</th><th>Alter</th></tr>";
  for (let i in data) {
    text += "<tr><td>" + data[i].vorname + "</td>";
    text += "<td>" + data[i].nachname + "</td>";
    text += "<td>" + data[i].geburtstag + "</td>";
    text += "<td>" + data[i].alter + "</td>";
    text +=
      '<td><button class="bttnrm" onclick="deletePerson(\'' +
      data[i].id +
      '\')"><img class="imgrm" src="delete.svg"></button></td></tr>';
  }
  text += "</table>";
  document.getElementById("tbl").innerHTML = text;
}

function deletePerson(id) {
  fetch("http://localhost:3001/remove/" + id, {
    method: "DELETE",
  })
    .then((res) => res.text())
    .then((res) => console.log(res));
  load();
}

function addPerson() {
  let vorname = document.getElementById("vorname").value;
  let nachname = document.getElementById("nachname").value;
  let tag = document.getElementById("tag").value;
  let monat = document.getElementById("monat").value;
  let jahr = document.getElementById("jahr").value;

  let ok = true;
  var d = new Date();
  let now = d.getFullYear();
  let nowm = d.getMonth() + 1;
  let nowd = d.getDate();

  if (
    tag < 1 ||
    tag > 31 ||
    monat < 1 ||
    monat > 12 ||
    findTag < 1 ||
    findTag > 31 ||
    findMonat < 1 ||
    findMonat > 12
  ) {
    alert("Dieses Datum ist nicht gültig...");
    ok = false;
  } else if (jahr < 1800) {
    alert("Diese Person lebt vermutlich nicht mehr...");
    ok = false;
  } else if (jahr >= now) {
    if (monat > nowm) {
      alert("Diese Person existiert vermutlich noch gar nicht...");
      ok = false;
    } else if (monat == nowm && tag > nowd) {
      alert("Diese Person existiert vermutlich noch gar nicht...");
      ok = false;
    }
  } else if (isNaN(tag)) {
    alert("Keine gültige Eingabe");
    ok = false;
  } else if (isNaN(monat)) {
    alert("Keine gültige Eingabe");
    ok = false;
  } else if (isNaN(jahr)) {
    alert("Keine gültige Eingabe");
    ok = false;
  }

  if (ok) {
    async function postData(url = "", data = {}) {
      const response = await fetch(url, {
        method: "POST",
        mode: "cors",
        cache: "no-cache",
        credentials: "same-origin",
        Allow: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        redirect: "follow",
        referrerPolicy: "no-referrer",
        body: JSON.stringify(data),
      });
      return response.json();
    }

    postData("http://localhost:3001/add", {
      vorname: vorname,
      nachname: nachname,
      tag: tag,
      monat: monat,
      jahr: jahr,
    }).then((data) => {
      console.log(data);
    });
  }
  document.getElementById("vorname").value = "";
  document.getElementById("nachname").value = "";
  document.getElementById("tag").value = "";
  document.getElementById("monat").value = "";
  document.getElementById("jahr").value = "";
  load();
}

function find() {
  var month = document.getElementById("findMonat").value;
  var day = document.getElementById("findTag").value;
  var year = document.getElementById("findJahr").value;
  var firstname = document.getElementById("findVorname").value;
  var lastname = document.getElementById("findNachname").value;
  var age = document.getElementById("findAlter").value;

  var url = "http://localhost:3001/find";
  var isFirst = true;
  console.log("url");

  if (firstname != "") {
    if (isFirst) {
      url += "?vorname=" + firstname;
      isFirst = false;
    } else url += "&vorname=" + firstname;
  }
  if (lastname != "") {
    if (isFirst) {
      url += "?nachname=" + lastname;
      isFirst = false;
    } else url += "&nachname=" + lastname;
  }
  if (day != "") {
    if (isFirst) {
      url += "?tag=" + day;
      isFirst = false;
    } else url += "&tag=" + day;
  }
  if (month != "") {
    if (isFirst) {
      url += "?monat=" + month;
      isFirst = false;
    } else url += "&monat=" + month;
  }
  if (year != "") {
    if (isFirst) {
      url += "?jahr=" + year;
      isFirst = false;
    } else url += "&jahr=" + year;
  }
  if (age != "") {
    if (isFirst) {
      url += "?alter=" + age;
      isFirst = false;
    } else url += "&alter=" + age;
  }

  console.log(url);

  fetch(url, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
    .then((response) => response.json())
    .then((response) => {
      var birthdays = JSON.parse(JSON.stringify(response));
      appendData(birthdays);
    });

  document.getElementById("findVorname").value = "";
  document.getElementById("findNachname").value = "";
  document.getElementById("findTag").value = "";
  document.getElementById("findMonat").value = "";
  document.getElementById("findJahr").value = "";
}
