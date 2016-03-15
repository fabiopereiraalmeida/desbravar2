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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "credito_usuario")
public class CreditoUsuario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	private Double valor;
	private Usuario usuario;
	private List<CreditoUsuarioDetalhes> creditoUsuarioDetalhes = new ArrayList<>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(precision = 11, scale = 2, nullable = false)
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@OneToOne
	@JoinColumn(name = "usuario_id")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	@OneToMany(mappedBy = "creditoUsuarioCabecalho", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CreditoUsuarioDetalhes> getCreditoUsuarioDetalhes() {
		return creditoUsuarioDetalhes;
	}

	public void setCreditoUsuarioDetalhes(List<CreditoUsuarioDetalhes> creditoUsuarioDetalhes) {
		this.creditoUsuarioDetalhes = creditoUsuarioDetalhes;
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
		CreditoUsuario other = (CreditoUsuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
