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
import javax.persistence.Table;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String razaoSocial;
	private String fantasia;
	private String cnpj;
	private String cpf;
	private String email;
	private String observacao;
	private List<TelefoneFornecedor> telefonesFornecedor = new ArrayList<>();
	private List<EnderecoFornecedor> enderecosFornecedor = new ArrayList<>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "razao_social", length = 100, nullable = false)
	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	
	@Column(length = 60 , nullable = false)
	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

	@Column( length = 20)
	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	
	@Column( length = 20)
	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Column(length = 150)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(columnDefinition = "text")
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<TelefoneFornecedor> getTelefones() {
		return telefonesFornecedor;
	}

	public void setTelefones(List<TelefoneFornecedor> telefones) {
		this.telefonesFornecedor = telefones;
	}

	@OneToMany(mappedBy = "fornecedor", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<EnderecoFornecedor> getEnderecos() {
		return enderecosFornecedor;
	}

	public void setEnderecos(List<EnderecoFornecedor> enderecos) {
		this.enderecosFornecedor = enderecos;
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
		Fornecedor other = (Fornecedor) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
