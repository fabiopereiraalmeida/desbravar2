package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarCliente;
import br.com.grupocaravela.dialog.BuscarProduto;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Caixa;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.ContaReceber;
import br.com.grupocaravela.objeto.CreditoUsuario;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Produto;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.objeto.VendaDetalhe;
import br.com.grupocaravela.relatorios.ChamaRelatorio;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda2Via;
import br.com.grupocaravela.relatorios.ChamaRelatorioReciboVenda;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.repositorio.RepositorioCreditoUsuario;
import br.com.grupocaravela.repositorio.RepositorioProduto;
import br.com.grupocaravela.repositorio.RepositorioUsuario;
import br.com.grupocaravela.tablemodel.TableModelListaVendas;
import br.com.grupocaravela.util.UsuarioLogado;
import net.sf.jasperreports.engine.JRException;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import com.toedter.calendar.JDateChooser;

public class JanelaVendas extends JFrame {

	private static final long serialVersionUID = 1L;

	// private EntityManagerProducer entityManagerProducer = new
	// EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private JPanel contentPane;
	private JTextField tfFantasiaCliente;
	private JTextField tfCodProduto;
	private JTextField tfNomeProduto;
	private JTextField tfQtdProduto;
	private JTextField tfSubTotal;
	private JTextField tfDesconto;
	private JTextField tfTotalGeral;
	private JTable tableProdutos;

	private Produto produtoImportado;
	private Produto produto;

	private Cliente cliente;
	private Usuario usuario;

	private VendaCabecalho vendaCabecalho;
	private List<VendaDetalhe> listaVendaDetalhes = new ArrayList<>();

	private TableModelListaVendas tableModelListaVendas;

	private JTextField tfRazaoSocialCliente;
	private JTextField tfRota;
	private JTextField tfSituacaoCadastralCliente;
	private JTextField tfCredito;
	private JComboBox cbUsuario;
	private JComboBox cbFormaPagamento;

	private JButton btBuscaCliente;
	private JButton btBuscaProduto;
	private JButton btOk;
	private JButton btnNovavenda;
	private JButton btnCancelarVenda;
	private JButton btnExcluirItem;
	private JButton btnFinalizarF;
	private JButton btnAlterarQtd;
	private JButton btnDescontoF;

	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private Double valorNotaIndividual = 0.0;

	// private RepositorioProduto repositorioProduto = new RepositorioProduto();
	// private RepositorioCreditoUsuario repositorioCreditoUsuario;

	private CreditoUsuario creditoUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaVendas frame = new JanelaVendas();
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
	public JanelaVendas() {
		criaJanela();

		iniciaConexao();

		Thread thread = new ThreadBasica(); // Cria a tread para carregar
											// ocomobox;
		thread.start(); // Inicia Thread

		carregarTableModel();
		tamanhoColunas();

		desativarVenda();
		/*
		 * // Evento ao fechar a janela addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { try {
		 * factory.close(); } catch (Exception e2) { // TODO: handle exception }
		 * } });
		 */
	}

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void criaJanela() {

		setTitle("Vendas");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1100, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Dados do cliente",
				TitledBorder.LEADING, TitledBorder.TOP, null, new Color(64, 64, 64)));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Op\u00E7\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null,
				Color.DARK_GRAY));

		JLabel lblProduto = new JLabel("Produto");
		lblProduto.setFont(new Font("Dialog", Font.BOLD, 18));

		tfCodProduto = new JTextField();
		tfCodProduto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					produtoImportado = buscarProdutoPorCodigo(tfCodProduto.getText());
					tfNomeProduto.setText(produtoImportado.getNome());
					tfQtdProduto.requestFocus();
					tfQtdProduto.selectAll();
				}

			}
		});
		tfCodProduto.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfCodProduto.setColumns(10);

		tfNomeProduto = new JTextField();
		tfNomeProduto.setEditable(false);
		tfNomeProduto.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfNomeProduto.setColumns(10);

		JLabel lblQtd = new JLabel("Qtd");
		lblQtd.setFont(new Font("Dialog", Font.BOLD, 18));

		tfQtdProduto = new JTextField();
		tfQtdProduto.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfCodProduto.selectAll();
			}
		});
		tfQtdProduto.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addListaVenda(produtoImportado, Double.parseDouble(tfQtdProduto.getText()), 0.0);
				}

			}
		});
		tfQtdProduto.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfQtdProduto.setColumns(10);

		btOk = new JButton("ok");
		btOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null,
				// produtoImportado.getNome());
				addListaVenda(produtoImportado, Double.parseDouble(tfQtdProduto.getText()), 0.0);

			}
		});
		btOk.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		JPanel panel_2 = new JPanel();

		JLabel lblSubTotal = new JLabel("Sub Total");
		lblSubTotal.setFont(new Font("Dialog", Font.BOLD, 18));

		// tfSubTotal = new JTextField();
		tfSubTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfSubTotal.setEditable(false);
		tfSubTotal.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfSubTotal.setColumns(10);

		JLabel lblDesconto = new JLabel("Desconto");
		lblDesconto.setFont(new Font("Dialog", Font.BOLD, 18));

		tfDesconto = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfDesconto.setEditable(false);
		tfDesconto.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfDesconto.setColumns(10);

		JLabel lblTotalGeral = new JLabel("Total Geral");
		lblTotalGeral.setFont(new Font("Dialog", Font.BOLD, 18));

		tfTotalGeral = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfTotalGeral.setEditable(false);
		tfTotalGeral.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfTotalGeral.setColumns(10);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(
				new TitledBorder(null, "Dados do vendedor", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btBuscaProduto = new JButton("");
		btBuscaProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// #############################################
				final Thread tr = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							TimeUnit.SECONDS.sleep(0);
						} catch (InterruptedException ex) {
							Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
						}
						// ######################METODO A SER
						// EXECUTADO##############################

						acaoBuscarProduto();

						// ######################FIM METODO A SER
						// EXECUTADO##############################
					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						tr.start();
						// .....
						EsperaJanela espera = new EsperaJanela();
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

				// ###############################################

			}
		});
		btBuscaProduto
				.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(panel_2,
										GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
										.addComponent(panel, GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE)
										.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE)
										.addGroup(gl_contentPane.createSequentialGroup().addComponent(lblSubTotal)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tfSubTotal, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblDesconto)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfDesconto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 186, Short.MAX_VALUE)
										.addComponent(lblTotalGeral).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfTotalGeral, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_contentPane.createSequentialGroup().addContainerGap().addComponent(panel_3,
								GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup().addGap(17).addComponent(lblProduto)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btBuscaProduto, GroupLayout.PREFERRED_SIZE, 38,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfCodProduto, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfNomeProduto, GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblQtd)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfQtdProduto, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btOk)))
						.addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING).addGroup(gl_contentPane
				.createSequentialGroup()
				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblProduto)
								.addComponent(tfCodProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(btBuscaProduto, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addGroup(
								gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(tfNomeProduto, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblQtd).addComponent(tfQtdProduto, GroupLayout.PREFERRED_SIZE, 26,
												GroupLayout.PREFERRED_SIZE))
						.addComponent(btOk, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE).addComponent(lblSubTotal)
						.addComponent(tfSubTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDesconto)
						.addComponent(tfDesconto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTotalGeral).addComponent(tfTotalGeral, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		JLabel lblVendedor = new JLabel("Vendedor");

		cbUsuario = new JComboBox();
		cbUsuario.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

			}
		});

		JLabel lblCrdito = new JLabel("Crédito");

		tfCredito = new JTextField();
		tfCredito.setEditable(false);
		tfCredito.setColumns(10);

		JLabel lblFormaDePagamento = new JLabel("Forma de pagamento");

		cbFormaPagamento = new JComboBox();

		JButton btnInserirValorDa = new JButton("Inserir valor da nota");
		btnInserirValorDa.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				valorNotaIndividual = Double.parseDouble(JOptionPane.showInputDialog("Infome o valor da nota", "1")
						.replace("R$ ", "").replace(".", "").replace(",", "."));
				tfTotalGeral.setText(valorNotaIndividual.toString());

				btnFinalizarF.setEnabled(true);

			}
		});

		JLabel lblVencimento = new JLabel("Vencimento");

		JComboBox comboBox = new JComboBox();
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblVendedor).addGap(12)
										.addComponent(cbUsuario, GroupLayout.PREFERRED_SIZE, 334,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblCrdito)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfCredito, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblFormaDePagamento)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbFormaPagamento, GroupLayout.PREFERRED_SIZE, 310,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblVencimento)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
								.addComponent(btnInserirValorDa)))
						.addContainerGap()));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(5).addComponent(lblVendedor))
								.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
										.addComponent(cbUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCrdito).addComponent(tfCredito, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblFormaDePagamento)
						.addComponent(cbFormaPagamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblVencimento).addComponent(comboBox, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap(47, Short.MAX_VALUE)
						.addComponent(btnInserirValorDa).addContainerGap()));
		panel_3.setLayout(gl_panel_3);

		JScrollPane scrollPane = new JScrollPane();

		tableProdutos = new JTable();
		scrollPane.setViewportView(tableProdutos);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 1064, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE));
		panel_2.setLayout(gl_panel_2);

		btnNovavenda = new JButton("Nova - F2");
		btnNovavenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				acaoNovaVenda();

			}
		});
		btnNovavenda.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		btnCancelarVenda = new JButton("Cancelar - F7");
		btnCancelarVenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Gostaria de cancelar a venda?", "Cancelar venda",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
					acaoCancelarVenda();
				}

			}
		});
		btnCancelarVenda.setIcon(
				new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		btnFinalizarF = new JButton("Finalizar - F11");
		btnFinalizarF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (cbUsuario.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(null, "Antes de finalizar a venda, é necessario escolher o vendedor");
					cbUsuario.requestFocus();
				} else {

					if (cliente == null) {
						JOptionPane.showMessageDialog(null,
								"Antes de finalizar a venda, é necessario escolher o cliente");

					} else {

						Object[] options = { "Sim", "Não" };
						int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Finalizar a venda?", "Finalizar Venda",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

						if (i == JOptionPane.YES_OPTION) {

							if (valorNotaIndividual != 0.0) {
								acaoFinalizarVendaValorIndividual(cliente, (Usuario) cbUsuario.getSelectedItem(),
										(FormaPagamento) cbFormaPagamento.getSelectedItem(), vendaCabecalho,
										valorNotaIndividual);
								valorNotaIndividual = 0.0;
								
								//imprimirComprovante();
							} else {

								if (verificaValorMinimoFormaPagamento(calcularValorTotalGeral()) == true) {
									acaoFinalizarVenda(cliente, usuario,
											(FormaPagamento) cbFormaPagamento.getSelectedItem(), vendaCabecalho,
											listaVendaDetalhes);
									
									//imprimirComprovante();

								} else {
									FormaPagamento fp = (FormaPagamento) cbFormaPagamento.getSelectedItem();
									JOptionPane.showMessageDialog(null,
											"ATENÇÃO!!! Valor minimo para esta forma de pagamento é de R$"
													+ fp.getValorMinimo()
													+ "! Favor optar por outra forma de pagamento! ");
									cbFormaPagamento.requestFocus();
								}

							}
						}
					}
				}

			}
		});
		btnFinalizarF
				.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		btnExcluirItem = new JButton("Exc. Item - F5");
		btnExcluirItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a exclusão do produto " + tableModelListaVendas
								.getVendaDetalhe(tableProdutos.getSelectedRow()).getProduto().getNome() + " da lista?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					int linha = tableProdutos.getSelectedRow();
					int linhaReal = tableProdutos.convertRowIndexToModel(linha);

					// acaoExcluirProduto(tableModelListaVendas.getVendaDetalhe(linhaReal));
					acaoExcluirProduto(linha);
					calcularTotais();
				}

			}
		});
		btnExcluirItem.setIcon(
				new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		btnAlterarQtd = new JButton("Alt. Qtd. - F6");
		btnAlterarQtd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Double quantidade = Double.parseDouble(JOptionPane.showInputDialog("Infome a Quantidade",
						tableModelListaVendas.getValueAt(tableProdutos.getSelectedRow(), 3)));

				int linha = tableProdutos.getSelectedRow();
				int linhaReal = tableProdutos.convertRowIndexToModel(linha);

				acaoAlterarQuantidade(linha, tableModelListaVendas.getVendaDetalhe(linha), quantidade);
				calcularTotais();

			}
		});
		btnAlterarQtd
				.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));

		btnDescontoF = new JButton("Desc. - F4");
		btnDescontoF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Gostaria de alterar o desconto para o produto " + tableModelListaVendas
								.getVendaDetalhe(tableProdutos.getSelectedRow()).getProduto().getNome() + " da lista?",
						"Desconto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					int linha = tableProdutos.getSelectedRow();
					int linhaReal = tableProdutos.convertRowIndexToModel(linha);

					Double desc = Double.parseDouble(JOptionPane.showInputDialog("Infome o desconto",
							tableModelListaVendas.getValueAt(tableProdutos.getSelectedRow(), 5)));

					acaoAlterarDesconto(linha, tableModelListaVendas.getVendaDetalhe(linha), desc);
					calcularTotais();

				}

			}
		});
		btnDescontoF
				.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/gastos_24.png")));
		
		JButton button = new JButton("0");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//#################### INICIO CAPTURANDO ENDEREÇO ###################
				EnderecoCliente enderecoCliente = null;
				try {
					enderecoCliente = cliente.getEnderecos().get(0);
				} catch (Exception e2) {
					enderecoCliente = null;
				}
				//#################### FIM CAPTURANDO ENDEREÇO ###################
				
				//################## INICIO CAPTURANDO FORMA DE PAGAMENTO ####################
				FormaPagamento f = (FormaPagamento) cbFormaPagamento.getSelectedItem();
				//################## FIM CAPTURANDO FORMA DE PAGAMENTO ####################
				
				//###################### INICIO PEGANDO DATA VENCIMENRTO ###################
				
				GregorianCalendar gcGregorian = new GregorianCalendar();
				gcGregorian.setTime(dataAtual());

				gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

				
				String dtVencimento = format.format(gcGregorian.getTime());
				
				//###################### FIM PEGANDO DATA VENCIMENRTO ###################
								
				ChamaRelatorioComprovanteVenda chamaRelatorio = new ChamaRelatorioComprovanteVenda();
								
				try {							
					
					//chamaRelatorio.report((Usuario) cbUsuario.getSelectedItem(), cliente, enderecoCliente, f.getNome(), format.format(dataAtual()), dtVencimento, vendaCabecalho.getId().toString());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage() );
				}
				
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNovavenda)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnCancelarVenda)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnExcluirItem)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAlterarQtd)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnDescontoF)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(button)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnFinalizarF)
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNovavenda)
						.addComponent(btnCancelarVenda)
						.addComponent(btnFinalizarF)
						.addComponent(btnExcluirItem)
						.addComponent(btnAlterarQtd)
						.addComponent(btnDescontoF)
						.addComponent(button))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);

		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setFont(new Font("Dialog", Font.BOLD, 18));

		tfFantasiaCliente = new JTextField();
		tfFantasiaCliente.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfFantasiaCliente.setEditable(false);
		tfFantasiaCliente.setColumns(10);

		btBuscaCliente = new JButton("");
		btBuscaCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// #############################################
				final Thread tr = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							TimeUnit.SECONDS.sleep(0);
						} catch (InterruptedException ex) {
							Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
						}
						// ######################METODO A SER
						// EXECUTADO##############################

						acaoBuscarCliente();
						tfCodProduto.requestFocus();

						// ######################FIM METODO A SER
						// EXECUTADO##############################
					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						tr.start();
						// .....
						EsperaJanela espera = new EsperaJanela();
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

				// ###############################################

			}
		});
		btBuscaCliente
				.setIcon(new ImageIcon(JanelaVendas.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

		JLabel lblFantasia = new JLabel("Fantasia");

		JLabel lblRazoSocial = new JLabel("Razão Social");

		tfRazaoSocialCliente = new JTextField();
		tfRazaoSocialCliente.setFont(new Font("Dialog", Font.PLAIN, 18));
		tfRazaoSocialCliente.setEditable(false);
		tfRazaoSocialCliente.setColumns(10);

		JLabel lblRota = new JLabel("Rota");

		tfRota = new JTextField();
		tfRota.setEditable(false);
		tfRota.setColumns(10);

		JLabel lblNewLabel = new JLabel("Situação Cadastral");

		tfSituacaoCadastralCliente = new JTextField();
		tfSituacaoCadastralCliente.setEditable(false);
		tfSituacaoCadastralCliente.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addComponent(lblCliente).addGap(4)
										.addComponent(btBuscaCliente, GroupLayout.PREFERRED_SIZE, 38,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblFantasia)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfFantasiaCliente, GroupLayout.PREFERRED_SIZE, 324,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblRazoSocial)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfRazaoSocialCliente, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
						.addGroup(
								gl_panel.createSequentialGroup().addComponent(lblRota)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfRota, GroupLayout.PREFERRED_SIZE, 218,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNewLabel)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfSituacaoCadastralCliente,
										GroupLayout.PREFERRED_SIZE, 221, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblFantasia)
								.addComponent(tfFantasiaCliente, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblRazoSocial).addComponent(tfRazaoSocialCliente,
										GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblCliente)
						.addComponent(btBuscaCliente, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(lblRota)
						.addComponent(tfRota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel).addComponent(tfSituacaoCadastralCliente, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(15, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregarTableModel() {
		this.tableModelListaVendas = new TableModelListaVendas();
		this.tableProdutos.setModel(tableModelListaVendas);

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		tableProdutos.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);
		tableProdutos.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
		tableProdutos.getColumnModel().getColumn(6).setCellRenderer(cellRendererCustomMoeda);
		tableProdutos.getColumnModel().getColumn(7).setCellRenderer(cellRendererCustomMoeda);
	}

	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		tableProdutos.getColumnModel().getColumn(0).setWidth(120);
		tableProdutos.getColumnModel().getColumn(0).setMinWidth(120);
		tableProdutos.getColumnModel().getColumn(3).setWidth(350);
		tableProdutos.getColumnModel().getColumn(3).setMinWidth(350);

	}

	private void addListaVenda(Produto p, Double qtd, Double desconto) { // Adiciona
																			// na
																			// tabela
																			// o
																			// produto

		limparTabela();
		// List<VendaDetalhe> listaVendaDetalhes = new ArrayList<>();
		VendaDetalhe vendaDetalhe = new VendaDetalhe();

		vendaDetalhe.setProduto(p);
		vendaDetalhe.setQuantidade(qtd);
		vendaDetalhe.setValorDesconto(desconto);
		vendaDetalhe.setValorParcial(qtd * p.getValorDesejavelVenda());
		vendaDetalhe.setValorTotal((qtd * p.getValorDesejavelVenda() - desconto));
		vendaDetalhe.setVendaCabecalho(vendaCabecalho);

		listaVendaDetalhes.add(vendaDetalhe);

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			VendaDetalhe v = listaVendaDetalhes.get(i);
			tableModelListaVendas.addVendaDetalhe(v);
		}

		tfCodProduto.setText("");
		tfQtdProduto.setText("1.0");

		tfCodProduto.requestFocus();

		calcularTotais();

		btnFinalizarF.setEnabled(true);
	}

	private void limparTabela() {
		while (tableProdutos.getModel().getRowCount() > 0) {
			tableModelListaVendas.removevendaDetalhe(0);
		}
	}

	private void limparCampos() {
		tfCredito.setText("0.0");
		tfCodProduto.setText("");
		tfDesconto.setText("0.0");
		tfFantasiaCliente.setText("");
		tfNomeProduto.setText("");
		tfQtdProduto.setText("1.0");
		tfRazaoSocialCliente.setText("");
		tfRota.setText("");
		tfSituacaoCadastralCliente.setText("");
		tfSubTotal.setText("0.0");
		tfTotalGeral.setText("0.0");

		listaVendaDetalhes.clear();
		limparTabela();
	}

	private void acaoNovaVenda() {
		
		ativarVenda();
		limparTabela();
		limparCampos();

		vendaCabecalho = new VendaCabecalho();
		listaVendaDetalhes.clear();

		bucarUsuarioLogado();

		buscarCreditoUsuario();

		btBuscaCliente.requestFocus();
	}

	private void carregajcbUsuario() {

		cbUsuario.removeAllItems();

		try {

			// trx.begin();
			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuarios = consulta.getResultList();
			// trx.commit();

			// JOptionPane.showMessageDialog(null, listaUsuarios.size());

			for (int i = 0; i < listaUsuarios.size(); i++) {

				// JOptionPane.showMessageDialog(null, i);

				Usuario u = listaUsuarios.get(i);
				cbUsuario.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox do usuário! " + e);
			trx.commit();
		}

	}

	private void carregajcbFormaPagamento() {

		cbFormaPagamento.removeAllItems();

		try {

			// trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento");
			List<FormaPagamento> listaFormaPagamento = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaFormaPagamento.size(); i++) {

				FormaPagamento f = listaFormaPagamento.get(i);
				cbFormaPagamento.addItem(f);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento da rota selecionada! " + e);
			trx.commit();
		}
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private void desativarVenda() {
		btBuscaCliente.setEnabled(false);
		btBuscaProduto.setEnabled(false);
		btOk.setEnabled(false);
		tfCodProduto.setEnabled(false);
		tfQtdProduto.setEnabled(false);
		btnAlterarQtd.setEnabled(false);
		btnCancelarVenda.setEnabled(false);
		btnDescontoF.setEnabled(false);
		btnExcluirItem.setEnabled(false);
		btnFinalizarF.setEnabled(false);

		cbUsuario.setEnabled(false);
		cbFormaPagamento.setEnabled(false);

		btnNovavenda.setEnabled(true);

	}

	private void ativarVenda() {
		btBuscaCliente.setEnabled(true);
		btBuscaProduto.setEnabled(true);
		btOk.setEnabled(true);
		tfCodProduto.setEnabled(true);
		tfQtdProduto.setEnabled(true);
		btnAlterarQtd.setEnabled(true);
		btnCancelarVenda.setEnabled(true);
		btnDescontoF.setEnabled(true);
		btnExcluirItem.setEnabled(true);
		// btnFinalizarF.setEnabled(true);

		cbUsuario.setEnabled(true);
		cbFormaPagamento.setEnabled(true);

		btnNovavenda.setEnabled(false);

	}

	private Produto buscarProdutoPorCodigo(String codigo) {

		Produto pro = null;
		try {

			// trx.begin();
			Query consulta = manager.createQuery("from Produto where codigo like '" + tfCodProduto.getText() + "'");
			List<Produto> listaProduto = consulta.getResultList();
			// trx.commit();

			pro = listaProduto.get(0);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Código não encontrado!!!");
			tfCodProduto.setText("");
			tfCodProduto.requestFocus();
			trx.commit();
		}
		return pro;
	}

	class ThreadBasica extends Thread {
		// Este método(run()) é chamado quando a thread é iniciada
		public void run() {

			if (verificaExistenciaFormaPagamento() == false) {
				JOptionPane.showMessageDialog(null,
						"ATENÇÃO! Não existe nunhuma forma de pagamento cadastrada. Antes de continuar cadastre uma forma de pagamento.");
				btnNovavenda.setEnabled(false);
			}

			carregajcbUsuario();
			carregajcbFormaPagamento();

		}
	}

	private void acaoCancelarVenda() {
		limparCampos();
		limparTabela();
		desativarVenda();
	}

	private void acaoBuscarCliente() {
		BuscarCliente buscarCliente = new BuscarCliente();

		buscarCliente.setModal(true);
		buscarCliente.setLocationRelativeTo(null);
		buscarCliente.setVisible(true);

		cliente = buscarCliente.getCliente();

		if (cliente != null) {

			if (cliente.getAtivo().equals(false)) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! O cliente selecionado esta bloqueado! Gostaria de selecionar outro cliente?",
						"Bloqueado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					acaoBuscarCliente();
				} else {
					acaoCancelarVenda();
					JOptionPane.showMessageDialog(null, "A venda foi cancela!!!");
				}

			} else {
				tfFantasiaCliente.setText(cliente.getFantasia());
				tfRazaoSocialCliente.setText(cliente.getRazaoSocial());
				tfRota.setText(cliente.getRota().getNome());
				if (cliente.getAtivo() == true) {
					tfSituacaoCadastralCliente.setText("Ativo");
				} else {
					tfSituacaoCadastralCliente.setText("Bloqueado");
				}

			}
		}
	}

	private void bucarUsuarioLogado() {

		try {
			cbUsuario.setSelectedItem(UsuarioLogado.getUsuario()); // tenta
																	// selecionar
																	// o usuario
																	// logado

			creditoUsuario = UsuarioLogado.getUsuario().getCreditoUsuario();
			tfCredito.setText(creditoUsuario.getValor().toString());

			usuario = UsuarioLogado.getUsuario();

		} catch (Exception e) {

			// TODO: handle exception
		}
	}

	private void acaoBuscarProduto() {
		BuscarProduto buscarProduto = new BuscarProduto();

		buscarProduto.setModal(true);
		buscarProduto.setLocationRelativeTo(null);
		buscarProduto.setVisible(true);

		produtoImportado = buscarProduto.getProduto();

		tfNomeProduto.setText(produtoImportado.getNome());
		tfCodProduto.setText(produtoImportado.getCodigo());

		tfQtdProduto.requestFocus();
		tfQtdProduto.selectAll();
	}
	/*
	 * private void acaoExcluirProduto(VendaDetalhe vd){
	 * 
	 * limparTabela();
	 * 
	 * listaVendaDetalhes.remove(vd);
	 * 
	 * for (int i = 0; i < listaVendaDetalhes.size(); i++) { VendaDetalhe v =
	 * listaVendaDetalhes.get(i); tableModelListaVendas.addVendaDetalhe(v); } }
	 */

	private void acaoExcluirProduto(int index) {

		limparTabela();

		listaVendaDetalhes.remove(index);

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			VendaDetalhe v = listaVendaDetalhes.get(i);
			tableModelListaVendas.addVendaDetalhe(v);
		}
	}
	/*
	 * private void acaoAlterarQuantidade(VendaDetalhe vd, Double qtd) {
	 * 
	 * limparTabela(); listaVendaDetalhes.remove(vd);
	 * 
	 * VendaDetalhe vendaDetalhe = vd;
	 * 
	 * vendaDetalhe.setQuantidade(qtd);
	 * 
	 * listaVendaDetalhes.add(vendaDetalhe);
	 * 
	 * for (int i = 0; i < listaVendaDetalhes.size(); i++) { VendaDetalhe v =
	 * listaVendaDetalhes.get(i); tableModelListaVendas.addVendaDetalhe(v); }
	 * 
	 * }
	 */

	private void acaoAlterarQuantidade(int index, VendaDetalhe vd, Double qtd) {

		limparTabela();

		vd.setQuantidade(qtd);
		vd.setValorParcial(qtd * vd.getProduto().getValorDesejavelVenda());
		vd.setValorTotal((qtd * vd.getProduto().getValorDesejavelVenda() - vd.getValorDesconto()));

		listaVendaDetalhes.set(index, vd);

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			VendaDetalhe v = listaVendaDetalhes.get(i);
			tableModelListaVendas.addVendaDetalhe(v);
		}
	}

	private void acaoAlterarDesconto(int index, VendaDetalhe vd, Double desc) {

		limparTabela();

		vd.setValorDesconto(desc);
		vd.setValorTotal((vd.getValorParcial() - vd.getValorDesconto()));

		listaVendaDetalhes.set(index, vd);

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			VendaDetalhe v = listaVendaDetalhes.get(i);
			tableModelListaVendas.addVendaDetalhe(v);
		}
	}

	private void acaoFinalizarVenda(Cliente c, Usuario u, FormaPagamento f, VendaCabecalho v, List<VendaDetalhe> l) {

		// v = new VendaCabecalho();

		try {

			v.setCliente(c);
			v.setDataVenda(dataAtual());
			v.setFormaPagamento(f);
			v.setUsuario(u);
			v.setValorDesconto(calcularValorTotalDesconto());
			v.setValorParcial(calcularValorTotalParcial());
			v.setValorTotal(calcularValorTotalGeral());

			v.setVendaCabecalho(l);

			trx.begin();
			manager.persist(v);
			trx.commit();
			
			Object[] options = { "Sim", "Não" };
			int i = JOptionPane.showOptionDialog(null, "Gerar 2ª via do comprovante de venda?", "2ª via",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (i == JOptionPane.YES_OPTION) {
				imprimirComprovante(v,c,f,u);
				imprimirComprovante2Via(v,c,f,u);
			}else {
				imprimirComprovante(v,c,f,u);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao finalizar a venda!" + e);
		}

		if (f.getNumeroDias() > 0) {
			criarContaReceber(c, v, v.getValorTotal());
		} else {
			criarCaixa(v);
		}
		
		baixaEstoque(v);

		limparCampos();
		desativarVenda();

		JOptionPane.showMessageDialog(null, "Venda concluida com sucesso!");

	}

	private void acaoFinalizarVendaValorIndividual(Cliente c, Usuario u, FormaPagamento f, VendaCabecalho v,
			Double valor) {

		// try {

		v.setCliente(c);
		v.setDataVenda(dataAtual());
		v.setFormaPagamento(f);
		v.setUsuario(u);
		v.setValorDesconto(0.0);
		v.setValorParcial(0.0);
		v.setValorTotal(valor);

		// v.setVendaCabecalho(l);
		trx.begin();
		manager.persist(v);
		trx.commit();
		
		Object[] options = { "Sim", "Não" };
		int i2 = JOptionPane.showOptionDialog(null, "Gerar 2ª via do comprovante de venda?", "2ª via",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (i2 == JOptionPane.YES_OPTION) {
			imprimirComprovante(v,c,f,u);
			imprimirComprovante2Via(v,c,f,u);
		}else {
			imprimirComprovante(v,c,f,u);
		}

		if (f.getNumeroDias() > 0) {
			criarContaReceber(c, v, v.getValorTotal());
		} else {
			criarCaixa(v);
		}

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			baixaEstoque(v);
		}

		JOptionPane.showMessageDialog(null, "Venda concluida com sucesso!");
		
		limparCampos();
		desativarVenda();

	}

	private void criarContaReceber(Cliente c, VendaCabecalho vc, Double valorDevido) {

		int qtdDias = vc.getFormaPagamento().getNumeroDias();

		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		try {

			for (int i = 0; i < vc.getFormaPagamento().getNumeroParcelas(); i++) {
				ContaReceber contaReceber = new ContaReceber();

				gcGregorian.set(GregorianCalendar.DAY_OF_MONTH,
						gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

				contaReceber.setCliente(c);
				contaReceber.setVendaCabecalho(vc);
				contaReceber.setValorDevido(valorDevido / vc.getFormaPagamento().getNumeroParcelas());
				contaReceber.setVencimento(gcGregorian.getTime());
				contaReceber.setQuitada(false);

				trx.begin();
				manager.persist(contaReceber);
				trx.commit();
			}

			JOptionPane.showMessageDialog(null, "Conta(s) a receber criada(s) com sucesso!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro na criação da conta a receber!");
		}

	}

	private void criarCaixa(VendaCabecalho vc) {

		try {
			Caixa caixa = new Caixa();

			caixa.setData(dataAtual());
			caixa.setValor(vc.getValorTotal());
			caixa.setVendaCabecalho(vc);

			trx.begin();
			manager.persist(caixa);
			trx.commit();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao cria caixa!" + e);
		}

	}

	private boolean verificaExistenciaFormaPagamento() {
		boolean retorno = true;
		try {
			// trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento");
			List<FormaPagamento> listaFormaPagamento = consulta.getResultList();
			// trx.commit();

			if (listaFormaPagamento.isEmpty()) {
				retorno = false;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao verificar exixtencia de forma de pagamento " + e);
			trx.commit();
		}

		return retorno;
	}

	private Double calcularValorTotalDesconto() {

		Double desconto = 0.0;

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			desconto = desconto + listaVendaDetalhes.get(i).getValorDesconto();
		}

		return desconto;
	}

	private Double calcularValorTotalParcial() {

		Double totalParcial = 0.0;

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			totalParcial = totalParcial + listaVendaDetalhes.get(i).getValorParcial();
		}

		return totalParcial;
	}

	private Double calcularValorTotalGeral() {

		Double valorTotalGeral = 0.0;

		for (int i = 0; i < listaVendaDetalhes.size(); i++) {
			valorTotalGeral = valorTotalGeral + listaVendaDetalhes.get(i).getValorTotal();
		}

		return valorTotalGeral;
	}

	private void calcularTotais() {

		tfSubTotal.setText(calcularValorTotalParcial().toString());
		tfDesconto.setText(calcularValorTotalDesconto().toString());
		tfTotalGeral.setText(calcularValorTotalGeral().toString());

	}

	private void baixaEstoque(VendaCabecalho vc) {

		try {
			
			Query consulta = manager.createQuery("from VendaDetalhe where venda_cabecalho_id like '" + vc.getId() + "'");
			List<VendaDetalhe> listaVendaDetalhe = consulta.getResultList();
			
			for (int i = 0; i < listaVendaDetalhe.size(); i++) {
				Produto p = listaVendaDetalhe.get(i).getProduto();
				
				p.setQuantidadeEstoque(p.getQuantidadeEstoque() - listaVendaDetalhe.get(i).getQuantidade());
				
				trx.begin();
				manager.merge(p);
				trx.commit();
			}

		} catch (Exception e) {
			trx.rollback();
			JOptionPane.showMessageDialog(null, "Erro ao dar baixa no estoque!!!");
		}
		
	}

	private boolean verificaValorMinimoFormaPagamento(Double valor) {

		boolean retorno = false;

		FormaPagamento fp = (FormaPagamento) cbFormaPagamento.getSelectedItem();

		if (valor >= fp.getValorMinimo()) {
			retorno = true;
		}

		return retorno;
	}
/*
	private void imprimirComprovante(VendaCabecalho vendaCabecalho) throws JRException {
		ChamaRelatorioReciboVenda chamaRelatorio = new ChamaRelatorioReciboVenda();

		chamaRelatorio.report("DetalhesReciboPrimeiraVia.jasper", vendaCabecalho, tableProdutos);
	}
*/
	private void buscarCreditoUsuario() {

		try {

			usuario = (Usuario) cbUsuario.getSelectedItem();

			Query consulta = manager.createQuery("from CreditoUsuario where usuario_id = '" + usuario.getId() + "'");
			List<CreditoUsuario> listaCreditoUsuario = consulta.getResultList();

			creditoUsuario = listaCreditoUsuario.get(0);
			tfCredito.setText(creditoUsuario.getValor().toString());

			// JOptionPane.showMessageDialog(null, "O crédito do vendedor é " +
			// creditoUsuario.getValor());

		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null,
					"Não foi possivel encontrar o crédito do usuario. Logo um crédito foi criado!!!");

			creditoUsuario = new CreditoUsuario();
			creditoUsuario.setUsuario(usuario);
			creditoUsuario.setValor(0.0);

			try {

				trx.begin();
				manager.persist(creditoUsuario);
				trx.commit();

				JOptionPane.showMessageDialog(null, "Crédito do usuario criado com sucesso!");
			} catch (Exception e3) {
				JOptionPane.showMessageDialog(null, "ERRO!! Crédito do usuario não criado! " + e3);
			}

		}
	}
	
	private void imprimirComprovante(VendaCabecalho vc, Cliente c, FormaPagamento f, Usuario u){
				
		//#################### INICIO CAPTURANDO ENDEREÇO ###################
		EnderecoCliente enderecoCliente = null;
		try {
			enderecoCliente = cliente.getEnderecos().get(0);
		} catch (Exception e2) {
			enderecoCliente = null;
		}
		//#################### FIM CAPTURANDO ENDEREÇO ###################
		
		//################## INICIO CAPTURANDO FORMA DE PAGAMENTO ####################
		//FormaPagamento f = (FormaPagamento) cbFormaPagamento.getSelectedItem();
		//################## FIM CAPTURANDO FORMA DE PAGAMENTO ####################
		
		//###################### INICIO PEGANDO DATA VENCIMENRTO ###################
		
		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

		
		String dtVencimento = format.format(gcGregorian.getTime());
		
		//###################### FIM PEGANDO DATA VENCIMENRTO ###################
						
		ChamaRelatorioComprovanteVenda chamaRelatorio = new ChamaRelatorioComprovanteVenda();
						
		try {							
			
			//chamaRelatorio.report((Usuario) cbUsuario.getSelectedItem(), cliente, enderecoCliente, f.getNome(), format.format(dataAtual()), dtVencimento, vcu.getId().toString(), vcu.getValorParcial().toString(), vcu.getValorDesconto().toString(), vcu.getValorTotal().toString());
			chamaRelatorio.report(u, c, enderecoCliente, f.getNome(), format.format(dataAtual()), dtVencimento, vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage() );
		}
	}
	
	private void imprimirComprovante2Via(VendaCabecalho vc, Cliente c, FormaPagamento f, Usuario u){
		
		//#################### INICIO CAPTURANDO ENDEREÇO ###################
		EnderecoCliente enderecoCliente = null;
		try {
			enderecoCliente = cliente.getEnderecos().get(0);
		} catch (Exception e2) {
			enderecoCliente = null;
		}
		//#################### FIM CAPTURANDO ENDEREÇO ###################
		
		//################## INICIO CAPTURANDO FORMA DE PAGAMENTO ####################
		//FormaPagamento f = (FormaPagamento) cbFormaPagamento.getSelectedItem();
		//################## FIM CAPTURANDO FORMA DE PAGAMENTO ####################
		
		//###################### INICIO PEGANDO DATA VENCIMENRTO ###################
		
		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

		
		String dtVencimento = format.format(gcGregorian.getTime());
		
		//###################### FIM PEGANDO DATA VENCIMENRTO ###################
						
		ChamaRelatorioComprovanteVenda2Via chamaRelatorio = new ChamaRelatorioComprovanteVenda2Via();
						
		try {							
			
			//chamaRelatorio.report((Usuario) cbUsuario.getSelectedItem(), cliente, enderecoCliente, f.getNome(), format.format(dataAtual()), dtVencimento, vcu.getId().toString(), vcu.getValorParcial().toString(), vcu.getValorDesconto().toString(), vcu.getValorTotal().toString());
			chamaRelatorio.report(u, c, enderecoCliente, f.getNome(), format.format(dataAtual()), dtVencimento, vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage() );
		}
	}
}