package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.RegistroCurvaABCCotaDTO;
import br.com.abril.nds.dto.RegistroCurvaABCDTO;
import br.com.abril.nds.dto.RegistroRankingSegmentoDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.TipoConsultaCurvaABC;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroRankingSegmentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.RegistroCotaRegiaoRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository.TipoPesquisaRanking;
import br.com.abril.nds.service.RelatorioVendasService;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.vo.ValidacaoVO;
@Service
public class RelatorioVendasServiceImpl implements RelatorioVendasService {

	@Autowired
	private RelatorioVendasRepository relatorioVendasRepository;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Autowired
	private PdvRepository pdvRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private RegistroCotaRegiaoRepository registroCotaRegiaoRepository;
	
	private static final BigDecimal CEM = new BigDecimal(100);
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		validarProdutoEdicao(filtroCurvaABCDistribuidorDTO);
		
		this.validarFiltroRegiao(filtroCurvaABCDistribuidorDTO);
		
		List<RegistroCurvaABCDistribuidorVO> lista = this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO, TipoPesquisaRanking.RankingCota);
		
		if(lista == null || lista.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		for(RegistroCurvaABCDistribuidorVO r : lista) {
			if(r.getParticipacao()!=null) {
				participacaoTotal = participacaoTotal.add(r.getParticipacao());
			}
		}
	
		this.carregarParticipacaoCurvaABCDistribuidor(lista, participacaoTotal);
		
		return lista;
	}

	@Override
	@Transactional	
	public void buscarQuantidadeDePdvs(List<RegistroCurvaABCDistribuidorVO> lista) {
		
		if (!lista.isEmpty()) {

			for (RegistroCurvaABCDistribuidorVO cotaCurvaDistribuidor : lista) {
				cotaCurvaDistribuidor.setQuantidadePdvs(this.pdvRepository.obterQntPDV(cotaCurvaDistribuidor.getIdCota(), null));
			}

		}
	}
	
	
	@Override
	@Transactional
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		
		this.validarFiltroRegiao(filtroCurvaABCEditorDTO);

		List<RegistroCurvaABCEditorVO> lista = this.relatorioVendasRepository.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()) {
			
			for(RegistroCurvaABCDTO r : lista) {
				if(r.getParticipacao()!=null) {
					participacaoTotal = participacaoTotal.add(r.getParticipacao());
				}
			}
			
		}
		
		complementarCurvaABCEditor(lista, filtroCurvaABCEditorDTO, participacaoTotal);
		
		return lista;
	}
	
	@Override
	@Transactional
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtroCurvaABCEditorDTO) {
		
		FiltroCurvaABCEditorDTO filtro = new FiltroCurvaABCEditorDTO();
		
		filtro.setCodigoEditor(filtroCurvaABCEditorDTO.getNumeroEditor());
		filtro.setDataDe(filtroCurvaABCEditorDTO.getDataDe());
		filtro.setDataAte(filtroCurvaABCEditorDTO.getDataAte());
		
		return this.relatorioVendasRepository.obterHistoricoEditor(filtro);
		
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		validarProdutoEdicao(filtroCurvaABCDistribuidorDTO);
		
		this.validarFiltroRegiao(filtroCurvaABCDistribuidorDTO);
		
		List<RegistroCurvaABCDistribuidorVO> lista = this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO, TipoPesquisaRanking.RankingProduto);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(lista==null || lista.isEmpty()){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		FiltroCurvaABCDistribuidorDTO filtroDistribuidor = new FiltroCurvaABCDistribuidorDTO();
		
		filtroDistribuidor.setDataDe(filtroCurvaABCDistribuidorDTO.getDataDe());
		filtroDistribuidor.setDataAte(filtroCurvaABCDistribuidorDTO.getDataAte());
		
		Map<Long, RankingDTO> mapRankingCota = this.rankingRepository.obterRankingCotaSomenteFaturamento(filtroDistribuidor);
		
		for(RegistroCurvaABCDistribuidorVO dto : lista){
			
			if(dto.getParticipacao()!=null) {
				participacaoTotal = participacaoTotal.add(dto.getParticipacao());
			}
			
			//buscarQuantidadeDePdvs(dto);
			
			if( mapRankingCota.get(dto.getIdCota()) != null ) {
				dto.setRkCota(mapRankingCota.get(dto.getIdCota()).getRanking());
			} else {
				dto.setRkCota(0L);
			}
			
		}
	
		carregarParticipacaoCurvaABCDistribuidor(lista, participacaoTotal);
		
		return lista;
	}


	private void validarProdutoEdicao(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		if(filtroCurvaABCDistribuidorDTO.getEdicaoProduto() == null){
			List<ProdutoEdicao> edicoesDoProduto = this.produtoEdicaoRepository.obterProdutosEdicaoComVendaEntreDatas(filtroCurvaABCDistribuidorDTO);
			
			List<Long> numEdicoesProduto = new ArrayList<>();
			
			for (ProdutoEdicao produtoEdicao : edicoesDoProduto) {
				numEdicoesProduto.add(produtoEdicao.getNumeroEdicao());
			}
			
			filtroCurvaABCDistribuidorDTO.setEdicaoProduto(numEdicoesProduto);
		}
		
		if(filtroCurvaABCDistribuidorDTO.getTipoConsultaCurvaABC() == TipoConsultaCurvaABC.PRODUTO){
			if (filtroCurvaABCDistribuidorDTO.getEdicaoProduto() == null || filtroCurvaABCDistribuidorDTO.getEdicaoProduto().isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma edição encontrada. Altere o período e tente novamente.");
			}
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		
		List<RegistroCurvaABCCotaDTO> lista = this.relatorioVendasRepository.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		
		BigDecimal participacaoTotal = BigDecimal.ZERO;
		
		if(!lista.isEmpty()) {
			for(RegistroCurvaABCDTO r : lista) {
				if(r.getParticipacao()!=null) {
					participacaoTotal = participacaoTotal.add(r.getParticipacao());
				}
			}
		}
		
		carregarParticipacaoCurvaABCCota(lista, participacaoTotal);
		
		for (RegistroCurvaABCCotaDTO registro : lista) {
			if(registro.getTipoPDV() != null && (filtroCurvaABCCotaDTO.getTipoPDV() == null || filtroCurvaABCCotaDTO.getTipoPDV().isEmpty())){
				filtroCurvaABCCotaDTO.setTipoPDV(registro.getTipoPDV());
				break;
			}else{
				if(!filtroCurvaABCCotaDTO.getTipoPDV().isEmpty()){
					break;
				}
			}
		}
		
		return lista;
	}

	@Override
	@Transactional(readOnly=true)
	public List<RegistroRankingSegmentoDTO> obterRankingSegmento(FiltroRankingSegmentoDTO filtro) {
		
		if (filtro.isPesquisaPorCota() == false && filtro.getIdTipoSegmento() == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de segmento.");
		}
		
		if ((filtro.getDe() != null && filtro.getAte() == null) || (filtro.getDe() == null && filtro.getAte() != null)) {

			throw new ValidacaoException(TipoMensagem.WARNING, "O período deve ser especificado corretamente.");
		}
		
		this.validarFiltroRegiao(filtro);

		List<RegistroRankingSegmentoDTO> registros = new ArrayList<>();
		
		if(!filtro.isPesquisaPorCota()){
			registros = this.relatorioVendasRepository.obterRankingSegmento(filtro);
		}else{
			
			for (TipoSegmentoProduto segmento : filtro.getListSegmentos()) {
				
				filtro.setIdTipoSegmento(segmento.getId());
				
				registros.addAll(this.relatorioVendasRepository.obterRelatorioVendaSegmentoPorCota(filtro));
				
			}
			
		}
		
		if (registros == null || registros.size() <= 0) {			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		filtro.setTotalFaturamento(this.obterFaturamentoTotalABCSegmento(registros));
		
		this.carregarParticipacoesCurvaABCSegmento(registros, filtro.getTotalFaturamento());
		
		return registros;
	}
	
	private BigDecimal obterFaturamentoTotalABCSegmento(List<RegistroRankingSegmentoDTO> lista) {
		BigDecimal totalFaturamento = BigDecimal.ZERO;
		
		for (RegistroRankingSegmentoDTO registro : lista) {
			  totalFaturamento = totalFaturamento.add(registro.getFaturamentoCapa() == null ? BigDecimal.ZERO : registro.getFaturamentoCapa());
		}
		return totalFaturamento;
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
				
				if(registro.getParticipacao() != null){
					participacaoRegistro = registro.getParticipacao().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
					registro.setParticipacao(participacaoRegistro);
				}else{
					registro.setParticipacao(BigDecimal.ZERO);
				}
				
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}

				
			}
		}

	}
	
	private void carregarParticipacoesCurvaABCSegmento(List<RegistroRankingSegmentoDTO> lista, BigDecimal participacaoTotal) {
		
		if (lista==null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
		}
		
		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		
		// Partipacao do registro em relacao a participacao total no periodo
		if (participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			//Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroRankingSegmentoDTO registro : lista) {
				
				if(registro.getFaturamentoCapa() != null){
					participacaoRegistro = registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
					registro.setParticipacao(participacaoRegistro);
				}else{
					registro.setParticipacao(BigDecimal.ZERO);
					registro.setParticipacaoAcumulada(BigDecimal.ZERO);
				}
				
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
		
		
		// Partipacao do registro em relacao a participacao total no periodo
		if ( participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			// Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCEditorVO registro : lista) {
				
				if(registro.getParticipacao() != null){
					participacaoRegistro = registro.getParticipacao().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				}else{
					participacaoRegistro = BigDecimal.ZERO;
				}
				
				registro.setParticipacao(participacaoRegistro);
				
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}
				
				registro.setDataDe(filtro.getDataDe());
				registro.setDataAte(filtro.getDataAte());
				
				registro.setPorcentagemVendaExemplares(obterPercentualVendaExemplares(registro.getVendaExemplares(), registro.getReparte()));
				
			}
		}
	}
	
	private BigDecimal obterPercentualVendaExemplares(BigInteger vendaExemplares, BigInteger reparte) {
		
		vendaExemplares = (vendaExemplares != null) ? vendaExemplares : BigInteger.ZERO; 
		reparte = (reparte != null) ? reparte: BigInteger.ZERO;
		
		if(BigInteger.ZERO.compareTo(vendaExemplares)!=0 &&
				BigInteger.ZERO.compareTo(reparte)!=0) {
			return MathUtil.divide(CEM.multiply(new BigDecimal(vendaExemplares)), new BigDecimal(reparte));
		}
		
		return BigDecimal.ZERO;
		
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
				
				registro.setPorcentagemVenda(obterPercentualVendaExemplares(registro.getVendaExemplares(), registro.getReparte()));
				
				if(registro.getParticipacao() != null) {
			    	participacaoRegistro =
						registro.getParticipacao().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				  
				}
				 registro.setParticipacao(participacaoRegistro);
				if(registro.getParticipacaoAcumulada()!=null) {
					BigDecimal participacaoAcumulada = registro.getParticipacaoAcumulada();
					registro.setParticipacaoAcumulada(participacaoAcumulada.multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN));
				}
				
			}
		}

	}
	
	public void validarFiltroRegiao(FiltroCurvaABCDTO filtro){
		
		if(filtro.getRegiaoID() != null){
			filtro.setNumCotasDentroDaRegiao(registroCotaRegiaoRepository.buscarNumeroCotasPorIdRegiao(filtro.getRegiaoID()));
			
			if(filtro.getNumCotasDentroDaRegiao().isEmpty()){
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro foi encontrado"));
			}
			
		}
	}
	
}