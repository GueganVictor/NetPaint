# NetworkPaint
## Projet de programmation répartie de S4

BLONDEL Richard, JEAN Arthur, GUEGAN Victor

Logiciel de dessin collaboratif en réseau pour le module M4102C.

#### Lancer le serveur

Pour lancer le serveur, utilisez simplement :

	java Serveur <port>

#### Lancer un client

Pour lancer un client, tapez la commande :

    java ihm.Accueil
    
Vous pouvez aussi indiquer directement l'adresse, le port, et votre pseudo :

    java ihm.Accueil localhost 4567 richard

### Si vous utilisez les JAR ...

Vous pouvez simplement les lancer sans indiquer les paquetages :

    java -jar NetPaintServeur.jar <port>
    java -jar NetPaintClient.jar