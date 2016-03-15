package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Fornecedor;

public class TableModelFornecedor extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Fornecedor> listaFornecedor;
    //Titulo das colunas
    private String[] colunas = {"Id", "Razão Social", "Fantasia", "CNPJ", "Email"};
    
    //Construtor
    public TableModelFornecedor(){
        this.listaFornecedor = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addFornecedor(Fornecedor c){
        this.listaFornecedor.add(c);
        fireTableDataChanged();
    }
    
    public void removefornecedor(int rowIndex){
        this.listaFornecedor.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Fornecedor getFornecedor(int rowIndex){
        return this.listaFornecedor.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaFornecedor.size();
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
                return this.listaFornecedor.get(rowIndex).getId();
                
            case 1:
                return this.listaFornecedor.get(rowIndex).getRazaoSocial();
                
            case 2:
                return this.listaFornecedor.get(rowIndex).getFantasia();
                
            case 3:
                return this.listaFornecedor.get(rowIndex).getCnpj();
                
            case 4:
                return this.listaFornecedor.get(rowIndex).getEmail();
                
            default:
                return this.listaFornecedor.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
