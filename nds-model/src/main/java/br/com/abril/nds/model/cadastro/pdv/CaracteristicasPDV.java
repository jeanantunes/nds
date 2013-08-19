package br.com.abril.nds.model.cadastro.pdv;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Informações de caracterização do PDV
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class CaracteristicasPDV {

	/**
	 * PDV é ponto principal
	 */
	@Column(name = "PONTO_PRINCIPAL")
	private boolean pontoPrincipal;
	
	/**
	 * PDV é balcão central
	 */
	@Column(name = "BALCAO_CENTRAL")
	private boolean balcaoCentral;
	
	/**
	 * PDV possui computador
	 */
	@Column(name = "POSSUI_COMPUTADOR")
	private boolean possuiComputador;
	
	/**
	 * PDV possui luminoso
	 */
	@Column(name = "POSSUI_LUMINOSO")
	private boolean possuiLuminoso;
	
	/**
	 * Texto do luminoso
	 */
	@Column(name = "TEXTO_LUMINOSO")
	private String textoLuminoso;

	@Column(name="POSSUI_CARTAO_CREDITO")
	private boolean possuiCartaoCredito;
	
	public boolean isPontoPrincipal() {
		return pontoPrincipal;
	}

	public void setPontoPrincipal(boolean pontoPrincipal) {
		this.pontoPrincipal = pontoPrincipal;
	}

	public boolean isBalcaoCentral() {
		return balcaoCentral;
	}
	
	public void setBalcaoCentral(boolean balcaoCentral) {
		this.balcaoCentral = balcaoCentral;
	}

	public boolean isPossuiComputador() {
		return possuiComputador;
	}

	public void setPossuiComputador(boolean possuiComputador) {
		this.possuiComputador = possuiComputador;
	}

	public boolean isPossuiLuminoso() {
		return possuiLuminoso;
	}

	public void setPossuiLuminoso(boolean possuiLuminoso) {
		this.possuiLuminoso = possuiLuminoso;
	}

	public String getTextoLuminoso() {
		return textoLuminoso;
	}

	public void setTextoLuminoso(String textoLuminoso) {
		this.textoLuminoso = textoLuminoso;
	}

	public boolean isPossuiCartaoCredito() {
		return possuiCartaoCredito;
	}

	public void setPossuiCartaoCredito(boolean possuiCartaoCredito) {
		this.possuiCartaoCredito = possuiCartaoCredito;
	}
	
}
