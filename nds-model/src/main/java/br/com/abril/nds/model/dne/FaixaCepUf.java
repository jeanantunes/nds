package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.abril.nds.model.dne.pk.FaixaCepUfPK;



/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="DNE_GU_FAIXAS_CEP_UF")
public class FaixaCepUf implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7266779973767558617L;

	@EmbeddedId
	private FaixaCepUfPK faixaCepUfPK;

	@Column(name="CEP_FIM_FX_UF", length=8)
	private String cepFinal;

	@Column(name="CEP_INI_FX_UF", nullable=false, length=8)
	private String cepInicial;

	@Column(name="CHAVE_UF_DNE")
	private Long chaveUf;

	@Column(name="QUANT_TOT_FX_CEP_UF")
	private Long quantidadeTotal;
	
    @ManyToOne
	@JoinColumn(name="SIGLA_UF", nullable=false, insertable=false, updatable=false)
	private UnidadeFederacao unidadesFederacao;

    public FaixaCepUf() {
    }

	/**
	 * @return the faixaCepUfPK
	 */
	public FaixaCepUfPK getFaixaCepUfPK() {
		return faixaCepUfPK;
	}

	/**
	 * @param faixaCepUfPK the faixaCepUfPK to set
	 */
	public void setFaixaCepUfPK(FaixaCepUfPK faixaCepUfPK) {
		this.faixaCepUfPK = faixaCepUfPK;
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
	 * @return the chaveUf
	 */
	public Long getChaveUf() {
		return chaveUf;
	}

	/**
	 * @param chaveUf the chaveUf to set
	 */
	public void setChaveUf(Long chaveUf) {
		this.chaveUf = chaveUf;
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
	 * @return the unidadesFederacao
	 */
	public UnidadeFederacao getUnidadesFederacao() {
		return unidadesFederacao;
	}

	/**
	 * @param unidadesFederacao the unidadesFederacao to set
	 */
	public void setUnidadesFederacao(UnidadeFederacao unidadesFederacao) {
		this.unidadesFederacao = unidadesFederacao;
	}

}