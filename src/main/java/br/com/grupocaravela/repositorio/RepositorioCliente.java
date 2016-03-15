package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Cliente;

public class RepositorioCliente {
	
	public EntityManagerFactory emf;
	public EntityManager em;
	
	public RepositorioCliente(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Cliente cliente){
		em.getTransaction().begin();
		em.merge(cliente);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Cliente cliente){
		em.getTransaction().begin();
		em.remove(cliente);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Cliente c = em.find(Cliente.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Cliente buscarPorId(Long id){
		em.getTransaction().begin();
		Cliente c = em.find(Cliente.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Cliente> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Cliente");
		List<Cliente> listaClientes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaClientes;
	}

	public List<Cliente> listarPorRazaoSocial(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select cliente from Cliente cliente where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Cliente where razaoSocial like '%" + nome + "%'");
		List<Cliente> listaClientes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaClientes;
	}
	

	public List<Cliente> listarPorFantasia(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select cliente from Cliente cliente where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Cliente where fantasia like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<Cliente> listaClientes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaClientes;
	}


	public List<Cliente> listarPorApelido(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select cliente from Cliente cliente where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Cliente where apelido like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<Cliente> listaClientes = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaClientes;
	}

}

