package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.ProdutoRepository;
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
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		Map<Long, RegistroCurvaABCDistribuidorVO> sumarizador = 
				new HashMap<Long, RegistroCurvaABCDistribuidorVO>();
		
		if(!lista.isEmpty()){
			
			Map<Long, Long> mapRankingCota =
					this.rankingRepository.obterRankingCota();
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				
				if (!sumarizador.containsKey(dto.getIdCota())){
					
					sumarizador.put(dto.getIdCota(), dto);
					dto.setRkCota(mapRankingCota.get(dto.getIdCota()));
					dto.setQuantidadePdvs(this.pdvRepository.obterQntPDV(dto.getIdCota(), null));
					
				} else {
					
					RegistroCurvaABCDistribuidorVO registro = sumarizador.get(dto.getIdCota());
					registro.setFaturamentoCapa(this.adicionarValor(registro.getFaturamentoCapa(), dto.getFaturamentoCapa()));
					
					registro.setVendaExemplares(
						registro.getVendaExemplares().add(dto.getVendaExemplares())
					);
				}
			}
		}
	
		return this.obterParticipacaoCurvaABCDistribuidor(sumarizador.values());
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		
		List<RegistroCurvaABCEditorVO> lista = 
			this.relatorioVendasRepository.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
		
		Map<Long, RegistroCurvaABCEditorVO> sumarizador = 
				new HashMap<Long, RegistroCurvaABCEditorVO>();
		
		if(!lista.isEmpty()){
			
			Map<Long, Long> mapRankingEditor =
					this.rankingRepository.obterRankingEditor();
			
			for(RegistroCurvaABCEditorVO dto : lista){
				
				if (!sumarizador.containsKey(dto.getCodigoEditor())){
					
					sumarizador.put(dto.getCodigoEditor(), dto);
					dto.setRkEditor(mapRankingEditor.get(dto.getCodigoEditor()));
					
				} else {
					
					RegistroCurvaABCEditorVO registro = sumarizador.get(dto.getCodigoEditor());
					
					registro.setReparte(this.adicionarValor(registro.getReparte(), dto.getReparte()));
					registro.setVendaExemplares(this.adicionarValor(registro.getVendaExemplares(), dto.getVendaExemplares()));
					//registro.setPorcentagemVendaExemplares(this.adicionarValor(registro.getPorcentagemVendaExemplares(), dto.getPorcentagemVendaExemplares()));
					registro.setFaturamentoCapa(this.adicionarValor(registro.getFaturamentoCapa(), dto.getFaturamentoCapa()));
					registro.setValorMargemDistribuidor(this.adicionarValor(registro.getValorMargemDistribuidor(), dto.getValorMargemDistribuidor()));
					//registro.setPorcentagemMargemDistribuidor(this.adicionarValor(registro.getPorcentagemMargemDistribuidor(), dto.getPorcentagemMargemDistribuidor()));
				}
			}
		}
		
		return this.complementarCurvaABCEditor(sumarizador.values(), filtroCurvaABCEditorDTO);
	}
	
	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCProduto(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista =
			this.relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		Map<Long, RegistroCurvaABCDistribuidorVO> sumarizador = 
				new HashMap<Long, RegistroCurvaABCDistribuidorVO>();
		
		if(!lista.isEmpty()){
			
			Produto produto =
					this.produtoRepository.obterProdutoPorCodigo(filtroCurvaABCDistribuidorDTO.getCodigoProduto());
			
			Map<Long, Long> mapRankingProdutoPorProduto =
					this.rankingRepository.obterRankingProdutoPorProduto();
				
				Map<Long, Long> mapRankingCotaPorProduto =
					this.rankingRepository.obterRankingCotaPorProduto(produto.getId());
			
			for(RegistroCurvaABCDistribuidorVO dto : lista){
				
				if (!sumarizador.containsKey(dto.getIdCota())){
					
					sumarizador.put(dto.getIdCota(), dto);
					
					dto.setRkProduto(mapRankingProdutoPorProduto.get(dto.getIdProduto()));
					dto.setRkCota(mapRankingCotaPorProduto.get(dto.getIdCota()));
					dto.setQuantidadePdvs(this.pdvRepository.obterQntPDV(dto.getIdCota(), null));
					
				} else {
					
					RegistroCurvaABCDistribuidorVO registro = sumarizador.get(dto.getIdCota());
					registro.setFaturamentoCapa(this.adicionarValor(registro.getFaturamentoCapa(), dto.getFaturamentoCapa()));
					registro.setVendaExemplares(this.adicionarValor(registro.getVendaExemplares(), dto.getVendaExemplares()));
				}
			}
		}
	
		return this.obterParticipacaoCurvaABCDistribuidor(lista);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<RegistroCurvaABCCotaDTO> obterCurvaABCCota(FiltroCurvaABCCotaDTO filtroCurvaABCCotaDTO) {
		
		List<RegistroCurvaABCCotaDTO> lista =
			this.relatorioVendasRepository.obterCurvaABCCota(filtroCurvaABCCotaDTO);
		
		Map<Long, RegistroCurvaABCCotaDTO> sumarizador = 
				new HashMap<Long, RegistroCurvaABCCotaDTO>();
		
		if(!lista.isEmpty()){
			
			Cota cota = this.cotaRepository.obterPorNumerDaCota(filtroCurvaABCCotaDTO.getCodigoCota());
			
			Map<Long, Long> mapRanking =
				this.rankingRepository.obterRankingProdutoPorCota(cota.getId());
			
			for(RegistroCurvaABCCotaDTO dto : lista){
				
				if (!sumarizador.containsKey(dto.getIdProdutoEdicao())){
					
					sumarizador.put(dto.getIdProdutoEdicao(), dto);
					
					dto.setRkProduto(mapRanking.get(dto.getIdProdutoEdicao()));
				} else {
					
					RegistroCurvaABCCotaDTO registro = sumarizador.get(dto.getIdProdutoEdicao());
					
					registro.setReparte(this.adicionarValor(registro.getReparte(), dto.getReparte()));
					registro.setVendaExemplares(this.adicionarValor(registro.getVendaExemplares(), dto.getVendaExemplares()));
					//registro.setPorcentagemVenda(this.adicionarValor(registro.getPorcentagemVenda(), dto.getPorcentagemVenda()));
					registro.setFaturamento(this.adicionarValor(registro.getFaturamento(), dto.getFaturamento()));
				}
			}
		}
		
		return this.obterParticipacaoCurvaABCCota(sumarizador.values());
	}
	
	/**
	 * Obtéma a porcentagem de participação e participação acumulada no resultado da consulta.
	 */
	private List<RegistroCurvaABCDistribuidorVO> obterParticipacaoCurvaABCDistribuidor(Collection<RegistroCurvaABCDistribuidorVO> lista) {

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
		
		// Partipacao do registro em relacao a participacao total no periodo
		if (participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			//Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCDistribuidorVO registro : lista) {
				
				participacaoRegistro = 
						registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				registro.setParticipacao(participacaoRegistro);
			}
		}

		return new ArrayList<>(lista);
	}
	
	/**
	 * Insere os registros de participação e participação acumulada no resultado da consulta HQL
	 * @param lista
	 * @return
	 */
	private List<RegistroCurvaABCEditorVO> complementarCurvaABCEditor(Collection<RegistroCurvaABCEditorVO> lista, FiltroCurvaABCEditorDTO filtro) {

		BigDecimal participacaoTotal = BigDecimal.ZERO;

		// Soma todos os valores de participacao
		for (RegistroCurvaABCEditorVO registro : lista) {
			if (registro.getFaturamentoCapa()!=null) {
				participacaoTotal = participacaoTotal.add(registro.getFaturamentoCapa());
			}
		}

		BigDecimal participacaoRegistro = BigDecimal.ZERO;
		//BigDecimal participacaoAcumulada = BigDecimal.ZERO;

		// Partipacao do registro em relacao a participacao total no periodo
		if ( participacaoTotal.compareTo(BigDecimal.ZERO) != 0) {
		
			// Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCEditorVO registro : lista) {
				
				participacaoRegistro = registro.getFaturamentoCapa().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				
				registro.setParticipacao(participacaoRegistro);
				
				//participacaoAcumulada = participacaoAcumulada.add(participacaoRegistro);
				
				//registro.setParticipacaoAcumulada(participacaoAcumulada);
				registro.setDataDe(filtro.getDataDe());
				registro.setDataAte(filtro.getDataAte());
				
				if (registro.getFaturamentoCapa().compareTo(BigDecimal.ZERO) != 0){
				
					registro.setPorcentagemMargemDistribuidor(
						registro.getValorMargemDistribuidor().divide(
							registro.getFaturamentoCapa(), RoundingMode.HALF_EVEN));
				
				} else {
					
					registro.setPorcentagemMargemDistribuidor(BigDecimal.ZERO);
				}
				
				registro.setPorcentagemVendaExemplares(
					MathUtil.divide(CEM.multiply(new BigDecimal(registro.getVendaExemplares())), 
							new BigDecimal(registro.getReparte())));
			}
		}

		return new ArrayList<>(lista);
	}
	
	private List<RegistroCurvaABCCotaDTO> obterParticipacaoCurvaABCCota(Collection<RegistroCurvaABCCotaDTO> lista) {

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
		
		// Partipacao do registro em relacao a participacao total no periodo
		if ( participacaoTotal.compareTo(BigDecimal.ZERO) != 0){
		
			// Verifica o percentual dos valores em relação ao total de participacao
			for (RegistroCurvaABCCotaDTO registro : lista) {
				
				participacaoRegistro =
						registro.getFaturamento().multiply(CEM).divide(participacaoTotal, RoundingMode.HALF_EVEN);
				
				registro.setParticipacao(participacaoRegistro);
				
				registro.setPorcentagemVenda(
						MathUtil.divide(CEM.multiply(new BigDecimal(registro.getVendaExemplares())), 
								new BigDecimal(registro.getReparte())));
			}
		}

		return new ArrayList<>(lista);
	}
	
	private BigDecimal adicionarValor(BigDecimal base, BigDecimal add){
		
		if (add == null){
			add = BigDecimal.ZERO;
		}
		
		if (base == null){
			return add;
		}
		
		return base.add(add);
	}
	
	private BigInteger adicionarValor(BigInteger base, BigInteger add){
		
		if (add == null){
			add = BigInteger.ZERO;
		}
		
		if (base == null){
			return add;
		}
		
		return base.add(add);
	}
}