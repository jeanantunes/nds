package br.com.abril.nds.service;

import java.util.List;
import java.util.Map;

import br.com.abril.nds.export.cnab.cobranca.DetalheSegmentoP;
import br.com.abril.nds.model.cadastro.Banco;

public interface GeradorArquivoCobrancaBancoService {
	
	void gerarArquivoCobranca(Map<Banco, List<DetalheSegmentoP>> mapaArquivoCobranca);
}
