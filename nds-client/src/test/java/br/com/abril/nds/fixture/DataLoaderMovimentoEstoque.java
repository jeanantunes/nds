package br.com.abril.nds.fixture;

import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.StatusConfirmacao;
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
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.OrigemNota;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.movimentacao.MovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.movimentacao.TipoMovimentoEstoque;
import br.com.abril.nds.model.seguranca.Usuario;

public class DataLoaderMovimentoEstoque {

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
		
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setLogin("usuario");
		usuario.setSenha("123");
		session.save(usuario);

		CFOP cfop = new CFOP();
		cfop.setCodigo("010101");
		cfop.setDescricao("default");
		cfop.setId(1L);
		session.save(cfop);
		
		TipoMovimento tipoMovimento = new TipoMovimento();
		tipoMovimento.setAprovacaoAutomatica(true);
		tipoMovimento.setDescricao("ssddgggg");
		tipoMovimento.setId(1L);
		tipoMovimento.setIncideDivida(true);
		tipoMovimento.setTipoMovimentoEstoque(TipoMovimentoEstoque.FALTA_EM);
		session.save(tipoMovimento);
		
		ItemNotaFiscal itemNotaFiscal = new ItemNotaFiscal();
		itemNotaFiscal.setId(1L);
		itemNotaFiscal.setOrigemNota(OrigemNota.MANUAL);
		itemNotaFiscal.setProdutoEdicao(produtoEdicao);
		itemNotaFiscal.setQuantidade(new BigDecimal(1.0));
		itemNotaFiscal.setUsuario(usuario);
		session.save(itemNotaFiscal);
		

		TipoNotaFiscal tipoNotaFiscal = new TipoNotaFiscal();
		tipoNotaFiscal.setDescricao("TIPO NOTA");
		tipoNotaFiscal.setId(1L);
		tipoNotaFiscal.setTipoOperacao(TipoOperacao.ENTRADA);
		session.save(tipoNotaFiscal);
		
		NotaFiscalFornecedor notaFiscalFornecedor = new NotaFiscalFornecedor();
		notaFiscalFornecedor.setId(1L);
		notaFiscalFornecedor.setCfop(cfop);
		notaFiscalFornecedor.setChaveAcesso("11111");
		notaFiscalFornecedor.setDataEmissao(new Date());
		notaFiscalFornecedor.setDataExpedicao(new Date());
		notaFiscalFornecedor.setJuridica(juridicaAcme);
		notaFiscalFornecedor.setNumero("2344242");
		notaFiscalFornecedor.setOrigemNota(OrigemNota.INTERFACE);
		notaFiscalFornecedor.setSerie("345353543");
		notaFiscalFornecedor.setStatusNotaFiscal(StatusNotaFiscal.PENDENTE);
		notaFiscalFornecedor.setTipoNotaFiscal(tipoNotaFiscal);
		notaFiscalFornecedor.setUsuario(usuario);
		session.save(notaFiscalFornecedor);
		
		RecebimentoFisico recebimentoFisico = new RecebimentoFisico();
		recebimentoFisico.setData(new Date());
		recebimentoFisico.setDataConfirmacao(new Date());
		recebimentoFisico.setId(1L);
		recebimentoFisico.setNotaFiscal(notaFiscalFornecedor);
		recebimentoFisico.setStatusConfirmacao(StatusConfirmacao.CONFIRMADO);
		recebimentoFisico.setUsuario(usuario);
		session.save(recebimentoFisico);
		
		ItemRecebimentoFisico itemRecebimentoFisico = new ItemRecebimentoFisico();
		itemRecebimentoFisico.setId(1L);
		itemRecebimentoFisico.setItemNotaFiscal(itemNotaFiscal);
		itemRecebimentoFisico.setQtdeFisico(new BigDecimal(1.0));
		itemRecebimentoFisico.setRecebimentoFisico(recebimentoFisico);
		session.save(itemRecebimentoFisico);
		
		MovimentoEstoque movimentoEstoque = new MovimentoEstoque();
		movimentoEstoque.setId(1L);
		movimentoEstoque.setDataInclusao(new Date());
		movimentoEstoque.setDiferenca(null);
		movimentoEstoque.setItemRecebimentoFisico(itemRecebimentoFisico);
		movimentoEstoque.setProdutoEdicao(produtoEdicao);
		movimentoEstoque.setQtde(new BigDecimal(1.0));
		movimentoEstoque.setTipoMovimento(tipoMovimento);
		movimentoEstoque.setUsuario(usuario);
		session.save(movimentoEstoque);
		
		
	}
	
	private static void save(Session session, Object... entidades) {
		for (Object entidade : entidades) {
			session.save(entidade);
		}
	}
	
}
