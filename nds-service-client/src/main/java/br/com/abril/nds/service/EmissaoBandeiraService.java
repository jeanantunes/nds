package br.com.abril.nds.service;

import java.util.Date;


public interface EmissaoBandeiraService {
	
	byte[] imprimirBandeira(Integer semana, Integer numeroPallets, Date dataEnvio, Long forncedor) throws Exception;
	
	byte[] imprimirBandeiraManual(String semana, Integer numeroPallets,String fornecedor,
			String praca, String canal, String dataEnvio, String titulo) throws Exception;
	
}
