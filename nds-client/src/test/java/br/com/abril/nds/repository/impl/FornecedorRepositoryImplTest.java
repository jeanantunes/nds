package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFornecedorDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoFornecedor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.vo.PaginacaoVO;

public class FornecedorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private FornecedorRepository fornecedorRepository;

	private Fornecedor fornecedor1;
	private Fornecedor fornecedor2;
	private Fornecedor fornecedor3;
	private Produto produto;
	private Editor abril;

	@Before
	public void setUp() {
		abril = Fixture.editoraAbril();
		save(abril);
		TipoFornecedor fornecedorPublicacao = Fixture.tipoFornecedorPublicacao();
		TipoFornecedor fornecedorOutros = Fixture.tipoFornecedorOutros();
		save(fornecedorPublicacao, fornecedorOutros);
		
		fornecedor1 = Fixture.fornecedorFC(fornecedorPublicacao);
		fornecedor1.setCodigoInterface(31);
		fornecedor2 = Fixture.fornecedorAcme(fornecedorOutros);
		fornecedor2.setCodigoInterface(32);
		fornecedor3 = Fixture.fornecedorDinap(fornecedorPublicacao);
		fornecedor3.setSituacaoCadastro(SituacaoCadastro.PENDENTE);
		fornecedor3.setCodigoInterface(30);
		save(fornecedor1, fornecedor2, fornecedor3);
		
		NCM ncmRevistas = Fixture.ncm(49029000l,"REVISTAS","KG");
		save(ncmRevistas);
		
		TipoProduto tipoProduto = Fixture.tipoRevista(ncmRevistas);
		save(tipoProduto);
		
		produto = Fixture.produtoCapricho(tipoProduto);
		produto.addFornecedor(fornecedor1);
		produto.addFornecedor(fornecedor3);
		produto.setEditor(abril);
		save(produto);
	}

	@Test
	public void obterFornecedoresDeProduto() {
		List<Fornecedor> fornecedores =
			fornecedorRepository.obterFornecedoresDeProduto(produto.getCodigo(), null);
		
		Assert.assertTrue(fornecedores.size() == 1);
		Assert.assertTrue(fornecedores.contains(fornecedor1));
	}
	
	@Test
	public void obterFornecedoresPermitemBalanceamento() {
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		Assert.assertEquals(1, fornecedores.size());
		Assert.assertTrue(fornecedores.contains(fornecedor1));
	}
	
	@Test
	public void obterFornecedoresNaoPermitemBalanceamento() {
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedores(
				false, SituacaoCadastro.ATIVO);
		Assert.assertEquals(1, fornecedores.size());
		Assert.assertTrue(fornecedores.contains(fornecedor2));
	}
	
	@Test
	public void obterFornecedoresPorFiltroOrderByCNPJ() {

		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();

		filtroConsultaFornecedor.setColunaOrdenacao(FiltroConsultaFornecedorDTO.ColunaOrdenacao.CNPJ);

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		int expectedSize = 3;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		FornecedorDTO fornecedor = listaFornecedores.get(0);
		Assert.assertEquals(fornecedor1.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(1);
		Assert.assertEquals(fornecedor3.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(2);
		Assert.assertEquals(fornecedor2.getId(), fornecedor.getIdFornecedor());
	}		
	
	@Test
	public void obterFornecedoresPorFiltroOrderByCodigo() {

		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();

		filtroConsultaFornecedor.setColunaOrdenacao(FiltroConsultaFornecedorDTO.ColunaOrdenacao.CODIGO);

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		int expectedSize = 3;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		FornecedorDTO fornecedor = listaFornecedores.get(0);
		Assert.assertEquals(fornecedor3.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(1);
		Assert.assertEquals(fornecedor1.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(2);
		Assert.assertEquals(fornecedor2.getId(), fornecedor.getIdFornecedor());
	}		

	@Test
	public void obterFornecedoresPorFiltroOrderByEmail() {

		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();

		filtroConsultaFornecedor.setColunaOrdenacao(FiltroConsultaFornecedorDTO.ColunaOrdenacao.EMAIL);

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		int expectedSize = 3;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		FornecedorDTO fornecedor = listaFornecedores.get(0);
		Assert.assertEquals(fornecedor2.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(1);
		Assert.assertEquals(fornecedor3.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(2);
		Assert.assertEquals(fornecedor1.getId(), fornecedor.getIdFornecedor());
	}

	@Test
	public void obterFornecedoresPorFiltroOrderByRazaoSocial() {

		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();

		filtroConsultaFornecedor.setColunaOrdenacao(FiltroConsultaFornecedorDTO.ColunaOrdenacao.RAZAO_SOCIAL);

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		int expectedSize = 3;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		FornecedorDTO fornecedor = listaFornecedores.get(0);
		Assert.assertEquals(fornecedor2.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(1);
		Assert.assertEquals(fornecedor3.getId(), fornecedor.getIdFornecedor());
		
		fornecedor = listaFornecedores.get(2);
		Assert.assertEquals(fornecedor1.getId(), fornecedor.getIdFornecedor());
	}

	@Test
	public void obterFornecedoresPorFiltroPaginado() {

		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();

		List<FornecedorDTO> listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		int expectedSize = 3;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		PaginacaoVO paginacao = new PaginacaoVO(1, 2, "asc");
		
		filtroConsultaFornecedor.setPaginacao(paginacao);
		
		listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		expectedSize = 2;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
		
		paginacao = new PaginacaoVO(1, 1, "asc");
		
		filtroConsultaFornecedor.setPaginacao(paginacao);
		
		listaFornecedores = 
				this.fornecedorRepository.obterFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		expectedSize = 1;
		
		Assert.assertNotNull(listaFornecedores);
		Assert.assertEquals(expectedSize, listaFornecedores.size());
	}
	
	@Test
	public void obterContagemFornecedoresPorFiltro() {
		
		FiltroConsultaFornecedorDTO filtroConsultaFornecedor = new FiltroConsultaFornecedorDTO();
		
		Long contagemFornecedores = 
				this.fornecedorRepository.obterContagemFornecedoresPorFiltro(filtroConsultaFornecedor);
		
		Long expected = 3L;
		
		Assert.assertNotNull(contagemFornecedores);
		Assert.assertEquals(expected, contagemFornecedores);
	}
	
	
	@Test
	public void obterFornecedoresIdNome(){
		fornecedorRepository.obterFornecedoresIdNome(null, null);
		fornecedorRepository.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, null);
		fornecedorRepository.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true);
		fornecedorRepository.obterFornecedoresIdNome(null, false);
	}
	
	@Test
	public void testBuscaFornecedoresLike() {
		
		List<Fornecedor> fornecedores =
			this.fornecedorRepository.obterFornecedorLikeNomeFantasia("din");
		
		Assert.assertNotNull(fornecedores);
		
	}
	
	@Test
	public void obterFornecedoresPorId() {
		
		List<Long> idsForncedores = new ArrayList<Long>();
		
		idsForncedores.add(fornecedor1.getId());
		idsForncedores.add(fornecedor2.getId());
		idsForncedores.add(fornecedor3.getId());
		
		List<Fornecedor> fornecedores =
			this.fornecedorRepository.obterFornecedoresPorId(idsForncedores);
		
		Assert.assertNotNull(fornecedores);
		
		Assert.assertEquals(idsForncedores.size(), fornecedores.size());
	}
	
	@Test
	public void obterMaxCodigoInterface(){
		this.fornecedorRepository.obterMaxCodigoInterface();
	}
	
}
