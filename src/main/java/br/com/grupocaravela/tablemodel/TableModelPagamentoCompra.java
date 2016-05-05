package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.ContaPagar;

public class TableModelPagamentoCompra extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<ContaPagar> listaContaPagar;
	// Titulo das colunas
	private String[] colunas = { "Valor", "Vencimento", "Observação"};

	// Construtor
	public TableModelPagamentoCompra() {
		this.listaContaPagar = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addContaPagar(ContaPagar c) {
		this.listaContaPagar.add(c);
		fireTableDataChanged();
	}

	public void removeContaPagar(int rowIndex) {
		this.listaContaPagar.remove(rowIndex);
		fireTableDataChanged();
	}

	public ContaPagar getContaPagar(int rowIndex) {
		return this.listaContaPagar.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaContaPagar.size();
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {

		case 0:
			try {
				return this.listaContaPagar.get(rowIndex).getValor();
			} catch (Exception e) {
				return "";
			}
			

		case 1:
			try {
				return formatData.format(this.listaContaPagar.get(rowIndex).getDataVencimento());
			} catch (Exception e) {
				return "";
			}
			
			
		case 2:			
			try {
        		return this.listaContaPagar.get(rowIndex).getObservacao();
			} catch (Exception e) {
				return "";
			}
						
		default:
			return this.listaContaPagar.get(rowIndex); // Desta forma é retornado o
													// objeto inteiro
		}
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
}
