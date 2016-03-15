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
import br.com.grupocaravela.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.FormaPagamento;

public class TableModelAndroidVendaCabecalho extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private EntityManager manager;
	private EntityTransaction trx;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<AndroidVendaCabecalho> listaAndroidVendaCabecalho;
    //Titulo das colunas
    private String[] colunas = {"Id", "Cliente", "Valor", "Data Venda", "Forma Pagamento"};
    
    //Construtor
    public TableModelAndroidVendaCabecalho(){
        this.listaAndroidVendaCabecalho = new ArrayList<>();
        iniciaConexao();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addAndroidVendaCabecalho(AndroidVendaCabecalho c){
        this.listaAndroidVendaCabecalho.add(c);
        fireTableDataChanged();
    }
    
    public void removeAndroidVendaCabecalho(int rowIndex){
        this.listaAndroidVendaCabecalho.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public AndroidVendaCabecalho getAndroidVendaCabecalho(int rowIndex){
        return this.listaAndroidVendaCabecalho.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaAndroidVendaCabecalho.size();
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
                return this.listaAndroidVendaCabecalho.get(rowIndex).getId();
            
            case 1:
                //return this.listaAndroidVendaCabecalho.get(rowIndex).getCliente().getRazaoSocial();
                return buscarRazaoSocial(this.listaAndroidVendaCabecalho.get(rowIndex).getCliente());
            	
            case 2:
                return this.listaAndroidVendaCabecalho.get(rowIndex).getValorTotal();
                            
            case 3:
            	
            	//return "";
            	
            	try {
            		return formatDataHora.format(this.listaAndroidVendaCabecalho.get(rowIndex).getDataVenda());
				} catch (Exception e) {
					return this.listaAndroidVendaCabecalho.get(rowIndex).getDataVenda();
				}
                                           
            case 4:
                //return this.listaAndroidVendaCabecalho.get(rowIndex).getFormaPagamento().getNome();
            	return buscarFormaPagamento(this.listaAndroidVendaCabecalho.get(rowIndex).getFormaPagamento());
            /*    
            case 6:
                return this.listaAndroidVendaCabecalho.get(rowIndex).getCliente().getRota().getNome();
              */                  
            default:
                return this.listaAndroidVendaCabecalho.get(rowIndex); //Desta forma é retornado o objeto inteiro
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
    
    private String buscarRazaoSocial(Long id) {
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
    
    private String buscarFormaPagamento(Long id) {
    	String retorno = null;
		//try {
			//trx.begin();
			Query consulta = manager.createQuery("from FormaPagamento where id like '" + id + "'" );
			List<FormaPagamento> listaFormaPagamento = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaFormaPagamento.size(); i++) {
				FormaPagamento f = listaFormaPagamento.get(i);
				retorno = f.getNome();
			}
		//} catch (Exception e) {
		//	JOptionPane.showMessageDialog(null, "Erro ao buscar o nome da forma de pagamento: " + e);
		//	retorno = null;
		//}
		
		return retorno;
	}
}
