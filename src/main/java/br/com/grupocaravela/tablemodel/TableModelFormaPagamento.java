package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.FormaPagamento;

public class TableModelFormaPagamento extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<FormaPagamento> listaFormaPagamento;
    //Titulo das colunas
    private String[] colunas = {"Id", "Nome" , "N° Pacelas", "Nº Dia", "Valor Minimo"};
    
    //Construtor
    public TableModelFormaPagamento(){
        this.listaFormaPagamento = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addFormaPagamento(FormaPagamento c){
        this.listaFormaPagamento.add(c);
        fireTableDataChanged();
    }
    
    public void removeformaPagamento(int rowIndex){
        this.listaFormaPagamento.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public FormaPagamento getFormaPagamento(int rowIndex){
        return this.listaFormaPagamento.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaFormaPagamento.size();
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
                return this.listaFormaPagamento.get(rowIndex).getId();
                
            case 1:
                return this.listaFormaPagamento.get(rowIndex).getNome();
                
            case 2:
                return this.listaFormaPagamento.get(rowIndex).getNumeroParcelas();
                
            case 3:
                return this.listaFormaPagamento.get(rowIndex).getNumeroDias();
                
            case 4:
                return this.listaFormaPagamento.get(rowIndex).getValorMinimo();
             
            default:
                return this.listaFormaPagamento.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
