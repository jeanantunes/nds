package br.com.abril.nds.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.CotaReparteDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.RecolhimentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.factory.devolucao.BalanceamentoRecolhimentoFactory;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ChamadaEncalheRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuicaoFornecedorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.strategy.devolucao.BalanceamentoRecolhimentoStrategy;
import br.com.abril.nds.util.BigDecimalUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.SemanaUtil;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Implementação de serviços referentes ao recolhimento.
 * 
 * @author Discover Technology
 * 
 */
@Service
public class RecolhimentoServiceImpl implements RecolhimentoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RecolhimentoServiceImpl.class);

	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected ChamadaEncalheRepository chamadaEncalheRepository;
		
	@Autowired
	protected ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	protected ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	protected MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	private DistribuicaoFornecedorService distribuicaoFornecedorService;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	LinkedList<Date> dinap = new LinkedList<Date>();
	LinkedList<Date> fc = new LinkedList<Date>();
	LinkedList<Date> dinapFC = new LinkedList<Date>();
	
	
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>>  copiarMatriz(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizOriginal){
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizCopia = new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		  
		  try {

		    //for (int i = 0; i < matrizOriginal.size(); i++) {
		    for(Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizOriginal.entrySet()){
		    
		     List<ProdutoRecolhimentoDTO> lista = new ArrayList<>();	
		     
		     lista.addAll(entry.getValue());
		    	
			 BeanUtils.copyProperties(lista, matrizOriginal.get(entry.getKey()));
			 
			 matrizCopia.put(entry.getKey(), lista);
				
			}
		  } catch (IllegalAccessException e) {
			  
			  LOGGER.error("", e);
		  } catch (InvocationTargetException e) {
			  
			  LOGGER.error("", e);
		  }
		  
		  return matrizCopia;
		
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer anoNumeroSemana,
			List<Long> listaIdsFornecedores,
			TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
			boolean forcarBalanceamento) {

		RecolhimentoDTO dadosRecolhimento = this.obterDadosRecolhimento(anoNumeroSemana, listaIdsFornecedores, tipoBalanceamentoRecolhimento, forcarBalanceamento);

		BalanceamentoRecolhimentoStrategy balanceamentoRecolhimentoStrategy = BalanceamentoRecolhimentoFactory.getStrategy(tipoBalanceamentoRecolhimento);

		BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoDTO = balanceamentoRecolhimentoStrategy.balancear(dadosRecolhimento);

		if(balanceamentoRecolhimentoDTO != null && balanceamentoRecolhimentoDTO.getMatrizRecolhimento() != null) {

			TreeMap<Date, List<ProdutoRecolhimentoDTO>> copiaMatriz = copiarMatriz(balanceamentoRecolhimentoDTO.getMatrizRecolhimento());

			Date dataMatriz;
			List <ProdutoRecolhimentoDTO> listaProdutos;

			for(Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimentoDTO.getMatrizRecolhimento().entrySet()){

				dataMatriz = entry.getKey();
				listaProdutos = entry.getValue();

				if(listaProdutos != null && !listaProdutos.isEmpty()) {

					for(ProdutoRecolhimentoDTO prDTO : listaProdutos) {

						/*
						if(prDTO!=null && dataMatriz!=null && prDTO.getDataRecolhimentoPrevista().after(dataMatriz) 
								&& prDTO.getStatusLancamento().compareTo(StatusLancamento.EXPEDIDO)==0
								){
							//remove e insere no dia certo 
		
							if(copiaMatriz.containsKey(prDTO.getDataRecolhimentoPrevista())){
		
								copiaMatriz.get(dataMatriz).remove(prDTO);
								prDTO.setNovaData(prDTO.getDataRecolhimentoPrevista());
								copiaMatriz.get(prDTO.getDataRecolhimentoPrevista()).add(prDTO);
							}
						}*/

					}
				}

			}
			balanceamentoRecolhimentoDTO.setMatrizRecolhimento(copiaMatriz);
		}

		return balanceamentoRecolhimentoDTO;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void salvarBalanceamentoRecolhimento(Usuario usuario, BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoDTO, StatusLancamento statusLancamento, Date dataPesquisa) {
		
		Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento = balanceamentoRecolhimentoDTO.getMatrizRecolhimento();
		
		if (matrizRecolhimento == null || matrizRecolhimento.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaRecolhimentos = new TreeMap<Long, ProdutoRecolhimentoDTO>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			//if(dataPesquisa==null || dataPesquisa.equals(entry.getKey())){
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
			
			if (listaProdutoRecolhimentoDTO == null || listaProdutoRecolhimentoDTO.isEmpty()) {
				continue;
			}
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutoRecolhimentoDTO) {
			
				Date novaDataRecolhimento = produtoRecolhimento.getNovaData();
				
				if (produtoRecolhimento.isBalanceamentoConfirmado()) {
					
					continue;
				}
				
				if (this.isProdutoAgrupado(produtoRecolhimento)) {
					
					continue;
				}
				
				this.montarInformacoesSalvarBalanceamento(mapaRecolhimentos, idsLancamento, produtoRecolhimento);
				
				if (!produtoRecolhimento.getIdsLancamentosAgrupados().isEmpty()) {
					
					for (Long idLancamento : produtoRecolhimento.getIdsLancamentosAgrupados()) {
						
						ProdutoRecolhimentoDTO produtoRecolhimentoAgrupado = this.obterProdutoRecolhimento(balanceamentoRecolhimentoDTO.getProdutosRecolhimentoAgrupados(), idLancamento);
						
						produtoRecolhimentoAgrupado.setNovaData(novaDataRecolhimento);
						
						this.montarInformacoesSalvarBalanceamento(mapaRecolhimentos, idsLancamento, produtoRecolhimentoAgrupado);
					}
				}
			//}
			}
		}
		
		this.atualizarLancamentos(idsLancamento, usuario, mapaRecolhimentos, statusLancamento, null);
	}

	private void montarInformacoesSalvarBalanceamento(
									Map<Long, ProdutoRecolhimentoDTO> mapaRecolhimentos,
									Set<Long> idsLancamento,
									ProdutoRecolhimentoDTO produtoRecolhimento) {
		
		mapaRecolhimentos.put(produtoRecolhimento.getIdLancamento(), produtoRecolhimento);
		
		idsLancamento.add(produtoRecolhimento.getIdLancamento());
	}
	
	private boolean isProdutoAgrupado(ProdutoRecolhimentoDTO produtoRecolhimento) {
		
		return (produtoRecolhimento.isProdutoAgrupado() && produtoRecolhimento.getIdsLancamentosAgrupados().isEmpty());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public TreeMap<Date, List<ProdutoRecolhimentoDTO>> confirmarBalanceamentoRecolhimento(
											Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											Integer numeroSemana,
											List<Date> datasConfirmadas,
											Usuario usuario,
											List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados) {
		
		if (matrizRecolhimento == null || matrizRecolhimento.isEmpty()) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de recolhimento não informada!");
		}
		
		Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento = new TreeMap<Long, ProdutoRecolhimentoDTO>();
		
		Set<Long> idsLancamento = new TreeSet<Long>();
		
		Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos = new TreeMap<Date, Set<Long>>();
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada = new TreeMap<Date, List<ProdutoRecolhimentoDTO>>();
		
		for (Date dataConfirmada : datasConfirmadas) {
			
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = matrizRecolhimento.get(dataConfirmada);
			
			if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
			
				continue;
			}
			
			this.ordenarProdutosRecolhimentoPorNome(produtosRecolhimento);
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if (this.isProdutoAgrupado(produtoRecolhimento)) {
					
					continue;
				}
				
				Date novaDataRecolhimento = produtoRecolhimento.getNovaData();
				
				if (produtoRecolhimento.isBalanceamentoConfirmado()) {
					
					this.montarMatrizRecolhimentosConfirmados(matrizConfirmada, produtoRecolhimento, null, novaDataRecolhimento);
					
					continue;
				}
				
				this.montarInformacoesConfirmarBalanceamento(mapaLancamentoRecolhimento, mapaDataRecolhimentoLancamentos, idsLancamento, produtoRecolhimento, novaDataRecolhimento);
				
				List<Long> idsLancamentosAgrupados = produtoRecolhimento.getIdsLancamentosAgrupados();
			
				if (!idsLancamentosAgrupados.isEmpty()) {
					
					for (Long idLancamento : idsLancamentosAgrupados) {
							
						ProdutoRecolhimentoDTO produtoRecolhimentoAgrupado = this.obterProdutoRecolhimento(produtosRecolhimentoAgrupados, idLancamento);
						
						produtoRecolhimentoAgrupado.setNovaData(novaDataRecolhimento);
						
						this.montarInformacoesConfirmarBalanceamento(mapaLancamentoRecolhimento, mapaDataRecolhimentoLancamentos, idsLancamento, produtoRecolhimentoAgrupado, novaDataRecolhimento);
					}
				}
			}
		}
		
		this.atualizarLancamentos(idsLancamento, usuario, mapaLancamentoRecolhimento, StatusLancamento.BALANCEADO_RECOLHIMENTO, matrizConfirmada);
		
		this.gerarChamadasEncalhe(mapaDataRecolhimentoLancamentos, numeroSemana, usuario);
		
		return matrizConfirmada;
	}

	private void montarInformacoesConfirmarBalanceamento(
									Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento,
									Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos,
									Set<Long> idsLancamento,
									ProdutoRecolhimentoDTO produtoRecolhimento,
									Date novaDataRecolhimento) {

		Long idLancamento = produtoRecolhimento.getIdLancamento();

        // Monta Map e Set para controlar a atualização dos lançamentos
		
		mapaLancamentoRecolhimento.put(idLancamento, produtoRecolhimento);
		
		idsLancamento.add(idLancamento);
		
        // Monta Map para controlar a geração de chamada de encalhe
		
		Set<Long> idsLancamentoPorData = mapaDataRecolhimentoLancamentos.get(novaDataRecolhimento);
		
		if (idsLancamentoPorData == null) {
			
			idsLancamentoPorData = new LinkedHashSet<Long>();
		}
		
		idsLancamentoPorData.add(idLancamento);
		
		mapaDataRecolhimentoLancamentos.put(novaDataRecolhimento, idsLancamentoPorData);
	}
	
	@SuppressWarnings("unchecked")
	private void ordenarProdutosRecolhimentoPorNome(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("nomeProduto"));
		comparatorChain.addComparator(new BeanComparator("numeroEdicao"));
		
		Collections.sort(produtosRecolhimento, comparatorChain);
	}

	/**
     * Método que atualiza as informações dos lançamentos.
     * 
     * @param idsLancamento - identificadores de lançamentos
     * @param usuario - usuário
     * @param mapaLancamentoRecolhimento - mapa de lancamentos e produtos de
     *            recolhimento
     * @param statusLancamento - status do lançamento
     */
	private void atualizarLancamentos(Set<Long> idsLancamento, Usuario usuario,
									  Map<Long, ProdutoRecolhimentoDTO> mapaLancamentoRecolhimento,
									  StatusLancamento statusLancamento,
									  TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada) {
		
		if (!idsLancamento.isEmpty()) {
		
			List<Lancamento> listaLancamentos = this.lancamentoRepository.obterLancamentosPorIdOrdenados(idsLancamento);
			
			if (listaLancamentos == null || listaLancamentos.isEmpty()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Lançamento não encontrado!");
			}
			
			if (idsLancamento.size() != listaLancamentos.size()) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Lançamento não encontrado!");
			}
			
			ProdutoRecolhimentoDTO produtoRecolhimento = null;
			
			for (Lancamento lancamento : listaLancamentos) {
				
				produtoRecolhimento = mapaLancamentoRecolhimento.get(lancamento.getId());
				
				Date novaData = produtoRecolhimento.getNovaData();
				
				if(statusLancamento != null) {
					
				  lancamento.setStatus(statusLancamento);
				}
				
				lancamento.setDataStatus(new Date());
				lancamento.setUsuario(usuario);		
				
				lancamento.setDataRecolhimentoDistribuidor(novaData);
				
				if (lancamento.getPeriodoLancamentoParcial() != null) {
				
				    this.parciaisService.alterarRecolhimento(lancamento, novaData);
				}
				
				this.lancamentoRepository.merge(lancamento);

				this.lancamentoService.atualizarRedistribuicoes(lancamento, novaData, (statusLancamento != null));
				
				this.montarMatrizRecolhimentosConfirmados(matrizConfirmada, produtoRecolhimento, lancamento, novaData);
				
			}
		}
	}
	
	/**
     * Monta a matriz de recolhimento com os recolhimentos confirmados.
     * 
     * @param matrizConfirmada - matriz de recolhimento confirmada
     * @param produtoRecolhimento - produto de recolhimento
     * @param lancamento - lançamento
     * @param novaData - nova data de recolhimento
     */
	private void montarMatrizRecolhimentosConfirmados(TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizConfirmada,
													  ProdutoRecolhimentoDTO produtoRecolhimento,
													  Lancamento lancamento,
													  Date novaData) {

		if (matrizConfirmada == null) {
			
			return;
		}
		
		if (lancamento != null) {
			
			produtoRecolhimento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoDistribuidor());
			produtoRecolhimento.setStatusLancamento(lancamento.getStatus().toString());
		}
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = matrizConfirmada.get(novaData);
		
		if (produtosRecolhimento == null) {
		
			produtosRecolhimento = new ArrayList<ProdutoRecolhimentoDTO>();
		}
		
		produtosRecolhimento.add(produtoRecolhimento);
		
		matrizConfirmada.put(novaData, produtosRecolhimento);
	}
	
	/**
	 * Processa lista de CotaReparteDTO de Lancamento
	 * 
	 * @param cotasReparte
	 * @param idLancamento
	 * @return List<CotaReparteDTO>
	 */
	private List<CotaReparteDTO> processaListaCotaReparteDTOLancamento(List<CotaReparteDTO> cotasReparte, Long idLancamento){
		
		List<CotaReparteDTO> cotasReparteLancamento = new ArrayList<CotaReparteDTO>();
		
		for (CotaReparteDTO item : cotasReparte){
			
			if (item.getIdLancamento().equals(idLancamento)){
				
				cotasReparteLancamento.add(item);
			}
		}
		
		return cotasReparteLancamento;
	}
	
	/**
	 * Processa lista de ChamadaEncalhe de ProdutoEdicao
	 * 
	 * @param listaChamadaEncalhe
	 * @param idProdutoEdicao
	 * @return List<ChamadaEncalhe>
	 */
	private List<ChamadaEncalhe> processaListaChamadaEncaleProdutoEdicao(List<ChamadaEncalhe> listaChamadaEncalhe, Long idProdutoEdicao){
		
		List<ChamadaEncalhe> chamadaEncalheProdutoEdicao = new ArrayList<ChamadaEncalhe>();
		
		for (ChamadaEncalhe item : listaChamadaEncalhe){
			
			if (item.getProdutoEdicao().getId().equals(idProdutoEdicao)){
				
				chamadaEncalheProdutoEdicao.add(item);
			}
		}
		
		return chamadaEncalheProdutoEdicao;
	}
	
	/**
     * Gera as chamadas de encalhe para os produtos da matriz de balanceamento.
     * 
     * @param mapaDataRecolhimentoLancamentos - mapa de datas de recolhimento e
     *            identificadores de lancamentos.
     * @param numeroSemana - número da semana
	     * @param usuario 
     */
	private void gerarChamadasEncalhe(Map<Date, Set<Long>> mapaDataRecolhimentoLancamentos, Integer numeroSemana, Usuario usuario) {
		
		if (mapaDataRecolhimentoLancamentos == null || mapaDataRecolhimentoLancamentos.isEmpty()) {
		
			return;
		}
		
		for (Map.Entry<Date, Set<Long>> entry : mapaDataRecolhimentoLancamentos.entrySet()) {
		
			Set<Long> idsLancamento = entry.getValue();
			
			Date dataRecolhimento = entry.getKey();
			
			if (idsLancamento == null || idsLancamento.isEmpty()) {
				
				continue;
			}
			
			Integer sequencia = this.chamadaEncalheRepository.obterMaiorSequenciaPorDia(dataRecolhimento);
			
			List<CotaReparteDTO> cotasReparte =	this.movimentoEstoqueCotaRepository.obterReparte(idsLancamento, null);
			
	
			List<ChamadaEncalhe> listaChamadaEncalhe = this.chamadaEncalheRepository.obterChamadasEncalheLancamentos(idsLancamento, false);

			for (Long idLancamento : idsLancamento) {

				Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);

				ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
				
				List<CotaReparteDTO> cotasReparteLancamento = this.processaListaCotaReparteDTOLancamento(cotasReparte, idLancamento);

				// odemir CE parciais para cota com reparte zero no periodo final
				// ver se 'e produto parcial, se periodo do lancamento 'e final
				// entao buscar cotas que nao teve reparte neste lancamento mas teve nos anteriores
				
				 if (produtoEdicao.isParcial() && lancamento.getPeriodoLancamentoParcial() != null && 
					 TipoLancamentoParcial.FINAL.equals(lancamento.getPeriodoLancamentoParcial().getTipo())) {
					 LOGGER.warn("Produto parcial com lancamento FINAL");
					// buscar cotas que nao teve reparte neste lancamento mas teve nos anteriores
					List <CotaReparteDTO> cotasRepartePeriodosAnteriores= this.movimentoEstoqueCotaRepository.obterRepartePeriodosAnteriores(lancamento);
					 
					 // incluir em cotasReparteLancamento
					for (CotaReparteDTO cotaRepartePeriodosAnteriores :cotasRepartePeriodosAnteriores ) {
						if (!cotasReparteLancamento.contains(cotaRepartePeriodosAnteriores)) {
							    LOGGER.error("INSERINDO COTA "+cotaRepartePeriodosAnteriores.getCota().getId());
								cotasReparteLancamento.add(cotaRepartePeriodosAnteriores);
						}
					}
				 }
				
				// odemir
				Set<ChamadaEncalhe> chamadasEncalheProdutoEdicao = new HashSet<>(this.processaListaChamadaEncaleProdutoEdicao(listaChamadaEncalhe, produtoEdicao.getId()));

				for (CotaReparteDTO cotaReparte : cotasReparteLancamento) {

					Cota cota = cotaReparte.getCota();
					
					BigInteger qtdPrevista = cotaReparte.getReparte();

					this.removerChamadaEncalheCotaAntecipadaChamadao(cota, new ArrayList<>(chamadasEncalheProdutoEdicao));
					
					ChamadaEncalhe chamadaEncalhe = this.getChamadaEncalheMatrizRecolhimento(new ArrayList<>(chamadasEncalheProdutoEdicao), dataRecolhimento);

					if (chamadaEncalhe == null) {
						
					    chamadaEncalhe = this.criarChamadaEncalhe(dataRecolhimento, produtoEdicao, ++sequencia);
					    this.chamadaEncalheRepository.adicionar(chamadaEncalhe);
					    listaChamadaEncalhe.add(chamadaEncalhe);
					    
					    chamadasEncalheProdutoEdicao.add(chamadaEncalhe);
					}

					Set<Lancamento> lancamentos = chamadaEncalhe.getLancamentos();
					
					if(lancamentos == null || lancamentos.isEmpty()) {
						
						lancamentos = new HashSet<Lancamento>();
					}
					
					lancamentos.add(lancamento);
					
					chamadaEncalhe.setLancamentos(lancamentos);
					
					chamadasEncalheProdutoEdicao.add(chamadaEncalhe);
					
					if (!chamadasEncalheProdutoEdicao.contains(chamadaEncalhe)) {
						
					}
					
					this.criarChamadaEncalheCota(qtdPrevista, cota, chamadaEncalhe, lancamento.getDataLancamentoDistribuidor(), cotaReparte.isCotaContribuinteExigeNF(), usuario);
				
					this.chamadaEncalheRepository.merge(chamadaEncalhe);
				}
			}
		}
	}

    private void removerChamadaEncalheCotaAntecipadaChamadao(Cota cota, List<ChamadaEncalhe> chamadasEncalhe) {
        
        ChamadaEncalheCota chamadaEncalheCota =
            this.getChamadaEncalheCotaAntecipadaChamadao(chamadasEncalhe, cota.getId());
        
        if (chamadaEncalheCota != null) {
        	this.chamadaEncalheCotaRepository.remover(chamadaEncalheCota);
        }
    }
	
	/**
	 * Verifica se a cota devolve encalhe para a criação ou não de ChamadaEncalheCota
	 * 
	 * @param tipoCota
	 * @param devolveEncalhe
	 * @return boolean
	 */
	private boolean isDevolveEncalhe(final TipoCota tipoCota, 
			                         Boolean devolveEncalhe){
		
		devolveEncalhe = (devolveEncalhe == null ? true : devolveEncalhe);
		
		if (tipoCota.equals(TipoCota.A_VISTA)){
			    
		    if (devolveEncalhe){
		    		
		    	return true;
		    }
		    else{
		    	
		    	return false;
		    }
		}

		return true;
	}
	
	/**
     * Método que cria uma chamada de encalhe para a cota.
     * 
     * @param qtdPrevista - quantidade prevista
     * @param cota - cota
     * @param chamadaEncalhe chamada de encalhe
	 * @param usuario 
     */
	private void criarChamadaEncalheCota(BigInteger qtdPrevista,
										 Cota cota, ChamadaEncalhe chamadaEncalhe,
										 Date dataLctoDistribuidor,
										 boolean cotaContribuinteExigeNF,
										 Usuario usuario) {
		
		//if(BigInteger.ZERO.compareTo(qtdPrevista) >= 0) {
			
			//return;
		//}
		
		boolean criarCahamadaEncalheCota = this.isDevolveEncalhe(cota.getTipoCota(), cota.isDevolveEncalhe());
		
		if (!criarCahamadaEncalheCota) {
		
		    return;
		}
		
		ChamadaEncalheCota chamadaEncalheCota = this.getChamadaEncalheCota(chamadaEncalhe, cota.getId());
		
		if (chamadaEncalheCota == null) {
			
			chamadaEncalheCota = new ChamadaEncalheCota();
		}
		
		BigInteger qtdPrevistaExistente = chamadaEncalheCota.getQtdePrevista() != null ? chamadaEncalheCota.getQtdePrevista() : BigInteger.ZERO;
		
		qtdPrevista = qtdPrevista.add(qtdPrevistaExistente);
		
		chamadaEncalheCota.setChamadaEncalhe(chamadaEncalhe);
		chamadaEncalheCota.setFechado(false);
		chamadaEncalheCota.setCota(cota);
		chamadaEncalheCota.setQtdePrevista(qtdPrevista);
		chamadaEncalheCota.setProcessoUtilizaNfe(cotaContribuinteExigeNF);
		chamadaEncalheCota.setUsuario(usuario);
		
		chamadaEncalheCota = this.chamadaEncalheCotaRepository.merge(chamadaEncalheCota);
		
		if (chamadaEncalhe.getChamadaEncalheCotas() == null) {
		    
		    chamadaEncalhe.setChamadaEncalheCotas(new HashSet<ChamadaEncalheCota>());
		}
		
		chamadaEncalhe.getChamadaEncalheCotas().add(chamadaEncalheCota);
		
		this.chamadaEncalheRepository.merge(chamadaEncalhe);
		
	}
	
	private ChamadaEncalhe getChamadaEncalheMatrizRecolhimento(List<ChamadaEncalhe> chamadasEncalhe,
	                                                           Date dataRecolhimento) {
		
		for (ChamadaEncalhe chamadaEncalhe : chamadasEncalhe) {
			
			TipoChamadaEncalhe tipoChamadaEncalhe = chamadaEncalhe.getTipoChamadaEncalhe();
			
			if (tipoChamadaEncalhe.equals(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)
			        && chamadaEncalhe.getDataRecolhimento().equals(dataRecolhimento)) {
				
				return chamadaEncalhe;
			}
		}
		
		return null;
	}
	
	private ChamadaEncalheCota getChamadaEncalheCotaAntecipadaChamadao(List<ChamadaEncalhe> chamadasEncalhe, Long idCota) {
		
		for (ChamadaEncalhe chamadaEncalhe : chamadasEncalhe) {
			
			TipoChamadaEncalhe tipoChamadaEncalhe = chamadaEncalhe.getTipoChamadaEncalhe();
			
			if (!tipoChamadaEncalhe.equals(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)) {
				
				for (ChamadaEncalheCota chamadaEncalheCota : chamadaEncalhe.getChamadaEncalheCotas()) {
					
					if (idCota.equals(chamadaEncalheCota.getCota().getId())) {
						
						return chamadaEncalheCota;
					}
				}
			}
		}
		
		return null;
	}
	
	private ChamadaEncalheCota getChamadaEncalheCota(ChamadaEncalhe chamadaEncalhe, Long idCota) {
			
		for (ChamadaEncalheCota chamadaEncalheCota : chamadaEncalhe.getChamadaEncalheCotas()) {
			
			if (idCota.equals(chamadaEncalheCota.getCota().getId())) {
		
				return chamadaEncalheCota;
			}
		}

		return null;
	}
	
	    /**
     * Método que cria uma chamada de encalhe.
     * 
     * @param dataRecolhimento - data de recolhimento
     * @param produtoEdicao - produto edição
     * @param sequencia
     * 
     * @return chamada de encalhe
     */
	private ChamadaEncalhe criarChamadaEncalhe(Date dataRecolhimento, ProdutoEdicao produtoEdicao, Integer sequencia) {
		
		ChamadaEncalhe chamadaEncalhe = new ChamadaEncalhe();
		
		chamadaEncalhe.setDataRecolhimento(dataRecolhimento);
		chamadaEncalhe.setProdutoEdicao(produtoEdicao);
		chamadaEncalhe.setTipoChamadaEncalhe(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		chamadaEncalhe.setSequencia(sequencia);
		
		return chamadaEncalhe;
	}
	
	    /**
     * Monta o DTO com as informações para realização do balanceamento.
     */
	private RecolhimentoDTO obterDadosRecolhimento(Integer anoNumeroSemana,
			 									   List<Long> listaIdsFornecedores,
			 									   TipoBalanceamentoRecolhimento tipoBalanceamento,
			 									   boolean forcarBalanceamento) {
		
		RecolhimentoDTO dadosRecolhimento = new RecolhimentoDTO();
		
		Intervalo<Date> periodoRecolhimento =
			getPeriodoRecolhimento(anoNumeroSemana);
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimentoFornecedor(periodoRecolhimento, listaIdsFornecedores);
		
		dadosRecolhimento.setDatasRecolhimentoFornecedor(datasRecolhimentoFornecedor);
		
		TreeSet<Date> datasRecolhimentoDisponiveis =
		    this.obterDatasRecolhimentoDisponiveis(datasRecolhimentoFornecedor);

		dadosRecolhimento.setDatasRecolhimentoDisponiveis(datasRecolhimentoDisponiveis);
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimento = null;

		if (TipoBalanceamentoRecolhimento.EDITOR.equals(tipoBalanceamento)) {
			
			produtosRecolhimento =
				this.lancamentoRepository.obterBalanceamentoRecolhimentoPorEditorData(periodoRecolhimento, 
					 																  listaIdsFornecedores, 
																					  GrupoProduto.CROMO);
		
		} else if (TipoBalanceamentoRecolhimento.AUTOMATICO.equals(tipoBalanceamento)
						|| TipoBalanceamentoRecolhimento.VALOR.equals(tipoBalanceamento)) {
			
			produtosRecolhimento =
				this.lancamentoRepository.obterBalanceamentoRecolhimento(periodoRecolhimento, 
																		 listaIdsFornecedores, 
																		 GrupoProduto.CROMO);
		}
		
		TreeMap<Date, BigDecimal> mapaExpectativaEncalheTotalDiaria =
			this.lancamentoRepository.obterExpectativasEncalhePorData(periodoRecolhimento, 
																	  listaIdsFornecedores, 
																	  GrupoProduto.CROMO);
		
        BigDecimal media = BigDecimal.ZERO;
		
		for (BigDecimal bigDecimal : mapaExpectativaEncalheTotalDiaria.values()) {
			media = media.add(bigDecimal);
		}
		
		dadosRecolhimento.addMediaRecolhimentoDistribuidor(media.longValue()/dadosRecolhimento.getDatasRecolhimentoFornecedor().size());
		
		List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados = new ArrayList<>();
		
		produtosRecolhimento = this.tratarAgrupamentoLancamentosComMesmaEdicao(produtosRecolhimento, produtosRecolhimentoAgrupados);
		
		dadosRecolhimento.setProdutosRecolhimentoAgrupados(produtosRecolhimentoAgrupados);
		
		dadosRecolhimento.setProdutosRecolhimento(produtosRecolhimento);
		
		dadosRecolhimento.setMapaExpectativaEncalheTotalDiaria(mapaExpectativaEncalheTotalDiaria);
		
		dadosRecolhimento.setCapacidadeRecolhimentoDistribuidor(this.distribuidorRepository.capacidadeRecolhimento());
		
		dadosRecolhimento.setForcarBalanceamento(forcarBalanceamento);
	
		dadosRecolhimento.setPeriodoRecolhimentoSemanaAnterior(this.getPeriodoSemanaAnterior(periodoRecolhimento.getDe()));
		
		if (!produtosRecolhimento.isEmpty()) {
		
			this.obterCotasOperacaoDiferenciada(periodoRecolhimento, produtosRecolhimento, dadosRecolhimento);
		}
		
		this.atualizarEncalheSedeAtendidaDosProdutos(produtosRecolhimento, dadosRecolhimento.getCotasOperacaoDiferenciada());
		
		return dadosRecolhimento;
	}

	    private TreeSet<Date> obterDatasRecolhimentoDisponiveis(TreeSet<Date> datasRecolhimentoFornecedor) {
            
	        TreeSet<Date> datasRecolhimentoDisponiveis = new TreeSet<>();
	        
	        boolean isMatrizRecolhimentoConfirmado;
	        boolean isAnteriorDataOperacao;
	        
	        Date dataOperacao = this.distribuidorRepository.obterDataOperacaoDistribuidor();
	        
	        for (Date dataRecolhimentoFornecedor : datasRecolhimentoFornecedor) {
	            
	            isMatrizRecolhimentoConfirmado =
	                    this.lancamentoRepository.existeMatrizRecolhimentoConfirmado(dataRecolhimentoFornecedor);
	            
	            isAnteriorDataOperacao = 
	            		DateUtil.isDataInicialMaiorDataFinal(dataOperacao, dataRecolhimentoFornecedor);
	            
	            if (!isMatrizRecolhimentoConfirmado && !isAnteriorDataOperacao) {
	                
	                datasRecolhimentoDisponiveis.add(dataRecolhimentoFornecedor);
	            }
	        }
	        
            return datasRecolhimentoDisponiveis;
        }

        /**
     * Monta o perídodo de recolhimento de acordo com a semana informada.
     */
	@Override
	@Transactional
	public Intervalo<Date> getPeriodoRecolhimento(Integer anoNumeroSemana) {
		
		final int codigoInicioSemana = 
				this.distribuidorRepository.buscarInicioSemanaRecolhimento().getCodigoDiaSemana();
		
		final Integer anoBase = SemanaUtil.getAno(anoNumeroSemana);
		
		final Integer numeroSemana = SemanaUtil.getSemana(anoNumeroSemana);
		
		final Date dataInicioSemana = 
			SemanaUtil.obterDataDaSemanaNoAno(
				numeroSemana,
				codigoInicioSemana, 
				anoBase);
		
		final Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		return new Intervalo<Date>(dataInicioSemana, dataFimSemana);
	}
	
	    /**
     * Obtém as datas de recolhimento dos fornecedores informados.
     */
	@Transactional (readOnly = true)
	public TreeSet<Date> obterDatasRecolhimentoFornecedor(Intervalo<Date> periodoRecolhimento,
														   List<Long> listaIdsFornecedores) {
       
		if (periodoRecolhimento == null || listaIdsFornecedores == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR ,
										 "periodo ou fornecedores nulos");
		}

		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.RECOLHIMENTO);
		
		if (listaDistribuicaoFornecedor == null || listaDistribuicaoFornecedor.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING ,
            // "Dias de recolhimento para os fornecedores não encontrados!");
					                      "Cadastrar os dias de recolhimento para os fornecedores selecionados.");
		}
		
		Set<Integer> diasSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			diasSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasRecolhimentoFornecedor = 
			this.obterDatasRecolhimento(periodoRecolhimento, diasSemanaFornecedor);
		
		return datasRecolhimentoFornecedor;
	}
	
	    /**
     * Obtém as datas para recolhimento no período informado, de acordo com os
     * dias da semana informados.
     */
	private TreeSet<Date> obterDatasRecolhimento(Intervalo<Date> periodoRecolhimento,
											 	 Set<Integer> diasRecolhimentoSemana) {
		
		TreeSet<Date> datasRecolhimento =
			SemanaUtil.obterPeriodoDeAcordoComDiasDaSemana(
				periodoRecolhimento.getDe(), periodoRecolhimento.getAte(), diasRecolhimentoSemana);
		
		TreeSet<Date> datasRecolhimentoComOperacao = new TreeSet<>();
		
		for (Date data : datasRecolhimento) {
			
			try {
				
				this.verificaDataOperacao(data);
				
				datasRecolhimentoComOperacao.add(data);
				
			} catch (ValidacaoException e) {
				
				continue;
			}
		}
		
		return datasRecolhimentoComOperacao;
	}

	@Override
	@Transactional
	public void voltarConfiguracaoOriginal(Integer numeroSemana, List<Long> fornecedores, Usuario usuario) {
		
		Intervalo<Date> periodoRecolhimento =
			getPeriodoRecolhimento(numeroSemana);
		
		List<Lancamento> lancamentos =  lancamentoRepository.obterLancamentosARecolherNaSemana(periodoRecolhimento, fornecedores);
			
		for(Lancamento lancamento: lancamentos) {
			
			lancamento.setStatus(StatusLancamento.EXPEDIDO);
			lancamento.setDataRecolhimentoDistribuidor(lancamento.getDataRecolhimentoPrevista());
			
			lancamento.setUsuario(usuario);
			
			lancamentoRepository.alterar(lancamento);
		}
	}
	
	@Transactional
	public void verificaDataOperacao(Date data) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
	
		if (this.calendarioService.isFeriadoSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
                    "A data de recolhimento deve ser uma data em que o distribuidor realiza operação!");
		}
		
		if (this.calendarioService.isFeriadoMunicipalSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
                    "A data de recolhimento deve ser uma data em que o distribuidor realiza operação!");
		}
	}
	
	@Transactional
	@Override
	public void processarProdutosProximaSemanaRecolhimento(List<ProdutoRecolhimentoDTO> produtos, Integer numeroSemana){
		
		if(produtos == null || produtos.isEmpty()){
			return;
		}
		
		for(ProdutoRecolhimentoDTO item : produtos){
			
			Date dataRecolhimento = this.obterDataValidaRecolhimento(numeroSemana, item.getIdFornecedor());
			
			lancamentoRepository.atualizarDataRecolhimentoDistribuidor(dataRecolhimento, item.getIdLancamento());
		}		
	}
	
	private Date obterDataValidaRecolhimento(Integer numeroSemana, Long idFornecedor){
		
		Intervalo<Date> periodoRecolhimento = getPeriodoRecolhimento(++numeroSemana);
		
		Date dataRecolhimento = periodoRecolhimento.getDe();
		
		Date dataValida = null;
		
		while(dataRecolhimento.compareTo(periodoRecolhimento.getAte())<=0){
			
			if(!lancamentoRepository.existeRecolhimentoBalanceado(dataRecolhimento)
					&& this.validarDiaRecolhimentoFornecedor(idFornecedor, dataRecolhimento)){
				dataValida = dataRecolhimento;
				break;
			}
			
			dataRecolhimento = DateUtil.adicionarDias(dataRecolhimento, 1);
		}		
				
		if(dataValida == null){
			dataValida = obterDataValidaRecolhimento(numeroSemana, idFornecedor);
		}
		
		return dataValida;
	}

	private boolean validarDiaRecolhimentoFornecedor(Long idFornecedor, Date dataRecolhimento) {
		List<Integer> diasRecolhimentoFornecedor = 
 this.distribuicaoFornecedorService
                .obterCodigosDiaDistribuicaoFornecedor(idFornecedor, OperacaoDistribuidor.RECOLHIMENTO);
		
		int codigoDiaCorrente = SemanaUtil.obterDiaDaSemana(dataRecolhimento);
		
		return diasRecolhimentoFornecedor.contains(codigoDiaCorrente);
	}
	
	private void obterCotasOperacaoDiferenciada(Intervalo<Date> periodoRecolhimento,
											    List<ProdutoRecolhimentoDTO> produtosRecolhimento,
											    RecolhimentoDTO dadosRecolhimento) {

		Map<Long, List<Date>> mapCotasDiasRecolhimento =
			this.obterCotasOperacaoDiferenciada(periodoRecolhimento);

		if (mapCotasDiasRecolhimento.isEmpty()) {
			
			return;
		}
		
		Set<Long> idsLancamento = this.obterIdsLancamento(produtosRecolhimento);

		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada =
			this.lancamentoRepository.obterLancamentosEncalhesPorCota(
				mapCotasDiasRecolhimento.keySet(), idsLancamento);

		this.setDatasRecolhimentoCotasOperaDif(mapCotasDiasRecolhimento, cotasOperacaoDiferenciada);

		dadosRecolhimento.setCotasOperacaoDiferenciada(cotasOperacaoDiferenciada);
	}
	
	private Map<Long, List<Date>> obterCotasOperacaoDiferenciada(Intervalo<Date> periodoRecolhimento) {
		
		Map<Long, List<Date>> mapCotasDiasRecolhimento = new HashMap<>();
		
		List<GrupoCota> gruposCota =
			this.grupoRepository.obterGruposCota(periodoRecolhimento.getDe());
		
		for (GrupoCota grupoCota : gruposCota) {
			
			Set<Cota> cotas = grupoCota.getCotas();
			Set<String> municipios = grupoCota.getMunicipios();
			Set<DiaSemana> diasRecolhimento = grupoCota.getDiasRecolhimento();
			
			if (cotas != null && !cotas.isEmpty()) {
			
				this.montarMapOperacaoDiferenciadaCotas(
					cotas, diasRecolhimento, periodoRecolhimento, mapCotasDiasRecolhimento);
				
			} else if (municipios != null && !municipios.isEmpty()) {
				
				this.montarMapOperacaoDiferenciadaMunicipios(
					municipios, diasRecolhimento, periodoRecolhimento, mapCotasDiasRecolhimento);
			}
		}
		
		return mapCotasDiasRecolhimento;
	}

	private void montarMapOperacaoDiferenciadaCotas(Set<Cota> cotas,
													Set<DiaSemana> diasRecolhimento,
													Intervalo<Date> periodoRecolhimento,
													Map<Long, List<Date>> mapCotasDiasRecolhimento) {
		
		List<Date> datasRecolhimento = 
			this.obterDatasRecolhimentoGrupo(diasRecolhimento, periodoRecolhimento);
		
		for (Cota cota : cotas) {
			
			mapCotasDiasRecolhimento.put(cota.getId(), datasRecolhimento);
		}
	}
	
	private void montarMapOperacaoDiferenciadaMunicipios(Set<String> municipios,
														 Set<DiaSemana> diasRecolhimento,
														 Intervalo<Date> periodoRecolhimento,
														 Map<Long, List<Date>> mapCotasDiasRecolhimento) {
		
		List<Date> datasRecolhimento = 
			this.obterDatasRecolhimentoGrupo(diasRecolhimento, periodoRecolhimento);
		
		for (String municipio : municipios) {
			
			List<Long> idsCotas = this.cotaRepository.obterIdsCotasPorMunicipio(municipio);
			
			for (Long idCota : idsCotas) {
				
				mapCotasDiasRecolhimento.put(idCota, datasRecolhimento);
			}
		}
	}

	private List<Date> obterDatasRecolhimentoGrupo(Set<DiaSemana> diasRecolhimento,
											   	   Intervalo<Date> periodoRecolhimento) {
		
		List<Date> datasRecolhimentoGrupo = new ArrayList<>();
		
		List<Calendar> datasDaSemana =
			this.getDatasDaSemana(periodoRecolhimento);
		
		Set<Integer> diasRecolhimentoNaSemana = this.getDiasRecolhimentoNaSemana(diasRecolhimento);
		
		for (Calendar dataRecolhimento : datasDaSemana) {
			
			if (diasRecolhimentoNaSemana.contains(SemanaUtil.obterDiaDaSemana(dataRecolhimento))) {
				
				datasRecolhimentoGrupo.add(dataRecolhimento.getTime());
			}
		}
		
		return datasRecolhimentoGrupo;
	}

	private Set<Integer> getDiasRecolhimentoNaSemana(Set<DiaSemana> diasRecolhimento) {
		
		Set<Integer> diasRecolhimentoNaSemana = new HashSet<>();
		
		for (DiaSemana diaSemana : diasRecolhimento) {
			
			diasRecolhimentoNaSemana.add(diaSemana.getCodigoDiaSemana());
		}
		
		return diasRecolhimentoNaSemana;
	}

	private List<Calendar> getDatasDaSemana(Intervalo<Date> periodoRecolhimento) {
		
		List<Calendar> datasDaSemana = new ArrayList<>();
		
		Calendar dataInicio = DateUtil.toCalendar(periodoRecolhimento.getDe());
		Calendar dataFim = DateUtil.toCalendar(periodoRecolhimento.getAte());
		
		Calendar dataPeriodoRecolhimento = dataInicio;
		
		while(dataPeriodoRecolhimento.compareTo(dataInicio) >= 0
				&& dataPeriodoRecolhimento.compareTo(dataFim) <= 0) {
			
			datasDaSemana.add(dataPeriodoRecolhimento);
			
			dataPeriodoRecolhimento = DateUtil.adicionarDias(dataPeriodoRecolhimento, 1);
		}
		
		return datasDaSemana;
	}
	
	private Set<Long> obterIdsLancamento(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		Set<Long> idsLancamento = new HashSet<>();
		
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : produtosRecolhimento) {
			
			if(produtoRecolhimentoDTO.getIdsLancamentosAgrupados() != null
					&& !produtoRecolhimentoDTO.getIdsLancamentosAgrupados().isEmpty()) {
				
				idsLancamento.addAll(produtoRecolhimentoDTO.getIdsLancamentosAgrupados());
			}
			idsLancamento.add(produtoRecolhimentoDTO.getIdLancamento());
		}
		
		return idsLancamento;
	}
	
	private void setDatasRecolhimentoCotasOperaDif(Map<Long, List<Date>> mapCotasDiasRecolhimento,
												   List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {

		List<Date> datasRecolhimento = null;
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciadaDTO : cotasOperacaoDiferenciada) {

			datasRecolhimento = mapCotasDiasRecolhimento.get(cotaOperacaoDiferenciadaDTO.getIdCota());

			cotaOperacaoDiferenciadaDTO.setDatasRecolhimento(datasRecolhimento);
		}
	}
	
	@Transactional
	public void montarMapasOperacaoDiferenciada(Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover,
												TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
												List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		
		if (cotasOperacaoDiferenciada == null || cotasOperacaoDiferenciada.isEmpty()) {
			
			return;
		}

		if (matrizRecolhimento == null) {
			
			return;
		}

		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			List<ProdutoRecolhimentoDTO> produtosRecolhimento = entry.getValue();
			
			for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
				
				if(produtoRecolhimento.isProdutoAgrupado()) {
					
					for(Long idLancamento : produtoRecolhimento.getIdsLancamentosAgrupados()) {
						
						List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
								this.obterCotasOperacaoDiferenciadaPorLancamento(cotasOperacaoDiferenciada, idLancamento);
						
						if (!cotasOperacaoDiferenciadaDoLancamento.isEmpty()) {
							
							this.montarMapsOperacaoDiferenciada(
								cotasOperacaoDiferenciadaDoLancamento, mapOperacaoDifAdicionar, mapOperacaoDifRemover, dataRecolhimento);
						}
					}
				}
				
				List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
					this.obterCotasOperacaoDiferenciadaPorLancamento(cotasOperacaoDiferenciada, produtoRecolhimento.getIdLancamento());
				
				if (!cotasOperacaoDiferenciadaDoLancamento.isEmpty()) {
					
					this.montarMapsOperacaoDiferenciada(
						cotasOperacaoDiferenciadaDoLancamento, mapOperacaoDifAdicionar, mapOperacaoDifRemover, dataRecolhimento);
				}
			}
		}
	}
	
	private void atualizarEncalheSedeAtendidaDosProdutos(
								List<ProdutoRecolhimentoDTO> produtosRecolhimento,
								List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada) {
		
		if (cotasOperacaoDiferenciada == null || cotasOperacaoDiferenciada.isEmpty()) {
			
			return;
		}

		if (produtosRecolhimento == null || produtosRecolhimento.isEmpty()) {
			
			return;
		}
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			if(produtoRecolhimento.getIdsLancamentosAgrupados() != null
					&& !produtoRecolhimento.getIdsLancamentosAgrupados().isEmpty()) {
				
				for(Long idLancamento : produtoRecolhimento.getIdsLancamentosAgrupados()) {
					
					List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
							this.obterCotasOperacaoDiferenciadaPorLancamento(cotasOperacaoDiferenciada, idLancamento);
						
						this.atualizarEncalheSedeAtendida(produtoRecolhimento, cotasOperacaoDiferenciadaDoLancamento);
				}
			}
			
			List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento =
				this.obterCotasOperacaoDiferenciadaPorLancamento(cotasOperacaoDiferenciada, produtoRecolhimento.getIdLancamento());
			
			this.atualizarEncalheSedeAtendida(produtoRecolhimento, cotasOperacaoDiferenciadaDoLancamento);
		}
	}
	
	private void atualizarEncalheSedeAtendida(
						   ProdutoRecolhimentoDTO produtoRecolhimento,
						   List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento) {
		
		BigDecimal expectativaEncalheAtendida = BigDecimal.ZERO;
		BigDecimal expectativaEncalheSede = produtoRecolhimento.getExpectativaEncalhe();
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciadaDoLancamento) {
			
			expectativaEncalheAtendida = expectativaEncalheAtendida.add(cotaOperacaoDiferenciada.getExpectativaEncalhe());
			
			expectativaEncalheSede = expectativaEncalheSede.subtract(cotaOperacaoDiferenciada.getExpectativaEncalhe());
		}
		
		if(produtoRecolhimento.getExpectativaEncalheAtendida() != null) {
			
			produtoRecolhimento.setExpectativaEncalheAtendida(produtoRecolhimento.getExpectativaEncalheAtendida().add(expectativaEncalheAtendida));
		} else {
			
			produtoRecolhimento.setExpectativaEncalheAtendida(expectativaEncalheAtendida);
		}
		produtoRecolhimento.setExpectativaEncalheSede(expectativaEncalheSede);
	}
	
	private List<CotaOperacaoDiferenciadaDTO> obterCotasOperacaoDiferenciadaPorLancamento(
										List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada,
										Long idLancamento) {
		
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento = new ArrayList<>();
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciada) {
			
			if (cotaOperacaoDiferenciada.getIdLancamento().equals(idLancamento)) {
				
				cotasOperacaoDiferenciadaDoLancamento.add(cotaOperacaoDiferenciada);
			}
		}
		
		return cotasOperacaoDiferenciadaDoLancamento;
	}
	
	private void montarMapsOperacaoDiferenciada(List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciadaDoLancamento,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar,
												Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover,
												Date dataRecolhimento) {
		
		List<Date> datasRecolhimento = null;
		
		for (CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada : cotasOperacaoDiferenciadaDoLancamento) {
			
			datasRecolhimento = cotaOperacaoDiferenciada.getDatasRecolhimento();
			
			if (!datasRecolhimento.contains(dataRecolhimento)) {
				
				Date dataRecolhimentoEscolhida =
					this.obterDataRecolhimentoOperacaoDiferenciada(datasRecolhimento, dataRecolhimento);
				
				this.addMap(mapOperacaoDifAdicionar, dataRecolhimentoEscolhida, cotaOperacaoDiferenciada);
				
				this.addMap(mapOperacaoDifRemover, dataRecolhimento, cotaOperacaoDiferenciada);
			}
		}
	}

	private void addMap(Map<Date, List<CotaOperacaoDiferenciadaDTO>> map,
						Date dataRecolhimento,
						CotaOperacaoDiferenciadaDTO cotaOperacaoDiferenciada) {
		
		List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada = map.get(dataRecolhimento);
		
		if (cotasOperacaoDiferenciada == null) {
			
			cotasOperacaoDiferenciada = new ArrayList<>();
		}
		
		cotasOperacaoDiferenciada.add(cotaOperacaoDiferenciada);
		
		map.put(dataRecolhimento, cotasOperacaoDiferenciada);
	}
	
	    /*
     * Obtém uma data de recolhimento de acordo com a datas de recolhimento da
     * cota. Primeiro tenta obter uma data maior que a data de recolhimento do
     * lançamento. Se não houver nenhuma pega a primeira data da cota, mesmo não
     * sendo mais que a data do lançamento.
     */
	private Date obterDataRecolhimentoOperacaoDiferenciada(List<Date> datasRecolhimento,
														   Date dataRecolhimento) {
		
		for (Date dataRecolhimentoOperacaoDiferenciada : datasRecolhimento) {
			
			if (dataRecolhimentoOperacaoDiferenciada.after(dataRecolhimento)) {
				
				return dataRecolhimentoOperacaoDiferenciada;
			}
		}
		
		return datasRecolhimento.get(0);
	}
	
	private List<ProdutoRecolhimentoDTO> tratarAgrupamentoLancamentosComMesmaEdicao(
											List<ProdutoRecolhimentoDTO> produtosRecolhimento,
											List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados) {

		Map<Long, List<Long>> mapProdutoEdicaoLancamento =
			this.montarMapProdutoEdicaoLancamento(produtosRecolhimento);

		List<ProdutoRecolhimentoDTO> produtosRecolhimentoRetorno = new ArrayList<>();

		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {

			if (produtoRecolhimento.isProdutoAgrupado()) {

				continue;
			}

			List<Long> idLancamentos = mapProdutoEdicaoLancamento.get(produtoRecolhimento.getIdProdutoEdicao());

			if (this.existeMaisDeUmLancamento(idLancamentos)) {

				produtosRecolhimentoAgrupados.add(produtoRecolhimento);
				
				this.agruparProdutosRecolhimento(produtoRecolhimento, idLancamentos, produtosRecolhimento, produtosRecolhimentoAgrupados);
			}

			produtosRecolhimentoRetorno.add(produtoRecolhimento);
		}

		return produtosRecolhimentoRetorno;
	}
	
	private Map<Long, List<Long>> montarMapProdutoEdicaoLancamento(List<ProdutoRecolhimentoDTO> produtosRecolhimento) {
		
		Map<Long, List<Long>> mapProdutoEdicaoLancamento = new HashMap<>();
		
		Long idLancamento = null;
		Long idProdutoEdicao = null;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {
			
			idLancamento = produtoRecolhimento.getIdLancamento();
			idProdutoEdicao = produtoRecolhimento.getIdProdutoEdicao();
			
			List<Long> idLancamentos = mapProdutoEdicaoLancamento.get(idProdutoEdicao);
			
			if (idLancamentos == null) {
				
				idLancamentos = new ArrayList<>();
			}
			
			idLancamentos.add(idLancamento);
			
			mapProdutoEdicaoLancamento.put(idProdutoEdicao, idLancamentos);
		}
		
		return mapProdutoEdicaoLancamento;
	}
	
	private void agruparProdutosRecolhimento(ProdutoRecolhimentoDTO produtoRecolhimento,
											 List<Long> idLancamentos,
											 List<ProdutoRecolhimentoDTO> produtosRecolhimento,
											 List<ProdutoRecolhimentoDTO> produtosRecolhimentosAgrupados) {

		for (Long idLancamento : idLancamentos) {

			if (produtoRecolhimento.getIdLancamento().equals(idLancamento)) {

				continue;
			}

			ProdutoRecolhimentoDTO produtoRecolhimentoMesmaEdicao = this.obterProdutoRecolhimento(produtosRecolhimento, idLancamento);

			ProdutoRecolhimentoDTO produtoRecolhimentoAntigo = null;
			ProdutoRecolhimentoDTO produtoRecolhimentoNovo = null;

			if (this.isLancamentoMaisAntigo(produtoRecolhimento, produtoRecolhimentoMesmaEdicao)) {

				produtoRecolhimentoAntigo = produtoRecolhimento;
				produtoRecolhimentoNovo = produtoRecolhimentoMesmaEdicao;

			} else {

				produtoRecolhimentoAntigo = produtoRecolhimentoMesmaEdicao;
				produtoRecolhimentoNovo = produtoRecolhimento;
			}

			this.efetivarAgrupamento(produtoRecolhimento, produtoRecolhimentoAntigo, produtoRecolhimentoNovo, idLancamento);
			
			produtosRecolhimentosAgrupados.add(produtoRecolhimentoMesmaEdicao);
		}
	}
	
	private ProdutoRecolhimentoDTO obterProdutoRecolhimento(List<ProdutoRecolhimentoDTO> produtosRecolhimento,
														   	Long idLancamento) {
	
		for (ProdutoRecolhimentoDTO produtoRecolhimento : produtosRecolhimento) {

			if (produtoRecolhimento.getIdLancamento().equals(idLancamento)) {

				return produtoRecolhimento;
			}
		}

		return null;
	}
	
	private void efetivarAgrupamento(ProdutoRecolhimentoDTO produtoRecolhimento,
									 ProdutoRecolhimentoDTO produtoRecolhimentoAntigo,
									 ProdutoRecolhimentoDTO produtoRecolhimentoNovo,
									 Long idLancamento) {

		BigDecimal encalhe =
			BigDecimalUtil.soma(produtoRecolhimentoAntigo.getExpectativaEncalhe(),
								produtoRecolhimentoNovo.getExpectativaEncalhe());
		
		BigDecimal encalheAtendida =
			BigDecimalUtil.soma(produtoRecolhimentoAntigo.getExpectativaEncalheAtendida(), 
								produtoRecolhimentoNovo.getExpectativaEncalheAtendida());
		
		BigDecimal encalheSede =
			BigDecimalUtil.soma(produtoRecolhimentoAntigo.getExpectativaEncalheSede(), 
								produtoRecolhimentoNovo.getExpectativaEncalheSede());
		
		BigDecimal valorTotal =
			BigDecimalUtil.soma(produtoRecolhimentoAntigo.getValorTotal(), 
								produtoRecolhimentoNovo.getValorTotal());
		
		Long peso = produtoRecolhimentoAntigo.getPeso() + produtoRecolhimentoNovo.getPeso();
		
		produtoRecolhimento.setDataLancamento(produtoRecolhimentoAntigo.getDataLancamento());
		produtoRecolhimento.setDataRecolhimentoPrevista(produtoRecolhimentoAntigo.getDataRecolhimentoPrevista());
		produtoRecolhimento.setDataRecolhimentoDistribuidor(produtoRecolhimentoAntigo.getDataRecolhimentoDistribuidor());
		produtoRecolhimento.setExpectativaEncalhe(encalhe);
		produtoRecolhimento.setExpectativaEncalheAtendida(encalheAtendida);
		produtoRecolhimento.setExpectativaEncalheSede(encalheSede);
		produtoRecolhimento.setValorTotal(valorTotal);
		produtoRecolhimento.setPeso(peso);
		
		produtoRecolhimento.getIdsLancamentosAgrupados().add(idLancamento);
		produtoRecolhimentoAntigo.setProdutoAgrupado(true);
		produtoRecolhimentoNovo.setProdutoAgrupado(true);
	}

	private boolean isLancamentoMaisAntigo(ProdutoRecolhimentoDTO produtoRecolhimentoDTO,
										   ProdutoRecolhimentoDTO produtoRecolhimento) {

		if (produtoRecolhimentoDTO.getDataLancamento().before(
				produtoRecolhimento.getDataLancamento())) {

			return true;
		}

		return false;
	}
	
	private boolean existeMaisDeUmLancamento(List<Long> idLancamentos) {
		
		return idLancamentos.size() > 1;
	}
	
	/**
	 * Remove chamada de encalhe da cota e chamada de encalhe
	 * 
	 * @param listaIdChamadaEncalheRemover
	 */
	private void removerChamadaEncalheCotaEChamadaEncalhe(List<Long> listaIdChamadaEncalheRemover){
		
		if(!listaIdChamadaEncalheRemover.isEmpty() && listaIdChamadaEncalheRemover.size() > 0 ){
			
			this.chamadaEncalheCotaRepository.removerChamadaEncalheCotaPorIdsChamadaEncalhe(listaIdChamadaEncalheRemover);
			
			this.chamadaEncalheRepository.removerChamadaEncalhePorIds(listaIdChamadaEncalheRemover);
		}
	}
	
	@Transactional
	public String reabrirMatriz(List<Date> datasConfirmadas, Usuario usuario) {
		
		this.validarReaberturaMatriz(
			datasConfirmadas, this.distribuidorService.obterDataOperacaoDistribuidor());
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterRecolhimentosConfirmados(datasConfirmadas);

		List<Long> listaIdChamadaEncalheRemover = new ArrayList<Long>();

		boolean recolhimento = false;
		
		boolean reimpressao = false;

		for(Lancamento lancamento: lancamentos) {
			
            if (!lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)) {
				
            	recolhimento = true;
			}
            
            if (this.lancamentoRepository.existeConferenciaEncalheParaLancamento(lancamento.getId(),TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)) {
        		
            	reimpressao = true;
			}
			
			//lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
            lancamento.setStatus(StatusLancamento.EXPEDIDO);
			
			lancamento.setUsuario(usuario);

			Set<ChamadaEncalhe> ces = lancamento.getChamadaEncalhe();
			
            for (ChamadaEncalhe ce : ces){
			    
				if(ce.getTipoChamadaEncalhe().equals(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)
				  && ce.getDataRecolhimento().equals(lancamento.getDataRecolhimentoDistribuidor())){
				    
					listaIdChamadaEncalheRemover.add(ce.getId());
				}
			}

			this.lancamentoRepository.alterar(lancamento);
		}
        
        this.removerChamadaEncalheCotaEChamadaEncalhe(listaIdChamadaEncalheRemover);

		if(recolhimento && reimpressao){
            return "Existem lançamentos que já se econtram em processo de recolhimento!  A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.";
		}else if(recolhimento){
            return "Existem lançamentos que já se econtram em processo de recolhimento!";
		}else if(reimpressao){
            return "A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.";
		}else{
			return "";
		}
	}
	
	@Transactional
	public String cadeadoMatriz(List<Date> datasConfirmadas, Usuario usuario) {
		
		this.validarReaberturaMatriz(
			datasConfirmadas, this.distribuidorService.obterDataOperacaoDistribuidor());
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterRecolhimentosEmBalanceamentoRecolhimento(datasConfirmadas);

		List<Long> listaIdChamadaEncalheRemover = new ArrayList<Long>();

		boolean recolhimento = false;
		
		boolean reimpressao = false;

		for(Lancamento lancamento: lancamentos) {
			
			
            if (!lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)) {
				
            	recolhimento = true;
			}
            
            if (this.lancamentoRepository.existeConferenciaEncalheParaLancamento(lancamento.getId(),TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)) {
        		
            	reimpressao = true;
            	
            	lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
            	
            	this.lancamentoRepository.alterar(lancamento);
			} else {
			
			//lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
            lancamento.setStatus(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
			
			lancamento.setUsuario(usuario);

			Set<ChamadaEncalhe> ces = lancamento.getChamadaEncalhe();
			
            for (ChamadaEncalhe ce : ces){
			    
				if(ce.getTipoChamadaEncalhe().equals(TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)){
				    
					listaIdChamadaEncalheRemover.add(ce.getId());
				}
			}

			this.lancamentoRepository.alterar(lancamento);
			}
		}
        
        this.removerChamadaEncalheCotaEChamadaEncalhe(listaIdChamadaEncalheRemover);

		if(recolhimento && reimpressao){
            return "Existem lançamentos que já se econtram em processo de recolhimento!  A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.";
		}else if(recolhimento){
            return "Existem lançamentos que já se econtram em processo de recolhimento!";
		}else if(reimpressao){
            return "A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.";
		}else{
			return "";
		}
	}

	@Transactional
	public void validarLancamentoParaReabertura(List<Date> datasConfirmadas) {
		
		List<Lancamento> lancamentos = this.lancamentoRepository.obterRecolhimentosConfirmados(datasConfirmadas);
		
			for(Lancamento lancamento: lancamentos) {
				
				if (!lancamento.getStatus().equals(StatusLancamento.BALANCEADO_RECOLHIMENTO)) {
			
					throw new ValidacaoException(TipoMensagem.WARNING,
                        "Existem lançamentos que já se econtram em processo de recolhimento!");
				}
		
				if (this.lancamentoRepository.existeConferenciaEncalheParaLancamento(lancamento.getId(),TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO)) {
			
					throw new ValidacaoException(TipoMensagem.WARNING,
                        "A chamada de encalhe da data seleciona já foi gerada. Realizar a reimpressão do documento.");
				}
		}
	}

	private void validarReaberturaMatriz(List<Date> datasConfirmadas, Date dataOperacao) {
		
		List<String> mensagens = new ArrayList<>();
		
		if (datasConfirmadas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma data foi informada!");
		}
		
		for (Date dataConfirmada : datasConfirmadas) {
			
			if (dataConfirmada.compareTo(dataOperacao) <= 0) {
				
				String dataFormatada = DateUtil.formatarDataPTBR(dataConfirmada);
				
				mensagens.add("Para reabrir a matriz, a data (" + dataFormatada
                        + ") deve ser maior que a data de operação!");
			}
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public boolean existeRecolhimentoBalanceado(Date dataRecolhimento) {
	    
	    return this.lancamentoRepository.existeRecolhimentoBalanceado(dataRecolhimento);
	}
	
	private Intervalo<Date> getPeriodoSemanaAnterior(Date dataInicioRecolhimento) {
	     
        Date dataInicioSemanaAnterior = DateUtil.subtrairDias(dataInicioRecolhimento, 7);
        Date dataFimSemanaAnterior = DateUtil.subtrairDias(dataInicioRecolhimento, 1);
        
        return new Intervalo<Date>(dataInicioSemanaAnterior, dataFimSemanaAnterior);
    }
	@Override
	public void salvarBalanceamentoRecolhimento(Usuario idUsuario,
			BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoDTO) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public List<Lancamento> obterRecolhimentosEmBalanceamentoRecolhimento(
			List<Date> datasConfirmadas) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	@Transactional
	public void removerChamadaEncalheCotaZerada(List<Date> dataConfirmadas) {
		
		for (Date dataConfirmada : dataConfirmadas) {
			this.chamadaEncalheCotaRepository.removerChamadaEncalheCotaZerada(dataConfirmada);
		}
	}
	
	@Transactional
	public Date obterDataRecolhimentoValido(Date dataLancamento, Long idFornecedor) {
		
		List<Long> lista = new ArrayList<Long>();
		Distribuidor distribuidor = distribuidorService.obter();
		
		Long idDinap = fornecedorService.obterFornecedorPorCodigoInterface(new Integer(distribuidor.getCodigoDistribuidorDinap())).getId();
		Long idFc;
		
		if(distribuidor.getCodigoDistribuidorFC() != null && !distribuidor.getCodigoDistribuidorFC().trim().equals("")) {
			
			Fornecedor f = fornecedorService.obterFornecedorPorCodigoInterface(Integer.valueOf(distribuidor.getCodigoDistribuidorFC()));
			if(f != null) {
				
				idFc = f.getId();
			} else {
				
				idFc = new Long("0");
			}
		} else {
			
			idFc = new Long("0");
		}
		
		if(idFornecedor == idDinap.intValue()) {	
		 
			 lista.add(new Long(1));
			 
			 if(dinap.isEmpty()) {
				 dinap.addAll(lancamentoRepository.obterDatasRecolhimentoValidasAux());
			 }
			 
			 dataLancamento = this.obterDataRecolhimentoValida(dataLancamento, dinap);
		 
		} else if(idFornecedor == idFc.intValue()) {
		
			 lista.add(new Long(2));
			 
			 if(fc.isEmpty()) {
				 fc.addAll(lancamentoRepository.obterDatasRecolhimentoValidasAux());
			 }
			 
			 dataLancamento = this.obterDataRecolhimentoValida(dataLancamento, fc);
		 
		} else {
			
			 lista.add(new Long(1));
			 lista.add(new Long(2));
				 
			 if(dinapFC.isEmpty()) {
				dinapFC.addAll(lancamentoRepository.obterDatasRecolhimentoValidasAux());
			 }
				 
			 dataLancamento = this.obterDataRecolhimentoValida(dataLancamento, dinapFC);
		}
		
		return dataLancamento;
	}
	
	private Date obterDataRecolhimentoValida(Date dataLancamento, LinkedList<Date> listaDatas){

		Date operacao;

		if(listaDatas==null || listaDatas.isEmpty()) {

			operacao = distribuidorService.obterDataOperacaoDistribuidor();

			if(dataLancamento.before(operacao)) {
				return operacao;
			} else {
				return dataLancamento;
			}
		} else if(listaDatas.contains(dataLancamento)) {
			return dataLancamento;
		} else if(dataLancamento.before(listaDatas.getFirst())) {
			return listaDatas.getFirst();
		} else {

			for(int i = 0; i < listaDatas.size(); i++) {
				if(i > 0 && i < listaDatas.size() && dataLancamento.before(listaDatas.get(i))) {

					return listaDatas.get(i);
				}
			}

			dataLancamento =  listaDatas.getFirst();

			return dataLancamento;
		}
		
	}
	
}
