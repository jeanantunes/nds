package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public class ParametroCobrancaDTO {

	
	Long idParametro;
	Long idBanco;
	
	BigDecimal valorMinimo;
	BigDecimal taxaMulta;
	BigDecimal valorMulta;
	BigDecimal taxaJuros;
	
	TipoCobranca tipoCobranca;
	FormaEmissao formaEmissao;
	TipoFormaCobranca tipoFormaCobranca;
	
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
			TipoFormaCobranca tipoFormaCobranca, boolean vencimentoDiaUtil,
			boolean acumulaDivida, boolean unificada, boolean evioEmail,
			boolean principal, String instrucoes, Integer diaDoMes,
			boolean domingo, boolean segunda, boolean terca, boolean quarta,
			boolean quinta, boolean sexta, boolean sabado,
			List<Long> fornecedoresId) {
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


	public TipoFormaCobranca getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}


	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
	}

}