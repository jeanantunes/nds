package br.com.abril.nds.model.estoque;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
public class Diferenca implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1707768328291865384L;

	@Id
	@GeneratedValue(generator = "DIFERENCA_SEQ")
	private Long id;
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;
	
	@ManyToOne(optional = true)
	private Usuario responsavel;
	
	@OneToOne(optional = true, cascade = CascadeType.REFRESH)
	private ItemRecebimentoFisico itemRecebimentoFisico;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DIFERENCA", nullable = false)
	private TipoDiferenca tipoDiferenca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ESTOQUE", nullable = false)
	private TipoEstoque tipoEstoque;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_CONFIRMACAO", nullable = false)
	private StatusConfirmacao statusConfirmacao;
	
	@Column(name = "AUTOMATICA")
	private Boolean automatica = false;
	
	@Transient
	private BigDecimal valorTotalDiferenca;
	
	@OneToMany(mappedBy = "diferenca")
	private Collection<RateioDiferenca> rateios;
	
	@OneToOne
	@JoinColumn(name = "LANCAMENTO_DIFERENCA_ID")
	private LancamentoDiferenca lancamentoDiferenca;
	
	@Transient
	private boolean existemRateios;

	@Column(name="DATA_MOVIMENTACAO")
	@Temporal(TemporalType.DATE)
	private Date dataMovimento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DIRECIONAMENTO", nullable = true)
	private TipoDirecionamentoDiferenca tipoDirecionamento;
	
	public Diferenca() {
		
	}
	
	public Diferenca(boolean existemRateios, Diferenca diferenca, BigDecimal valorTotalDiferenca) {
		
		this.id = diferenca.id;
		this.qtde = diferenca.qtde;
		this.responsavel = diferenca.responsavel;
		this.itemRecebimentoFisico = diferenca.itemRecebimentoFisico;
		this.produtoEdicao = diferenca.produtoEdicao;
		this.tipoDiferenca = diferenca.tipoDiferenca;
		this.statusConfirmacao = diferenca.statusConfirmacao;
		this.automatica = diferenca.automatica;
		this.tipoEstoque = diferenca.tipoEstoque;
		this.lancamentoDiferenca = diferenca.lancamentoDiferenca;
		this.valorTotalDiferenca = valorTotalDiferenca;
		this.existemRateios = existemRateios;
		this.dataMovimento = diferenca.getDataMovimento();
		this.tipoDirecionamento = diferenca.getTipoDirecionamento();
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public BigInteger getQtde() {
		return qtde;
	}
	
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	public Usuario getResponsavel() {
		return responsavel;
	}
	
	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}
	
	/**
	 * @return the tipoDirecionamento
	 */
	public TipoDirecionamentoDiferenca getTipoDirecionamento() {
		return tipoDirecionamento;
	}

	/**
	 * @param tipoDirecionamento the tipoDirecionamento to set
	 */
	public void setTipoDirecionamento(TipoDirecionamentoDiferenca tipoDirecionamento) {
		this.tipoDirecionamento = tipoDirecionamento;
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
	
	public Collection<RateioDiferenca> getRateios() {
		return rateios;
	}

	public void setRateios(Collection<RateioDiferenca> rateios) {
		this.rateios = rateios;
	}

	public LancamentoDiferenca getLancamentoDiferenca() {
		return lancamentoDiferenca;
	}

	public void setLancamentoDiferenca(LancamentoDiferenca lancamentoDiferenca) {
		this.lancamentoDiferenca = lancamentoDiferenca;
	}
	
	/**
	 * @return the dataMovimento
	 */
	public Date getDataMovimento() {
		return dataMovimento;
	}

	/**
	 * @param dataMovimento the dataMovimento to set
	 */
	public void setDataMovimento(Date dataMovimento) {
		this.dataMovimento = dataMovimento;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Diferenca other = (Diferenca) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * @return the tipoEstoque
	 */
	public TipoEstoque getTipoEstoque() {
		return tipoEstoque;
	}

	/**
	 * @param tipoEstoque the tipoEstoque to set
	 */
	public void setTipoEstoque(TipoEstoque tipoEstoque) {
		this.tipoEstoque = tipoEstoque;
	}

	/**
	 * @return the existemRateios
	 */
	public boolean isExistemRateios() {
		return existemRateios;
	}

	/**
	 * @param existemRateios the existemRateios to set
	 */
	public void setExistemRateios(boolean existemRateios) {
		this.existemRateios = existemRateios;
	}
	
	/**
	 * Calcula o valor total absoluto da diferença R$, não
	 * levando em considereação o desconto do
	 * produto
	 * @return total da diferença em R$
	 */
	public BigDecimal getValorTotal() {
	   BigDecimal preco = produtoEdicao.getPrecoVenda();
       BigDecimal exemplares = new BigDecimal(getQtdeExemplares());
	   return exemplares.multiply(preco == null?BigDecimal.ZERO:preco);
	}
	
    /**
     * Calcula a qtde de exemplares da diferença
     * 
     * @return qtde da diferença em exemplares
     */
	public BigInteger getQtdeExemplares() {
	    return qtde;
	}
	
    /**
     * Calcula o valor real da diferença R$, ou seja, caso a diferença seja dos
     * tipos {@link TipoDiferenca#FALTA_DE} ou {@link TipoDiferenca#FALTA_EM} o
     * valor será nagtivo.
     * 
     * @return valor total real da diferença
     */
	public BigDecimal getValorTotalReal() {
	    BigDecimal totalReal = getValorTotal();
        return tipoDiferenca.isSobra() ? totalReal : totalReal.negate();
	}
}