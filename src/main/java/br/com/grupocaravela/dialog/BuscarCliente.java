package br.com.grupocaravela.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.hibernate.service.spi.InjectService;

import br.com.grupocaravela.aguarde.EsperaLista;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Cidade;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.render.MoedaRender;
import br.com.grupocaravela.repositorio.RepositorioCliente;
import br.com.grupocaravela.tablemodel.TableModelCliente;
import br.com.grupocaravela.view.JanelaCadastroCidades;
import br.com.grupocaravela.view.JanelaCadastroClientes;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComboBox;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.DefaultComboBoxModel;
import java.awt.Toolkit;

public class BuscarCliente extends JDialog {

	private static final long serialVersionUID = 1L;

	// private EntityManagerFactory factory;
	private EntityManager manager;
	private EntityTransaction trx;

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField tfLocalizar;

	private TableModelCliente tableModelCliente;
	private Cliente cliente;
	private Long idCliente;

	private JComboBox cbFiltro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BuscarCliente dialog = new BuscarCliente();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create the dialog.
	 */
	public BuscarCliente() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(BuscarCliente.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setTitle("Busca de clientes");
		setAlwaysOnTop(true);

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
		setBounds(100, 100, 900, 440);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING).addComponent(panel,
				GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE));

		JScrollPane scrollPane = new JScrollPane();

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {

					importar();
				}
			}
		});
		scrollPane.setViewportView(table);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.DARK_GRAY));

		JButton button_1 = new JButton("Cancelar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// factory.close();
				dispose();
			}
		});
		button_1.setIcon(
				new ImageIcon(BuscarCliente.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		JButton button_2 = new JButton("Importar");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				importar();
			}
		});
		button_2.setIcon(new ImageIcon(BuscarCliente.class.getResource("/br/com/grupocaravela/icones/aprovar_24.png")));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1
				.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGap(0, 664, Short.MAX_VALUE)
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap(397, Short.MAX_VALUE)
								.addComponent(button_1).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(button_2).addContainerGap()));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGap(0, 57, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createSequentialGroup()
						.addContainerGap().addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_2).addComponent(button_1))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.DARK_GRAY));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664,
										Short.MAX_VALUE)
						.addComponent(panel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)).addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_panel.createSequentialGroup()
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addGap(6)));

		JButton button = new JButton("Buscar");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filtrar();
			}
		});

		tfLocalizar = new JTextField();
		tfLocalizar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					filtrar();
				}
			}
		});
		tfLocalizar.setColumns(10);

		JLabel label = new JLabel("Localizar");
		label.setIcon(new ImageIcon(BuscarCliente.class.getResource("/br/com/grupocaravela/icones/lupa_24.png")));
		label.setFont(new Font("Dialog", Font.BOLD, 12));

		cbFiltro = new JComboBox();
		cbFiltro.setModel(new DefaultComboBoxModel(new String[] { "Razão Social", "Fantasia", "Apelido" }));
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2
				.setHorizontalGroup(
						gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup().addContainerGap().addComponent(label)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, 204,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfLocalizar, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(button)
										.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbFiltro, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(tfLocalizar, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
						.addComponent(button)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);
		contentPanel.setLayout(gl_contentPanel);
	}

	private void carregarTableModel() {
		this.tableModelCliente = new TableModelCliente();
		this.table.setModel(tableModelCliente);

	}

	private void filtrar() {

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
				espera.setAlwaysOnTop(true);
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

	private void importar() {
		cliente = tableModelCliente.getCliente(table.getSelectedRow());
		idCliente = cliente.getId();
		dispose();
	}

	private void limparTabela() {
		while (table.getModel().getRowCount() > 0) {
			tableModelCliente.removecliente(0);
		}
	}

	private void buscarTodos() {
		try {

			trx.begin();
			Query consulta = manager.createQuery("from Cliente ORDER BY razao_social ASC");
			List<Cliente> listaClientes = consulta.getResultList();
			trx.commit();

			for (int i = 0; i < listaClientes.size(); i++) {
				Cliente c = listaClientes.get(i);
				tableModelCliente.addCliente(c);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
		}
	}

	private void filtrar(int op) {

		switch (op) {
		case 0:
			try {

				trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where razaoSocial like '%" + tfLocalizar.getText() + "%' ORDER BY razao_social ASC");
				List<Cliente> listaClientes = consulta.getResultList();
				trx.commit();

				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		case 1:
			try {

				trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where fantasia like '%" + tfLocalizar.getText() + "%' ORDER BY razao_social ASC");
				List<Cliente> listaClientes = consulta.getResultList();
				trx.commit();

				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		case 2:
			try {

				trx.begin();
				Query consulta = manager
						.createQuery("from Cliente where apelido like '%" + tfLocalizar.getText() + "%' ORDER BY razao_social ASC");
				List<Cliente> listaClientes = consulta.getResultList();
				trx.commit();

				for (int i = 0; i < listaClientes.size(); i++) {
					Cliente c = listaClientes.get(i);
					tableModelCliente.addCliente(c);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de clientes: " + e);
			}
			break;

		default:

		}
	}

	public Cliente getCliente() {
		return cliente;
	}

	public Long getIdCliente() {
		return idCliente;
	}
}
