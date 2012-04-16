package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.InfoChamdaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ChamadaAntecipadaEncalheService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;

/**
 * Classe de implementação referentes a serviços de chamada antecipada de encalhe. 
 *   
 * @author Discover Technology
 */

@Service
public class ChamadaAntecipadaEncalheServiceImpl implements ChamadaAntecipadaEncalheService {
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuicaoFornecedorRepository distribuicaoFornecedorRepository;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private ChamadaEncalheRepository chamadaEncalheRepository;
	
	@Transactional
	@Override
	public void gravarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro){
		
		List<ChamadaAntecipadaEncalheDTO> lisAntecipadaEncalheDTOs = 
				cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		
		InfoChamdaAntecipadaEncalheDTO infoEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		infoEncalheDTO.setChamadasAntecipadaEncalhe(lisAntecipadaEncalheDTOs);
		infoEncalheDTO.setCodigoProduto(filtro.getCodigoProduto());
		infoEncalheDTO.setDataAntecipacao(filtro.getDataAntecipacao());
		infoEncalheDTO.setNumeroEdicao(filtro.getNumeroEdicao());
		infoEncalheDTO.setDataProgramada(DateUtil.parseDataPTBR(filtro.getDataProgramada()));
		
		gravar(infoEncalheDTO);
	}
	
	@Transactional
	@Override
	public void gravarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO){
		
		gravar(infoEncalheDTO);
	}
	
	/**
	 * Grava as informações da chamada de encalhe antecipada das cotas informadas.
	 * 
	 * @param infoEncalheDTO
	 */
	private void gravar(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO ){
		
		if(infoEncalheDTO == null){
			throw new IllegalArgumentException("Parâmetros inválido para gravar chamada antecipada de encalhe");
		}
		
		if(infoEncalheDTO.getChamadasAntecipadaEncalhe() == null 
				|| infoEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty() ){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foi informado cotas para antecipação de recolhimento de encalhe");
		}
		
		if(infoEncalheDTO.getDataAntecipacao().compareTo(new Date()) <= 0){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser maior que a data atual!");
		}
		
		if(infoEncalheDTO.getDataAntecipacao().compareTo(infoEncalheDTO.getDataProgramada()) >= 0){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser menor que a Data Programada!");
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.obterProdutoEdicaoPorCodProdutoNumEdicao(infoEncalheDTO.getCodigoProduto(), 
														  infoEncalheDTO.getNumeroEdicao());
		
		if(!isDataRecolhimentoDistribuidor(infoEncalheDTO.getDataAntecipacao(),
										   infoEncalheDTO.getCodigoProduto(),
										   produtoEdicao.getId())){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Distribuidor não ");
		}
		
		
		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(produtoEdicao,infoEncalheDTO.getDataAntecipacao());
		
		if(chamadaEncalhe == null){
			chamadaEncalhe = new ChamadaEncalhe();
		}
		
		chamadaEncalhe.setFinalRecolhimento(infoEncalheDTO.getDataAntecipacao());
		chamadaEncalhe.setInicioRecolhimento(infoEncalheDTO.getDataAntecipacao());
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
		
		ChamadaEncalheCota chamadaEncalheCota = null;
		Cota cota = null;
		
		for(ChamadaAntecipadaEncalheDTO dto : infoEncalheDTO.getChamadasAntecipadaEncalhe()){
			
			cota  = cotaRepository.obterPorNumerDaCota(dto.getNumeroCota());
			chamadaEncalheCota = new ChamadaEncalheCota();
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
			chamadaEncalheCota.setConferido(Boolean.FALSE);
			chamadaEncalheCota.setCota(cota);
			chamadaEncalheCota.setQtdePrevista(dto.getQntExemplares());
			
			chamadaEncalheCotaRepository.adicionar(chamadaEncalheCota);
		}
	}
	
	/**
	 * Verifica se o distribuidor recolhe encalhe no dia da semana da data informada.
	 * 
	 * @param dataAntecipacao - data de antecipação para recolhimento do encalhe antecipado
	 * @param codigoProduto - código do produto
	 * @param idProdutoEdicao - identificador do produto edição
	 * 
	 * @return boolean 
	 */
	private boolean isDataRecolhimentoDistribuidor(Date dataAntecipacao, String codigoProduto, Long idProdutoEdicao) {
		
		DiaSemana diaSemana = DiaSemana.getByDate(dataAntecipacao);
		
		return  distribuicaoFornecedorRepository.verificarRecolhimentoDiaSemana(codigoProduto, idProdutoEdicao, diaSemana);
	}

	@Transactional(readOnly=true)
	@Override
	public InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO antecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		List<ChamadaAntecipadaEncalheDTO> list = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		
		antecipadaEncalheDTO.setChamadasAntecipadaEncalhe(list);
		
		antecipadaEncalheDTO.setTotalRegistros(cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro));
		
		antecipadaEncalheDTO.setTotalExemplares(sumarizarExemplares(list));
		antecipadaEncalheDTO.setTotalCotas(new BigDecimal(list.size()));
		
		return antecipadaEncalheDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Date obterDataRecolhimentoPrevista(String codigoProduto, Long numeroEdicao){
		 
		 return lancamentoRepository.obterDataRecolhimentoPrevista(codigoProduto, numeroEdicao);
	}
	
	@Transactional(readOnly=true)
	@Override
	public InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalheSumarizado(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO antecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		List<ChamadaAntecipadaEncalheDTO> list = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		
		antecipadaEncalheDTO.setTotalExemplares(sumarizarExemplares(list));
		antecipadaEncalheDTO.setTotalCotas(new BigDecimal(list.size()));
		
		return antecipadaEncalheDTO;
	}
	
	@Transactional(readOnly=true)
	@Override
	public BigDecimal obterQntExemplaresCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		return cotaRepository.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);
	}
	
	/**
	 * 
	 * Retorna os exemplares sumarizados
	 * 
	 * @param list - lista de ChamadaAntecipadaEncalheDTO
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal sumarizarExemplares(List<ChamadaAntecipadaEncalheDTO> list){
		
		Integer valorExemplares = 0;
		
		for(ChamadaAntecipadaEncalheDTO dto : list){
			
			valorExemplares += dto.getQntExemplares().intValue();
		}
		
		return new BigDecimal(valorExemplares);
	}
	
}
