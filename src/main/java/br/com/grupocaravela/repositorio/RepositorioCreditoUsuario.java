package br.com.grupocaravela.repositorio;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.grupocaravela.objeto.CreditoUsuario;
import br.com.grupocaravela.objeto.Usuario;

public class RepositorioCreditoUsuario {
	
	EntityManagerFactory emf;
	EntityManager em;
	
	public RepositorioCreditoUsuario(){
		emf = Persistence.createEntityManagerFactory("DesbravarPU");
		em = emf.createEntityManager();
	}
	
	public void fecharConexao(){
		emf.close();
	}
	
	public void salvar(CreditoUsuario creditoUsuario){
		em.getTransaction().begin();
		em.merge(creditoUsuario);
		em.getTransaction().commit();
		
		
	}
	
	public void remover(CreditoUsuario creditoUsuario){
		em.getTransaction().begin();
		em.remove(creditoUsuario);
		em.getTransaction().commit();
		
	}
	
	public void removerPorId(Long id){
		em.getTransaction().begin();
		CreditoUsuario c = em.find(CreditoUsuario.class, id);
		em.remove(c);
		em.getTransaction().commit();
		
	}
	
	public CreditoUsuario buscarPorId(Long id){
		em.getTransaction().begin();
		CreditoUsuario c = em.find(CreditoUsuario.class, id);
		em.getTransaction().commit();
		
		return c;
	}
	
	public CreditoUsuario buscarPorUsuario(Usuario usuario){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from CreditoUsuario where usuario_id like '%" + usuario + "%'");
		List<CreditoUsuario> listaCreditoUsuarios = consulta.getResultList();
		CreditoUsuario cu = listaCreditoUsuarios.get(0);
		em.getTransaction().commit();
		
		return cu;
	}
	
	public List<CreditoUsuario> listarTodos(){
		em.getTransaction().begin();
		Query consulta = em.createQuery("from CreditoUsuario");
		List<CreditoUsuario> listaCreditoUsuarios = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCreditoUsuarios;
	}

	public List<CreditoUsuario> listarPorNome(String nome){
		em.getTransaction().begin();
		//Query consulta = em.createQuery("select creditoUsuario from CreditoUsuario creditoUsuario where nome like '%" + nome + "%'");
		Query consulta = em.createQuery("from CreditoUsuario where nome like '%" + nome + "%'");
		//sessao.createQuery("from Cliente where " + chaveBusca + " like  '%" + jtfLocalizar.getText() + "%' order by razaoSocialCliente").list();
		List<CreditoUsuario> listaCreditoUsuarios = consulta.getResultList();
		em.getTransaction().commit();
		
		return listaCreditoUsuarios;
	}
}
