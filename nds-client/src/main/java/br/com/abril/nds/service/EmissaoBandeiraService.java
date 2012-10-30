package br.com.abril.nds.service;

public interface EmissaoBandeiraService {
	
	byte[] imprimirBandeira(Integer semana, Integer numeroPallets) throws Exception;
	
	byte[] imprimirBandeiraManual(Integer semana, Integer numeroPallets,String nome, String codigoPracaNoProdin, String praca, String destino, String canal) throws Exception;

}
