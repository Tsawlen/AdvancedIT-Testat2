# Testat Übung Dokumentation
## Aufgabenstellung 
Aufgabe war es einen Server zu erstellen, welcher auf die Kommandos SAVE und GET hört.  
Wenn als Kommando SAVE gewählt wird soll der sich dahinter befindene String unter einem einzigartigen String gespeichert werden.  
Ist das Kommando GET <Key> so soll ein String zurückgegeben werden, so unter dem gegebenen Key einer gefunden wird.  
  
## SAVE Kommando
Um dieses Kommando zu testen werden einige Strings an den Server geschickt. Die Antworten werden hier dokumentiert werden.  
### Beispiel 1: "SAVE Dies ist ein Test"
Das Programm fordert dazu auf eine Eingabe zu tätigen, hier wird "Dies ist ein Test eingegeben". Dies führt zu folgender Ausgabe:
```java
Bitte geben sie ein Kommando ein: 
SAVE Dies ist ein Test
KEY ec69a3ef-43d0-4b02-82fc-65b8622c1629
```
#### Auswerung des 1. Beispiels
Diese Antwort lässt darauf schließen, dass die Nachricht vom Server verarbeitet wurde und die Nachricht unter dem Namen "ec69a3ef-43d0-4b02-82fc-65b8622c1629" gespeichert wurde. Dieser Name ist eine UUID. Dies wurde gewählt, da die Anforderung ist, dass jeder Dateiname einzigartig ist. Da UUIDs per Definition einzigartig sind ist dies als beste Wahl für dieses Problem anzusehen.

### Beispiel 2: "Save erkennt es das Kommando auch, wenn es kleingeschrieben ist?"
Das Programm erwartet erneut eine Eingabe. Nun ist zu testen, ob das Programm das Kommando auch erkennt, wenn es nicht groß geschrieben ist. Folgende Antwort des Servers deutet es an:
```java
Bitte geben sie ein Kommando ein: 
Save erkennt es das Kommando auch, wenn es kleingeschrieben ist?
KEY 65048b87-4dd3-4c40-96b1-c3da8d8d6712
```
#### Auswertung des 2. Beispiels
Dieses Beispiel zeigt, dass das Kommando unabhängig davon erkannt wird wie viele Großbuchstaben verwendet werden. Dies war auf Grund der Programmierung allerdings auch zu erwarten. Diese ist hier wie folgt:
```java
String[] components = msg.split(" ");
		
switch (components[0].toUpperCase()) {
	//the first word of the Message is save
	case "SAVE": {
		return MessageType.SAVE;
	}
```
Die 3. Zeile dieses Codes zeigt, dass der erste Teil des Kommando-Strings komplett in Großbuchstaben umgewandelt wird, um dann mit dem Schlüsselwort verglichen zu werden und den richtigen Kommandotypen zurückzugeben.
  
## GET Kommando
Dieses Kommando soll in Kombination mit einem Schlüssel dafür sorgen, dass der gespeicherte String zurückgegeben wird. Um dies zu testen werden erst beide zuvor gespeicherten Strings erfragt und danach ein nicht vorhandener.

### Beispiel 3: "GET ec69a3ef-43d0-4b02-82fc-65b8622c1629"
Dieses Kommando soll den gespeicherten Text aus Beispiel 1 zurückgeben. Folgendes ist die Antwort des Servers:
```java
Bitte geben sie ein Kommando ein: 
GET ec69a3ef-43d0-4b02-82fc-65b8622c1629
OK Dies ist ein Test
```
#### Auswertung des 3. Beispiels
Dieses Beispiel zeigt, dass dieses Kommando zu funktionieren scheint. Der zurückgegebene Text "Dies ist ein Test" entspricht genau dem, was in Beispiel 1 ursprünglich übermittelt wurde.
### Beispiel 4: "Get 65048b87-4dd3-4c40-96b1-c3da8d8d6712"
Dieser Test soll zeigen, dass auch diese Kommando unabhängig von der Schreibweise erkannt wird. Folgendes ist die Antwort des Servers:
```java
Bitte geben sie ein Kommando ein: 
Get 65048b87-4dd3-4c40-96b1-c3da8d8d6712
OK erkennt es das Kommando auch, wenn es kleingeschrieben ist?
```
#### Auswertung des 4. Beispiels
Dies beweist, dass auch dieses Kommando unabhängig von seiner Schreibweise funktioiert. Dies ist wie in Beispiel 2 darauf zurückzuführen, dass alle Buchstaben in Großbuchstaben umgewandelt werden.
### Beispiel 5: "GET ec69a3ef-43d0-4b02-82fc-65b8622c1630"
Dieses Beispiel soll zeigen, dass der Server damit umgehen kann, wenn ein Dokument angefordert wird, welches nicht auf dem Server existiert, ohne abzustürzen. Folgendes ist die Antwort des Servers:
```java
Bitte geben sie ein Kommando ein: 
GET ec69a3ef-43d0-4b02-82fc-65b8622c1630
FAILED
```
Der Server beschwert sich allerdings wie folgt:
```java
java.io.FileNotFoundException: C:\Users\Müller\Desktop\Messages\ec69a3ef-43d0-4b02-82fc-65b8622c1630.msg (The system cannot find the file specified)
	at java.base/java.io.FileInputStream.open0(Native Method)
	at java.base/java.io.FileInputStream.open(FileInputStream.java:216)
	at java.base/java.io.FileInputStream.<init>(FileInputStream.java:157)
	at java.base/java.io.FileInputStream.<init>(FileInputStream.java:111)
	at java.base/java.io.FileReader.<init>(FileReader.java:60)
	at de.tsawlen.server.main.Server.getMessage(Server.java:182)
	at de.tsawlen.server.main.Server.main(Server.java:70)
```
Dieser Fehler resultiert allerdings nicht darin, dass der Server abstürzt, was dadurch bewiesen werden kann, dass dieser auf weitere Kommandos reagiert:
```java
Bitte geben sie ein Kommando ein: 
GET ec69a3ef-43d0-4b02-82fc-65b8622c1629
OK Dies ist ein Test
```
#### Auswertung des 5. Beispiels:
Dies beweist, dass der Server nicht abstüzt, wenn ein unbekannter String angefordert wird. Dies liegt daran, dass der lesende Abschnitt mit einem Try-Catch-Block geschützt ist.

## Unbekanntes Kommando
Auch soll getestet sein, dass der Server mit vollkommen unbekannten Kommandos umgehen kann ohne abzustürzen.
### Beispiel 6: "Bring den Müll raus!"
Dieses Kommando ist dem Server klar unbekannt. Seine Reaktion auf diese Aufforderung ist wie folgt:
```java
Bitte geben sie ein Kommando ein: 
Bring den Müll raus!
Status 404: Unknown Command
```
#### Auswertung des 6. Beispiels:
Mit dieser Antwort ist bewiesen, dass der Server auch auf unbekannte Anfragen reagieren kann ohne abzustürzen. Zudem informiert der Server den Nutzer mit einem bekannten Fehlercode, nämlich "Status 404: Unkown Command".

### Beispiel 7: " "
Dieses Beispiel soll zeigen, was passiert, wenn man dem Server über den Client einen leeren String schickt. Der Server antwortet folgendes:
```java
Bitte geben sie ein Kommando ein: 

Status 404: Unknown Command
```
#### Auswertung des 7. Beispiels:
Hiermit ist klar, dass der Server kein Problem damit hat, leere Werte geschickt zu bekommen und hierdurch nicht abstürzt. 

### Beispiel 8: "SAVE dies ist ein Test ohne Messages Ordner" 
In diesem Beispiel soll das Verhalten des Servers gezeigt werden, wenn kein Messages Ordner auf dem Desktop existiert. Der Server antwortet folgendes:
```java
Bitte geben sie ein Kommando ein: 
SAVE dies ist ein Test ohne Messages Ordner
KEY ae77762b-74cc-489e-a4da-085c32688411
```
Da dies erstmal als Fehler zu interpretieren könnte, muss klar gestellt sein, dass dies keiner ist. Der Server gibt in diesem Fehlerfall folgende Ausgabe:
```java
Messages Folder does not exist!
Creating....
```
#### Auswertung des 8. Beispiels:
Dieses Beispiel zeigt also erst einen vermeindlichen Fehler. Allerdings ist dies kein Fehler, da der Server automatisch erkennt, dass der Ordner zum Speichern nicht existiert und dieses deshalb erstellt und dann dort die Nachricht hinspeichert.

### Beispiel 9: "Client start mit abgeschalteten Server"
In diesem Beispiel soll getestet werden, was passiert, wenn der Client gestartet wird, ohne das der Server läuft. Der Client gibt folgendes aus:
```java
Connection failed!
```
#### Auswertung des 9. Beispiels
Der Client stürzt nicht ab, sondern beendet sich selber, was dafür spricht, dass auch hier kein Problem vorliegt

## Auswertung
Mit all diesen Testfällen ist festzustellen, dass der Server funktioniert und seine Aufgabe gemäß den Anforderungen erfüllen kann!