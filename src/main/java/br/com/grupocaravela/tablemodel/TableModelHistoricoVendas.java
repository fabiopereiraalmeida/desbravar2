package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.VendaCabecalho;
import br.com.grupocaravela.objeto.VendaCabecalho;

public class TableModelHistoricoVendas extends AbstractTableModel{

	private static final long serialVersionUID = 1L;

	private ArrayList<VendaCabecalho> listaVendaCabecalho;
    //Titulo das colunas
    private String[] colunas = {"Id", "Cliente", "Vendedor", "Data", "Valor"};
    private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
    //Construtor
    public TableModelHistoricoVendas(){
        this.listaVendaCabecalho = new ArrayList<>();
    }
    
    //############### Inicio dos Metodos do TableModel ###################
    public void addVendaCabecalho(VendaCabecalho c){
        this.listaVendaCabecalho.add(c);
        fireTableDataChanged();
    }
    
    public void removeVendaCabecalho(int rowIndex){
        this.listaVendaCabecalho.remove(rowIndex);
        fireTableDataChanged();
    }
    
    public VendaCabecalho getVendaCabecalho(int rowIndex){
        return this.listaVendaCabecalho.get(rowIndex); 
    }
    
    //############### Fim dos Metodos do TableModel ###################

    @Override
    public int getRowCount() {
        return this.listaVendaCabecalho.size();
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
                return this.listaVendaCabecalho.get(rowIndex).getId();
            
            case 1:
                return this.listaVendaCabecalho.get(rowIndex).getCliente().getRazaoSocial();
                
            case 2:
                return this.listaVendaCabecalho.get(rowIndex).getUsuario().getNome();

            case 3:
            	try {
            		return formatData.format(this.listaVendaCabecalho.get(rowIndex).getDataVenda());
				} catch (Exception e) {
					return this.listaVendaCabecalho.get(rowIndex).getDataVenda();
				}
                
            case 4:
                return this.listaVendaCabecalho.get(rowIndex).getValorTotal();
                
            default:
                return this.listaVendaCabecalho.get(rowIndex); //Desta forma é retornado o objeto inteiro
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getColumnName(int columnIndex){
        return this.colunas[columnIndex];
    }
}
