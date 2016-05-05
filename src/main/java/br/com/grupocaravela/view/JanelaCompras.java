package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
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
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarFornecedor;
import br.com.grupocaravela.dialog.BuscarProduto;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.CompraCabecalho;
import br.com.grupocaravela.objeto.CompraDetalhe;
import br.com.grupocaravela.objeto.ContaPagar;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Fornecedor;
import br.com.grupocaravela.objeto.Produto;
import br.com.grupocaravela.objeto.ProdutoLote;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaDetalhe;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelCompraCabecalho;
import br.com.grupocaravela.tablemodel.TableModelCompraDetalhe;
import br.com.grupocaravela.tablemodel.TableModelPagamentoCompra;
import br.com.grupocaravela.util.UsuarioLogado;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.beans.PropertyChangeEvent;

public class JanelaCompras extends JFrame {

	// private EntityManagerProducer entityManagerProducer = new
	// EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCompraCabecalho tableModelCompraCabecalho;
	private TableModelCompraDetalhe tableModelCompraDetalhe;
	private TableModelPagamentoCompra tableModelPagamentoCompra;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableCompraCabecalho;
	private JTextField tfId;
	private JTextField tfNumeroNota;

	private CompraCabecalho compraCabecalho;
	// private CompraDetalhe compraDetalhe;
	private List<CompraDetalhe> listaCompraDetalhe = new ArrayList<>();
	private List<ContaPagar> listaContasPagar = new ArrayList<>();
	
	private JTextField tfFornecedor;
	private JTable tableCompraDetalhes;
	private JTextField tfValorUnitario;
	private JTextField tfValorTotal;
	private JTextField tfQuantidade;
	private JTextField tfNome;
	private JTextField tfUnidade;
	private JTextField tfLote;
	private JTextField tfCodProduto;
	private JDateChooser dcDataCompra;
	private JDateChooser dcFabricacao;
	private JDateChooser dcDataEntrega;
	private JButton btnBuscarFornecedor;

	private Fornecedor fornecedorImportado = null;
	private Usuario usuario;
	private Produto produtoImportado;
	private JComboBox cbUsuario;
	private JTextField tfTotal;
	private JDateChooser dcValidade;
	private JLabel lblEstadoDaCompra;

	//private boolean finalizada = false;
	//private boolean entregue = false;
	private JButton btnAddProduto;

	// private FormaPagamento formaPagamento = null;
	private JButton btnRecebercompra;
	private JTable tablePagamento;
	private JTextField tfValorCompra;
	private JTextField tfNumeroParcelas;
	private JTextField tfDataCompraPagamento;
	private JTextField tfValorParcela;
	private JTextField tfParcelaNumero;
	private JButton btnDelProduto;
	private JButton btnAltQuantidade;
	private JButton btnAltValor;
	
	private boolean compraFinalizada;
	private boolean compraRecebida;
	private JButton btnFinalizarCompra;
	private JDateChooser dcVencimentoParcela;
	
	private ContaPagar cp;
	private JButton btnOk;
	private JButton btnGerarContaPagar;
	private JTextPane tpObservacao;
	private JButton btnCriarContaA;
	private JButton btnOk_1;
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCompras frame = new JanelaCompras();
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
	public JanelaCompras() {

		criarJanela();
		carregarTableModel();
		tamanhoColunas();

		tfValorCompra.setDisabledTextColor(Color.BLACK);
		tfDataCompraPagamento.setDisabledTextColor(Color.BLACK);
		tfDataCompraPagamento.setDisabledTextColor(Color.BLACK);
		
		
		iniciaConexao();

		carregajcbUsuario();

		carregarTodasCompras();

		// Evento ao abrir a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				tfLocalizar.requestFocus();
				super.windowActivated(e);
			}
		});
		/*
		 * //Evento ao fechar a janela addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) {
		 * 
		 * try { factory.close();
		 * 
		 * } catch (Exception e2) { // TODO: handle exception } } });
		 */
	}

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void criarJanela() {

		setTitle("Compras");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 930, 727);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.PREFERRED_SIZE, 918, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista de compras", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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

		tableCompraCabecalho = new JTable();
		tableCompraCabecalho.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableCompraCabecalho);
		tableCompraCabecalho.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					compraCabecalho = tableModelCompraCabecalho.getCompraCabecalho(tableCompraCabecalho.getSelectedRow());

					limparCamposNovaCompra();
					limparTabelaDetalhes();

					carregarCampos(compraCabecalho);
					
					compraFinalizada = compraCabecalho.getFinalizada();
					compraRecebida = compraCabecalho.getEntregue();
					
					verificaEstadoCompra();
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(0, false);
					
					carregarContaPagar(compraCabecalho);
				}
			}
		});

		JButton btnNovo = new JButton("Nova compra");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// formaPagamento = null;
				fornecedorImportado = null;

				limparCamposNovaCompra();
				limparTabelaDetalhes();

				compraCabecalho = new CompraCabecalho();
				listaCompraDetalhe.clear();

				bucarUsuarioLogado();

				dcDataCompra.setDate(dataAtual());

				compraFinalizada = false;
				compraRecebida= false;
				
				verificaEstadoCompra();

				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnNovo.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		JButton btnExcluir = new JButton("Excluir compra");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				compraCabecalho = tableModelCompraCabecalho.getCompraCabecalho(tableCompraCabecalho.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão da compraCabecalho no valor de R$"
								+ compraCabecalho.getValorCompra() + "?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirCompraCabecalho(compraCabecalho);
					limparTabelaCabecalho();
				}
			}
		});
		btnExcluir.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		JButton btnDetalhes = new JButton("Detalhes da compra");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				compraCabecalho = tableModelCompraCabecalho.getCompraCabecalho(tableCompraCabecalho.getSelectedRow());

				limparCamposNovaCompra();
				limparTabelaDetalhes();

				carregarCampos(compraCabecalho);
				
				compraFinalizada = compraCabecalho.getFinalizada();
				compraRecebida = compraCabecalho.getEntregue();
				
				verificaEstadoCompra();
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				
				carregarContaPagar(compraCabecalho);

			}
		});
		btnDetalhes
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(lblLocalizar).addGap(12)
								.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE).addGap(12)
								.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(scrollPane,
								GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING,
								gl_panel.createSequentialGroup().addContainerGap(336, Short.MAX_VALUE)
										.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 175,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12).addComponent(btnDetalhes).addGap(12).addComponent(btnNovo,
												GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(12)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblLocalizar)
						.addGroup(gl_panel.createSequentialGroup().addGap(3)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(btnBuscar))))
				.addGap(12).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE).addGap(8)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(btnExcluir)
						.addComponent(btnDetalhes).addComponent(btnNovo))
				.addContainerGap()));
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes da compra", null, panel_1, null);

		JLabel lblId = new JLabel("id:");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("Nº Nota:");

		tfNumeroNota = new JTextField();
		tfNumeroNota.setColumns(10);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (fornecedorImportado == null) {
					JOptionPane.showMessageDialog(null, "Para finalizar é necessario informar o fornecedor!!!");
				} else {

					// listaCompraDetalhe.add(cd);

					// salvarCompraCabecalho(compraCabecalho);
					/*
					 * for (int i = 0; i < listaCompraDetalhe.size(); i++) {
					 * CompraDetalhe c = listaCompraDetalhe.get(i);
					 * tableModelCompraDetalhe.addCompraDetalhe(c); }
					 */
					salvarCompraCabecalho(compraCabecalho);
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(0, true);
					tfLocalizar.requestFocus();
				}

			}
		});
		btnSalvar.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				limparCamposNovaCompra();
				tfLocalizar.requestFocus();

			}
		});
		btnCancelar.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		dcDataCompra = new JDateChooser();

		dcDataEntrega = new JDateChooser();

		JLabel lblDataCompra = new JLabel("Data compra:");

		JLabel lblDataEntrega = new JLabel("Data entrega:");

		JLabel lblFornecedor = new JLabel("Fornecedor:");

		tfFornecedor = new JTextField();
		tfFornecedor.setEditable(false);
		tfFornecedor.setColumns(10);

		btnBuscarFornecedor = new JButton("...");
		btnBuscarFornecedor.addActionListener(new ActionListener() {
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

						acaoBuscarFornecedor();

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

		JLabel lblUsuario = new JLabel("Usuario:");

		cbUsuario = new JComboBox();

		JPanel panel_2 = new JPanel();

		JLabel lblEstado = new JLabel("Estado:");

		lblEstadoDaCompra = new JLabel("Estado da compra");

		JLabel lblTotal = new JLabel("Total:");

		tfTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfTotal.setEditable(false);
		tfTotal.setColumns(10);

		btnFinalizarCompra = new JButton("Finalizar Compra");
		btnFinalizarCompra.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Gostaria de finalizar a compra?",
						"Finaliza Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					
					compraFinalizada = true;
					verificaEstadoCompra();
				}

				if (compraFinalizada) {
					btnRecebercompra.setEnabled(true);
					btnFinalizarCompra.setEnabled(false);
				}
			}
		});
		btnFinalizarCompra
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/atender_24.png")));

		btnRecebercompra = new JButton("ReceberCompra");
		btnRecebercompra.setEnabled(false);
		btnRecebercompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma o recebimento da compra?",
						"Finaliza Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {

					compraRecebida = true;	
					dcDataEntrega.setDate(dataAtual());;
					
					//tfDataCompraPagamento.setText(compraCabecalho.getDataCompra().toString());
					//tfValorCompra.setText(compraCabecalho.getValorCompra().toString());
					//tfNumeroParcelas.setText("1");
					//compraCabecalho.setEntregue(true);
					verificaEstadoCompra();				
					
					/*
					
					tabbedPane.setSelectedIndex(2);
					//tabbedPane.setEnabledAt(1, true);
					//tabbedPane.setEnabledAt(0, false);
					
					ContaPagar cp = new ContaPagar();
					
					cp.setDataCriacao(dataAtual());
					cp.setCompraCabecalho(compraCabecalho);
					cp.setValor(Double.parseDouble(tfTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
					
					listaContasPagar.add(cp);
					
					carregarTabelaContaPagar();
					
					*/
				}
			}
		});
		btnRecebercompra
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/receber_24.png")));
		
		btnGerarContaPagar = new JButton("Gerar Conta Pagar");
		btnGerarContaPagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Gerar conta(s) a pagar para esta compra?",
						"Gerar conta(s) a pagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
					
					salvarCompraCabecalho(compraCabecalho);

					tfDataCompraPagamento.setText(compraCabecalho.getDataCompra().toString());
					tfValorCompra.setText(compraCabecalho.getValorCompra().toString());
					tfNumeroParcelas.setText("1");
					
					tabbedPane.setSelectedIndex(2);
					// tabbedPane.setEnabledAt(1, true);
					// tabbedPane.setEnabledAt(0, false);

					ContaPagar cp = new ContaPagar();

					cp.setDataCriacao(dataAtual());
					cp.setCompraCabecalho(compraCabecalho);
					cp.setValor(Double
							.parseDouble(tfTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));

					listaContasPagar.add(cp);

					carregarTabelaContaPagar();
				}

			}
		});
		btnGerarContaPagar.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/gastos2_24.png")));
		btnGerarContaPagar.setEnabled(false);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(lblId)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblNome)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfNumeroNota, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblFornecedor)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfFornecedor, GroupLayout.PREFERRED_SIZE, 292, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(btnBuscarFornecedor))
							.addGroup(gl_panel_1.createSequentialGroup()
								.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel_1.createSequentialGroup()
										.addComponent(lblDataCompra)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(dcDataCompra, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblDataEntrega)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(dcDataEntrega, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblUsuario))
									.addGroup(gl_panel_1.createSequentialGroup()
										.addComponent(lblEstado)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblEstadoDaCompra)
										.addGap(91)
										.addComponent(lblTotal)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbUsuario, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnFinalizarCompra)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnRecebercompra)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnGerarContaPagar)
							.addPreferredGap(ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
							.addComponent(btnCancelar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSalvar)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(dcDataEntrega, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblId)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNome)
								.addComponent(tfNumeroNota, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblFornecedor)
								.addComponent(tfFornecedor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBuscarFornecedor))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDataCompra)
								.addComponent(dcDataCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDataEntrega)
								.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblUsuario)
									.addComponent(cbUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEstado)
						.addComponent(lblEstadoDaCompra)
						.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTotal))
					.addGap(16)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
					.addGap(12)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFinalizarCompra)
						.addComponent(btnRecebercompra)
						.addComponent(btnSalvar)
						.addComponent(btnCancelar)
						.addComponent(btnGerarContaPagar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);

		JScrollPane scrollPane_1 = new JScrollPane();

		tableCompraDetalhes = new JTable();
		tableCompraDetalhes.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (tableCompraDetalhes.getSelectedRow() != -1) {
					btnDelProduto.setEnabled(true);
					btnAltQuantidade.setEnabled(true);
					btnAltValor.setEnabled(true);
					btnAddProduto.setEnabled(true);

				} else {
					btnDelProduto.setEnabled(false);
					btnAltQuantidade.setEnabled(false);
					btnAltValor.setEnabled(false);
					btnAddProduto.setEnabled(false);
				}
				
				int linha = tableCompraDetalhes.getSelectedRow();
				int linhaReal = tableCompraDetalhes.convertRowIndexToModel(linha);
				
				CompraDetalhe cd = tableModelCompraDetalhe.getCompraDetalhe(linhaReal);
				
				produtoImportado = cd.getProduto();
				
				tfCodProduto.setText(produtoImportado.getCodigo());
				tfNome.setText(produtoImportado.getNome());
				tfUnidade.setText(produtoImportado.getUnidade().getNome());
				tfQuantidade.setText(cd.getProdutoLote().getQuantidade().toString());
				tfValorUnitario.setText(cd.getProdutoLote().getValorCompra().toString());
				tfValorTotal.setText(cd.getValorTotal().toString());
				tfLote.setText(cd.getProdutoLote().getLote());
				dcFabricacao.setDate(cd.getProdutoLote().getFabricacao());
				dcFabricacao.setDate(cd.getProdutoLote().getFabricacao());
				
			}
		});
		scrollPane_1.setViewportView(tableCompraDetalhes);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));

		JButton btnImpProduto = new JButton("Imp. Produto");
		btnImpProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				produtoImportado = null;

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
		btnImpProduto
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/mais_24.png")));

		btnDelProduto = new JButton("Del. Produto");
		btnDelProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a exclusão do produto " + tableModelCompraDetalhe
								.getCompraDetalhe(tableCompraDetalhes.getSelectedRow()).getProduto().getNome()
								+ " da lista?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					int linha = tableCompraDetalhes.getSelectedRow();
					int linhaReal = tableCompraDetalhes.convertRowIndexToModel(linha);

					// acaoExcluirProduto(tableModelListaVendas.getVendaDetalhe(linhaReal));
					acaoExcluirProduto(linha);
					calcularTotais();
					
					limparCamposNovoProduto();
				}

			}
		});
		btnDelProduto.setEnabled(false);
		btnDelProduto.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		btnAltQuantidade = new JButton("Alt. Quantidade");
		btnAltQuantidade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Double quantidade = Double.parseDouble(JOptionPane.showInputDialog("Infome a Quantidade",
						tableModelCompraDetalhe.getValueAt(tableCompraDetalhes.getSelectedRow(), 3)));

				int linha = tableCompraDetalhes.getSelectedRow();
				int linhaReal = tableCompraDetalhes.convertRowIndexToModel(linha);

				acaoAlterarQuantidade(linha, tableModelCompraDetalhe.getCompraDetalhe(linha), quantidade);
				
				calcularTotais();
				
				limparCamposNovoProduto();
			}
		});
		btnAltQuantidade.setEnabled(false);
		btnAltQuantidade.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));

		btnAltValor = new JButton("Alt. Valor");
		btnAltValor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Double valor = Double.parseDouble(JOptionPane.showInputDialog("Infome o valor",
						tableModelCompraDetalhe.getValueAt(tableCompraDetalhes.getSelectedRow(), 4)));

				int linha = tableCompraDetalhes.getSelectedRow();
				int linhaReal = tableCompraDetalhes.convertRowIndexToModel(linha);

				acaoAlterarValor(linha, tableModelCompraDetalhe.getCompraDetalhe(linha), valor);
				
				calcularTotais();
				
				limparCamposNovoProduto();
				
			}
		});
		btnAltValor.setEnabled(false);
		btnAltValor.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/forma_pagamento_24.png")));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(btnImpProduto)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnDelProduto)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAltQuantidade)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAltValor)
					.addContainerGap(249, Short.MAX_VALUE))
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
				.addComponent(panel_3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 911, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(6)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnDelProduto, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnImpProduto, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(btnAltQuantidade)
						.addComponent(btnAltValor))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE))
		);

		JLabel lblCod = new JLabel("Cod.:");

		tfCodProduto = new JTextField();
		tfCodProduto.setEditable(false);
		tfCodProduto.setColumns(10);

		JLabel lblNome_1 = new JLabel("Nome:");

		tfNome = new JTextField();
		tfNome.setEditable(false);
		tfNome.setColumns(10);

		JLabel lblUnidade = new JLabel("Unidade:");

		tfUnidade = new JTextField();
		tfUnidade.setEditable(false);
		tfUnidade.setColumns(10);

		tfLote = new JTextField();
		tfLote.setColumns(10);

		JLabel lblLote = new JLabel("Lote:");

		tfValorTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorTotal.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				Double qtd = 0.0;

				try {
					qtd = Double.parseDouble(tfQuantidade.getText());
				} catch (Exception e2) {
					qtd = 0.0;
				}

				Double vt = 0.0;

				try {
					vt = Double
							.parseDouble(tfValorTotal.getText().replace("R$ ", "").replace(".", "").replace(",", "."));
				} catch (Exception e2) {
					vt = 0.0;
				}

				Double r = vt / qtd;

				tfValorUnitario.setText(r.toString());

			}
		});
		tfValorTotal.setColumns(10);

		JLabel lblValorTotal = new JLabel("Valor total:");

		tfValorUnitario = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorUnitario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {

				tfLote.requestFocus();

				Double qtd = 0.0;
				try {
					qtd = Double.parseDouble(tfQuantidade.getText().replace(".", "").replace(",", "."));	
				} catch (Exception e2) {
					qtd = 0.0;
				}
				
				Double vu = Double
						.parseDouble(tfValorUnitario.getText().replace("R$ ", "").replace(".", "").replace(",", "."));

				Double t = qtd * vu;

				tfValorTotal.setText(t.toString());

			}
		});
		tfValorUnitario.setColumns(10);

		JLabel lblValorUnitrio = new JLabel("Valor unitário:");

		tfQuantidade = new JTextField();
		tfQuantidade.setColumns(10);

		JLabel lblQuantidade = new JLabel("Quantidade:");

		JLabel lblFabricacao = new JLabel("Fabricação:");

		dcFabricacao = new JDateChooser();

		btnAddProduto = new JButton("Add Prod. Lista");
		btnAddProduto.setEnabled(false);
		btnAddProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparTabelaDetalhes();
				addProduto(produtoImportado);
				limparCamposNovoProduto();
				// btnAddProduto.setEnabled(false);
			}
		});
		btnAddProduto
				.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		JLabel label = new JLabel("Validade:");

		dcValidade = new JDateChooser();
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblFabricacao).addGap(12)
										.addComponent(dcFabricacao, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(label, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(dcValidade, GroupLayout.PREFERRED_SIZE, 161,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_3.createSequentialGroup()
										.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
												.addGroup(gl_panel_3.createSequentialGroup().addComponent(lblQuantidade)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(tfQuantidade, 0, 0, Short.MAX_VALUE))
												.addGroup(gl_panel_3.createSequentialGroup()
														.addComponent(lblCod).addGap(12).addComponent(tfCodProduto,
																GroupLayout.PREFERRED_SIZE, 138,
																GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_3.createSequentialGroup().addGap(18)
														.addComponent(lblNome_1).addGap(12)
														.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, 426,
																GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(lblUnidade).addGap(12)
														.addComponent(tfUnidade, GroupLayout.PREFERRED_SIZE, 57,
																GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_panel_3.createSequentialGroup()
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(lblValorUnitrio).addGap(12)
														.addComponent(tfValorUnitario, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(lblValorTotal).addGap(12)
														.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(lblLote).addGap(12)
														.addComponent(tfLote, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
								.addComponent(btnAddProduto))
						.addContainerGap(36, Short.MAX_VALUE)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblCod))
								.addComponent(tfCodProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblNome_1))
								.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblUnidade))
								.addComponent(tfUnidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(12)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblQuantidade))
								.addComponent(tfQuantidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblValorUnitrio))
								.addComponent(tfValorUnitario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblValorTotal))
								.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_3.createSequentialGroup().addGap(2).addComponent(lblLote))
								.addComponent(tfLote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(12)
						.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addComponent(lblFabricacao)
								.addComponent(dcFabricacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(label).addComponent(dcValidade, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addComponent(btnAddProduto, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		panel_3.setLayout(gl_panel_3);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Pagamento", null, panel_4, null);

		JScrollPane scrollPane_2 = new JScrollPane();

		tablePagamento = new JTable();
		tablePagamento.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int linha = tablePagamento.getSelectedRow();
				int linhaReal = tablePagamento.convertRowIndexToModel(linha);
				
				cp = tableModelPagamentoCompra.getContaPagar(linhaReal);
				
				tfValorParcela.setText(cp.getValor().toString());
				dcVencimentoParcela.setDate(cp.getDataVencimento());
				
				tfParcelaNumero.setText((tablePagamento.getSelectedRow()+1) + "/" + tablePagamento.getRowCount());
				
			}
		});
		scrollPane_2.setViewportView(tablePagamento);

		JPanel panel_5 = new JPanel();

		JLabel lblValorDaCompra = new JLabel("Valor da compra:");

		JLabel lblDataCompra_1 = new JLabel("Data Compra");

		tfValorCompra = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorCompra.setEnabled(false);
		tfValorCompra.setEditable(false);
		tfValorCompra.setColumns(10);

		JLabel lblNParcelas = new JLabel("nº Parcelas:");

		tfNumeroParcelas = new JTextField();
		tfNumeroParcelas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					
					limparTabelaContasPagar();				
					alterarParcelasContaPagar(compraCabecalho.getValorCompra(), Integer.parseInt(tfNumeroParcelas.getText()));				
					carregarTabelaContaPagar();
					
					//btnOk.requestFocus();
					
				}
			}
		});
		
		tfNumeroParcelas.setColumns(10);

		tfDataCompraPagamento = new JTextField();
		tfDataCompraPagamento.setEditable(false);
		tfDataCompraPagamento.setColumns(10);

		JLabel lblValorParcela = new JLabel("Valor Parcela:");

		tfValorParcela = new DecimalFormattedField(DecimalFormattedField.REAL);
		
		tfValorParcela.setColumns(10);

		JLabel lblVencimento = new JLabel("Vencimento:");

		dcVencimentoParcela = new JDateChooser();
		

		JLabel lblObservao = new JLabel("Observação:");

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(0, 0, 0)));

		JLabel lblParcela = new JLabel("Parcela:");

		tfParcelaNumero = new JTextField();
		tfParcelaNumero.setEditable(false);
		tfParcelaNumero.setColumns(10);
		
		btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				limparTabelaContasPagar();				
				alterarParcelasContaPagar(compraCabecalho.getValorCompra(), Integer.parseInt(tfNumeroParcelas.getText()));				
				carregarTabelaContaPagar();
			}
		});
		
		btnOk_1 = new JButton("Ok");
		btnOk_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cp.setValor(Double.parseDouble(tfValorParcela.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
				cp.setDataVencimento(dcVencimentoParcela.getDate());
				cp.setObservacao(tpObservacao.getText());
				
				listaContasPagar.set(tablePagamento.getSelectedRow(), cp);
				
				limparTabelaContasPagar();
				carregarTabelaContaPagar();
			}
		});
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_6, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE)
								.addGroup(gl_panel_4.createSequentialGroup()
									.addComponent(lblValorParcela)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfValorParcela, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblVencimento)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcVencimentoParcela, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblParcela)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfParcelaNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnOk_1))
								.addComponent(lblObservao))
							.addContainerGap())
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(lblNParcelas)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfNumeroParcelas, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnOk)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValorDaCompra)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfValorCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblDataCompra_1)
							.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
							.addComponent(tfDataCompraPagamento, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
							.addGap(104))))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNParcelas)
						.addComponent(tfNumeroParcelas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfDataCompraPagamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnOk)
						.addComponent(tfValorCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblValorDaCompra)
						.addComponent(lblDataCompra_1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblValorParcela)
								.addComponent(tfValorParcela, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblVencimento))
							.addComponent(dcVencimentoParcela, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblParcela)
							.addComponent(tfParcelaNumero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnOk_1)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblObservao)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
					.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);

		tpObservacao = new JTextPane();
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE));
		panel_6.setLayout(gl_panel_6);

		btnCriarContaA = new JButton("Criar Conta a Receber");
		btnCriarContaA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a geração da(s) contas a pagar no valor total de " + tfValorCompra.getText() + "?",
						"Gerar conta(s) a pagar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {
				
					criarContaPagar(listaContasPagar);
					
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(2, false);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(0, true);
					tfLocalizar.requestFocus();
				}
				
			}
		});
		btnCriarContaA.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar_1 = new JButton("Cancelar");
		btnCancelar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(2, false);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				limparCamposNovaCompra();
				tfLocalizar.requestFocus();
			}
		});
		btnCancelar_1.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5
				.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_panel_5.createSequentialGroup().addContainerGap(537, Short.MAX_VALUE)
								.addComponent(btnCancelar_1).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(btnCriarContaA).addContainerGap()));
		gl_panel_5
				.setVerticalGroup(
						gl_panel_5.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
								gl_panel_5.createSequentialGroup().addContainerGap(71, Short.MAX_VALUE)
										.addGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING)
												.addComponent(btnCancelar_1).addComponent(btnCriarContaA))
										.addContainerGap()));
		panel_5.setLayout(gl_panel_5);
		panel_4.setLayout(gl_panel_4);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelCompraCabecalho = new TableModelCompraCabecalho();
		this.tableCompraCabecalho.setModel(tableModelCompraCabecalho);

		this.tableModelCompraDetalhe = new TableModelCompraDetalhe();
		this.tableCompraDetalhes.setModel(tableModelCompraDetalhe);

		this.tableModelPagamentoCompra = new TableModelPagamentoCompra();
		this.tablePagamento.setModel(tableModelPagamentoCompra);
		
		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		tableCompraCabecalho.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);

		tableCompraDetalhes.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);
		tableCompraDetalhes.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);

		tablePagamento.getColumnModel().getColumn(0).setCellRenderer(cellRendererCustomMoeda);
	}

	private void carregarTabela() {
		try {

			// trx.begin();
			Query consulta = manager.createQuery("from CompraCabecalho");
			List<CompraCabecalho> listaCompraCabecalhos = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCompraCabecalhos.size(); i++) {
				CompraCabecalho c = listaCompraCabecalhos.get(i);
				tableModelCompraCabecalho.addCompraCabecalho(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de compraCabecalhos: " + e);
		}
	}

	private void limparTabelaCabecalho() {
		while (tableCompraCabecalho.getModel().getRowCount() > 0) {
			tableModelCompraCabecalho.removeCompraCabecalho(0);
		}
	}

	private void limparTabelaDetalhes() {
		while (tableCompraDetalhes.getModel().getRowCount() > 0) {
			tableModelCompraDetalhe.removeCompraDetalhe(0);
		}
	}
	
	private void limparTabelaContasPagar() {
		while (tablePagamento.getModel().getRowCount() > 0) {
			tableModelPagamentoCompra.removeContaPagar(0);
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
					Logger.getLogger(JanelaCompras.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabelaCabecalho();

				try {

					// trx.begin();
					Query consulta = manager.createQuery("from CompraCabecalho");
					List<CompraCabecalho> listaCompraCabecalhos = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaCompraCabecalhos.size(); i++) {
						CompraCabecalho c = listaCompraCabecalhos.get(i);
						tableModelCompraCabecalho.addCompraCabecalho(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de compraCabecalhos: " + e);
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

	private void salvarCompraCabecalho(CompraCabecalho c) {

		c.setDataCompra(dcDataCompra.getDate());
		try {
			c.setDataRecebimento(dcDataEntrega.getDate());
		} catch (Exception e) {
			c.setDataRecebimento(null);
		}

		c.setFornecedor(fornecedorImportado);
		c.setNumeroNota(tfNumeroNota.getText());
		c.setUsuario((Usuario) cbUsuario.getSelectedItem());
		c.setValorCompra(Double.parseDouble(tfTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));

		c.setFinalizada(compraFinalizada);
		c.setEntregue(compraRecebida);
		
		try {
			trx.begin();
			manager.persist(c);
			for (int i = 0; i < listaCompraDetalhe.size(); i++) {
				ProdutoLote pl = listaCompraDetalhe.get(i).getProdutoLote();
				CompraDetalhe cd = listaCompraDetalhe.get(i);
				manager.persist(pl);
				manager.persist(cd);
			}
			trx.commit();

			JOptionPane.showMessageDialog(null, "CompraCabecalho foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO ao salvar a compra! " + e);
		}
	}

	private void carregarCampos(CompraCabecalho c) {

		tfId.setText(c.getId().toString());
		fornecedorImportado = c.getFornecedor();
		tfFornecedor.setText(c.getFornecedor().getRazaoSocial());

		usuario = c.getUsuario();
		cbUsuario.setSelectedItem(c.getUsuario());

		tfNumeroNota.setText(c.getNumeroNota());
		dcDataCompra.setDate(c.getDataCompra());
		dcDataEntrega.setDate(c.getDataRecebimento());

		tfTotal.setText(c.getValorCompra().toString());

		compraFinalizada = c.getFinalizada();
		compraRecebida = c.getEntregue();

		verificaEstadoCompra();

		// listaCompraDetalhe = carregarListaItens(c);
		carregarListaItens(c);

		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe v = listaCompraDetalhe.get(i);
			tableModelCompraDetalhe.addCompraDetalhe(v);
		}

	}

	private void carregarListaItens(CompraCabecalho cc) {

		// List<CompraDetalhe> retorno = new ArrayList<>();

		listaCompraDetalhe.clear();

		try {
			Query consulta = manager
					.createQuery("FROM CompraDetalhe WHERE compra_cabecalho_id LIKE '" + cc.getId().toString() + "'");
			List<CompraDetalhe> listaCd = consulta.getResultList();

			for (int i = 0; i < listaCd.size(); i++) {
				CompraDetalhe cd = listaCd.get(i);
				// retorno.add(cd);
				listaCompraDetalhe.add(cd);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a lista de compraDetalhes: " + e);
		}

		// return retorno;

	}

	private void limparCamposNovaCompra() {
		tfId.setText("");
		tfCodProduto.setText("");
		tfNome.setText("");
		tfFornecedor.setText("");
		tfLote.setText("");
		tfNumeroNota.setText("");
		tfQuantidade.setText("");
		tfUnidade.setText("");
		tfTotal.setText("0");
		tfValorTotal.setText("0");
		tfValorUnitario.setText("0");
		tpObservacao.setText("");

		dcDataCompra.setDate(null);
		dcDataEntrega.setDate(null);
		dcFabricacao.setDate(null);
		dcValidade.setDate(null);

		cbUsuario.setSelectedIndex(-1);
	}

	private void limparCamposNovoProduto() {

		tfCodProduto.setText("");
		tfNome.setText("");
		tfLote.setText("");
		tfQuantidade.setText("");
		tfUnidade.setText("");
		tfValorTotal.setText("0");
		tfValorUnitario.setText("0");

		dcFabricacao.setDate(null);
		dcValidade.setDate(null);

	}

	private void excluirCompraCabecalho(CompraCabecalho c) {
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "CompraCabecalho foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void acaoBuscarFornecedor() {
		BuscarFornecedor buscarFornecedor = new BuscarFornecedor();

		buscarFornecedor.setModal(true);
		buscarFornecedor.setLocationRelativeTo(null);
		buscarFornecedor.setVisible(true);

		fornecedorImportado = buscarFornecedor.getFornecedor();

		tfFornecedor.setText(fornecedorImportado.getRazaoSocial());
	}

	private void bucarUsuarioLogado() {

		try {
			cbUsuario.setSelectedItem(UsuarioLogado.getUsuario()); // tenta
																	// selecionar
																	// o usuario
																	// logado
			usuario = UsuarioLogado.getUsuario();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void carregajcbUsuario() {

		cbUsuario.removeAllItems();

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from Usuario ORDER BY nome ASC");
			List<Usuario> listaUsuarios = consulta.getResultList();
			// trx.commit();

			// JOptionPane.showMessageDialog(null, listaUsuarios.size());

			for (int i = 0; i < listaUsuarios.size(); i++) {

				Usuario u = listaUsuarios.get(i);
				cbUsuario.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox do usuário! " + e);
			trx.commit();
		}
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}

	private void acaoBuscarProduto() {
		BuscarProduto buscarProduto = new BuscarProduto();

		buscarProduto.setModal(true);
		buscarProduto.setLocationRelativeTo(null);
		buscarProduto.setVisible(true);

		produtoImportado = buscarProduto.getProduto();

		tfCodProduto.setText(produtoImportado.getCodigo());
		tfNome.setText(produtoImportado.getNome());
		tfUnidade.setText(produtoImportado.getUnidade().getNome());

		if (produtoImportado != null) {
			btnAddProduto.setEnabled(true);
		}

		tfQuantidade.requestFocus();

	}

	private void addProduto(Produto p) {

		limparTabelaDetalhes();
		
		if (verificaIgualdadeProduto(p) < 0) {
			
			ProdutoLote pl = new ProdutoLote();

			pl.setFabricacao(dcFabricacao.getDate());

			pl.setLote(tfLote.getText());

			try {
				pl.setQuantidade(Double.parseDouble(tfQuantidade.getText()));
			} catch (Exception e) {
				pl.setQuantidade(0.0);
			}
			

			pl.setValorCompra(
					Double.parseDouble(tfValorUnitario.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));

			pl.setValidade(dcValidade.getDate());

			pl.setProduto(p);
			p.getProdutoLote().add(pl);

			CompraDetalhe cd = new CompraDetalhe();
			cd.setProduto(p);
			cd.setValorTotal(
					Double.parseDouble(tfValorTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			cd.setCompraCabecalho(compraCabecalho);
			cd.setProdutoLote(pl);

			listaCompraDetalhe.add(cd);
		
		}else{
			
			ProdutoLote pl = listaCompraDetalhe.get(verificaIgualdadeProduto(p)).getProdutoLote();
			CompraDetalhe cd = listaCompraDetalhe.get(verificaIgualdadeProduto(p));

			pl.setFabricacao(dcFabricacao.getDate());
			pl.setValidade(dcValidade.getDate());

			pl.setLote(tfLote.getText());

			try {
				pl.setQuantidade(Double.parseDouble(tfQuantidade.getText()));
			} catch (Exception e) {
				pl.setQuantidade(0.0);
			}
			

			pl.setValorCompra(
					Double.parseDouble(tfValorUnitario.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));

			cd.setProdutoLote(pl);
			cd.setValorTotal(pl.getQuantidade() * pl.getValorCompra());

			listaCompraDetalhe.set(verificaIgualdadeProduto(p), cd);
		}	
		
		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe c = listaCompraDetalhe.get(i);
			tableModelCompraDetalhe.addCompraDetalhe(c);
		}
		
		calcularTotais();		
		
	}

	private void carregarTodasCompras() {
		try {

			Query consulta = manager.createQuery("from CompraCabecalho");
			List<CompraCabecalho> listaCompraCabecalhos = consulta.getResultList();

			for (int i = 0; i < listaCompraCabecalhos.size(); i++) {
				CompraCabecalho c = listaCompraCabecalhos.get(i);
				tableModelCompraCabecalho.addCompraCabecalho(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de compraCabecalhos: " + e);
		}
	}

	private void verificaEstadoCompra() {
/*
		try {
			finalizada = compraCabecalho.getFinalizada();
		} catch (Exception e) {
			finalizada = false;
		}

		try {
			entregue = compraCabecalho.getEntregue();
		} catch (Exception e) {
			entregue = false;
		}
*/
		btnFinalizarCompra.setEnabled(true);
		btnRecebercompra.setEnabled(false);
		btnGerarContaPagar.setEnabled(false);
		
		lblEstadoDaCompra.setText("ABERTO");
		lblEstadoDaCompra.setForeground(Color.ORANGE);

		if (compraFinalizada) {
			lblEstadoDaCompra.setText("FINALIZADA!");
			lblEstadoDaCompra.setForeground(Color.GREEN);
			
			btnFinalizarCompra.setEnabled(false);
			btnRecebercompra.setEnabled(true);
			btnGerarContaPagar.setEnabled(false);
		}

		if (compraRecebida) {
			lblEstadoDaCompra.setText("ENTREGUE!");
			lblEstadoDaCompra.setForeground(Color.BLUE);
			btnFinalizarCompra.setEnabled(false);
			btnRecebercompra.setEnabled(false);
			btnGerarContaPagar.setEnabled(true);
		}
	}

	private void acaoExcluirProduto(int index) {

		limparTabelaDetalhes();

		listaCompraDetalhe.remove(index);

		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe v = listaCompraDetalhe.get(i);
			tableModelCompraDetalhe.addCompraDetalhe(v);
		}
	}

	private void calcularTotais() {

		Double total = 0.0;

		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe c = listaCompraDetalhe.get(i);
			//tableModelCompraDetalhe.addCompraDetalhe(c);

			total = total + c.getValorTotal();
		}

		tfTotal.setText(total.toString());
	}

	private void acaoAlterarQuantidade(int index, CompraDetalhe vd, Double qtd) {

		try {

			limparTabelaDetalhes();

			ProdutoLote pl = vd.getProdutoLote();

			pl.setQuantidade(qtd);

			vd.setProdutoLote(pl);
			vd.setValorTotal(qtd * pl.getValorCompra());

			listaCompraDetalhe.set(index, vd);
				
			for (int i = 0; i < listaCompraDetalhe.size(); i++) {
				CompraDetalhe c = listaCompraDetalhe.get(i);
				tableModelCompraDetalhe.addCompraDetalhe(c);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao alterar a quantidade do item selecionado! " + e);
		}

	}
	
	private void acaoAlterarValor(int index, CompraDetalhe vd, Double valor) {

		try {

			limparTabelaDetalhes();

			ProdutoLote pl = vd.getProdutoLote();

			pl.setValorCompra(valor);

			vd.setProdutoLote(pl);
			vd.setValorTotal(valor * pl.getQuantidade());

			listaCompraDetalhe.set(index, vd);
				
			for (int i = 0; i < listaCompraDetalhe.size(); i++) {
				CompraDetalhe c = listaCompraDetalhe.get(i);
				tableModelCompraDetalhe.addCompraDetalhe(c);
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao alterar o valor do item selecionado! " + e);
		}
	}
	
	private int verificaIgualdadeProduto(Produto p){
		int retorno = -1;
		
		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe c = listaCompraDetalhe.get(i);
			if (c.getProduto().equals(p)) {
				retorno = i;
			}
		}
		return retorno;
	}
	
	private void carregarTabelaContaPagar(){
		
		for (int i = 0; i < listaContasPagar.size(); i++) {
			ContaPagar c = listaContasPagar.get(i);
			tableModelPagamentoCompra.addContaPagar(c);
		}
		
	}
	
	private void alterarParcelasContaPagar(Double valorTotal, int qtd){
		
		listaContasPagar.clear();
		
		for (int i = 0; i < qtd; i++) {
			ContaPagar contap = new ContaPagar();
			contap.setCompraCabecalho(compraCabecalho);
			contap.setDataCriacao(dataAtual());
			contap.setValor(valorTotal/qtd);
			
			listaContasPagar.add(contap);
		}
		
	}
	
	private void criarContaPagar(List<ContaPagar> lcp){
		
		try {
			trx.begin();
			
			for (int i = 0; i < lcp.size(); i++) {
				ContaPagar c = lcp.get(i);
				c.setDescricao("Favorecido: " + tfFornecedor.getText());
				c.setPago(false);
				manager.persist(c);	
			}
			
			trx.commit();
			JOptionPane.showMessageDialog(null, "Conta(s) a pagar criadas com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao criar conta(s) a pagar!");
		}		
	}
	
	private void carregarContaPagar(CompraCabecalho cc) {

		listaContasPagar.clear();
		List<ContaPagar> listaCp = null;

		try {
			Query consulta = manager
					.createQuery("FROM ContaPagar WHERE compra_cabecalho_id LIKE '" + cc.getId().toString() + "'");
			listaCp = consulta.getResultList();

			for (int i = 0; i < listaCp.size(); i++) {
				ContaPagar cd = listaCp.get(i);
				// retorno.add(cd);
				listaContasPagar.add(cd);
			}
			/*
			carregarTabelaContaPagar();
			
			tfNumeroParcelas.setEnabled(false);
			tfValorParcela.setEnabled(false);
			dcVencimentoParcela.setEnabled(false);
			btnGerarContaPagar.setEnabled(false);
			btnCriarContaA.setEnabled(false);			
			*/
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a lista de compraDetalhes: " + e);
		}
		
		
		if (listaCp.isEmpty()) {
			tfNumeroParcelas.setEnabled(true);
			tfValorParcela.setEnabled(true);
			dcVencimentoParcela.setEnabled(true);
			btnGerarContaPagar.setEnabled(true);
			btnCriarContaA.setEnabled(true);
			btnOk.setEnabled(true);
			btnOk_1.setEnabled(true);
			
			tabbedPane.setEnabledAt(2, false);
		}else{
			
			carregarTabelaContaPagar();
			
			tfNumeroParcelas.setEnabled(false);
			tfValorParcela.setEnabled(false);
			dcVencimentoParcela.setEnabled(false);
			btnGerarContaPagar.setEnabled(false);
			btnCriarContaA.setEnabled(false);
			btnOk.setEnabled(false);
			btnOk_1.setEnabled(false);
			
			tabbedPane.setEnabledAt(2, true);
		}
	}
	
	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		
		tableCompraCabecalho.getColumnModel().getColumn(0).setWidth(50);
		tableCompraCabecalho.getColumnModel().getColumn(0).setMinWidth(30);		
		tableCompraCabecalho.getColumnModel().getColumn(1).setWidth(80);
		tableCompraCabecalho.getColumnModel().getColumn(1).setMinWidth(50);
		tableCompraCabecalho.getColumnModel().getColumn(2).setWidth(100);
		tableCompraCabecalho.getColumnModel().getColumn(2).setMinWidth(50);
		tableCompraCabecalho.getColumnModel().getColumn(3).setWidth(250);
		tableCompraCabecalho.getColumnModel().getColumn(3).setMinWidth(150);
		tableCompraCabecalho.getColumnModel().getColumn(4).setWidth(250);
		tableCompraCabecalho.getColumnModel().getColumn(4).setMinWidth(150);
		
		tableCompraDetalhes.getColumnModel().getColumn(0).setWidth(180);
		tableCompraDetalhes.getColumnModel().getColumn(0).setMinWidth(120);		
		tableCompraDetalhes.getColumnModel().getColumn(1).setWidth(450);
		tableCompraDetalhes.getColumnModel().getColumn(1).setMinWidth(300);
		tableCompraDetalhes.getColumnModel().getColumn(2).setWidth(70);
		tableCompraDetalhes.getColumnModel().getColumn(2).setMinWidth(50);
		
		tablePagamento.getColumnModel().getColumn(0).setWidth(100);
		tablePagamento.getColumnModel().getColumn(0).setMinWidth(80);
		tablePagamento.getColumnModel().getColumn(1).setWidth(100);
		tablePagamento.getColumnModel().getColumn(1).setMinWidth(80);
		
	}
}
