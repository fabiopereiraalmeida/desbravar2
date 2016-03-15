package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorioReciboVenda {

	String sistema = System.getProperty("os.name");

	//método
	    public void report(String endereco, VendaCabecalho vendaCabecalho, JTable table) throws JRException {

	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper;

	        URL cabecalhoRecibo = getClass().getResource("CabecalhoRecibo.jasper");
	        //URL arquivo2 = getClass().getResource("/relatorios/");
	        
	        Map map = new HashMap<>();
	        
	        map.put("IP_SERVIDOR", Empresa.getIpServidor());
	        
	        map.put("RAZAO_SOCIAL", vendaCabecalho.getCliente().getRazaoSocial());
	        map.put("FANTASIA", vendaCabecalho.getCliente().getFantasia());
	        try {
	        	map.put("ENDERECO", vendaCabecalho.getCliente().getEnderecos().get(0).getEndereco());
	        	map.put("END_NUMERO", vendaCabecalho.getCliente().getEnderecos().get(0).getNumero());
	        	map.put("BAIRRO", vendaCabecalho.getCliente().getEnderecos().get(0).getBairro());
		        map.put("UF", vendaCabecalho.getCliente().getEnderecos().get(0).getUf());
		        map.put("CIDADE", vendaCabecalho.getCliente().getEnderecos().get(0).getCidade());
		        map.put("CEP", vendaCabecalho.getCliente().getEnderecos().get(0).getCep());
			} catch (Exception e) {
				map.put("ENDERECO", "Sem endereço cadastrado");
				map.put("END_NUMERO", "s/n");
	        	map.put("BAIRRO", "Sem bairro");
		        map.put("UF", "s/u");
		        map.put("CIDADE", "não cadastrado");
		        map.put("CEP", "00.000-000");
			}
	        
	        
	        map.put("APELIDO", vendaCabecalho.getCliente().getApelido());
	        
	        try {
	        	map.put("TEL", vendaCabecalho.getCliente().getTelefones().get(0).getTelefone());
			} catch (Exception e) {
				map.put("TEL", "Sem telefone");
			}	        
	        
	        map.put("ROTA", vendaCabecalho.getCliente().getRota().getNome());
	        //map.put("N_NOTA", vendaCabecalho.get);
	        map.put("COD_VENDA", vendaCabecalho.getId());
	        map.put("FORMA_PAGAMENTO", vendaCabecalho.getFormaPagamento().getNome());
	        map.put("DATA_HORA", vendaCabecalho.getDataVenda().toString());
	        map.put("VENDEDOR", vendaCabecalho.getUsuario().getNome());
	        
	        map.put("SUBREPORT_DIR", cabecalhoRecibo);
	        
	        JRTableModelDataSource src = new JRTableModelDataSource(table.getModel());

	        URL arquivo = getClass().getResource(endereco);
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);

	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, src);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }
	
}
