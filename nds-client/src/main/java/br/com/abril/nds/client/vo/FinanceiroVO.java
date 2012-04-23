package br.com.abril.nds.client.vo;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class FinanceiroVO {

	Long idCota;
	Integer numCota;
	
	TipoCobranca tipoCobranca;
	long idBanco;
	boolean recebeEmail;
	
	String numBanco;
	String nomeBanco;
	String agencia;
	String agenciaDigito;
	String conta;
	String contaDigito;
	
	long fatorVencimento;
	boolean sugereSuspensao;
	boolean contrato;

	boolean domingo;
	boolean segunda;
	boolean terca;
	boolean quarta;
	boolean quinta;
	boolean sexta;
	boolean sabado;
	
	BigDecimal valorMinimo;
	BigDecimal comissao;
	Integer qtdDividasAberto;
	BigDecimal vrDividasAberto;
	
	
	public FinanceiroVO() {
		
	}


	public FinanceiroVO(Integer numCota, TipoCobranca tipoCobranca,
			long idBanco, boolean recebeEmail, String numBanco,
			String nomeBanco, String agencia, String agenciaDigito,
			String conta, String contaDigito, long fatorVencimento,
			boolean sugereSuspensao, boolean contrato, boolean domingo,
			boolean segunda, boolean terca, boolean quarta, boolean quinta,
			boolean sexta, boolean sabado, BigDecimal valorMinimo,
			BigDecimal comissao, Integer qtdDividasAberto,
			BigDecimal vrDividasAberto) {
		super();
		this.numCota = numCota;
		this.tipoCobranca = tipoCobranca;
		this.idBanco = idBanco;
		this.recebeEmail = recebeEmail;
		this.numBanco = numBanco;
		this.nomeBanco = nomeBanco;
		this.agencia = agencia;
		this.agenciaDigito = agenciaDigito;
		this.conta = conta;
		this.contaDigito = contaDigito;
		this.fatorVencimento = fatorVencimento;
		this.sugereSuspensao = sugereSuspensao;
		this.contrato = contrato;
		this.domingo = domingo;
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
		this.valorMinimo = valorMinimo;
		this.comissao = comissao;
		this.qtdDividasAberto = qtdDividasAberto;
		this.vrDividasAberto = vrDividasAberto;
	}

	
	public Long getIdCota() {
		return idCota;
	}


	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	
	public Integer getNumCota() {
		return numCota;
	}


	public void setNumCota(Integer numCota) {
		this.numCota = numCota;
	}
	

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}


	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}


	public long getIdBanco() {
		return idBanco;
	}


	public void setIdBanco(long idBanco) {
		this.idBanco = idBanco;
	}


	public boolean isRecebeEmail() {
		return recebeEmail;
	}


	public void setRecebeEmail(boolean recebeEmail) {
		this.recebeEmail = recebeEmail;
	}


	public String getNumBanco() {
		return numBanco;
	}


	public void setNumBanco(String numBanco) {
		this.numBanco = numBanco;
	}


	public String getNomeBanco() {
		return nomeBanco;
	}


	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}


	public String getAgencia() {
		return agencia;
	}


	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}


	public String getAgenciaDigito() {
		return agenciaDigito;
	}


	public void setAgenciaDigito(String agenciaDigito) {
		this.agenciaDigito = agenciaDigito;
	}


	public String getConta() {
		return conta;
	}


	public void setConta(String conta) {
		this.conta = conta;
	}


	public String getContaDigito() {
		return contaDigito;
	}


	public void setContaDigito(String contaDigito) {
		this.contaDigito = contaDigito;
	}


	public long getFatorVencimento() {
		return fatorVencimento;
	}


	public void setFatorVencimento(long fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}


	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}


	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
	}


	public boolean isContrato() {
		return contrato;
	}


	public void setContrato(boolean contrato) {
		this.contrato = contrato;
	}


	public boolean isDomingo() {
		return domingo;
	}


	public void setDomingo(boolean domingo) {
		this.domingo = domingo;
	}


	public boolean isSegunda() {
		return segunda;
	}


	public void setSegunda(boolean segunda) {
		this.segunda = segunda;
	}


	public boolean isTerca() {
		return terca;
	}


	public void setTerca(boolean terca) {
		this.terca = terca;
	}


	public boolean isQuarta() {
		return quarta;
	}


	public void setQuarta(boolean quarta) {
		this.quarta = quarta;
	}


	public boolean isQuinta() {
		return quinta;
	}


	public void setQuinta(boolean quinta) {
		this.quinta = quinta;
	}


	public boolean isSexta() {
		return sexta;
	}


	public void setSexta(boolean sexta) {
		this.sexta = sexta;
	}


	public boolean isSabado() {
		return sabado;
	}


	public void setSabado(boolean sabado) {
		this.sabado = sabado;
	}


	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}


	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}


	public BigDecimal getComissao() {
		return comissao;
	}


	public void setComissao(BigDecimal comissao) {
		this.comissao = comissao;
	}


	public Integer getQtdDividasAberto() {
		return qtdDividasAberto;
	}


	public void setQtdDividasAberto(Integer qtdDividasAberto) {
		this.qtdDividasAberto = qtdDividasAberto;
	}


	public BigDecimal getVrDividasAberto() {
		return vrDividasAberto;
	}


	public void setVrDividasAberto(BigDecimal vrDividasAberto) {
		this.vrDividasAberto = vrDividasAberto;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agencia == null) ? 0 : agencia.hashCode());
		result = prime * result
				+ ((agenciaDigito == null) ? 0 : agenciaDigito.hashCode());
		result = prime * result
				+ ((comissao == null) ? 0 : comissao.hashCode());
		result = prime * result + ((conta == null) ? 0 : conta.hashCode());
		result = prime * result
				+ ((contaDigito == null) ? 0 : contaDigito.hashCode());
		result = prime * result + (contrato ? 1231 : 1237);
		result = prime * result + (domingo ? 1231 : 1237);
		result = prime * result
				+ (int) (fatorVencimento ^ (fatorVencimento >>> 32));
		result = prime * result + (int) (idBanco ^ (idBanco >>> 32));
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
		result = prime * result
				+ ((nomeBanco == null) ? 0 : nomeBanco.hashCode());
		result = prime * result
				+ ((numBanco == null) ? 0 : numBanco.hashCode());
		result = prime * result + ((numCota == null) ? 0 : numCota.hashCode());
		result = prime
				* result
				+ ((qtdDividasAberto == null) ? 0 : qtdDividasAberto.hashCode());
		result = prime * result + (quarta ? 1231 : 1237);
		result = prime * result + (quinta ? 1231 : 1237);
		result = prime * result + (recebeEmail ? 1231 : 1237);
		result = prime * result + (sabado ? 1231 : 1237);
		result = prime * result + (segunda ? 1231 : 1237);
		result = prime * result + (sexta ? 1231 : 1237);
		result = prime * result + (sugereSuspensao ? 1231 : 1237);
		result = prime * result + (terca ? 1231 : 1237);
		result = prime * result
				+ ((tipoCobranca == null) ? 0 : tipoCobranca.hashCode());
		result = prime * result
				+ ((valorMinimo == null) ? 0 : valorMinimo.hashCode());
		result = prime * result
				+ ((vrDividasAberto == null) ? 0 : vrDividasAberto.hashCode());
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
		FinanceiroVO other = (FinanceiroVO) obj;
		if (agencia == null) {
			if (other.agencia != null)
				return false;
		} else if (!agencia.equals(other.agencia))
			return false;
		if (agenciaDigito == null) {
			if (other.agenciaDigito != null)
				return false;
		} else if (!agenciaDigito.equals(other.agenciaDigito))
			return false;
		if (comissao == null) {
			if (other.comissao != null)
				return false;
		} else if (!comissao.equals(other.comissao))
			return false;
		if (conta == null) {
			if (other.conta != null)
				return false;
		} else if (!conta.equals(other.conta))
			return false;
		if (contaDigito == null) {
			if (other.contaDigito != null)
				return false;
		} else if (!contaDigito.equals(other.contaDigito))
			return false;
		if (contrato != other.contrato)
			return false;
		if (domingo != other.domingo)
			return false;
		if (fatorVencimento != other.fatorVencimento)
			return false;
		if (idBanco != other.idBanco)
			return false;
		if (idCota == null) {
			if (other.idCota != null)
				return false;
		} else if (!idCota.equals(other.idCota))
			return false;
		if (nomeBanco == null) {
			if (other.nomeBanco != null)
				return false;
		} else if (!nomeBanco.equals(other.nomeBanco))
			return false;
		if (numBanco == null) {
			if (other.numBanco != null)
				return false;
		} else if (!numBanco.equals(other.numBanco))
			return false;
		if (numCota == null) {
			if (other.numCota != null)
				return false;
		} else if (!numCota.equals(other.numCota))
			return false;
		if (qtdDividasAberto == null) {
			if (other.qtdDividasAberto != null)
				return false;
		} else if (!qtdDividasAberto.equals(other.qtdDividasAberto))
			return false;
		if (quarta != other.quarta)
			return false;
		if (quinta != other.quinta)
			return false;
		if (recebeEmail != other.recebeEmail)
			return false;
		if (sabado != other.sabado)
			return false;
		if (segunda != other.segunda)
			return false;
		if (sexta != other.sexta)
			return false;
		if (sugereSuspensao != other.sugereSuspensao)
			return false;
		if (terca != other.terca)
			return false;
		if (tipoCobranca != other.tipoCobranca)
			return false;
		if (valorMinimo == null) {
			if (other.valorMinimo != null)
				return false;
		} else if (!valorMinimo.equals(other.valorMinimo))
			return false;
		if (vrDividasAberto == null) {
			if (other.vrDividasAberto != null)
				return false;
		} else if (!vrDividasAberto.equals(other.vrDividasAberto))
			return false;
		return true;
	}
	
	
}