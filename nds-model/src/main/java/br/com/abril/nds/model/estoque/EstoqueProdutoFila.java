package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ESTOQUE_PRODUTO_FILA")
@SequenceGenerator(name="ESTOQUE_PROD_FILA_SEQ", initialValue = 1, allocationSize = 1)
public class EstoqueProdutoFila implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ESTOQUE_PROD_FILA_SEQ")
	@Column(name = "ID")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ESTOQUE", nullable = false)
	private TipoEstoque tipoEstoque;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OPERACAO_ESTOQUE", nullable = false)
	private OperacaoEstoque operacaoEstoque;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	public OperacaoEstoque getOperacaoEstoque() {
		return operacaoEstoque;
	}

	public void setOperacaoEstoque(OperacaoEstoque operacaoEstoque) {
		this.operacaoEstoque = operacaoEstoque;
	}

	@Override
	public String toString() {
		return "EstoqueProdutoFila [id=" + id + ", produtoEdicao="
				+ (produtoEdicao != null ? produtoEdicao.getId():"null") + ", cota=" + (cota!= null ? cota.getId():"null" )+ ", qtde=" + qtde
				+ ", tipoEstoque=" +(tipoEstoque != null ? tipoEstoque.getDescricao():"null") + ", operacaoEstoque="
				+ (operacaoEstoque!= null ? operacaoEstoque.toString():"null") + "]";
	}
	
}

