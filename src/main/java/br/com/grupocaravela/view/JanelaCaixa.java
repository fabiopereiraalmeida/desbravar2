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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.Caixa;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.relatorios.ChamaRelatorioEspelho;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelCaixa;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;

import javax.swing.LayoutStyle.ComponentPlacement;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import java.awt.Font;

public class JanelaCaixa extends JFrame {

	// private EntityManagerProducer entityManagerProducer = new
	// EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCaixa tableModelCaixa;

	private JPanel contentPane;
	private JTable tableLista;

	private Caixa caixa;
	private JTextField tfValorTotal;
	private JDateChooser dcDataInicial;
	private JDateChooser dcDataFinal;

	private SimpleDateFormat formatDataHoraInternacional = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private JComboBox cbUsuario;
	//private SimpleDateFormat formatDataHoraInternacional = new SimpleDateFormat("yyyy-MM-dd");
	
	// private RepositorioCaixa repositorioCaixa = new RepositorioCaixa();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCaixa frame = new JanelaCaixa();
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
	public JanelaCaixa() {

		criarJanela();
		carregarTableModel();
		
		tamanhoColunas();

		iniciaConexao();
		
		carregajcbUsuario();
		
		dcDataInicial.setDate(dataAtual());
		dcDataFinal.setDate(dataAtual());

		// Evento ao abrir a janela
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				//tfLocalizar.requestFocus();
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

		setTitle("Caixa");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCaixa.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 385, Short.MAX_VALUE).addContainerGap()));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Caixa simples", null, panel, null);

		JScrollPane scrollPane = new JScrollPane();

		tableLista = new JTable();
		tableLista.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableLista);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				caixa = tableModelCaixa.getCaixa(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Confirma a Exclusão da caixa ?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirCaixa(caixa);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(
				new ImageIcon(JanelaCaixa.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(JanelaCaixa.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		dcDataInicial = new JDateChooser();

		JLabel lblDataInicial = new JLabel("Data Inicial:");

		JLabel lblDataFinal = new JLabel("Data final:");

		dcDataFinal = new JDateChooser();

		JLabel lblValorTotal = new JLabel("Valor Total:");

		tfValorTotal = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorTotal.setEditable(false);
		tfValorTotal.setEnabled(false);
		tfValorTotal.setFont(new Font("Dialog", Font.PLAIN, 16));
		tfValorTotal.setColumns(10);
		tfValorTotal.setDisabledTextColor(Color.BLACK);
		
		JButton btnImprimir = new JButton("Imprimir");
		btnImprimir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ChamaRelatorioEspelho chamaRelatorioEspelho = new ChamaRelatorioEspelho();
				
				Usuario u = (Usuario) cbUsuario.getSelectedItem();
				Double tt = Double.valueOf(tfValorTotal.getText().replace("R$ ", "").replace(".", "").replace(",", "."));
				
				try {
					chamaRelatorioEspelho.reportEspelhoCaixa("EspelhoCaixa.jasper", u, null, dcDataInicial.getDate(), dcDataFinal.getDate(), tt);
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
				
			}
		});
		btnImprimir.setIcon(new ImageIcon(JanelaCaixa.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		
		cbUsuario = new JComboBox();
		
		JLabel lblUsuario = new JLabel("Usuario:");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblValorTotal)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE, 196, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 227, Short.MAX_VALUE)
							.addComponent(btnImprimir)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(dcDataInicial, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(dcDataFinal, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lblDataInicial)
									.addGap(55)
									.addComponent(lblDataFinal)))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsuario)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(cbUsuario, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
									.addGap(81)
									.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(7)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
							.addComponent(cbUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnBuscar))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblDataFinal)
									.addComponent(lblUsuario))
								.addComponent(lblDataInicial))
							.addGap(11)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(dcDataInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(dcDataFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(5)))
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExcluir, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblValorTotal)
						.addComponent(tfValorTotal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnImprimir, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelCaixa = new TableModelCaixa();
		this.tableLista.setModel(tableModelCaixa);

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);

		tableLista.getColumnModel().getColumn(5).setCellRenderer(cellRendererCustomMoeda);
	}

	private void carregarTabela() {
		try {

			// trx.begin();
			Query consulta = manager.createQuery("from Caixa");
			List<Caixa> listaCaixas = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCaixas.size(); i++) {
				Caixa c = listaCaixas.get(i);
				tableModelCaixa.addCaixa(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de caixas: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelCaixa.removecaixa(0);
		}
	}

	@SuppressWarnings("deprecation")
	private void buscarNome() {
						
		Date dtInicial = dcDataInicial.getDate();
		Date dtFinal = dcDataFinal.getDate();
		
		dtInicial.setHours(0);
		dtInicial.setMinutes(0);
		dtInicial.setSeconds(0);
		
		dtFinal.setHours(23);
		dtFinal.setMinutes(59);
		dtFinal.setSeconds(59);
				
		String dataInical = formatDataHoraInternacional.format(dtInicial.getTime());
		String dataFinal = formatDataHoraInternacional.format(dtFinal.getTime());
		
		//Usuario user = (Usuario) cbFuncionario.getSelectedItem();
		
		//JOptionPane.showMessageDialog(null, user.getNome());

		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaCaixa.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();
				
				Usuario u = (Usuario) cbUsuario.getSelectedItem();

				try {
					Double totalSomado = 0.0;
					
					//Query consulta = manager.createQuery("FROM Caixa WHERE data BETWEEN '" + dataInical + "' AND '" + dataFinal + "' AND venda_cabecalho_id IN (SELECT id FROM VendaCabecalho WHERE usuario_id = '" + u.getId() + "') ORDER BY id DESC");// AND venda_cabecalho_id IN (SELECT id FROM VendaCabecalho WHERE usuario_id IN (SELECT Usuario WHERE id LIKE '" + user.getId() + "'))");
					
					Query consulta = manager.createQuery("FROM Caixa WHERE data BETWEEN '" + dataInical + "' AND '" + dataFinal + "' AND usuario_id = '" + u.getId() + "' ORDER BY id DESC");
					
					List<Caixa> listaCaixas = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaCaixas.size(); i++) {
						Caixa c = listaCaixas.get(i);
						tableModelCaixa.addCaixa(c);

						if (listaCaixas.get(i).getValor() != null) {
							totalSomado = totalSomado + listaCaixas.get(i).getValor();
						}
						
					}

					tfValorTotal.setText(totalSomado.toString());

				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de caixas: " + e);
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

	private void excluirCaixa(Caixa c) {

		Double valor = c.getValor();
		Long id = c.getId();
		
		try {
			
			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Caixa foi removida com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "Exclusão do caixa id nº " + id + " no valor de R$ " + valor, dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		tableLista.getColumnModel().getColumn(0).setWidth(50);
		tableLista.getColumnModel().getColumn(0).setMaxWidth(80);
		
		tableLista.getColumnModel().getColumn(1).setWidth(50);
		tableLista.getColumnModel().getColumn(1).setMaxWidth(80);
		
		tableLista.getColumnModel().getColumn(3).setWidth(150);
		tableLista.getColumnModel().getColumn(3).setMaxWidth(150);
		
		tableLista.getColumnModel().getColumn(5).setWidth(120);
		tableLista.getColumnModel().getColumn(5).setMaxWidth(150);

	}
	
	private void carregajcbUsuario() {
		
		cbUsuario.removeAllItems();

		try {

			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuarios = consulta.getResultList();
			

			for (int i = 0; i < listaUsuarios.size(); i++) {

				Usuario u = listaUsuarios.get(i);
				cbUsuario.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento do ComboBox do usuario! " + e);
			trx.commit();
		}
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}
}
