package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.fiscal.NCM;
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
	
	private ProdutoEdicao produtoEdicao;
	
	@Before
	public void setup() throws ParseException {
		this.produtoEdicao = this.criarProdutoEdicao();
		
		for (int dia = 1; dia <= 3; dia++) {
			
			Date dataLancamento = this.simpleDateFormat.parse(dia + "/01/2012");
			
			Lancamento lancamento = this.criarLancamento(this.produtoEdicao, dataLancamento);
			
			this.criarEstudo(lancamento, this.produtoEdicao);
		}
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		NCM ncmRevistas = Fixture.ncm(123l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoProduto("grupo teste", GrupoProduto.REVISTA, ncmRevistas, null, 10L);
		
		save(tipoProduto);
		
		Produto produto = Fixture.produto("1", "teste", "teste", PeriodicidadeProduto.SEMANAL, tipoProduto, 5, 5, new Long(10000), TributacaoFiscal.TRIBUTADO);
		produto.addFornecedor(dinap);
		save(produto);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, new Long(10000), BigDecimal.TEN, BigDecimal.TEN, "ABCD", produto, null, false);
		
		save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	private Lancamento criarLancamento(ProdutoEdicao produtoEdicao, Date dataReferencia) {
		
		Lancamento lancamento = 
			Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicao, dataReferencia, 
					dataReferencia, new Date(), new Date(), BigInteger.TEN, StatusLancamento.CONFIRMADO, null, 1);
		
		save(lancamento);
		
		return lancamento;
	}
	
	private Estudo criarEstudo(Lancamento lancamento, ProdutoEdicao produtoEdicao) {
		
		Estudo estudo = Fixture.estudo(BigInteger.valueOf(100), lancamento.getDataLancamentoDistribuidor(), produtoEdicao);
		
		save(estudo);
		
		return estudo;
	}
	
//	TESTE SEM USO DE MASSA
	
	@Test
	public void testarObterEstudoDoLancamentoPorDataProdutoEdicao() {
		
		Estudo estudo;
		
		Calendar data = Calendar.getInstance();
		Date dataReferencia = data.getTime();
		
		Long idProdutoEdicao = 1L;
		
		estudo = estudoRepository.obterEstudoDoLancamentoPorDataProdutoEdicao(dataReferencia, idProdutoEdicao);
		
		Assert.assertNull(estudo);
		
	}

}
