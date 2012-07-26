package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.lightcouch.CouchDbClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.couchdb.CouchDbProperties;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.DiferencaEstoqueRepository;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.server.model.FormatoIndicador;
import br.com.abril.nds.server.model.GrupoIndicador;
import br.com.abril.nds.server.model.Indicador;
import br.com.abril.nds.server.model.OperacaoDistribuidor;
import br.com.abril.nds.server.model.Status;
import br.com.abril.nds.server.model.StatusOperacao;
import br.com.abril.nds.server.model.TipoIndicador;
import br.com.abril.nds.service.IntegracaoOperacionalDistribuidorService;

/**
 * Implementação do serviço de integração operacional do distribuidor.
 * 
 * @author Discover Technology
 *
 */
@Service
public class IntegracaoOperacionalDistribuidorServiceImpl implements IntegracaoOperacionalDistribuidorService {
	
	private static final String DB_NAME = "db_integracao";
	
	@Autowired
	private CouchDbProperties couchDbProperties;
	
	private CouchDbClient couchDbClientIntegracao;
	
	@Autowired
	private DistribuidorRepository distribuidorRepository;
	
	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	@Autowired
	private EstoqueProdutoCotaRepository estoqueProdutoCotaRepository;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private DiferencaEstoqueRepository diferencaEstoqueRepository;
	
	@PostConstruct
	public void initCouchDbClient() {
		
		this.couchDbClientIntegracao = 
			new CouchDbClient(
				DB_NAME, true, couchDbProperties.getProtocol(), couchDbProperties.getHost(), 
					couchDbProperties.getPort(), couchDbProperties.getUsername(), 
						couchDbProperties.getPassword());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void integrarInformacoesOperacionais(OperacaoDistribuidor operacaoDistribuidor) {
		
		this.couchDbClientIntegracao.save(operacaoDistribuidor);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly = true)
	public OperacaoDistribuidor obterInformacoesOperacionais() {
		
		Distribuidor distribuidor = this.distribuidorRepository.obter();
		
		OperacaoDistribuidor operacaoDistribuidor = new OperacaoDistribuidor();
		operacaoDistribuidor.setDataOperacao(distribuidor.getDataOperacao());
		//TODO: operacaoDistribuidor.setIdDistribuidorInterface(idDistribuidorInterface);
		//TODO: operacaoDistribuidor.setUf(distribuidor.getEnderecos().get(0).getEndereco().getUf());
		operacaoDistribuidor.setNome(distribuidor.getJuridica().getRazaoSocial());
		
		StatusOperacao statusOperacao = new StatusOperacao();
		statusOperacao.setData(new Date());
		
		if (this.fechamentoEncalheRepository.buscaControleFechamentoEncalhe(new Date())){
			
			
			statusOperacao.setStatus(Status.ENCERRADO);
		} else if (this.fechamentoEncalheRepository.buscaControleFechamentoEncalhePorData(new Date()) != null){
			
			statusOperacao.setStatus(Status.FECHAMENTO);
		} else {
			
			statusOperacao.setStatus(Status.OPERANDO);
		}
		
		operacaoDistribuidor.setStatusOperacao(statusOperacao);
		
		List<Indicador> indicadores = new ArrayList<Indicador>();
		
		Object valorIndicador = null;
		
		//GRUPO LANÇAMENTO
		//TITULOS_LANCADOS
		Indicador indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setTipoIndicador(TipoIndicador.TITULOS_LANCADOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.lancamentoRepository.obterQuantidadeLancamentos(StatusLancamento.LANCADO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//TITULOS_FURADOS
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setTipoIndicador(TipoIndicador.TITULOS_FURADOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.lancamentoRepository.obterQuantidadeLancamentos(StatusLancamento.FURO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//CONSIGNADO
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.LANCAMENTO);
		indicador.setTipoIndicador(TipoIndicador.CONSIGNADO);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.lancamentoRepository.obterConsignadoDia(StatusLancamento.CONFIRMADO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		
		//GRUPO RECOLHIMENTO
		//TITULOS_RECOLHIDOS
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.RECOLHIMENTO);
		indicador.setTipoIndicador(TipoIndicador.TITULOS_RECOLHIDOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.lancamentoRepository.obterQuantidadeLancamentos(StatusLancamento.RECOLHIDO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//TITULOS_RECOLHIDOS
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.RECOLHIMENTO);
		indicador.setTipoIndicador(TipoIndicador.RECOLHIMENTO);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.lancamentoRepository.obterConsignadoDia(StatusLancamento.RECOLHIDO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//FINANCEIRO
		//VENCIMENTOS
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.VENCIMENTOS);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.dividaRepository.obterValorDividasDataOperacao(true, false);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//INADIMPLENCIA
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.INADIMPLENCIA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.dividaRepository.obterValorDividasDataOperacao(false, false);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//INADIMPLENCIA_ACUMULADA
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.INADIMPLENCIA_ACUMULADA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.dividaRepository.obterValorDividasDataOperacao(false, true);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//COBRANCA
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.COBRANCA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.dividaRepository.obterValoresDividasGeradasDataOperacao(false);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//COBRANCA_POSTERGADA
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.COBRANCA_POSTERGADA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.dividaRepository.obterValoresDividasGeradasDataOperacao(true);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//LIQUIDACAO
		indicador = new Indicador();
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.FINANCEIRO);
		indicador.setTipoIndicador(TipoIndicador.LIQUIDACAO);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.cobrancaRepository.obterValorCobrancasQuitadasPorData(new Date());
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//CONSIGNADO
		//TOTAL_RUA
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.CONSIGNADO);
		indicador.setTipoIndicador(TipoIndicador.TOTAL_RUA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.estoqueProdutoCotaRepository.obterConsignado(false);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//TOTAL_RUA_INADIMPLENCIA
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.CONSIGNADO);
		indicador.setTipoIndicador(TipoIndicador.TOTAL_RUA_INADIMPLENCIA);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.estoqueProdutoCotaRepository.obterConsignado(true);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//JORNALEIRO
		//JORNALEIROS
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.JORNALEIRO);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.cotaRepository.obterQuantidadeCotas(null);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//JORNALEIROS_ATIVOS
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.JORNALEIRO);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS_ATIVOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.ATIVO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);

		//JORNALEIROS_SUSPENSOS
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.JORNALEIRO);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS_SUSPENSOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.SUSPENSO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//JORNALEIROS_INATIVOS
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.JORNALEIRO);
		indicador.setTipoIndicador(TipoIndicador.JORNALEIROS_INATIVOS);
		indicador.setFormatoIndicador(FormatoIndicador.DECIMAL);
		valorIndicador = this.cotaRepository.obterQuantidadeCotas(SituacaoCadastro.INATIVO);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//QUALIDADE_OPERACIONAL
		//SOBRAS_DE
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.QUALIDADE_OPERACIONAL);
		indicador.setTipoIndicador(TipoIndicador.SOBRAS_DE);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.diferencaEstoqueRepository.obterValorFinanceiroPorTipoDiferenca(TipoDiferenca.SOBRA_DE);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//SOBRAS_EM
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.QUALIDADE_OPERACIONAL);
		indicador.setTipoIndicador(TipoIndicador.SOBRAS_EM);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.diferencaEstoqueRepository.obterValorFinanceiroPorTipoDiferenca(TipoDiferenca.SOBRA_EM);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//FALTAS_DE
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.QUALIDADE_OPERACIONAL);
		indicador.setTipoIndicador(TipoIndicador.FALTAS_DE);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.diferencaEstoqueRepository.obterValorFinanceiroPorTipoDiferenca(TipoDiferenca.FALTA_DE);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);
		
		//FALTAS_EM
		indicador.setData(new Date());
		indicador.setDistribuidor(operacaoDistribuidor);
		indicador.setGrupoIndicador(GrupoIndicador.QUALIDADE_OPERACIONAL);
		indicador.setTipoIndicador(TipoIndicador.FALTAS_EM);
		indicador.setFormatoIndicador(FormatoIndicador.MONETARIO);
		valorIndicador = this.diferencaEstoqueRepository.obterValorFinanceiroPorTipoDiferenca(TipoDiferenca.FALTA_EM);
		indicador.setValor(valorIndicador == null ? null: valorIndicador.toString());
		indicadores.add(indicador);		
		
		operacaoDistribuidor.setIndicadores(indicadores);
		
		return operacaoDistribuidor;
	}
}