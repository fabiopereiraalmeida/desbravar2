package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Rota;

public class RepositorioRota {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioRota(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Rota rota){
		em.getTransaction().begin();
		em.merge(rota);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Rota rota){
		em.getTransaction().begin();
		em.remove(rota);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Rota c = em.find(Rota.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Rota buscarPorId(Long id){
		em.getTransaction().begin();
		Rota c = em.find(Rota.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Rota> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Rota");
		List<Rota> listaRotas = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaRotas;
	}

	public List<Rota> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select rota from Rota rota where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Rota where nome like '%" + nome + "%'");
		List<Rota> listaRotas = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaRotas;
	}
}
