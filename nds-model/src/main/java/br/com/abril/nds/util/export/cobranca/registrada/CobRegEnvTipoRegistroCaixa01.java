package br.com.abril.nds.util.export.cobranca.registrada;

import br.com.abril.nds.util.export.cobranca.util.CobRegBaseDTO;
import br.com.abril.nds.util.export.cobranca.util.CobRegfield;
import org.apache.commons.lang.StringUtils;

public class CobRegEnvTipoRegistroCaixa01 extends CobRegBaseDTO {

	@CobRegfield(tamanho=1, tipo="char", ordem=1)
	private String tipoRegistro = "1";

	@CobRegfield(tamanho = 2, tipo="char", ordem=2)
	private String codigoInscricaoSacado;

	@CobRegfield(tamanho = 14, tipo="char", ordem=3)
	private String numeroCNPJCPF;

	@CobRegfield(tamanho = 4, tipo = "char", ordem = 4)
	private String agenciaCedente;

	//Codigo Beneficiario tam = 6
	@CobRegfield(tamanho = 6, tipo = "char", ordem = 5)
	private String codBeneficiario;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 6)
	private String idEmissao;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 7)
	private String idPostagem;

	@CobRegfield(tamanho = 2, tipo="char", ordem=8)
	private String taxaPermanencia;

	@CobRegfield(tamanho = 25, tipo="char", ordem=9)
	private String empresaBeneficiario;

	@CobRegfield(tamanho = 17, tipo="char", ordem=10)
	private String nossoNumero;

	@CobRegfield(tamanho = 2, tipo = "char", ordem = 11)
	private String filler;

	@CobRegfield(tamanho = 1, tipo = "char", ordem = 12)
	private String usoLivre;

	@CobRegfield(tamanho = 30, tipo="char", ordem=13)
	private String mensagem;

	@CobRegfield(tamanho = 2, tipo="char", ordem=14)
	private String carteira;

	@CobRegfield(tamanho = 2, tipo="char", ordem=15)
	private String codOcorrencia;

	@CobRegfield(tamanho = 10, tipo="char", ordem=16)
	private String numero;

	@CobRegfield(tamanho = 6, tipo="char", ordem=17)
	private String dataVencimentoTitulo;


	@CobRegfield(tamanho= 13, tipo="numeric", ordem=18)
	private String valorTitulo;

	@CobRegfield(tamanho= 3, tipo="char", ordem=19)
	private String codigoBanco;

	@CobRegfield(tamanho = 5, tipo="char", ordem=20)
	private String agencia;

	@CobRegfield(tamanho = 2, tipo="char", ordem=21)
	private String especieTitulo;

	@CobRegfield(tamanho = 1, tipo="char", ordem=22)
	private String aceite;

	@CobRegfield(tamanho = 6, tipo="char", ordem=23)
	private String dataEmissao;

	@CobRegfield(tamanho = 2, tipo="char", ordem=24)
	private String instrucao1;

	@CobRegfield(tamanho = 2, tipo="char", ordem=25)
	private String instrucao2;

	@CobRegfield(tamanho = 13, tipo="char", ordem=26)
	private String jurosMora;

	@CobRegfield(tamanho = 6, tipo="char", ordem=27)
	private String dataDesconto;

	@CobRegfield(tamanho = 13, tipo="char", ordem=28)
	private String valorDesconto;

	@CobRegfield(tamanho = 13, tipo="char", ordem=29)
	private String valorIOF;

	@CobRegfield(tamanho = 13, tipo="char", ordem=30)
	private String valorAbatimento;

    @CobRegfield(tamanho = 2, tipo="char", ordem=31)
    private String identificacaoTipoIncricaoPagador;

	@CobRegfield(tamanho = 14, tipo="char", ordem=32)
	private String numeroDocumento;

	@CobRegfield(tamanho = 40, tipo="char", ordem=33)
	private String sacadoAvalista;

	//Endereco 40 || Bairro 12 || CEP 8 || Cidade 15 || UF 2
	@CobRegfield(tamanho = 77, tipo="char", ordem=34)
	private String enderecoSacado;

	@CobRegfield(tamanho = 6, tipo="char", ordem=35)
	private String dataMulta;

	@CobRegfield(tamanho = 10, tipo="char", ordem=36)
	private String valorMulta;

	@CobRegfield(tamanho = 22, tipo="char", ordem=37)
	private String nomeSacado;

	@CobRegfield(tamanho = 2, tipo="char", ordem=38)
	private String instrucao3;

	@CobRegfield(tamanho = 2, tipo="char", ordem=39)
	private String prazoProtesto;

	@CobRegfield(tamanho = 1, tipo="char", ordem=40)
	private String codigoMoeda;

	@CobRegfield(tamanho = 6, tipo="char", ordem=41)
	private String numeroSequencial;

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
		this.filler = StringUtils.leftPad(filler, 2, ' ');
	}

	public String getAgenciaCedente() {
		return agenciaCedente;
	}

	public void setAgenciaCedente(String agenciaCedente) {
		this.agenciaCedente = StringUtils.leftPad(agenciaCedente, 4, '0');
	}

	public String getUsoLivre() {
		return usoLivre;
	}

	public void setUsoLivre(String usoLivre) {
		this.usoLivre = StringUtils.leftPad(usoLivre, 1, '1');
	}

	public String getTaxaPermanencia() {
		return taxaPermanencia;
	}

	public void setTaxaPermanencia(String taxaPermanencia) {
		this.taxaPermanencia = StringUtils.leftPad(taxaPermanencia,2,'0');
	}

	public String getEmpresaBeneficiario() {
		return empresaBeneficiario;
	}

	public void setEmpresaBeneficiario(String empresaBeneficiario) {
		this.empresaBeneficiario = StringUtils.rightPad(empresaBeneficiario,25, "0");
	}

	public String getNossoNumero() {
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = StringUtils.leftPad(nossoNumero,17,"0");
	}

	public String getCarteira() {
		return carteira;
	}

	public void setCarteira(String carteira) {
		this.carteira = StringUtils.leftPad(carteira,2,"01");
	}


	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = StringUtils.rightPad(numero.substring(7,17),10,"0");
	}

	public String getValorTitulo() {
		return valorTitulo;
	}

	public void setValorTitulo(String valorTitulo) {
		this.valorTitulo = StringUtils.leftPad(valorTitulo, 13,"0");
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = StringUtils.leftPad(codigoBanco,3);
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = StringUtils.leftPad(agencia,5,"0");
	}

	public String getEspecieTitulo() {
		return especieTitulo;
	}

	public void setEspecieTitulo(String especieTitulo) {
		this.especieTitulo = StringUtils.leftPad(especieTitulo,2,"02");
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
		this.dataEmissao = StringUtils.leftPad(dataEmissao,6);
	}
	
	public String getInstrucao1() {
		return instrucao1;
	}

	public void setInstrucao1(String instrucao1) {
		this.instrucao1 = StringUtils.leftPad(instrucao1, 2);
	}

	public String getInstrucao2() {
		return instrucao2;
	}

	public void setInstrucao2(String instrucao2) {
		this.instrucao2 = StringUtils.leftPad(instrucao2, 2, '0');
	}

	public String getInstrucao3() {
		return instrucao3;
	}

	public void setInstrucao3(String instrucao3) {
		this.instrucao3 = StringUtils.leftPad(instrucao3, 2, '0');
	}

	public String getJurosMora() {
		return jurosMora;
	}

	public void setJurosMora(String jurosMora) {
		this.jurosMora = StringUtils.leftPad(jurosMora, 13, '0');
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

	public String getCodigoInscricaoSacado() {
		return codigoInscricaoSacado;
	}

	public void setCodigoInscricaoSacado(String codigoInscricaoSacado) {
		this.codigoInscricaoSacado = StringUtils.leftPad(codigoInscricaoSacado, 2, "0");
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
		this.nomeSacado = StringUtils.leftPad(nomeSacado, 22, " ");
	}

	public String getEnderecoSacado() {
		return enderecoSacado;
	}

	public void setEnderecoSacado(String enderecoSacado) {
		this.enderecoSacado = StringUtils.rightPad(enderecoSacado, 77, " ");
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getPrazoProtesto() {
		return prazoProtesto;
	}

	public void setPrazoProtesto(String prazoProtesto) {
		this.prazoProtesto = StringUtils.leftPad(prazoProtesto,2, " ");
	}

	public String getCodigoMoeda() {
		return codigoMoeda;
	}

	public void setCodigoMoeda(String codigoMoeda) {
		this.codigoMoeda = StringUtils.leftPad(codigoMoeda,1,'1');
	}

	public String getCodBeneficiario() {
		return codBeneficiario;
	}

	public void setCodBeneficiario(String codBeneficiario) {
		this.codBeneficiario = StringUtils.leftPad(codBeneficiario, 6);
	}

	public String getIdEmissao() {
		return idEmissao;
	}

	public void setIdEmissao(String idEmissao) {
		this.idEmissao = StringUtils.leftPad(idEmissao,1);
	}

	public String getIdPostagem() {
		return idPostagem;
	}

	public void setIdPostagem(String idPostagem) {
		this.idPostagem = idPostagem;
	}

	public String getCodOcorrencia() {
		return codOcorrencia;
	}

	public void setCodOcorrencia(String codOcorrencia) {
		this.codOcorrencia = StringUtils.leftPad(codOcorrencia, 2,"01");
	}

	public String getDataMulta() {
		return dataMulta;
	}

	public void setDataMulta(String dataMulta) {
		this.dataMulta = StringUtils.leftPad(dataMulta,6," ");
	}

	public String getValorMulta() {
		return valorMulta;
	}

	public void setValorMulta(String valorMulta) {
		this.valorMulta = StringUtils.leftPad(valorMulta,10, " ");
	}

	public String getDataVencimentoTitulo() {
		return dataVencimentoTitulo;
	}

	public void setDataVencimentoTitulo(String dataVencimentoTitulo) {
		this.dataVencimentoTitulo = dataVencimentoTitulo;
	}

    public String getIdentificacaoTipoIncricaoPagador() {
        return identificacaoTipoIncricaoPagador;
    }

    public void setIdentificacaoTipoIncricaoPagador(String identificacaoTipoIncricaoPagador) {
        this.identificacaoTipoIncricaoPagador = StringUtils.leftPad(identificacaoTipoIncricaoPagador,2,"0");
    }

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = StringUtils.leftPad(numeroDocumento, 14," ");
	}

	public String getSacadoAvalista() {
		return sacadoAvalista;
	}

	public void setSacadoAvalista(String sacadoAvalista) {
		this.sacadoAvalista = StringUtils.rightPad(sacadoAvalista,40, " ");
	}

	public String getNumeroSequencial() {
		return numeroSequencial;
	}

	public void setNumeroSequencial(String numeroSequencial) {
		this.numeroSequencial = StringUtils.leftPad(numeroSequencial,6, "0");
	}
}