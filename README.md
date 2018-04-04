# NetworkPaint
## Projet de programmation répartie de S4

BLONDEL Richard, JEAN Arthur, GUEGAN Victor

Logiciel de dessin collaboratif en réseau pour le module M4102C.

### Si vous utilisez les JAR ...
Vous pouvez simplement le jar sans paquetage :

    java -jar NetPaint.jar

Ou double click sur Windows.

### Si vous utilisez les classes ...

#### Lancer le serveur



Pour lancer le serveur, utilisez simplement :

    java ihm.Connexion 

Puis rentrez le port du serveur et cliquez sur "Créer serveur".

#### Lancer un client

Pour lancer un client, tapez la commande :

    java ihm.Connexion
    
Puis rentrez votre pseudo, l'adresse et le port du serveur et cliquez sur "Rejoindre".
    
Vous pouvez aussi indiquer directement l'adresse, le port, et votre pseudo :

    java ihm.Connexion localhost 8200 Jackie