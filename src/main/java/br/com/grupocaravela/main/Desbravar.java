package br.com.grupocaravela.main;

import java.awt.Color;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.JOptionPane;

import br.com.grupocaravela.aguarde.BootSplash;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.view.JanelaLogin;

public class Desbravar {
	
	public static void main(String[] args) {
		
		// #############################################
				final Thread tr = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							
							JanelaLogin loginView = new JanelaLogin();
							loginView.setVisible(true);
							loginView.setLocationRelativeTo(null);
							
						} catch (Exception ex) {
							
							JOptionPane.showMessageDialog(null, "Erro na tentativa de conexão com o servidor!!! Verifique a conexão!");
						}
						
					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						tr.start();
						// .....
						BootSplash bootSplash = new BootSplash();
						bootSplash.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
						//bootSplash.setUndecorated(true);
						bootSplash.setBackground(new Color(0, 0, 0, 0));
						bootSplash.setVisible(true);
						bootSplash.setLocationRelativeTo(null);
						try {
							tr.join();
							bootSplash.dispose();

						} catch (InterruptedException ex) {
							// Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE,
							// null, ex);
						}
					}
				}).start();
		}

	
}
