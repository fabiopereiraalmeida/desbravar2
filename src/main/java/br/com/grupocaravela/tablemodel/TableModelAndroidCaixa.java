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
import br.com.grupocaravela.objeto.AndroidCaixa;
import br.com.grupocaravela.objeto.Cliente;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Usuario;

public class TableModelAndroidCaixa extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private EntityManager manager;
	private EntityTransaction trx;

	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<AndroidCaixa> listaAndroidCaixa;
    //Titulo das colunas
    private String[] colunas = {"Vendedor", "Cliente", "Valor", "Data", "Estado"};
    
    //Construtor
    public TableModelAndroidCaixa(){
        this.listaAndroidCaixa = new ArrayList<>();
        iniciaConexao();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addAndroidCaixa(AndroidCaixa c){
        this.listaAndroidCaixa.add(c);
        fireTableDataChanged();
    }
    
    public void removeAndroidCaixa(int rowIndex){
        this.listaAndroidCaixa.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public AndroidCaixa getAndroidCaixa(int rowIndex){
        return this.listaAndroidCaixa.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaAndroidCaixa.size();
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
                //return this.listaAndroidCaixa.get(rowIndex).getId();
            	return buscarNomeUsuario(this.listaAndroidCaixa.get(rowIndex).getUsuario());
            case 1:
                //return this.listaAndroidCaixa.get(rowIndex).getCliente().getRazaoSocial();
                return buscarRazaoSocialCliente(this.listaAndroidCaixa.get(rowIndex).getCliente());
            	
            case 2:
                return this.listaAndroidCaixa.get(rowIndex).getValor();
                            
            case 3:
            	
            	//return "";
            	
            	try {
            		return formatDataHora.format(this.listaAndroidCaixa.get(rowIndex).getDataRecebimento());
				} catch (Exception e) {
					return "Sem data";
				}
            	
            case 4:
            	
            	if (this.listaAndroidCaixa.get(rowIndex).getAtivo() == true) {
					return "Pendente";
				}else{
					return "Aprovado";
				}                                         
           
            default:
                return this.listaAndroidCaixa.get(rowIndex); //Desta forma é retornado o objeto inteiro
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
