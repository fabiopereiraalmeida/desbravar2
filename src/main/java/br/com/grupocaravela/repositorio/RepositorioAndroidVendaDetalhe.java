package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.AndroidVendaDetalhe;

public class RepositorioAndroidVendaDetalhe {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioAndroidVendaDetalhe(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(AndroidVendaDetalhe androidVendaDetalhe){
		em.getTransaction().begin();
		em.merge(androidVendaDetalhe);
		em.getTransaction().commit();		
		
	}
	
	public void remover(AndroidVendaDetalhe androidVendaDetalhe){
		em.getTransaction().begin();
		em.remove(androidVendaDetalhe);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		AndroidVendaDetalhe c = em.find(AndroidVendaDetalhe.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public AndroidVendaDetalhe buscarPorId(Long id){
		em.getTransaction().begin();
		AndroidVendaDetalhe c = em.find(AndroidVendaDetalhe.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<AndroidVendaDetalhe> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from AndroidVendaDetalhe");
		List<AndroidVendaDetalhe> listaAndroidVendaDetalhes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaAndroidVendaDetalhes;
	}

	public List<AndroidVendaDetalhe> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select androidVendaDetalhe from AndroidVendaDetalhe androidVendaDetalhe where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from AndroidVendaDetalhe where nome like '%" + nome + "%'");
		List<AndroidVendaDetalhe> listaAndroidVendaDetalhes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaAndroidVendaDetalhes;
	}
}
