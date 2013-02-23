package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.InformacoesBaseProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;

public interface InformacoesProdutoRepository extends Repository<InformacoesProdutoDTO, Long> {

	List<InformacoesProdutoDTO> buscarProdutos (FiltroInformacoesProdutoDTO dto);
//	List<InformacoesProdutoDTO> buscarProdutos (Long numeroEdicao, String tipoClassif);
	List<InformacoesBaseProdDTO> buscarBase (String codProduto);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao); 
}
