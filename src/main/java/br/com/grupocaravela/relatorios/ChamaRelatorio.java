package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorio {

	String sistema = System.getProperty("os.name");

	//método
	    public void report(String endereco) throws JRException {

	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper; 

	        Map map = new HashMap<>();
	        
	        //map.put("IP_SERVIDOR", Empresa.getIpServidor());

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }	
}
