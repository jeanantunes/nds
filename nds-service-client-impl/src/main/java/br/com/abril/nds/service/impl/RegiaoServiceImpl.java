package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.RegiaoCotaDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_CotaDTO;
import br.com.abril.nds.dto.RegiaoNMaiores_ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroCotasRegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroRegiaoNMaioresProdDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.distribuicao.RegistroCotaRegiao;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.RegiaoRepository;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Service
public class RegiaoServiceImpl implements RegiaoService  {
	
	@Autowired
	private RegiaoRepository regiaoRepository;

	@Autowired
	private TipoSegmentoProdutoRepository segmentoRepository;
	
	@Autowired
	private RegistroCotaRegiaoRepository registroCotaRegiaoRepository;
	
	@Autowired
	private TipoClassificacaoProdutoRepository tipoClassificacaoProduto;
	
	@Override
	@Transactional
	public void salvarRegiao(Regiao regiao) {
		regiaoRepository.adicionar(regiao);
		}
	
	@Transactional(readOnly=true)
	@Override
	public List<RegiaoDTO> buscarRegiao() {
		return regiaoRepository.buscarRegiao();
	}
	
	@Override
	@Transactional
	public List<RegiaoCotaDTO> carregarCotasRegiao(FiltroCotasRegiaoDTO filtro) {		
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		return registroCotaRegiaoRepository.carregarCotasRegiao(filtro);
	}

	@Override
	@Transactional
	public void excluirRegiao(Long id) {
		Regiao regiao = this.regiaoRepository.buscarPorId(id);
		
		registroCotaRegiaoRepository.removerRegistroCotaReagiaPorRegiao(regiao);
		regiaoRepository.remover(regiao);
	}

	@Override
	@Transactional
	public void excluirRegistroCotaRegiao(Long id) {
		registroCotaRegiaoRepository.removerPorId(id);
		
	}
	
	@Override
	@Transactional
	public List<RegiaoCotaDTO> buscarPorCEP(FiltroCotasRegiaoDTO filtro) {
		if(filtro == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Filtro não deve ser nulo.");
		
		return registroCotaRegiaoRepository.buscarPorCEP(filtro);
	}

	@Override
	@Transactional
	public void addCotaNaRegiao(RegistroCotaRegiao registro) {
		registroCotaRegiaoRepository.adicionar(registro);
	}

	@Override
	@Transactional
	public Regiao obterRegiaoPorId(Long idRegiao) {
		return regiaoRepository.buscarPorId(idRegiao);
	}

	@Override
	@Transactional
	public List<TipoSegmentoProduto> carregarSegmentos() {
		return segmentoRepository.obterTipoSegmentoProdutoOrdenados(Ordenacao.ASC);
	}


	@Override
	@Transactional
	public void alterarRegiao(Regiao regiao) {
		regiaoRepository.merge(regiao);
	}

	@Override
	@Transactional
	public List<Integer> buscarNumeroCotasPorIdRegiao(Long idRegiao) {
		return registroCotaRegiaoRepository.buscarNumeroCotasPorIdRegiao(idRegiao);
	}

	@Override
	@Transactional
	public List<RegiaoCotaDTO> buscarPorSegmento(FiltroCotasRegiaoDTO filtro) {
		return regiaoRepository.buscarCotasPorSegmento(filtro);
	}
	
	@Override
	@Transactional
	public List<TipoClassificacaoProduto> buscarClassificacao() {
		return tipoClassificacaoProduto.buscarTodos();
	}

	@Override
	@Transactional
	public List<RegiaoNMaiores_ProdutoDTO> buscarProdutos(FiltroRegiaoNMaioresProdDTO filtro) {
		return registroCotaRegiaoRepository.buscarProdutos(filtro);
	}

	@Override
	@Transactional
	public List<RegiaoNMaiores_CotaDTO> rankingCotas(List<String> idsProdEdicaoParaMontagemRanking, Integer limite) {
		return registroCotaRegiaoRepository.rankingCotas(idsProdEdicaoParaMontagemRanking, limite);
	}

	@Override
	@Transactional
	public List<String> listaIdProdEdicaoParaRanking(String codProd, String numEdicao) {
		return registroCotaRegiaoRepository.idProdEdicaoParaMontagemDoRanking(codProd, numEdicao);
	}

	@Override
	@Transactional
	public List<RegiaoNMaiores_CotaDTO> filtroRankingCotas(Integer numCota) {
		return registroCotaRegiaoRepository.filtroRanking(numCota);
	}

	@Override
	@Transactional
	public BigDecimal calcularFaturamentoCota(Long cotaID, Intervalo<Date> intervalo) {

	    return registroCotaRegiaoRepository.calcularFaturamentoCota(cotaID, intervalo);
	}
	
}
