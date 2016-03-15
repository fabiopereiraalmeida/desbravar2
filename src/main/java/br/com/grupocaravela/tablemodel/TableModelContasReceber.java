package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.ContaReceber;

public class TableModelContasReceber extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");

	private ArrayList<ContaReceber> listaContaReceber;
    //Titulo das colunas
    private String[] colunas = {"Id", "Razão Social", "Fantasia", "Vencimento", "Valor", "Vendedor"};
    
    //Construtor
    public TableModelContasReceber(){
        this.listaContaReceber = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addContaReceber(ContaReceber c){
        this.listaContaReceber.add(c);
        fireTableDataChanged();
    }
    
    public void removeContaReceber(int rowIndex){
        this.listaContaReceber.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public ContaReceber getContaReceber(int rowIndex){
        return this.listaContaReceber.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaContaReceber.size();
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
                return this.listaContaReceber.get(rowIndex).getId();
            
            case 1:
                return this.listaContaReceber.get(rowIndex).getCliente().getRazaoSocial();
                
            case 2:
                return this.listaContaReceber.get(rowIndex).getCliente().getFantasia();
            
            case 3:
            	try {
            		return formatData.format(this.listaContaReceber.get(rowIndex).getVencimento());
				} catch (Exception e) {
					return this.listaContaReceber.get(rowIndex).getVencimento();
				}
                
                
            case 4:
                return this.listaContaReceber.get(rowIndex).getValorDevido();
                
            case 5:
            	if(this.listaContaReceber.get(rowIndex).getVendaCabecalho() == null){
            		return "Não definido";
            	}else{
            		try {
                		return this.listaContaReceber.get(rowIndex).getVendaCabecalho().getUsuario().getNome();
    				} catch (Exception e) {
    					//return this.listaContaReceber.get(rowIndex).getVendaCabecalho().getUsuario().getNome();
    				}
            	}
            	
            default:
                return this.listaContaReceber.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
