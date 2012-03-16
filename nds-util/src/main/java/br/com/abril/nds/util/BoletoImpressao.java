package br.com.abril.nds.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import org.jrimum.bopepo.BancosSuportados;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.jrimum.domkee.comum.pessoa.endereco.CEP;
import org.jrimum.domkee.comum.pessoa.endereco.Endereco;
import org.jrimum.domkee.comum.pessoa.endereco.UnidadeFederativa;
import org.jrimum.domkee.financeiro.banco.febraban.Agencia;
import org.jrimum.domkee.financeiro.banco.febraban.Carteira;
import org.jrimum.domkee.financeiro.banco.febraban.Cedente;
import org.jrimum.domkee.financeiro.banco.febraban.ContaBancaria;
import org.jrimum.domkee.financeiro.banco.febraban.NumeroDaConta;
import org.jrimum.domkee.financeiro.banco.febraban.Sacado;
import org.jrimum.domkee.financeiro.banco.febraban.SacadorAvalista;
import org.jrimum.domkee.financeiro.banco.febraban.TipoDeTitulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo;
import org.jrimum.domkee.financeiro.banco.febraban.Titulo.Aceite;

public class BoletoImpressao {
	
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
	
	private String contaBanco;
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


	/**
	 * Monta o boleto para o formato "Bobepo"
	 * @param b : Dados do boleto
	 * @return
	 */
	private Boleto montar(){
		
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
        
        //SACADO AVALISTA
        SacadorAvalista sacadorAvalista = new SacadorAvalista(getSacadorAvalistaNome(), getSacadorAvalistaDocumento());

        //ENDERECO DO SACADO AVALISTA
        Endereco enderecoSacAval = new Endereco();
        enderecoSacAval.setUF(UnidadeFederativa.valueOf(getEnderecoSacadorAvalistaUf()));
        enderecoSacAval.setLocalidade(getEnderecoSacadorAvalistaLocalidade());
        enderecoSacAval.setCep(new CEP(getEnderecoSacadorAvalistaCep()));
        enderecoSacAval.setBairro(getEnderecoSacadorAvalistaBairro());
        enderecoSacAval.setLogradouro(getEnderecoSacadorAvalistaLogradouro());
        enderecoSacAval.setNumero(getEnderecoSacadorAvalistaNumero());
        sacadorAvalista.addEndereco(enderecoSacAval);

        //CONTA BANCARIA
        ContaBancaria contaBancaria = new ContaBancaria(BancosSuportados.valueOf(getContaBanco()).create());
        contaBancaria.setNumeroDaConta(new NumeroDaConta(getContaNumero(), "0"));
        contaBancaria.setCarteira(new Carteira(getContaCarteira()));
        contaBancaria.setAgencia(new Agencia(getContaAgencia(), "1"));
        
        //TITULO
        Titulo titulo = new Titulo(contaBancaria, sacado, cedente /*, sacadorAvalista*/);
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
	  * Gera o arquivo PDF.
	  * 
	  * @param arquivoBoleto
	  */
	 public File gerar(String path) {
		 
	    //MONTA BOLETO COM PARAMETROS PASSADOS
	    Boleto boleto = this.montar();
	    
        //GERA BOLETO
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    
	    //GERA O BOLETO PDF NO PATH INFORMADO
	    File arquivoPdf = boletoViewer.getPdfAsFile(path);
	    
	    return arquivoPdf;
	 }
	 
	 
	 /**
	  * Envia arquivo gerado por email.
	  * 
	  * @param arquivoBoleto
	  */
	 @SuppressWarnings("unused")
	 public void email(String to, String from, String mensagem,         String pathAnexo/*!!!!*/   ) {
	     File anexo = this.gerar(pathAnexo);
		
		
		 //AQUI ADICIONAR ENVIO POR EMAIL
		
		
	  }
	 
	 
	 /**
	  * Exibe o arquivo na tela.
	  * 
	  * @param arquivoBoleto
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
	  * Retorna um array de bytes para exibir o arquivo no browser.
	 * @throws IOException 
	  * 
	  */
	 public byte[] obterArrayByte()  {
		//MONTA BOLETO COM PARAMETROS PASSADOS
	    Boleto boleto = this.montar();
	    
        //GERA BOLETO
	    BoletoViewer boletoViewer = new BoletoViewer(boleto);
	    
	    //GERA O BOLETO PDF NO FORMATO ARRAY DE BYTES
	    byte[] byteArrayPdf = boletoViewer.getPdfAsByteArray();
	    
	    return byteArrayPdf;
	 }
	 
	 
	 
	 
	 

	 
	 
	 
	 
	 
	 
	 /*
	 //TESTE
	 public static void main(String[] args) throws IOException{
	        BoletoImpressao boleto = new BoletoImpressao();
	        
	        
	        boleto.setCedenteNome("PROJETO JRimum");
			boleto.setCedenteDocumento("00.000.208/0001-00");

			boleto.setSacadoNome("PROJETO JRimum");
			boleto.setSacadoDocumento("00.000.208/0001-00");

	        boleto.setEnderecoSacadoUf("RN");//!!
	        boleto.setEnderecoSacadoLocalidade("Natal");
	        boleto.setEnderecoSacadoCep("59064-120");
	        boleto.setEnderecoSacadoBairro("Grande Centro");
	        boleto.setEnderecoSacadoLogradouro("Rua poeta dos programas");
	        boleto.setEnderecoSacadoNumero("1");
	 
	        boleto.setSacadorAvalistaNome("PROJETO JRimum");
			boleto.setSacadorAvalistaDocumento("00.000.208/0001-00");
			
			boleto.setEnderecoSacadorAvalistaUf("RN");//!!
	        boleto.setEnderecoSacadorAvalistaLocalidade("Natal");
	        boleto.setEnderecoSacadorAvalistaCep("59064-120");
	        boleto.setEnderecoSacadorAvalistaBairro("Grande Centro");
	        boleto.setEnderecoSacadorAvalistaLogradouro("Rua poeta dos programas");
	        boleto.setEnderecoSacadorAvalistaNumero("1");

	        boleto.setContaBanco("BANCO_BRADESCO");
	        boleto.setContaNumero(123456);
	        boleto.setContaCarteira(30);
	        boleto.setContaAgencia(1234);
	        
	        boleto.setTituloNumeroDoDocumento("123456");
	        boleto.setTituloNossoNumero("99345678912");
	        boleto.setTituloDigitoDoNossoNumero("5");
	        boleto.setTituloValor(new BigDecimal(0.23));
	        boleto.setTituloDataDoDocumento(new Date());
	        boleto.setTituloDataDoVencimento(new Date());
	        boleto.setTituloTipoDeDocumento("DM_DUPLICATA_MERCANTIL");//!!!
	        boleto.setTituloAceite("A");//!!!
	        boleto.setTituloDesconto(new BigDecimal(0.05));
	        boleto.setTituloDeducao(BigDecimal.ZERO);
	        boleto.setTituloMora(BigDecimal.ZERO);
	        boleto.setTituloAcrecimo(BigDecimal.ZERO);
	        boleto.setTituloValorCobrado(BigDecimal.ZERO);

	        boleto.setBoletoLocalPagamento("Pagável preferencialmente na Rede X ou em " +
	                        "qualquer Banco até o Vencimento.");
	        boleto.setBoletoInstrucaoAoSacado("Senhor sacado, sabemos sim que o valor " +
	                        "cobrado não é o esperado, aproveite o DESCONTÃO!");
	        boleto.setBoletoInstrucao1("PARA PAGAMENTO 1 até Hoje não cobrar nada!");
	        boleto.setBoletoInstrucao2("PARA PAGAMENTO 2 até Amanhã Não cobre!");
	        boleto.setBoletoInstrucao3("PARA PAGAMENTO 3 até Depois de amanhã, OK, não cobre.");
	        boleto.setBoletoInstrucao4("PARA PAGAMENTO 4 até 04/xx/xxxx de 4 dias atrás COBRAR O VALOR DE: R$ 01,00");
	        boleto.setBoletoInstrucao5("PARA PAGAMENTO 5 até 05/xx/xxxx COBRAR O VALOR DE: R$ 02,00");
	        boleto.setBoletoInstrucao6("PARA PAGAMENTO 6 até 06/xx/xxxx COBRAR O VALOR DE: R$ 03,00");
	        boleto.setBoletoInstrucao7("PARA PAGAMENTO 7 até xx/xx/xxxx COBRAR O VALOR QUE VOCÊ QUISER!");
	        boleto.setBoletoInstrucao8("APÓS o Vencimento, Pagável Somente na Rede X.");
	        
	        
	        //boletoImpressao.gerar("D:/Boleto.pdf");
	        //boleto.visualizar("D:/Boleto.pdf");
	        byte[] b = boleto.obterArrayByte();
	        
		}
	    */
	 
	 
	 
}
