package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Unidade;

public class TableModelUnidade extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Unidade> listaUnidade;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome"};
    
    //Construtor
    public TableModelUnidade(){
        this.listaUnidade = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addUnidade(Unidade c){
        this.listaUnidade.add(c);
        fireTableDataChanged();
    }
    
    public void removeunidade(int rowIndex){
        this.listaUnidade.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Unidade getUnidade(int rowIndex){
        return this.listaUnidade.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaUnidade.size();
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
                return this.listaUnidade.get(rowIndex).getId();
                
            case 1:
                return this.listaUnidade.get(rowIndex).getNome();
             
            default:
                return this.listaUnidade.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
