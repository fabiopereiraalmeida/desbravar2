package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Unidade;

public class RepositorioUnidade {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioUnidade(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Unidade unidade){
		em.getTransaction().begin();
		em.merge(unidade);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Unidade unidade){
		em.getTransaction().begin();
		em.remove(unidade);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Unidade c = em.find(Unidade.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Unidade buscarPorId(Long id){
		em.getTransaction().begin();
		Unidade c = em.find(Unidade.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Unidade> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Unidade");
		List<Unidade> listaUnidades = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaUnidades;
	}

	public List<Unidade> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select unidade from Unidade unidade where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Unidade where nome like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<Unidade> listaUnidades = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaUnidades;
	}
}
