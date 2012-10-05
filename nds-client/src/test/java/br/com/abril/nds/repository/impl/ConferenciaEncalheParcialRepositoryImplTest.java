package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.ConferenciaEncalheParcial;
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
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ConferenciaEncalheParcialRepository;

public class ConferenciaEncalheParcialRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private ConferenciaEncalheParcialRepository conferenciaEncalheParcialRepositoryImpl;
	
	private Lancamento lancamentoVeja;
	
    private Fornecedor fornecedorFC;
	private Fornecedor fornecedorDinap;
	private TipoProduto tipoCromo;
	private TipoFornecedor tipoFornecedorPublicacao;
	private Editor abril;
	
	
	@Before
	public void setUp() {
		abril = Fixture.editoraAbril();
		save(abril);
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(fornecedorFC, fornecedorDinap);

		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		TipoProduto tipoRevista = Fixture.tipoRevista(ncmRevistas);
		tipoCromo = Fixture.tipoCromo(ncmCromo);
		save(tipoRevista, tipoCromo);
		
		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.setEditor(abril);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.setEditor(abril);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.setEditor(abril);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.setEditor(abril);
		capricho.addFornecedor(fornecedorDinap);
		save(veja, quatroRodas, infoExame, capricho);
		
		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.setEditor(abril);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(cromoReiLeao);

		ProdutoEdicao veja1 = Fixture.produtoEdicao("1", 1L, 10, 7,
				new Long(100), BigDecimal.TEN, new BigDecimal(15), "ABCDEFGHIJKLMNOPQ", 1L, veja, null, false);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao("1", 2L, 15, 30,
				new Long(100), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPA", 2L,
				quatroRodas, null, false);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao("1", 3L, 5, 30,
				new Long(100), BigDecimal.TEN, new BigDecimal(12), "ABCDEFGHIJKLMNOPB", 3L, infoExame, null, false);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao("1", 1L, 10, 15,
				new Long(120), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPC", 4L, capricho, null, false);
		
		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao("1", 1L, 100, 60,
				new Long(10), BigDecimal.ONE, new BigDecimal(1.5), "ABCDEFGHIJKLMNOPD", 5L, cromoReiLeao, null, false);
		
		save(veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		CFOP cfop = Fixture.cfop5102();
		save(cfop);
		
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(tipoNotaFiscal);
		
		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
				notaFiscal1Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(50));
		save(itemNotaFiscal1Veja);
		
		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
				notaFiscal1Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico1Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico1Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico1Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
				veja1, 
				usuario,
				notaFiscal2Veja, 
				Fixture.criarData(22, Calendar.FEBRUARY,2012), 
				Fixture.criarData(22, Calendar.FEBRUARY,2012),
				TipoLancamento.LANCAMENTO,
				BigInteger.valueOf(50));
		
		save(itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
				notaFiscal2Veja, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico2Veja);
			
		ItemRecebimentoFisico itemRecebimentoFisico2Veja = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, BigInteger.valueOf(50));
		save(itemRecebimentoFisico2Veja);
		
		
		NotaFiscalEntradaFornecedor notaFiscal4Rodas= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC, tipoNotaFiscal,
						usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(notaFiscal4Rodas);

		ItemNotaFiscalEntrada itemNotaFiscal4Rodas = 
		
				Fixture.itemNotaFiscal(
						quatroRoda2, 
						usuario,
						notaFiscal4Rodas, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						Fixture.criarData(22, Calendar.FEBRUARY,2012), 
						TipoLancamento.LANCAMENTO,
						BigInteger.valueOf(25));
		
		save(itemNotaFiscal4Rodas);
		
		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
				notaFiscal4Rodas, usuario, dataRecebimento,
				dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(recebimentoFisico4Rodas);
			
		ItemRecebimentoFisico itemRecebimentoFisico4Rodas = 
				Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, BigInteger.valueOf(25));
		save(itemRecebimentoFisico4Rodas);
		
		lancamentoVeja = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				veja1,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				BigInteger.valueOf(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, itemRecebimentoFisico1Veja, 1);
		
		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);
		
		
		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(lancamentoVeja, estudo);

		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				veja1, cotaManoel, BigInteger.TEN, BigInteger.ZERO);
		save(estoqueProdutoCota);
		
		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(usuarioJoao);
		
		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(tipoMovimentoEnvioEncalhe);

		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCota,
				BigInteger.valueOf(12), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(25), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(14), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				veja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigInteger.valueOf(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		
		save(mec);
		
		
		ConferenciaEncalheParcial conferenciaEncalheParcial = Fixture.conferenciaEncalheParcial(
				usuarioJoao, 
				veja1, 
				StatusAprovacao.PENDENTE, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				new Date(), 
				BigInteger.TEN);
		save(conferenciaEncalheParcial);
		
		conferenciaEncalheParcial = Fixture.conferenciaEncalheParcial(
				usuarioJoao, 
				veja1, 
				StatusAprovacao.PENDENTE, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				new Date(), 
				BigInteger.TEN);
		save(conferenciaEncalheParcial);
		
		conferenciaEncalheParcial = Fixture.conferenciaEncalheParcial(
				usuarioJoao, 
				veja1, 
				StatusAprovacao.PENDENTE, 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				new Date(), 
				BigInteger.TEN);
		
		save(conferenciaEncalheParcial);
		
	}
	
	
	@Test
	public void testObterQtdTotalEncalheParcial() {
		
		BigInteger qtd = conferenciaEncalheParcialRepositoryImpl.obterQtdTotalEncalheParcial(StatusAprovacao.PENDENTE, Fixture.criarData(28, Calendar.FEBRUARY, 2012), "1" ,1L);
		
		Assert.assertTrue(qtd.intValue() == 30);
	}
	
	@Test
	public void testObterListaContagemDevolucao() {
		
		List<ContagemDevolucaoDTO> result = conferenciaEncalheParcialRepositoryImpl.obterListaContagemDevolucao(false, false, StatusAprovacao.PENDENTE, null, null, null, null);
		
		Assert.assertEquals(1, result.size());
	}
	
	@Test
	public void testObterListaConferenciaEncalhe() {
		
		boolean 			diferencaApurada 	= false;
		boolean 			nfParcialGerada  	= false;
		StatusAprovacao 	statusAprovacao 	= StatusAprovacao.PENDENTE;
		Date 				dataMovimento 		= Fixture.criarData(28, Calendar.FEBRUARY, 2012);
		Long 				idProdutoEdicao 	= null;
		String 				codigoProduto 		= "1";
		Long 				numeroEdicao 		= 1L;
		
		List<ConferenciaEncalheParcial> result = null;
		
		result = conferenciaEncalheParcialRepositoryImpl.obterListaConferenciaEncalhe(
				diferencaApurada, 
				nfParcialGerada, 
				statusAprovacao, 
				dataMovimento, 
				idProdutoEdicao,
				codigoProduto,
				numeroEdicao);
		
		Assert.assertEquals(3, result.size());
		
		diferencaApurada 	= false;                      
		nfParcialGerada  	= false;                      
		statusAprovacao 	= StatusAprovacao.APROVADO;   
		dataMovimento 		= null;                 
		idProdutoEdicao 	= null;                         
		codigoProduto 		= null;                         
		numeroEdicao 		= null;                         

		result = conferenciaEncalheParcialRepositoryImpl.obterListaConferenciaEncalhe(
				diferencaApurada, 
				nfParcialGerada, 
				statusAprovacao, 
				dataMovimento, 
				idProdutoEdicao,
				codigoProduto,
				numeroEdicao);
		
		Assert.assertEquals(0, result.size());
		
		diferencaApurada 	= false;                      
		nfParcialGerada  	= false;                      
		statusAprovacao 	= StatusAprovacao.APROVADO;   
		dataMovimento 		= Fixture.criarData(28, Calendar.FEBRUARY, 2012);       
		idProdutoEdicao 	= null;                         
		codigoProduto 		= "1";                         
		numeroEdicao 		= 1L;                         
	}

}
