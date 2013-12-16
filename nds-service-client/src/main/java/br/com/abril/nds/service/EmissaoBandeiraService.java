package br.com.abril.nds.service;


public interface EmissaoBandeiraService {
	
	byte[] imprimirBandeira(Integer semana, Integer numeroPallets) throws Exception;
	
	byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,String fornecedor,
			String praca, String canal, String dataEnvio, String titulo) throws Exception;
	
}
