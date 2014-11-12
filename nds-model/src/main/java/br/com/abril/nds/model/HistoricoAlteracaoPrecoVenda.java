package br.com.abril.nds.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name="HISTORICO_ALTERACAO_PRECO_VENDA")
public class HistoricoAlteracaoPrecoVenda {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name = "DATA_CRIACAO", nullable = false)
	private Date dataCriacao;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Column(name = "VALOR_ANTIGO")
	private BigDecimal valorAntigo;
	
	@Column(name = "VALOR_ATUAL")
	private BigDecimal valorAtual;

	@OneToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}

	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}

	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}

	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setValorAntigo(BigDecimal valorAntigo) {
		this.valorAntigo = valorAntigo;
	}

	public BigDecimal getValorAtual() {
		return valorAtual;
	}

	public void setValorAtual(BigDecimal valorAtual) {
		this.valorAtual = valorAtual;
	}
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public BigDecimal getValorAntigo() {
		return valorAntigo;
	}
	
}