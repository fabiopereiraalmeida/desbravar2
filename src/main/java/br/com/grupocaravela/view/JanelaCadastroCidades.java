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
import br.com.grupocaravela.objeto.Cidade;
import br.com.grupocaravela.repositorio.RepositorioCidade;
import br.com.grupocaravela.tablemodel.TableModelCidade;

import java.awt.Toolkit;

public class JanelaCadastroCidades extends JFrame {

	// private EntityManagerFactory factory;
	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelCidade tableModelCidade;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfNome;

	private Cidade cidade;
	private JTextField tfUf;

	// private RepositorioCidade repositorioCidade = new RepositorioCidade();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroCidades frame = new JanelaCadastroCidades();
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
	public JanelaCadastroCidades() {

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

		setTitle("Cidades");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 613, 428);
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
				new ImageIcon(JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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
		scrollPane.setBounds(12, 48, 565, 252);
		panel.add(scrollPane);

		tableLista = new JTable();
		tableLista.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(tableLista);
		tableLista.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {

					cidade = tableModelCidade.getCidade(tableLista.getSelectedRow());

					carregarCampos(cidade);
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
				cidade = new Cidade();
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				tfNome.requestFocus();

			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		btnNovo.setBounds(460, 312, 117, 34);
		panel.add(btnNovo);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cidade = tableModelCidade.getCidade(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão da cidade " + cidade.getNome() + "?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirCidade(cidade);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
		btnExcluir.setBounds(193, 312, 117, 34);
		panel.add(btnExcluir);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});
		btnBuscar.setBounds(460, 12, 117, 25);
		panel.add(btnBuscar);

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				cidade = tableModelCidade.getCidade(tableLista.getSelectedRow());

				carregarCampos(cidade);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
		btnDetalhes.setBounds(322, 312, 126, 34);
		panel.add(btnDetalhes);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("nome");

		tfNome = new JTextField();

		tfNome.setColumns(10);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				salvarCidade(cidade);
				tabbedPane.setSelectedIndex(0);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);
				tfLocalizar.requestFocus();

			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

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
				JanelaCadastroCidades.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JLabel lblUf = new JLabel("Uf");

		tfUf = new JTextField();
		tfUf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				tfUf.setText(tfUf.getText().toUpperCase());
			}
		});
		tfUf.setColumns(10);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING,
										gl_panel_1.createSequentialGroup().addComponent(btnCancelar)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSalvar))
						.addGroup(gl_panel_1.createSequentialGroup().addGroup(gl_panel_1
								.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(Alignment.LEADING,
										gl_panel_1.createSequentialGroup().addComponent(lblUf)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(tfUf, 0, 0, Short.MAX_VALUE))
								.addGroup(Alignment.LEADING,
										gl_panel_1.createSequentialGroup().addComponent(lblId)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(tfId,
														GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNome)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfNome, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)))
				.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblId)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNome).addComponent(tfNome, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblUf).addComponent(tfUf,
						GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 263, Short.MAX_VALUE).addGroup(gl_panel_1
						.createParallelGroup(Alignment.BASELINE).addComponent(btnSalvar).addComponent(btnCancelar))
				.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelCidade = new TableModelCidade();
		this.tableLista.setModel(tableModelCidade);
	}

	private void carregarTabela() {
		try {

			// trx.begin();
			Query consulta = manager.createQuery("from Cidade");
			List<Cidade> listaCidades = consulta.getResultList();
			// trx.commit();

			for (int i = 0; i < listaCidades.size(); i++) {
				Cidade c = listaCidades.get(i);
				tableModelCidade.addCidade(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cidades: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelCidade.removecidade(0);
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
					Logger.getLogger(JanelaCadastroCidades.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					// trx.begin();
					Query consulta = manager
							.createQuery("from Cidade where nome like '%" + tfLocalizar.getText() + "%'");
					List<Cidade> listaCidades = consulta.getResultList();
					// trx.commit();

					for (int i = 0; i < listaCidades.size(); i++) {
						Cidade c = listaCidades.get(i);
						tableModelCidade.addCidade(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de cidades: " + e);
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

	private void salvarCidade(Cidade c) {
		try {

			c.setNome(tfNome.getText());
			c.setUf(tfUf.getText());

			trx.begin();
			manager.persist(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cidade foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(Cidade c) {

		tfId.setText(c.getId().toString());
		tfNome.setText(c.getNome());
		tfUf.setText(c.getUf());

	}

	private void limparCampos() {
		tfId.setText("");
		tfNome.setText("");
		tfUf.setText("");
	}

	private void excluirCidade(Cidade c) {
		try {

			trx.begin();
			manager.remove(c);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Cidade foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}
}
