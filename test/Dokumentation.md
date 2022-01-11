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