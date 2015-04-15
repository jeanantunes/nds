package br.com.abril.nds.model.estoque;

import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "HISTORICO_ESTOQUE_PRODUTO")
@SequenceGenerator(name="HISTORICO_ESTOQUE_PROD_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoEstoqueProduto {
	
	
	public HistoricoEstoqueProduto(Date data, ProdutoEdicao produtoEdicao,
			BigInteger qtde, BigInteger qtdeSuplementar,
			BigInteger qtdeDevolucaoEncalhe,
			BigInteger qtdeDevolucaoFornecedor, BigInteger qtdeJuramentado,
			BigInteger qtdeDanificado) {
		super();
		this.data = data;
		this.produtoEdicao = produtoEdicao;
		this.qtde = qtde;
		this.qtdeSuplementar = qtdeSuplementar;
		this.qtdeDevolucaoEncalhe = qtdeDevolucaoEncalhe;
		this.qtdeDevolucaoFornecedor = qtdeDevolucaoFornecedor;
		this.qtdeJuramentado = qtdeJuramentado;
		this.qtdeDanificado = qtdeDanificado;
	}
	
	@Id
	@GeneratedValue(generator = "HISTORICO_ESTOQUE_PROD_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA", nullable = false)
	private Date data;

	@OneToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@Column(name = "QTDE_SUPLEMENTAR")
	private BigInteger qtdeSuplementar;
	
	@Column(name = "QTDE_DEVOLUCAO_ENCALHE")
	private BigInteger qtdeDevolucaoEncalhe;
	
	@Column(name = "QTDE_DEVOLUCAO_FORNECEDOR")
	private BigInteger qtdeDevolucaoFornecedor;
	
	@Column(name = "QTDE_JURAMENTADO")
	private BigInteger qtdeJuramentado;
	
	@Column(name = "QTDE_DANIFICADO")
	private BigInteger qtdeDanificado;
	
	@Version
	@Column(name = "VERSAO")
	private Long versao = 0L;
	
	public HistoricoEstoqueProduto() {
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public BigInteger getQtde() {
		return qtde;
	}
	
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	public BigInteger getQtdeSuplementar() {
		return qtdeSuplementar;
	}
	
	public void setQtdeSuplementar(BigInteger qtdeSuplementar) {
		this.qtdeSuplementar = qtdeSuplementar;
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
	public BigInteger getQtdeDevolucaoEncalhe() {
		return qtdeDevolucaoEncalhe;
	}

	/**
	 * @param qtdeDevolucaoEncalhe the qtdeDevolucaoEncalhe to set
	 */
	public void setQtdeDevolucaoEncalhe(BigInteger qtdeDevolucaoEncalhe) {
		this.qtdeDevolucaoEncalhe = qtdeDevolucaoEncalhe;
	}

	/**
	 * @return the qtdeDevolucaoFornecedor
	 */
	public BigInteger getQtdeDevolucaoFornecedor() {
		return qtdeDevolucaoFornecedor;
	}

	/**
	 * @param qtdeDevolucaoFornecedor the qtdeDevolucaoFornecedor to set
	 */
	public void setQtdeDevolucaoFornecedor(BigInteger qtdeDevolucaoFornecedor) {
		this.qtdeDevolucaoFornecedor = qtdeDevolucaoFornecedor;
	}

	public BigInteger getQtdeJuramentado() {
		return qtdeJuramentado;
	}

	public void setQtdeJuramentado(BigInteger qtdeJuramentado) {
		this.qtdeJuramentado = qtdeJuramentado;
	}

	public BigInteger getQtdeDanificado() {
		return qtdeDanificado;
	}

	public void setQtdeDanificado(BigInteger qtdeDanificado) {
		this.qtdeDanificado = qtdeDanificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getData() == null) ? 0 : this.getData().hashCode());
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getProdutoEdicao() == null) ? 0 : this.getProdutoEdicao().hashCode());
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
		HistoricoEstoqueProduto other = (HistoricoEstoqueProduto) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getData() == null) {
			if (other.getData() != null)
				return false;
		} else if (!this.getData().equals(other.getData()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}
}
