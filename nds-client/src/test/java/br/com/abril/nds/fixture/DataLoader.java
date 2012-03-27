package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.RateioDiferenca;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.DateUtil;

public class DataLoader {
	
	private static PessoaJuridica juridicaAcme;
	private static PessoaJuridica juridicaDinap;
	private static PessoaJuridica juridicaFc;
	private static PessoaJuridica juridicaValida;
	private static PessoaFisica manoel;
	private static PessoaFisica jose;
	private static PessoaFisica maria;
	
	private static TipoMovimentoEstoque tipoMovimentoFaltaEm;
	private static TipoMovimentoEstoque tipoMovimentoFaltaDe;
	private static TipoMovimentoEstoque tipoMovimentoSobraEm;
	private static TipoMovimentoEstoque tipoMovimentoSobraDe;
	private static TipoMovimentoEstoque tipoMovimentoRecFisico;
	private static TipoMovimentoEstoque tipoMovimentoRecReparte;
	private static TipoMovimentoEstoque tipoMovimentoEnvioEncalhe;
	
	private static  TipoMovimento tipoMovimentoRecebimentoReparte;
	private static  TipoMovimento tipoMovimentoEnvioJornaleiro;
	
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinenceiroReparte;
	
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta;
	
	private static MovimentoFinanceiroCota movimentoFinanceiroCota1;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota2;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota3;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota4;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota5;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota6;

	private static Carteira carteiraRegistrada;
	private static Carteira carteiraSemRegistro;
	
	private static CFOP cfop5102;
	private static TipoNotaFiscal tipoNotaFiscalRecebimento;
	private static Usuario usuarioJoao;
	private static Fornecedor fornecedorAcme;
	private static Fornecedor fornecedorDinap;
	private static Fornecedor fornecedorFc;
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
	private static Lancamento lancamentoSuper1;
	private static Lancamento lancamentoCapricho1;

	private static NotaFiscalEntradaFornecedor notaFiscalFornecedor;
	private static ItemNotaFiscalEntrada itemNotaFiscalFornecedor;
	private static RecebimentoFisico recebimentoFisico;
	private static ItemRecebimentoFisico itemRecebimentoFisico;
	private static EstoqueProduto estoqueProdutoVeja1;
	private static MovimentoEstoque movimentoRecFisicoVeja1;
	private static TipoFornecedor tipoFornecedorPublicacao;
	private static TipoFornecedor tipoFornecedorOutros;
	
	private static Cota cotaJose;
	private static Cota cotaManoel;
	private static Cota cotaMaria;

	
	private static Estudo estudoVeja1;
	private static Estudo estudoVeja1Atual;
	private static Estudo estudoVeja2;
	private static Estudo estudoSuper1;
	private static Estudo estudoCapricho1;
	private static EstudoCota estudoCotaSuper1Manoel;
	private static EstudoCota estudoCotaManoel;
	private static EstudoCota estudoCotaVeja2Joao;
	private static EstudoCota estudoCotaCaprichoZe;
	private static EstudoCota estudoCotaVeja1Joao;
	
	private static Box box300Reparte;
	private static Box box1;
	private static Box box2;
	
	private static Banco bancoHSBC;
	
	private static Divida divida1;
	private static Divida divida2;
	private static Divida divida3;
	private static Divida divida4;
	private static Divida divida5;
	private static Divida divida6;
	
	private static Boleto boleto1;
	private static Boleto boleto2;
	private static Boleto boleto3;
	private static Boleto boleto4;
	private static Boleto boleto5;
	private static Boleto boleto6;
	private static Boleto boleto7;
	private static Boleto boleto8;
	private static Boleto boleto9;
	
	private static FormaCobranca formaBoleto;
	
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
			//carregarDadosParaContagemdDevolucao(session);
			carregarDados(session);
			//carregarDadosParaResumoExpedicao(session);
			//carregarDadosParaResumoExpedicaoBox(session);
			//carregarDadosInadimplencia(session);

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

		criarCarteira(session);
		criarBanco(session);
		criarPessoas(session);
		criarDistribuidor(session);
		criarParametrosSistema(session);
		criarUsuarios(session);
		criarTiposFornecedores(session);
		criarBox(session);
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
		criarEstudosCota(session);
		criarMovimentosEstoqueCota(session);
		criarFeriado(session);		
		criarEnderecoCotaPF(session);
		criarParametroEmail(session);
		criarDivida(session);
		criarBoletos(session);
		criarmovimentosFinanceiroCota(session);
		
		// Inicio dos inserts na tabela MOVIMENTO_ESTOQUE
		
		MovimentoEstoque movimentoEstoqueDiferenca =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca);
		
		MovimentoEstoque movimentoEstoqueDiferenca2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(2),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca2);
		
		MovimentoEstoque movimentoEstoqueDiferenca3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(3),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca3);
		
		MovimentoEstoque movimentoEstoqueDiferenca4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(4),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca4);
		
		// Fim dos inserts na tabela MOVIMENTO_ESTOQUE
		
		// Inicio dos inserts na tabela DIFERENCA
		
		Diferenca diferenca =
			Fixture.diferenca(new BigDecimal(1), usuarioJoao, produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
		session.save(diferenca);
		
		Diferenca diferenca2 =
			Fixture.diferenca(new BigDecimal(2), usuarioJoao, produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
							  StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca2, true);
		session.save(diferenca2);
		
		Diferenca diferenca3 =
			Fixture.diferenca(new BigDecimal(3), usuarioJoao, produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca3, true);
		session.save(diferenca3);
		
		Diferenca diferenca4 =
			Fixture.diferenca(new BigDecimal(4), usuarioJoao, produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
					          StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca4, true);
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

	private static void criarDivida(Session session) {
		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota1), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota2), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado3 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota3), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado4 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota4), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado5 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota5), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado6 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota6), cotaManoel,
						new Date(), new BigDecimal(200));
		
		save(session, consolidado1, consolidado2, consolidado3,
					  consolidado4, consolidado5, consolidado6);
		
		divida1 = Fixture.divida(consolidado1, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida2 = Fixture.divida(consolidado2, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida3 = Fixture.divida(consolidado3, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida4 = Fixture.divida(consolidado4, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida5 = Fixture.divida(consolidado5, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida6 = Fixture.divida(consolidado6, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		save(session, divida1, divida2, divida3, divida4, divida5, divida6);
	}

	private static void criarTiposFornecedores(Session session) {
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session, tipoFornecedorPublicacao);
		tipoFornecedorOutros = Fixture.tipoFornecedorOutros();
		save(session, tipoFornecedorOutros);
	}

	private static void criarMovimentosEstoqueCota(Session session) {
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		MovimentoEstoqueCota mec1 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		MovimentoEstoqueCota mec2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		MovimentoEstoqueCota mec3 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		MovimentoEstoqueCota mec4 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		MovimentoEstoqueCota mec5 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		MovimentoEstoqueCota mec6 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, null);
		
		save(session, mec1, mec2, mec3, mec4, mec5, mec6);
		
		movimentoFinanceiroCota1 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec1), new Date());
		
		movimentoFinanceiroCota2 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec2), new Date());
		
		movimentoFinanceiroCota3 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec3), new Date());
		
		movimentoFinanceiroCota4 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec4), new Date());
		
		movimentoFinanceiroCota5 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec5), new Date());
		
		movimentoFinanceiroCota6 = Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinenceiroReparte, usuarioJoao,
				new BigDecimal(200), Arrays.asList(mec6), new Date());

		save(session, movimentoFinanceiroCota1, movimentoFinanceiroCota2,
					  movimentoFinanceiroCota3, movimentoFinanceiroCota4,
					  movimentoFinanceiroCota5, movimentoFinanceiroCota6);
	}
	
	private static void criarMovimentosEstoqueCotaConferenciaEncalhe(Session session) {
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe
				(DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()), 
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
		

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()), 
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
				DateUtil.adicionarDias(new Date(),produtoEdicaoVeja1.getPeb()),
				produtoEdicaoVeja1,
				tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
		
	}

	private static void criarParametrosSistema(Session session) {
		ParametroSistema parametroSistema = Fixture.parametroSistema(
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
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		session.save(notaFiscalFornecedor);

		itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaFiscalFornecedor);
	}

	private static void criarEstudos(Session session) {
		
		estudoVeja1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja1.getDataLancamentoDistribuidor(), produtoEdicaoVeja1);
		session.save(estudoVeja1);
		
		estudoVeja2 = Fixture
				.estudo(BigDecimal.TEN, lancamentoVeja2.getDataLancamentoDistribuidor(), produtoEdicaoVeja2);
		session.save(estudoVeja2);
		
		estudoSuper1 = Fixture.estudo(
			BigDecimal.TEN, lancamentoSuper1.getDataLancamentoDistribuidor(), produtoEdicaoSuper1);
		
		session.save(estudoSuper1);
		
		estudoCapricho1 = Fixture
				.estudo(BigDecimal.TEN, lancamentoCapricho1.getDataLancamentoDistribuidor(), produtoEdicaoCapricho1);
		session.save(estudoCapricho1);
		
		estudoVeja1Atual = Fixture
				.estudo(BigDecimal.TEN, new Date(), produtoEdicaoVeja1);
		session.save(estudoVeja1Atual);
	}

    private static void criarEstudosCota(Session session) {
		
		estudoCotaVeja1Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja1, cotaJose);
		save(session,estudoCotaVeja1Joao);
		
		estudoCotaVeja2Joao = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoVeja2, cotaJose);
		save(session,estudoCotaVeja2Joao);
		
		estudoCotaCaprichoZe = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoCapricho1, cotaMaria);
		save(session,estudoCotaCaprichoZe);
		
		estudoCotaSuper1Manoel = Fixture.estudoCota(new BigDecimal(10), new BigDecimal(10), estudoSuper1, cotaManoel);
		save(session,estudoCotaSuper1Manoel);
		
		estudoCotaManoel = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.TEN, estudoVeja1Atual, cotaManoel);
		save(session,estudoCotaManoel);
	
	}
	
	private static void criarLancamentosExpedidos(Session session){
		
		Expedicao expedicao = Fixture.expedicao(usuarioJoao,Fixture.criarData(1, 3, 2010));
		session.save(expedicao);
		
		lancamentoVeja1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigDecimal.TEN,  StatusLancamento.EXPEDIDO,
						itemRecebimentoFisico,expedicao);
		session.save(lancamentoVeja1);
		
		lancamentoVeja2 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.EXPEDIDO,

						null,expedicao);
		session.save(lancamentoVeja2);

		lancamentoSuper1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.EXPEDIDO,
								null,expedicao);
		session.save(lancamentoSuper1);
		
		lancamentoCapricho1 = Fixture
				.lancamentoExpedidos(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.EXPEDIDO,
								null,expedicao);
		session.save(lancamentoCapricho1);		
		
	}
	
	private static void criarLancamentos(Session session) {
		
		
		lancamentoVeja1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja1.getPeb()), new Date(),
						new Date(), BigDecimal.TEN,  StatusLancamento.CONFIRMADO,
						itemRecebimentoFisico);
		session.save(lancamentoVeja1);
		
		lancamentoVeja2 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoVeja2,
						DateUtil.adicionarDias(new Date(), 0),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoVeja2.getPeb()), new Date(),

						new Date(), BigDecimal.TEN, StatusLancamento.BALANCEADO,

						null);
		session.save(lancamentoVeja2);

		lancamentoSuper1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoSuper1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoSuper1.getPeb()), new Date(),
								new Date(), new BigDecimal(100), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoSuper1);
		
		lancamentoCapricho1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCapricho1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCapricho1.getPeb()), new Date(),
								new Date(), new BigDecimal(1000), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoCapricho1);
		
		
		
		Lancamento lancamentoInfoExame1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoInfoExame1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoInfoExame1.getPeb()), new Date(),
								new Date(), new BigDecimal(500), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoInfoExame1);
		
		Lancamento lancamentoQuatroRodas1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoQuatroRodas1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoQuatroRodas1.getPeb()), new Date(),
								new Date(), new BigDecimal(1500), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoQuatroRodas1);
		
		Lancamento lancamentoBoaForma1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBoaForma1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBoaForma1.getPeb()), new Date(),
								new Date(), new BigDecimal(190), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoBoaForma1);
		
		Lancamento lancamentoBravo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoBravo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoBravo1.getPeb()), new Date(),
								new Date(), new BigDecimal(250), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoBravo1);
		
		Lancamento lancamentoBoaCaras1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCaras1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCaras1.getPeb()), new Date(),
								new Date(), new BigDecimal(290), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoBoaCaras1);
		
		Lancamento lancamentoCasaClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoCasaClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoCasaClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(350), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoCasaClaudia1);
		
		Lancamento lancamentoClaudia1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoClaudia1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoClaudia1.getPeb()), new Date(),
								new Date(), new BigDecimal(400), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoClaudia1);
		
		Lancamento lancamentoContigo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoContigo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoContigo1.getPeb()), new Date(),
								new Date(), new BigDecimal(185), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoContigo1);
		
		Lancamento lancamentoManequim1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoManequim1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoManequim1.getPeb()), new Date(),
								new Date(), new BigDecimal(225), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoManequim1);
		
		Lancamento lancamentoNatGeo1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoNatGeo1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoNatGeo1.getPeb()), new Date(),
								new Date(), new BigDecimal(75), StatusLancamento.CONFIRMADO,
								null);
		session.save(lancamentoNatGeo1);
		
		Lancamento lancamentoPlacar1 = Fixture
				.lancamento(
						TipoLancamento.LANCAMENTO,
						produtoEdicaoPlacar1,
						DateUtil.adicionarDias(new Date(), 1),
						DateUtil.adicionarDias(new Date(),
								produtoEdicaoPlacar1.getPeb()), new Date(),
								new Date(), new BigDecimal(195), StatusLancamento.CONFIRMADO,
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
				"56.003.315/0001-47", "333.333.333.333", "distrib_acme@mail.com");
		save(session, juridicaDistrib);

		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE);
		save(session, formaBoleto);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1);
		
		distribuidor = Fixture.distribuidor(juridicaDistrib, new Date(), politicaCobranca);
		distribuidor.getFormasCobranca().add(formaBoleto);
		save(session, distribuidor);
	}

	private static void criarCotas(Session session) {
		
		
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(session, cotaManoel);
		
		cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		save(session, cotaJose);
		
		cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO,box2);
		save(session, cotaMaria);
		
		ParametroCobrancaCota parametroCobrancaConta = 
			Fixture.parametroCobrancaCota(1, BigDecimal.TEN, cotaManoel, 1, 
										  formaBoleto, true, BigDecimal.TEN);
		save(session, parametroCobrancaConta);
	}

	private static void criarFornecedores(Session session) {
		fornecedorAcme = Fixture.fornecedorAcme(tipoFornecedorOutros);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		fornecedorFc = Fixture.fornecedorFC(tipoFornecedorPublicacao);
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

		tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		
		tipoMovimentoFinanceiroCredito = Fixture.tipoMovimentoFinanceiroCredito();
		tipoMovimentoFinanceiroDebito = Fixture.tipoMovimentoFinanceiroDebito();
		tipoMovimentoFinenceiroReparte = Fixture.tipoMovimentoFinanceiroReparte();
		tipoMovimentoFinanceiroJuros = Fixture.tipoMovimentoFinanceiroJuros();
		tipoMovimentoFinanceiroMulta = Fixture.tipoMovimentoFinanceiroMulta();
		
		tipoMovimentoRecebimentoReparte = Fixture.tipoMovimentoRecebimentoReparte();	

		tipoMovimentoEnvioJornaleiro = Fixture.tipoMovimentoEnvioJornaleiro();
		save(session,tipoMovimentoRecebimentoReparte,tipoMovimentoEnvioJornaleiro);
		
		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte,
				tipoMovimentoFinanceiroCredito, tipoMovimentoFinanceiroDebito,
				tipoMovimentoEnvioEncalhe, tipoMovimentoFinenceiroReparte,
				tipoMovimentoFinanceiroJuros, tipoMovimentoFinanceiroMulta);

	}

	private static void criarDiasDistribuicaoFornecedores(Session session) {
		DistribuicaoFornecedor dinapSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEGUNDA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor dinapQuarta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUARTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor dinapQuinta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUINTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		DistribuicaoFornecedor dinapSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEXTA_FEIRA,
				OperacaoDistribuidor.DISTRIBUICAO);
		save(session, dinapSegunda, dinapQuarta, dinapQuinta, dinapSexta);

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
												   TipoMovimentoEstoque tipoMovimento, 
												   Usuario usuario,
												   EstoqueProduto estoqueProduto,
												   TipoDiferenca tipoDiferenca) {
		
		for (int i = 1; i <= quantidadeRegistros; i++) {
			
			MovimentoEstoque movimentoEstoqueDiferenca = 
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.PENDENTE, "motivo");
			
			session.save(movimentoEstoqueDiferenca);
			
			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.PENDENTE, null, movimentoEstoqueDiferenca, true);
			
			session.save(diferenca);
		}
	}
	
	
	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
			session.flush();
		}
	}
	
	
	/**
	 * Gera massa de dados para o teste de Resumo de Expedicao agrupadas por produto
	 * @param session
	 */
	private static void carregarDadosParaResumoExpedicao(Session session){

		TipoProduto tipoRevista = Fixture.tipoRevista();
		session.save(tipoRevista);
		
		CFOP cfop = Fixture.cfop5102();
		session.save(cfop);
		
		Usuario usuario = Fixture.usuarioJoao();
		session.save(usuario);
		
		TipoMovimentoEstoque tipoMovimentoSobraDe  = Fixture.tipoMovimentoSobraDe();
		session.save(tipoMovimentoSobraDe);
		
		TipoMovimentoEstoque tipoMovimentoFaltDe  = Fixture.tipoMovimentoFaltaDe();
		session.save(tipoMovimentoFaltDe);
		
		TipoMovimentoEstoque tipoMovimentoFaltEM  = Fixture.tipoMovimentoFaltaEm();
		session.save(tipoMovimentoFaltEM);
		
		int indDiferenca = 10;
		
		for(Integer i=0;i<10; i++) {
			
			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
			session.save(juridica);
			
			TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
			session.save(tipoFornecedorPublicacao);
			
			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao);
			session.save(fornecedor);
			
			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista);
			produto.addFornecedor(fornecedor);
			session.save(produto); 
			
			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), produto);	
			session.save(produtoEdicao);
			
			
			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			session.save(tipoNotaFiscal);

			
			List<ItemRecebimentoFisico> listaRecebimentos = new ArrayList<ItemRecebimentoFisico>() ;
			
			EstoqueProduto estoque  =  Fixture.estoqueProduto(produtoEdicao, BigDecimal.ZERO);
			session.save(estoque);
			
			for(int x= 1; x< 3 ;x++){
				
				NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
						.notaFiscalEntradaFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
								usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
				session.save(notaFiscalFornecedor);
				
				ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
						produtoEdicao, usuario, notaFiscalFornecedor, 
						Fixture.criarData(23, Calendar.FEBRUARY, 2012), new Date(),TipoLancamento.LANCAMENTO,
						new BigDecimal(i));					
				session.save(itemNotaFiscal);
				
				
				RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
					notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
				session.save(recebimentoFisico);
				
				ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
						itemNotaFiscal, recebimentoFisico, new BigDecimal(i+x));
				session.save(itemFisico);
				
				
				MovimentoEstoque movimentoEstoque  = Fixture.movimentoEstoque(itemFisico, produtoEdicao,tipoMovimentoFaltDe , usuario, estoque, StatusAprovacao.APROVADO, "Teste");
				
				session.save(movimentoEstoque);
				
				if(indDiferenca > 5){
					
					
					Diferenca diferenca = Fixture.diferenca(new BigDecimal(10), usuario, produtoEdicao, TipoDiferenca.SOBRA_DE, StatusConfirmacao.CONFIRMADO, itemFisico, movimentoEstoque, true);
					session.save(diferenca);
					
					itemFisico.setDiferenca(diferenca);
					session.update(itemFisico);
					
					indDiferenca --;
				}
			
				
				listaRecebimentos.add(itemFisico);
			}
			
			Expedicao expedicao = Fixture.expedicao(usuario,Fixture.criarData(1, 3, 2010));
			session.save(expedicao);
			
			Lancamento lancamento = Fixture.lancamentos(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					new BigDecimal(100), 
					StatusLancamento.EXPEDIDO, 
					listaRecebimentos);
			lancamento.setReparte(new BigDecimal(10));
			lancamento.setExpedicao(expedicao);
			session.save(lancamento);
		
			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(new BigDecimal(i));
			session.save(estudo);
		}
	}
	


	/**
	 * Gera massa de dados para teste de consulta de lan�amentos agrupadas por Box
	 * @param session
	 */
	public static void carregarDadosParaResumoExpedicaoBox(Session session){
		
		criarDistribuidor(session);
		criarParametrosSistema(session);
		criarUsuarios(session);
		criarTiposFornecedores(session);
		criarFornecedores(session);
		criarDiasDistribuicaoFornecedores(session);
		criarBox(session);
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
		criarLancamentosExpedidos(session);
		criarEstudos(session);
		criarMovimentosEstoqueCota(session);
		criarEstudosCota(session);
		
		MovimentoEstoque movimentoEstoqueDiferenca =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca);
		
		MovimentoEstoque movimentoEstoqueDiferenca2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(2),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca2);
		
		MovimentoEstoque movimentoEstoqueDiferenca3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(3),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca3);
		
		MovimentoEstoque movimentoEstoqueDiferenca4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoRecFisico, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(4),
									 StatusAprovacao.APROVADO, "motivo");
		session.save(movimentoEstoqueDiferenca4);
		
		
		Diferenca diferenca =
			Fixture.diferenca(new BigDecimal(1), usuarioJoao, produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
		session.save(diferenca);
		
		Diferenca diferenca2 =
			Fixture.diferenca(new BigDecimal(2), usuarioJoao, produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
							  StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca2, true);
		session.save(diferenca2);
		
		Diferenca diferenca3 =
			Fixture.diferenca(new BigDecimal(3), usuarioJoao, produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
							  StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca3, true);
		session.save(diferenca3);
		
		Diferenca diferenca4 =
			Fixture.diferenca(new BigDecimal(4), usuarioJoao, produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
					          StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico, movimentoEstoqueDiferenca4, true);
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
		
		RateioDiferenca rateioDiferencaCotaManoel = Fixture.rateioDiferenca(new BigDecimal(10), cotaManoel, diferenca3, estudoCotaSuper1Manoel);
		session.save(rateioDiferencaCotaManoel);
	
		RateioDiferenca rateioDiferencaJose = Fixture.rateioDiferenca(new BigDecimal(10), cotaJose, diferenca,estudoCotaVeja2Joao);
		session.save(rateioDiferencaJose);
		
	}

	
	private static void criarCarteira(Session session){
		carteiraRegistrada = Fixture.carteira(30, TipoRegistroCobranca.REGISTRADA);
	    
		carteiraSemRegistro = Fixture.carteira(1, TipoRegistroCobranca.SEM_REGISTRO);
	    
		save(session,carteiraRegistrada,carteiraSemRegistro);
	}
	
	
	private static void criarBox(Session session){
		
		box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		session.save(box1);
		
		box2 = Fixture.criarBox("Box-2", "BX-002", TipoBox.REPARTE);
		session.save(box2);
		
		box300Reparte = Fixture.boxReparte300();
		session.save(box300Reparte);
	}
	
	
	private static void criarPessoas(Session session){
		juridicaAcme = Fixture.pessoaJuridica("Acme",
				"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
		juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11.111.111/0001-11", "111.111.111.111", "dinap@mail.com");
		juridicaFc = Fixture.pessoaJuridica("FC",
				"22.222.222/0001-22", "222.222.222.222", "fc@mail.com");
		juridicaValida = Fixture.pessoaJuridica("Juridica Valida",
				"93.081.738/0001-01", "333.333.333.333", "distrib_acme@mail.com");
		
		manoel = Fixture.pessoaFisica("319.435.088-95",
				"developertestermail@gmail.com", "Manoel da Silva");

		jose = Fixture.pessoaFisica("123.456.789-01",
				"jose@mail.com", "Jose da Silva");
		
		maria = Fixture.pessoaFisica("123.456.789-02",
				"maria@mail.com", "Maria da Silva");
		
		save(session, juridicaAcme, juridicaDinap, juridicaFc, juridicaValida,manoel,jose,maria);
	}
	

	
	//FINANCEIRO - CONSULTA BOLETOS
	private static void criarBoletos(Session session) {
		
		Boleto boleto1 = Fixture.boleto("1309309032012440",
		                new Date(), 
		                DateUtil.parseDataPTBR("09/03/2012"), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                 bancoHSBC,
		                 divida1);
		
		Boleto boleto2 = Fixture.boleto("1309309032012442",
		                new Date(), 
		                new Date(), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                bancoHSBC,
		                divida2);
		
		Boleto boleto3 = Fixture.boleto("1309309032012443",
		                new Date(), 
		                new Date(), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                bancoHSBC,
		                divida3);
		
		Boleto boleto4 = Fixture.boleto("1309309032012444",
		                new Date(), 
		                new Date(), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                bancoHSBC,
		                divida4);
		
		Boleto boleto5 = Fixture.boleto("1309309032012445",
		                new Date(), 
		                new Date(), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                bancoHSBC,
		                divida5);
		
		Boleto boleto6 = Fixture.boleto("1309309032012446",
		                new Date(), 
		                new Date(), 
		                null, 
		                BigDecimal.ZERO, 
		                new BigDecimal(200), 
		                "TIPO_BAIXA",
		                "ACAO", 
		                StatusCobranca.NAO_PAGO,
		                cotaManoel,
		                bancoHSBC,
		                divida6);
		
		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6);	 
	}
	
	private static void criarFeriado(Session session) {
		Feriado feriadoIndependencia =

				Fixture.feriado(DateUtil.parseDataPTBR("07/09/2012"), "Independência do Brasil");
		save(session, feriadoIndependencia);
		
		Feriado feriadoProclamacao =
				Fixture.feriado(DateUtil.parseDataPTBR("15/11/2012"), "Proclamação da República");

		save(session, feriadoProclamacao);
	}

	private static void criarEnderecoCotaPF(Session session) {
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COMERCIAL, "13730-000", "Rua Marechal Deodoro", 50, "Centro", "Mococa", "SP");

		EnderecoCota enderecoCota = new EnderecoCota();
		enderecoCota.setCota(cotaManoel);
		enderecoCota.setEndereco(endereco);
		enderecoCota.setPrincipal(true);
		enderecoCota.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco endereco2 = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		EnderecoCota enderecoCota2 = new EnderecoCota();
		enderecoCota2.setCota(cotaManoel);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(false);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);		

		save(session, endereco, enderecoCota, endereco2, enderecoCota2);
	}
	
	private static void dadosExpedicao(Session session) {
		
		Box box300Reparte = Fixture.boxReparte300();
		save(session,box300Reparte);

		
		TipoProduto tipoRevista = Fixture.tipoRevista();
		save(session,tipoRevista);
		
		CFOP cfop = Fixture.cfop5102();
		save(session,cfop);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(session,usuario);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session,tipoFornecedorPublicacao);
		
		for(Integer i=1000;i<1050; i++) {
			
			PessoaJuridica juridica = Fixture.pessoaJuridica("PessoaJ"+i,
					"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
			save(session,juridica);
			
			Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true, tipoFornecedorPublicacao);
			save(session,fornecedor);
			
			Produto produto = Fixture.produto("00"+i, "descricao"+i, "nome"+i, PeriodicidadeProduto.ANUAL, tipoRevista);
			produto.addFornecedor(fornecedor);
			save(session,produto); 
			
			ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(i.longValue(), 50, 40, 
					new BigDecimal(30), new BigDecimal(20), new BigDecimal(10), produto);	
			save(session,produtoEdicao);
			
			
			TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
			save(session,tipoNotaFiscal);

			NotaFiscalEntradaFornecedor notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop, juridica, fornecedor, tipoNotaFiscal,
							usuario, new BigDecimal(1),new BigDecimal(1),new BigDecimal(1));
			save(session,notaFiscalFornecedor);
			
			ItemNotaFiscalEntrada itemNotaFiscal= Fixture.itemNotaFiscal(
					produtoEdicao, usuario, notaFiscalFornecedor, 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					DateUtil.adicionarDias(Fixture.criarData(23, Calendar.FEBRUARY, 2012), 7),
					TipoLancamento.LANCAMENTO,
					new BigDecimal(i));					
			save(session,itemNotaFiscal);
			
			RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
				notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
			save(session,recebimentoFisico);
			
			
			ItemRecebimentoFisico itemFisico = Fixture.itemRecebimentoFisico(
					itemNotaFiscal, recebimentoFisico, new BigDecimal(i));
			save(session,itemFisico);
			
			Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao,
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					Fixture.criarData(23, Calendar.FEBRUARY, 2012), 
					new BigDecimal(100), 
					StatusLancamento.CONFIRMADO, 
					itemFisico);
			lancamento.setReparte(new BigDecimal(10));
			save(session,lancamento);
		
			Estudo estudo = new Estudo();
			estudo.setDataLancamento(Fixture.criarData(23, Calendar.FEBRUARY, 2012));
			estudo.setProdutoEdicao(produtoEdicao);
			estudo.setQtdeReparte(new BigDecimal(10));
			save(session,estudo);
			
			Pessoa pessoa = Fixture.pessoaJuridica("razaoS"+i, "CNPK" + i, "ie"+i, "email"+i);
			Cota cota = Fixture.cota(i, pessoa, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(3), new BigDecimal(3), 
					estudo, cota);
			save(session,pessoa,cota,estudoCota);		
			
			Pessoa pessoa2 = Fixture.pessoaJuridica("razaoS2"+i, "CNPK" + i, "ie"+i, "email"+i);
			Cota cota2 = Fixture.cota(i, pessoa2, SituacaoCadastro.ATIVO, box300Reparte);
			EstudoCota estudoCota2 = Fixture.estudoCota(new BigDecimal(7), new BigDecimal(7), 
					estudo, cota2);
			save(session, pessoa2,cota2,estudoCota2);		
			
			
			TipoMovimento tipoMovimento = Fixture.tipoMovimentoRecebimentoReparte();	

			TipoMovimento tipoMovimento2 = Fixture.tipoMovimentoEnvioJornaleiro();
			save(session,tipoMovimento,tipoMovimento2);
		}
	}
	
	private static void criarParametroEmail(Session session){
		save(session, Fixture.criarParametrosEmail());
	}
	
	private static void criarBanco(Session session) {
		bancoHSBC = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
							  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399");
		
		save(session, bancoHSBC);
	}
	
	private static void carregarDadosInadimplencia(Session session) {
		carregarDados(session);
				
		boleto1.setDataVencimento( Fixture.criarData(10, 3, 2012));
		boleto2.setDataVencimento( Fixture.criarData(11, 2, 2012));
		boleto3.setDataVencimento( Fixture.criarData(12, 3, 2012));
		boleto4.setDataVencimento( Fixture.criarData(13, 3, 2012));
		boleto5.setDataVencimento( Fixture.criarData(14, 2, 2012));
		boleto6.setDataVencimento( Fixture.criarData(15, 3, 2012));
		boleto7.setDataVencimento( Fixture.criarData(16, 3, 2012));
		boleto8.setDataVencimento( Fixture.criarData(8, 1, 2012));
		boleto8.setCota(cotaMaria);
		boleto8.setStatusCobranca(StatusCobranca.NAO_PAGO);
		boleto9.setDataVencimento( Fixture.criarData(18, 3, 2012));		
		
		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6, boleto7, boleto8, boleto9);
		
		HistoricoAcumuloDivida histInadimplencia1 = 
				Fixture.criarHistoricoAcumuloDivida( boleto1.getDivida(), Fixture.criarData(10, 3, 2012), 
						usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia1);
	    HistoricoAcumuloDivida histInadimplencia2 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto2.getDivida(), Fixture.criarData(10, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia2);
	    HistoricoAcumuloDivida histInadimplencia3 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto3.getDivida(), Fixture.criarData(11, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia3);
	    HistoricoAcumuloDivida histInadimplencia4 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto4.getDivida(), Fixture.criarData(12, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia4);
	    HistoricoAcumuloDivida histInadimplencia5 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto5.getDivida(), Fixture.criarData(13, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia5);
	    HistoricoAcumuloDivida histInadimplencia6 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto6.getDivida(), Fixture.criarData(14, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia6);
	    HistoricoAcumuloDivida histInadimplencia7 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto7.getDivida(), Fixture.criarData(15, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia7);
	    HistoricoAcumuloDivida histInadimplencia8 = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto8.getDivida(), Fixture.criarData(16, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia8);
	    HistoricoAcumuloDivida histInadimplencia = 
	    		Fixture.criarHistoricoAcumuloDivida( boleto9.getDivida(), Fixture.criarData(17, 3, 2012), 
	    				usuarioJoao, StatusInadimplencia.ATIVA);
	    save(session, histInadimplencia);
	    
	    EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaJose, new BigDecimal(10), BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		EstoqueProdutoCota estoqueProdutoCota2 = Fixture.estoqueProdutoCota(
				produtoEdicaoBravo1, cotaJose, new BigDecimal(10.77), BigDecimal.ZERO);
		save(session, estoqueProdutoCota2);
		
		EstoqueProdutoCota estoqueProdutoCota3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota3);
		
		EstoqueProdutoCota estoqueProdutoCota4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaMaria, new BigDecimal(186), BigDecimal.ZERO);
		save(session, estoqueProdutoCota4);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(100.56), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
		
		MovimentoEstoqueCota mec2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCota,
				new BigDecimal(28), cotaMaria, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec2);
	    
	}
	
	private static void carregarDadosParaContagemdDevolucao(Session session) {

		Lancamento lancamentoVeja = null;
		Fornecedor fornecedorFC = null;
		Fornecedor fornecedorDinap = null;
		TipoProduto tipoCromo = null;
		TipoFornecedor tipoFornecedorPublicacao = null;

		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		fornecedorFC = Fixture.fornecedorFC(tipoFornecedorPublicacao);
		fornecedorDinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(session, fornecedorFC, fornecedorDinap);

		TipoProduto tipoRevista = Fixture.tipoRevista();
		tipoCromo = Fixture.tipoCromo();
		save(session, tipoRevista, tipoCromo);

		Produto veja = Fixture.produtoVeja(tipoRevista);
		veja.addFornecedor(fornecedorDinap);

		Produto quatroRodas = Fixture.produtoQuatroRodas(tipoRevista);
		quatroRodas.addFornecedor(fornecedorDinap);

		Produto infoExame = Fixture.produtoInfoExame(tipoRevista);
		infoExame.addFornecedor(fornecedorDinap);

		Produto capricho = Fixture.produtoCapricho(tipoRevista);
		capricho.addFornecedor(fornecedorDinap);
		save(session, veja, quatroRodas, infoExame, capricho);

		Produto cromoReiLeao = Fixture.produtoCromoReiLeao(tipoCromo);
		cromoReiLeao.addFornecedor(fornecedorDinap);
		save(session, cromoReiLeao);
		
		Produto boaForma = Fixture.produtoBoaForma(tipoRevista);
		boaForma.addFornecedor(fornecedorDinap);
		save(session, boaForma);
		
		Produto bravo = Fixture.produtoBravo(tipoRevista);
		bravo.addFornecedor(fornecedorFC);
		save(session, bravo);
		
		Produto caras = Fixture.produtoCaras(tipoRevista);
		caras.addFornecedor(fornecedorFC);
		save(session, caras);
		
		Produto casaClaudia = Fixture.produtoCasaClaudia(tipoRevista);
		casaClaudia.addFornecedor(fornecedorDinap);
		save(session, casaClaudia);
		
		Produto contigo = Fixture.produtoContigo(tipoRevista);
		contigo.addFornecedor(fornecedorDinap);
		save(session, contigo);
		
		Produto manequim = Fixture.produtoManequim(tipoRevista);
		manequim.addFornecedor(fornecedorDinap);
		save(session, manequim);
		
		Produto placar = Fixture.produtoPlacar(tipoRevista);
		placar.addFornecedor(fornecedorDinap);
		save(session, placar);
		
		Produto nationalGeographic = Fixture.produtoNationalGeographic(tipoRevista);
		placar.addFornecedor(fornecedorDinap);
		save(session, nationalGeographic);

		ProdutoEdicao veja1 = Fixture.produtoEdicao(1L, 10, 7,
		new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), veja);

		ProdutoEdicao quatroRoda2 = Fixture.produtoEdicao(2L, 15, 30,
		new BigDecimal(0.1), BigDecimal.TEN, BigDecimal.TEN,
		quatroRodas);

		ProdutoEdicao infoExame3 = Fixture.produtoEdicao(3L, 5, 30,
		new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(12), infoExame);

		ProdutoEdicao capricho1 = Fixture.produtoEdicao(1L, 10, 15,
		new BigDecimal(0.12), BigDecimal.TEN, BigDecimal.TEN, capricho);

		ProdutoEdicao cromoReiLeao1 = Fixture.produtoEdicao(1L, 100, 60,
		new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), cromoReiLeao);
		
		///
		ProdutoEdicao boaForma1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), boaForma);
		
		ProdutoEdicao bravo1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), bravo);
		
		ProdutoEdicao caras1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), caras);
		
		ProdutoEdicao casaClaudia1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), casaClaudia);
		
		ProdutoEdicao contigo1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), contigo);
		
		ProdutoEdicao manequim1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), manequim);
		
		ProdutoEdicao placar1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), placar);
		
		ProdutoEdicao nationalGeographic1 = Fixture.produtoEdicao(1L, 100, 60,
				new BigDecimal(0.01), BigDecimal.ONE, new BigDecimal(1.5), nationalGeographic);
		

		save(session, veja1, quatroRoda2, infoExame3, capricho1, cromoReiLeao1,boaForma1,bravo1,caras1,casaClaudia1,contigo1,manequim1,placar1,nationalGeographic1);

		Usuario usuario = Fixture.usuarioJoao();
		save(session, usuario);

		CFOP cfop = Fixture.cfop5102();
		save(session, cfop);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(session, tipoNotaFiscal);

		NotaFiscalEntradaFornecedor notaFiscal1Veja = Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscal1Veja);

		ItemNotaFiscalEntrada itemNotaFiscal1Veja = Fixture.itemNotaFiscal(veja1, usuario,
		notaFiscal1Veja,
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		TipoLancamento.LANCAMENTO,
		new BigDecimal(50));
		save(session, itemNotaFiscal1Veja);

		Date dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		RecebimentoFisico recebimentoFisico1Veja = Fixture.recebimentoFisico(
		notaFiscal1Veja, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisico1Veja);

		ItemRecebimentoFisico itemRecebimentoFisico1Veja =
		Fixture.itemRecebimentoFisico(itemNotaFiscal1Veja, recebimentoFisico1Veja, new BigDecimal(50));
		save(session, itemRecebimentoFisico1Veja);


		NotaFiscalEntradaFornecedor notaFiscal2Veja = Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscal2Veja);

		ItemNotaFiscalEntrada itemNotaFiscal2Veja = Fixture.itemNotaFiscal(
		veja1,
		usuario,
		notaFiscal2Veja,
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		TipoLancamento.LANCAMENTO,
		new BigDecimal(50));

		save(session, itemNotaFiscal2Veja);

		RecebimentoFisico recebimentoFisico2Veja = Fixture.recebimentoFisico(
		notaFiscal2Veja, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisico2Veja);

		ItemRecebimentoFisico itemRecebimentoFisico2Veja =
		Fixture.itemRecebimentoFisico(itemNotaFiscal2Veja, recebimentoFisico2Veja, new BigDecimal(50));
		save(session, itemRecebimentoFisico2Veja);


		NotaFiscalEntradaFornecedor notaFiscal4Rodas= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscal4Rodas);

		ItemNotaFiscalEntrada itemNotaFiscal4Rodas =

		Fixture.itemNotaFiscal(
		quatroRoda2,
		usuario,
		notaFiscal4Rodas,
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		Fixture.criarData(22, Calendar.FEBRUARY,2012),
		TipoLancamento.LANCAMENTO,
		new BigDecimal(25));

		save(session, itemNotaFiscal4Rodas);

		RecebimentoFisico recebimentoFisico4Rodas = Fixture.recebimentoFisico(
		notaFiscal4Rodas, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisico4Rodas);

		ItemRecebimentoFisico itemRecebimentoFisico4Rodas =
		Fixture.itemRecebimentoFisico(itemNotaFiscal4Rodas, recebimentoFisico4Rodas, new BigDecimal(25));
		save(session, itemRecebimentoFisico4Rodas);
		
////////////////////////
		NotaFiscalEntradaFornecedor notaFiscalInfoExame= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalInfoExame);

		ItemNotaFiscalEntrada itemNotaFiscalInfoExame =

								Fixture.itemNotaFiscal(
								infoExame3,
								usuario,
								notaFiscalInfoExame,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalInfoExame);

		RecebimentoFisico recebimentoFisicoInfoExame = Fixture.recebimentoFisico(
		notaFiscalInfoExame, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoInfoExame);

		ItemRecebimentoFisico itemRecebimentoFisicoInfoExame =
		Fixture.itemRecebimentoFisico(itemNotaFiscalInfoExame, recebimentoFisicoInfoExame, new BigDecimal(25));
		save(session, itemRecebimentoFisicoInfoExame);

		///////////////////////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalCapricho= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
				usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalCapricho);

		ItemNotaFiscalEntrada itemNotaFiscalCapricho =

								Fixture.itemNotaFiscal(
								capricho1,
								usuario,
								notaFiscalCapricho,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalCapricho);

		RecebimentoFisico recebimentoFisicoCapricho = Fixture.recebimentoFisico(
		notaFiscalCapricho, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoCapricho);

		ItemRecebimentoFisico itemRecebimentoFisicoCapricho =
		Fixture.itemRecebimentoFisico(itemNotaFiscalCapricho, recebimentoFisicoCapricho, new BigDecimal(25));
		save(session, itemRecebimentoFisicoCapricho);
				
		/////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalReiLeao= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
				usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalReiLeao);

		ItemNotaFiscalEntrada itemNotaFiscalReiLeao=

								Fixture.itemNotaFiscal(
								cromoReiLeao1,
								usuario,
								notaFiscalReiLeao,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalReiLeao);

		RecebimentoFisico recebimentoFisicoReiLeao= Fixture.recebimentoFisico(
		notaFiscalReiLeao, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoReiLeao);

		ItemRecebimentoFisico itemRecebimentoFisicoReiLeao =
		Fixture.itemRecebimentoFisico(itemNotaFiscalReiLeao, recebimentoFisicoReiLeao, new BigDecimal(25));
		save(session, itemRecebimentoFisicoReiLeao);
		
		
		///////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalBoaForma= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
				usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalBoaForma);

		ItemNotaFiscalEntrada itemNotaFiscalBoaForma=

								Fixture.itemNotaFiscal(
								boaForma1,
								usuario,
								notaFiscalBoaForma,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalBoaForma);

		RecebimentoFisico recebimentoFisicoBoaForma= Fixture.recebimentoFisico(
		notaFiscalBoaForma, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoBoaForma);

		ItemRecebimentoFisico itemRecebimentoFisicoBoaForma =
		Fixture.itemRecebimentoFisico(itemNotaFiscalBoaForma, recebimentoFisicoBoaForma, new BigDecimal(25));
		save(session, itemRecebimentoFisicoBoaForma);
		
		
		///////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalBravo= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
				usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalBravo);

		ItemNotaFiscalEntrada itemNotaFiscalBravo=

								Fixture.itemNotaFiscal(
								bravo1,
								usuario,
								notaFiscalBoaForma,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalBravo);

		RecebimentoFisico recebimentoFisicoBravo= Fixture.recebimentoFisico(
		notaFiscalBravo, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoBravo);

		ItemRecebimentoFisico itemRecebimentoFisicoBravo =
		Fixture.itemRecebimentoFisico(itemNotaFiscalBravo, recebimentoFisicoBravo, new BigDecimal(25));
		save(session, itemRecebimentoFisicoBravo);
		

		//////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalCaras= Fixture
				.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
				usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalCaras);

		ItemNotaFiscalEntrada itemNotaFiscalCaras=

								Fixture.itemNotaFiscal(
								caras1,
								usuario,
								notaFiscalCaras,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));

		save(session, itemNotaFiscalCaras);

		RecebimentoFisico recebimentoFisicoCaras= Fixture.recebimentoFisico(
		notaFiscalCaras, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoCaras);

		ItemRecebimentoFisico itemRecebimentoFisicoCaras=
		Fixture.itemRecebimentoFisico(itemNotaFiscalCaras, recebimentoFisicoCaras, new BigDecimal(25));
		save(session, itemRecebimentoFisicoCaras);

		
		//////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalCasaClaudia= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalCasaClaudia);
		
		ItemNotaFiscalEntrada itemNotaFiscalCasaClaudia=
		
								Fixture.itemNotaFiscal(
								casaClaudia1,
								usuario,
								notaFiscalCasaClaudia,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));
		
		save(session, itemNotaFiscalCasaClaudia);
		
		RecebimentoFisico recebimentoFisicoCasaClaudia= Fixture.recebimentoFisico(
		notaFiscalCasaClaudia, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoCasaClaudia);
		
		ItemRecebimentoFisico itemRecebimentoFisicoCasaClaudia=
		Fixture.itemRecebimentoFisico(itemNotaFiscalCasaClaudia, recebimentoFisicoCasaClaudia, new BigDecimal(25));
		save(session, itemRecebimentoFisicoCasaClaudia);

		//////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalContigo= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalContigo);
		
		ItemNotaFiscalEntrada itemNotaFiscalContigo=
		
								Fixture.itemNotaFiscal(
								contigo1,
								usuario,
								notaFiscalContigo,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));
		
		save(session, itemNotaFiscalContigo);
		
		RecebimentoFisico recebimentoFisicoContigo= Fixture.recebimentoFisico(
		notaFiscalContigo, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoContigo);
		
		ItemRecebimentoFisico itemRecebimentoFisicoContigo=
		Fixture.itemRecebimentoFisico(itemNotaFiscalContigo, recebimentoFisicoContigo, new BigDecimal(25));
		save(session, itemRecebimentoFisicoContigo);

		/////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalManequim= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalManequim);
		
		ItemNotaFiscalEntrada itemNotaFiscalManequim=
		
								Fixture.itemNotaFiscal(
								manequim1,
								usuario,
								notaFiscalManequim,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));
		
		save(session, itemNotaFiscalManequim);
		
		RecebimentoFisico recebimentoFisicoManequim= Fixture.recebimentoFisico(
		notaFiscalManequim, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoManequim);
		
		ItemRecebimentoFisico itemRecebimentoFisicoManequim=
		Fixture.itemRecebimentoFisico(itemNotaFiscalManequim, recebimentoFisicoManequim, new BigDecimal(25));
		save(session, itemRecebimentoFisicoManequim);

		////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalPlacar= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalPlacar);
		
		ItemNotaFiscalEntrada itemNotaFiscalPlacar=
		
								Fixture.itemNotaFiscal(
								placar1,
								usuario,
								notaFiscalPlacar,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));
		
		save(session, itemNotaFiscalPlacar);
		
		RecebimentoFisico recebimentoFisicoPlacar= Fixture.recebimentoFisico(
		notaFiscalPlacar, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoPlacar);
		
		ItemRecebimentoFisico itemRecebimentoFisicoPlacar=
		Fixture.itemRecebimentoFisico(itemNotaFiscalPlacar, recebimentoFisicoPlacar, new BigDecimal(25));
		save(session, itemRecebimentoFisicoPlacar);
		/////////////////////////////////////////////
		
		NotaFiscalEntradaFornecedor notaFiscalNationalGeographic= Fixture
		.notaFiscalEntradaFornecedor(cfop, fornecedorFC.getJuridica(), fornecedorFC, tipoNotaFiscal,
		usuario, BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.TEN);
		save(session, notaFiscalNationalGeographic);
		
		ItemNotaFiscalEntrada itemNotaFiscalNationalGeographic=
		
								Fixture.itemNotaFiscal(
								nationalGeographic1,
								usuario,
								notaFiscalNationalGeographic,
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								Fixture.criarData(22, Calendar.FEBRUARY,2012),
								TipoLancamento.LANCAMENTO,
								new BigDecimal(25));
		
		save(session, itemNotaFiscalNationalGeographic);
		
		RecebimentoFisico recebimentoFisicoNationalGeographic= Fixture.recebimentoFisico(
		notaFiscalNationalGeographic, usuario, dataRecebimento,
		dataRecebimento, StatusConfirmacao.CONFIRMADO);
		save(session, recebimentoFisicoNationalGeographic);
		
		ItemRecebimentoFisico itemRecebimentoFisicoNationalGeographic=
		Fixture.itemRecebimentoFisico(itemNotaFiscalNationalGeographic, recebimentoFisicoNationalGeographic, new BigDecimal(25));
		save(session, itemRecebimentoFisicoNationalGeographic);
		
		///////////////////////////////////////////
		
		lancamentoVeja = Fixture.lancamento(
		TipoLancamento.SUPLEMENTAR,
		veja1,
		Fixture.criarData(22, Calendar.FEBRUARY, 2012),
		Fixture.criarData(28, Calendar.FEBRUARY, 2012),
		new Date(),
		new Date(),
		new BigDecimal(100),
		StatusLancamento.CONFIRMADO, itemRecebimentoFisico1Veja);

		lancamentoVeja.getRecebimentos().add(itemRecebimentoFisico2Veja);


		Estudo estudo = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), veja1);

		save(session, lancamentoVeja, estudo);
		
		Lancamento lancamento4Rodas = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				quatroRoda2,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisico4Rodas);

		Estudo estudox = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), quatroRoda2);

		save(session, lancamento4Rodas, estudox);
		
		
		Lancamento lancamentoInfoExame = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				infoExame3,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoInfoExame);

		Estudo estudoxx = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), infoExame3);

		save(session, lancamentoInfoExame, estudoxx);
		
		Lancamento lancamentoCapricho = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				capricho1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoCapricho);

		Estudo estudoxxx = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), capricho1);

		save(session, lancamentoCapricho, estudoxxx);
		
		Lancamento lancamentoReiLeao = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				cromoReiLeao1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoReiLeao);

		Estudo estudoxxxx = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), cromoReiLeao1);

		save(session, lancamentoReiLeao, estudoxxxx);
		
		////////////
		
		Lancamento lancamentoBoaForma = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				boaForma1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBoaForma);

		Estudo estudoxxxx1 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), boaForma1);

		save(session, lancamentoBoaForma, estudoxxxx1);
		
		Lancamento lancamentoBravo = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				bravo1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoBravo);

		Estudo estudoxxxx2 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), bravo1);

		save(session, lancamentoBravo, estudoxxxx2);
	
		Lancamento lancamentoCaras = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				caras1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoCaras);

		Estudo estudoxxxx3 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), caras1);

		save(session, lancamentoCaras, estudoxxxx3);
		
		Lancamento lancamentoCasaClaudia = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				casaClaudia1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoCasaClaudia);

		Estudo estudoxxxx4 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), casaClaudia1);

		save(session, lancamentoCasaClaudia, estudoxxxx4);
		
		Lancamento lancamentoContigo = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				contigo1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoContigo);

		Estudo estudoxxxx5 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), contigo1);

		save(session, lancamentoContigo, estudoxxxx5);
		
		Lancamento lancamentoManequim = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				manequim1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoManequim);

		Estudo estudoxxxx6 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), manequim1);

		save(session, lancamentoManequim, estudoxxxx6);
		
		Lancamento lancamentoPlacar = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				placar1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoPlacar);

		Estudo estudoxxxx7 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012), placar1);

		save(session, lancamentoPlacar, estudoxxxx7);
		
		Lancamento lancamentoNationalGeographic = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR,
				nationalGeographic1,
				Fixture.criarData(23, Calendar.FEBRUARY, 2012),
				Fixture.criarData(29, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.CONFIRMADO, itemRecebimentoFisicoNationalGeographic);

		Estudo estudoxxxx9 = Fixture.estudo(new BigDecimal(100),
		Fixture.criarData(22, Calendar.FEBRUARY, 2012),nationalGeographic1);

		save(session, lancamentoNationalGeographic, estudoxxxx9);
				///////////////////
	
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
		"manoel@mail.com", "Manoel da Silva");
		save(session, manoel);

		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(session, box1);

		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(session, cotaManoel);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
		veja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(session, usuarioJoao);

		TipoMovimentoEstoque tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		save(session, tipoMovimentoEnvioEncalhe);


		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
		Fixture.criarData(28, Calendar.FEBRUARY, 2012),
		veja1,
		tipoMovimentoEnvioEncalhe,
		usuarioJoao,
		estoqueProdutoCota,
		new BigDecimal(12), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");

		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
		Fixture.criarData(28, Calendar.FEBRUARY, 2012),
		veja1,
		tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
		new BigDecimal(25), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");

		save(session, mec);

		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
		Fixture.criarData(28, Calendar.FEBRUARY, 2012),
		veja1,
		tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
		new BigDecimal(14), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");

		save(session, mec);


		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe(
		Fixture.criarData(28, Calendar.FEBRUARY, 2012),
		veja1,
		tipoMovimentoEnvioEncalhe, usuarioJoao, estoqueProdutoCota,
		new BigDecimal(19), cotaManoel, StatusAprovacao.APROVADO, "Aprovado");
		save(session, mec);
	
		}
	
	private static void criarmovimentosFinanceiroCota(Session session) {

		Pessoa pessoa = Fixture.juridicaAcme();
		save(session, pessoa);

		Box box = Fixture.boxReparte300();
		save(session, box);
		
		Cota cota = Fixture.cota(123, pessoa, SituacaoCadastro.ATIVO, box);
		save(session, cota);

		Usuario usuario = Fixture.usuarioJoao();
		save(session, usuario);
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.add(Calendar.DATE, 10);
		
		MovimentoFinanceiroCota movimentoFinanceiroCotaCredito = 
				Fixture.movimentoFinanceiroCota(
					cota, tipoMovimentoFinanceiroCredito, usuario, new BigDecimal("225"), 
					null, calendar.getTime()
				);
		
		calendar.add(Calendar.DATE, 10);
		
		MovimentoFinanceiroCota movimentoFinanceiroCotaDebito = 
				Fixture.movimentoFinanceiroCota(
					cota, tipoMovimentoFinanceiroDebito, usuario, new BigDecimal("225"), 
					null, calendar.getTime()
				);

		calendar.add(Calendar.DATE, 10);
		
		MovimentoFinanceiroCota movimentoFinanceiroCotaReparte = 
				Fixture.movimentoFinanceiroCota(
					cota, tipoMovimentoFinenceiroReparte, usuario, new BigDecimal("225"), 
					null, calendar.getTime()
				);

		save(session, movimentoFinanceiroCotaCredito, movimentoFinanceiroCotaDebito, movimentoFinanceiroCotaReparte);
	}
	
	
	
}
