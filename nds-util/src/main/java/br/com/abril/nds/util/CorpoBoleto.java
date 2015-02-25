package br.com.abril.nds.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CorpoBoleto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String codigoCedente;
	private String digitoCodigoCedente;
	private String cedenteNome;
	private String cedenteDocumento;
	
	private String sacadoNome;
	private String sacadoDocumento;
	
	private String sacadorAvalistaNome;
	private String sacadorAvalistaDocumento;
	
	private String enderecoSacadoUf;
	private String enderecoSacadoLocalidade;
	private String enderecoSacadoCep;
	private String enderecoSacadoBairro;
	private String enderecoSacadoLogradouro;
	private String enderecoSacadoNumero;
	
	private String enderecoSacadorAvalistaUf;
	private String enderecoSacadorAvalistaLocalidade;
	private String enderecoSacadorAvalistaCep;
	private String enderecoSacadorAvalistaBairro;
	private String enderecoSacadorAvalistaLogradouro;
	private String enderecoSacadorAvalistaNumero;
	

	private String contaNumeroBanco;
	private String contaTipoDeCobranca;
	private Integer contaNumero;
	private Integer contaCarteira;
	private Integer contaAgencia;
	private String digitoAgencia;
	
	private String tituloNumeroDoDocumento;
	private String tituloNossoNumero;
	private String tituloDigitoDoNossoNumero;
	private String tituloTipoIdentificadorCNR;
	private BigDecimal tituloValor;
	private Date tituloDataDoDocumento;
	private Date tituloDataDoVencimento;
	
	private String tituloTipoDeDocumento;
	private String tituloAceite;
    
    private BigDecimal tituloDesconto;
    private BigDecimal tituloDeducao;
    private BigDecimal tituloMora;
    private BigDecimal tituloAcrecimo;
    private BigDecimal tituloValorCobrado;

    private String boletoLocalPagamento;
    private String boletoInstrucaoAoSacado;
    private String boletoInstrucao1;
    private String boletoInstrucao2;
    private String boletoInstrucao3;
    private String boletoInstrucao4;
    private String boletoInstrucao5;
    private String boletoInstrucao6;
    private String boletoInstrucao7;
    private String boletoInstrucao8;
    
    private Boolean boletoSemValor;
    
	/**
	 * @return the codigoCedente
	 */
	public String getCodigoCedente() {
		return codigoCedente;
	}
	/**
	 * @param codigoCedente the codigoCedente to set
	 */
	public void setCodigoCedente(String codigoCedente) {
		this.codigoCedente = codigoCedente;
	}
	
	/**
	 * @return
	 */
	public String getDigitoCodigoCedente() {
		return digitoCodigoCedente;
	}
	/**
	 * @param digitoCodigoCedente
	 */
	public void setDigitoCodigoCedente(String digitoCodigoCedente) {
		this.digitoCodigoCedente = digitoCodigoCedente;
	}
	/**
	 * @return the cedenteNome
	 */
	public String getCedenteNome() {
		return cedenteNome;
	}
	/**
	 * @param cedenteNome the cedenteNome to set
	 */
	public void setCedenteNome(String cedenteNome) {
		this.cedenteNome = cedenteNome;
	}
	/**
	 * @return the cedenteDocumento
	 */
	public String getCedenteDocumento() {
		return cedenteDocumento;
	}
	/**
	 * @param cedenteDocumento the cedenteDocumento to set
	 */
	public void setCedenteDocumento(String cedenteDocumento) {
		this.cedenteDocumento = cedenteDocumento;
	}
	/**
	 * @return the sacadoNome
	 */
	public String getSacadoNome() {
		return sacadoNome;
	}
	/**
	 * @param sacadoNome the sacadoNome to set
	 */
	public void setSacadoNome(String sacadoNome) {
		this.sacadoNome = sacadoNome;
	}
	/**
	 * @return the sacadoDocumento
	 */
	public String getSacadoDocumento() {
		return sacadoDocumento;
	}
	/**
	 * @param sacadoDocumento the sacadoDocumento to set
	 */
	public void setSacadoDocumento(String sacadoDocumento) {
		this.sacadoDocumento = sacadoDocumento;
	}
	/**
	 * @return the sacadorAvalistaNome
	 */
	public String getSacadorAvalistaNome() {
		return sacadorAvalistaNome;
	}
	/**
	 * @param sacadorAvalistaNome the sacadorAvalistaNome to set
	 */
	public void setSacadorAvalistaNome(String sacadorAvalistaNome) {
		this.sacadorAvalistaNome = sacadorAvalistaNome;
	}
	/**
	 * @return the sacadorAvalistaDocumento
	 */
	public String getSacadorAvalistaDocumento() {
		return sacadorAvalistaDocumento;
	}
	/**
	 * @param sacadorAvalistaDocumento the sacadorAvalistaDocumento to set
	 */
	public void setSacadorAvalistaDocumento(String sacadorAvalistaDocumento) {
		this.sacadorAvalistaDocumento = sacadorAvalistaDocumento;
	}
	/**
	 * @return the enderecoSacadoUf
	 */
	public String getEnderecoSacadoUf() {
		return enderecoSacadoUf;
	}
	/**
	 * @param enderecoSacadoUf the enderecoSacadoUf to set
	 */
	public void setEnderecoSacadoUf(String enderecoSacadoUf) {
		this.enderecoSacadoUf = enderecoSacadoUf;
	}
	/**
	 * @return the enderecoSacadoLocalidade
	 */
	public String getEnderecoSacadoLocalidade() {
		return enderecoSacadoLocalidade;
	}
	/**
	 * @param enderecoSacadoLocalidade the enderecoSacadoLocalidade to set
	 */
	public void setEnderecoSacadoLocalidade(String enderecoSacadoLocalidade) {
		this.enderecoSacadoLocalidade = enderecoSacadoLocalidade;
	}
	/**
	 * @return the enderecoSacadoCep
	 */
	public String getEnderecoSacadoCep() {
		return enderecoSacadoCep;
	}
	/**
	 * @param enderecoSacadoCep the enderecoSacadoCep to set
	 */
	public void setEnderecoSacadoCep(String enderecoSacadoCep) {
		this.enderecoSacadoCep = enderecoSacadoCep;
	}
	/**
	 * @return the enderecoSacadoBairro
	 */
	public String getEnderecoSacadoBairro() {
		return enderecoSacadoBairro;
	}
	/**
	 * @param enderecoSacadoBairro the enderecoSacadoBairro to set
	 */
	public void setEnderecoSacadoBairro(String enderecoSacadoBairro) {
		this.enderecoSacadoBairro = enderecoSacadoBairro;
	}
	/**
	 * @return the enderecoSacadoLogradouro
	 */
	public String getEnderecoSacadoLogradouro() {
		return enderecoSacadoLogradouro;
	}
	/**
	 * @param enderecoSacadoLogradouro the enderecoSacadoLogradouro to set
	 */
	public void setEnderecoSacadoLogradouro(String enderecoSacadoLogradouro) {
		this.enderecoSacadoLogradouro = enderecoSacadoLogradouro;
	}
	/**
	 * @return the enderecoSacadoNumero
	 */
	public String getEnderecoSacadoNumero() {
		return enderecoSacadoNumero;
	}
	/**
	 * @param enderecoSacadoNumero the enderecoSacadoNumero to set
	 */
	public void setEnderecoSacadoNumero(String enderecoSacadoNumero) {
		this.enderecoSacadoNumero = enderecoSacadoNumero;
	}
	/**
	 * @return the enderecoSacadorAvalistaUf
	 */
	public String getEnderecoSacadorAvalistaUf() {
		return enderecoSacadorAvalistaUf;
	}
	/**
	 * @param enderecoSacadorAvalistaUf the enderecoSacadorAvalistaUf to set
	 */
	public void setEnderecoSacadorAvalistaUf(String enderecoSacadorAvalistaUf) {
		this.enderecoSacadorAvalistaUf = enderecoSacadorAvalistaUf;
	}
	/**
	 * @return the enderecoSacadorAvalistaLocalidade
	 */
	public String getEnderecoSacadorAvalistaLocalidade() {
		return enderecoSacadorAvalistaLocalidade;
	}
	/**
	 * @param enderecoSacadorAvalistaLocalidade the enderecoSacadorAvalistaLocalidade to set
	 */
	public void setEnderecoSacadorAvalistaLocalidade(
			String enderecoSacadorAvalistaLocalidade) {
		this.enderecoSacadorAvalistaLocalidade = enderecoSacadorAvalistaLocalidade;
	}
	/**
	 * @return the enderecoSacadorAvalistaCep
	 */
	public String getEnderecoSacadorAvalistaCep() {
		return enderecoSacadorAvalistaCep;
	}
	/**
	 * @param enderecoSacadorAvalistaCep the enderecoSacadorAvalistaCep to set
	 */
	public void setEnderecoSacadorAvalistaCep(String enderecoSacadorAvalistaCep) {
		this.enderecoSacadorAvalistaCep = enderecoSacadorAvalistaCep;
	}
	/**
	 * @return the enderecoSacadorAvalistaBairro
	 */
	public String getEnderecoSacadorAvalistaBairro() {
		return enderecoSacadorAvalistaBairro;
	}
	/**
	 * @param enderecoSacadorAvalistaBairro the enderecoSacadorAvalistaBairro to set
	 */
	public void setEnderecoSacadorAvalistaBairro(
			String enderecoSacadorAvalistaBairro) {
		this.enderecoSacadorAvalistaBairro = enderecoSacadorAvalistaBairro;
	}
	/**
	 * @return the enderecoSacadorAvalistaLogradouro
	 */
	public String getEnderecoSacadorAvalistaLogradouro() {
		return enderecoSacadorAvalistaLogradouro;
	}
	/**
	 * @param enderecoSacadorAvalistaLogradouro the enderecoSacadorAvalistaLogradouro to set
	 */
	public void setEnderecoSacadorAvalistaLogradouro(
			String enderecoSacadorAvalistaLogradouro) {
		this.enderecoSacadorAvalistaLogradouro = enderecoSacadorAvalistaLogradouro;
	}
	/**
	 * @return the enderecoSacadorAvalistaNumero
	 */
	public String getEnderecoSacadorAvalistaNumero() {
		return enderecoSacadorAvalistaNumero;
	}
	/**
	 * @param enderecoSacadorAvalistaNumero the enderecoSacadorAvalistaNumero to set
	 */
	public void setEnderecoSacadorAvalistaNumero(
			String enderecoSacadorAvalistaNumero) {
		this.enderecoSacadorAvalistaNumero = enderecoSacadorAvalistaNumero;
	}
	/**
	 * @return the contaNumeroBanco
	 */
	public String getContaNumeroBanco() {
		return contaNumeroBanco;
	}
	/**
	 * @param contaNumeroBanco the contaNumeroBanco to set
	 */
	public void setContaNumeroBanco(String contaNumeroBanco) {
		this.contaNumeroBanco = contaNumeroBanco;
	}
	/**
	 * @return the contaTipoDeCobranca
	 */
	public String getContaTipoDeCobranca() {
		return contaTipoDeCobranca;
	}
	/**
	 * @param contaTipoDeCobranca the contaTipoDeCobranca to set
	 */
	public void setContaTipoDeCobranca(String contaTipoDeCobranca) {
		this.contaTipoDeCobranca = contaTipoDeCobranca;
	}
	/**
	 * @return the contaNumero
	 */
	public Integer getContaNumero() {
		return contaNumero;
	}
	/**
	 * @param contaNumero the contaNumero to set
	 */
	public void setContaNumero(Integer contaNumero) {
		this.contaNumero = contaNumero;
	}
	/**
	 * @return the contaCarteira
	 */
	public Integer getContaCarteira() {
		return contaCarteira;
	}
	/**
	 * @param contaCarteira the contaCarteira to set
	 */
	public void setContaCarteira(Integer contaCarteira) {
		this.contaCarteira = contaCarteira;
	}
	/**
	 * @return the contaAgencia
	 */
	public Integer getContaAgencia() {
		return contaAgencia;
	}
	/**
	 * @param contaAgencia the contaAgencia to set
	 */
	public void setContaAgencia(Integer contaAgencia) {
		this.contaAgencia = contaAgencia;
	}
	
	/**
	 * @return the digitoAgencia
	 */
	public String getDigitoAgencia() {
		return digitoAgencia;
	}
	/**
	 * @param digitoAgencia the digitoAgencia to set
	 */
	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}
	/**
	 * @return the tituloNumeroDoDocumento
	 */
	public String getTituloNumeroDoDocumento() {
		return tituloNumeroDoDocumento;
	}
	/**
	 * @param tituloNumeroDoDocumento the tituloNumeroDoDocumento to set
	 */
	public void setTituloNumeroDoDocumento(String tituloNumeroDoDocumento) {
		this.tituloNumeroDoDocumento = tituloNumeroDoDocumento;
	}
	/**
	 * @return the tituloNossoNumero
	 */
	public String getTituloNossoNumero() {
		return tituloNossoNumero;
	}
	/**
	 * @param tituloNossoNumero the tituloNossoNumero to set
	 */
	public void setTituloNossoNumero(String tituloNossoNumero) {
		this.tituloNossoNumero = tituloNossoNumero;
	}
	/**
	 * @return the tituloDigitoDoNossoNumero
	 */
	public String getTituloDigitoDoNossoNumero() {
		return tituloDigitoDoNossoNumero;
	}
	/**
	 * @param tituloDigitoDoNossoNumero the tituloDigitoDoNossoNumero to set
	 */
	public void setTituloDigitoDoNossoNumero(String tituloDigitoDoNossoNumero) {
		this.tituloDigitoDoNossoNumero = tituloDigitoDoNossoNumero;
	}
	/**
	 * @return the tituloTipoIdentificadorCNR
	 */
	public String getTituloTipoIdentificadorCNR() {
		return tituloTipoIdentificadorCNR;
	}
	/**
	 * @param tituloTipoIdentificadorCNR the tituloTipoIdentificadorCNR to set
	 */
	public void setTituloTipoIdentificadorCNR(String tituloTipoIdentificadorCNR) {
		this.tituloTipoIdentificadorCNR = tituloTipoIdentificadorCNR;
	}
	/**
	 * @return the tituloValor
	 */
	public BigDecimal getTituloValor() {
		return tituloValor;
	}
	/**
	 * @param tituloValor the tituloValor to set
	 */
	public void setTituloValor(BigDecimal tituloValor) {
		this.tituloValor = tituloValor;
	}
	/**
	 * @return the tituloDataDoDocumento
	 */
	public Date getTituloDataDoDocumento() {
		return tituloDataDoDocumento;
	}
	/**
	 * @param tituloDataDoDocumento the tituloDataDoDocumento to set
	 */
	public void setTituloDataDoDocumento(Date tituloDataDoDocumento) {
		this.tituloDataDoDocumento = tituloDataDoDocumento;
	}
	/**
	 * @return the tituloDataDoVencimento
	 */
	public Date getTituloDataDoVencimento() {
		return tituloDataDoVencimento;
	}
	/**
	 * @param tituloDataDoVencimento the tituloDataDoVencimento to set
	 */
	public void setTituloDataDoVencimento(Date tituloDataDoVencimento) {
		this.tituloDataDoVencimento = tituloDataDoVencimento;
	}
	/**
	 * @return the tituloTipoDeDocumento
	 */
	public String getTituloTipoDeDocumento() {
		return tituloTipoDeDocumento;
	}
	/**
	 * @param tituloTipoDeDocumento the tituloTipoDeDocumento to set
	 */
	public void setTituloTipoDeDocumento(String tituloTipoDeDocumento) {
		this.tituloTipoDeDocumento = tituloTipoDeDocumento;
	}
	/**
	 * @return the tituloAceite
	 */
	public String getTituloAceite() {
		return tituloAceite;
	}
	/**
	 * @param tituloAceite the tituloAceite to set
	 */
	public void setTituloAceite(String tituloAceite) {
		this.tituloAceite = tituloAceite;
	}
	/**
	 * @return the tituloDesconto
	 */
	public BigDecimal getTituloDesconto() {
		return tituloDesconto;
	}
	/**
	 * @param tituloDesconto the tituloDesconto to set
	 */
	public void setTituloDesconto(BigDecimal tituloDesconto) {
		this.tituloDesconto = tituloDesconto;
	}
	/**
	 * @return the tituloDeducao
	 */
	public BigDecimal getTituloDeducao() {
		return tituloDeducao;
	}
	/**
	 * @param tituloDeducao the tituloDeducao to set
	 */
	public void setTituloDeducao(BigDecimal tituloDeducao) {
		this.tituloDeducao = tituloDeducao;
	}
	/**
	 * @return the tituloMora
	 */
	public BigDecimal getTituloMora() {
		return tituloMora;
	}
	/**
	 * @param tituloMora the tituloMora to set
	 */
	public void setTituloMora(BigDecimal tituloMora) {
		this.tituloMora = tituloMora;
	}
	/**
	 * @return the tituloAcrecimo
	 */
	public BigDecimal getTituloAcrecimo() {
		return tituloAcrecimo;
	}
	/**
	 * @param tituloAcrecimo the tituloAcrecimo to set
	 */
	public void setTituloAcrecimo(BigDecimal tituloAcrecimo) {
		this.tituloAcrecimo = tituloAcrecimo;
	}
	/**
	 * @return the tituloValorCobrado
	 */
	public BigDecimal getTituloValorCobrado() {
		return tituloValorCobrado;
	}
	/**
	 * @param tituloValorCobrado the tituloValorCobrado to set
	 */
	public void setTituloValorCobrado(BigDecimal tituloValorCobrado) {
		this.tituloValorCobrado = tituloValorCobrado;
	}
	/**
	 * @return the boletoLocalPagamento
	 */
	public String getBoletoLocalPagamento() {
		return boletoLocalPagamento;
	}
	/**
	 * @param boletoLocalPagamento the boletoLocalPagamento to set
	 */
	public void setBoletoLocalPagamento(String boletoLocalPagamento) {
		this.boletoLocalPagamento = boletoLocalPagamento;
	}
	/**
	 * @return the boletoInstrucaoAoSacado
	 */
	public String getBoletoInstrucaoAoSacado() {
		return boletoInstrucaoAoSacado;
	}
	/**
	 * @param boletoInstrucaoAoSacado the boletoInstrucaoAoSacado to set
	 */
	public void setBoletoInstrucaoAoSacado(String boletoInstrucaoAoSacado) {
		this.boletoInstrucaoAoSacado = boletoInstrucaoAoSacado;
	}
	/**
	 * @return the boletoInstrucao1
	 */
	public String getBoletoInstrucao1() {
		return boletoInstrucao1;
	}
	/**
	 * @param boletoInstrucao1 the boletoInstrucao1 to set
	 */
	public void setBoletoInstrucao1(String boletoInstrucao1) {
		this.boletoInstrucao1 = boletoInstrucao1;
	}
	/**
	 * @return the boletoInstrucao2
	 */
	public String getBoletoInstrucao2() {
		return boletoInstrucao2;
	}
	/**
	 * @param boletoInstrucao2 the boletoInstrucao2 to set
	 */
	public void setBoletoInstrucao2(String boletoInstrucao2) {
		this.boletoInstrucao2 = boletoInstrucao2;
	}
	/**
	 * @return the boletoInstrucao3
	 */
	public String getBoletoInstrucao3() {
		return boletoInstrucao3;
	}
	/**
	 * @param boletoInstrucao3 the boletoInstrucao3 to set
	 */
	public void setBoletoInstrucao3(String boletoInstrucao3) {
		this.boletoInstrucao3 = boletoInstrucao3;
	}
	/**
	 * @return the boletoInstrucao4
	 */
	public String getBoletoInstrucao4() {
		return boletoInstrucao4;
	}
	/**
	 * @param boletoInstrucao4 the boletoInstrucao4 to set
	 */
	public void setBoletoInstrucao4(String boletoInstrucao4) {
		this.boletoInstrucao4 = boletoInstrucao4;
	}
	/**
	 * @return the boletoInstrucao5
	 */
	public String getBoletoInstrucao5() {
		return boletoInstrucao5;
	}
	/**
	 * @param boletoInstrucao5 the boletoInstrucao5 to set
	 */
	public void setBoletoInstrucao5(String boletoInstrucao5) {
		this.boletoInstrucao5 = boletoInstrucao5;
	}
	/**
	 * @return the boletoInstrucao6
	 */
	public String getBoletoInstrucao6() {
		return boletoInstrucao6;
	}
	/**
	 * @param boletoInstrucao6 the boletoInstrucao6 to set
	 */
	public void setBoletoInstrucao6(String boletoInstrucao6) {
		this.boletoInstrucao6 = boletoInstrucao6;
	}
	/**
	 * @return the boletoInstrucao7
	 */
	public String getBoletoInstrucao7() {
		return boletoInstrucao7;
	}
	/**
	 * @param boletoInstrucao7 the boletoInstrucao7 to set
	 */
	public void setBoletoInstrucao7(String boletoInstrucao7) {
		this.boletoInstrucao7 = boletoInstrucao7;
	}
	/**
	 * @return the boletoInstrucao8
	 */
	public String getBoletoInstrucao8() {
		return boletoInstrucao8;
	}
	/**
	 * @param boletoInstrucao8 the boletoInstrucao8 to set
	 */
	public void setBoletoInstrucao8(String boletoInstrucao8) {
		this.boletoInstrucao8 = boletoInstrucao8;
	}
	/**
	 * @return the boletoSemValor
	 */
	public Boolean isBoletoSemValor() {
		return boletoSemValor==null?false:boletoSemValor;
	}
	/**
	 * @param boletoSemValor the boletoSemValor to set
	 */
	public void setBoletoSemValor(Boolean boletoSemValor) {
		this.boletoSemValor = boletoSemValor==null?false:boletoSemValor;
	}
		
}
