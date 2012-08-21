package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.DistribuicaoDistribuidor;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;

public class MatrizLancamentoServiceImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private MatrizLancamentoServiceImpl service;
	
	@Mock
	private LancamentoRepository lancamentoRepository;
	
	@Mock
	private DistribuidorRepository distribuidorRepository;
	
	private Distribuidor distribuidor;
	
	private Fornecedor fornecedorDinap;
	private Fornecedor fornecedorFc;
	
	@Before
	public void setUp() {

		MockitoAnnotations.initMocks(this);
		
		Banco banco = Fixture.hsbc(); 
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE, parametroCobranca);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"33.333.333/0001-33", "333.333.333.333", "distrib_acme@mail.com", "99.999-9");
		
		distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);
		
		distribuidor.setQtdDiasLimiteParaReprogLancamento(5);
		
		PessoaJuridica juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11.111.111/0001-11", "111.111.111.111", "dinap@mail.com", "99.999-9");
		PessoaJuridica juridicaFc = Fixture.pessoaJuridica("FC",
				"22.222.222/0001-22", "222.222.222.222", "fc@mail.com", "99.999-9");
		
		fornecedorDinap = Fixture.fornecedor(juridicaDinap,
				SituacaoCadastro.ATIVO, true, Fixture.tipoFornecedorPublicacao(), null);
		
		fornecedorDinap.setId(1L);
		
		fornecedorFc = Fixture.fornecedor(juridicaFc,
				SituacaoCadastro.ATIVO, true, Fixture.tipoFornecedorPublicacao(), null);
		
		fornecedorFc.setId(2L);		
	}
	
	@Test
	public void balancear() {
		
		FiltroLancamentoDTO filtro = this.montarFiltro();
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor =
			this.listaDistribuicaoFornecedor();
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor =
			this.listaDistribuicaoDistribuidor();
		
		Mockito.when(distribuidorRepository.obter()).thenReturn(distribuidor);
		
		Mockito.when(
			distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				filtro.getIdsFornecedores(), OperacaoDistribuidor.DISTRIBUICAO)).thenReturn(listaDistribuicaoFornecedor);
		
		Mockito.when(
			distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.DISTRIBUICAO)).thenReturn(listaDistribuicaoDistribuidor);
		
		List<ProdutoLancamentoDTO> produtosLancamentoMock = obterProdutosLancamentoMock();
		
		Intervalo<Date> periodoDistribuicao = getPeriodoDistribuicao();
		
		Mockito.when(
			lancamentoRepository.obterBalanceamentoLancamento(
				periodoDistribuicao, filtro.getIdsFornecedores())).thenReturn(produtosLancamentoMock);
		
		service.lancamentoRepository = lancamentoRepository;
		service.distribuidorRepository = distribuidorRepository;
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			service.obterMatrizLancamento(this.montarFiltro(), false);
		
		Assert.assertNotNull(balanceamentoLancamento);
		
		List<ProdutoLancamentoDTO> produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry 
				: balanceamentoLancamento.getMatrizLancamento().entrySet()) {
			
			produtosLancamento.addAll(entry.getValue());	
		}
		
		Assert.assertTrue(!produtosLancamento.isEmpty());
	}
	
	private FiltroLancamentoDTO montarFiltro() {
		
		List<Long> idsFornecedores = new ArrayList<Long>();
		
		idsFornecedores.add(fornecedorDinap.getId());
		idsFornecedores.add(fornecedorFc.getId());
		
		FiltroLancamentoDTO filtro =
			new FiltroLancamentoDTO(new Date(), idsFornecedores, null, "codigoProduto");
		
		return filtro;
	}
	
	private Intervalo<Date> getPeriodoDistribuicao() {
		
		Date dataAtual = new Date();
		
		int numeroSemana =
			DateUtil.obterNumeroSemanaNoAno(dataAtual,
											distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataInicialSemana =
			DateUtil.obterDataDaSemanaNoAno(numeroSemana,
											distribuidor.getInicioSemana().getCodigoDiaSemana(), dataAtual);
		
		Date dataFinalSemana =
			DateUtil.adicionarDias(dataInicialSemana, 6);
		
		Intervalo<Date> periodoDistribuicao = new Intervalo<Date>(dataInicialSemana, dataFinalSemana);
		
		return periodoDistribuicao;
	}
	
	private List<DistribuicaoFornecedor> listaDistribuicaoFornecedor() {
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor =
			new ArrayList<DistribuicaoFornecedor>();
		
		DistribuicaoFornecedor distribuicaoFornecedorDinapTerca =
			Fixture.distribuicaoFornecedor(fornecedorDinap, DiaSemana.TERCA_FEIRA,
										   OperacaoDistribuidor.DISTRIBUICAO, distribuidor);

		DistribuicaoFornecedor distribuicaoFornecedorDinapQuinta =
			Fixture.distribuicaoFornecedor(fornecedorDinap, DiaSemana.QUINTA_FEIRA,
										   OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		
		DistribuicaoFornecedor distribuicaoFornecedorFcQuarta =
			Fixture.distribuicaoFornecedor(fornecedorFc, DiaSemana.QUARTA_FEIRA,
										   OperacaoDistribuidor.DISTRIBUICAO, distribuidor);

		DistribuicaoFornecedor distribuicaoFornecedorFCSexta =
			Fixture.distribuicaoFornecedor(fornecedorFc, DiaSemana.SEXTA_FEIRA,
										   OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		
		listaDistribuicaoFornecedor.add(distribuicaoFornecedorDinapTerca);
		listaDistribuicaoFornecedor.add(distribuicaoFornecedorDinapQuinta);
		listaDistribuicaoFornecedor.add(distribuicaoFornecedorFcQuarta);
		listaDistribuicaoFornecedor.add(distribuicaoFornecedorFCSexta);
		
		return listaDistribuicaoFornecedor;
	}
	
	private List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor() {
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor =
			new ArrayList<DistribuicaoDistribuidor>();
		
		DistribuicaoDistribuidor distribuicaoDistribuidorTerca =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.TERCA_FEIRA,
										   	 OperacaoDistribuidor.DISTRIBUICAO);

		DistribuicaoDistribuidor distribuicaoDistribuidorQuinta =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.QUINTA_FEIRA,
				   	 						 OperacaoDistribuidor.DISTRIBUICAO);
		
		DistribuicaoDistribuidor distribuicaoDistribuidorSexta =
			Fixture.distribuicaoDistribuidor(distribuidor, DiaSemana.SEXTA_FEIRA,
				   	 						 OperacaoDistribuidor.DISTRIBUICAO);
		
		listaDistribuicaoDistribuidor.add(distribuicaoDistribuidorTerca);
		listaDistribuicaoDistribuidor.add(distribuicaoDistribuidorQuinta);
		listaDistribuicaoDistribuidor.add(distribuicaoDistribuidorSexta);
		
		return listaDistribuicaoDistribuidor;
	}
	
	private List<ProdutoLancamentoDTO> obterProdutosLancamentoMock() {
		
		List<ProdutoLancamentoDTO> produtosLancamento = new ArrayList<ProdutoLancamentoDTO>();
		
		Date data1 = DateUtil.removerTimestamp(new Date());
		Date data2 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data1, 1));
		Date data3 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data2, 1));
		Date data4 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data3, 1));
		Date data5 = DateUtil.removerTimestamp(DateUtil.adicionarDias(data4, 1));
		
		Set<Date> datas = new TreeSet<Date>();
		
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		datas.add(data4);
		datas.add(data5);
		
		Date dataRecolhimentoPrevista =
			DateUtil.removerTimestamp(DateUtil.adicionarDias(new Date(), 10));
		
		int k = 0;
		
		for (Date data : datas) {
			
			for (int i = 0; i < 100; i++) {
			
				ProdutoLancamentoDTO produtoLancamento = new ProdutoLancamentoDTO();
				
				BigDecimal repartePrevisto = new BigDecimal("100.0");
				repartePrevisto = repartePrevisto.add(new BigDecimal(k));
				
				produtoLancamento.setIdLancamento((long) k);
				produtoLancamento.setIdProdutoEdicao((long) k);
				produtoLancamento.setDataLancamentoPrevista(data);
				produtoLancamento.setDataLancamentoDistribuidor(data);
				produtoLancamento.setRepartePrevisto(repartePrevisto);
				produtoLancamento.setDataRecolhimentoPrevista(dataRecolhimentoPrevista);
				produtoLancamento.setPeso(new BigDecimal(10));
				produtoLancamento.setValorTotal(new BigDecimal(2));
				produtoLancamento.setReparteFisico(new BigDecimal(5));
				produtoLancamento.setStatusLancamento(StatusLancamento.PLANEJADO.toString());
				produtoLancamento.setPeriodicidadeProduto(PeriodicidadeProduto.ANUAL.toString());
				
				if (k == 101) {
					produtoLancamento.setStatusLancamento(StatusLancamento.CANCELADO_GD.toString());
					produtoLancamento.setPeriodicidadeProduto(PeriodicidadeProduto.SEMANAL.toString());
				}
				
				produtosLancamento.add(produtoLancamento);
				
				
				k++;
			}
		}
		
		return produtosLancamento;
	}

}
