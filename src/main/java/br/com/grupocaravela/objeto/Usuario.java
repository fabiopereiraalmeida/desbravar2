package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private String usuario;
	private String senha;
	private List<TelefoneCliente> telefones = new ArrayList<>();
	private Boolean ativo;
	private Date ultimoAcesso;
	private List<Rota> rotas = new ArrayList<>();
	private CreditoUsuario creditoUsuario;
	private Cargo cargo;
	
	private String endereco;
	private String enderecoNumero;
	private String complemento;
	private Cidade cidade;
	private String bairro;
	private String uf;
	private String cep;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false, length = 80)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(nullable = false, length = 30)
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	@Column(nullable = false, length = 30)
	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@OneToMany
	public List<TelefoneCliente> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<TelefoneCliente> telefones) {
		this.telefones = telefones;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ultimo_acesso")
	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}
	/*
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<UsuarioRota> getUsuarioRota() {
		return UsuarioRota;
	}
	*/
	
	@ManyToMany(mappedBy = "usuarios")
	public List<Rota> getRota() {
		return rotas;
	}
	
	public void setRota(List<Rota> Rota) {
		Rota = Rota;
	}

	@OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval =true)	
	public CreditoUsuario getCreditoUsuario() {
		return creditoUsuario;
	}

	public void setCreditoUsuario(CreditoUsuario creditoUsuario) {
		this.creditoUsuario = creditoUsuario;
	}
	
	@OneToOne
	@JoinColumn(name = "cargo_id")
	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	@Column(length = 80)
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Column(name = "endereco_numero", length = 6)
	public String getEnderecoNumero() {
		return enderecoNumero;
	}

	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}

	@Column(length = 30)
	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@ManyToOne
	@JoinColumn(name = "cidade_id")
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	@Column(length = 50)
	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	@Column(length = 2)
	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	@Column(length = 20)
	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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
		Usuario other = (Usuario) obj;
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
