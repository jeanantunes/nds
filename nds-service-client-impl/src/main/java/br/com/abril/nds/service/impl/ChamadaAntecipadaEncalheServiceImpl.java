package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.InfoChamdaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
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
		
		if(!filtro.isRecolhimentoFinal()) {
			obterDataChamadaEncalheAntecipada(filtro);
		}
		
		validarSeExisteMatrizRecolhimento(filtro);
		
		filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
		List<ChamadaAntecipadaEncalheDTO> chamadasEncalheCota = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
		
		efetuarCancelamentoChamadaAntecipada(chamadasEncalheCota);
	}
	
	private void efetuarCancelamentoChamadaAntecipada(List<ChamadaAntecipadaEncalheDTO> chamdadasAntecipada){
		
		if(!chamdadasAntecipada.isEmpty()){
			
			Set<Long> chamadasEncalheASerRemovidas = new HashSet<Long>();
			
			ChamadaEncalheCota chamadaEncalheCota = null;
			
			for(ChamadaAntecipadaEncalheDTO chEncalhe : chamdadasAntecipada){
				
				chamadaEncalheCota  = chamadaEncalheCotaRepository.buscarPorId(chEncalhe.getCodigoChamadaEncalhe());
				
				chamadaEncalheCotaRepository.remover(chamadaEncalheCota);
				
				chamadasEncalheASerRemovidas.add(chamadaEncalheCota.getChamadaEncalhe().getId());
			}
			
			if(!chamadasEncalheASerRemovidas.isEmpty()){
				
				removerChamadasAntecipadas(chamadasEncalheASerRemovidas);
			}
		}
	}
	
	/**
	 * Remove uma chamada encalhe caso a mesma não tenha mais nenhuma associação com chamdas encalhe cota.
	 * 
	 * @param idChamadaAntecipada - identificadores da chamada de encalhe
	 */
	private void removerChamadasAntecipadas(Set<Long> idsChamadaAntecipada) {

		for(Long id :idsChamadaAntecipada){
			
			Long qnt = chamadaEncalheCotaRepository.obterQntChamadaEncalheCota(id);
			
			if(qnt!= null && qnt == 0){

				ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository.buscarPorId(id);
				
				for (Lancamento lancamento : chamadaEncalhe.getLancamentos()) {
					
					lancamento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoPrevista());
				
					this.lancamentoRepository.alterar(lancamento);
				}
				
				chamadaEncalhe.getLancamentos().clear();
				
				chamadaEncalheRepository.remover(chamadaEncalhe);
			}
		}
	}

	@Transactional
	@Override
	public void reprogramarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro){
		

		if(filtro.getDataAntecipacao().compareTo(this.distribuidorRepository.obterDataOperacaoDistribuidor()) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser maior que a data atual!");
		}
		
		if(!filtro.isRecolhimentoFinal()) {
			obterDataChamadaEncalheAntecipada(filtro);
		}
		
		validarSeExisteMatrizRecolhimento(filtro);
		
		List<ChamadaAntecipadaEncalheDTO> lisAntecipadaEncalheDTOs = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro); 

		cancelarChamadaAntecipadaCota(filtro);
		
		InfoChamdaAntecipadaEncalheDTO infoEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		infoEncalheDTO.setChamadasAntecipadaEncalhe(lisAntecipadaEncalheDTOs);
		infoEncalheDTO.setCodigoProduto(filtro.getCodigoProduto());
		infoEncalheDTO.setDataAntecipacao(filtro.getDataAntecipacao());
		infoEncalheDTO.setNumeroEdicao(filtro.getNumeroEdicao());
		infoEncalheDTO.setDataProgramada(DateUtil.parseDataPTBR(filtro.getDataProgramada()));
		infoEncalheDTO.setRecolhimentoFinal(filtro.isRecolhimentoFinal());
		gravar(infoEncalheDTO);
		
	}
	
	@Transactional
	@Override
	public void reprogramarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO) {
		
		if(infoEncalheDTO.getDataAntecipacao().compareTo(this.distribuidorRepository.obterDataOperacaoDistribuidor()) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser maior que a data atual!");
		}
		
		validarSeExisteMatrizRecolhimento(infoEncalheDTO);
		
		// cancelarChamadaAntecipadaCota(infoEncalheDTO);
		
		gravarChamadaAntecipacaoEncalheProduto(infoEncalheDTO);
		
	}
	
	@Transactional
	@Override
	public void gravarChamadaAntecipacaoEncalheProduto(FiltroChamadaAntecipadaEncalheDTO filtro){
		
		validarSeExisteMatrizRecolhimento(filtro);
		
		List<ChamadaAntecipadaEncalheDTO> lisAntecipadaEncalheDTOs = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
		
		if(filtro.getChamadasNaoSelecionadas() != null ){			
			lisAntecipadaEncalheDTOs.removeAll(filtro.getChamadasNaoSelecionadas());
		}
		
		
		InfoChamdaAntecipadaEncalheDTO infoEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		infoEncalheDTO.setChamadasAntecipadaEncalhe(lisAntecipadaEncalheDTOs);
		infoEncalheDTO.setCodigoProduto(filtro.getCodigoProduto());
		infoEncalheDTO.setDataAntecipacao(filtro.getDataAntecipacao());
		infoEncalheDTO.setNumeroEdicao(filtro.getNumeroEdicao());
		infoEncalheDTO.setDataProgramada(DateUtil.parseDataPTBR(filtro.getDataProgramada()));
		infoEncalheDTO.setRecolhimentoFinal(filtro.isRecolhimentoFinal());
		
		gravar(infoEncalheDTO);
	}
	
	@Transactional
	@Override
	public void gravarChamadaAntecipacaoEncalheProduto(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO){
		
		validarSeExisteMatrizRecolhimento(infoEncalheDTO);
		
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
		
		if(infoEncalheDTO.getChamadasAntecipadaEncalhe() == null || infoEncalheDTO.getChamadasAntecipadaEncalhe().isEmpty() ){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Não foi informado cotas para antecipação de recolhimento de encalhe");
		}
		
		Date dataAntecipacao = infoEncalheDTO.getDataAntecipacao();
		
		if(dataAntecipacao.compareTo(this.distribuidorRepository.obterDataOperacaoDistribuidor()) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser maior que a data atual!");
		}
		
		if(dataAntecipacao.compareTo(infoEncalheDTO.getDataProgramada()) >= 0){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Data Antecipada deve ser menor que a Data Programada!");
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository
				.obterProdutoEdicaoPorCodProdutoNumEdicao(infoEncalheDTO.getCodigoProduto(), 
														  infoEncalheDTO.getNumeroEdicao());
	
		ChamadaEncalhe chamadaEncalhe = 
				chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(
					produtoEdicao,
					dataAntecipacao,
					TipoChamadaEncalhe.ANTECIPADA);
		
		if(chamadaEncalhe == null){
		
			Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataAntecipacao);
			
			chamadaEncalhe = new ChamadaEncalhe();
			
			chamadaEncalhe.setSequencia(++sequencia);
		}
		
		Set<Lancamento> lancamentos = obterLancamentoPorId(infoEncalheDTO.getChamadasAntecipadaEncalhe());
		
		if (infoEncalheDTO.isRecolhimentoFinal()) {
			
			for (Lancamento lancamento : lancamentos) {
				
				lancamento.setDataRecolhimentoDistribuidor(dataAntecipacao);
				lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
				this.lancamentoRepository.alterar(lancamento);
				
			}
		}
		
		chamadaEncalhe.setLancamentos(lancamentos);
		chamadaEncalhe.setDataRecolhimento(dataAntecipacao);
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.ANTECIPADA);
		
		chamadaEncalhe = chamadaEncalheRepository.merge(chamadaEncalhe);
		
		ChamadaEncalheCota chamadaEncalheCota = null;
		Cota cota = null;
		
		for(ChamadaAntecipadaEncalheDTO dto : infoEncalheDTO.getChamadasAntecipadaEncalhe()){
			
			if(BigInteger.ZERO.compareTo(dto.getQntExemplares()) >= 0 ) {
				continue;
			}
			
			cota  = cotaRepository.obterPorNumeroDaCota(dto.getNumeroCota());
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
		
		Date dataDistribuidor = this.distribuidorRepository.obterDataOperacaoDistribuidor();
		
		filtro.setDataOperacao(dataDistribuidor);

		if(filtro.isProgramacaoCE()){
			
			list = chamadaEncalheCotaRepository.obterCotasProgramadaParaAntecipacoEncalhe(filtro);
			antecipadaEncalheDTO.setTotalRegistros(chamadaEncalheCotaRepository.obterQntCotasProgramadaParaAntecipacoEncalhe(filtro));

			if(list != null && !list.isEmpty()) {
				
				ChamadaAntecipadaEncalheDTO chamadaAntecipada = list.iterator().next();
				
				antecipadaEncalheDTO.setRecolhimentoFinal(chamadaAntecipada.getDataRecolhimento().equals(chamadaAntecipada.getDataRecolhimentoDistribuidor())); 
			}
			
		} else{
			
			list = cotaRepository.obterCotasSujeitasAntecipacoEncalhe(filtro);
			antecipadaEncalheDTO.setTotalRegistros(cotaRepository.obterQntCotasSujeitasAntecipacoEncalhe(filtro));
		}
		
		if(list != null && !list.isEmpty()) {
			
			ChamadaAntecipadaEncalheDTO chamadaAntecipada = list.iterator().next();
			 
			if (chamadaAntecipada.getDataRecolhimentoPrevista() != null) {
				antecipadaEncalheDTO.setTipoChamadaEncalhe(chamadaAntecipada.getTipoChamadaEncalhe());
				antecipadaEncalheDTO.setDataRecolhimentoPrevista(chamadaAntecipada.getDataRecolhimentoPrevista());
			} 
		}
		
		
		antecipadaEncalheDTO.setChamadasAntecipadaEncalhe(list);
		antecipadaEncalheDTO.setTotalExemplares(sumarizarExemplares(list));
		antecipadaEncalheDTO.setTotalCotas(new BigDecimal(list.size()));
		
		return antecipadaEncalheDTO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Date obterDataRecolhimentoReal(String codigoProduto, Long numeroEdicao){

		codigoProduto = StringUtils.leftPad(codigoProduto, 8, '0');

		 return lancamentoRepository.obterDataRecolhimentoDistribuidor(codigoProduto, numeroEdicao);
	}
	
	@Transactional(readOnly=true)
	@Override
	public InfoChamdaAntecipadaEncalheDTO obterInfoChamdaAntecipadaEncalheSumarizado(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		InfoChamdaAntecipadaEncalheDTO antecipadaEncalheDTO = new InfoChamdaAntecipadaEncalheDTO();
		
		List<ChamadaAntecipadaEncalheDTO> list = null;
		
		if(filtro.isProgramacaoCE()){
			
			filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
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

		filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
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
		
		filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		qntPrevistaEncalhe = chamadaEncalheCotaRepository.obterQntExemplaresComProgramacaoAntecipadaEncalheCota(filtro);
		
		if(qntPrevistaEncalhe == null || (qntPrevistaEncalhe.compareTo(BigDecimal.ZERO) <= 0)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Cota não possui programação de chamada antecipada de encalhe!");
		}
		
		return qntPrevistaEncalhe;
	}
	
	@Transactional(readOnly=true)
	@Override
	public ChamadaAntecipadaEncalheDTO obterChamadaEncalheAntecipada(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		filtro.setDataOperacao(this.distribuidorRepository.obterDataOperacaoDistribuidor());
		
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
	
	@Transactional
	public Date obterProximaDataEncalhe(Date base) {
		return chamadaEncalheRepository.obterProximaDataEncalhe(base);
	}

	private void obterDataChamadaEncalheAntecipada(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(filtro.getCodigoProduto(), filtro.getNumeroEdicao());
		
		ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository.obterPorEdicaoTipoChamadaEncalhe(produtoEdicao, TipoChamadaEncalhe.ANTECIPADA);

		if(chamadaEncalhe != null){
			
			filtro.setDataAntecipacao(chamadaEncalhe.getDataRecolhimento());
			
		}
	}
	
	
	private void validarSeExisteMatrizRecolhimento(FiltroChamadaAntecipadaEncalheDTO filtro) {
		
		ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(null, filtro.getDataAntecipacao(), TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		if(chamadaEncalhe != null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi possivel realizar a antecipação. Pois existe matriz recolhimento confirmada.");
		}
	}
	
	private void validarSeExisteMatrizRecolhimento(InfoChamdaAntecipadaEncalheDTO infoEncalheDTO) {
		
		ChamadaEncalhe chamadaEncalhe = chamadaEncalheRepository.obterPorNumeroEdicaoEDataRecolhimento(null, infoEncalheDTO.getDataAntecipacao(), TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);

		if(chamadaEncalhe != null){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não foi possivel realizar a antecipação. Pois existe matriz recolhimento confirmada.");
		}
	}
}