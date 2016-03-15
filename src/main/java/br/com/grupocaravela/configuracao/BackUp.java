package br.com.grupocaravela.configuracao;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class BackUp {

	private String[] dumps;

	public void salvarBackup() {
		JFileChooser jFileChooser = new JFileChooser();

		jFileChooser.setDialogTitle("Informe o diretório e nome do backup");

		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);
		/*
		 * //filtra por extensao jFileChooser.setFileFilter(new FileFilter() {
		 * 
		 * @Override public String getDescription() { return "Extensão PDF"; }
		 * 
		 * @Override public boolean accept(File f) { return
		 * f.getName().toLowerCase().endsWith("pdf"); } });
		 */

		// mostra janela para salvar
		int acao = jFileChooser.showSaveDialog(null);

		// executa acao conforme opcao selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {
			// escolheu arquivo
			System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
			
			salvar(jFileChooser.getSelectedFile().getAbsolutePath());
			//File f = new File(salvar(), jFileChooser.getSelectedFile().getAbsolutePath());
			
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}
	}

	private void salvar(String nome) {

		dumps = null;
		
		if ("Linux".equals(System.getProperty("os.name"))) {
			dumps = new String[] {"/bin/sh", "-c", "mysqldump -h " + Empresa.getIpServidor() + " -u root -ppeperoni desbravar > " + nome + ".sql"};
			//JOptionPane.showMessageDialog(null, "PASSIE AKI");
		} else {
			dumps = new String[] {"cmd.exe", "/c", "C:\\GrupoCaravela\\mysqldump.exe -h " + Empresa.getIpServidor() + " -u root -ppeperoni desbravar > " + nome + ".sql"};
		}

		try {

			Runtime bkp = Runtime.getRuntime();
			bkp.exec(dumps);

			JOptionPane.showMessageDialog(null, "Backup realizado com sucesso!");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erro" + ex.getMessage());
		}

	}
	
	public void uploadBackup() {
		JFileChooser jFileChooser = new JFileChooser();

		jFileChooser.setDialogTitle("Informe o diretório e nome do backup");

		// seta para selecionar apenas arquivos
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// desabilita todos os tipos de arquivos
		jFileChooser.setAcceptAllFileFilterUsed(false);
		/*
		 * //filtra por extensao jFileChooser.setFileFilter(new FileFilter() {
		 * 
		 * @Override public String getDescription() { return "Extensão PDF"; }
		 * 
		 * @Override public boolean accept(File f) { return
		 * f.getName().toLowerCase().endsWith("pdf"); } });
		 */

		// mostra janela para salvar
		int acao = jFileChooser.showSaveDialog(null);

		// executa acao conforme opcao selecionada
		if (acao == JFileChooser.APPROVE_OPTION) {
			// escolheu arquivo
			System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
			
			//File selectedFile = jFileChooser.getSelectedFile();
			
			upload(jFileChooser.getSelectedFile().getAbsolutePath());
			//File f = new File(salvar(), jFileChooser.getSelectedFile().getAbsolutePath());
			
		} else if (acao == JFileChooser.CANCEL_OPTION) {
			// apertou botao cancelar
		} else if (acao == JFileChooser.ERROR_OPTION) {
			// outra opcao
		}
	}
	
	private void upload(String nome){
		
		dumps = null;
		
		if ("Linux".equals(System.getProperty("os.name"))) {
			dumps = new String[] {"/bin/sh", "-c","mysql -h " + Empresa.getIpServidor() + " -u root -ppeperoni desbravar < " + nome};
			//JOptionPane.showMessageDialog(null, "PASSIE AKI");
		} else {
			 //dump = "cmd.exe /c mysqldump -h localhost -u root -ppeperoni cheque > //tmp/" + nomeBkp;
			dumps = new String[] {"cmd.exe", "/c", "C:\\GrupoCaravela\\mysql.exe -h " + Empresa.getIpServidor() + " -u root -ppeperoni desbravar < " + nome};
			//"cmd.exe", "/c", "C:\\GrupoCaravela\\mysqldump.exe -h " + lerArquivoIp() + " -u root -ppeperoni cheque
		}

		try {

			Runtime bkp = Runtime.getRuntime();
			bkp.exec(dumps);

			JOptionPane.showMessageDialog(null, "Upload realizado com sucesso!");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "erro" + ex.getMessage());
		}

		
	}

	private java.util.Date dataAtual() {

		java.util.Date hoje = new java.util.Date();
		return hoje;

	}
	
	/*
	private String lerArquivoIp() {

		FileReader fileReader;
		String sistema = System.getProperty("os.name");
		String ip = null;

		try {
			if ("Linux".equals(sistema)) {
				fileReader = new FileReader("/opt/GrupoCaravela/software/conf.txt");
			} else {
				fileReader = new FileReader("c:\\GrupoCaravela\\software\\conf.txt");
			}

			BufferedReader reader = new BufferedReader(fileReader);
			String data = null;
			while ((data = reader.readLine()) != null) {
				ip = String.valueOf(data);
			}
			fileReader.close();
			reader.close();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "ERRO! Erro ao ler o arquivo de versão do sistema!");
		}

		return ip;
	}
*/
	
	/*
	
	public boolean backupDatabase(String path, String whichServer){
	    String cmd;
	    Process runtimeProcess;

	    if(whichServer.equalsIgnoreCase("local"))
	    {
	        cmd = "mysqldump -u " + getSourceUsername() + " -p" + getSourceServerPassword()
	            + " --add-drop-database -B " + getSourceDatabase() + " -r " + path;
	    }         
	    else if(whichServer.equalsIgnoreCase("remote"))
	    {
	        cmd = "mysqldump -u " + getDestinationUsername() + " -p" + getDestinationServerPassword()
	            + " --add-drop-database -B " + getDestinationDatabase() + " -r " + path;
	    }
	    else
	    {
	        System.out.println("Input server incorrect");
	        return false;
	    }

	    try{

	        String[] cmdArray = new String[]{"C:\\wamp\\bin\\mysql\\mysql5.5.24\\bin\\mysqldump.exe", cmd};
	        System.out.println("Preparing for dump.");
	        runtimeProcess = Runtime.getRuntime().exec(cmdArray);

	        int processCompleted = runtimeProcess.waitFor();

	        if(processCompleted == 0)
	        {
	            System.out.println("Dump done!");
	            return true;
	        }
	        else
	        {
	            System.out.println("Error doing dump!");
	        }

	    } catch(Exception ex)
	    {
	        System.out.println("Exception -> " + ex.getMessage());
	    }
	    return false;

	}
	
	/*
	public boolean backupDatabase(String path, String whichServer) 
	{

	    List<String> args = new ArrayList<String>();
	    args.add("C:\\wamp\\bin\\mysql\\mysql5.5.24\\bin\\mysqldump.exe");

	    args.add("-u");
	    args.add(getSourceUsername());
	    args.add("-p");
	    args.add(getSourceServerPassword());
	    args.add("--add-drop-database");
	    args.add("-B");
	    args.add(getSourceDatabase());
	    args.add("-r");
	    args.add(path);

	    try{
	        ProcessBuilder pb = new ProcessBuilder(args);
	        pb.redirectError();
	        Process p = pb.start();

	        InputStream is = p.getInputStream();

	        int in = -1;

	        while((in = is.read()) != -1)
	        {
	            System.out.println((char) in);
	        }

	        int proccessCompleted = p.waitFor();

	        if(proccessCompleted == 0)
	        {
	            System.out.println("Dump done!");
	            return true;
	        }
	        else
	        {
	            System.out.println("Error doing dump!");
	            return false;
	        }
	    }
	    catch(IOException | InterruptedException ex)
	    {
	        System.out.println("Exception exportDB -> " + ex.getMessage() + "|" + ex.getLocalizedMessage());
	    }
	    return false;
	}
	*/
}
