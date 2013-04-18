## Inhaltsverzeichnis
* [Idee](#Idee)

## <a id="Idee"/>Idee
Ein Ticketingsystem in einer abgegrenzten Produktivumgebung. Dieses soll einem bestimmten Benutzerkreis die Möglichkeit geben it-technische Supportanfragen an deren jeweiliger Betreuer(Supporter) zu übermitteln(synchron).

Die Supporter bekommen einen Alert in ihrem Frontend über neu eingegangene Anfragen, und können direkt mit dem Benutzer anhand der Kontaktdaten in Kontakt treten(synchron).

Darüberhinaus kann der Supporter eine Lösung verfassen, diese dann an den Benutzer übermittelt wird(synchron). 

Sind die Lösungen fuer eine Archivirung relevant werden diese in die KB übergeben.und koenne nach Stichworten abgerufen werden(optional(asynchron))

Supportanfragen die nicht vom jeweiligen Support-Benutzer bewältigt werden können werden über Schwerpunkt-Tags je nach Wissenschwerpunkt, an andere Supportbenutzer weitergegeben.(asynchron) 

darüberhinaus "eskaliert" eine supportanfrage wenn sie eine bestimmte Zeit überschritten hat und so als errinerungsfunktion fungiert(Synchron) die Anfrage wird nach dem Zeitpunkt an alle anderen SUpporter weitergeleitet(asynchron).
