package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.FormaPagamento;

public class RepositorioFormaPagamento {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioFormaPagamento(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(FormaPagamento formaPagamento){
		em.getTransaction().begin();
		em.merge(formaPagamento);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(FormaPagamento formaPagamento){
		em.getTransaction().begin();
		em.remove(formaPagamento);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		FormaPagamento c = em.find(FormaPagamento.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public FormaPagamento buscarPorId(Long id){
		em.getTransaction().begin();
		FormaPagamento c = em.find(FormaPagamento.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<FormaPagamento> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from FormaPagamento");
		List<FormaPagamento> listaFormaPagamentos = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaFormaPagamentos;
	}

	public List<FormaPagamento> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select formaPagamento from FormaPagamento formaPagamento where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from FormaPagamento where nome like '%" + nome + "%'");
		List<FormaPagamento> listaFormaPagamentos = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaFormaPagamentos;
	}
}
