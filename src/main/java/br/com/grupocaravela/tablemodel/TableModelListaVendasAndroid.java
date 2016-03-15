package br.com.grupocaravela.tablemodel;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.AndroidVendaDetalhe;
import br.com.grupocaravela.objeto.FormaPagamento;
import br.com.grupocaravela.objeto.Produto;

public class TableModelListaVendasAndroid extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<AndroidVendaDetalhe> listaAndroidVendaDetalhe;
	// Titulo das colunas
	private String[] colunas = { "Codigo", "Nome", "Valor", "Qtd", "Valor parcial", "Desconto", "Total"};

	private EntityManager manager;
	private EntityTransaction trx;
	
	// Construtor
	public TableModelListaVendasAndroid() {
		this.listaAndroidVendaDetalhe = new ArrayList<>();
		iniciaConexao();
	}

	// ############### Inicio dos Metodos do TableModel ###################
	public void addAndroidVendaDetalhe(AndroidVendaDetalhe c) {
		this.listaAndroidVendaDetalhe.add(c);
		fireTableDataChanged();
	}

	public void removeAndroidVendaDetalhe(int rowIndex) {
		this.listaAndroidVendaDetalhe.remove(rowIndex);
		fireTableDataChanged();
	}

	public AndroidVendaDetalhe getAndroidVendaDetalhe(int rowIndex) {
		return this.listaAndroidVendaDetalhe.get(rowIndex);
	}

	// ############### Fim dos Metodos do TableModel ###################

	@Override
	public int getRowCount() {
		return this.listaAndroidVendaDetalhe.size();
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
		
		Produto p = buscarProduto(this.getAndroidVendaDetalhe(rowIndex).getProduto());
		
		switch (columnIndex) {
		case 0:
			//return this.listaAndroidVendaDetalhe.get(rowIndex).getProduto().getCodigo();
			
			//return buscarProduto(this.getAndroidVendaDetalhe(rowIndex).getProduto()).getCodigo();
			
			return p.getCodigo();

		case 1:
			//return this.listaAndroidVendaDetalhe.get(rowIndex).getProduto().getNome();
			//return buscarProduto(this.getAndroidVendaDetalhe(rowIndex).getProduto()).getNome();
			return p .getNome();
			
		case 2:
			//return this.listaAndroidVendaDetalhe.get(rowIndex).getProduto().getValorDesejavelVenda();
			//return buscarProduto(this.getAndroidVendaDetalhe(rowIndex).getProduto()).getValorDesejavelVenda();
			return p .getValorDesejavelVenda();
			
		case 3:
			return this.listaAndroidVendaDetalhe.get(rowIndex).getQuantidade();
			
		case 4:
			return this.listaAndroidVendaDetalhe.get(rowIndex).getValorParcial();
			
		case 5:
			return this.listaAndroidVendaDetalhe.get(rowIndex).getValorDesconto();
			
		case 6:
			return this.listaAndroidVendaDetalhe.get(rowIndex).getValorTotal();

		default:
			return this.listaAndroidVendaDetalhe.get(rowIndex); // Desta forma é retornado
														// o objeto inteiro
		}
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getColumnName(int columnIndex) {
		return this.colunas[columnIndex];
	}
	
	private void iniciaConexao() {

		// factory = Persistence.createEntityManagerFactory("DesbravarPU");
				manager = EntityManagerProducer.createEntityManager();
				trx = manager.getTransaction();
	}
	
	private Produto buscarProduto(Long id) {
    	Produto retorno = null;
		try {
			//trx.begin();
			Query consulta = manager.createQuery("from Produto where id like '" + id + "'" );
			List<Produto> listaProduto = consulta.getResultList();
			//trx.commit();

			for (int i = 0; i < listaProduto.size(); i++) {
				Produto p = listaProduto.get(i);
				retorno = p;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao buscar o produto: " + e);
			retorno = null;
		}
		
		return retorno;
	}
}
