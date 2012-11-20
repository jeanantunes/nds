package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaFaturamentoDTO;
import br.com.abril.nds.dto.CotaTransportadorDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class MovimentoFinanceiroCotaRepositoryImplTest extends AbstractRepositoryImplTest  {
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	private Cota cotaManoel;	
	
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito;
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe;
	private TipoMovimentoFinanceiro tipoMovimentoFinanceiroReparte;
	
	@Before
	public void setup() {
		try {
		Banco banco = Fixture.hsbc(); 
		save(banco);
		
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333333333333", "distrib_acme@mail.com", "99.999-9");
		save(juridicaDistrib);
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
  		save(parametroCobranca);
		
		FormaCobranca formaBoleto =
			Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
										BigDecimal.ONE, BigDecimal.ONE,parametroCobranca);
		save(formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		Distribuidor distribuidor = Fixture.distribuidor(1, juridicaDistrib, new Date(), politicasCobranca);
		save(distribuidor);
		
		politicaCobranca.setDistribuidor(distribuidor);
		save(politicaCobranca);
		
		tipoMovimentoFinanceiroCredito = Fixture.tipoMovimentoFinanceiroCredito();
		save(tipoMovimentoFinanceiroCredito);
		
		tipoMovimentoFinanceiroEnvioEncalhe = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();
		save(tipoMovimentoFinanceiroEnvioEncalhe);
		
		tipoMovimentoFinanceiroReparte = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		save(tipoMovimentoFinanceiroReparte);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		Box box = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box);
		
		PessoaFisica manoel = Fixture.pessoaFisica("319.435.088-95",
				"developertestermail@gmail.com", "Manoel da Silva");
		save(manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box);
		save(cotaManoel);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota1 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(200), null, StatusAprovacao.APROVADO, new Date(), true);

		MovimentoFinanceiroCota movimentoFinanceiroCota2 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(100), null, StatusAprovacao.APROVADO, new Date(), true);

		save(movimentoFinanceiroCota1, movimentoFinanceiroCota2);
		
		
		List<MovimentoFinanceiroCota> lista = new ArrayList<MovimentoFinanceiroCota>();
		lista.add(movimentoFinanceiroCota1);
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = 
				Fixture.consolidadoFinanceiroCota(lista, cotaManoel, new Date(), BigDecimal.TEN, new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0), new BigDecimal(0));
		save(consolidadoFinanceiroCota);
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void obterMovimentoFinanceiroCota() {
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiro =
			movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(null);
		
		Assert.assertNotNull(listaMovimentoFinanceiro);
		
		Assert.assertTrue(!listaMovimentoFinanceiro.isEmpty());

	}
	
	@Test
	public void obterMovimentoFinanceiroCotaPorCota() {
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiro =
				movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCota(cotaManoel.getId());
		
		Assert.assertNotNull(listaMovimentoFinanceiro);

		Assert.assertTrue(!listaMovimentoFinanceiro.isEmpty());
		
	}
	
	@Test
	public void obterDebitoCredioCotaDataOperacao() {
		
		Integer numeroCota = 123;
		
		Date dataOperacao = new Date();
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota =
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(numeroCota, dataOperacao, null);
		
		Assert.assertNotNull(listaDebitoCreditoCota);
		
	}
	
	public void obterDebitoCredioCotaDataOperacaoPoriposMovimentoFinanceiroIgnorados() {
		
		Integer numeroCota = 123;
		
		Date dataOperacao = new Date();
		
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(
				tipoMovimentoFinanceiroReparte
		);

		tiposMovimentoFinanceiroIgnorados.add( 
				tipoMovimentoFinanceiroEnvioEncalhe
		);

		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota =
				movimentoFinanceiroCotaRepository.obterDebitoCreditoCotaDataOperacao(numeroCota, dataOperacao, tiposMovimentoFinanceiroIgnorados);
		
		Assert.assertNotNull(listaDebitoCreditoCota);
		
	}
	
	@Test
	public void obterDebitoCreditoPorPeriodoOperacaoPorTiposMovimentoFinanceiroIgnorados() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		filtro.setIdCota(cotaManoel.getId());
		filtro.setDataRecolhimentoInicial(new Date());
		filtro.setDataRecolhimentoFinal(new Date());
		
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		
		tiposMovimentoFinanceiroIgnorados.add(
				tipoMovimentoFinanceiroReparte
		);

		tiposMovimentoFinanceiroIgnorados.add( 
				tipoMovimentoFinanceiroEnvioEncalhe
		);

		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaDTO = movimentoFinanceiroCotaRepository.obterDebitoCreditoPorPeriodoOperacao(filtro, tiposMovimentoFinanceiroIgnorados); 
		
		Assert.assertNotNull(listaDebitoCreditoCotaDTO);
		
		int tamanhoEsperado = 1;
		
		Assert.assertEquals(tamanhoEsperado, listaDebitoCreditoCotaDTO.size());
		
		BigDecimal valorTotalDebitoCredito = BigDecimal.ZERO;
		
		for(DebitoCreditoCotaDTO debitoCreditoCotaDTO: listaDebitoCreditoCotaDTO) {
			if(OperacaoFinaceira.CREDITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
				valorTotalDebitoCredito = valorTotalDebitoCredito.add(debitoCreditoCotaDTO.getValor());
			} else if(OperacaoFinaceira.DEBITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
				valorTotalDebitoCredito = valorTotalDebitoCredito.subtract(debitoCreditoCotaDTO.getValor());
			}
		}
		
		Assert.assertNotNull(valorTotalDebitoCredito);
		
	}
	
	@Test
	public void obterDebitoCreditoPorPeriodoOperacao() {
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		filtro.setIdCota(cotaManoel.getId());
		filtro.setDataRecolhimentoInicial(new Date());
		filtro.setDataRecolhimentoFinal(new Date());
		
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaDTO = movimentoFinanceiroCotaRepository.obterDebitoCreditoPorPeriodoOperacao(filtro, null); 
		
		Assert.assertNotNull(listaDebitoCreditoCotaDTO);
		
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaSucesso() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
		
		Assert.assertFalse(listaMovimentoFinanceiroCota.isEmpty());
		
		int expectedListSize = 1;
		int actualListSize = listaMovimentoFinanceiroCota.size();
		
		Assert.assertEquals(expectedListSize, actualListSize);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPaginadoSucesso() {

		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaRepository
				.buscarTodos().get(0);

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro = (TipoMovimentoFinanceiro) movimentoFinanceiroCota
				.getTipoMovimento();

		tipoMovimentoFinanceiro.setDescricao("nova descrição");

		MovimentoFinanceiroCota novoMovimentoFinanceiroCota = Fixture
				.movimentoFinanceiroCota(movimentoFinanceiroCota.getCota(),
						tipoMovimentoFinanceiro,
						movimentoFinanceiroCota.getUsuario(),
						new BigDecimal("450"), null, StatusAprovacao.APROVADO, new Date(), true);

		save(novoMovimentoFinanceiroCota);

		tipoMovimentoFinanceiro.setDescricao("outra nova descrição");

		novoMovimentoFinanceiroCota = Fixture.movimentoFinanceiroCota(
				movimentoFinanceiroCota.getCota(), tipoMovimentoFinanceiro,
				movimentoFinanceiroCota.getUsuario(),
				new BigDecimal("170"), null, StatusAprovacao.APROVADO, new Date(), true);

		save(novoMovimentoFinanceiroCota);
		
		Integer quantidadeTotalRegistros = 
				this.movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(filtroDebitoCreditoDTO);			

		Assert.assertNotNull(quantidadeTotalRegistros);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
			
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaPorIdTipoMovimento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setIdTipoMovimento(1L);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPorIdPeriodoLancamento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setDataLancamentoInicio(Fixture.criarData(1, Calendar.NOVEMBER, 2012));
		filtroDebitoCreditoDTO.setDataLancamentoFim(Fixture.criarData(31, Calendar.NOVEMBER, 2012));

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPorIdPeriodoVencimento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setDataVencimentoInicio(Fixture.criarData(1, Calendar.NOVEMBER, 2012));
		filtroDebitoCreditoDTO.setDataVencimentoFim(Fixture.criarData(31, Calendar.NOVEMBER, 2012));

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPorIdNumeroCota() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setNumeroCota(1);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaPaginacao() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setPaginacao(new PaginacaoVO());
		filtroDebitoCreditoDTO.getPaginacao().setPaginaAtual(1);
		filtroDebitoCreditoDTO.getPaginacao().setQtdResultadosPorPagina(1);
		filtroDebitoCreditoDTO.getPaginacao().setOrdenacao(Ordenacao.ASC);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoDataLancamento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.DATA_LANCAMENTO);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoDataVencimento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.DATA_VENCIMENTO);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoNomeCota() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.NOME_COTA);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoNumeroCota() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.NUMERO_COTA);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoObservacao() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.OBSERVACAO);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}
	
	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoTipoLancamento() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.TIPO_LANCAMENTO);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	public void obterMovimentosFinanceiroCotaOrdenadoValor() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = getFiltroDebitoCredito();
		filtroDebitoCreditoDTO.setColunaOrdenacao(FiltroDebitoCreditoDTO.ColunaOrdenacao.VALOR);

		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceiroCota(filtroDebitoCreditoDTO);
		
		Assert.assertNotNull(listaMovimentoFinanceiroCota);
	}

	@Test
	@SuppressWarnings("unused")
	public void obterMovimentoFinanceiroCotaParaMovimentoEstoqueCota() {
		
		Long idMovimentoEstoqueCota = 1L;
		
		MovimentoFinanceiroCota movimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaParaMovimentoEstoqueCota(idMovimentoEstoqueCota);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterContagemMovimentosFinanceiroCota() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = new FiltroDebitoCreditoDTO();
		
		Integer totalMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterContagemMovimentosFinanceiroCota(filtroDebitoCreditoDTO);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterQuantidadeMovimentoFinanceiroDataOperacao() {
		
		Date dataAtual = Fixture.criarData(06, Calendar.NOVEMBER, 2012);
		
		Long totalMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterQuantidadeMovimentoFinanceiroDataOperacao(dataAtual);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterSomatorioValorMovimentosFinanceiroCota() {
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = new FiltroDebitoCreditoDTO();
		
		BigDecimal somatoriaMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterSomatorioValorMovimentosFinanceiroCota(filtroDebitoCreditoDTO);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterSaldoCotaPorOperacaoPorOperacaoCredito() {
		
		Integer numeroCota = 1; 
		OperacaoFinaceira operacao = OperacaoFinaceira.CREDITO;
		
		BigDecimal somatoriaMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, operacao);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterSaldoCotaPorOperacaoPorOperacaoDebito() {
		
		Integer numeroCota = 1; 
		OperacaoFinaceira operacao = OperacaoFinaceira.DEBITO;
		
		BigDecimal somatoriaMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterSaldoCotaPorOperacao(numeroCota, operacao);

	}

	@Test
	public void obterMovimentosFinanceirosPorCobranca() {
		
		Long idCobranca = 1L;
		
		List<MovimentoFinanceiroCota> listaMovimentosFinanceiroCotas = 
				this.movimentoFinanceiroCotaRepository.obterMovimentosFinanceirosPorCobranca(idCobranca);
		
		Assert.assertNotNull(listaMovimentosFinanceiroCotas);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterSaldoCobrancaPorOperacaoPorCredito() {
		
		Long idCobranca = 1L; 
		OperacaoFinaceira operacao = OperacaoFinaceira.CREDITO;
		
		BigDecimal somatoriaMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, operacao);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterSaldoCobrancaPorOperacaoPorDebito() {
		
		Long idCobranca = 1L; 
		OperacaoFinaceira operacao = OperacaoFinaceira.DEBITO;
		
		BigDecimal somatoriaMovimentosFinanceiroCota = 
				this.movimentoFinanceiroCotaRepository.obterSaldoCobrancaPorOperacao(idCobranca, operacao);

	}

	@Test
	public void obterDebitoCreditoSumarizadosParaCotaDataOperacao() {
		
		Integer numeroCota = 1; 
		Date dataOperacao = Fixture.criarData(06, Calendar.NOVEMBER, 2012); 
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotas = 
				this.movimentoFinanceiroCotaRepository.obterDebitoCreditoSumarizadosParaCotaDataOperacao(numeroCota, dataOperacao, null);
		
		Assert.assertNotNull(listaDebitoCreditoCotas);

	}

	@Test
	public void obterDebitoCreditoSumarizadosParaCotaDataOperacaoPorTipoMovimentoFinanceiroIgnorados() {
		
		Integer numeroCota = 1; 
		Date dataOperacao = Fixture.criarData(06, Calendar.NOVEMBER, 2012); 
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroReparte);
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotas = 
				this.movimentoFinanceiroCotaRepository.obterDebitoCreditoSumarizadosParaCotaDataOperacao(numeroCota, dataOperacao, tiposMovimentoFinanceiroIgnorados);
		
		Assert.assertNotNull(listaDebitoCreditoCotas);

	}

	@Test
	public void obterFaturamentoCotasPorPeriodo() {
		
		List<Cota> cotas = new ArrayList<Cota>();
		cotas.add(cotaManoel);
		Date dataInicial = Fixture.criarData(01, Calendar.NOVEMBER, 2012); 
		Date dataFinal = Fixture.criarData(31, Calendar.NOVEMBER, 2012); 
		
		List<CotaFaturamentoDTO> listaCotaFaturamentos = 
				this.movimentoFinanceiroCotaRepository.obterFaturamentoCotasPorPeriodo(cotas, dataInicial, dataFinal);
		
		Assert.assertNotNull(listaCotaFaturamentos);

	}

	@Test
	public void obterResumoTransportadorCotaOrdenadoRoteiro() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("roteiro");
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaOrdenadoRota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("rota");
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaOrdenadoNumeroCota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("numCota");
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaOrdenadoNomeCota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("nomeCota");
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaOrdenadoValor() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("valor");
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaPorDataDe() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("roteiro");
		filtro.setEntregaDataInicio(Fixture.criarData(06, Calendar.NOVEMBER, 2012));
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaPorDataAte() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("roteiro");
		filtro.setEntregaDataFim(Fixture.criarData(06, Calendar.NOVEMBER, 2012));
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaPorDataIdTransportador() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("roteiro");
		filtro.setIdTransportador(1L);
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	public void obterResumoTransportadorCotaPaginacao() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setSortColumn("roteiro");
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(1);
		
		List<CotaTransportadorDTO> listaCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterResumoTransportadorCota(filtro);
		
		Assert.assertNotNull(listaCotaTransportadores);

	}

	@Test
	@SuppressWarnings("unused")
	public void obterCountResumoTransportadorCota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		
		Long totalCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterCountResumoTransportadorCota(filtro);
		
	}

	@Test
	@SuppressWarnings("unused")
	public void obterCountResumoTransportadorCotaPorDataDe() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setEntregaDataInicio(Fixture.criarData(06, Calendar.NOVEMBER, 2012));
		
		Long totalCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterCountResumoTransportadorCota(filtro);
		
	}

	@Test
	@SuppressWarnings("unused")
	public void obterCountResumoTransportadorCotaPorDataAte() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setEntregaDataFim(Fixture.criarData(06, Calendar.NOVEMBER, 2012));
		
		Long totalCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterCountResumoTransportadorCota(filtro);
		
	}

	@Test
	@SuppressWarnings("unused")
	public void obterCountResumoTransportadorCotaPorIdCota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		filtro.setIdCota(1L);
		
		Long totalCotaTransportadores = this.movimentoFinanceiroCotaRepository.obterCountResumoTransportadorCota(filtro);
		
	}

	@Test
	public void obterDetalhesTrasportadorPorCota() {
		
		FiltroRelatorioServicosEntregaDTO filtro = new FiltroRelatorioServicosEntregaDTO();
		
		List<MovimentoFinanceiroDTO> listaMovimentoFinanceiros = this.movimentoFinanceiroCotaRepository.obterDetalhesTrasportadorPorCota(filtro);
		
		Assert.assertNotNull(listaMovimentoFinanceiros);
		
	}

	private FiltroDebitoCreditoDTO getFiltroDebitoCredito() {

		Calendar calendar = Calendar.getInstance();
		
		FiltroDebitoCreditoDTO filtroDebitoCreditoDTO = new FiltroDebitoCreditoDTO();
		
		filtroDebitoCreditoDTO.setColunaOrdenacao(ColunaOrdenacao.TIPO_LANCAMENTO);
		
		calendar.add(Calendar.DATE, -5);
		filtroDebitoCreditoDTO.setDataVencimentoInicio(calendar.getTime());
		
		calendar.add(Calendar.DATE, 10);
		filtroDebitoCreditoDTO.setDataVencimentoFim(calendar.getTime());
		
		filtroDebitoCreditoDTO.setPaginacao(new PaginacaoVO(1, 1, "asc"));

		return filtroDebitoCreditoDTO;
	}

	@Test
	@SuppressWarnings("unused")
	public void obterSaldoDistribuidor() {
		
		Date data = null;
	    TipoCota tipoCota = null; 
	    OperacaoFinaceira operacaoFinaceira = null;
		
		BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(data, tipoCota, operacaoFinaceira);
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterSaldoDistribuidorPorData() {
		
		Date data = Fixture.criarData(20, Calendar.NOVEMBER, 2012);
	    TipoCota tipoCota = null; 
	    OperacaoFinaceira operacaoFinaceira = null;
		
		BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(data, tipoCota, operacaoFinaceira);
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterSaldoDistribuidorPorTipoCota() {
		
		Date data = null;
	    TipoCota tipoCota = TipoCota.A_VISTA; 
	    OperacaoFinaceira operacaoFinaceira = null;
		
		BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(data, tipoCota, operacaoFinaceira);
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterSaldoDistribuidorPorOperacaoFinanceiraCredito() {
		
		Date data = null;
	    TipoCota tipoCota = null; 
	    OperacaoFinaceira operacaoFinaceira = OperacaoFinaceira.CREDITO;
		
		BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(data, tipoCota, operacaoFinaceira);
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void obterSaldoDistribuidorPorOperacaoFinanceiraDebito() {
		
		Date data = null;
	    TipoCota tipoCota = null; 
	    OperacaoFinaceira operacaoFinaceira = OperacaoFinaceira.DEBITO;
		
		BigDecimal saldo = this.movimentoFinanceiroCotaRepository.obterSaldoDistribuidor(data, tipoCota, operacaoFinaceira);
		
	}
	
}
