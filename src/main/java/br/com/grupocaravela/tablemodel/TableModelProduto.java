package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Produto;

public class TableModelProduto extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<Produto> listaProduto;
    //Titulo das colunas
    private String[] colunas = {"Id", "Código", "Nome", "Valor Venda", "Estoque", "Unidade"};
    
    //Construtor
    public TableModelProduto(){
        this.listaProduto = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addProduto(Produto c){
        this.listaProduto.add(c);
        fireTableDataChanged();
    }
    
    public void removeproduto(int rowIndex){
        this.listaProduto.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public Produto getProduto(int rowIndex){
        return this.listaProduto.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaProduto.size();
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
                return this.listaProduto.get(rowIndex).getId();
                
            case 1:
                return this.listaProduto.get(rowIndex).getCodigo();
                
            case 2:
                return this.listaProduto.get(rowIndex).getNome();
                
            case 3:
                return this.listaProduto.get(rowIndex).getValorDesejavelVenda();
                
            case 4:
                return this.listaProduto.get(rowIndex).getQuantidadeEstoque();
                
            case 5:
                return this.listaProduto.get(rowIndex).getUnidade();
             
            default:
                return this.listaProduto.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
