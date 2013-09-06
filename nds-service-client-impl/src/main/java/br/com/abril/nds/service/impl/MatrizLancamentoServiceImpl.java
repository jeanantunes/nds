package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
	
	private int indiceImpressao;

	@Override
	@Transactional(readOnly = true)
	public BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro) {
	
		this.validarFiltro(filtro);
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento = this.obterDadosLancamento(filtro);
		
		BalanceamentoLancamentoDTO matrizLancamento = this.balancear(dadosBalanceamentoLancamento);
		
		List<ProdutoLancamentoCanceladoDTO> produtosLancamentosCancelados = this.obterProdutosLancamentosCancelados(filtro);
		
		if (produtosLancamentosCancelados != null && !produtosLancamentosCancelados.isEmpty()) {
			matrizLancamento.setProdutosLancamentosCancelados(produtosLancamentosCancelados);
		}
		
		return matrizLancamento;
	}
	
	private List<ProdutoLancamentoCanceladoDTO> obterProdutosLancamentosCancelados(FiltroLancamentoDTO filtro) {
		
		int codigoDiaSemana = 
				this.distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();
		
		Date dataLancamento = filtro.getData();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento, codigoDiaSemana);
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(codigoDiaSemana, dataLancamento, numeroSemana);
		
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
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		this.validarDadosConfirmacao(matrizLancamento);

		Map<Long, ProdutoLancamentoDTO> mapaLancamento = new TreeMap<Long, ProdutoLancamentoDTO>();

		for (Date dataConfirmada : datasConfirmadas) {
			
			List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = matrizLancamento.get(dataConfirmada);
			
			if (listaProdutoLancamentoDTO == null || listaProdutoLancamentoDTO.isEmpty()) {

				continue;
			}
			
			Integer sequenciaMatriz =
				this.lancamentoRepository.obterProximaSequenciaMatrizPorData(dataConfirmada);
			
			this.ordenarProdutos(listaProdutoLancamentoDTO);
			
			for (ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
				
				produtoLancamento.setSequenciaMatriz(sequenciaMatriz++);
				
				Long idLancamento = produtoLancamento.getIdLancamento();
				
				// Monta Map para controlar a atualização dos lançamentos
				
				mapaLancamento.put(idLancamento, produtoLancamento);
			}
		}
		
		if (!mapaLancamento.isEmpty()) {
			
			this.atualizarLancamentos(
				matrizLancamentoRetorno, usuario, mapaLancamento, OperacaoMatrizLancamento.CONFIRMAR);
		}
		
		return matrizLancamentoRetorno;
	}
	
	/**
	 * Efetua a ordenação dos produtos por nome do produto e tipo de lançamento, se produto for parcial respeita a ordem de PARCIAL, FINAL
	 * 
	 * @param produtos
	 */
	private void ordenarProdutos(List<ProdutoLancamentoDTO> produtos) {
		
		Collections.sort(produtos,  new Comparator<ProdutoLancamentoDTO>() {
			
			 public int compare(ProdutoLancamentoDTO p1, ProdutoLancamentoDTO p2) {  
				 
				 int tipoLancamentoP1 = 0;
				 int tipoLancamentoP2 = 0;
				 
				 if(p1.getDescricaoLancamento().equalsIgnoreCase("Parcial")) {
					 tipoLancamentoP1 = 1;
				 }
				 
				 if(p2.getDescricaoLancamento().equalsIgnoreCase("Parcial")) {
					 tipoLancamentoP2 = 1;
				 }
				 
				 if(p1.getDescricaoLancamento().equalsIgnoreCase("Final")) {
					 tipoLancamentoP1 = 2;
				 }
				 
				 if(p2.getDescricaoLancamento().equalsIgnoreCase("Final")) {
					 tipoLancamentoP2 = 2;
				 }
						 
				 return (tipoLancamentoP1 + p1.getNomeProduto()).compareTo(tipoLancamentoP2 + p2.getNomeProduto());
				 
          }  
		});
				
	}
	
	@Override
	@Transactional
	public TreeMap<Date, List<ProdutoLancamentoDTO>> salvarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento, Usuario usuario) {
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno =
			new TreeMap<Date, List<ProdutoLancamentoDTO>>();
		
		this.validarDadosConfirmacao(matrizLancamento);

		Map<Long, ProdutoLancamentoDTO> mapaLancamento = new TreeMap<Long, ProdutoLancamentoDTO>();

		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			Date dataLancamento = entry.getKey();
			List<ProdutoLancamentoDTO> listaProdutoLancamentoDTO = entry.getValue();
			
			if (listaProdutoLancamentoDTO == null || listaProdutoLancamentoDTO.isEmpty()) {

				continue;
			}
			
			for (ProdutoLancamentoDTO produtoLancamento : listaProdutoLancamentoDTO) {
				
				if (!this.isProdutoConfirmado(produtoLancamento)) {
				
					Long idLancamento = produtoLancamento.getIdLancamento();
					
					// Monta Map para controlar a atualização dos lançamentos
					
					mapaLancamento.put(idLancamento, produtoLancamento);
					
				} else {
					
					this.montarMatrizLancamentosConfirmadosRetorno(
						matrizLancamentoRetorno, produtoLancamento, dataLancamento);
				}
			}
		}
		
		if (!mapaLancamento.isEmpty()) {
			
			this.atualizarLancamentos(
				matrizLancamentoRetorno, usuario, mapaLancamento, OperacaoMatrizLancamento.SALVAR);
		}
		
		return matrizLancamentoRetorno;
	}

	private void validarDadosConfirmacao(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		if (matrizLancamento == null || matrizLancamento.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Matriz de lançamento não informada!");
		}
	}
	
	/**
	 * Método que atualiza as informações dos lançamentos.
	 * 
	 * @param matrizLancamentoRetorno
	 * @param idsLancamento - identificadores de lançamentos
	 * @param usuario - usuário
	 * @param mapaLancamento - mapa de lancamentos e produtos de recolhimento
	 * 
	 * @return {@link TreeMap<Date, List<ProdutoLancamentoDTO>>}
	 */
	private void atualizarLancamentos(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamentoRetorno,											
									  Usuario usuario,
									  Map<Long, ProdutoLancamentoDTO> mapaLancamento,
									  OperacaoMatrizLancamento operacaoMatrizLancamento) {
		
		StatusLancamento proximoStatusLancamento =
			this.getProximoStatusLancamentoPorOperacao(operacaoMatrizLancamento);
		
		Set<Long> idsLancamento = mapaLancamento.keySet();
		
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
				!(lancamento.getStatus().equals(proximoStatusLancamento));
			
			if (gerarHistoricoLancamento) {
				
				this.gerarHistoricoLancamento(usuario, lancamento);
			}
			
			this.alterarLancamento(produtoLancamento, lancamento, novaData, proximoStatusLancamento);
			
			this.montarMatrizLancamentosRetorno(matrizLancamentoRetorno, produtoLancamento,
												lancamento, novaData, proximoStatusLancamento);
			
			this.lancamentoRepository.merge(lancamento);
		}
	}

	private StatusLancamento getProximoStatusLancamentoPorOperacao(
													OperacaoMatrizLancamento operacaoMatrizLancamento) {
		
		StatusLancamento statusLancamento = null;
		
		if (operacaoMatrizLancamento.equals(OperacaoMatrizLancamento.SALVAR)) {
			
			statusLancamento = StatusLancamento.EM_BALANCEAMENTO;
		}
		
		if (operacaoMatrizLancamento.equals(OperacaoMatrizLancamento.CONFIRMAR)) {
			
			statusLancamento = StatusLancamento.BALANCEADO;
		}
		
		return statusLancamento;
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

	private void alterarLancamento(ProdutoLancamentoDTO produtoLancamento,
								   Lancamento lancamento, Date novaData,
								   StatusLancamento statusLancamento) {
		
		lancamento.setDataLancamentoDistribuidor(novaData);
		lancamento.setStatus(statusLancamento);
		lancamento.setDataStatus(new Date());
		lancamento.setSequenciaMatriz(produtoLancamento.getSequenciaMatriz());
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
	
	private void montarMatrizLancamentosConfirmadosRetorno(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
														   ProdutoLancamentoDTO produtoLancamento,
														   Date dataLancamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(dataLancamento);

		if (produtosLancamento == null) {

			produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		}

		produtosLancamento.add(produtoLancamento);

		matrizLancamento.put(dataLancamento, produtosLancamento);
	}
	
	private void montarMatrizLancamentosRetorno(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
												ProdutoLancamentoDTO produtoLancamento,
												Lancamento lancamento,
												Date novaData,
												StatusLancamento statusLancamento) {
		
		if (produtoLancamento.isLancamentoAgrupado()) {

			return;
		}

		produtoLancamento.setDataLancamentoDistribuidor(novaData);
		produtoLancamento.setStatusLancamento(statusLancamento.toString());
	
		List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(novaData);
		
		if (produtosLancamento == null) {
			
			produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		}
		
		produtosLancamento.add(produtoLancamento);
		
		matrizLancamento.put(novaData, produtosLancamento);
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
		
		balanceamentoLancamento.setDatasExpedicaoConfirmada(
			dadosBalanceamentoLancamento.getDatasExpedicaoConfirmada());
		
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
		
		indiceImpressao = 1;
		
		this.imprimir("Id | Código | Nome | Edição | Reparte | Dt Prevista | Dt Distribuidor | Dt Balanceamento | Periodicidade");
		
		this.imprimir("=============== PRODUTOS SALVOS / CONFIRMADOS ===============");
		
		List<ProdutoLancamentoDTO> produtosLancamentoBalancear =
			this.processarProdutosLancamentoNaoBalanceaveis(matrizLancamento,
															dadosBalanceamentoLancamento);
		
		Set<Date> datasConfirmadas =
			this.obterDatasConfirmadas(
				matrizLancamento, dadosBalanceamentoLancamento.getDatasExpedicaoConfirmada());
		
		TreeSet<Date> datasDistribuicao =
			this.obterDatasDistribuicao(dadosBalanceamentoLancamento, datasConfirmadas);
		
		Set<Date> datasExpectativaReparte =
			dadosBalanceamentoLancamento.getDatasExpectativaReparte();
		
		Set<Date> datasExpectativaReparteOrdenado =
			ordenarMapaExpectativaRepartePorDatasDistribuicao(datasExpectativaReparte,
															  datasDistribuicao);
		
		this.imprimir("=============== PRODUTOS BALANCEÁVEIS ===============");
		
		for (Date dataLancamentoPrevista : datasExpectativaReparteOrdenado) {
			
			List<ProdutoLancamentoDTO> produtosLancamentoBalanceaveisDataPrevista = 
				this.obterProdutosLancamentoBalanceaveisPorData(produtosLancamentoBalancear,
																dataLancamentoPrevista);
			
			List<ProdutoLancamentoDTO> produtosLancamentoNaoBalanceados =
				this.processarProdutosLancamentoBalanceaveis(matrizLancamento,
															 datasDistribuicao,
															 dataLancamentoPrevista,
															 dadosBalanceamentoLancamento,
															 produtosLancamentoBalanceaveisDataPrevista);
			
			if (produtosLancamentoNaoBalanceados != null
					&& !produtosLancamentoNaoBalanceados.isEmpty()) {
				
				produtosLancamentoNaoBalanceadosTotal.addAll(produtosLancamentoNaoBalanceados);
			}
		}
		
		if (!produtosLancamentoNaoBalanceadosTotal.isEmpty()) {

			this.imprimir("=============== REALOCAÇÃO DE PRODUTOS QUE SOBRARAM ===============");
			
			produtosLancamentoNaoBalanceadosTotal = 
				this.realocarSobrasProdutosLancamento(
					matrizLancamento, produtosLancamentoNaoBalanceadosTotal,
					datasDistribuicao, dadosBalanceamentoLancamento);
		}
		
		if (!produtosLancamentoNaoBalanceadosTotal.isEmpty()) {
			
			this.processarProdutosLancamentoNaoBalanceados(matrizLancamento,
				   produtosLancamentoNaoBalanceadosTotal,
				   datasDistribuicao,
				   dadosBalanceamentoLancamento);
		}
		
		return matrizLancamento;
	}

	private Set<Date> obterDatasConfirmadas(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											Set<Date> datasExpedicaoConfirmada) {
		
		Set<Date> datasConfirmadas = new TreeSet<>();
		
		datasConfirmadas.addAll(datasExpedicaoConfirmada);
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : matrizLancamento.entrySet()) {
			
			for (ProdutoLancamentoDTO produtoLancamento : entry.getValue()) {
				
				datasConfirmadas.add(entry.getKey());
				
				if (!this.isProdutoConfirmado(produtoLancamento)) {
					
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
				
				datasExpectativaReparteOrdenado.add(dataDistribuicao);
				
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
			this.processarProdutosLancamentoConfirmadosEExpedidos(matrizLancamento, dadosLancamentoBalanceamento);
		
		return produtosLancamentoNaoProcessados;
	}

	private List<ProdutoLancamentoDTO> processarProdutosLancamentoConfirmadosEExpedidos(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											DadosBalanceamentoLancamentoDTO dadosLancamentoBalanceamento) {
		
		List<ProdutoLancamentoDTO> produtosLancamentoNaoProcessados = new ArrayList<>();
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			dadosLancamentoBalanceamento.getProdutosLancamento();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
			
			if (!this.isProdutoBalanceavel(produtoLancamento,
					dadosLancamentoBalanceamento.getPeriodoDistribuicao())) {

				this.adicionarProdutoLancamentoNaMatriz(
					matrizLancamento, produtoLancamento, dataLancamentoDistribuidor);
			
			} else {
			
				produtosLancamentoNaoProcessados.add(produtoLancamento);
			}
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
												List<ProdutoLancamentoDTO> rodutosLancamentoBalanceaveis) {
		
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
		
		BigInteger expectativaReparteDataEscolhida =
			this.obterExpectativaReparteTotal(produtosLancamentoDataEscolhida);
		
		produtosLancamentoNaoBalanceados =
			this.balancearProdutosLancamento(
				matrizLancamento, rodutosLancamentoBalanceaveis, dadosBalanceamentoLancamento,
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
		
		if (dataLancamentoPrevista != null && datasDistribuicao.contains(dataLancamentoPrevista)) {
			
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
			
			boolean excedeLimiteDataReprogramacao =
				this.excedeLimiteDataReprogramacao(
					produtoLancamento, qtdDiasLimiteParaReprogLancamento, dataLancamento);
			
			boolean excedeCapacidadeDistribuidor =
				this.excedeCapacidadeDistribuidor(
					expectativaReparteDataAtual, produtoLancamento, capacidadeDistribuicao);
			
			boolean existeLancamentoNaData =
				this.existeLancamentoNaData(matrizLancamento, produtoLancamento, dataLancamento);
			
			if (!existeLancamentoNaData
					&& (permiteExcederCapacidadeDistribuicao
						|| (!excedeLimiteDataReprogramacao && !excedeCapacidadeDistribuidor))) {
				
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
	
	private boolean existeLancamentoNaData(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
										   ProdutoLancamentoDTO produtoLancamentoAdicionar,
										   Date dataLancamento) {
	
		List<ProdutoLancamentoDTO> produtosLancamento = matrizLancamento.get(dataLancamento);
		
		if (produtosLancamento != null && !produtosLancamento.isEmpty()) {
		
			for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
				if (!produtoLancamentoAdicionar.getIdLancamento().equals(produtoLancamento.getIdLancamento())
						&& produtoLancamentoAdicionar.getIdProdutoEdicao().equals(produtoLancamento.getIdProdutoEdicao())
						&& dataLancamento.compareTo(produtoLancamento.getNovaDataLancamento()) == 0) {
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Valida se a data de lançamento excede a data limite para reprogramação do produto.
	 */
	private boolean excedeLimiteDataReprogramacao(ProdutoLancamentoDTO produtoLancamento,
												  Integer qtdDiasLimiteParaReprogLancamento,
												  Date dataLancamento) {
		
		Date dataLimiteReprogramacao =
			this.calendarioService.subtrairDiasUteisComOperacao(
				produtoLancamento.getDataRecolhimentoPrevista(), qtdDiasLimiteParaReprogLancamento);
		
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
	 * Processa os produtos que não puderam ser balanceados.
	 */
	private List<ProdutoLancamentoDTO> realocarSobrasProdutosLancamento(
											TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
											List<ProdutoLancamentoDTO> produtosLancamentoBalancear,
											TreeSet<Date> datasDistribuicao,
											DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		long quantidadeProdutosBalancear = produtosLancamentoBalancear.size();
		
		long quantidadeProdutosNaoBalanceados = 0;
		
		while (!produtosLancamentoBalancear.isEmpty()
					&& quantidadeProdutosBalancear != quantidadeProdutosNaoBalanceados) {
		
			quantidadeProdutosBalancear = produtosLancamentoBalancear.size();
			
			Map<Date, BigInteger> mapaExpectativaReparteTotalDiariaAtual = 
				this.gerarMapaExpectativaReparteDiarioOrdenadoPelaMaiorData(matrizLancamento,
																			datasDistribuicao);
			
			this.ordenarProdutosLancamentoPorPeriodicidadeExpectativaReparte(produtosLancamentoBalancear);			
			
			produtosLancamentoBalancear =
				this.alocarSobrasMatrizLancamento(matrizLancamento,
											 	  produtosLancamentoBalancear,
											 	  mapaExpectativaReparteTotalDiariaAtual,
											 	  dadosBalanceamentoLancamento,
											 	  dadosBalanceamentoLancamento.getCapacidadeDistribuicao(),
											 	  false);
			
			quantidadeProdutosNaoBalanceados = produtosLancamentoBalancear.size();
		}
		
		return produtosLancamentoBalancear;
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
		
		this.imprimir("=============== REALOCAÇÃO DE PRODUTOS (EXCEDE CAPACIDADE DO DISTRIBUIDOR) ===============");
		
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
		
		if (!produtosLancamentoBalancear.isEmpty()) {
			
			this.imprimir("=============== REALOCAÇÃO DE PRODUTOS (BALANCEAR PARA PRIMEIRO DIA) ===============");
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
		
		this.imprimirProdutoLancamento(produtoLancamento, dataLancamento);
		
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
				
				if (this.agruparPrimeiroLancamento(produtoLancamentoAdicionar, produtoLancamento)) {
					
					this.agruparProdutos(produtoLancamentoAdicionar, produtoLancamento);
					
				} else {
					
					this.agruparProdutos(produtoLancamento, produtoLancamentoAdicionar);
				}
			}
		}
	}
	
	/**
	 * Verifica se o primeiro produto passado como parâmetro deve ser agrupado.
	 * Primeiramente verifica pelo maior status de lançamento.
	 * Em seguida verifica pelo id de lançamento maior.
	 */
	private boolean agruparPrimeiroLancamento(ProdutoLancamentoDTO produtoLancamentoAdicionar,
		   								      ProdutoLancamentoDTO produtoLancamento) {
		
		if (produtoLancamento.getStatus().ordinal() > produtoLancamentoAdicionar.getStatus().ordinal()) {
			
			return true;
			
		} else if (produtoLancamento.getStatus().ordinal() < produtoLancamentoAdicionar.getStatus().ordinal()) {
			
			return false;
			
		} else {
			
			if (produtoLancamento.getIdLancamento().compareTo(produtoLancamentoAdicionar.getIdLancamento()) == 1) {
				
				return true;
				
			} else {
				
				return false;
			}
		}
	}
	
	private void agruparProdutos(ProdutoLancamentoDTO produtoLancamentoAgrupar,
			 					 ProdutoLancamentoDTO produtoLancamento) {
	
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
	 * Verifica se o produto é balanceável.
	 */
	public boolean isProdutoBalanceavel(ProdutoLancamentoDTO produtoLancamento, Intervalo<Date> periodoDistribuicao) {
		
		Date dataLancamentoDistribuidor = produtoLancamento.getDataLancamentoDistribuidor();
		Date dataInicial = periodoDistribuicao.getDe();
		Date dataFinal = periodoDistribuicao.getAte();
		
		boolean isDataNoPeriodo =
			DateUtil.validarDataEntrePeriodo(dataLancamentoDistribuidor, dataInicial, dataFinal);
		
		if (this.isProdutoConfirmado(produtoLancamento)
				|| (produtoLancamento.isStatusLancamentoEmBalanceamento()
						&& isDataNoPeriodo)) {
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * Verifica se o produto foi confirmado.
	 */
	public boolean isProdutoConfirmado(ProdutoLancamentoDTO produtoLancamento) {
		
		if (produtoLancamento.isStatusLancamentoBalanceado()
				|| produtoLancamento.isStatusLancamentoExpedido()) {
			
			return true;
		}
		
		return false;
	}
	
	public boolean isDataConfirmada(ProdutoLancamentoDTO produtoLancamentoDTO) {

		List<ProdutoLancamentoDTO> listaverificadaConfirmada = this.lancamentoRepository
				.verificarDataConfirmada(produtoLancamentoDTO);

		if (listaverificadaConfirmada.isEmpty()) {

			return false;

		} else {

			for (ProdutoLancamentoDTO verificarConfirmado : listaverificadaConfirmada) {
				if (this.isProdutoConfirmado(verificarConfirmado)) {

					return true;

				}
			}

			return false;
		}
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private DadosBalanceamentoLancamentoDTO obterDadosLancamento(FiltroLancamentoDTO filtro) {
		
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento = new DadosBalanceamentoLancamentoDTO();
		
		Date dataLancamento = filtro.getData();
		
		int codigoDiaSemana = this.distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();
		
		int numeroSemana = DateUtil.obterNumeroSemanaNoAno(dataLancamento, codigoDiaSemana);
		
		Intervalo<Date> periodoDistribuicao = this.getPeriodoDistribuicao(codigoDiaSemana, dataLancamento, numeroSemana);
		
		TreeSet<Date> datasDistribuicaoFornecedor = this.obterDatasDistribuicaoFornecedor(periodoDistribuicao, filtro.getIdsFornecedores());
		
		dadosBalanceamentoLancamento.setPeriodoDistribuicao(periodoDistribuicao);
		
		dadosBalanceamentoLancamento.setNumeroSemana(numeroSemana);
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedor(datasDistribuicaoFornecedor);
		
		dadosBalanceamentoLancamento.setCapacidadeDistribuicao(this.distribuidorRepository.capacidadeDistribuicao());
		
		List<ProdutoLancamentoDTO> produtosLancamento =
			this.lancamentoRepository.obterBalanceamentoLancamento(periodoDistribuicao,
																   filtro.getIdsFornecedores());
		
		dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);
		
		Set<Date> datasExpectativaReparte = new LinkedHashSet<Date>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			datasExpectativaReparte.add(produtoLancamento.getDataLancamentoDistribuidor());
		}
		
		dadosBalanceamentoLancamento.setDatasExpectativaReparte(datasExpectativaReparte);
		
		dadosBalanceamentoLancamento.setQtdDiasLimiteParaReprogLancamento(
			this.distribuidorRepository.qtdDiasLimiteParaReprogLancamento());
		
		dadosBalanceamentoLancamento.setDataLancamento(dataLancamento);
		
		Set<Date> datasExpedicaoConfirmada =
			this.lancamentoRepository.obterDatasLancamentosExpedidos(periodoDistribuicao);
		
		dadosBalanceamentoLancamento.setDatasExpedicaoConfirmada(datasExpedicaoConfirmada);
		
		return dadosBalanceamentoLancamento;
	}

	/**
	 * Monta o perídodo da semana de distribuição referente à data informada.
	 */
	private Intervalo<Date> getPeriodoDistribuicao(int codigoDiaSemana,
												   Date dataLancamento,
												   int numeroSemana) {
		
		Date dataInicialSemana =
			DateUtil.obterDataDaSemanaNoAno(numeroSemana, codigoDiaSemana, dataLancamento);
		
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
	public List<ConfirmacaoVO> obterDatasConfirmacao(BalanceamentoLancamentoDTO balanceamentoLancamento) {
		
		TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento =
			balanceamentoLancamento.getMatrizLancamento();
		
		Set<Date> datasConfirmacao = balanceamentoLancamento.getMatrizLancamento().keySet();
		
		Set<Date> datasExpedicaoConfirmada = balanceamentoLancamento.getDatasExpedicaoConfirmada();
		
		Set<Date> datasConfirmadas =
			this.obterDatasConfirmadas(matrizLancamento, datasExpedicaoConfirmada);
		
		Map<Date, Boolean> mapaDatasConfirmacaoOrdenada = new TreeMap<Date, Boolean>();

		for (Date dataConfirmacao : datasConfirmacao) {
			mapaDatasConfirmacaoOrdenada.put(dataConfirmacao, false);	
		}
		
		for (Date dataConfirmada : datasConfirmadas) {
			mapaDatasConfirmacaoOrdenada.put(dataConfirmada, true);	
		}
		
		List<ConfirmacaoVO> confirmacoesVO = new ArrayList<ConfirmacaoVO>();

		Set<Entry<Date, Boolean>> entrySet = mapaDatasConfirmacaoOrdenada.entrySet();
		
		for (Entry<Date, Boolean> item : entrySet) {
			
			boolean dataConfirmada = item.getValue();
			
			confirmacoesVO.add(
				new ConfirmacaoVO(DateUtil.formatarDataPTBR(item.getKey()), dataConfirmada));
		}

		return confirmacoesVO;
	}
	
	@Transactional
	public void voltarConfiguracaoInicial(Date dataLancamento, BalanceamentoLancamentoDTO balanceamentoLancamento) {
		
		if (dataLancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de lançamento não informada!");
		}
		
		int codigoDiaSemana = 
				this.distribuidorRepository.buscarInicioSemana().getCodigoDiaSemana();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento, codigoDiaSemana);
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(codigoDiaSemana, dataLancamento, numeroSemana);
		
		this.voltarConfiguracaoInicialLancamentosPrevistos(periodoDistribuicao);
		
		this.voltarConfiguracaoInicialLancamentosDistribuidor(periodoDistribuicao);
	}
	
	private void voltarConfiguracaoInicialLancamentosPrevistos(Intervalo<Date> periodoDistribuicao) {
		
		List<Lancamento> lancamentos =
			this.lancamentoRepository.obterLancamentosPrevistosPorPeriodo(periodoDistribuicao);
		
		for (Lancamento lancamento : lancamentos) {
			
			lancamento.setDataLancamentoDistribuidor(lancamento.getDataLancamentoPrevista());
			
			lancamento.setStatus(StatusLancamento.CONFIRMADO);
			
			lancamentoRepository.merge(lancamento);
		}
	}
	
	private void voltarConfiguracaoInicialLancamentosDistribuidor(Intervalo<Date> periodoDistribuicao) {
		
		List<Lancamento> lancamentos =
			this.lancamentoRepository.obterLancamentosDistribuidorPorPeriodo(periodoDistribuicao);
		
		for (Lancamento lancamento : lancamentos) {
				
			lancamento.setStatus(StatusLancamento.CONFIRMADO);
			
			lancamentoRepository.merge(lancamento);
		}
	}
	
	/**
	 * Enum para identificação da operação da Matriz de Lançamento.
	 */
	private enum OperacaoMatrizLancamento {
		
		SALVAR,
		CONFIRMAR;
	}
	
	private void imprimirProdutoLancamento(ProdutoLancamentoDTO produtoLancamento, Date dataLancamento) {
		
		StringBuilder log = new StringBuilder();
		
		log.append(indiceImpressao++ + " | ");
		log.append(produtoLancamento.getCodigoProduto() + " | ");
		log.append(produtoLancamento.getNomeProduto() + " | ");
		log.append(produtoLancamento.getNumeroEdicao() + " | ");
		log.append(produtoLancamento.getRepartePrevisto() + " | ");
		log.append(DateUtil.formatarDataPTBR(produtoLancamento.getDataLancamentoPrevista()) + " | ");
		log.append(DateUtil.formatarDataPTBR(produtoLancamento.getDataLancamentoDistribuidor()) + " | ");
		log.append(DateUtil.formatarDataPTBR(dataLancamento) + " | ");
		log.append(produtoLancamento.getPeriodicidadeProduto().name());
		
		this.imprimir(log.toString());
	}
	
	private void imprimir(String log) {
		
		System.out.println(log);
	}
	
}
