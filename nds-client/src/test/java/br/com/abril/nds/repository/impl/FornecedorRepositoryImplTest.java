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
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoFornecedor;
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
	public void obterFornecedoresNaoReferenciadosComCota(){
		List<Fornecedor> lista = fornecedorRepository.obterFornecedoresNaoReferenciadosComCota(1L);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresCota(){
		List<Fornecedor> lista = fornecedorRepository.obterFornecedoresCota(1L);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresAtivos(){
		List<Fornecedor> lista = fornecedorRepository.obterFornecedoresAtivos();
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedores(){
		List<Fornecedor> lista = fornecedorRepository.obterFornecedores();
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresPorCnpj(){
		List<Fornecedor> lista = fornecedorRepository.obterFornecedores("123.456.789-87");
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresDeProdutoPorCodigoProduto() {
		List<Fornecedor> fornecedores =
			fornecedorRepository.obterFornecedoresDeProduto(produto.getCodigo(), null);
		
		Assert.assertTrue(fornecedores.size() == 1);
		Assert.assertTrue(fornecedores.contains(fornecedor1));
	}
	
	@Test
	public void obterFornecedoresDeProdutoPorGrupoFornecedor() {
		List<Fornecedor> fornecedores =
			fornecedorRepository.obterFornecedoresDeProduto(null, GrupoFornecedor.OUTROS);
		
		Assert.assertNotNull(fornecedores);
	}
	
	@Test
	public void obterFornecedoresDeProduto() {
		List<Fornecedor> fornecedores =
			fornecedorRepository.obterFornecedoresDeProduto(null, null);
		
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
	public void obterFornecedoresPorFiltroPorCnpj(){
		FiltroConsultaFornecedorDTO filtro = new FiltroConsultaFornecedorDTO();
		filtro.setCnpj("123.456.789-87");
		List<FornecedorDTO> lista = fornecedorRepository.obterFornecedoresPorFiltro(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresPorFiltroPorNomeFantasia(){
		FiltroConsultaFornecedorDTO filtro = new FiltroConsultaFornecedorDTO();
		filtro.setNomeFantasia("Nome Fantasia");
		List<FornecedorDTO> lista = fornecedorRepository.obterFornecedoresPorFiltro(filtro);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void obterFornecedoresPorFiltroPorRazaoSocial(){
		FiltroConsultaFornecedorDTO filtro = new FiltroConsultaFornecedorDTO();
		filtro.setRazaoSocial("Razao Social");
		List<FornecedorDTO> lista = fornecedorRepository.obterFornecedoresPorFiltro(filtro);
		Assert.assertNotNull(lista);
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
	public void obterFornecedoresIdNomeNulo(){
		fornecedorRepository.obterFornecedoresIdNome(null, null);
	}
	
	@Test
	public void obterFornecedoresIdNomePorSituacaoCadastro(){
		fornecedorRepository.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, null);
	}
	
	@Test
	public void obterFornecedoresIdNomePorInterfaceTrue(){
		fornecedorRepository.obterFornecedoresIdNome(null, true);
	}
	
	@Test
	public void obterFornecedoresIdNomePorInterfaceFalse(){
		fornecedorRepository.obterFornecedoresIdNome(null, false);
	}
	
	@Test
	public void testBuscaFornecedoresLike() {
		
		List<Fornecedor> fornecedores =
			this.fornecedorRepository.obterFornecedorLikeNomeFantasia("din");
		
		Assert.assertNotNull(fornecedores);
		
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQuantidadeFornecedoresPorIdPessoa(){
		Integer qtdade = fornecedorRepository.obterQuantidadeFornecedoresPorIdPessoa(1L, null);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterQuantidadeFornecedoresPorIdPessoaEIdFornecedor(){
		Integer qtdade = fornecedorRepository.obterQuantidadeFornecedoresPorIdPessoa(1L, 1L);
	}
	
	@Test
	public void obterFornecedoresPorIdPessoa(){
		List<Fornecedor> fornecedores =
				this.fornecedorRepository.obterFornecedoresPorIdPessoa(1L);
			
			Assert.assertNotNull(fornecedores);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterFornecedorPorCodigo(){
		Fornecedor fornecedor =  fornecedorRepository.obterFornecedorPorCodigo(1);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterMinCodigoInterfaceDisponivel(){
		Integer minCodigo = fornecedorRepository.obterMinCodigoInterfaceDisponivel();
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
	
	@SuppressWarnings("unused")
	@Test
	public void obterMaxCodigoInterface(){
		Integer maxCodigo = fornecedorRepository.obterMaxCodigoInterface();
	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterEnderecoPrincipal(){
		EnderecoFornecedor endereco = fornecedorRepository.obterEnderecoPrincipal(1L);
	}
}
