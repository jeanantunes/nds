package br.com.abril.nds.client.vo;


public class ParametroCobrancaVO {
	
	Long idParametro;
	String formaPagamento;
	String banco;
	String valorMinimoEmissao;
	String formaEmissao;

	boolean vencimentoDiaUtil;
	boolean acumulaDivida;
	boolean cobrancaUnificada;
	boolean evioPorEmail;
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
	
	public boolean isVencimentoDiaUtil() {
		return vencimentoDiaUtil;
	}

	public void setVencimentoDiaUtil(boolean vencimentoDiaUtil) {
		this.vencimentoDiaUtil = vencimentoDiaUtil;
	}

	public boolean isAcumulaDivida() {
		return acumulaDivida;
	}

	public void setAcumulaDivida(boolean acumulaDivida) {
		this.acumulaDivida = acumulaDivida;
	}

	public boolean isCobrancaUnificada() {
		return cobrancaUnificada;
	}

	public void setCobrancaUnificada(boolean cobrancaUnificada) {
		this.cobrancaUnificada = cobrancaUnificada;
	}

	public boolean isEvioPorEmail() {
		return evioPorEmail;
	}

	public void setEvioPorEmail(boolean evioPorEmail) {
		this.evioPorEmail = evioPorEmail;
	}

	public String getFormaEmissao() {
		return formaEmissao;
	}
	
	public void setFormaEmissao(String formaEmissao) {
		this.formaEmissao = formaEmissao;
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
		result = prime * result + (acumulaDivida ? 1231 : 1237);
		result = prime * result + ((banco == null) ? 0 : banco.hashCode());
		result = prime * result + (cobrancaUnificada ? 1231 : 1237);
		result = prime * result + (evioPorEmail ? 1231 : 1237);
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
		result = prime * result + (vencimentoDiaUtil ? 1231 : 1237);
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
		if (acumulaDivida != other.acumulaDivida)
			return false;
		if (banco == null) {
			if (other.banco != null)
				return false;
		} else if (!banco.equals(other.banco))
			return false;
		if (cobrancaUnificada != other.cobrancaUnificada)
			return false;
		if (evioPorEmail != other.evioPorEmail)
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
		if (vencimentoDiaUtil != other.vencimentoDiaUtil)
			return false;
		return true;
	}

}
