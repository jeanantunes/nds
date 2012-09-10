package br.com.abril.nds.dto;
import java.math.BigDecimal;

import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;

public class FormaCobrancaCaucaoLiquidaDTO {
	
	Long idFormaCobrancaCaucaoLiquida;
	Long idCota;
	Long idCaucaoLiquida;
	
	//DADOS DO FORMULARIO
	TipoCobrancaCotaGarantia tipoCobranca;
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
	
	Integer qtdeParcelas;
	BigDecimal valorParcela;
	
	BigDecimal valorDescontoAtual;
	BigDecimal utilizarDesconto;
	BigDecimal descontoCotaDesconto;
	
	BigDecimal valor;

	public FormaCobrancaCaucaoLiquidaDTO(){
		
	}
	
	//CONTRUTOR PARA O FORMULARIO
	public FormaCobrancaCaucaoLiquidaDTO(long idCota, TipoCobrancaCotaGarantia tipoCobranca, 
			TipoFormaCobranca tipoFormaCobranca) {
		super();
		this.idCota = idCota;
		this.tipoCobranca = tipoCobranca;
		this.tipoFormaCobranca = tipoFormaCobranca;
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

	public TipoCobrancaCotaGarantia getTipoCobranca() {
		return tipoCobranca;
	}

	public void setTipoCobranca(TipoCobrancaCotaGarantia tipoCobranca) {
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

	public BigDecimal getValorDescontoAtual() {
		return valorDescontoAtual;
	}

	public void setValorDescontoAtual(BigDecimal valorDescontoAtual) {
		this.valorDescontoAtual = valorDescontoAtual;
	}

	public BigDecimal getUtilizarDesconto() {
		return utilizarDesconto;
	}

	public void setUtilizarDesconto(BigDecimal utilizarDesconto) {
		this.utilizarDesconto = utilizarDesconto;
	}

	public BigDecimal getDescontoCotaDesconto() {
		return descontoCotaDesconto;
	}

	public void setDescontoCotaDesconto(BigDecimal descontoCotaDesconto) {
		this.descontoCotaDesconto = descontoCotaDesconto;
	}
}
