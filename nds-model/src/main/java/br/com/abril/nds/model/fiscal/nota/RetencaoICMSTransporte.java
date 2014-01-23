package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.abril.nds.util.TipoSecao;
import br.com.abril.nds.util.export.fiscal.nota.NFEExport;

/**
 * Grupo de Retenção do ICMS do transporte
 * 
 * @author Diego Fernandes
 * 
 */
@Entity
@Table(name="NOTA_FISCAL_RETENCAO_ICMS_TRANSPORTE")
public class RetencaoICMSTransporte implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4337764465802898445L;

	@Id
	private Long id;
	
	
	@OneToOne
	@JoinColumn(name="ID", referencedColumnName="id", updatable=false, insertable=false)
	private NotaFiscal notaFiscal;
	
	/**
	 * vServ
	 */
	@Column(name="VL_SERVICO", nullable=false, precision=15, scale=2)
	@NFEExport(secao = TipoSecao.X11, posicao = 0)
	private BigDecimal valorServico;
	
	/**
	 * vBCRet
	 */
	@Column(name="VL_BC_RET_ICMS", nullable=false, precision=15, scale=2)
	@NFEExport(secao = TipoSecao.X11, posicao = 1)
	private BigDecimal valorBaseCalculo;
	
	/**
	 * pICMSRet
	 */
	@Column(name="PER_ALIQUOTA", nullable=false, precision=5, scale=2)
	@NFEExport(secao = TipoSecao.X11, posicao = 2)
	private BigDecimal percentualAliquota;
	
	/**
	 * vICMSRet
	 */
	@Column(name="VL_ICMS", nullable=false, precision=15, scale=2)
	@NFEExport(secao = TipoSecao.X11, posicao = 3)
	private BigDecimal valorICMS;
	
	/**
	 * CFOP
	 */
	@Column(name="CFOP", nullable=false,length=4)
	@NFEExport(secao = TipoSecao.X11, posicao = 4, mascara = "0000")
	private Integer cfop;
	
	
	/**
	 * cMunFG
	 */
	@Column(name="CODIGO_MUNICIPIO", nullable=false, length=7)
	@NFEExport(secao = TipoSecao.X11, posicao = 5)
	private Long codigoMunicipio;


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
	 * @return the valorServico
	 */
	public BigDecimal getValorServico() {
		return valorServico;
	}


	/**
	 * @param valorServico the valorServico to set
	 */
	public void setValorServico(BigDecimal valorServico) {
		this.valorServico = valorServico;
	}


	/**
	 * @return the valorBaseCalculo
	 */
	public BigDecimal getValorBaseCalculo() {
		return valorBaseCalculo;
	}


	/**
	 * @param valorBaseCalculo the valorBaseCalculo to set
	 */
	public void setValorBaseCalculo(BigDecimal valorBaseCalculo) {
		this.valorBaseCalculo = valorBaseCalculo;
	}


	/**
	 * @return the percentualAliquota
	 */
	public BigDecimal getPercentualAliquota() {
		return percentualAliquota;
	}


	/**
	 * @param percentualAliquota the percentualAliquota to set
	 */
	public void setPercentualAliquota(BigDecimal percentualAliquota) {
		this.percentualAliquota = percentualAliquota;
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
	 * @return the cfop
	 */
	public Integer getCfop() {
		return cfop;
	}


	/**
	 * @param cfop the cfop to set
	 */
	public void setCfop(Integer cfop) {
		this.cfop = cfop;
	}


	/**
	 * @return the codigoMunicipio
	 */
	public Long getCodigoMunicipio() {
		return codigoMunicipio;
	}


	/**
	 * @param codigoMunicipio the codigoMunicipio to set
	 */
	public void setCodigoMunicipio(Long codigoMunicipio) {
		this.codigoMunicipio = codigoMunicipio;
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
