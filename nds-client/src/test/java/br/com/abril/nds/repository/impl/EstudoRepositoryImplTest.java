package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.util.Constantes;

public class EstudoRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
	
	private Date dataReferencia;
	
	private ProdutoEdicao produtoEdicao;
	
	@Before
	public void setup() throws ParseException {

		this.dataReferencia = this.simpleDateFormat.parse("01/01/2012");
		
		this.produtoEdicao = this.criarProdutoEdicao();
		
		for (int dia = 1; dia <= 3; dia++) {
			
			Date dataLancamento = this.simpleDateFormat.parse(dia + "/01/2012");
			
			Lancamento lancamento = this.criarLancamento(this.produtoEdicao, dataLancamento);
			
			this.criarEstudo(lancamento, this.produtoEdicao);
		}
	}
	
	@Test
	public void obterEstudoDoLancamentoMaisProximo() {
		
		Estudo estudo =
			this.estudoRepository.obterEstudoDoLancamentoMaisProximo(
				this.dataReferencia, this.produtoEdicao.getProduto().getCodigo(), this.produtoEdicao.getNumeroEdicao());
		
		Assert.assertNotNull(estudo);
		
		//Assert.assertEquals(dataReferencia, estudo.getLancamento().getDataLancamentoDistribuidor());
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		
		Fornecedor dinap = Fixture.fornecedorDinap();
		getSession().save(dinap);
		
		TipoProduto tipoProduto = Fixture.tipoProduto("grupo teste", GrupoProduto.REVISTA, "ncm");
		
		getSession().save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "teste", "teste", PeriodicidadeProduto.SEMANAL, tipoProduto);
		produto.addFornecedor(dinap);
		getSession().save(produto);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, produto);
		
		getSession().save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	private Lancamento criarLancamento(ProdutoEdicao produtoEdicao, Date dataReferencia) {
		
		Lancamento lancamento = 
			Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataReferencia, 
					dataReferencia, new Date(), new Date(), BigDecimal.TEN, StatusLancamento.RECEBIDO, null);
		
		getSession().save(lancamento);
		
		return lancamento;
	}
	
	private Estudo criarEstudo(Lancamento lancamento, ProdutoEdicao produtoEdicao) {
		
		Estudo estudo = Fixture.estudo(new BigDecimal(100), lancamento.getDataLancamentoDistribuidor(), produtoEdicao);
		
		getSession().save(estudo);
		
		return estudo;
	}

}
