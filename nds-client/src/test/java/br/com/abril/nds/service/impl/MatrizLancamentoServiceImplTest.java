package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
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
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.repository.DistribuidorRepository;
import br.com.abril.nds.repository.LancamentoRepository;

public class MatrizLancamentoServiceImplTest {
	
	private MatrizLancamentoServiceImpl service;
	
	private LancamentoRepository lancamentoRepository;
	private DistribuidorRepository distribuidorRepository;

	private DistribuicaoFornecedor dinapSegunda;
	private DistribuicaoFornecedor dinapQuarta;
	private DistribuicaoFornecedor dinapSexta;
	private DistribuicaoFornecedor fcSegunda;
	private DistribuicaoFornecedor fcSexta;
	
	private Distribuidor distribuidor;
	
	private Fornecedor fornecedorDinap;
	private Fornecedor fornecedorFc;
	
	@Before
	public void setUp() {
		service = new MatrizLancamentoServiceImpl();
		lancamentoRepository = Mockito.mock(LancamentoRepository.class);
		distribuidorRepository = Mockito.mock(DistribuidorRepository.class);
		
		service.lancamentoRepository = lancamentoRepository;
		service.distribuidorRepository = distribuidorRepository;
		
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		Banco banco = Fixture.hsbc(carteira); 
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN);
		
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
		
		dinapSegunda = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		dinapQuarta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		dinapSexta = Fixture.distribuicaoFornecedor(
				fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);

		fcSegunda = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		fcSexta = Fixture.distribuicaoFornecedor(
				fornecedorFc, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO, distribuidor);
		
	}
	
	@Test
	public void obterResumoPeriodo() {
		List<Long> fornecedores = Arrays.asList(1L, 2L);
		List<DistribuicaoFornecedor> distribuicoes = Arrays.asList(
				dinapSegunda, dinapQuarta, dinapSexta, fcSegunda, fcSexta);
		Mockito.when(
				distribuidorRepository.buscarDiasDistribuicaoFornecedor(fornecedores, OperacaoDistribuidor.DISTRIBUICAO))
				.thenReturn(distribuicoes);
		List<Date> periodo = Arrays.asList(
				Fixture.criarData(2, Calendar.MARCH, 2012),
				Fixture.criarData(5, Calendar.MARCH, 2012),
				Fixture.criarData(7, Calendar.MARCH, 2012));

		List<ResumoPeriodoBalanceamentoDTO> resumos = new ArrayList<ResumoPeriodoBalanceamentoDTO>();

		Mockito.when(
				lancamentoRepository
						.buscarResumosPeriodo(periodo, fornecedores, GrupoProduto.CROMO))
				.thenReturn(resumos);
		Date dataInicial = Fixture.criarData(1, Calendar.MARCH, 2012);
		service.obterResumoPeriodo(dataInicial, fornecedores);

		Mockito.verify(distribuidorRepository).buscarDiasDistribuicaoFornecedor(
				fornecedores, OperacaoDistribuidor.DISTRIBUICAO);
		Mockito.verify(lancamentoRepository).buscarResumosPeriodo(periodo,
				fornecedores, GrupoProduto.CROMO);
	}
	
	@Test
	public void balancear() {
		
		FiltroLancamentoDTO filtro = this.montarFiltro();
		
		List<DistribuicaoFornecedor> listaDistribuicaoFornecedor =
			this.listaDistribuicaoFornecedor();
		
		List<DistribuicaoDistribuidor> listaDistribuicaoDistribuidor =
			this.listaDistribuicaoDistribuidor();
		
		Mockito.when(this.distribuidorRepository.obter()).thenReturn(distribuidor);
		
		Mockito.when(
			this.distribuidorRepository.buscarDiasDistribuicaoFornecedor(
				filtro.getIdsFornecedores(), OperacaoDistribuidor.DISTRIBUICAO)).thenReturn(listaDistribuicaoFornecedor);
		
		Mockito.when(
			this.distribuidorRepository.buscarDiasDistribuicaoDistribuidor(
				distribuidor.getId(), OperacaoDistribuidor.DISTRIBUICAO)).thenReturn(listaDistribuicaoDistribuidor);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento =
			service.obterMatrizLancamento(this.montarFiltro());
		
		Assert.assertNull(balanceamentoLancamento);
	}
	
	private FiltroLancamentoDTO montarFiltro() {
		
		Date data = Fixture.criarData(11, 5, 2012);
		
		List<Long> idsFornecedores = new ArrayList<Long>();
		
		idsFornecedores.add(fornecedorDinap.getId());
		idsFornecedores.add(fornecedorFc.getId());
		
		FiltroLancamentoDTO filtro =
			new FiltroLancamentoDTO(data, idsFornecedores, null, "codigoProduto");
		
		return filtro;
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

}
