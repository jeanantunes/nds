package br.com.abril.nds.model.estoque;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "DIFERENCA")
@SequenceGenerator(name="DIFERENCA_SEQ", initialValue = 1, allocationSize = 1)
public class Diferenca {

	@Id
	@GeneratedValue(generator = "DIFERENCA_SEQ")
	private Long id;
	
	@Column(name = "QTDE", nullable = false)
	private BigDecimal qtde;
	
	@ManyToOne(optional = true)
	private Usuario responsavel;
	
	@OneToOne(optional = true)
	private ItemRecebimentoFisico itemRecebimentoFisico;
	
	@ManyToOne(optional = false)
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DIFERENCA", nullable = false)
	private TipoDiferenca tipoDiferenca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_CONFIRMACAO", nullable = false)
	private StatusConfirmacao statusConfirmacao;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "MOVIMENTO_ESTOQUE_ID")
	private MovimentoEstoque movimentoEstoque;
	
	@Column(name = "AUTOMATICA")
	private Boolean automatica;
	
	@Transient
	private BigDecimal valorTotalDiferenca;
	
	public Diferenca() {
		
	}
	
	public Diferenca(Diferenca diferenca, BigDecimal valorTotalDiferenca) {
		
		this.id = diferenca.id;
		this.qtde = diferenca.qtde;
		this.responsavel = diferenca.responsavel;
		this.itemRecebimentoFisico = diferenca.itemRecebimentoFisico;
		this.produtoEdicao = diferenca.produtoEdicao;
		this.tipoDiferenca = diferenca.tipoDiferenca;
		this.statusConfirmacao = diferenca.statusConfirmacao;
		this.movimentoEstoque = diferenca.movimentoEstoque;
		this.automatica = diferenca.automatica;
		
		this.valorTotalDiferenca = valorTotalDiferenca;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigDecimal getQtde() {
		return qtde;
	}
	
	public void setQtde(BigDecimal qtde) {
		this.qtde = qtde;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	
	public ItemRecebimentoFisico getItemRecebimentoFisico() {
		return itemRecebimentoFisico;
	}
	
	public void setItemRecebimentoFisico(ItemRecebimentoFisico itemRecebimentoFisico) {
		this.itemRecebimentoFisico = itemRecebimentoFisico;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}
	
	public TipoDiferenca getTipoDiferenca() {
		return tipoDiferenca;
	}
	
	public void setTipoDiferenca(TipoDiferenca tipoDiferenca) {
		this.tipoDiferenca = tipoDiferenca;
	}

	public StatusConfirmacao getStatusConfirmacao() {
		return statusConfirmacao;
	}

	public void setStatusConfirmacao(StatusConfirmacao statusConfirmacao) {
		this.statusConfirmacao = statusConfirmacao;
	}

	public MovimentoEstoque getMovimentoEstoque() {
		return movimentoEstoque;
	}

	public void setMovimentoEstoque(MovimentoEstoque movimentoEstoque) {
		this.movimentoEstoque = movimentoEstoque;
	}

	public Boolean isAutomatica() {
		return automatica;
	}

	public void setAutomatica(Boolean automatica) {
		this.automatica = automatica;
	}

	public BigDecimal getValorTotalDiferenca() {
		return valorTotalDiferenca;
	}

	public void setValorTotalDiferenca(BigDecimal valorTotalDiferenca) {
		this.valorTotalDiferenca = valorTotalDiferenca;
	}

}