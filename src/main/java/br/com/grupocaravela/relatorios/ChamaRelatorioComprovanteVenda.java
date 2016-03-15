package br.com.grupocaravela.relatorios;

import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import org.hibernate.loader.custom.Return;

import br.com.grupocaravela.configuracao.Empresa;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.EnderecoCliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Rota;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.util.ConectaBanco;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ChamaRelatorioComprovanteVenda {

	private String sistema = System.getProperty("os.name");
	private SimpleDateFormat formatBra = new SimpleDateFormat("dd/MM/yyyy");

	//método
	    public void report(Usuario usuario, Cliente cliente, EnderecoCliente enderecoCliente, String formaPagamento, String dataVenda, String Vencimento, String codVenda, Double valorParcial, Double desconto, Double valorTotal) throws JRException {
	    	
	        Connection conn = ConectaBanco.getConnection();

	        JasperReport jasper;

	        Map map = new HashMap<>();

	        URL arquivo = getClass().getResource("ComprovanteVenda.jasper");
	        jasper = (JasperReport) JRLoader.loadObject(arquivo);
	        
	        map.put("IP_SERVIDOR", Empresa.getIpServidor());
	        
	        map.put("EMPRESA_FRASE", Empresa.getFrase());
	        map.put("EMPRESA_CNPJ", "CNPJ: " + Empresa.getCnpj());
	        map.put("EMPRESA_ENDERECO", Empresa.getEndereco() + ", Nº " + Empresa.getEnderecoNumero() + " - " + Empresa.getBairro() 
	        + " - " + Empresa.getCidade() + " - CEP: " + Empresa.getCep() );
	        map.put("EMPRESA_TELEFONE", "Tel.: " + Empresa.getTelefonePrincipal() + " / " + Empresa.getTelefoneSecundario());
	        
	       if (usuario != null) {
	    	   map.put("VENDEDOR", usuario.getNome());
		}	        	
			
	       if (enderecoCliente != null) {
	    	   	try {
	    		   	map.put("ENDERECO", enderecoCliente.getEndereco());
	    	   	} catch (Exception e) {
	    		   	map.put("ENDERECO", "");
	    	   	}
	    	   	try {
	    		   	map.put("ENDERECO_NUMERO", enderecoCliente.getNumero());
	    	   	} catch (Exception e) {
	    		   	map.put("ENDERECO_NUMERO", "");
	    	   	}
	    	    try {
	    	    	map.put("BAIRRO", enderecoCliente.getBairro());
				} catch (Exception e) {
					map.put("BAIRRO", "");
				}
	    	    try {
	    	    	map.put("UF", enderecoCliente.getUf());
				} catch (Exception e) {
					map.put("UF", "");
				}
	    	    try {
	    	    	map.put("CIDADE", enderecoCliente.getCidade().getNome());
				} catch (Exception e) {
					map.put("CIDADE", "");
				}
	    	    try {
	    	    	map.put("CEP", enderecoCliente.getCep());
				} catch (Exception e) {
					map.put("CEP", "");
				}
	    	    
	       	}
	        	
	        	if (cliente != null) {
	        		map.put("NOME_CLIENTE", cliente.getRazaoSocial());
		        	try {
		        		map.put("FANTASIA_CLIENTE", cliente.getFantasia());
					} catch (Exception e) {
						map.put("FANTASIA_CLIENTE", "");
					}
		        	
		        	try {
		        		map.put("ROTA", cliente.getRota().getNome());
					} catch (Exception e) {
						map.put("ROTA", "");
					}
		        	
		        	try {
		        		map.put("TELEFONE_CLIENTE", cliente.getTelefones().get(0).toString());
					} catch (Exception e) {
						map.put("TELEFONE_CLIENTE", "");
					}
				}        	
	        	
	        	map.put("COD_VENDA", codVenda);
					        		        
	        	map.put("FORMA_PAGAMENTO", formaPagamento);
	        	map.put("DATA_VENDA", dataVenda);
	        	map.put("VENCIMENTO", Vencimento);
	        	
	        	map.put("VALOR_PARCIAL", valorParcial);
	        	map.put("DESCONTO", desconto);
	        	map.put("VALOR_TOTAL", valorTotal);
			
	        	        	        
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, map, conn);

	        JasperViewer jv = new JasperViewer(jasperPrint, false);
	        jv.setVisible(true);

	    }

	    /*
	    private String calcularDatasVencimentos(FormaPagamento f){
			
	    	String retorno = null;
			
			int qtdDias = f.getNumeroDias();

			GregorianCalendar gcGregorian = new GregorianCalendar();
			gcGregorian.setTime(vendaCabecalho.getDataVenda());

			try {

				for (int i = 0; i < f.getNumeroParcelas(); i++) {
					//ContaReceber contaReceber = new ContaReceber();

					gcGregorian.set(GregorianCalendar.DAY_OF_MONTH,
							gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

					retorno = formatBra.format(gcGregorian.getTime());					
					
				}

			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Erro ao informar os vencimentos!");
			}
			
			return retorno;
		}
		*/
	
}
