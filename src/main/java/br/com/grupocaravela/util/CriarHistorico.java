package br.com.grupocaravela.util;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.grupocaravela.configuracao.EntityManagerProducer;
import br.com.grupocaravela.objeto.Historico;
import br.com.grupocaravela.objeto.Usuario;

public final class CriarHistorico {

	private static EntityManager manager;
	private static EntityTransaction trx;
	
	//Usuario usuario;
	//String descricao;
	
	/*
	public CriarHistorico(Usuario usuario, String descricao) {
		super();
		this.usuario = usuario;
		this.descricao = descricao;
	}
	*/
	public static void criar(Usuario usuario, String descricao, Date data){
		
		Historico historico = new Historico();
		
		historico.setUsuario(usuario);
		historico.setDescricao(descricao);
		historico.setData(data);
		
		iniciaConexao();
		
		trx.begin();
		manager.persist(historico);
		trx.commit();
		
		manager.close();
		
	}
	
	private static void iniciaConexao() {

		manager = EntityManagerProducer.createEntityManager();
		trx = manager.getTransaction();
	}
	
}
