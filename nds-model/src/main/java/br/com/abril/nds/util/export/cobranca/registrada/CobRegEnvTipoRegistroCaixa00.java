package br.com.abril.nds.util.export.cobranca.registrada;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;
import org.apache.commons.lang.StringUtils;

public class CobRegEnvTipoRegistroCaixa00 extends CobRegBaseDTO {


	@CobRegfield(tamanho = 1, tipo="char", ordem=1)
	private String tipoRegistro;

	@CobRegfield(tamanho = 1, tipo="char", ordem=2)
	private String identificacaoArquivoRemessa;

	@CobRegfield(tamanho = 7, tipo="char", ordem=3)
	private String identificacaoExtenso;

	@CobRegfield(tamanho = 2, tipo="char", ordem=4)
	private String codigoServico;

	@CobRegfield(tamanho = 15, tipo="char", ordem=5)
	private String literalServicos;

	@CobRegfield(tamanho = 4, tipo="char", ordem=6)
	private String agenciaCedente;

	@CobRegfield(tamanho = 6, tipo="char", ordem=7)
	private String contaCliente;

	@CobRegfield(tamanho = 10, tipo="char", ordem=8)
	private String filler;

	@CobRegfield(tamanho = 30, tipo="char", ordem=9)
	private String nomeCliente;

	@CobRegfield(tamanho = 3, tipo="char", ordem=10)
	private String numeroBanco;

	@CobRegfield(tamanho = 15, tipo="char", ordem=11)
	private String nomeBanco;

	@CobRegfield(tamanho = 6, tipo="char", ordem=12)
	private String dataGravacaoArquivo;

	@CobRegfield(tamanho=289, tipo="char", ordem=13)
	private String filler2;

	@CobRegfield(tamanho = 5, tipo="char", ordem=14)
	private String sequencialA;

	@CobRegfield(tamanho = 6, tipo="char", ordem=15)
	private String sequencialB;

	private int sequencial;

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

	public String getAgenciaCedente() {
		return agenciaCedente;
	}

	public void setAgenciaCedente(String agenciaCedente) {
		this.agenciaCedente = StringUtils.leftPad(agenciaCedente, 4, '0');
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = StringUtils.leftPad(filler, 2, ' ');
	}

	public String getContaCliente() {
		return contaCliente;
	}

	public void setContaCliente(String contaCliente) {
		this.contaCliente = StringUtils.leftPad(contaCliente, 7, '0');
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 =  StringUtils.leftPad(filler, 289, ' ');
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
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

	public String getSequencialA() {
		return sequencialA;
	}

	public void setSequencialA(String sequencialA) {
		this.sequencialA = StringUtils.leftPad(sequencialA,5,"0");
	}

	public String getSequencialB() {
		return sequencialB;
	}

	public void setSequencialB(String sequencialB) {
		this.sequencialB = StringUtils.leftPad(sequencialB, 6,"0");
	}

	public int getSequencial() {
		return sequencial;
	}
}