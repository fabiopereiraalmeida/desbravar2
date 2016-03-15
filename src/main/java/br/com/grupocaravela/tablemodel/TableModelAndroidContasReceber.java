package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.AndroidContaReceber;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Usuario;
import br.com.grupocaravela.objeto.VendaCabecalho;

public class TableModelAndroidContasReceber extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private EntityManager manager;
	private EntityTransaction trx;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<AndroidContaReceber> listaAndroidContaReceber;
    //Titulo das colunas
    private String[] colunas = {"Cliente", "Vendedor", "Recebedor", "Data transmissão", "Valor recebido", "Estado"};
    
    //Construtor
    public TableModelAndroidContasReceber(){
        this.listaAndroidContaReceber = new ArrayList<>();
        iniciaConexao();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addAndroidContaReceber(AndroidContaReceber c){
        this.listaAndroidContaReceber.add(c);
        fireTableDataChanged();
    }
    
    public void removeAndroidContaReceber(int rowIndex){
        this.listaAndroidContaReceber.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public AndroidContaReceber getAndroidContaReceber(int rowIndex){
        return this.listaAndroidContaReceber.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaAndroidContaReceber.size();
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                //return this.listaAndroidContaReceber.get(rowIndex).getId();
            	return buscarRazaoSocialCliente(this.listaAndroidContaReceber.get(rowIndex).getCliente());
            	
            case 1:
                //return this.listaAndroidContaReceber.get(rowIndex).getId();
            	//return buscarRazaoSocialCliente(this.listaAndroidContaReceber.get(rowIndex).getCliente());
            	VendaCabecalho vc = buscarVendaCabecalho(this.listaAndroidContaReceber.get(rowIndex).getVendaCabecalho());
            	try {
            		return buscarNomeUsuario(vc.getUsuario().getId());
				} catch (Exception e) {
					return "Não encontrado";
				}
            	
            case 2:
                return buscarNomeUsuario(this.listaAndroidContaReceber.get(rowIndex).getUsuario());
            	
            	
            case 3:
            	try {
            		return formatData.format(this.listaAndroidContaReceber.get(rowIndex).getDataTransmissao());
				} catch (Exception e) {
					return "Sem data";
				}
                
            case 4:
                return this.listaAndroidContaReceber.get(rowIndex).getValorDevido();
                
            case 5:
            	if (this.listaAndroidContaReceber.get(rowIndex).getAtivo() == true) {
            		return "Pendente";
				} else{
					return "Aprovado";
				}            	                       
           
            default:
                return this.listaAndroidContaReceber.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
    
    private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
				manager = EntityManagerProducer.createEntityManager();
				trx = manager.getTransaction();
	}
    
    private String buscarRazaoSocialCliente(Long id) {
    	String retorno = null;
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Cliente where id like '" + id + "'" );
			List<Cliente> listaClientes = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaClientes.size(); i++) {
				Cliente c = listaClientes.get(i);
				retorno = c.getRazaoSocial();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar a razão social clientes: " + e);
			retorno = null;
		}
		
		return retorno;
	}
    
    private VendaCabecalho buscarVendaCabecalho(Long id) {
    	VendaCabecalho retorno = null;
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from VendaCabecalho where id like '" + id + "'" );
			List<VendaCabecalho> listaVendaCabecalho = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaVendaCabecalho.size(); i++) {
				VendaCabecalho vc2 = listaVendaCabecalho.get(i);
				retorno = vc2;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar o nome do usuario: " + e);
			retorno = null;
		}
		
		return retorno;
	}
    
    private String buscarNomeUsuario(Long id) {
    	String retorno = null;
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Usuario where id like '" + id + "'" );
			List<Usuario> listaUsuario = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaUsuario.size(); i++) {
				Usuario u = listaUsuario.get(i);
				retorno = u.getNome();
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar o nome do usuario: " + e);
			retorno = null;
		}
		
		return retorno;
	}
}
