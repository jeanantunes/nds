package br.com.abril.nds.dto;

import java.math.BigDecimal;
import java.util.Date;

public class BoletoDTO {

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
	
	private String  contaBanco;
	private Integer contaNumero;
	private Integer contaCarteira;
	private Integer contaAgencia;
	
	private String tituloNumeroDoDocumento;
	private String tituloNossoNumero;
	private String tituloDigitoDoNossoNumero;
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
    
	public String getCedenteNome() {
		return cedenteNome;
	}
	
	public void setCedenteNome(String cedenteNome) {
		this.cedenteNome = cedenteNome;
	}
	
	public String getCedenteDocumento() {
		return cedenteDocumento;
	}
	
	public void setCedenteDocumento(String cedenteDocumento) {
		this.cedenteDocumento = cedenteDocumento;
	}
	
	public String getSacadoNome() {
		return sacadoNome;
	}
	
	public void setSacadoNome(String sacadoNome) {
		this.sacadoNome = sacadoNome;
	}
	
	public String getSacadoDocumento() {
		return sacadoDocumento;
	}
	
	public void setSacadoDocumento(String sacadoDocumento) {
		this.sacadoDocumento = sacadoDocumento;
	}
	
	public String getSacadorAvalistaNome() {
		return sacadorAvalistaNome;
	}
	
	public void setSacadorAvalistaNome(String sacadorAvalistaNome) {
		this.sacadorAvalistaNome = sacadorAvalistaNome;
	}
	
	public String getSacadorAvalistaDocumento() {
		return sacadorAvalistaDocumento;
	}
	
	public void setSacadorAvalistaDocumento(String sacadorAvalistaDocumento) {
		this.sacadorAvalistaDocumento = sacadorAvalistaDocumento;
	}
	
	public String getEnderecoSacadoUf() {
		return enderecoSacadoUf;
	}
	
	public void setEnderecoSacadoUf(String enderecoSacadoUf) {
		this.enderecoSacadoUf = enderecoSacadoUf;
	}
	
	public String getEnderecoSacadoLocalidade() {
		return enderecoSacadoLocalidade;
	}
	
	public void setEnderecoSacadoLocalidade(String enderecoSacadoLocalidade) {
		this.enderecoSacadoLocalidade = enderecoSacadoLocalidade;
	}
	
	public String getEnderecoSacadoCep() {
		return enderecoSacadoCep;
	}
	
	public void setEnderecoSacadoCep(String enderecoSacadoCep) {
		this.enderecoSacadoCep = enderecoSacadoCep;
	}
	
	public String getEnderecoSacadoBairro() {
		return enderecoSacadoBairro;
	}
	
	public void setEnderecoSacadoBairro(String enderecoSacadoBairro) {
		this.enderecoSacadoBairro = enderecoSacadoBairro;
	}
	
	public String getEnderecoSacadoLogradouro() {
		return enderecoSacadoLogradouro;
	}
	
	public void setEnderecoSacadoLogradouro(String enderecoSacadoLogradouro) {
		this.enderecoSacadoLogradouro = enderecoSacadoLogradouro;
	}
	
	public String getEnderecoSacadoNumero() {
		return enderecoSacadoNumero;
	}
	
	public void setEnderecoSacadoNumero(String enderecoSacadoNumero) {
		this.enderecoSacadoNumero = enderecoSacadoNumero;
	}
	
	public String getEnderecoSacadorAvalistaUf() {
		return enderecoSacadorAvalistaUf;
	}
	
	public void setEnderecoSacadorAvalistaUf(String enderecoSacadorAvalistaUf) {
		this.enderecoSacadorAvalistaUf = enderecoSacadorAvalistaUf;
	}
	
	public String getEnderecoSacadorAvalistaLocalidade() {
		return enderecoSacadorAvalistaLocalidade;
	}
	
	public void setEnderecoSacadorAvalistaLocalidade(
			String enderecoSacadorAvalistaLocalidade) {
		this.enderecoSacadorAvalistaLocalidade = enderecoSacadorAvalistaLocalidade;
	}
	
	public String getEnderecoSacadorAvalistaCep() {
		return enderecoSacadorAvalistaCep;
	}
	
	public void setEnderecoSacadorAvalistaCep(String enderecoSacadorAvalistaCep) {
		this.enderecoSacadorAvalistaCep = enderecoSacadorAvalistaCep;
	}
	
	public String getEnderecoSacadorAvalistaBairro() {
		return enderecoSacadorAvalistaBairro;
	}
	
	public void setEnderecoSacadorAvalistaBairro(
			String enderecoSacadorAvalistaBairro) {
		this.enderecoSacadorAvalistaBairro = enderecoSacadorAvalistaBairro;
	}
	
	public String getEnderecoSacadorAvalistaLogradouro() {
		return enderecoSacadorAvalistaLogradouro;
	}
	
	public void setEnderecoSacadorAvalistaLogradouro(
			String enderecoSacadorAvalistaLogradouro) {
		this.enderecoSacadorAvalistaLogradouro = enderecoSacadorAvalistaLogradouro;
	}
	
	public String getEnderecoSacadorAvalistaNumero() {
		return enderecoSacadorAvalistaNumero;
	}
	
	public void setEnderecoSacadorAvalistaNumero(
			String enderecoSacadorAvalistaNumero) {
		this.enderecoSacadorAvalistaNumero = enderecoSacadorAvalistaNumero;
	}
	
	public String getContaBanco() {
		return contaBanco;
	}
	
	public void setContaBanco(String contaBanco) {
		this.contaBanco = contaBanco;
	}
	
	public Integer getContaNumero() {
		return contaNumero;
	}
	
	public void setContaNumero(Integer contaNumero) {
		this.contaNumero = contaNumero;
	}
	
	public Integer getContaCarteira() {
		return contaCarteira;
	}
	
	public void setContaCarteira(Integer contaCarteira) {
		this.contaCarteira = contaCarteira;
	}
	
	public Integer getContaAgencia() {
		return contaAgencia;
	}
	
	public void setContaAgencia(Integer contaAgencia) {
		this.contaAgencia = contaAgencia;
	}
	
	public String getTituloNumeroDoDocumento() {
		return tituloNumeroDoDocumento;
	}
	
	public void setTituloNumeroDoDocumento(String tituloNumeroDoDocumento) {
		this.tituloNumeroDoDocumento = tituloNumeroDoDocumento;
	}
	
	public String getTituloNossoNumero() {
		return tituloNossoNumero;
	}
	
	public void setTituloNossoNumero(String tituloNossoNumero) {
		this.tituloNossoNumero = tituloNossoNumero;
	}
	
	public String getTituloDigitoDoNossoNumero() {
		return tituloDigitoDoNossoNumero;
	}
	
	public void setTituloDigitoDoNossoNumero(String tituloDigitoDoNossoNumero) {
		this.tituloDigitoDoNossoNumero = tituloDigitoDoNossoNumero;
	}
	
	public BigDecimal getTituloValor() {
		return tituloValor;
	}
	
	public void setTituloValor(BigDecimal tituloValor) {
		this.tituloValor = tituloValor;
	}
	
	public Date getTituloDataDoDocumento() {
		return tituloDataDoDocumento;
	}
	
	public void setTituloDataDoDocumento(Date tituloDataDoDocumento) {
		this.tituloDataDoDocumento = tituloDataDoDocumento;
	}
	
	public Date getTituloDataDoVencimento() {
		return tituloDataDoVencimento;
	}
	
	public void setTituloDataDoVencimento(Date tituloDataDoVencimento) {
		this.tituloDataDoVencimento = tituloDataDoVencimento;
	}
	
	public String getTituloTipoDeDocumento() {
		return tituloTipoDeDocumento;
	}
	
	public void setTituloTipoDeDocumento(String tituloTipoDeDocumento) {
		this.tituloTipoDeDocumento = tituloTipoDeDocumento;
	}
	
	public String getTituloAceite() {
		return tituloAceite;
	}
	
	public void setTituloAceite(String tituloAceite) {
		this.tituloAceite = tituloAceite;
	}
	
	public BigDecimal getTituloDesconto() {
		return tituloDesconto;
	}
	
	public void setTituloDesconto(BigDecimal tituloDesconto) {
		this.tituloDesconto = tituloDesconto;
	}
	
	public BigDecimal getTituloDeducao() {
		return tituloDeducao;
	}
	
	public void setTituloDeducao(BigDecimal tituloDeducao) {
		this.tituloDeducao = tituloDeducao;
	}
	
	public BigDecimal getTituloMora() {
		return tituloMora;
	}
	
	public void setTituloMora(BigDecimal tituloMora) {
		this.tituloMora = tituloMora;
	}
	
	public BigDecimal getTituloAcrecimo() {
		return tituloAcrecimo;
	}
	
	public void setTituloAcrecimo(BigDecimal tituloAcrecimo) {
		this.tituloAcrecimo = tituloAcrecimo;
	}
	
	public BigDecimal getTituloValorCobrado() {
		return tituloValorCobrado;
	}
	
	public void setTituloValorCobrado(BigDecimal tituloValorCobrado) {
		this.tituloValorCobrado = tituloValorCobrado;
	}
	
	public String getBoletoLocalPagamento() {
		return boletoLocalPagamento;
	}
	
	public void setBoletoLocalPagamento(String boletoLocalPagamento) {
		this.boletoLocalPagamento = boletoLocalPagamento;
	}
	
	public String getBoletoInstrucaoAoSacado() {
		return boletoInstrucaoAoSacado;
	}
	
	public void setBoletoInstrucaoAoSacado(String boletoInstrucaoAoSacado) {
		this.boletoInstrucaoAoSacado = boletoInstrucaoAoSacado;
	}
	
	public String getBoletoInstrucao1() {
		return boletoInstrucao1;
	}
	
	public void setBoletoInstrucao1(String boletoInstrucao1) {
		this.boletoInstrucao1 = boletoInstrucao1;
	}
	
	public String getBoletoInstrucao2() {
		return boletoInstrucao2;
	}
	
	public void setBoletoInstrucao2(String boletoInstrucao2) {
		this.boletoInstrucao2 = boletoInstrucao2;
	}
	
	public String getBoletoInstrucao3() {
		return boletoInstrucao3;
	}
	
	public void setBoletoInstrucao3(String boletoInstrucao3) {
		this.boletoInstrucao3 = boletoInstrucao3;
	}
	
	public String getBoletoInstrucao4() {
		return boletoInstrucao4;
	}
	
	public void setBoletoInstrucao4(String boletoInstrucao4) {
		this.boletoInstrucao4 = boletoInstrucao4;
	}
	
	public String getBoletoInstrucao5() {
		return boletoInstrucao5;
	}
	
	public void setBoletoInstrucao5(String boletoInstrucao5) {
		this.boletoInstrucao5 = boletoInstrucao5;
	}
	
	public String getBoletoInstrucao6() {
		return boletoInstrucao6;
	}
	
	public void setBoletoInstrucao6(String boletoInstrucao6) {
		this.boletoInstrucao6 = boletoInstrucao6;
	}
	
	public String getBoletoInstrucao7() {
		return boletoInstrucao7;
	}
	
	public void setBoletoInstrucao7(String boletoInstrucao7) {
		this.boletoInstrucao7 = boletoInstrucao7;
	}
	
	public String getBoletoInstrucao8() {
		return boletoInstrucao8;
	}
	
	public void setBoletoInstrucao8(String boletoInstrucao8) {
		this.boletoInstrucao8 = boletoInstrucao8;
	}
    
	
}
