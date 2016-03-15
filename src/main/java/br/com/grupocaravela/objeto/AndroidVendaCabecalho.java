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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "android_venda_cabecalho")
public class AndroidVendaCabecalho implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long cliente;
	private Double valorParcial;
	private Double valorDesconto;
	private Double valorTotal;
	private Date dataVenda;
	private Date dataTransmissao;
	private Long usuario;
	private Long formaPagamento;
	private Boolean vendaAprovada;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "cliente_id", nullable = false)
	public Long getCliente() {
		return cliente;
	}

	public void setCliente(Long cliente) {
		this.cliente = cliente;
	}

	@Column(name = "valor_parcial", precision = 11, scale = 2)
	public Double getValorParcial() {
		return valorParcial;
	}

	public void setValorParcial(Double valorParcial) {
		this.valorParcial = valorParcial;
	}

	@Column(name = "valor_desconto", precision = 11, scale = 2)
	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "valor_total", precision = 11, scale = 2)
	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_venda")
	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
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

	@Column(name = "forma_pagamento_id")
	public Long getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(Long formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@Column(name = "venda_aprovada")
	public Boolean getVendaAprovada() {
		return vendaAprovada;
	}

	public void setVendaAprovada(Boolean vendaAprovada) {
		this.vendaAprovada = vendaAprovada;
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
		AndroidVendaCabecalho other = (AndroidVendaCabecalho) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
