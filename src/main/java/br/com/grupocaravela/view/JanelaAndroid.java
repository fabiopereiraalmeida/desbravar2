package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.dialog.BuscarProduto;
import br.com.grupocaravela.objeto.AndroidCaixa;
import br.com.grupocaravela.objeto.AndroidContaReceber;
import br.com.grupocaravela.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.objeto.AndroidVendaDetalhe;
import br.com.grupocaravela.objeto.Caixa;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.ContaReceber;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Produto;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.objeto.VendaDetalhe;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda2Via;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelAndroidCaixa;
import br.com.grupocaravela.tablemodel.TableModelAndroidContasReceber;
import br.com.grupocaravela.tablemodel.TableModelAndroidVendaCabecalho;
import br.com.grupocaravela.tablemodel.TableModelListaVendasAndroid;
import br.com.grupocaravela.util.UsuarioLogado;

public class JanelaAndroid extends JFrame {

	// private EntityManagerFactory factory;
	private EntityManager manager;
	private EntityTransaction trx;
//
	private JPanel contentPane;
	private JComboBox cbVendedor;
	private JTable tableVendaAndroid;
	private JTable tableItensVenda;

	private TableModelAndroidVendaCabecalho tableModelAndroidVendaCabecalho;
	private TableModelListaVendasAndroid tableModelListaVendasAndroid;
	private TableModelAndroidCaixa tableModelAndroidCaixa;
	private TableModelAndroidContasReceber tableModelAndroidContasReceber;

	private JDateChooser dcInicial;
	private JDateChooser dcFinal;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private AndroidVendaCabecalho androidVendaCabecalho;
	private AndroidVendaDetalhe androidVendaDetalhe;
	
	private AndroidContaReceber androidContaReceber;

	// private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHoraInternacional = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat formatBRA = new SimpleDateFormat("dd/MM/yyyy");
	private JCheckBox chckbxUtilizarFiltro;

	private Produto produtoImportado;
	private JComboBox cbFormapagamentoVendaDetalhes;
	private JComboBox cbVencimentos;
	private JLabel lbSubTotal;
	private JLabel lbDesconto;
	private JLabel lbTotalGeral;
	private JTable tableContasRecebidas;
	private JLabel tfTotalContasRecebidas;
	private JComboBox cbVendedorContasReceber;
	private JCheckBox checkBoxRecebidas;
	private JButton button;
	private JDateChooser dcInicialRecebidas;
	private JDateChooser dcFinalRecebidas;
	private JCheckBox cbMostrarTodasContas;
	
	private Cargo cargo = UsuarioLogado.getUsuario().getCargo();
	private JButton btnExcluir2;
	private JButton btnExcluir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaAndroid frame = new JanelaAndroid();
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
	public JanelaAndroid() {

		carregarJanela();
		iniciaConexao();

		carregarTableModel();

		carregajcbVendedor();
		// carregajcbRota();
		carregajcbFormaPagamento();

		// CARREGANDO A TABELA DE CAIXA
		buscarCaixaSemFiltro();
		buscarContaReceberSemFiltro();

		ativarFiltroBusca(false);
		permicoes();

		/*
		 * // Evento ao fechar a janela addWindowListener(new WindowAdapter() {
		 * 
		 * @Override public void windowClosing(WindowEvent e) { try {
		 * factory.close(); } catch (Exception e2) { // TODO: handle exception }
		 * 
		 * } });
		 */
	}

	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void carregarJanela() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 601);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Vendas do android", null, panel, null);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Filtro", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JPanel panel_3 = new JPanel();

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(Color.DARK_GRAY));

		chckbxUtilizarFiltro = new JCheckBox("Utilizar filtro");
		chckbxUtilizarFiltro.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (chckbxUtilizarFiltro.isSelected()) {
					ativarFiltroBusca(true);
				} else {
					ativarFiltroBusca(false);
				}

			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759,
										Short.MAX_VALUE)
						.addComponent(panel_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
						.addComponent(chckbxUtilizarFiltro, Alignment.LEADING)
						.addComponent(panel_5, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(
								gl_panel.createSequentialGroup().addContainerGap().addComponent(chckbxUtilizarFiltro)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 96,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_5,
												GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));

		JButton btnAprovarVenda = new JButton("Aprovar venda");
		btnAprovarVenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ArrayList<AndroidVendaCabecalho> listaAndroidVendaCabecalho = new ArrayList();

				int idx[] = tableVendaAndroid.getSelectedRows();

				for (int h = 0; h < idx.length; h++) {
					int linhaReal = tableVendaAndroid.convertRowIndexToModel(idx[h]);
					listaAndroidVendaCabecalho.add(tableModelAndroidVendaCabecalho.getAndroidVendaCabecalho(linhaReal));
				}

				for (int i = 0; i < listaAndroidVendaCabecalho.size(); i++) {

					List<Produto> pq = verificaEstoque(listaAndroidVendaCabecalho.get(i));
					
					if (pq.size() > 0) {
						for (int j = 0; j < pq.size(); j++) {
							JOptionPane.showMessageDialog(null, "A quantidade de " + pq.get(j).getNome() + " vendida é superior o estoque do produto! Favor alterar a quantidade. ");
						}						
					}else{
					
					Query consulta = manager.createQuery("from FormaPagamento where id like '"
							+ listaAndroidVendaCabecalho.get(i).getFormaPagamento() + "'");
					List<FormaPagamento> listaFormaPagamnento = consulta.getResultList();

					FormaPagamento f = listaFormaPagamnento.get(0);

					Object[] options = { "Sim", "Não" };
					int w = JOptionPane.showOptionDialog(null,
							"ATENÇÃO!!! Aprovar a venda para o cliente "
									+ buscarCliente(listaAndroidVendaCabecalho.get(i).getCliente()) + " no valor de R$ "
									+ listaAndroidVendaCabecalho.get(i).getValorTotal() + " na forma de pagamento "
									+ f.getNome() + "?",
							"Aprovar Venda", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);

					if (w == JOptionPane.YES_OPTION) {
						// FormaPagamento f = (FormaPagamento)
						// cbFormapagamentoVendaDetalhes.getSelectedItem();

						// androidVendaCabecalho =
						// tableModelAndroidVendaCabecalho.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());
						
						androidVendaCabecalho = listaAndroidVendaCabecalho.get(i);
						
						aprovarVenda(androidVendaCabecalho, buscarCliente(androidVendaCabecalho.getCliente()),
								buscarFormaPagamento(androidVendaCabecalho.getFormaPagamento()),
								buscarUsuario(androidVendaCabecalho.getUsuario()));						
						
					}

				}
			}

				/*
				 * FormaPagamento f = (FormaPagamento)
				 * cbFormapagamentoVendaDetalhes.getSelectedItem();
				 * 
				 * 
				 * Object[] options = { "Sim", "Não" }; int i =
				 * JOptionPane.showOptionDialog(null,
				 * "ATENÇÃO!!! Aprovar a venda na forma de pagamento " +
				 * f.getNome() + "?", "Aprovar Venda",
				 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				 * null, options, options[0]);
				 * 
				 * if (i == JOptionPane.YES_OPTION) { // FormaPagamento f =
				 * (FormaPagamento) //
				 * cbFormapagamentoVendaDetalhes.getSelectedItem();
				 * 
				 * androidVendaCabecalho = tableModelAndroidVendaCabecalho
				 * .getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow())
				 * ;
				 * 
				 * aprovarVenda(androidVendaCabecalho,
				 * buscarCliente(androidVendaCabecalho.getCliente()),
				 * buscarFormaPagamento(androidVendaCabecalho.getFormaPagamento(
				 * )), buscarUsuario(androidVendaCabecalho.getUsuario())); }
				 */
				// ########################### Limpar e buscar tabela
				// ###############################3

				limparTabela();
				limparTabelaItens();

				if (chckbxUtilizarFiltro.isSelected()) {
					buscarComFiltro(dcInicial.getDate(), dcFinal.getDate(), (Usuario) cbVendedor.getSelectedItem());
				} else {
					buscarSemFiltro();
				}

				// ########################### Limpar e buscar tabela
				// ###############################3

			}
		});
		btnAprovarVenda
				.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		JButton btnEditarVenda = new JButton("Editar Venda");
		btnEditarVenda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// limparTabela();
				limparTabelaItens();

				androidVendaCabecalho = tableModelAndroidVendaCabecalho
						.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());

				calcularDatasVencimentos(buscarFormaPagamento(androidVendaCabecalho.getFormaPagamento()));

				buscarItensVenda(androidVendaCabecalho);
				calcularTotalVenda(androidVendaCabecalho);

				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);

			}
		});
		btnEditarVenda
				.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));

		JButton btnImprimirComprovante = new JButton("Imprimir Comprovante");
		btnImprimirComprovante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnImprimirComprovante.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));

		btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				androidVendaCabecalho = tableModelAndroidVendaCabecalho
						.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão da venda?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirVendaAndroid(androidVendaCabecalho);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5
				.setHorizontalGroup(
						gl_panel_5
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
										.addComponent(btnImprimirComprovante)
										.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
										.addComponent(btnExcluir).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnEditarVenda).addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnAprovarVenda).addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_5
				.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panel_5.createParallelGroup(Alignment.BASELINE).addComponent(btnImprimirComprovante)
						.addComponent(btnAprovarVenda).addComponent(btnEditarVenda).addComponent(btnExcluir))
				.addContainerGap()));
		panel_5.setLayout(gl_panel_5);

		JScrollPane scrollPane = new JScrollPane();

		tableVendaAndroid = new JTable();
		tableVendaAndroid.setAutoCreateRowSorter(true);
		tableVendaAndroid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					limparTabelaItens();

					androidVendaCabecalho = tableModelAndroidVendaCabecalho
							.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());

					buscarItensVenda(androidVendaCabecalho);
					calcularTotalVenda(androidVendaCabecalho);

					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);

				}
			}
		});
		scrollPane.setViewportView(tableVendaAndroid);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
				gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_3.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE).addGap(0)));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addComponent(scrollPane,
				GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE));
		panel_3.setLayout(gl_panel_3);

		JLabel lblVendedor = new JLabel("Vendedor");

		cbVendedor = new JComboBox();

		JLabel lblDataInicial = new JLabel("Data Inicial");

		dcInicial = new JDateChooser();

		JLabel lblDataFinal = new JLabel("Data Final");

		dcFinal = new JDateChooser();

		JButton btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparTabela();
				limparTabelaItens();

				if (chckbxUtilizarFiltro.isSelected()) {
					buscarComFiltro(dcInicial.getDate(), dcFinal.getDate(), (Usuario) cbVendedor.getSelectedItem());
				} else {
					buscarSemFiltro();
				}

			}
		});
		btnFiltrar.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_2
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup().addComponent(lblVendedor)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(cbVendedor, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE))
						.addGroup(
								gl_panel_2.createSequentialGroup().addComponent(lblDataInicial)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(dcInicial, GroupLayout.PREFERRED_SIZE, 173,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblDataFinal)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(dcFinal, GroupLayout.PREFERRED_SIZE, 187, GroupLayout.PREFERRED_SIZE)))
				.addPreferredGap(ComponentPlacement.RELATED, 67, Short.MAX_VALUE).addComponent(btnFiltrar)
				.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnFiltrar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_panel_2.createSequentialGroup()
								.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE).addComponent(lblVendedor)
										.addComponent(cbVendedor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
										.addComponent(lblDataInicial)
										.addComponent(dcInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(lblDataFinal).addComponent(dcFinal, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
				.addContainerGap(27, Short.MAX_VALUE)));
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes da venda", null, panel_1, null);

		tabbedPane.setEnabledAt(1, false);

		JPanel panel_4 = new JPanel();

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(Color.DARK_GRAY));

		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new LineBorder(Color.DARK_GRAY));

		JPanel panel_13 = new JPanel();
		panel_13.setBorder(new LineBorder(Color.DARK_GRAY));

		JLabel label = new JLabel("Sub Total");
		label.setFont(new Font("Dialog", Font.PLAIN, 12));

		lbSubTotal = new JLabel("R$ 0000,00");
		lbSubTotal.setFont(new Font("Dialog", Font.BOLD, 12));

		JLabel label_2 = new JLabel("Desconto");
		label_2.setFont(new Font("Dialog", Font.PLAIN, 12));

		lbDesconto = new JLabel("R$ 0000,00");

		JLabel label_4 = new JLabel("Total Geral");
		label_4.setFont(new Font("Dialog", Font.PLAIN, 12));

		lbTotalGeral = new JLabel("R$ 0000,00");
		lbTotalGeral.setFont(new Font("Dialog", Font.BOLD, 12));
		GroupLayout gl_panel_13 = new GroupLayout(panel_13);
		gl_panel_13.setHorizontalGroup(gl_panel_13.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 759, Short.MAX_VALUE)
				.addGroup(gl_panel_13.createSequentialGroup().addContainerGap().addComponent(label)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lbSubTotal).addGap(18)
						.addComponent(label_2).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lbDesconto)
						.addPreferredGap(ComponentPlacement.RELATED, 270, Short.MAX_VALUE).addComponent(label_4)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lbTotalGeral).addContainerGap()));
		gl_panel_13
				.setVerticalGroup(
						gl_panel_13
								.createParallelGroup(
										Alignment.TRAILING)
								.addGap(0, 37,
										Short.MAX_VALUE)
						.addGroup(gl_panel_13.createSequentialGroup()
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(gl_panel_13.createParallelGroup(Alignment.BASELINE).addComponent(lbTotalGeral)
										.addComponent(label_4).addComponent(label).addComponent(lbSubTotal)
										.addComponent(label_2).addComponent(lbDesconto))
								.addContainerGap()));
		panel_13.setLayout(gl_panel_13);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel_4, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 759,
										Short.MAX_VALUE)
						.addComponent(panel_6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
						.addComponent(panel_12, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
						.addComponent(panel_13, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel_1
				.setVerticalGroup(
						gl_panel_1
								.createParallelGroup(
										Alignment.TRAILING)
								.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
										.addComponent(panel_12, GroupLayout.PREFERRED_SIZE, 77,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_13, GroupLayout.PREFERRED_SIZE, 37,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(panel_6,
												GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));

		JLabel lblFormaDePagamento = new JLabel("Forma de pagamento");

		cbFormapagamentoVendaDetalhes = new JComboBox();
		cbFormapagamentoVendaDetalhes.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				calcularDatasVencimentos((FormaPagamento) cbFormapagamentoVendaDetalhes.getSelectedItem());
			}
		});

		JLabel lblVencimento = new JLabel("Vencimento(s)");

		JButton btnFinalizar = new JButton("Aprovar venda");
		btnFinalizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				androidVendaCabecalho = tableModelAndroidVendaCabecalho
						.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());
				
				List<Produto> pq = verificaEstoque(androidVendaCabecalho);
				
				if (pq.size() > 0) {
					for (int j = 0; j < pq.size(); j++) {
						JOptionPane.showMessageDialog(null, "A quantidade de " + pq.get(j).getNome() + " vendida é superior o estoque do produto! Favor alterar a quantidade. ");
					}						
				}else{
				
				FormaPagamento f = (FormaPagamento) cbFormapagamentoVendaDetalhes.getSelectedItem();

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Aprovar a venda na forma de pagamento " + f.getNome() + "?", "Aprovar Venda",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
					// FormaPagamento f = (FormaPagamento)
					// cbFormapagamentoVendaDetalhes.getSelectedItem();

					//androidVendaCabecalho = tableModelAndroidVendaCabecalho
					//		.getAndroidVendaCabecalho(tableVendaAndroid.getSelectedRow());

					aprovarVenda(androidVendaCabecalho, buscarCliente(androidVendaCabecalho.getCliente()), f,
							buscarUsuario(androidVendaCabecalho.getUsuario()));

					limparTabela();

					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(1, false);
				}

				}
			}
		});
		btnFinalizar
				.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		cbVencimentos = new JComboBox();
		GroupLayout gl_panel_12 = new GroupLayout(panel_12);
		gl_panel_12.setHorizontalGroup(gl_panel_12.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_12.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING).addComponent(lblFormaDePagamento)
								.addComponent(cbFormapagamentoVendaDetalhes, GroupLayout.PREFERRED_SIZE, 335,
										GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_12.createParallelGroup(Alignment.LEADING).addComponent(lblVencimento)
						.addComponent(cbVencimentos, GroupLayout.PREFERRED_SIZE, 186, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE).addComponent(btnFinalizar)
				.addContainerGap()));
		gl_panel_12.setVerticalGroup(gl_panel_12.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_12.createSequentialGroup().addContainerGap(29, Short.MAX_VALUE)
						.addComponent(btnFinalizar).addContainerGap())
				.addGroup(gl_panel_12.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_12.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_12.createSequentialGroup().addComponent(lblVencimento).addGap(25))
								.addGroup(gl_panel_12.createSequentialGroup().addComponent(lblFormaDePagamento)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_panel_12.createParallelGroup(Alignment.BASELINE)
												.addComponent(cbFormapagamentoVendaDetalhes, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(cbVencimentos, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(18, Short.MAX_VALUE)));
		panel_12.setLayout(gl_panel_12);

		JScrollPane scrollPane_1 = new JScrollPane();

		tableItensVenda = new JTable();
		tableItensVenda.setAutoCreateRowSorter(true);
		scrollPane_1.setViewportView(tableItensVenda);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
				gl_panel_4.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_4.createSequentialGroup()
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE).addGap(0)));
		gl_panel_4.setVerticalGroup(gl_panel_4.createParallelGroup(Alignment.LEADING).addComponent(scrollPane_1,
				GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE));
		panel_4.setLayout(gl_panel_4);

		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addItem(acaoBuscarProduto(), 1.0, androidVendaCabecalho);

				limparTabelaItens();
				buscarItensVenda(androidVendaCabecalho);

				calcularTotalVenda(androidVendaCabecalho);
			}
		});
		btnAddItem.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/maismais_24.png")));

		JButton btnExcluirItem = new JButton("Excluir Item");
		btnExcluirItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				androidVendaDetalhe = tableModelListaVendasAndroid
						.getAndroidVendaDetalhe(tableItensVenda.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão do item selecionado?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirAndroidVendaDetalhe(androidVendaDetalhe);
					limparTabelaItens();
					buscarItensVenda(androidVendaCabecalho);

					calcularTotalVenda(androidVendaCabecalho);
				}
			}

		});
		btnExcluirItem.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnAltQtd = new JButton("Alt. Qtd");
		btnAltQtd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				androidVendaDetalhe = tableModelListaVendasAndroid
						.getAndroidVendaDetalhe(tableItensVenda.getSelectedRow());

				Double quantidade = Double.parseDouble(JOptionPane.showInputDialog("Infome a Quantidade",
						tableModelListaVendasAndroid.getValueAt(tableItensVenda.getSelectedRow(), 3)));

				alterarQuantidadeAndroidVendaDetalhe(androidVendaDetalhe, quantidade);

				limparTabelaItens();
				buscarItensVenda(androidVendaCabecalho);

				calcularTotalVenda(androidVendaCabecalho);

			}
		});
		btnAltQtd
				.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));

		JButton btnDesconto = new JButton("Desconto");
		btnDesconto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				androidVendaDetalhe = tableModelListaVendasAndroid
						.getAndroidVendaDetalhe(tableItensVenda.getSelectedRow());

				Double desconto = Double.parseDouble(JOptionPane
						.showInputDialog("Infome o desconto desejado", androidVendaDetalhe.getValorDesconto())
						.replace(",", "."));

				alterarDescontoAndroidVendaDetalhe(androidVendaDetalhe, desconto);

				limparTabelaItens();
				buscarItensVenda(androidVendaCabecalho);

				calcularTotalVenda(androidVendaCabecalho);

			}
		});
		btnDesconto.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/forma_pagamento_24.png")));

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Salvar alterações?", "Salvar Venda",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {
					FormaPagamento f = (FormaPagamento) cbFormapagamentoVendaDetalhes.getSelectedItem();

					androidVendaCabecalho.setFormaPagamento(f.getId());

					trx.begin();
					manager.persist(androidVendaCabecalho);
					trx.commit();

					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(1, false);

					JOptionPane.showMessageDialog(null, "Venda salva com sucesso!");
				}
			}
		});
		btnSalvar.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap().addComponent(btnAddItem)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnExcluirItem)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnAltQtd)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnDesconto)
						.addPreferredGap(ComponentPlacement.RELATED, 88, Short.MAX_VALUE).addComponent(btnSalvar)
						.addContainerGap()));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_6
				.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING).addComponent(btnAddItem)
						.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE).addComponent(btnExcluirItem)
								.addComponent(btnAltQtd).addComponent(btnDesconto).addComponent(btnSalvar)))
				.addContainerGap()));
		panel_6.setLayout(gl_panel_6);
		panel_1.setLayout(gl_panel_1);

		JPanel panel_11 = new JPanel();
		tabbedPane.addTab("Contas recebidas", null, panel_11, null);

		JPanel panel_14 = new JPanel();
		panel_14.setBorder(new LineBorder(Color.DARK_GRAY));

		dcInicialRecebidas = new JDateChooser();
		dcInicialRecebidas.setEnabled(false);

		JLabel label_1 = new JLabel("Data Inicial");

		JLabel label_3 = new JLabel("Data final");

		dcFinalRecebidas = new JDateChooser();
		dcFinalRecebidas.setEnabled(false);

		JLabel lblRecebedor = new JLabel("Recebedor");

		cbVendedorContasReceber = new JComboBox();
		cbVendedorContasReceber.setEnabled(false);

		button = new JButton("Filtrar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				filtrarAndroidContasReceber();

			}
		});
		button.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		GroupLayout gl_panel_14 = new GroupLayout(panel_14);
		gl_panel_14
				.setHorizontalGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addGap(0, 759, Short.MAX_VALUE)
						.addGroup(gl_panel_14.createSequentialGroup().addContainerGap()
								.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING)
										.addComponent(dcInicialRecebidas, GroupLayout.PREFERRED_SIZE, 121,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(label_1))
						.addGap(18)
						.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addComponent(label_3).addComponent(
								dcFinalRecebidas, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addComponent(lblRecebedor)
								.addComponent(cbVendedorContasReceber, GroupLayout.PREFERRED_SIZE, 295,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, 56, Short.MAX_VALUE).addComponent(button)
						.addContainerGap()));
		gl_panel_14.setVerticalGroup(gl_panel_14.createParallelGroup(Alignment.LEADING).addGap(0, 72, Short.MAX_VALUE)
				.addGroup(gl_panel_14.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_14.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_14.createSequentialGroup().addComponent(lblRecebedor)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cbVendedorContasReceber, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(button)
						.addGroup(gl_panel_14.createSequentialGroup()
								.addGroup(gl_panel_14.createParallelGroup(Alignment.BASELINE).addComponent(label_1)
										.addComponent(label_3))
								.addGap(6)
								.addGroup(gl_panel_14.createParallelGroup(Alignment.LEADING)
										.addComponent(dcFinalRecebidas, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(dcInicialRecebidas, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
						.addContainerGap(13, Short.MAX_VALUE)));
		panel_14.setLayout(gl_panel_14);

		checkBoxRecebidas = new JCheckBox("Utilizar filtro");
		checkBoxRecebidas.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {

				if (checkBoxRecebidas.isSelected()) {
					dcInicialRecebidas.setEnabled(true);
					dcFinalRecebidas.setEnabled(true);
					cbVendedorContasReceber.setEnabled(true);

				} else {
					dcInicialRecebidas.setEnabled(false);
					dcFinalRecebidas.setEnabled(false);
					cbVendedorContasReceber.setEnabled(false);
				}
			}
		});

		JPanel panel_15 = new JPanel();

		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new LineBorder(Color.DARK_GRAY));

		JButton button_1 = new JButton("Aprovar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ArrayList<AndroidContaReceber> listaAndroidContaReceber = new ArrayList();

				int idx[] = tableContasRecebidas.getSelectedRows();

				for (int h = 0; h < idx.length; h++) {
					int linhaReal = tableContasRecebidas.convertRowIndexToModel(idx[h]);
					listaAndroidContaReceber.add(tableModelAndroidContasReceber.getAndroidContaReceber(linhaReal));
				}

				for (int i = 0; i < listaAndroidContaReceber.size(); i++) {

					Object[] options = { "Sim", "Não" };
					int w = JOptionPane.showOptionDialog(null,
							"ATENÇÃO!!! Confirma a aprovação da nota do cliente "
									+ buscarCliente(listaAndroidContaReceber.get(i).getCliente()) + " no valor de R$ "
									+ listaAndroidContaReceber.get(i).getValorDevido() + " recebida?",
							"Aprovação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
							options[0]);

					if (w == JOptionPane.YES_OPTION) {

						// AndroidContaReceber acr =
						// tableModelAndroidContasReceber
						// .getAndroidContaReceber(tableContasRecebidas.getSelectedRow());
						
						AndroidContaReceber acr = listaAndroidContaReceber.get(i);

						ContaReceber cr;

						// try {
						Query consulta = manager
								.createQuery("from ContaReceber where id like '" + acr.getVendaCabecalho() + "'");
						List<ContaReceber> listaContaReceber = consulta.getResultList();

						cr = listaContaReceber.get(0);

						receberNota(acr, cr);
						
						criarCaixaDoCaixa(listaAndroidContaReceber.get(i).getValorDevido(), dataAtual(), cr.getVendaCabecalho()); // Criar Caixa
						
						// } catch (Exception e2) {
						// JOptionPane.showMessageDialog(null, "ERRO!!");
						// }
						// limparTabela();

						// filtrarAndroidContasReceber();
					}

				}

				filtrarAndroidContasReceber();

				/*
				 * 
				 * Object[] options = { "Sim", "Não" }; int i =
				 * JOptionPane.showOptionDialog(null,
				 * "ATENÇÃO!!! Confirma a aprovação da nota recebida?",
				 * "Aprovação", JOptionPane.YES_NO_OPTION,
				 * JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				 * 
				 * if (i == JOptionPane.YES_OPTION) {
				 * 
				 * AndroidContaReceber acr = tableModelAndroidContasReceber
				 * .getAndroidContaReceber(tableContasRecebidas.getSelectedRow()
				 * ); ContaReceber cr;
				 * 
				 * // try { Query consulta = manager .createQuery(
				 * "from ContaReceber where id like '" + acr.getVendaCabecalho()
				 * + "'"); List<ContaReceber> listaContaReceber =
				 * consulta.getResultList();
				 * 
				 * cr = listaContaReceber.get(0);
				 * 
				 * receberNota(acr, cr); // } catch (Exception e2) { //
				 * JOptionPane.showMessageDialog(null, "ERRO!!"); // } //
				 * limparTabela();
				 * 
				 * filtrarAndroidContasReceber();
				 * 
				 * }
				 */
			}
		});
		button_1.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));

		JButton button_2 = new JButton("Relatório");
		button_2.setIcon(
				new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));

		JLabel label_6 = new JLabel("Total do caixa:");
		label_6.setFont(new Font("Dialog", Font.BOLD, 16));

		tfTotalContasRecebidas = new JLabel("R$ 0.0");
		tfTotalContasRecebidas.setFont(new Font("Dialog", Font.BOLD, 16));
		
		btnExcluir2 = new JButton("Excluir");
		btnExcluir2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				androidContaReceber = tableModelAndroidContasReceber
						.getAndroidContaReceber(tableContasRecebidas.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão da conta recebida?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirContaReceberAndroid(androidContaReceber);
					
					limparTabelaRecebidas();
				}
				
			}
		});
		btnExcluir2.setEnabled(false);
		btnExcluir2.setIcon(new ImageIcon(JanelaAndroid.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_16 = new GroupLayout(panel_16);
		gl_panel_16.setHorizontalGroup(
			gl_panel_16.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_16.createSequentialGroup()
					.addContainerGap()
					.addComponent(button_1)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(button_2)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnExcluir2, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
					.addComponent(label_6)
					.addGap(18)
					.addComponent(tfTotalContasRecebidas)
					.addContainerGap())
		);
		gl_panel_16.setVerticalGroup(
			gl_panel_16.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_16.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_16.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_16.createParallelGroup(Alignment.BASELINE)
							.addComponent(tfTotalContasRecebidas)
							.addComponent(label_6)
							.addComponent(button_1)
							.addComponent(button_2))
						.addComponent(btnExcluir2, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_16.setLayout(gl_panel_16);

		cbMostrarTodasContas = new JCheckBox("Mostrar todas contas recebidas");
		GroupLayout gl_panel_11 = new GroupLayout(panel_11);
		gl_panel_11.setHorizontalGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_14, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
								.addGroup(gl_panel_11.createSequentialGroup()
										.addComponent(checkBoxRecebidas, GroupLayout.PREFERRED_SIZE, 114,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, 393, Short.MAX_VALUE)
										.addComponent(cbMostrarTodasContas))
						.addComponent(panel_15, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
						.addComponent(panel_16, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)).addContainerGap()));
		gl_panel_11.setVerticalGroup(gl_panel_11.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_11.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_11.createParallelGroup(Alignment.BASELINE).addComponent(checkBoxRecebidas)
								.addComponent(cbMostrarTodasContas))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_14, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_15, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_16, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		JScrollPane scrollPane_3 = new JScrollPane();

		tableContasRecebidas = new JTable();
		tableContasRecebidas.setAutoCreateRowSorter(true);
		scrollPane_3.setViewportView(tableContasRecebidas);
		GroupLayout gl_panel_15 = new GroupLayout(panel_15);
		gl_panel_15.setHorizontalGroup(gl_panel_15.createParallelGroup(Alignment.LEADING).addComponent(scrollPane_3,
				Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE));
		gl_panel_15.setVerticalGroup(
				gl_panel_15.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_15.createSequentialGroup()
						.addGap(5).addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)));
		panel_15.setLayout(gl_panel_15);
		panel_11.setLayout(gl_panel_11);
	}

	private void carregarTableModel() {
		this.tableModelAndroidVendaCabecalho = new TableModelAndroidVendaCabecalho();
		this.tableVendaAndroid.setModel(tableModelAndroidVendaCabecalho);

		this.tableModelListaVendasAndroid = new TableModelListaVendasAndroid();
		this.tableItensVenda.setModel(tableModelListaVendasAndroid);

		this.tableModelAndroidCaixa = new TableModelAndroidCaixa();

		this.tableModelAndroidContasReceber = new TableModelAndroidContasReceber();
		this.tableContasRecebidas.setModel(tableModelAndroidContasReceber);

		// this.tableModelContasReceber = new TableModelContasReceber();

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);

		tableVendaAndroid.getColumnModel().getColumn(3).setCellRenderer(cellRendererCustomMoeda);

		tableItensVenda.getColumnModel().getColumn(2).setCellRenderer(cellRendererCustomMoeda);
		tableItensVenda.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);
		tableItensVenda.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
		tableItensVenda.getColumnModel().getColumn(6).setCellRenderer(cellRendererCustomMoeda);

		tableContasRecebidas.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);

	}

	/*
	 * private void buscarComFiltro() { try { trx.begin(); Query consulta =
	 * manager.createQuery("from AndroidVendaCabecalho where venda_aprovada = 0"
	 * ); List<AndroidVendaCabecalho> listaAndroidVendaCabecalho =
	 * consulta.getResultList(); trx.commit();
	 * 
	 * for (int i = 0; i < listaAndroidVendaCabecalho.size(); i++) {
	 * AndroidVendaCabecalho avc = listaAndroidVendaCabecalho.get(i);
	 * tableModelAndroidVendaCabecalho.addAndroidVendaCabecalho(avc); } } catch
	 * (Exception e) { JOptionPane.showMessageDialog(null,
	 * "Erro ao carregar a tabela de vendas do android: " + e); } }
	 */
	private void buscarSemFiltro() {
		// System.out.println("PASSEI AKI 01");
		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidVendaCabecalho where venda_aprovada = 0");
			List<AndroidVendaCabecalho> listaAndroidVendaCabecalho = consulta.getResultList();

			for (int i = 0; i < listaAndroidVendaCabecalho.size(); i++) {
				AndroidVendaCabecalho avc = listaAndroidVendaCabecalho.get(i);
				tableModelAndroidVendaCabecalho.addAndroidVendaCabecalho(avc);
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarCaixaSemFiltro() {

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidCaixa where ativo = 1");
			List<AndroidCaixa> listaAndroidCaixa = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidCaixa.size(); i++) {
				AndroidCaixa ac = listaAndroidCaixa.get(i);
				tableModelAndroidCaixa.addAndroidCaixa(ac);

				total = total + ac.getValor();
			}
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

/*
	private void buscarCaixaSemFiltroTudo() {

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidCaixa");
			List<AndroidCaixa> listaAndroidCaixa = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidCaixa.size(); i++) {
				AndroidCaixa ac = listaAndroidCaixa.get(i);
				tableModelAndroidCaixa.addAndroidCaixa(ac);

				total = total + ac.getValor();
			}

			lbTotal.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}
*/
	
/*
	private void buscarCaixaComFiltroTudo(Usuario u, Date dtInicial, Date dtFinal) {

		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidCaixa where data_recebimento between '" + dataInical
					+ "' and '" + dataFinal + "' and usuario_id like '" + u.getId() + "'");
			List<AndroidCaixa> listaAndroidCaixa = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidCaixa.size(); i++) {
				AndroidCaixa ac = listaAndroidCaixa.get(i);
				tableModelAndroidCaixa.addAndroidCaixa(ac);

				total = total + ac.getValor();
			}

			lbTotal.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}
*/
	
/*	
	private void buscarCaixaComFiltro(Usuario u, Date dtInicial, Date dtFinal) {

		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidCaixa where data_recebimento between '" + dataInical
					+ "' and '" + dataFinal + "' and usuario_id like '" + u.getId() + "' and ativo = 1");
			List<AndroidCaixa> listaAndroidCaixa = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidCaixa.size(); i++) {
				AndroidCaixa ac = listaAndroidCaixa.get(i);
				tableModelAndroidCaixa.addAndroidCaixa(ac);

				total = total + ac.getValor();
			}

			lbTotal.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}
*/
	
	private void buscarContaReceberSemFiltroTudo() {

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidContaReceber");
			List<AndroidContaReceber> listaAndroidContaReceber = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidContaReceber.size(); i++) {
				AndroidContaReceber acr = listaAndroidContaReceber.get(i);
				tableModelAndroidContasReceber.addAndroidContaReceber(acr);

				total = total + acr.getValorDevido();
			}

			tfTotalContasRecebidas.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarContaReceberSemFiltro() {

		Double total = 0.0;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidContaReceber where ativo = '1'");
			List<AndroidContaReceber> listaAndroidContaReceber = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidContaReceber.size(); i++) {
				AndroidContaReceber acr = listaAndroidContaReceber.get(i);
				tableModelAndroidContasReceber.addAndroidContaReceber(acr);

				total = total + acr.getValorDevido();
			}

			tfTotalContasRecebidas.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarContaReceberComFiltro(Date dtInicial, Date dtFinal, Usuario usuario) {

		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());

		Double total = 0.0;

		try {

			Query consulta = manager
					.createQuery("from AndroidContaReceber where ativo = '1' AND dataTransmissao BETWEEN '" + dataInical
							+ "' AND '" + dataFinal + "' " + "AND usuario_id like '" + usuario.getId() + "'");
			List<AndroidContaReceber> listaAndroidContaReceber = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidContaReceber.size(); i++) {
				AndroidContaReceber acr = listaAndroidContaReceber.get(i);
				tableModelAndroidContasReceber.addAndroidContaReceber(acr);

				total = total + acr.getValorDevido();
			}

			tfTotalContasRecebidas.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarContaReceberComFiltroTudo(Date dtInicial, Date dtFinal, Usuario usuario) {

		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());

		Double total = 0.0;

		try {

			Query consulta = manager.createQuery("from AndroidContaReceber where dataTransmissao BETWEEN '" + dataInical
					+ "' AND '" + dataFinal + "' " + "AND usuario_id like '" + usuario.getId() + "'");
			List<AndroidContaReceber> listaAndroidContaReceber = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidContaReceber.size(); i++) {
				AndroidContaReceber acr = listaAndroidContaReceber.get(i);
				tableModelAndroidContasReceber.addAndroidContaReceber(acr);

				total = total + acr.getValorDevido();
			}

			tfTotalContasRecebidas.setText("R$ " + total);
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarComFiltro(Date dtInicial, Date dtFinal, Usuario usuario) {

		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from AndroidVendaCabecalho where data_venda BETWEEN '" + dataInical
					+ "' AND '" + dataFinal + "' " + "AND usuario_id like '" + usuario.getId() + "' AND venda_aprovada = '0'");
			List<AndroidVendaCabecalho> listaAndroidVendaCabecalho = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidVendaCabecalho.size(); i++) {
				AndroidVendaCabecalho avc = listaAndroidVendaCabecalho.get(i);
				tableModelAndroidVendaCabecalho.addAndroidVendaCabecalho(avc);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de vendas do android: " + e);
		}
	}

	private void buscarItensVenda(AndroidVendaCabecalho avc) {
		try {
			// trx.begin();
			cbFormapagamentoVendaDetalhes.setSelectedItem(buscarFormaPagamento(avc.getFormaPagamento()));

			Query consulta = manager.createQuery(
					"from AndroidVendaDetalhe where android_venda_cabecalho_id like '" + avc.getId() + "'");
			List<AndroidVendaDetalhe> listaAndroidVendaDetalhes = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidVendaDetalhes.size(); i++) {
				AndroidVendaDetalhe avd = listaAndroidVendaDetalhes.get(i);
				tableModelListaVendasAndroid.addAndroidVendaDetalhe(avd);
			}
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de
			// venda detalhes do android: " + e);
		}
	}

	private void limparTabela() {
		while (tableVendaAndroid.getModel().getRowCount() > 0) {
			tableModelAndroidVendaCabecalho.removeAndroidVendaCabecalho(0);
		}
	}

	private void limparTabelaItens() {
		while (tableItensVenda.getModel().getRowCount() > 0) {
			tableModelListaVendasAndroid.removeAndroidVendaDetalhe(0);
		}
	}
	
/*
	private void limparTabelaCaixa() {
		while (tableCaixa.getModel().getRowCount() > 0) {
			tableModelAndroidCaixa.removeAndroidCaixa(0);
		}
	}
*/
	
	private void limparTabelaRecebidas() {
		while (tableContasRecebidas.getModel().getRowCount() > 0) {
			tableModelAndroidContasReceber.removeAndroidContaReceber(0);
		}
	}

	private void carregajcbVendedor() {

		cbVendedor.removeAllItems();
		cbVendedorContasReceber.removeAllItems();

		try {

			// trx.begin();
			Query consulta = manager.createQuery("from Usuario ORDER BY nome ASC");
			List<Usuario> listaUsuarios = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaUsuarios.size(); i++) {

				Usuario u = listaUsuarios.get(i);
				cbVendedor.addItem(u);
				//cbVendedorCaixa.addItem(u);
				cbVendedorContasReceber.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox de vendedores! " + e);
			trx.commit();
		}
	}

	/*
	 * private void carregajcbRota() {
	 * 
	 * try {
	 * 
	 * trx.begin(); Query consulta = manager.createQuery("from Rota");
	 * List<Rota> listaRotas = consulta.getResultList(); trx.commit();
	 * 
	 * for (int i = 0; i < listaRotas.size(); i++) {
	 * 
	 * Rota r = listaRotas.get(i); cbRota.addItem(r); }
	 * 
	 * } catch (Exception e) { JOptionPane.showMessageDialog(null,
	 * "Erro no carregamento da rota! " + e); } }
	 */
	private void carregajcbFormaPagamento() {

		cbFormapagamentoVendaDetalhes.removeAllItems();

		try {

			trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento ORDER BY nome ASC");
			List<FormaPagamento> listaFormaPagamento = consulta.getResultList();
			trx.commit();

			for (int i = 0; i < listaFormaPagamento.size(); i++) {

				FormaPagamento f = listaFormaPagamento.get(i);
				cbFormapagamentoVendaDetalhes.addItem(f);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento da Forma de pagamento! " + e);
		}
	}

	private void ativarFiltroBusca(Boolean op) {

		cbVendedor.setEnabled(op);

		dcFinal.setEnabled(op);
		dcInicial.setEnabled(op);

	}

	private void excluirAndroidVendaDetalhe(AndroidVendaDetalhe androidvd) {
		try {

			trx.begin();
			manager.remove(androidvd);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O item foi removido com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void alterarQuantidadeAndroidVendaDetalhe(AndroidVendaDetalhe androidvd, Double qtd) {
		try {

			Double qtdAntiga = androidvd.getQuantidade();

			Double valorUnit = androidvd.getValorParcial() / qtdAntiga;

			Double desconto = androidvd.getValorDesconto();

			androidvd.setQuantidade(qtd);

			Double valorParcial = valorUnit * qtd;

			Double valorTotal = valorParcial - desconto;

			androidvd.setValorParcial(valorParcial);
			androidvd.setValorTotal(valorParcial - desconto);

			trx.begin();
			manager.persist(androidvd);
			trx.commit();

			JOptionPane.showMessageDialog(null, "A quantidade do item foi alterada com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void alterarDescontoAndroidVendaDetalhe(AndroidVendaDetalhe androidvd, Double desc) {
		try {

			Double qtdAntiga = androidvd.getQuantidade();

			Double valorUnit = androidvd.getValorParcial() / qtdAntiga;

			Double desconto = desc;

			Double valorParcial = valorUnit * qtdAntiga;

			Double valorTotal = valorParcial - desconto;

			androidvd.setValorParcial(valorParcial);
			androidvd.setValorTotal(valorParcial - desconto);
			androidvd.setValorDesconto(desconto);

			trx.begin();
			manager.persist(androidvd);
			trx.commit();

			JOptionPane.showMessageDialog(null, "O desconto do item foi alterado com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	/*
	 * private void criarVendaCabecalho(Cliente cliente, AndroidVendaCabecalho
	 * avc, Usuario usuario){
	 * 
	 * VendaCabecalho vc = new VendaCabecalho();
	 * 
	 * try { vc.setCliente(cliente); vc.setDataVenda(avc.getDataVenda());
	 * vc.setFormaPagamento(null); vc.setUsuario(usuario);
	 * vc.setValorDesconto(avc.getValorDesconto());
	 * vc.setValorParcial(avc.getValorParcial());
	 * vc.setValorTotal(avc.getValorTotal()); //vc.setVendaCabecalho(null);
	 * 
	 * trx.begin(); manager.persist(vc); trx.commit(); } catch (Exception e) {
	 * JOptionPane.showMessageDialog(null, "Erro ao salvar a venda Cabeçalho: "
	 * + e.getMessage()); }
	 */

	private void addItem(Produto produto, Double quantidade, AndroidVendaCabecalho androidVendaCabecalho) {

		AndroidVendaDetalhe androidVendaDetalhe = new AndroidVendaDetalhe();

		// try {
		androidVendaDetalhe.setProduto(produto.getId());
		androidVendaDetalhe.setQuantidade(quantidade);
		androidVendaDetalhe.setValorDesconto(0.0);
		androidVendaDetalhe.setValorParcial(quantidade * produto.getValorDesejavelVenda());
		androidVendaDetalhe.setValorTotal(quantidade * produto.getValorDesejavelVenda());
		androidVendaDetalhe.setAndroidVendaCabecalho(androidVendaCabecalho.getId());

		trx.begin();
		manager.persist(androidVendaDetalhe);
		trx.commit();
		// } catch (Exception e) {
		// JOptionPane.showMessageDialog(null, "Erro ao adicionar o item: " +
		// e.getMessage());
		// }

	}

	private Produto acaoBuscarProduto() {

		Produto pd = new Produto();

		BuscarProduto buscarProduto = new BuscarProduto();

		buscarProduto.setModal(true);
		buscarProduto.setLocationRelativeTo(null);
		buscarProduto.setVisible(true);

		pd = buscarProduto.getProduto();

		return pd;

	}

	private void calcularTotalVenda(AndroidVendaCabecalho avc) {

		Double valorParcial = 0.0;
		Double desconto = 0.0;
		Double total = 0.0;

		try {
			Query consulta = manager.createQuery(
					"from AndroidVendaDetalhe where android_venda_cabecalho_id like '" + avc.getId() + "'");
			List<AndroidVendaDetalhe> listaAndroidVendaDetalhe = consulta.getResultList();

			for (int i = 0; i < listaAndroidVendaDetalhe.size(); i++) {
				AndroidVendaDetalhe avd = listaAndroidVendaDetalhe.get(i);

				valorParcial = valorParcial + avd.getValorParcial();
				desconto = desconto + avd.getValorDesconto();
				total = total + avd.getValorTotal();

			}

			avc.setValorDesconto(desconto);
			avc.setValorParcial(valorParcial);
			avc.setValorTotal(total);

			lbSubTotal.setText("R$ " + valorParcial);
			lbDesconto.setText("R$ " + desconto);
			lbTotalGeral.setText("R$ " + total);

			trx.begin();
			manager.persist(avc);
			trx.commit();

		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Erro ao calcular totais do cabeçalho: " + e);
		}

	}

	private FormaPagamento buscarFormaPagamento(Long id) {

		FormaPagamento formaPagamento = null;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento where id like '" + id + "'");
			List<FormaPagamento> listaFormaPagamento = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaFormaPagamento.size(); i++) {
				formaPagamento = listaFormaPagamento.get(i);

			}
		} catch (Exception e) {
			formaPagamento = null;
			JOptionPane.showMessageDialog(null, "Erro ao buscar a forma de pagamento: " + e);
		}

		return formaPagamento;
	}

	private Cliente buscarCliente(Long id) {

		Cliente cliente = null;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from Cliente where id like '" + id + "'");
			List<Cliente> listaCliente = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCliente.size(); i++) {
				cliente = listaCliente.get(i);

			}
		} catch (Exception e) {
			cliente = null;
			JOptionPane.showMessageDialog(null, "Erro ao buscar o cliente: " + e);
		}

		return cliente;
	}

	private Usuario buscarUsuario(Long id) {

		Usuario usuario = null;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from Usuario where id like '" + id + "'");
			List<Usuario> listaUsuario = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaUsuario.size(); i++) {
				usuario = listaUsuario.get(i);

			}
		} catch (Exception e) {
			usuario = null;
			JOptionPane.showMessageDialog(null, "Erro ao buscar o usuario: " + e);
		}

		return usuario;
	}

	private Produto buscarProduto(Long id) {

		Produto produto = null;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from Produto where id like '" + id + "'");
			List<Produto> listaProduto = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaProduto.size(); i++) {
				produto = listaProduto.get(i);

			}
		} catch (Exception e) {
			produto = null;
			JOptionPane.showMessageDialog(null, "Erro ao buscar o usuario: " + e);
		}

		return produto;
	}

	private VendaCabecalho buscarVendaCabecalho(Long id) {

		VendaCabecalho vendaCabecalho = null;

		try {
			// trx.begin();
			Query consulta = manager.createQuery("from VendaCabecalho where id like '" + id + "'");
			List<VendaCabecalho> listaVendaCabecalho = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaVendaCabecalho.size(); i++) {
				vendaCabecalho = listaVendaCabecalho.get(i);

			}
		} catch (Exception e) {
			vendaCabecalho = null;
			JOptionPane.showMessageDialog(null, "Erro ao buscar o usuario: " + e);
		}

		return vendaCabecalho;
	}

	private void baixaEstoque(VendaCabecalho vc) {

		try {

			Query consulta = manager
					.createQuery("from VendaDetalhe where venda_cabecalho_id like '" + vc.getId() + "'");
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

	private void aprovarVenda(AndroidVendaCabecalho avc, Cliente cliente, FormaPagamento formaPagamento,
			Usuario usuario) {

		VendaCabecalho vc = new VendaCabecalho();

		try {
			vc.setCliente(cliente);
			vc.setDataVenda(avc.getDataVenda());
			vc.setFormaPagamento(formaPagamento);
			vc.setUsuario(usuario);
			vc.setValorDesconto(avc.getValorDesconto());
			vc.setValorParcial(avc.getValorParcial());
			vc.setValorTotal(avc.getValorTotal());

			androidVendaCabecalho.setVendaAprovada(true);

			trx.begin();
			manager.persist(vc);
			manager.persist(androidVendaCabecalho);
			trx.commit();

			criarVendasDetalhes(avc, vc);

			imprimirComprovante(vc, cliente, formaPagamento, usuario);
			imprimirComprovante2Via(vc, cliente, formaPagamento, usuario);

			/*
			 * Object[] options = { "Sim", "Não" }; int i =
			 * JOptionPane.showOptionDialog(null,
			 * "Gerar 2ª via do comprovante de venda?", "2ª via",
			 * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
			 * options, options[0]);
			 * 
			 * if (i == JOptionPane.YES_OPTION) { imprimirComprovante(vc,
			 * cliente, formaPagamento, usuario); imprimirComprovante2Via(vc,
			 * cliente, formaPagamento, usuario); } else {
			 * imprimirComprovante(vc, cliente, formaPagamento, usuario); }
			 */
			if (formaPagamento.getNumeroDias() > 0) {
				criarContaReceber(cliente, vc, avc.getValorTotal());
			} else {
				criarCaixa(vc);
			}

			baixaEstoque(vc);

			JOptionPane.showMessageDialog(null, "Venda aprovada com sucesso!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao aprovar venda: " + e);
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

	private void criarCaixaDoCaixa(Double valor, Date data, VendaCabecalho vendaCabecalho) {

		try {
			Caixa caixa = new Caixa();

			caixa.setData(data);
			caixa.setValor(valor);
			caixa.setVendaCabecalho(vendaCabecalho);

			trx.begin();
			manager.persist(caixa);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Caixa criado com sucesso!!!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao cria caixa!" + e);
		}

	}

	private void criarCaixaDoCaixa2(AndroidCaixa ac) {

		try {
			Caixa caixa = new Caixa();

			caixa.setData(dataAtual());
			caixa.setValor(ac.getValor());
			caixa.setVendaCabecalho(buscarVendaCabecalho(ac.getVendaCabecalho()));

			ac.setAtivo(false); // Muda o estado para recebido

			trx.begin();
			manager.persist(caixa);
			manager.persist(ac); // Salva o AndroidCaixa
			trx.commit();

			JOptionPane.showMessageDialog(null, "Aprovado com sucesso!!!");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao cria caixa!" + e);
		}

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

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private void criarVendasDetalhes(AndroidVendaCabecalho avc, VendaCabecalho vc) {

		VendaDetalhe vendaDetalhe;

		try {
			// trx.begin();
			Query consulta = manager.createQuery(
					"from AndroidVendaDetalhe where android_venda_cabecalho_id like '" + avc.getId() + "'");
			List<AndroidVendaDetalhe> listaAndroidVendaDetalhes = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidVendaDetalhes.size(); i++) {
				AndroidVendaDetalhe avd = listaAndroidVendaDetalhes.get(i);

				vendaDetalhe = new VendaDetalhe();

				vendaDetalhe.setProduto(buscarProduto(avd.getProduto()));
				vendaDetalhe.setQuantidade(avd.getQuantidade());
				vendaDetalhe.setValorDesconto(avd.getValorDesconto());
				vendaDetalhe.setValorParcial(avd.getValorParcial());
				vendaDetalhe.setValorTotal(avd.getValorTotal());
				vendaDetalhe.setVendaCabecalho(vc);

				trx.begin();
				manager.persist(vendaDetalhe);
				trx.commit();

			}
		} catch (Exception e) {
			// JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de
			// venda detalhes do android: " + e);
		}

	}

	private void calcularDatasVencimentos(FormaPagamento f) {

		cbVencimentos.removeAllItems();

		int qtdDias = f.getNumeroDias();

		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		try {

			for (int i = 0; i < f.getNumeroParcelas(); i++) {
				// ContaReceber contaReceber = new ContaReceber();

				gcGregorian.set(GregorianCalendar.DAY_OF_MONTH,
						gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

				String dt = formatBRA.format(gcGregorian.getTime());

				cbVencimentos.addItem(dt);
				// contaReceber.setVencimento(gcGregorian.getTime());

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao informar os vencimentos!");
		}
	}

	private void excluirVendaAndroid(AndroidVendaCabecalho avc) {
		try {

			Query consulta = manager.createQuery(
					"from AndroidVendaDetalhe where android_venda_cabecalho_id like '" + avc.getId() + "'");
			List<AndroidVendaDetalhe> listaAndroidVendaDetalhe = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaAndroidVendaDetalhe.size(); i++) {
				AndroidVendaDetalhe avd = listaAndroidVendaDetalhe.get(i);

				trx.begin();
				manager.remove(avd);
				trx.commit();
			}

			trx.begin();
			manager.remove(avc);
			trx.commit();

			JOptionPane.showMessageDialog(null, "A venda foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void excluirContaReceberAndroid(AndroidContaReceber acr) {
		try {

			Query consulta = manager.createQuery(
					"from AndroidContaReceber where id like '" + acr.getId() + "'");
			List<AndroidContaReceber> listaAndroidContaReceber = consulta.getResultList();
			// trx.commit();
			
			AndroidContaReceber a = listaAndroidContaReceber.get(0);
			
			trx.begin();
			manager.remove(a);
			trx.commit();

			JOptionPane.showMessageDialog(null, "A Conta Recebida foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void imprimirComprovante(VendaCabecalho vc, Cliente c, FormaPagamento f, Usuario u) {

		// #################### INICIO CAPTURANDO ENDEREÇO ###################
		EnderecoCliente enderecoCliente = null;
		try {
			enderecoCliente = c.getEnderecos().get(0);
		} catch (Exception e2) {
			enderecoCliente = null;
		}
		// #################### FIM CAPTURANDO ENDEREÇO ###################

		// ################## INICIO CAPTURANDO FORMA DE PAGAMENTO
		// ####################
		// FormaPagamento f = (FormaPagamento)
		// cbFormaPagamento.getSelectedItem();
		// ################## FIM CAPTURANDO FORMA DE PAGAMENTO
		// ####################

		// ###################### INICIO PEGANDO DATA VENCIMENRTO
		// ###################

		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		gcGregorian.set(GregorianCalendar.DAY_OF_MONTH,
				gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

		String dtVencimento = formatBRA.format(gcGregorian.getTime());

		// ###################### FIM PEGANDO DATA VENCIMENRTO
		// ###################

		ChamaRelatorioComprovanteVenda chamaRelatorio = new ChamaRelatorioComprovanteVenda();

		try {

			// chamaRelatorio.report((Usuario) cbUsuario.getSelectedItem(),
			// cliente, enderecoCliente, f.getNome(),
			// format.format(dataAtual()), dtVencimento, vcu.getId().toString(),
			// vcu.getValorParcial().toString(),
			// vcu.getValorDesconto().toString(),
			// vcu.getValorTotal().toString());
			chamaRelatorio.report(u, c, enderecoCliente, f.getNome(), formatBRA.format(dataAtual()), dtVencimento,
					vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage());
		}
	}

	private void imprimirComprovante2Via(VendaCabecalho vc, Cliente c, FormaPagamento f, Usuario u) {

		// #################### INICIO CAPTURANDO ENDEREÇO ###################
		EnderecoCliente enderecoCliente = null;
		try {
			enderecoCliente = c.getEnderecos().get(0);
		} catch (Exception e2) {
			enderecoCliente = null;
		}
		// #################### FIM CAPTURANDO ENDEREÇO ###################

		// ################## INICIO CAPTURANDO FORMA DE PAGAMENTO
		// ####################
		// FormaPagamento f = (FormaPagamento)
		// cbFormaPagamento.getSelectedItem();
		// ################## FIM CAPTURANDO FORMA DE PAGAMENTO
		// ####################

		// ###################### INICIO PEGANDO DATA VENCIMENRTO
		// ###################

		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(dataAtual());

		gcGregorian.set(GregorianCalendar.DAY_OF_MONTH,
				gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

		String dtVencimento = formatBRA.format(gcGregorian.getTime());

		// ###################### FIM PEGANDO DATA VENCIMENRTO
		// ###################

		ChamaRelatorioComprovanteVenda2Via chamaRelatorio = new ChamaRelatorioComprovanteVenda2Via();

		try {

			// chamaRelatorio.report((Usuario) cbUsuario.getSelectedItem(),
			// cliente, enderecoCliente, f.getNome(),
			// format.format(dataAtual()), dtVencimento, vcu.getId().toString(),
			// vcu.getValorParcial().toString(),
			// vcu.getValorDesconto().toString(),
			// vcu.getValorTotal().toString());
			chamaRelatorio.report(u, c, enderecoCliente, f.getNome(), formatBRA.format(dataAtual()), dtVencimento,
					vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage());
		}
	}

	private void receberNota(AndroidContaReceber acr, ContaReceber cr) {

		if (acr.getValorDevido() < cr.getValorDevido()) {

			cr.setValorDevido(cr.getValorDevido() - acr.getValorDevido());

			acr.setAtivo(false);

			try {
				trx.begin();
				manager.persist(cr);
				trx.commit();

				JOptionPane.showMessageDialog(null, "A conta foi abatida com sucesso!!!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "ERRO!! ao abater a conta a receber!!!");
			}

		} else {
			cr.setValorDevido(0.0);
			cr.setQuitada(true);

			acr.setAtivo(false);

			try {
				trx.begin();
				manager.persist(cr);
				trx.commit();

				JOptionPane.showMessageDialog(null, "Conta recebida com sucesso!!!");
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "ERRO!! ao quitar a conta a receber!!!");
			}
		}
		/*
		 * if (acr.getValorDevido() == cr.getValorDevido()) {
		 * 
		 * cr.setValorDevido(0.0); cr.setQuitada(true);
		 * 
		 * try { trx.begin(); manager.persist(cr); trx.commit();
		 * 
		 * JOptionPane.showMessageDialog(null, "Conta recebida com sucesso!!!");
		 * } catch (Exception e) { JOptionPane.showMessageDialog(null,
		 * "ERRO!! ao quitar a conta a receber!!!"); }
		 * 
		 * 
		 * }
		 */
	}

	private void filtrarAndroidContasReceber() {
		limparTabelaRecebidas();

		if (checkBoxRecebidas.isSelected()) {

			Usuario u = (Usuario) cbVendedorContasReceber.getSelectedItem();
			if (cbMostrarTodasContas.isSelected()) {
				buscarContaReceberComFiltroTudo(dcInicialRecebidas.getDate(), dcFinalRecebidas.getDate(), u);
			} else {
				buscarContaReceberComFiltro(dcInicialRecebidas.getDate(), dcFinalRecebidas.getDate(), u);
			}

		} else {

			if (cbMostrarTodasContas.isSelected()) {
				buscarContaReceberSemFiltroTudo();
			} else {
				buscarContaReceberSemFiltro();
			}
		}
	}
	
	/*
	private List<Produto> verificaEstoque(AndroidVendaCabecalho avc) {

		List<Produto> retorno = null;
		
		try {

			Query consulta = manager
					.createQuery("from VendaDetalhe where venda_cabecalho_id like '" + vc.getId() + "'");
			List<VendaDetalhe> listaVendaDetalhe = consulta.getResultList();

			for (int i = 0; i < listaVendaDetalhe.size(); i++) {
				Produto p = listaVendaDetalhe.get(i).getProduto();

				Double q = p.getQuantidadeEstoque() - listaVendaDetalhe.get(i).getQuantidade();
				
				if (q < 0) {
					retorno.add(p);
				}				
			}

		} catch (Exception e) {
			//trx.rollback();
			JOptionPane.showMessageDialog(null, "Erro ao verificar quantidade em estoque no estoque!!! " + e);
		}
		return retorno;

	}
	*/
	
	private  List<Produto> verificaEstoque(AndroidVendaCabecalho avc) {

		List<Produto> retorno = new ArrayList<>();
		
		//try {
			
			Query consulta = manager.createQuery(
					"from AndroidVendaDetalhe where android_venda_cabecalho_id like '" + avc.getId() + "'");
			List<AndroidVendaDetalhe> listaAndroidVendaDetalhes = consulta.getResultList();
			
			for (int i = 0; i < listaAndroidVendaDetalhes.size(); i++) {
				AndroidVendaDetalhe avd = listaAndroidVendaDetalhes.get(i);

				Produto p = buscarProduto(avd.getProduto());
				
				Double q = p.getQuantidadeEstoque() - avd.getQuantidade();
				
				if (q < 0) {
					retorno.add(p);
				}				
			}
		//} catch (Exception e) {
		//	JOptionPane.showMessageDialog(null, "Erro ao verificar quantidade em estoque no estoque!!! " + e);
		//}
		return retorno;
	}
	
private void permicoes(){
				
		if (cargo.isAcessoContasReceber()) {
			btnExcluir2.setEnabled(true);
		}
		
		if (cargo.isAcessoVendas()) {
			btnExcluir.setEnabled(true);
		}
		
	}
}
