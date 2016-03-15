package br.com.grupocaravela.objeto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "android_conta_receber")
public class AndroidContaReceber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long cliente;
	private Long usuario;
	private Long vendaCabecalho;
	private Double valorDevido;
	private Date dataTransmissao;
	private Date dataRecebimento;
	private Boolean ativo;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "cliente_id")
	public Long getCliente() {
		return cliente;
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}
	
	@Column(name = "usuario_id")
	public Long getUsuario() {
		return usuario;
	}

	public void setUsuario(Long usuario) {
		this.usuario = usuario;
	}

	@Column(name = "venda_cabecalho_id")
	public Long getVendaCabecalho() {
		return vendaCabecalho;
	}

	public void setVendaCabecalho(Long vendaCabecalho) {
		this.vendaCabecalho = vendaCabecalho;
	}

	@Column(name = "valor_devido", precision = 11, scale = 2)
	public Double getValorDevido() {
		return valorDevido;
	}

	public void setValorDevido(Double valorDevido) {
		this.valorDevido = valorDevido;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataTransmissao() {
		return dataTransmissao;
	}

	public void setDataTransmissao(Date dataTransmissao) {
		this.dataTransmissao = dataTransmissao;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}
	
	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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
		AndroidContaReceber other = (AndroidContaReceber) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
