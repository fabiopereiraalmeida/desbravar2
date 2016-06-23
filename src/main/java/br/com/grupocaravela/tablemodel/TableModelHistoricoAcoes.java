package br.com.grupocaravela.tablemodel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.objeto.Historico;

public class TableModelHistoricoAcoes extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat formatDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private ArrayList<Historico> listaHistorico;
	// Titulo das colunas
	private String[] colunas = { "id", "Data", "Usuario", "Descrição" };

	// Construtor
	public TableModelHistoricoAcoes() {
		this.listaHistorico = new ArrayList<>();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addHistorico(Historico c) {
		this.listaHistorico.add(c);
		fireTableDataChanged();
	}

	public void removehistorico(int rowIndex) {
		this.listaHistorico.remove(rowIndex);
		fireTableDataChanged();
	}

	public Historico getHistorico(int rowIndex) {
		return this.listaHistorico.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaHistorico.size();
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
			return this.listaHistorico.get(rowIndex).getId();

		case 1:
			try {
				return formatDataHora.format(this.listaHistorico.get(rowIndex).getData());
			} catch (Exception e) {
				return "";
			}
			
		case 2:			
			try {
        		return this.listaHistorico.get(rowIndex).getUsuario().getNome();
			} catch (Exception e) {
				return "";
			}
			
			
		case 3:
			try {
				return this.listaHistorico.get(rowIndex).getDescricao();
			} catch (Exception e) {
				return "";
			}

		default:
			return this.listaHistorico.get(rowIndex); // Desta forma é retornado o
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
