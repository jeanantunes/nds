package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatBoolean;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * @author erick.dzadotz
 * @version 1.0
 */
@Record
public class EMS0109Input extends IntegracaoDocument implements Serializable {

	private static final long serialVersionUID = -6140778050208950414L;

	private String codigoDistribuidor;
	
	private Date dataGeracaoArquivo;
	
	private Date horaGeracaoArquivo;
	
	private String mnemonicoTabela;
	
	private Integer contextoPublicacao;
	
	private Integer codigoFornecedor;
	
	private String codigoPublicacao;
	
	private String nomePublicacao;
	
	private Integer peb;
	
	private Integer pacotePadrao;
	
	private String condicaoTransmissaoHistograma;
	
	private String porcentagemAbrangencia;
	
	private String condicaoLancamentoImediato;
	
	private String formaComercializacao;
	
	private String formaPagamento;

	private String condicaoPagamentoAntecipado;
	
	private String condicaoPermissaoAlteracao;

	private String percetualCobrancaAntecipada;

	private String diasCobrancaAntecipada;

	private String slogan;

	private String comprimento;
	
	private String largura;

	private String espessura;

	private Long peso;

	private boolean status;

	private Date dataDesativacao;

	private String criterio1SelecaoCota;

	private String argumento1Criterio1;

	private String argumento2Criterio1;

	private String argumento3Criterio1;

	private String tipoCombinacao1;

	private String criterio2SelecaoCota;

	private String argumento1Criterio2;

	private String argumento2Criterio2;

	private String argumento3Criterio2;

	private String tipoCombinacao2;

	private String criterio3SelecaoCota;

	private String argumento1Criterio3;

	private String argumento2Criterio3;

	private String argumento3Criterio3;

	private String codigoTributacaoFiscal;

	private String codigoSituacaoTributaria;

	private String regimeRecolhimento;

	private Integer periodicidade;

	private Long categoria;

	private String colecao;

	private String algoritmo;

	private String codigoHistograma;

	private String tipoHistograma;

	private String contextoEditor;
	
	private Long codigoEditor; 

	private String tipoDesconto;
	
	private String percentualLimiteCotasFixadas;
	
	private String percentualLimiteFixacaoReparte;
	
	private String sentidoAbrangencia;
	
	private String codigoGrupoEditorial;
	
	private String codigoSubGrupoEditorial;
	
	/**
	 * Construtor Padrao
	 */
	public EMS0109Input() {
		
	}

	/**
	 * @return the codigoDistribuidor
	 */
	@Field(offset=1, length=7)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}

	/**
	 * @return the dataGeracaoArquivo
	 */
	/*@Field(offset=8, length=8)*/
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}

	/**
	 * @return the horaGeracaoArquivo
	 */
	/*@Field(offset=16, length=6)*/
	public Date getHoraGeracaoArquivo() {
		return horaGeracaoArquivo;
	}

	/**
	 * @return the mnemonicoTabela
	 */
	/*@Field(offset=22, length=4)*/
	public String getMnemonicoTabela() {
		return mnemonicoTabela;
	}

	/**
	 * @return the contextoPublicacao
	 */
	@Field(offset=26, length=2)
	public Integer getContextoPublicacao() {
		return contextoPublicacao;
	}

	/**
	 * @return the codigoFornecedor
	 */
	@Field(offset=28, length=7)
	public Integer getCodigoFornecedor() {
		return codigoFornecedor;
	}

	/**
	 * @return the codigoPublicacao
	 */
	@Field(offset=35, length=8)
	public String getCodigoPublicacao() {
		return codigoPublicacao;
	}

	/**
	 * @return the nomePublicacao
	 */
	@Field(offset=43, length=30)
	public String getNomePublicacao() {
		return nomePublicacao;
	}

	/**
	 * @return the peb
	 */
	@Field(offset=73, length=3)
	public Integer getPeb() {
		return peb;
	}

	/**
	 * @return the pacotePadrao
	 */
	@Field(offset=76, length=5)
	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	/**
	 * @return the condicaoTransmissaoHistograma
	 */
	@Field(offset=81, length=1)
	public String getCondicaoTransmissaoHistograma() {
		return condicaoTransmissaoHistograma;
	}

	/**
	 * @return the porcentagemAbrangencia
	 */
	@Field(offset=82, length=3)
	public String getPorcentagemAbrangencia() {
		return porcentagemAbrangencia;
	}

	/**
	 * @return the condicaoLancamentoImediato
	 */
	@Field(offset=85, length=1)
	public String getCondicaoLancamentoImediato() {
		return condicaoLancamentoImediato;
	}

	/**
	 * @return the formaComercializacao
	 */
	@Field(offset=86, length=3)
	public String getFormaComercializacao() {
		return formaComercializacao;
	}

	/**
	 * @return the formaPagamento
	 */
	@Field(offset=89, length=2)
	public String getFormaPagamento() {
		return formaPagamento;
	}

	/**
	 * @return the condicaoPagamentoAntecipado
	 */
	@Field(offset=91, length=1)
	public String getCondicaoPagamentoAntecipado() {
		return condicaoPagamentoAntecipado;
	}

	/**
	 * @return the condicaoPermissaoAlteracao
	 */
	@Field(offset=92, length=1)
	public String getCondicaoPermissaoAlteracao() {
		return condicaoPermissaoAlteracao;
	}

	/**
	 * @return the percetualCobrancaAntecipada
	 */
	@Field(offset=93, length=7)
	public String getPercetualCobrancaAntecipada() {
		return percetualCobrancaAntecipada;
	}

	/**
	 * @return the diasCobrancaAntecipada
	 */
	@Field(offset=100, length=3)
	public String getDiasCobrancaAntecipada() {
		return diasCobrancaAntecipada;
	}

	/**
	 * @return the slogan
	 */
	@Field(offset=103, length=50)
	public String getSlogan() {
		return slogan;
	}

	/**
	 * @return the comprimento
	 */
	@Field(offset=153, length=5)
	public String getComprimento() {
		return comprimento;
	}

	/**
	 * @return the largura
	 */
	@Field(offset=158, length=5)
	public String getLargura() {
		return largura;
	}

	/**
	 * @return the espessura
	 */
	@Field(offset=163, length=5)
	public String getEspessura() {
		return espessura;
	}

	/**
	 * @return the peso
	 */
	@Field(offset=168, length=5)
	public Long getPeso() {
		return peso;
	}

	/**
	 * @return the status
	 */
	
	@FixedFormatBoolean(trueValue = "A")
	@Field(offset=173, length=1)
	public boolean isStatus() {
		return status;
	}

	/**
	 * @return the dataDesativacao
	 */
	@Field(offset=174, length=8)
	public Date getDataDesativacao() {
		return dataDesativacao;
	}



	/**
	 * @return the criterio1SelecaoCota
	 */
	@Field(offset=182, length=2)
	public String getCriterio1SelecaoCota() {
		return criterio1SelecaoCota;
	}

	/**
	 * @return the argumento1Criterio1
	 */
	@Field(offset=184, length=3)
	public String getArgumento1Criterio1() {
		return argumento1Criterio1;
	}

	/**
	 * @return the argumento2Criterio1
	 */
	@Field(offset=187, length=3)
	public String getArgumento2Criterio1() {
		return argumento2Criterio1;
	}

	/**
	 * @return the argumento3Criterio1
	 */
	@Field(offset=190, length=3)
	public String getArgumento3Criterio1() {
		return argumento3Criterio1;
	}

	/**
	 * @return the tipoCombinacao1
	 */
	@Field(offset=193, length=2)
	public String getTipoCombinacao1() {
		return tipoCombinacao1;
	}

	/**
	 * @return the criterio2SelecaoCota
	 */
	@Field(offset=195, length=2)
	public String getCriterio2SelecaoCota() {
		return criterio2SelecaoCota;
	}

	/**
	 * @return the argumento1Criterio2
	 */
	@Field(offset=197, length=3)
	public String getArgumento1Criterio2() {
		return argumento1Criterio2;
	}

	/**
	 * @return the argumento2Criterio2
	 */
	@Field(offset=200, length=3)
	public String getArgumento2Criterio2() {
		return argumento2Criterio2;
	}

	/**
	 * @return the argumento3Criterio2
	 */
	@Field(offset=203, length=3)
	public String getArgumento3Criterio2() {
		return argumento3Criterio2;
	}

	/**
	 * @return the tipoCombinacao2
	 */
	@Field(offset=206, length=2)
	public String getTipoCombinacao2() {
		return tipoCombinacao2;
	}

	/**
	 * @return the criterio3SelecaoCota
	 */
	@Field(offset=208, length=2)
	public String getCriterio3SelecaoCota() {
		return criterio3SelecaoCota;
	}

	/**
	 * @return the argumento1Criterio3
	 */
	@Field(offset=210, length=3)
	public String getArgumento1Criterio3() {
		return argumento1Criterio3;
	}

	/**
	 * @return the argumento2Criterio3
	 */
	@Field(offset=213, length=3)
	public String getArgumento2Criterio3() {
		return argumento2Criterio3;
	}

	/**
	 * @return the argumento3Criterio3
	 */
	@Field(offset=216, length=3)
	public String getArgumento3Criterio3() {
		return argumento3Criterio3;
	}

	/**
	 * @return the codigoTributacaoFiscal
	 */
	@Field(offset=219, length=1)
	public String getCodigoTributacaoFiscal() {
		return codigoTributacaoFiscal;
	}

	/**
	 * @return the codigoSituacaoTributaria
	 */
	@Field(offset=220, length=1)
	public String getCodigoSituacaoTributaria() {
		return codigoSituacaoTributaria;
	}

	/**
	 * @return the regimeRecolhimento
	 */
	@Field(offset=221, length=1)
	public String getRegimeRecolhimento() {
		return regimeRecolhimento;
	}

	/**
	 * @return the periodicidade
	 */
	@Field(offset=222, length=3)
	public Integer getPeriodicidade() {
		return periodicidade;
	}

	/**
	 * @return the categoria
	 */
	@Field(offset=225, length=3)
	public Long getCategoria() {
		return categoria;
	}

	/**
	 * @return the colecao
	 */
	@Field(offset=228, length=3)
	public String getColecao() {
		return colecao;
	}

	/**
	 * @return the algoritmo
	 */
	@Field(offset=231, length=2)
	public String getAlgoritmo() {
		return algoritmo;
	}

	/**
	 * @return the codigoHistograma
	 */
	@Field(offset=233, length=3)
	public String getCodigoHistograma() {
		return codigoHistograma;
	}

	/**
	 * @return the tipoHistograma
	 */
	@Field(offset=236, length=1)
	public String getTipoHistograma() {
		return tipoHistograma;
	}

	/**
	 * @return the contextoEditor
	 */
	@Field(offset=237, length=1)
	public String getContextoEditor() {
		return contextoEditor;
	}

	/**
	 * @return the codigoEditor
	 */
	@Field(offset=238, length=7)
	public Long getCodigoEditor() {
		return codigoEditor;
	}

	/**
	 * @return the tipoDesconto
	 */
	@Field(offset=245, length=2)
	public String getTipoDesconto() {
		return tipoDesconto;
	}

	/**
	 * @return the percentualLimiteCotasFixadas
	 */
	@Field(offset=247, length=3)
	public String getPercentualLimiteCotasFixadas() {
		return percentualLimiteCotasFixadas;
	}

	/**
	 * @return the percentualLimiteFixacaoReparte
	 */
	@Field(offset=250, length=3)
	public String getPercentualLimiteFixacaoReparte() {
		return percentualLimiteFixacaoReparte;
	}

	/**
	 * @return the sentidoAbrangencia
	 */
	@Field(offset=253, length=1)
	public String getSentidoAbrangencia() {
		return sentidoAbrangencia;
	}

	/**
	 * @return the codigoGrupoEditorial
	 */
	@Field(offset=254, length=3)
	public String getCodigoGrupoEditorial() {
		return codigoGrupoEditorial;
	}

	/**
	 * @return the codigoSubGrupoEditorial
	 */
	@Field(offset=257, length=3)
	public String getCodigoSubGrupoEditorial() {
		return codigoSubGrupoEditorial;
	}

	/**
	 * @param codigoDistribuidor the codigoDistribuidor to set
	 */
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}

	/**
	 * @param dataGeracaoArquivo the dataGeracaoArquivo to set
	 */
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}

	/**
	 * @param horaGeracaoArquivo the horaGeracaoArquivo to set
	 */
	public void setHoraGeracaoArquivo(Date horaGeracaoArquivo) {
		this.horaGeracaoArquivo = horaGeracaoArquivo;
	}

	/**
	 * @param mnemonicoTabela the mnemonicoTabela to set
	 */
	public void setMnemonicoTabela(String mnemonicoTabela) {
		this.mnemonicoTabela = mnemonicoTabela;
	}

	/**
	 * @param contextoPublicacao the contextoPublicacao to set
	 */
	public void setContextoPublicacao(Integer contextoPublicacao) {
		this.contextoPublicacao = contextoPublicacao;
	}

	/**
	 * @param codigoFornecedor the codigoFornecedor to set
	 */
	public void setCodigoFornecedor(Integer codigoFornecedor) {
		this.codigoFornecedor = codigoFornecedor;
	}

	/**
	 * @param codigoPublicacao the codigoPublicacao to set
	 */
	public void setCodigoPublicacao(String codigoPublicacao) {
		this.codigoPublicacao = codigoPublicacao;
	}

	/**
	 * @param nomePublicacao the nomePublicacao to set
	 */
	public void setNomePublicacao(String nomePublicacao) {
		this.nomePublicacao = nomePublicacao;
	}

	/**
	 * @param peb the peb to set
	 */
	public void setPeb(Integer peb) {
		this.peb = peb;
	}

	/**
	 * @param pacotePadrao the pacotePadrao to set
	 */
	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	/**
	 * @param condicaoTransmissaoHistograma the condicaoTransmissaoHistograma to set
	 */
	public void setCondicaoTransmissaoHistograma(
			String condicaoTransmissaoHistograma) {
		this.condicaoTransmissaoHistograma = condicaoTransmissaoHistograma;
	}

	/**
	 * @param porcentagemAbrangencia the porcentagemAbrangencia to set
	 */
	public void setPorcentagemAbrangencia(String porcentagemAbrangencia) {
		this.porcentagemAbrangencia = porcentagemAbrangencia;
	}

	/**
	 * @param condicaoLancamentoImediato the condicaoLancamentoImediato to set
	 */
	public void setCondicaoLancamentoImediato(String condicaoLancamentoImediato) {
		this.condicaoLancamentoImediato = condicaoLancamentoImediato;
	}

	/**
	 * @param formaComercializacao the formaComercializacao to set
	 */
	public void setFormaComercializacao(String formaComercializacao) {
		this.formaComercializacao = formaComercializacao;
	}

	/**
	 * @param formaPagamento the formaPagamento to set
	 */
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	/**
	 * @param condicaoPagamentoAntecipado the condicaoPagamentoAntecipado to set
	 */
	public void setCondicaoPagamentoAntecipado(String condicaoPagamentoAntecipado) {
		this.condicaoPagamentoAntecipado = condicaoPagamentoAntecipado;
	}

	/**
	 * @param condicaoPermissaoAlteracao the condicaoPermissaoAlteracao to set
	 */
	public void setCondicaoPermissaoAlteracao(String condicaoPermissaoAlteracao) {
		this.condicaoPermissaoAlteracao = condicaoPermissaoAlteracao;
	}

	/**
	 * @param percetualCobrancaAntecipada the percetualCobrancaAntecipada to set
	 */
	public void setPercetualCobrancaAntecipada(String percetualCobrancaAntecipada) {
		this.percetualCobrancaAntecipada = percetualCobrancaAntecipada;
	}

	/**
	 * @param diasCobrancaAntecipada the diasCobrancaAntecipada to set
	 */
	public void setDiasCobrancaAntecipada(String diasCobrancaAntecipada) {
		this.diasCobrancaAntecipada = diasCobrancaAntecipada;
	}

	/**
	 * @param slogan the slogan to set
	 */
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}

	/**
	 * @param comprimento the comprimento to set
	 */
	public void setComprimento(String comprimento) {
		this.comprimento = comprimento;
	}

	/**
	 * @param largura the largura to set
	 */
	public void setLargura(String largura) {
		this.largura = largura;
	}

	/**
	 * @param espessura the espessura to set
	 */
	public void setEspessura(String espessura) {
		this.espessura = espessura;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(Long peso) {
		this.peso = peso;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}

	/**
	 * @param dataDesativacao the dataDesativacao to set
	 */
	public void setDataDesativacao(Date dataDesativacao) {
		this.dataDesativacao = dataDesativacao;
	}

	/**
	 * @param criterio1SelecaoCota the criterio1SelecaoCota to set
	 */
	public void setCriterio1SelecaoCota(String criterio1SelecaoCota) {
		this.criterio1SelecaoCota = criterio1SelecaoCota;
	}

	/**
	 * @param argumento1Criterio1 the argumento1Criterio1 to set
	 */
	public void setArgumento1Criterio1(String argumento1Criterio1) {
		this.argumento1Criterio1 = argumento1Criterio1;
	}

	/**
	 * @param argumento2Criterio1 the argumento2Criterio1 to set
	 */
	public void setArgumento2Criterio1(String argumento2Criterio1) {
		this.argumento2Criterio1 = argumento2Criterio1;
	}

	/**
	 * @param argumento3Criterio1 the argumento3Criterio1 to set
	 */
	public void setArgumento3Criterio1(String argumento3Criterio1) {
		this.argumento3Criterio1 = argumento3Criterio1;
	}

	/**
	 * @param tipoCombinacao1 the tipoCombinacao1 to set
	 */
	public void setTipoCombinacao1(String tipoCombinacao1) {
		this.tipoCombinacao1 = tipoCombinacao1;
	}

	/**
	 * @param criterio2SelecaoCota the criterio2SelecaoCota to set
	 */
	public void setCriterio2SelecaoCota(String criterio2SelecaoCota) {
		this.criterio2SelecaoCota = criterio2SelecaoCota;
	}

	/**
	 * @param argumento1Criterio2 the argumento1Criterio2 to set
	 */
	public void setArgumento1Criterio2(String argumento1Criterio2) {
		this.argumento1Criterio2 = argumento1Criterio2;
	}

	/**
	 * @param argumento2Criterio2 the argumento2Criterio2 to set
	 */
	public void setArgumento2Criterio2(String argumento2Criterio2) {
		this.argumento2Criterio2 = argumento2Criterio2;
	}

	/**
	 * @param argumento3Criterio2 the argumento3Criterio2 to set
	 */
	public void setArgumento3Criterio2(String argumento3Criterio2) {
		this.argumento3Criterio2 = argumento3Criterio2;
	}

	/**
	 * @param tipoCombinacao2 the tipoCombinacao2 to set
	 */
	public void setTipoCombinacao2(String tipoCombinacao2) {
		this.tipoCombinacao2 = tipoCombinacao2;
	}

	/**
	 * @param criterio3SelecaoCota the criterio3SelecaoCota to set
	 */
	public void setCriterio3SelecaoCota(String criterio3SelecaoCota) {
		this.criterio3SelecaoCota = criterio3SelecaoCota;
	}

	/**
	 * @param argumento1Criterio3 the argumento1Criterio3 to set
	 */
	public void setArgumento1Criterio3(String argumento1Criterio3) {
		this.argumento1Criterio3 = argumento1Criterio3;
	}

	/**
	 * @param argumento2Criterio3 the argumento2Criterio3 to set
	 */
	public void setArgumento2Criterio3(String argumento2Criterio3) {
		this.argumento2Criterio3 = argumento2Criterio3;
	}

	/**
	 * @param argumento3Criterio3 the argumento3Criterio3 to set
	 */
	public void setArgumento3Criterio3(String argumento3Criterio3) {
		this.argumento3Criterio3 = argumento3Criterio3;
	}

	/**
	 * @param codigoTributacaoFiscal the codigoTributacaoFiscal to set
	 */
	public void setCodigoTributacaoFiscal(String codigoTributacaoFiscal) {
		this.codigoTributacaoFiscal = codigoTributacaoFiscal;
	}

	/**
	 * @param codigoSituacaoTributaria the codigoSituacaoTributaria to set
	 */
	public void setCodigoSituacaoTributaria(String codigoSituacaoTributaria) {
		this.codigoSituacaoTributaria = codigoSituacaoTributaria;
	}

	/**
	 * @param regimeRecolhimento the regimeRecolhimento to set
	 */
	public void setRegimeRecolhimento(String regimeRecolhimento) {
		this.regimeRecolhimento = regimeRecolhimento;
	}

	/**
	 * @param periodicidade the periodicidade to set
	 */
	public void setPeriodicidade(Integer periodicidade) {
		this.periodicidade = periodicidade;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(Long categoria) {
		this.categoria = categoria;
	}

	/**
	 * @param colecao the colecao to set
	 */
	public void setColecao(String colecao) {
		this.colecao = colecao;
	}

	/**
	 * @param algoritmo the algoritmo to set
	 */
	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}

	/**
	 * @param codigoHistograma the codigoHistograma to set
	 */
	public void setCodigoHistograma(String codigoHistograma) {
		this.codigoHistograma = codigoHistograma;
	}

	/**
	 * @param tipoHistograma the tipoHistograma to set
	 */
	public void setTipoHistograma(String tipoHistograma) {
		this.tipoHistograma = tipoHistograma;
	}

	/**
	 * @param contextoEditor the contextoEditor to set
	 */
	public void setContextoEditor(String contextoEditor) {
		this.contextoEditor = contextoEditor;
	}

	/**
	 * @param codigoEditor the codigoEditor to set
	 */
	public void setCodigoEditor(Long codigoEditor) {
		this.codigoEditor = codigoEditor;
	}

	/**
	 * @param tipoDesconto the tipoDesconto to set
	 */
	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	/**
	 * @param percentualLimiteCotasFixadas the percentualLimiteCotasFixadas to set
	 */
	public void setPercentualLimiteCotasFixadas(String percentualLimiteCotasFixadas) {
		this.percentualLimiteCotasFixadas = percentualLimiteCotasFixadas;
	}

	/**
	 * @param percentualLimiteFixacaoReparte the percentualLimiteFixacaoReparte to set
	 */
	public void setPercentualLimiteFixacaoReparte(
			String percentualLimiteFixacaoReparte) {
		this.percentualLimiteFixacaoReparte = percentualLimiteFixacaoReparte;
	}

	/**
	 * @param sentidoAbrangencia the sentidoAbrangencia to set
	 */
	public void setSentidoAbrangencia(String sentidoAbrangencia) {
		this.sentidoAbrangencia = sentidoAbrangencia;
	}

	/**
	 * @param codigoGrupoEditorial the codigoGrupoEditorial to set
	 */
	public void setCodigoGrupoEditorial(String codigoGrupoEditorial) {
		this.codigoGrupoEditorial = codigoGrupoEditorial;
	}

	/**
	 * @param codigoSubGrupoEditorial the codigoSubGrupoEditorial to set
	 */
	public void setCodigoSubGrupoEditorial(String codigoSubGrupoEditorial) {
		this.codigoSubGrupoEditorial = codigoSubGrupoEditorial;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((algoritmo == null) ? 0 : algoritmo.hashCode());
		result = prime
				* result
				+ ((argumento1Criterio1 == null) ? 0 : argumento1Criterio1
						.hashCode());
		result = prime
				* result
				+ ((argumento1Criterio2 == null) ? 0 : argumento1Criterio2
						.hashCode());
		result = prime
				* result
				+ ((argumento1Criterio3 == null) ? 0 : argumento1Criterio3
						.hashCode());
		result = prime
				* result
				+ ((argumento2Criterio1 == null) ? 0 : argumento2Criterio1
						.hashCode());
		result = prime
				* result
				+ ((argumento2Criterio2 == null) ? 0 : argumento2Criterio2
						.hashCode());
		result = prime
				* result
				+ ((argumento2Criterio3 == null) ? 0 : argumento2Criterio3
						.hashCode());
		result = prime
				* result
				+ ((argumento3Criterio1 == null) ? 0 : argumento3Criterio1
						.hashCode());
		result = prime
				* result
				+ ((argumento3Criterio2 == null) ? 0 : argumento3Criterio2
						.hashCode());
		result = prime
				* result
				+ ((argumento3Criterio3 == null) ? 0 : argumento3Criterio3
						.hashCode());
		result = prime * result
				+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime
				* result
				+ ((codigoDistribuidor == null) ? 0 : codigoDistribuidor
						.hashCode());
		result = prime * result
				+ ((codigoEditor == null) ? 0 : codigoEditor.hashCode());
		result = prime
				* result
				+ ((codigoFornecedor == null) ? 0 : codigoFornecedor.hashCode());
		result = prime
				* result
				+ ((codigoGrupoEditorial == null) ? 0 : codigoGrupoEditorial
						.hashCode());
		result = prime
				* result
				+ ((codigoHistograma == null) ? 0 : codigoHistograma.hashCode());
		result = prime
				* result
				+ ((codigoPublicacao == null) ? 0 : codigoPublicacao.hashCode());
		result = prime
				* result
				+ ((codigoSituacaoTributaria == null) ? 0
						: codigoSituacaoTributaria.hashCode());
		result = prime
				* result
				+ ((codigoSubGrupoEditorial == null) ? 0
						: codigoSubGrupoEditorial.hashCode());
		result = prime
				* result
				+ ((codigoTributacaoFiscal == null) ? 0
						: codigoTributacaoFiscal.hashCode());
		result = prime * result + ((colecao == null) ? 0 : colecao.hashCode());
		result = prime * result
				+ ((comprimento == null) ? 0 : comprimento.hashCode());
		result = prime
				* result
				+ ((condicaoLancamentoImediato == null) ? 0
						: condicaoLancamentoImediato.hashCode());
		result = prime
				* result
				+ ((condicaoPagamentoAntecipado == null) ? 0
						: condicaoPagamentoAntecipado.hashCode());
		result = prime
				* result
				+ ((condicaoPermissaoAlteracao == null) ? 0
						: condicaoPermissaoAlteracao.hashCode());
		result = prime
				* result
				+ ((condicaoTransmissaoHistograma == null) ? 0
						: condicaoTransmissaoHistograma.hashCode());
		result = prime * result
				+ ((contextoEditor == null) ? 0 : contextoEditor.hashCode());
		result = prime
				* result
				+ ((contextoPublicacao == null) ? 0 : contextoPublicacao
						.hashCode());
		result = prime
				* result
				+ ((criterio1SelecaoCota == null) ? 0 : criterio1SelecaoCota
						.hashCode());
		result = prime
				* result
				+ ((criterio2SelecaoCota == null) ? 0 : criterio2SelecaoCota
						.hashCode());
		result = prime
				* result
				+ ((criterio3SelecaoCota == null) ? 0 : criterio3SelecaoCota
						.hashCode());
		result = prime * result
				+ ((dataDesativacao == null) ? 0 : dataDesativacao.hashCode());
		result = prime
				* result
				+ ((dataGeracaoArquivo == null) ? 0 : dataGeracaoArquivo
						.hashCode());
		result = prime
				* result
				+ ((diasCobrancaAntecipada == null) ? 0
						: diasCobrancaAntecipada.hashCode());
		result = prime * result
				+ ((espessura == null) ? 0 : espessura.hashCode());
		result = prime
				* result
				+ ((formaComercializacao == null) ? 0 : formaComercializacao
						.hashCode());
		result = prime * result
				+ ((formaPagamento == null) ? 0 : formaPagamento.hashCode());
		result = prime
				* result
				+ ((horaGeracaoArquivo == null) ? 0 : horaGeracaoArquivo
						.hashCode());
		result = prime * result + ((largura == null) ? 0 : largura.hashCode());
		result = prime * result
				+ ((mnemonicoTabela == null) ? 0 : mnemonicoTabela.hashCode());
		result = prime * result
				+ ((nomePublicacao == null) ? 0 : nomePublicacao.hashCode());
		result = prime * result
				+ ((pacotePadrao == null) ? 0 : pacotePadrao.hashCode());
		result = prime * result + ((peb == null) ? 0 : peb.hashCode());
		result = prime
				* result
				+ ((percentualLimiteCotasFixadas == null) ? 0
						: percentualLimiteCotasFixadas.hashCode());
		result = prime
				* result
				+ ((percentualLimiteFixacaoReparte == null) ? 0
						: percentualLimiteFixacaoReparte.hashCode());
		result = prime
				* result
				+ ((percetualCobrancaAntecipada == null) ? 0
						: percetualCobrancaAntecipada.hashCode());
		result = prime * result
				+ ((periodicidade == null) ? 0 : periodicidade.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		result = prime
				* result
				+ ((porcentagemAbrangencia == null) ? 0
						: porcentagemAbrangencia.hashCode());
		result = prime
				* result
				+ ((regimeRecolhimento == null) ? 0 : regimeRecolhimento
						.hashCode());
		result = prime
				* result
				+ ((sentidoAbrangencia == null) ? 0 : sentidoAbrangencia
						.hashCode());
		result = prime * result + ((slogan == null) ? 0 : slogan.hashCode());
		result = prime * result + (status ? 1231 : 1237);
		result = prime * result
				+ ((tipoCombinacao1 == null) ? 0 : tipoCombinacao1.hashCode());
		result = prime * result
				+ ((tipoCombinacao2 == null) ? 0 : tipoCombinacao2.hashCode());
		result = prime * result
				+ ((tipoDesconto == null) ? 0 : tipoDesconto.hashCode());
		result = prime * result
				+ ((tipoHistograma == null) ? 0 : tipoHistograma.hashCode());
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
		EMS0109Input other = (EMS0109Input) obj;
		if (algoritmo == null) {
			if (other.algoritmo != null)
				return false;
		} else if (!algoritmo.equals(other.algoritmo))
			return false;
		if (argumento1Criterio1 == null) {
			if (other.argumento1Criterio1 != null)
				return false;
		} else if (!argumento1Criterio1.equals(other.argumento1Criterio1))
			return false;
		if (argumento1Criterio2 == null) {
			if (other.argumento1Criterio2 != null)
				return false;
		} else if (!argumento1Criterio2.equals(other.argumento1Criterio2))
			return false;
		if (argumento1Criterio3 == null) {
			if (other.argumento1Criterio3 != null)
				return false;
		} else if (!argumento1Criterio3.equals(other.argumento1Criterio3))
			return false;
		if (argumento2Criterio1 == null) {
			if (other.argumento2Criterio1 != null)
				return false;
		} else if (!argumento2Criterio1.equals(other.argumento2Criterio1))
			return false;
		if (argumento2Criterio2 == null) {
			if (other.argumento2Criterio2 != null)
				return false;
		} else if (!argumento2Criterio2.equals(other.argumento2Criterio2))
			return false;
		if (argumento2Criterio3 == null) {
			if (other.argumento2Criterio3 != null)
				return false;
		} else if (!argumento2Criterio3.equals(other.argumento2Criterio3))
			return false;
		if (argumento3Criterio1 == null) {
			if (other.argumento3Criterio1 != null)
				return false;
		} else if (!argumento3Criterio1.equals(other.argumento3Criterio1))
			return false;
		if (argumento3Criterio2 == null) {
			if (other.argumento3Criterio2 != null)
				return false;
		} else if (!argumento3Criterio2.equals(other.argumento3Criterio2))
			return false;
		if (argumento3Criterio3 == null) {
			if (other.argumento3Criterio3 != null)
				return false;
		} else if (!argumento3Criterio3.equals(other.argumento3Criterio3))
			return false;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (codigoDistribuidor == null) {
			if (other.codigoDistribuidor != null)
				return false;
		} else if (!codigoDistribuidor.equals(other.codigoDistribuidor))
			return false;
		if (codigoEditor == null) {
			if (other.codigoEditor != null)
				return false;
		} else if (!codigoEditor.equals(other.codigoEditor))
			return false;
		if (codigoFornecedor == null) {
			if (other.codigoFornecedor != null)
				return false;
		} else if (!codigoFornecedor.equals(other.codigoFornecedor))
			return false;
		if (codigoGrupoEditorial == null) {
			if (other.codigoGrupoEditorial != null)
				return false;
		} else if (!codigoGrupoEditorial.equals(other.codigoGrupoEditorial))
			return false;
		if (codigoHistograma == null) {
			if (other.codigoHistograma != null)
				return false;
		} else if (!codigoHistograma.equals(other.codigoHistograma))
			return false;
		if (codigoPublicacao == null) {
			if (other.codigoPublicacao != null)
				return false;
		} else if (!codigoPublicacao.equals(other.codigoPublicacao))
			return false;
		if (codigoSituacaoTributaria == null) {
			if (other.codigoSituacaoTributaria != null)
				return false;
		} else if (!codigoSituacaoTributaria
				.equals(other.codigoSituacaoTributaria))
			return false;
		if (codigoSubGrupoEditorial == null) {
			if (other.codigoSubGrupoEditorial != null)
				return false;
		} else if (!codigoSubGrupoEditorial
				.equals(other.codigoSubGrupoEditorial))
			return false;
		if (codigoTributacaoFiscal == null) {
			if (other.codigoTributacaoFiscal != null)
				return false;
		} else if (!codigoTributacaoFiscal.equals(other.codigoTributacaoFiscal))
			return false;
		if (colecao == null) {
			if (other.colecao != null)
				return false;
		} else if (!colecao.equals(other.colecao))
			return false;
		if (comprimento == null) {
			if (other.comprimento != null)
				return false;
		} else if (!comprimento.equals(other.comprimento))
			return false;
		if (condicaoLancamentoImediato == null) {
			if (other.condicaoLancamentoImediato != null)
				return false;
		} else if (!condicaoLancamentoImediato
				.equals(other.condicaoLancamentoImediato))
			return false;
		if (condicaoPagamentoAntecipado == null) {
			if (other.condicaoPagamentoAntecipado != null)
				return false;
		} else if (!condicaoPagamentoAntecipado
				.equals(other.condicaoPagamentoAntecipado))
			return false;
		if (condicaoPermissaoAlteracao == null) {
			if (other.condicaoPermissaoAlteracao != null)
				return false;
		} else if (!condicaoPermissaoAlteracao
				.equals(other.condicaoPermissaoAlteracao))
			return false;
		if (condicaoTransmissaoHistograma == null) {
			if (other.condicaoTransmissaoHistograma != null)
				return false;
		} else if (!condicaoTransmissaoHistograma
				.equals(other.condicaoTransmissaoHistograma))
			return false;
		if (contextoEditor == null) {
			if (other.contextoEditor != null)
				return false;
		} else if (!contextoEditor.equals(other.contextoEditor))
			return false;
		if (contextoPublicacao == null) {
			if (other.contextoPublicacao != null)
				return false;
		} else if (!contextoPublicacao.equals(other.contextoPublicacao))
			return false;
		if (criterio1SelecaoCota == null) {
			if (other.criterio1SelecaoCota != null)
				return false;
		} else if (!criterio1SelecaoCota.equals(other.criterio1SelecaoCota))
			return false;
		if (criterio2SelecaoCota == null) {
			if (other.criterio2SelecaoCota != null)
				return false;
		} else if (!criterio2SelecaoCota.equals(other.criterio2SelecaoCota))
			return false;
		if (criterio3SelecaoCota == null) {
			if (other.criterio3SelecaoCota != null)
				return false;
		} else if (!criterio3SelecaoCota.equals(other.criterio3SelecaoCota))
			return false;
		if (dataDesativacao == null) {
			if (other.dataDesativacao != null)
				return false;
		} else if (!dataDesativacao.equals(other.dataDesativacao))
			return false;
		if (dataGeracaoArquivo == null) {
			if (other.dataGeracaoArquivo != null)
				return false;
		} else if (!dataGeracaoArquivo.equals(other.dataGeracaoArquivo))
			return false;
		if (diasCobrancaAntecipada == null) {
			if (other.diasCobrancaAntecipada != null)
				return false;
		} else if (!diasCobrancaAntecipada.equals(other.diasCobrancaAntecipada))
			return false;
		if (espessura == null) {
			if (other.espessura != null)
				return false;
		} else if (!espessura.equals(other.espessura))
			return false;
		if (formaComercializacao == null) {
			if (other.formaComercializacao != null)
				return false;
		} else if (!formaComercializacao.equals(other.formaComercializacao))
			return false;
		if (formaPagamento == null) {
			if (other.formaPagamento != null)
				return false;
		} else if (!formaPagamento.equals(other.formaPagamento))
			return false;
		if (horaGeracaoArquivo == null) {
			if (other.horaGeracaoArquivo != null)
				return false;
		} else if (!horaGeracaoArquivo.equals(other.horaGeracaoArquivo))
			return false;
		if (largura == null) {
			if (other.largura != null)
				return false;
		} else if (!largura.equals(other.largura))
			return false;
		if (mnemonicoTabela == null) {
			if (other.mnemonicoTabela != null)
				return false;
		} else if (!mnemonicoTabela.equals(other.mnemonicoTabela))
			return false;
		if (nomePublicacao == null) {
			if (other.nomePublicacao != null)
				return false;
		} else if (!nomePublicacao.equals(other.nomePublicacao))
			return false;
		if (pacotePadrao == null) {
			if (other.pacotePadrao != null)
				return false;
		} else if (!pacotePadrao.equals(other.pacotePadrao))
			return false;
		if (peb == null) {
			if (other.peb != null)
				return false;
		} else if (!peb.equals(other.peb))
			return false;
		if (percentualLimiteCotasFixadas == null) {
			if (other.percentualLimiteCotasFixadas != null)
				return false;
		} else if (!percentualLimiteCotasFixadas
				.equals(other.percentualLimiteCotasFixadas))
			return false;
		if (percentualLimiteFixacaoReparte == null) {
			if (other.percentualLimiteFixacaoReparte != null)
				return false;
		} else if (!percentualLimiteFixacaoReparte
				.equals(other.percentualLimiteFixacaoReparte))
			return false;
		if (percetualCobrancaAntecipada == null) {
			if (other.percetualCobrancaAntecipada != null)
				return false;
		} else if (!percetualCobrancaAntecipada
				.equals(other.percetualCobrancaAntecipada))
			return false;
		if (periodicidade == null) {
			if (other.periodicidade != null)
				return false;
		} else if (!periodicidade.equals(other.periodicidade))
			return false;
		if (peso == null) {
			if (other.peso != null)
				return false;
		} else if (!peso.equals(other.peso))
			return false;
		if (porcentagemAbrangencia == null) {
			if (other.porcentagemAbrangencia != null)
				return false;
		} else if (!porcentagemAbrangencia.equals(other.porcentagemAbrangencia))
			return false;
		if (regimeRecolhimento == null) {
			if (other.regimeRecolhimento != null)
				return false;
		} else if (!regimeRecolhimento.equals(other.regimeRecolhimento))
			return false;
		if (sentidoAbrangencia == null) {
			if (other.sentidoAbrangencia != null)
				return false;
		} else if (!sentidoAbrangencia.equals(other.sentidoAbrangencia))
			return false;
		if (slogan == null) {
			if (other.slogan != null)
				return false;
		} else if (!slogan.equals(other.slogan))
			return false;
		if (status != other.status)
			return false;
		if (tipoCombinacao1 == null) {
			if (other.tipoCombinacao1 != null)
				return false;
		} else if (!tipoCombinacao1.equals(other.tipoCombinacao1))
			return false;
		if (tipoCombinacao2 == null) {
			if (other.tipoCombinacao2 != null)
				return false;
		} else if (!tipoCombinacao2.equals(other.tipoCombinacao2))
			return false;
		if (tipoDesconto == null) {
			if (other.tipoDesconto != null)
				return false;
		} else if (!tipoDesconto.equals(other.tipoDesconto))
			return false;
		if (tipoHistograma == null) {
			if (other.tipoHistograma != null)
				return false;
		} else if (!tipoHistograma.equals(other.tipoHistograma))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EMS0109Input [codigoDistribuidor=" + codigoDistribuidor
				+ ", dataGeracaoArquivo=" + dataGeracaoArquivo
				+ ", horaGeracaoArquivo=" + horaGeracaoArquivo
				+ ", mnemonicoTabela=" + mnemonicoTabela
				+ ", contextoPublicacao=" + contextoPublicacao
				+ ", codigoFornecedor=" + codigoFornecedor
				+ ", codigoPublicacao=" + codigoPublicacao
				+ ", nomePublicacao=" + nomePublicacao + ", peb=" + peb
				+ ", pacotePadrao=" + pacotePadrao
				+ ", condicaoTransmissaoHistograma="
				+ condicaoTransmissaoHistograma + ", porcentagemAbrangencia="
				+ porcentagemAbrangencia + ", condicaoLancamentoImediato="
				+ condicaoLancamentoImediato + ", formaComercializacao="
				+ formaComercializacao + ", formaPagamento=" + formaPagamento
				+ ", condicaoPagamentoAntecipado="
				+ condicaoPagamentoAntecipado + ", condicaoPermissaoAlteracao="
				+ condicaoPermissaoAlteracao + ", percetualCobrancaAntecipada="
				+ percetualCobrancaAntecipada + ", diasCobrancaAntecipada="
				+ diasCobrancaAntecipada + ", slogan=" + slogan
				+ ", comprimento=" + comprimento + ", largura=" + largura
				+ ", espessura=" + espessura + ", peso=" + peso + ", status="
				+ status + ", dataDesativacao=" + dataDesativacao
				+ ", criterio1SelecaoCota=" + criterio1SelecaoCota
				+ ", argumento1Criterio1=" + argumento1Criterio1
				+ ", argumento2Criterio1=" + argumento2Criterio1
				+ ", argumento3Criterio1=" + argumento3Criterio1
				+ ", tipoCombinacao1=" + tipoCombinacao1
				+ ", criterio2SelecaoCota=" + criterio2SelecaoCota
				+ ", argumento1Criterio2=" + argumento1Criterio2
				+ ", argumento2Criterio2=" + argumento2Criterio2
				+ ", argumento3Criterio2=" + argumento3Criterio2
				+ ", tipoCombinacao2=" + tipoCombinacao2
				+ ", criterio3SelecaoCota=" + criterio3SelecaoCota
				+ ", argumento1Criterio3=" + argumento1Criterio3
				+ ", argumento2Criterio3=" + argumento2Criterio3
				+ ", argumento3Criterio3=" + argumento3Criterio3
				+ ", codigoTributacaoFiscal=" + codigoTributacaoFiscal
				+ ", codigoSituacaoTributaria=" + codigoSituacaoTributaria
				+ ", regimeRecolhimento=" + regimeRecolhimento
				+ ", periodicidade=" + periodicidade + ", categoria="
				+ categoria + ", colecao=" + colecao + ", algoritmo="
				+ algoritmo + ", codigoHistograma=" + codigoHistograma
				+ ", tipoHistograma=" + tipoHistograma + ", contextoEditor="
				+ contextoEditor + ", codigoEditor=" + codigoEditor
				+ ", tipoDesconto=" + tipoDesconto
				+ ", percentualLimiteCotasFixadas="
				+ percentualLimiteCotasFixadas
				+ ", percentualLimiteFixacaoReparte="
				+ percentualLimiteFixacaoReparte + ", sentidoAbrangencia="
				+ sentidoAbrangencia + ", codigoGrupoEditorial="
				+ codigoGrupoEditorial + ", codigoSubGrupoEditorial="
				+ codigoSubGrupoEditorial + "]";
	}	
}
