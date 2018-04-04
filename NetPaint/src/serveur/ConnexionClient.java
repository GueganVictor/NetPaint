package serveur;

import java.io.*;
import java.net.Socket;

/**
 * Classe représentant une connexion en cours avec un client,
 * se déroulant dans un fil d'exécution séparé.
 *
 * <p>
 *     Une connexion en cours est représentée par un socket ouvert
 *     sur lequel le client peut communiquer avec le serveur.
 * </p>
 */
class ConnexionClient extends Thread {
	/**
	 * Constante définissant la commande qui, lorsqu'elle est reçue du client,
	 * ferme la connexion avec celui-ci.
	 */
	private static final String COMMANDE_QUITTER = "\\q";

	/**
	 * Socket sur lequel le client et le serveur communiquent.
	 */
	private Socket socket;

	/**
	 * Instance du serveur qui écoute les connexions entrantes.
	 */
	private Serveur serveur;


	private BufferedReader in;
	private PrintWriter out;

	private boolean enFonctionnement;

	/**
	 * Constructeur qui maintient une connexion ouverte avec un client.
	 *
	 * @param connexion le socket lié au client.
	 * @param serveur une instance du serveur.
	 */
	ConnexionClient(Socket connexion, Serveur serveur) {
		socket = connexion;
		this.serveur = serveur;

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		enFonctionnement = true;
	}

	/**
	 * Méthode permettant d'envoyer une commande au client.
	 *
	 * @param commande la commande à envoyer au client.
	 */
	void envoyer(String commande) {
		out.println(commande);
		out.flush();
	}

	/**
	 * Point d'entrée de ce fil d'exécution.
	 *
	 * <p>
	 *     On attend simplement les commandes du client.
	 * </p>
	 */
	@Override
	public void run() {
		try {
			while (enFonctionnement) {
				// On récupère les commandes du client, etc.
				String commande = in.readLine();


				System.out.println("Commande client reçue : " + commande);

				if (commande.equals(ConnexionClient.COMMANDE_QUITTER)) {
					enFonctionnement = false;
					break;
				}

				serveur.envoyerClients(commande);
			}

			in.close();
			out.close();
			socket.close();

			serveur.retirerClient(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
