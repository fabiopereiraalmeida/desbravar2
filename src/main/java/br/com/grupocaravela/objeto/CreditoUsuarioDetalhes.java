package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "credito_usuario_detalhes")
public class CreditoUsuarioDetalhes implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Double valor;
	private Date data;
	private VendaCabecalho vendaCabecalho;
	private CreditoUsuario creditoUsuarioCabecalho;

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

	@Temporal(TemporalType.TIMESTAMP)
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	@ManyToOne
	@JoinColumn(name = "venda_cabecalho")
	public VendaCabecalho getVendaCabecalho() {
		return vendaCabecalho;
	}

	public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
		this.vendaCabecalho = vendaCabecalho;
	}

	@ManyToOne
	@JoinColumn(name = "credito_usuario_cabecalho_id")
	public CreditoUsuario getCreditoUsuarioCabecalho() {
		return creditoUsuarioCabecalho;
	}

	public void setCreditoUsuarioCabecalho(CreditoUsuario creditoUsuarioCabecalho) {
		this.creditoUsuarioCabecalho = creditoUsuarioCabecalho;
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
		CreditoUsuarioDetalhes other = (CreditoUsuarioDetalhes) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
