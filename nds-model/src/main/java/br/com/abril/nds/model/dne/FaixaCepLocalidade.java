package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.dne.pk.FaixaCepLocalidadePK;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_FAIXAS_CEP_LOCALIDADE")
public class FaixaCepLocalidade implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2590363446442822298L;

	@EmbeddedId
	private FaixaCepLocalidadePK faixaCepLocalidadePK;

	@Column(name="CEP_FIM_FX_LOC_CODIF", length=8)
	private String cepFinal;

	@Column(name="CEP_INI_FX_LOC_CODIF", length=8)
	private String cepInicial;

	@Column(name="QUANT_TOT_FX_CEP_LOC")
	private Long quantidadeTotal;

    @ManyToOne
	@JoinColumn(name="CHAVE_LOC_DNE", nullable=false, insertable=false, updatable=false)
	private Localidade localidade;

    public FaixaCepLocalidade() {
    }

	/**
	 * @return the faixaCepLocalidadePK
	 */
	public FaixaCepLocalidadePK getFaixaCepLocalidadePK() {
		return faixaCepLocalidadePK;
	}

	/**
	 * @param faixaCepLocalidadePK the faixaCepLocalidadePK to set
	 */
	public void setFaixaCepLocalidadePK(FaixaCepLocalidadePK faixaCepLocalidadePK) {
		this.faixaCepLocalidadePK = faixaCepLocalidadePK;
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
	 * @return the localidade
	 */
	public Localidade getLocalidade() {
		return localidade;
	}

	/**
	 * @param localidade the localidade to set
	 */
	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

}