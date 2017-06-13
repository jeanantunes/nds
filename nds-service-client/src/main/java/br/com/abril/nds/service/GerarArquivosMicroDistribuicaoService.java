package br.com.abril.nds.service;

import java.io.IOException;
import java.util.Date;

import br.com.abril.nds.model.seguranca.Usuario;

public interface GerarArquivosMicroDistribuicaoService {

	void gerarArquivoMatriz(String caminhoArquivo, Date data, Usuario usuario, String nomeInterface) ;
	
	void gerarArquivoDeajo(Date data, Usuario usuario);
	
	void gerarArquivoDeapr(Date data, Usuario usuario);

	void processarMicrodistribuicao(String caminhoArquivo, Usuario usuario, String codDistribuidor) throws IOException;
}
