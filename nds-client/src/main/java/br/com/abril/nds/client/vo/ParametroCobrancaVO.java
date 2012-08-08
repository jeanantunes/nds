package br.com.abril.nds.client.vo;


public class ParametroCobrancaVO {
	
	Long idPolitica;
	String forma;
	String banco;
	String valorMinimoEmissao;
	String formaEmissao;
	String acumulaDivida;
	String cobrancaUnificada;
	String envioEmail;
	boolean principal;
	String fornecedores;
	String concentracaoPagamentos;
	
	
	
	public Long getIdPolitica() {
		return idPolitica;
	}
	
	public void setIdPolitica(Long idPolitica) {
		this.idPolitica = idPolitica;
	}
	
	public String getForma() {
		return forma;
	}
	
	public void setForma(String forma) {
		this.forma = forma;
	}
	
	public String getBanco() {
		return banco;
	}
	
	public void setBanco(String banco) {
		this.banco = banco;
	}
	
	public String getValorMinimoEmissao() {
		return valorMinimoEmissao;
	}
	
	public void setValorMinimoEmissao(String vlr_minimo_emissao) {
		this.valorMinimoEmissao = vlr_minimo_emissao;
	}
	
	public String getFormaEmissao() {
		return formaEmissao;
	}
	public void setFormaEmissao(String formaEmissao) {
		this.formaEmissao = formaEmissao;
	}
	
	public String getAcumulaDivida() {
		return acumulaDivida;
	}
	
	public void setAcumulaDivida(String acumula_divida) {
		this.acumulaDivida = acumula_divida;
	}
	
	public String getCobrancaUnificada() {
		return cobrancaUnificada;
	}
	
	public void setCobrancaUnificada(String cobranca_unificada) {
		this.cobrancaUnificada = cobranca_unificada;
	}
	
	public String getEnvioEmail() {
		return envioEmail;
	}
	
	public void setEnvioEmail(String envio_email) {
		this.envioEmail = envio_email;
	}
	
	public boolean isPrincipal() {
		return principal;
	}
	
	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((acumulaDivida == null) ? 0 : acumulaDivida.hashCode());
		result = prime * result + ((banco == null) ? 0 : banco.hashCode());
		result = prime
				* result
				+ ((cobrancaUnificada == null) ? 0 : cobrancaUnificada
						.hashCode());
		result = prime * result
				+ ((envioEmail == null) ? 0 : envioEmail.hashCode());
		result = prime * result + ((forma == null) ? 0 : forma.hashCode());
		result = prime * result
				+ ((formaEmissao == null) ? 0 : formaEmissao.hashCode());
		result = prime * result
				+ ((idPolitica == null) ? 0 : idPolitica.hashCode());
		result = prime * result + (principal ? 1231 : 1237);
		result = prime
				* result
				+ ((valorMinimoEmissao == null) ? 0 : valorMinimoEmissao
						.hashCode());
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
		ParametroCobrancaVO other = (ParametroCobrancaVO) obj;
		if (acumulaDivida == null) {
			if (other.acumulaDivida != null)
				return false;
		} else if (!acumulaDivida.equals(other.acumulaDivida))
			return false;
		if (banco == null) {
			if (other.banco != null)
				return false;
		} else if (!banco.equals(other.banco))
			return false;
		if (cobrancaUnificada == null) {
			if (other.cobrancaUnificada != null)
				return false;
		} else if (!cobrancaUnificada.equals(other.cobrancaUnificada))
			return false;
		if (envioEmail == null) {
			if (other.envioEmail != null)
				return false;
		} else if (!envioEmail.equals(other.envioEmail))
			return false;
		if (forma == null) {
			if (other.forma != null)
				return false;
		} else if (!forma.equals(other.forma))
			return false;
		if (formaEmissao == null) {
			if (other.formaEmissao != null)
				return false;
		} else if (!formaEmissao.equals(other.formaEmissao))
			return false;
		if (idPolitica == null) {
			if (other.idPolitica != null)
				return false;
		} else if (!idPolitica.equals(other.idPolitica))
			return false;
		if (principal != other.principal)
			return false;
		if (valorMinimoEmissao == null) {
			if (other.valorMinimoEmissao != null)
				return false;
		} else if (!valorMinimoEmissao.equals(other.valorMinimoEmissao))
			return false;
		return true;
	}

	/**
	 * @return the fornecedores
	 */
	public String getFornecedores() {
		return fornecedores;
	}

	/**
	 * @param fornecedores the fornecedores to set
	 */
	public void setFornecedores(String fornecedores) {
		this.fornecedores = fornecedores;
	}

	/**
	 * @return the concentracaoPagamentos
	 */
	public String getConcentracaoPagamentos() {
		return concentracaoPagamentos;
	}

	/**
	 * @param concentracaoPagamentos the concentracaoPagamentos to set
	 */
	public void setConcentracaoPagamentos(String concentracaoPagamentos) {
		this.concentracaoPagamentos = concentracaoPagamentos;
	}

}
