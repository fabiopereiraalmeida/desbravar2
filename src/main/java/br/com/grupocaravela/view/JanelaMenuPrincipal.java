package br.com.grupocaravela.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import br.com.grupocaravela.aguarde.EsperaJanela;
import br.com.grupocaravela.configuracao.BackUp;
import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.configuracao.Versao;
import br.com.grupocaravela.objeto.Cargo;
import br.com.grupocaravela.relatorios.ChamaRelatorio;
import br.com.grupocaravela.util.UsuarioLogado;
import net.sf.jasperreports.engine.JRException;

public class JanelaMenuPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private JLabel lblNomeusuario;
	private JLabel lblCargoUsuario;

	private JLabel lblDataHoraAcesso;
	private JLabel lblVersao;

	private JMenuItem mntmFazerBackup;

	private JMenuItem mntmRestaurarBackup;

	private JMenuItem mntmConfiguraes;
	
	private Cargo cargo = UsuarioLogado.getUsuario().getCargo();

	private JButton btnVendas;

	private JMenuItem mntmSobre;

	private JButton btnClientes;

	private JButton btnReceber;

	private JButton btnProdutos;

	private JButton btnCompras;

	private JButton btnPagar;

	private JButton btnVAndroid;

	private JMenuItem mntmCargo;

	private JMenuItem mntmCidades;

	private JMenuItem mntmClientes;

	private JMenuItem mntmFormapagamento;

	private JMenuItem mntmFornecedores;

	private JMenuItem mntmProdutos;

	private JMenuItem mntmRotas;

	private JMenuItem mntmUnidades;

	private JMenuItem mntmUsuarios;

	private JMenuItem mntmClientes_detalhes;

	private JMenuItem mntmEspelhos;

	private JMenuItem mntmCaixa;

	private JMenuItem mntmClientesRelao;

	private JMenuItem mntmProdutosDetalhes;

	private JMenuItem mntmProdutosRelao;

	private JMenuItem mntmCategorias;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JanelaMenuPrincipal frame = new JanelaMenuPrincipal();
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
	public JanelaMenuPrincipal() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/logoCaravela.png")));
		setTitle("Desbravar 2 - Menu principal");
		carregarJanela();
		carregaUsuario();
		carrgarVersao();
		
		permicoes();
		
	}

	private void carregarJanela() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 681);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);
		
		JMenu mnHistrico = new JMenu("Histórico");
		mnHistrico.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/historico_24.png")));
		mnArquivo.add(mnHistrico);
		
		JMenuItem mntmVendas = new JMenuItem("Vendas");
		mntmVendas.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/vendas_24.png")));
		mntmVendas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						JanelaHistoricoVendas historicoVendas = new JanelaHistoricoVendas();
						historicoVendas.setVisible(true);
						historicoVendas.setLocationRelativeTo(null);
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

				// ###############################################
				
			}
		});
		mnHistrico.add(mntmVendas);

		JMenuItem mntmSair = new JMenuItem("Sair");
		mntmSair.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/sair_24.png")));
		mnArquivo.add(mntmSair);

		JMenu mnArquivo_1 = new JMenu("Editar");
		menuBar.add(mnArquivo_1);

		mntmCategorias = new JMenuItem("Categorias");
		mntmCategorias.setEnabled(false);
		mntmCategorias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroCategorias cadastroCategorias = new JanelaCadastroCategorias();
						cadastroCategorias.setVisible(true);
						cadastroCategorias.setLocationRelativeTo(null);
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

				// ###############################################

			}
		});

		mntmCargo = new JMenuItem("Cargo");
		mntmCargo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroCargo cadastroCargo = new JanelaCadastroCargo();
						cadastroCargo.setVisible(true);
						cadastroCargo.setLocationRelativeTo(null);
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

				// ###############################################

			}
		});
		mntmCargo.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/cargo_24.png")));
		mnArquivo_1.add(mntmCargo);
		mntmCategorias.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/categoria_24.png")));
		mnArquivo_1.add(mntmCategorias);

		mntmCidades = new JMenuItem("Cidades");
		mntmCidades.setEnabled(false);
		mntmCidades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroCidades cadastroCidades = new JanelaCadastroCidades();
						cadastroCidades.setVisible(true);
						cadastroCidades.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmCidades.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/cidade_24.png")));
		mnArquivo_1.add(mntmCidades);

		mntmClientes = new JMenuItem("Clientes");
		mntmClientes.setEnabled(false);
		mntmClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroClientes cadastroClientes = new JanelaCadastroClientes();
						cadastroClientes.setVisible(true);
						cadastroClientes.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmClientes.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/clientes_24.png")));
		mnArquivo_1.add(mntmClientes);

		mntmFormapagamento = new JMenuItem("FormaPagamento");
		mntmFormapagamento.setEnabled(false);
		mntmFormapagamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroFormaPagamentos cadastroFormaPagamentos = new JanelaCadastroFormaPagamentos();
						cadastroFormaPagamentos.setVisible(true);
						cadastroFormaPagamentos.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmFormapagamento.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/forma_pagamento_24.png")));
		mnArquivo_1.add(mntmFormapagamento);

		mntmFornecedores = new JMenuItem("Fornecedores");
		mntmFornecedores.setEnabled(false);
		mntmFornecedores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroFornecedores cadastroFornecedores = new JanelaCadastroFornecedores();
						cadastroFornecedores.setVisible(true);
						cadastroFornecedores.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmFornecedores.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/fornecedor_24.png")));
		mnArquivo_1.add(mntmFornecedores);

		mntmProdutos = new JMenuItem("Produtos");
		mntmProdutos.setEnabled(false);
		mntmProdutos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroProdutos cadastroProdutos = new JanelaCadastroProdutos();
						cadastroProdutos.setVisible(true);
						cadastroProdutos.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmProdutos.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/produtos_24.png")));
		mnArquivo_1.add(mntmProdutos);

		mntmRotas = new JMenuItem("Rotas");
		mntmRotas.setEnabled(false);
		mntmRotas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroRotas cadastroRotas = new JanelaCadastroRotas();
						cadastroRotas.setVisible(true);
						cadastroRotas.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmRotas.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/rota_24.png")));
		mnArquivo_1.add(mntmRotas);

		mntmUnidades = new JMenuItem("Unidades");
		mntmUnidades.setEnabled(false);
		mntmUnidades.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroUnidades cadastroUnidades = new JanelaCadastroUnidades();
						cadastroUnidades.setVisible(true);
						cadastroUnidades.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmUnidades.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/unidade_24.png")));
		mnArquivo_1.add(mntmUnidades);

		mntmUsuarios = new JMenuItem("Usuários");
		mntmUsuarios.setEnabled(false);
		mntmUsuarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroUsuarios cadastroUsuarios = new JanelaCadastroUsuarios();
						cadastroUsuarios.setVisible(true);
						cadastroUsuarios.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		mntmUsuarios.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/colaborador_24.png")));
		mnArquivo_1.add(mntmUsuarios);

		JMenu mnRelatrios = new JMenu("Relatórios");
		menuBar.add(mnRelatrios);

		mntmClientes_detalhes = new JMenuItem("Clientes Detalhes");
		mntmClientes_detalhes.setEnabled(false);
		mntmClientes_detalhes.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		mntmClientes_detalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					relatorioClienteDetalhado();
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		mntmEspelhos = new JMenuItem("Espelhos");
		mntmEspelhos.setEnabled(false);
		mntmEspelhos.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/espelho_24.png")));
		mntmEspelhos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaEspelhos janelaEspelhos = new JanelaEspelhos();
						janelaEspelhos.setVisible(true);
						janelaEspelhos.setLocationRelativeTo(null);
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

				// ###############################################

			}
		});
		
		mntmCaixa = new JMenuItem("Caixa");
		mntmCaixa.setEnabled(false);
		mntmCaixa.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/pagamentos_24.png")));
		mntmCaixa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						JanelaCaixa janelaCaixa = new JanelaCaixa();
						janelaCaixa.setVisible(true);
						janelaCaixa.setLocationRelativeTo(null);
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

				// ###############################################
				
			}
		});
		mnRelatrios.add(mntmCaixa);
		mnRelatrios.add(mntmEspelhos);
		mnRelatrios.add(mntmClientes_detalhes);

		mntmClientesRelao = new JMenuItem("Clientes Relação");
		mntmClientesRelao.setEnabled(false);
		mntmClientesRelao.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		mntmClientesRelao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					relatorioClienteSimples();
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		mnRelatrios.add(mntmClientesRelao);

		mntmProdutosDetalhes = new JMenuItem("Produtos detalhes");
		mntmProdutosDetalhes.setEnabled(false);
		mntmProdutosDetalhes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					relatorioProdutoDetalhado();
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmProdutosDetalhes.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		mnRelatrios.add(mntmProdutosDetalhes);

		mntmProdutosRelao = new JMenuItem("Produtos relação");
		mntmProdutosRelao.setEnabled(false);
		mntmProdutosRelao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					relatorioProdutoSimples();
				} catch (JRException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mntmProdutosRelao.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/relatorios_24.png")));
		mnRelatrios.add(mntmProdutosRelao);

		JMenu mnFerramentas = new JMenu("Ferramentas");
		menuBar.add(mnFerramentas);

		JMenu mnSistema = new JMenu("Sistema");
		menuBar.add(mnSistema);
		
		mntmFazerBackup = new JMenuItem("Fazer BackUp");
		mntmFazerBackup.setEnabled(false);
		mntmFazerBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BackUp backUp = new BackUp();

				backUp.salvarBackup();
				
			}
		});
		
		mntmConfiguraes = new JMenuItem("Configurações");
		mntmConfiguraes.setEnabled(false);
		mntmConfiguraes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				chamaConfigurar();
				
			}
		});
		mntmConfiguraes.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/configure_24.png")));
		mnSistema.add(mntmConfiguraes);
		mntmFazerBackup.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/backup_24.png")));
		mnSistema.add(mntmFazerBackup);
		
		mntmRestaurarBackup = new JMenuItem("Restaurar BackUp");
		mntmRestaurarBackup.setEnabled(false);
		mntmRestaurarBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BackUp backUp = new BackUp();

				backUp.uploadBackup();
				
			}
		});
		mntmRestaurarBackup.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/backup_upload_24.png")));
		mnSistema.add(mntmRestaurarBackup);

		JMenu mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);
		
		mntmSobre = new JMenuItem("Sobre");
		mntmSobre.setIcon(new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/sobre_24.png")));
		mnAjuda.add(mntmSobre);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(153, 153, 153)));

		JPanel panel_1 = new JPanel();

		btnVendas = new JButton("Vendas");
		btnVendas.setEnabled(false);
		btnVendas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
						JanelaVendas vendas = new JanelaVendas();
						vendas.setVisible(true);
						vendas.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		panel_1.add(btnVendas);
		btnVendas.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnVendas.setHorizontalTextPosition(SwingConstants.CENTER);
		btnVendas.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/vendas_64.png")));

		JPanel panel_2 = new JPanel();

		btnClientes = new JButton("Clientes");
		btnClientes.setEnabled(false);
		btnClientes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
						JanelaCadastroClientes cadastroClientes = new JanelaCadastroClientes();
						cadastroClientes.setVisible(true);
						cadastroClientes.setLocationRelativeTo(null);
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

				// ###############################################

			}
		});
		btnClientes.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnClientes.setHorizontalTextPosition(SwingConstants.CENTER);
		btnClientes.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/clientes_64.png")));
		panel_2.add(btnClientes);

		btnReceber = new JButton("C. Receber");
		btnReceber.setEnabled(false);
		btnReceber.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/recebimento_64.png")));
		btnReceber.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnReceber.setHorizontalTextPosition(SwingConstants.CENTER);
		btnReceber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaContasReceber janelaContasReceber = new JanelaContasReceber();
						janelaContasReceber.setVisible(true);
						janelaContasReceber.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		panel_2.add(btnReceber);

		JPanel panel_3 = new JPanel();

		btnProdutos = new JButton("Produtos");
		btnProdutos.setEnabled(false);
		btnProdutos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

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
						JanelaCadastroProdutos cadastroProdutos = new JanelaCadastroProdutos();
						cadastroProdutos.setVisible(true);
						cadastroProdutos.setLocationRelativeTo(null);
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

				// ###############################################

			}
		});
		btnProdutos.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/produtos_64.png")));
		btnProdutos.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnProdutos.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_3.add(btnProdutos);

		btnCompras = new JButton("Compras");
		btnCompras.setEnabled(false);
		btnCompras.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/compras_64.png")));
		btnCompras.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnCompras.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_3.add(btnCompras);

		btnPagar = new JButton("C. Pagar");
		btnPagar.setEnabled(false);
		btnPagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnPagar.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/pagamentos_64.png")));
		btnPagar.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnPagar.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_3.add(btnPagar);

		JPanel panel_4 = new JPanel();

		btnVAndroid = new JButton("V. Android");
		btnVAndroid.setEnabled(false);
		btnVAndroid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						JanelaAndroid android = new JanelaAndroid();
						android.setVisible(true);
						android.setLocationRelativeTo(null);
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

				// ###############################################
			}
		});
		btnVAndroid.setIcon(new ImageIcon(
				JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/venda_android_64.png")));
		btnVAndroid.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnVAndroid.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_4.add(btnVAndroid);

		JPanel panel_7 = new JPanel();

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/imagens/logo_256.png")));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 396, Short.MAX_VALUE)
					.addContainerGap())
		);
		panel_7.setLayout(gl_panel_7);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new LineBorder(new Color(153, 153, 153)));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
						.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
						.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addGap(2))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(4))
		);

		JLabel lblUsuario = new JLabel("Usuario:");

		lblNomeusuario = new JLabel("NomeUsuario");

		JLabel lblCargo = new JLabel("Cargo:");

		lblCargoUsuario = new JLabel("CargoUsuario");

		JLabel lblUltimoAcesso = new JLabel("Ultimo Acesso:");

		lblDataHoraAcesso = new JLabel("DataHoraAcesso");

		lblVersao = new JLabel("x.xx");
		lblVersao.setFont(new Font("Dialog", Font.BOLD, 10));

		JLabel lblNewLabel_1 = new JLabel("Sistema licenciado para a ");
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 10));

		JLabel lblNomeEmpresa = new JLabel(Empresa.getNomeEpresa());
		lblNomeEmpresa.setFont(new Font("Dialog", Font.BOLD, 10));

		JLabel lblVersoDoAmrica = new JLabel("Versão do América nº ");
		lblVersoDoAmrica.setFont(new Font("Dialog", Font.BOLD, 10));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblUsuario)
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addComponent(lblNomeusuario, GroupLayout.PREFERRED_SIZE, 312,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(lblCargo).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblCargoUsuario, GroupLayout.PREFERRED_SIZE, 185,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
								.addComponent(lblUltimoAcesso).addGap(12).addComponent(lblDataHoraAcesso,
										GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_6.createSequentialGroup().addComponent(lblNewLabel_1)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblNomeEmpresa, GroupLayout.PREFERRED_SIZE, 332,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
								.addComponent(lblVersoDoAmrica).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblVersao)))
						.addContainerGap()));
		gl_panel_6
				.setVerticalGroup(
						gl_panel_6
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(
										gl_panel_6.createSequentialGroup().addContainerGap()
												.addGroup(
														gl_panel_6.createParallelGroup(Alignment.LEADING)
																.addComponent(lblUltimoAcesso)
																.addGroup(gl_panel_6.createSequentialGroup()
																		.addGroup(gl_panel_6
																				.createParallelGroup(Alignment.LEADING)
																				.addComponent(lblDataHoraAcesso)
																				.addGroup(gl_panel_6
																						.createParallelGroup(
																								Alignment.BASELINE)
																						.addComponent(lblUsuario)
																						.addComponent(lblNomeusuario)
																						.addComponent(lblCargo)
																						.addComponent(lblCargoUsuario)))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
														.addComponent(lblVersao).addComponent(lblVersoDoAmrica))
										.addGroup(gl_panel_6.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblNewLabel_1).addComponent(lblNomeEmpresa)))))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		panel_6.setLayout(gl_panel_6);

		JPanel panel_5 = new JPanel();

		JButton btnLogoff = new JButton("LogOff");
		btnLogoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
						JanelaLogin jnLogin = new JanelaLogin();
						jnLogin.setVisible(true);
						jnLogin.setLocationRelativeTo(null);
						
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

				// ###############################################
				
			}
		});
		btnLogoff.setIcon(
				new ImageIcon(JanelaMenuPrincipal.class.getResource("/br/com/grupocaravela/icones/log-off_64.png")));
		btnLogoff.setVerticalTextPosition(SwingConstants.BOTTOM);
		btnLogoff.setHorizontalTextPosition(SwingConstants.CENTER);
		panel_5.add(btnLogoff);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup().addGap(5)
								.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addGap(5)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addGap(5)
				.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(5)
				.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
				.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(5)
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addComponent(panel_4, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		panel.setLayout(gl_panel);
		contentPane.setLayout(gl_contentPane);
	}

	private void carregaUsuario() {
		try {
			lblNomeusuario.setText(UsuarioLogado.getUsuario().getNome());
			String dtUltimoAcesso = formatDataHora.format(UsuarioLogado.getUsuario().getUltimoAcesso());
			lblDataHoraAcesso.setText(dtUltimoAcesso);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Usuario logado não encontrado!!!");
		}

	}

	private void carrgarVersao() {
		lblVersao.setText(Versao.getVersao().toString());
		try {
			lblCargoUsuario.setText(UsuarioLogado.getUsuario().getCargo().getNome());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Não foi possivel encontrar o cargo para o usuario logado!");
			lblCargoUsuario.setText("");
		}

	}

	private void relatorioClienteDetalhado() throws JRException {

		ChamaRelatorio chamaRelatorio = new ChamaRelatorio();

		chamaRelatorio.report("ClienteDetalhado.jasper");

	}

	private void relatorioClienteSimples() throws JRException {

		ChamaRelatorio chamaRelatorio = new ChamaRelatorio();

		chamaRelatorio.report("ClienteRelacao.jasper");

	}
	
	private void relatorioProdutoDetalhado() throws JRException {

		ChamaRelatorio chamaRelatorio = new ChamaRelatorio();

		chamaRelatorio.report("ProdutoDetalhado.jasper");

	}

	private void relatorioProdutoSimples() throws JRException {

		ChamaRelatorio chamaRelatorio = new ChamaRelatorio();

		chamaRelatorio.report("ProdutosRelacao.jasper");

	}
	
	private void chamaConfigurar() {
		JanelaConfiguracao configuracao = new JanelaConfiguracao();
		configuracao.setVisible(true);
		configuracao.setLocationRelativeTo(null);
	}
	
	private void permicoes(){
		
		if (cargo.isAcessoConfiguracao()) {
			mntmConfiguraes.setEnabled(true);
			mntmFazerBackup.setEnabled(true);
			mntmRestaurarBackup.setEnabled(true);
		}
		
		if (cargo.isAcessoAndroid()) {
			btnVAndroid.setEnabled(true);
		}
				
		if (cargo.isAcessoCategorias()) {
			mntmCategorias.setEnabled(true);			
		}
		
		if (cargo.isAcessoCidades()) {
			mntmCidades.setEnabled(true);
		}
		
		if (cargo.isAcessoClientes()) {
			btnClientes.setEnabled(true);
			mntmClientes.setEnabled(true);
		}
		
		if (cargo.isAcessoCompras()) {
			btnCompras.setEnabled(true);
		}
		
		if (cargo.isAcessoContasPagar()) {
			btnPagar.setEnabled(true);
		}
		
		if (cargo.isAcessoContasReceber()) {
			btnReceber.setEnabled(true);			
		}
		
		if (cargo.isAcessoFormaPagamento()) {
			mntmFormapagamento.setEnabled(true);
		}
		
		if (cargo.isAcessoFornecedores()) {
			mntmFornecedores.setEnabled(true);
		}
		
		if (cargo.isAcessoProdutos()) {
			btnProdutos.setEnabled(true);
			mntmProdutos.setEnabled(true);
		}
		
		if (cargo.isAcessoRelatorios()) {
			mntmCaixa.setEnabled(true);
			mntmEspelhos.setEnabled(true);
			mntmClientes_detalhes.setEnabled(true);
			mntmClientesRelao.setEnabled(true);
			mntmProdutosDetalhes.setEnabled(true);
			mntmProdutosRelao.setEnabled(true);
		}
		
		if (cargo.isAcessoRotas()) {
			mntmRotas.setEnabled(true);
		}
		
		if (cargo.isAcessoUnidade()) {
			mntmUnidades.setEnabled(true);
		}
		
		if (cargo.isAcessoUsuarios()) {
			mntmUsuarios.setEnabled(true);
		}
		
		if (cargo.isAcessoVendas()) {
			btnVendas.setEnabled(true);
		}
	}
}
