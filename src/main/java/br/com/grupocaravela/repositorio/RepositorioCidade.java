package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Cidade;

public class RepositorioCidade {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioCidade(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Cidade cidade){
		em.getTransaction().begin();
		em.merge(cidade);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Cidade cidade){
		em.getTransaction().begin();
		em.remove(cidade);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Cidade c = em.find(Cidade.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Cidade buscarPorId(Long id){
		em.getTransaction().begin();
		Cidade c = em.find(Cidade.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Cidade> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Cidade");
		List<Cidade> listaCidades = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCidades;
	}

	public List<Cidade> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select cidade from Cidade cidade where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Cidade where nome like '%" + nome + "%'");
		List<Cidade> listaCidades = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCidades;
	}
}
