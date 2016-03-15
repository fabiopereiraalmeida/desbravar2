package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cargo")
public class Cargo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String observacao;
	private boolean acessoConfiguracao;
	private boolean acessoVendas;
	private boolean acessoProdutos;
	private boolean acessoClientes;
	private boolean acessoCompras;
	private boolean acessoRelatorios;
	private boolean acessoContasReceber;
	private boolean acessoContasPagar;
	private boolean acessoAndroid;
	private boolean acessoRotas;
	private boolean acessoCidades;
	private boolean acessoCategorias;
	private boolean acessoUnidade;
	private boolean acessoFormaPagamento;
	private boolean acessoFornecedores;
	private boolean acessoUsuarios;
	private List<Usuario> usuarioList = new ArrayList<>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 30)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(columnDefinition = "text")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public boolean isAcessoConfiguracao() {
		return acessoConfiguracao;
	}

	public void setAcessoConfiguracao(boolean acessoConfiguracao) {
		this.acessoConfiguracao = acessoConfiguracao;
	}

	public boolean isAcessoVendas() {
		return acessoVendas;
	}

	public void setAcessoVendas(boolean acessoVendas) {
		this.acessoVendas = acessoVendas;
	}

	public boolean isAcessoProdutos() {
		return acessoProdutos;
	}

	public void setAcessoProdutos(boolean acessoProdutos) {
		this.acessoProdutos = acessoProdutos;
	}

	public boolean isAcessoClientes() {
		return acessoClientes;
	}

	public void setAcessoClientes(boolean acessoClientes) {
		this.acessoClientes = acessoClientes;
	}

	public boolean isAcessoCompras() {
		return acessoCompras;
	}

	public void setAcessoCompras(boolean acessoCompras) {
		this.acessoCompras = acessoCompras;
	}

	public boolean isAcessoRelatorios() {
		return acessoRelatorios;
	}

	public void setAcessoRelatorios(boolean acessoRelatorios) {
		this.acessoRelatorios = acessoRelatorios;
	}

	public boolean isAcessoContasReceber() {
		return acessoContasReceber;
	}

	public void setAcessoContasReceber(boolean acessoContasReceber) {
		this.acessoContasReceber = acessoContasReceber;
	}

	public boolean isAcessoContasPagar() {
		return acessoContasPagar;
	}

	public void setAcessoContasPagar(boolean acessoContasPagar) {
		this.acessoContasPagar = acessoContasPagar;
	}

	public boolean isAcessoAndroid() {
		return acessoAndroid;
	}

	public void setAcessoAndroid(boolean acessoAndroid) {
		this.acessoAndroid = acessoAndroid;
	}

	public boolean isAcessoRotas() {
		return acessoRotas;
	}

	public void setAcessoRotas(boolean acessoRotas) {
		this.acessoRotas = acessoRotas;
	}

	public boolean isAcessoCidades() {
		return acessoCidades;
	}

	public void setAcessoCidades(boolean acessoCidades) {
		this.acessoCidades = acessoCidades;
	}

	public boolean isAcessoCategorias() {
		return acessoCategorias;
	}

	public void setAcessoCategorias(boolean acessoCategorias) {
		this.acessoCategorias = acessoCategorias;
	}

	public boolean isAcessoUnidade() {
		return acessoUnidade;
	}

	public void setAcessoUnidade(boolean acessoUnidade) {
		this.acessoUnidade = acessoUnidade;
	}

	public boolean isAcessoFormaPagamento() {
		return acessoFormaPagamento;
	}

	public void setAcessoFormaPagamento(boolean acessoFormaPagamento) {
		this.acessoFormaPagamento = acessoFormaPagamento;
	}

	public boolean isAcessoFornecedores() {
		return acessoFornecedores;
	}

	public void setAcessoFornecedores(boolean acessoFornecedores) {
		this.acessoFornecedores = acessoFornecedores;
	}

	public boolean isAcessoUsuarios() {
		return acessoUsuarios;
	}

	public void setAcessoUsuarios(boolean acessoUsuarios) {
		this.acessoUsuarios = acessoUsuarios;
	}
	
	@OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<Usuario> getUsuarioList() {
		return usuarioList;
	}

	public void setUsuarioList(List<Usuario> usuarioList) {
		this.usuarioList = usuarioList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cargo other = (Cargo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String toString(){
        return this.nome;
    }
}
