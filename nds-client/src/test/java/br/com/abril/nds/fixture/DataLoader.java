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
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;
import br.com.abril.nds.model.cadastro.ParametroSistema;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
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
		PessoaJuridica juridicaAcme = Fixture.pessoaJuridica("Acme",
				"00.000.000/0001-00", "000.000.000.000", "acme@mail.com");
		PessoaJuridica juridicaDinap = Fixture.pessoaJuridica("Dinap",
				"11.111.111/0001-11", "111.111.111.111", "dinap@mail.com");
		PessoaJuridica juridicaFc = Fixture.pessoaJuridica("FC",
				"22.222.222/0001-22", "222.222.222.222", "fc@mail.com");
		PessoaJuridica juridicaDistrib = Fixture.pessoaJuridica("Distribuidor Acme",
				"33.333.333/0001-33", "333.333.333.333", "distrib_acme@mail.com");
		save(session, juridicaAcme, juridicaDinap, juridicaFc, juridicaDistrib);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(session, manoel);
		
		Fornecedor fornecedorAcme = Fixture.fornecedor(juridicaAcme,
				SituacaoCadastro.ATIVO, false);
		Fornecedor fornecedorDinap = Fixture.fornecedor(juridicaDinap,
				SituacaoCadastro.ATIVO, true);
		Fornecedor fornecedorFc = Fixture.fornecedor(juridicaFc,
				SituacaoCadastro.ATIVO, true);
		save(session, fornecedorAcme, fornecedorDinap, fornecedorFc);
		
		Distribuidor distribuidor = Fixture.distribuidor(juridicaDistrib, new Date());
		save(session, distribuidor);
		
		Cota cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO);
		save(session, cotaManoel);
		
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

		TipoProduto tipoProduto = Fixture.tipoProduto("Revista",
				GrupoProduto.REVISTA, "99000642");
		session.save(tipoProduto);

		Produto produtoVeja = Fixture.produto("1", "Revista Veja", "Veja",
				PeriodicidadeProduto.SEMANAL, tipoProduto);
		produtoVeja.addFornecedor(fornecedorDinap);
		session.save(produtoVeja);

		Produto produtoSuper = Fixture.produto("2",
				"Revista Superinteressante", "Superinteressante",
				PeriodicidadeProduto.MENSAL, tipoProduto);
		produtoSuper.addFornecedor(fornecedorDinap);
		session.save(produtoSuper);
		
		ProdutoEdicao produtoEdicaoVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja, fornecedorAcme);
		session.save(produtoEdicaoVeja1);
		
		ProdutoEdicao produtoEdicaoVeja2 = Fixture.produtoEdicao(2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja, fornecedorAcme);
		session.save(produtoEdicaoVeja2);
		
		ProdutoEdicao produtoEdicaoVeja3 = Fixture.produtoEdicao(3L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja, fornecedorAcme);
		session.save(produtoEdicaoVeja3);
		
		ProdutoEdicao produtoEdicaoVeja4 = Fixture.produtoEdicao(4L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoVeja, fornecedorAcme);
		session.save(produtoEdicaoVeja4);
		
		ProdutoEdicao produtoEdicaoSuper1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produtoSuper, fornecedorAcme);
		session.save(produtoEdicaoSuper1);
		
		Usuario usuario = Fixture.usuarioJoao();
		session.save(usuario);

		CFOP cfop = Fixture.cfop5102();
		session.save(cfop);
		
		TipoMovimento tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		TipoMovimento tipoMovimentoFaltaDe = Fixture.tipoMovimentoFaltaDe();
		TipoMovimento tipoMovimentoSobraEm = Fixture.tipoMovimentoSobraEm();
		TipoMovimento tipoMovimentoSobraDe = Fixture.tipoMovimentoSobraDe();
		
		TipoMovimento tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		TipoMovimento tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		
		save(session, tipoMovimentoFaltaEm, tipoMovimentoFaltaDe, tipoMovimentoSobraEm, tipoMovimentoSobraDe);

		session.save(tipoMovimentoRecFisico);
		
		session.save(tipoMovimentoRecReparte);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		session.save(tipoNotaFiscal);

		NotaFiscalFornecedor notaFiscalFornecedor = Fixture
				.notaFiscalFornecedor(cfop, juridicaAcme, fornecedorAcme, tipoNotaFiscal,
						usuario);
		session.save(notaFiscalFornecedor);

		ItemNotaFiscal itemNotaFiscal = Fixture.itemNotaFiscal(produtoEdicaoVeja1,
				usuario, notaFiscalFornecedor, new Date(), BigDecimal.TEN);
		session.save(itemNotaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico = 
			Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigDecimal.TEN);
		session.save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(produtoEdicaoVeja1, BigDecimal.ZERO);
		session.save(estoqueProduto);
		
		MovimentoEstoque movimentoEstoque = Fixture.movimentoEstoque(
				itemRecebimentoFisico, produtoEdicaoVeja1, tipoMovimentoRecFisico, usuario,
				estoqueProduto, StatusAprovacao.APROVADO);
		session.save(movimentoEstoque);
		session.update(estoqueProduto);
		
		Lancamento lancamento1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				produtoEdicaoVeja1, new Date(), new Date(), new Date(), new Date(),
				BigDecimal.TEN, StatusLancamento.RECEBIDO, itemRecebimentoFisico);
		session.save(lancamento1);
		Estudo estudo = Fixture
				.estudo(BigDecimal.TEN, new Date(), produtoEdicaoVeja1);
		session.save(estudo);
		
		Lancamento lancamento2 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				produtoEdicaoVeja2, new Date(), new Date(), new Date(), new Date(),
				BigDecimal.TEN, StatusLancamento.RECEBIDO, null);
		session.save(lancamento2);
		
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				produtoEdicaoVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(produtoEdicaoVeja1,
				tipoMovimentoRecReparte, usuario, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO);
		save(session, mec);
		
		ParametroSistema parametroSistema = Fixture.parametroSistema(1L,
				TipoParametroSistema.PATH_IMAGENS_CAPA,
				"C:\\apache-tomcat-7.0.25\\webapps\\nds-client\\capas\\");
		session.save(parametroSistema);
		
		// Início dos inserts na tabela MOVIMENTO_ESTOQUE
		
		MovimentoEstoque movimentoEstoqueDiferenca = Fixture.movimentoEstoque(null, produtoEdicaoVeja1,
																			  tipoMovimentoRecFisico, usuario,
																			  estoqueProduto, new Date(),
																			  new BigDecimal(1), StatusAprovacao.APROVADO);
		session.save(movimentoEstoqueDiferenca);
		
		MovimentoEstoque movimentoEstoqueDiferenca2 = Fixture.movimentoEstoque(null, produtoEdicaoVeja2,
																			   tipoMovimentoRecFisico, usuario,
																			   estoqueProduto, new Date(),
																			   new BigDecimal(2), StatusAprovacao.APROVADO);
		session.save(movimentoEstoqueDiferenca2);
		
		MovimentoEstoque movimentoEstoqueDiferenca3 = Fixture.movimentoEstoque(null, produtoEdicaoVeja3,
																			   tipoMovimentoRecFisico, usuario,
																			   estoqueProduto, new Date(),
																			   new BigDecimal(3), StatusAprovacao.APROVADO);
		session.save(movimentoEstoqueDiferenca3);
		
		MovimentoEstoque movimentoEstoqueDiferenca4 = Fixture.movimentoEstoque(null, produtoEdicaoVeja4,
																			   tipoMovimentoRecFisico, usuario,
																			   estoqueProduto, new Date(),
																			   new BigDecimal(4), StatusAprovacao.APROVADO);
		session.save(movimentoEstoqueDiferenca4);
		
		// Fim dos inserts na tabela MOVIMENTO_ESTOQUE
		
		// Início dos inserts na tabela DIFERENCA
		
		Diferenca diferenca = Fixture.diferenca(new BigDecimal(1), usuario,
												produtoEdicaoVeja1, TipoDiferenca.FALTA_EM,
						  						StatusConfirmacao.CONFIRMADO, null,
						  						movimentoEstoqueDiferenca);
		session.save(diferenca);
		
		Diferenca diferenca2 = Fixture.diferenca(new BigDecimal(2), usuario,
												produtoEdicaoVeja2, TipoDiferenca.FALTA_DE,
												StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico,
												movimentoEstoqueDiferenca2);
		session.save(diferenca2);
		
		Diferenca diferenca3 = Fixture.diferenca(new BigDecimal(3), usuario,
												 produtoEdicaoVeja3, TipoDiferenca.SOBRA_EM,
												 StatusConfirmacao.CONFIRMADO, null,
												 movimentoEstoqueDiferenca3);
		session.save(diferenca3);
		
		Diferenca diferenca4 = Fixture.diferenca(new BigDecimal(4), usuario,
												 produtoEdicaoVeja4, TipoDiferenca.SOBRA_DE,
												 StatusConfirmacao.CONFIRMADO, itemRecebimentoFisico,
												 movimentoEstoqueDiferenca4);
		session.save(diferenca4);
		
		// Fim dos inserts na tabela DIFERENCA
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja1, tipoMovimentoFaltaEm, 
				usuario, estoqueProduto, TipoDiferenca.FALTA_EM);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja2, tipoMovimentoFaltaDe, 
				usuario, estoqueProduto, TipoDiferenca.FALTA_DE);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja3, tipoMovimentoSobraDe, 
				usuario, estoqueProduto, TipoDiferenca.SOBRA_DE);
		
		gerarCargaDiferencaEstoque(
			session, 50, produtoEdicaoVeja4, tipoMovimentoSobraEm, 
				usuario, estoqueProduto, TipoDiferenca.SOBRA_EM);
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
					null, produtoEdicao, tipoMovimento, usuario, estoqueProduto, new Date(), new BigDecimal(i), StatusAprovacao.APROVADO);
			
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
