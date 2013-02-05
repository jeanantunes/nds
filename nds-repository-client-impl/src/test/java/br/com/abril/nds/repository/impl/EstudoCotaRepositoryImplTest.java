package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;

public class EstudoCotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EstudoCotaRepositoryImpl estudoCotaRepository;
	
	private Date dataReferencia = Fixture.criarData(1, 1, 2012);
	
	private static final Integer NUMERO_COTA = 1;
	
	@Before
	public void setup() {
		
		ProdutoEdicao produtoEdicao = this.criarProdutoEdicao();
		
		Lancamento lancamento = criarLancamento(produtoEdicao);
		
		Estudo estudo = this.criarEstudo(lancamento, produtoEdicao);
		
		Cota cota = this.criarCota();
		
		EstudoCota estudoCota = Fixture.estudoCota(BigInteger.TEN, BigInteger.TEN, estudo, cota);
		
		save(estudoCota);
	}
	
	@Test
	public void obterEstudoCota() {
		
		EstudoCota estudoCota = this.estudoCotaRepository.obterEstudoCota(NUMERO_COTA, dataReferencia);
		
		Assert.assertNotNull(estudoCota);
		
		Assert.assertEquals(NUMERO_COTA, estudoCota.getCota().getNumeroCota());
		
		Assert.assertTrue(dataReferencia.before(estudoCota.getEstudo().getDataLancamento()));
	}
	
	private Cota criarCota() {
		Box box300Reparte = Fixture.boxReparte300();
		save(box300Reparte);
		
		Pessoa pessoa = Fixture.juridicaFC();
		
		save(pessoa);
		
		Cota cota = Fixture.cota(NUMERO_COTA, pessoa, SituacaoCadastro.ATIVO, box300Reparte);
		
		save(cota);
		
		return cota;
	}
	
	private ProdutoEdicao criarProdutoEdicao() {
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		TipoFornecedor tipoFornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		save(tipoFornecedorPublicacao);
		
		Fornecedor dinap = Fixture.fornecedorDinap(tipoFornecedorPublicacao);
		save(dinap);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		
		save(tipoProduto);
		
		Produto produto = Fixture.produtoVeja(tipoProduto);
		produto.addFornecedor(dinap);
		produto.setEditor(abril);
		save(produto);
		
		ProdutoEdicao produtoEdicao = 
			Fixture.produtoEdicao(1L, 1, 1, new Long(10000), BigDecimal.TEN, BigDecimal.TEN, "ABCDEFGHIJKLMNOPQ", produto, null, false);
		
		save(produtoEdicao);
		
		return produtoEdicao;
	}
	
	private Lancamento criarLancamento(ProdutoEdicao produtoEdicao) {
		
		Lancamento lancamento = 
			Fixture.lancamento(
				TipoLancamento.LANCAMENTO, 
				produtoEdicao, 
				this.dataReferencia, 
				this.dataReferencia, 
				new Date(), 
				new Date(), 
				BigInteger.TEN,
				StatusLancamento.CONFIRMADO, null, 1);
		
		save(lancamento);
		
		return lancamento;
	}
	
	private Estudo criarEstudo(Lancamento lancamento, ProdutoEdicao produtoEdicao) {
		
		Estudo estudo = Fixture.estudo(BigInteger.TEN, new Date(), produtoEdicao);
		
		save(estudo);
		
		
		return estudo;
	}
	
	@Test
	public void obterEstudoCotaPorDataProdutoEdicaoDataLancamento() {
		
		Date dataDeLancamento = Fixture.criarData(29, Calendar.OCTOBER, 2012);
		
		List<EstudoCotaDTO> estudoCotas = estudoCotaRepository.obterEstudoCotaPorDataProdutoEdicao(dataDeLancamento, null);
		
		Assert.assertNotNull(estudoCotas);
		
	}
	
	@Test
	public void obterEstudoCotaPorDataProdutoEdicaoIdProdutoEdicao() {
		Long idProdutoEdicao = 1L;
				
		List<EstudoCotaDTO> estudoCotas = estudoCotaRepository.obterEstudoCotaPorDataProdutoEdicao(null, idProdutoEdicao);
		
		Assert.assertNotNull(estudoCotas);
		
	}
	
	@Test
	public void obterEstudoCotaDataLancamento() {
		Date dataDeLancamento = Fixture.criarData(29, Calendar.OCTOBER, 2012);
				
		EstudoCota estudoCotas = estudoCotaRepository.obterEstudoCota(dataDeLancamento, null, null);
		
			
	}
	
	@Test
	public void obterEstudoCotaIdProdutoEdicao() {
		Long idProdutoEdicao = 1L;
				
		EstudoCota estudoCotas = estudoCotaRepository.obterEstudoCota(null, idProdutoEdicao, null);
		
			
	}
	
	@Test
	public void obterEstudoCotaIdCota() {
		Long idCota = 1L;
						
		EstudoCota estudoCotas = estudoCotaRepository.obterEstudoCota(null, null, idCota);
		
			
	}
	
	@Test
	public void obterEstudoCotaDeLancamentoComEstudoFechadoDataLancamentoDistribuidor() {
		Date dataLancamentoDistribuidor = Fixture.criarData(29, Calendar.OCTOBER, 2012);
						
		EstudoCota estudoCotas = 
		estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(dataLancamentoDistribuidor, null, null);
		
			
	}
	
	@Test
	public void obterEstudoCotaDeLancamentoComEstudoFechadoIdProdutoEdicao() {
		Long idProdutoEdicao = 1L;
						
		EstudoCota estudoCotas = 
		estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(null, idProdutoEdicao, null);
		
			
	}
	
	@Test
	public void obterEstudoCotaDeLancamentoComEstudoFechadoNumeroCota() {
		Integer numeroCota = 1;
						
		EstudoCota estudoCotas = 
		estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(null, null, numeroCota);
		
			
	}

}
