package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.Fornecedor;

public class RepositorioFornecedor {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioFornecedor(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(Fornecedor fornecedor){
		em.getTransaction().begin();
		em.merge(fornecedor);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(Fornecedor fornecedor){
		em.getTransaction().begin();
		em.remove(fornecedor);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		Fornecedor c = em.find(Fornecedor.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public Fornecedor buscarPorId(Long id){
		em.getTransaction().begin();
		Fornecedor c = em.find(Fornecedor.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public List<Fornecedor> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from Fornecedor");
		List<Fornecedor> listaFornecedors = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaFornecedors;
	}

	public List<Fornecedor> listarPorRazaoSocial(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select fornecedor from Fornecedor fornecedor where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Fornecedor where razaoSocial like '%" + nome + "%'");
		List<Fornecedor> listaFornecedors = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaFornecedors;
	}
	
	public List<Fornecedor> listarPorFantasia(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select fornecedor from Fornecedor fornecedor where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from Fornecedor where fantasia like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<Fornecedor> listaFornecedors = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaFornecedors;
	}
}
