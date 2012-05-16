package br.com.abril.nds.client.vo;


public class ParametroCobrancaVO {
	
	Long idParametro;
	String forma;
	String banco;
	String vlr_minimo_emissao;
	String formaEmissao;
	String acumula_divida;
	String cobranca_unificada;
	String envio_email;
	boolean principal;
	
	public Long getIdParametro() {
		return idParametro;
	}
	
	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
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
	
	public String getVlr_minimo_emissao() {
		return vlr_minimo_emissao;
	}
	
	public void setVlr_minimo_emissao(String vlr_minimo_emissao) {
		this.vlr_minimo_emissao = vlr_minimo_emissao;
	}
	
	public String getFormaEmissao() {
		return formaEmissao;
	}
	public void setFormaEmissao(String formaEmissao) {
		this.formaEmissao = formaEmissao;
	}
	
	public String getAcumula_divida() {
		return acumula_divida;
	}
	
	public void setAcumula_divida(String acumula_divida) {
		this.acumula_divida = acumula_divida;
	}
	
	public String getCobranca_unificada() {
		return cobranca_unificada;
	}
	
	public void setCobranca_unificada(String cobranca_unificada) {
		this.cobranca_unificada = cobranca_unificada;
	}
	
	public String getEnvio_email() {
		return envio_email;
	}
	
	public void setEnvio_email(String envio_email) {
		this.envio_email = envio_email;
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
				+ ((acumula_divida == null) ? 0 : acumula_divida.hashCode());
		result = prime * result + ((banco == null) ? 0 : banco.hashCode());
		result = prime
				* result
				+ ((cobranca_unificada == null) ? 0 : cobranca_unificada
						.hashCode());
		result = prime * result
				+ ((envio_email == null) ? 0 : envio_email.hashCode());
		result = prime * result + ((forma == null) ? 0 : forma.hashCode());
		result = prime * result
				+ ((formaEmissao == null) ? 0 : formaEmissao.hashCode());
		result = prime * result
				+ ((idParametro == null) ? 0 : idParametro.hashCode());
		result = prime * result + (principal ? 1231 : 1237);
		result = prime
				* result
				+ ((vlr_minimo_emissao == null) ? 0 : vlr_minimo_emissao
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
		if (acumula_divida == null) {
			if (other.acumula_divida != null)
				return false;
		} else if (!acumula_divida.equals(other.acumula_divida))
			return false;
		if (banco == null) {
			if (other.banco != null)
				return false;
		} else if (!banco.equals(other.banco))
			return false;
		if (cobranca_unificada == null) {
			if (other.cobranca_unificada != null)
				return false;
		} else if (!cobranca_unificada.equals(other.cobranca_unificada))
			return false;
		if (envio_email == null) {
			if (other.envio_email != null)
				return false;
		} else if (!envio_email.equals(other.envio_email))
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
		if (idParametro == null) {
			if (other.idParametro != null)
				return false;
		} else if (!idParametro.equals(other.idParametro))
			return false;
		if (principal != other.principal)
			return false;
		if (vlr_minimo_emissao == null) {
			if (other.vlr_minimo_emissao != null)
				return false;
		} else if (!vlr_minimo_emissao.equals(other.vlr_minimo_emissao))
			return false;
		return true;
	}

}
