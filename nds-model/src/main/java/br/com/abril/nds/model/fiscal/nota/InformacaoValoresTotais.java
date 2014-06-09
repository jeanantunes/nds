package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

@Embeddable
@XmlAccessorType(XmlAccessType.FIELD)
public class InformacaoValoresTotais implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7464935296322958775L;
	
	/**
	 * vBC
	 */
	@Column(name="VL_BC_ICMS", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=0, tamanho=15)
	private BigDecimal valorBaseCalculoICMS = BigDecimal.ZERO;
	
	/**
	 * vICMS
	 */
	@Column(name="VL_TOTAL_ICMS", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=1, tamanho=15)
	private BigDecimal valorICMS= BigDecimal.ZERO;
	
	/**
	 * vBCST
	 */
	@Column(name="VL_BC_ICMS_ST", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=2, tamanho=15)
	private BigDecimal valorBaseCalculoICMSST= BigDecimal.ZERO;
	
	/**
	 * vST
	 */
	@Column(name="VL_TOTAL_ICMS_ST", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=3, tamanho =15)
	private BigDecimal valorICMSST= BigDecimal.ZERO;

	/**
	 * vProd
	 */
	@Column(name="VL_TOTAL_PRODUTOS", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=4, tamanho =15)
	private BigDecimal valorProdutos= BigDecimal.ZERO;
	
	/**
	 * vFrete
	 */
	@Column(name="VL_TOTAL_FRETE", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=5, tamanho =15)
	private BigDecimal valorFrete= BigDecimal.ZERO;
	
	/**
	 * vSeg
	 */
	@Column(name="VL_TOTAL_SEGURO", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=6, tamanho =15)
	private BigDecimal valorSeguro= BigDecimal.ZERO;
	
	/**
	 * vDesc
	 */
	@Column(name="VL_TOTAL_DESCONTO", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=7, tamanho =15)
	private BigDecimal valorDesconto= BigDecimal.ZERO;
	
	/**
	 * vIPI
	 */
	@Column(name="VL_TOTAL_IPI", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=9, tamanho =15)
	private BigDecimal valorIPI= BigDecimal.ZERO;
	
	/**
	 * vPIS
	 */
	@Column(name="VL_TOTAL_PIS", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=10, tamanho =15)
	private BigDecimal valorPIS= BigDecimal.ZERO;
	
	/**
	 * vCOFINS
	 */
	@Column(name="VL_TOTAL_COFINS", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=11, tamanho =15)
	private BigDecimal valorCOFINS= BigDecimal.ZERO;
	
	/**
	 * vOutro
	 */
	@Column(name="VL_TOTAL_OUTRO", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=12, tamanho =15)
	private BigDecimal valorOutro= BigDecimal.ZERO;
	
	/**
	 * vNF
	 */
	@Column(name="VL_TOTAL_NF", nullable=false, precision=18, scale=4)
	@NFEExport(secao=TipoSecao.W02, posicao=13, tamanho =15)
	private BigDecimal valorNotaFiscal= BigDecimal.ZERO;
	
	/**
	 * ISSQNtot
	 */
	@OneToOne(mappedBy="notaFiscal")
	private ValoresTotaisISSQN totaisISSQN;
	
	@OneToOne(mappedBy="notaFiscal")
	private ValoresRetencoesTributos retencoesTributos;

	/**
	 * @return the valorBaseCalculoICMS
	 */
	public BigDecimal getValorBaseCalculoICMS() {
		return valorBaseCalculoICMS;
	}

	/**
	 * @param valorBaseCalculoICMS the valorBaseCalculoICMS to set
	 */
	public void setValorBaseCalculoICMS(BigDecimal valorBaseCalculoICMS) {
		this.valorBaseCalculoICMS = valorBaseCalculoICMS;
	}

	/**
	 * @return the valorICMS
	 */
	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	/**
	 * @param valorICMS the valorICMS to set
	 */
	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}

	/**
	 * @return the valorBaseCalculoICMSST
	 */
	public BigDecimal getValorBaseCalculoICMSST() {
		return valorBaseCalculoICMSST;
	}

	/**
	 * @param valorBaseCalculoICMSST the valorBaseCalculoICMSST to set
	 */
	public void setValorBaseCalculoICMSST(BigDecimal valorBaseCalculoICMSST) {
		this.valorBaseCalculoICMSST = valorBaseCalculoICMSST;
	}

	/**
	 * @return the valorICMSST
	 */
	public BigDecimal getValorICMSST() {
		return valorICMSST;
	}

	/**
	 * @param valorICMSST the valorICMSST to set
	 */
	public void setValorICMSST(BigDecimal valorICMSST) {
		this.valorICMSST = valorICMSST;
	}

	/**
	 * @return the valorProdutos
	 */
	public BigDecimal getValorProdutos() {
		return valorProdutos;
	}

	/**
	 * @param valorProdutos the valorProdutos to set
	 */
	public void setValorProdutos(BigDecimal valorProdutos) {
		this.valorProdutos = valorProdutos;
	}

	/**
	 * @return the valorFrete
	 */
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	/**
	 * @param valorFrete the valorFrete to set
	 */
	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	/**
	 * @return the valorSeguro
	 */
	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	/**
	 * @param valorSeguro the valorSeguro to set
	 */
	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	/**
	 * @return the valorDesconto
	 */
	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	/**
	 * @param valorDesconto the valorDesconto to set
	 */
	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	/**
	 * @return the valorIPI
	 */
	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	/**
	 * @param valorIPI the valorIPI to set
	 */
	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	/**
	 * @return the valorPIS
	 */
	public BigDecimal getValorPIS() {
		return valorPIS;
	}

	/**
	 * @param valorPIS the valorPIS to set
	 */
	public void setValorPIS(BigDecimal valorPIS) {
		this.valorPIS = valorPIS;
	}

	/**
	 * @return the valorCOFINS
	 */
	public BigDecimal getValorCOFINS() {
		return valorCOFINS;
	}

	/**
	 * @param valorCOFINS the valorCOFINS to set
	 */
	public void setValorCOFINS(BigDecimal valorCOFINS) {
		this.valorCOFINS = valorCOFINS;
	}

	/**
	 * @return the valorOutro
	 */
	public BigDecimal getValorOutro() {
		return valorOutro;
	}

	/**
	 * @param valorOutro the valorOutro to set
	 */
	public void setValorOutro(BigDecimal valorOutro) {
		this.valorOutro = valorOutro;
	}

	/**
	 * @return the valorNotaFiscal
	 */
	public BigDecimal getValorNotaFiscal() {
		return valorNotaFiscal;
	}

	/**
	 * @param valorNotaFiscal the valorNotaFiscal to set
	 */
	public void setValorNotaFiscal(BigDecimal valorNotaFiscal) {
		this.valorNotaFiscal = valorNotaFiscal;
	}

	/**
	 * @return the totaisISSQN
	 */
	public ValoresTotaisISSQN getTotaisISSQN() {
		return totaisISSQN;
	}

	/**
	 * @param totaisISSQN the totaisISSQN to set
	 */
	public void setTotaisISSQN(ValoresTotaisISSQN totaisISSQN) {
		this.totaisISSQN = totaisISSQN;
	}

	/**
	 * @return the retencoesTributos
	 */
	public ValoresRetencoesTributos getRetencoesTributos() {
		return retencoesTributos;
	}

	/**
	 * @param retencoesTributos the retencoesTributos to set
	 */
	public void setRetencoesTributos(ValoresRetencoesTributos retencoesTributos) {
		this.retencoesTributos = retencoesTributos;
	}

	
	

}
