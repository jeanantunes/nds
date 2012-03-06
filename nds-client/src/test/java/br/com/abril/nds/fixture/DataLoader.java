package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;

public class DataLoader {

	private static TipoMovimento tipoMovimentoFaltaEm;
	private static TipoMovimento tipoMovimentoFaltaDe;
	private static TipoMovimento tipoMovimentoSobraEm;
	private static TipoMovimento tipoMovimentoSobraDe;
	private static TipoMovimento tipoMovimentoRecFisico;
	private static TipoMovimento tipoMovimentoRecReparte;
	private static CFOP cfop5102;
	private static TipoNotaFiscal tipoNotaFiscalRecebimento;
	private static Usuario usuarioJoao;
	private static Fornecedor fornecedorAcme;
	private static Fornecedor fornecedorDinap;
	private static Fornecedor fornecedorFc;
	private static Cota cotaManoel;
	private static Distribuidor distribuidor;
	private static TipoProduto tipoProdutoRevista;
	private static Produto produtoVeja;
	private static Produto produtoSuper;
	private static Produto produtoCapricho;
	private static Produto produtoInfoExame;
	private static Produto produtoQuatroRodas;
	private static Produto produtoBoaForma;
	private static Produto produtoBravo;
	private static Produto produtoCaras;
	private static Produto produtoCasaClaudia;
	private static Produto produtoClaudia;
	private static Produto produtoContigo;
	private static Produto produtoManequim;
	private static Produto produtoNatGeo;
	private static Produto produtoPlacar;
	private static ProdutoEdicao produtoEdicaoVeja1;
	private static ProdutoEdicao produtoEdicaoVeja2;
	private static ProdutoEdicao produtoEdicaoVeja3;
	private static ProdutoEdicao produtoEdicaoVeja4;
	private static ProdutoEdicao produtoEdicaoSuper1;
	private static ProdutoEdicao produtoEdicaoCapricho1;
	private static ProdutoEdicao produtoEdicaoInfoExame1;
	private static ProdutoEdicao produtoEdicaoQuatroRodas1;
	private static ProdutoEdicao produtoEdicaoBoaForma1;
	private static ProdutoEdicao produtoEdicaoBravo1;
	private static ProdutoEdicao produtoEdicaoCaras1;
	private static ProdutoEdicao produtoEdicaoCasaClaudia1;
	private static ProdutoEdicao produtoEdicaoClaudia1;
	private static ProdutoEdicao produtoEdicaoContigo1;
	private static ProdutoEdicao produtoEdicaoManequim1;
	private static ProdutoEdicao produtoEdicaoNatGeo1;
	private static ProdutoEdicao produtoEdicaoPlacar1;
	private static Lancamento lancamentoVeja1;
	private static Lancamento lancamentoVeja2;
	private static Estudo estudoVeja1;
	private static NotaFiscalFornecedor notaFiscalFornecedor;
	private static ItemNotaFiscal itemNotaFiscalFornecedor;
	private static RecebimentoFisico recebimentoFisico;
	private static ItemRecebimentoFisico itemRecebimentoFisico;
	private static EstoqueProduto estoqueProdutoVeja1;
	private static MovimentoEstoque movimentoRecFisicoVeja1;


	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath:/applicationContext-dataLoader.xml");
		SessionFactory sf = null;
		Session session = null;
		Transaction tx = null;
		boolean commit = false;
		try {
			sf = ctx.getBean(SessionFactory.class);
			session = sf.openSession();
			tx = session.beginTransaction();
			carregarDados(session);
			commit = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (commit) {
				tx.commit();
			} else {
				tx.rollback();
			}
			if (session != null) {
				session.close();
			}
			if (sf != null) {
				sf.close();
			}
		}
	}

	private static void carregarDados(Session session) {
		criarDistribuidor(session);
		criarParametrosSistema(session);
		criarUsuarios(session);
		criarFornecedores(session);
		criarDiasDistribuicaoFornecedores(session);
		criarCotas(session);
		criarTiposProduto(session);
		criarProdutos(session);
		criarProdutosEdicao(session);
		criarCFOP(session);
		criarTiposMovimento(session);
		criarTiposNotaFiscal(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarMovimentosEstoque(session);
		criarLancamentos(session);
		criarEstudos(session);
		criarMovimentosEstoqueCota(session);
		

		
		// Inicio dos inserts na tabela MOVIMENTO_ESTOQUE
		
		MovimentoEstoque movimentoEstoqueDiferenca = Fixture.movimentoEstoque(null, produtoEdicaoVeja1,

																			  tipoMovimentoRecFisico, usuarioJoao,
																			  estoqueProdutoVeja1, new Date(),
																			  new BigDecimal(1), StatusAprovacao.APROVADO,
																			  "Aprovado.");
		session.save(movimentoEstoqueDiferenca);
		
		MovimentoEstoque movimentoEstoqueDiferenca2 = Fixture.movimentoEstoque(null, produtoEdicaoVeja2,
																			   tipoMovimentoRecFisico, usuarioJoao,
																			   estoqueProdutoVeja1, new Date(),
																			   new BigDecimal(2), StatusAprovacao.APROVADO,
																			   "Aprovado.");
		session.save(movimentoEstoqueDiferenca2);
		
		MovimentoEstoque movimentoEstoqueDiferenca3 = Fixture.movimentoEstoque(null, produtoEdicaoVeja3,
																			   tipoMovimentoRecFisico, usuarioJoao,
																			   estoqueProdutoVeja1, new Date(),
																			   new BigDecimal(3), StatusAprovacao.APROVADO,
																			   "Aprovado.");
		session.save(movimentoEstoqueDiferenca3);
		
		MovimentoEstoque movimentoEstoqueDiferenca4 = Fixture.movimentoEstoque(null, produtoEdicaoVeja4,
																			   tipoMovimentoRecFisico, usuarioJoao,
																			   estoqueProdutoVeja1, new Date(),
																			   new BigDecimal(4), StatusAprovacao.APROVADO,
																			   "Aprovado.");
		session.save(movimentoEstoqueDiferenca4);
		
		// Fim dos inserts na tabela MOVIMENTO_ESTOQUE
		
		// Inicio dos inserts na tabela DIFERENCA
		
		Diferenca diferenca = Fixture.diferenca(new BigDecimal(1), usuarioJoao,
												produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
						  						StatusConfirmacao.CONFIRMADO, null,
						  						movimentoEstoqueDiferenca);
		session.save(diferenca);
		
		Diferenca diferenca2 = Fixture.diferenca(new BigDecimal(2), usuarioJoao,
												produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
												StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico,
												movimentoEstoqueDiferenca2);
		session.save(diferenca2);
		
		Diferenca diferenca3 = Fixture.diferenca(new BigDecimal(3), usuarioJoao,
												 produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
												 StatusConfirmacao.CONFIRMADO, null,
												 movimentoEstoqueDiferenca3);
		session.save(diferenca3);
		
		Diferenca diferenca4 = Fixture.diferenca(new BigDecimal(4), usuarioJoao,
												 produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
												 StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico,
												 movimentoEstoqueDiferenca4);
		session.save(diferenca4);
		
		// Fim dos inserts na tabela DIFERENCA
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja1, tipoMovimentoFaltaEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_EM);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.FALTA_DE);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja3, tipoMovimentoSobraDe, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_DE);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja4, tipoMovimentoSobraEm, 
				usuarioJoao, estoqueProdutoVeja1, TipoDiferenca.SOBRA_EM);
	}

	private static void criarMovimentosEstoqueCota(Session session) {
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
	}

	private static void criarParametrosSistema(Session session) {
		ParametroSistema parametroSistema = Fixture.parametroSistema(1L,
				TipoParametroSistema.PATH_IMAGENS_CAPA,
				"C:\\apache-tomcat-7.0.25\\webapps\\nds-client\\capas\\");
		session.save(parametroSistema);
	}

	private static void criarMovimentosEstoque(Session session) {
		movimentoRecFisicoVeja1 = Fixture.movimentoEstoque(
				itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				estoqueProdutoVeja1, StatusAprovacao.APROVADO, "Aprovado");
		session.save(movimentoRecFisicoVeja1);
		session.update(estoqueProdutoVeja1);
	}

	private static void criarEstoquesProdutos(Session session) {
		estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigDecimal.ZERO);
		session.save(estoqueProdutoVeja1);
	}

	private static void criarRecebimentosFisicos(Session session) {
		recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);
		
		itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigDecimal.TEN);
		session.save(itemRecebimentoFisico);
	}

	private static void criarNotasFiscais(Session session) {
		notaFiscalFornecedor = Fixture
				.notaFiscalFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		session.save(notaFiscalFornecedor);

		itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), BigDecimal.TEN);
		session.save(itemNotaFiscalFornecedor);
	}

	private static void criarEstudos(Session session) {
		estudoVeja1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		session.save(estudoVeja1);
	}

	private static void criarLancamentos(Session session) {
		lancamentoVeja1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigDecimal.TEN, StatusLancamento.RECEBIDO,
						itemRecebimentoFisico);
		session.save(lancamentoVeja1);
		
		lancamentoVeja2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),
						new Date(), BigDecimal.TEN, StatusLancamento.RECEBIDO,
						null);
		session.save(lancamentoVeja2);

		Lancamento lancamentoSuper1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoSuper1);
		
		Lancamento lancamentoCapricho1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoCapricho1);
		
		Lancamento lancamentoInfoExame1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoInfoExame1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), new BigDecimal(500), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoInfoExame1);
		
		Lancamento lancamentoQuatroRodas1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoQuatroRodas1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoQuatroRodas1.getPeb()), new Date(),
								new Date(), new BigDecimal(1500), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoQuatroRodas1);
		
		Lancamento lancamentoBoaForma1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBoaForma1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBoaForma1.getPeb()), new Date(),
								new Date(), new BigDecimal(190), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoBoaForma1);
		
		Lancamento lancamentoBravo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBravo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBravo1.getPeb()), new Date(),
								new Date(), new BigDecimal(250), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoBravo1);
		
		Lancamento lancamentoBoaCaras1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCaras1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCaras1.getPeb()), new Date(),
								new Date(), new BigDecimal(290), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoBoaCaras1);
		
		Lancamento lancamentoCasaClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCasaClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCasaClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(350), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoCasaClaudia1);
		
		Lancamento lancamentoClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(400), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoClaudia1);
		
		Lancamento lancamentoContigo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoContigo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoContigo1.getPeb()), new Date(),
								new Date(), new BigDecimal(185), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoContigo1);
		
		Lancamento lancamentoManequim1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoManequim1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoManequim1.getPeb()), new Date(),
								new Date(), new BigDecimal(225), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoManequim1);
		
		Lancamento lancamentoNatGeo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoNatGeo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoNatGeo1.getPeb()), new Date(),
								new Date(), new BigDecimal(75), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoNatGeo1);
		
		Lancamento lancamentoPlacar1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoPlacar1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoPlacar1.getPeb()), new Date(),
								new Date(), new BigDecimal(195), StatusLancamento.RECEBIDO,
								null);
		session.save(lancamentoPlacar1);
	}

	private static void criarProdutosEdicao(Session session) {
		produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		session.save(produtoEdicaoVeja1);
		
		produtoEdicaoVeja2 = Fixture.produtoEdicao(2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		session.save(produtoEdicaoVeja2);
		
		produtoEdicaoVeja3 = Fixture.produtoEdicao(3L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		session.save(produtoEdicaoVeja3);
		
		produtoEdicaoVeja4 = Fixture.produtoEdicao(4L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja);
		session.save(produtoEdicaoVeja4);
		
		produtoEdicaoSuper1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoSuper);
		session.save(produtoEdicaoSuper1);
		
		produtoEdicaoCapricho1 = Fixture.produtoEdicao(1L, 9, 14,
				new BigDecimal(0.15), new BigDecimal(9), new BigDecimal(13.5),
				produtoCapricho);
		session.save(produtoEdicaoCapricho1);
		
		produtoEdicaoInfoExame1 = Fixture.produtoEdicao(1L, 12, 30,
				new BigDecimal(0.25), new BigDecimal(11), new BigDecimal(14.5),
				produtoInfoExame);
		session.save(produtoEdicaoInfoExame1);
		
		produtoEdicaoQuatroRodas1 = Fixture.produtoEdicao(1L, 7, 30,
				new BigDecimal(0.30), new BigDecimal(12.5), new BigDecimal(16.5),
				produtoQuatroRodas);
		session.save(produtoEdicaoQuatroRodas1);
		
		produtoEdicaoBoaForma1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(12), new BigDecimal(15),
				produtoBoaForma);
		session.save(produtoEdicaoBoaForma1);
		
		produtoEdicaoBravo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.12), new BigDecimal(17), new BigDecimal(20),
				produtoBravo);
		session.save(produtoEdicaoBravo1);
		
		produtoEdicaoCaras1 = Fixture.produtoEdicao(1L, 15, 30,
				new BigDecimal(0.20), new BigDecimal(20), new BigDecimal(25),
				produtoCaras);
		session.save(produtoEdicaoCaras1);
		
		produtoEdicaoCasaClaudia1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(20), new BigDecimal(25),
				produtoCasaClaudia);
		session.save(produtoEdicaoCasaClaudia1);
		
		produtoEdicaoClaudia1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(10), new BigDecimal(11),
				produtoClaudia);
		session.save(produtoEdicaoClaudia1);
		
		produtoEdicaoContigo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(12), new BigDecimal(15),
				produtoContigo);
		session.save(produtoEdicaoContigo1);
		
		produtoEdicaoManequim1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.10), new BigDecimal(15), new BigDecimal(20),
				produtoManequim);
		session.save(produtoEdicaoManequim1);
		
		produtoEdicaoNatGeo1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.15), new BigDecimal(20), new BigDecimal(23),
				produtoNatGeo);
		session.save(produtoEdicaoNatGeo1);
		
		produtoEdicaoPlacar1 = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(9), new BigDecimal(12),
				produtoPlacar);
		session.save(produtoEdicaoPlacar1);
	}

	private static void criarProdutos(Session session) {
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		session.save(produtoVeja);

		produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
		produtoSuper.addFornecedor(fornecedorDinap);
		session.save(produtoSuper);
		
		produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		session.save(produtoCapricho);
		
		produtoInfoExame = Fixture.produtoInfoExame(tipoProdutoRevista);
		produtoInfoExame.addFornecedor(fornecedorDinap);
		session.save(produtoInfoExame);
		
		produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
		produtoQuatroRodas.addFornecedor(fornecedorDinap);
		session.save(produtoQuatroRodas);
		
		produtoBoaForma = Fixture.produtoBoaForma(tipoProdutoRevista);
		produtoBoaForma.addFornecedor(fornecedorDinap);
		session.save(produtoBoaForma);
		
		produtoBravo = Fixture.produtoBravo(tipoProdutoRevista);
		produtoBravo.addFornecedor(fornecedorDinap);
		session.save(produtoBravo);
		
		produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		produtoCaras.addFornecedor(fornecedorDinap);
		session.save(produtoCaras);
		
		produtoCasaClaudia = Fixture.produtoCasaClaudia(tipoProdutoRevista);
		produtoCasaClaudia.addFornecedor(fornecedorDinap);
		session.save(produtoCasaClaudia);
		
		produtoClaudia = Fixture.produtoClaudia(tipoProdutoRevista);
		produtoClaudia.addFornecedor(fornecedorDinap);
		session.save(produtoClaudia);
		
		produtoContigo = Fixture.produtoContigo(tipoProdutoRevista);
		produtoContigo.addFornecedor(fornecedorDinap);
		session.save(produtoContigo);
		
		produtoManequim = Fixture.produtoManequim(tipoProdutoRevista);
		produtoManequim.addFornecedor(fornecedorDinap);
		session.save(produtoManequim);
		
		produtoNatGeo = Fixture.produtoNationalGeographic(tipoProdutoRevista);
		produtoNatGeo.addFornecedor(fornecedorDinap);
		session.save(produtoNatGeo);
		
		produtoPlacar = Fixture.produtoPlacar(tipoProdutoRevista);
		produtoPlacar.addFornecedor(fornecedorDinap);
		session.save(produtoPlacar);
	}

	private static void criarTiposProduto(Session session) {
		tipoProdutoRevista = Fixture.tipoRevista();
		session.save(tipoProdutoRevista);
	}

	private static void criarDistribuidor(Session session) {
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"33.333.333/0001-33", "333.333.333.333", "distrib_acme@mail.com");
		save(session, juridicaDistrib);

		distribuidor = Fixture.distribuidor(juridicaDistrib, new Date());
		save(session, distribuidor);
	}

	private static void criarCotas(Session session) {
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(session, manoel);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO);
		save(session, cotaManoel);
	}

	private static void criarFornecedores(Session session) {
		fornecedorAcme = Fixture.fornecedorAcme();
		fornecedorDinap = Fixture.fornecedorDinap();
		fornecedorFc = Fixture.fornecedorFC();
		save(session, fornecedorAcme, fornecedorDinap, fornecedorFc);
	}

	private static void criarUsuarios(Session session) {
		usuarioJoao = Fixture.usuarioJoao();
		session.save(usuarioJoao);
	}

	private static void criarTiposNotaFiscal(Session session) {
		tipoNotaFiscalRecebimento = Fixture.tipoNotaFiscalRecebimento();
		session.save(tipoNotaFiscalRecebimento);
	}

	private static void criarCFOP(Session session) {
		cfop5102 = Fixture.cfop5102();
		session.save(cfop5102);
	}

	private static void criarTiposMovimento(Session session) {
		tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		tipoMovimentoSobraEm = Fixture.tipoMovimentoSobraEm();
		tipoMovimentoSobraDe = Fixture.tipoMovimentoSobraDe();
		tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte);
	}

	private static void criarDiasDistribuicaoFornecedores(Session session) {
		DistribuicaoFornecedor dinapSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor dinapQuarta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor dinapSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		save(session, dinapSegunda, dinapQuarta, dinapSexta);

		DistribuicaoFornecedor fcSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor fcSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		save(session, fcSegunda, fcSexta);
	}
	
	private static void gerarCargaDiferencaEstoque(Session session,
												   int quantidadeRegistros,
												   ProdutoEdicao produtoEdicao, 
												   TipoMovimento tipoMovimento, 
												   Usuario usuario,
												   EstoqueProduto estoqueProduto,
												   TipoDiferenca tipoDiferenca) {
		
		for (int i = 1; i <= quantidadeRegistros; i++) {
			
			MovimentoEstoque movimentoEstoqueDiferenca = 
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.APROVADO, "Aprovado.");
			
			session.save(movimentoEstoqueDiferenca);
			
			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca);
			
			session.save(diferenca);
		}
	}
	
	
	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
		}
	}

}
