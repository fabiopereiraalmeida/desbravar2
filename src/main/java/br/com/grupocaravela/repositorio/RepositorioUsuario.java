package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Usuario;

public class RepositorioUsuario {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioUsuario(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Usuario usuario){
		em.getTransaction().begin();
		em.merge(usuario);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Usuario usuario){
		em.getTransaction().begin();
		em.remove(usuario);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Usuario c = em.find(Usuario.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Usuario buscarPorId(Long id){
		em.getTransaction().begin();
		Usuario c = em.find(Usuario.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Usuario> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Usuario");
		List<Usuario> listaUsuarios = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaUsuarios;
	}

	public List<Usuario> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select usuario from Usuario usuario where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Usuario where nome like '%" + nome + "%'");
		List<Usuario> listaUsuarios = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaUsuarios;
	}
}
