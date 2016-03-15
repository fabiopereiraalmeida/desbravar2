package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JTabbedPane;

public class JanelaConfiguracao extends JFrame {

	private JPanel contentPane;
	private JTextField tfIp;
	private JPanel panel_1;
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JTextField tfPortaHttp;
	private JLabel lblPorta;
	private JTextField tfRazaoSocial;
	private JTextField tfFantasia;
	private JTextField tfPortaMysql;
	private JTextField tfEndereco;
	private JTextField tfEnderecoNumero;
	private JTextField tfBairro;
	private JTextField tfCidade;
	private JFormattedTextField ftfCnpj;
	private JFormattedTextField ftfInscricaoEstadual;
	private JFormattedTextField ftfCep;
	private JFormattedTextField ftfTelefonePrincipal;
	private JFormattedTextField ftfTelefoneSecundario;
	private JTextField tfFrase;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaConfiguracao frame = new JanelaConfiguracao();
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
	public JanelaConfiguracao() {
		carregarJanela();

		try {
			lerArquivoXml();
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		/*
		 * try { tfIp.setText(lerArquivoIp());
		 * tfPortaHttp.setText(lerArquivoPorta()); } catch (Exception e) {
		 * JOptionPane.showMessageDialog(null,
		 * "Erro ao ler o arquivo de configuração!"); }
		 */
	}

	private void carregarJanela() {
		setTitle("Configurações");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 690, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		panel_1 = new JPanel();

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
				.addComponent(tabbedPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE).addGap(18)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Dados da empresa", null, panel, null);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Dados da empresa",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		JLabel label_5 = new JLabel("Razão Social:");

		tfRazaoSocial = new JTextField();
		tfRazaoSocial.setColumns(10);

		JLabel label_6 = new JLabel("Fantasia:");

		tfFantasia = new JTextField();
		tfFantasia.setText((String) null);
		tfFantasia.setColumns(10);

		JLabel label_7 = new JLabel("CNPJ:");

		ftfCnpj = new JFormattedTextField();

		JLabel lblInscrioEstadual = new JLabel("Inscrição Estadual:");

		ftfInscricaoEstadual = new JFormattedTextField();

		JLabel lblFrase = new JLabel("Frase:");

		tfFrase = new JTextField();
		tfFrase.setColumns(10);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false).addComponent(label_5)
						.addComponent(tfRazaoSocial, GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
						.addComponent(label_6)
						.addGroup(gl_panel_3.createSequentialGroup()
								.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addComponent(label_7)
										.addComponent(ftfCnpj, GroupLayout.PREFERRED_SIZE, 219,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
										.addComponent(ftfInscricaoEstadual, GroupLayout.PREFERRED_SIZE, 184,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblInscrioEstadual, GroupLayout.PREFERRED_SIZE, 153,
												GroupLayout.PREFERRED_SIZE)))
						.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, 519, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFrase).addComponent(tfFrase))
				.addContainerGap(74, Short.MAX_VALUE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap().addComponent(label_6)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_5)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_3.createSequentialGroup().addComponent(label_7)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(ftfCnpj,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblInscrioEstadual)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(ftfInscricaoEstadual, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblFrase)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(tfFrase, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap(78, Short.MAX_VALUE)));
		panel_3.setLayout(gl_panel_3);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE).addGap(15)));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addContainerGap()
								.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
								.addContainerGap()));
		panel.setLayout(gl_panel);

		JPanel panel_6 = new JPanel();
		tabbedPane.addTab("Contato", null, panel_6, null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Endere\u00E7o",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));

		tfEndereco = new JTextField();
		tfEndereco.setColumns(10);

		JLabel label = new JLabel("Endereço:");

		JLabel label_1 = new JLabel("Nº:");

		tfEnderecoNumero = new JTextField();
		tfEnderecoNumero.setColumns(10);

		tfBairro = new JTextField();
		tfBairro.setColumns(10);

		JLabel label_2 = new JLabel("Bairro:");

		tfCidade = new JTextField();
		tfCidade.setColumns(10);

		JLabel label_3 = new JLabel("Cidade:");

		JLabel label_4 = new JLabel("CEP:");

		ftfCep = new JFormattedTextField();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGap(0, 646, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
										.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
												.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, 492,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(label))
								.addGap(18)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(label_1)
										.addComponent(tfEnderecoNumero, GroupLayout.PREFERRED_SIZE, 78,
												GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_2.createSequentialGroup()
								.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
										.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, 285,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(label_2))
								.addGap(18)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
										.addComponent(tfCidade, GroupLayout.PREFERRED_SIZE, 296,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(label_3)))
								.addComponent(label_4)
								.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(25, Short.MAX_VALUE)));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGap(0, 209, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE).addComponent(label)
								.addComponent(label_1))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(tfEnderecoNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE).addComponent(label_2)
								.addComponent(label_3))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(tfCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_4)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(ftfCep, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(31, Short.MAX_VALUE)));
		panel_2.setLayout(gl_panel_2);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Telefones", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6
				.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_panel_6.createSequentialGroup()
								.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
										.addGroup(Alignment.LEADING,
												gl_panel_6.createSequentialGroup().addContainerGap().addComponent(
														panel_7, GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE))
						.addGroup(gl_panel_6.createSequentialGroup().addGap(15).addComponent(panel_2,
								GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))).addContainerGap()));
		gl_panel_6
				.setVerticalGroup(
						gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 191,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
										.addContainerGap()));

		JLabel lblTelefonePrincipal = new JLabel("Telefone principal:");

		ftfTelefonePrincipal = new JFormattedTextField();

		ftfTelefoneSecundario = new JFormattedTextField();

		JLabel lblTelefoneSecundario = new JLabel("Telefone secundario:");
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
								.addComponent(ftfTelefonePrincipal, GroupLayout.PREFERRED_SIZE, 171,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTelefonePrincipal))
				.addGap(18)
				.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addComponent(lblTelefoneSecundario)
						.addComponent(ftfTelefoneSecundario, GroupLayout.PREFERRED_SIZE, 183,
								GroupLayout.PREFERRED_SIZE))
						.addContainerGap(255, Short.MAX_VALUE)));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE).addComponent(lblTelefonePrincipal)
								.addComponent(lblTelefoneSecundario))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
						.addComponent(ftfTelefonePrincipal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(ftfTelefoneSecundario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
						.addContainerGap(37, Short.MAX_VALUE)));
		panel_7.setLayout(gl_panel_7);
		panel_6.setLayout(gl_panel_6);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Configuração do servidor", null, panel_4, null);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Configura\u00E7\u00E3o do servidor", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4
				.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_4
				.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 308, Short.MAX_VALUE)
								.addContainerGap()));

		JLabel lblIp = new JLabel("IP:");

		tfIp = new JTextField();
		tfIp.setColumns(10);

		lblPorta = new JLabel("Porta HTTP:");

		tfPortaHttp = new JTextField();
		tfPortaHttp.setColumns(10);

		tfPortaMysql = new JTextField();
		tfPortaMysql.setColumns(10);

		JLabel lblPortaMysql = new JLabel("Porta MySQL:");
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING).addComponent(lblIp)
								.addComponent(tfIp, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(tfPortaHttp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblPorta))
				.addGap(18)
				.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING).addComponent(lblPortaMysql).addComponent(
						tfPortaMysql, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(291, Short.MAX_VALUE)));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_panel_5.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_5.createSequentialGroup()
										.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblPorta).addComponent(lblPortaMysql))
								.addGap(6)
								.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
										.addComponent(tfPortaHttp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(tfPortaMysql, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_5.createSequentialGroup().addComponent(lblIp).addGap(6).addComponent(tfIp,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(45, Short.MAX_VALUE)));
		panel_5.setLayout(gl_panel_5);
		panel_4.setLayout(gl_panel_4);

		btnConfirmar = new JButton("Confirmar");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				criarArquivoXml();

				// escreverArquivoNomeEmpresa(tfFantasia.getText());
				// escreverArquivoIp(tfIp.getText());
				// escreverArquivoPorta(tfPortaHttp.getText());

				// Empresa.setIpServidor(lerArquivoIp());

				JOptionPane.showMessageDialog(null,
						"O sistema será fechado para que as configurações sejam aplicadas!");

				System.exit(0);

			}
		});
		btnConfirmar.setIcon(
				new ImageIcon(JanelaConfiguracao.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setIcon(new ImageIcon(
				JanelaConfiguracao.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap(399, Short.MAX_VALUE)
						.addComponent(btnCancelar).addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(btnConfirmar).addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap(18, Short.MAX_VALUE).addGroup(gl_panel_1
						.createParallelGroup(Alignment.TRAILING).addComponent(btnCancelar).addComponent(btnConfirmar))
				.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
	}

	private String lerArquivoNomeEmpresa() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String nome = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/nome.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\nome.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				nome = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo do nome da empresa!");
		}

		return nome;
	}

	private void escreverArquivoNomeEmpresa(String nome) {

		File file = null;

		// Permissao de acesso
		if ("Linux".equals(System.getProperty("os.name"))) { // Verifica se o
																// sistema é
																// linux
			file = new File("/opt/GrupoCaravela/software/nome.txt");
		} else {
			file = new File("c:\\GrupoCaravela\\software\\nome.txt");
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(nome);
			writer.flush(); // Cria o conteúdo do arquivo.
			writer.close(); // Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de configuração nome!!!");
		}
	}

	private String lerArquivoIp() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String ip = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/conf.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\conf.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				ip = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de ip do sistema!");
		}

		return ip;
	}

	private void escreverArquivoIp(String ip) {

		File file = null;

		// Permissao de acesso
		if ("Linux".equals(System.getProperty("os.name"))) { // Verifica se o
																// sistema é
																// linux
			file = new File("/opt/GrupoCaravela/software/conf.txt");
		} else {
			file = new File("c:\\GrupoCaravela\\software\\conf.txt");
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(ip);
			writer.flush(); // Cria o conteúdo do arquivo.
			writer.close(); // Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de ip configuração!!!");
		}
	}

	private String lerArquivoPorta() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String porta = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/porta.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\porta.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				porta = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de porta do sistema!");
		}

		return porta;
	}

	private void escreverArquivoPorta(String porta) {

		File file = null;

		// Permissao de acesso
		if ("Linux".equals(System.getProperty("os.name"))) { // Verifica se o
																// sistema é
																// linux
			file = new File("/opt/GrupoCaravela/software/porta.txt");
		} else {
			file = new File("c:\\GrupoCaravela\\software\\porta.txt");
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(porta);
			writer.flush(); // Cria o conteúdo do arquivo.
			writer.close(); // Fechando conexão e escrita do arquivo.
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar arquivo de porta configuração!!!");
		}
	}

	private void criarArquivoXml() {

		Document docConfig = new Document();

		Element configuracao = new Element("configuracao");

		Element empresa = new Element("empresa");

		Element fantasia = new Element("fantasia");
		fantasia.setText(tfFantasia.getText());
		Empresa.setNomeEpresa(tfFantasia.getText());

		Element razaoSoacial = new Element("razaoSocial");
		razaoSoacial.setText(tfRazaoSocial.getText());
		Empresa.setRazaoSocial(tfRazaoSocial.getText());

		Element cnpj = new Element("cnpj");
		cnpj.setText(ftfCnpj.getText());
		Empresa.setCnpj(ftfCnpj.getText());

		Element inscricaoEstadual = new Element("inscricaoEstadual");
		inscricaoEstadual.setText(ftfInscricaoEstadual.getText());
		Empresa.setInscricaoEstadua(ftfInscricaoEstadual.getText());

		Element endereco = new Element("endereco");
		endereco.setText(tfEndereco.getText());
		Empresa.setEndereco(tfEndereco.getText());

		Element enderecoNumero = new Element("enderecoNumero");
		enderecoNumero.setText(tfEnderecoNumero.getText());
		Empresa.setEnderecoNumero(tfEnderecoNumero.getText());

		Element bairro = new Element("bairro");
		bairro.setText(tfBairro.getText());
		Empresa.setBairro(tfBairro.getText());

		Element cidade = new Element("cidade");
		cidade.setText(tfCidade.getText());
		Empresa.setCidade(tfCidade.getText());

		Element cep = new Element("cep");
		cep.setText(ftfCep.getText());
		Empresa.setCep(ftfCep.getText());

		Element telefonePrincipal = new Element("telefonePrincipal");
		telefonePrincipal.setText(ftfTelefonePrincipal.getText());
		Empresa.setTelefonePrincipal(ftfTelefonePrincipal.getText());

		Element telefoneSecundario = new Element("telefoneSecundario");
		telefoneSecundario.setText(ftfTelefoneSecundario.getText());
		Empresa.setTelefoneSecundario(ftfTelefoneSecundario.getText());

		Element ipServidor = new Element("ipServidor");
		ipServidor.setText(tfIp.getText());
		Empresa.setIpServidor(tfIp.getText());

		Element portaHttp = new Element("portaHttp");
		portaHttp.setText(tfPortaHttp.getText());
		Empresa.setPortaHttpServidor(tfPortaHttp.getText());

		Element portaMysql = new Element("portaMysql");
		portaMysql.setText(tfPortaMysql.getText());
		Empresa.setPortaMysqlServidor(tfPortaMysql.getText());

		Element frase = new Element("frase");
		frase.setText(tfFrase.getText());
		Empresa.setFrase(tfFrase.getText());

		empresa.addContent(fantasia);
		empresa.addContent(razaoSoacial);
		empresa.addContent(cnpj);
		empresa.addContent(inscricaoEstadual);
		empresa.addContent(endereco);
		empresa.addContent(enderecoNumero);
		empresa.addContent(bairro);
		empresa.addContent(cidade);
		empresa.addContent(cep);
		empresa.addContent(telefonePrincipal);
		empresa.addContent(telefoneSecundario);
		empresa.addContent(ipServidor);
		empresa.addContent(portaHttp);
		empresa.addContent(portaMysql);
		empresa.addContent(frase);

		configuracao.addContent(empresa);

		docConfig.setRootElement(configuracao);

		XMLOutputter xout = new XMLOutputter();

		try {

			// Criando o arquivo de saida

			FileWriter arquivo = null;

			if ("Linux".equals(System.getProperty("os.name"))) { // Verifica se
																	// o sistema
																	// é linux
				arquivo = new FileWriter(new File("/opt/GrupoCaravela/conf.xml"));
			} else {
				arquivo = new FileWriter(new File("c:\\GrupoCaravela\\conf.xml"));
			}

			xout.output(docConfig, arquivo);

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	private void lerArquivoXml() {

		File arquivo = null;

		if ("Linux".equals(System.getProperty("os.name"))) { // Verifica se o
																// sistema é
																// linux

			try {
				arquivo = new File("/opt/GrupoCaravela/conf.xml");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Arquivo de configuração não encontrado");
			}

		} else {
			try {
				arquivo = new File("c:\\GrupoCaravela\\conf.xml");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Arquivo de configuração não encontrado");
			}

		}

		SAXBuilder builder = new SAXBuilder();

		Document doc = null;
		try {
			doc = builder.build(arquivo);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Element configuracao = (Element) doc.getRootElement();

		List conf = configuracao.getChildren();

		Iterator i = conf.iterator();

		while (i.hasNext()) {
			Element empresa = (Element) i.next();

			tfFantasia.setText(empresa.getChildText("fantasia"));
			tfRazaoSocial.setText(empresa.getChildText("razaoSocial"));
			ftfCnpj.setText(empresa.getChildText("cnpj"));
			ftfInscricaoEstadual.setText(empresa.getChildText("inscricaoEstadual"));
			tfEndereco.setText(empresa.getChildText("endereco"));
			tfEnderecoNumero.setText(empresa.getChildText("enderecoNumero"));
			tfBairro.setText(empresa.getChildText("bairro"));
			tfCidade.setText(empresa.getChildText("cidade"));
			ftfCep.setText(empresa.getChildText("cep"));
			ftfTelefonePrincipal.setText(empresa.getChildText("telefonePrincipal"));
			ftfTelefoneSecundario.setText(empresa.getChildText("telefoneSecundario"));
			tfIp.setText(empresa.getChildText("ipServidor"));
			tfPortaHttp.setText(empresa.getChildText("portaHttp"));
			tfPortaMysql.setText(empresa.getChildText("portaMysql"));
			tfFrase.setText(empresa.getChildText("frase"));

		}
	}
}
