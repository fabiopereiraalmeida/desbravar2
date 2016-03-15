package br.com.grupocaravela.view;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarProduto;
import br.com.grupocaravela.dialog.BuscarRota;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.objeto.Cidade;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.CreditoUsuario;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.Rota;
import br.com.grupocaravela.objeto.TelefoneCliente;
import br.com.grupocaravela.objeto.TelefoneUsuario;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.repositorio.RepositorioCreditoUsuario;
import br.com.grupocaravela.repositorio.RepositorioUsuario;
import br.com.grupocaravela.tablemodel.TableModelUsuario;

import java.awt.Toolkit;
import javax.swing.JTextPane;
import javax.swing.JPasswordField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;

public class JanelaCadastroUsuarios extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelUsuario tableModelUsuario;

	private TelefoneUsuario telefoneUsuario;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfNome;

	private Usuario usuario;
	private CreditoUsuario creditoUsuario;
	private JTextField tfUsuario;
	private JPasswordField pfSenha;
	private JTextField tfUltimoAcesso;
	private JComboBox cbEstado;
	
	private File arquivoFoto;

	// private RepositorioUsuario repositorioUsuario = new RepositorioUsuario();
	// private RepositorioCreditoUsuario repositorioCreditoUsuario = new
	// RepositorioCreditoUsuario();

	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private JTextField tfCredito;
	private JTextField tfEndereco;
	private JTextField tfNumeroEndereco;
	private JTextField tfComplemento;
	private JTextField tfBairro;
	private JTextField tfUf;

	private JFormattedTextField ftfCep;

	private JButton btnBuscar;
	private JButton btnExcluir;
	private JButton btnDetalhes;
	private JButton btnNovo;

	private JComboBox cbCidade;

	private JTabbedPane tabbedPane;
	private JPanel panel_4;
	private JLabel label;
	private JComboBox cbTipoTelefone;
	private JLabel label_1;
	private JFormattedTextField ftfNumeroTelefone;
	private JLabel label_2;
	private JComboBox cbTelefonesCadastrados;
	private JButton btnNovoTelefone;
	private JButton btnSalvarTelefone;
	private JButton btnExcluirTelefone;
	private JPanel panel_5;
	private JLabel lblRotasCadastradas;
	private JComboBox cbRotasCadastradas;
	private JLabel lblNome_1;
	private JTextField tfNomeRota;
	private JLabel lblObservao;
	private JPanel panel_6;
	private Rota rota;
	private JTextPane tpRota;

	private List<Rota> rotaList = new ArrayList<>();
	private List<Usuario> usuarioList = new ArrayList<>();
	private JPanel panel_7;
	private JLabel lblImagem;
	private JPanel panel_2;
	private JButton button;
	private JButton button_1;
	private JComboBox cbCargo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroUsuarios frame = new JanelaCadastroUsuarios();
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
	public JanelaCadastroUsuarios() {

		criarJanela();
		carregarTableModel();

		iniciaConexao();

		if (verificaExistenciaCidade() == false) {
			JOptionPane.showMessageDialog(null,
					"ATENÇÃO! Não extiste nunhuma cidade cadastrada. Antes de continuar cadastre uma cidade.");
			btnDetalhes.setEnabled(false);
			btnExcluir.setEnabled(false);
			btnNovo.setEnabled(false);
			btnBuscar.setEnabled(false);
			tabbedPane.setEnabled(false);
			tfLocalizar.setEnabled(false);
			tableLista.setEnabled(false);

		}

		carregajcbCidade();
		carregajcbCargo();

		// Evento ao abrir a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				tfLocalizar.requestFocus();
				super.windowActivated(e);
			}
		});

		
	}

	private void iniciaConexao() {

		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void criarJanela() {

		setTitle("Usuarios");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 816, 506);
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
				.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 452, Short.MAX_VALUE)
		);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar
				.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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
		scrollPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});

		tableLista = new JTable();
		tableLista.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableLista);
		tableLista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					limparCampos();
					
					usuario = tableModelUsuario.getUsuario(tableLista.getSelectedRow());
					creditoUsuario = usuario.getCreditoUsuario();

					carregarCampos(usuario);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(0, false);

				}
			}
		});

		btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();
				usuario = new Usuario();

				creditoUsuario = new CreditoUsuario();
				creditoUsuario.setValor(0.0);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				tfNome.requestFocus();

			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				usuario = tableModelUsuario.getUsuario(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão da usuario " + usuario.getNome() + "?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirUsuario(usuario);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();

				usuario = tableModelUsuario.getUsuario(tableLista.getSelectedRow());
				creditoUsuario = usuario.getCreditoUsuario();

				carregarCampos(usuario);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING,
										gl_panel.createSequentialGroup().addComponent(lblLocalizar)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 537,
														Short.MAX_VALUE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnBuscar,
														GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING,
								gl_panel.createSequentialGroup()
										.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12).addComponent(btnDetalhes).addGap(12).addComponent(btnNovo,
												GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(15)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBuscar).addComponent(lblLocalizar))
						.addGap(12).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false).addComponent(btnExcluir)
								.addComponent(btnDetalhes).addComponent(btnNovo))
						.addContainerGap()));
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("nome");

		tfNome = new JTextField();
		tfNome.setColumns(10);

		JLabel lblUsuario = new JLabel("Usuario");

		tfUsuario = new JTextField();
		tfUsuario.setColumns(10);

		pfSenha = new JPasswordField();

		JLabel lblSenha = new JLabel("Senha");

		JLabel lblUltimoAcesso = new JLabel("Ultimo Acesso");

		tfUltimoAcesso = new JTextField();
		tfUltimoAcesso.setEditable(false);
		tfUltimoAcesso.setColumns(10);

		JLabel lblEstado = new JLabel("Estado");

		cbEstado = new JComboBox();
		cbEstado.setModel(new DefaultComboBoxModel(new String[] {"ATIVO", "BLOQUEADO"}));

		JLabel lblCrdito = new JLabel("Crédito");

		tfCredito = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfCredito.setColumns(10);

		JLabel lblEndereo = new JLabel("Endereço");

		tfEndereco = new JTextField();
		tfEndereco.setColumns(10);

		JLabel lblN = new JLabel("Nº");

		tfNumeroEndereco = new JTextField();
		tfNumeroEndereco.setColumns(10);

		JLabel lblComplemento = new JLabel("Complemento");

		tfComplemento = new JTextField();
		tfComplemento.setColumns(10);

		JLabel lblBairro = new JLabel("Bairro");

		tfBairro = new JTextField();
		tfBairro.setColumns(10);

		JLabel lblCidade = new JLabel("Cidade");

		cbCidade = new JComboBox();

		JLabel lblUf = new JLabel("Uf");

		tfUf = new JTextField();
		tfUf.setColumns(10);

		JLabel lblCep = new JLabel("CEP");

		ftfCep = new JFormattedTextField();

		try {
			MaskFormatter maskCep = new MaskFormatter("##.###-###");
			maskCep.install(ftfCep);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "Imagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		lblImagem = new JLabel("");
		lblImagem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					buscarImagemLocal();
				} catch (IOException | InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		lblImagem.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
		lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 284, Short.MAX_VALUE)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGap(0, 284, Short.MAX_VALUE)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_7.setLayout(gl_panel_7);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));
		
		button = new JButton("Cancelar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				limparCampos();
			}
		});
		button.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		
		button_1 = new JButton("Salvar");
		button_1.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				salvarUsuario(usuario);
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				try {
					salvarImagem();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 775, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(527, Short.MAX_VALUE)
					.addComponent(button)
					.addGap(6)
					.addComponent(button_1)
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 53, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(button)
						.addComponent(button_1))
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		
		JLabel lblUsuario_1 = new JLabel("Cargo");
		
		cbCargo = new JComboBox();

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblId)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNome)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfNome, GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblUsuario)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, 185, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblSenha)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(pfSenha, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblCrdito)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfCredito, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblEstado)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbEstado, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblUltimoAcesso)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfUltimoAcesso, GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblCep)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, 173, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblUf)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblEndereo)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, 373, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblBairro)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, 249, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblCidade)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbCidade, GroupLayout.PREFERRED_SIZE, 312, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblN)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfNumeroEndereco, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblComplemento)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfComplemento, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblUsuario_1)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbCargo, GroupLayout.PREFERRED_SIZE, 293, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
							.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
							.addGap(10)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblId)
						.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNome)
						.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblUsuario)
								.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(pfSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblUltimoAcesso)
										.addComponent(tfUltimoAcesso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCrdito)
										.addComponent(tfCredito, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblEstado)
										.addComponent(cbEstado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblEndereo)
										.addComponent(tfEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblN)
										.addComponent(tfNumeroEndereco, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblComplemento)
										.addComponent(tfComplemento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(12)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblBairro)
										.addComponent(tfBairro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCidade)
										.addComponent(cbCidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblUsuario_1)
										.addComponent(cbCargo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblSenha))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(63)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCep)
						.addComponent(ftfCep, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUf)
						.addComponent(tfUf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(59))
		);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Telefones / Rotas", null, panel_3, null);

		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Telefones", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		label = new JLabel("Tipo");

		cbTipoTelefone = new JComboBox();
		cbTipoTelefone.setModel(new DefaultComboBoxModel(new String[] {"FIXO", "CELULAR", "FAX"}));

		label_1 = new JLabel("Numero:");

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
					// MaskFormatter masktel11 = new MaskFormatter("####
					// ###-####");

					if (telefone.length() == 10) {
						masktel8.install(ftfNumeroTelefone);
						ftfNumeroTelefone.setText(telefone);
						// btnSalvarTelefone.setEnabled(true);
					} else if (telefone.length() == 11) {
						masktel9.install(ftfNumeroTelefone);
						ftfNumeroTelefone.setText(telefone);
						// btnSalvarTelefone.setEnabled(true);
					} else if (telefone.length() == 0) {
						btnSalvarTelefone.setEnabled(false);
					}

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Erro!" + e2);
				}
			}
		});

		label_2 = new JLabel("Telefones cadastrados");

		cbTelefonesCadastrados = new JComboBox();
		cbTelefonesCadastrados.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				carregarTelefoneSelecionado((TelefoneUsuario) cbTelefonesCadastrados.getSelectedItem());

			}
		});

		btnNovoTelefone = new JButton("Novo");
		btnNovoTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ftfNumeroTelefone.setFormatterFactory(null);
				ftfNumeroTelefone.setText("");
				ftfNumeroTelefone.requestFocus();
			}
		});
		btnNovoTelefone
				.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		btnSalvarTelefone = new JButton("Salvar");
		btnSalvarTelefone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (ftfNumeroTelefone.getText() != "") {
					acaoSalvarTelefone(usuario);
					carregarCbTelefonesCadastrados(usuario);
					btnSalvarTelefone.setEnabled(false);
				} else {
					JOptionPane.showMessageDialog(null, "Antes de incluir, informe o numero de telefone!");
				}

			}
		});
		btnSalvarTelefone.setIcon(
				new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		btnSalvarTelefone.setEnabled(false);

		btnExcluirTelefone = new JButton("Excluir");
		btnExcluirTelefone.setIcon(new ImageIcon(
				JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup().addComponent(label)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbTipoTelefone, GroupLayout.PREFERRED_SIZE, 161,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_1)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(ftfNumeroTelefone,
										GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createSequentialGroup().addComponent(label_2)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbTelefonesCadastrados,
										GroupLayout.PREFERRED_SIZE, 295, GroupLayout.PREFERRED_SIZE)))
				.addGap(18)
				.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_4.createSequentialGroup().addComponent(btnSalvarTelefone)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnExcluirTelefone))
						.addComponent(btnNovoTelefone, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE))
				.addContainerGap(18, Short.MAX_VALUE)));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(label_2)
						.addComponent(cbTelefonesCadastrados, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNovoTelefone))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE).addComponent(label)
						.addComponent(cbTipoTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1)
						.addComponent(ftfNumeroTelefone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSalvarTelefone).addComponent(btnExcluirTelefone))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_4.setLayout(gl_panel_4);

		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "Rotas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_3.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 715,
										Short.MAX_VALUE)
						.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 727, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel_3
				.setVerticalGroup(
						gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
										.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 118,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
										.addContainerGap()));

		lblRotasCadastradas = new JLabel("Rotas cadastradas");

		cbRotasCadastradas = new JComboBox();

		lblNome_1 = new JLabel("Nome");

		tfNomeRota = new JTextField();
		tfNomeRota.setEditable(false);
		tfNomeRota.setColumns(10);

		lblObservao = new JLabel("Observação");

		panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(Color.DARK_GRAY));

		JButton btnAddRota = new JButton("Add Rota");
		btnAddRota.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				rota = new Rota();
				acaoAddRota();
				
				carregarCbRotasCadastradas(usuario);
			}
		});
		btnAddRota
				.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/mais_24.png")));

		JButton btnNewButton = new JButton("Excluir Rota");
		btnNewButton.setIcon(new ImageIcon(
				JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_5.createSequentialGroup().addComponent(lblRotasCadastradas)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbRotasCadastradas,
												GroupLayout.PREFERRED_SIZE, 416, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_5.createSequentialGroup().addComponent(lblNome_1)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfNomeRota, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblObservao)
						.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
								.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE).addGap(18)
								.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING, false)
										.addComponent(btnAddRota, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))))
						.addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_5.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_5.createSequentialGroup().addComponent(btnAddRota)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton))
						.addGroup(Alignment.LEADING, gl_panel_5.createSequentialGroup()
								.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblRotasCadastradas)
										.addComponent(cbRotasCadastradas, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGap(18)
								.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE).addComponent(lblNome_1)
										.addComponent(tfNomeRota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblObservao)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)))
						.addContainerGap()));

		tpRota = new JTextPane();
		tpRota.setEditable(false);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(tpRota,
				GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(tpRota,
				GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE));
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
		panel_3.setLayout(gl_panel_3);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelUsuario = new TableModelUsuario();
		this.tableLista.setModel(tableModelUsuario);
	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaUsuarios.size(); i++) {
				Usuario c = listaUsuarios.get(i);
				tableModelUsuario.addUsuario(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de usuarios: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelUsuario.removeusuario(0);
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
					Logger.getLogger(JanelaCadastroUsuarios.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					//trx.begin();
					Query consulta = manager
							.createQuery("from Usuario where nome like '%" + tfLocalizar.getText() + "%'");
					List<Usuario> listaUsuarios = consulta.getResultList();
					//trx.commit();

					for (int i = 0; i < listaUsuarios.size(); i++) {
						Usuario c = listaUsuarios.get(i);
						tableModelUsuario.addUsuario(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de usuarios: " + e);
					trx.commit();
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

	private void salvarUsuario(Usuario u) {
		try {

			u.setNome(tfNome.getText());
			u.setSenha(pfSenha.getText());
			u.setUsuario(tfUsuario.getText());

			u.setEndereco(tfEndereco.getText());
			u.setEnderecoNumero(tfNumeroEndereco.getText());
			u.setComplemento(tfComplemento.getText());
			u.setCidade((Cidade) cbCidade.getSelectedItem());
			u.setCargo((Cargo) cbCargo.getSelectedItem());
			u.setBairro(tfBairro.getText());
			u.setUf(tfUf.getText());
			u.setCep(ftfCep.getText());
			
			if (cbEstado.getSelectedIndex() == 0) {
				u.setAtivo(true);
			}else{
				u.setAtivo(false);
			}

			// salvando o credito
			creditoUsuario.setValor(
					Double.parseDouble(tfCredito.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			creditoUsuario.setUsuario(u);

			creditoUsuario.getUsuario().setCreditoUsuario(creditoUsuario);

			trx.begin();
			manager.persist(u);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Usuario foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
			trx.commit();
		}

	}

	private void carregarCampos(Usuario u) {

		tfId.setText(u.getId().toString());
		tfNome.setText(u.getNome());
		tfUsuario.setText(u.getUsuario());
		pfSenha.setText(u.getSenha());
		try {			
			String ua = formatDataHora.format(u.getUltimoAcesso());
			tfUltimoAcesso.setText(ua);
		} catch (Exception e) {
			tfUltimoAcesso.setText("");
		}

		cbCidade.setSelectedItem(u.getCidade());
		cbCargo.setSelectedItem(u.getCargo());

		tfEndereco.setText(u.getEndereco());
		tfNumeroEndereco.setText(u.getEnderecoNumero());
		tfComplemento.setText(u.getComplemento());
		tfBairro.setText(u.getBairro());
		tfUf.setText(u.getUf());
		ftfCep.setText(u.getCep());

		try {
			tfCredito.setText(creditoUsuario.getValor().toString());
		} catch (Exception e) {
			creditoUsuario = new CreditoUsuario();
			creditoUsuario.setValor(0.0);
			tfCredito.setText("0.0");
			
		}
		
		if (u.getAtivo() == true) {
			cbEstado.setSelectedIndex(0);
		}else{
			cbEstado.setSelectedIndex(1);
		}

		try {
			carregarCbTelefonesCadastrados(u);
		} catch (Exception e) {
			
			// TODO: handle exception
		}

		try {
			carregarCbRotasCadastradas(u);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		buscarImagemServidor();
	}

	private void limparCampos() {
		tfId.setText("");
		tfNome.setText("");
		tfUltimoAcesso.setText("");
		tfUsuario.setText("");
		pfSenha.setText("");
		tfEndereco.setText("");
		tfNumeroEndereco.setText("");
		tfComplemento.setText("");
		tfBairro.setText("");
		tfUf.setText("");
		ftfCep.setText("");
		try {
			cbCidade.setSelectedIndex(0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		lblImagem.setIcon(new ImageIcon(JanelaCadastroUsuarios.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));

	}

	private void excluirUsuario(Usuario c) {
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Usuario foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
			
		}
	}

	private void carregajcbCargo() {

		cbCargo.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Cargo");
			List<Cargo> listaCargo = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaCargo.size(); i++) {

				Cargo c = listaCargo.get(i);
				cbCargo.addItem(c);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos Cargos! " + e);			
		}
	}
	
	private void carregajcbCidade() {

		cbCidade.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Cidade");
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

	private boolean verificaExistenciaCidade() {

		boolean retorno = true;
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Cidade");
			List<Cidade> listaCidades = consulta.getResultList();
			//trx.commit();

			if (listaCidades.isEmpty()) {
				retorno = false;
			}
		} catch (Exception e) {
			
		}
			
		

		return retorno;
	}

	private void acaoSalvarTelefone(Usuario u) {

		telefoneUsuario = new TelefoneUsuario();

		salvarUsuario(u);
		
		try {
			telefoneUsuario.setTelefone(ftfNumeroTelefone.getText());
			
			if (cbTipoTelefone.getSelectedIndex() == 0) {
				telefoneUsuario.setTipoTelefone("FIXO");
			}
			if (cbTipoTelefone.getSelectedIndex() == 1) {
				telefoneUsuario.setTipoTelefone("CELULAR");
			}
			if (cbTipoTelefone.getSelectedIndex() == 2) {
				telefoneUsuario.setTipoTelefone("FAX");
			}
			telefoneUsuario.setUsuario(u);

			trx.begin();			
			manager.persist(telefoneUsuario);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Telefone adicionado com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao adicionar o telefone!");
			trx.commit();
		}
	}

	private void carregarCbTelefonesCadastrados(Usuario u) {

		cbTelefonesCadastrados.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from TelefoneUsuario where usuario_id like '" + u.getId() + "'");
			List<TelefoneUsuario> listaTelefonesUsuarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaTelefonesUsuarios.size(); i++) {

				TelefoneUsuario t = listaTelefonesUsuarios.get(i);
				cbTelefonesCadastrados.addItem(t);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos telefones do usuario! " + e);
			
		}
	}

	private void carregarTelefoneSelecionado(TelefoneUsuario tu) {

		try {
			cbTipoTelefone.setSelectedItem(tu.getTipoTelefone());
			ftfNumeroTelefone.setText(tu.getTelefone());
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "Erro ao carregar o telefone
			// selecionado!" + e);
		}

	}

	private void carregarCbRotasCadastradas(Usuario u) {

		cbRotasCadastradas.removeAllItems();

		try {

			rotaList = usuario.getRota();
			/*
			 * Query consulta = manager.createQuery(
			 * "from Rota where usuario_id like '" + u.getId() + "'");
			 * List<Rota> listaRota = consulta.getResultList(); trx.commit();
			 */
			for (int i = 0; i < rotaList.size(); i++) {

				rota = rotaList.get(i);
				cbRotasCadastradas.addItem(rota);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento das rotas do usuario! " + e);
		}
	}

	private void acaoAddRota() {

		try {
			BuscarRota buscarRota = new BuscarRota();
			buscarRota.setModal(true);
			buscarRota.setLocationRelativeTo(null);
			buscarRota.setVisible(true);

			rota = buscarRota.getRota();

			tfNomeRota.setText(rota.getNome());
			tpRota.setText(rota.getObservacao());

			rotaList.add(rota);
			usuarioList.add(usuario);
			
			usuario.setRota(rotaList);
			rota.setUsuarios(usuarioList);

			trx.begin();
			manager.persist(rota);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Rota adicionada com sucesso ao usuario!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao adicionar a rota ao usuario!");
			
		}
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
			arquivoFoto = new File("/opt/GrupoCaravela/software/imagens/usuario/img.png");
		} else {
			arquivoFoto = new File("c:\\GrupoCaravela\\software\\imagens\\usuario\\img.png");
		}
		ImageIO.write(newImage, "png", new File(arquivoFoto.getAbsolutePath()));

	}

	private void salvarImagem() throws Exception {
		
		int totalBytes;
		int byteTrasferred;

		HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
				"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
						+ "/desbravar/imagens/usuario/upload.php?filename=" + usuario.getId() + ".png")
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

	}

	private void buscarImagemServidor() {

		String sistema = System.getProperty("os.name");
		ImageIcon imgThisImg = null;

		try {
			URL url = null;
			url = new URL("http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
					+ "/desbravar/imagens/usuario/" + usuario.getId() + ".png");
			url.openStream();
			imgThisImg = new ImageIcon(url);
			lblImagem.setIcon(imgThisImg);

		} catch (Exception e1) {

		}
	}
}
