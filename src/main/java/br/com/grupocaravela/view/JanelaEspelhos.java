package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Rota;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.relatorios.ChamaRelatorio;
import br.com.grupocaravela.relatorios.ChamaRelatorioEspelho;
import br.com.grupocaravela.util.ConectaBanco;
import br.com.grupocaravela.view.JanelaVendas.ThreadBasica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JCheckBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class JanelaEspelhos extends JFrame {

	private JPanel contentPane;

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private JComboBox cbVendedorContasReceber;

	private JComboBox cbRotaContasReceber1;

	private JComboBox cbRotaVendaProdutos3;

	private JComboBox cbVendedorVendasProdutos2;

	private JComboBox cbRotaVendaProdutos1;

	private JDateChooser dcDataInicialVendasProdutos;

	private JDateChooser dcDataFinalVendasProdutos;

	private JComboBox cbRotaContasReceber2;

	private JDateChooser dcInicialContasReceber;

	private JDateChooser dcFinalContasReceber;

	private JComboBox cbVendedorContasReceber3;

	private JComboBox cbRotaContasReceber3;

	private JComboBox cbVendedorVendas;

	private JComboBox cbRotaVendas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaEspelhos frame = new JanelaEspelhos();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JanelaEspelhos() {
		carregarJanela();

		Thread thread = new ThreadBasica();
		thread.start(); // Inicia Thread
/*
		// Evento ao fechar a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					factory.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
*/		
	}

	class ThreadBasica extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {

			iniciaConexao();
			carregajcbUsuario();
			carregajcbRota();

		}
	}

	private void carregarJanela() {

		setTitle("Espelhos");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Vendas de produtos", null, panel, null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
								.addContainerGap()));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
				"Espelho com filtro de Rota, Vendedor e Data para produtos vendidos", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));

		JLabel label_9 = new JLabel("Rota");

		cbRotaVendaProdutos3 = new JComboBox();

		JLabel label_10 = new JLabel("Vendedor");

		cbVendedorVendasProdutos2 = new JComboBox();

		JButton button_4 = new JButton("Gerar relatório");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				// ###############################################
				try {
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					//try {
						cre.report("EspelhoProdutosVendidosRotaVendedor.jasper",
								(Usuario) cbVendedorVendasProdutos2.getSelectedItem(),
								(Rota) cbRotaVendaProdutos3.getSelectedItem(), dcDataInicialVendasProdutos.getDate(),
								dcDataFinalVendasProdutos.getDate());
					//} catch (JRException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado");
				}

				// ###############################################

			}
		});
		button_4.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(label_9)
							.addGap(12)
							.addComponent(cbRotaVendaProdutos3, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(label_10)
							.addGap(12)
							.addComponent(cbVendedorVendasProdutos2, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
							.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
					.addContainerGap(12, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGap(5)
									.addComponent(label_9))
								.addComponent(cbRotaVendaProdutos3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGap(5)
									.addComponent(label_10))
								.addComponent(cbVendedorVendasProdutos2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
				"Espelho com filtro de Rota para produtos vendidos", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(51, 51, 51)));

		JLabel label_13 = new JLabel("Rota");

		cbRotaVendaProdutos1 = new JComboBox();

		JButton button_6 = new JButton("Gerar relatório");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ###############################################
				try {
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					//try {
						cre.report("EspelhoProdutosVendidosRota.jasper",
								null,
								(Rota) cbRotaVendaProdutos3.getSelectedItem(), dcDataInicialVendasProdutos.getDate(),
								dcDataFinalVendasProdutos.getDate());
					//} catch (JRException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado");
				}

				// ###############################################
				
			}
		});
		button_6.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_10 = new GroupLayout(panel_10);
		gl_panel_10.setHorizontalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_10.createSequentialGroup()
					.addContainerGap()
					.addComponent(label_13)
					.addGap(12)
					.addComponent(cbRotaVendaProdutos1, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
					.addComponent(button_6, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_10.setVerticalGroup(
			gl_panel_10.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_10.createSequentialGroup()
					.addContainerGap(18, Short.MAX_VALUE)
					.addGroup(gl_panel_10.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_10.createSequentialGroup()
							.addGap(5)
							.addComponent(label_13))
						.addComponent(cbRotaVendaProdutos1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_6, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_10.setLayout(gl_panel_10);

		JPanel panel_11 = new JPanel();
		panel_11.setBorder(
				new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Espelho sem filtro para produtos vendidos",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		JButton button_7 = new JButton("Gerar relatório");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// ###############################################
				try {
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					//try {
						cre.report("EspelhoProdutosVendidos.jasper",
								null,
								null, dcDataInicialVendasProdutos.getDate(),
								dcDataFinalVendasProdutos.getDate());
					//} catch (JRException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado");
				}

				// ###############################################
				
			}
		});
		button_7.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11
				.setHorizontalGroup(gl_panel_11.createParallelGroup(Alignment.TRAILING).addGap(0, 733, Short.MAX_VALUE)
						.addGroup(gl_panel_11.createSequentialGroup().addContainerGap(543, Short.MAX_VALUE)
								.addComponent(button_7, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_panel_11.setVerticalGroup(gl_panel_11.createParallelGroup(Alignment.LEADING).addGap(0, 66, Short.MAX_VALUE)
				.addGroup(gl_panel_11.createSequentialGroup()
						.addComponent(button_7, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(10, Short.MAX_VALUE)));
		panel_11.setLayout(gl_panel_11);
		
				JLabel label = new JLabel("Data inicial");
				label.setFont(new Font("Dialog", Font.BOLD, 14));
		
				dcDataInicialVendasProdutos = new JDateChooser();
		
				JLabel label_8 = new JLabel("Data final");
				label_8.setFont(new Font("Dialog", Font.BOLD, 14));
		
				dcDataFinalVendasProdutos = new JDateChooser();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(label)
							.addGap(12)
							.addComponent(dcDataInicialVendasProdutos, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(label_8, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(dcDataFinalVendasProdutos, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_11, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
						.addComponent(panel_10, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(4)
							.addComponent(label))
						.addComponent(dcDataInicialVendasProdutos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(4)
							.addComponent(label_8))
						.addComponent(dcDataFinalVendasProdutos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(panel_11, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_10, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(84, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Contas a receber", null, panel_1, null);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1
				.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
								.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_1
				.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
								.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
								.addContainerGap()));

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(
				new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Espelho sem filtro para contas a receber",
						TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		JButton button = new JButton("Gerar relatório");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// ###########################################################
					ChamaRelatorio cr = new ChamaRelatorio();

					try {
						cr.report("EspelhoContasReceberSemFiltro.jasper");
					} catch (JRException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// ###########################################################
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possivel emitir o relatório solicitado");
				}

			}
		});
		button.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_5.createSequentialGroup().addContainerGap(543, Short.MAX_VALUE)
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
						.addComponent(button, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(10, Short.MAX_VALUE)));
		panel_5.setLayout(gl_panel_5);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
				"Espelho com filtro de Rota para contas a receber", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(51, 51, 51)));

		JLabel label_3 = new JLabel("Rota");

		cbRotaContasReceber1 = new JComboBox();

		JButton button_1 = new JButton("Gerar relatório");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// ###############################################
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					try {
						cre.report("EspelhoContasReceberRota.jasper", null,
								(Rota) cbRotaContasReceber1.getSelectedItem(), null, null);
					} catch (JRException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possivel emitir o relatório solicitado");
				}

				// ###############################################

			}
		});
		button_1.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6
				.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGap(0, 733, Short.MAX_VALUE)
						.addGroup(gl_panel_6.createSequentialGroup().addContainerGap().addComponent(label_3).addGap(12)
								.addComponent(cbRotaContasReceber1, GroupLayout.PREFERRED_SIZE, 262,
										GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGap(0, 66, Short.MAX_VALUE)
				.addGroup(gl_panel_6.createSequentialGroup()
						.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_6.createSequentialGroup().addGap(5)
														.addComponent(label_3))
												.addComponent(cbRotaContasReceber1, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
				"Espelho com filtro de Rota e Vendedor para contas a receber", TitledBorder.LEADING, TitledBorder.TOP,
				null, new Color(51, 51, 51)));

		JLabel label_2 = new JLabel("Rota");

		cbRotaContasReceber2 = new JComboBox();

		JButton button_2 = new JButton("Gerar relatório");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					// ###############################################
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					try {
						cre.report("EspelhoContasReceberRotaVendedor.jasper", (Usuario) cbVendedorContasReceber.getSelectedItem(),
								(Rota) cbRotaContasReceber2.getSelectedItem(), null, null);
					} catch (JRException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possivel emitir o relatório solicitado");
				}

				// ###############################################
			}
		});
		button_2.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));

		JLabel label_1 = new JLabel("Vendedor");

		cbVendedorContasReceber = new JComboBox();
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_7
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_7.createSequentialGroup().addComponent(label_2).addGap(12)
								.addComponent(cbRotaContasReceber2, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_7.createSequentialGroup().addComponent(label_1).addGap(12).addComponent(
								cbVendedorContasReceber, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
				.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_7.createSequentialGroup().addGap(5)
														.addComponent(label_2))
												.addComponent(cbRotaContasReceber2, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_7.createSequentialGroup().addGap(5).addComponent(label_1))
								.addComponent(cbVendedorContasReceber, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_7.createSequentialGroup().addGap(20).addComponent(button_2,
								GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_7.setLayout(gl_panel_7);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
				"Espelho com filtro de Rota, Vendedor e Data para contas a receber", TitledBorder.LEADING,
				TitledBorder.TOP, null, new Color(51, 51, 51)));

		JLabel label_4 = new JLabel("Rota");

		cbRotaContasReceber3 = new JComboBox();

		JLabel label_5 = new JLabel("Vendedor");

		cbVendedorContasReceber3 = new JComboBox();

		JLabel lblVencInicial = new JLabel("Venc. inicial");

		dcInicialContasReceber = new JDateChooser();

		JLabel lblVencFinal = new JLabel("Venc. final");

		dcFinalContasReceber = new JDateChooser();

		JButton button_3 = new JButton("Gerar relatório");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					// ###############################################
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					try {
						cre.report("EspelhoContasReceberRotaVendedorData.jasper", (Usuario) cbVendedorContasReceber3.getSelectedItem(),
								(Rota) cbRotaContasReceber3.getSelectedItem(), dcInicialContasReceber.getDate(), dcFinalContasReceber.getDate());
					} catch (JRException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possivel emitir o relatório solicitado");
				}

				// ###############################################
				
			}
		});
		button_3.setIcon(
				new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addComponent(lblVencInicial, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(dcInicialContasReceber, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblVencFinal, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(dcFinalContasReceber, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_8.createSequentialGroup()
									.addComponent(label_4)
									.addGap(12)
									.addComponent(cbRotaContasReceber3, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_8.createSequentialGroup()
									.addComponent(label_5)
									.addGap(12)
									.addComponent(cbVendedorContasReceber3, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
							.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_8.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_8.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_8.createSequentialGroup()
							.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(dcFinalContasReceber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_8.createSequentialGroup()
							.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_8.createSequentialGroup()
									.addGap(5)
									.addComponent(label_4))
								.addComponent(cbRotaContasReceber3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_8.createSequentialGroup()
									.addGap(5)
									.addComponent(label_5))
								.addComponent(cbVendedorContasReceber3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(13)
							.addGroup(gl_panel_8.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblVencFinal)
								.addComponent(lblVencInicial)
								.addComponent(dcInicialContasReceber, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_8.setLayout(gl_panel_8);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
						.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_4.setLayout(gl_panel_4);
		panel_1.setLayout(gl_panel_1);
		
		JPanel panel_9 = new JPanel();
		tabbedPane.addTab("Vendas", null, panel_9, null);
		
		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new LineBorder(Color.BLACK));
		GroupLayout gl_panel_9 = new GroupLayout(panel_9);
		gl_panel_9.setHorizontalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_12, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_9.setVerticalGroup(
			gl_panel_9.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_9.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_12, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JLabel label_6 = new JLabel("Data inicial");
		label_6.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JDateChooser dcDataInicialVendas = new JDateChooser();
		
		JLabel label_7 = new JLabel("Data final");
		label_7.setFont(new Font("Dialog", Font.BOLD, 14));
		
		JDateChooser dcDataFinalVendas = new JDateChooser();
		
		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)),
						"Espelho com filtro de Rota, Vendedor e Data para produtos vendidos", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(51, 51, 51)));
		
		JLabel label_11 = new JLabel("Rota");
		
		cbRotaVendas = new JComboBox();
		
		JLabel label_12 = new JLabel("Vendedor");
		
		cbVendedorVendas = new JComboBox();
		
		JButton button_5 = new JButton("Gerar relatório");
		button_5.setIcon(new ImageIcon(JanelaEspelhos.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ###############################################
				try {
					ChamaRelatorioEspelho cre = new ChamaRelatorioEspelho();

					//try {
						cre.report("EspelhoVendas.jasper",
								(Usuario) cbVendedorVendas.getSelectedItem(),
								(Rota) cbRotaVendas.getSelectedItem(), dcDataInicialVendas.getDate(),
								dcDataFinalVendas.getDate());
					//} catch (JRException e1) {
						// TODO Auto-generated catch block
					//	e1.printStackTrace();
					//}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "ERRO! Não foi possível gerar o relatório solicitado");
				}

				// ###############################################

			}
		});
		GroupLayout gl_panel_13 = new GroupLayout(panel_13);
		gl_panel_13.setHorizontalGroup(
			gl_panel_13.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 733, Short.MAX_VALUE)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_13.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_13.createSequentialGroup()
							.addComponent(label_11)
							.addGap(12)
							.addComponent(cbRotaVendas, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_13.createSequentialGroup()
							.addComponent(label_12)
							.addGap(12)
							.addComponent(cbVendedorVendas, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 183, Short.MAX_VALUE)
							.addComponent(button_5, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_13.setVerticalGroup(
			gl_panel_13.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 106, Short.MAX_VALUE)
				.addGroup(gl_panel_13.createSequentialGroup()
					.addContainerGap(12, Short.MAX_VALUE)
					.addGroup(gl_panel_13.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_13.createSequentialGroup()
							.addGroup(gl_panel_13.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_13.createSequentialGroup()
									.addGap(5)
									.addComponent(label_11))
								.addComponent(cbRotaVendas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_13.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_13.createSequentialGroup()
									.addGap(5)
									.addComponent(label_12))
								.addComponent(cbVendedorVendas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(button_5, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_13.setLayout(gl_panel_13);
		GroupLayout gl_panel_12 = new GroupLayout(panel_12);
		gl_panel_12.setHorizontalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_12.createSequentialGroup()
							.addComponent(label_6, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(dcDataInicialVendas, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(label_7, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(dcDataFinalVendas, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 733, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panel_12.setVerticalGroup(
			gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_12.createSequentialGroup()
							.addGap(4)
							.addComponent(label_6, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(dcDataInicialVendas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_12.createSequentialGroup()
							.addGap(4)
							.addComponent(label_7, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(dcDataFinalVendas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(250, Short.MAX_VALUE))
		);
		panel_12.setLayout(gl_panel_12);
		panel_9.setLayout(gl_panel_9);
	}

	private void carregajcbUsuario() {
		
		cbVendedorVendasProdutos2.removeAllItems();
		cbVendedorContasReceber.removeAllItems();
		cbVendedorContasReceber3.removeAllItems();
		cbVendedorVendas.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuarios = consulta.getResultList();
			//trx.commit();

			// JOptionPane.showMessageDialog(null, listaUsuarios.size());

			for (int i = 0; i < listaUsuarios.size(); i++) {

				// JOptionPane.showMessageDialog(null, i);

				Usuario u = listaUsuarios.get(i);
				cbVendedorVendasProdutos2.addItem(u);
				cbVendedorContasReceber.addItem(u);
				cbVendedorContasReceber3.addItem(u);
				cbVendedorVendas.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox do vendedor! " + e);
			trx.commit();
		}

	}

	private void carregajcbRota() {

		cbRotaVendaProdutos1.removeAllItems();
		cbRotaVendaProdutos3.removeAllItems();
		cbRotaContasReceber1.removeAllItems();
		cbRotaContasReceber2.removeAllItems();
		cbRotaContasReceber3.removeAllItems();
		cbRotaVendas.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Rota");
			List<Rota> listaRotas = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaRotas.size(); i++) {

				Rota r = listaRotas.get(i);
				cbRotaVendaProdutos1.addItem(r);
				cbRotaVendaProdutos3.addItem(r);
				cbRotaContasReceber1.addItem(r);
				cbRotaContasReceber2.addItem(r);
				cbRotaContasReceber3.addItem(r);
				cbRotaVendas.addItem(r);

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox da rota! " + e);
			trx.commit();
		}

	}

	private void iniciaConexao() {

		//factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private Date dataInicialVendasProdutos() {
		Date retorno = null;

		retorno = dcDataInicialVendasProdutos.getDate();

		return retorno;
	}

	private Date dataFinalVendasProdutos() {
		Date retorno = null;

		retorno = dcDataFinalVendasProdutos.getDate();

		return retorno;
	}
}
