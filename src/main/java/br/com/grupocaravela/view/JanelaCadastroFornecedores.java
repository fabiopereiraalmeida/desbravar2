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
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.Fornecedor;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.repositorio.RepositorioCliente;
import br.com.grupocaravela.repositorio.RepositorioFornecedor;
import br.com.grupocaravela.tablemodel.TableModelFornecedor;

import java.awt.Toolkit;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class JanelaCadastroFornecedores extends JFrame {

	private static final long serialVersionUID = 1L;

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelFornecedor tableModelFornecedor;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfRazaoSocial;

	private Fornecedor fornecedor;
	private JTextField tfFantasia;
	private JTextField tfCnpj;
	private JTextField tfCpf;
	private JTextField tfEmail;

	private JTextPane tpObservacao;
	private JComboBox cbFiltro;

	// private RepositorioFornecedor repositorioFornecedor = new
	// RepositorioFornecedor();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroFornecedores frame = new JanelaCadastroFornecedores();
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
	public JanelaCadastroFornecedores() {

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

		setTitle("Fornecedors");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 760, 428);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 789, Short.MAX_VALUE));
		gl_contentPane.setVerticalGroup(
				gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(gl_contentPane.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 385, Short.MAX_VALUE).addContainerGap()));

		JPanel panel = new JPanel();
		tabbedPane.addTab("Lista", null, panel, null);

		JLabel lblLocalizar = new JLabel("Localizar:");
		lblLocalizar.setIcon(
				new ImageIcon(JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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
					carregarCampos(tableModelFornecedor.getFornecedor(tableLista.getSelectedRow()));
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
				fornecedor = new Fornecedor();
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);
				tfRazaoSocial.requestFocus();

			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				fornecedor = tableModelFornecedor.getFornecedor(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão do fornecedor " + fornecedor.getRazaoSocial() + "?", "Exclusão",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirFornecedor(fornecedor);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				buscarNome();

			}
		});

		JButton btnDetalhes = new JButton("Detalhes");
		btnDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();

				fornecedor = tableModelFornecedor.getFornecedor(tableLista.getSelectedRow());

				carregarCampos(fornecedor);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				tabbedPane.setEnabledAt(0, false);

			}
		});
		btnDetalhes.setIcon(
				new ImageIcon(JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));

		cbFiltro = new JComboBox();
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Razão Social", "Fantasia" }));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(
						Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addGap(12).addComponent(lblLocalizar)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addGap(12))
				.addGroup(gl_panel.createSequentialGroup().addGap(12)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 719, Short.MAX_VALUE).addGap(12))
				.addGroup(gl_panel.createSequentialGroup().addContainerGap(347, Short.MAX_VALUE)
						.addComponent(btnExcluir, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addGap(12).addComponent(btnDetalhes).addGap(12)
						.addComponent(btnNovo, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup().addGap(12)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING).addComponent(lblLocalizar)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnBuscar)
								.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)))
				.addGap(11).addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE).addGap(12)
				.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false).addComponent(btnExcluir)
						.addComponent(btnDetalhes).addComponent(btnNovo))
				.addContainerGap()));
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Detalhes", null, panel_1, null);

		JLabel lblId = new JLabel("id");

		tfId = new JTextField();
		tfId.setEditable(false);
		tfId.setColumns(10);

		JLabel lblNome = new JLabel("Razão Social");

		tfRazaoSocial = new JTextField();
		tfRazaoSocial.setColumns(10);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (verificaCamposVazios() == false) {
					salvarFornecedor(fornecedor);
					tabbedPane.setSelectedIndex(0);
					tabbedPane.setEnabledAt(1, false);
					tabbedPane.setEnabledAt(0, true);
					tfLocalizar.requestFocus();
				}
			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

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
				JanelaCadastroFornecedores.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JLabel lblFantasia = new JLabel("Fantasia");

		tfFantasia = new JTextField();
		tfFantasia.setColumns(10);

		JLabel lblCnpj = new JLabel("CNPJ");

		tfCnpj = new JTextField();
		tfCnpj.setColumns(10);

		JLabel lblCpf = new JLabel("CPF");

		tfCpf = new JTextField();
		tfCpf.setColumns(10);

		JLabel lblEmail = new JLabel("Email");

		tfEmail = new JTextField();
		tfEmail.setColumns(10);

		JLabel label_4 = new JLabel("Obs.");

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));

		tpObservacao = new JTextPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGap(0, 520, Short.MAX_VALUE)
				.addComponent(tpObservacao, GroupLayout.DEFAULT_SIZE, 518, Short.MAX_VALUE));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING).addGap(0, 195, Short.MAX_VALUE)
				.addComponent(tpObservacao, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE));
		panel_2.setLayout(gl_panel_2);

		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnCancelar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSalvar))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblId)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNome)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, 462, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(lblEmail, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfEmail))
							.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
								.addComponent(lblFantasia)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, 268, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblCnpj, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfCnpj, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblCpf, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(tfCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblId)
						.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNome)
						.addComponent(tfRazaoSocial, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFantasia)
						.addComponent(tfFantasia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCnpj)
						.addComponent(tfCnpj, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblCpf)
						.addComponent(tfCpf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(tfEmail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEmail))
					.addGap(14)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(label_4)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSalvar)
						.addComponent(btnCancelar))
					.addContainerGap())
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelFornecedor = new TableModelFornecedor();
		this.tableLista.setModel(tableModelFornecedor);

	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Fornecedor");
			List<Fornecedor> listaFornecedors = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaFornecedors.size(); i++) {
				Fornecedor c = listaFornecedors.get(i);
				tableModelFornecedor.addFornecedor(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de fornecedors: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelFornecedor.removefornecedor(0);
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
					Logger.getLogger(JanelaCadastroFornecedores.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				if (tfLocalizar.getText().isEmpty()) {
					carregarTabela();
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

	private void salvarFornecedor(Fornecedor f) {
		try {

			f.setRazaoSocial(tfRazaoSocial.getText());
			f.setCpf(tfCpf.getText());
			f.setCnpj(tfCnpj.getText());
			f.setFantasia(tfFantasia.getText());
			f.setEmail(tfEmail.getText());
			f.setObservacao(tpObservacao.getText());

			trx.begin();
			manager.persist(f);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Fornecedor foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(Fornecedor f) {

		tfId.setText(f.getId().toString());
		tfRazaoSocial.setText(f.getRazaoSocial());
		tfCpf.setText(f.getCpf());
		tfCnpj.setText(f.getCnpj());
		tfFantasia.setText(f.getFantasia());
		tfEmail.setText(f.getEmail());
		tpObservacao.setText(f.getObservacao());

	}

	private void limparCampos() {
		tfId.setText("");
		tfRazaoSocial.setText("");
		tfCpf.setText("");
		tfCnpj.setText("");
		tfFantasia.setText("");
		tfEmail.setText("");
		tpObservacao.setText("");
	}

	private void excluirFornecedor(Fornecedor f) {
		try {

			trx.begin();
			manager.remove(f);
			trx.commit();

			JOptionPane.showMessageDialog(null, "Fornecedor foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private boolean verificaCamposVazios() {

		boolean retorno = false;

		if (tfRazaoSocial.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe um nome!");
			tfRazaoSocial.requestFocus();
			retorno = true;
		} else if (tfFantasia.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o numero de parcelas!");
			tfFantasia.requestFocus();
			retorno = true;
		} else if (tfCnpj.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o numero de dias!");
			tfCnpj.requestFocus();
			retorno = true;
		} else if (tfCpf.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe os juros!");
			tfCpf.requestFocus();
			retorno = true;
		} else if (tfEmail.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o valor minimo!");
			tfEmail.requestFocus();
			retorno = true;
		}

		return retorno;
	}

	private void filtrar(int op) {
		switch (op) {
		case 0:
			try {

				//trx.begin();
				Query consulta = manager.createQuery("from Fornecedor where razaoSocial like '%" + tfLocalizar.getText() + "%'");
				List<Fornecedor> listaFornecedors = consulta.getResultList();
				//trx.commit();
				for (int i = 0; i < listaFornecedors.size(); i++) {
					Fornecedor c = listaFornecedors.get(i);
					tableModelFornecedor.addFornecedor(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}

		case 1:
			try {

				//trx.begin();
				Query consulta = manager.createQuery("from Fornecedor where fantasia like '%" + tfLocalizar.getText() + "%'");
				List<Fornecedor> listaFornecedors = consulta.getResultList();
				//trx.commit();
				
				for (int i = 0; i < listaFornecedors.size(); i++) {
					Fornecedor c = listaFornecedors.get(i);
					tableModelFornecedor.addFornecedor(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}

		default:

		}
	}
}
