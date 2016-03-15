package br.com.grupocaravela.aguarde;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class EsperaAtualizar extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblAguardeAtualizando;
	private JProgressBar progressBar;
	private int valorProgressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EsperaAtualizar frame = new EsperaAtualizar();
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
	public EsperaAtualizar() {
		
		carregarJanela();

		Thread thread = new ThreadBasica(); // Cria a tread para carregar o
		// comobox;
		//thread.start(); // Inicia Thread
		Thread t = new Thread(new ProgressRunnable());  
        t.start(); 

	}

	private void carregarJanela() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 224, 374);
		contentPane = new JPanel();
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBorder(new LineBorder(Color.DARK_GRAY));

		setContentPane(contentPane);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(EsperaAtualizar.class.getResource("/br/com/grupocaravela/gif/caravela3D.gif")));

		lblAguardeAtualizando = new JLabel("Aguarde, atualizando!..");
		lblAguardeAtualizando.setHorizontalAlignment(SwingConstants.CENTER);

		progressBar = new JProgressBar();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(
						gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
										.addComponent(lblAguardeAtualizando, GroupLayout.DEFAULT_SIZE, 216,
												Short.MAX_VALUE)
										.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup().addGap(2).addComponent(label,
						GroupLayout.PREFERRED_SIZE, 216, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblAguardeAtualizando)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);
	}

	class ThreadBasica extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {
			
			
			/*
			 * carregajcbRota(); carregajcbCidade();
			 */

		}
	}
	
	private JProgressBar getProgressBar() {  
        if (progressBar != null)
            return progressBar;
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);  //seta o valor do início da barra
        progressBar.setMaximum(100);    //seta o valor do fim da barra  
        valorProgressBar = progressBar.getMinimum();
        progressBar.setStringPainted(true); //Exibe uma string na barra
        progressBar.setString("Em andamento...");   //seta a string a ser exibida
        
        return progressBar;
    }
  
    private class ProgressRunnable implements Runnable {
        public void run() {
            try {  
                while (valorProgressBar <= progressBar.getMaximum()) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            progressBar.setValue(valorProgressBar++);  //atualiza o valor da barra
                        }  
                    });
                    Thread.sleep(50);
                }  
            } catch (InterruptedException ex){
                ex.printStackTrace();
            }
        }
    }
}
