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
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Carteira;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoCota;
import br.com.abril.nds.model.cadastro.EnderecoDistribuidor;
import br.com.abril.nds.model.cadastro.Feriado;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.HistoricoSituacaoCota;
import br.com.abril.nds.model.cadastro.Moeda;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao.TipoOperacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneDistribuidor;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TipoRegistroCobranca;
import br.com.abril.nds.model.cadastro.TipoTelefone;
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
import br.com.abril.nds.model.financeiro.CobrancaCheque;
import br.com.abril.nds.model.financeiro.CobrancaDeposito;
import br.com.abril.nds.model.financeiro.CobrancaDinheiro;
import br.com.abril.nds.model.financeiro.CobrancaTransferenciaBancaria;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.HistoricoAcumuloDivida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ControleNumeracaoNotaFiscal;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.ParametroEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
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

	private static PessoaFisica guilherme;
	private static PessoaFisica murilo;
	private static PessoaFisica mariana;
	private static PessoaFisica orlando;
	private static PessoaFisica joao;
	private static PessoaFisica luis;

	private static TipoMovimentoEstoque tipoMovimentoFaltaEm;
	private static TipoMovimentoEstoque tipoMovimentoFaltaDe;
	private static TipoMovimentoEstoque tipoMovimentoSobraEm;
	private static TipoMovimentoEstoque tipoMovimentoSobraDe;
	private static TipoMovimentoEstoque tipoMovimentoRecFisico;
	private static TipoMovimentoEstoque tipoMovimentoRecReparte;
	private static TipoMovimentoEstoque tipoMovimentoEnvioEncalhe;
	
	private static TipoMovimentoEstoque tipoMovimentoVendaEncalhe;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCompraEncalhe;
	
	private static  TipoMovimentoEstoque tipoMovimentoEnvioJornaleiro;
	
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroCredito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroDebito;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte;
	
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroJuros;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroMulta;
	private static TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe;
	
	private static MovimentoEstoqueCota movimentoEstoqueCota1;
	private static MovimentoEstoqueCota movimentoEstoqueCota2;
	private static MovimentoEstoqueCota movimentoEstoqueCota3;
	private static MovimentoEstoqueCota movimentoEstoqueCota4;
	private static MovimentoEstoqueCota movimentoEstoqueCota5;
	private static MovimentoEstoqueCota movimentoEstoqueCota6;
	private static MovimentoEstoqueCota movimentoEstoqueCota7;
	private static MovimentoEstoqueCota movimentoEstoqueCota8;
	private static MovimentoEstoqueCota movimentoEstoqueCota9;
	
	private static MovimentoFinanceiroCota movimentoFinanceiroCota1;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota2;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota3;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota4;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota5;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota6;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota7;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota8;
	private static MovimentoFinanceiroCota movimentoFinanceiroCota9;

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
	private static TipoProduto tipoRefrigerante;
	
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
	private static Produto cocaCola;
	
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
	private static ProdutoEdicao cocaColaLight;
		
	private static Lancamento lancamentoVeja1;
	private static Lancamento lancamentoVeja2;
	private static Lancamento lancamentoSuper1;
	private static Lancamento lancamentoCapricho1;
	private static Lancamento lancamentoCocaCola;

	private static NotaFiscalEntradaFornecedor notaFiscalFornecedor;
	private static ItemNotaFiscalEntrada itemNotaFiscalFornecedor;
	private static RecebimentoFisico recebimentoFisico;
	private static ItemRecebimentoFisico itemRecebimentoFisico;
	
	private static EstoqueProduto estoqueProdutoVeja1;
	private static EstoqueProduto estoqueProdutoVeja2;
	private static EstoqueProduto estoqueProdutoVeja3;
	private static EstoqueProduto estoqueProdutoVeja4;
	private static EstoqueProduto estoqueProdutoSuper1;
	private static EstoqueProduto estoqueProdutoCapricho1;
	private static EstoqueProduto estoqueProdutoInfoExame1;	
	
	private static TipoFornecedor tipoFornecedorPublicacao;
	private static TipoFornecedor tipoFornecedorOutros;
	
	private static ItemNotaFiscalEntrada itemNotaFiscalCoca;
	private static ItemRecebimentoFisico itemCocaRecebimentoFisico;
	
	private static Cota cotaJose;
	private static Cota cotaManoel;
	private static Cota cotaMaria;
	private static Cota cotaLuis;
	private static Cota cotaJoao;
	private static Cota cotaGuilherme;
	private static Cota cotaMurilo;
	private static Cota cotaMariana;
	private static Cota cotaOrlando;
	
	private static ParametroCobrancaCota parametroCobrancaGuilherme;
	private static ParametroCobrancaCota parametroCobrancaMurilo;
	private static ParametroCobrancaCota parametroCobrancaMariana;
	private static ParametroCobrancaCota parametroCobrancaOralando;
		
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
	private static Banco bancoITAU;
	private static Banco bancoDOBRASIL;
	private static Banco bancoBRADESCO;
	
	private static Divida divida1;
	private static Divida divida2;
	private static Divida divida3;
	private static Divida divida4;
	private static Divida divida5;
	private static Divida divida6;
	private static Divida divida7;
	private static Divida divida8;
	private static Divida divida9;
	private static Divida divida10;
	private static Divida divida11;
	private static Divida divida12;
	private static Divida divida13;
	private static Divida divida14;

	
	private static Divida dividaGuilherme1;
	private static Divida dividaGuilherme2;
	private static Divida dividaGuilherme3;
	private static Divida dividaMurilo1;
	private static Divida dividaMurilo2;
	private static Divida dividaMurilo3;
	private static Divida dividaMariana1;
	private static Divida dividaMariana2;
	private static Divida dividaOrlando;
	
	private static EstoqueProdutoCota estoqueProdutoCotaVeja1;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja2;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja3;
	private static EstoqueProdutoCota estoqueProdutoCotaVeja4;
	private static EstoqueProdutoCota estoqueProdutoCotaSuper1;
	private static EstoqueProdutoCota estoqueProdutoCotaCapricho1;
	private static EstoqueProdutoCota estoqueProdutoCotaQuatroRodas1;
	private static EstoqueProdutoCota estoqueProdutoCotaInfoExame1;
	
	private static CobrancaDinheiro cobrancaGuilherme1;
	private static CobrancaDinheiro cobrancaGuilherme2;
	private static CobrancaDinheiro cobrancaGuilherme3;
	private static Boleto cobrancaMurilo1;
	private static Boleto cobrancaMurilo2;
	private static Boleto cobrancaMurilo3;
	private static Boleto cobrancaMariana1;
	private static Boleto cobrancaMariana2;
	private static CobrancaDeposito cobrancaOrlando;
	
	private static ControleBaixaBancaria baixaBancaria;
		

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
	private static FormaCobranca formaDinheiro;
	private static FormaCobranca formaCheque;
	private static FormaCobranca formaDeposito;
	private static FormaCobranca formaTransferenciBancaria;
	
	private static Editor editoraAbril;
	
	
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

	private static void gerarCotasAusentes(Session session) {
		CotaAusente cotaAusente1 = Fixture.cotaAusente(new Date(), true, cotaGuilherme);
		CotaAusente cotaAusente2 = Fixture.cotaAusente(new Date(), true, cotaMurilo);
		CotaAusente cotaAusente3 = Fixture.cotaAusente(new Date(), true, cotaMariana);
		CotaAusente cotaAusente4 = Fixture.cotaAusente(Fixture.criarData(1, 1, 2001), true, cotaOrlando);
		
		save(session, cotaAusente1, cotaAusente2, cotaAusente3, cotaAusente4);
	}

	private static void carregarDados(Session session) {
		criarCarteira(session);
		criarBanco(session);
		criarPessoas(session);
		criarDistribuidor(session);
		criarEnderecoDistribuidor(session);
		criarTelefoneDistribuidor(session);
		criarParametrosSistema(session);
		criarUsuarios(session);
		criarTiposFornecedores(session);
		criarBox(session);
		criarFornecedores(session);
		criarDiasDistribuicaoFornecedores(session);
		criarCotas(session);
		criarEditores(session);
		criarTiposProduto(session);
		criarProdutos(session);
		criarProdutosEdicao(session);
		criarCFOP(session);
		criarTiposMovimento(session);
		
		criarTiposNotaFiscal(session);
		criarNotasFiscais(session);
		criarRecebimentosFisicos(session);
		criarEstoquesProdutos(session);
		criarEstoqueProdutoCota(session);
		criarMovimentosEstoque(session);
		criarLancamentos(session);
		criarEstudos(session);
		criarEstudosCota(session);
		criarMovimentosEstoqueCota(session);
		criarFeriado(session);		
		criarEnderecoCotaPF(session);
		criarParametroEmail(session);
		criarDivida(session);

		criarCobrancas(session);
	
		criarBoletos(session);
		criarCobrancaCheque(session);
		criarCobrancaDepositoBancaria(session);
		criarCobrancaDinheiro(session);
		criarCobrancaTranferenciaBancaria(session);
		criarMovimentosFinanceiroCota(session);
		criarNotasFiscaisEntradaFornecedor(session);
		criarRotaRoteiroCota(session);		
		criarControleBaixaBancaria(session);
		criarParametrosCobrancaCota(session);
		criarNotasFiscaisEntradaFornecedor(session);
		gerarCotasAusentes(session);
		criarDadosContaCorrenteEncalhe(session);
		
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
		
		gerarCargaHistoricoSituacaoCota(session, 100);
		
		gerarCargaDadosConsultaEncalhe(session);
		
	}

	private static void criarDadosContaCorrenteEncalhe(Session session) {
		
		Date dataAtual = new Date();
		List<MovimentoEstoqueCota> listaMovimentoEstoqueCota = new ArrayList<MovimentoEstoqueCota>();
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
				save(session, manoel);
				
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.REPARTE);
		save(session, box1);
		
		Usuario usuario = Fixture.usuarioJoao();
		save(session, usuario);
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		save(session, tipoProduto);
		
		TipoFornecedor tipoFornecedor = Fixture.tipoFornecedor("Tipo A",GrupoFornecedor.PUBLICACAO);
		save(session, tipoFornecedor);
		
	
		produtoBravo.addFornecedor(fornecedorAcme);
		save(session, produtoBravo);
		
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(234L,12 , 1, new BigDecimal(9), new BigDecimal(8), 
				new BigDecimal(10), produtoBravo);		
		save(session, produtoEdicao);
				
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session, tipoFornecedorPublicacao);
				
		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		save(session, tipoNotaFiscal);
					
		NotaFiscalEntradaFornecedor notaFiscal = Fixture.notaFiscalEntradaFornecedor(cfop5102, juridicaDinap, fornecedorAcme, tipoNotaFiscal, usuario, new BigDecimal(145),  new BigDecimal(10),  new BigDecimal(10));
		save(session, notaFiscal);
		
		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(notaFiscal, usuario, new Date(), new Date(), StatusConfirmacao.PENDENTE);
		save(session, recebimentoFisico);
		
		ItemNotaFiscalEntrada itemNotaFiscal= 
				Fixture.itemNotaFiscal(
						produtoEdicao, 
						usuario, 
						notaFiscal, 
						new Date(), 
						new Date(),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(12));
		save(session, itemNotaFiscal);
		
		ItemRecebimentoFisico itemRecebimentoFisico= Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, new BigDecimal(12));
		save(session, itemRecebimentoFisico);
		
		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataAtual, dataAtual, dataAtual, dataAtual, new BigDecimal(30), StatusLancamento.CONFIRMADO, itemRecebimentoFisico);
		save(session, lancamento);	
		
		Estudo estudo = Fixture.estudo(BigDecimal.TEN, dataAtual, produtoEdicao);
		save(session, estudo);
		
		EstudoCota estudoCota = Fixture.estudoCota(new BigDecimal(30), new BigDecimal(30), estudo, cotaManoel);
		save(session, estudoCota);
		
		Expedicao expedicao = Fixture.expedicao(usuario, dataAtual);
		save(session, expedicao);
		
		EstoqueProdutoCota estoqueProdutoCota = new EstoqueProdutoCota();
		estoqueProdutoCota = Fixture.estoqueProdutoCota(produtoEdicao,new BigDecimal(30), cotaManoel, listaMovimentoEstoqueCota);
		save(session, estoqueProdutoCota);
				
		MovimentoEstoqueCota movimento = new MovimentoEstoqueCota();
		movimento = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimentoEnvioEncalhe, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimento);
				
		MovimentoFinanceiroCota movimentoFinanceiroCota= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiroEnvioEncalhe, usuario, 
				new BigDecimal(230), listaMovimentoEstoqueCota, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCota);
		
		
		MovimentoEstoqueCota movimentoVendaEncalhe  = Fixture.movimentoEstoqueCota(produtoEdicao, tipoMovimentoVendaEncalhe, usuario, estoqueProdutoCota, new BigDecimal(23), cotaManoel, StatusAprovacao.APROVADO, "motivo");
		save(session, movimentoVendaEncalhe);
		
		List<MovimentoEstoqueCota> listMovimentoEstoqueCotas = new ArrayList<MovimentoEstoqueCota>();
		
		listMovimentoEstoqueCotas.add(movimentoVendaEncalhe);
		MovimentoFinanceiroCota movimentoFinanceiroCompraEncalhe= Fixture.movimentoFinanceiroCota(cotaManoel, tipoMovimentoFinanceiroCompraEncalhe, usuario, 
				new BigDecimal(230), listMovimentoEstoqueCotas, StatusAprovacao.APROVADO, dataAtual, true);
		save(session, movimentoFinanceiroCompraEncalhe);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = new ArrayList<MovimentoFinanceiroCota>();
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCota);
		listaMovimentoFinanceiroCota.add(movimentoFinanceiroCompraEncalhe);
		
		ConsolidadoFinanceiroCota consolidadoFinanceiroCota = Fixture.consolidadoFinanceiroCota(listaMovimentoFinanceiroCota, cotaManoel, dataAtual, new BigDecimal(230));
		save(session, consolidadoFinanceiroCota);
		
	}
	
	
	
	
	
	

	private static void criarControleBaixaBancaria(Session session) {
		baixaBancaria = Fixture.controleBaixaBancaria(new Date(), StatusControle.CONCLUIDO_SUCESSO, usuarioJoao);
		save(session, baixaBancaria);
		
	}

	private static void criarEstoqueProdutoCota(Session session) {
		
		estoqueProdutoCotaVeja1 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja1);
		
		estoqueProdutoCotaVeja2 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja2, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja2);
		
		estoqueProdutoCotaVeja3 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja3, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja3);
		
		estoqueProdutoCotaVeja4 = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja4, cotaGuilherme, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaVeja4);
		
		estoqueProdutoCotaSuper1 = Fixture.estoqueProdutoCota(
				produtoEdicaoSuper1, cotaMurilo, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaSuper1);
		
		estoqueProdutoCotaCapricho1 = Fixture.estoqueProdutoCota(
				produtoEdicaoCapricho1, cotaMariana, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaCapricho1);
		
		estoqueProdutoCotaQuatroRodas1 = Fixture.estoqueProdutoCota(
				produtoEdicaoQuatroRodas1, cotaOrlando, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaQuatroRodas1);
				
		estoqueProdutoCotaInfoExame1 = Fixture.estoqueProdutoCota(
				produtoEdicaoInfoExame1, cotaOrlando, BigDecimal.TEN, BigDecimal.TEN);
		save(session, estoqueProdutoCotaInfoExame1);
	}

	private static void criarCobrancas(Session session) {
		
		cobrancaGuilherme1 = Fixture.criarCobrancaDinheiro("1234567890123", 
                new Date(),  Fixture.criarData(1, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme1,1);
		
		cobrancaGuilherme2 = Fixture.criarCobrancaDinheiro("1234567890124", 
                new Date(),  Fixture.criarData(2, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme2,1);
		
		cobrancaGuilherme3 = Fixture.criarCobrancaDinheiro("1234567890125", 
                new Date(),  Fixture.criarData(3, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaGuilherme, bancoHSBC, dividaGuilherme3,1);
		
		cobrancaMurilo1 = Fixture.boleto("1234567890126", "123", "1234567890126123",
                new Date(),  Fixture.criarData(4, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo1,1);
		
		cobrancaMurilo2 = Fixture.boleto("1234567890127", "123", "1234567890127123",
                new Date(),  Fixture.criarData(5, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo2,1);
		
		cobrancaMurilo3 = Fixture.boleto("1234567890128", "123", "1234567890128123",
                new Date(),  Fixture.criarData(6, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMurilo, bancoHSBC, dividaMurilo3,1);
		
		cobrancaMariana1 = Fixture.boleto("1234567890129", "123", "1234567890129123",
                new Date(),  Fixture.criarData(7, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMariana, bancoHSBC, dividaMariana1,1);
		
		cobrancaMariana2 = Fixture.boleto("1234567890120", "123", "1234567890120123",
                new Date(),  Fixture.criarData(8, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMariana, bancoHSBC, dividaMariana2,1);
		
		cobrancaOrlando = Fixture.criarCobrancaDeposito("1234567890130", 
                new Date(),  Fixture.criarData(9, 1, 2010),
                new Date(), BigDecimal.ZERO, new BigDecimal(200),
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaOrlando, bancoHSBC, dividaOrlando,1);
		
		save(session, cobrancaGuilherme1, cobrancaGuilherme2, cobrancaGuilherme3,
				cobrancaMurilo1, cobrancaMurilo2, cobrancaMurilo3,
				cobrancaMariana1, cobrancaMariana2, 
				cobrancaOrlando);
	}

	private static void criarParametrosCobrancaCota(Session session) {
		
		FormaCobranca formaBoleto =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE);
		FormaCobranca formaDeposito =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE);
		FormaCobranca formaDinheiro =
				Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
											BigDecimal.ONE, BigDecimal.ONE);
		
		save(session,formaBoleto,formaDeposito,formaDinheiro);
		
		parametroCobrancaGuilherme = Fixture.parametroCobrancaCota(
				1, null, cotaGuilherme, 1, formaDinheiro, 
				false, new BigDecimal(1000));
		
		parametroCobrancaMurilo = Fixture.parametroCobrancaCota(
				null, new BigDecimal(100), cotaMurilo, 1, formaBoleto, 
				false, new BigDecimal(1000));
		
		parametroCobrancaMariana = Fixture.parametroCobrancaCota(
				null, null, cotaMariana, 1, formaBoleto, 
				false, new BigDecimal(1000));
		
		parametroCobrancaOralando = Fixture.parametroCobrancaCota(
				null, null, cotaOrlando, 1, formaDeposito, 
				false, new BigDecimal(1000));
	
		save(session, parametroCobrancaGuilherme, parametroCobrancaMurilo, parametroCobrancaMariana, parametroCobrancaOralando);
	}

	private static void criarRotaRoteiroCota(Session session) {
		
		Rota rota = Fixture.rota("005");
		session.save(rota);
		
		Roteiro roteiro = Fixture.roteiro("Pinheiros");
		session.save(roteiro);
		
		RotaRoteiroOperacao rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaManoel, TipoOperacao.IMPRESSAO_DIVIDA);
		session.save(rotaRoteiroOperacao);
		
		rota = Fixture.rota("004");
		session.save(rota);
		
		roteiro = Fixture.roteiro("Interlagos");
		session.save(roteiro);
		
		rotaRoteiroOperacao = Fixture.rotaRoteiroOperacao(rota, roteiro, cotaJose, TipoOperacao.IMPRESSAO_DIVIDA);
		session.save(rotaRoteiroOperacao);
		
	}

	private static void criarDivida(Session session) {
		ConsolidadoFinanceiroCota consolidado1 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota1), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado2 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota2), cotaJose,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado3 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota3), cotaMaria,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado4 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota4), cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado5 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota5), cotaJose,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado6 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota6), cotaMaria,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado7 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado8 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaJose,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado9 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaMaria,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado10 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaManoel,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado11 = Fixture
				.consolidadoFinanceiroCota(
						null, cotaLuis,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado12 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota7), cotaJoao,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado13 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota8), cotaJose,
						new Date(), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidado14 = Fixture
				.consolidadoFinanceiroCota(
						Arrays.asList(movimentoFinanceiroCota9), cotaJose,
						new Date(), new BigDecimal(200));
		
		
		ConsolidadoFinanceiroCota consolidadoGuilherme1 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(1, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoGuilherme2 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(2, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoGuilherme3 = Fixture
				.consolidadoFinanceiroCota(null, cotaGuilherme,
						Fixture.criarData(3, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoMurilo1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(4, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoMurilo2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(5, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoMurilo3 = Fixture
				.consolidadoFinanceiroCota(null, cotaMurilo,
						Fixture.criarData(6, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoMariana1 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(7, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoMariana2 = Fixture
				.consolidadoFinanceiroCota(null, cotaMariana,
						Fixture.criarData(8, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoOrlando1 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(9, 1, 2010), new BigDecimal(200));
		
		ConsolidadoFinanceiroCota consolidadoOrlando2 = Fixture
				.consolidadoFinanceiroCota(null, cotaOrlando ,
						Fixture.criarData(10, 1, 2010), new BigDecimal(200));
		
		save(session, consolidado1, consolidado2, consolidado3,
					  consolidado4, consolidado5, consolidado6,
					  consolidado7, consolidado8, consolidado9,
					  consolidado10, consolidado11,consolidado12,
					  consolidado13,consolidado14,
					  consolidadoGuilherme1, consolidadoGuilherme2, consolidadoGuilherme3,
					  consolidadoMurilo1, consolidadoMurilo2, consolidadoMurilo3,
					  consolidadoMariana1, consolidadoMariana2,
					  consolidadoOrlando1, consolidadoOrlando2);


		
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
		
		divida7 = Fixture.divida(consolidado7, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida8 = Fixture.divida(consolidado8, cotaManoel, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida9 = Fixture.divida(consolidado9, cotaMaria, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida10 = Fixture.divida(consolidado10, cotaMaria, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida11 = Fixture.divida(consolidado11, cotaLuis, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida12 = Fixture.divida(consolidado12, cotaJoao, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida13 = Fixture.divida(consolidado13, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		divida14 = Fixture.divida(consolidado14, cotaJose, new Date(),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));

		
		dividaGuilherme1 = Fixture.divida(consolidadoGuilherme1, cotaGuilherme, Fixture.criarData(1, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaGuilherme2 = Fixture.divida(consolidadoGuilherme2, cotaGuilherme, Fixture.criarData(2, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaGuilherme3 = Fixture.divida(consolidadoGuilherme3, cotaGuilherme, Fixture.criarData(3, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaMurilo1 = Fixture.divida(consolidadoMurilo1, cotaMurilo, Fixture.criarData(4, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaMurilo2 = Fixture.divida(consolidadoMurilo2, cotaMurilo, Fixture.criarData(5, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaMurilo3 = Fixture.divida(consolidadoMurilo3, cotaMurilo, Fixture.criarData(6, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaMariana1 = Fixture.divida(consolidadoMariana1, cotaMariana, Fixture.criarData(7, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaMariana2 = Fixture.divida(consolidadoMariana2, cotaMariana, Fixture.criarData(8, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		dividaOrlando = Fixture.divida(consolidadoOrlando1, cotaOrlando, Fixture.criarData(9, 1, 2010),
				usuarioJoao, StatusDivida.EM_ABERTO, new BigDecimal(200));
		
		save(session, divida1, divida2, divida3, divida4, divida5, divida6,
				      divida7, divida8, divida9, divida10, divida11,
				      divida12,divida13,divida14,
				      dividaGuilherme1, dividaGuilherme2, dividaGuilherme3,
				      dividaMurilo1, dividaMurilo2, dividaMurilo3,
				      dividaMariana1, dividaMariana2,
				      dividaOrlando);


	}

	private static void criarTiposFornecedores(Session session) {
		tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(session, tipoFornecedorPublicacao);
		tipoFornecedorOutros = Fixture.tipoFornecedorOutros();
		save(session, tipoFornecedorOutros);
	}

	private static void criarMovimentosEstoqueCota(Session session) {
		
		movimentoEstoqueCota1 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota2 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota3 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota4 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja2,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja2,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota5 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota6 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja3,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja3,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota7 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota8 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja4,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja4,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		movimentoEstoqueCota9 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.PENDENTE, null);
		
		MovimentoEstoqueCota movimentoEstoqueCota10 = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaVeja1,
				BigDecimal.TEN, cotaGuilherme, StatusAprovacao.PENDENTE, null);
		
		MovimentoEstoqueCota movimentoEstoqueCota11 = Fixture.movimentoEstoqueCota(produtoEdicaoInfoExame1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaInfoExame1,
				new BigDecimal(55), cotaGuilherme, StatusAprovacao.PENDENTE, null);
		
		MovimentoEstoqueCota movimentoEstoqueCota12 = Fixture.movimentoEstoqueCota(produtoEdicaoSuper1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaSuper1,
				BigDecimal.TEN, cotaMurilo, StatusAprovacao.PENDENTE, null);
		
		MovimentoEstoqueCota movimentoEstoqueCota13 = Fixture.movimentoEstoqueCota(produtoEdicaoCapricho1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaCapricho1,
				BigDecimal.TEN, cotaMariana, StatusAprovacao.PENDENTE, null);
		
		MovimentoEstoqueCota movimentoEstoqueCota14 = Fixture.movimentoEstoqueCota(produtoEdicaoQuatroRodas1,
				tipoMovimentoRecReparte, usuarioJoao, estoqueProdutoCotaQuatroRodas1,
				BigDecimal.TEN, cotaOrlando, StatusAprovacao.PENDENTE, null);
		
		save(session, movimentoEstoqueCota1, movimentoEstoqueCota2, movimentoEstoqueCota3,
			 movimentoEstoqueCota4, movimentoEstoqueCota5, movimentoEstoqueCota6,
			 movimentoEstoqueCota7, movimentoEstoqueCota8, movimentoEstoqueCota9,
			 movimentoEstoqueCota10, movimentoEstoqueCota11, movimentoEstoqueCota12,
			 movimentoEstoqueCota13, movimentoEstoqueCota14);
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
		MovimentoEstoque movimentoRecFisicoVeja1 = 
			Fixture.movimentoEstoque(itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuarioJoao,
				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
				 StatusAprovacao.APROVADO, "Aprovado");
		
		session.save(movimentoRecFisicoVeja1);
		
		MovimentoEstoque movimentoEstoque1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque1);
		
		MovimentoEstoque movimentoEstoque2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoFaltaEm, usuarioJoao,
									 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque2);
		
		MovimentoEstoque movimentoEstoque3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), new BigDecimal(2),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque3);
		
		MovimentoEstoque movimentoEstoque4 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoFaltaDe, usuarioJoao,
									 estoqueProdutoVeja2, new Date(), new BigDecimal(2),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoque4);
		
		MovimentoEstoque movimentoEstoque5 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), new BigDecimal(3),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque5);
		
		MovimentoEstoque movimentoEstoqueDiferenca6 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoSobraEm, usuarioJoao,
									 estoqueProdutoVeja3, new Date(), new BigDecimal(3),
									 StatusAprovacao.PENDENTE, null);
			session.save(movimentoEstoqueDiferenca6);
		
		MovimentoEstoque movimentoEstoque7 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), new BigDecimal(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque7);
		
		MovimentoEstoque movimentoEstoque8 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja4, tipoMovimentoSobraDe, usuarioJoao,
									 estoqueProdutoVeja4, new Date(), new BigDecimal(4),
									 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEstoque8);
		
		MovimentoEstoque movimentoRecFisicoVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja2);
		
		MovimentoEstoque movimentoRecFisicoVeja3 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja3, tipoMovimentoRecFisico, usuarioJoao,
					 				 estoqueProdutoVeja3, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoRecFisicoVeja3);
		
		MovimentoEstoque movimentoEnvioJornaleiroVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);	
		session.save(movimentoEnvioJornaleiroVeja1);
		
		MovimentoEstoque movimentoEnvioJornaleiroSuper1 =
			Fixture.movimentoEstoque(null, produtoEdicaoSuper1, tipoMovimentoEnvioJornaleiro, usuarioJoao,
					 				 estoqueProdutoSuper1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioJornaleiroSuper1);
		
		MovimentoEstoque movimentoEnvioEncalheVeja1 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja1, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja1, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja1);
		
		MovimentoEstoque movimentoEnvioEncalheVeja2 =
			Fixture.movimentoEstoque(null, produtoEdicaoVeja2, tipoMovimentoEnvioEncalhe, usuarioJoao,
					 				 estoqueProdutoVeja2, new Date(), new BigDecimal(1),
					 				 StatusAprovacao.PENDENTE, null);
		session.save(movimentoEnvioEncalheVeja2);
	}

	private static void criarEstoquesProdutos(Session session) {
		estoqueProdutoVeja1 = Fixture.estoqueProduto(produtoEdicaoVeja1, BigDecimal.ZERO);
		
		estoqueProdutoVeja2 = Fixture.estoqueProduto(produtoEdicaoVeja2, BigDecimal.ZERO);
		
		estoqueProdutoVeja3 = Fixture.estoqueProduto(produtoEdicaoVeja3, BigDecimal.ZERO);
		
		estoqueProdutoVeja4 = Fixture.estoqueProduto(produtoEdicaoVeja4, BigDecimal.ZERO);
		
		estoqueProdutoSuper1 = Fixture.estoqueProduto(produtoEdicaoSuper1, BigDecimal.ZERO);
		
		estoqueProdutoCapricho1 = Fixture.estoqueProduto(produtoEdicaoCapricho1, BigDecimal.ZERO);
		
		estoqueProdutoInfoExame1 = Fixture.estoqueProduto(produtoEdicaoInfoExame1, BigDecimal.ZERO);
		
		save(session, estoqueProdutoVeja1, estoqueProdutoVeja2, estoqueProdutoVeja3,
			 estoqueProdutoVeja4, estoqueProdutoSuper1, estoqueProdutoCapricho1,
			 estoqueProdutoInfoExame1);
	}

	private static void criarRecebimentosFisicos(Session session) {
		recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuarioJoao, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);
		
		itemRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalFornecedor, recebimentoFisico, BigDecimal.TEN);
		session.save(itemRecebimentoFisico);
		
		itemCocaRecebimentoFisico = Fixture.itemRecebimentoFisico(itemNotaFiscalCoca, recebimentoFisico, new BigDecimal(50));
		session.save(itemCocaRecebimentoFisico);
		
		//TODO
	}

	private static void criarNotasFiscais(Session session) {
		notaFiscalFornecedor = Fixture
				.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
						usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
		session.save(notaFiscalFornecedor);

		itemNotaFiscalFornecedor = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaFiscalFornecedor);
		
		//TODO
		itemNotaFiscalCoca= Fixture.itemNotaFiscal(cocaColaLight,
				usuarioJoao, notaFiscalFornecedor, new Date(), new Date(), TipoLancamento.LANCAMENTO, BigDecimal.TEN);
		session.save(itemNotaFiscalCoca);
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
						new Date(), BigDecimal.TEN,  StatusLancamento.ESTUDO_FECHADO,
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
		
		lancamentoCocaCola = Fixture.lancamento(TipoLancamento.LANCAMENTO,cocaColaLight , 
				new Date(), new Date(), new Date(), new Date(), new BigDecimal(100), StatusLancamento.CONFIRMADO, itemCocaRecebimentoFisico);
		save(session, lancamentoCocaCola);
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
		
		cocaColaLight = Fixture.produtoEdicao(1L, 10, 30,
				new BigDecimal(0.20), new BigDecimal(9), new BigDecimal(12),
				cocaCola);
		session.save(cocaColaLight);
	}

	private static void criarProdutos(Session session) {
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.addFornecedor(fornecedorDinap);
		produtoVeja.setEditor(editoraAbril);
		session.save(produtoVeja);

		produtoSuper = Fixture.produtoSuperInteressante(tipoProdutoRevista);
		produtoSuper.addFornecedor(fornecedorDinap);
		produtoSuper.setEditor(editoraAbril);
		session.save(produtoSuper);
		
		produtoCapricho = Fixture.produtoCapricho(tipoProdutoRevista);
		produtoCapricho.addFornecedor(fornecedorDinap);
		produtoCapricho.setEditor(editoraAbril);
		session.save(produtoCapricho);
		
		produtoInfoExame = Fixture.produtoInfoExame(tipoProdutoRevista);
		produtoInfoExame.addFornecedor(fornecedorDinap);
		produtoInfoExame.setEditor(editoraAbril);
		session.save(produtoInfoExame);
		
		produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProdutoRevista);
		produtoQuatroRodas.addFornecedor(fornecedorDinap);
		produtoQuatroRodas.setEditor(editoraAbril);
		session.save(produtoQuatroRodas);
		
		produtoBoaForma = Fixture.produtoBoaForma(tipoProdutoRevista);
		produtoBoaForma.addFornecedor(fornecedorDinap);
		produtoBoaForma.setEditor(editoraAbril);
		session.save(produtoBoaForma);
		
		produtoBravo = Fixture.produtoBravo(tipoProdutoRevista);
		produtoBravo.addFornecedor(fornecedorDinap);
		produtoBravo.setEditor(editoraAbril);
		session.save(produtoBravo);
		
		produtoCaras = Fixture.produtoCaras(tipoProdutoRevista);
		produtoCaras.addFornecedor(fornecedorDinap);
		produtoCaras.setEditor(editoraAbril);
		session.save(produtoCaras);
		
		produtoCasaClaudia = Fixture.produtoCasaClaudia(tipoProdutoRevista);
		produtoCasaClaudia.addFornecedor(fornecedorDinap);
		produtoCasaClaudia.setEditor(editoraAbril);
		session.save(produtoCasaClaudia);
		
		produtoClaudia = Fixture.produtoClaudia(tipoProdutoRevista);
		produtoClaudia.addFornecedor(fornecedorDinap);
		produtoClaudia.setEditor(editoraAbril);
		session.save(produtoClaudia);
		
		produtoContigo = Fixture.produtoContigo(tipoProdutoRevista);
		produtoContigo.addFornecedor(fornecedorDinap);
		produtoContigo.setEditor(editoraAbril);
		session.save(produtoContigo);
		
		produtoManequim = Fixture.produtoManequim(tipoProdutoRevista);
		produtoManequim.addFornecedor(fornecedorDinap);
		produtoManequim.setEditor(editoraAbril);
		session.save(produtoManequim);
		
		produtoNatGeo = Fixture.produtoNationalGeographic(tipoProdutoRevista);
		produtoNatGeo.addFornecedor(fornecedorDinap);
		produtoNatGeo.setEditor(editoraAbril);
		session.save(produtoNatGeo);
		
		produtoPlacar = Fixture.produtoPlacar(tipoProdutoRevista);
		produtoPlacar.addFornecedor(fornecedorDinap);
		produtoPlacar.setEditor(editoraAbril);
		session.save(produtoPlacar);
		
		cocaCola = Fixture.produto("564", "Coca-Cola", "Coca-Cola", PeriodicidadeProduto.MENSAL, tipoRefrigerante);
		cocaCola.addFornecedor(fornecedorAcme);
		save(session, cocaCola);
	}
	
	private static void criarEditores(Session session) {
		editoraAbril = Fixture.editoraAbril();
		save(session, editoraAbril);
	}

	private static void criarTiposProduto(Session session) {
		tipoProdutoRevista = Fixture.tipoRevista();
		session.save(tipoProdutoRevista);
		
		tipoRefrigerante = Fixture.tipoProduto("Refrigerante",GrupoProduto.OUTROS, "5644566");
		session.save(tipoRefrigerante);
	}

	private static void criarDistribuidor(Session session) {
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"56.003.315/0001-47", "333.333.333.333", "distrib_acme@mail.com");
		save(session, juridicaDistrib);

		formaBoleto = Fixture.formaCobrancaBoleto(true, new BigDecimal(200), true, bancoHSBC,
												  BigDecimal.ONE, BigDecimal.ONE);
		
		formaCheque = Fixture.formaCobrancaCheque(true, new BigDecimal(200), true, bancoHSBC,
				  BigDecimal.ONE, BigDecimal.ONE);
		
		formaDeposito = Fixture.formaCobrancaDeposito(true, new BigDecimal(200), true, bancoHSBC,
				  BigDecimal.ONE, BigDecimal.ONE);
		
		formaDinheiro = Fixture.formaCobrancaDinheiro(true, new BigDecimal(200), true, bancoHSBC,
				  BigDecimal.ONE, BigDecimal.ONE);
		
		formaTransferenciBancaria = Fixture.formaCobrancaTransferencia(true, new BigDecimal(200), true, bancoHSBC,
				  BigDecimal.ONE, BigDecimal.ONE);
		
		save(session, formaBoleto,formaCheque,formaDeposito,formaDinheiro,formaTransferenciBancaria);
		
		PoliticaCobranca politicaCobranca =
			Fixture.criarPoliticaCobranca(distribuidor, formaBoleto, true, true, true, 1,"Assunto","Mansagem");
		
		PoliticaSuspensao politicaSuspensao = new PoliticaSuspensao();
		politicaSuspensao.setValor(new BigDecimal(0));
		
		distribuidor = Fixture.distribuidor(juridicaDistrib, new Date(), politicaCobranca);
		distribuidor.getFormasCobranca().add(formaBoleto);

		distribuidor.setPoliticaSuspensao(politicaSuspensao);

		distribuidor.getFormasCobranca().add(formaCheque);
		distribuidor.getFormasCobranca().add(formaDeposito);
		distribuidor.getFormasCobranca().add(formaDinheiro);
		distribuidor.getFormasCobranca().add(formaTransferenciBancaria);
		
		save(session, distribuidor);
	}
	
	private static void criarEnderecoDistribuidor(Session session){
		
		Endereco endereco = Fixture.criarEndereco(
				TipoEndereco.COBRANCA, "13222-020", "Rua João de Souza", 51, "Centro", "São Paulo", "SP");
		
		EnderecoDistribuidor enderecoDistribuidor = Fixture.enderecoDistribuidor(distribuidor, endereco, true, TipoEndereco.COBRANCA);
		
		save(session,endereco,enderecoDistribuidor);
	}
	
	private static void criarTelefoneDistribuidor(Session session){
		
		Telefone telefone = Fixture.telefone("019", "259633", "012");
		
		TelefoneDistribuidor teDistribuidor = Fixture.telefoneDistribuidor(distribuidor, true, telefone, TipoTelefone.COMERCIAL);
		
		save(session, telefone,teDistribuidor);
	}

	private static void criarCotas(Session session) {
		

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO,box1);
		save(session, cotaManoel);
		
		cotaJose = Fixture.cota(1234, jose, SituacaoCadastro.ATIVO,box1);
		save(session, cotaJose);
		
		cotaMaria = Fixture.cota(12345, maria, SituacaoCadastro.ATIVO,box2);
		save(session, cotaMaria);
		
		cotaGuilherme = Fixture.cota(333, guilherme, SituacaoCadastro.ATIVO,box2);
		cotaGuilherme.setSugereSuspensao(true);
		save(session, cotaGuilherme);
		
		cotaMurilo= Fixture.cota(22345, murilo, SituacaoCadastro.ATIVO,box2);
		cotaMurilo.setSugereSuspensao(true);
		save(session, cotaMurilo);
		
		cotaMariana = Fixture.cota(32345, mariana, SituacaoCadastro.ATIVO,box1);
		cotaMariana.setSugereSuspensao(true);
		save(session, cotaMariana);
		
		cotaOrlando = Fixture.cota(4444, orlando, SituacaoCadastro.INATIVO,box1);
		cotaOrlando.setSugereSuspensao(false);
		save(session, cotaOrlando);

		cotaJoao = Fixture.cota(9999, joao, SituacaoCadastro.ATIVO,box2);
		save(session, cotaJoao);
		
		cotaLuis = Fixture.cota(888, luis, SituacaoCadastro.ATIVO,box2);
		save(session, cotaLuis);

		
		ParametroCobrancaCota parametroCobrancaConta = 
			Fixture.parametroCobrancaCota(1, BigDecimal.TEN, cotaManoel, 1, 
										  formaBoleto, true, BigDecimal.TEN);
		
		ParametroCobrancaCota parametroCobrancaConta1 = 
				Fixture.parametroCobrancaCota(2, BigDecimal.TEN, cotaJose, 1, 
											  formaCheque, true, BigDecimal.TEN);
		
		ParametroCobrancaCota parametroCobrancaConta2 = 
				Fixture.parametroCobrancaCota(2, BigDecimal.TEN, cotaMaria, 1, 
											  formaDeposito, true, BigDecimal.TEN);
		
		ParametroCobrancaCota parametroCobrancaConta3 = 
				Fixture.parametroCobrancaCota(2, BigDecimal.TEN, cotaJoao, 1, 
											  formaDinheiro, false, BigDecimal.TEN);
		
		ParametroCobrancaCota parametroCobrancaConta4 = 
				Fixture.parametroCobrancaCota(2, BigDecimal.TEN, cotaLuis, 1, 
											  formaTransferenciBancaria, true, BigDecimal.TEN);
		
		
		save(session, parametroCobrancaConta,parametroCobrancaConta1,parametroCobrancaConta2,parametroCobrancaConta3,parametroCobrancaConta4);
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
		
		
		tipoMovimentoVendaEncalhe = Fixture.tipoMovimentoVendaEncalhe();
		
		tipoMovimentoFinanceiroCompraEncalhe = Fixture.tipoMovimentoFinanceiroCompraEncalhe();
		
		save(session, tipoMovimentoVendaEncalhe,tipoMovimentoFinanceiroCompraEncalhe);

		tipoMovimentoEnvioEncalhe = Fixture.tipoMovimentoEnvioEncalhe();
		
		tipoMovimentoFinanceiroCredito = Fixture.tipoMovimentoFinanceiroCredito();
		tipoMovimentoFinanceiroDebito = Fixture.tipoMovimentoFinanceiroDebito();
		tipoMovimentoFinanceiroRecebimentoReparte = Fixture.tipoMovimentoFinanceiroRecebimentoReparte();
		tipoMovimentoFinanceiroJuros = Fixture.tipoMovimentoFinanceiroJuros();
		tipoMovimentoFinanceiroMulta = Fixture.tipoMovimentoFinanceiroMulta();
		tipoMovimentoFinanceiroEnvioEncalhe = Fixture.tipoMovimentoFinanceiroEnvioEncalhe();
		
		tipoMovimentoEnvioJornaleiro = Fixture.tipoMovimentoEnvioJornaleiro();
		save(session,tipoMovimentoEnvioJornaleiro);
		
		tipoMovimentoFinanceiroCredito.setAprovacaoAutomatica(false);
		tipoMovimentoFinanceiroDebito.setAprovacaoAutomatica(false);
		
		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe,
				tipoMovimentoSobraEm, tipoMovimentoSobraDe,
				tipoMovimentoRecFisico, tipoMovimentoRecReparte,
				tipoMovimentoFinanceiroCredito, tipoMovimentoFinanceiroDebito,
				tipoMovimentoEnvioEncalhe, tipoMovimentoFinanceiroRecebimentoReparte,
				tipoMovimentoFinanceiroJuros, tipoMovimentoFinanceiroMulta,
				tipoMovimentoFinanceiroEnvioEncalhe);

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
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.APROVADO, null);
			
			session.save(movimentoEstoqueDiferenca);
			
			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.PENDENTE, null, movimentoEstoqueDiferenca, true);
			
			session.save(diferenca);
		}
		
		for (int i = 1; i <= quantidadeRegistros; i++) {
			
			MovimentoEstoque movimentoEstoqueDiferenca = 
				Fixture.movimentoEstoque(
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.PENDENTE, null);
			
			session.save(movimentoEstoqueDiferenca);
			
			Diferenca diferenca = 
				Fixture.diferenca(
					new BigDecimal(i), usuario, produtoEdicao, tipoDiferenca, 
						StatusConfirmacao.CONFIRMADO, null, movimentoEstoqueDiferenca, true);
			
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
				
				
				MovimentoEstoque movimentoEstoque  = 
					Fixture.movimentoEstoque(itemFisico, produtoEdicao, tipoMovimentoFaltDe, usuario,
						estoque, new Date(), new BigDecimal(1),
						StatusAprovacao.APROVADO, "Aprovado");
				
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
		criarPessoas(session);
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
				"10.000.000/0001-00", "000.000.000.000", "sys.discover@gmail.com");
		juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11.111.111/0001-11", "111.111.111.111", "sys.discover@gmail.com");
		juridicaFc = Fixture.pessoaJuridica("FC",
				"22.222.222/0001-22", "222.222.222.222", "sys.discover@gmail.com");
		juridicaValida = Fixture.pessoaJuridica("Juridica Valida",
				"93.081.738/0001-01", "333.333.333.333", "sys.discover@gmail.com");
		
		manoel = Fixture.pessoaFisica("319.435.088-95",
				"sys.discover@gmail.com", "Manoel da Silva");

		jose = Fixture.pessoaFisica("123.456.789-01",
				"sys.discover@gmail.com", "Jose da Silva");
		
		maria = Fixture.pessoaFisica("123.456.789-02",
				"sys.discover@gmail.com", "Maria da Silva");
		
		guilherme = Fixture.pessoaFisica("999.333.555-11", "sys.discover@gmail.com", "Guilherme de Morais Leandro");
		
		murilo = Fixture.pessoaFisica("999.333.555-22", "sys.discover@gmail.com", "Murilo");
		
		mariana = Fixture.pessoaFisica("999.333.555-33", "sys.discover@gmail.com", "Mariana");
		
		orlando = Fixture.pessoaFisica("999.333.555-44", "sys.discover@gmail.com", "Orlando");
		
		luis  = Fixture.pessoaFisica("123.456.789-03",
				"sys.discover@gmail.com", "Luis Silva");
		
	    joao = Fixture.pessoaFisica("123.456.789-04",
				"sys.discover@gmail.com", "João da Silva");
		
		save(session, juridicaAcme, juridicaDinap, juridicaFc, juridicaValida,manoel,jose,maria,
				guilherme,murilo,mariana,orlando,luis,joao);

	}
	

	
	//FINANCEIRO - CONSULTA BOLETOS
	private static void criarBoletos(Session session) {
		
		//Boletos já pagos
		Boleto boleto1 = Fixture.boleto("1309309032012", "440", "1309309032012440", 
				                        new Date(),  new Date(),
				                        new Date(), BigDecimal.ZERO, new BigDecimal(100),
										"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
										cotaManoel, bancoHSBC, divida1,0);
		
		Boleto boleto2 = Fixture.boleto("1309709032012", "747", "1309709032012747", 
				                        new Date(), new Date(), 
				                        new Date(),  BigDecimal.ZERO, new BigDecimal(200), 
		                				"TIPO_BAIXA", "ACAO", StatusCobranca.PAGO,
		                				cotaManoel, bancoHSBC, divida2,0);
		
		
		//Boletos vencidos
		Boleto boleto3 = Fixture.boleto("1310209032012", "740", "1310209032012740",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"),
										null,  BigDecimal.ZERO, new BigDecimal(300), 
										"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
										cotaManoel, bancoHSBC, divida3,0);
		
		Boleto boleto4 = Fixture.boleto("1310609032012", "041", "1310609032012041",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"), 
		                				null, BigDecimal.ZERO, new BigDecimal(400), 
		                				"TIPO_BAIXA", "ACAO",  StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida4,0);
		
		Boleto boleto5 = Fixture.boleto("1310809032012", "641", "1310809032012641",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("09/03/2012"), 
		                				null, BigDecimal.ZERO, new BigDecimal(500), "TIPO_BAIXA",
		                				"ACAO", StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida5,0);
		
		
		//Boletos não vencidos
		Boleto boleto6 = Fixture.boleto("1311009032012", "840", "1311009032012840",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"), 
		                				null, BigDecimal.ZERO, new BigDecimal(2258.62),
		                				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
		                				cotaManoel, bancoHSBC, divida6,0);
		
		Boleto boleto7 = Fixture.boleto("1311109032012", "642", "1311109032012642",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"), 
                						null, BigDecimal.ZERO, new BigDecimal(700), 
                						"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
                						cotaManoel, bancoHSBC, divida7,0);
		
		Boleto boleto8 = Fixture.boleto("1312309032012", "043", "1312309032012043",
				                        DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"), 
                						null, BigDecimal.ZERO, new BigDecimal(800), 
                						"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
                						cotaManoel, bancoHSBC, divida8,0);
		
		save(session, boleto1, boleto2, boleto3, boleto4, boleto5, boleto6, 
				      boleto7, boleto8);	

	}
	
	
	private static void criarCobrancaCheque(Session session){
		
		CobrancaCheque cobranca = Fixture.cobrancaCheque("1234567890118",
                DateUtil.parseDataPTBR("11/03/2012"), DateUtil.parseDataPTBR("15/03/2012"), 
                null,BigDecimal.ZERO,new BigDecimal(900), 
                "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
                cotaJose,divida14,0);
		
		CobrancaCheque cobranca1 = Fixture.cobrancaCheque("1012345678812",
                new Date(),DateUtil.adicionarDias(new Date(), 30) , 
	             null,BigDecimal.ZERO,new BigDecimal(1000), 
	             "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
	             cotaJose,divida13,0);
		
		save(session, cobranca,cobranca1);
	}
	
	private static void criarCobrancaDinheiro(Session session){
		
		
		CobrancaDinheiro cobranca = Fixture.cobrancaDinheiro("1313209032012", 
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
				 null, BigDecimal.ZERO, new BigDecimal(1100), 
				 "TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				 cotaLuis, bancoHSBC, divida11,0);
		
		save(session, cobranca);
	}
	
	private static void criarCobrancaTranferenciaBancaria(Session session){
		

		CobrancaTransferenciaBancaria cobranca1 = Fixture.cobrancaTransferencaiBancaria("1234567821123",
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("15/03/2012"), 
                null,BigDecimal.ZERO,new BigDecimal(1200), 
                "TIPO_BAIXA","ACAO",StatusCobranca.NAO_PAGO,
                cotaJoao,bancoITAU,divida12,0);
		
		save(session, cobranca1);
	}
	
	private static void criarCobrancaDepositoBancaria(Session session){
		
		CobrancaDeposito cobranca = Fixture.cobrancaDeposito("1312409032012", 
                DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"), 
				null, BigDecimal.ZERO, new BigDecimal(1300), 
				"TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				cotaMaria, bancoHSBC, divida9,0);

		CobrancaDeposito cobranca1 = Fixture.cobrancaDeposito("1312509032012", 
                 DateUtil.parseDataPTBR("09/03/2012"), DateUtil.parseDataPTBR("12/03/2012"),
				 null, BigDecimal.ZERO, new BigDecimal(1400), 
				 "TIPO_BAIXA", "ACAO", StatusCobranca.NAO_PAGO,
				 cotaMaria, bancoHSBC, divida10,0);
		
		save(session, cobranca,cobranca1);
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
		enderecoCota2.setCota(cotaMaria);
		enderecoCota2.setEndereco(endereco2);
		enderecoCota2.setPrincipal(false);
		enderecoCota2.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco enderecoLuis = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		
		EnderecoCota enderecoCotaLuis = new EnderecoCota();
		enderecoCotaLuis.setCota(cotaLuis);
		enderecoCotaLuis.setEndereco(enderecoLuis);
		enderecoCotaLuis.setPrincipal(false);
		enderecoCotaLuis.setTipoEndereco(TipoEndereco.COBRANCA);
		
		Endereco enderecoJoao = Fixture.criarEndereco(
				TipoEndereco.LOCAL_ENTREGA, "13730-000", "Rua X", 50, "Vila Carvalho", "Mococa", "SP");

		
		EnderecoCota enderecoCotaJoao = new EnderecoCota();
		enderecoCotaJoao.setCota(cotaJoao);
		enderecoCotaJoao.setEndereco(enderecoJoao);
		enderecoCotaJoao.setPrincipal(false);
		enderecoCotaJoao.setTipoEndereco(TipoEndereco.COBRANCA);

		save(session, endereco, enderecoCota, endereco2,enderecoCota2,enderecoLuis,enderecoCotaLuis,enderecoJoao,enderecoCotaJoao);
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
					"30.000.000/0001-00", "000.000.000.000", "acme@mail.com");
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
							  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "HSBC", "399", BigDecimal.ZERO, BigDecimal.ZERO);
		
		bancoITAU = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  12345L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_ITAU", "184", BigDecimal.ZERO, BigDecimal.ZERO);
		
		bancoDOBRASIL = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_DO_BRASIL", "001", BigDecimal.ZERO, BigDecimal.ZERO);
		
		bancoBRADESCO = Fixture.banco(10L, true, carteiraSemRegistro, "1010",
				  123456L, "1", "1", "Instrucoes.", Moeda.REAL, "BANCO_BRADESCO", "065", BigDecimal.ZERO, BigDecimal.ZERO);
		
		save(session, bancoHSBC,bancoITAU,bancoDOBRASIL,bancoBRADESCO);
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
	
	private static void gerarCargaDadosConsultaEncalhe(Session session) {
		
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE = null;
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE_2 = null;
		ItemRecebimentoFisico itemRecebimentoFisicoProdutoCE_3 = null;
		
		Date dataRecebimento = null;

		Lancamento lancamentoRevistaCE = null;
		Lancamento lancamentoRevistaCE_2 = null;
		Lancamento lancamentoRevistaCE_3 = null;
		
		Cota cotaJohnyConsultaEncalhe = null;
		
		ProdutoEdicao produtoEdicaoCE = null;
		ProdutoEdicao produtoEdicaoCE_2 = null;
		ProdutoEdicao produtoEdicaoCE_3 = null;
		
		PeriodicidadeProduto periodicidade = PeriodicidadeProduto.MENSAL;
		
		Produto produtoCE = Fixture.produto("00084", "Produto CE", "ProdutoCE", periodicidade, tipoProdutoRevista);
		Produto produtoCE_2 = Fixture.produto("00085", "Produto CE 2", "ProdutoCE_2", periodicidade, tipoProdutoRevista);
		Produto produtoCE_3 = Fixture.produto("00086", "Produto CE 3", "ProdutoCE_3", periodicidade, tipoProdutoRevista);
		
		produtoCE.addFornecedor(fornecedorDinap);
		produtoCE_2.addFornecedor(fornecedorDinap);
		produtoCE_3.addFornecedor(fornecedorDinap);
		
		save(session, produtoCE, produtoCE_2, produtoCE_3);

		produtoEdicaoCE = Fixture.produtoEdicao(84L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(15), produtoCE);
		produtoEdicaoCE.setDesconto(BigDecimal.ZERO);

		
		produtoEdicaoCE_2 = Fixture.produtoEdicao(85L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(18), produtoCE_2);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		produtoEdicaoCE_3 = Fixture.produtoEdicao(86L, 10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(90), produtoCE_3);
		produtoEdicaoCE.setDesconto(BigDecimal.ONE);

		
		save(session, produtoEdicaoCE, produtoEdicaoCE_2, produtoEdicaoCE_3);
		
		NotaFiscalEntradaFornecedor notaFiscalProdutoCE = 
				Fixture.notaFiscalEntradaFornecedor(
						cfop5102, 
						fornecedorFc.getJuridica(), 
						fornecedorFc, 
						tipoNotaFiscalRecebimento,
						usuarioJoao, 
						BigDecimal.TEN, 
						BigDecimal.ZERO, 
						BigDecimal.TEN);
		
		save(session, notaFiscalProdutoCE);

		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(session, itemNotaFiscalProdutoCE);
		
		
		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_2 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_2, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(session, itemNotaFiscalProdutoCE_2);
		
		ItemNotaFiscalEntrada itemNotaFiscalProdutoCE_3 = 
				Fixture.itemNotaFiscal(
						produtoEdicaoCE_3, 
						usuarioJoao,
						notaFiscalProdutoCE, 
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						Fixture.criarData(22, Calendar.FEBRUARY,2012),
						TipoLancamento.LANCAMENTO,
						new BigDecimal(50));
		
		save(session, itemNotaFiscalProdutoCE_3);
		
		
		dataRecebimento = Fixture.criarData(22, Calendar.FEBRUARY, 2012);
		
		RecebimentoFisico recebimentoFisicoProdutoCE = 
				Fixture.recebimentoFisico(
						notaFiscalProdutoCE, 
						usuarioJoao, 
						dataRecebimento,
						dataRecebimento, 
						StatusConfirmacao.CONFIRMADO);
		
		save(session, recebimentoFisicoProdutoCE);
			
		itemRecebimentoFisicoProdutoCE = 
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE, 
						recebimentoFisicoProdutoCE, 
						new BigDecimal(50));
		
		save(session, itemRecebimentoFisicoProdutoCE);

		itemRecebimentoFisicoProdutoCE_2 = 
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_2, 
						recebimentoFisicoProdutoCE, 
						new BigDecimal(50));
		
		save(session, itemRecebimentoFisicoProdutoCE_2);

		itemRecebimentoFisicoProdutoCE_3 = 
				Fixture.itemRecebimentoFisico(
						itemNotaFiscalProdutoCE_3, 
						recebimentoFisicoProdutoCE, 
						new BigDecimal(50));
		
		save(session, itemRecebimentoFisicoProdutoCE_3);

		
		lancamentoRevistaCE = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE);
		
		lancamentoRevistaCE.getRecebimentos().add(itemRecebimentoFisicoProdutoCE);
		
		
		Estudo estudo = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE);

		save(session, lancamentoRevistaCE, estudo);

		
		lancamentoRevistaCE_2 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE_2,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE_2);
		
		lancamentoRevistaCE_2.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_2);
		
		
		Estudo estudo_2 = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE_2);

		save(session, lancamentoRevistaCE_2, estudo_2);

		
		lancamentoRevistaCE_3 = Fixture.lancamento(
				TipoLancamento.SUPLEMENTAR, 
				produtoEdicaoCE_3,
				Fixture.criarData(22, Calendar.FEBRUARY, 2012),
				Fixture.criarData(28, Calendar.FEBRUARY, 2012),
				new Date(),
				new Date(),
				new BigDecimal(100),
				StatusLancamento.BALANCEADO_RECOLHIMENTO, 
				itemRecebimentoFisicoProdutoCE_3);
		
		lancamentoRevistaCE_3.getRecebimentos().add(itemRecebimentoFisicoProdutoCE_3);
		
		
		Estudo estudo_3 = Fixture.estudo(new BigDecimal(100),
				Fixture.criarData(22, Calendar.FEBRUARY, 2012), produtoEdicaoCE_3);

		save(session, lancamentoRevistaCE_3, estudo_3);

		
		PessoaFisica johnyCE = Fixture.pessoaFisica(
				"352.855.474-00",
				"johny@discover.com.br", "Johny da Silva");
		save(session, johnyCE);
		
		cotaJohnyConsultaEncalhe = Fixture.cota(2593, johnyCE, SituacaoCadastro.ATIVO, box1);
		save(session, cotaJohnyConsultaEncalhe);
		
		
		
		EstoqueProdutoCota estoqueProdutoCotaJohny = 
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny);
		
		ChamadaEncalhe chamadaEncalhe = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				Fixture.criarData(10, Calendar.MARCH, 2012), 
				produtoEdicaoCE, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		save(session, chamadaEncalhe);
		
		
		EstoqueProdutoCota estoqueProdutoCotaJohny_2 = 
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_2, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny_2);
		
		ChamadaEncalhe chamadaEncalhe_2 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				Fixture.criarData(10, Calendar.MARCH, 2012), 
				produtoEdicaoCE_2, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		save(session, chamadaEncalhe_2);
		
		

		EstoqueProdutoCota estoqueProdutoCotaJohny_3 = 
				Fixture.estoqueProdutoCota(
				produtoEdicaoCE_3, cotaJohnyConsultaEncalhe, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCotaJohny_3);
		
		ChamadaEncalhe chamadaEncalhe_3 = Fixture.chamadaEncalhe(
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				Fixture.criarData(10, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3, 
				TipoChamadaEncalhe.MATRIZ_RECOLHIMENTO);
		
		save(session, chamadaEncalhe_3);
		
		
		/**
		 * MOVIMENTOS DE ENVIO ENCALHE ABAIXO
		 */
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny,
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(2, Calendar.MARCH, 2012), 
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny,
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny,
				new BigDecimal(8), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(34), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(45), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(4, Calendar.MARCH, 2012), 
				produtoEdicaoCE_2,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_2,
				new BigDecimal(65), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(28, Calendar.FEBRUARY, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(31), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(3, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(41), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(5, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(6, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(7, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
		mec = Fixture.movimentoEstoqueCotaEnvioEncalhe( 
				Fixture.criarData(7, Calendar.MARCH, 2012), 
				produtoEdicaoCE_3,
				tipoMovimentoEnvioEncalhe, 
				usuarioJoao, 
				estoqueProdutoCotaJohny_3,
				new BigDecimal(85), cotaJohnyConsultaEncalhe, 
				StatusAprovacao.APROVADO, 
				"Aprovado");
		
		save(session, mec);
		
	}
	
	private static void carregarDadosParaContagemDevolucao(Session session) {

		Lancamento lancamentoVeja = null;
		Fornecedor fornecedorFC = null;
		Fornecedor fornecedorDinap = null;
		TipoProduto tipoCromo = null;
		TipoFornecedor tipoFornecedorPublicacao = null;

		CFOP cfopDentroEstado = Fixture.cfop1209();
		save(session, cfopDentroEstado);
		
		CFOP cfopForaEstado = Fixture.cfop1210();
		save(session, cfopForaEstado);
		
		ParametroEmissaoNotaFiscal parametroEmissaoNotaFiscal = Fixture.parametroEmissaoNotaFiscal(
				cfopDentroEstado, 
				cfopForaEstado, 
				GrupoNotaFiscal.DEVOLUCAO_MERCADORIA_FORNECEDOR, 
				"0001");
		
		save(session, parametroEmissaoNotaFiscal);
		
		criarDistribuidor(session);
		
		criarTiposMovimento(session);
		
		ControleNumeracaoNotaFiscal controleNumeracaoNotaFiscal = Fixture.controleNumeracaoNotaFiscal(1L, "0001");
		save(session, controleNumeracaoNotaFiscal);
		
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

		tipoNotaFiscal = Fixture.tipoNotaFiscalDevolucao();
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

		Cota cotaManoel = Fixture.cota(1237, manoel, SituacaoCadastro.ATIVO, box1);
		save(session, cotaManoel);

		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
		veja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);

		Usuario usuarioJoao = Fixture.usuarioJoao();
		save(session, usuarioJoao);


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
	
	private static void criarMovimentosFinanceiroCota(Session session) {

		movimentoFinanceiroCota1 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota1),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota2 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota2),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota3 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota3),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota4 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota4),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota5 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota5),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota6 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota6),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota7 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota7),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota8 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota8),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		movimentoFinanceiroCota9 = Fixture.movimentoFinanceiroCota(
			cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
			new BigDecimal(200), Arrays.asList(movimentoEstoqueCota9),
			StatusAprovacao.PENDENTE, new Date(), true);
		
		save(session, movimentoFinanceiroCota1, movimentoFinanceiroCota2,
					  movimentoFinanceiroCota3, movimentoFinanceiroCota4,
					  movimentoFinanceiroCota5, movimentoFinanceiroCota6,
					  movimentoFinanceiroCota7, movimentoFinanceiroCota8,
					  movimentoFinanceiroCota9);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota10 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 10), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota11 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 20), true);		
		
		MovimentoFinanceiroCota movimentoFinanceiroCota12 = 
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(225), null, StatusAprovacao.PENDENTE,
				DateUtil.adicionarDias(new Date(), 30), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroCota13 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroRecebimentoReparte, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 60), true);
				
		MovimentoFinanceiroCota movimentoFinanceiroDebito1 =
				Fixture.movimentoFinanceiroCota(
					cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					DateUtil.adicionarDias(new Date(), 50), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroDebito2 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroDebito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroCredito1 =
			Fixture.movimentoFinanceiroCota(
				cotaManoel, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.APROVADO,
				DateUtil.adicionarDias(new Date(), 40), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroCredito2 =
			Fixture.movimentoFinanceiroCota(
				cotaJoao, tipoMovimentoFinanceiroCredito, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroJuros1 =
			Fixture.movimentoFinanceiroCota(
				cotaJose, tipoMovimentoFinanceiroJuros, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroJuros2 =
			Fixture.movimentoFinanceiroCota(
				cotaMaria, tipoMovimentoFinanceiroJuros, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroMulta1 =
			Fixture.movimentoFinanceiroCota(
				cotaJose, tipoMovimentoFinanceiroMulta, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroMulta2 =
			Fixture.movimentoFinanceiroCota(
				cotaMaria, tipoMovimentoFinanceiroMulta, usuarioJoao,
				new BigDecimal(650), null, StatusAprovacao.PENDENTE,
				new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroEnvioEncalhe1 =
				Fixture.movimentoFinanceiroCota(
					cotaJose, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);
		
		MovimentoFinanceiroCota movimentoFinanceiroEnvioEncalhe2 =
				Fixture.movimentoFinanceiroCota(
					cotaMaria, tipoMovimentoFinanceiroEnvioEncalhe, usuarioJoao,
					new BigDecimal(650), null, StatusAprovacao.PENDENTE,
					new Date(), true);
		
		save(session, movimentoFinanceiroCota10, movimentoFinanceiroCota11,
				  	  movimentoFinanceiroCota12, movimentoFinanceiroCota13,
				  	  movimentoFinanceiroDebito1, movimentoFinanceiroDebito2,
				  	  movimentoFinanceiroCredito1, movimentoFinanceiroCredito2,
				  	  movimentoFinanceiroJuros1, movimentoFinanceiroJuros2,
				  	  movimentoFinanceiroMulta1, movimentoFinanceiroMulta2,
				  	  movimentoFinanceiroEnvioEncalhe1, movimentoFinanceiroEnvioEncalhe2);
	}

	private static void criarNotasFiscaisEntradaFornecedor(Session session) {

		for (int i = 0; i < 50; i++) {

			Calendar calendar = Calendar.getInstance();
			
			notaFiscalFornecedor = Fixture
					.notaFiscalEntradaFornecedor(cfop5102, fornecedorDinap.getJuridica(), fornecedorDinap, tipoNotaFiscalRecebimento,
							usuarioJoao, new BigDecimal(15), new BigDecimal(5), BigDecimal.TEN);
			
			calendar.add(Calendar.DATE, i * 3);
			
			notaFiscalFornecedor.setDataEmissao(calendar.getTime());
			
			session.save(notaFiscalFornecedor);
		}
	}	
	
	private static void gerarCargaHistoricoSituacaoCota(Session session, int quantidade) {
		
		Date dataAtual = new Date();
		
		for (int i = 1; i <= quantidade; i++) {
			
			HistoricoSituacaoCota historicoSituacaoCota = new HistoricoSituacaoCota();
			
			historicoSituacaoCota.setCota(cotaManoel);
			historicoSituacaoCota.setDataEdicao(new Date());
			historicoSituacaoCota.setDescricao("Descrição " + i);
			historicoSituacaoCota.setMotivo(MotivoAlteracaoSituacao.INADIMPLENCIA);
			historicoSituacaoCota.setNovaSituacao(SituacaoCadastro.ATIVO);
			historicoSituacaoCota.setSituacaoAnterior(SituacaoCadastro.SUSPENSO);
			historicoSituacaoCota.setTipoEdicao(TipoEdicao.ALTERACAO);
			historicoSituacaoCota.setDataInicioValidade(DateUtil.adicionarDias(dataAtual, i));
			historicoSituacaoCota.setDataFimValidade(DateUtil.adicionarDias(dataAtual, i));
			historicoSituacaoCota.setResponsavel(usuarioJoao);
			
			session.save(historicoSituacaoCota);
		}
	}
	
}
