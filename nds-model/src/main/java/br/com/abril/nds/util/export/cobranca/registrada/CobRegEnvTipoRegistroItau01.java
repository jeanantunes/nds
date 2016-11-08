package br.com.abril.nds.util.export.cobranca.registrada;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;

public class CobRegEnvTipoRegistroItau01 extends CobRegBaseDTO {
	
	@CobRegfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "1";
	
	@CobRegfield(tamanho = 2, tipo = "char", ordem = 2)
	private String codigoInscricao;
	
	@CobRegfield(tamanho = 14, tipo = "char", ordem = 3)
	private String numeroInscricao;

	@CobRegfield(tamanho = 4, tipo = "char", ordem = 4)
	private String agenciaCedente;
	
	@CobRegfield(tamanho = 2, tipo = "char", ordem = 5)
	private String zeros;

	@CobRegfield(tamanho = 5, tipo = "char", ordem = 6)
	private String contaCliente;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 7)
	private String digitoConta;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=8)
	private String brancos;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=9)
	private String instrucao;
	
	@CobRegfield(tamanho = 25, tipo="char", ordem=10)
	private String usoDaEmpresa;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=11)
	private String nossoNumero;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=12)
	private String qtdeMoeda;
	
	@CobRegfield(tamanho = 3, tipo="char", ordem=13)
	private String numeroCarteira;
	
	@CobRegfield(tamanho = 21, tipo="char", ordem=14)
	private String usoBanco;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=15)
	private String carteira;
	
	@CobRegfield(tamanho = 2, tipo="numeric", ordem=16)
	private String codOcerrencia;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=17)
	private String numeroDocumento;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=18)
	private String dataVencimento;
	
	@CobRegfield(tamanho= 13, tipo="numeric", ordem=19)
	private String valorTitulo;
	
	@CobRegfield(tamanho= 3, tipo="char", ordem=20)
	private String numeroBanco;
	
	@CobRegfield(tamanho = 5, tipo="char", ordem=21)
	private String agencia;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=22)
	private String especie;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=23)
	private String aceite;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=24)
	private String dataEmissao;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=25)
	private String codigoInstrucao;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=26)
	private String codigoInstrucao2;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=27)
	private String jurosDia;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=28)
	private String dataDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=29)
	private String valorDesconto;

	@CobRegfield(tamanho = 13, tipo="char", ordem=30)
	private String valorIOC;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=31)
	private String valorAbatimento;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=32)
	private String codigoInscricaoSacado;
	
	@CobRegfield(tamanho = 14, tipo="char", ordem=33)
	private String numeroCNPJCPF;
	
	@CobRegfield(tamanho = 30, tipo="char", ordem=34)
	private String nomeSacado;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=35)
	private String brancos01;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=36)
	private String enderecoSacado;
	
	@CobRegfield(tamanho = 12, tipo="char", ordem=37)
	private String bairro;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=38)
	private String CEP;
	
	@CobRegfield(tamanho = 15, tipo="char", ordem=39)
	private String cidade;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=40)
	private String UF;
	
	@CobRegfield(tamanho = 30, tipo="char", ordem=41)
	private String sacadoAvalista;
		
	@CobRegfield(tamanho = 4, tipo="char", ordem=42)
	private String brancos1;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=43)
	private String dataDeMora;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=44)
	private String prazo;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=45)
	private String branco2;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=46)
	private String sequencialRegistro;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	
	public String getAgenciaCedente() {
		return agenciaCedente;
	}

	public void setAgenciaCedente(String agenciaCedente) {
		this.agenciaCedente = StringUtils.leftPad(agenciaCedente, 4, '0');
	}

	public String getZeros() {
		return zeros;
	}

	public void setZeros(String zeros) {
		this.zeros = StringUtils.leftPad(zeros, 2, '0');
	}

	public String getContaCliente() {
		return contaCliente;
	}

	public void setContaCliente(String contaCliente) {
		this.contaCliente = StringUtils.leftPad(contaCliente, 7, '0');
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getBrancos() {
		return brancos;
	}

	public void setBrancos(String brancos) {
		this.brancos = StringUtils.leftPad(brancos, 4, ' ');
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
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
		this.valorTitulo = StringUtils.leftPad(valorTitulo, 13, '0');
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = StringUtils.leftPad(agencia, 5, '0');
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getAceite() {
		return aceite;
	}

	public void setAceite(String aceite) {
		this.aceite = aceite;
	}

	public String getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(String dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	public String getCodigoInstrucao() {
		return codigoInstrucao;
	}

	public void setCodigoInstrucao(String codigoInstrucao) {
		this.codigoInstrucao = StringUtils.leftPad(codigoInstrucao, 4, '0');
	}

	public String getJurosDia() {
		return jurosDia;
	}

	public void setJurosDia(String jurosDia) {
		this.jurosDia = StringUtils.leftPad(jurosDia, 13, '0');
	}

	public String getDataDesconto() {
		return dataDesconto;
	}

	public void setDataDesconto(String dataDesconto) {
		this.dataDesconto = StringUtils.leftPad(dataDesconto, 6, '0');
	}

	public String getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = StringUtils.leftPad(valorDesconto, 13, '0');
	}

	public String getValorIOC() {
		return valorIOC;
	}

	public void setValorIOC(String valorIOC) {
		this.valorIOC = StringUtils.leftPad(valorIOC, 13, '0');
	}

	public String getValorAbatimento() {
		return valorAbatimento;
	}

	public void setValorAbatimento(String valorAbatimento) {
		this.valorAbatimento = StringUtils.leftPad(valorAbatimento, 13, '0');
	}

	public String getCodigoInscricaoSacado() {
		return codigoInscricaoSacado;
	}

	public void setCodigoInscricaoSacado(String codigoInscricaoSacado) {
		this.codigoInscricaoSacado = StringUtils.leftPad(codigoInscricaoSacado, 2, '0');
	}

	public String getNumeroCNPJCPF() {
		return numeroCNPJCPF;
	}

	public void setNumeroCNPJCPF(String numeroCNPJCPF) {
		this.numeroCNPJCPF = StringUtils.leftPad(numeroCNPJCPF, 14, '0');
	}

	public String getNomeSacado() {
		return nomeSacado;
	}

	public void setNomeSacado(String nomeSacado) {
		this.nomeSacado = nomeSacado;
	}

	public String getEnderecoSacado() {
		return enderecoSacado;
	}

	public void setEnderecoSacado(String enderecoSacado) {
		this.enderecoSacado = enderecoSacado;
	}
	public String getCEP() {
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUF() {
		return UF;
	}

	public void setUF(String uF) {
		UF = uF;
	}
	
	public String getCodigoInscricao() {
		return codigoInscricao;
	}

	public void setCodigoInscricao(String codigoInscricao) {
		this.codigoInscricao = codigoInscricao;
	}

	public String getNumeroInscricao() {
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public String getInstrucao() {
		return instrucao;
	}

	public void setInstrucao(String instrucao) {
		this.instrucao = instrucao;
	}

	public String getUsoDaEmpresa() {
		return usoDaEmpresa;
	}

	public void setUsoDaEmpresa(String usoDaEmpresa) {
		this.usoDaEmpresa = StringUtils.leftPad(usoDaEmpresa, 25, ' ');
	}
	
	public String getQtdeMoeda() {
		return qtdeMoeda;
	}

	public void setQtdeMoeda(String qtdeMoeda) {
		this.qtdeMoeda = StringUtils.leftPad(qtdeMoeda, 13, '0');
	}

	public String getNumeroCarteira() {
		return numeroCarteira;
	}

	public void setNumeroCarteira(String numeroCarteira) {
		this.numeroCarteira = numeroCarteira;
	}

	public String getUsoBanco() {
		return usoBanco;
	}

	public void setUsoBanco(String usoBanco) {
		this.usoBanco = StringUtils.leftPad(usoBanco, 21, ' ');
	}

	public String getCodOcerrencia() {
		return codOcerrencia;
	}

	public void setCodOcerrencia(String codOcerrencia) {
		this.codOcerrencia = codOcerrencia;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = StringUtils.leftPad(numeroDocumento, 10, '0');
	}

	public String getCodigoInstrucao2() {
		return codigoInstrucao2;
	}

	public void setCodigoInstrucao2(String codigoInstrucao2) {
		this.codigoInstrucao2 = codigoInstrucao2;
	}

	public String getBrancos01() {
		return brancos01;
	}

	public void setBracos01(String brancos01) {
		this.brancos01 = brancos01;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getSacadoAvalista() {
		return sacadoAvalista;
	}

	public void setSacadoAvalista(String sacadoAvalista) {
		this.sacadoAvalista = sacadoAvalista;
	}

	public String getBrancos1() {
		return brancos1;
	}

	public void setBrancos1(String brancos1) {
		this.brancos1 = brancos1;
	}

	public String getDataDeMora() {
		return dataDeMora;
	}

	public void setDataDeMora(String dataDeMora) {
		this.dataDeMora = dataDeMora;
	}

	public String getPrazo() {
		return prazo;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}

	public String getBranco2() {
		return branco2;
	}

	public void setBranco2(String branco2) {
		this.branco2 = StringUtils.leftPad(branco2, 1, ' ');
	}

	public String getSequencialRegistro() {
		return sequencialRegistro;
	}

	public void setSequencialRegistro(String sequencialRegistro) {
		this.sequencialRegistro = StringUtils.leftPad(sequencialRegistro, 6, '0');
	}
}