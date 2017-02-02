package br.com.abril.nds.util.export.cobranca.registrada;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;

public class CobRegEnvTipoRegistroBradesco00 extends CobRegBaseDTO {

	@CobRegfield(tamanho = 1, tipo="char", ordem=1)
	private String tipoRegistro="0";
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=2)
	private String identificacaoArquivoRemessa;

	@CobRegfield(tamanho = 7, tipo="char", ordem=3)
	private String identificacaoExtenso;

	@CobRegfield(tamanho = 2, tipo="char", ordem=4)
	private String codigoServico;

	@CobRegfield(tamanho = 15, tipo="char", ordem=5)
	private String literalServicos;
	
	@CobRegfield(tamanho = 20, tipo="char", ordem=6)
	private String codigoEmpresa; 
	
	@CobRegfield(tamanho = 30, tipo="char", ordem=7)
	private String nomeEmpresa;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=11)
	private String numeroBanco;
	
	@CobRegfield(tamanho = 15, tipo="char", ordem=12)
	private String nomeBanco;

	@CobRegfield(tamanho = 6, tipo="char", ordem=13)
	private String dataGravacaoArquivo;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=15)
	private String filler;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=16)
	private String identificacaoSistema;
	
	@CobRegfield(tamanho = 7, tipo="char", ordem=17)
	private String sequencialHeader;
	
	@CobRegfield(tamanho = 277, tipo="char", ordem=15)
	private String filler2;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=16)
	private String sequencial;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getIdentificacaoArquivoRemessa() {
		return identificacaoArquivoRemessa;
	}

	public void setIdentificacaoArquivoRemessa(String identificacaoArquivoRemessa) {
		this.identificacaoArquivoRemessa = identificacaoArquivoRemessa;
	}

	public String getIdentificacaoExtenso() {
		return identificacaoExtenso;
	}

	public void setIdentificacaoExtenso(String identificacaoExtenso) {
		this.identificacaoExtenso = identificacaoExtenso;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
	}

	public String getLiteralServicos() {
		return literalServicos;
	}

	public void setLiteralServicos(String literalServicos) {
		this.literalServicos = literalServicos;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 =  StringUtils.leftPad(filler2, 8, ' ');
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getDataGravacaoArquivo() {
		return dataGravacaoArquivo;
	}

	public void setDataGravacaoArquivo(String dataGravacaoArquivo) {
		this.dataGravacaoArquivo = dataGravacaoArquivo;
	}

	public String getSequencial() {
		return sequencial;
	}

	public void setSequencial(String sequencial) {
		this.sequencial = StringUtils.leftPad(sequencial, 6, '0');;
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public void setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
	}

	public String getNomeEmpresa() {
		return nomeEmpresa;
	}

	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	public String getIdentificacaoSistema() {
		return identificacaoSistema;
	}

	public void setIdentificacaoSistema(String identificacaoSistema) {
		this.identificacaoSistema = identificacaoSistema;
	}

	public String getSequencialHeader() {
		return sequencialHeader;
	}

	public void setSequencialHeader(String sequencialHeader) {
		this.sequencialHeader = sequencialHeader;
	}

}