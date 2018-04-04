package ihm;

import net.miginfocom.swing.MigLayout;
import serveur.Serveur;

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
public class FenetreServeur extends JFrame implements ActionListener {

    private JPanel panelChat;
    private PanelDessin panel;

    private JButton bEnvoiMessage;
    private JTextField textMessage;
    private JTextArea textZoneChat;

    private ArrayList<Forme> tabForme = new ArrayList<>();
    private Forme selection;

    private Serveur serveur;

    private class PanelDessin extends JPanel {

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
    }

    public FenetreServeur(Serveur serveur) {
        this.serveur = serveur;
        setTitle("NetPaint 1.0 - Serveur");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
        setResizable(false);

        setLayout(new MigLayout("", "[][]", "[][]"));

        panel = new PanelDessin();
        panel.setMinimumSize(new Dimension(550, 550));
        panel.setBackground(Color.LIGHT_GRAY);

        add(panel, "span 1 2");

        panelChat = new JPanel();
        panelChat.setLayout(new MigLayout("", "[][]", "[][]"));

        textMessage = new JTextField(25);
        bEnvoiMessage = new JButton("Envoyer");
        bEnvoiMessage.addActionListener(this);
        textZoneChat = new JTextArea();
        textZoneChat.setColumns(23);
        textZoneChat.setRows(20);
        textZoneChat.setText("Bienvenue dans la console serveur !");
        textZoneChat.setBorder(BorderFactory.createCompoundBorder(panelChat.getBorder(),
                BorderFactory.createLineBorder(Color.LIGHT_GRAY)));
        textZoneChat.setEditable(false);
        textZoneChat.setLineWrap(true);
        panelChat.add(new JScrollPane(textZoneChat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), "span, wrap");
        panelChat.add(textMessage, "wrap");
        panelChat.add(bEnvoiMessage);
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
                    serveur.arreterServeur();
                    System.exit(0);
                }
            }
        };
        addWindowListener(exitListener);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void traiterMessage(String donnees) {
        textZoneChat.setText(textZoneChat.getText()+"\n"+donnees);
        if (!donnees.contains(",")) {
            if (donnees.equals("stop")) serveur.arreterServeur();
            if (donnees.equals("clear")) nettoyer();
        } else {
            if (donnees.split(",")[0].equals("chat")) {
                ecrireMessage(donnees);
            } else if (donnees.split(",")[0].contains("DEL")) {
                supprimerForme(donnees);
            }
            else {
                dessinerForme(donnees);
            }
        }
    }

    public void nettoyer() {

    }

    /**
     * Dessine sur le plan de travail la forme reçue dans la chaîne <code>sServ</code>.
     *
     * @param sServ la chaîne reçue par le client contenant une forme à dessiner.
     */
    public void dessinerForme(String sServ) {
        Graphics g = panel.getGraphics();
        Forme forme = new Forme(sServ);
        g.setColor(forme.getCouleur());
        String typeForme = forme.getType();
        if (forme.isRempli() && true) {
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
        g.setColor(Color.LIGHT_GRAY);
        Forme forme = new Forme(sServ);

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
        if (e.getSource().equals(bEnvoiMessage)) {
            serveur.envoyerClients(textMessage.getText());
            textMessage.setText("");
        }
    }

}
