package br.com.abril.nds.model.fiscal;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;

@Entity
@Table(name = "ITEM_NOTA_FISCAL_SAIDA")
@SequenceGenerator(name="ITEM_NF_SAIDA_SEQ", initialValue = 1, allocationSize = 1)
public class ItemNotaFiscalSaida {

	@Id
	@GeneratedValue(generator = "ITEM_NF_SAIDA_SEQ")
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "NOTA_FISCAL_ID")
	private NotaFiscalSaida notaFiscal;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PRODUTO_EDICAO_ID")
	private ProdutoEdicao produtoEdicao;

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
	
	public NotaFiscalSaida getNotaFiscal() {
		return notaFiscal;
	}
	
	public void setNotaFiscal(NotaFiscalSaida notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	public ProdutoEdicao getProdutoEdicao() {
		return produtoEdicao;
	}
	
	public void setProdutoEdicao(ProdutoEdicao produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
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
	
}