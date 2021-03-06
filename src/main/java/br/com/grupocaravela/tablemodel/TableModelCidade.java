package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Cidade;

public class TableModelCidade extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Cidade> listaCidade;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome", "UF"};
    
    //Construtor
    public TableModelCidade(){
        this.listaCidade = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addCidade(Cidade c){
        this.listaCidade.add(c);
        fireTableDataChanged();
    }
    
    public void removecidade(int rowIndex){
        this.listaCidade.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Cidade getCidade(int rowIndex){
        return this.listaCidade.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaCidade.size();
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
                return this.listaCidade.get(rowIndex).getId();
            
            case 1:
                return this.listaCidade.get(rowIndex).getNome();
                
            case 2:
                return this.listaCidade.get(rowIndex).getUf();
                
            default:
                return this.listaCidade.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
