package br.com.abril.nds.service;

import java.io.IOException;
import java.util.Date;

import br.com.abril.nds.model.seguranca.Usuario;

public interface GerarArquivosMicroDistribuicaoService {

	void gerarArquivoMatriz(Date data, Usuario usuario);
	
	void gerarArquivoDeajo(Date data);
	
	void gerarArquivoDeapr(Date data);

	void processarMicrodistribuicao(String caminhoArquivo, Usuario usuario, String codDistribuidor) throws IOException;
}
