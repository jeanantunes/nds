package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.FixedFormatPattern;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0111Input extends IntegracaoDocument implements Serializable {
	/**
	 * @author Jones.Costa
	 * @version 1.0
	 */
	private static final long serialVersionUID = -6653473316882827815L;

	private String codigoDistribuidor;
	private Date dataGeracaoArquivo;
	//private Time horaGeracaoArquivo;
	private String mnemonicoTabela;
	private String contextoProduto;
	private Long codigoFornecedorProduto;
	private String codigoProduto;
	private Long edicaoProduto;	
	private Long numeroLancamento;	
	private Long numeroFase;	
	private Date dataLancamento;	
	private String tipoLancamento;	
	private String tipoProduto;	
	private Long repartePrevisto;	
	private BigDecimal pctAbrangencia;	
	private BigDecimal pctEntregaAntecipada;	
	private String condCotasAtuais;	
	private String tipoBaseRegiao;	
	private String tipoHistorico;	
	private String condBasePacotePadrao;	
	private BigDecimal precoPrevisto;	
	private Long repartePromocional;	
	private String condDistribuicaoFases;
	private Date dataRecolhimento;
	
		
	@Field(offset = 1, length = 7)
	public String getCodigoDistribuidor() {
		return codigoDistribuidor;
	}
	public void setCodigoDistribuidor(String codigoDistribuidor) {
		this.codigoDistribuidor = codigoDistribuidor;
	}
	
	
	@Field(offset = 8, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataGeracaoArquivo() {
		return dataGeracaoArquivo;
	}
	public void setDataGeracaoArquivo(Date dataGeracaoArquivo) {
		this.dataGeracaoArquivo = dataGeracaoArquivo;
	}
	
	
//	@Field(offset = 16, length = 6)
//	@FixedFormatPattern("hhmmss")
//	public Time getHoraGeracaoArquivo() {
//		return horaGeracaoArquivo;
//	}
//	public void setHoraGeracaoArquivo(Time horaGeracaoArquivo) {
//		this.horaGeracaoArquivo = horaGeracaoArquivo;
//	}
	
	
	@Field(offset = 22, length = 4)
	public String getMnemonicoTabela() {
		return mnemonicoTabela;
	}
	public void setMnemonicoTabela(String mnemonicoTabela) {
		this.mnemonicoTabela = mnemonicoTabela;
	}
	
	
	@Field(offset = 26, length = 1)
	public String getContextoProduto() {
		return contextoProduto;
	}
	public void setContextoProduto(String contextoProduto) {
		this.contextoProduto = contextoProduto;
	}
	
	
	@Field(offset = 27, length = 7)
	public Long getCodigoFornecedorProduto() {
		return codigoFornecedorProduto;
	}
	public void setCodigoFornecedorProduto(Long codigoFornecedorProduto) {
		this.codigoFornecedorProduto = codigoFornecedorProduto;
	}
	
	
	@Field(offset = 34, length = 8)
	public String getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	
	
	@Field(offset = 42, length = 4)
	public Long getEdicaoProduto() {
		return edicaoProduto;
	}
	public void setEdicaoProduto(Long edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}
	
	
	@Field(offset = 46, length = 2)
	public Long getNumeroLancamento() {
		return numeroLancamento;
	}
	public void setNumeroLancamento(Long numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}
	
	
	@Field(offset = 48, length = 1)
	public Long getNumeroFase() {
		return numeroFase;
	}
	public void setNumeroFase(Long numeroFase) {
		this.numeroFase = numeroFase;
	}
	
	
	@Field(offset = 50, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	
	
	@Field(offset = 58, length = 3)
	public String getTipoLancamento() {
		return tipoLancamento;
	}
	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}
	
	
	@Field(offset = 61, length = 1)
	public String getTipoProduto() {
		return tipoProduto;
	}
	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	
	@Field(offset = 62, length = 8)
	public Long getRepartePrevisto() {
		return repartePrevisto;
	}
	public void setRepartePrevisto(Long repartePrevisto) {
		this.repartePrevisto = repartePrevisto;
	}
	
	
	@Field(offset = 70, length = 3)
	public BigDecimal getPctAbrangencia() {
		return pctAbrangencia;
	}
	public void setPctAbrangencia(BigDecimal pctAbrangencia) {
		this.pctAbrangencia = pctAbrangencia;
	}
	
	
	@Field(offset = 73, length = 3)
	public BigDecimal getPctEntregaAntecipada() {
		return pctEntregaAntecipada;
	}
	public void setPctEntregaAntecipada(BigDecimal pctEntregaAntecipada) {
		this.pctEntregaAntecipada = pctEntregaAntecipada;
	}
	
	
	@Field(offset = 76, length = 1)
	public String getCondCotasAtuais() {
		return condCotasAtuais;
	}
	public void setCondCotasAtuais(String condCotasAtuais) {
		this.condCotasAtuais = condCotasAtuais;
	}
	
	
	@Field(offset = 77, length = 3)
	public String getTipoBaseRegiao() {
		return tipoBaseRegiao;
	}
	public void setTipoBaseRegiao(String tipoBaseRegiao) {
		this.tipoBaseRegiao = tipoBaseRegiao;
	}
	
	
	@Field(offset = 80, length = 1)
	public String getTipoHistorico() {
		return tipoHistorico;
	}
	public void setTipoHistorico(String tipoHistorico) {
		this.tipoHistorico = tipoHistorico;
	}
	
	
	@Field(offset = 81, length = 1)
	public String getCondBasePacotePadrao() {
		return condBasePacotePadrao;
	}
	public void setCondBasePacotePadrao(String condBasePacotePadrao) {
		this.condBasePacotePadrao = condBasePacotePadrao;
	}
	
	
	@Field(offset = 82, length = 10 )
	public BigDecimal getPrecoPrevisto() {
		return precoPrevisto;
	}
	public void setPrecoPrevisto(BigDecimal precoPrevisto) {
		this.precoPrevisto = precoPrevisto;
	}
	
	
	@Field(offset = 92, length = 8)
	public Long getRepartePromocional() {
		return repartePromocional;
	}
	public void setRepartePromocional(Long repartePromocional) {
		this.repartePromocional = repartePromocional;
	}
	
	
	@Field(offset = 100, length = 1)
	public String getCondDistribuicaoFases() {
		return condDistribuicaoFases;
	}
	public void setCondDistribuicaoFases(String condDistribuicaoFases) {
		this.condDistribuicaoFases = condDistribuicaoFases;
	}
	
	
	@Field(offset = 166, length = 8)
	@FixedFormatPattern("yyyyMMdd")
	public Date getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(Date dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
	
	
}
