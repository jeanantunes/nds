package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;

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
		
		Fornecedor fornecedorAcme = Fixture.fornecedor(juridicaAcme,
				SituacaoCadastro.ATIVO, false);
		Fornecedor fornecedorDinap = Fixture.fornecedor(juridicaDinap,
				SituacaoCadastro.ATIVO, true);
		Fornecedor fornecedorFc = Fixture.fornecedor(juridicaFc,
				SituacaoCadastro.ATIVO, true);
		save(session, fornecedorAcme, fornecedorDinap, fornecedorFc);
		
		Distribuidor distribuidor = Fixture.distribuidor(juridicaDistrib, new Date());
		save(session, distribuidor);
		
		DistribuicaoFornecedor dinapSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEGUNDA_FEIRA);
		DistribuicaoFornecedor dinapQuarta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.QUARTA_FEIRA);
		DistribuicaoFornecedor dinapSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorDinap, DiaSemana.SEXTA_FEIRA);
		save(session, dinapSegunda, dinapQuarta, dinapSexta);
		
		DistribuicaoFornecedor fcSegunda = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEGUNDA_FEIRA);
		DistribuicaoFornecedor fcSexta = Fixture.distribuicaoFornecedor(
				distribuidor, fornecedorFc, DiaSemana.SEXTA_FEIRA);
		save(session, fcSegunda, fcSexta);

		TipoProduto tipoProduto = Fixture.tipoProduto("Revista",
				GrupoProduto.REVISTA, "99000642");
		session.save(tipoProduto);

		Produto produto = Fixture.produto("1", 
				"Revista Veja", "Veja", PeriodicidadeProduto.SEMANAL,
				tipoProduto);
		produto.getFornecedores().add(fornecedorAcme);
		session.save(produto);
		ProdutoEdicao produtoEdicao = Fixture.produtoEdicao(1L, 10, 14,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20),
				produto);
		session.save(produtoEdicao);
	}
	
	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
		}
	}

}
