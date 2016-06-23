package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.objeto.Rota;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorioEspelho {

	String sistema = System.getProperty("os.name");

	//método
	    public void report(String endereco, Usuario usuario, Rota rota, Date dataInicial, Date dataFinal) throws JRException {

	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper;

	        Map map = new HashMap<>();

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("IP_SERVIDOR", Empresa.getIpServidor());
	        
	        if (usuario != null) {
	        	//JOptionPane.showMessageDialog(null, "id usuario " + usuario.getId());
	        	map.put("ID_USUARIO", usuario.getId());
	        	map.put("NOME_USUARIO", usuario.getNome());
			}
	        if (rota != null) {
	        	//JOptionPane.showMessageDialog(null, "id rota " + rota.getId());
	        	map.put("ID_ROTA", rota.getId());
	        	map.put("NOME_ROTA", rota.getNome());
			}
	        
	        if (dataInicial != null && dataFinal != null) {
	        	SimpleDateFormat formatUsa = new SimpleDateFormat("yyyy/MM/dd");
		        SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");
		       	        
		        map.put("DATA_INICIAL_SQL", formatUsa.format(dataInicial));
		        map.put("DATA_FINAL_SQL", formatUsa.format(dataFinal));
		        
		        map.put("DATA_INICIAL", formatBra.format(dataInicial));
		        map.put("DATA_FINAL", formatBra.format(dataFinal));
			}
	        
	        	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	    
	    public void reportEspelhoCaixa(String endereco, Usuario usuario, Rota rota, Date dataInicial, Date dataFinal, Double total) throws JRException {

	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper;

	        Map map = new HashMap<>();

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("IP_SERVIDOR", Empresa.getIpServidor());
	        map.put("TOTAL", total);
	        
	        if (usuario != null) {
	        	//JOptionPane.showMessageDialog(null, "id usuario " + usuario.getId());
	        	//map.put("ID_USUARIO", usuario.getId());
	        	map.put("NOME_USUARIO", usuario.getNome());
			}
	        if (rota != null) {
	        	//JOptionPane.showMessageDialog(null, "id rota " + rota.getId());
	        	map.put("ID_ROTA", rota.getId());
	        	map.put("NOME_ROTA", rota.getNome());
			}
	        
	        if (dataInicial != null && dataFinal != null) {
	        	SimpleDateFormat formatUsa = new SimpleDateFormat("yyyy-MM-dd");
		        SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");
		       	        
		        map.put("DATA_INICIAL_SQL", formatUsa.format(dataInicial) + " 00:00:00");
		        map.put("DATA_FINAL_SQL", formatUsa.format(dataFinal) + " 23:59:59");
		        
		        map.put("DATA_INICIAL", formatBra.format(dataInicial));
		        map.put("DATA_FINAL", formatBra.format(dataFinal));
			}
	        
	        	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	
}
