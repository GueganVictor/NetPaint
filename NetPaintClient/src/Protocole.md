##Connexion d'un client au serveur :
1. Le client ouvre un socket vers le serveur.
2. Le client envoie son pseudo au serveur.

##Communication client - serveur :
_le client dessine sur son interface_

* le client envoie le rectangle fait sous forme d'une chaîne de caractères
* le serveur reçoit la chaîne
* le serveur retransmet la chaîne à tous les clients
* les clients reçoivent la chaîne qu'ils décodent, et l'affichent sur leur interface

##Format des chaînes de caractères de dessin :

    forme,(p|v),x,y,couleur

**Forme** : la forme représentée, valeurs possibles :
* `rectangle`
* ...

**Plein ou vide** : p indique que la forme est pleine, indique qu'elle est vide.

**x et y** : les coordonnées par rapport au plan de dessin de la forme.

**couleur** : le code hexadécimal de la couleur de la forme.

####Exemple :

`rectangle,p,20,30,FF0000`

Cette chaîne indique un rectangle plein, aux coordonnées (20, 30) de couleur rouge.