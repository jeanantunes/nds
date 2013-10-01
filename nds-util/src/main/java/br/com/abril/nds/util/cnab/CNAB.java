package br.com.abril.nds.util.cnab;


public class CNAB {

	private CNAB(){}
	
	public int indiceDataPagamentoInicio;
	public int indiceDataPagamentoFim;
	
	public int indiceNossoNumeroInicio;
	public int indiceNossoNumeroFim;
	
	public int indiceValorPagamentoInicio;
	public int indiceValorPagamentoFim;
	
	public int indiceNumeroRegistroInicio;
	public int indiceNumeroRegistroFim;

	public int indiceNumeroAgenciaInicio;
	public int indiceNumeroAgenciaFim;
	
	public int indiceNumeroContaInicio;
	public int indiceNumeroContaFim;
	
	
	public String obterDataPagamento(String linha) {
		return linha.substring(indiceDataPagamentoInicio, indiceDataPagamentoFim);
	}
	
	public String obterNossoNumero(String linha) {
		return linha.substring(indiceNossoNumeroInicio, indiceNossoNumeroFim);
	}
	
	public String obterValorPagamento(String linha) {
		return linha.substring(indiceValorPagamentoInicio, indiceValorPagamentoFim);
	}
	
	public String obterNumeroRegistro(String linha) {
		return linha.substring(indiceNumeroRegistroInicio, indiceNumeroRegistroFim);
	}
	
	public String obterNumeroAgencia(String linha) {
		return linha.substring(indiceNumeroAgenciaInicio, indiceNumeroAgenciaFim); 
	}
	
	public String obterNumeroConta(String linha) {
		return linha.substring(indiceNumeroContaInicio, indiceNumeroContaFim);
	}
	
	/**
	 * Retorna Cnab configurado para o banco HSBC 
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Hsbc(){
    	
    	CNAB cnab = new CNAB();
    	
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 53;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
		cnab.indiceNumeroAgenciaInicio = 26;
		cnab.indiceNumeroAgenciaFim = 31;
		
		cnab.indiceNumeroContaInicio = 33;
		cnab.indiceNumeroContaFim = 44;
		
		return cnab;
	}
	
    /**
	 * Retorna Cnab configurado para o banco Bradesco
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Bradesco(){
    	
    	CNAB cnab = new CNAB();

    	cnab.indiceDataPagamentoInicio = 110;
    	cnab.indiceDataPagamentoFim = 116;
		
    	cnab.indiceNossoNumeroInicio = 70;
    	cnab.indiceNossoNumeroFim = 81;
		
    	cnab.indiceValorPagamentoInicio = 256;
    	cnab.indiceValorPagamentoFim = 266;
		
    	cnab.indiceNumeroRegistroInicio = 395;
    	cnab.indiceNumeroRegistroFim = 400;
		
    	cnab.indiceNumeroAgenciaInicio = 26;
    	cnab.indiceNumeroAgenciaFim = 29;
		
    	cnab.indiceNumeroContaInicio = 31;
    	cnab.indiceNumeroContaFim = 37;
    	
    	return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco Itau
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400Itau(){
    	
    	CNAB cnab = new CNAB();

    	cnab.indiceDataPagamentoInicio = 366;
    	cnab.indiceDataPagamentoFim = 391;
		
    	cnab.indiceNossoNumeroInicio = 63;
    	cnab.indiceNossoNumeroFim = 70;
		
    	cnab.indiceValorPagamentoInicio = 127;
    	cnab.indiceValorPagamentoFim = 139;
		
    	cnab.indiceNumeroRegistroInicio = 395;
		cnab.indiceNumeroRegistroFim = 400;

		cnab.indiceNumeroAgenciaInicio = 143;
		cnab.indiceNumeroAgenciaFim = 147;
		
		cnab.indiceNumeroContaInicio = 24;
		cnab.indiceNumeroContaFim = 28;
		
		return cnab;
	}
    
    /**
	 * Retorna Cnab configurado para o banco BB
	 * @param cnab
	 */
    public static CNAB newInstanceCnab400BancoDoBrasil(){
    	
    	CNAB cnab = new CNAB();
 		
		cnab.indiceDataPagamentoInicio = 110;
		cnab.indiceDataPagamentoFim = 116;
		
		cnab.indiceNossoNumeroInicio = 37;
		cnab.indiceNossoNumeroFim = 53;
		
		cnab.indiceValorPagamentoInicio = 253;
		cnab.indiceValorPagamentoFim = 266;
		
		cnab.indiceNumeroRegistroInicio = 394;
		cnab.indiceNumeroRegistroFim = 400;
		
		cnab.indiceNumeroAgenciaInicio = 26;
		cnab.indiceNumeroAgenciaFim = 31;
		
		cnab.indiceNumeroContaInicio = 33;
		cnab.indiceNumeroContaFim = 44;
		
		return cnab;
	}
	
}
