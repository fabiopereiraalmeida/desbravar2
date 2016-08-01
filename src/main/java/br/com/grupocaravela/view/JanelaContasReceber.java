package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Caixa;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.ContaReceber;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.objeto.VendaDetalhe;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelCliente;
import br.com.grupocaravela.tablemodel.TableModelContasReceber;
import br.com.grupocaravela.tablemodel.TableModelListaVendas;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class JanelaContasReceber extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable table;
	
	private Cargo cargo = UsuarioLogado.getUsuario().getCargo();

	private TableModelContasReceber tableModelContasReceber;
	private TableModelListaVendas tableModelListaVendas;
	private JComboBox cbFiltro;
	private JTextField tfId;
	private JTextField tfRazaoSocial;
	private JTextField tfFantasia;
	private JTextField tfApelido;
	private JTextField tfIdCompra;
	private JTextField tfFormaPagamento;
	private JTextField tfValorDevido;
	private JTable tableItens;
	private JTextField tfVencimento;
	private JTextField tfAbater;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ContaReceber contaReceber;
	private List<VendaDetalhe> listaVendaDetalhes;
	private DecimalFormattedField tfValorSubTotal;
	private DecimalFormattedField tfValorDesconto;
	private DecimalFormattedField tfValorTotalGeral;
	private JTextField tfVendedor;
	private JButton btnExtornarCompravenda;
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaContasReceber frame = new JanelaContasReceber();
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
	public JanelaContasReceber() {

		criarJanela();
		carregarTableModel();
		iniciaConexao();
		tamanhoColunas();
		
		tfLocalizar.requestFocus();
		
		permicoes();
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

	private void carregarTableModel() {
		this.tableModelContasReceber = new TableModelContasReceber();
		this.table.setModel(tableModelContasReceber);

		this.tableModelListaVendas = new TableModelListaVendas();
		this.tableItens.setModel(tableModelListaVendas);

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);

		table.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);

		//tableItens.getColumnModel().getColumn(2).setCellRenderer(cellRendererCustomMoeda);
		tableItens.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);
		tableItens.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
		tableItens.getColumnModel().getColumn(6).setCellRenderer(cellRendererCustomMoeda);
		tableItens.getColumnModel().getColumn(7).setCellRenderer(cellRendererCustomMoeda);

	}

	private void iniciaConexao() {

		//factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void criarJanela() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE));

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Lista de contas", null, panel_3, null);

		JPanel panel = new JPanel();

		JLabel label = new JLabel("Localizar:");
		label.setIcon(new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

		cbFiltro = new JComboBox();
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Razão Social", "Fantasia", "Vendedor" }));

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

		JButton button = new JButton("Buscar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarNome();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel.createSequentialGroup().addContainerGap()
										.addComponent(label, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, 210,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(button)
										.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(label,
										GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup().addGap(3).addComponent(cbFiltro,
								GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(button)))
						.addContainerGap(46, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();

				contaReceber = tableModelContasReceber.getContaReceber(table.getSelectedRow());

				carregarCampos(contaReceber);
				carregarTabelaItens(contaReceber);

				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(0, false);
				tabbedPane.setEnabledAt(1, true);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_2.createSequentialGroup().addContainerGap(759, Short.MAX_VALUE).addComponent(btnDetalhes)
						.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_2.createSequentialGroup().addContainerGap(42, Short.MAX_VALUE).addComponent(btnDetalhes)
						.addContainerGap()));
		panel_2.setLayout(gl_panel_2);

		JPanel panel_1 = new JPanel();

		JScrollPane scrollPane = new JScrollPane();

		table = new JTable();
		table.setAutoCreateRowSorter(true);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					limparCampos();

					contaReceber = tableModelContasReceber.getContaReceber(table.getSelectedRow());

					carregarCampos(contaReceber);
					carregarTabelaItens(contaReceber);

					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(0, false);
					tabbedPane.setEnabledAt(1, true);

				}
			}
		});
		scrollPane.setViewportView(table);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE).addGap(25))
				.addGroup(gl_panel_3.createSequentialGroup()
						.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
								.addGroup(Alignment.LEADING, gl_panel_3.createSequentialGroup().addContainerGap()
										.addComponent(panel, GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)))
								.addGap(25)));
		gl_panel_3
				.setVerticalGroup(
						gl_panel_3.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_panel_3.createSequentialGroup().addGap(5)
												.addComponent(panel, GroupLayout.PREFERRED_SIZE, 53,
														GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_2,
												GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
				gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup().addGap(12)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE).addGap(5)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE));
		panel_1.setLayout(gl_panel_1);
		panel_3.setLayout(gl_panel_3);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_4, null);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Itens da compra",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));

		JPanel panel_6 = new JPanel();

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_4.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 859,
										Short.MAX_VALUE)
						.addComponent(panel_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel_4
				.setVerticalGroup(
						gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_4.createSequentialGroup().addContainerGap()
										.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 147,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_7,
												GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));

		JButton btnQuitar = new JButton("Quitar");
		btnQuitar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Gostaria de quitar a conta de " + tfRazaoSocial.getText() + " no valor de "
								+ tfValorDevido.getText() + "?",
						"Quitar conta", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
						options[0]);

				if (i == JOptionPane.YES_OPTION) {

					criarCaixa(contaReceber.getValorDevido(), contaReceber.getVendaCabecalho());

					quitarContaReceber(contaReceber);
					
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(0, true);
					tabbedPane.setEnabledAt(1, false);
					
				}

			}
		});
		btnQuitar.setIcon(
				new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/caixa_24.png")));

		JButton btnCancelar = new JButton("Voltar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparTabelaItens();
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(0, true);
				tabbedPane.setEnabledAt(1, false);
				limparCampos();

			}
		});
		btnCancelar.setIcon(new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/voltar_24.png")));

		tfAbater = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfAbater.setForeground(Color.RED);
		tfAbater.setFont(new Font("Dialog", Font.BOLD, 18));
		tfAbater.setColumns(10);

		JButton btnAbater = new JButton("Abater");
		btnAbater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirme o abatimento no valor de " + tfAbater.getText() + "?", "Abater valor",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
					criarCaixa(
							Double.parseDouble(
									tfAbater.getText().replace("R$ ", "").replace(".", "").replace(",", ".")),
							contaReceber.getVendaCabecalho());

					abaterValorContaReceber(Double
							.parseDouble(tfAbater.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));

					limparCampos();
					carregarCampos(contaReceber);

				} else {

					tfAbater.requestFocus();

				}
			}
		});
		btnAbater.setIcon(
				new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/atualizar_24.png")));

		JLabel lblAbater = new JLabel("Abater");
		
		btnExtornarCompravenda = new JButton("Extornar compra/venda");
		btnExtornarCompravenda.setIcon(new ImageIcon(JanelaContasReceber.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_64.png")));
		btnExtornarCompravenda.setEnabled(false);
		btnExtornarCompravenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! ESTA OPERAÇÃO IRA EXTORNAR(EXCLUIR) A CONTA A RECEBER DO CLIENTE " + tfRazaoSocial.getText().toUpperCase() + " ASSIM COMO A VENDA?", "EXTORNAR CONTA A RECEBER",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
					extornar(contaReceber);

				}
			}
		});
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAbater)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tfAbater, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAbater)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnExtornarCompravenda)
					.addPreferredGap(ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
					.addComponent(btnCancelar)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnQuitar)
					.addContainerGap())
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnQuitar)
						.addComponent(btnCancelar)
						.addComponent(lblAbater)
						.addComponent(tfAbater, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAbater)
						.addComponent(btnExtornarCompravenda, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_7.setLayout(gl_panel_7);

		JLabel lblId = new JLabel("Id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblRazoSocial = new JLabel("Razão Social");

		tfRazaoSocial = new JTextField();
		tfRazaoSocial.setEditable(false);
		tfRazaoSocial.setColumns(10);

		tfApelido = new JTextField();
		tfApelido.setEditable(false);
		tfApelido.setColumns(10);

		JLabel lblApelido = new JLabel("Apelido");

		tfFantasia = new JTextField();
		tfFantasia.setEditable(false);
		tfFantasia.setColumns(10);

		JLabel lblFantasia = new JLabel("Fantasia");

		JLabel lblIdCompra = new JLabel("Id venda");

		tfIdCompra = new JTextField();
		tfIdCompra.setEditable(false);
		tfIdCompra.setColumns(10);

		JLabel lblFormaPagamento = new JLabel("Forma pagamento");

		tfFormaPagamento = new JTextField();
		tfFormaPagamento.setEditable(false);
		tfFormaPagamento.setColumns(10);

		tfValorDevido = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorDevido.setForeground(Color.BLUE);
		tfValorDevido.setFont(new Font("Dialog", Font.BOLD, 18));
		tfValorDevido.setEditable(false);
		tfValorDevido.setColumns(10);

		JLabel lblValorDevido = new JLabel("Valor devido");
		lblValorDevido.setFont(new Font("Dialog", Font.BOLD, 18));

		tfVencimento = new JTextField();
		tfVencimento.setEditable(false);
		tfVencimento.setColumns(10);

		JLabel lblVencimento = new JLabel("Vencimento");

		JLabel lblVendedor = new JLabel("Vendedor:");

		tfVendedor = new JTextField();
		tfVendedor.setEditable(false);
		tfVendedor.setColumns(10);
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblId).addGap(12)
										.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblRazoSocial)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfRazaoSocial, GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
						.addGroup(
								gl_panel_6.createSequentialGroup().addComponent(lblFantasia).addGap(12)
										.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, 325,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblApelido).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfApelido, GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
								.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblIdCompra).addGap(12)
										.addComponent(tfIdCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblFormaPagamento).addGap(12)
										.addComponent(tfFormaPagamento, GroupLayout.PREFERRED_SIZE, 347,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblVencimento).addGap(12)
										.addComponent(tfVencimento, GroupLayout.PREFERRED_SIZE, 156,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblVendedor)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfVendedor, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
								.addComponent(lblValorDevido).addGap(12).addComponent(tfValorDevido,
										GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)))
				.addContainerGap()));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblId))
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblRazoSocial))
						.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(12)
				.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblFantasia))
						.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblApelido))
						.addComponent(tfApelido, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(18)
				.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblIdCompra))
						.addComponent(tfIdCompra, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblFormaPagamento))
						.addComponent(tfFormaPagamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(12)
				.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblVencimento))
						.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
								.addComponent(tfVencimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblVendedor).addComponent(tfVendedor, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup().addGap(2).addComponent(lblValorDevido))
						.addComponent(tfValorDevido, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);

		JScrollPane scrollPane_1 = new JScrollPane();

		tableItens = new JTable();
		tableItens.setRowSelectionAllowed(false);
		tableItens.setEnabled(false);
		scrollPane_1.setViewportView(tableItens);

		JLabel label_1 = new JLabel("Sub Total");
		label_1.setFont(new Font("Dialog", Font.BOLD, 12));

		tfValorSubTotal = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfValorSubTotal.setFont(new Font("Dialog", Font.PLAIN, 12));
		tfValorSubTotal.setEditable(false);
		tfValorSubTotal.setColumns(10);

		JLabel label_2 = new JLabel("Desconto");
		label_2.setFont(new Font("Dialog", Font.BOLD, 12));

		tfValorDesconto = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfValorDesconto.setFont(new Font("Dialog", Font.PLAIN, 12));
		tfValorDesconto.setEditable(false);
		tfValorDesconto.setColumns(10);

		JLabel label_3 = new JLabel("Total Geral");
		label_3.setFont(new Font("Dialog", Font.BOLD, 12));

		tfValorTotalGeral = new DecimalFormattedField("R$ #,##0.00;R$ -#,##0.00");
		tfValorTotalGeral.setFont(new Font("Dialog", Font.PLAIN, 12));
		tfValorTotalGeral.setEditable(false);
		tfValorTotalGeral.setColumns(10);
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5
				.setHorizontalGroup(gl_panel_5
						.createParallelGroup(
								Alignment.LEADING)
						.addGroup(gl_panel_5.createSequentialGroup().addContainerGap().addComponent(label_1)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfValorSubTotal, GroupLayout.PREFERRED_SIZE, 94,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(label_2)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfValorDesconto, GroupLayout.PREFERRED_SIZE, 83,
										GroupLayout.PREFERRED_SIZE)
								.addGap(307).addComponent(label_3).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfValorTotalGeral, GroupLayout.PREFERRED_SIZE, 90,
										GroupLayout.PREFERRED_SIZE)
								.addGap(15))
						.addGroup(gl_panel_5.createSequentialGroup().addGap(7)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_5
				.createSequentialGroup().addGap(5)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfValorTotalGeral, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfValorSubTotal, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfValorDesconto, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))));
		panel_5.setLayout(gl_panel_5);
		panel_4.setLayout(gl_panel_4);
		contentPane.setLayout(gl_contentPane);
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

	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelContasReceber.removeContaReceber(0);
		}
	}

	private void limparTabelaItens() {
		while (tableItens.getModel().getRowCount() > 0) {
			tableModelListaVendas.removevendaDetalhe(0);
		}
	}

	private void buscarTodos() {
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from ContaReceber where quitada = '0'");
			List<ContaReceber> listaContaReceber = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaContaReceber.size(); i++) {
				ContaReceber cr = listaContaReceber.get(i);
				tableModelContasReceber.addContaReceber(cr);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de contas a receber: " + e);
		}
	}

	private void filtrar(int op) {

		switch (op) {
		case 0:
			try {

				//trx.begin();
				// Query consulta = manager.createQuery("from ContaReceber cr,
				// Cliente cl where cr.cliente = cl.id and cl.razaoSocial like
				// 'Vanneis Pinherio Soares'");
				Query consulta = manager.createQuery(
						"from ContaReceber where cliente_id in (select id from Cliente where razao_social like '%"
								+ tfLocalizar.getText() + "%') and quitada = '0'");
				List<ContaReceber> listaContaReceber = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaContaReceber.size(); i++) {
					ContaReceber c = listaContaReceber.get(i);
					tableModelContasReceber.addContaReceber(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de ContaReceber: " + e);
			}
			break;

		case 1:
			try {

				//trx.begin();
				Query consulta = manager.createQuery(
						"from ContaReceber where cliente_id in (select id from Cliente where fantasia like '%"
								+ tfLocalizar.getText() + "%') and quitada = '0'");
				List<ContaReceber> listaContaReceber = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaContaReceber.size(); i++) {
					ContaReceber c = listaContaReceber.get(i);
					tableModelContasReceber.addContaReceber(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de ContaReceber: " + e);
			}
			break;

		case 2:
			try {

				//trx.begin();
				Query consulta = manager.createQuery(
						"from ContaReceber where venda_cabecalho_id in (select id from VendaCabecalho where usuario_id in (select id from Usuario where nome like '%"
								+ tfLocalizar.getText() + "%')) and quitada = '0'");
				List<ContaReceber> listaContaReceber = consulta.getResultList();
				//trx.commit();

				for (int i = 0; i < listaContaReceber.size(); i++) {
					ContaReceber c = listaContaReceber.get(i);
					tableModelContasReceber.addContaReceber(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de ContaReceber: " + e);
			}
			break;

		default:

		}
	}

	private void limparCampos() {
		tfId.setText("");
		tfAbater.setText("0");
		tfApelido.setText("");
		tfFantasia.setText("");
		tfIdCompra.setText("");
		tfRazaoSocial.setText("");
		tfValorDevido.setText("0");
		tfFormaPagamento.setText("");
		tfVencimento.setText("");
		tfVendedor.setText("");

		tfValorDesconto.setText("0");
		tfValorSubTotal.setText("0");
		tfValorTotalGeral.setText("0");

	}

	private void carregarCampos(ContaReceber cr) {

		tfApelido.setText(cr.getCliente().getApelido());
		tfFantasia.setText(cr.getCliente().getFantasia());
		try {
			tfFormaPagamento.setText(cr.getVendaCabecalho().getFormaPagamento().getNome());
		} catch (Exception e) {
			tfFormaPagamento.setText("Não definido!");
		}

		tfId.setText(cr.getId().toString());
		try {
			tfIdCompra.setText(cr.getVendaCabecalho().getId().toString());
		} catch (Exception e) {
			tfIdCompra.setText("nulo");
		}

		tfRazaoSocial.setText(cr.getCliente().getRazaoSocial());
		tfValorDevido.setText(cr.getValorDevido().toString());
		try {
			tfVencimento.setText(formatData.format(cr.getVencimento()));
		} catch (Exception e) {
			tfVencimento.setText("Não definido!");
		}
		try {
			tfVendedor.setText(cr.getVendaCabecalho().getUsuario().getNome());
		} catch (Exception e) {
			tfVendedor.setText("Não definido!");
		}

	}

	private void carregarTabelaItens(ContaReceber cr) {

		limparTabelaItens();

		try {
			// trx.begin();
			Query consulta = manager.createQuery(
					"from VendaDetalhe where venda_cabecalho_id like '" + cr.getVendaCabecalho().getId() + "'");
			listaVendaDetalhes = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaVendaDetalhes.size(); i++) {
				VendaDetalhe vd = listaVendaDetalhes.get(i);
				tableModelListaVendas.addVendaDetalhe(vd);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela com os produtos da venda: " + e);
		}

		calcularTotais();

	}

	private Double calcularValorTotalDesconto() {

		Double desconto = 0.0;

		try {
			for (int i = 0; i < listaVendaDetalhes.size(); i++) {
				desconto = desconto + listaVendaDetalhes.get(i).getValorDesconto();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return desconto;
	}

	private Double calcularValorTotalParcial() {

		Double totalParcial = 0.0;

		try {
			for (int i = 0; i < listaVendaDetalhes.size(); i++) {
				totalParcial = totalParcial + listaVendaDetalhes.get(i).getValorParcial();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return totalParcial;
	}

	private Double calcularValorTotalGeral() {

		Double valorTotalGeral = 0.0;

		try {
			for (int i = 0; i < listaVendaDetalhes.size(); i++) {
				valorTotalGeral = valorTotalGeral + listaVendaDetalhes.get(i).getValorTotal();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return valorTotalGeral;
	}

	private void calcularTotais() {

		tfValorSubTotal.setText(calcularValorTotalParcial().toString());
		tfValorDesconto.setText(calcularValorTotalDesconto().toString());
		tfValorTotalGeral.setText(calcularValorTotalGeral().toString());

	}

	private void abaterValorContaReceber(Double valor) {
				
		contaReceber.setValorDevido(contaReceber.getValorDevido() - valor);

		try {
			trx.begin();
			manager.persist(contaReceber);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Valor abatido com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "Abatimento de " + tfAbater.getText() + " de conta a receber com id \"" + contaReceber.getId() + "\" do cliente \"" + contaReceber.getCliente().getRazaoSocial() + " no valor de R$ " + contaReceber.getVendaCabecalho().getValorTotal(), dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao abater valor!" + e);
		}

	}

	private void quitarContaReceber(ContaReceber cr) {

		cr.setValorDevido(0.0);
		cr.setQuitada(true);

		try {
			trx.begin();
			manager.persist(cr);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Conta quitada com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "Quitação de conta a receber no valor de R$ " + cr.getValorDevido() + " com id \"" + cr.getId() + "\" do cliente \"" + cr.getCliente().getRazaoSocial() + " no valor de R$ " + cr.getVendaCabecalho().getValorTotal(), dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao quitar conta!" + e);
		}

	}

	private void criarCaixa(Double valor, VendaCabecalho vc) {

		try {
			Caixa caixa = new Caixa();

			caixa.setData(dataAtual());
			caixa.setValor(valor);
			
			try {
				caixa.setVendaCabecalho(vc);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
			}
			
			try {
				caixa.setUsuario(UsuarioLogado.getUsuario());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
			}
				
			trx.begin();
			manager.persist(caixa);
			trx.commit();
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao criar caixa!" + e);

		}

	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}
	
	public void tamanhoColunas() {
        //tableLista.setAutoResizeMode(tableLista.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setWidth(70);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(1).setWidth(260);
        table.getColumnModel().getColumn(1).setMinWidth(260);
        table.getColumnModel().getColumn(2).setWidth(220);
        table.getColumnModel().getColumn(2).setMinWidth(220);
        
    }
	
	private void extornar(ContaReceber cre){
		
		try {

			trx.begin();
			manager.remove(cre);
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "A conta a receber do cliente" + cre.getCliente().getRazaoSocial() + " no valor de R$ " + cre.getValorDevido() + " com o id nº " + cre.getId() + " foi extronada com sucesso", dataAtual());
			manager.remove(cre.getVendaCabecalho());
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "A venda do cliente" + cre.getCliente().getRazaoSocial() + " no valor de R$ " + cre.getVendaCabecalho().getValorTotal() + " com o id nº " + cre.getVendaCabecalho().getId() + " foi extronada com sucesso", dataAtual());
			trx.commit();

			JOptionPane.showMessageDialog(null, "Conta a receber e venda extornada com sucesso!");
			
			limparTabelaItens();
			tabbedPane.setSelectedIndex(0);
			tabbedPane.setEnabledAt(0, true);
			tabbedPane.setEnabledAt(1, false);
			limparCampos();
			
			limparTabela();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}		
	}
	
private void permicoes(){		
		
		if (cargo.isAcessoContasReceber() && cargo.isAcessoVendas()) {
			btnExtornarCompravenda.setEnabled(true);			
		}
				
	}
}
