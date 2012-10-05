package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalhe;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.HistoricoLancamentoRepository;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;

public class ParciaisServiceImplTest extends AbstractRepositoryImplTest  {

	private Distribuidor distribuidor;
	
	private Integer fatorRelancamento = 5;
	
	private Integer peb = 20;
	
	private Date dtInicial;
	
	private Date dtFinal;
	
	private Usuario usuarioJoao;
	
	private ProdutoEdicao produtoEdicaoVeja1;
	
    private Lancamento lancamentoVeja;
	
	private Fornecedor fornecedorFC;
	
	private Fornecedor fornecedorDinap;
	
	private TipoFornecedor tipoFornecedorPublicacao;
	
	private Cota cotaManoel;
	
	private ItemRecebimentoFisico itemRecebimentoFisico1Veja;
	
	private ItemRecebimentoFisico itemRecebimentoFisico2Veja;
	
	private CFOP cfop;
	
	private TipoNotaFiscal tipoNotaFiscal;
	
	private ControleConferenciaEncalheCota controleConferenciaEncalheCota;
	
	@Autowired
	private ParciaisServiceImpl parciaisServiceImpl;
		
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	private LancamentoParcial lancamentoParcial;
	
	@Autowired
	private PeriodoLancamentoParcialRepository periodoLancamentoParcialRepository;
	
	@Autowired
	private HistoricoLancamentoRepository historicoLancamentoRepository;

	@Before
	public void setUp() {
		
		usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
				
		Banco banco = Fixture.hsbc(); 
		save(banco);
		
		
		PessoaJuridica pj = Fixture.pessoaJuridica("Distrib", "01.001.001/001-00",
				"000.000.000.00", "distrib@mail.com", "99.999-9");
		save(pj);
		
		ParametroCobrancaCota parametroCobranca = 
				Fixture.parametroCobrancaCota(null, 2, BigDecimal.TEN, null, 1, 
											  true, BigDecimal.TEN, null);
		
  		save(parametroCobranca);
		
		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, banco,
											BigDecimal.ONE, BigDecimal.ONE, parametroCobranca);
			save(formaBoleto);
		
			PoliticaCobranca politicaCobranca =
					Fixture.criarPoliticaCobranca(null, formaBoleto, true, true, true, 1,"Assunto","Mensagem",true,FormaEmissao.INDIVIDUAL_BOX);
			
		
		Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
		politicasCobranca.add(politicaCobranca);
		
		distribuidor = Fixture.distribuidor(1, pj, new Date(), politicasCobranca);
		
		distribuidor.setFatorRelancamentoParcial(fatorRelancamento);
		save(distribuidor);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Produto produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		save(produtoVeja);
		
		produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, peb,
				new Long(100), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		
		produtoEdicaoVeja1.setParcial(true);
		save(produtoEdicaoVeja1);
		
		
		dtInicial = Fixture.criarData(1, 1, 2010);
		dtFinal = Fixture.criarData(1, 1, 2011);
		
		lancamentoParcial = Fixture.criarLancamentoParcial(produtoEdicaoVeja1, dtInicial, dtFinal,StatusLancamentoParcial.PROJETADO);
		save(lancamentoParcial);
		
		
		//VENDAS PARCIAIS
		
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);

		cfop = Fixture.cfop5102();
		save(cfop);
		
		tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuarioJoao, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(produtoEdicaoVeja1, usuarioJoao,
				notaFiscal1Veja, 
				dtInicial,
				dtFinal,
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);
		
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuarioJoao, dtInicial,
				dtInicial, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuarioJoao, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				produtoEdicaoVeja1, 
				usuarioJoao,
				notaFiscal2Veja, 
				dtInicial, 
				dtFinal,
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50));
		
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuarioJoao, dtInicial,
				dtInicial, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoVeja1,
				dtInicial,
				dtInicial,
				dtFinal,
				dtFinal,
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, itemRecebimentoFisico1Veja, 1);
		
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),dtInicial, produtoEdicaoVeja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);
		
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				dtInicial, 
				produtoEdicaoVeja1, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		save(chamadaEncalhe);

		ChamadaEncalheCota chamadaEncalheCota = Fixture.chamadaEncalheCota(
				chamadaEncalhe, 
				false, 
				cotaManoel, 
				BigInteger.TEN);
		save(chamadaEncalheCota);

		ControleConferenciaEncalhe controleConferenciaEncalhe = 
				Fixture.controleConferenciaEncalhe(StatusOperacao.EM_ANDAMENTO, dtInicial);
		save(controleConferenciaEncalhe);

		controleConferenciaEncalheCota = Fixture.controleConferenciaEncalheCota(
				controleConferenciaEncalhe, 
				cotaManoel, 
				dtInicial, 
				dtFinal, 
				dtFinal, 
				StatusOperacao.CONCLUIDO, 
				usuarioJoao,
				box1);
		
		save(controleConferenciaEncalheCota);		

		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				dtInicial, 
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(8), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		ConferenciaEncalhe conferenciaEncalhe = Fixture.conferenciaEncalhe(mec, chamadaEncalheCota, controleConferenciaEncalheCota,
				dtFinal,BigInteger.valueOf(8),BigInteger.valueOf(8), produtoEdicaoVeja1);
		save(conferenciaEncalhe);
		
	}

	@Test
	public void gerarPeriodosParcias() {
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 1, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos = lancamentoRepository.buscarTodos();
		
		List<HistoricoLancamento> historicos = historicoLancamentoRepository.buscarTodos();
		
		List<PeriodoLancamentoParcial> periodos = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos.size(),2);
		Assert.assertEquals(historicos.size(),1);
		Assert.assertEquals(periodos.size(),1);
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 5, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos2 = lancamentoRepository.buscarTodos();
		List<HistoricoLancamento> historicos2 = historicoLancamentoRepository.buscarTodos();
		List<PeriodoLancamentoParcial> periodos2 = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos2.size(),7);
		Assert.assertEquals(historicos2.size(),6);
		Assert.assertEquals(periodos2.size(),6);
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 50, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos3 = lancamentoRepository.buscarTodos();
		List<HistoricoLancamento> historicos3 = historicoLancamentoRepository.buscarTodos();
		List<PeriodoLancamentoParcial> periodos3 = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos3.size(),16);
		Assert.assertEquals(historicos3.size(),15);
		Assert.assertEquals(periodos3.size(),15);
		
	}
	
	@Test
	public void obterDetalhesVenda() {
		
		List<ParcialVendaDTO> parciaisVenda = parciaisServiceImpl.obterDetalhesVenda(this.dtInicial, this.dtFinal, this.produtoEdicaoVeja1.getId());

		Assert.assertEquals(parciaisVenda.size(),1);	
	}
	
}
