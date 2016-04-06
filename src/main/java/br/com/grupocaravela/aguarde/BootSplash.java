package br.com.grupocaravela.aguarde;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Toolkit;

public class BootSplash extends JFrame {
//teste
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BootSplash frame = new BootSplash();
					frame.setBackground(new Color(0, 0, 0, 0));
					frame.setUndecorated(true);
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public BootSplash() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(BootSplash.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 360, 640);
		//setOpacity(0.90f);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		//contentPane.setBackground(new Color(0, 0, 0, 0));
		setContentPane(contentPane);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(BootSplash.class.getResource("/br/com/grupocaravela/gif/caravela.gif")));
		contentPane.add(label, BorderLayout.CENTER);
	}

}
