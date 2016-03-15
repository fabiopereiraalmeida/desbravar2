package br.com.grupocaravela.objeto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "compra_detalhe")
public class CompraDetalhe implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Produto produto;
	private Double quantidade;
	private Double desconto;
	private Double valorTotal;
	private CompraCabecalho compraCabecalho;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@OneToOne
	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Column(length = 11, scale = 3)
	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	@Column(length = 11, scale = 3)
	public Double getDesconto() {
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	@Column(name = "valor_total", precision = 11, scale = 3)
	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	@ManyToOne
	@JoinColumn(name = "compra_cabecalho_id", nullable = false)
	public CompraCabecalho getCompraCabecalho() {
		return compraCabecalho;
	}

	public void setCompraCabecalho(CompraCabecalho compraCabecalho) {
		this.compraCabecalho = compraCabecalho;
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
		CompraDetalhe other = (CompraDetalhe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
