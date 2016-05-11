package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;
import br.com.abril.nds.repository.LancamentoParcialRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

@Service
public class ParciaisServiceImpl implements ParciaisService{
	
	@Autowired
	private LancamentoParcialRepository lancamentoParcialRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private EstudoGeradoRepository estudoGeradoRepository;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ItemRecebimentoFisicoRepository itemRecebimentoFisicoRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(rollbackFor=ValidacaoException.class)
	public void inserirNovoPeriodo(Long idProdutoEdicao, Date dataRecolhimento, Usuario usuario) {
		
		LancamentoParcial lancamentoParcial = this.lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(idProdutoEdicao);
		
		this.validarDataRecolhimentoPeriodo(lancamentoParcial, dataRecolhimento,null);
		
		this.gerarPeriodoManual(lancamentoParcial, dataRecolhimento, idProdutoEdicao, usuario);
	}
	
	private void validarDataRecolhimentoPeriodo(LancamentoParcial lancamento, Date dataRecolhimento,Lancamento lanc) {

		if (!this.calendarioService.isDiaUtil(dataRecolhimento)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de recolhimento não é um dia util.");
		}

	
		// se lancamento nao estiver recolhido ou for novo lancamento, permitir alterar data de recolhimento mesmo que maior que a data de recolhimento final
		
			if (DateUtil.isDataInicialMaiorDataFinal(dataRecolhimento, lancamento.getRecolhimentoFinal())) {
				if(lanc != null && !Arrays.asList(StatusLancamento.PLANEJADO, StatusLancamento.CONFIRMADO, StatusLancamento.EM_BALANCEAMENTO
						, StatusLancamento.BALANCEADO, StatusLancamento.EXPEDIDO).contains(lanc.getStatus())) {
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de recolhimento ultrapassa o recolhimento final("+DateUtil.formatarData(lancamento.getRecolhimentoFinal(),"dd/MM/yyyy")+").Essa alteração deve ser feita através da matriz de recolhimento.");
				}
				else
				{ // alterar data do recolhimento final
					lancamento.setRecolhimentoFinal(dataRecolhimento);
				    lancamentoParcialRepository.merge(lancamento);
					
				}
				
		}
		
		if (DateUtil.isDataInicialMaiorDataFinal(this.distribuidorService.obterDataOperacaoDistribuidor(), dataRecolhimento)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de recolhimento não pode anteceder a data de operação.");
		}
		
		if (this.lancamentoRepository.existemLancamentosConfirmados(dataRecolhimento)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Já existem lançamentos confirmados para esta data de recolhimento.");
		}
	}
	
	private PeriodoLancamentoParcial gerarPeriodoManual(LancamentoParcial lancamentoParcial, Date dataRecolhimento, Long idProdutoEdicao, Usuario usuario) {

		PeriodoLancamentoParcial periodoPosterior = this.obterPeriodoPosterior(dataRecolhimento, idProdutoEdicao);
		
		if ( periodoPosterior == null || periodoPosterior.getUltimoLancamento() == null  ) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não encontrado periodo de lançamento acima da data de operacão.");
		}
		Date dataRecolhimentoProximoPeriodo = 
				(Date) SerializationUtils.clone(periodoPosterior.getUltimoLancamento().getDataRecolhimentoDistribuidor());
		
		Date dataLancamento = this.obterProximaDataComFatorRelancamentoParcialDistribuidor(dataRecolhimento);
		
		if(periodoPosterior.getUltimoLancamento() != null && periodoPosterior.getUltimoLancamento().getPeriodoLancamentoParcial() != null
				&& periodoPosterior.getUltimoLancamento().getPeriodoLancamentoParcial().getLancamentos() != null) {
			
			for(Lancamento lanc : periodoPosterior.getUltimoLancamento().getPeriodoLancamentoParcial().getLancamentos()) {
				lanc.setDataRecolhimentoDistribuidor(dataRecolhimento);
				lanc.setDataRecolhimentoPrevista(dataRecolhimento);
			}
			
		} else {
			
			periodoPosterior.getUltimoLancamento().setDataRecolhimentoDistribuidor(dataRecolhimento);
			periodoPosterior.getUltimoLancamento().setDataRecolhimentoPrevista(dataRecolhimento);
		}
		
		periodoPosterior.setTipo(TipoLancamentoParcial.PARCIAL);

		PeriodoLancamentoParcial novoPeriodo = this.gerarPeriodoParcial(				
			lancamentoParcial, 
			periodoPosterior.getNumeroPeriodo() + 1, 
			periodoPosterior.getTipo()
		);

		this.gerarLancamento(novoPeriodo, new ProdutoEdicao(idProdutoEdicao), dataLancamento, dataRecolhimentoProximoPeriodo, usuario);
		
		this.reajustarPeriodosLancamentoParcial(lancamentoParcial);
		
		return novoPeriodo;
	}
	
	private void reajustarPeriodosLancamentoParcial(LancamentoParcial lancamentoParcial) {

		List<Lancamento> lancamentos = this.periodoLancamentoParcialRepository.obterLancamentosParciais(lancamentoParcial.getId());

		int ultimoPeriodo = lancamentos != null ? lancamentos.size() : 0;
		
		int numeroPeriodo = 1;
		int numeroLancamento = 1;
		
		Date proximaDataLancamento = null;
		
		PeriodoLancamentoParcial periodo = null;
		
		for (Lancamento lancamento : lancamentos) {
			
			PeriodoLancamentoParcial periodoAtual = lancamento.getPeriodoLancamentoParcial();
			
			lancamento.setNumeroLancamento(numeroLancamento);

			if (!periodoAtual.equals(periodo)) {
				
				periodo = periodoAtual;

				periodoAtual.setNumeroPeriodo(numeroPeriodo);
				
				if (numeroPeriodo == ultimoPeriodo) {
					
					periodoAtual.setTipo(TipoLancamentoParcial.FINAL);
				
				} else {
					
					periodoAtual.setTipo(TipoLancamentoParcial.PARCIAL);
				}

				numeroPeriodo++;

				numeroLancamento = 1;

				proximaDataLancamento = this.reajustarDatasLancamento(
					lancamento, 
					proximaDataLancamento, 
					(int) (DateUtil.obterDiferencaDias(
						periodoAtual.getLancamentoParcial().getLancamentoInicial(), 
						periodoAtual.getLancamentoParcial().getRecolhimentoFinal()
					) / ultimoPeriodo)
				);

			} else {
				
				numeroLancamento++;				
			}

			this.periodoLancamentoParcialRepository.merge(periodoAtual);		
		}
	}

	private Date reajustarDatasLancamento(Lancamento lancamento, Date proximaDataLancamento, Integer peb) {
		
		try {
			
			if (proximaDataLancamento != null) {
				
				lancamento.setDataLancamentoDistribuidor(proximaDataLancamento);
				lancamento.setDataLancamentoPrevista(proximaDataLancamento);
				
				proximaDataLancamento = null;
			}

			this.validarPEBLancamento(lancamento);

		} catch (ValidacaoException e) {
			
			Date dataRecolhimento = lancamento.getDataRecolhimentoDistribuidor();
			
			lancamento.setDataRecolhimentoDistribuidor(this.obterDataRecolhimentoUtil(dataRecolhimento,peb));
			lancamento.setDataRecolhimentoPrevista(this.obterDataRecolhimentoUtil(dataRecolhimento,peb));
			
			return this.obterProximaDataComFatorRelancamentoParcialDistribuidor(this.obterDataRecolhimentoUtil(dataRecolhimento,peb));
		}
		
		return null;
	}
	
	/**
	 * Valida a PEB do lançamento, que não poder ser menor que a PEB mínima estipulada.
	 */
	private void validarPEBLancamento(Lancamento lancamento) {
		
		if (DateUtil.obterDiferencaDias(lancamento.getDataLancamentoDistribuidor(), lancamento.getDataRecolhimentoDistribuidor()) < Lancamento.PEB_MINIMA_LANCAMENTO) {
			
			List<String> mensagens = new ArrayList<String>();
			
			mensagens.add(String.format(
				"Produto %s Cod.:%s / Ed.:%s inconsistente.",
				lancamento.getProdutoEdicao().getProduto().getNome(),
				lancamento.getProdutoEdicao().getProduto().getCodigo(),
				lancamento.getProdutoEdicao().getNumeroEdicao()
			)); 
			
			mensagens.add(String.format(
				"Não podem haver períodos com peb menor que %s dias.", 
				Lancamento.PEB_MINIMA_LANCAMENTO)
			);
			
			throw new ValidacaoException(TipoMensagem.WARNING,mensagens);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Lancamento alterarRecolhimento(Lancamento lancamento, Date novaData) {

		Date proximaData = this.obterProximaDataComFatorRelancamentoParcialDistribuidor(novaData); 

		lancamento.alterarRecolhimentoParcial(novaData, proximaData);

		return lancamento;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Date obterProximaDataComFatorRelancamentoParcialDistribuidor(Date dataRecolhimento) {

		Integer fatorRelancamentoParcial = this.distribuidorService.fatorRelancamentoParcial();
		
		return this.calendarioService.adicionarDiasUteis(dataRecolhimento, fatorRelancamentoParcial);
	}
	
	private PeriodoLancamentoParcial obterPeriodoPosterior(Date dataRecolhimento, Long idProdutoEdicao) {

		return this.periodoLancamentoParcialRepository.obterPeriodoPosterior(dataRecolhimento, idProdutoEdicao);
	}
	
	@Transactional
	public void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(idProdutoEdicao);
		
		gerarPeriodosParcias(produtoEdicao, qtdePeriodos, usuario);
	}
	
	@Transactional(readOnly=true)
	public Integer calcularPebParcial(String codigoProduto, Long edicaoProduto, Integer qtdePeriodos){
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicaoProduto);
		
		return getPebProduto(produtoEdicao, qtdePeriodos);
	}
	
	private Integer getPebProduto(ProdutoEdicao produtoEdicao, Integer qntPeriodos){
		
		if(produtoEdicao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Produto Edição não encontrado!");
		}
		
		if(qntPeriodos == null || qntPeriodos <= 1) {
			return produtoEdicao.getPeb();
		}
		
		if(((produtoEdicao.getPeb() / qntPeriodos)) < Lancamento.PEB_MINIMA_LANCAMENTO) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Produto Edição não pode ter PEB menor que %s dias!", Lancamento.PEB_MINIMA_LANCAMENTO));
		}
		
		return ((produtoEdicao.getPeb() / qntPeriodos));
	}

	private void gerarPeriodosParcias(ProdutoEdicao produtoEdicao, Integer qtdePeriodos, Usuario usuario) {
		
		validarProdutoEdicao(produtoEdicao);
		
		LancamentoParcial lancamentoParcial = obterLancamentoParcialValidado(produtoEdicao, qtdePeriodos);
		
		this.validarLancamentosRecolhidos(lancamentoParcial);
		
		Long qntPeriodosNaoBalanceados = periodoLancamentoParcialRepository.obterQntPeriodosAposBalanceamentoRealizado(lancamentoParcial.getId());
		
		Integer peb = getPebProduto(produtoEdicao, qtdePeriodos);
		
		if(this.limparPeriodos(qtdePeriodos, lancamentoParcial, qntPeriodosNaoBalanceados)) {
			
			qtdePeriodos = qtdePeriodos - qntPeriodosNaoBalanceados.intValue();
		}
				
		this.processarDadosLancamentoParcial(produtoEdicao, qtdePeriodos, usuario, lancamentoParcial, peb);
	}
	
	private void validarLancamentosRecolhidos(LancamentoParcial lancamentoParcial) {
		
		for (PeriodoLancamentoParcial periodo : lancamentoParcial.getPeriodos()) {
			
			for (Lancamento lancamento : periodo.getLancamentos()) {

				if (lancamento.isRecolhido()) {

					throw new ValidacaoException(TipoMensagem.WARNING, "Parcial não pode ser recalculada, pois existe(m) lançamento(s) recolhido(s)");
				}
			}
		}
	}

	private void processarDadosLancamentoParcial(ProdutoEdicao produtoEdicao,
												 Integer qtdePeriodos, Usuario usuario,
												 LancamentoParcial lancamentoParcial, 
												 Integer peb) {
		Date dtLancamento = null;
		Date dtRecolhimento = null;
		
		Lancamento ultimoLancamento = lancamentoRepository.obterUltimoLancamentoDaEdicao(produtoEdicao.getId(), null);
		
		TipoLancamentoParcial tipoLancamentoParcial = TipoLancamentoParcial.PARCIAL;
		
		for(int numeroPeriodo = 1 ; numeroPeriodo <= qtdePeriodos; numeroPeriodo++) {
		
			if(ultimoLancamento == null) {
				
				dtLancamento = lancamentoParcial.getLancamentoInicial();
				
				dtRecolhimento = dtLancamento;
				
			} else {

				dtLancamento = this.obterProximaDataComFatorRelancamentoParcialDistribuidor(ultimoLancamento.getDataRecolhimentoDistribuidor());
				
				dtRecolhimento = ultimoLancamento.getDataRecolhimentoDistribuidor();
			}
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtLancamento) > 0) {
				break;
			}			
			
			dtRecolhimento = this.obterDataRecolhimentoUtil(dtRecolhimento, peb);
			
			if(DateUtil.obterDiferencaDias(lancamentoParcial.getRecolhimentoFinal(), dtRecolhimento) > 0) {
				
				numeroPeriodo = qtdePeriodos;	
				dtRecolhimento = lancamentoParcial.getRecolhimentoFinal();
			}
			
			if(numeroPeriodo == qtdePeriodos){
				tipoLancamentoParcial = TipoLancamentoParcial.FINAL;
			}
			
			PeriodoLancamentoParcial novoPeriodo = gerarPeriodoParcial(lancamentoParcial,numeroPeriodo,tipoLancamentoParcial);
			
			ultimoLancamento = this.gerarLancamento(novoPeriodo,produtoEdicao, dtLancamento, dtRecolhimento, usuario);
		}
	}
	
	private Date obterDataRecolhimentoUtil(Date dataRecolhimento, Integer peb) {
		
		return this.obterDataUtilMaisProxima(DateUtil.adicionarDias(dataRecolhimento,peb));
	}

	@Transactional(readOnly=true)
	public Date obterDataUtilMaisProxima(Date data) {
		
		boolean diaUtil = this.calendarioService.isDiaUtil(data);
		
		if (diaUtil) {
			
			return data;
		}
		
		Date dataRecolhimentoPosterior = this.obterProximaData(data, 1);
		
		long difDiasPosterior = DateUtil.obterDiferencaDias(data, dataRecolhimentoPosterior);
		
		if (difDiasPosterior == 1) {
			
			return dataRecolhimentoPosterior;
		}
		
		Date dataRecolhimentoAntecipada = this.obterProximaData(data, -1);
		
		long difDiasAntecipada = DateUtil.obterDiferencaDias(dataRecolhimentoAntecipada,data);
		
		return (difDiasAntecipada < difDiasPosterior)
					? dataRecolhimentoAntecipada : dataRecolhimentoPosterior;
	}

	private Date obterProximaData(Date dataRecolhimento, int numDias) {
		
		Date proximaData = DateUtil.adicionarDias(dataRecolhimento, numDias);
		
		boolean diaUtil = this.calendarioService.isDiaUtil(proximaData);
		
		if (diaUtil) {
			
			return proximaData;
			
		} else {
			
			return this.obterProximaData(proximaData, numDias);
		}
	}

	private boolean limparPeriodos(int qtdePeriodos, LancamentoParcial lancamentoParcial,long qntPeriodosNaoBalanceados){
		
		if (qntPeriodosNaoBalanceados <= qtdePeriodos) {

			List<PeriodoLancamentoParcial> periodosARemover = new ArrayList<PeriodoLancamentoParcial>();

			if (lancamentoParcial.getPeriodos() != null && lancamentoParcial.getPeriodos().size() > 0) {

				for (PeriodoLancamentoParcial item : lancamentoParcial.getPeriodos()) {
					
					Lancamento lancamento = item.getLancamentoPeriodoParcial();
					
					if(lancamento!= null && getStatusLancamentoPreExpedicao().contains(lancamento.getStatus())){
						
						periodoLancamentoParcialRepository.remover(item);

						periodosARemover.add(item);
					}
				}

				lancamentoParcial.getPeriodos().removeAll(periodosARemover);
				
				return true;
			}
			
			return false;

		} else {

			throw new ValidacaoException(TipoMensagem.WARNING,"Quantidade de períodos é menor que a quantidade já programada para lançamento");
		}
	}

	private void validarProdutoEdicao(ProdutoEdicao produtEdicao) {
		
		if(produtEdicao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não deve ser nulo.");
	}

		
	private PeriodoLancamentoParcial gerarPeriodoParcial(LancamentoParcial lancamentoParcial,int numeroPeriodo,TipoLancamentoParcial tipoLancamentoParcial) {
		PeriodoLancamentoParcial periodo = new PeriodoLancamentoParcial();
		periodo.setLancamentoParcial(lancamentoParcial);
		periodo.setTipo(tipoLancamentoParcial);
		periodo.setStatus(StatusLancamentoParcial.PROJETADO);
		periodo.setNumeroPeriodo(numeroPeriodo);

		return periodoLancamentoParcialRepository.merge(periodo);
	}

	@SuppressWarnings("unused")
	private HistoricoLancamento gerarHistoricoLancamento(Lancamento lancamento, Usuario usuario) {

		HistoricoLancamento historico = new HistoricoLancamento();
		historico.setLancamento(lancamento);
		historico.setTipoEdicao(TipoEdicao.INCLUSAO);
		historico.setStatusNovo(lancamento.getStatus());
		historico.setDataEdicao(new Date());
		historico.setResponsavel(usuario);
		
		return historico;		
	}

	private Lancamento gerarLancamento(PeriodoLancamentoParcial novoPeriodo,ProdutoEdicao produtoEdicao, Date dtLancamento, Date dtRecolhimento, Usuario usuario) {
		
		Lancamento lancamento = new Lancamento();
		lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
		lancamento.setPeriodoLancamentoParcial(novoPeriodo);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setDataLancamentoPrevista(dtLancamento);
		lancamento.setDataLancamentoDistribuidor(dtLancamento);
		
		lancamento.setDataRecolhimentoPrevista(dtRecolhimento);

		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setSequenciaMatriz(null);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.PLANEJADO);
		lancamento.setNumeroLancamento(BigInteger.ONE.intValue());
		lancamento.setUsuario(usuario);
		
		Date proximaData = this.obterProximaDataComFatorRelancamentoParcialDistribuidor(dtRecolhimento); 

		lancamento.novoRecolhimentoParcial(dtRecolhimento, proximaData);

		return this.lancamentoRepository.merge(lancamento);
	}

	private LancamentoParcial obterLancamentoParcialValidado(ProdutoEdicao produtoEdicao, Integer qtdePeriodos) {
		
		if(qtdePeriodos == null || qtdePeriodos<=0)
			throw new ValidacaoException(TipoMensagem.WARNING, "Quantidade de periodos informada deve ser maior que zero.");
		
		if(produtoEdicao == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não deve ser nulo.");
		
		if(!produtoEdicao.isParcial())
			throw new ValidacaoException(TipoMensagem.WARNING, "ProdutoEdicao não é parcial.");
		
		LancamentoParcial lancamentoParcial = lancamentoParcialRepository.obterLancamentoPorProdutoEdicao(produtoEdicao.getId());
		
		if(lancamentoParcial == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há LancamentoParcial para o ProdutoEdicao " + produtoEdicao.getId() +".");
				
		return lancamentoParcial;		
	}

	@Override
	@Transactional(rollbackFor=ValidacaoException.class)
	public void alterarPeriodo(Long idLancamento, Date dataLancamento,
			Date dataRecolhimento, Usuario usuario) {

		if(dataLancamento.compareTo(dataRecolhimento) >= 0 ){
			throw new ValidacaoException(TipoMensagem.WARNING,"Data de recolhimeno não pode ser menor ou igual a data de lançamento");
		}
		
		if(idLancamento == null) 
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do Lancamento não deve ser nulo.");
			
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
		if(lancamento == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento não deve ser nulo.");
		
		// caso esteja mudando a data de recolhimento, verificar se situacao do lancamento perfmite
		if (lancamento.getDataRecolhimentoDistribuidor().compareTo(dataRecolhimento) != 0) {
					
					this.validarStatusLancamentoPeriodo(lancamento, "Lancamento já foi realizado, não pode ser alterado.");
				}
				
		if (lancamento.getDataLancamentoDistribuidor().compareTo(dataLancamento) != 0) {
			
			this.validarStatusLancamentoPeriodo(lancamento, "Lancamento já foi realizado, não pode ser alterado.");
		}

		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);

		if(DateUtil.isDataInicialMaiorDataFinal(periodo.getLancamentoParcial().getLancamentoInicial(), dataLancamento))
			throw new ValidacaoException(TipoMensagem.WARNING, "A data de Lançamento é inferior ao lançamento inicial da parcial.");

		this.validarDataRecolhimentoPeriodo(periodo.getLancamentoParcial(), dataRecolhimento,lancamento);

		PeriodoLancamentoParcial periodoAnterior = 
				periodoLancamentoParcialRepository.obterPeriodoPorNumero(periodo.getNumeroPeriodo()-1, periodo.getLancamentoParcial().getId());
		
		PeriodoLancamentoParcial proximoPeriodo = 
				periodoLancamentoParcialRepository.obterPeriodoPorNumero(periodo.getNumeroPeriodo()+1, periodo.getLancamentoParcial().getId());
				
		this.validarPeriodoLancamento(dataLancamento,dataRecolhimento,periodoAnterior,proximoPeriodo,periodo.getLancamentoParcial());
		
		lancamento.setDataLancamentoDistribuidor(dataLancamento);
		lancamento.setDataLancamentoPrevista(dataLancamento);

		lancamento.alterarRecolhimentoParcial(
			dataRecolhimento, 
			this.obterProximaDataComFatorRelancamentoParcialDistribuidor(dataRecolhimento)
		);
		
		this.validarPEBLancamento(lancamento);

		lancamento.setUsuario(usuario);
		
		lancamentoRepository.merge(lancamento);
		
		this.reajustarRedistribuicoes(periodo,dataLancamento,dataRecolhimento);
	}
	
	@Override
	@Transactional
	public void reajustarRedistribuicoes(PeriodoLancamentoParcial periodo,Date dataLancamento, Date dataRecolhimento) {
		
		List<Lancamento> redistribuicoesAnteriores = 
				periodoLancamentoParcialRepository.obterRedistribuicoesPosterioresAoLancamento(periodo.getId(), dataRecolhimento);
		
		if(!redistribuicoesAnteriores.isEmpty()){

			for(Lancamento item : redistribuicoesAnteriores){
				excluirLancamentoParcial(item.getId());
			}
		}
		
		List<Lancamento> redistribuicoesPosteriores = 
				periodoLancamentoParcialRepository.obterRedistribuicoesPosterioresAoLancamento(periodo.getId(), dataLancamento);
		
		if(!redistribuicoesPosteriores.isEmpty()){
		
			int numeroLancamento = 2;
			
			for(Lancamento item : redistribuicoesPosteriores){
				item.setNumeroLancamento(numeroLancamento);
				item.setDataRecolhimentoDistribuidor(dataRecolhimento);
				item.setDataRecolhimentoPrevista(dataRecolhimento);
				lancamentoRepository.merge(item);
				numeroLancamento = numeroLancamento + 1;
			}
		}
		
	}
	
	private void validarPeriodoLancamento( Date dataLancamento,
										Date dataRecolhimento,
										PeriodoLancamentoParcial periodoAnterior,
										PeriodoLancamentoParcial proximoPeriodo,
										LancamentoParcial lancamentoParcial) {
		
		if(periodoAnterior == null && proximoPeriodo == null){
			return;
		}
		
		Lancamento proximoLancamento = (proximoPeriodo == null ) ? null : proximoPeriodo.getLancamentoPeriodoParcial();
		Lancamento lancamentoAnterior = (periodoAnterior == null) ? null :periodoAnterior.getLancamentoPeriodoParcial();
		
		if(lancamentoAnterior == null && proximoLancamento!= null){
			
			if(!DateUtil.validarDataEntrePeriodo(dataLancamento, lancamentoParcial.getLancamentoInicial(),proximoLancamento.getDataLancamentoDistribuidor())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de lançamento deve estar entre lancamento inicial("+DateUtil.formatarData(lancamentoParcial.getLancamentoInicial(),"dd/MM/yyyy")+") e proximo lancamento("+
						DateUtil.formatarData(proximoLancamento.getDataLancamentoDistribuidor(),"dd/MM/yyyy")+")." );
			}
			
			if(!DateUtil.validarDataEntrePeriodo(dataRecolhimento, lancamentoParcial.getLancamentoInicial(),proximoLancamento.getDataRecolhimentoDistribuidor())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de recolhimento deve estar entre  lancamento inicial("+DateUtil.formatarData(lancamentoParcial.getLancamentoInicial(),"dd/MM/yyyy")+") e proximo recolhimento("+
						DateUtil.formatarData(proximoLancamento.getDataRecolhimentoDistribuidor(),"dd/MM/yyyy")+").");
			}
		}
		
		if(lancamentoAnterior != null && proximoLancamento == null){
			
			if(!DateUtil.validarDataEntrePeriodo(dataLancamento,lancamentoAnterior.getDataLancamentoDistribuidor(),lancamentoParcial.getRecolhimentoFinal())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de lançamento deve estar entre  lancamento anterior("+DateUtil.formatarData(lancamentoAnterior.getDataLancamentoDistribuidor(),"dd/MM/yyyy")+") e recolhimento final("+
						DateUtil.formatarData(lancamentoParcial.getRecolhimentoFinal(),"dd/MM/yyyy")+")." );
			}
			
			if(!DateUtil.validarDataEntrePeriodo(dataRecolhimento,lancamentoAnterior.getDataRecolhimentoDistribuidor(),lancamentoParcial.getRecolhimentoFinal())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de recolhimento deve estar entre  recolhimento anterior("+DateUtil.formatarData(lancamentoAnterior.getDataRecolhimentoDistribuidor(),"dd/MM/yyyy")+") e recolhimento final("+
						DateUtil.formatarData(lancamentoParcial.getRecolhimentoFinal(),"dd/MM/yyyy")+")."
						);
			}
		}
		
		if(proximoLancamento != null && lancamentoAnterior!= null){
			
			if(!DateUtil.validarDataEntrePeriodo(dataLancamento,lancamentoAnterior.getDataLancamentoDistribuidor(),proximoLancamento.getDataLancamentoDistribuidor())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de lançamento  deve estar entre  lançamento anterior("+DateUtil.formatarData(lancamentoAnterior.getDataLancamentoDistribuidor(),"dd/MM/yyyy")+") e lancamento posterior("+
						DateUtil.formatarData(proximoLancamento.getDataLancamentoDistribuidor(),"dd/MM/yyyy") +").");
			}
			
			if(!DateUtil.validarDataEntrePeriodo(dataRecolhimento,lancamentoAnterior.getDataLancamentoDistribuidor(),proximoLancamento.getDataRecolhimentoDistribuidor())){
				throw new ValidacaoException(TipoMensagem.WARNING, "A nova data de recolhimento  deve estar entre  lançamento anterior("+
						DateUtil.formatarData(lancamentoAnterior.getDataLancamentoDistribuidor(),"dd/MM/yyyy")+") e recolhimento posterior("+DateUtil.formatarData(proximoLancamento.getDataRecolhimentoDistribuidor(),"dd/MM/yyyy")+").");
			}
		}
	}
	
	@Override
	@Transactional
	public void excluirPeriodo(Long idLancamento) {
		
		if(idLancamento == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Id do Lancamento não deve ser nulo.");
		}
			
		Lancamento lancamento = lancamentoRepository.buscarPorId(idLancamento);
		
		if(lancamento == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento não deve ser nulo.");
		}
		
		if (lancamento.getRecebimentos() != null) {

			this.itemRecebimentoFisicoRepository.removerRecebimentoFisicoLancamento(lancamento.getId());
		}

		validarStatusLancamentoPeriodo(lancamento, "Lancamento já foi expedido, não pode ser excluido.");

		validarEstudoGeradoLancamento(lancamento.getId());
		
		PeriodoLancamentoParcial periodo = periodoLancamentoParcialRepository.obterPeriodoPorIdLancamento(idLancamento);
		
		if (periodo.getLancamentoParcial() != null 
				&&  periodo.getLancamentoParcial().getPeriodos() != null 
				&& periodo.getLancamentoParcial().getPeriodos().size() == 1) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Para excluir todos os lançamentos parciais deve ser alterado o 'Regime Recolhimento' no cadastro de edição");
		}
		
		PeriodoLancamentoParcial periodoAnterior = periodoLancamentoParcialRepository.obterPeriodoPorNumero(periodo.getNumeroPeriodo()-1, periodo.getLancamentoParcial().getId());
		
		if(TipoLancamentoParcial.FINAL.equals(periodo.getTipo())) {
			
			if(periodoAnterior != null) {
				
				Lancamento lancamentoPeriodo = periodoAnterior.getLancamentoPeriodoParcial();
				
				validarStatusLancamentoPeriodoExclusao(lancamentoPeriodo, "Lançamento não pode ser excluido! É necessário manter um período final");
				
				periodoAnterior.setTipo(TipoLancamentoParcial.FINAL);			
			}
		}

		this.atualizarRecolhimentosPeriodoAnterior(periodoAnterior, periodo.getUltimoLancamento().getDataRecolhimentoDistribuidor());

		if(!periodo.getLancamentos().isEmpty()) {
			for(Lancamento item : periodo.getLancamentos()) {
				this.validarExclusaoRedistribuicaoParcial(item);
			}
		}
		
		periodoLancamentoParcialRepository.merge(periodoAnterior);
		
		periodoLancamentoParcialRepository.remover(periodo);
	}

	private void validarEstudoGeradoLancamento(Long idLancamento) {
		
		List<EstudoGerado> eg = estudoGeradoRepository.obterPorLancamentoId(idLancamento);
		
		if(eg != null && !eg.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Lancamento possui estudo, não pode ser excluido.");
		}
	}

	private void atualizarRecolhimentosPeriodoAnterior(PeriodoLancamentoParcial periodoAnterior, Date dataRecolhimento) {

		for (Lancamento lancamento : periodoAnterior.getLancamentos()) {

			lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
			lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
		}
	}

	private void validarStatusLancamentoPeriodo(Lancamento lancamento, String mensagem) {
		
		if(!getStatusLancamentoPreExpedicao().contains(lancamento.getStatus())){ 
			throw new ValidacaoException(TipoMensagem.WARNING, mensagem);
		}
	}

	private void validarStatusLancamentoPeriodoExclusao(Lancamento lancamento, String mensagem) {
		
		if(!getStatusLancamentoRecolhimento().contains(lancamento.getStatus())){ 
			throw new ValidacaoException(TipoMensagem.WARNING, mensagem);
		}
	}

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoRdicao
	 * @return List<ParcialVendaDTO>
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento,
			Date dataRecolhimento, Long idProdutoEdicao, Long idPeriodo) {
		return periodoLancamentoParcialRepository.obterDetalhesVenda(dataLancamento, dataRecolhimento, idProdutoEdicao, idPeriodo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RedistribuicaoParcialDTO> obterRedistribuicoesParciais(Long idPeriodo) {
		
		return this.periodoLancamentoParcialRepository.obterRedistribuicoesParciais(idPeriodo);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> obterRedistribuicoes(Long idPeriodo) {
		
		return this.periodoLancamentoParcialRepository.obterRedistribuicoes(idPeriodo);
	}
	
	@Override
	@Transactional
	public void incluirRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO) {
		
		Long idPeriodo = redistribuicaoParcialDTO.getIdPeriodo();
		
		PeriodoLancamentoParcial periodoLancamentoParcial =
			this.periodoLancamentoParcialRepository.buscarPorId(idPeriodo);
		
		this.validarInclusaoRedistribuicaoParcial(
			redistribuicaoParcialDTO, periodoLancamentoParcial);
		
		ProdutoEdicao produtoEdicao = periodoLancamentoParcial.getLancamentoParcial().getProdutoEdicao();
		
		Lancamento lancamento = new Lancamento();
		
		lancamento.setDataLancamentoDistribuidor(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataLancamentoPrevista(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataRecolhimentoDistribuidor(redistribuicaoParcialDTO.getDataRecolhimento());
		lancamento.setDataRecolhimentoPrevista(redistribuicaoParcialDTO.getDataRecolhimento());
		lancamento.setReparte(BigInteger.ZERO);
		lancamento.setDataCriacao(new Date());
		lancamento.setDataStatus(new Date());
		lancamento.setStatus(StatusLancamento.CONFIRMADO);
		lancamento.setTipoLancamento(TipoLancamento.REDISTRIBUICAO);
		lancamento.setProdutoEdicao(produtoEdicao);
		lancamento.setUsuario(this.usuarioService.getUsuarioLogado());
		
		lancamento.setNumeroLancamento(
			this.produtoEdicaoService.obterNumeroLancamento(produtoEdicao.getId(), idPeriodo));
		
		lancamento.setPeriodoLancamentoParcial(periodoLancamentoParcial);
		
		this.lancamentoRepository.adicionar(lancamento);
	}
	
	@Override
	@Transactional
	public void salvarRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO) {
		
		Long idLancamento = redistribuicaoParcialDTO.getIdLancamentoRedistribuicao();
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamento);
		
		this.validarAlteracaoRedistribuicaoParcial(
			lancamento, redistribuicaoParcialDTO.getDataLancamento());
		
		lancamento.setDataLancamentoDistribuidor(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setDataLancamentoPrevista(redistribuicaoParcialDTO.getDataLancamento());
		lancamento.setUsuario(this.usuarioService.getUsuarioLogado());
		
		this.lancamentoRepository.alterar(lancamento);
	}
	
	@Override
	@Transactional
	public void excluirLancamentoParcial(Long idLancamentoRedistribuicao) {
		
		Lancamento lancamento = this.lancamentoRepository.buscarPorId(idLancamentoRedistribuicao);
		
		this.validarExclusaoRedistribuicaoParcial(lancamento);
		
		this.lancamentoRepository.remover(lancamento);
	}

	private void validarInclusaoRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO,
		  											  PeriodoLancamentoParcial periodoLancamentoParcial) {
		
		if (periodoLancamentoParcial == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Período Lançamento Parcial não encontrado!");
		}
		
		Lancamento ultimoLancamento = periodoLancamentoParcial.getUltimoLancamento();
		
		if (ultimoLancamento.getDataLancamentoDistribuidor()
				.compareTo(redistribuicaoParcialDTO.getDataLancamento()) >= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data do lançamento deve ser maior do que a data de lançamento anterior!");
		}
		
		if (this.lancamentoRepository.existemLancamentosConfirmados(redistribuicaoParcialDTO.getDataRecolhimento())) {
		
			throw new ValidacaoException(TipoMensagem.WARNING, 
					"Já existem lançamentos confirmados para esta data de recolhimento.");
		}
		
		if (ultimoLancamento.getDataRecolhimentoDistribuidor()
				.compareTo(redistribuicaoParcialDTO.getDataLancamento()) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data do lançamento deve ser menor do que a data de recolhimento do período!");
		}
		
		Lancamento lancamentoPeriodo = periodoLancamentoParcial.getLancamentoPeriodoParcial();
		
		List<StatusLancamento> statusLancamentoPreRecolhimento = 
			this.getStatusLancamentoPreRecolhimento();
		
		if (!(statusLancamentoPreRecolhimento.contains(lancamentoPeriodo.getStatus()))) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Não é possível incluir uma redistribuição para esse lançamento. O lançamento já se encontra em recolhimento!");
		}
	}
	
	private void validarAlteracaoRedistribuicaoParcial(Lancamento lancamento, Date dataLancamentoRedistribuicao) {
		
		this.validarRedistribuicaoExpedida(lancamento);
		
		Integer numeroLancamento = lancamento.getNumeroLancamento();
		
		PeriodoLancamentoParcial periodoLancamentoParcial = lancamento.getPeriodoLancamentoParcial();
		
		Lancamento lancamentoAnterior =
			periodoLancamentoParcial.getLancamentoAnterior(numeroLancamento);
		
		Lancamento lancamentoPosterior =
			periodoLancamentoParcial.getLancamentoPosterior(numeroLancamento);
		
		if (lancamentoAnterior != null
				&& lancamentoAnterior.getDataLancamentoDistribuidor().compareTo(dataLancamentoRedistribuicao) >= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de lançamento deve ser maior que a data do lançamento anterior!");
		}
		
		if (lancamentoPosterior != null
				&& lancamentoPosterior.getDataLancamentoDistribuidor().compareTo(dataLancamentoRedistribuicao) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data de lançamento deve ser menor que a data do lançamento posterior!");
		}
		
		if (lancamento.getDataRecolhimentoDistribuidor().compareTo(dataLancamentoRedistribuicao) <= 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data do lançamento deve ser menor do que a data de recolhimento do período!");
		}
		
		if (this.lancamentoRepository.existemLancamentosConfirmados(lancamento.getDataRecolhimentoDistribuidor())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
					"Já existem lançamentos confirmados para esta data de recolhimento.");
		}
	}
	
	private void validarExclusaoRedistribuicaoParcial(Lancamento lancamento) {
		
		this.validarRedistribuicaoExpedida(lancamento);
	}

	private void validarRedistribuicaoExpedida(Lancamento lancamento) {
		
		if (lancamento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Lançamento não encontrado!");
		}
		
		List<StatusLancamento> statusLancamentoPreExpedicao = 
			this.getStatusLancamentoPreExpedicao();
		
		if (!(statusLancamentoPreExpedicao.contains(lancamento.getStatus()))) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"Não é possível excluir essa redistribuição, pois já foi realizada a expedição!");
		}
	}
	
	private List<StatusLancamento> getStatusLancamentoPreRecolhimento() {
		
		List<StatusLancamento> statusLancamentos = new ArrayList<>();
		
		statusLancamentos.add(StatusLancamento.CONFIRMADO);
		statusLancamentos.add(StatusLancamento.PLANEJADO);
		statusLancamentos.add(StatusLancamento.EM_BALANCEAMENTO);
		statusLancamentos.add(StatusLancamento.BALANCEADO);
		statusLancamentos.add(StatusLancamento.EXPEDIDO);
		
		return statusLancamentos;
	}
	
	private List<StatusLancamento> getStatusLancamentoPreExpedicao() {
		
		List<StatusLancamento> statusLancamentos = new ArrayList<>();
		
		statusLancamentos.add(StatusLancamento.CONFIRMADO);
		statusLancamentos.add(StatusLancamento.PLANEJADO);
		statusLancamentos.add(StatusLancamento.EM_BALANCEAMENTO);
		statusLancamentos.add(StatusLancamento.BALANCEADO);
		
		return statusLancamentos;
	}
	
	private List<StatusLancamento> getStatusLancamentoRecolhimento() {
		
		List<StatusLancamento> statusLancamentos = new ArrayList<>();
		
		statusLancamentos.add(StatusLancamento.CONFIRMADO);
		statusLancamentos.add(StatusLancamento.PLANEJADO);
		statusLancamentos.add(StatusLancamento.EM_BALANCEAMENTO);
		statusLancamentos.add(StatusLancamento.BALANCEADO);
		
		statusLancamentos.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
		statusLancamentos.add(StatusLancamento.FURO);
		statusLancamentos.add(StatusLancamento.EXPEDIDO);
		
		return statusLancamentos;
	}
	
	@Override
	@Transactional
	public void atualizarReparteDoProximoLancamentoPeriodo(Lancamento lancamento, Usuario usuario, BigInteger reparte) {
		
		Lancamento proximoLancamento =
			this.getPrimeiroLancamentoNaoJuramentado(lancamento);

		if (proximoLancamento != null) {

			proximoLancamento.setReparte(reparte);

			proximoLancamento.setUsuario(usuario);

			this.lancamentoRepository.alterar(proximoLancamento);
		}
	}
	
	@Transactional
	public Lancamento getProximoLancamentoPeriodo(Lancamento lancamento) {
		
		PeriodoLancamentoParcial periodoLancamentoParcial =
			lancamento.getPeriodoLancamentoParcial();
		
		Integer proximoNumeroPeriodo = periodoLancamentoParcial.getNumeroPeriodo() + 1;
		
		PeriodoLancamentoParcial proximoPeriodoLancamentoParcial =
			this.periodoLancamentoParcialRepository.obterPeriodoPorNumero(
				proximoNumeroPeriodo, periodoLancamentoParcial.getLancamentoParcial().getId());
		
		if (proximoPeriodoLancamentoParcial == null) {
		
			return null;
		}
		
		return proximoPeriodoLancamentoParcial.getLancamentoPeriodoParcial();
	}
	
	@Transactional
    public Lancamento getPrimeiroLancamentoNaoJuramentado(Lancamento lancamento) {
        
        PeriodoLancamentoParcial periodoLancamentoParcial =
            lancamento.getPeriodoLancamentoParcial();
        
        Integer proximoNumeroPeriodo = periodoLancamentoParcial.getNumeroPeriodo() + 1;
        
        Lancamento primeiroLancamentoNaoJuramentado =
            this.periodoLancamentoParcialRepository.obterPrimeiroLancamentoNaoJuramentado(
                proximoNumeroPeriodo, periodoLancamentoParcial.getLancamentoParcial().getId());
        
        return primeiroLancamentoNaoJuramentado;
    }

}
