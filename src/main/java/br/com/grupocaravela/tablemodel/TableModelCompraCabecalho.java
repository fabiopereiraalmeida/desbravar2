package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.CompraCabecalho;

public class TableModelCompraCabecalho extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<CompraCabecalho> listaCompraCabecalho;
	// Titulo das colunas
	private String[] colunas = { "Id", "Nº Nota", "Data da Compra", "Fornecedor", "Usuario", "Valor", "Est. Compra" };

	// Construtor
	public TableModelCompraCabecalho() {
		this.listaCompraCabecalho = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addCompraCabecalho(CompraCabecalho c) {
		this.listaCompraCabecalho.add(c);
		fireTableDataChanged();
	}

	public void removeCompraCabecalho(int rowIndex) {
		this.listaCompraCabecalho.remove(rowIndex);
		fireTableDataChanged();
	}

	public CompraCabecalho getCompraCabecalho(int rowIndex) {
		return this.listaCompraCabecalho.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaCompraCabecalho.size();
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
			return this.listaCompraCabecalho.get(rowIndex).getId();

		case 1:
			return this.listaCompraCabecalho.get(rowIndex).getNumeroNota();
			
		case 2:
			return this.listaCompraCabecalho.get(rowIndex).getDataCompra();

		case 3:
			return this.listaCompraCabecalho.get(rowIndex).getFornecedor().getRazaoSocial();
			
		case 4:
			return this.listaCompraCabecalho.get(rowIndex).getUsuario().getNome();

		case 5:
			return this.listaCompraCabecalho.get(rowIndex).getValorCompra();
			
		case 6:
			return this.listaCompraCabecalho.get(rowIndex).getFinalizada();
			
		default:
			return this.listaCompraCabecalho.get(rowIndex); // Desta forma é retornado o
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
