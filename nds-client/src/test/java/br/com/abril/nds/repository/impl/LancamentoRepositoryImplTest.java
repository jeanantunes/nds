package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class LancamentoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private LancamentoRepositoryImpl lancamentoRepository;
	
	private Lancamento lancamento1;

	@Before
	public void setUp() {
		PessoaJuridica juridica = Fixture.pessoaJuridica("FC", "00.000.000/0001-00", "000.000.000.000",  "fc@mail.com");
		Fornecedor fornecedor = Fixture.fornecedor(juridica, SituacaoCadastro.ATIVO, true);
		
		TipoProduto tipoRevista = Fixture.tipoProduto("Revistas",
				GrupoProduto.REVISTA, "99000642");
		Produto veja = Fixture.produto("Revista Veja", "Veja",
				PeriodicidadeProduto.SEMANAL, tipoRevista);
		veja.getFornecedores().add(fornecedor);
		ProdutoEdicao veja001 = Fixture.produtoEdicao(10, 7,
				new BigDecimal(0.1), BigDecimal.TEN, new BigDecimal(20), veja);
		lancamento1 = Fixture.lancamento(TipoLancamento.LANCAMENTO,
				veja001, new Date(), new Date());
		save(juridica, fornecedor, tipoRevista, veja, veja001, lancamento1);
	}

	@Test
	public void obterLancamentosBalanceamentoMatriz() {
		List<Lancamento> lancamentos = lancamentoRepository
				.obterLancamentosBalanceamentoMartriz(new Date(), new Date(),
						null);
		Assert.assertNotNull(lancamentos);
		Assert.assertEquals(1, lancamentos.size());
	}

}
