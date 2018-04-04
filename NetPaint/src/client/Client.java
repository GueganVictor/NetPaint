package client;

import ihm.FenetreDessin;

import java.io.*;
import java.net.Socket;

/**
 * Classe représentant un client connecté au serveur de dessin partagé.
 *
 * <p>
 *     Le client tourne dans un fil d'exécution (<em>Thread</em>) qui lui est propre.
 * </p>
 *
 * <p>
 *     Un client possède un socket, un pseudo, et une fenêtre permettant de dessiner.
 * </p>
 */
public class Client extends Thread {
	/**
	 * Le pseudo du client, choisi à la connexion.
	 */
	private String pseudo;

	/**
	 * Socket permettant la communication avec le serveur.
	 */
	private Socket socket;

	private BufferedReader in;
	private PrintWriter out;

	/**
	 * Fenêtre dans laquelle l'utilisateur dessine et envoie des messages.
	 *
	 * @see ihm.FenetreDessin
	 */
	private FenetreDessin fenetreDessin;

	/**
	 * Indique si le client est en cours de fonctionnement ou non.
	 */
	private boolean enFonctionnement;

	/**
	 * Constructeur de client qui tente d'ouvrir une connexion sur l'adresse et le
	 * port donnés en paramètres. Le pseudo est utilisé pour les messages dans le chat.
	 *
	 * @param hostname le nom d'hôte ou adresse IP à laquelle se connecter.
	 * @param port le port sur lequel se connecter sur l'adresse <code>hostname</code>.
	 * @param pseudo le pseudo de ce client.
	 */
	public Client(String hostname, int port, String pseudo) {
		this.pseudo = pseudo;

		try {
			// On ouvre le socket :
			socket = new Socket(hostname, port);

			// puis on ouvre les "tubes" pour envoyer et recevoir des données :
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());

			enFonctionnement = true;

			// Création de la fenêtre de dessin :
			fenetreDessin = new FenetreDessin(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Point d'entrée de ce fil d'exécution.
	 *
	 * <p>
	 *     On écoute sur le socket et on attend l'arrivée des messages du serveur.
	 * </p>
	 *
	 * <p>
	 *     On traite chaque message avec la méthode {@link #traiterMessage(String)}.
	 * </p>
	 */
	@Override
	public void run() {
		try {
			// On récupère les commandes du serveur, etc.
			while (enFonctionnement) {
				System.out.println("En attente d'un message serveur ...");
				String message = in.readLine();

				System.out.println("commande = " + message);

				// Lors de la déconnexion, on reçoit un message qui vaut null ...
				if (message != null) traiterMessage(message);
			}

			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant d'envoyer un message du chat sur le socket de ce client.
	 *
	 * <p>
	 *     Cette méthode envoie un message de chat au serveur tel que c'est implémenté dans
	 *     le protocole, donc sous la forme : <code>chat,Un message</code>
	 * </p>
	 *
	 * <p>
	 *     Dans le message, le client rajoute son pseudo de manière à ce que le message s'affiche
	 *     ainsi : <code>Richard : salut</code>.
	 * </p>
	 *
	 * @param message le message à envoyer au serveur.
	 */
	public void envoyerMessage(String message) {
		envoyer("chat," + pseudo + " : " + message);
	}

	/**
	 * Méthode permettant d'envoyer une commande au serveur.
	 *
	 * <p>
	 *     Les commandes disponibles et une description du protocole sont dans le fichier
	 *     <code>package-info.java</code> de la documentation serveur.
	 * </p>
	 *
	 * @param commande la commande à envoyer au serveur.
	 */
	public void envoyer(String commande) {
		out.println(commande);
		out.flush();
	}

	/**
	 * Méthode de traitement d'un message reçu du serveur.
	 *
	 * <p>
	 *     Le message reçu est analysé et l'action correspondante est effectuée
	 *     sur la fenêtre de dessin.
	 * </p>
	 *
	 * @param donnees le message reçu du serveur.
	 */
	private void traiterMessage(String donnees) {
		if (donnees.split(",")[0].equals("chat")) {
			fenetreDessin.ecrireMessage(donnees);
		} else if (donnees.split(",")[0].contains("DEL")) {
			fenetreDessin.supprimerForme(donnees);
		}
		else {
			fenetreDessin.dessinerForme(donnees);
		}
	}

	/**
	 * Méthode permettant d'arrêter le fonctionnement de ce client.
	 *
	 * <p>
	 *     Utilisée lorsque le client ferme la fenêtre de dessin.
	 * </p>
	 */
	public void arreter() {
		enFonctionnement = false;
	}
}