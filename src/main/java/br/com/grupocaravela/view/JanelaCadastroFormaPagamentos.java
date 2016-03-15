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
import br.com.grupocaravela.mask.DecimalFormattedField;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.repositorio.RepositorioFormaPagamento;
import br.com.grupocaravela.tablemodel.TableModelFormaPagamento;

import java.awt.Toolkit;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.Color;
import javax.swing.JTextPane;

public class JanelaCadastroFormaPagamentos extends JFrame {

	//private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private TableModelFormaPagamento tableModelFormaPagamento;

	private JPanel contentPane;
	private JTextField tfLocalizar;
	private JTable tableLista;
	private JTextField tfId;
	private JTextField tfNome;

	private FormaPagamento formaPagamento;
	private JTextField tfNumParcelas;
	private JTextField tfNumDias;
	private JTextField tfJuros;
	private JTextField tfValorMinimo;

	private JTextPane tpObservacao;

	//private RepositorioFormaPagamento repositorioFormaPagamento = new RepositorioFormaPagamento();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {

				try {
					JanelaCadastroFormaPagamentos frame = new JanelaCadastroFormaPagamentos();
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
	public JanelaCadastroFormaPagamentos() {

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

		setTitle("FormaPagamentos");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
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
				new ImageIcon(JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));

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
					
					limparCampos();

					formaPagamento = tableModelFormaPagamento.getFormaPagamento(tableLista.getSelectedRow());

					carregarCampos(formaPagamento);
					tabbedPane.setSelectedIndex(1);
				}
			}
		});

		JButton btnNovo = new JButton("Novo");
		btnNovo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limparCampos();
				formaPagamento = new FormaPagamento();
				tabbedPane.setSelectedIndex(1);
				tfNome.requestFocus();

			}
		});
		btnNovo.setIcon(
				new ImageIcon(JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/novo_24.png")));
		btnNovo.setBounds(460, 312, 117, 34);
		panel.add(btnNovo);

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				formaPagamento = tableModelFormaPagamento.getFormaPagamento(tableLista.getSelectedRow());

				Object[] options = { "Sim", "Não" };
				int i = JOptionPane.showOptionDialog(null,
						"ATENÇÃO!!! Confirma a Exclusão da formaPagamento " + formaPagamento.getNome() + "?",
						"Exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

				if (i == JOptionPane.YES_OPTION) {

					excluirFormaPagamento(formaPagamento);
					limparTabela();
				}
			}
		});
		btnExcluir.setIcon(new ImageIcon(
				JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/alerta_vermelho_24.png")));
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

				limparCampos();

				formaPagamento = tableModelFormaPagamento.getFormaPagamento(tableLista.getSelectedRow());

				carregarCampos(formaPagamento);
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(0, true);

			}
		});
		btnDetalhes.setIcon(new ImageIcon(
				JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/alterar_24.png")));
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

				if (verificaCamposVazios() == false) {
					salvarFormaPagamento(formaPagamento);
					tabbedPane.setSelectedIndex(0);
					tfLocalizar.requestFocus();
				}
			}
		});
		btnSalvar.setIcon(new ImageIcon(
				JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				tabbedPane.setSelectedIndex(0);
				limparCampos();
				tfLocalizar.requestFocus();

			}
		});
		btnCancelar.setIcon(new ImageIcon(
				JanelaCadastroFormaPagamentos.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JLabel label = new JLabel("Nº Parcelas");

		tfNumParcelas = new JTextField();
		tfNumParcelas.setColumns(10);

		JLabel label_1 = new JLabel("Nº Dias");

		tfNumDias = new JTextField();
		tfNumDias.setColumns(10);

		JLabel label_2 = new JLabel("Juros");

		tfJuros = new DecimalFormattedField(DecimalFormattedField.PORCENTAGEM);
		tfJuros.setColumns(10);

		JLabel label_3 = new JLabel("Valor Minimo");

		tfValorMinimo = new DecimalFormattedField(DecimalFormattedField.REAL);
		tfValorMinimo.setColumns(10);

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
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap().addGroup(gl_panel_1
						.createParallelGroup(
								Alignment.LEADING)
						.addGroup(Alignment.TRAILING,
								gl_panel_1.createSequentialGroup().addComponent(lblId)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfId, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(lblNome)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfNome, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING,
								gl_panel_1.createSequentialGroup().addComponent(btnCancelar)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnSalvar))
						.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(12)
								.addComponent(tfNumParcelas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
								.addGap(12).addComponent(tfNumDias, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(
								gl_panel_1.createSequentialGroup()
										.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 36,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12)
										.addComponent(tfJuros, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 91,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12).addComponent(tfValorMinimo, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(
								gl_panel_1.createSequentialGroup()
										.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 33,
												GroupLayout.PREFERRED_SIZE)
										.addGap(12).addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 520,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblId)
								.addComponent(tfId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNome).addComponent(tfNome, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.UNRELATED).addPreferredGap(ComponentPlacement.UNRELATED)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label))
						.addComponent(tfNumParcelas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_1))
						.addComponent(tfNumDias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(12)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_2))
						.addComponent(tfJuros, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createSequentialGroup().addGap(2).addComponent(label_3))
						.addComponent(tfValorMinimo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(12)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(label_4).addComponent(panel_2,
						GroupLayout.PREFERRED_SIZE, 195, GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED, 25, Short.MAX_VALUE).addGroup(gl_panel_1
						.createParallelGroup(Alignment.BASELINE).addComponent(btnSalvar).addComponent(btnCancelar))
				.addContainerGap()));
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);

	}

	private void carregarTableModel() {
		this.tableModelFormaPagamento = new TableModelFormaPagamento();
		this.tableLista.setModel(tableModelFormaPagamento);

		// Render de Moeda
		NumberFormat numeroMoeda = NumberFormat.getNumberInstance();
		numeroMoeda.setMinimumFractionDigits(2);
		DefaultTableCellRenderer cellRendererCustomMoeda = new MoedaRender(numeroMoeda);
		tableLista.getColumnModel().getColumn(4).setCellRenderer(cellRendererCustomMoeda);
	}

	private void carregarTabela() {
		try {

			//trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento");
			List<FormaPagamento> listaFormaPagamentos = consulta.getResultList();
			//trx.commit();
			
			for (int i = 0; i < listaFormaPagamentos.size(); i++) {
				FormaPagamento c = listaFormaPagamentos.get(i);
				tableModelFormaPagamento.addFormaPagamento(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de formaPagamentos: " + e);
		}
	}

	private void limparTabela() {
		while (tableLista.getModel().getRowCount() > 0) {
			tableModelFormaPagamento.removeformaPagamento(0);
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
					Logger.getLogger(JanelaCadastroFormaPagamentos.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				limparTabela();

				try {

					//trx.begin();
					Query consulta = manager.createQuery("from FormaPagamento where nome like '%" + tfLocalizar.getText() + "%'");
					List<FormaPagamento> listaFormaPagamentos = consulta.getResultList();
					//trx.commit();
					
					for (int i = 0; i < listaFormaPagamentos.size(); i++) {
						FormaPagamento c = listaFormaPagamentos.get(i);
						tableModelFormaPagamento.addFormaPagamento(c);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de formaPagamentos: " + e);
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

	private void salvarFormaPagamento(FormaPagamento f) {
		try {

			f.setNome(tfNome.getText());
			f.setJuros(Double.parseDouble(tfJuros.getText().replace("%", "").replace(".", "").replace(",", ".")));
			f.setNumeroDias(Integer.parseInt(tfNumDias.getText()));
			f.setNumeroParcelas(Integer.parseInt(tfNumParcelas.getText()));
			f.setValorMinimo(Double.parseDouble(tfValorMinimo.getText().replace("R$ ", "").replace(".", "").replace(",", ".")));
			f.setObservacao(tpObservacao.getText());

			trx.begin();
			manager.persist(f);
			trx.commit();

			JOptionPane.showMessageDialog(null, "FormaPagamento foi salva com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}

	}

	private void carregarCampos(FormaPagamento f) {

		tfId.setText(f.getId().toString());
		tfNome.setText(f.getNome());
		tfJuros.setText(f.getJuros().toString());
		tfNumDias.setText(f.getNumeroDias().toString());
		tfNumParcelas.setText(f.getNumeroParcelas().toString());
		tfValorMinimo.setText(f.getValorMinimo().toString());
		tpObservacao.setText(f.getObservacao());

	}

	private void limparCampos() {
		tfId.setText("");
		tfNome.setText("");
		tfJuros.setText("");
		tfNumDias.setText("0");
		tfNumParcelas.setText("1");
		tfValorMinimo.setText("");
		tpObservacao.setText("");
	}

	private void excluirFormaPagamento(FormaPagamento f) {
		try {

			trx.begin();
			manager.remove(f);

			JOptionPane.showMessageDialog(null, "FormaPagamento foi removida com sucesso!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! " + e);
		}
	}

	private boolean verificaCamposVazios() {

		boolean retorno = false;

		if (tfNome.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe um nome!");
			tfNome.requestFocus();
			retorno = true;
		} else if (tfNumParcelas.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o numero de parcelas!");
			tfNumParcelas.requestFocus();
			retorno = true;
		} else if (tfNumDias.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o numero de dias!");
			tfNumDias.requestFocus();
			retorno = true;
		} else if (tfJuros.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe os juros!");
			tfJuros.requestFocus();
			retorno = true;
		} else if (tfValorMinimo.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Informe o valor minimo!");
			tfValorMinimo.requestFocus();
			retorno = true;
		}

		return retorno;
	}
}
