package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.EstudoCotaRepository;

public class EstudoCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	private Date dataReferencia = Fixture.criarData(1, 1, 2012);
	
	private static final Integer NUMERO_COTA = 1;
	
	@Before
	public void setup() {
		
		ProdutoEdicao produtoEdicao = this.criarProdutoEdicao();
		
		Lancamento lancamento = criarLancamento(produtoEdicao);
		
		Estudo estudo = this.criarEstudo(lancamento, produtoEdicao);
		
		Cota cota = this.criarCota();
		
		EstudoCota estudoCota = Fixture.estudoCota(BigDecimal.TEN, BigDecimal.TEN, estudo, cota);
		
		getSession().save(estudoCota);
	}
	
	@Test
	@Ignore
	public void obterEstudoCota() {
		
		EstudoCota estudoCota = this.estudoCotaRepository.obterEstudoCota(NUMERO_COTA, dataReferencia);
		
		Assert.assertNotNull(estudoCota);
		
		Assert.assertEquals(NUMERO_COTA, estudoCota.getCota().getNumeroCota());
		
		//Assert.assertEquals(dataReferencia, estudoCota.getEstudo().getLancamento().getDataLancamentoDistribuidor());
	}
	
	private Cota criarCota() {
		
		Pessoa pessoa = Fixture.juridicaFC();
		
		getSession().save(pessoa);
		
		Cota cota = Fixture.cota(NUMERO_COTA, pessoa, SituacaoCadastro.ATIVO);
		
		getSession().save(cota);
		
		return cota;
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		
		TipoProduto tipoProduto = Fixture.tipoRevista();
		
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		
		getSession().save(produto);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, produto);
		
		getSession().save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	private Lancamento criarLancamento(ProdutoEdicao produtoEdicao) {
		
		Lancamento lancamento = 
			Fixture.lancamento(
				TipoLancamento.LANCAMENTO, produtoEdicao, this.dataReferencia, this.dataReferencia);
		
		getSession().save(lancamento);
		
		return lancamento;
	}
	
	private Estudo criarEstudo(Lancamento lancamento, ProdutoEdicao produtoEdicao) {
		
		Estudo estudo = Fixture.estudo(BigDecimal.TEN, new Date(), produtoEdicao);
		
		getSession().save(estudo);
		
		return estudo;
	}
	
}
