package br.com.grupocaravela.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Cidade;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.Rota;
import br.com.grupocaravela.objeto.TelefoneCliente;
import br.com.grupocaravela.tablemodel.TableModelCliente;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;

public class JanelaCadastroClientes extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCliente tableModelCliente;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfFantasia;

	private Cliente cliente;
	private TelefoneCliente telefoneCliente;
	private EnderecoCliente enderecoCliente;

	private JTextField tfRazaoSocial;
	private JTextField tfApelido;
	private JTextField tfRg;
	private JTextField tfInscEstadual;
	private JTextField tfEmail;
	private JTextField tfLimiteCredito;
	private JTextField tfDataCadastro;
	private JTextPane tpObservacao;
	private JDateChooser dcDataNasc;
	private JComboBox cbFiltro;
	private JComboBox cbRota;
	private JComboBox cbSituacao;
	private JComboBox cbTipoTelefone;
	private JComboBox cbTelefonesCadastrados;
	private JComboBox cbEnderecosCadastrados;
	private JComboBox cbCidade;

	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private JTextField tfEndereco;
	private JTextField tfNumeroEndereco;
	private JTextField tfBairro;
	private JTextField tfComplento;
	private JTextField tfUf;

	private JFormattedTextField ftfCep;
	private JFormattedTextField ftfNumeroTelefone;
	private JFormattedTextField ftfCpf;
	private JFormattedTextField ftfCnpj;

	private JButton btnExcluir;
	private JButton btnDetalhes;
	private JButton btnNovo;
	private JButton btnBuscar;
	private JButton btnSalvarTelefone;
	private JButton btnSalvarEndereco;

	private JTabbedPane tabbedPane;
	
	private File arquivoFoto;
	private JLabel lblImagem;

	// private RepositorioCliente repositorioCliente = new RepositorioCliente();
	// private RepositorioRota repositorioRota;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroClientes frame = new JanelaCadastroClientes();
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
	public JanelaCadastroClientes() {

		criarJanela();
		carregarTableModel();
		
		tamanhoColunas();

		iniciaConexao();

		Thread thread = new ThreadBasica(); // Cria a tread para carregar o
											// comobox;
		thread.start(); // Inicia Thread

		// Evento ao abrir a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				tfLocalizar.requestFocus();
				super.windowActivated(e);
			}
		});
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

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
				manager = EntityManagerProducer.createEntityManager();
				trx = manager.getTransaction();
	}

	private void criarJanela() {

		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setTitle("Clientes");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 566, Short.MAX_VALUE)
		);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar
				.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

		tfLocalizar = new JTextField();
		tfLocalizar.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfLocalizar.selectAll();
			}
		});
		tfLocalizar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buscarNome();
				}
			}
		});
		tfLocalizar.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();

		tableLista = new JTable();
		tableLista.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableLista);
		tableLista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					limparCampos();

					cliente = tableModelCliente.getCliente(tableLista.getSelectedRow());

					carregarCampos(cliente);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(2, true);
					tabbedPane.setEnabledAt(0, false);

				}
			}
		});

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		cbFiltro = new JComboBox();
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Razão Social", "Fantasia", "Apelido" }));
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblLocalizar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblLocalizar)
						.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuscar))
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(13))
		);
		
				btnExcluir = new JButton("Excluir");
				btnExcluir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						cliente = tableModelCliente.getCliente(tableLista.getSelectedRow());

						Object[] options = { "Sim", "Não" };
						int i = JOptionPane.showOptionDialog(null,
								"ATENÇÃO!!! Confirma a Exclusão da cliente " + cliente.getFantasia() + "?", "Exclusão",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

						if (i == JOptionPane.YES_OPTION) {

							excluirCliente(cliente);
							limparTabela();
						}
					}
				});
				btnExcluir.setIcon(new ImageIcon(
						JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		
				btnDetalhes = new JButton("Detalhes");
				btnDetalhes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						limparCampos();

						cliente = tableModelCliente.getCliente(tableLista.getSelectedRow());

						carregarCampos(cliente);

						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(1, true);
						tabbedPane.setEnabledAt(2, true);
						tabbedPane.setEnabledAt(0, false);

					}
				});
				btnDetalhes.setIcon(
						new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
				btnNovo = new JButton("Novo");
				btnNovo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						limparCampos();

						String agora = formatDataHora.format(dataAtual());
						tfDataCadastro.setText(agora);

						cliente = new Cliente(); // Cria um novo cliente para ser salvo
													// no banco

						// carregajcbRota(); // Carrega o jComboBox da rota

						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(1, true);
						tabbedPane.setEnabledAt(2, true);
						tabbedPane.setEnabledAt(0, false);
						tfFantasia.requestFocus();

						Thread thread2 = new ThreadVerificaExistenciaRota(); // Cria a
																				// tread
																				// para
																				// carregar
																				// o
																				// comobox;
						thread2.start(); // Inicia Thread

					}
				});
				btnNovo.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8.setHorizontalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_8.createSequentialGroup()
					.addContainerGap(475, Short.MAX_VALUE)
					.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(btnDetalhes)
					.addGap(12)
					.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_8.setVerticalGroup(
			gl_panel_8.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_8.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_8.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExcluir)
						.addComponent(btnDetalhes)
						.addComponent(btnNovo))
					.addContainerGap())
		);
		panel_8.setLayout(gl_panel_8);
		panel.setLayout(gl_panel);
		

		//TipoCliente[] tiposClientes = TipoCliente.values();

		//Situacao[] situacoes = Situacao.values();
		
				JPanel panel_1 = new JPanel();
				tabbedPane.addTab("Detalhes", null, panel_1, null);
				
						JLabel lblId = new JLabel("id");
						
								tfId = new JTextField();
								tfId.setEditable(false);
								tfId.setColumns(10);
								
										JLabel lblFantasia = new JLabel("Fantasia");
										
												tfFantasia = new JTextField();
												
												tfFantasia.setColumns(10);
																
																		JLabel lblRazpSocial = new JLabel("Razão Social");
																		
																				JLabel lblApelido = new JLabel("Apelido");
																				
																						tfRazaoSocial = new JTextField();
																						
																						tfRazaoSocial.setColumns(10);
																						
																								tfApelido = new JTextField();
																								
																								tfApelido.setColumns(10);
																								
																										JLabel lblCnpj = new JLabel("CNPJ");
																										
																												JLabel lblNewLabel = new JLabel("RG");
																														
																																tfRg = new JTextField();
																																tfRg.setColumns(10);
																																
																																		JLabel lblCpf = new JLabel("CPF");
																																		
																																				JLabel lblInscEstadual = new JLabel("Insc. Estadual");
																																				
																																						tfInscEstadual = new JTextField();
																																						tfInscEstadual.setColumns(10);
																																						
																																								JLabel lblDataNasc = new JLabel("Data Nasc.");
																																								
																																										dcDataNasc = new JDateChooser();
																																										
																																												JLabel lblEmail = new JLabel("Email");
																																												
																																														tfEmail = new JTextField();
																																														
																																														tfEmail.setColumns(10);
																																																
																																																		JLabel lblSituao = new JLabel("Situação");
																																																		cbSituacao = new JComboBox();
																																																		cbSituacao.setModel(new DefaultComboBoxModel(new String[] {"Ativo", "Bloqueado"}));
																																																		
																																																				JLabel lblLimiteCrdito = new JLabel("Limite Crédito");
																																																				
																																																						tfLimiteCredito = new DecimalFormattedField(DecimalFormattedField.REAL);
																																																						tfLimiteCredito.setColumns(10);
																																																						
																																																								JLabel lblRota = new JLabel("Rota");
																																																								
																																																										cbRota = new JComboBox();
																																																										
																																																												JLabel lblDataCadastro = new JLabel("Data Cadastro");
																																																												
																																																														tfDataCadastro = new JTextField();
																																																														tfDataCadastro.setEditable(false);
																																																														tfDataCadastro.setColumns(10);
																																																														
																																																																JLabel lblObs = new JLabel("Obs.");
																																																																
																																																																		JPanel panel_2 = new JPanel();
																																																																		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));
																																																																		
																																																																		ftfCpf = new JFormattedTextField();
																																																																		ftfCpf.addFocusListener(new FocusAdapter() {
																																																																			@Override
																																																																			public void focusGained(FocusEvent e) {
																																																																				ftfCpf.selectAll();
																																																																			}
																																																																		});
																																																																		
																																																																		try {
																																																																			MaskFormatter maskCpf = new MaskFormatter("###.###.###-##");
																																																																			maskCpf.install(ftfCpf);
																																																																		} catch (Exception e) {
																																																																			// TODO: handle exception
																																																																		}
																																																																		
																																																																		ftfCnpj = new JFormattedTextField();
																																																																		ftfCnpj.addFocusListener(new FocusAdapter() {
																																																																			@Override
																																																																			public void focusGained(FocusEvent e) {
																																																																				ftfCnpj.selectAll();
																																																																			}
																																																																		});
																																																																		
																																																																		try {
																																																																			MaskFormatter maskCnpj = new MaskFormatter("##.###.###/####-##");
																																																																			maskCnpj.install(ftfCnpj);
																																																																		} catch (Exception e) {
																																																																			// TODO: handle exception
																																																																		}
																																																																		
																																																																		JPanel panel_6 = new JPanel();
																																																																		panel_6.setBorder(new TitledBorder(null, "Imagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
																																																																		
																																																																		lblImagem = new JLabel("");
																																																																		lblImagem.addMouseListener(new MouseAdapter() {
																																																																			@Override
																																																																			public void mouseClicked(MouseEvent e) {
																																																																				
																																																																				try {
																																																																					buscarImagemLocal();
																																																																				} catch (
																																																																						IOException
																																																																						| InterruptedException e1) {
																																																																					// TODO Auto-generated catch block
																																																																					e1.printStackTrace();
																																																																				}
																																																																			}
																																																																		});
																																																																		lblImagem.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
																																																																		lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
																																																																		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
																																																																		gl_panel_6.setHorizontalGroup(
																																																																			gl_panel_6.createParallelGroup(Alignment.TRAILING)
																																																																				.addGap(0, 284, Short.MAX_VALUE)
																																																																				.addGroup(gl_panel_6.createSequentialGroup()
																																																																					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																																																																					.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
																																																																					.addContainerGap())
																																																																		);
																																																																		gl_panel_6.setVerticalGroup(
																																																																			gl_panel_6.createParallelGroup(Alignment.LEADING)
																																																																				.addGap(0, 284, Short.MAX_VALUE)
																																																																				.addGroup(gl_panel_6.createSequentialGroup()
																																																																					.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
																																																																					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
																																																																		);
																																																																		panel_6.setLayout(gl_panel_6);
																																																																		
																																																																		JPanel panel_7 = new JPanel();
																																																																		panel_7.setBorder(new LineBorder(Color.DARK_GRAY));
																																																																		
																																																																		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
																																																																		gl_panel_1.setHorizontalGroup(
																																																																			gl_panel_1.createParallelGroup(Alignment.TRAILING)
																																																																				.addGroup(gl_panel_1.createSequentialGroup()
																																																																					.addContainerGap()
																																																																					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addComponent(lblId)
																																																																							.addPreferredGap(ComponentPlacement.RELATED)
																																																																							.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
																																																																							.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																							.addComponent(lblFantasia)
																																																																							.addPreferredGap(ComponentPlacement.RELATED)
																																																																							.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, 481, GroupLayout.PREFERRED_SIZE))
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblRazpSocial)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, 433, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblApelido)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(tfApelido, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblDataNasc)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(dcDataNasc, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblCnpj)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(ftfCnpj, GroupLayout.PREFERRED_SIZE, 194, GroupLayout.PREFERRED_SIZE)
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addComponent(lblCpf)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(ftfCpf, GroupLayout.PREFERRED_SIZE, 183, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblInscEstadual)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(tfInscEstadual, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addComponent(lblNewLabel)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(tfRg, GroupLayout.PREFERRED_SIZE, 163, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblEmail)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, 403, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblRota)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(cbRota, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE))
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addComponent(lblSituao)
																																																																									.addPreferredGap(ComponentPlacement.RELATED)
																																																																									.addComponent(cbSituacao, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)))
																																																																							.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
																																																																							.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE))
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addComponent(lblLimiteCrdito)
																																																																							.addPreferredGap(ComponentPlacement.RELATED)
																																																																							.addComponent(tfLimiteCredito, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																							.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																							.addComponent(lblDataCadastro)
																																																																							.addPreferredGap(ComponentPlacement.RELATED)
																																																																							.addComponent(tfDataCadastro, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addComponent(lblObs)
																																																																							.addPreferredGap(ComponentPlacement.RELATED)
																																																																							.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 814, Short.MAX_VALUE)))
																																																																					.addContainerGap())
																																																																		);
																																																																		gl_panel_1.setVerticalGroup(
																																																																			gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																				.addGroup(gl_panel_1.createSequentialGroup()
																																																																					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addContainerGap()
																																																																							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																								.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																								.addComponent(lblId)
																																																																								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																								.addComponent(lblFantasia))
																																																																							.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																								.addGroup(gl_panel_1.createSequentialGroup()
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblRazpSocial)
																																																																										.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblApelido)
																																																																										.addComponent(tfApelido, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																										.addComponent(lblDataNasc)
																																																																										.addComponent(dcDataNasc, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblSituao)
																																																																										.addComponent(cbSituacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addGap(16)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblInscEstadual)
																																																																										.addComponent(tfInscEstadual, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																										.addComponent(lblNewLabel)
																																																																										.addComponent(tfRg, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addGap(79)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblEmail)
																																																																										.addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																									.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																										.addComponent(lblRota)
																																																																										.addComponent(cbRota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
																																																																								.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)))
																																																																						.addGroup(gl_panel_1.createSequentialGroup()
																																																																							.addGap(208)
																																																																							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																								.addComponent(lblCnpj)
																																																																								.addComponent(ftfCnpj, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																								.addComponent(lblCpf)
																																																																								.addComponent(ftfCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
																																																																					.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																																																																						.addComponent(lblLimiteCrdito)
																																																																						.addComponent(tfLimiteCredito, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																						.addComponent(lblDataCadastro)
																																																																						.addComponent(tfDataCadastro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
																																																																					.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
																																																																						.addComponent(lblObs)
																																																																						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
																																																																					.addPreferredGap(ComponentPlacement.UNRELATED)
																																																																					.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
																																																																					.addContainerGap())
																																																																		);
																																																																		
																																																																				JButton btnCancelar = new JButton("Cancelar");
																																																																				btnCancelar.addActionListener(new ActionListener() {
																																																																					public void actionPerformed(ActionEvent e) {

																																																																						tabbedPane.setSelectedIndex(0);
																																																																						tabbedPane.setEnabledAt(1, false);
																																																																						tabbedPane.setEnabledAt(2, false);
																																																																						tabbedPane.setEnabledAt(0, true);
																																																																						limparCampos();
																																																																						tfLocalizar.requestFocus();
																																																																						btnSalvarEndereco.setEnabled(false);

																																																																					}
																																																																				});
																																																																				btnCancelar.setIcon(new ImageIcon(
																																																																						JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
																																																																		
																																																																				JButton btnSalvar = new JButton("Salvar");
																																																																				btnSalvar.addActionListener(new ActionListener() {
																																																																					public void actionPerformed(ActionEvent e) {

																																																																						if (verificaCamposVazios() == false) {
																																																																							salvarCliente(cliente);
																																																																							try {
																																																																								salvarImagem();
																																																																							} catch (Exception e1) {
																																																																								// TODO Auto-generated catch block
																																																																								e1.printStackTrace();
																																																																							}
																																																																							tabbedPane.setSelectedIndex(0);
																																																																							tabbedPane.setEnabledAt(1, false);
																																																																							tabbedPane.setEnabledAt(2, false);
																																																																							tabbedPane.setEnabledAt(0, true);
																																																																							tfLocalizar.requestFocus();
																																																																						}

																																																																						/*
																																																																						 * if (tfRazaoSocial.getText().isEmpty()) {
																																																																						 * JOptionPane.showMessageDialog(null, "Informe a Razão Social!"
																																																																						 * ); tfRazaoSocial.requestFocus();
																																																																						 * 
																																																																						 * } else { if (tfFantasia.getText().isEmpty()) {
																																																																						 * JOptionPane.showMessageDialog(null, "Informe a Fantasia!");
																																																																						 * tfFantasia.requestFocus();
																																																																						 * 
																																																																						 * } else { if (tfApelido.getText().isEmpty()) {
																																																																						 * JOptionPane.showMessageDialog(null, "Informe o Apelido");
																																																																						 * tfApelido.requestFocus();
																																																																						 * 
																																																																						 * } else { if (tfLimiteCredito.getText().isEmpty()) {
																																																																						 * JOptionPane.showMessageDialog(null,
																																																																						 * "Informe o Limite de Crédito");
																																																																						 * tfLimiteCredito.requestFocus();
																																																																						 * 
																																																																						 * } else {
																																																																						 * 
																																																																						 * 
																																																																						 * } } } }
																																																																						 */
																																																																					}

																																																																				});
																																																																				btnSalvar.setIcon(
																																																																						new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
																																																																		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
																																																																		gl_panel_7.setHorizontalGroup(
																																																																			gl_panel_7.createParallelGroup(Alignment.LEADING)
																																																																				.addGroup(Alignment.TRAILING, gl_panel_7.createSequentialGroup()
																																																																					.addContainerGap(218, Short.MAX_VALUE)
																																																																					.addComponent(btnCancelar)
																																																																					.addGap(6)
																																																																					.addComponent(btnSalvar)
																																																																					.addContainerGap())
																																																																		);
																																																																		gl_panel_7.setVerticalGroup(
																																																																			gl_panel_7.createParallelGroup(Alignment.LEADING)
																																																																				.addGroup(Alignment.TRAILING, gl_panel_7.createSequentialGroup()
																																																																					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																																																																					.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
																																																																						.addComponent(btnCancelar)
																																																																						.addComponent(btnSalvar))
																																																																					.addContainerGap())
																																																																		);
																																																																		panel_7.setLayout(gl_panel_7);
																																																																		
																																																																				tpObservacao = new JTextPane();
																																																																				
																																																																				GroupLayout gl_panel_2 = new GroupLayout(panel_2);
																																																																				gl_panel_2.setHorizontalGroup(
																																																																					gl_panel_2.createParallelGroup(Alignment.LEADING)
																																																																						.addComponent(tpObservacao, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
																																																																				);
																																																																				gl_panel_2.setVerticalGroup(
																																																																					gl_panel_2.createParallelGroup(Alignment.LEADING)
																																																																						.addComponent(tpObservacao, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
																																																																				);
																																																																				panel_2.setLayout(gl_panel_2);
																																																																				panel_1.setLayout(gl_panel_1);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Contato / Endereço", null, panel_3, null);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Telefones", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblTipo_1 = new JLabel("Tipo");

		
		cbTipoTelefone = new JComboBox();
		cbTipoTelefone.setModel(new DefaultComboBoxModel(new String[] {"FIXO", "CELULAR", "FAX"}));

		JLabel lblNumero = new JLabel("Numero:");

		ftfNumeroTelefone = new JFormattedTextField();
		ftfNumeroTelefone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				btnSalvarTelefone.setEnabled(true);
			}
		});
		ftfNumeroTelefone.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				String telefone = ftfNumeroTelefone.getText();

					try {
						MaskFormatter masktel8 = new MaskFormatter("(##) ####-####");
						MaskFormatter masktel9 = new MaskFormatter("(##) #####-####");
						//MaskFormatter masktel11 = new MaskFormatter("#### ###-####");

						if (telefone.length() == 10) {
							masktel8.install(ftfNumeroTelefone);
							ftfNumeroTelefone.setText(telefone);
							//btnSalvarTelefone.setEnabled(true);
						} else if (telefone.length() == 11) {
							masktel9.install(ftfNumeroTelefone);
							ftfNumeroTelefone.setText(telefone);
							//btnSalvarTelefone.setEnabled(true);						
						} else if (telefone.length() == 0) {
							btnSalvarTelefone.setEnabled(false);
						}

					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Erro!" + e2);
					}
				}			
		});

		btnSalvarTelefone = new JButton("Salvar");
		btnSalvarTelefone.setEnabled(false);
		btnSalvarTelefone.setIcon(
				new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		btnSalvarTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (ftfNumeroTelefone.getText() != "") {
					acaoSalvarTelefone(cliente);
					carregarCbTelefonesCadastrados(cliente);
					btnSalvarTelefone.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "Antes de incluir, informe o numero de telefone!");
				}

			}
		});

		JLabel lblTelefonesCadastrados = new JLabel("Telefones cadastrados");

		cbTelefonesCadastrados = new JComboBox();
		cbTelefonesCadastrados.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				carregarTelefoneSelecionado((TelefoneCliente) cbTelefonesCadastrados.getSelectedItem());
			}
		});

		JButton btnExcluirTelefone = new JButton("Excluir");
		btnExcluirTelefone.setIcon(new ImageIcon(
				JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		btnExcluirTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão do telefone selecionado?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirTelefone((TelefoneCliente) cbTelefonesCadastrados.getSelectedItem());
					limparCamposTelefone();

				}
			}

		});
		
		JButton btnNovo_1 = new JButton("Novo");
		btnNovo_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				ftfNumeroTelefone.setFormatterFactory(null);
				ftfNumeroTelefone.setText("");
				ftfNumeroTelefone.requestFocus();
			}
		});
		btnNovo_1.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(lblTelefonesCadastrados)
							.addGap(12)
							.addComponent(cbTelefonesCadastrados, GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
							.addComponent(btnNovo_1, GroupLayout.PREFERRED_SIZE, 219, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_panel_4.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblTipo_1)
							.addGap(12)
							.addComponent(cbTipoTelefone, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNumero)
							.addGap(12)
							.addComponent(ftfNumeroTelefone, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
							.addComponent(btnSalvarTelefone)
							.addGap(6)
							.addComponent(btnExcluirTelefone)))
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(10)
							.addComponent(lblTelefonesCadastrados))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbTelefonesCadastrados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnNovo_1))))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(lblTipo_1))
						.addComponent(cbTipoTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(5)
							.addComponent(lblNumero))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGap(3)
							.addComponent(ftfNumeroTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSalvarTelefone)
						.addComponent(btnExcluirTelefone)))
		);
		panel_4.setLayout(gl_panel_4);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Endere\u00E7os", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JLabel lblEndereosCadastrados = new JLabel("Endereços cadastrados");

		cbEnderecosCadastrados = new JComboBox();
		cbEnderecosCadastrados.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				carregarEnderecoSelecionado((EnderecoCliente) cbEnderecosCadastrados.getSelectedItem());
			}
		});

		JLabel lblEnd = new JLabel("End.");

		tfEndereco = new JTextField();
		
		tfEndereco.setColumns(10);

		JLabel lblN = new JLabel("Nº");

		tfNumeroEndereco = new JTextField();
		tfNumeroEndereco.setColumns(10);

		JLabel lblCidade = new JLabel("Cidade");

		cbCidade = new JComboBox();

		JLabel lblNewLabel_1 = new JLabel("Bairro");

		tfBairro = new JTextField();
		
		tfBairro.setColumns(10);

		JLabel lblComplemento = new JLabel("Complemento");

		tfComplento = new JTextField();
		
		tfComplento.setColumns(10);

		JLabel lblUf = new JLabel("UF");

		tfUf = new JTextField();
		tfUf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfUf.setText(tfUf.getText().toUpperCase());
			}
		});
		tfUf.setColumns(10);

		JLabel lblCep = new JLabel("CEP");

		ftfCep = new JFormattedTextField();
		ftfCep.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				ftfCep.selectAll();
			}
		});

		try {
			MaskFormatter maskCep = new MaskFormatter("##.###-###");
			maskCep.install(ftfCep);
		} catch (Exception e) {
			// TODO: handle exception
		}

		btnSalvarEndereco = new JButton("Salvar endereço");
		btnSalvarEndereco.setEnabled(false);
		btnSalvarEndereco.setIcon(
				new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		btnSalvarEndereco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				acaoAdicionarEndereco(cliente);
				carregarCbEnderecosCadastrados(cliente);
				btnSalvarEndereco.setEnabled(false);
				
			}
		});

		JButton btnExcluirEndereco = new JButton("Excluir endereço");
		btnExcluirEndereco.setIcon(new ImageIcon(
				JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		btnExcluirEndereco.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão do endereço selecionado?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirEndereco((EnderecoCliente) cbEnderecosCadastrados.getSelectedItem());
					limparCamposEndereco();

				}
			}
		});
		
		JButton btnNovoEndereo = new JButton("Novo endereço");
		btnNovoEndereo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				btnSalvarEndereco.setEnabled(true);
				limparCamposEndereco();
				tfEndereco.requestFocus();
				
			}
		});
		btnNovoEndereo.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
		JButton btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		btnVoltar.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/icones/voltar_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addContainerGap(201, Short.MAX_VALUE)
					.addComponent(btnVoltar)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNovoEndereo)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnExcluirEndereco)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnSalvarEndereco)
					.addContainerGap())
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEndereosCadastrados)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbEnderecosCadastrados, GroupLayout.PREFERRED_SIZE, 564, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(94, Short.MAX_VALUE))
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEnd)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, 487, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblN)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfNumeroEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(152, Short.MAX_VALUE))
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblComplemento)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfComplento, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, 246, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(228, Short.MAX_VALUE))
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCidade)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbCidade, GroupLayout.PREFERRED_SIZE, 255, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblUf)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(415, Short.MAX_VALUE))
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCep)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(604, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEndereosCadastrados)
						.addComponent(cbEnderecosCadastrados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEnd)
						.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblN)
						.addComponent(tfNumeroEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblComplemento)
						.addComponent(tfComplento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel_1)
						.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCidade)
						.addComponent(cbCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUf)
						.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCep)
						.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnVoltar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnSalvarEndereco, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnExcluirEndereco, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNovoEndereo, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel_5.setLayout(gl_panel_5);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
						.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
					.addGap(12))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		contentPane.setLayout(gl_contentPane);

	}

	class ThreadBasica extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {

			if (verificaExistenciaCidade() == false) {
				JOptionPane.showMessageDialog(null,
						"ATENÇÃO! Não extiste nunhuma cidade cadastrada. Antes de continuar cadastre uma cidade.");
				btnDetalhes.setEnabled(false);
				btnExcluir.setEnabled(false);
				btnNovo.setEnabled(false);
				btnBuscar.setEnabled(false);
				tabbedPane.setEnabled(false);
				tfLocalizar.setEnabled(false);
				cbFiltro.setEnabled(false);
				tableLista.setEnabled(false);

			}

			carregajcbRota();
			carregajcbCidade();

		}
	}

	class ThreadVerificaExistenciaRota extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {
			// carregajcbRota();
			if (verificaExistenciaRota() == false) {
				JOptionPane.showMessageDialog(null,
						"Antes de cadastrar qualquer cliente é necessario o cadastro da rota!");
			}
		}
	}

	private void carregarTableModel() {
		this.tableModelCliente = new TableModelCliente();
		this.tableLista.setModel(tableModelCliente);
	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Cliente");
			List<Cliente> listaClientes = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaClientes.size(); i++) {
				Cliente c = listaClientes.get(i);
				tableModelCliente.addCliente(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelCliente.removecliente(0);
		}
	}

	private void buscarNome() {

		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaCadastroClientes.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				if (tfLocalizar.getText().isEmpty()) {
					buscarTodos();
				} else {
					filtrar(cbFiltro.getSelectedIndex());
				}

				// ######################FIM METODO A SER
				// EXECUTADO##############################
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				tr.start();
				// .....
				EsperaLista espera = new EsperaLista();
				espera.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
				espera.setUndecorated(true);
				espera.setVisible(true);
				espera.setLocationRelativeTo(null);
			
				try {
					tr.join();
					espera.dispose();

				} catch (InterruptedException ex) {
					// Logger.getLogger(MenuView.class.getName()).log(Level.SEVERE,
					// null, ex);
				}
			}
		}).start();

		// ###############################################]

	}

	private void salvarCliente(Cliente c) {
		try {

			c.setFantasia(tfFantasia.getText());
			c.setRazaoSocial(tfRazaoSocial.getText());
			c.setApelido(tfApelido.getText());
			try {
				c.setInscricaoEstadual(tfInscEstadual.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				c.setEmail(tfEmail.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				c.setCnpj(ftfCnpj.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				c.setRg(tfRg.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				c.setCpf(ftfCpf.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			c.setLimiteCredito(Double.parseDouble(tfLimiteCredito.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			try {
				c.setDataNascimento(dcDataNasc.getDate());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				c.setObservacao(tpObservacao.getText());
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (c.getDataCadastro() == null) {
				c.setDataCadastro(dataAtual());
			}
			c.setRota((Rota) cbRota.getSelectedItem());
			
			if (cbSituacao.getSelectedItem().toString().equals("Ativo")) {
				c.setAtivo(true);
			}else{
				c.setAtivo(false);
			}

			//c.setSituacao((Situacao) cbSituacao.getSelectedItem());
			//c.setTipoCliente((TipoCliente) cbTipo.getSelectedItem());

			trx.begin();
			manager.persist(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cliente foi salva com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "O cliente " + c.getRazaoSocial() + " com id nº " + c.getId() + "foi salvo", dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(Cliente c) {

		tfId.setText(c.getId().toString());
		tfRazaoSocial.setText(c.getRazaoSocial());
		tfFantasia.setText(c.getFantasia());
		tfApelido.setText(c.getApelido());
		try {
			tfInscEstadual.setText(c.getInscricaoEstadual());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			tfEmail.setText(c.getEmail());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			ftfCnpj.setText(c.getCnpj());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			tfRg.setText(c.getRg());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			ftfCpf.setText(c.getCpf());
		} catch (Exception e) {
			// TODO: handle exception
		}
		tfLimiteCredito.setText(c.getLimiteCredito().toString());
		tfDataCadastro.setText(formatDataHora.format(c.getDataCadastro()));
		try {
			tpObservacao.setText(c.getObservacao());
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			dcDataNasc.setDate(c.getDataNascimento());
		} catch (Exception e) {
			// TODO: handle exception
		}

		//cbSituacao.setSelectedItem(c.getSituacao());
		//cbTipo.setSelectedItem(c.getTipoCliente());

		cbRota.setSelectedItem(c.getRota());

		try {
			carregarCbTelefonesCadastrados(c);
		} catch (Exception e) {
			// TODO: handle exception
		}

		try {
			carregarCbEnderecosCadastrados(c);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		buscarImagemServidor();
	}

	private void limparCampos() {
		
		tfId.setText("");
		tfRazaoSocial.setText("");
		tfFantasia.setText("");
		tfApelido.setText("");
		tfInscEstadual.setText("");
		tfEmail.setText("");
		ftfCnpj.setText("");
		tfRg.setText("");
		ftfCpf.setText("");
		tfLimiteCredito.setText("0");
		tfDataCadastro.setText("");
		tpObservacao.setText("");
		dcDataNasc.setDate(dataAtual());
		cbTelefonesCadastrados.removeAllItems();
		cbTipoTelefone.setSelectedIndex(0);
		ftfNumeroTelefone.setText("");
		cbEnderecosCadastrados.removeAllItems();
		tfEndereco.setText("");
		tfNumeroEndereco.setText("");
		tfBairro.setText("");
		cbCidade.removeAllItems();
		tfUf.setText("");
		ftfCep.setText("");
		
		cbRota.setSelectedIndex(0);
		
		
		lblImagem.setIcon(new ImageIcon(JanelaCadastroClientes.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
	}

	private boolean verificaExistenciaRota() {
		boolean retorno = false;

		try {
			//trx.begin();
			List<Rota> listaRotas = new ArrayList();
			Query consulta = manager.createQuery("from Rota");
			listaRotas = consulta.getResultList();
			//trx.commit();

			if (listaRotas.size() > 0) {
				retorno = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao verificar existencia de rotas! " + e);
		}

		return retorno;

	}

	private void excluirCliente(Cliente c) {
		
		String n = c.getRazaoSocial();
		Long id = c.getId();
		
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cliente foi removida com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "O cliente " + n + " com id nº " + id + "foi excluido", dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void buscarTodos() {
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Cliente order by razao_social");
			List<Cliente> listaClientes = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaClientes.size(); i++) {
				Cliente c = listaClientes.get(i);
				tableModelCliente.addCliente(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
		}
	}

	private void filtrar(int op) {

		switch (op) {
		case 0:
			try {

				//trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where razaoSocial like '%" + tfLocalizar.getText() + "%' order by razao_social");
				List<Cliente> listaClientes = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		case 1:
			try {

				//trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where fantasia like '%" + tfLocalizar.getText() + "%' order by razao_social");
				List<Cliente> listaClientes = consulta.getResultList();
				//trx.commit();
				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		case 2:
			try {

				//trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where apelido like '%" + tfLocalizar.getText() + "%' order by razao_social");
				List<Cliente> listaClientes = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		default:

		}
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private boolean verificaCamposVazios() {

		boolean retorno = false;

		if (tfRazaoSocial.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe a Razão Social!");
			tfRazaoSocial.requestFocus();
			retorno = true;
		} else if (tfFantasia.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe a Fantasia!");
			tfFantasia.requestFocus();
			retorno = true;
		} else if (tfApelido.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o Apelido");
			tfApelido.requestFocus();
			retorno = true;
		} else if (tfLimiteCredito.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o Limite de Crédito");
			tfLimiteCredito.requestFocus();
			retorno = true;
		}

		return retorno;
	}

	private void carregajcbRota() {

		cbRota.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Rota ORDER BY nome ASC");
			List<Rota> listaRotas = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaRotas.size(); i++) {

				Rota r = listaRotas.get(i);
				cbRota.addItem(r);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento da rota selecionada! " + e);
		}
	}

	private void carregajcbCidade() {

		cbCidade.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Cidade ORDER BY nome ASC");
			List<Cidade> listaCidades = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaCidades.size(); i++) {

				Cidade c = listaCidades.get(i);
				cbCidade.addItem(c);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento das cidades! " + e);
		}
	}

	private void acaoSalvarTelefone(Cliente c) {

		telefoneCliente = new TelefoneCliente();

		try {
			telefoneCliente.setTelefone(ftfNumeroTelefone.getText());
			telefoneCliente.setTipoTelefone(cbTipoTelefone.getSelectedItem().toString());
			//telefoneCliente.setTipoTelefone((TipoTelefone) cbTipoTelefone.getSelectedItem());
			telefoneCliente.setCliente(c);

			trx.begin();
			manager.persist(telefoneCliente);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Telefone adicionado com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao adicionar o telefone!");
		}

	}

	private void acaoAdicionarEndereco(Cliente c) {

		enderecoCliente = new EnderecoCliente();

		try {
			enderecoCliente.setBairro(tfBairro.getText());
			enderecoCliente.setCep(ftfCep.getText());
			enderecoCliente.setCidade((Cidade) cbCidade.getSelectedItem());
			enderecoCliente.setComplemento(tfComplento.getText());
			enderecoCliente.setEndereco(tfEndereco.getText());
			enderecoCliente.setNumero(tfNumeroEndereco.getText());
			enderecoCliente.setUf(tfUf.getText());
			enderecoCliente.setCliente(c);

			trx.begin();
			manager.persist(enderecoCliente);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Endereço adicionado com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao adicionar o endereço!");
		}

	}

	private void carregarCbTelefonesCadastrados(Cliente c) {

		cbTelefonesCadastrados.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from TelefoneCliente where cliente_id like '" + c.getId() + "'");
			List<TelefoneCliente> listaTelefonesClientes = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaTelefonesClientes.size(); i++) {

				TelefoneCliente t = listaTelefonesClientes.get(i);
				cbTelefonesCadastrados.addItem(t);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos telefones do cliente! " + e);
		}
	}

	private void carregarCbEnderecosCadastrados(Cliente c) {

		cbEnderecosCadastrados.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from EnderecoCliente where cliente_id like '" + c.getId() + "'");
			List<EnderecoCliente> listaEnderecoCliente = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaEnderecoCliente.size(); i++) {

				EnderecoCliente e = listaEnderecoCliente.get(i);
				cbEnderecosCadastrados.addItem(e);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos endereços do cliente! " + e);
		}
	}

	private boolean verificaExistenciaCidade() {
		boolean retorno = true;

		//trx.begin();
		Query consulta = manager.createQuery("from Cidade");
		List<Cidade> listaCidades = consulta.getResultList();
		//trx.commit();

		if (listaCidades.isEmpty()) {
			retorno = false;
		}
		return retorno;
	}

	private void carregarTelefoneSelecionado(TelefoneCliente tc) {

		try {
			cbTipoTelefone.setSelectedItem(tc.getTipoTelefone());
			ftfNumeroTelefone.setText(tc.getTelefone());
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "Erro ao carregar o telefone
			// selecionado!" + e);
		}

	}

	private void carregarEnderecoSelecionado(EnderecoCliente ec) {

		try {
			cbCidade.setSelectedItem(ec.getCidade());
			tfEndereco.setText(ec.getEndereco());
			tfNumeroEndereco.setText(ec.getNumero());
			tfComplento.setText(ec.getComplemento());
			tfBairro.setText(ec.getBairro());
			tfUf.setText(ec.getUf());
			ftfCep.setText(ec.getCep());
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "Erro ao carregar o endereço
			// selecionado!" + e);
		}

	}

	private void excluirTelefone(TelefoneCliente tc) {

		try {
			trx.begin();
			manager.remove(tc);
			trx.commit();

			carregarCbTelefonesCadastrados(cliente);

			JOptionPane.showMessageDialog(null, "Telefone excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir o telefone! " + e);
		}

	}

	private void excluirEndereco(EnderecoCliente ec) {

		try {
			trx.begin();
			manager.remove(ec);
			trx.commit();

			carregarCbEnderecosCadastrados(cliente);

			JOptionPane.showMessageDialog(null, "Endereço excluido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao excluir o endereço! " + e);
		}

	}

	private void limparCamposTelefone() {
		ftfNumeroTelefone.setText("");
		cbTipoTelefone.setSelectedIndex(0);
	}

	private void limparCamposEndereco() {
		tfEndereco.setText("");
		tfNumeroEndereco.setText("");
		tfComplento.setText("");
		tfBairro.setText("");
		cbCidade.setSelectedIndex(0);
		tfUf.setText("");
		ftfCep.setText("");
	}
	
	private void buscarImagemLocal() throws IOException, InterruptedException {

		BufferedImage tmp = null;
		BufferedImage newImage = null;

		String sistema = System.getProperty("os.name");

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Salvar imagem...");

		int opcao = fileChooser.showSaveDialog(null);

		if (opcao == JFileChooser.APPROVE_OPTION) {

			File url = fileChooser.getSelectedFile();

			try {

				tmp = ImageIO.read(url);

				int w = 250;
				int h = 250;

				newImage = new BufferedImage(w, h, tmp.getType());
				Graphics2D g2d = newImage.createGraphics();
				g2d.setComposite(AlphaComposite.Src);
				g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.drawImage(tmp, 0, 0, w, h, null);
				g2d.dispose();

				lblImagem.setIcon(new ImageIcon(newImage));

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		if ("Linux".equals(sistema)) {
			arquivoFoto = new File("/opt/GrupoCaravela/software/imagens/cliente/img.png");
		} else {
			arquivoFoto = new File("c:\\GrupoCaravela\\software\\imagens\\cliente\\img.png");
		}
		ImageIO.write(newImage, "png", new File(arquivoFoto.getAbsolutePath()));

	}

	private void salvarImagem() throws Exception {
		
		int totalBytes;
		int byteTrasferred;

		HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
				"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
						+ "/desbravar/imagens/cliente/upload.php?filename=" + cliente.getId() + ".png")
								.openConnection();
		httpUrlConnection.setDoOutput(true);
		httpUrlConnection.setRequestMethod("POST");
		OutputStream os = httpUrlConnection.getOutputStream();
		Thread.sleep(1000);
		
		BufferedInputStream fis = new BufferedInputStream(new FileInputStream(arquivoFoto));
		totalBytes = fis.available();

		for (int i = 0; i < totalBytes; i++) {
			os.write(fis.read());
			byteTrasferred = i + 1;
		}

		os.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));

		String s = null;
		while ((s = in.readLine()) != null) {
			System.out.println(s);
		}
		in.close();
		fis.close();
		
		CriarHistorico.criar(UsuarioLogado.getUsuario(), "A imagem do cliente " + cliente.getRazaoSocial() + " com id nº " + cliente.getId() + "foi alterada", dataAtual());

	}

	private void buscarImagemServidor() {

		String sistema = System.getProperty("os.name");
		ImageIcon imgThisImg = null;

		try {
			URL url = null;
			url = new URL("http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
					+ "/desbravar/imagens/cliente/" + cliente.getId() + ".png");
			url.openStream();
			imgThisImg = new ImageIcon(url);
			lblImagem.setIcon(imgThisImg);

		} catch (Exception e1) {

		}
	}
	
	public void tamanhoColunas() {
        //tableLista.setAutoResizeMode(tableLista.AUTO_RESIZE_OFF);
        tableLista.getColumnModel().getColumn(0).setWidth(70);
        tableLista.getColumnModel().getColumn(0).setMaxWidth(70);
        tableLista.getColumnModel().getColumn(1).setWidth(260);
        tableLista.getColumnModel().getColumn(1).setMinWidth(260);
        tableLista.getColumnModel().getColumn(2).setWidth(220);
        tableLista.getColumnModel().getColumn(2).setMinWidth(220);
        tableLista.getColumnModel().getColumn(3).setWidth(220);
        tableLista.getColumnModel().getColumn(4).setMaxWidth(220);
    }
}
