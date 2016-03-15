package br.com.grupocaravela.configuracao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import br.com.grupocaravela.view.JanelaConfiguracao;

public class EntityManagerProducer {

	private static EntityManagerFactory factory;
	private static Boolean servidorLocal;
			
	public EntityManagerProducer() {
		
		try {
			
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.url", "jdbc:mysql://" + lerArquivoXml() +"/desbravar");
			properties.put("javax.persistence.jdbc.user", "root");
			properties.put("javax.persistence.jdbc.password", "peperoni");
			
			factory = Persistence.createEntityManagerFactory("DesbravarPU", properties);
			servidorLocal = true;
		} catch (Exception e) {
			
			Object[] options = { "Sim", "Não" };
			int i = JOptionPane.showOptionDialog(null,
					"ATENÇÃO!!! Servidor não encontrado. Gostaria de alterar as configurações com o servidor?", "Conexão",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (i == JOptionPane.YES_OPTION) {
				
				chamaConfigurar();
				
			}
			
			
			/*
			
			Object[] options = { "Sim", "Não" };
			int i = JOptionPane.showOptionDialog(null,
					"ATENÇÃO!!! Não foi encontrado um servidor local. Gostaria de conectar ao servidor usando a internet?", "Conexão",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (i == JOptionPane.YES_OPTION) {
			
			JOptionPane.showMessageDialog(null,
					"ATENÇÃO! Você esta realizando um acesso via Internet, isso acarretara em uma conexão lenta!!!");
			factory = Persistence.createEntityManagerFactory("DesbravarPU_WAN");
			servidorLocal = false;
			
			} else {
				
				JOptionPane.showMessageDialog(null,
						"ATENÇÃO! Servidor não encontrado!!!");
			}
		*/	
		}		
				
	}
	
	public static EntityManager createEntityManager() {
		return factory.createEntityManager();
	}
	
	public void closeEntityManager(EntityManager manager){
		manager.close();
	}

	public static Boolean getServidorLocal() {
		return servidorLocal;
	}

	private void chamaConfigurar() {
		JanelaConfiguracao configuracao = new JanelaConfiguracao();
		configuracao.setVisible(true);
		configuracao.setLocationRelativeTo(null);
	}
	
private String lerArquivoXml(){
		
		String retorno = null;
	
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
		        		        
		        retorno = empresa.getChildText("ipServidor");
		        
		        
		}
		return retorno;
	}
	
}
