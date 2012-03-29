package br.com.abril.nds.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.ParametrosBancariosMap;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeCobranca;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;
import org.jrimum.domkee.financeiro.banco.hsbc.TipoIdentificadorCNR;

public class GeradorBoleto {
	
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


	public String getContaNumeroBanco() {
		return contaNumeroBanco;
	}
	
	public void setContaNumeroBanco(String contaNumeroBanco) {
		this.contaNumeroBanco = contaNumeroBanco;
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
	

	public String getContaTipoDeCobranca() {
		return contaTipoDeCobranca;
	}


	public void setContaTipoDeCobranca(String contaTipoDeCobranca) {
		this.contaTipoDeCobranca = contaTipoDeCobranca;
	}


	public String getTituloTipoIdentificadorCNR() {
		return tituloTipoIdentificadorCNR;
	}


	public void setTituloTipoIdentificadorCNR(String tituloTipoIdentificadorCNR) {
		this.tituloTipoIdentificadorCNR = tituloTipoIdentificadorCNR;
	}


	/**
	 * Monta o boleto para o formato "Bopepo"
	 * @param b : Dados do boleto
	 * @return
	 */
	private Boleto createBoleto(){
		
        //CEDENTE
        Cedente cedente = new Cedente(getCedenteNome(), getCedenteDocumento());
        
        //SACADO
        Sacado sacado = new Sacado(getSacadoNome(), getSacadoDocumento());
        //ENDERECO DO SACADO
        Endereco enderecoSac = new Endereco();
        enderecoSac.setUF(UnidadeFederativa.valueOf(getEnderecoSacadoUf()));
        enderecoSac.setLocalidade(getEnderecoSacadoLocalidade());
        enderecoSac.setCep(new CEP(getEnderecoSacadoCep()));
        enderecoSac.setBairro(getEnderecoSacadoBairro());
        enderecoSac.setLogradouro(getEnderecoSacadoLogradouro());
        enderecoSac.setNumero(getEnderecoSacadoNumero());
        sacado.addEndereco(enderecoSac);
        
        SacadorAvalista sacadorAvalista = null;
        if ( (getSacadorAvalistaNome()!=null) && !("".equals(getSacadorAvalistaNome())) ){
	        //SACADOR AVALISTA
	        sacadorAvalista = new SacadorAvalista(getSacadorAvalistaNome(), getSacadorAvalistaDocumento());
	        //ENDERECO DO SACADO AVALISTA
	        Endereco enderecoSacAval = new Endereco();
	        enderecoSacAval.setUF(UnidadeFederativa.valueOf(getEnderecoSacadorAvalistaUf()));
	        enderecoSacAval.setLocalidade(getEnderecoSacadorAvalistaLocalidade());
	        enderecoSacAval.setCep(new CEP(getEnderecoSacadorAvalistaCep()));
	        enderecoSacAval.setBairro(getEnderecoSacadorAvalistaBairro());
	        enderecoSacAval.setLogradouro(getEnderecoSacadorAvalistaLogradouro());
	        enderecoSacAval.setNumero(getEnderecoSacadorAvalistaNumero());
	        sacadorAvalista.addEndereco(enderecoSacAval);
        }
        
        //CONTA BANCARIA
        ContaBancaria contaBancaria = new ContaBancaria(getBancoByNumero(getContaNumeroBanco()).create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(getContaNumero(), "0"));
        //CARTEIRA DA CONTA BANCARIA  
        Carteira carteira = new Carteira(this.getContaCarteira());
        //TIPO DE COBRANCA DA CARTEIRA DA CONTA BANCARIA  
        carteira.setTipoCobranca(TipoDeCobranca.valueOf(this.getContaTipoDeCobranca()));   
        contaBancaria.setCarteira(carteira);
        contaBancaria.setAgencia(new Agencia(getContaAgencia(), "1"));
        
        //TITULO
        Titulo titulo;
        if (sacadorAvalista!=null){
            titulo = new Titulo(contaBancaria, sacado, cedente, sacadorAvalista);
        }
        else{
        	titulo = new Titulo(contaBancaria, sacado, cedente);
        }
        //PARAMETROS BANCARIOS DO TITULO
        ParametrosBancariosMap parametrosBancarios = new
        ParametrosBancariosMap();
        parametrosBancarios.adicione(TipoIdentificadorCNR.class.getName(),
                                     TipoIdentificadorCNR.valueOf(this.getTituloTipoIdentificadorCNR()));
        titulo.setParametrosBancarios(parametrosBancarios);
        titulo.setNumeroDoDocumento(getTituloNumeroDoDocumento());
        titulo.setNossoNumero(getTituloNossoNumero());
        titulo.setDigitoDoNossoNumero(getTituloDigitoDoNossoNumero());
        titulo.setValor(getTituloValor());
        titulo.setDataDoDocumento(getTituloDataDoDocumento());
        titulo.setDataDoVencimento(getTituloDataDoVencimento());
        titulo.setTipoDeDocumento(TipoDeTitulo.valueOf(getTituloTipoDeDocumento()));
        titulo.setAceite(Aceite.valueOf(getTituloAceite()));
        titulo.setDesconto(getTituloDesconto());
        titulo.setDeducao(getTituloDeducao());
        titulo.setMora(getTituloMora());
        titulo.setAcrecimo(getTituloAcrecimo());
        titulo.setValorCobrado(getTituloValorCobrado());

        //BOLETO
        Boleto boleto = new Boleto(titulo);
        boleto.setLocalPagamento(getBoletoLocalPagamento());
        boleto.setInstrucaoAoSacado(getBoletoInstrucaoAoSacado());
        boleto.setInstrucao1(getBoletoInstrucao1());
        boleto.setInstrucao2(getBoletoInstrucao2());
        boleto.setInstrucao3(getBoletoInstrucao3());
        boleto.setInstrucao4(getBoletoInstrucao4());
        boleto.setInstrucao5(getBoletoInstrucao5());
        boleto.setInstrucao6(getBoletoInstrucao6());
        boleto.setInstrucao7(getBoletoInstrucao7());
        boleto.setInstrucao8(getBoletoInstrucao8());
        
        return boleto;
	}


	 /**
	  * Gera o arquivo PDF local.
	  * @param path
	  */
	 public File gerar(String path) {
	    Boleto boleto = this.createBoleto();
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    File arquivoPdf = boletoViewer.getPdfAsFile(path);
	    return arquivoPdf;
	 }
	 

	 /**
	  * Exibe o arquivo na tela utilizando o Viewer.
	  * @param pathAnexo
	  */
	 public void visualizar(String pathAnexo) {
		 java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
		 File arquivoBoleto = this.gerar(pathAnexo);
         try {
             desktop.open(arquivoBoleto);
         } catch (IOException e) {
             e.printStackTrace();
         }    
	 }
	 
	 
	 /**
	  * Retorna um Array de Bytes do Boleto PDF.
	  * 
	  */
	 public byte[] getBytePdf()  {
	    Boleto boleto = this.createBoleto();
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    byte[] byteArrayPdf = boletoViewer.getPdfAsByteArray();
	    return byteArrayPdf;
	 }
	 
	 
	 /**
	  * Retorna um File do Boleto PDF.
	  * 
	  */
	 public File getFilePdf(){
 		Boleto boleto = this.createBoleto();
 		BoletoViewer viewer = new BoletoViewer(boleto);
 		File file = new File("boleto"+boleto.getTitulo().getNossoNumero()+".pdf");
 		viewer.getPdfAsFile(file);
 		return file;
     }
	 
	 /**
	  * Recupera o banco suportado pelo número
	  * @param número do banco
	  * @return banco suportado que corresponde ao número do banco, ou null caso
	  * o número do banco 
	  */
	 private static BancosSuportados getBancoByNumero(String numero) {
		 for (BancosSuportados banco : BancosSuportados.values()) {
			 if (banco.getCodigoDeCompensacao().equals(numero)) {
				 return banco;
			 }
		 }
		 return null;
	 }

}
