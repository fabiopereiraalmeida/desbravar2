package br.com.grupocaravela.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
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
import java.net.ProtocolException;
import java.net.URL;
import java.text.NumberFormat;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Categoria;
import br.com.grupocaravela.objeto.Produto;
import br.com.grupocaravela.objeto.Unidade;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelProduto;

public class JanelaCadastroProdutos extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelProduto tableModelProduto;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfCodigo;

	private Produto produto;
	private JTextField tfCusto;
	private JTextField tfEstoque;
	private JTextField tfPeso;
	private JComboBox cbFiltro;
	private JTextField tfNome;
	private JTextField tfQtdMinima;
	private JTextField tfQtdDesejada;
	private JTextField tfMargemLucroMin;
	private JTextField tfMargemLucroDesej;
	private JTextField tfValorVendaMin;
	private JTextField tfValorVendaDesej;

	private JComboBox cbUnidade;
	private JComboBox cbCategoria;
	private static JLabel lblImagem;
	private File arquivoFoto;
	private ImageIcon imgThisImg;

	// private RepositorioProduto repositorioProduto = new RepositorioProduto();
	// private RepositorioUnidade repositorioUnidade;
	// private RepositorioCategoria repositorioCategoria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroProdutos frame = new JanelaCadastroProdutos();
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
	public JanelaCadastroProdutos() {

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

		//factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void criarJanela() {
		setTitle("Produtos");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 931, 545);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 431, Short.MAX_VALUE));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar.setIcon(
				new ImageIcon(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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

					produto = tableModelProduto.getProduto(tableLista.getSelectedRow());
					carregarCampos(produto);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, true);
				}
			}
		});

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		cbFiltro = new JComboBox();
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Nome", "Código" }));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(12)
					.addComponent(lblLocalizar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addGap(12))
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLocalizar)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnBuscar)
							.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addGap(11)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				produto = tableModelProduto.getProduto(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão do produto " + produto.getNome() + "?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirProduto(produto);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();

				produto = tableModelProduto.getProduto(tableLista.getSelectedRow()); //Pega o produto selecionado
			
				carregarCampos(produto);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				
				
			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (verificaExistenciaUnidade() == true) {
					if (verificaExistenciaCategorias() == true) {
						limparCampos();
						produto = new Produto();
						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(0, false);
						tabbedPane.setEnabledAt(1, true);
						tfCodigo.requestFocus();
					} else {
						JOptionPane.showMessageDialog(null, "É necessario cadastrar categorias antes de proceguir!");
					}
				} else {
					JOptionPane.showMessageDialog(null, "É necessario cadastrar unidades antes de proceguir!");
				}

			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4
				.setHorizontalGroup(
						gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
								gl_panel_4.createSequentialGroup().addContainerGap(460, Short.MAX_VALUE)
										.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(btnDetalhes).addGap(12).addComponent(btnNovo,
												GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_4.createSequentialGroup()
						.addContainerGap(17, Short.MAX_VALUE).addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(btnExcluir).addComponent(btnDetalhes).addComponent(btnNovo))
						.addContainerGap()));
		panel_4.setLayout(gl_panel_4);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("Código");

		tfCodigo = new JTextField();
		tfCodigo.setColumns(10);

		JLabel lblFantasia = new JLabel("Custo");

		tfCusto = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfCusto.setColumns(10);

		JLabel lblCnpj = new JLabel("Estoque");

		tfEstoque = new JTextField();
		tfEstoque.setColumns(10);

		JLabel lblCpf = new JLabel("Peso");

		tfPeso = new DecimalFormattedField(DecimalFormattedField.PESO);
		tfPeso.setColumns(10);

		JLabel lblTipo = new JLabel("Categoria");
		
		JLabel lblNome_1 = new JLabel("Nome");

		tfNome = new JTextField();
		tfNome.setColumns(10);

		JLabel lblUnidade = new JLabel("Unidade");

		cbUnidade = new JComboBox();
		// carregajcbUnidade();

		cbCategoria = new JComboBox();
		// carregajcbCategoria();

		JLabel lblQtdMinima = new JLabel("Qtd. Minima");

		tfQtdMinima = new JTextField();
		tfQtdMinima.setColumns(10);

		JLabel lblQtdDesejada = new JLabel("Qtd. Desejada");

		tfQtdDesejada = new JTextField();
		tfQtdDesejada.setColumns(10);

		JLabel lblMargemLucroMin = new JLabel("Margem Lucro Min.");

		JLabel lblMargemLucroDesej = new JLabel("Margem Lucro Desej.");

		tfMargemLucroMin = new DecimalFormattedField(DecimalFormattedField.PORCENTAGEM);
		tfMargemLucroMin.setColumns(10);

		tfMargemLucroDesej = new DecimalFormattedField(DecimalFormattedField.PORCENTAGEM);
		tfMargemLucroDesej.setColumns(10);

		JLabel lblValorVendMin = new JLabel("Valor Vend. Min.");

		JLabel lblValorVendDesej = new JLabel("Valor Vend. Desej.");

		tfValorVendaMin = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorVendaMin.setColumns(10);

		tfValorVendaDesej = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorVendaDesej.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));

		JPanel panel_imagem = new JPanel();
		panel_imagem.setBorder(new TitledBorder(null, "Imagem", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(panel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 868,
												Short.MAX_VALUE)
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblFantasia)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfCusto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCnpj)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfEstoque, GroupLayout.PREFERRED_SIZE, 85,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCpf)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfPeso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblUnidade)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cbUnidade, GroupLayout.PREFERRED_SIZE, 138,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 122, Short.MAX_VALUE)).addGroup(
												gl_panel_1.createSequentialGroup().addComponent(lblId)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80,
																GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNome)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfCodigo, GroupLayout.PREFERRED_SIZE, 137,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNome_1)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfNome,
												GroupLayout.PREFERRED_SIZE, 399, GroupLayout.PREFERRED_SIZE)))
								.addContainerGap())
						.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblTipo)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(cbCategoria,
												GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblQtdMinima)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfQtdMinima, GroupLayout.PREFERRED_SIZE, 72,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblQtdDesejada)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfQtdDesejada,
												GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblMargemLucroMin)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfMargemLucroMin, GroupLayout.PREFERRED_SIZE, 88,
												GroupLayout.PREFERRED_SIZE)
										.addGap(32).addComponent(lblValorVendMin)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfValorVendaMin,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblMargemLucroDesej)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfMargemLucroDesej, GroupLayout.PREFERRED_SIZE, 97,
												GroupLayout.PREFERRED_SIZE)
										.addGap(33).addComponent(lblValorVendDesej)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfValorVendaDesej,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
								.addComponent(panel_imagem, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(29)))));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblId)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNome)
						.addComponent(tfCodigo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addComponent(lblNome_1).addComponent(tfNome,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
				.addGap(12)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblFantasia)
						.addComponent(tfCusto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCnpj)
						.addComponent(tfEstoque, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCpf)
						.addComponent(tfPeso, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblUnidade).addComponent(cbUnidade, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup()
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblTipo).addComponent(
								cbCategoria, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
						.addGap(28)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblQtdMinima)
								.addComponent(tfQtdMinima, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblQtdDesejada).addComponent(tfQtdDesejada, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(30)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblMargemLucroMin)
								.addComponent(tfMargemLucroMin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblValorVendMin).addComponent(tfValorVendaMin, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblMargemLucroDesej)
								.addComponent(tfMargemLucroDesej, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblValorVendDesej)
								.addComponent(tfValorVendaDesej, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_1.createSequentialGroup().addGap(29).addComponent(panel_imagem,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
				.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		lblImagem = new JLabel("");
		lblImagem.setIcon(
				new ImageIcon(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
		lblImagem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				try {
					buscarImagemLocal();
				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		});
		lblImagem.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_imagem = new GroupLayout(panel_imagem);
		gl_panel_imagem.setHorizontalGroup(gl_panel_imagem.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_imagem.createSequentialGroup()
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel_imagem.setVerticalGroup(gl_panel_imagem.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_imagem.createSequentialGroup()
						.addComponent(lblImagem, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_imagem.setLayout(gl_panel_imagem);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setEnabledAt(1, false);
				limparCampos();
				tfLocalizar.requestFocus();

			}
		});
		btnCancelar.setIcon(new ImageIcon(
				JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (verificaCamposVazios() == false) {
					salvarProduto(produto);
					try {
						salvarImagem();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setEnabledAt(1, false);
					tfLocalizar.requestFocus();
				}
			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(652, Short.MAX_VALUE)
					.addComponent(btnCancelar)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSalvar)
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelar)
						.addComponent(btnSalvar))
					.addContainerGap())
		);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	class ThreadBasica extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {

			carregajcbUnidade();
			carregajcbCategoria();
		}
	}

	private void carregarTableModel() {
		this.tableModelProduto = new TableModelProduto();
		this.tableLista.setModel(tableModelProduto);

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		tableLista.getColumnModel().getColumn(3).setCellRenderer(cellRendererCustomMoeda);

	}
		

	private void carregarTabela() {
				
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Produto where ativo = '1'");
			List<Produto> listaProdutos = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaProdutos.size(); i++) {
				Produto c = listaProdutos.get(i);
				tableModelProduto.addProduto(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de produtos: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelProduto.removeproduto(0);
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
					Logger.getLogger(JanelaCadastroProdutos.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				if (tfLocalizar.getText().isEmpty()) {
					carregarTabela();
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

	private void salvarProduto(Produto p) {
		try {

			p.setCodigo(tfCodigo.getText());
			p.setNome(tfNome.getText());
			p.setPeso(Double.parseDouble(tfPeso.getText().replace("Kg ", "").replace(".", "").replace(",", ".")));
			p.setQuantidadeEstoque(Double.parseDouble(tfEstoque.getText()));
			p.setValorCusto(
					Double.parseDouble(tfCusto.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			p.setQuantidadeMinimaEstoque(Double.parseDouble(tfQtdMinima.getText()));
			p.setQuantidadeDesejavelEstoque(Double.parseDouble(tfQtdDesejada.getText()));
			p.setMargemLucroMinimo(
					Double.parseDouble(tfMargemLucroMin.getText().replace("%", "").replace(".", "").replace(",", ".")));
			p.setMargemLucroDesejavel(Double
					.parseDouble(tfMargemLucroDesej.getText().replace("%", "").replace(".", "").replace(",", ".")));
			p.setValorMinimoVenda(Double
					.parseDouble(tfValorVendaMin.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			p.setValorDesejavelVenda(Double
					.parseDouble(tfValorVendaDesej.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			p.setCategoria((Categoria) cbCategoria.getSelectedItem());
			p.setUnidade((Unidade) cbUnidade.getSelectedItem());
			p.setAtivo(true);

			trx.begin();
			manager.persist(p);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Produto foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(Produto p) {

		tfId.setText(p.getId().toString());
		tfNome.setText(p.getNome());
		tfCodigo.setText(p.getCodigo());
		tfPeso.setText(p.getPeso().toString());
		tfEstoque.setText(p.getQuantidadeEstoque().toString());
		tfCusto.setText(p.getValorCusto().toString());
		tfQtdMinima.setText(p.getQuantidadeMinimaEstoque().toString());
		tfQtdDesejada.setText(p.getQuantidadeDesejavelEstoque().toString());
		tfMargemLucroMin.setText(p.getMargemLucroMinimo().toString());
		tfMargemLucroDesej.setText(p.getMargemLucroDesejavel().toString());
		tfValorVendaDesej.setText(p.getValorDesejavelVenda().toString());
		tfValorVendaMin.setText(p.getValorMinimoVenda().toString());
		cbCategoria.setSelectedItem(p.getCategoria());
		cbUnidade.setSelectedItem(p.getUnidade());

		buscarImagemServidor(p.getId().toString());

	}

	private void limparCampos() {
		tfId.setText("");
		tfNome.setText("");
		tfCodigo.setText("");
		tfPeso.setText("0");
		tfEstoque.setText("");
		tfCusto.setText("0");
		tfQtdMinima.setText("0");
		tfQtdDesejada.setText("0");
		tfMargemLucroMin.setText("0");
		tfMargemLucroDesej.setText("0");
		tfValorVendaMin.setText("0");
		tfValorVendaDesej.setText("0");

		lblImagem.setIcon(
				new ImageIcon(JanelaCadastroProdutos.class.getResource("/br/com/grupocaravela/imagens/semFoto.png")));
	}

	private void excluirProduto(Produto p) {
		try {
			
			p.setAtivo(false);

			trx.begin();
			manager.persist(p);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Produto foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private boolean verificaCamposVazios() {

		boolean retorno = false;

		if (tfCodigo.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe um código!");
			tfCodigo.requestFocus();
			retorno = true;
		} else if (tfNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o nome do produto!");
			tfNome.requestFocus();
			retorno = true;
		} else if (tfCusto.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o custo!");
			tfCusto.requestFocus();
			retorno = true;
		} else if (tfEstoque.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o estoque!");
			tfEstoque.requestFocus();
			retorno = true;
		} else if (tfValorVendaMin.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o valor minimo de venda!");
			tfValorVendaMin.requestFocus();
			retorno = true;
		} else if (tfValorVendaDesej.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o valor desejavel de venda!");
			tfValorVendaDesej.requestFocus();
			retorno = true;
		} else if (cbCategoria.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(null, "Informe a categoria para o produto!");
			cbCategoria.requestFocus();
			retorno = true;
		}

		return retorno;
	}

	private void filtrar(int op) {
		switch (op) {
		case 0:
			try {

				//trx.begin();
				Query consulta = manager.createQuery("from Produto where nome like '%" + tfLocalizar.getText() + "%' and ativo = '1'");
				List<Produto> listaProdutos = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaProdutos.size(); i++) {
					Produto c = listaProdutos.get(i);
					tableModelProduto.addProduto(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		case 1:
			try {

				//trx.begin();
				Query consulta = manager
						.createQuery("from Produto where codigo like '%" + tfLocalizar.getText() + "%' and ativo = '1'");
				List<Produto> listaProdutos = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaProdutos.size(); i++) {
					Produto c = listaProdutos.get(i);
					tableModelProduto.addProduto(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		default:

		}
	}

	private void carregajcbUnidade() {

		cbUnidade.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Unidade");
			List<Unidade> listaUnidades = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaUnidades.size(); i++) {

				Unidade u = listaUnidades.get(i);
				cbUnidade.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento da rota selecionada! " + e);
		}
	}

	private void carregajcbCategoria() {

		cbCategoria.removeAllItems();

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Categoria");
			List<Categoria> listaCategorias = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaCategorias.size(); i++) {

				Categoria c = listaCategorias.get(i);
				cbCategoria.addItem(c);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento da rota selecionada! " + e);
		}
	}

	private boolean verificaExistenciaUnidade() {
		boolean retorno = false;

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Unidade");
			List<Unidade> listaUnidades = consulta.getResultList();
			//trx.commit();

			if (listaUnidades.size() > 0) {
				retorno = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao verificar existencia de unidades! " + e);
		}

		return retorno;

	}

	private boolean verificaExistenciaCategorias() {
		boolean retorno = false;

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Categoria");
			List<Categoria> listaCategorias = consulta.getResultList();
			//trx.commit();

			if (listaCategorias.size() > 0) {
				retorno = true;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao verificar existencia de categorias! " + e);
		}

		return retorno;

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
			arquivoFoto = new File("/opt/GrupoCaravela/software/imagens/produto/img.png");
		} else {
			arquivoFoto = new File("c:\\GrupoCaravela\\software\\imagens\\produto\\img.png");
		}
		ImageIO.write(newImage, "png", new File(arquivoFoto.getAbsolutePath()));

	}

	private void salvarImagem() throws Exception {
		
		try {
			int totalBytes;
			int byteTrasferred;

			HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL(
					"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
							+ "/desbravar/imagens/produto/upload.php?filename=" + produto.getId() + ".png")
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
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void buscarImagemServidor(String id) {

		String idProduto = null;
		idProduto = id;
		
		if ("Linux".equals(System.getProperty("os.name"))) { //Resolvendo problema de compatibilidade com o windows
            imgThisImg = new ImageIcon("/opt/GrupoCaravela/software/imagens/produto/semFoto.png");
            lblImagem.setIcon(imgThisImg);

        } else {
            imgThisImg = new ImageIcon("c:\\GrupoCaravela\\software\\imagens\\produto\\semFoto.png");
            lblImagem.setIcon(imgThisImg);
        }
		
		URL url = null;
		
		try {			
								
			url = new URL("http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
					+ "/desbravar/imagens/produto/" + idProduto + ".png");
			
			url.openStream();
			
			Thread.sleep(1000);
									
			imgThisImg = new ImageIcon(url);
				
			lblImagem.setIcon(new ImageIcon(imgThisImg.getImage()));						

		} catch (Exception e1) {

		}
				
	}
	
	public void tamanhoColunas() {
        //tableLista.setAutoResizeMode(tableLista.AUTO_RESIZE_OFF);
        tableLista.getColumnModel().getColumn(0).setWidth(70);
        tableLista.getColumnModel().getColumn(0).setMaxWidth(70);
        tableLista.getColumnModel().getColumn(2).setWidth(300);
        tableLista.getColumnModel().getColumn(2).setMinWidth(300);
        tableLista.getColumnModel().getColumn(5).setWidth(50);
        tableLista.getColumnModel().getColumn(5).setMaxWidth(50);
    }
}
