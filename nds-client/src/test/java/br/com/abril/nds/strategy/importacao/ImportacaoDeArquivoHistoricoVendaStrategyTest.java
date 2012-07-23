package br.com.abril.nds.strategy.importacao;

import java.io.File;
import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.impl.AbstractRepositoryImplTest;
import br.com.abril.nds.service.ImportacaoArquivoService;
import br.com.abril.nds.service.vo.RetornoImportacaoArquivoVO;
import br.com.abril.nds.strategy.importacao.input.HistoricoVendaInput;
import br.com.abril.nds.util.TipoImportacaoArquivo;

public class ImportacaoDeArquivoHistoricoVendaStrategyTest extends AbstractRepositoryImplTest {

	@Autowired
	private ImportacaoArquivoService importacaoArquivoService;
	
	@Autowired
	private ImportacaoDeArquivoHistoricoVendaStrategy arquivoHistoricoVendaStrategy;
	
	private Produto produtoVeja;
	private ProdutoEdicao produtoEdicaoVeja1;

	private Cota cotaManoel;
	
	@Before
	public void setup() {
		
		Usuario usuario = Fixture.usuarioJoao();
		save(usuario);
		
		PessoaFisica manoel = Fixture.pessoaFisica("123.456.789-00",
				"manoel@mail.com", "Manoel da Silva");
		save(manoel);
		
		Box box1 = Fixture.criarBox("Box-1", "BX-001", TipoBox.LANCAMENTO, false);
		save(box1);
		
		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);
		
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
				BigDecimal.ONE, BigDecimal.TEN, new BigDecimal(20), "ABCDEFGHIJKLMNOPQRSTU", 1L,
				produtoVeja, null, false);
		
		save(produtoEdicaoVeja1);	
	}
	
	@Test
	public void testeImportacaoHistorivoVendaComErro(){
		
		File arquivo  = new File ("src/test/resources/importacao/HVCT.txt");
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.HISTORICO_VENDA);
		
		Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		Assert.assertTrue(!retornoImportacaoArquivoVO.isSucessoNaImportacao());
	}
	
	@Test
	public void testeImportacaoHistoricoVendaComErroEstruturaArquivo(){
		
		File arquivo  = new File ("src/test/resources/importacao/HVCT_ERRO_ESTRUTURA.txt");
		
		RetornoImportacaoArquivoVO retornoImportacaoArquivoVO  = importacaoArquivoService.processarImportacaoArquivo(arquivo, TipoImportacaoArquivo.HISTORICO_VENDA);
		
		Assert.assertNotNull(retornoImportacaoArquivoVO);
		
		Assert.assertTrue(retornoImportacaoArquivoVO.getMotivoDoErro().toString(),!retornoImportacaoArquivoVO.isSucessoNaImportacao());
		
	}
	
	@Test
	public void testeProcessarInclusaoLancamento(){
		
		HistoricoVendaInput input = new HistoricoVendaInput();
		
		input.setCodigoProduto(Integer.parseInt(produtoVeja.getCodigo()));
		input.setNumeroCota(cotaManoel.getNumeroCota());
		input.setNumeroEdicao(produtoEdicaoVeja1.getNumeroEdicao().intValue());
		input.setQuantidadeDevolvidaProduto(10);
		input.setQuantidadeRecebidaProduto(5);
		try {
		arquivoHistoricoVendaStrategy.processarImportacaoDados(input);
		}catch(Exception e) { 
			e.printStackTrace();
		}
	}
}
