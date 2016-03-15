package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Caixa;

public class TableModelCaixa extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<Caixa> listaCaixa;
	// Titulo das colunas
	private String[] colunas = { "id", "Cod Venda", "Data", "Funcionario", "Valor" };

	// Construtor
	public TableModelCaixa() {
		this.listaCaixa = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addCaixa(Caixa c) {
		this.listaCaixa.add(c);
		fireTableDataChanged();
	}

	public void removecaixa(int rowIndex) {
		this.listaCaixa.remove(rowIndex);
		fireTableDataChanged();
	}

	public Caixa getCaixa(int rowIndex) {
		return this.listaCaixa.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaCaixa.size();
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
			return this.listaCaixa.get(rowIndex).getId();

		case 1:
			try {
				return this.listaCaixa.get(rowIndex).getVendaCabecalho().getId();
			} catch (Exception e) {
				return "";
			}
			
			
		case 2:			
			try {
        		return formatDataHora.format(this.listaCaixa.get(rowIndex).getData());
			} catch (Exception e) {
				return this.listaCaixa.get(rowIndex).getData();
			}
			
			
		case 3:
			try {
				return this.listaCaixa.get(rowIndex).getVendaCabecalho().getUsuario().getNome();
			} catch (Exception e) {
				return "";
			}
			
			
		case 4:
			return this.listaCaixa.get(rowIndex).getValor();

		default:
			return this.listaCaixa.get(rowIndex); // Desta forma é retornado o
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
