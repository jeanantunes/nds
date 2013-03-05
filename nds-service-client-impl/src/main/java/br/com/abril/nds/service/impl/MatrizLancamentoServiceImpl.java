package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.DadosBalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.BigIntegerUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.ConfirmacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected CalendarioService calendarioService;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	

	@Override
	@Transactional(readOnly = true)
	public BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro) {
	
		this.validarFiltro(filtro);
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento = 
			this.obterDadosLancamento(filtro);
		
		BalanceamentoLancamentoDTO matrizLancamento = this.balancear(dadosBalanceamentoLancamento);
		
		List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados = 
													this.obterProdutosLancamentosCancelados(filtro);
		
		if (produtosLancamentosCancelados != null && !produtosLancamentosCancelados.isEmpty()) {
			matrizLancamento.setProdutosLancamentosCancelados(produtosLancamentosCancelados);
		}
		
		return matrizLancamento;
	}
	
	private List<ProdutoLancamentoCanceladoDTO> obterProdutosLancamentosCancelados(FiltroLancamentoDTO filtro) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Date dataLancamento = filtro.getData();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, dataLancamento, numeroSemana);
		
		List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados = 
				this.lancamentoRepository.obterLancamentosCanceladosPor(
						periodoDistribuicao, filtro.getIdsFornecedores());
		
		return produtosLancamentosCancelados;
				
	}
	
	@Override
	@Transactional
	public TreeMap<Date, List<ProdutoLancamentoDTO>> confirmarMatrizLancamento(
													TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
													List<Date> datasConfirmadas, Usuario usuario) {
		
		this.validarDadosConfirmacao(matrizLancamento);

		Map<Long, ProdutoLancamentoDTO> mapaLancamento =
				new TreeMap<Long, ProdutoLancamentoDTO>();

		Set<Long> idsLancamento = new TreeSet<Long>();

		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = entry.getValue();
			
			if (listaProdutoLancamentoDTO == null || listaProdutoLancamentoDTO.isEmpty()) {

				continue;
			}

			for (ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
				
				Date novaDataLancamento = produtoLancamento.getNovaDataLancamento();
				
				Long idLancamento = produtoLancamento.getIdLancamento();

				// Monta Map e Set para controlar a atualização dos lançamentos 
				
				if (datasConfirmadas.contains(novaDataLancamento)) {

					idsLancamento.add(idLancamento);
					
					mapaLancamento.put(idLancamento, produtoLancamento);
				}
			}
		}
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoConfirmada = null;
		
		if (!idsLancamento.isEmpty()) {
		
			matrizLancamentoConfirmada =
				this.atualizarLancamentos(idsLancamento, usuario, mapaLancamento);
		}
		
		return matrizLancamentoConfirmada;
	}

	private void validarDadosConfirmacao(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		if (matrizLancamento == null || matrizLancamento.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de lançamento não informada!");
		}
	}
	
	/**
	 * Método que atualiza as informações dos lançamentos.
	 * 
	 * @param idsLancamento - identificadores de lançamentos
	 * @param usuario - usuário
	 * @param mapaLancamento - mapa de lancamentos e produtos de recolhimento
	 * 
	 * @return {@link TreeMap<Date, List<ProdutoLancamentoDTO>>}
	 */
	private TreeMap<Date, List<ProdutoLancamentoDTO>> atualizarLancamentos(
													Set<Long> idsLancamento, Usuario usuario,
													Map<Long, ProdutoLancamentoDTO> mapaLancamento) {
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoConfirmada =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		List<Lancamento> listaLancamentos =
			this.lancamentoRepository.obterLancamentosPorIdOrdenados(idsLancamento);
		
		this.validarDadosAtualizacaoLancamento(idsLancamento, listaLancamentos);
		
		for (Lancamento lancamento : listaLancamentos) {
			
			ProdutoLancamentoDTO produtoLancamento = mapaLancamento.get(lancamento.getId());
			
			Date novaData = produtoLancamento.getNovaDataLancamento();
			
			if (produtoLancamento.isLancamentoAgrupado()) {
				
				continue;
			}
			
			this.tratarLancamentosAgrupadosParaConfirmacao(
				lancamento, novaData, listaLancamentos, mapaLancamento);
			
			boolean gerarHistoricoLancamento =
				!(lancamento.getStatus().equals(StatusLancamento.BALANCEADO));
			
			if (gerarHistoricoLancamento) {
				
				this.gerarHistoricoLancamento(usuario, lancamento);
			}
			
			this.alterararLancamento(produtoLancamento, lancamento, novaData);
			
			this.montarMatrizLancamentosConfirmados(matrizLancamentoConfirmada, produtoLancamento,
													lancamento, novaData);
			
			this.lancamentoRepository.merge(lancamento);	
		}
		
		return matrizLancamentoConfirmada;
	}

	private void validarDadosAtualizacaoLancamento(Set<Long> idsLancamento,
												   List<Lancamento> listaLancamentos) {
		
		if (listaLancamentos == null || listaLancamentos.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado!");
		}
		
		if (idsLancamento.size() != listaLancamentos.size()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado!");
		}
	}
	
	private void efetivarAgrupamentoLancamento(Lancamento lancamentoAtualizar,
											   Lancamento lancamentoAgrupar) {
		
		lancamentoAtualizar.setEstudo(
			this.agruparEstudo(lancamentoAtualizar, lancamentoAgrupar));
		
		lancamentoAtualizar.setRecebimentos(
			this.agruparRecebimentos(lancamentoAtualizar, lancamentoAgrupar));
		
		this.agruparLancamento(lancamentoAtualizar, lancamentoAgrupar);
	}
	
	private void agruparLancamento(Lancamento lancamentoAtualizar,
								   Lancamento lancamentoAgrupar) {

		BigInteger somaReparte =
			BigIntegerUtil.soma(lancamentoAtualizar.getReparte(), lancamentoAgrupar.getReparte());
		
		lancamentoAtualizar.setReparte(somaReparte);
		
		BigInteger somaRepartePromocional =
				BigIntegerUtil.soma(lancamentoAtualizar.getRepartePromocional(),
								lancamentoAgrupar.getRepartePromocional());
			
		lancamentoAtualizar.setRepartePromocional(somaRepartePromocional);
	}
	
	private Estudo agruparEstudo(Lancamento lancamentoAtualizar,
								 Lancamento lancamentoAgrupar) {
		
		Estudo estudoAtualizar = lancamentoAtualizar.getEstudo();
		
		Estudo estudoAgrupar = lancamentoAgrupar.getEstudo();
		
		if (estudoAgrupar == null) {
		
			return estudoAtualizar;
		}
		
		if (estudoAtualizar != null) {
		
			BigInteger somaQtdeReparte =
				BigIntegerUtil.soma(estudoAtualizar.getQtdeReparte(), estudoAgrupar.getQtdeReparte());
			
			estudoAtualizar.setQtdeReparte(somaQtdeReparte);
		
		} else {
			
			estudoAtualizar = new Estudo();
			
			estudoAtualizar.setDataLancamento(estudoAgrupar.getDataLancamento());
			estudoAtualizar.setProdutoEdicao(estudoAgrupar.getProdutoEdicao());
			estudoAtualizar.setQtdeReparte(estudoAgrupar.getQtdeReparte());
			estudoAtualizar.setStatus(StatusLancamento.ESTUDO_FECHADO);
			estudoAtualizar.setDataCadastro(new Date());
		}
		
		Set<EstudoCota> estudoCotasAgrupar = estudoAgrupar.getEstudoCotas();
		
		if (estudoCotasAgrupar == null || estudoCotasAgrupar.isEmpty()) {
			
			return estudoAtualizar;
		}
		
		Set<EstudoCota> estudoCotasAtualizar = estudoAtualizar.getEstudoCotas();
		
		estudoAtualizar.setEstudoCotas(
			this.agruparEstudoCota(estudoAtualizar, estudoCotasAtualizar, estudoCotasAgrupar));
		
		return estudoAtualizar;
	}
	
	private Set<EstudoCota> agruparEstudoCota(Estudo estudoAtualizar,
											  Set<EstudoCota> estudoCotasAtualizar,
 			  								  Set<EstudoCota> estudoCotasAgrupar) {
		
		for (EstudoCota estudoCotaAgrupar : estudoCotasAgrupar) {
			
			boolean existeEstudoCota = false;

			if (estudoCotasAtualizar == null) {
				
				estudoCotasAtualizar = new TreeSet<EstudoCota>();
			}
			
			for (EstudoCota estudoCotaAtualizar : estudoCotasAtualizar) {
	
				if (estudoCotaAtualizar.getCota().getId().equals(estudoCotaAgrupar.getCota().getId())) {
					
					existeEstudoCota = true;
					
					estudoCotaAtualizar.setQtdeEfetiva(
						estudoCotaAtualizar.getQtdeEfetiva().add(estudoCotaAgrupar.getQtdeEfetiva()));
					
					estudoCotaAtualizar.setQtdePrevista(
						estudoCotaAtualizar.getQtdePrevista().add(estudoCotaAgrupar.getQtdePrevista()));
					
					break;
				}
			}
			
			if (!existeEstudoCota) {
				
				EstudoCota estudoCotaAtualizar = new EstudoCota();
				
				estudoCotaAtualizar.setCota(estudoCotaAgrupar.getCota());
				estudoCotaAtualizar.setQtdeEfetiva(estudoCotaAgrupar.getQtdeEfetiva());
				estudoCotaAtualizar.setQtdePrevista(estudoCotaAgrupar.getQtdePrevista());
				estudoCotaAtualizar.setEstudo(estudoAtualizar);
				
				estudoCotasAtualizar.add(estudoCotaAtualizar);
			}
		}
		
		return estudoCotasAtualizar;
	}

	private Set<ItemRecebimentoFisico> agruparRecebimentos(Lancamento lancamentoAtualizar,
									 					   Lancamento lancamentoAgrupar) {
		
		Set<ItemRecebimentoFisico> recebimentosAtualizar = lancamentoAtualizar.getRecebimentos();
		
		Set<ItemRecebimentoFisico> recebimentosAgrupar = lancamentoAgrupar.getRecebimentos();
		
		if (recebimentosAgrupar == null || recebimentosAgrupar.isEmpty()) {
			
			return recebimentosAtualizar;
		}
		
		if (recebimentosAtualizar == null) {
			
			recebimentosAtualizar = new TreeSet<ItemRecebimentoFisico>();
		}
		
		recebimentosAtualizar.addAll(recebimentosAgrupar);
		
		return recebimentosAtualizar;
	}

	private void tratarLancamentosAgrupadosParaConfirmacao(Lancamento lancamentoAtualizar,
										 				   Date novaData,
										 				   List<Lancamento> lancamentos,
										 				   Map<Long, ProdutoLancamentoDTO> mapaLancamento) {
		
		ProdutoLancamentoDTO produtoLancamento = mapaLancamento.get(lancamentoAtualizar.getId());
		
		for (ProdutoLancamentoDTO produtoLancamentoAgrupado
				: produtoLancamento.getProdutosLancamentoAgrupados()) {
			
			Lancamento lancamentoAgrupar =
				this.obterLancamentoDaLista(produtoLancamentoAgrupado.getIdLancamento(),
										    lancamentos);
			
			this.efetivarAgrupamentoLancamento(lancamentoAtualizar, lancamentoAgrupar);
			
			try {
				
				lancamentoRepository.remover(lancamentoAgrupar);
			
			} catch (Exception e) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"Erro ao excluir o lançamento do Produto: "
						+ produtoLancamento.getNomeProduto()
						+ " - Edição: " + produtoLancamento.getNumeroEdicao());
			}
		}
		
		produtoLancamento.getProdutosLancamentoAgrupados().clear();
	}
	
	private Lancamento obterLancamentoDaLista(Long idLancamento,
											  List<Lancamento> lancamentos) {
		
		for (Lancamento lancamento : lancamentos) {
			
			if (lancamento.getId().equals(idLancamento)) {
				
				return lancamento;
			}
		}
		
		return null;
	}

	private void alterararLancamento(ProdutoLancamentoDTO produtoLancamento,
									 Lancamento lancamento, Date novaData) {
		
		if (lancamento.getDataLancamentoDistribuidor().compareTo(novaData) != 0) {
			lancamento.setNumeroReprogramacoes(atualizarNumeroReprogramacoes(lancamento));
		}
		
		lancamento.setDataLancamentoDistribuidor(novaData);
		lancamento.setStatus(StatusLancamento.BALANCEADO);
		lancamento.setDataStatus(new Date());		
	}

	private void gerarHistoricoLancamento(Usuario usuario, Lancamento lancamento) {
		
		HistoricoLancamento historicoLancamento = new HistoricoLancamento();
		
		historicoLancamento.setLancamento(lancamento);
		historicoLancamento.setTipoEdicao(TipoEdicao.ALTERACAO);
		historicoLancamento.setStatus(lancamento.getStatus());
		historicoLancamento.setDataEdicao(new Date());
		historicoLancamento.setResponsavel(usuario);
		
		this.historicoLancamentoRepository.merge(historicoLancamento);
	}
	
	private void montarMatrizLancamentosConfirmados(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoConfirmada,
													ProdutoLancamentoDTO produtoLancamento,
													Lancamento lancamento,
													Date novaData) {
		
		if (produtoLancamento.isLancamentoAgrupado()) {

			return;
		}

		produtoLancamento.setDataLancamentoDistribuidor(novaData);
		produtoLancamento.setStatusLancamento(StatusLancamento.BALANCEADO.toString());
		produtoLancamento.setNumeroReprogramacoes(lancamento.getNumeroReprogramacoes());
	
		List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamentoConfirmada.get(novaData);
		
		if (produtosLancamento == null) {
			
			produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		}
		
		produtosLancamento.add(produtoLancamento);
		
		matrizLancamentoConfirmada.put(novaData, produtosLancamento);
	}
	
	private Integer atualizarNumeroReprogramacoes(Lancamento lancamento) {
		
		Integer numeroReprogramacoes = lancamento.getNumeroReprogramacoes();
		
		if (numeroReprogramacoes == null) {
			
			numeroReprogramacoes = 0;
		}
		
		numeroReprogramacoes++;
		
		return numeroReprogramacoes;
	}
	
	/**
	 * Valida o filtro informado para realizar o balanceamento.
	 */
	private void validarFiltro(FiltroLancamentoDTO filtro) {
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Os dados do filtro devem ser informados!");
			
		} else {
		
			List<String> mensagens = new ArrayList<String>();
			
			if (filtro.getData() == null) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (filtro.getIdsFornecedores() == null
					|| filtro.getIdsFornecedores().isEmpty()) {
				
				mensagens.add("Os dados do filtro da tela devem ser informados!");
			}
			
			if (!mensagens.isEmpty()) {
				
				ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
				
				throw new ValidacaoException(validacaoVO);
			}
		}
	}
	
	/**
	 * Efetua todas as etapas para a realização do balanceamento da matriz de lançamento.
	 */
	private BalanceamentoLancamentoDTO balancear(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		this.validarDadosEntradaBalanceamento(dadosBalanceamentoLancamento);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = new BalanceamentoLancamentoDTO();
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento = null;
		
		matrizLancamento = this.gerarMatrizBalanceamentoLancamento(dadosBalanceamentoLancamento);
		
		balanceamentoLancamento.setMatrizLancamento(matrizLancamento);
		
		balanceamentoLancamento.setCapacidadeDistribuicao(
			dadosBalanceamentoLancamento.getCapacidadeDistribuicao());
		
		balanceamentoLancamento.setNumeroSemana(dadosBalanceamentoLancamento.getNumeroSemana());
		
		balanceamentoLancamento.setDataLancamento(
			dadosBalanceamentoLancamento.getDataLancamento());
		
		return balanceamentoLancamento;
	}
	
	/**
	 * Valida os dados de entrada para realização do balanceamento.
	 */
	private void validarDadosEntradaBalanceamento(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		if (dadosBalanceamentoLancamento == null
				|| dadosBalanceamentoLancamento.getCapacidadeDistribuicao() == null
				|| dadosBalanceamentoLancamento.getDatasDistribuicaoFornecedor() == null
				|| dadosBalanceamentoLancamento.getDatasExpectativaReparte() == null
				|| dadosBalanceamentoLancamento.getProdutosLancamento() == null
				|| dadosBalanceamentoLancamento.getQtdDiasLimiteParaReprogLancamento() == null) {
			
			throw new RuntimeException("Dados para efetuar balanceamento inválidos!");
		}
	}
	
	/**
	 * Gera o mapa contendo a matriz de balanceamento de lançamento.
	 */
	private TreeMap<Date, List<ProdutoLancamentoDTO>> gerarMatrizBalanceamentoLancamento(
												DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceadosTotal = 
			new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoBalancear =
			this.processarProdutosLancamentoNaoBalanceaveis(matrizLancamento,
															dadosBalanceamentoLancamento);
		
		Set<Date> datasConfirmadas = this.obterDatasConfirmadas(matrizLancamento);
		
		TreeSet<Date> datasDistribuicao =
			this.obterDatasDistribuicao(dadosBalanceamentoLancamento, datasConfirmadas);
		
		Set<Date> datasExpectativaReparte =
			dadosBalanceamentoLancamento.getDatasExpectativaReparte();
		
		Set<Date> datasExpectativaReparteOrdenado =
			ordenarMapaExpectativaRepartePorDatasDistribuicao(datasExpectativaReparte,
															  datasDistribuicao);
		
		for (Date dataLancamentoPrevista : datasExpectativaReparteOrdenado) {
			
			List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
				this.processarProdutosLancamentoBalanceaveis(matrizLancamento,
															 datasDistribuicao,
															 dataLancamentoPrevista,
															 dadosBalanceamentoLancamento,
															 produtosLancamentoBalancear);
			
			if (produtosLancamentoNaoBalanceados != null
					&& !produtosLancamentoNaoBalanceados.isEmpty()) {
				
				produtosLancamentoNaoBalanceadosTotal.addAll(produtosLancamentoNaoBalanceados);
			}
		}
		
		if (!produtosLancamentoNaoBalanceadosTotal.isEmpty()) {
		
			this.processarProdutosLancamentoNaoBalanceados(matrizLancamento,
														   produtosLancamentoNaoBalanceadosTotal,
														   datasDistribuicao,
														   dadosBalanceamentoLancamento);
		}
		
		return matrizLancamento;
	}

	private Set<Date> obterDatasConfirmadas(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		Set<Date> datasConfirmadas = new TreeSet<>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			for (ProdutoLancamentoDTO produtoLancamento : entry.getValue()) {
				
				datasConfirmadas.add(entry.getKey());
				
				boolean balanceamentoConfirmado = produtoLancamento.isBalanceamentoConfirmado();
				
				if (!balanceamentoConfirmado) {
					
					datasConfirmadas.remove(entry.getKey());
					
					break;
				}
			}
		}
		
		return datasConfirmadas;
	}

	/**
	 * Obtém as datas de distribuição, desconsiderando as datas em que o balanceamento já foi confirmado.
	 */
	private TreeSet<Date> obterDatasDistribuicao(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
												 Set<Date> datasConfirmadas) {
		
		TreeSet<Date> datasDistribuicao =
			dadosBalanceamentoLancamento.getDatasDistribuicaoFornecedor();
		
		for (Date dataConfirmada : datasConfirmadas) {
			
			datasDistribuicao.remove(dataConfirmada);
		}
		
		return datasDistribuicao;
	}
	
	/**
	 * Efetua a ordenação do set de expectativa de reparte de acordo com as datas
	 * de distribuição passadas como parâmetro.
	 */
	private Set<Date> ordenarMapaExpectativaRepartePorDatasDistribuicao(
													Set<Date> datasExpectativaReparte, 
													TreeSet<Date> datasDistribuicao) {
		
		Set<Date> datasExpectativaReparteOrdenado =
			new LinkedHashSet<Date>();
		
		for (Date dataDistribuicao : datasDistribuicao) {

			if (datasExpectativaReparte.contains(dataDistribuicao)) {
				
				datasExpectativaReparteOrdenado.add(
					dataDistribuicao);
				
				datasExpectativaReparte.remove(dataDistribuicao);
			}
		}
		
		datasExpectativaReparteOrdenado.addAll(datasExpectativaReparte);
		
		return datasExpectativaReparteOrdenado;
	}
	
	/**
	 * Processa os produtos para lançamento que não devem ser balanceados
	 * e adiciona os mesmos no mapa da matriz de balanceamento.
	 */
	private List<ProdutoLancamentoDTO> processarProdutosLancamentoNaoBalanceaveis(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														    DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados =
			this.processarProdutosLancamentoConfirmados(matrizLancamento, dadosLancamentoBalanceamento);
		
		produtosLancamentoNaoProcessados =
			this.processarProdutosLancamentoComLimiteReprogramacoes(
				matrizLancamento, dadosLancamentoBalanceamento, produtosLancamentoNaoProcessados);
		
		return produtosLancamentoNaoProcessados;
	}

	private List<ProdutoLancamentoDTO> processarProdutosLancamentoConfirmados(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados = new ArrayList<>();
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			dadosLancamentoBalanceamento.getProdutosLancamento();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
			
			if (produtoLancamento.isBalanceamentoConfirmado()) {

				this.adicionarProdutoLancamentoNaMatriz(
					matrizLancamento, produtoLancamento, dataLancamentoDistribuidor);
			
			} else {
			
				produtosLancamentoNaoProcessados.add(produtoLancamento);
			}
		}
		
		return produtosLancamentoNaoProcessados;
	}
	
	private List<ProdutoLancamentoDTO> processarProdutosLancamentoComLimiteReprogramacoes(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento,
											List<ProdutoLancamentoDTO> produtosLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados = new ArrayList<>();
		
		Set<Date> datasConfirmadas = this.obterDatasConfirmadas(matrizLancamento);
		
		TreeSet<Date> datasDistribuicao =
			this.obterDatasDistribuicao(dadosLancamentoBalanceamento, datasConfirmadas);
		
		Date dataLancamentoEscolhida = null;
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
		
			if (produtoLancamento.excedeNumeroReprogramacoes()) {
				
				if (datasDistribuicao.contains(dataLancamentoDistribuidor)) {
					
					dataLancamentoEscolhida = dataLancamentoDistribuidor;
				
				} else {
				
					 dataLancamentoEscolhida =
						 this.obterDataDistribuicaoEscolhida(
							matrizLancamento, datasDistribuicao, dataLancamentoDistribuidor);
				}
				
				if (dataLancamentoEscolhida != null) {
					
					this.adicionarProdutoLancamentoNaMatriz(
						matrizLancamento, produtoLancamento, dataLancamentoEscolhida);
					
					continue;
				}
			}
			
			produtosLancamentoNaoProcessados.add(produtoLancamento);
		}
		
		return produtosLancamentoNaoProcessados;
	}

	/**
	 * Processa os produtos para lançamento que devem ser balanceados
	 * e adiciona os mesmos no mapa da matriz de balanceamento.
	 */
	private List<ProdutoLancamentoDTO> processarProdutosLancamentoBalanceaveis(
											    TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
												TreeSet<Date> datasDistribuicao,
												Date dataLancamentoPrevista,
												DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
												List<ProdutoLancamentoDTO> produtosLancamento) {
		
		Date dataLancamentoEscolhida =
			this.obterDataDistribuicaoEscolhida(matrizLancamento,
											    datasDistribuicao,
											    dataLancamentoPrevista);
		
		if (dataLancamentoEscolhida == null) {
			
			return null;
		}
		
		List<ProdutoLancamentoDTO> produtosLancamentoDataEscolhida =
			matrizLancamento.get(dataLancamentoEscolhida);
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
			new ArrayList<ProdutoLancamentoDTO>();
		
		List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveisDataPrevista = 
			this.obterProdutosLancamentoBalanceaveisPorData(produtosLancamento,
															dataLancamentoPrevista);
		
		BigInteger expectativaReparteDataEscolhida =
			this.obterExpectativaReparteTotal(produtosLancamentoDataEscolhida);
		
		produtosLancamentoNaoBalanceados =
			this.balancearProdutosLancamento(
				matrizLancamento, produtosLancamentoBalanceaveisDataPrevista, dadosBalanceamentoLancamento,
				expectativaReparteDataEscolhida, dataLancamentoEscolhida,
				dadosBalanceamentoLancamento.getCapacidadeDistribuicao(), false);
		
		return produtosLancamentoNaoBalanceados;
	}
	
	/**
	 * Obtém uma data de distribuição de acordo as datas de distribuição permitidas.
	 * Ordem de tentativa de escolha da data:
	 * 1º Data igual a data prevista
	 * 2º Menor data que ainda não possui nenhum produto balanceado
	 * 3º Data que possui menor quantidade de expectativa de reparte balanceado
	 */
	private Date obterDataDistribuicaoEscolhida(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
			  									TreeSet<Date> datasDistribuicao,
			  									Date dataLancamentoPrevista) {
		
		Date dataLancamentoEscolhida = null;
		
		if (datasDistribuicao.contains(dataLancamentoPrevista)) {
			
			dataLancamentoEscolhida = dataLancamentoPrevista;
		}
		
		if (dataLancamentoEscolhida == null) {
			
			for (Date dataDistribuicao : datasDistribuicao) {
				
				List<ProdutoLancamentoDTO> produtosLancamento =
					matrizLancamento.get(dataDistribuicao);
				
				if (produtosLancamento == null) {
					
					dataLancamentoEscolhida = dataDistribuicao;
					
					break;
				}
			}
		}
		
		if (dataLancamentoEscolhida == null) {
			
			BigInteger menorExpectativaReparte = null;
			
			for (Date dataDistribuicao : datasDistribuicao) {
				
				List<ProdutoLancamentoDTO> produtosLancamento =
					matrizLancamento.get(dataDistribuicao);
				
				BigInteger expectativaReparteData = this.obterExpectativaReparteTotal(produtosLancamento);
				
				if (menorExpectativaReparte == null
						|| expectativaReparteData.compareTo(menorExpectativaReparte) == -1) {
					
					menorExpectativaReparte = expectativaReparteData;
					
					dataLancamentoEscolhida = dataDistribuicao;
				}
			}
		}
		
		return dataLancamentoEscolhida;
	}
	
	/**
	 * Obtém os produtos balanceáveis de uma determinada data.
	 */
	private List<ProdutoLancamentoDTO> obterProdutosLancamentoBalanceaveisPorData(
													List<ProdutoLancamentoDTO> produtosLancamento,
													Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoFiltrados = new ArrayList<ProdutoLancamentoDTO>();
				
		if (produtosLancamento == null 
				|| produtosLancamento.isEmpty()
				|| dataLancamento == null) {
			
			return produtosLancamentoFiltrados;
		}
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
				
			if (produtoLancamento.getDataLancamentoDistribuidor().equals(dataLancamento)) {
				
				produtosLancamentoFiltrados.add(produtoLancamento);
			}
		}
		
		this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoFiltrados);
		
		return produtosLancamentoFiltrados;
	}
	
	/**
	 * Obtém a expectativa de reparte total dos produtos para lançamento.
	 */
	private BigInteger obterExpectativaReparteTotal(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		BigInteger expectativaReparteTotal = BigInteger.ZERO;
		
		if (produtosLancamento != null) {
			
			for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
				
				if (produtoLancamento.isLancamentoAgrupado()) {
					
					continue;
				}
				
				if (produtoLancamento.getRepartePrevisto() != null) {
					
					expectativaReparteTotal = 
						expectativaReparteTotal.add(produtoLancamento.getRepartePrevisto());
				}
			}
		}
		
		return expectativaReparteTotal;
	}
	
	/**
	 * Método que efetua o balanceamento de acordo com os parâmetros informados.
	 */
	private List<ProdutoLancamentoDTO> balancearProdutosLancamento(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
											DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
											BigInteger expectativaReparteDataAtual,
											Date dataLancamento,
											BigInteger capacidadeDistribuicao,
											boolean permiteExcederCapacidadeDistribuicao) {
		
		Integer qtdDiasLimiteParaReprogLancamento =
			dadosBalanceamentoLancamento.getQtdDiasLimiteParaReprogLancamento();
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
			new ArrayList<ProdutoLancamentoDTO>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamentoBalanceaveis) {
			
			if (permiteExcederCapacidadeDistribuicao
					|| (!this.excedeLimiteDataReprogramacao(
							produtoLancamento, qtdDiasLimiteParaReprogLancamento, dataLancamento)
						&& !this.excedeCapacidadeDistribuidor(
								expectativaReparteDataAtual, produtoLancamento, capacidadeDistribuicao))) {
				
				expectativaReparteDataAtual =
					expectativaReparteDataAtual.add(produtoLancamento.getRepartePrevisto());
				
				this.adicionarProdutoLancamentoNaMatriz(matrizLancamento,
														produtoLancamento,
				   										dataLancamento);
			
			} else {
				
				produtosLancamentoNaoBalanceados.add(produtoLancamento);
			}
		}
		
		return produtosLancamentoNaoBalanceados;
	}
	
	/**
	 * Valida se a data de lançamento excede a data limite para reprogramação do produto.
	 */
	private boolean excedeLimiteDataReprogramacao(ProdutoLancamentoDTO produtoLancamento,
												  Integer qtdDiasLimiteParaReprogLancamento,
												  Date dataLancamento) {
		
		Date dataLimiteReprogramacao =
			DateUtil.subtrairDias(produtoLancamento.getDataRecolhimentoPrevista(),
								  qtdDiasLimiteParaReprogLancamento);
		
		return (dataLancamento.compareTo(dataLimiteReprogramacao) == 1);
	}
	
	/**
	 * Valida se o produto informado excede a capacidade de distribuição no dia.
	 */
	private boolean excedeCapacidadeDistribuidor(BigInteger expectativaReparteDataAtual,
											     ProdutoLancamentoDTO produtoLancamento,
											     BigInteger capacidadeDistribuicao) {
		
		expectativaReparteDataAtual = 
			expectativaReparteDataAtual.add(produtoLancamento.getRepartePrevisto());
		
		return (expectativaReparteDataAtual.compareTo(capacidadeDistribuicao) == 1);
	}
	
	/**
	 * Processa os produtos que não puderam ser balanceados
	 * pois estes excederam a capacidade de distribuição.
	 */
	private void processarProdutosLancamentoNaoBalanceados(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														   List<ProdutoLancamentoDTO> produtosLancamentoBalancear,
														   TreeSet<Date> datasDistribuicao,
														   DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		BigInteger capacidadeDistribuicaoExcedente = 
			this.obterCapacidadeDistribuicaoExcedente(
				produtosLancamentoBalancear, datasDistribuicao, dadosBalanceamentoLancamento);
		
		Map<Date, BigInteger> mapaExpectativaReparteTotalDiariaAtual = null;
		
		long quantidadeProdutosBalancear = produtosLancamentoBalancear.size();
		
		long quantidadeProdutosNaoBalanceados = 0;
		
		while (!produtosLancamentoBalancear.isEmpty()
					&& quantidadeProdutosBalancear != quantidadeProdutosNaoBalanceados) {
		
			quantidadeProdutosBalancear = produtosLancamentoBalancear.size();
			
			mapaExpectativaReparteTotalDiariaAtual = 
				this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																			datasDistribuicao);
			
			this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoBalancear);
			
			produtosLancamentoBalancear =
				this.alocarSobrasMatrizLancamento(matrizLancamento,
											 	  produtosLancamentoBalancear,
											 	  mapaExpectativaReparteTotalDiariaAtual,
											 	  dadosBalanceamentoLancamento,
											 	  capacidadeDistribuicaoExcedente,
											 	  false);
			
			quantidadeProdutosNaoBalanceados = produtosLancamentoBalancear.size();
		}
		
		mapaExpectativaReparteTotalDiariaAtual = 
			this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																		datasDistribuicao);
		
		this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoBalancear);
		
		this.alocarSobrasMatrizLancamento(matrizLancamento,
									 	  produtosLancamentoBalancear,
									 	  mapaExpectativaReparteTotalDiariaAtual,
									 	  dadosBalanceamentoLancamento,
									 	  capacidadeDistribuicaoExcedente,
									 	  true);
	}

	private BigInteger obterCapacidadeDistribuicaoExcedente(
										List<ProdutoLancamentoDTO> produtosLancamentoBalancear,
										TreeSet<Date> datasDistribuicao,
										DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		BigInteger reparteTotalBalancear =
			this.obterExpectativaReparteTotal(produtosLancamentoBalancear);
		
		BigInteger capacidadeDistribuicaoExcedente = null;
		
		BigInteger capacidadeDistribuicao = dadosBalanceamentoLancamento.getCapacidadeDistribuicao();
		
		BigInteger totalDiasDistribuicao = BigInteger.valueOf(datasDistribuicao.size());
		
		BigInteger mediaReparteExcedente = reparteTotalBalancear.divide(totalDiasDistribuicao);
		
		capacidadeDistribuicaoExcedente = capacidadeDistribuicao.add(mediaReparteExcedente);
		
		return capacidadeDistribuicaoExcedente;
	}
	
	/**
	 * Ordena os produtos informados por periodicidade do produto
	 * e pela expectativa de reparte.
	 */
	@SuppressWarnings("unchecked")
	private void ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(
														List<ProdutoLancamentoDTO> produtosLancamento) {
		
		ComparatorChain comparatorChain = new ComparatorChain();
		
		comparatorChain.addComparator(new BeanComparator("ordemPeriodicidadeProduto"));
		comparatorChain.addComparator(new BeanComparator("repartePrevisto"), true);
		
		Collections.sort(produtosLancamento, comparatorChain);
	}

	/**
	 * Gera o mapa de expectativa de reparte diário ordenado pela maior data.
	 */
	private Map<Date, BigInteger> gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(
														TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														TreeSet<Date> datasDistribuicao) {
		
		Map<Date, BigInteger> mapaExpectativaReparteTotalDiaria = 
			new TreeMap<Date, BigInteger>();
		
		for (Date dataDistribuicao : datasDistribuicao) {
			
			List<ProdutoLancamentoDTO> produtosLancamento =
				matrizLancamento.get(dataDistribuicao);
			
			BigInteger expectativaReparteData =
				this.obterExpectativaReparteTotal(produtosLancamento);
			
			mapaExpectativaReparteTotalDiaria.put(dataDistribuicao, expectativaReparteData);
		}
		
		return mapaExpectativaReparteTotalDiaria;
	}

	/**
	 * Aloca na matriz de balanceamento os produtos que não foram balanceados anteriormente. 
	 */
	private List<ProdutoLancamentoDTO> alocarSobrasMatrizLancamento(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveis,
											Map<Date, BigInteger> mapaExpectativaReparteTotalDiariaAtual,
											DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento,
											BigInteger capacidadeDistribuicao,
											boolean permiteExcederCapacidadeDistribuicao) {
		
		for (Map.Entry<Date, BigInteger> entry : mapaExpectativaReparteTotalDiariaAtual.entrySet()) {
			
			if (produtosLancamentoBalanceaveis == null 
					|| produtosLancamentoBalanceaveis.isEmpty()) {
				
				break;
			}
			
			Date dataLancamento = entry.getKey();
			
			BigInteger expectativaReparteDataAtual = entry.getValue();
			
			produtosLancamentoBalanceaveis =
				this.balancearProdutosLancamento(
					matrizLancamento, produtosLancamentoBalanceaveis, dadosBalanceamentoLancamento,
					expectativaReparteDataAtual, dataLancamento, capacidadeDistribuicao,
					permiteExcederCapacidadeDistribuicao);
		}
		
		return produtosLancamentoBalanceaveis;
	}
	
	/**
	 * Adiciona o produto informado na matriz de balanceamento na data informada.
	 */
	private void adicionarProdutoLancamentoNaMatriz(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
													ProdutoLancamentoDTO produtoLancamento,
													Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoMatriz = 
			matrizLancamento.get(dataLancamento);
		
		if (produtosLancamentoMatriz == null) {
			
			produtosLancamentoMatriz = new ArrayList<ProdutoLancamentoDTO>();
		}
		
		produtoLancamento.setNovaDataLancamento(dataLancamento);
		produtoLancamento.setDataLancamentoDistribuidor(dataLancamento);
		
		this.tratarAgrupamentoPorProdutoDataLcto(produtoLancamento, produtosLancamentoMatriz);
		
		produtosLancamentoMatriz.add(produtoLancamento);
		
		matrizLancamento.put(dataLancamento, produtosLancamentoMatriz);
	}
	
	@Override
	public void tratarAgrupamentoPorProdutoDataLcto(ProdutoLancamentoDTO produtoLancamentoAdicionar,
  			 										List<ProdutoLancamentoDTO> produtosLancamento) {
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			if (produtoLancamentoAdicionar.isLancamentoAgrupado()) {
				
				continue;
			}
			
			if (!produtoLancamento.isLancamentoAgrupado()
					&& !produtoLancamentoAdicionar.getIdLancamento().equals(produtoLancamento.getIdLancamento())
					&& produtoLancamentoAdicionar.getIdProdutoEdicao().equals(produtoLancamento.getIdProdutoEdicao())
					&& produtoLancamentoAdicionar.getNovaDataLancamento().equals(produtoLancamento.getNovaDataLancamento())) {
				
				if (this.isIdPrimeiroLancamentoMaior(produtoLancamentoAdicionar, produtoLancamento)) {
					
					this.agruparProdutos(produtoLancamento, produtoLancamentoAdicionar);
					
				} else {
					
					this.agruparProdutos(produtoLancamentoAdicionar, produtoLancamento);
				}
			}
		}
	}
	
	private boolean isIdPrimeiroLancamentoMaior(ProdutoLancamentoDTO produtoLancamentoAdicionar,
		   							ProdutoLancamentoDTO produtoLancamento) {
		
		if (produtoLancamentoAdicionar.getIdLancamento().compareTo(produtoLancamento.getIdLancamento()) == 1) {
			
			return true;
		}
		
		return false;
	}
	
	private void agruparProdutos(ProdutoLancamentoDTO produtoLancamento,
			 					 ProdutoLancamentoDTO produtoLancamentoAgrupar) {
	
		BigInteger repartePreviso =
			produtoLancamento.getRepartePrevisto().add(produtoLancamentoAgrupar.getRepartePrevisto());
		
		BigInteger reparteFisico = BigInteger.ZERO;
		BigInteger reparteFisicoAgrupar = BigInteger.ZERO;
		
		reparteFisico =
			(produtoLancamento.getReparteFisico() != null)
				? produtoLancamento.getReparteFisico() : BigInteger.ZERO;
		
		reparteFisicoAgrupar =
			(produtoLancamentoAgrupar.getReparteFisico() != null)
				? produtoLancamentoAgrupar.getReparteFisico() : BigInteger.ZERO;
		
		BigDecimal valorTotal =
			produtoLancamento.getValorTotal().add(produtoLancamentoAgrupar.getValorTotal());
		
		produtoLancamento.setRepartePrevisto(repartePreviso);
		produtoLancamento.setReparteFisico(reparteFisico.add(reparteFisicoAgrupar));
		produtoLancamento.setValorTotal(valorTotal);
		
		if (!produtoLancamentoAgrupar.getProdutosLancamentoAgrupados().isEmpty()) {
			
			produtoLancamento.getProdutosLancamentoAgrupados().addAll(
				produtoLancamentoAgrupar.getProdutosLancamentoAgrupados());
		}
		
		produtoLancamento.getProdutosLancamentoAgrupados().add(produtoLancamentoAgrupar);
		
		produtoLancamentoAgrupar.setLancamentoAgrupado(true);
	}
	
	/**
	 * Verifica se o produto é balanceável ou não.
	 */
	public boolean isProdutoBalanceavel(ProdutoLancamentoDTO produtoLancamento) {
		
		if (produtoLancamento.excedeNumeroReprogramacoes()) {
			
			return false;
		}
		
		if (produtoLancamento.isBalanceamentoConfirmado()) {
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private DadosBalanceamentoLancamentoDTO obterDadosLancamento(FiltroLancamentoDTO filtro) {
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			new DadosBalanceamentoLancamentoDTO();
		
		Date dataLancamento = filtro.getData();
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, dataLancamento, numeroSemana);
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicaoFornecedor(periodoDistribuicao, filtro.getIdsFornecedores());
		
		dadosBalanceamentoLancamento.setNumeroSemana(numeroSemana);
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedor(
			datasDistribuicaoFornecedor);
		
		dadosBalanceamentoLancamento.setCapacidadeDistribuicao(
			distribuidor.getCapacidadeDistribuicao());
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			this.lancamentoRepository.obterBalanceamentoLancamento(periodoDistribuicao,
																   filtro.getIdsFornecedores());
		
		dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);
		
		Set<Date> datasExpectativaReparte = new LinkedHashSet<Date>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			datasExpectativaReparte.add(produtoLancamento.getDataLancamentoDistribuidor());
		}
		
		dadosBalanceamentoLancamento.setDatasExpectativaReparte(datasExpectativaReparte);
		
		// TODO: a regra quanto à utilização desse parâmetro será tratada em um próximo ajuste
		dadosBalanceamentoLancamento.setQtdDiasLimiteParaReprogLancamento(
			distribuidor.getQtdDiasLimiteParaReprogLancamento());
		
		dadosBalanceamentoLancamento.setDataLancamento(dataLancamento);
		
		return dadosBalanceamentoLancamento;
	}

	/**
	 * Monta o perídodo da semana de distribuição referente à data informada.
	 */
	private Intervalo<Date> getPeriodoDistribuicao(Distribuidor distribuidor,
												   Date dataLancamento,
												   int numeroSemana) {
		
		Date dataInicialSemana =
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana(), dataLancamento);
		
		Date dataFinalSemana =
			DateUtil.adicionarDias(dataInicialSemana, 6);
		
		Intervalo<Date> periodo = new Intervalo<Date>(dataInicialSemana, dataFinalSemana);
		
		return periodo;
	}
	
	/**
	 * Obtém as datas de distribuição dos fornecedores informados.
	 */
	private TreeSet<Date> obterDatasDistribuicaoFornecedor(Intervalo<Date> periodoDistribuicao,
														   List<Long> listaIdsFornecedores) {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor = 
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				listaIdsFornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		
		if (listaDistribuicaoFornecedor == null || listaDistribuicaoFornecedor.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING ,
										 "Dias de distribuição para os fornecedores não encontrados!");
		}
		
		Set<Integer> codigosDiaSemanaFornecedor = new TreeSet<Integer>();
		
		for (DistribuicaoFornecedor distribuicaoFornecedor : listaDistribuicaoFornecedor) {
			
			codigosDiaSemanaFornecedor.add(distribuicaoFornecedor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicao(periodoDistribuicao, codigosDiaSemanaFornecedor);
		
		return datasDistribuicaoFornecedor;
	}
	
	/**
	 * Obtém as datas para distribuição no período informado,
	 * de acordo com os códigos dos dias da semana informados.
	 */
	private TreeSet<Date> obterDatasDistribuicao(Intervalo<Date> periodoRecolhimento,
									 			 Set<Integer> codigosDiaSemana) {
		
		TreeSet<Date> datasDistribuicao =
			DateUtil.obterPeriodoDeAcordoComDiasDaSemana(periodoRecolhimento.getDe(),  
														 periodoRecolhimento.getAte(),
														 codigosDiaSemana);
		
		TreeSet<Date> datasDistribuicaoComOperacao = new TreeSet<>();
		
		for (Date data : datasDistribuicao) {
			
			try {
				
				this.verificaDataOperacao(data);
				
				datasDistribuicaoComOperacao.add(data);
				
			} catch (ValidacaoException e) {
				
				continue;
			}
		}
		
		return datasDistribuicaoComOperacao;
	}
	
	public void verificaDataOperacao(Date data) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		
		if (DateUtil.isSabadoDomingo(cal)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de lançamento deve ser uma data em que o distribuidor realiza operação!");
		}
		
		if (this.calendarioService.isFeriadoSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de lançamento deve ser uma data em que o distribuidor realiza operação!");
		}
		
		if (this.calendarioService.isFeriadoMunicipalSemOperacao(data)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de lançamento deve ser uma data em que o distribuidor realiza operação!");
		}
	}
	
	@Transactional(readOnly=true)
	public List<ConfirmacaoVO> obterDatasConfirmacao(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();

		Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new LinkedHashMap<Date, Boolean>();

		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {

			Date novaData = produtoLancamento.getNovaDataLancamento();
			
			boolean confirmado =
				(produtoLancamento.isBalanceamentoConfirmado()
					&& (produtoLancamento.getDataLancamentoDistribuidor()
							.compareTo(novaData) == 0));
			
			if (mapaDatasConfirmacaoOrdenada.get(novaData) != null
					&& !mapaDatasConfirmacaoOrdenada.get(novaData)) {
				
				continue;
			}
			
			mapaDatasConfirmacaoOrdenada.put(novaData, confirmado);
		}

		Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada.entrySet();

		for (Entry<Date, Boolean> item : entrySet) {
			
			boolean dataConfirmada = item.getValue();
			
			confirmacoesVO.add(
				new ConfirmacaoVO(DateUtil.formatarDataPTBR(item.getKey()), dataConfirmada));
		}

		return confirmacoesVO;
	}
	
	@Transactional
	public void voltarConfiguracaoInicial(Date dataLancamento, TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		if (dataLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de lançamento não informada!");
		}
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, dataLancamento, numeroSemana);
		
		Set<Date> datasConfirmadas = this.obterDatasConfirmadas(matrizLancamento);
		
		this.voltarConfiguracaoInicialLancamentosPrevistos(periodoDistribuicao, datasConfirmadas);
		
		this.voltarConfiguracaoInicialLancamentosDistribuidor(periodoDistribuicao, datasConfirmadas);
	}
	
	private void voltarConfiguracaoInicialLancamentosPrevistos(Intervalo<Date> periodoDistribuicao,
															   Set<Date> datasConfirmadas) {
		
		List<Lancamento> lancamentos =
			this.lancamentoRepository.obterLancamentosPrevistosPorPeriodo(periodoDistribuicao);
		
		for (Lancamento lancamento : lancamentos) {
			
			//TODO: o trecho abaixo será retirado no momento da CR para inclusão de botão de salva na matriz.
			
//			if (lancamento.getNumeroReprogramacoes() == null
//					|| lancamento.getNumeroReprogramacoes() < Constantes.NUMERO_REPROGRAMACOES_LIMITE) {
//			
//				lancamento.setDataLancamentoDistribuidor(lancamento.getDataLancamentoPrevista());
//			}
			
			if (!datasConfirmadas.contains(lancamento.getDataLancamentoPrevista())) {
				
				lancamento.setDataLancamentoDistribuidor(lancamento.getDataLancamentoPrevista());
				
				lancamento.setStatus(StatusLancamento.CONFIRMADO);
				
				lancamentoRepository.merge(lancamento);
			}
		}
	}
	
	private void voltarConfiguracaoInicialLancamentosDistribuidor(Intervalo<Date> periodoDistribuicao,
		   														  Set<Date> datasConfirmadas) {
		
		List<Lancamento> lancamentos =
			this.lancamentoRepository.obterLancamentosDistribuidorPorPeriodo(periodoDistribuicao);
		
		for (Lancamento lancamento : lancamentos) {
			
			if (!datasConfirmadas.contains(lancamento.getDataLancamentoPrevista())) {
				
				lancamento.setStatus(StatusLancamento.CONFIRMADO);
				
				lancamentoRepository.merge(lancamento);
			}
		}
	}

}
