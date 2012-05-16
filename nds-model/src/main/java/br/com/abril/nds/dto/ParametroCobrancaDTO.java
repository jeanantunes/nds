package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;

public class ParametroCobrancaDTO {

	
	Long idParametro;
	Long idBanco;
	
	BigDecimal valorMinimo;
	BigDecimal taxaMulta;
	BigDecimal valorMulta;
	BigDecimal taxaJuros;
	
	TipoCobranca tipoCobranca;
	FormaEmissao formaEmissao;
	
	boolean vencimentoDiaUtil;
	boolean acumulaDivida;
	boolean unificada;
	boolean evioEmail;
	boolean principal;
	
	String instrucoes;
	
	//DADOS DA FORMA DE COBRANÇA VINCULADA
	Integer diaDoMes;
	boolean domingo;
	boolean segunda;
	boolean terca;
	boolean quarta;
	boolean quinta;
	boolean sexta;
	boolean sabado;
	
	List<Long> fornecedoresId;

	
	public ParametroCobrancaDTO() {
		
	}


	public ParametroCobrancaDTO(Long idParametro, Long idBanco,
			BigDecimal valorMinimo, BigDecimal taxaMulta,
			BigDecimal valorMulta, BigDecimal taxaJuros,
			TipoCobranca tipoCobranca, FormaEmissao formaEmissao,
			boolean vencimentoDiaUtil, boolean acumulaDivida,
			boolean unificada, boolean evioEmail, boolean principal,
			String instrucoes, Integer diaDoMes, boolean domingo,
			boolean segunda, boolean terca, boolean quarta, boolean quinta,
			boolean sexta, boolean sabado, List<Long> fornecedoresId) {
		super();
		this.idParametro = idParametro;
		this.idBanco = idBanco;
		this.valorMinimo = valorMinimo;
		this.taxaMulta = taxaMulta;
		this.valorMulta = valorMulta;
		this.taxaJuros = taxaJuros;
		this.tipoCobranca = tipoCobranca;
		this.formaEmissao = formaEmissao;
		this.vencimentoDiaUtil = vencimentoDiaUtil;
		this.acumulaDivida = acumulaDivida;
		this.unificada = unificada;
		this.evioEmail = evioEmail;
		this.principal = principal;
		this.instrucoes = instrucoes;
		this.diaDoMes = diaDoMes;
		this.domingo = domingo;
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
		this.fornecedoresId = fornecedoresId;
	}


	public Long getIdParametro() {
		return idParametro;
	}


	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}


	public Long getIdBanco() {
		return idBanco;
	}


	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}


	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}


	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}


	public BigDecimal getTaxaMulta() {
		return taxaMulta;
	}


	public void setTaxaMulta(BigDecimal taxaMulta) {
		this.taxaMulta = taxaMulta;
	}


	public BigDecimal getValorMulta() {
		return valorMulta;
	}


	public void setValorMulta(BigDecimal valorMulta) {
		this.valorMulta = valorMulta;
	}


	public BigDecimal getTaxaJuros() {
		return taxaJuros;
	}


	public void setTaxaJuros(BigDecimal taxaJuros) {
		this.taxaJuros = taxaJuros;
	}


	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}


	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}


	public FormaEmissao getFormaEmissao() {
		return formaEmissao;
	}


	public void setFormaEmissao(FormaEmissao formaEmissao) {
		this.formaEmissao = formaEmissao;
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


	public boolean isUnificada() {
		return unificada;
	}


	public void setUnificada(boolean unificada) {
		this.unificada = unificada;
	}


	public boolean isEvioEmail() {
		return evioEmail;
	}


	public void setEvioEmail(boolean evioEmail) {
		this.evioEmail = evioEmail;
	}


	public boolean isPrincipal() {
		return principal;
	}


	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}


	public String getInstrucoes() {
		return instrucoes;
	}


	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}

	public Integer getDiaDoMes() {
		return diaDoMes;
	}


	public void setDiaDoMes(Integer diaDoMes) {
		this.diaDoMes = diaDoMes;
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


	public List<Long> getFornecedoresId() {
		return fornecedoresId;
	}


	public void setFornecedoresId(List<Long> fornecedoresId) {
		this.fornecedoresId = fornecedoresId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (acumulaDivida ? 1231 : 1237);
		result = prime * result
				+ ((diaDoMes == null) ? 0 : diaDoMes.hashCode());
		result = prime * result + (domingo ? 1231 : 1237);
		result = prime * result + (evioEmail ? 1231 : 1237);
		result = prime * result
				+ ((formaEmissao == null) ? 0 : formaEmissao.hashCode());
		result = prime * result
				+ ((fornecedoresId == null) ? 0 : fornecedoresId.hashCode());
		result = prime * result + ((idBanco == null) ? 0 : idBanco.hashCode());
		result = prime * result
				+ ((idParametro == null) ? 0 : idParametro.hashCode());
		result = prime * result
				+ ((instrucoes == null) ? 0 : instrucoes.hashCode());
		result = prime * result + (principal ? 1231 : 1237);
		result = prime * result + (quarta ? 1231 : 1237);
		result = prime * result + (quinta ? 1231 : 1237);
		result = prime * result + (sabado ? 1231 : 1237);
		result = prime * result + (segunda ? 1231 : 1237);
		result = prime * result + (sexta ? 1231 : 1237);
		result = prime * result
				+ ((taxaJuros == null) ? 0 : taxaJuros.hashCode());
		result = prime * result
				+ ((taxaMulta == null) ? 0 : taxaMulta.hashCode());
		result = prime * result + (terca ? 1231 : 1237);
		result = prime * result
				+ ((tipoCobranca == null) ? 0 : tipoCobranca.hashCode());
		result = prime * result + (unificada ? 1231 : 1237);
		result = prime * result
				+ ((valorMinimo == null) ? 0 : valorMinimo.hashCode());
		result = prime * result
				+ ((valorMulta == null) ? 0 : valorMulta.hashCode());
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
		ParametroCobrancaDTO other = (ParametroCobrancaDTO) obj;
		if (acumulaDivida != other.acumulaDivida)
			return false;
		if (diaDoMes == null) {
			if (other.diaDoMes != null)
				return false;
		} else if (!diaDoMes.equals(other.diaDoMes))
			return false;
		if (domingo != other.domingo)
			return false;
		if (evioEmail != other.evioEmail)
			return false;
		if (formaEmissao != other.formaEmissao)
			return false;
		if (fornecedoresId == null) {
			if (other.fornecedoresId != null)
				return false;
		} else if (!fornecedoresId.equals(other.fornecedoresId))
			return false;
		if (idBanco == null) {
			if (other.idBanco != null)
				return false;
		} else if (!idBanco.equals(other.idBanco))
			return false;
		if (idParametro == null) {
			if (other.idParametro != null)
				return false;
		} else if (!idParametro.equals(other.idParametro))
			return false;
		if (instrucoes == null) {
			if (other.instrucoes != null)
				return false;
		} else if (!instrucoes.equals(other.instrucoes))
			return false;
		if (principal != other.principal)
			return false;
		if (quarta != other.quarta)
			return false;
		if (quinta != other.quinta)
			return false;
		if (sabado != other.sabado)
			return false;
		if (segunda != other.segunda)
			return false;
		if (sexta != other.sexta)
			return false;
		if (taxaJuros == null) {
			if (other.taxaJuros != null)
				return false;
		} else if (!taxaJuros.equals(other.taxaJuros))
			return false;
		if (taxaMulta == null) {
			if (other.taxaMulta != null)
				return false;
		} else if (!taxaMulta.equals(other.taxaMulta))
			return false;
		if (terca != other.terca)
			return false;
		if (tipoCobranca != other.tipoCobranca)
			return false;
		if (unificada != other.unificada)
			return false;
		if (valorMinimo == null) {
			if (other.valorMinimo != null)
				return false;
		} else if (!valorMinimo.equals(other.valorMinimo))
			return false;
		if (valorMulta == null) {
			if (other.valorMulta != null)
				return false;
		} else if (!valorMulta.equals(other.valorMulta))
			return false;
		if (vencimentoDiaUtil != other.vencimentoDiaUtil)
			return false;
		return true;
	}


}