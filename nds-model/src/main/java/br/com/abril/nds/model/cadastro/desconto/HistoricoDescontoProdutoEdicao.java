package br.com.abril.nds.model.cadastro.desconto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "HISTORICO_DESCONTO_PRODUTO_EDICAO")
@SequenceGenerator(name="HISTORICO_DESCONTO_PRODUTO_EDICAO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoDescontoProdutoEdicao implements Serializable {

	private static final long serialVersionUID = -7746843614617906694L;

	@Id
	@GeneratedValue(generator = "HISTORICO_DESCONTO_PRODUTO_EDICAO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "DESCONTO")
	private BigDecimal desconto;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="DATA_ALTERACAO")
	private Date dataAlteracao;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "DISTRIBUIDOR_ID")
	private Distribuidor distribuidor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "FORNECEDOR_ID")
	private Fornecedor fornecedor;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_ID")
	private Produto produto;

	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the desconto
	 */
	public BigDecimal getDesconto() {
		return desconto;
	}

	/**
	 * @param desconto the desconto to set
	 */
	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	/**
	 * @return the dataAlteracao
	 */
	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	/**
	 * @param dataAlteracao the dataAlteracao to set
	 */
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the distribuidor
	 */
	public Distribuidor getDistribuidor() {
		return distribuidor;
	}

	/**
	 * @param distribuidor the distribuidor to set
	 */
	public void setDistribuidor(Distribuidor distribuidor) {
		this.distribuidor = distribuidor;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	/**
	 * @return the produtoEdicao
	 */
	public Produto getProduto() {
		return produto;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
}
