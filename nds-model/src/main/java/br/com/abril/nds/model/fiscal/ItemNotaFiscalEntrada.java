package br.com.abril.nds.model.fiscal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "ITEM_NOTA_FISCAL_ENTRADA")
@SequenceGenerator(name="ITEM_NF_ENTRADA_SEQ", initialValue = 1, allocationSize = 1)
public class ItemNotaFiscalEntrada implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1875057047416707211L;

	@Id
	@GeneratedValue(generator = "ITEM_NF_ENTRADA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NCM_PRODUTO")
	private String NCMProduto;

	@Column(name = "CFOP_PRODUTO")
	private String CFOPProduto;
	
	@Column(name = "UNIDADE_PRODUTO")
	private Long unidadeProduto;
	
	@Column(name = "CST_PRODUTO")
	private String CSTProduto;
	
	@Column(name = "CSOSN_PRODUTO")
	private String CSOSNProduto;
	
	@Column(name = "BASE_CALCULO_PRODUTO")
	private BigDecimal baseCalculoProduto;
	
	@Column(name = "ALIQUOTA_ICMS_PRODUTO")
	private BigDecimal aliquotaICMSProduto;
	
	@Column(name = "VALOR_ICMS_PRODUTO")
	private BigDecimal valorICMSProduto;
	
	@Column(name = "ALIQUOTA_IPI_PRODUTO")
	private BigDecimal aliquotaIPIProduto;
	
	@Column(name = "VALOR_IPI_PRODUTO")
	private BigDecimal valorIPIProduto;	
	
	@Column(name = "QTDE", nullable = false)
	private BigInteger qtde;

	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscalEntrada notaFiscal;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ORIGEM")
	private Origem origem;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LANCAMENTO", nullable = false)
	private Date dataLancamento;	
	
	/* Alterado amando de Eduardo "PunkRock" Castro 17/08/2012 */
	@Column(name = "TIPO_LANCAMENTO", nullable = true)
	private TipoLancamento tipoLancamento;	
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "DATA_RECOLHIMENTO", nullable = false)
	private Date dataRecolhimento;	
	
	@OneToOne(mappedBy = "itemNotaFiscal")
	private ItemRecebimentoFisico recebimentoFisico;
	
	@Column(name = "PRECO", nullable = true)
	private BigDecimal preco;
	
	@Column(name = "DESCONTO", nullable = true)
	private BigDecimal desconto;

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
	
	public NotaFiscalEntrada getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(NotaFiscalEntrada notaFiscal) {
		this.notaFiscal = notaFiscal;
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
	
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Origem getOrigem() {
		return origem;
	}

	public void setOrigem(Origem origem) {
		this.origem = origem;
	}
	
	public Date getDataLancamento() {
		return dataLancamento;
	}
	
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	public ItemRecebimentoFisico getRecebimentoFisico() {
		return recebimentoFisico;
	}
	
	public void setRecebimentoFisico(ItemRecebimentoFisico recebimentoFisico) {
		this.recebimentoFisico = recebimentoFisico;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}

	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}

	/**
	 * Obtém nCMProduto
	 *
	 * @return String
	 */
	public String getNCMProduto() {
		return NCMProduto;
	}

	/**
	 * Atribuí nCMProduto
	 * @param nCMProduto 
	 */
	public void setNCMProduto(String nCMProduto) {
		NCMProduto = nCMProduto;
	}

	/**
	 * Obtém cFOPProduto
	 *
	 * @return String
	 */
	public String getCFOPProduto() {
		return CFOPProduto;
	}

	/**
	 * Atribuí cFOPProduto
	 * @param cFOPProduto 
	 */
	public void setCFOPProduto(String cFOPProduto) {
		CFOPProduto = cFOPProduto;
	}

	/**
	 * Obtém unidadeProduto
	 *
	 * @return Long
	 */
	public Long getUnidadeProduto() {
		return unidadeProduto;
	}

	/**
	 * Atribuí unidadeProduto
	 * @param unidadeProduto 
	 */
	public void setUnidadeProduto(Long unidadeProduto) {
		this.unidadeProduto = unidadeProduto;
	}

	/**
	 * Obtém cSTProduto
	 *
	 * @return String
	 */
	public String getCSTProduto() {
		return CSTProduto;
	}

	/**
	 * Atribuí cSTProduto
	 * @param cSTProduto 
	 */
	public void setCSTProduto(String cSTProduto) {
		CSTProduto = cSTProduto;
	}

	/**
	 * Obtém cSOSNProduto
	 *
	 * @return String
	 */
	public String getCSOSNProduto() {
		return CSOSNProduto;
	}

	/**
	 * Atribuí cSOSNProduto
	 * @param cSOSNProduto 
	 */
	public void setCSOSNProduto(String cSOSNProduto) {
		CSOSNProduto = cSOSNProduto;
	}

	/**
	 * Obtém baseCalculoProduto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getBaseCalculoProduto() {
		return baseCalculoProduto;
	}

	/**
	 * Atribuí baseCalculoProduto
	 * @param baseCalculoProduto 
	 */
	public void setBaseCalculoProduto(BigDecimal baseCalculoProduto) {
		this.baseCalculoProduto = baseCalculoProduto;
	}

	/**
	 * Obtém aliquotaICMSProduto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getAliquotaICMSProduto() {
		return aliquotaICMSProduto;
	}

	/**
	 * Atribuí aliquotaICMSProduto
	 * @param aliquotaICMSProduto 
	 */
	public void setAliquotaICMSProduto(BigDecimal aliquotaICMSProduto) {
		this.aliquotaICMSProduto = aliquotaICMSProduto;
	}

	/**
	 * Obtém valorICMSProduto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorICMSProduto() {
		return valorICMSProduto;
	}

	/**
	 * Atribuí valorICMSProduto
	 * @param valorICMSProduto 
	 */
	public void setValorICMSProduto(BigDecimal valorICMSProduto) {
		this.valorICMSProduto = valorICMSProduto;
	}

	/**
	 * Obtém aliquotaIPIProduto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getAliquotaIPIProduto() {
		return aliquotaIPIProduto;
	}

	/**
	 * Atribuí aliquotaIPIProduto
	 * @param aliquotaIPIProduto 
	 */
	public void setAliquotaIPIProduto(BigDecimal aliquotaIPIProduto) {
		this.aliquotaIPIProduto = aliquotaIPIProduto;
	}

	/**
	 * Obtém valorIPIProduto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorIPIProduto() {
		return valorIPIProduto;
	}

	/**
	 * Atribuí valorIPIProduto
	 * @param valorIPIProduto 
	 */
	public void setValorIPIProduto(BigDecimal valorIPIProduto) {
		this.valorIPIProduto = valorIPIProduto;
	}

	/**
	 * @return the preco
	 */
	public BigDecimal getPreco() {
		return preco;
	}

	/**
	 * @param preco the preco to set
	 */
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getNotaFiscal() == null) ? 0 : this.getNotaFiscal().hashCode());
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
		ItemNotaFiscalEntrada other = (ItemNotaFiscalEntrada) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId() .equals(other.getId()))
			return false;
		if (this.getNotaFiscal() == null) {
			if (other.getNotaFiscal() != null)
				return false;
		} else if (!this.getNotaFiscal().equals(other.getNotaFiscal()))
			return false;
		if (this.getProdutoEdicao() == null) {
			if (other.getProdutoEdicao() != null)
				return false;
		} else if (!this.getProdutoEdicao().equals(other.getProdutoEdicao()))
			return false;
		return true;
	}
}