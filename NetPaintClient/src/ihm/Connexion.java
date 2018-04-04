package ihm;

import client.Client;
import net.miginfocom.swing.MigLayout;
import serveur.Serveur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe qui permet d'afficher une fenêtre de connexion à un serveur de dessin.
 */
class Connexion extends JFrame implements ActionListener {

	private JLabel labelTitre;

	private JTextField champPseudo;
	private JLabel labelPseudo;
	private JTextField champAdresse;
	private JLabel labelAdresse;
	private JTextField champPort;
	private JLabel labelPort;

	private JButton bRejoindre;
	private JButton bCreer;

	private Connexion() {

		setTitle("Connexion");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		setLayout(new MigLayout("", "[center]", "[][][]"));

		labelTitre = new JLabel("Bienvenue dans NetworkPaint !");
		labelTitre.setFont(labelTitre.getFont().deriveFont(23.0f));
		labelTitre.setBorder(BorderFactory.createCompoundBorder(labelTitre.getBorder(), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		add(labelTitre, "wrap, span 2 1");

		labelPseudo = new JLabel("Pseudo :");
		labelPseudo.setFont(labelPseudo.getFont().deriveFont(15.0f));
		add(labelPseudo, "wrap, gaptop 30, span 2 1");

		champPseudo = new JTextField();
		champPseudo.setFont(labelTitre.getFont().deriveFont(15.0f));
		champPseudo.setBorder(BorderFactory.createCompoundBorder(champPseudo.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		champPseudo.setColumns(20);
		add(champPseudo, "wrap, gaptop 5, span 2 1");

		labelAdresse = new JLabel("Adresse serveur :");
		labelAdresse.setFont(labelAdresse.getFont().deriveFont(15.0f));
		add(labelAdresse, "wrap, gaptop 30, span 2 1");

		champAdresse = new JTextField();
		champAdresse.setFont(labelTitre.getFont().deriveFont(15.0f));
		champAdresse.setBorder(BorderFactory.createCompoundBorder(champAdresse.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		champAdresse.setColumns(20);
		add(champAdresse, "wrap, gaptop 5, span 2 1");

		labelPort = new JLabel("Port utilisé :");
		labelPort.setFont(labelPort.getFont().deriveFont(15.0f));
		add(labelPort, "wrap, gaptop 30, span 2 1");

		champPort = new JTextField();
		champPort.setFont(labelTitre.getFont().deriveFont(15.0f));
		champPort.setBorder(BorderFactory.createCompoundBorder(champPort.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		champPort.setColumns(20);
		add(champPort, "wrap, gaptop 5, span 2 1");

		bRejoindre = new JButton("Se connecter");
		bRejoindre.addActionListener(this);
		add(bRejoindre, "gaptop 30, gapbottom 20, h 50! ");

		bCreer = new JButton("Créer serveur");
		bCreer.addActionListener(this);
		add(bCreer, "gaptop 30, gapbottom 20, h 50! ");

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(bCreer)) {
			if (champPort.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Veuillez remplir le port !");
			} else try {
				new Serveur(Integer.parseInt(champPort.getText()));
			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(this, "Le port ne doit contenir que des chiffres !");
			}
		} else if (champAdresse.getText().trim().equals("") || champPort.getText().trim().equals("")  || champPseudo.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(this, "Veuillez remplir tout les champs !");
		} else try {
			new Client(champAdresse.getText(), Integer.parseInt(champPort.getText()), champPseudo.getText()).start();

			dispose();
		} catch (NumberFormatException nfe) {
			JOptionPane.showMessageDialog(this, "Le port ne doit contenir que des chiffres !");
		}
	}

	public static void main(String[] args) {
		if (args.length  == 3) {
			new Client(args[0], Integer.parseInt(args[1]), args[2]).start();
		}
		else new Connexion();
	}
}
