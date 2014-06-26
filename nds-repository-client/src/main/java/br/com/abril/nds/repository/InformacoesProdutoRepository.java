package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.InfoProdutosBonificacaoDTO;
import br.com.abril.nds.dto.InformacoesCaracteristicasProdDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.InformacoesVendaEPerceDeVendaDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;

public interface InformacoesProdutoRepository extends Repository<InformacoesProdutoDTO, Long> {

	List<InformacoesProdutoDTO> buscarProdutos (FiltroInformacoesProdutoDTO dto);
	
	List<InfoProdutosBonificacaoDTO> buscarItensRegiao (Long idEstudo);
	
	InformacoesCaracteristicasProdDTO buscarCaracteristicas (String codProduto, Long numEdicao);
	
	InformacoesVendaEPerceDeVendaDTO buscarVendas (String codProduto, Long numEdicao);
	
}
