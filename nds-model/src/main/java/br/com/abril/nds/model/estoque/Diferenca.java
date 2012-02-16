package br.com.abril.nds.model.estoque;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
public class Diferenca {

	@Id
	private Long id;
	private BigDecimal qtde;
	@ManyToOne
	private Usuario responsavel;
	@OneToOne
	private ItemRecebimentoFisico itemRecebimentoFisico;
	@ManyToOne
	private ProdutoEdicao produtoEdicao;
	@Enumerated(EnumType.STRING)
	private TipoDiferenca tipoDiferenca;
	@Enumerated(EnumType.STRING)
	private StatusConfirmacao statusConfirmacao;
	
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

}