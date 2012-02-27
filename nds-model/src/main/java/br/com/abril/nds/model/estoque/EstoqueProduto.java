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
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;

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
	@OneToMany(mappedBy = "estoqueProduto")
	List<MovimentoEstoque> movimentos = new ArrayList<MovimentoEstoque>();
	@Version
	@Column(name = "VERSAO")
	private Long versao;
	
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

}
