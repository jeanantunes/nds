package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.util.Intervalo;

public interface RegiaoService {

	void salvarRegiao(Regiao regiao);
	
	List<RegiaoDTO> buscarRegiao();
	
	Regiao obterRegiaoPorId (Long Id);
	
	void addCotaNaRegiao (RegistroCotaRegiao registro);
	
	void excluirRegistroCotaRegiao(Long id);

	void excluirRegiao (Long id);
	
	void alterarRegiao (Regiao regiao);
	
	List<RegiaoCotaDTO> carregarCotasRegiao (FiltroCotasRegiaoDTO filtro);
	
	List<RegiaoCotaDTO> buscarPorCEP (FiltroCotasRegiaoDTO filtro);
	
	List<TipoSegmentoProduto> carregarSegmentos();
	
	List<Integer> buscarNumeroCotasPorIdRegiao (Long idRegiao);
	
//	List<RegiaoCotaDTO> carregarTodasCotasDaRegiao ();
	
	List<RegiaoCotaDTO> buscarPorSegmento (FiltroCotasRegiaoDTO filtro);
	
	List<TipoClassificacaoProduto> buscarClassificacao();
	
	List<RegiaoNMaiores_ProdutoDTO> buscarProdutos(FiltroRegiaoNMaioresProdDTO filtro);
	
	List<RegiaoNMaiores_CotaDTO> rankingCotas (List<String> idsProdEdicaoParaMontagemRanking, Integer limite);
	
	List<String> listaIdProdEdicaoParaRanking(String codProd, String numEdicao);
	
	List<RegiaoNMaiores_CotaDTO> filtroRankingCotas (Integer numCota);
	
	BigDecimal calcularFaturamentoCota (Long cotaID, Intervalo<Date> intervalo);
}
