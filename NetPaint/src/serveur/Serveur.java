package serveur;

import client.Client;
import ihm.FenetreDessin;
import ihm.FenetreServeur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Représente le fil d'exécution dans lequel le serveur écoute
 * les connexions entrantes.
 *
 * <p>
 *     Les clients connectés sont gardés dans la liste {@link #clients} sous
 *     la forme d'instances de l'objet {@link ConnexionClient}.
 * </p>
 */
public class Serveur extends Thread {
	/**
	 * Représente le port sur lequel écoute le serveur.
	 */
	private int port;

	/**
	 * Le socket sur lequel le port écoute les connexions entrantes.
	 */
	private ServerSocket socket;

	/**
	 * La liste des clients connectés.
	 */
	private ArrayList<ConnexionClient> clients;

	private FenetreServeur fenetreServeur;

	private boolean enFonctionnement;

	/**
	 * Constructeur de serveur qui ouvre la socket sur le port donné.
	 *
	 * @param port le port sur lequel écouter les connexions entrantes.
	 */
	public Serveur(int port) {
		this.port = port;

		try {
			socket = new ServerSocket(port);
			enFonctionnement = true;
			fenetreServeur = new FenetreServeur(this);
		} catch (IOException e) {
			e.printStackTrace();
		}

		clients = new ArrayList<>();
	}

	public void majIHM(String commande) {
		fenetreServeur.traiterMessage(commande);
	}

	/**
	 * Retire le client donné de la liste des clients connectés.
	 *
	 * @param connexionClient le client à retirer.
	 */
	void retirerClient(ConnexionClient connexionClient) {
		clients.remove(connexionClient);
	}

	/**
	 * Envoie le message donné à tous les clients connectés.
	 *
	 * @param commande la commande à transmettres aux clients.
	 */
	public void envoyerClients(String commande) {
		for (ConnexionClient client : clients) {
			client.envoyer(commande);
		}
	}

	/**
	 * Point d'entrée de ce fil d'exécution.
	 *
	 * <p>
	 *     Le serveur attend simplement les connexions entrantes.
	 * </p>
	 */
	@Override
	public void run() {
		while (enFonctionnement) {
			try {
				System.out.println("Serveur : j'attends ...");
				Socket client = socket.accept();

				ConnexionClient connexionClient = new ConnexionClient(client, this);
				connexionClient.start();
				clients.add(connexionClient);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Arrêt.");
	}

	/**
	 * Arrête le serveur et ferme son socket.
	 */
	public void arreterServeur() {
		enFonctionnement = false;

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage : java Serveur <port>");
			System.exit(1);
		}

		new Serveur(Integer.parseInt(args[0])).start();
	}

}
