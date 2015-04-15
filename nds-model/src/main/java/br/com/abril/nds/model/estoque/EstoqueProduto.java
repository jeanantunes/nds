package br.com.abril.nds.model.estoque;

import java.io.Serializable;
import java.math.BigInteger;
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
public class EstoqueProduto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "ESTOQUE_PROD_SEQ")
	@Column(name = "ID")
	private Long id;

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
	
	@Column(name = "QTDE_DANIFICADO")
	private BigInteger qtdeDanificado;
	
	@Column(name = "QTDE_PERDA")
	private BigInteger qtdePerda;
	
	@Column(name = "QTDE_GANHO")
	private BigInteger qtdeGanho;
	
	@Column(name = "QTDE_JURAMENTADO")
    private BigInteger qtdeJuramentado;
	
	@OneToMany(mappedBy = "estoqueProduto")
	private List<MovimentoEstoque> movimentos = new ArrayList<MovimentoEstoque>();
	
	@Version
	@Column(name = "VERSAO")
	private Long versao = 0L;
	
	public EstoqueProduto() {
	}
	
	public EstoqueProduto(Long id) {
		this.id=id;
	}

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

	public BigInteger getQtdeDanificado() {
		return qtdeDanificado;
	}

	public void setQtdeDanificado(BigInteger qtdeDanificado) {
		this.qtdeDanificado = qtdeDanificado;
	}

	public BigInteger getQtdePerda() {
		return qtdePerda;
	}

	public void setQtdePerda(BigInteger qtdePerda) {
		this.qtdePerda = qtdePerda;
	}

	public BigInteger getQtdeGanho() {
		return qtdeGanho;
	}

	public void setQtdeGanho(BigInteger qtdeGanho) {
		this.qtdeGanho = qtdeGanho;
	}

	public BigInteger getQtdeJuramentado() {
        return qtdeJuramentado;
    }

    public void setQtdeJuramentado(BigInteger qtdeJuramentado) {
        this.qtdeJuramentado = qtdeJuramentado;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		EstoqueProduto other = (EstoqueProduto) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}
    
}
