package br.com.abril.nds.util.export.cobranca.registrada;

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

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 7)
	private String digitoContaCorrente;
	
	@CobRegfield(tamanho = 17, tipo="char", ordem=8)
	private String identificacaoEmpresaBeneficiaria;
	
	@CobRegfield(tamanho = 25, tipo="char", ordem=9)
	private String numeroControlePaticipante;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=10)
	private String codigoBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=11)
	private String campoMulta;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=12)
	private String percentualMulta;
	
	@CobRegfield(tamanho = 11, tipo="char", ordem=13)
	private String identificacaoTituloBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=14)
	private String digitoConferenciaBancaria;
	
	@CobRegfield(tamanho = 10, tipo="numeric", ordem=15)
	private String descontoBonificacao;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=16)
	private String condicaoEmissaoPapeleta;
	
	@CobRegfield(tamanho = 1, tipo="numeric", ordem=17)
	private String identificacaoBoletoDebitoAutomatico;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=18)
	private String identificaOperacaoBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=19)
	private String indicadorRateiroCredito;
	
	@CobRegfield(tamanho= 1, tipo="numeric", ordem=20)
	private String enderecamentoAvisoDebitoAutomatico;
	
	@CobRegfield(tamanho= 2, tipo="char", ordem=21)
	private String branco;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=22)
	private String identificacaoOcorrencia;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=23)
	private String numeroDocumento;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=24)
	private String dataVencimento;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=25)
	private String valorTitulo;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=26)
	private String bancoEncarregadoCobranca;
	
	@CobRegfield(tamanho = 5, tipo="char", ordem=27)
	private String agenciaDepositaria;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=28)
	private String especieTitulo;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=29)
	private String identificacao;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=30)
	private String dataEmissaoTitulo;

	@CobRegfield(tamanho = 2, tipo="char", ordem=31)
	private String instrucao;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=32)
	private String instrucao2;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=33)
	private String jurosdia;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=34)
	private String dataLimiteCocessaoDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=35)
	private String valorDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=36)
	private String valorIOF;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=37)
	private String valorAbatimento;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=38)
	private String identificacaoTipoIncricaoPagador;
	
	@CobRegfield(tamanho = 14, tipo="char", ordem=39)
	private String numeroIncricaoPagador;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=40)
	private String nomePagador;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=41)
	private String enderecoCompleto;
	
	@CobRegfield(tamanho = 12, tipo="char", ordem=42)
	private String mensagemCompleto;
	
	@CobRegfield(tamanho = 5, tipo="char", ordem=43)
	private String cep;
		
	@CobRegfield(tamanho = 3, tipo="char", ordem=44)
	private String sufixoCep;
	
	@CobRegfield(tamanho = 60, tipo="char", ordem=45)
	private String sacadoAvalista;

	@CobRegfield(tamanho = 6, tipo="char", ordem=46)
	private String sequencialRegistro;
	
	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getAgenciaDebito() {
		return agenciaDebito;
	}

	public void setAgenciaDebito(String agenciaDebito) {
		this.agenciaDebito = agenciaDebito;
	}

	public String getDigitoAgenciaDebito() {
		return digitoAgenciaDebito;
	}

	public void setDigitoAgenciaDebito(String digitoAgenciaDebito) {
		this.digitoAgenciaDebito = digitoAgenciaDebito;
	}

	public String getRazaoContaCorrente() {
		return razaoContaCorrente;
	}

	public void setRazaoContaCorrente(String razaoContaCorrente) {
		this.razaoContaCorrente = razaoContaCorrente;
	}

	public String getContaCorrente() {
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public String getDigitoContaCorrente() {
		return digitoContaCorrente;
	}

	public void setDigitoContaCorrente(String digitoContaCorrente) {
		this.digitoContaCorrente = digitoContaCorrente;
	}

	public String getIdentificacaoEmpresaBeneficiaria() {
		return identificacaoEmpresaBeneficiaria;
	}

	public void setIdentificacaoEmpresaBeneficiaria(String identificacaoEmpresaBeneficiaria) {
		this.identificacaoEmpresaBeneficiaria = identificacaoEmpresaBeneficiaria;
	}

	public String getNumeroControlePaticipante() {
		return numeroControlePaticipante;
	}

	public void setNumeroControlePaticipante(String numeroControlePaticipante) {
		this.numeroControlePaticipante = numeroControlePaticipante;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getCampoMulta() {
		return campoMulta;
	}

	public void setCampoMulta(String campoMulta) {
		this.campoMulta = campoMulta;
	}

	public String getPercentualMulta() {
		return percentualMulta;
	}

	public void setPercentualMulta(String percentualMulta) {
		this.percentualMulta = percentualMulta;
	}

	public String getIdentificacaoTituloBanco() {
		return identificacaoTituloBanco;
	}

	public void setIdentificacaoTituloBanco(String identificacaoTituloBanco) {
		this.identificacaoTituloBanco = identificacaoTituloBanco;
	}

	public String getDigitoConferenciaBancaria() {
		return digitoConferenciaBancaria;
	}

	public void setDigitoConferenciaBancaria(String digitoConferenciaBancaria) {
		this.digitoConferenciaBancaria = digitoConferenciaBancaria;
	}

	public String getDescontoBonificacao() {
		return descontoBonificacao;
	}

	public void setDescontoBonificacao(String descontoBonificacao) {
		this.descontoBonificacao = descontoBonificacao;
	}

	public String getCondicaoEmissaoPapeleta() {
		return condicaoEmissaoPapeleta;
	}

	public void setCondicaoEmissaoPapeleta(String condicaoEmissaoPapeleta) {
		this.condicaoEmissaoPapeleta = condicaoEmissaoPapeleta;
	}

	public String getIdentificacaoBoletoDebitoAutomatico() {
		return identificacaoBoletoDebitoAutomatico;
	}

	public void setIdentificacaoBoletoDebitoAutomatico(String identificacaoBoletoDebitoAutomatico) {
		this.identificacaoBoletoDebitoAutomatico = identificacaoBoletoDebitoAutomatico;
	}

	public String getIdentificaOperacaoBanco() {
		return identificaOperacaoBanco;
	}

	public void setIdentificaOperacaoBanco(String identificaOperacaoBanco) {
		this.identificaOperacaoBanco = identificaOperacaoBanco;
	}

	public String getIndicadorRateiroCredito() {
		return indicadorRateiroCredito;
	}

	public void setIndicadorRateiroCredito(String indicadorRateiroCredito) {
		this.indicadorRateiroCredito = indicadorRateiroCredito;
	}

	public String getEnderecamentoAvisoDebitoAutomatico() {
		return enderecamentoAvisoDebitoAutomatico;
	}

	public void setEnderecamentoAvisoDebitoAutomatico(String enderecamentoAvisoDebitoAutomatico) {
		this.enderecamentoAvisoDebitoAutomatico = enderecamentoAvisoDebitoAutomatico;
	}

	public String getBranco() {
		return branco;
	}

	public void setBranco(String branco) {
		this.branco = branco;
	}

	public String getIdentificacaoOcorrencia() {
		return identificacaoOcorrencia;
	}

	public void setIdentificacaoOcorrencia(String identificacaoOcorrencia) {
		this.identificacaoOcorrencia = identificacaoOcorrencia;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getValorTitulo() {
		return valorTitulo;
	}

	public void setValorTitulo(String valorTitulo) {
		this.valorTitulo = valorTitulo;
	}

	public String getBancoEncarregadoCobranca() {
		return bancoEncarregadoCobranca;
	}

	public void setBancoEncarregadoCobranca(String bancoEncarregadoCobranca) {
		this.bancoEncarregadoCobranca = bancoEncarregadoCobranca;
	}

	public String getAgenciaDepositaria() {
		return agenciaDepositaria;
	}

	public void setAgenciaDepositaria(String agenciaDepositaria) {
		this.agenciaDepositaria = agenciaDepositaria;
	}

	public String getEspecieTitulo() {
		return especieTitulo;
	}

	public void setEspecieTitulo(String especieTitulo) {
		this.especieTitulo = especieTitulo;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getDataEmissaoTitulo() {
		return dataEmissaoTitulo;
	}

	public void setDataEmissaoTitulo(String dataEmissaoTitulo) {
		this.dataEmissaoTitulo = dataEmissaoTitulo;
	}

	public String getInstrucao() {
		return instrucao;
	}

	public void setInstrucao(String instrucao) {
		this.instrucao = instrucao;
	}

	public String getInstrucao2() {
		return instrucao2;
	}

	public void setInstrucao2(String instrucao2) {
		this.instrucao2 = instrucao2;
	}

	public String getJurosdia() {
		return jurosdia;
	}

	public void setJurosdia(String jurosdia) {
		this.jurosdia = jurosdia;
	}

	public String getDataLimiteCocessaoDesconto() {
		return dataLimiteCocessaoDesconto;
	}

	public void setDataLimiteCocessaoDesconto(String dataLimiteCocessaoDesconto) {
		this.dataLimiteCocessaoDesconto = dataLimiteCocessaoDesconto;
	}

	public String getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getValorIOF() {
		return valorIOF;
	}

	public void setValorIOF(String valorIOF) {
		this.valorIOF = valorIOF;
	}

	public String getValorAbatimento() {
		return valorAbatimento;
	}

	public void setValorAbatimento(String valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}
	
	public String getIdentificacaoTipoIncricaoPagador() {
		return identificacaoTipoIncricaoPagador;
	}

	public void setIdentificacaoTipoIncricaoPagador(String identificacaoTipoIncricaoPagador) {
		this.identificacaoTipoIncricaoPagador = identificacaoTipoIncricaoPagador;
	}

	public String getNumeroIncricaoPagador() {
		return numeroIncricaoPagador;
	}

	public void setNumeroIncricaoPagador(String numeroIncricaoPagador) {
		this.numeroIncricaoPagador = numeroIncricaoPagador;
	}

	public String getNomePagador() {
		return nomePagador;
	}

	public void setNomePagador(String nomePagador) {
		this.nomePagador = nomePagador;
	}

	public String getEnderecoCompleto() {
		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

	public String getMensagemCompleto() {
		return mensagemCompleto;
	}

	public void setMensagemCompleto(String mensagemCompleto) {
		this.mensagemCompleto = mensagemCompleto;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getSufixoCep() {
		return sufixoCep;
	}

	public void setSufixoCep(String sufixoCep) {
		this.sufixoCep = sufixoCep;
	}

	public String getSacadoAvalista() {
		return sacadoAvalista;
	}

	public void setSacadoAvalista(String sacadoAvalista) {
		this.sacadoAvalista = sacadoAvalista;
	}

	public String getSequencialRegistro() {
		return sequencialRegistro;
	}

	public void setSequencialRegistro(String sequencialRegistro) {
		this.sequencialRegistro = sequencialRegistro;
	}
}