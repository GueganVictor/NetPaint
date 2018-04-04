package ihm;

import java.awt.*;

/**
 * Classe représentant une forme du plan de dessin.
 * <p>
 * <p>
 * Dans le protocole, une forme est représentée de la façon suivante :
 * </p>
 * <pre>
 *     typeForme,(p|v),x,y,couleur
 * </pre>
 * <p>
 * <p>
 * ... où le type de la forme est soit <code>carre</code> ou <code>cercle</code>, et
 * <code>p</code> ou <code>v</code> indique si la forme est pleine ou vide.
 * </p>
 */
class Forme {

	static final String CARRE = "carre";
	static final String CERCLE = "cercle";

	private String type;
	private boolean rempli;
	private int x, y;
	private int largeur, hauteur;
	private Color couleur;

	Forme(String type, int x, int y, int largeur, int hauteur, boolean rempli, Color couleur) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.rempli = rempli;
		this.couleur = couleur;
	}

	/**
	 * Constructeur renvoyant une forme construite à partir d'une chaîne reçue
	 * du serveur.
	 *
	 * @param forme la chaîne reçue par le client.
	 */
	Forme(String forme) {
		String[] donneesForme = forme.split(",");
		type = donneesForme[0].contains("DEL") ? donneesForme[0].substring(3) : donneesForme[0];
		rempli = donneesForme[1].equals("p");
		x = Integer.parseInt(donneesForme[2]);
		y = Integer.parseInt(donneesForme[3]);
		largeur = Integer.parseInt(donneesForme[4]);
		hauteur = Integer.parseInt(donneesForme[5]);
		couleur = Color.decode("#" + donneesForme[6]);
	}

	boolean isRempli() {
		return rempli;
	}

	void setRempli(boolean rempli) {
		this.rempli = rempli;
	}

	String getType() {
		return type;
	}

	void setType(String type) {
		this.type = type;
	}

	int getX() {
		return x;
	}

	void setX(int x) {
		this.x = x;
	}

	int getY() {
		return y;
	}

	void setY(int y) {
		this.y = y;
	}

	int getLargeur() {
		return this.largeur;
	}

	void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	int getHauteur() {
		return this.hauteur;
	}

	void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

	Color getCouleur() {
		return couleur;
	}

	void setCouleur(Color couleur) {
		this.couleur = couleur;
	}

	/**
	 * Renvoie une description de la forme conforme au protocole.
	 *
	 * @return une description de la forme.
	 */
	public String toString() {
		return "" + type + "," + (rempli ? "p" : "v") + "," +
				x + "," + y + "," + largeur + "," + hauteur + ","
				+ Integer.toHexString(couleur.getRGB() & 0xffffff);
	}
}
