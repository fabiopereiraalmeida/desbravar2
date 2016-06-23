package br.com.grupocaravela.view;

import java.awt.EventQueue;
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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
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
import javax.swing.table.DefaultTableCellRenderer;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Cidade;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda;
import br.com.grupocaravela.relatorios.ChamaRelatorioComprovanteVenda2Via;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.repositorio.RepositorioCidade;
import br.com.grupocaravela.tablemodel.TableModelCidade;
import br.com.grupocaravela.tablemodel.TableModelHistoricoAcoes;
import br.com.grupocaravela.tablemodel.TableModelHistoricoVendas;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;

import java.awt.Toolkit;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

public class JanelaHistoricoAcoes extends JFrame {

	// private EntityManagerFactory factory;
	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;
	
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatSQL = new SimpleDateFormat("yyyy-MM-dd");

	private TableModelHistoricoAcoes tableModelHistoricoAcoes;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JDateChooser dcDataInicial;
	private JDateChooser dcDataFinal;
	private JComboBox comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaHistoricoAcoes frame = new JanelaHistoricoAcoes();
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
	public JanelaHistoricoAcoes() {

		criarJanela();
		carregarTableModel();
		tamanhoColunas();
		iniciaConexao();
		
		carregajcb();
		
		dcDataInicial.setDate(dataAtual());
		dcDataFinal.setDate(dataAtual());

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

	private void criarJanela() {

		setTitle("Histórico de ações no sistema");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaHistoricoAcoes.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 932, 428);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
				JButton btnBuscar = new JButton("Buscar");
				btnBuscar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						buscarNome();

					}
				});
		
				JLabel lblLocalizar = new JLabel("Localizar descrição:");
				lblLocalizar.setIcon(
						new ImageIcon(JanelaHistoricoAcoes.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		
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
		
				tableLista = new JTable();
				
				JScrollPane scrollPane = new JScrollPane();
				
				scrollPane.setViewportView(tableLista);
				
				tableLista.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {


						}
					}
				});
		
		JButton btnImprimirVia = new JButton("Imprimir");
		btnImprimirVia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnImprimirVia.setIcon(new ImageIcon(JanelaHistoricoAcoes.class.getResource("/br/com/grupocaravela/icones/impressora_24.png")));
		
		comboBox = new JComboBox();
		
		JLabel lblUsuario = new JLabel("Usuario:");
		
		JLabel lblDataInicial = new JLabel("Data Inicial:");
		
		dcDataInicial = new JDateChooser();
		
		JLabel lblDataFinal = new JLabel("Data final:");
		
		dcDataFinal = new JDateChooser();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblDataInicial)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcDataInicial, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblDataFinal)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(dcDataFinal, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(lblLocalizar)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblUsuario)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addGap(6))
						.addComponent(btnImprimirVia, Alignment.TRAILING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(15)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblUsuario)
										.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addContainerGap()
									.addComponent(lblLocalizar, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblDataInicial)
								.addComponent(dcDataInicial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblDataFinal)
								.addComponent(dcDataFinal, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnBuscar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnImprimirVia)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelHistoricoAcoes = new TableModelHistoricoAcoes();
		this.tableLista.setModel(tableModelHistoricoAcoes);		
		
	}

	private void carregarTabela() {
		try {

			Query consulta = manager.createQuery("from Historico");
			List<Historico> listaHistorico = consulta.getResultList();

			for (int i = 0; i < listaHistorico.size(); i++) {
				Historico h = listaHistorico.get(i);
				tableModelHistoricoAcoes.addHistorico(h);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de históricos: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelHistoricoAcoes.removehistorico(0);
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
					Logger.getLogger(JanelaHistoricoAcoes.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				Usuario usuario = (Usuario) comboBox.getSelectedItem();
				
				//String dataInicial = formatSQL.format(dcDataInicial.getDate() + " 00:00:00");
				//String dataFinal = formatSQL.format(dcDataFinal.getDate() + " 23:59:59");
				
				String dataInicial = formatSQL.format(dcDataInicial.getDate());
				String dataFinal = formatSQL.format(dcDataFinal.getDate());
				
				try {

					Query consulta = manager.createQuery("from Historico WHERE usuario_id LIKE '" + usuario.getId() + "' AND data BETWEEN '" + dataInicial + " 00:00:00' AND '" + dataFinal + " 23:59:59'");
					//Query consulta = manager.createQuery("from Historico WHERE usuario_id LIKE '" + usuario.getId() + "'");
					List<Historico> listaHistorico = consulta.getResultList();

					for (int i = 0; i < listaHistorico.size(); i++) {
						Historico h = listaHistorico.get(i);
						tableModelHistoricoAcoes.addHistorico(h);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela: " + e);
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
	
	private void imprimirComprovante(VendaCabecalho vc, Cliente c, FormaPagamento f, Usuario u){
		
		//#################### INICIO CAPTURANDO ENDEREÇO ###################
		EnderecoCliente enderecoCliente = null;
		try {
			enderecoCliente = c.getEnderecos().get(0);
		} catch (Exception e2) {
			enderecoCliente = null;
		}
		//#################### FIM CAPTURANDO ENDEREÇO ###################
		
		//################## INICIO CAPTURANDO FORMA DE PAGAMENTO ####################
		//FormaPagamento f = (FormaPagamento) cbFormaPagamento.getSelectedItem();
		//################## FIM CAPTURANDO FORMA DE PAGAMENTO ####################
		
		//###################### INICIO PEGANDO DATA VENCIMENRTO ###################
		
		GregorianCalendar gcGregorian = new GregorianCalendar();
		gcGregorian.setTime(vc.getDataVenda());

		gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (f.getNumeroDias()));

		
		String dtVencimento = format.format(gcGregorian.getTime());
		
		//###################### FIM PEGANDO DATA VENCIMENRTO ###################
						
		ChamaRelatorioComprovanteVenda chamaRelatorio = new ChamaRelatorioComprovanteVenda();
						
		try {							
			
			chamaRelatorio.report(u, c, enderecoCliente, f.getNome(), format.format(vc.getDataVenda()), dtVencimento, vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage() );
		}
		
		ChamaRelatorioComprovanteVenda2Via chamaRelatorio2 = new ChamaRelatorioComprovanteVenda2Via();
		
		try {							
			
			chamaRelatorio2.report(u, c, enderecoCliente, f.getNome(), format.format(vc.getDataVenda()), dtVencimento, vc.getId().toString(), vc.getValorParcial(), vc.getValorDesconto(), vc.getValorTotal());
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar relatório! " + e1.getMessage() );
		}
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		return hoje;

	}
	
	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		tableLista.getColumnModel().getColumn(0).setWidth(30);
		tableLista.getColumnModel().getColumn(0).setMaxWidth(50);

		tableLista.getColumnModel().getColumn(1).setMinWidth(100);
		tableLista.getColumnModel().getColumn(1).setMaxWidth(140);

		tableLista.getColumnModel().getColumn(2).setMinWidth(120);
		tableLista.getColumnModel().getColumn(2).setMaxWidth(150);
		
		tableLista.getColumnModel().getColumn(0).setWidth(200);
		tableLista.getColumnModel().getColumn(0).setMinWidth(150);
	}
	
	private void carregajcb() {

		comboBox.removeAllItems();

		try {

			Query consulta = manager.createQuery("from Usuario ORDER BY nome ASC");
			List<Usuario> listaUsuarios = consulta.getResultList();

			for (int i = 0; i < listaUsuarios.size(); i++) {

				Usuario u = listaUsuarios.get(i);
				comboBox.addItem(u);
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro no carregamento dos usuarios! " + e.getMessage());			
		}
	}
		
}
