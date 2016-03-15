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
@Table(name = "produto")
public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String codigo;
	private String nome;
	private Double valorCusto;
	private Double valorMinimoVenda;
	private Double valorDesejavelVenda;
	private Double margemLucroMinimo;
	private Double margemLucroDesejavel;
	private Double quantidadeEstoque;
	private Double quantidadeMinimaEstoque;
	private Double quantidadeDesejavelEstoque;
	private Double peso;
	private Unidade unidade;
	private Categoria categoria;
	private Boolean ativo;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 20, nullable = false)
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(length = 100, nullable = false)
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Column(name = "valor_custo", precision = 11, scale = 3, nullable = false)
	public Double getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(Double valorCusto) {
		this.valorCusto = valorCusto;
	}

	@Column(name = "valor_minimo_venda", precision = 11, scale = 2, nullable = false)
	public Double getValorMinimoVenda() {
		return valorMinimoVenda;
	}

	public void setValorMinimoVenda(Double valorMinimoVenda) {
		this.valorMinimoVenda = valorMinimoVenda;
	}

	@Column(name = "valor_desejavel_venda", precision = 11, scale = 2, nullable = false)
	public Double getValorDesejavelVenda() {
		return valorDesejavelVenda;
	}

	public void setValorDesejavelVenda(Double valorDesejavelVenda) {
		this.valorDesejavelVenda = valorDesejavelVenda;
	}

	@Column(name = "margem_lucro_minimo", precision = 11, scale = 3)
	public Double getMargemLucroMinimo() {
		return margemLucroMinimo;
	}

	public void setMargemLucroMinimo(Double margemLucroMinimo) {
		this.margemLucroMinimo = margemLucroMinimo;
	}

	@Column(name = "margem_lucro_desejavel", precision = 11, scale = 3)
	public Double getMargemLucroDesejavel() {
		return margemLucroDesejavel;
	}

	public void setMargemLucroDesejavel(Double margemLucroDesejavel) {
		this.margemLucroDesejavel = margemLucroDesejavel;
	}

	@Column(name = "quantidade_estoque", precision = 11, scale = 3, nullable = false)
	public Double getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Double quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	@Column(name = "quantidade_minima_estoque", precision = 11, scale = 3)
	public Double getQuantidadeMinimaEstoque() {
		return quantidadeMinimaEstoque;
	}

	public void setQuantidadeMinimaEstoque(Double quantidadeMinimaEstoque) {
		this.quantidadeMinimaEstoque = quantidadeMinimaEstoque;
	}

	@Column(name = "quantidade_desejavel_estoque", precision = 11, scale = 3)
	public Double getQuantidadeDesejavelEstoque() {
		return quantidadeDesejavelEstoque;
	}

	public void setQuantidadeDesejavelEstoque(Double quantidadeDesejavelEstoque) {
		this.quantidadeDesejavelEstoque = quantidadeDesejavelEstoque;
	}
	
	@Column(precision = 11, scale = 3)
	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@ManyToOne
	@JoinColumn(name = "unidade_id", nullable = false)
	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	@ManyToOne
	@JoinColumn(name = "categoria_id", nullable = false)
	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
