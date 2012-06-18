package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.DadosBalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class MatrizLancamentoServiceImpl implements MatrizLancamentoService {
	
	private static final String FORMATO_DATA_LANCAMENTO = "dd/MM/yyyy";
	
	@Autowired
	protected LancamentoRepository lancamentoRepository;
	
	@Autowired
	protected DistribuidorRepository distribuidorRepository;

	@Override
	@Transactional(readOnly = true)
	public BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro) {
	
		// TODO: validar filtro
		
		// TODO:
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			this.obterDadosLancamento(filtro, true);
		
		return balancear(dadosBalanceamentoLancamento);
	}
		
	@Override
	@Transactional
	public void confirmarMatrizLancamento(TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento) {
		
		// TODO:
	}
	
	private BalanceamentoLancamentoDTO balancear(DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento) {
		
		// TODO:
		
		return null;
	}
	
	/**
	 * Monta o DTO com as informações para realização do balanceamento.
	 */
	private DadosBalanceamentoLancamentoDTO obterDadosLancamento(FiltroLancamentoDTO filtro,
			 									 				 boolean forcarBalanceamento) {
		
		Distribuidor distribuidor = distribuidorRepository.obter();
		
		Intervalo<Date> periodoDistribuicao = 
			this.getPeriodoDistribuicao(distribuidor, filtro.getData());
		
		TreeSet<Date> datasDistribuicaoFornecedor = 
			this.obterDatasDistribuicaoFornecedor(periodoDistribuicao, filtro.getIdsFornecedores());
			
		TreeSet<Date> datasDistribuicaoDistribuidor = 
			this.obterDatasDistribuicaoDistribuidor(distribuidor, periodoDistribuicao);
			
		DadosBalanceamentoLancamentoDTO dadosBalanceamentoLancamento =
			new DadosBalanceamentoLancamentoDTO();
		
		dadosBalanceamentoLancamento.setDatasDistribuicaoFornecedor(datasDistribuicaoFornecedor);
		dadosBalanceamentoLancamento.setDatasDistribuicaoDistribuidor(datasDistribuicaoDistribuidor);
		
		dadosBalanceamentoLancamento.setCapacidadeDistribuicao(
			distribuidor.getCapacidadeDistribuicao());
		
		List<ProdutoLancamentoDTO> produtosLancamento = this.obterProdutosLancamentoMock();
		
		dadosBalanceamentoLancamento.setProdutosLancamento(produtosLancamento);
		
		dadosBalanceamentoLancamento.setMapaExpectativaReparteTotalDiario(
			this.obterMapaExpectativaReparteTotalDiarioMock(produtosLancamento));
		
		return dadosBalanceamentoLancamento;
	}
	
	/**
	 * Monta o perídodo da semana de distribuição referente à data informada.
	 */
	private Intervalo<Date> getPeriodoDistribuicao(Distribuidor distribuidor, Date dataLancamento) {
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataLancamento,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataInicialSemana =
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
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
	 * Obtém as datas de distribuição do distribuidor.
	 */
	private TreeSet<Date> obterDatasDistribuicaoDistribuidor(Distribuidor distribuidor,
														     Intervalo<Date> periodoDistribuicao) {
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor = 
			this.distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.DISTRIBUICAO);
		
		Set<Integer> codigosDiaSemanaDistribuidor = new TreeSet<Integer>();
		
		for (DistribuicaoDistribuidor distribuicaoDistribuidor : listaDistribuicaoDistribuidor) {
			
			codigosDiaSemanaDistribuidor.add(distribuicaoDistribuidor.getDiaSemana().getCodigoDiaSemana());
		}
		
		TreeSet<Date> datasDistribuicaoDistribuidor = 
			this.obterDatasDistribuicao(periodoDistribuicao, codigosDiaSemanaDistribuidor);
		
		return datasDistribuicaoDistribuidor;
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
		
		return datasDistribuicao;
	}
	
	/*
	 * Mock para obter os produtos de lançamento
	 */
	private List<ProdutoLancamentoDTO> obterProdutosLancamentoMock() {
		
		List<ProdutoLancamentoDTO> produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		
		Date data1 = DateUtil.parseDataPTBR("07/06/2012");
		Date data2 = DateUtil.parseDataPTBR("08/06/2012");
		Date data3 = DateUtil.parseDataPTBR("11/06/2012");

		Set<Date> datas = new TreeSet<Date>();
		
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		
		long x = 0;
		
		for (Date data : datas) {
			
			for (; x < 300; x++) {
			
				ProdutoLancamentoDTO produtoLancamento = new ProdutoLancamentoDTO();
				
				BigDecimal repartePrevisto = new BigDecimal("100.0");
				repartePrevisto.add(new BigDecimal(x));
				
				produtoLancamento.setIdLancamento(x);
				produtoLancamento.setDataLancamentoPrevista(data);
				produtoLancamento.setRepartePrevisto(new BigDecimal("100.0").add(new BigDecimal(x)));
				
				produtosLancamento.add(produtoLancamento);
			}
		}
		
		return produtosLancamento;
	}
	
	/*
	 * Mock para obter o mapa de expectativa de reparte total diario
	 */
	private TreeMap<Date, BigDecimal> obterMapaExpectativaReparteTotalDiarioMock(List<ProdutoLancamentoDTO> produtosLancamento) {
		
		TreeMap<Date, BigDecimal> mapaExpectativaReparteTotalDiario =
			new TreeMap<Date, BigDecimal>();
		
		for (ProdutoLancamentoDTO produtoLancamento : produtosLancamento) {
			
			BigDecimal expectativaReparte =
				mapaExpectativaReparteTotalDiario.get(produtoLancamento.getDataLancamentoPrevista());
			
			if (expectativaReparte != null) {
				
				expectativaReparte = expectativaReparte.add(produtoLancamento.getRepartePrevisto());
			
			} else {
				
				expectativaReparte = produtoLancamento.getRepartePrevisto();
			}
			
			mapaExpectativaReparteTotalDiario.put(produtoLancamento.getDataLancamentoPrevista(),
					  							  expectativaReparte);
		}
				
		return mapaExpectativaReparteTotalDiario;
	}
	
	
	
	
	@Override
	@Transactional
	public List<LancamentoDTO> buscarLancamentosBalanceamento(FiltroLancamentoDTO filtro) {
		
		List<Lancamento> lancamentos = lancamentoRepository
				.obterBalanceamentoMatrizLancamentos(filtro);
		List<LancamentoDTO> dtos = new ArrayList<LancamentoDTO>(
				lancamentos.size());
		for (Lancamento lancamento : lancamentos) {
			LancamentoDTO dto = montarDTO(filtro.getData(),lancamento);
			dtos.add(dto);
		}
		return dtos;
	}

	@Override
	@Transactional(readOnly = true)
	public SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data,
			List<Long> idsFornecedores) {
		return lancamentoRepository.sumarioBalanceamentoMatrizLancamentos(data,
				idsFornecedores);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ResumoPeriodoBalanceamentoDTO> obterResumoPeriodo(
			Date dataInicial, List<Long> fornecedores) {
		Date dataFinal = DateUtil.adicionarDias(dataInicial, 6);
		List<DistribuicaoFornecedor> distribuicoes = distribuidorRepository
				.buscarDiasDistribuicaoFornecedor(fornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		Set<DiaSemana> diasDistribuicao = EnumSet.noneOf(DiaSemana.class);
		for (DistribuicaoFornecedor distribuicao : distribuicoes) {
			diasDistribuicao.add(distribuicao.getDiaSemana());
		}

		List<Date> periodoDistribuicao = filtrarPeriodoDistribuicao(
				dataInicial, dataFinal, diasDistribuicao);
		List<ResumoPeriodoBalanceamentoDTO> resumos = lancamentoRepository
				.buscarResumosPeriodo(periodoDistribuicao, fornecedores,
						GrupoProduto.CROMO);
		
		return montarResumoPeriodo(periodoDistribuicao, resumos);
	}

	private List<ResumoPeriodoBalanceamentoDTO> montarResumoPeriodo(
			List<Date> periodoDistribuicao,
			List<ResumoPeriodoBalanceamentoDTO> resumos) {
		Map<Date, ResumoPeriodoBalanceamentoDTO> mapa = new HashMap<Date, ResumoPeriodoBalanceamentoDTO>();
		for (ResumoPeriodoBalanceamentoDTO resumo : resumos) {
			mapa.put(resumo.getData(), resumo);
		}
		List<ResumoPeriodoBalanceamentoDTO> retorno = new ArrayList<ResumoPeriodoBalanceamentoDTO>(
				periodoDistribuicao.size());
		for (Date data : periodoDistribuicao) {
			ResumoPeriodoBalanceamentoDTO resumo = mapa.get(data);
			if (resumo == null) {
				resumo = ResumoPeriodoBalanceamentoDTO.empty(data);
			}
			retorno.add(resumo);
		}
		return retorno;
	}

	private List<Date> filtrarPeriodoDistribuicao (Date dataInicial,
			Date dataFinal, Collection<DiaSemana> diasDistribuicao) {
		List<Date> datas = new ArrayList<Date>();
		while (dataInicial.before(dataFinal) || dataInicial.equals(dataFinal)) {
			DiaSemana ds = DiaSemana.getByDate(dataInicial);
			if (diasDistribuicao.contains(ds)) {
				datas.add(dataInicial);
			}
			dataInicial = DateUtil.adicionarDias(dataInicial, 1);
		}
		return datas;
	}

	private LancamentoDTO montarDTO(Date data, Lancamento lancamento) {
		ProdutoEdicao produtoEdicao = lancamento.getProdutoEdicao();
		Produto produto = produtoEdicao.getProduto();
		LancamentoDTO dto = new LancamentoDTO();
		dto.setCodigoProduto(produto.getCodigo());
		dto.setDataMatrizDistrib(DateUtil.formatarData(
				lancamento.getDataLancamentoDistribuidor(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataPrevisto(DateUtil.formatarData(
				lancamento.getDataLancamentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setDataRecolhimento(DateUtil.formatarData(
				lancamento.getDataRecolhimentoPrevista(),
				FORMATO_DATA_LANCAMENTO));
		dto.setId(lancamento.getId());
		dto.setIdFornecedor(1L);
		dto.setNomeFornecedor(produto.getFornecedor().getJuridica().getNomeFantasia());
		dto.setLancamento(lancamento.getTipoLancamento().getDescricao());
		dto.setNomeProduto(produto.getNome());
		dto.setNumEdicao(produtoEdicao.getNumeroEdicao());
		dto.setPacotePadrao(produtoEdicao.getPacotePadrao());
		dto.setPreco(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
		dto.setReparte(lancamento.getReparte().toString());
		BigDecimal total = produtoEdicao.getPrecoVenda().multiply(lancamento.getReparte());
		dto.setTotal(CurrencyUtil.formatarValor(total));
		dto.setFisico(lancamento.getTotalRecebimentoFisico().toString());
		Estudo estudo = lancamento.getEstudo();
		if (estudo != null) {
			dto.setQtdeEstudo(estudo.getQtdeReparte().toString());
		} else {
			dto.setQtdeEstudo("0");
		}
		dto.setFuro(lancamento.isFuro());
		dto.setCancelamentoGD(lancamento.isCancelamentoGD());
		dto.setExpedido(lancamento.isExpedido());
		dto.setEstudoFechado(lancamento.isEstudoFechado());
		if (DateUtil.isHoje(data) && lancamento.isSemRecebimentoFisico()) {
			dto.setSemFisico(true);
		}
		return dto;
	}
	
	

}
