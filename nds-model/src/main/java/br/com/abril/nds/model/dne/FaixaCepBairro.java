package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.dne.pk.FaixaCepBairroPK;



/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_FAIXAS_CEP_BAIRRO")
public class FaixaCepBairro implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7315093858385189634L;

	@EmbeddedId
	private FaixaCepBairroPK faixaCepBairroPK;

	@Column(name="CEP_FIM_FX_BAI", length=8)
	private String cepFinal;

	@Column(name="CEP_INI_FX_BAI", length=8)
	private String cepInicial;

	@Column(name="QUANT_TOT_FX_CEP_BAI")
	private Long quantidadeTotal;

	
    @ManyToOne
	@JoinColumn(name="CHAVE_BAI_DNE", nullable=false, insertable=false, updatable=false)
	private Bairro bairro;

    public FaixaCepBairro() {
    }

	/**
	 * @return the faixaCepBairroPK
	 */
	public FaixaCepBairroPK getFaixaCepBairroPK() {
		return faixaCepBairroPK;
	}

	/**
	 * @param faixaCepBairroPK the faixaCepBairroPK to set
	 */
	public void setFaixaCepBairroPK(FaixaCepBairroPK faixaCepBairroPK) {
		this.faixaCepBairroPK = faixaCepBairroPK;
	}

	/**
	 * @return the cepFinal
	 */
	public String getCepFinal() {
		return cepFinal;
	}

	/**
	 * @param cepFinal the cepFinal to set
	 */
	public void setCepFinal(String cepFinal) {
		this.cepFinal = cepFinal;
	}

	/**
	 * @return the cepInicial
	 */
	public String getCepInicial() {
		return cepInicial;
	}

	/**
	 * @param cepInicial the cepInicial to set
	 */
	public void setCepInicial(String cepInicial) {
		this.cepInicial = cepInicial;
	}

	/**
	 * @return the quantidadeTotal
	 */
	public Long getQuantidadeTotal() {
		return quantidadeTotal;
	}

	/**
	 * @param quantidadeTotal the quantidadeTotal to set
	 */
	public void setQuantidadeTotal(Long quantidadeTotal) {
		this.quantidadeTotal = quantidadeTotal;
	}

	/**
	 * @return the bairro
	 */
	public Bairro getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}
}