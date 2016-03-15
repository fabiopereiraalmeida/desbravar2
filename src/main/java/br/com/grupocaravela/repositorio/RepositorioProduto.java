package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Produto;

public class RepositorioProduto {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	
	public RepositorioProduto(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Produto produto){
		em.getTransaction().begin();
		em.merge(produto);
		em.getTransaction().commit();
				
	}
	
	public void remover(Produto produto){
		em.getTransaction().begin();
		em.remove(produto);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Produto c = em.find(Produto.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Produto buscarPorId(Long id){
		em.getTransaction().begin();
		Produto c = em.find(Produto.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public Produto buscarPorCodigo(String cod){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Produto where codigo like '" + cod + "'");
		Produto pro = (Produto) consulta.getResultList();
		em.getTransaction().commit();
		
		return pro;
	}
	
	public List<Produto> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Produto");
		List<Produto> listaProdutos = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaProdutos;
	}

	public List<Produto> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select produto from Produto produto where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Produto where nome like '%" + nome + "%'");
		List<Produto> listaProdutos = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaProdutos;
	}
	
	public List<Produto> listarPorCodigo(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select produto from Produto produto where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Produto where codigo like '%" + nome + "%'");
		List<Produto> listaProdutos = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaProdutos;
	}
}
