package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;

public interface RegistroCotaRegiaoRepository extends Repository<RegistroCotaRegiao, Long> {
	
	List<RegiaoCotaDTO> carregarCotasRegiao (FiltroCotasRegiaoDTO filtro);
	
	List<RegiaoCotaDTO> buscarPorCEP (FiltroCotasRegiaoDTO filtro);
	
	List<Integer> buscarNumeroCotasPorIdRegiao (Long idRegiao);
	
	List<RegiaoNMaiores_ProdutoDTO> buscarProdutos (FiltroRegiaoNMaioresProdDTO filtro);
	
	List<RegiaoNMaiores_CotaDTO> rankingCotas (List<String> idsProdEdicaoParaMontagemRanking, Integer limite);
	
	List<String> idProdEdicaoParaMontagemDoRanking (String codigoProduto, String numeroEdicao);
	
	List<RegiaoNMaiores_CotaDTO> filtroRanking (Integer numCota);

	void removerRegistroCotaReagiaPorRegiao(Regiao regiao);

	List<RegistroCotaRegiao> obterRegistroCotaReagiaPorRegiao(Regiao regiao);
	
	BigDecimal calcularFaturamentoCota (Long cotaID);
	
}
