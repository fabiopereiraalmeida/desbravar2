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
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.toedter.calendar.JDateChooser;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.ContaPagar;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.tablemodel.TableModelContaPagar;
import java.awt.Font;
import javax.swing.JCheckBox;

public class JanelaContasPagar extends JFrame {

	// private EntityManagerFactory factory;
	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelContaPagar tableModelContaPagar;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfDescricao;

	private ContaPagar contaPagar;
	private JTextField tfValor;
	private JDateChooser dcCriacao;
	private JDateChooser dcVencimento;
	private JDateChooser dcPagamento;
	private JTextPane tpObservacao;
	private JLabel lblEstado;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private JButton btnPagarConta;

	// private RepositorioContaPagar repositorioContaPagar = new RepositorioContaPagar();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaContasPagar frame = new JanelaContasPagar();
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
	public JanelaContasPagar() {

		criarJanela();
		carregarTableModel();
		tamanhoColunas();
		iniciaConexao();

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

		setTitle("Contas a pagar");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 854, 569);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar.setIcon(
				new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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

					contaPagar = tableModelContaPagar.getContaPagar(tableLista.getSelectedRow());

					carregarCampos(contaPagar);
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(0, false);

				}
			}
		});

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();
				contaPagar = new ContaPagar();
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				tfDescricao.requestFocus();

			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setEnabled(false);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				contaPagar = tableModelContaPagar.getContaPagar(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão da contaPagar " + contaPagar.getDescricao() + "?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirContaPagar(contaPagar);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setIcon(new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				contaPagar = tableModelContaPagar.getContaPagar(tableLista.getSelectedRow());

				carregarCampos(contaPagar);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
		JCheckBox chckbxTodas = new JCheckBox("Todas");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblLocalizar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxTodas)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 509, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(btnDetalhes)
							.addGap(12)
							.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBuscar)
						.addComponent(lblLocalizar)
						.addComponent(chckbxTodas))
					.addGap(12)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExcluir)
						.addComponent(btnDetalhes)
						.addComponent(btnNovo))
					.addContainerGap())
		);
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id:");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("Descrição:");

		tfDescricao = new JTextField();

		tfDescricao.setColumns(10);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				salvarContaPagar(contaPagar);
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				tfLocalizar.requestFocus();

			}
		});
		btnSalvar.setIcon(new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				limparCampos();
				tfLocalizar.requestFocus();

			}
		});
		btnCancelar.setIcon(new ImageIcon(
				JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JLabel lblUf = new JLabel("Data criação:");
		
		dcCriacao = new JDateChooser();
		
		JLabel lblVencimento = new JLabel("Vencimento:");
		
		dcVencimento = new JDateChooser();
		
		JLabel lblDataPagamento = new JLabel("Data Pagamento:");
		
		dcPagamento = new JDateChooser();
		
		JLabel lblValor = new JLabel("Valor:");
		
		tfValor = new DecimalFormattedField(DecimalFormattedField.REAL);
		
		tfValor.setColumns(10);
		
		JLabel lblObservao = new JLabel("Observação:");
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		tpObservacao = new JTextPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(tpObservacao, GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(tpObservacao, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
		);
		panel_2.setLayout(gl_panel_2);
		
		JLabel lblEstadoDaConta = new JLabel("Estado da conta:");
		
		lblEstado = new JLabel("Estado");
		lblEstado.setFont(new Font("Dialog", Font.BOLD, 36));
		
		btnPagarConta = new JButton("Pagar conta");
		btnPagarConta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (dcPagamento.getDate() == null) {
					dcPagamento.setDate(dataAtual());
				}
								
				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma o pagamento da conta no valor de " + tfValor.getText() + " com o vencimento para " + formatData.format(contaPagar.getDataVencimento()) + "?", "Pagar Conta",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					try {
						trx.begin();
						contaPagar.setPago(true);
						contaPagar.setDataPagamento(dcPagamento.getDate());
						manager.persist(contaPagar);
						trx.commit();
						
						lblEstado.setText("Pago!");
						lblEstado.setForeground(Color.GREEN);
						
						btnPagarConta.setEnabled(false);
						
						JOptionPane.showMessageDialog(null, "Conta paga com sucesso!!!");
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, "Erro ao pagar conta: " + e2);
					}
				}				
			}
		});
		btnPagarConta.setIcon(new ImageIcon(JanelaContasPagar.class.getResource("/br/com/grupocaravela/icones/caixa_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(12)
							.addComponent(lblId)
							.addGap(12)
							.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(lblNome)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfDescricao, GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblObservao))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblUf)
									.addGap(12)
									.addComponent(dcCriacao, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblVencimento)
									.addGap(12)
									.addComponent(dcVencimento, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(lblDataPagamento)
									.addGap(12)
									.addComponent(dcPagamento, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblValor)
									.addGap(12)
									.addComponent(tfValor, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEstado)
								.addComponent(lblEstadoDaConta)))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnPagarConta)
							.addPreferredGap(ComponentPlacement.RELATED, 429, Short.MAX_VALUE)
							.addComponent(btnCancelar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSalvar)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(2)
							.addComponent(lblId))
						.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(2)
							.addComponent(lblNome))
						.addComponent(tfDescricao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(4)
									.addComponent(lblUf))
								.addComponent(dcCriacao, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(4)
									.addComponent(lblVencimento))
								.addComponent(dcVencimento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(4)
									.addComponent(lblDataPagamento))
								.addComponent(dcPagamento, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGap(2)
									.addComponent(lblValor))
								.addComponent(tfValor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblObservao))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblEstadoDaConta)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblEstado)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
					.addGap(187)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnPagarConta)
						.addComponent(btnCancelar)
						.addComponent(btnSalvar))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelContaPagar = new TableModelContaPagar();
		this.tableLista.setModel(tableModelContaPagar);
		
		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		tableLista.getColumnModel().getColumn(2).setCellRenderer(cellRendererCustomMoeda);
	}

	private void carregarTabela() {
		try {

			// trx.begin();
			Query consulta = manager.createQuery("from ContaPagar");
			List<ContaPagar> listaContaPagars = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaContaPagars.size(); i++) {
				ContaPagar c = listaContaPagars.get(i);
				tableModelContaPagar.addContaPagar(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de contaPagars: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelContaPagar.removecontaPagar(0);
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
					Logger.getLogger(JanelaContasPagar.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("from ContaPagar where descricao like '%" + tfLocalizar.getText() + "%'");
					List<ContaPagar> listaContaPagars = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaContaPagars.size(); i++) {
						ContaPagar c = listaContaPagars.get(i);
						tableModelContaPagar.addContaPagar(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de contaPagars: " + e);
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
		// ###############################################
	}

	private void salvarContaPagar(ContaPagar c) {
		try {
			c.setDescricao(tfDescricao.getText());
			c.setDataCriacao(dcCriacao.getDate());
			c.setDataPagamento(dcPagamento.getDate());
			c.setDataVencimento(dcVencimento.getDate());
			c.setValor(Double.parseDouble(tfValor.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			c.setObservacao(tpObservacao.getText());

			trx.begin();
			manager.persist(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "ContaPagar foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private void carregarCampos(ContaPagar c) {

		tfId.setText(c.getId().toString());
		tfDescricao.setText(c.getDescricao());
		tfValor.setText(c.getValor().toString());
		dcCriacao.setDate(c.getDataCriacao());
		dcPagamento.setDate(c.getDataPagamento());
		dcVencimento.setDate(c.getDataVencimento());
		tpObservacao.setText(c.getObservacao());
		
		if (c.getPago()) {
			lblEstado.setText("Pago!");
			lblEstado.setForeground(Color.GREEN);
			
			btnPagarConta.setEnabled(false);
		}else{
			lblEstado.setText("Não pago!");
			lblEstado.setForeground(Color.RED);
			
			btnPagarConta.setEnabled(true);
		}
	}

	private void limparCampos() {
		tfId.setText("");
		tfDescricao.setText("");
		tfValor.setText("0");
		dcCriacao.setDate(null);
		dcVencimento.setDate(null);
		dcVencimento.setDate(null);
		tpObservacao.setText("");
	}

	private void excluirContaPagar(ContaPagar c) {
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "ContaPagar foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private void tamanhoColunas() {
		// tableProdutos.setAutoResizeMode(tableProdutos.AUTO_RESIZE_OFF);
		
		tableLista.getColumnModel().getColumn(0).setWidth(50);
		tableLista.getColumnModel().getColumn(0).setMinWidth(30);
		tableLista.getColumnModel().getColumn(0).setMaxWidth(80);
		tableLista.getColumnModel().getColumn(1).setWidth(350);
		tableLista.getColumnModel().getColumn(1).setMinWidth(200);
			
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;
	}
}
