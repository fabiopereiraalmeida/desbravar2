package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.CompraDetalhe;

public class TableModelCompraDetalhe extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<CompraDetalhe> listaCompraDetalhe;
	// Titulo das colunas
	private String[] colunas = { "Cod", "Nome", "Un", "Qtd", "Valor Unit.", "Valor Tot." };

	// Construtor
	public TableModelCompraDetalhe() {
		this.listaCompraDetalhe = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addCompraDetalhe(CompraDetalhe c) {
		this.listaCompraDetalhe.add(c);
		fireTableDataChanged();
	}

	public void removeCompraDetalhe(int rowIndex) {
		this.listaCompraDetalhe.remove(rowIndex);
		fireTableDataChanged();
	}

	public CompraDetalhe getCompraDetalhe(int rowIndex) {
		return this.listaCompraDetalhe.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaCompraDetalhe.size();
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
			return this.listaCompraDetalhe.get(rowIndex).getProduto().getCodigo();

		case 1:
			return this.listaCompraDetalhe.get(rowIndex).getProduto().getNome();
			
		case 2:
			return this.listaCompraDetalhe.get(rowIndex).getProduto().getUnidade().getNome();
			
		case 3:
			try {
				return this.listaCompraDetalhe.get(rowIndex).getProdutoLote().getQuantidade();
			} catch (Exception e) {
				return "";
			}			

		case 4:
			try {
				return this.listaCompraDetalhe.get(rowIndex).getProdutoLote().getValorCompra();
			} catch (Exception e) {
				return "";
			}			
			
		case 5:
			return this.listaCompraDetalhe.get(rowIndex).getValorTotal();
			
		default:
			return this.listaCompraDetalhe.get(rowIndex); // Desta forma é retornado o
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
