package br.com.abril.nds.model.cadastro.desconto;

import java.io.Serializable;
import java.math.BigDecimal;

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
import javax.persistence.UniqueConstraint;

import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "DESCONTO_COTA_PRODUTO_EXCESSOES",
	   uniqueConstraints= {@UniqueConstraint(columnNames = {"FORNECEDOR_ID", "COTA_ID", "PRODUTO_ID", "PRODUTO_EDICAO_ID"})})
@SequenceGenerator(name="DESCONTO_COTA_PRODUTO_EXC_SEQ", initialValue = 1, allocationSize = 1)
public class DescontoCotaProdutoExcessao implements Serializable {

	private static final long serialVersionUID = -6962370571993126767L;

	@Id
	@GeneratedValue(generator = "DESCONTO_COTA_PRODUTO_EXC_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCONTO", nullable=false, precision=5, scale=2)
	private BigDecimal desconto;

	@ManyToOne(optional = true)
	@JoinColumn(name = "DISTRIBUIDOR_ID", referencedColumnName="ID")
	private Distribuidor distribuidor;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PRODUTO_ID")
	private Produto produto;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DESCONTO", nullable = false)
	private TipoDesconto tipoDesconto;
	
	@Column(name = "DESCONTO_PREDOMINANTE", nullable = false)
	private boolean descontoPredominante;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public TipoDesconto getTipoDesconto() {
		return tipoDesconto;
	}

	public void setTipoDesconto(TipoDesconto tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public boolean isDescontoPredominante() {
		return descontoPredominante;
	}

	public void setDescontoPredominante(boolean descontoPredominante) {
		this.descontoPredominante = descontoPredominante;
	}

}
