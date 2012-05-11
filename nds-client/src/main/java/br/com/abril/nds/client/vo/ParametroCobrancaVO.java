package br.com.abril.nds.client.vo;


public class ParametroCobrancaVO {
	
	Long idParametro;
	String formaPagamento;
	String banco;
	String valorMinimoEmissao;
	String acumulaDivida;
	String cobrancaUnificada;
	String formaEmissao;
	String evioPorEmail;
	boolean principal;
	
	
	public String getFormaPagamento() {
		return formaPagamento;
	}
	
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
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
	
	public void setValorMinimoEmissao(String valorMinimoEmissao) {
		this.valorMinimoEmissao = valorMinimoEmissao;
	}
	
	public String getAcumulaDivida() {
		return acumulaDivida;
	}
	
	public void setAcumulaDivida(String acumulaDivida) {
		this.acumulaDivida = acumulaDivida;
	}
	
	public String getCobrancaUnificada() {
		return cobrancaUnificada;
	}
	
	public void setCobrancaUnificada(String cobrancaUnificada) {
		this.cobrancaUnificada = cobrancaUnificada;
	}
	
	public String getFormaEmissao() {
		return formaEmissao;
	}
	
	public void setFormaEmissao(String formaEmissao) {
		this.formaEmissao = formaEmissao;
	}
	
	public String getEvioPorEmail() {
		return evioPorEmail;
	}
	
	public void setEvioPorEmail(String evioPorEmail) {
		this.evioPorEmail = evioPorEmail;
	}
	
	public Long getIdParametro() {
		return idParametro;
	}
	
	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
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
				+ ((evioPorEmail == null) ? 0 : evioPorEmail.hashCode());
		result = prime * result
				+ ((formaEmissao == null) ? 0 : formaEmissao.hashCode());
		result = prime * result
				+ ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
		result = prime * result
				+ ((idParametro == null) ? 0 : idParametro.hashCode());
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
		if (evioPorEmail == null) {
			if (other.evioPorEmail != null)
				return false;
		} else if (!evioPorEmail.equals(other.evioPorEmail))
			return false;
		if (formaEmissao == null) {
			if (other.formaEmissao != null)
				return false;
		} else if (!formaEmissao.equals(other.formaEmissao))
			return false;
		if (formaPagamento == null) {
			if (other.formaPagamento != null)
				return false;
		} else if (!formaPagamento.equals(other.formaPagamento))
			return false;
		if (idParametro == null) {
			if (other.idParametro != null)
				return false;
		} else if (!idParametro.equals(other.idParametro))
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

}
