package ihm;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Accueil extends JFrame implements ActionListener {

    JButton bCreerServ;
    JButton bRejoinServ;

    public Accueil() {
        setTitle("Connexion");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new MigLayout("", "[center]", "[][][]"));

        JLabel labelTitre = new JLabel("Bienvenue dans NetworkPaint !");
        labelTitre.setFont(labelTitre.getFont().deriveFont(23.0f));
        labelTitre.setBorder(BorderFactory.createCompoundBorder(labelTitre.getBorder(), BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        add(labelTitre, "wrap");

        bCreerServ = new JButton("Créer un serveur");
        bCreerServ.addActionListener(this);
        add(bCreerServ,"wrap, gaptop 20, h 40!");

        bRejoinServ = new JButton("Rejoindre un serveur");
        bRejoinServ.addActionListener(this);
        add(bRejoinServ,"wrap, gaptop 20, h 40!, gapbottom 10");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Connexion(e.getSource().equals(bCreerServ));
        dispose();
    }

    public static void main(String[] args) {
        new Accueil();
    }
}
