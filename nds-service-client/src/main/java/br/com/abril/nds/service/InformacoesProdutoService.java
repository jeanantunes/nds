package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.InformacoesBaseProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface InformacoesProdutoService {
	List<TipoClassificacaoProduto> buscarClassificacao();
	
	List<InformacoesProdutoDTO> buscarProduto (FiltroInformacoesProdutoDTO filtro);
	
	List<InformacoesBaseProdDTO> buscarBases (String codProduto);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	
}
