package br.com.abril.nds.model.estoque;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ESTOQUE_PRODUTO")
@SequenceGenerator(name="ESTOQUE_PROD_SEQ", initialValue = 1, allocationSize = 1)
public class EstoqueProduto {
	
	@Id
	@GeneratedValue(generator = "ESTOQUE_PROD_SEQ")
	@Column(name = "ID")
	private Long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;
	
	@Column(name = "QTDE_SUPLEMENTAR")
	private BigDecimal qtdeSuplementar;
	
	@Column(name = "QTDE_DEVOLUCAO_ENCALHE")
	private BigDecimal qtdeDevolucaoEncalhe;
	
	@Column(name = "QTDE_DEVOLUCAO_FORNECEDOR")
	private BigDecimal qtdeDevolucaoFornecedor;
	
	@OneToMany(mappedBy = "estoqueProduto")
	private List<MovimentoEstoque> movimentos = new ArrayList<MovimentoEstoque>();
	
	@Version
	@Column(name = "VERSAO")
	private Long versao = 0L;
	
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
	
	public BigDecimal getQtde() {
		return qtde;
	}
	
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
	public BigDecimal getQtdeSuplementar() {
		return qtdeSuplementar;
	}
	
	public void setQtdeSuplementar(BigDecimal qtdeSuplementar) {
		this.qtdeSuplementar = qtdeSuplementar;
	}
	
	public List<MovimentoEstoque> getMovimentos() {
		return movimentos;
	}
	
	public void setMovimentos(List<MovimentoEstoque> movimentos) {
		this.movimentos = movimentos;
	}
	
	public Long getVersao() {
		return versao;
	}
	
	public void setVersao(Long versao) {
		this.versao = versao;
	}

	/**
	 * @return the qtdeDevolucaoEncalhe
	 */
	public BigDecimal getQtdeDevolucaoEncalhe() {
		return qtdeDevolucaoEncalhe;
	}

	/**
	 * @param qtdeDevolucaoEncalhe the qtdeDevolucaoEncalhe to set
	 */
	public void setQtdeDevolucaoEncalhe(BigDecimal qtdeDevolucaoEncalhe) {
		this.qtdeDevolucaoEncalhe = qtdeDevolucaoEncalhe;
	}

	/**
	 * @return the qtdeDevolucaoFornecedor
	 */
	public BigDecimal getQtdeDevolucaoFornecedor() {
		return qtdeDevolucaoFornecedor;
	}

	/**
	 * @param qtdeDevolucaoFornecedor the qtdeDevolucaoFornecedor to set
	 */
	public void setQtdeDevolucaoFornecedor(BigDecimal qtdeDevolucaoFornecedor) {
		this.qtdeDevolucaoFornecedor = qtdeDevolucaoFornecedor;
	}

}
