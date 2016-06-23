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

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.tablemodel.TableModelCargo;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;

import java.awt.Toolkit;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;

public class JanelaCadastroCargo extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCargo tableModelCargo;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfNome;

	private Cargo cargo;
	private JTextPane tpObservacao;
	private JCheckBox chckbxConfigurao;
	private JCheckBox chckbxVendas;
	private JCheckBox chckbxProdutos;
	private JCheckBox chckbxClientes;
	private JCheckBox chckbxCompras;
	private JCheckBox chckbxRelatrios;
	private JCheckBox chckbxContasReceber;
	private JCheckBox chckbxContasPagar;
	private JCheckBox chckbxAndroid;
	private JCheckBox chckbxRotas;
	private JCheckBox chckbxCidade;
	private JCheckBox chckbxCategorias;
	private JCheckBox chckbxUnidades;
	private JCheckBox chckbxFormaPagamento;
	private JCheckBox chckbxFornecedores;
	private JCheckBox chckbxUsuarios;

	// private RepositorioCargo repositorioCargo = new RepositorioCargo();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroCargo frame = new JanelaCadastroCargo();
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
	public JanelaCadastroCargo() {

		criarJanela();
		carregarTableModel();
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

		setTitle("Cargo");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 613, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 594, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 385, Short.MAX_VALUE).addContainerGap()));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar.setBounds(12, 12, 97, 24);
		lblLocalizar.setIcon(
				new ImageIcon(JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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
		tfLocalizar.setBounds(121, 15, 327, 19);
		tfLocalizar.setColumns(10);
		panel.setLayout(null);
		panel.add(lblLocalizar);
		panel.add(tfLocalizar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 48, 565, 345);
		panel.add(scrollPane);

		tableLista = new JTable();
		tableLista.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableLista);
		tableLista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					carregarCampos(tableModelCargo.getCargo(tableLista.getSelectedRow()));
					tabbedPane.setSelectedIndex(1);
					tabbedPane.setEnabledAt(1, true);
					tabbedPane.setEnabledAt(0, false);
				}
			}
		});

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});
		btnBuscar.setBounds(460, 12, 117, 25);
		panel.add(btnBuscar);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new LineBorder(Color.DARK_GRAY));
		panel_5.setBounds(12, 405, 565, 56);
		panel.add(panel_5);
		
				JButton btnNovo = new JButton("Novo");
				btnNovo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						limparCampos();
						cargo = new Cargo();
						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(1, true);
						tabbedPane.setEnabledAt(0, false);
						tfNome.requestFocus();

					}
				});
				btnNovo.setIcon(
						new ImageIcon(JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		
				JButton btnDetalhes = new JButton("Detalhes");
				btnDetalhes.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						cargo = tableModelCargo.getCargo(tableLista.getSelectedRow());

						carregarCampos(cargo);
						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(1, true);
						tabbedPane.setEnabledAt(0, false);

					}
				});
				btnDetalhes.setIcon(
						new ImageIcon(JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		
				JButton btnExcluir = new JButton("Excluir");
				btnExcluir.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						cargo = tableModelCargo.getCargo(tableLista.getSelectedRow());

						Object[] options = { "Sim", "Não" };
						int i = JOptionPane.showOptionDialog(null,
								"ATENÇÃO!!! Confirma a Exclusão da cargo " + cargo.getNome() + "?", "Exclusão",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

						if (i == JOptionPane.YES_OPTION) {

							excluirCargo(cargo);
							limparTabela();
						}
					}
				});
				btnExcluir.setIcon(new ImageIcon(
						JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addContainerGap(169, Short.MAX_VALUE)
					.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(btnDetalhes)
					.addGap(12)
					.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_5.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
						.addComponent(btnExcluir)
						.addComponent(btnDetalhes)
						.addComponent(btnNovo))
					.addContainerGap())
		);
		panel_5.setLayout(gl_panel_5);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("nome");

		tfNome = new JTextField();
		
		tfNome.setColumns(10);

		JLabel lblUf = new JLabel("Observação");

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(Color.DARK_GRAY));

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(
				new TitledBorder(null, "Permiss\u00F5es", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1
				.setHorizontalGroup(
						gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
								gl_panel_1.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
												.addComponent(panel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 565,
														Short.MAX_VALUE)
										.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
										.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblId)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80,
														GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNome)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfNome, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
						.addGroup(Alignment.LEADING,
								gl_panel_1.createSequentialGroup().addComponent(lblUf)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblId)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNome).addComponent(tfNome, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(lblUf).addComponent(panel_2,
						GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE).addContainerGap()));

		chckbxConfigurao = new JCheckBox("Configuração");

		chckbxVendas = new JCheckBox("Vendas");

		chckbxProdutos = new JCheckBox("Produtos");

		chckbxClientes = new JCheckBox("Clientes");

		chckbxCompras = new JCheckBox("Compras");

		chckbxRelatrios = new JCheckBox("Relatórios");

		chckbxContasReceber = new JCheckBox("Contas Receber");

		chckbxContasPagar = new JCheckBox("Contas Pagar");

		chckbxAndroid = new JCheckBox("Android");

		chckbxRotas = new JCheckBox("Rotas");

		chckbxCidade = new JCheckBox("Cidade");

		chckbxCategorias = new JCheckBox("Categorias");

		chckbxUnidades = new JCheckBox("Unidades");

		chckbxFormaPagamento = new JCheckBox("Forma Pagamento");

		chckbxFornecedores = new JCheckBox("Fornecedores");
		
		chckbxUsuarios = new JCheckBox("Usuarios");
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxConfigurao)
						.addComponent(chckbxVendas)
						.addComponent(chckbxProdutos)
						.addComponent(chckbxClientes)
						.addComponent(chckbxCompras)
						.addComponent(chckbxAndroid)
						.addComponent(chckbxRotas))
					.addGap(18)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(chckbxContasPagar)
						.addComponent(chckbxUnidades)
						.addComponent(chckbxCategorias)
						.addComponent(chckbxCidade)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxFormaPagamento)
								.addComponent(chckbxRelatrios)
								.addComponent(chckbxContasReceber))
							.addGap(18)
							.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxFornecedores)
								.addComponent(chckbxUsuarios))))
					.addContainerGap(150, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxConfigurao)
						.addComponent(chckbxRelatrios)
						.addComponent(chckbxFornecedores))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxVendas)
						.addComponent(chckbxContasReceber)
						.addComponent(chckbxUsuarios))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxProdutos)
						.addComponent(chckbxContasPagar))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(chckbxClientes)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxCompras)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxAndroid)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxRotas))
						.addGroup(gl_panel_4.createSequentialGroup()
							.addComponent(chckbxCategorias)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxCidade)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxUnidades)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(chckbxFormaPagamento)))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		panel_4.setLayout(gl_panel_4);

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
				JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				salvarCargo(cargo);
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				tfLocalizar.requestFocus();

			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCadastroCargo.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_3.createSequentialGroup().addContainerGap(243, Short.MAX_VALUE).addComponent(btnCancelar)
						.addGap(6).addComponent(btnSalvar).addContainerGap()));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel_3.createSequentialGroup().addContainerGap(29, Short.MAX_VALUE).addGroup(gl_panel_3
						.createParallelGroup(Alignment.LEADING).addComponent(btnCancelar).addComponent(btnSalvar))
						.addContainerGap()));
		panel_3.setLayout(gl_panel_3);

		tpObservacao = new JTextPane();
		tpObservacao.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tpObservacao.setText(tpObservacao.getText().toUpperCase());
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addComponent(tpObservacao,
				GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE));
		panel_2.setLayout(gl_panel_2);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelCargo = new TableModelCargo();
		this.tableLista.setModel(tableModelCargo);
	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Cargo");
			List<Cargo> listaCargo = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaCargo.size(); i++) {
				Cargo c = listaCargo.get(i);
				tableModelCargo.addCargo(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cargo: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelCargo.removecargo(0);
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
					Logger.getLogger(JanelaCadastroCargo.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					//trx.begin();
					Query consulta = manager
							.createQuery("from Cargo where nome like '%" + tfLocalizar.getText() + "%'");
					List<Cargo> listaCargo = consulta.getResultList();
					//trx.commit();

					for (int i = 0; i < listaCargo.size(); i++) {
						Cargo c = listaCargo.get(i);
						tableModelCargo.addCargo(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cargo: " + e);
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

	private void salvarCargo(Cargo c) {
		try {

			c.setNome(tfNome.getText());
			c.setObservacao(tpObservacao.getText());
			
			c.setAcessoAndroid(chckbxAndroid.isSelected());
			c.setAcessoCategorias(chckbxCategorias.isSelected());
			c.setAcessoCidades(chckbxCidade.isSelected());
			c.setAcessoClientes(chckbxClientes.isSelected());
			c.setAcessoCompras(chckbxCompras.isSelected());
			c.setAcessoConfiguracao(chckbxConfigurao.isSelected());
			c.setAcessoContasPagar(chckbxContasPagar.isSelected());
			c.setAcessoContasReceber(chckbxContasReceber.isSelected());
			c.setAcessoFormaPagamento(chckbxFormaPagamento.isSelected());
			c.setAcessoFornecedores(chckbxFornecedores.isSelected());
			c.setAcessoProdutos(chckbxProdutos.isSelected());
			c.setAcessoRelatorios(chckbxRelatrios.isSelected());
			c.setAcessoRotas(chckbxRotas.isSelected());
			c.setAcessoUnidade(chckbxUnidades.isSelected());
			c.setAcessoUsuarios(chckbxUsuarios.isSelected());
			c.setAcessoVendas(chckbxVendas.isSelected());

			trx.begin();
			manager.persist(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cargo foi salva com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "O cargo " + c.getNome() + " com o id nº " + c.getId() + " foi salvo", dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(Cargo c) {

		tfId.setText(c.getId().toString());
		tfNome.setText(c.getNome());
		tpObservacao.setText(c.getObservacao());

		if (c.isAcessoAndroid() == true) {
			chckbxAndroid.setSelected(true);
		}
		if (c.isAcessoCategorias() == true) {
			chckbxCategorias.setSelected(true);
		}
		if (c.isAcessoCidades() == true) {
			chckbxCidade.setSelected(true);
		}
		if (c.isAcessoClientes() == true) {
			chckbxClientes.setSelected(true);
		}
		if (c.isAcessoCompras() == true) {
			chckbxCompras.setSelected(true);
		}
		if (c.isAcessoConfiguracao() == true) {
			chckbxConfigurao.setSelected(true);
		}
		if (c.isAcessoContasPagar() == true) {
			chckbxContasPagar.setSelected(true);
		}
		if (c.isAcessoContasReceber() == true) {
			chckbxContasReceber.setSelected(true);
		}
		if (c.isAcessoFormaPagamento() == true) {
			chckbxFormaPagamento.setSelected(true);
		}
		if (c.isAcessoFornecedores() == true) {
			chckbxFornecedores.setSelected(true);
		}
		if (c.isAcessoProdutos() == true) {
			chckbxProdutos.setSelected(true);
		}
		if (c.isAcessoRelatorios() == true) {
			chckbxRelatrios.setSelected(true);
		}
		if (c.isAcessoRotas() == true) {
			chckbxRotas.setSelected(true);
		}
		if (c.isAcessoUnidade() == true) {
			chckbxUnidades.setSelected(true);
		}
		if (c.isAcessoUsuarios() == true) {
			chckbxUsuarios.setSelected(true);
		}
		if (c.isAcessoVendas() == true) {
			chckbxVendas.setSelected(true);
		}
		
	}

	private void limparCampos() {
		tfId.setText("");
		tfNome.setText("");
		tpObservacao.setText("");
		chckbxAndroid.setSelected(false);
		chckbxCategorias.setSelected(false);
		chckbxCidade.setSelected(false);
		chckbxClientes.setSelected(false);
		chckbxCompras.setSelected(false);
		chckbxConfigurao.setSelected(false);
		chckbxContasPagar.setSelected(false);
		chckbxContasReceber.setSelected(false);
		chckbxFormaPagamento.setSelected(false);
		chckbxFornecedores.setSelected(false);
		chckbxProdutos.setSelected(false);
		chckbxRelatrios.setSelected(false);
		chckbxRotas.setSelected(false);
		chckbxUnidades.setSelected(false);
		chckbxUsuarios.setSelected(false);
		chckbxVendas.setSelected(false);
	}

	private void excluirCargo(Cargo c) {
		
		String n = c.getNome();
		Long id = c.getId();
		
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cargo foi removida com sucesso!");
			
			CriarHistorico.criar(UsuarioLogado.getUsuario(), "O cargo " + n + " com o id nº " + id + " foi excluido", dataAtual());
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
	
	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}
	
}
