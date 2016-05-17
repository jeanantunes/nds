package br.com.abril.nds.util.export.cobranca.registrada;

import org.apache.commons.lang.StringUtils;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;

public class CobRegEnvTipoRegistro01 extends CobRegBaseDTO {
	
	@CobRegfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "1";
	
	@CobRegfield(tamanho = 16, tipo = "char", ordem = 2)
	private String filler;

	@CobRegfield(tamanho = 4, tipo = "char", ordem = 3)
	private String agenciaCedente;
	
	@CobRegfield(tamanho = 2, tipo = "char", ordem = 4)
	private String filler1;

	@CobRegfield(tamanho = 7, tipo = "char", ordem = 5)
	private String contaCliente;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 6)
	private String digitoConta;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=7)
	private String taxa;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=8)
	private String filler2;
	
	@CobRegfield(tamanho = 25, tipo="char", ordem=9)
	private String numeroControle;
	
	@CobRegfield(tamanho = 7, tipo="char", ordem=10)
	private String nossoNumero;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=11)
	private String digitoNossoNumero;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=12)
	private String numeroContrato;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=13)
	private String dataSegundoDesconto;
	
	@CobRegfield(tamanho = 13, tipo="numeric", ordem=14)
	private String valorSegundoDesconto;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=15)
	private String filler3;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=16)
	private String carteira;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=17)
	private String codigoServico;
	
	@CobRegfield(tamanho = 10, tipo="char", ordem=18)
	private String numero;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=19)
	private String dataVencimento;
	
	@CobRegfield(tamanho= 13, tipo="numeric", ordem=20)
	private String valorTitulo;
	
	@CobRegfield(tamanho= 3, tipo="char", ordem=21)
	private String numeroBanco;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=22)
	private String agencia;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=23)
	private String filler4;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=24)
	private String especie;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=25)
	private String aceite;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=26)
	private String dataEmissao;
	
	@CobRegfield(tamanho = 4, tipo="char", ordem=27)
	private String codigoInstrucao;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=28)
	private String jurosDia;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=29)
	private String dataDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=30)
	private String valorDesconto;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=31)
	private String valorIOC;
	
	@CobRegfield(tamanho = 13, tipo="char", ordem=32)
	private String valorAbatimento;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=33)
	private String codigoInscricaoSacado;
	
	@CobRegfield(tamanho = 14, tipo="char", ordem=34)
	private String numeroCNPJCPF;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=35)
	private String nomeSacado;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=36)
	private String enderecoSacado;
	
	@CobRegfield(tamanho = 12, tipo="char", ordem=37)
	private String complemento;
	
	@CobRegfield(tamanho = 8, tipo="char", ordem=38)
	private String CEP;
	
	@CobRegfield(tamanho = 15, tipo="char", ordem=39)
	private String cidade;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=40)
	private String UF;
	
	@CobRegfield(tamanho = 40, tipo="char", ordem=41)
	private String mensagemCedenteNomeSacadorAvalista;
	
	@CobRegfield(tamanho = 2, tipo="char", ordem=42)
	private String prazoProtesto;
	
	@CobRegfield(tamanho = 1, tipo="char", ordem=43)
	private String codigoMoeda;
	
	@CobRegfield(tamanho = 6, tipo="char", ordem=44)
	private String sequencialRegistro;

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = StringUtils.leftPad(filler, 16, ' ');
	}

	public String getAgenciaCedente() {
		return agenciaCedente;
	}

	public void setAgenciaCedente(String agenciaCedente) {
		this.agenciaCedente = agenciaCedente;
	}

	public String getFiller1() {
		return filler1;
	}

	public void setFiller1(String filler1) {
		this.filler1 = StringUtils.leftPad(filler1, 2, '0');
	}

	public String getContaCliente() {
		return contaCliente;
	}

	public void setContaCliente(String contaCliente) {
		this.contaCliente = contaCliente;
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getTaxa() {
		return taxa;
	}

	public void setTaxa(String taxa) {
		this.taxa = taxa;
	}

	public String getFiller2() {
		return filler2;
	}

	public void setFiller2(String filler2) {
		this.filler2 = filler2;
	}

	public String getNumeroControle() {
		return numeroControle;
	}

	public void setNumeroControle(String numeroControle) {
		this.numeroControle = numeroControle;
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getDigitoNossoNumero() {
		return digitoNossoNumero;
	}

	public void setDigitoNossoNumero(String digitoNossoNumero) {
		this.digitoNossoNumero = digitoNossoNumero;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = StringUtils.leftPad(numeroContrato, 10, '0');
	}

	public String getDataSegundoDesconto() {
		return dataSegundoDesconto;
	}

	public void setDataSegundoDesconto(String dataSegundoDesconto) {
		this.dataSegundoDesconto = StringUtils.leftPad(dataSegundoDesconto, 6, '0');
	}

	public String getValorSegundoDesconto() {
		return valorSegundoDesconto;
	}

	public void setValorSegundoDesconto(String valorSegundoDesconto) {
		this.valorSegundoDesconto = StringUtils.leftPad(valorSegundoDesconto, 13, '0');
	}

	public String getFiller3() {
		return filler3;
	}

	public void setFiller3(String filler3) {
		this.filler3 = StringUtils.leftPad(filler3, 8, ' ');
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = carteira;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public void setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
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
		this.agencia = agencia;
	}

	public String getFiller4() {
		return filler4;
	}

	public void setFiller4(String filler4) {
		this.filler4 = filler4;
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
		this.codigoInstrucao = codigoInstrucao;
	}

	public String getJurosDia() {
		return jurosDia;
	}

	public void setJurosDia(String jurosDia) {
		this.jurosDia = jurosDia;
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
		this.valorAbatimento = valorAbatimento;
	}

	public String getCodigoInscricaoSacado() {
		return codigoInscricaoSacado;
	}

	public void setCodigoInscricaoSacado(String codigoInscricaoSacado) {
		this.codigoInscricaoSacado = codigoInscricaoSacado;
	}

	public String getNumeroCNPJCPF() {
		return numeroCNPJCPF;
	}

	public void setNumeroCNPJCPF(String numeroCNPJCPF) {
		this.numeroCNPJCPF = numeroCNPJCPF;
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

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
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

	public String getMensagemCedenteNomeSacadorAvalista() {
		return mensagemCedenteNomeSacadorAvalista;
	}

	public void setMensagemCedenteNomeSacadorAvalista(String mensagemCedenteNomeSacadorAvalista) {
		this.mensagemCedenteNomeSacadorAvalista = mensagemCedenteNomeSacadorAvalista;
	}

	public String getPrazoProtesto() {
		return prazoProtesto;
	}

	public void setPrazoProtesto(String prazoProtesto) {
		this.prazoProtesto = prazoProtesto;
	}

	public String getCodigoMoeda() {
		return codigoMoeda;
	}

	public void setCodigoMoeda(String codigoMoeda) {
		this.codigoMoeda = codigoMoeda;
	}

	public String getSequencialRegistro() {
		return sequencialRegistro;
	}

	public void setSequencialRegistro(String sequencialRegistro) {
		this.sequencialRegistro = StringUtils.leftPad(sequencialRegistro, 6, '0');
	}
}