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
@Table(name = "produtoLote")
public class ProdutoLote implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String lote;
	private Date fabricacao;
	private Date validade;
	private Double quantidade;
	private Double valorCompra;
	private Produto Produto;
	private List<CompraDetalhe> compraDetalhe = new ArrayList<>();
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "lote", length = 25, nullable = false)
	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "fabricacao")
	public Date getFabricacao() {
		return fabricacao;
	}

	public void setFabricacao(Date fabricacao) {
		this.fabricacao = fabricacao;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "validade")
	public Date getValidade() {
		return validade;
	}

	public void setValidade(Date validade) {
		this.validade = validade;
	}

	@Column(name = "quantidade", precision = 11, scale = 3, nullable = false)
	public Double getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	@Column(name = "valorCompra", precision = 11, scale = 3, nullable = false)
	public Double getValorCompra() {
		return valorCompra;
	}

	public void setValorCompra(Double valorCompra) {
		this.valorCompra = valorCompra;
	}
	
	@ManyToOne
	@JoinColumn(name = "produto_id", nullable = false)
	public Produto getProduto() {
		return Produto;
	}

	public void setProduto(Produto produto) {
		Produto = produto;
	}
	
	@OneToMany(mappedBy = "produtoLote", cascade = CascadeType.ALL, orphanRemoval = true)
	public List<CompraDetalhe> getCompraDetalhe() {
		return compraDetalhe;
	}

	public void setCompraDetalhe(List<CompraDetalhe> compraDetalhe) {
		this.compraDetalhe = compraDetalhe;
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
		ProdutoLote other = (ProdutoLote) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


}
