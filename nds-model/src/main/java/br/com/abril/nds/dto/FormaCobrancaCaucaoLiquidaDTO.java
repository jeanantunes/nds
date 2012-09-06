package br.com.abril.nds.dto;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public class FormaCobrancaCaucaoLiquidaDTO {
	
	Long idFormaCobrancaCaucaoLiquida;
	Long idCota;
	Long idCaucaoLiquida;
	
	//DADOS DO FORMULARIO
	TipoCobranca tipoCobranca;
	TipoFormaCobranca tipoFormaCobranca;
	
	String numBanco;
	String nomeBanco;
	Long agencia;
	String agenciaDigito;
	Long conta;
	String contaDigito;
	String nomeCorrentista;	
	
	Integer diaDoMes;
	Integer primeiroDiaQuinzenal;
	Integer segundoDiaQuinzenal;
	boolean domingo;
	boolean segunda;
	boolean terca;
	boolean quarta;
	boolean quinta;
	boolean sexta;
	boolean sabado;
	
	BigDecimal valor;
	Integer qtdeParcelas;
	BigDecimal valorParcela;
	
	BigDecimal valorFormaPagamentoDeposito;

	public FormaCobrancaCaucaoLiquidaDTO(){
		
	}
	
	//CONTRUTOR PARA O FORMULARIO
	public FormaCobrancaCaucaoLiquidaDTO(long idCota, TipoCobranca tipoCobranca, 
			TipoFormaCobranca tipoFormaCobranca,String numBanco, String nomeBanco,String nomeCorrentista,
			Long agencia, String agenciaDigito, Long conta,
			String contaDigito, Integer diaDoMes, Integer primeiroDiaQuinzenal, Integer segundoDiaQuinzenal, boolean domingo, boolean segunda,
			boolean terca, boolean quarta, boolean quinta, boolean sexta,
			boolean sabado) {
		super();
		this.idCota = idCota;
		this.tipoCobranca = tipoCobranca;
		this.tipoFormaCobranca = tipoFormaCobranca;
		this.numBanco = numBanco;
		this.nomeBanco = nomeBanco;
		this.nomeCorrentista = nomeCorrentista;
		this.agencia = agencia;
		this.agenciaDigito = agenciaDigito;
		this.conta = conta;
		this.contaDigito = contaDigito;
		this.diaDoMes = diaDoMes;
		this.primeiroDiaQuinzenal = primeiroDiaQuinzenal;
		this.segundoDiaQuinzenal = segundoDiaQuinzenal;
		this.domingo = domingo;
		this.segunda = segunda;
		this.terca = terca;
		this.quarta = quarta;
		this.quinta = quinta;
		this.sexta = sexta;
		this.sabado = sabado;
	}

	public Long getIdFormaCobrancaCaucaoLiquida() {
		return idFormaCobrancaCaucaoLiquida;
	}

	public void setIdFormaCobrancaCaucaoLiquida(Long idFormaCobrancaCaucaoLiquida) {
		this.idFormaCobrancaCaucaoLiquida = idFormaCobrancaCaucaoLiquida;
	}

	public Long getIdCaucaoLiquida() {
		return idCaucaoLiquida;
	}

	public void setIdCaucaoLiquida(Long idCaucaoLiquida) {
		this.idCaucaoLiquida = idCaucaoLiquida;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}

	public TipoFormaCobranca getTipoFormaCobranca() {
		return tipoFormaCobranca;
	}

	public void setTipoFormaCobranca(TipoFormaCobranca tipoFormaCobranca) {
		this.tipoFormaCobranca = tipoFormaCobranca;
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

	public String getNomeCorrentista() {
		return nomeCorrentista;
	}

	public void setNomeCorrentista(String nomeCorrentista) {
		this.nomeCorrentista = nomeCorrentista;
	}

	public Long getAgencia() {
		return agencia;
	}

	public void setAgencia(Long agencia) {
		this.agencia = agencia;
	}

	public String getAgenciaDigito() {
		return agenciaDigito;
	}

	public void setAgenciaDigito(String agenciaDigito) {
		this.agenciaDigito = agenciaDigito;
	}

	public Long getConta() {
		return conta;
	}

	public void setConta(Long conta) {
		this.conta = conta;
	}

	public String getContaDigito() {
		return contaDigito;
	}

	public void setContaDigito(String contaDigito) {
		this.contaDigito = contaDigito;
	}

	public Integer getDiaDoMes() {
		return diaDoMes;
	}

	public void setDiaDoMes(Integer diaDoMes) {
		this.diaDoMes = diaDoMes;
	}

	public Integer getPrimeiroDiaQuinzenal() {
		return primeiroDiaQuinzenal;
	}

	public void setPrimeiroDiaQuinzenal(Integer primeiroDiaQuinzenal) {
		this.primeiroDiaQuinzenal = primeiroDiaQuinzenal;
	}

	public Integer getSegundoDiaQuinzenal() {
		return segundoDiaQuinzenal;
	}

	public void setSegundoDiaQuinzenal(Integer segundoDiaQuinzenal) {
		this.segundoDiaQuinzenal = segundoDiaQuinzenal;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Integer getQtdeParcelas() {
		return qtdeParcelas;
	}

	public void setQtdeParcelas(Integer qtdeParcelas) {
		this.qtdeParcelas = qtdeParcelas;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public BigDecimal getValorFormaPagamentoDeposito() {
		return valorFormaPagamentoDeposito;
	}

	public void setValorFormaPagamentoDeposito(
			BigDecimal valorFormaPagamentoDeposito) {
		this.valorFormaPagamentoDeposito = valorFormaPagamentoDeposito;
	}
}
