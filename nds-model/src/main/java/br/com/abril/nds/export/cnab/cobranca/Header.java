package br.com.abril.nds.export.cnab.cobranca;

import com.ancientprogramming.fixedformat4j.annotation.Align;
import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class Header {

	// Controle
	private Long codigoBanco;
	private Long lote;
	private Long tipoRegistro = 1L;
	
	// Serviço
	private String operacao;
	private Long servico = 1L;
	private String cnab1;
	private Long layoutLote = 45L;
	
	private String cnab2;
	
	// Empresa
	private Long tipoInscicao;
	private Long numeroInscricao;
	private String convenio;
	private Long codigoAgencia;
	private String dvAgencia;
	private Long numeroConta;
	private String dvConta;
	private String dvAgenciaConta;
	private String nomeEmpresa;
	
	private String informacao1;
	private String informacao2;
	private Long numeroRemessaRetorno;
	private Long dataGravacaoRemessaRetorno;
	private Long dataCredito;
	private String cnab3;
	
	/**
	 * @return the codigoBanco
	 */
	@Field(offset=1, length=3, align=Align.RIGHT, paddingChar='0')
	public Long getCodigoBanco() {
		return codigoBanco;
	}
	
	/**
	 * @param codigoBanco the codigoBanco to set
	 */
	public void setCodigoBanco(Long codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	
	/**
	 * @return the lote
	 */
	@Field(offset=4, length=4, align=Align.RIGHT, paddingChar='0')
	public Long getLote() {
		return lote;
	}
	
	/**
	 * @param lote the lote to set
	 */
	public void setLote(Long lote) {
		this.lote = lote;
	}
	
	/**
	 * @return the tipoRegistro
	 */
	@Field(offset=8, length=1, align=Align.RIGHT, paddingChar='0')
	public Long getTipoRegistro() {
		return tipoRegistro;
	}
	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(Long tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	/**
	 * @return the operacao
	 */
	@Field(offset=9, length=1, align=Align.LEFT, paddingChar='0')
	public String getOperacao() {
		return operacao;
	}
	
	/**
	 * @param operacao the operacao to set
	 */
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	
	/**
	 * @return the servico
	 */
	@Field(offset=10, length=2, align=Align.RIGHT, paddingChar='0')
	public Long getServico() {
		return servico;
	}
	
	/**
	 * @param servico the servico to set
	 */
	public void setServico(Long servico) {
		this.servico = servico;
	}
	
	/**
	 * @return the cnab1
	 */
	@Field(offset=12, length=2, align=Align.LEFT, paddingChar=' ')
	public String getCnab1() {
		return cnab1;
	}
	
	/**
	 * @param cnab1 the cnab1 to set
	 */
	public void setCnab1(String cnab1) {
		this.cnab1 = cnab1;
	}
	
	/**
	 * @return the layoutLote
	 */
	@Field(offset=14, length=3, align=Align.RIGHT, paddingChar='0')
	public Long getLayoutLote() {
		return layoutLote;
	}
	
	/**
	 * @param layoutLote the layoutLote to set
	 */
	public void setLayoutLote(Long layoutLote) {
		this.layoutLote = layoutLote;
	}
	
	/**
	 * @return the cnab2
	 */
	@Field(offset=17, length=1, align=Align.LEFT, paddingChar=' ')
	public String getCnab2() {
		return cnab2;
	}
	
	/**
	 * @param cnab2 the cnab2 to set
	 */
	public void setCnab2(String cnab2) {
		this.cnab2 = cnab2;
	}
	
	/**
	 * @return the tipoInscicao
	 */
	@Field(offset=18, length=1, align=Align.RIGHT, paddingChar='0')
	public Long getTipoInscicao() {
		return tipoInscicao;
	}
	
	/**
	 * @param tipoInscicao the tipoInscicao to set
	 */
	public void setTipoInscicao(Long tipoInscicao) {
		this.tipoInscicao = tipoInscicao;
	}
	
	/**
	 * @return the numeroInscricao
	 */
	@Field(offset=19, length=15, align=Align.RIGHT, paddingChar='0')
	public Long getNumeroInscricao() {
		return numeroInscricao;
	}
	
	/**
	 * @param numeroInscricao the numeroInscricao to set
	 */
	public void setNumeroInscricao(Long numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}
	
	/**
	 * @return the convenio
	 */
	@Field(offset=34, length=20, align=Align.LEFT, paddingChar='0')
	public String getConvenio() {
		return convenio;
	}
	
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	/**
	 * @return the codigoAgencia
	 */
	@Field(offset=54, length=5, align=Align.RIGHT, paddingChar='0')
	public Long getCodigoAgencia() {
		return codigoAgencia;
	}
	
	/**
	 * @param codigoAgencia the codigoAgencia to set
	 */
	public void setCodigoAgencia(Long codigoAgencia) {
		this.codigoAgencia = codigoAgencia;
	}
	
	/**
	 * @return the dvAgencia
	 */
	@Field(offset=59, length=1, align=Align.LEFT, paddingChar='0')
	public String getDvAgencia() {
		return dvAgencia;
	}
	
	/**
	 * @param dvAgencia the dvAgencia to set
	 */
	public void setDvAgencia(String dvAgencia) {
		this.dvAgencia = dvAgencia;
	}
	
	/**
	 * @return the numeroConta
	 */
	@Field(offset=60, length=12, align=Align.RIGHT, paddingChar='0')
	public Long getNumeroConta() {
		return numeroConta;
	}
	
	/**
	 * @param numeroConta the numeroConta to set
	 */
	public void setNumeroConta(Long numeroConta) {
		this.numeroConta = numeroConta;
	}
	
	/**
	 * @return the dvConta
	 */
	@Field(offset=72, length=1, align=Align.LEFT, paddingChar='0')
	public String getDvConta() {
		return dvConta;
	}
	
	/**
	 * @param dvConta the dvConta to set
	 */
	public void setDvConta(String dvConta) {
		this.dvConta = dvConta;
	}
	
	/**
	 * @return the dvAgenciaConta
	 */
	@Field(offset=73, length=1, align=Align.LEFT, paddingChar='0')
	public String getDvAgenciaConta() {
		return dvAgenciaConta;
	}
	
	/**
	 * @param dvAgenciaConta the dvAgenciaConta to set
	 */
	public void setDvAgenciaConta(String dvAgenciaConta) {
		this.dvAgenciaConta = dvAgenciaConta;
	}
	
	/**
	 * @return the nomeEmpresa
	 */
	@Field(offset=74, length=30, align=Align.LEFT, paddingChar='0')
	public String getNomeEmpresa() {
		return nomeEmpresa;
	}
	
	/**
	 * @param nomeEmpresa the nomeEmpresa to set
	 */
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}
	
	/**
	 * @return the informacao1
	 */
	@Field(offset=104, length=40, align=Align.LEFT, paddingChar='0')
	public String getInformacao1() {
		return informacao1;
	}
	
	/**
	 * @param informacao1 the informacao1 to set
	 */
	public void setInformacao1(String informacao1) {
		this.informacao1 = informacao1;
	}
	
	/**
	 * @return the informacao2
	 */
	@Field(offset=144, length=40, align=Align.LEFT, paddingChar='0')
	public String getInformacao2() {
		return informacao2;
	}
	
	/**
	 * @param informacao2 the informacao2 to set
	 */
	public void setInformacao2(String informacao2) {
		this.informacao2 = informacao2;
	}
	
	/**
	 * @return the numeroRemessaRetorno
	 */
	@Field(offset=184, length=8, align=Align.RIGHT, paddingChar='0')
	public Long getNumeroRemessaRetorno() {
		return numeroRemessaRetorno;
	}
	
	/**
	 * @param numeroRemessaRetorno the numeroRemessaRetorno to set
	 */
	public void setNumeroRemessaRetorno(Long numeroRemessaRetorno) {
		this.numeroRemessaRetorno = numeroRemessaRetorno;
	}
	
	/**
	 * @return the dataGravacaoRemessaRetorno
	 */
	@Field(offset=192, length=8, align=Align.RIGHT, paddingChar='0')
	public Long getDataGravacaoRemessaRetorno() {
		return dataGravacaoRemessaRetorno;
	}
	
	/**
	 * @param dataGravacaoRemessaRetorno the dataGravacaoRemessaRetorno to set
	 */
	public void setDataGravacaoRemessaRetorno(Long dataGravacaoRemessaRetorno) {
		this.dataGravacaoRemessaRetorno = dataGravacaoRemessaRetorno;
	}
	
	/**
	 * @return the dataCredito
	 */
	@Field(offset=200, length=8, align=Align.RIGHT, paddingChar='0')
	public Long getDataCredito() {
		return dataCredito;
	}
	
	/**
	 * @param dataCredito the dataCredito to set
	 */
	public void setDataCredito(Long dataCredito) {
		this.dataCredito = dataCredito;
	}
	
	/**
	 * @return the cnab3
	 */
	@Field(offset=208, length=33, align=Align.LEFT, paddingChar=' ')
	public String getCnab3() {
		return cnab3;
	}
	
	/**
	 * @param cnab3 the cnab3 to set
	 */
	public void setCnab3(String cnab3) {
		this.cnab3 = cnab3;
	}
	
}




