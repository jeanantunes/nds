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
import br.com.abril.nds.model.estoque.EstoqueProduto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
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

		Produto revistaVeja = Fixture.produto("1", "Revista Veja", "Veja",
				PeriodicidadeProduto.SEMANAL, tipoProduto);
		revistaVeja.addFornecedor(fornecedorDinap);
		session.save(revistaVeja);
		
		Produto revistaSuper = Fixture.produto("2",
				"Revista Superinteressante", "Superinteressante",
				PeriodicidadeProduto.MENSAL, tipoProduto);
		revistaSuper.addFornecedor(fornecedorDinap);
		session.save(revistaSuper);
		
		ProdutoEdicao revistaVeja1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				revistaVeja);
		session.save(revistaVeja1);
		
		ProdutoEdicao revistaVeja2 = Fixture.produtoEdicao(2L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				revistaVeja);
		session.save(revistaVeja2);
		
		ProdutoEdicao revistaVeja3 = Fixture.produtoEdicao(3L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				revistaVeja);
		session.save(revistaVeja3);
		
		ProdutoEdicao revistaSuper1 = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				revistaSuper);
		session.save(revistaSuper1);
		
		Usuario usuario = Fixture.usuarioJoao();
		session.save(usuario);

		CFOP cfop = Fixture.cfop5102();
		session.save(cfop);
		
		TipoMovimento tipoMovimentoFaltaEm = Fixture.tipoMovimentoFaltaEm();
		TipoMovimento tipoMovimentoRecFisico = Fixture.tipoMovimentoRecebimentoFisico();
		TipoMovimento tipoMovimentoRecReparte = Fixture.tipoMovimentoRecebimentoReparte();
		session.save(tipoMovimentoFaltaEm);
		session.save(tipoMovimentoRecFisico);
		session.save(tipoMovimentoRecReparte);

		TipoNotaFiscal tipoNotaFiscal = Fixture.tipoNotaFiscalRecebimento();
		session.save(tipoNotaFiscal);

		NotaFiscalFornecedor notaFiscalFornecedor = Fixture
				.notaFiscalFornecedor(cfop, juridicaAcme, fornecedorAcme, tipoNotaFiscal,
						usuario);
		session.save(notaFiscalFornecedor);

		ItemNotaFiscal itemNotaFiscal = Fixture.itemNotaFiscal(revistaVeja1,
				usuario, notaFiscalFornecedor, new Date(), BigDecimal.TEN);
		session.save(itemNotaFiscal);

		RecebimentoFisico recebimentoFisico = Fixture.recebimentoFisico(
			notaFiscalFornecedor, usuario, new Date(), new Date(), StatusConfirmacao.CONFIRMADO);
		session.save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico = 
			Fixture.itemRecebimentoFisico(itemNotaFiscal, recebimentoFisico, BigDecimal.TEN);
		session.save(itemRecebimentoFisico);
		
		EstoqueProduto estoqueProduto = Fixture.estoqueProduto(revistaVeja1, BigDecimal.ZERO);
		session.save(estoqueProduto);
		
		MovimentoEstoque movimentoEstoque = Fixture.movimentoEstoque(
				itemRecebimentoFisico, revistaVeja1, tipoMovimentoRecFisico, usuario,
				estoqueProduto, StatusAprovacao.APROVADO);
		session.save(movimentoEstoque);
		session.update(estoqueProduto);
		
		Lancamento lancamento = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				revistaVeja1, new Date(), new Date(), new Date(), new Date(),
				BigDecimal.TEN, StatusLancamento.RECEBIDO, itemRecebimentoFisico);
		session.save(lancamento);

		Estudo estudo = Fixture
				.estudo(BigDecimal.TEN, new Date(), revistaVeja1);
		session.save(estudo);
		
		EstoqueProdutoCota estoqueProdutoCota = Fixture.estoqueProdutoCota(
				revistaVeja1, cotaManoel, BigDecimal.TEN, BigDecimal.ZERO);
		save(session, estoqueProdutoCota);
		
		MovimentoEstoqueCota mec = Fixture.movimentoEstoqueCota(revistaVeja1,
				tipoMovimentoRecReparte, usuario, estoqueProdutoCota,
				BigDecimal.TEN, cotaManoel, StatusAprovacao.APROVADO);
		save(session, mec);
		
		ParametroSistema parametroSistema = Fixture.parametroSistema(1L,
				TipoParametroSistema.PATH_IMAGENS_CAPA,
				"C:\\apache-tomcat-7.0.25\\webapps\\nds-client\\capas\\");
		session.save(parametroSistema);
	}
	
	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
		}
	}

}
