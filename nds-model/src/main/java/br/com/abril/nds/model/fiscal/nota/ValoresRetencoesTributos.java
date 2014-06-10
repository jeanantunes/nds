package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

/**
 * Grupo de Retenções de Tributos
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name="NOTA_FISCAL_VALORES_RETENCOES_TRIBUTOS")
public class ValoresRetencoesTributos implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1034197158132649569L;
	@Id
	private Long id;
	
	/**
	 * vRetPIS
	 */
	@Column(name="VL_RET_PIS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=0, tamanho=15)
	private BigDecimal valorRetidoPIS;
	
	/**
	 * vRetCOFINS
	 */
	@Column(name="VL_RET_COFINS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=1, tamanho=15)
	private BigDecimal valorRetidoCOFINS;
	
	/**
	 * vRetCSLL
	 */
	@Column(name="VL_RET_CSLL", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=2, tamanho=15)
	private BigDecimal valorRetidoCSLL;
	
	
	/**
	 * vBCIRRF
	 */
	@Column(name="VL_BC_IRRF", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=3, tamanho=15)
	private BigDecimal valorBaseCalculoIRRF;
	
	/**
	 * vIRRF
	 */
	@Column(name="VL_RET_IRRF", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=4, tamanho=15)
	private BigDecimal valorRetidoIRRF;
	
	/**
	 * vBCRetPrev
	 */
	@Column(name="VL_BC_PREV", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=5, tamanho=15)
	private BigDecimal valorBaseCalculoPrevidencia;	
	
	/**
	 * vRetPrev
	 */
	@Column(name="VL_RET_PREV", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W23, posicao=6, tamanho=15)
	private BigDecimal valorRetidoPrevidencia;
	
	@OneToOne(optional=false)
	@MapsId("ID") @JoinColumn(name="ID")
	private NotaFiscal notaFiscal;

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
	 * @return the valorRetidoPIS
	 */
	public BigDecimal getValorRetidoPIS() {
		return valorRetidoPIS;
	}

	/**
	 * @param valorRetidoPIS the valorRetidoPIS to set
	 */
	public void setValorRetidoPIS(BigDecimal valorRetidoPIS) {
		this.valorRetidoPIS = valorRetidoPIS;
	}

	/**
	 * @return the valorRetidoCOFINS
	 */
	public BigDecimal getValorRetidoCOFINS() {
		return valorRetidoCOFINS;
	}

	/**
	 * @param valorRetidoCOFINS the valorRetidoCOFINS to set
	 */
	public void setValorRetidoCOFINS(BigDecimal valorRetidoCOFINS) {
		this.valorRetidoCOFINS = valorRetidoCOFINS;
	}

	/**
	 * @return the valorRetidoCSLL
	 */
	public BigDecimal getValorRetidoCSLL() {
		return valorRetidoCSLL;
	}

	/**
	 * @param valorRetidoCSLL the valorRetidoCSLL to set
	 */
	public void setValorRetidoCSLL(BigDecimal valorRetidoCSLL) {
		this.valorRetidoCSLL = valorRetidoCSLL;
	}

	/**
	 * @return the valorBaseCalculoIRRF
	 */
	public BigDecimal getValorBaseCalculoIRRF() {
		return valorBaseCalculoIRRF;
	}

	/**
	 * @param valorBaseCalculoIRRF the valorBaseCalculoIRRF to set
	 */
	public void setValorBaseCalculoIRRF(BigDecimal valorBaseCalculoIRRF) {
		this.valorBaseCalculoIRRF = valorBaseCalculoIRRF;
	}

	/**
	 * @return the valorRetidoIRRF
	 */
	public BigDecimal getValorRetidoIRRF() {
		return valorRetidoIRRF;
	}

	/**
	 * @param valorRetidoIRRF the valorRetidoIRRF to set
	 */
	public void setValorRetidoIRRF(BigDecimal valorRetidoIRRF) {
		this.valorRetidoIRRF = valorRetidoIRRF;
	}

	/**
	 * @return the valorBaseCalculoPrevidencia
	 */
	public BigDecimal getValorBaseCalculoPrevidencia() {
		return valorBaseCalculoPrevidencia;
	}

	/**
	 * @param valorBaseCalculoPrevidencia the valorBaseCalculoPrevidencia to set
	 */
	public void setValorBaseCalculoPrevidencia(
			BigDecimal valorBaseCalculoPrevidencia) {
		this.valorBaseCalculoPrevidencia = valorBaseCalculoPrevidencia;
	}

	/**
	 * @return the valorRetidoPrevidencia
	 */
	public BigDecimal getValorRetidoPrevidencia() {
		return valorRetidoPrevidencia;
	}

	/**
	 * @param valorRetidoPrevidencia the valorRetidoPrevidencia to set
	 */
	public void setValorRetidoPrevidencia(BigDecimal valorRetidoPrevidencia) {
		this.valorRetidoPrevidencia = valorRetidoPrevidencia;
	}

	/**
	 * @return the notaFiscal
	 */
	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	/**
	 * @param notaFiscal the notaFiscal to set
	 */
	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	
	
}
