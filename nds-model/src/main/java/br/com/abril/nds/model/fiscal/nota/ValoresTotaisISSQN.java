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
/**
 * SSQNtot<br>
 * Grupo de valores totais referentes ao Imposto sobre serviços de qualquer natureza.
 * @author Diego Fernandes
 *
 */
@Entity
@Table(name="NOTA_FISCAL_VALORES_TOTAIS_ISSQN")
public class ValoresTotaisISSQN implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5885021936595829481L;

	@Id
	private Long id;
	
	/**
	 * vServ
	 */
	@Column(name="VL_TOTAL_SERVICOS", nullable=true, precision=15, scale=2)
	private BigDecimal valorServicos;
	
	/**
	 * vBC
	 */
	@Column(name="VL_TOTAL_BC_ISS", nullable=true, precision=15, scale=2)
	private BigDecimal valorBaseCalculo;
	
	/**
	 * vISS
	 */
	@Column(name="VL_TOTAL_ISS", nullable=true, precision=15, scale=2)
	private BigDecimal valorISS;
	/**
	 * vPIS
	 */
	@Column(name="VL_TOTAL_PIS", nullable=true, precision=15, scale=2)
	private BigDecimal valorPIS;
	
	/**
	 * vCOFINS
	 */
	@Column(name="VL_TOTAL_COFINS", nullable=true, precision=15, scale=2)
	private BigDecimal valorCOFINS;
	

	@OneToOne(optional=false) @MapsId("ID") @JoinColumn(name="ID")
	private NotaFiscal notaFiscal;

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
	 * @return the valorServicos
	 */
	public BigDecimal getValorServicos() {
		return valorServicos;
	}

	/**
	 * @param valorServicos the valorServicos to set
	 */
	public void setValorServicos(BigDecimal valorServicos) {
		this.valorServicos = valorServicos;
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
	 * @return the valorISS
	 */
	public BigDecimal getValorISS() {
		return valorISS;
	}

	/**
	 * @param valorISS the valorISS to set
	 */
	public void setValorISS(BigDecimal valorISS) {
		this.valorISS = valorISS;
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
}
