package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoImportacaoArquivo;

public class ImportacaoDeArquivoMatrizStrategyTest extends AbstractRepositoryImplTest {

	@Autowired
	private ImportacaoArquivoService importacaoArquivoService;
	
	@Autowired
	private ImportacaoDeArquivoMatrizStrategy arquivoMatrizStrategy;
	
	private Produto produtoVeja;
	private ProdutoEdicao produtoEdicaoVeja1;
	
	@Before
	public void setup() {
			
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		NCM ncmCromo = Fixture.ncm(48205000l,"CROMO","KG");
		save(ncmCromo);
		
		TipoProduto tipoProdutoRevista = Fixture.tipoRevista(ncmRevistas);
		save(tipoProdutoRevista);
		
		Editor abril = Fixture.editoraAbril();
		save(abril);
		
		produtoVeja = Fixture.produtoVeja(tipoProdutoRevista);
		produtoVeja.setEditor(abril);
		save(produtoVeja);		
				
		produtoEdicaoVeja1 = Fixture.produtoEdicao("1", 1L, 10, 14,
				BigDecimal.ONE, BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQ", 1L,
				produtoVeja, null, false);
		
		save(produtoEdicaoVeja1);	
	}
	
	@Test
	public void testeImportacaoMatrizComErro(){
		
		File arquivo  = new File ("src/test/resources/importacao/MATRIZ.NEW");
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.MATRIZ);
		
		Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		Assert.assertTrue(!retornoImportacaoArquivoVO.isSucessoNaImportacao());
	}
	
	@Test
	public void testeImportacaoMatrizComErroEstruturaArquivo(){
		
		File arquivo  = new File ("src/test/resources/importacao/MATRIZ_ERRO_ESTRUTURA.NEW");
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.MATRIZ);
		
		Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		Assert.assertTrue(retornoImportacaoArquivoVO.getMotivoDoErro().toString(),!retornoImportacaoArquivoVO.isSucessoNaImportacao());
		
	}
	
	@Test
	public void testeProcessarInclusaoLancamento(){
		
		EMS0108Input input = new EMS0108Input();
		input.setCodigoPublicacao(Long.parseLong(produtoVeja.getCodigo()));
		input.setEdicao(produtoEdicaoVeja1.getNumeroEdicao());
		input.setDataLancamentoRecolhimentoProduto(DateUtil.adicionarDias(new Date(), 10));
		
		arquivoMatrizStrategy.processarImportacaoDados(input);
	}
	
	@Test
	public void testeProcessarInclusaoLancamentoRecolhimento(){
		
	   Lancamento lancamento =	 Fixture.lancamento(TipoLancamento.LANCAMENTO, produtoEdicaoVeja1,
												   DateUtil.adicionarDias(new Date(), -5 ), 
												   DateUtil.adicionarDias(new Date(), -5 ),
												   DateUtil.adicionarDias(new Date(), -10 ), 
												   DateUtil.adicionarDias(new Date(), -10 ), 
												   BigInteger.ZERO, 
												   StatusLancamento.PLANEJADO, null, null);
		
		save(lancamento);
		
		EMS0108Input input = new EMS0108Input();
		input.setCodigoPublicacao(Long.parseLong(produtoVeja.getCodigo()));
		input.setEdicao(produtoEdicaoVeja1.getNumeroEdicao());
		input.setDataLancamentoRecolhimentoProduto(DateUtil.adicionarDias(new Date(), -5));
		input.setEdicaoRecolhimento(produtoEdicaoVeja1.getNumeroEdicao());
		
		arquivoMatrizStrategy.processarImportacaoDados(input);
	}
	
}
