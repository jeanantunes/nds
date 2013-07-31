package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.service.RelatorioVendasService;
import br.com.abril.nds.vo.ValidacaoVO;
@Service
public class RelatorioVendasServiceImpl implements RelatorioVendasService {

	@Autowired
	private RelatorioVendasRepository relatorioVendasRepository;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		Map<Long, Long> mapRankingCota =
			this.rankingRepository.obterRankingCota();
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				dto.setRkCota(mapRankingCota.get(dto.getIdCota()));
			}
		}
	
		return this.obterParticipacaoCurvaABCDistribuidor(lista);
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		
		List<RegistroCurvaABCEditorVO> lista = 
			this.relatorioVendasRepository.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
		
		Map<Long, Long> mapRankingEditor =
			this.rankingRepository.obterRankingEditor();
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCEditorVO dto : lista){
				dto.setRkEditor(mapRankingEditor.get(dto.getCodigoEditor()));
			}
		}
		
		return this.complementarCurvaABCEditor(lista, filtroCurvaABCEditorDTO);
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		Produto produto =
			this.produtoRepository.obterProdutoPorCodigo(filtroCurvaABCDistribuidorDTO.getCodigoProduto());
		
		Map<Long, Long> mapRankingProdutoPorProduto =
			this.rankingRepository.obterRankingProdutoPorProduto();
		
		Map<Long, Long> mapRankingCotaPorProduto =
			this.rankingRepository.obterRankingCotaPorProduto(produto.getId());
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				
				dto.setRkProduto(mapRankingProdutoPorProduto.get(dto.getIdProduto()));
				dto.setRkCota(mapRankingCotaPorProduto.get(dto.getIdCota()));
			}
		}
	
		return this.obterParticipacaoCurvaABCDistribuidor(lista);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		
		List<RegistroCurvaABCCotaDTO> lista =
			this.relatorioVendasRepository.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(filtroCurvaABCCotaDTO.getCodigoCota());
		
		Map<Long, Long> mapRanking =
			this.rankingRepository.obterRankingProdutoPorCota(cota.getId());
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCCotaDTO dto : lista){
				dto.setRkProduto(mapRanking.get(dto.getIdProdutoEdicao()));
			}
		}
		
		return this.obterParticipacaoCurvaABCCota(lista);
	}
	
	/**
	 * Obtéma a porcentagem de participação e participação acumulada no resultado da consulta.
	 */
	private List<RegistroCurvaABCDistribuidorVO> obterParticipacaoCurvaABCDistribuidor(List<RegistroCurvaABCDistribuidorVO> lista) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		BigDecimal CEM = new BigDecimal(100);
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		// Soma todos os valores de participacao
		for (RegistroCurvaABCDistribuidorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;
		
		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCDistribuidorVO registro : lista) {
			
			// Partipacao do registro em relacao a participacao total no periodo
			if (participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
				participacaoRegistro = registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setParticipacao(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
		}

		return lista;
	}
	
	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * @param lista
	 * @return
	 */
	private List<RegistroCurvaABCEditorVO> complementarCurvaABCEditor(List<RegistroCurvaABCEditorVO> lista, FiltroCurvaABCEditorDTO filtro) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;

		// Soma todos os valores de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setParticipacaoAcumulada(participacaoAcumulada);
			registro.setDataDe(filtro.getDataDe());
			registro.setDataAte(filtro.getDataAte());
		}

		return lista;
	}
	
	private List<RegistroCurvaABCCotaDTO> obterParticipacaoCurvaABCCota(List<RegistroCurvaABCCotaDTO> lista) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		// Soma todos os valores de participacao
		for (RegistroCurvaABCCotaDTO registro : lista) {
			if (registro.getFaturamento()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamento());
			}
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;
		
		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCCotaDTO registro : lista) {
			
			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamento().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setParticipacao(participacaoRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
		}

		return lista;
	}

}
