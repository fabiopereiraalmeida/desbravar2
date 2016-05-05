package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.ContaPagar;

public class TableModelContaPagar extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<ContaPagar> listaContaPagar;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	
    //Titulo das colunas
    private String[] colunas = {"Id", "Descrição", "Valor", "Vencimento", "Estado"};
    
    //Construtor
    public TableModelContaPagar(){
        this.listaContaPagar = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addContaPagar(ContaPagar c){
        this.listaContaPagar.add(c);
        fireTableDataChanged();
    }
    
    public void removecontaPagar(int rowIndex){
        this.listaContaPagar.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public ContaPagar getContaPagar(int rowIndex){
        return this.listaContaPagar.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaContaPagar.size();
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
                return this.listaContaPagar.get(rowIndex).getId();
            
            case 1:
                return this.listaContaPagar.get(rowIndex).getDescricao();
                
            case 2:
                return this.listaContaPagar.get(rowIndex).getValor();
                
            case 3:
            	try {
					return formatData.format(this.listaContaPagar.get(rowIndex).getDataVencimento());
				} catch (Exception e) {
					return "Erro!";
				}                
                
            case 4:
            	if (this.listaContaPagar.get(rowIndex).getPago()) {
            		return "Pago";
				}else{
					return "Não pago";
				}
            	
            	
            default:
                return this.listaContaPagar.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
