package br.com.grupocaravela.configuracao;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import br.com.grupocaravela.aguarde.EsperaJanela;

public class Update {

	private Versao versao = new Versao();

	String sistema = System.getProperty("os.name");

	private Boolean servidorLocal = EntityManagerProducer.getServidorLocal();

	/**
	 * Arquivo para update
	 */
	//private String urlUpdateFile = "http://ENDEREÇO_DO_SEU_JAR/arquivo.jar";

	public void verificaVersao() {

		atualizaOrientacao();

		JOptionPane.showMessageDialog(null, lerArquivoVersao().toString());

		if (lerArquivoVersao() > versao.getVersao()) {

			Object[] option = { "Atualizar", "Não Atualizar" };
			int i = JOptionPane.showOptionDialog(null, "ATENÇÃO!!! Ja existe uma nova versão para este sistema!",
					"Atualizar sistema", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option,
					option[0]);
			if (i == JOptionPane.YES_OPTION) {
				atualizarAguarde();
			}
		}

	}

	public void atualizaOrientacao() {

		//if (servidorLocal) {
			try {
				if ("Linux".equals(sistema)) {
					download(
							"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
									+ "/desbravar/software/sistema/versao.txt",
							"/opt/GrupoCaravela/software/sistema/versao.txt", null, 80);
				} else {
					download(
							"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
									+ "/desbravar/software/sistema/versao.txt",
							"c:\\GrupoCaravela\\software\\sistema\\versao.txt", null, 80);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"ERRO! Não foi possivel verificar a versão do sistema! Procurar o suporte Tecnico!!!");
			}
			/*
		} else {
			
			try {
				if ("Linux".equals(sistema)) {
					download(
							"http://" + Empresa.getIpWebServidor() + ":" + Empresa.getPortaWebHttpServidor()
									+ "/desbravar/software/sistema/versao.txt",
							"/opt/GrupoCaravela/software/sistema/versao.txt", null, 80);
				} else {
					download(
							"http://" + Empresa.getIpWebServidor() + ":" + Empresa.getPortaWebHttpServidor()
									+ "/desbravar/software/sistema/versao.txt",
							"c:\\GrupoCaravela\\software\\sistema\\versao.txt", null, 80);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"ERRO! Não foi possivel verificar a versão do sistema! Procurar o suporte Tecnico!!!");
			}
		}
		*/

	}

	public void atualizar() {

		//if (servidorLocal) {
			try {
				if ("Linux".equals(sistema)) {

					try {
						downloadSistema(
								"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
										+ "/desbravar/software/sistema/Desbravar2.jar",
								"/opt/GrupoCaravela/software/sistema/Desbravar2.jar", null, 80);

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
					}

				} else {

					try {
						downloadSistema(
								"http://" + Empresa.getIpServidor() + ":" + Empresa.getPortaHttpServidor()
										+ "/desbravar/software/sistema/Desbravar2.jar",
								"c:\\GrupoCaravela\\software\\sistema\\Desbravar2.jar", null, 80);

					} catch (Exception e) {
						JOptionPane.showMessageDialog(null,
								"ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
					}

				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
			}
			/*
		} else {
			
			try {
				if ("Linux".equals(sistema)) {

					try {
						downloadSistema(
								"http://" + Empresa.getIpWebServidor() + ":" + Empresa.getPortaWebHttpServidor()
										+ "/desbravar/software/sistema/Desbravar2.jar",
								"/opt/GrupoCaravela/software/sistema/Desbravar2.jar", null, 80);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
					}

				} else {

					try {
						download(
								"http://" + Empresa.getIpWebServidor() + ":" + Empresa.getPortaWebHttpServidor()
										+ "/desbravar/software/sistema/Desbravar2.jar",
								"c:\\GrupoCaravela\\software\\sistema\\Desbravar2.jar", null, 80);

					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null,
								"ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
					}

				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
			}
		}
		*/

	}

	/**
	 * Faz download de arquivo
	 */

	private void download(String address, String localFileName, String host, int porta) {

		// leitor do arquivo a ser baixado
		InputStream in = null;
		// conexão com a internete
		URLConnection conn = null;
		// escritor do arquivo que será baixado
		OutputStream out = null;

		System.out.println("Update.download() BAIXANDO " + address);

		try {
			URL url = new URL(address);
			out = new BufferedOutputStream(new FileOutputStream(localFileName));

			// verifica se existe proxy
			if (host != "" && host != null) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, porta));
				conn = url.openConnection(proxy);
			} else {
				conn = url.openConnection();
			}

			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println(localFileName + "\t" + numWritten);

		} catch (Exception exception) {
			exception.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possivel estabelecer conexão com o servidor!!!");
			// System.exit(0);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
	}

	private void downloadSistema(String address, String localFileName, String host, int porta) {

		// leitor do arquivo a ser baixado
		InputStream in = null;
		// conexão com a internete
		URLConnection conn = null;
		// escritor do arquivo que será baixado
		OutputStream out = null;

		System.out.println("Update.download() BAIXANDO " + address);

		try {
			URL url = new URL(address);
			out = new BufferedOutputStream(new FileOutputStream(localFileName));

			// verifica se existe proxy
			if (host != "" && host != null) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, porta));
				conn = url.openConnection(proxy);
			} else {
				conn = url.openConnection();
			}

			in = conn.getInputStream();
			byte[] buffer = new byte[1024];
			int numRead;
			long numWritten = 0;
			while ((numRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, numRead);
				numWritten += numRead;
			}
			System.out.println(localFileName + "\t" + numWritten);

			JOptionPane.showMessageDialog(null,
					"Versão atualizada com sucesso! - Versão atual - " + lerArquivoVersao());
			JOptionPane.showMessageDialog(null, "Aguarde a abertura/reinicio do sistema!!!");

			try {
				if ("Linux".equals(sistema)) {
					Runtime.getRuntime().exec("java -jar /opt/GrupoCaravela/software/sistema/Desbravar2.jar");
				} else {
					Runtime.getRuntime().exec("java -jar c:\\GrupoCaravela\\software\\sistema\\Desbravar2.jar");
				}
				System.exit(0);

			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "ERRO! Erro ao atualizar o sistema! Procurar o suporte Tecnico!!!");
			}

		} catch (Exception exception) {
			exception.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possivel estabelecer conexão com o servidor!!!");
			// System.exit(0);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ioe) {
			}
		}
	}

	public Double lerArquivoVersao() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		Double versao = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/sistema/versao.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\sistema\\versao.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				versao = Double.parseDouble(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de versão do sistema!");
		}

		return versao;
	}

	public List<String> lerArquivoDependencias() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		List<String> dependencia = new ArrayList<String>();

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela2/Software/dist/lib/dependencias.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\Software\\dist\\lib\\dependencias.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				dependencia.add(data);

			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de versão do sistema!");
		}

		return dependencia;
	}

	// #######################################
	public void atualizarAguarde() {

		final Thread tr = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException ex) {
					Logger.getLogger(Update.class.getName()).log(Level.SEVERE, null, ex);
				}
				atualizar();

			}
		});
		new Thread(new Runnable() {
			@Override
			public void run() {
				tr.start();

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

}
