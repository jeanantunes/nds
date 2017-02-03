package br.com.abril.nds.util.export.cobranca.registrada;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;

public class CobRegEnvTipoRegistroBradesco01 extends CobRegBaseDTO {
	
	@CobRegfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "1";
	
	@CobRegfield(tamanho = 5, tipo = "char", ordem = 2)
	private String agenciaDebito;
	
	@CobRegfield(tamanho = 1, tipo = "char", ordem = 3)
	private String digitoAgenciaDebito;

	@CobRegfield(tamanho = 5, tipo = "char", ordem = 4)
	private String razaoContaCorrente;
	
	@CobRegfield(tamanho = 7, tipo = "char", ordem = 5)
	private String contaCorrente;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 6)
	private String digitoContaCorrente;
	
	@CobRegfield(tamanho = 17, tipo="char", ordem=7)
	private String identificacaoEmpresaBeneficiaria;
	
	@CobRegfield(tamanho = 25, tipo="char", ordem=8)
	private String numeroControlePaticipante;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=9)
	private String codigoBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=10)
	private String campoMulta;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=11)
	private String percentualMulta;
	
	@CobRegfield(tamanho = 11, tipo="char", ordem=12)
	private String identificacaoTituloBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=13)
	private String digitoConferenciaBancaria;
	
	@CobRegfield(tamanho = 10, tipo="numeric", ordem=14)
	private String descontoBonificacao;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=15)
	private String condicaoEmissaoPapeleta;
	
	@CobRegfield(tamanho = 1, tipo="numeric", ordem=16)
	private String identificacaoBoletoDebitoAutomatico;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=17)
	private String identificaOperacaoBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=18)
	private String indicadorRateiroCredito;
	
	@CobRegfield(tamanho= 1, tipo="numeric", ordem=19)
	private String enderecamentoAvisoDebitoAutomatico;
	
	@CobRegfield(tamanho= 2, tipo="char", ordem=20)
	private String branco;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=21)
	private String identificacaoOcorrencia;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=22)
	private String numeroDocumento;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=23)
	private String dataVencimento;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=24)
	private String valorTitulo;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=25)
	private String bancoEncarregadoCobranca;
	
	@CobRegfield(tamanho = 5, tipo="char", ordem=26)
	private String agenciaDepositaria;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=27)
	private String especieTitulo;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=28)
	private String identificacao;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=29)
	private String dataEmissaoTitulo;

	@CobRegfield(tamanho = 2, tipo="char", ordem=30)
	private String instrucao;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=31)
	private String instrucao2;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=32)
	private String jurosdia;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=33)
	private String dataLimiteCocessaoDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=34)
	private String valorDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=35)
	private String valorIOF;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=36)
	private String valorAbatimento;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=37)
	private String identificacaoTipoIncricaoPagador;
	
	@CobRegfield(tamanho = 14, tipo="char", ordem=38)
	private String numeroIncricaoPagador;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=39)
	private String nomePagador;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=40)
	private String enderecoCompleto;
	
	@CobRegfield(tamanho = 12, tipo="char", ordem=41)
	private String mensagemCompleto;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=42)
	private String cepCompleto;
	
	@CobRegfield(tamanho = 60, tipo="char", ordem=43)
	private String sacadoAvalista;

	@CobRegfield(tamanho = 6, tipo="char", ordem=44)
	private String sequencialBradescoRegistro;
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = StringUtils.leftPad(tipoRegistro, 1, '0');
	}

	public String getAgenciaDebito() {
		return agenciaDebito;
	}

	public void setAgenciaDebito(String agenciaDebito) {
		this.agenciaDebito = StringUtils.leftPad(agenciaDebito, 5, '0');
	}

	public String getDigitoAgenciaDebito() {
		return digitoAgenciaDebito;
	}

	public void setDigitoAgenciaDebito(String digitoAgenciaDebito) {
		this.digitoAgenciaDebito = StringUtils.leftPad(digitoAgenciaDebito, 1, ' ');
	}

	public String getRazaoContaCorrente() {
		return razaoContaCorrente;
	}

	public void setRazaoContaCorrente(String razaoContaCorrente) {
		this.razaoContaCorrente = StringUtils.leftPad(razaoContaCorrente, 5, '0');
	}

	public String getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = StringUtils.leftPad(contaCorrente, 7, '0');
	}

	public String getDigitoContaCorrente() {
		return digitoContaCorrente;
	}

	public void setDigitoContaCorrente(String digitoContaCorrente) {
		this.digitoContaCorrente = StringUtils.leftPad(digitoContaCorrente, 1, ' ');
	}

	public String getIdentificacaoEmpresaBeneficiaria() {
		return identificacaoEmpresaBeneficiaria;
	}

	public void setIdentificacaoEmpresaBeneficiaria(String identificacaoEmpresaBeneficiaria) {
		this.identificacaoEmpresaBeneficiaria = StringUtils.leftPad(identificacaoEmpresaBeneficiaria, 17, ' ');
	}

	public String getNumeroControlePaticipante() {
		return numeroControlePaticipante;
	}

	public void setNumeroControlePaticipante(String numeroControlePaticipante) {
		this.numeroControlePaticipante = StringUtils.leftPad(numeroControlePaticipante, 25, ' ');
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = StringUtils.leftPad(codigoBanco, 3, ' ');
	}

	public String getCampoMulta() {
		return campoMulta;
	}

	public void setCampoMulta(String campoMulta) {
		this.campoMulta = StringUtils.leftPad(campoMulta, 1, ' ');
	}

	public String getPercentualMulta() {
		return percentualMulta;
	}

	public void setPercentualMulta(String percentualMulta) {
		this.percentualMulta = StringUtils.leftPad(percentualMulta, 4, ' ');
	}

	public String getIdentificacaoTituloBanco() {
		return identificacaoTituloBanco;
	}

	public void setIdentificacaoTituloBanco(String identificacaoTituloBanco) {
		this.identificacaoTituloBanco = StringUtils.leftPad(identificacaoTituloBanco, 11, ' ');
	}

	public String getDigitoConferenciaBancaria() {
		return digitoConferenciaBancaria;
	}

	public void setDigitoConferenciaBancaria(String digitoConferenciaBancaria) {
		this.digitoConferenciaBancaria = StringUtils.leftPad(digitoConferenciaBancaria, 1, '0');
	}

	public String getDescontoBonificacao() {
		return descontoBonificacao;
	}

	public void setDescontoBonificacao(String descontoBonificacao) {
		this.descontoBonificacao = StringUtils.leftPad(descontoBonificacao, 10, ' ');
	}

	public String getCondicaoEmissaoPapeleta() {
		return condicaoEmissaoPapeleta;
	}

	public void setCondicaoEmissaoPapeleta(String condicaoEmissaoPapeleta) {
		this.condicaoEmissaoPapeleta = StringUtils.leftPad(condicaoEmissaoPapeleta, 1, ' ');
	}

	public String getIdentificacaoBoletoDebitoAutomatico() {
		return identificacaoBoletoDebitoAutomatico;
	}

	public void setIdentificacaoBoletoDebitoAutomatico(String identificacaoBoletoDebitoAutomatico) {
		this.identificacaoBoletoDebitoAutomatico = StringUtils.leftPad(identificacaoBoletoDebitoAutomatico, 1, ' ');
	}

	public String getIdentificaOperacaoBanco() {
		return identificaOperacaoBanco;
	}

	public void setIdentificaOperacaoBanco(String identificaOperacaoBanco) {
		this.identificaOperacaoBanco = StringUtils.leftPad(identificaOperacaoBanco, 10, ' ');
	}

	public String getIndicadorRateiroCredito() {
		return indicadorRateiroCredito;
	}

	public void setIndicadorRateiroCredito(String indicadorRateiroCredito) {
		this.indicadorRateiroCredito = StringUtils.leftPad(indicadorRateiroCredito, 1, ' ');
	}

	public String getEnderecamentoAvisoDebitoAutomatico() {
		return enderecamentoAvisoDebitoAutomatico;
	}

	public void setEnderecamentoAvisoDebitoAutomatico(String enderecamentoAvisoDebitoAutomatico) {
		this.enderecamentoAvisoDebitoAutomatico = StringUtils.leftPad(enderecamentoAvisoDebitoAutomatico,  1, ' ');
	}

	public String getBranco() {
		return branco;
	}

	public void setBranco(String branco) {
		this.branco = StringUtils.leftPad(branco,  2, ' ');
	}

	public String getIdentificacaoOcorrencia() {
		return identificacaoOcorrencia;
	}

	public void setIdentificacaoOcorrencia(String identificacaoOcorrencia) {
		this.identificacaoOcorrencia = StringUtils.leftPad(identificacaoOcorrencia, 2, '0');
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = StringUtils.leftPad(numeroDocumento, 10, '0');
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = StringUtils.leftPad(dataVencimento, 6, '0');
	}

	public String getValorTitulo() {
		return valorTitulo;
	}

	public void setValorTitulo(String valorTitulo) {
		this.valorTitulo = StringUtils.leftPad(valorTitulo, 13, '0');
	}

	public String getBancoEncarregadoCobranca() {
		return bancoEncarregadoCobranca;
	}

	public void setBancoEncarregadoCobranca(String bancoEncarregadoCobranca) {
		this.bancoEncarregadoCobranca = StringUtils.leftPad(bancoEncarregadoCobranca, 3, '0');
	}

	public String getAgenciaDepositaria() {
		return agenciaDepositaria;
	}

	public void setAgenciaDepositaria(String agenciaDepositaria) {
		this.agenciaDepositaria = StringUtils.leftPad(agenciaDepositaria, 5, '0');
	}

	public String getEspecieTitulo() {
		return especieTitulo;
	}

	public void setEspecieTitulo(String especieTitulo) {
		this.especieTitulo = StringUtils.leftPad(especieTitulo, 2, '0');
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = StringUtils.leftPad(identificacao, 1, '0');;
	}

	public String getDataEmissaoTitulo() {
		return dataEmissaoTitulo;
	}

	public void setDataEmissaoTitulo(String dataEmissaoTitulo) {
		this.dataEmissaoTitulo = StringUtils.leftPad(dataEmissaoTitulo, 6, '0');
	}

	public String getInstrucao() {
		return instrucao;
	}

	public void setInstrucao(String instrucao) {
		this.instrucao = StringUtils.leftPad(instrucao, 2, ' ');
	}

	public String getInstrucao2() {
		return instrucao2;
	}

	public void setInstrucao2(String instrucao2) {
		this.instrucao2 = StringUtils.leftPad(instrucao2, 2, ' ');
	}

	public String getJurosdia() {
		return jurosdia;
	}

	public void setJurosdia(String jurosdia) {
		this.jurosdia = StringUtils.leftPad(jurosdia, 13, '0');
	}

	public String getDataLimiteCocessaoDesconto() {
		return dataLimiteCocessaoDesconto;
	}

	public void setDataLimiteCocessaoDesconto(String dataLimiteCocessaoDesconto) {
		this.dataLimiteCocessaoDesconto = StringUtils.leftPad(dataLimiteCocessaoDesconto, 6, '0');
	}

	public String getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = StringUtils.leftPad(valorDesconto, 13, '0');
	}

	public String getValorIOF() {
		return valorIOF;
	}

	public void setValorIOF(String valorIOF) {
		this.valorIOF = StringUtils.leftPad(valorIOF, 13, '0');
	}

	public String getValorAbatimento() {
		return valorAbatimento;
	}

	public void setValorAbatimento(String valorAbatimento) {
		this.valorAbatimento = StringUtils.leftPad(valorAbatimento, 13, '0');
	}
	
	public String getIdentificacaoTipoIncricaoPagador() {
		return identificacaoTipoIncricaoPagador;
	}

	public void setIdentificacaoTipoIncricaoPagador(String identificacaoTipoIncricaoPagador) {
		this.identificacaoTipoIncricaoPagador = StringUtils.leftPad(identificacaoTipoIncricaoPagador, 2, '0');;
	}

	public String getNumeroIncricaoPagador() {
		return numeroIncricaoPagador;
	}

	public void setNumeroIncricaoPagador(String numeroIncricaoPagador) {
		this.numeroIncricaoPagador =  StringUtils.leftPad(numeroIncricaoPagador, 14, '0');
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = StringUtils.rightPad(nomePagador, 40, ' ');
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = StringUtils.leftPad(enderecoCompleto, 40, ' ');
	}

	public String getMensagemCompleto() {
		return mensagemCompleto;
	}

	public void setMensagemCompleto(String mensagemCompleto) {
		this.mensagemCompleto = StringUtils.leftPad(mensagemCompleto, 12, ' ');
	}
	
	public String getCepCompleto() {
		return cepCompleto;
	}

	public void setCepCompleto(String cepCompleto) {
		this.cepCompleto = StringUtils.leftPad(cepCompleto, 8, ' ');
	}

	public String getSacadoAvalista() {
		return sacadoAvalista;
	}

	public void setSacadoAvalista(String sacadoAvalista) {
		this.sacadoAvalista = StringUtils.rightPad(sacadoAvalista, 60, ' ');
	}
	
	public String getSequencialBradescoRegistro() {
		return sequencialBradescoRegistro;
	}

	public void setSequencialBradescoRegistro(String sequencialBradescoRegistro) {
		this.sequencialBradescoRegistro = StringUtils.leftPad(sequencialBradescoRegistro, 6, '0');
	}
}