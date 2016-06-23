package br.com.grupocaravela.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.SoftBevelBorder;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.configuracao.Update;
import br.com.grupocaravela.configuracao.Versao;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.objeto.Categoria;
import br.com.grupocaravela.objeto.ContaReceber;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.repositorio.RepositorioCategoria;
import br.com.grupocaravela.repositorio.RepositorioRota;
import br.com.grupocaravela.repositorio.RepositorioUsuario;
import br.com.grupocaravela.util.CriarHistorico;
import br.com.grupocaravela.util.UsuarioLogado;
import br.com.grupocaravela.view.JanelaCadastroClientes.ThreadBasica;

import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.border.CompoundBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JPasswordField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class JanelaLogin extends JFrame {

	private EntityManagerProducer entityManagerProducer = new EntityManagerProducer();
	private EntityManager manager;
	private EntityTransaction trx;

	private JPanel contentPane;
	private JTextField tfUsuario;

	private Usuario usuarioLogado;
	private JPasswordField tfSenha;
	private boolean usuarioExiste;

	private Update update = new Update();
	private Versao versao = new Versao();

	// private RepositorioUsuario repositorioUsuario;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaLogin frame = new JanelaLogin();
					// frame.setLocationRelativeTo(null);
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
	public JanelaLogin() {

		carregarJanela();
		
		iniciaConexao();
		verificaExistenciaUsuario();
		
		try {
			carregarObjetoEmpresa();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Arquivo de configuração nao encontrado!");
		}		
		
		Thread thread = new ThreadBasica(); // Cria a tread para carregar 
		// comobox;
		thread.start(); // Inicia Thread
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

	class ThreadBasica extends Thread {

		public ThreadBasica() {
			// TODO Auto-generated constructor stub

			update.atualizaOrientacao(); // baixa arquivo txt do servidor com a
											// versão atual

			if (update.lerArquivoVersao() > versao.getVersao()) {
				Object[] option = { "Atualizar", "Não Atualizar" };
				int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Ja existe uma nova versão para este sistema!",
						"Atualizar sistema", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option,
						option[0]);
				if (i == JOptionPane.YES_OPTION) {

					dispose();
					update.atualizarAguarde();					

				}

			}

		}
	}

	private void iniciaConexao() {
		//factory = Persistence.createEntityManagerFactory("DesbravarPU");
		manager = entityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}

	private void carregarJanela() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 360, 640);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setHorizontalAlignment(SwingConstants.CENTER);

		tfUsuario = new JTextField();
		tfUsuario.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					tfSenha.requestFocus();
				}
			}
		});
		tfUsuario.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfUsuario.selectAll();
			}
		});
		tfUsuario.setHorizontalAlignment(SwingConstants.CENTER);
		tfUsuario.setFont(new Font("Dialog", Font.PLAIN, 16));
		tfUsuario.setColumns(10);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				acaoLogar();

			}
		});
		btnEntrar.setIcon(
				new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/icones/alerta_verde_24.png")));

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JOptionPane.showMessageDialog(null, "Sair");
				System.exit(0);
			}
		});
		btnCancelar.setIcon(
				new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/icones/alerta_amarelo_24.png")));

		tfSenha = new JPasswordField();
		tfSenha.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					acaoLogar();
				}
			}
		});
		tfSenha.setHorizontalAlignment(SwingConstants.CENTER);
		tfSenha.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				tfSenha.selectAll();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblUsuario, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
								.addComponent(lblSenha, GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)))
				.addGroup(gl_panel.createSequentialGroup().addGap(80)
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(btnCancelar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 259,
										Short.MAX_VALUE)
						.addComponent(btnEntrar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE))
						.addGap(80))
				.addGroup(gl_panel.createSequentialGroup().addGap(80)
						.addComponent(tfUsuario, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE).addGap(80))
				.addGroup(gl_panel.createSequentialGroup().addGap(80)
						.addComponent(tfSenha, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE).addGap(80)));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_panel.createSequentialGroup().addContainerGap().addComponent(lblUsuario)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, 20,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblSenha).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(tfSenha, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addGap(28).addComponent(btnEntrar)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 34,
												GroupLayout.PREFERRED_SIZE)
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/imagens/logo_128.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblWwwgrupocaravelacom = new JLabel("www.grupocaravela.com.br");
		lblWwwgrupocaravelacom.setFont(new Font("DejaVu Sans Light", Font.BOLD, 12));
		lblWwwgrupocaravelacom.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnConfigurar = new JButton("Configurar");
		btnConfigurar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					if (verificaAdministrador(tfUsuario.getText(), tfSenha.getText())) {
						chamaConfigurar();
					}else{
						JOptionPane.showMessageDialog(null, "Para ter acesso as configurações é necessario informar corretamente o usuario e senha!");
						tfUsuario.requestFocus();
					}
				}
			
		});
		btnConfigurar.setIcon(new ImageIcon(JanelaLogin.class.getResource("/br/com/grupocaravela/icones/configure_24.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(lblWwwgrupocaravelacom, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
				.addComponent(lblNewLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(95)
					.addComponent(btnConfigurar, GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
					.addGap(95))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
					.addGap(46)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnConfigurar, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
					.addComponent(lblWwwgrupocaravelacom))
		);
		contentPane.setLayout(gl_contentPane);
	}

	private void logar() {
		UsuarioLogado ul = new UsuarioLogado();
		ul.setUsuario(usuarioLogado);

		// #############################################
		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException ex) {
					Logger.getLogger(JanelaMenuPrincipal.class.getName()).log(Level.SEVERE, null, ex);
				}
				// ######################METODO A SER
				// EXECUTADO##############################
				
				//carregarObjetoEmpresa();

				usuarioLogado.setUltimoAcesso(dataAtual());
				trx.begin();
				manager.persist(usuarioLogado);
				trx.commit();

				JanelaMenuPrincipal menuPrincipal = new JanelaMenuPrincipal();
				menuPrincipal.setVisible(true);
				menuPrincipal.setLocationRelativeTo(null);
				manager.close();
				
				CriarHistorico.criar(usuarioLogado, "O usuario " + usuarioLogado.getNome() + " fez login no sistema", dataAtual());
				
				dispose();
				// ######################FIM METODO A SER
				// EXECUTADO##############################
			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				tr.start();
				// .....
				EsperaJanela espera = new EsperaJanela();
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
	}

	private boolean verificaSenha(String usuario, String senha) {

		boolean retorno = false;

		try {

			//trx.begin();
			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuarios = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaUsuarios.size(); i++) {
				Usuario u = listaUsuarios.get(i);

				if (u.getUsuario().equals(usuario)) {

					usuarioExiste = true;

					if (u.getSenha().equals(senha)) {

						usuarioLogado = u;

						retorno = true;
					} else {
						JOptionPane.showMessageDialog(null, "Senha incorreta");
						tfSenha.requestFocus();
					}
				} else {
					usuarioExiste = false;
				}

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de categorias: " + e);
		}

		return retorno;
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		// java.util.Date hoje = Calendar.getInstance().getTime();
		return hoje;

	}

	private void limparCampos() {
		tfUsuario.setText("");
		tfSenha.setText("");
	}

	private void acaoLogar() {

		if (verificaSenha(tfUsuario.getText(), tfSenha.getText()) == true) {

			if (usuarioLogado.getAtivo().equals(false)) {
				JOptionPane.showMessageDialog(null, "Usuario Bloqueado!!!");
				limparCampos();
				tfUsuario.requestFocus();
			} else {
				logar();
			}
		} else {
			if (usuarioExiste == false) {
				JOptionPane.showMessageDialog(null, "Usuario não encontrado!!!");
				tfUsuario.requestFocus();
			}

		}
	}

	private void verificaExistenciaUsuario() {

		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Usuario");
			List<Usuario> listaUsuario = consulta.getResultList();
			//trx.commit();

			if (listaUsuario.isEmpty()) {
				Usuario u = new Usuario();
				Cargo c = new Cargo();

				c.setAcessoAndroid(true);
				c.setAcessoCategorias(true);
				c.setAcessoCidades(true);
				c.setAcessoClientes(true);
				c.setAcessoCompras(true);
				c.setAcessoConfiguracao(true);
				c.setAcessoContasPagar(true);
				c.setAcessoContasReceber(true);
				c.setAcessoFormaPagamento(true);
				c.setAcessoFornecedores(true);
				c.setAcessoProdutos(true);
				c.setAcessoRelatorios(true);
				c.setAcessoRotas(true);
				c.setAcessoUnidade(true);
				c.setAcessoUsuarios(true);
				c.setAcessoVendas(true);
				c.setNome("Administrador");
				c.setObservacao("Este cargo ter acesso a todo o sistema sem restrição!");

				trx.begin();
				manager.persist(c);
				trx.commit();

				u.setNome("Administrador");
				u.setUsuario("admin");
				u.setSenha("desbravar");
				u.setBairro("Administrador");
				u.setCep("00.000-000");
				u.setComplemento("Administrador");
				u.setEndereco("Administrador");
				u.setEnderecoNumero("0");
				u.setAtivo(true);
				u.setUf("XX");
				u.setCargo(c);

				trx.begin();
				manager.persist(u);
				trx.commit();

			}

			for (int i = 0; i < listaUsuario.size(); i++) {
				Usuario u = listaUsuario.get(i);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao carregar a tabela de contas a receber: " + e);
		}
	}
	
	private void chamaConfigurar() {
		JanelaConfiguracao configuracao = new JanelaConfiguracao();
		configuracao.setVisible(true);
		configuracao.setLocationRelativeTo(null);
	}
	
	private boolean verificaAdministrador(String usuario, String senha){
		
		boolean retorno = false;
				
		Query consulta = manager.createQuery("from Usuario");
		List<Usuario> listaUsuarios = consulta.getResultList();
		//trx.commit();

		for (int i = 0; i < listaUsuarios.size(); i++) {
			Usuario u = listaUsuarios.get(i);

			if (u.getUsuario().equals(usuario)) {
				
				if (u.getSenha().equals(senha)) {
					
					Cargo c = u.getCargo();
					
					if (c.isAcessoConfiguracao()) {
						retorno = true;
					}else{
						retorno = false;
						JOptionPane.showMessageDialog(null, "O usuario não tem privilégios de acesso as configurações!!!");
					}					
				}
			} 
		}			
		return retorno;
	}
	
	private void carregarObjetoEmpresa(){
		
		File arquivo = null;	

   	 	if ("Linux".equals(System.getProperty("os.name"))) {  //Verifica se o sistema é linux
   		 arquivo = new File("/opt/GrupoCaravela/conf.xml");
        } else {
       	 arquivo = new File("c:\\GrupoCaravela\\conf.xml");            	 
        }
        
		SAXBuilder builder = new SAXBuilder();
		     
		Document doc = null;
		try {
			doc = builder.build(arquivo);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		             
		Element configuracao = (Element) doc.getRootElement();
		         
		List conf = configuracao.getChildren();
		             
		Iterator i = conf.iterator();
		             
		while( i.hasNext() ){
		        Element empresa = (Element) i.next();
		        
		        Empresa.setNomeEpresa(empresa.getChildText("fantasia"));
		        Empresa.setRazaoSocial(empresa.getChildText("razaoSocial"));
		        Empresa.setCnpj(empresa.getChildText("cnpj"));
		        Empresa.setInscricaoEstadua(empresa.getChildText("inscricaoEstadual"));
		        Empresa.setEndereco(empresa.getChildText("endereco"));
		        Empresa.setEnderecoNumero(empresa.getChildText("enderecoNumero"));
		        Empresa.setBairro(empresa.getChildText("bairro"));
		        Empresa.setCidade(empresa.getChildText("cidade"));
		        Empresa.setCep(empresa.getChildText("cep"));
		        Empresa.setTelefonePrincipal(empresa.getChildText("telefonePrincipal"));
		        Empresa.setTelefoneSecundario(empresa.getChildText("telefoneSecundario"));
		        Empresa.setIpServidor(empresa.getChildText("ipServidor"));
		        Empresa.setPortaHttpServidor(empresa.getChildText("portaHttp"));
		        Empresa.setPortaMysqlServidor(empresa.getChildText("portaMysql"));
		        Empresa.setFrase(empresa.getChildText("frase"));		        
		}
		
	}
}
