package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Cliente;

public class TableModelCliente extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Cliente> listaCliente;
    //Titulo das colunas
    private String[] colunas = {"id", "Razão Social", "Fanatasia", "Apelido", "CNPJ"};
    
    //Construtor
    public TableModelCliente(){
        this.listaCliente = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addCliente(Cliente c){
        this.listaCliente.add(c);
        fireTableDataChanged();
    }
    
    public void removecliente(int rowIndex){
        this.listaCliente.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Cliente getCliente(int rowIndex){
        return this.listaCliente.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaCliente.size();
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
                return this.listaCliente.get(rowIndex).getId();
            
            case 1:
                return this.listaCliente.get(rowIndex).getRazaoSocial();
                
            case 2:
                return this.listaCliente.get(rowIndex).getFantasia();
                
            case 3:
                return this.listaCliente.get(rowIndex).getApelido();
                
            case 4:
                return this.listaCliente.get(rowIndex).getCnpj();
                
            default:
                return this.listaCliente.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
