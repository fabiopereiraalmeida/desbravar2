package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Categoria;

public class RepositorioCategoria {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioCategoria(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Categoria categoria){
		em.getTransaction().begin();
		em.merge(categoria);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Categoria categoria){
		em.getTransaction().begin();
		em.remove(categoria);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Categoria c = em.find(Categoria.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Categoria buscarPorId(Long id){
		em.getTransaction().begin();
		Categoria c = em.find(Categoria.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Categoria> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Categoria");
		List<Categoria> listaCategorias = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCategorias;
	}

	public List<Categoria> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select categoria from Categoria categoria where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Categoria where nome like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<Categoria> listaCategorias = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCategorias;
	}
}
