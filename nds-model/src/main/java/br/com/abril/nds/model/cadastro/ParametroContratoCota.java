package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Entidade para suportar funcionalidade de impressão do 
 * contrato entre cota e distribuidor
 * 
 * @author francisco.garcia
 *
 */
@Entity
@Table(name = "PARAMETRO_CONTRATO_COTA")
@SequenceGenerator(name="PARAMETRO_CONTRATO_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroContratoCota {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "PARAMETRO_CONTRATO_COTA_SEQ")
	private Long id;
	
	/**
	 * Fator de período de duração do contrato entre cota e distribuidor, 
	 * em meses.
	 * O contrato tem a vigência calculada à partir do início de atividade da
	 * cota mais o fator de duração de contrato da cota, que é definido
	 * em meses.
	 */
	@Column(name = "DURACAO_CONTRATO_COTA", nullable = false)
	private int duracaoContratoCota;
	
	/**
	 * Dias para aviso prévio na rescisão do contrato
	 * 
	 * */
	@Column(name = "DIAS_AVISO_RESCISAO", nullable = false)
	private int diasAvisoRescisao;
	
	/**
	 * Complemento do contrato da cota. 
	 * Este complemento será anexado à parte padrão do contrato da cota 
	 */
	@Lob
	@Column(name = "COMPLEMENTO_CONTRATO")
	private String complementoContrato; 
	
	/**
	 * Condições de Contratação
	 */
	@Lob
	@Column(name = "CONDICOES_CONTRATACAO")
	private String condicoesContratacao; 
	
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
	 * @return the duracaoContratoCota
	 */
	public int getDuracaoContratoCota() {
		return duracaoContratoCota;
	}

	/**
	 * @param duracaoContratoCota the duracaoContratoCota to set
	 */
	public void setDuracaoContratoCota(int duracaoContratoCota) {
		this.duracaoContratoCota = duracaoContratoCota;
	}

	/**
	 * @return the diasAvisoRescisao
	 */
	public int getDiasAvisoRescisao() {
		return diasAvisoRescisao;
	}

	/**
	 * @param diasAvisoRescisao the diasAvisoRescisao to set
	 */
	public void setDiasAvisoRescisao(int diasAvisoRescisao) {
		this.diasAvisoRescisao = diasAvisoRescisao;
	}

	/**
	 * @return the complementoContrato
	 */
	public String getComplementoContrato() {
		return complementoContrato;
	}

	/**
	 * @param complementoContrato the complementoContrato to set
	 */
	public void setComplementoContrato(String complementoContrato) {
		this.complementoContrato = complementoContrato;
	}

	public String getCondicoesContratacao() {
		return condicoesContratacao;
	}

	public void setCondicoesContratacao(String condicoesContratacao) {
		this.condicoesContratacao = condicoesContratacao;
	}

}
