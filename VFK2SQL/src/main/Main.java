package main;

import javax.swing.UIManager;

import api.Api;
import gui.Gui;
/**
 * Hlavni trida, urcena pro spusteni programu ve dvou variantach GUI i API.
 * @author Jan Malek
 *
 */
public class Main {
	public static void main(String[] args) {
		if (args.length > 0) Api.input(args);
		else         
			try {
	        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(Gui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new Gui().setVisible(true);
                }
            });
	}
}
