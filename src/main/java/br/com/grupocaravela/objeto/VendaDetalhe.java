package br.com.grupocaravela.objeto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "venda_detalhe")
public class VendaDetalhe implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private VendaCabecalho vendaCabecalho;
	private Produto produto;
	private String codigo;
	private Double quantidade;
	private Double valorParcial;
	private Double valorDesconto;
	private Double valorTotal;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "venda_cabecalho_id", nullable = false)
	public VendaCabecalho getVendaCabecalho() {
		return vendaCabecalho;
	}

	public void setVendaCabecalho(VendaCabecalho vendaCabecalho) {
		this.vendaCabecalho = vendaCabecalho;
	}

	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
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

	@Column(name = "valor_parcial", precision = 11, scale =3)
	public Double getValorParcial() {
		return valorParcial;
	}

	public void setValorParcial(Double valorParcial) {
		this.valorParcial = valorParcial;
	}

	@Column(name = "valor_desconto", precision = 11, scale =3)
	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	@Column(name = "valor_total", precision = 11, scale =3)
	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
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
		VendaDetalhe other = (VendaDetalhe) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
