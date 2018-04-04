package ihm;

import client.Client;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Classe qui affiche la fenêtre de dessin à l'utilisateur.
 *
 * <p>
 *     La fenêtre contient un plan de travail, où l'utilisateur peut dessiner,
 *     dans un panneau à gauche.
 * </p>
 *
 * <p>
 *     Dans la partie droite de la fenêtre, on a le chat ainsi que les outils
 *     de dessin.
 * </p>
 */
public class FenetreDessin extends JFrame implements ActionListener {

	private JPanel panelDroit;
	private JPanel panelChat;
	private PanelDessin panel;

	private ButtonGroup choixForme;
	private JRadioButton radioCarre;
	private JRadioButton radioCercle;

	private ButtonGroup choixRemplissage;
	private JRadioButton radioRempli;
	private JRadioButton radioVide;

	private JButton bSupprimer;

	private JButton bNettoyer;

	private JButton bEnvoiMessage;
	private JTextField textMessage;
	private JTextArea textZoneChat;

	private int xDepart, yDepart;

	private ColorChooserButton choixCouleur;
	private Color couleurDessin = Color.DARK_GRAY;

	private ArrayList<Forme> tabForme = new ArrayList<>();
	private Forme selection;

	private Client client;

	private class PanelDessin extends JPanel implements MouseListener {

		/**
		 * Méthode appelée lors d'un clic sur le plan de travail.
		 *
		 * <p>
		 *     Les informations de dessin sont récupérées : la couleur choisie, la forme choisie ...
		 * </p>
		 * <p>
		 *     Une chaîne contenant la forme à dessiner, conforme au protocole, est envoyée au serveur.
		 * </p>
		 *
		 * @param e l'événement souris qui a déclenché l'appel à cette méthode.
		 */
		public void mouseClicked(MouseEvent e) {
			switch (e.getButton()) {
					case MouseEvent.BUTTON3:
						int x = e.getX();
						int y = e.getY();

						for (Forme f : tabForme) {
							if ((x > f.getX() && x < f.getX() + f.getLargeur()) && (y > f.getY() && y < f.getY() + f.getHauteur())) {
								selection = f;
							}
						}

						break;
				}

		}

		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1){
				xDepart = e.getX(); yDepart = e.getY();
			}
		}

		public void mouseReleased(MouseEvent e) {
			Graphics g = this.getGraphics();
			StringBuilder sb = new StringBuilder();

			int largeurint, hauteurint, temp;
			largeurint = e.getX() - xDepart;
			hauteurint = e.getY() - yDepart;
			System.out.println("x,y:"+xDepart+","+yDepart+"  -  l,h:"+largeurint+","+hauteurint);
			if (largeurint < 0) {
				largeurint = -largeurint;
				xDepart -= largeurint;
			}
			if ( hauteurint < 0) {
				hauteurint = -hauteurint;
				yDepart -= hauteurint;
			}
			System.out.println(xDepart+","+yDepart+"  -  "+largeurint+","+hauteurint);

			if (e.getButton() == MouseEvent.BUTTON1) {
				g.setColor(couleurDessin);
				if (radioRempli.isSelected()) {
					if (radioCercle.isSelected()) {
						tabForme.add(new Forme(Forme.CERCLE, xDepart, yDepart, largeurint, hauteurint, true, couleurDessin));
						sb.append(Forme.CERCLE + ",");
					}
					if (radioCarre.isSelected()) {
						tabForme.add(new Forme(Forme.CARRE, xDepart, yDepart, largeurint, hauteurint, true, couleurDessin));
						sb.append(Forme.CARRE + ",");
					}

					sb.append("p,");
				} else {
					if (radioCercle.isSelected()) {
						tabForme.add(new Forme(Forme.CERCLE, xDepart, yDepart, largeurint, hauteurint, false, couleurDessin));
						sb.append(Forme.CERCLE + ",");
					}
					if (radioCarre.isSelected()) {
						tabForme.add(new Forme(Forme.CARRE, xDepart, yDepart, largeurint, hauteurint, false, couleurDessin));
						sb.append(Forme.CARRE + ",");
					}

					sb.append("v,");
				}

				sb.append(xDepart).append(",").append(yDepart).append(",")
						.append(largeurint).append(",")
						.append(hauteurint).append(",")
						.append(Integer.toHexString(choixCouleur.getSelectedColor().getRGB() & 0xFFFFFF));

				client.envoyer(sb.toString());
			}
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	public FenetreDessin(Client client) {
		this.client = client;

		setTitle("NetPaint 1.0");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(900, 600);
		setResizable(false);

        /*try {
            img = ImageIO.read(new File("images/imgFond.png"));
        } catch(IOException e) { e.printStackTrace(); }
        */
		setLayout(new MigLayout("", "[][]", "[][]"));

		panel = new PanelDessin();
		panel.setMinimumSize(new Dimension(550, 550));
		panel.setBackground(Color.LIGHT_GRAY);
		panel.addMouseListener(panel);

		add(panel, "span 1 2");

		panelDroit = new JPanel(new MigLayout());

		choixForme = new ButtonGroup();
		radioCarre = new JRadioButton("Carre");
		radioCarre.setSelected(true);
		radioCercle = new JRadioButton("Cercle");
		choixForme.add(radioCarre);
		choixForme.add(radioCercle);
		panelDroit.add(radioCarre);
		panelDroit.add(radioCercle, "wrap");

		choixRemplissage = new ButtonGroup();
		radioRempli = new JRadioButton("Rempli", true);
		radioVide = new JRadioButton("Vide");
		choixRemplissage.add(radioRempli);
		choixRemplissage.add(radioVide);
		panelDroit.add(radioRempli, " gaptop 30");
		panelDroit.add(radioVide, "wrap");

		choixCouleur = new ColorChooserButton(Color.DARK_GRAY);
		choixCouleur.addColorChangedListener(new ColorChooserButton.ColorChangedListener() {
			@Override
			public void colorChanged(Color newColor) {
				couleurDessin = newColor;
			}
		});
		panelDroit.add(choixCouleur, "gaptop 30, center");

		bSupprimer = new JButton("Supprimer");
		bSupprimer.addActionListener(this);
		panelDroit.add(bSupprimer, "gaptop 30, center, wrap");

		bNettoyer = new JButton("Nettoyer");
		bNettoyer.addActionListener(this);
		panelDroit.add(bNettoyer,"gaptop 5, center");

		panelChat = new JPanel();
		panelChat.setLayout(new MigLayout("", "[][]", "[][]"));

		textMessage = new JTextField(25);
		bEnvoiMessage = new JButton("Envoyer");
		bEnvoiMessage.addActionListener(this);
		textZoneChat = new JTextArea();
		textZoneChat.setColumns(23);
		textZoneChat.setRows(20);
		textZoneChat.setText("Bienvenue dans le chat");
		textZoneChat.setBorder(BorderFactory.createCompoundBorder(panelChat.getBorder(),
				BorderFactory.createLineBorder(Color.LIGHT_GRAY)));
		textZoneChat.setEditable(false);
		textZoneChat.setLineWrap(true);
		panelChat.add(new JScrollPane(textZoneChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), "span, wrap");
		panelChat.add(textMessage, "wrap");
		panelChat.add(bEnvoiMessage);

		add(panelDroit, "gapleft 30, wrap");
		add(panelChat, "gapleft 10");

		/* Permet de définir un comportement personnalisé lorsque l'utilisateur clique
		 sur la croix en haut à droite de la fenêtre. On peut donc envoyer un message
		 au serveur disant que l'on se déconnecte. */
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int quitter = JOptionPane.showOptionDialog(
						null, "Voulez-vous vraiment quitter NetPaint ?",
						"quitter", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				System.out.println(quitter);
				if (quitter == 0) {
					client.envoyer("\\q");
					client.arreter();
					System.exit(0);
				}
			}
		};
		addWindowListener(exitListener);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void nettoyer() {
		System.out.println("je suis nettoyer");
	}

	/**
	 * Dessine sur le plan de travail la forme reçue dans la chaîne <code>sServ</code>.
	 *
	 * @param sServ la chaîne reçue par le client contenant une forme à dessiner.
	 */
	public void dessinerForme(String sServ) {
		Graphics g = panel.getGraphics();

		Forme forme = new Forme(sServ);
		String typeForme = forme.getType();
		g.setColor(forme.getCouleur());

		if (forme.isRempli()) {
			if (typeForme.equals(Forme.CERCLE)) {
				g.fillOval(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			}
			if (typeForme.equals(Forme.CARRE)) {
				g.fillRect(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			}
		} else {
			if (typeForme.equals(Forme.CERCLE)) {
				g.drawOval(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			}
			if (typeForme.equals(Forme.CARRE)) {
				g.drawRect(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			}
		}

		tabForme.add(forme);
	}

	/**
	 * Supprime du plan de travail la forme reçue dans la chaîne <code>sServ</code>.
	 *
	 * @param sServ la chaîne reçue par le client contenant une forme à effacer.
	 */
	public void supprimerForme(String sServ) {
		Graphics g = panel.getGraphics();

		Forme forme = new Forme(sServ);

		g.setColor(Color.LIGHT_GRAY);

		if (forme.getType().equals(Forme.CERCLE)) {
			if (forme.isRempli()) g.fillOval(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			else g.drawOval(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
		}
		if (forme.getType().equals(Forme.CARRE)) {
			if (forme.isRempli()) g.fillRect(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
			else g.drawRect(forme.getX(), forme.getY(), forme.getLargeur(), forme.getHauteur());
		}

		tabForme.remove(selection);
		selection = null;
	}

	/**
	 * Ajoute au chat le message reçu dans la chaîne <code>sServ</code>.
	 *
	 * @param sServ la chaîne contenant le message reçu par le client.
	 */
	public void ecrireMessage(String sServ) {
		sServ = sServ.substring(sServ.indexOf(",") + 1);
		textZoneChat.setText(textZoneChat.getText() + "\n" + sServ);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(bSupprimer)) {
			Graphics g = panel.getGraphics();
			g.setColor(Color.LIGHT_GRAY);

			if (selection != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("DEL");

				if (selection.getType().equals(Forme.CERCLE)) {
					sb.append(Forme.CERCLE + ",");

					if (selection.isRempli()) {
						sb.append("p,");
					} else {
						sb.append("v,");
					}
				}
				if (selection.getType().equals(Forme.CARRE)) {
					sb.append(Forme.CARRE + ",");

					if (selection.isRempli()) {
						sb.append("p,");
					} else {
						sb.append("v,");
					}
				}

				sb.append(selection.getX()).append(",").append(selection.getY()).append(",").append(selection.getLargeur()).append(",").append(selection.getHauteur()).append(",")
						.append(Integer.toHexString(choixCouleur.getSelectedColor().getRGB() & 0xFFFFFF));
				client.envoyer(sb.toString());
			}
		}

		if (e.getSource().equals(bNettoyer)) {
			client.envoyer("clear");
		}

		if (e.getSource().equals(bEnvoiMessage)) {
			client.envoyerMessage(textMessage.getText());

			textMessage.setText("");
		}
	}
}
