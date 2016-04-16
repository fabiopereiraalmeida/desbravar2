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

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarFornecedor;
import br.com.grupocaravela.dialog.BuscarProduto;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.CompraCabecalho;
import br.com.grupocaravela.objeto.CompraDetalhe;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Fornecedor;
import br.com.grupocaravela.objeto.Produto;
import br.com.grupocaravela.objeto.ProdutoLote;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaDetalhe;
import br.com.grupocaravela.tablemodel.TableModelCompraCabecalho;
import br.com.grupocaravela.tablemodel.TableModelCompraDetalhe;
import br.com.grupocaravela.util.UsuarioLogado;

import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class JanelaCompras extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCompraCabecalho tableModelCompraCabecalho;
	private TableModelCompraDetalhe tableModelCompraDetalhe;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableCompraCabecalho;
	private JTextField tfId;
	private JTextField tfNumeroNota;

	private CompraCabecalho compraCabecalho;
	//private CompraDetalhe compraDetalhe;
	private List<CompraDetalhe> listaCompraDetalhe = new ArrayList<>();
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
	private JComboBox cbFormaPagamento;
	
	private Fornecedor fornecedorImportado = null;
	private Usuario usuario;
	private Produto produtoImportado;
	private JComboBox cbUsuario;
	private JTextField tfTotal;
	private JDateChooser dcValidade;
	private JLabel lblEstadoDaCompra;
	
	private boolean finalizada = false;
	private boolean entregue = false;
	private JButton btnAddProduto;
	
	private FormaPagamento formaPagamento = null;
	private JButton btnRecebercompra;
	
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
		
		iniciaConexao();
		
		carregajcbUsuario();
		carregajcbFormaPagamento();
		
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
		//Evento ao fechar a janela
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

	private void iniciaConexao(){
		
		//factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
	private void criarJanela() {
		
		setTitle("Compras");
		setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 930, 652);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 918, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista de compras", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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

					carregarCampos(compraCabecalho);
					verificaEstadoCompra();
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(0, false);
				}
			}
		});

		JButton btnNovo = new JButton("Nova compra");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				formaPagamento = null;
				fornecedorImportado = null;
				
				limparCamposNovaCompra();
				limparTabelaDetalhes();
				
				compraCabecalho = new CompraCabecalho();
				listaCompraDetalhe.clear();
				
				bucarUsuarioLogado();
				
				dcDataCompra.setDate(dataAtual());
				
				verificaEstadoCompra();
				
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				
			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

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
		btnExcluir.setIcon(new ImageIcon(
				JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

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

				carregarCampos(compraCabecalho);
				verificaEstadoCompra();
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(lblLocalizar)
							.addGap(12)
							.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 651, Short.MAX_VALUE)
							.addGap(12)
							.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(12)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 889, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addContainerGap(336, Short.MAX_VALUE)
							.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(btnDetalhes)
							.addGap(12)
							.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLocalizar)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnBuscar))))
					.addGap(12)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
					.addGap(8)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExcluir)
						.addComponent(btnDetalhes)
						.addComponent(btnNovo))
					.addContainerGap())
		);
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

				if (formaPagamento == null) {
					JOptionPane.showMessageDialog(null, "Para finalizar é necessario informar a forma de pagamento!!!");
				}else if (fornecedorImportado == null) {
					JOptionPane.showMessageDialog(null, "Para finalizar é necessario informar o fornecedor!!!");
				}else{
				
				//listaCompraDetalhe.add(cd);
				
				//salvarCompraCabecalho(compraCabecalho);
				/*
				for (int i = 0; i < listaCompraDetalhe.size(); i++) {
					CompraDetalhe c = listaCompraDetalhe.get(i);
					tableModelCompraDetalhe.addCompraDetalhe(c);
				}
				*/
				salvarCompraCabecalho(compraCabecalho);
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				tfLocalizar.requestFocus();
				}

			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				limparCamposNovaCompra();
				tfLocalizar.requestFocus();

			}
		});
		btnCancelar.setIcon(new ImageIcon(
				JanelaCompras.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));
		
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
		
		JLabel lblFormaPagamento = new JLabel("Forma pagamento:");
		
		cbFormaPagamento = new JComboBox();
		cbFormaPagamento.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				formaPagamento = (FormaPagamento) cbFormaPagamento.getSelectedItem();
				
			}
		});
		
		JPanel panel_2 = new JPanel();
		
		JLabel lblEstado = new JLabel("Estado:");
		
		lblEstadoDaCompra = new JLabel("Estado da compra");
		
		JLabel lblTotal = new JLabel("Total:");
		
		tfTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfTotal.setEditable(false);
		tfTotal.setColumns(10);
		
		JButton btnFinalizarCompra = new JButton("Finalizar Compra");
		btnFinalizarCompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Gostaria de finalizar a compra?",
						"Finaliza Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
				
					finalizada = true;
					compraCabecalho.setFinalizada(true);
					
					verificaEstadoCompra();
					
				}
				
				if (finalizada) {
					btnRecebercompra.setEnabled(true);
				}
			}
		});
		btnFinalizarCompra.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/atender_24.png")));
		
		btnRecebercompra = new JButton("ReceberCompra");
		btnRecebercompra.setEnabled(false);
		btnRecebercompra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma o recebimento da compra?",
						"Finaliza Compra", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
				
					entregue = true;
					compraCabecalho.setEntregue(true);
					verificaEstadoCompra();
					
				}
			}
		});
		btnRecebercompra.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/receber_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblDataCompra)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcDataCompra, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblDataEntrega)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcDataEntrega, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblUsuario)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbUsuario, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
									.addComponent(lblEstado)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblEstadoDaCompra)
									.addGap(91)
									.addComponent(lblTotal)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblFormaPagamento)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(cbFormaPagamento, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED, 155, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(panel_2, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(btnFinalizarCompra)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnRecebercompra)
									.addGap(286)
									.addComponent(btnCancelar)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSalvar)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGap(154)))
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
						.addComponent(lblFormaPagamento)
						.addComponent(cbFormaPagamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEstado)
						.addComponent(lblEstadoDaCompra)
						.addComponent(tfTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTotal))
					.addGap(16)
					.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFinalizarCompra)
						.addComponent(btnRecebercompra)
						.addComponent(btnSalvar)
						.addComponent(btnCancelar))
					.addContainerGap())
		);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		tableCompraDetalhes = new JTable();
		scrollPane_1.setViewportView(tableCompraDetalhes);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
					.addGap(12))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGap(6)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
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
		tfValorTotal.setColumns(10);
		
		JLabel lblValorTotal = new JLabel("Valor total:");
		
		tfValorUnitario = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorUnitario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				
				tfLote.requestFocus();
				
				Double qtd = Double.parseDouble(tfQuantidade.getText().replace(".", "").replace(",", "."));
				Double vu = Double.parseDouble(tfValorUnitario.getText().replace("R$ ", "").replace(".", "").replace(",", "."));
				
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
		btnImpProduto.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/mais_24.png")));
		
		btnAddProduto = new JButton("Add Produto");
		btnAddProduto.setEnabled(false);
		btnAddProduto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				limparTabelaDetalhes();
				addProduto(produtoImportado);
				limparCamposNovoProduto();
				btnAddProduto.setEnabled(false);
			}
		});
		btnAddProduto.setIcon(new ImageIcon(JanelaCompras.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));
		
		JLabel label = new JLabel("Validade:");
		
		dcValidade = new JDateChooser();
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(btnImpProduto)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnAddProduto, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addComponent(lblFabricacao)
							.addGap(12)
							.addComponent(dcFabricacao, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(dcValidade, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addComponent(lblQuantidade)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfQuantidade, 0, 0, Short.MAX_VALUE))
								.addGroup(gl_panel_3.createSequentialGroup()
									.addComponent(lblCod)
									.addGap(12)
									.addComponent(tfCodProduto, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)))
							.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3.createSequentialGroup()
									.addGap(18)
									.addComponent(lblNome_1)
									.addGap(12)
									.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, 426, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblUnidade)
									.addGap(12)
									.addComponent(tfUnidade, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_3.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(lblValorUnitrio)
									.addGap(12)
									.addComponent(tfValorUnitario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblValorTotal)
									.addGap(12)
									.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblLote)
									.addGap(12)
									.addComponent(tfLote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap(13, Short.MAX_VALUE)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblCod))
						.addComponent(tfCodProduto, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblNome_1))
						.addComponent(tfNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblUnidade))
						.addComponent(tfUnidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblQuantidade))
						.addComponent(tfQuantidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblValorUnitrio))
						.addComponent(tfValorUnitario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblValorTotal))
						.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(2)
							.addComponent(lblLote))
						.addComponent(tfLote, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(lblFabricacao)
						.addComponent(dcFabricacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label)
						.addComponent(dcValidade, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addComponent(btnImpProduto)
						.addComponent(btnAddProduto, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelCompraCabecalho = new TableModelCompraCabecalho();
		this.tableCompraCabecalho.setModel(tableModelCompraCabecalho);
		
		this.tableModelCompraDetalhe = new TableModelCompraDetalhe();
		this.tableCompraDetalhes.setModel(tableModelCompraDetalhe);
	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from CompraCabecalho");
			List<CompraCabecalho> listaCompraCabecalhos = consulta.getResultList();
			//trx.commit();
			
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

					//trx.begin();
					Query consulta = manager.createQuery("from CompraCabecalho");
					List<CompraCabecalho> listaCompraCabecalhos = consulta.getResultList();
					//trx.commit();
					
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
			
			//c.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());
			c.setFormaPagamento(formaPagamento);
			c.setFornecedor(fornecedorImportado);
			c.setNumeroNota(tfNumeroNota.getText());
			c.setUsuario((Usuario) cbUsuario.getSelectedItem());
			c.setValorCompra(Double.parseDouble(tfTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			
			c.setFinalizada(finalizada);
			c.setEntregue(entregue);
						
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
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(CompraCabecalho c) {

		tfId.setText(c.getId().toString());
		fornecedorImportado = c.getFornecedor();
		tfFornecedor.setText(c.getFornecedor().getRazaoSocial());
		
		usuario = c.getUsuario();
		cbUsuario.setSelectedItem(c.getUsuario());
		
		formaPagamento = c.getFormaPagamento();
		cbFormaPagamento.setSelectedItem(c.getFormaPagamento());
		
		tfNumeroNota.setText(c.getNumeroNota());
		dcDataCompra.setDate(c.getDataCompra());
		dcDataEntrega.setDate(c.getDataRecebimento());
		
		tfTotal.setText(c.getValorCompra().toString());
		
		finalizada = c.getFinalizada();
		entregue = c.getEntregue();
		
		verificaEstadoCompra();
		
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
		tfValorTotal.setText("");
		tfValorUnitario.setText("");
		
		dcDataCompra.setDate(null);
		dcDataEntrega.setDate(null);
		dcFabricacao.setDate(null);
		dcValidade.setDate(null);
				
		cbFormaPagamento.setSelectedIndex(-1);
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
			Query consulta = manager.createQuery("from FormaPagamento ORDER BY nome ASC");
			List<FormaPagamento> listaFormaPagamentos = consulta.getResultList();
			// trx.commit();

			// JOptionPane.showMessageDialog(null, listaUsuarios.size());

			for (int i = 0; i < listaFormaPagamentos.size(); i++) {

				// JOptionPane.showMessageDialog(null, i);

				FormaPagamento f = listaFormaPagamentos.get(i);
				cbFormaPagamento.addItem(f);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox de forma de pagamento! " + e);
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
	
	private void addProduto(Produto p){
		
		Double total = 0.0;
		//compraCabecalho.setFornecedor(fornecedorImportado);
		//compraCabecalho.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());
				
		ProdutoLote pl = new ProdutoLote();		
		pl.setFabricacao(dcFabricacao.getDate());
		pl.setLote(tfLote.getText());		
		pl.setQuantidade(Double.parseDouble(tfQuantidade.getText()));
		pl.setValorCompra(Double.parseDouble(tfValorUnitario.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
		pl.setValidade(dcValidade.getDate());
		pl.setProduto(p);
		p.getProdutoLote().add(pl);
				
		CompraDetalhe cd = new CompraDetalhe();		
		cd.setProduto(p);
		cd.setValorTotal(Double.parseDouble(tfValorTotal.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));		
		cd.setCompraCabecalho(compraCabecalho);
		cd.setProdutoLote(pl);
		pl.getCompraDetalhe().add(cd);
		
		/*
		trx.begin();
		manager.persist(compraCabecalho);
		manager.persist(pl);
		manager.persist(cd);
		trx.commit();
		*/
				
		listaCompraDetalhe.add(cd);		
		
		for (int i = 0; i < listaCompraDetalhe.size(); i++) {
			CompraDetalhe c = listaCompraDetalhe.get(i);
			tableModelCompraDetalhe.addCompraDetalhe(c);
			
			total = total + c.getValorTotal();
		}
		
		tfTotal.setText(total.toString());
		
	}
	
	private void carregarTodasCompras(){
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
	
	private void verificaEstadoCompra(){

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
		
		lblEstadoDaCompra.setText("ABERTO");
		lblEstadoDaCompra.setForeground(Color.ORANGE);
		
		if (finalizada) {
			lblEstadoDaCompra.setText("FINALIZADA!");
			lblEstadoDaCompra.setForeground(Color.GREEN);
		}
		
		if (entregue) {
			lblEstadoDaCompra.setText("ENTREGUE!");
			lblEstadoDaCompra.setForeground(Color.BLUE);		
		}
		
	}
}
