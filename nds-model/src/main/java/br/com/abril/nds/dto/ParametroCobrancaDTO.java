package br.com.abril.nds.dto;

import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoCobranca;

public class ParametroCobrancaDTO {

	Long idParametroCobranca;
	Long idCota;
	Integer numCota;
	
	long fatorVencimento;
	boolean sugereSuspensao;
	boolean contrato;
	
	BigDecimal valorMinimo;
	BigDecimal comissao;
	Integer qtdDividasAberto;
	BigDecimal vrDividasAberto;
	
	
	public ParametroCobrancaDTO() {
		
	}


	public ParametroCobrancaDTO(Integer numCota, TipoCobranca tipoCobranca,
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
		this.fatorVencimento = fatorVencimento;
		this.sugereSuspensao = sugereSuspensao;
		this.contrato = contrato;
		this.valorMinimo = valorMinimo;
		this.comissao = comissao;
		this.qtdDividasAberto = qtdDividasAberto;
		this.vrDividasAberto = vrDividasAberto;
	}

	
	public Long getIdParametroCobranca() {
		return idParametroCobranca;
	}


	public void setIdParametroCobranca(Long idParametroCobranca) {
		this.idParametroCobranca = idParametroCobranca;
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
		result = prime * result
				+ ((comissao == null) ? 0 : comissao.hashCode());
		result = prime * result + (contrato ? 1231 : 1237);
		result = prime * result
				+ (int) (fatorVencimento ^ (fatorVencimento >>> 32));
		result = prime * result + ((idCota == null) ? 0 : idCota.hashCode());
		result = prime * result + ((numCota == null) ? 0 : numCota.hashCode());
		result = prime
				* result
				+ ((qtdDividasAberto == null) ? 0 : qtdDividasAberto.hashCode());
		result = prime * result + (sugereSuspensao ? 1231 : 1237);
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
		ParametroCobrancaDTO other = (ParametroCobrancaDTO) obj;
		if (comissao == null) {
			if (other.comissao != null)
				return false;
		} else if (!comissao.equals(other.comissao))
			return false;
		if (contrato != other.contrato)
			return false;
		if (fatorVencimento != other.fatorVencimento)
			return false;
		if (idCota == null) {
			if (other.idCota != null)
				return false;
		} else if (!idCota.equals(other.idCota))
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
		if (sugereSuspensao != other.sugereSuspensao)
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