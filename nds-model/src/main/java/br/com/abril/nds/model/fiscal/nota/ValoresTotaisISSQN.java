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
	@NFEExport(secao=TipoSecao.W17, posicao=0, tamanho=15)
	private BigDecimal valorServicos;
	
	/**
	 * vBC
	 */
	@Column(name="VL_TOTAL_BC_ISS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W17, posicao=1, tamanho=15)
	private BigDecimal valorBaseCalculo;
	
	/**
	 * vISS
	 */
	@Column(name="VL_TOTAL_ISS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W17, posicao=2, tamanho=15)
	private BigDecimal valorISS;
	
	/**
	 * vPIS
	 */
	@Column(name="VL_TOTAL_PIS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W17, posicao=3, tamanho=15)
	private BigDecimal valorPIS;
	
	/**
	 * vCOFINS
	 */
	@Column(name="VL_TOTAL_COFINS", nullable=true, precision=15, scale=2)
	@NFEExport(secao=TipoSecao.W17, posicao=4, tamanho=15)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result
				+ ((this.getNotaFiscal() == null) ? 0 : this.getNotaFiscal().hashCode());
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
		ValoresTotaisISSQN other = (ValoresTotaisISSQN) obj;
		if (this.getId() == null) {
			if (other.id != null)
				return false;
		} else if (!this.getId().equals(other.id))
			return false;
		if (this.getNotaFiscal() == null) {
			if (other.notaFiscal != null)
				return false;
		} else if (!this.getNotaFiscal().equals(other.notaFiscal))
			return false;
		return true;
	}
}
