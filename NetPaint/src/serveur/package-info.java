/**
 * Paquetage gérant la partie serveur.
 *
 * <p>Le protocole utilisé est le suivant :</p>
 *
 * <h1>Connexion</h1>
 *
 * <p>
 *     Le serveur écoute sur son port attribué et attend les connexions entrantes.
 * </p>
 * <p>
 *     Un client se connecte : un socket lui est attribué par le serveur, à travers
 *     la classe {@link serveur.ConnexionClient}.
 * </p>
 *
 * <h1>Communication</h1>
 *
 * <h3>Ajout d'une forme au dessin</h3>
 *
 * <p>
 *     Lorsqu'un utilisateur dessine sur son plan de travail, une chaîne représentant
 *     la forme est envoyée au serveur. Cette chaîne est représentée ainsi :
 * </p>
 *
 * <pre>
 *     typeForme,p|v,x,y,couleur
 * </pre>
 *
 * <p>Le type de la forme est un mot donnant le type de la forme : carré, cercle ...</p>
 * <p>
 *     Le caractère <code>p</code> ou <code>v</code> indique si la forme est pleine
 *     ou vide.
 * </p>
 * <p>
 *     <code>x</code> et <code>y</code> indiquent les coordonnées de la forme sur le
 *     plan de dessin.
 * </p>
 * <p><code>couleur</code> est la couleur de la forme au format hexadécimal.</p>
 *
 * <p>Une fois la forme envoyée au serveur, celui-ci la renvoie à tous les clients connectés.</p>
 * <p>Les clients connectés reçoivent la forme sous ce même format et l'ajoutent à leur dessin</p>
 *
 * <h3>Suppression d'une forme</h3>
 *
 * <p>Chaque client possède une liste locale des formes présentes sur le dessin.</p>
 *
 * <p>
 *     Lorsqu'un utilisateur supprime une forme du dessin, la forme est retirée de la liste
 *     et le client envoie la forme au serveur sous forme d'une chaîne précédée du mot-clé <code>DEL</code>.
 * </p>
 * <p>Pour le reste, cette chaîne est au même format que décrit précédemment.</p>
 *
 * <p>Lorsque les clients reçoivent cette chaîne, ils suppriment la forme de leur dessin.</p>
 *
 * <h1>Déconnexion</h1>
 *
 * <p>Lorsqu'un client ferme sa fenêtre de dessin, un message est envoyé au serveur : <code>\q</code>.</p>
 *
 * <p>Le serveur ferme donc la connexion avec ce client.</p>
 *
 * <p>
 *     À chaque fermeture d'une connexion avec un client, le serveur vérifie le nombre de clients
 *     connectés. S'il n'y a plus aucun connexion en cours, le serveur s'arrête automatiquement.
 * </p>
 */
package serveur;