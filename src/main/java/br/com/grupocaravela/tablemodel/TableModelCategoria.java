package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Categoria;

public class TableModelCategoria extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<Categoria> listaCategoria;
	// Titulo das colunas
	private String[] colunas = { "Id", "Nome" };

	// Construtor
	public TableModelCategoria() {
		this.listaCategoria = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addCategoria(Categoria c) {
		this.listaCategoria.add(c);
		fireTableDataChanged();
	}

	public void removecategoria(int rowIndex) {
		this.listaCategoria.remove(rowIndex);
		fireTableDataChanged();
	}

	public Categoria getCategoria(int rowIndex) {
		return this.listaCategoria.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaCategoria.size();
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
			return this.listaCategoria.get(rowIndex).getId();

		case 1:
			return this.listaCategoria.get(rowIndex).getNome();

		default:
			return this.listaCategoria.get(rowIndex); // Desta forma é retornado
														// o objeto inteiro
		}
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
}
