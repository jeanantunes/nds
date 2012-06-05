package br.com.abril.nds.model.fiscal.nota;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Identificacao implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3614623505646574143L;
	
	/**
	 * cNF
	 */
	@Column(name = "CODIGO_CHAVE_ACESSO", length = 8, nullable = false)
	private Integer codigoChaveAcesso;
	
	/**
	 * cDV
	 */
	@Column(name = "DV_CHAVE_ACESSO", length = 1, nullable = false)
	private Integer digitoVerificadorChaveAcesso;
	
	/**
	 * tpNF
	 */
	@Column(name = "TIPO_OPERACAO", length = 1, nullable = false)
	private Integer tipoOperacao;
	
	/**
	 * Construtor padrão.
	 */
	public Identificacao() {
		
	}

	/**
	 * @return the codigoChaveAcesso
	 */
	public Integer getCodigoChaveAcesso() {
		return codigoChaveAcesso;
	}

	/**
	 * @param codigoChaveAcesso the codigoChaveAcesso to set
	 */
	public void setCodigoChaveAcesso(Integer codigoChaveAcesso) {
		this.codigoChaveAcesso = codigoChaveAcesso;
	}

	/**
	 * @return the digitoVerificadorChaveAcesso
	 */
	public Integer getDigitoVerificadorChaveAcesso() {
		return digitoVerificadorChaveAcesso;
	}

	/**
	 * @param digitoVerificadorChaveAcesso the digitoVerificadorChaveAcesso to set
	 */
	public void setDigitoVerificadorChaveAcesso(Integer digitoVerificadorChaveAcesso) {
		this.digitoVerificadorChaveAcesso = digitoVerificadorChaveAcesso;
	}

	/**
	 * @return the tipoOperacao
	 */
	public Integer getTipoOperacao() {
		return tipoOperacao;
	}

	/**
	 * @param tipoOperacao the tipoOperacao to set
	 */
	public void setTipoOperacao(Integer tipoOperacao) {
		this.tipoOperacao = tipoOperacao;
	}

}
