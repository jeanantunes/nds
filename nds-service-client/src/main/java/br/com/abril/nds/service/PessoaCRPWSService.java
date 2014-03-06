package br.com.abril.nds.service;

import v1.pessoadetalhe.ebo.abril.types.PessoaDto;


public interface PessoaCRPWSService {

	public PessoaDto obterDadosFiscais(String codSistema,Integer codTipoDoc,String numDoc);
	
}
