package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.service.RelatorioVendasService;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.PaginacaoVO;
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
	
	@Autowired
	private PdvRepository pdvRepository;
	
	private static final BigDecimal CEM = new BigDecimal(100);
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista = this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()){
			
			Map<Long, RankingDTO> mapRankingCota = this.rankingRepository.obterRankingCota(filtroCurvaABCDistribuidorDTO);
			
			participacaoTotal = obterParticipacaoTotal(mapRankingCota.values());
			
			
			for(RegistroCurvaABCDistribuidorVO dto : lista) {
				
				dto.setRkCota(mapRankingCota.get(dto.getIdCota()).getRanking());
				dto.setFaturamentoCapa(mapRankingCota.get(dto.getIdCota()).getValor());
				dto.setVendaExemplares(mapRankingCota.get(dto.getIdCota()).getVendaExemplares());	
				dto.setQuantidadePdvs(this.pdvRepository.obterQntPDV(dto.getIdCota(), null));
				dto.setParticipacaoAcumulada(mapRankingCota.get(dto.getIdCota()).getValorAcumulado());

				
			}
		}
	
		this.carregarParticipacaoCurvaABCDistribuidor(lista, participacaoTotal);
		
		return lista;
	}
	
	@Transactional
	public Integer obterQtdeRegistrosCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		return relatorioVendasRepository.obterQtdRegistrosCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
	}

	@Transactional
	public Integer obterQtdeRegistrosCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		return relatorioVendasRepository.obterQtdRegistrosCurvaABCEditor(filtroCurvaABCEditorDTO);
	}
	
	@Transactional
	public Integer obterQtdeRegistrosCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		return relatorioVendasRepository.obterQtdRegistrosCurvaABCCota(filtroCurvaABCCotaDTO);
	}
	
	
	
	@Override
	@Transactional
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		
		List<RegistroCurvaABCEditorVO> lista = this.relatorioVendasRepository.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()) {
			
			Map<Long, RankingDTO> mapRankingEditor = this.rankingRepository.obterRankingEditor(filtroCurvaABCEditorDTO);
			
			participacaoTotal = obterParticipacaoTotal(mapRankingEditor.values());
			
			for(RegistroCurvaABCEditorVO dto : lista) {
				
				dto.setRkEditor(mapRankingEditor.get(dto.getCodigoEditor()).getRanking());
				dto.setFaturamentoCapa(mapRankingEditor.get(dto.getCodigoEditor()).getValor());
				dto.setReparte(mapRankingEditor.get(dto.getCodigoEditor()).getReparte());
				dto.setVendaExemplares(mapRankingEditor.get(dto.getCodigoEditor()).getVendaExemplares());
				dto.setPorcentagemVendaExemplares(mapRankingEditor.get(dto.getCodigoEditor()).getPorcentagemVendaExemplares());
				dto.setFaturamentoCapa(mapRankingEditor.get(dto.getCodigoEditor()).getFaturamentoCapa());
				dto.setValorMargemDistribuidor(mapRankingEditor.get(dto.getCodigoEditor()).getValorMargemDistribuidor());
				dto.setPorcentagemMargemDistribuidor(mapRankingEditor.get(dto.getCodigoEditor()).getPorcentagemMargemDistribuidor());
				dto.setParticipacaoAcumulada(mapRankingEditor.get(dto.getCodigoEditor()).getValorAcumulado());

				
				
			}
		}
		
		complementarCurvaABCEditor(lista, filtroCurvaABCEditorDTO, participacaoTotal);
		
		return lista;
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()){
			
			Produto produto =
					this.produtoRepository.obterProdutoPorCodigo(filtroCurvaABCDistribuidorDTO.getCodigoProduto());
			
			Map<Long, RankingDTO> mapRankingProdutoPorProduto =
					this.rankingRepository.obterRankingProdutoPorProduto(filtroCurvaABCDistribuidorDTO);
				
			Map<Long, RankingDTO> mapRankingCotaPorProduto =
					this.rankingRepository.obterRankingCotaPorProduto(filtroCurvaABCDistribuidorDTO, produto.getId());
			
			participacaoTotal = obterParticipacaoTotal(mapRankingCotaPorProduto.values());
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){

				dto.setRkProduto(mapRankingProdutoPorProduto.get(dto.getIdProduto()).getRanking());
				dto.setRkCota(mapRankingCotaPorProduto.get(dto.getIdCota()).getRanking());
				
				dto.setFaturamentoCapa(mapRankingCotaPorProduto.get(dto.getIdCota()).getValor());
				dto.setQuantidadePdvs(this.pdvRepository.obterQntPDV(dto.getIdCota(), null));
				dto.setVendaExemplares(mapRankingCotaPorProduto.get(dto.getIdCota()).getVendaExemplares());
				
			}
		}
	
		carregarParticipacaoCurvaABCDistribuidor(lista, participacaoTotal);
		
		return lista;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		
		List<RegistroCurvaABCCotaDTO> lista = this.relatorioVendasRepository.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()) {
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(filtroCurvaABCCotaDTO.getCodigoCota());
			
			Map<Long, RankingDTO> mapRanking = this.rankingRepository.obterRankingProdutoPorCota(filtroCurvaABCCotaDTO, cota.getId());

			participacaoTotal = obterParticipacaoTotal(mapRanking.values());
			
			for(RegistroCurvaABCCotaDTO dto : lista) {
				
				dto.setRkProduto(mapRanking.get(dto.getIdProdutoEdicao()).getRanking());
				dto.setFaturamento(mapRanking.get(dto.getIdProdutoEdicao()).getValor());
				dto.setReparte(mapRanking.get(dto.getIdProdutoEdicao()).getReparte());
				dto.setVendaExemplares(mapRanking.get(dto.getIdProdutoEdicao()).getVendaExemplares());
				dto.setPorcentagemVenda(mapRanking.get(dto.getIdProdutoEdicao()).getPorcentagemVendaExemplares());
				dto.setParticipacaoAcumulada(mapRanking.get(dto.getIdProdutoEdicao()).getValorAcumulado());
				
			}
			
		}
		
		carregarParticipacaoCurvaABCCota(lista, participacaoTotal);
		
		return lista;
	}
	
	private BigDecimal obterParticipacaoTotal(Collection<RankingDTO> rankings) {
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		for(RankingDTO r : rankings) {
			if(r.getValor()!=null) {
				participacaoTotal = participacaoTotal.add(r.getValor());
			}
		}
		
		return participacaoTotal;
		
	}
	
	/**
	 * Obtéma a porcentagem de participação e participação acumulada no resultado da consulta.
	 */
	private void carregarParticipacaoCurvaABCDistribuidor(List<RegistroCurvaABCDistribuidorVO> lista, BigDecimal participacaoTotal) {
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		
		// Partipacao do registro em relacao a participacao total no periodo
		if (participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			//Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCDistribuidorVO registro : lista) {
				
				participacaoRegistro = registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				
				registro.setParticipacao(participacaoRegistro);
				
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}

				
			}
		}

	}
	
	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * 
	 * @param lista
	 * @param filtro
	 * @param participacaoTotal
	 */
	private void complementarCurvaABCEditor(List<RegistroCurvaABCEditorVO> lista, FiltroCurvaABCEditorDTO filtro, BigDecimal participacaoTotal) {

		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		//BigDecimal participacaoAcumulada = BigDecimal.ZERO;

		// Partipacao do registro em relacao a participacao total no periodo
		if ( participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			// Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCEditorVO registro : lista) {
				
				participacaoRegistro = registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				
				registro.setParticipacao(participacaoRegistro);
				
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}
				
				registro.setDataDe(filtro.getDataDe());
				registro.setDataAte(filtro.getDataAte());

				registro.setPorcentagemVendaExemplares(BigDecimal.ZERO);
				
				if(	registro.getReparte() != null && 
					BigInteger.ZERO.compareTo(registro.getReparte()) < 0){
					

					registro.setPorcentagemVendaExemplares(
							MathUtil.divide(CEM.multiply(new BigDecimal(registro.getVendaExemplares())), 
									new BigDecimal(registro.getReparte())));
					
					
				}
				
			}
		}

	}
	
	private void carregarParticipacaoCurvaABCCota(List<RegistroCurvaABCCotaDTO> lista, BigDecimal participacaoTotal) {
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		
		// Partipacao do registro em relacao a participacao total no periodo
		if ( participacaoTotal.compareTo(BigDecimal.ZERO) != 0){
		
			// Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCCotaDTO registro : lista) {
				
				participacaoRegistro =
						registro.getFaturamento().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				
				registro.setParticipacao(participacaoRegistro);
				
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}
				
				
			}
		}

	}
	
}