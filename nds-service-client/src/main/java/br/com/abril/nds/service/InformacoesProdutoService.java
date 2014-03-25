package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;

public interface InformacoesProdutoService {
	
	List<TipoClassificacaoProduto> buscarClassificacao();
	
	List<InformacoesProdutoDTO> buscarProduto (FiltroInformacoesProdutoDTO filtro);
	
	List<EdicaoBaseEstudoDTO> buscarBases (Long idEstudo);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	
	List<InfoProdutosItemRegiaoEspecificaDTO> buscarItemRegiao (Long idEstudos);
	
	List<ProdutoBaseSugeridaDTO> buscarBaseSugerida(Long idEstudo);
	
	InformacoesVendaEPerceDeVendaDTO buscarVendas (String codProduto, Long numEdicao);
	
}
