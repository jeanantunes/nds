package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
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

	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		Map<Long, Long> mapRanking =
			this.rankingRepository.obterRankingProdutoDistribuidor();
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				dto.setRkCota(mapRanking.get(dto.getIdCota()));
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

}
