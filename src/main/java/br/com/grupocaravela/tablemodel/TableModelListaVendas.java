package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.VendaDetalhe;

public class TableModelListaVendas extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<VendaDetalhe> listaVendaDetalhe;
	// Titulo das colunas
	private String[] colunas = { "Codigo", "Qtd", "Un", "Nome", "Valor", "Valor parcial", "Desconto", "Total"};

	// Construtor
	public TableModelListaVendas() {
		this.listaVendaDetalhe = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addVendaDetalhe(VendaDetalhe c) {
		this.listaVendaDetalhe.add(c);
		fireTableDataChanged();
	}

	public void removevendaDetalhe(int rowIndex) {
		this.listaVendaDetalhe.remove(rowIndex);
		fireTableDataChanged();
	}

	public VendaDetalhe getVendaDetalhe(int rowIndex) {
		return this.listaVendaDetalhe.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaVendaDetalhe.size();
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
			return this.listaVendaDetalhe.get(rowIndex).getProduto().getCodigo();

		case 1:
			return this.listaVendaDetalhe.get(rowIndex).getQuantidade();
						
		case 2:
			return this.listaVendaDetalhe.get(rowIndex).getProduto().getUnidade().getNome();
			
		case 3:
			return this.listaVendaDetalhe.get(rowIndex).getProduto().getNome();
			
		case 4:
			return this.listaVendaDetalhe.get(rowIndex).getProduto().getValorDesejavelVenda();
			
		case 5:
			return this.listaVendaDetalhe.get(rowIndex).getValorParcial();
			
		case 6:
			return this.listaVendaDetalhe.get(rowIndex).getValorDesconto();
			
		case 7:
			return this.listaVendaDetalhe.get(rowIndex).getValorTotal();

		default:
			return this.listaVendaDetalhe.get(rowIndex); // Desta forma é retornado
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
