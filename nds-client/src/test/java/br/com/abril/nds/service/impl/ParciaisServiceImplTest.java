package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.HistoricoLancamento;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
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
		
		Carteira carteira = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
		save(carteira);
		
		Banco banco = Fixture.hsbc(carteira); 
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
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		
		produtoEdicaoVeja1.setParcial(true);
		save(produtoEdicaoVeja1);
		
		
		dtInicial = Fixture.criarData(1, 1, 2010);
		dtFinal = Fixture.criarData(1, 1, 2011);
		
		lancamentoParcial = Fixture.criarLancamentoParcial(produtoEdicaoVeja1, dtInicial, dtFinal,StatusLancamentoParcial.PROJETADO);
		save(lancamentoParcial);
	}

	@Test
	public void gerarPeriodosParcias() {
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 1, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos = lancamentoRepository.buscarTodos();
		
		List<HistoricoLancamento> historicos = historicoLancamentoRepository.buscarTodos();
		
		List<PeriodoLancamentoParcial> periodos = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos.size(),1);
		Assert.assertEquals(historicos.size(),1);
		Assert.assertEquals(periodos.size(),1);
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 5, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos2 = lancamentoRepository.buscarTodos();
		List<HistoricoLancamento> historicos2 = historicoLancamentoRepository.buscarTodos();
		List<PeriodoLancamentoParcial> periodos2 = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos2.size(),6);
		Assert.assertEquals(historicos2.size(),6);
		Assert.assertEquals(periodos2.size(),6);
		
		parciaisServiceImpl.gerarPeriodosParcias(produtoEdicaoVeja1, 50, usuarioJoao, peb, distribuidor);
		
		List<Lancamento> lancamentos3 = lancamentoRepository.buscarTodos();
		List<HistoricoLancamento> historicos3 = historicoLancamentoRepository.buscarTodos();
		List<PeriodoLancamentoParcial> periodos3 = periodoLancamentoParcialRepository.buscarTodos();
		
		Assert.assertEquals(lancamentos3.size(),15);
		Assert.assertEquals(historicos3.size(),15);
		Assert.assertEquals(periodos3.size(),15);
		
		
	}

}
