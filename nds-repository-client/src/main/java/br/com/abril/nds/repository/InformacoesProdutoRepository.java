package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.InformacoesAbrangenciaEMinimoProdDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InfoProdutosItemRegiaoEspecificaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;

public interface InformacoesProdutoRepository extends Repository<InformacoesProdutoDTO, Long> {

	List<InformacoesProdutoDTO> buscarProdutos (FiltroInformacoesProdutoDTO dto);
	List<InfoProdutosItemRegiaoEspecificaDTO> buscarItensRegiao ();
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	InformacoesAbrangenciaEMinimoProdDTO buscarAbrangenciaEMinimo (Long estudoId);
	
}
