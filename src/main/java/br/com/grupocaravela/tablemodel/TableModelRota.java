package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Rota;

public class TableModelRota extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Rota> listaRota;
	// Titulo das colunas
	private String[] colunas = { "Id", "Nome" };

	// Construtor
	public TableModelRota() {
		this.listaRota = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addRota(Rota c) {
		this.listaRota.add(c);
		fireTableDataChanged();
	}

	public void removerota(int rowIndex) {
		this.listaRota.remove(rowIndex);
		fireTableDataChanged();
	}

	public Rota getRota(int rowIndex) {
		return this.listaRota.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaRota.size();
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
			return this.listaRota.get(rowIndex).getId();

		case 1:
			return this.listaRota.get(rowIndex).getNome();

		default:
			return this.listaRota.get(rowIndex); // Desta forma é retornado o
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
