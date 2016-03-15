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
@Table(name = "android_caixa")
public class AndroidCaixa implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Double valor;
	private Long androidVendaCabecalho;
	private Long vendaCabecalho;
	private Date dataRecebimento;
	private Date dataTransmissao;
	private Long usuario;
	private Long cliente;
	private Boolean ativo;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(precision = 11, scale = 2)
	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	@Column(name = "android_venda_cabecalho_id")
	public Long getAndroidVendaCabecalho() {
		return androidVendaCabecalho;
	}

	public void setAndroidVendaCabecalho(Long androidVendaCabecalho) {
		this.androidVendaCabecalho = androidVendaCabecalho;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_recebimento")
	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_transmissao")
	public Date getDataTransmissao() {
		return dataTransmissao;
	}

	public void setDataTransmissao(Date dataTransmissao) {
		this.dataTransmissao = dataTransmissao;
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

	@Column(name = "cliente_id")
	public Long getCliente() {
		return cliente;
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}
	
	@Column(name = "ativo")
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
		AndroidCaixa other = (AndroidCaixa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
