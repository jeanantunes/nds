package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.service.RelatorioVendasService;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.ValidacaoVO;
@Service
public class RelatorioVendasServiceImpl implements RelatorioVendasService {

	@Autowired
	private RelatorioVendasRepository relatorioVendasRepository;
	
	@Autowired
	private RankingRepository rankingRepository;

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
	
		return obterParticipacaoCurvaABCDistribuidor(lista);
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
		
		Map<Long, Long> mapRankingProdutoPorProduto =
			this.rankingRepository.obterRankingProdutoPorProduto(null);
		
		Map<Long, Long> mapRankingCotaPorProduto =
			this.rankingRepository.obterRankingCotaPorProduto(null);
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				
				//TODO:
				dto.setRkProduto(mapRankingProdutoPorProduto.get(null));
				dto.setRkCota(mapRankingCotaPorProduto.get(dto.getIdCota()));
			}
		}
	
		return obterParticipacaoCurvaABCDistribuidor(lista);
	}
	
	/**
	 * Obtéma a porcentagem de participação e participação acumulada no resultado da consulta.
	 */
	private List<RegistroCurvaABCDistribuidorVO> obterParticipacaoCurvaABCDistribuidor(List<RegistroCurvaABCDistribuidorVO> lista) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
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
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
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
		BigInteger vendaTotal = BigInteger.ZERO;

		// Soma todos os valores de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
			vendaTotal = vendaTotal.add(registro.getVendaExemplares());
			
			BigDecimal porcentagemMargem = null;
			
			if (registro.getFaturamentoCapa() != null && registro.getFaturamentoCapa().compareTo(BigDecimal.ZERO) > 0) {
				
				porcentagemMargem = MathUtil.divide(registro.getValorMargemDistribuidor(),registro.getFaturamentoCapa());	
			
			} else {
			
				porcentagemMargem = BigDecimal.ZERO; 
			}

			registro.setPorcentagemMargemDistribuidor(porcentagemMargem);
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		BigDecimal participacaoAcumulada = BigDecimal.ZERO;
		BigDecimal porcentagemVendaRegistro = BigDecimal.ZERO;

		// Verifica o percentual dos valores em relação ao total de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {

			// Partipacao do registro em relacao a participacao total no periodo
			if ( participacaoTotal.doubleValue() != 0 ) {
				participacaoRegistro = new BigDecimal((registro.getFaturamentoCapa().doubleValue()*100)/participacaoTotal.doubleValue());
			}
			registro.setParticipacao(participacaoRegistro);

			if (vendaTotal.doubleValue() != 0) {
				porcentagemVendaRegistro = new BigDecimal(registro.getVendaExemplares().doubleValue()*100/vendaTotal.doubleValue());
			}
			
			participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
			
			registro.setPorcentagemVendaExemplares(porcentagemVendaRegistro);
			registro.setParticipacaoAcumulada(participacaoAcumulada);
			registro.setDataDe(filtro.getDataDe());
			registro.setDataAte(filtro.getDataAte());
		}

		return lista;
	}

}
