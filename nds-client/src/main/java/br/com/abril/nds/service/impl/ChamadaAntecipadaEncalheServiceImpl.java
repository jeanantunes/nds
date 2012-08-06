package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuicaoFornecedorRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
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
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Transactional
	@Override
	public void cancelarChamadaAntecipadaCota(InfoChamdaAntecipadaEncalheDTO infoChamdaAntecipadaEncalheDTO){
		
		List<ChamadaAntecipadaEncalheDTO> chamadasEncalhe = infoChamdaAntecipadaEncalheDTO.getChamadasAntecipadaEncalhe();
		
		efetuarCancelamentoChamadaAntecipada(chamadasEncalhe);
	}
	
	@Override
	@Transactional
	public void cancelarChamadaAntecipadaCota(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		filtro.setDataOperacao(distribuidorRepository.obter().getDataOperacao());
		
		List<ChamadaAntecipadaEncalheDTO> chamadasEncalheCota = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
		
		efetuarCancelamentoChamadaAntecipada(chamadasEncalheCota);
	}
	
	private void efetuarCancelamentoChamadaAntecipada(List<ChamadaAntecipadaEncalheDTO> chamdadasAntecipada){
		
		if(!chamdadasAntecipada.isEmpty()){
			
			HashMap<Long,Long> chamadasEncalheASerRemovidas = new HashMap<Long, Long>();
			
			ChamadaEncalheCota chamadaEncalheCota = null;
			
			for(ChamadaAntecipadaEncalheDTO chEncalhe : chamdadasAntecipada){
				
				chamadaEncalheCota  = chamadaEncalheCotaRepository.buscarPorId(chEncalhe.getCodigoChamadaEncalhe());
				
				chamadaEncalheCotaRepository.remover(chamadaEncalheCota);
				
				chamadasEncalheASerRemovidas.put(chamadaEncalheCota.getChamadaEncalhe().getId(), 
												 chamadaEncalheCota.getChamadaEncalhe().getId());
			}
			
			if(!chamadasEncalheASerRemovidas.values().isEmpty()){
				
				removerChamadasAntecipadas(chamadasEncalheASerRemovidas.values().toArray(new Long[]{}));
			}
		}
	}
	
	/**
	 * Remove uma chamada encalhe caso a mesma não tenha mais nenhuma associação com chamdas encalhe cota.
	 * 
	 * @param idChamadaAntecipada - identificadores da chamada de encalhe
	 */
	private void removerChamadasAntecipadas(Long... idChamadaAntecipada){
		
		ChamadaEncalhe chamadaEncalhe = null;
		
		for(Long id :idChamadaAntecipada){
			
			Long qnt = chamadaEncalheCotaRepository.obterQntChamadaEncalheCota(id);
			
			if(qnt!= null && qnt == 0){
				
				chamadaEncalhe = chamadaEncalheRepository.buscarPorId(id);
				chamadaEncalhe.getLancamentos().clear();
				
				chamadaEncalheRepository.remover(chamadaEncalhe);
			}
		}
	}

	@Transactional
	@Override
	public void reprogramarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro){
		
		List<ChamadaAntecipadaEncalheDTO> lisAntecipadaEncalheDTOs = 
				chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro); 

		cancelarChamadaAntecipadaCota(filtro);
		
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
	public void reprogramarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO) {
		
		gravarChamadaAntecipacaoEncalheProduto(infoEncalheDTO);
		
		cancelarChamadaAntecipadaCota(infoEncalheDTO);
	}
	
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
	
		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					produtoEdicao,
					infoEncalheDTO.getDataAntecipacao(),
					TipoChamadaEncalhe.ANTECIPADA);
		
		if(chamadaEncalhe == null){
			chamadaEncalhe = new ChamadaEncalhe();
		}
		
		chamadaEncalhe.setLancamentos(obterLancamentoPorId(infoEncalheDTO.getChamadasAntecipadaEncalhe()));
		chamadaEncalhe.setDataRecolhimento(infoEncalheDTO.getDataAntecipacao());
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
		
		ChamadaEncalheCota chamadaEncalheCota = null;
		Cota cota = null;
		
		for(ChamadaAntecipadaEncalheDTO dto : infoEncalheDTO.getChamadasAntecipadaEncalhe()){
			
			cota  = cotaRepository.obterPorNumerDaCota(dto.getNumeroCota());
			chamadaEncalheCota = new ChamadaEncalheCota();
			chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
			chamadaEncalheCota.setFechado(Boolean.FALSE);
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
	@SuppressWarnings("unused")
	private boolean isDataRecolhimentoDistribuidor(Date dataAntecipacao, String codigoProduto, Long idProdutoEdicao) {
		
		DiaSemana diaSemana = DiaSemana.getByDate(dataAntecipacao);
		
		return  distribuicaoFornecedorRepository.verificarRecolhimentoDiaSemana(codigoProduto, idProdutoEdicao, diaSemana);
	}

	@Transactional(readOnly=true)
	@Override
	public InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO antecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		List<ChamadaAntecipadaEncalheDTO> list = null; 
		
		if(filtro.isProgramacaoCE()){
			
			filtro.setDataOperacao(distribuidorRepository.obter().getDataOperacao());
			list = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
			antecipadaEncalheDTO.setTotalRegistros(chamadaEncalheCotaRepository.obterQntCotasProgramadaParaAntecipacoEncalhe(filtro));
		}
		else{
			
			list = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
			antecipadaEncalheDTO.setTotalRegistros(cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro));
		}
		
		antecipadaEncalheDTO.setChamadasAntecipadaEncalhe(list);
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
		
		List<ChamadaAntecipadaEncalheDTO> list = null;
		
		if(filtro.isProgramacaoCE()){
			
			filtro.setDataOperacao(distribuidorRepository.obter().getDataOperacao());
			list = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
		}
		else{
			
			list = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		}
		
		antecipadaEncalheDTO.setTotalExemplares(sumarizarExemplares(list));
		antecipadaEncalheDTO.setTotalCotas(new BigDecimal(list.size()));
		
		return antecipadaEncalheDTO;
	}
	
	@Transactional(readOnly=true)
	@Override
	public BigInteger obterQntExemplaresCotasSujeitasAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		BigInteger qntPrevistaEncalhe = BigInteger.ZERO;
		
		qntPrevistaEncalhe = cotaRepository.obterQntExemplaresCotasSujeitasAntecipacoEncalhe(filtro);
		
		if(qntPrevistaEncalhe == null || (qntPrevistaEncalhe.compareTo(BigInteger.ZERO) <= 0)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Cota não possui exemplares em estoque para chamada antecipada de encalhe!");
		}
		
		return qntPrevistaEncalhe;
	}
	
	
	@Transactional(readOnly=true)
	@Override
	public BigDecimal obterQntExemplaresComProgramacaoAntecipadaEncalheCota(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		BigDecimal qntPrevistaEncalhe = BigDecimal.ZERO;
		
		filtro.setDataOperacao(distribuidorRepository.obter().getDataOperacao());
		qntPrevistaEncalhe = chamadaEncalheCotaRepository.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);
		
		if(qntPrevistaEncalhe == null || (qntPrevistaEncalhe.compareTo(BigDecimal.ZERO) <= 0)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Cota não possui programação de chamada antecipada de encalhe!");
		}
		
		return qntPrevistaEncalhe;
	}
	
	@Transactional(readOnly=true)
	@Override
	public ChamadaAntecipadaEncalheDTO obterChamadaEncalheAntecipada(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		filtro.setDataOperacao(distribuidorRepository.obter().getDataOperacao());
		
		List<ChamadaAntecipadaEncalheDTO> chamada = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
		
		if(chamada == null || chamada.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"Cota não possui programação de chamada antecipada de encalhe!");
		}
		
		return chamada.get(0);
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
	
	private Set<Lancamento> obterLancamentoPorId(List<ChamadaAntecipadaEncalheDTO> chamadasEncalhe){
		
		Set<Long> idsLancamento = new HashSet<Long>(); 
		
		for(ChamadaAntecipadaEncalheDTO chm : chamadasEncalhe){
			idsLancamento.add(chm.getIdLancamento());
		}
		
		Set<Lancamento> retorno = new HashSet<Lancamento>();
		retorno.addAll(lancamentoRepository.obterLancamentosPorIdOrdenados(idsLancamento));
		
		return retorno;
	}
	
}
