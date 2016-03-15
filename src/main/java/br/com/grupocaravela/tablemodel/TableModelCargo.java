package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Cargo;

public class TableModelCargo extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Cargo> listaCargo;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome"};
    
    //Construtor
    public TableModelCargo(){
        this.listaCargo = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addCargo(Cargo c){
        this.listaCargo.add(c);
        fireTableDataChanged();
    }
    
    public void removecargo(int rowIndex){
        this.listaCargo.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Cargo getCargo(int rowIndex){
        return this.listaCargo.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaCargo.size();
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
                return this.listaCargo.get(rowIndex).getId();
            
            case 1:
                return this.listaCargo.get(rowIndex).getNome();
                                
            default:
                return this.listaCargo.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
