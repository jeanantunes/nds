package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.model.cadastro.FormaCobrancaBoleto;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public class ParametroCobrancaDTO {

	
	Long idPolitica;
	Long idBanco;
	
	BigDecimal taxaMulta;
	BigDecimal valorMulta;
	BigDecimal taxaJuros;
	
	TipoCobranca tipoCobranca;
	FormaEmissao formaEmissao;
	TipoFormaCobranca tipoFormaCobranca;
	
	boolean vencimentoDiaUtil;
	boolean acumulaDivida;
	boolean unificada;
	boolean envioEmail;
	boolean principal;
	
	String instrucoes1;
	String instrucoes2;
	String instrucoes3;
	String instrucoes4;
	
	//DADOS DA FORMA DE COBRANÇA VINCULADA
	private List<Integer> diasDoMes;
	
	boolean domingo;
	boolean segunda;
	boolean terca;
	boolean quarta;
	boolean quinta;
	boolean sexta;
	boolean sabado;
	
	private List<Long> fornecedoresId;
	
	private FormaCobrancaBoleto formaCobrancaBoleto;	
	
	private Integer fatorVencimento;
	
	private Long idFornecedorPadrao;
	
	private TipoCota tipoCota;

	public ParametroCobrancaDTO() {
		
	}


	public ParametroCobrancaDTO(Long idPolitica, Long idBanco, Long idFornecedorPadrao,
			BigDecimal valorMinimo, BigDecimal taxaMulta,
			BigDecimal valorMulta, BigDecimal taxaJuros,
			TipoCobranca tipoCobranca, FormaEmissao formaEmissao,
			TipoFormaCobranca tipoFormaCobranca, boolean vencimentoDiaUtil,
			boolean acumulaDivida, boolean unificada, boolean envioEmail,
			boolean principal,boolean cobradoPeloBackoffice, String instrucoes1,
			String instrucoes2, String instrucoes3, String instrucoes4, List<Integer> diasDoMes,
			boolean domingo, boolean segunda, boolean terca, boolean quarta,
			boolean quinta, boolean sexta, boolean sabado,
			List<Long> fornecedoresId) {
		super();
		this.idPolitica = idPolitica;
		this.idBanco = idBanco;
		this.idFornecedorPadrao = idFornecedorPadrao;
		this.taxaMulta = taxaMulta;
		this.valorMulta = valorMulta;
		this.taxaJuros = taxaJuros;
		this.tipoCobranca = tipoCobranca;
		this.formaEmissao = formaEmissao;
		this.vencimentoDiaUtil = vencimentoDiaUtil;
		this.acumulaDivida = acumulaDivida;
		this.unificada = unificada;
		this.envioEmail = envioEmail;
		this.principal = principal;
		this.instrucoes1 = instrucoes1;
		this.instrucoes2 = instrucoes2;
		this.instrucoes3 = instrucoes3;
		this.instrucoes4 = instrucoes4;
		this.diasDoMes = diasDoMes;
		this.domingo = domingo;
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
		this.fornecedoresId = fornecedoresId;
	}


	public Long getIdPolitica() {
		return idPolitica;
	}


	public void setIdPolitica(Long idPolitica) {
		this.idPolitica = idPolitica;
	}


	public Long getIdBanco() {
		return idBanco;
	}


	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
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


	public boolean isEnvioEmail() {
		return envioEmail;
	}


	public void setEnvioEmail(boolean envioEmail) {
		this.envioEmail = envioEmail;
	}


	public boolean isPrincipal() {
		return principal;
	}


	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}



	public String getInstrucoes1() {
		return instrucoes1;
	}


	public void setInstrucoes1(String instrucoes1) {
		this.instrucoes1 = instrucoes1;
	}


	public String getInstrucoes2() {
		return instrucoes2;
	}


	public void setInstrucoes2(String instrucoes2) {
		this.instrucoes2 = instrucoes2;
	}


	public String getInstrucoes3() {
		return instrucoes3;
	}


	public void setInstrucoes3(String instrucoes3) {
		this.instrucoes3 = instrucoes3;
	}


	public String getInstrucoes4() {
		return instrucoes4;
	}


	public void setInstrucoes4(String instrucoes4) {
		this.instrucoes4 = instrucoes4;
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


	/**
	 * @return the diasDoMes
	 */
	public List<Integer> getDiasDoMes() {
		return diasDoMes;
	}


	/**
	 * @param diasDoMes the diasDoMes to set
	 */
	public void setDiasDoMes(List<Integer> diasDoMes) {
		this.diasDoMes = diasDoMes;
	}


	/**
	 * @return the formaCobrancaBoleto
	 */
	public FormaCobrancaBoleto getFormaCobrancaBoleto() {
		return formaCobrancaBoleto;
	}


	/**
	 * @param formaCobrancaBoleto the formaCobrancaBoleto to set
	 */
	public void setFormaCobrancaBoleto(FormaCobrancaBoleto formaCobrancaBoleto) {
		this.formaCobrancaBoleto = formaCobrancaBoleto;
	}


	public Integer getFatorVencimento() {
		return fatorVencimento;
	}


	public void setFatorVencimento(Integer fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}


	public Long getIdFornecedorPadrao() {
		return idFornecedorPadrao;
	}


	public void setIdFornecedorPadrao(Long idFornecedorPadrao) {
		this.idFornecedorPadrao = idFornecedorPadrao;
	}


	public TipoCota getTipoCota() {
		return tipoCota;
	}


	public void setTipoCota(TipoCota tipoCota) {
		this.tipoCota = tipoCota;
	}

}