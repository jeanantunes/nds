package br.com.abril.nds.util;


public class BaixaBancConsolidada {
	
	protected int indiceIdentificadorLinhaInicio;
	protected int indiceIdentificadorLinhaFim;
	
	protected int indiceDataBaixaInicio;
	protected int indiceDataBaixaFim;
	
	protected int indiceDestinoInicio;
	protected int indiceDestinoFim;
	
	protected int indiceCodJornaleiroInicio;
	protected int indiceCodJornaleiroFim;
	
	protected int indiceDataVencimentoInicio;
	protected int indiceDataVencimentoFim;
	
	protected int indiceDataPagamentoInicio;
	protected int indiceDataPagamentoFim;
	
	protected int indiceValorDoBoletoInicio;
	protected int indiceValorDoBoletoFim;
	
	protected int indiceValorPagoInicio;
	protected int indiceValorPagoFim;
	
	protected int indiceNossoNumeroConsolidadoInicio;
	protected int indiceNossoNumeroConsolidadoFim;
	
	protected int indiceQtdRegistrosInicio;
	protected int indiceQtdRegistrosFim;
	
	
	public static BaixaBancConsolidada obterBaixaBancariaConsolidada(){
		//retorna template padrão, no momento é o único trabalhado
		return BaixaBancConsolidada.newInstanceBaixaBancConsolidada();
    }
	
	public static BaixaBancConsolidada newInstanceBaixaBancConsolidada(){
		
    	BaixaBancConsolidada bbConsolidada = new BaixaBancConsolidada();
		
		bbConsolidada.indiceIdentificadorLinhaInicio = 0;
		bbConsolidada.indiceIdentificadorLinhaFim = 1;
		                                   
		bbConsolidada.indiceDataBaixaInicio = 1;             
		bbConsolidada.indiceDataBaixaFim = 11;                

		bbConsolidada.indiceDestinoInicio = 11;               
		bbConsolidada.indiceDestinoFim = 13;                  

		bbConsolidada.indiceCodJornaleiroInicio = 1;         
		bbConsolidada.indiceCodJornaleiroFim = 6;            

		bbConsolidada.indiceDataVencimentoInicio = 6;        
		bbConsolidada.indiceDataVencimentoFim = 16;           

		bbConsolidada.indiceDataPagamentoInicio = 16;         
		bbConsolidada.indiceDataPagamentoFim = 26;            

		bbConsolidada.indiceValorDoBoletoInicio = 26;         
		bbConsolidada.indiceValorDoBoletoFim = 36;            

		bbConsolidada.indiceValorPagoInicio = 36;             
    	bbConsolidada.indiceValorPagoFim = 46;
    	
    	bbConsolidada.indiceNossoNumeroConsolidadoInicio = 46;
    	bbConsolidada.indiceNossoNumeroConsolidadoFim = 62;
    	
    	bbConsolidada.indiceQtdRegistrosInicio = 1;
    	bbConsolidada.indiceQtdRegistrosFim = 11;
    	
    	return bbConsolidada;
    	
    }
	
	public String obterIdentificadorLinha(String linha){
		return linha.substring(indiceIdentificadorLinhaInicio, indiceIdentificadorLinhaFim);
	}
	
	public String obterDataBaixa(String linha){
		return linha.substring(indiceDataBaixaInicio, indiceDataBaixaFim);
	}
	
	public String obterDestino(String linha){
		return linha.substring(indiceDestinoInicio, indiceDestinoFim);
	}
	
	public String obterCodJornaleiro(String linha){
		return linha.substring(indiceCodJornaleiroInicio, indiceCodJornaleiroFim);
	}
	
	public String obterDataVencimentoBoleto(String linha){
		return linha.substring(indiceDataVencimentoInicio, indiceDataVencimentoFim);
	}
	
	public String obterDataPagamentoBoleto(String linha){
		return linha.substring(indiceDataPagamentoInicio, indiceDataPagamentoFim);
	}
	
	public String obterValorBoleto(String linha){
		return linha.substring(indiceValorDoBoletoInicio, indiceValorDoBoletoFim);
	}
	
	public String obterValorPago(String linha){
		return linha.substring(indiceValorPagoInicio, indiceValorPagoFim);
	}
	
	public String obterNossoNumeroConsolidado(String linha){
		return linha.substring(indiceNossoNumeroConsolidadoInicio, indiceNossoNumeroConsolidadoFim);
	}
	
	public String obterQtdeTotalRegistros(String linha){
		return linha.substring(indiceQtdRegistrosInicio, indiceQtdRegistrosFim);
	}
	
	public String obterDataPagamento(String linha) {
		return linha.substring(indiceDataPagamentoInicio, indiceDataPagamentoFim);
	}

}
