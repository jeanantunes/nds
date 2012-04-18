package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
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
	
	@Before
	public void setUp() {
		service = new MatrizLancamentoServiceImpl();
		lancamentoRepository = Mockito.mock(LancamentoRepository.class);
		distribuidorRepository = Mockito.mock(DistribuidorRepository.class);
		
		service.lancamentoRepository = lancamentoRepository;
		service.distribuidorRepository = distribuidorRepository;
		
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		Banco banco = Fixture.hsbc(carteira); 
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem");
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"33.333.333/0001-33", "333.333.333.333", "distrib_acme@mail.com");
		Distribuidor distribuidor = Fixture.distribuidor(juridicaDistrib, new Date(), politicaCobranca);
		
		PessoaJuridica juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11.111.111/0001-11", "111.111.111.111", "dinap@mail.com");
		PessoaJuridica juridicaFc = Fixture.pessoaJuridica("FC",
				"22.222.222/0001-22", "222.222.222.222", "fc@mail.com");
		
		Fornecedor fornecedorDinap = Fixture.fornecedor(juridicaDinap,
				SituacaoCadastro.ATIVO, true, Fixture.tipoFornecedorPublicacao());
		Fornecedor fornecedorFc = Fixture.fornecedor(juridicaFc,
				SituacaoCadastro.ATIVO, true, Fixture.tipoFornecedorPublicacao());
		
		dinapSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		dinapQuarta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		dinapSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);

		fcSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		fcSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		
	}
	
	@Test
	public void obterResumoPeriodo() {
		List<Long> fornecedores = Arrays.asList(1L, 2L);
		List<DistribuicaoFornecedor> distribuicoes = Arrays.asList(
				dinapSegunda, dinapQuarta, dinapSexta, fcSegunda, fcSexta);
		Mockito.when(
				distribuidorRepository.buscarDiasDistribuicao(fornecedores))
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

		Mockito.verify(distribuidorRepository).buscarDiasDistribuicao(
				fornecedores);
		Mockito.verify(lancamentoRepository).buscarResumosPeriodo(periodo,
				fornecedores, GrupoProduto.CROMO);
	}
	

}
