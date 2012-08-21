package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.model.fiscal.NCM;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Testes unitários para o repositório referentes aos descontos de produto.
 * 
 * @author Discover Technology
 *
 */
public class DescontoProdutoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private DescontoProdutoRepository descontoProdutoRepository;
	
	private DescontoProduto descontoProdutoVeja;
	
	private DescontoProduto descontoProdutoQuatroRodas;
	
	private DescontoProduto descontoProdutoInfoExame;
	
	private DescontoProduto descontoProdutoCapricho;
	
	private DescontoProduto descontoProdutoSuperInteressante;
	
	private static Cota cota;
	
	@Before
	public void setup() {
		
		PessoaJuridica abril = Fixture.juridicaAbril();
		
		Distribuidor distribuidor = Fixture.distribuidor(1, abril, new Date(), null);
		
		NCM ncm = Fixture.ncm(1L, "ncm", "m");

		Usuario usuario = Fixture.usuarioJoao();

		TipoProduto tipoProduto = Fixture.tipoCromo(ncm);
		
		save(abril, distribuidor, ncm, usuario, tipoProduto);

		/* Produto de código 1 */
		Produto produtoVeja = Fixture.produtoVeja(tipoProduto);
		
		ProdutoEdicao produtoEdicaoVeja = Fixture.produtoEdicao(
			"123", 2L, 10, 10, new Long(100), new BigDecimal(10), 
			new BigDecimal(10), "55201", 1L, produtoVeja, new BigDecimal(10), false
		);
		
		Set<Cota> cotas = new LinkedHashSet<Cota>();
		
		cota = Fixture.cota(123, abril, SituacaoCadastro.ATIVO, null);
		Cota cota1 = Fixture.cota(123, abril, SituacaoCadastro.ATIVO, null);
		Cota cota2 = Fixture.cota(1234, abril, SituacaoCadastro.ATIVO, null);
		Cota cota3 = Fixture.cota(12345, abril, SituacaoCadastro.ATIVO, null);
		
		save(cota, cota1, cota2, cota3);
		
		cotas.add(cota);
		cotas.add(cota1);
		cotas.add(cota2);
		cotas.add(cota3);
		
		DescontoProduto descontoProdutoVeja = new DescontoProduto();
		descontoProdutoVeja.setCotas(cotas);
		descontoProdutoVeja.setDataAlteracao(new Date());
		descontoProdutoVeja.setDesconto(new BigDecimal(50));
		descontoProdutoVeja.setDistribuidor(distribuidor);
		descontoProdutoVeja.setProdutoEdicao(produtoEdicaoVeja);
		descontoProdutoVeja.setUsuario(usuario);
		
		save(produtoVeja, produtoEdicaoVeja, descontoProdutoVeja);
		
		this.descontoProdutoVeja = merge(descontoProdutoVeja);
		
		/* Produto de código 2 */
		Produto produtoQuatroRodas = Fixture.produtoQuatroRodas(tipoProduto);
		
		ProdutoEdicao produtoEdicaoQuatroRodas = Fixture.produtoEdicao(
			"123", 2L, 10, 10, new Long(100), new BigDecimal(10), 
			new BigDecimal(10), "55201", 1L, produtoQuatroRodas, new BigDecimal(10), false
		);

		DescontoProduto descontoProdutoQuatroRodas = new DescontoProduto();
		descontoProdutoQuatroRodas.setDataAlteracao(new Date());
		descontoProdutoQuatroRodas.setDesconto(new BigDecimal(50));
		descontoProdutoQuatroRodas.setDistribuidor(distribuidor);
		descontoProdutoQuatroRodas.setProdutoEdicao(produtoEdicaoQuatroRodas);
		descontoProdutoQuatroRodas.setUsuario(usuario);
		
		save(produtoQuatroRodas, produtoEdicaoQuatroRodas, descontoProdutoQuatroRodas);
		
		/* Produto de código 3 */
		Produto produtoInfoExame = Fixture.produtoInfoExame(tipoProduto);
		
		ProdutoEdicao produtoEdicaoInfoExame = Fixture.produtoEdicao(
			"123", 2L, 10, 10, new Long(100), new BigDecimal(10), 
			new BigDecimal(10), "55201", 1L, produtoInfoExame, new BigDecimal(10), false
		);
		
		DescontoProduto descontoProdutoInfoExame = new DescontoProduto();
		descontoProdutoInfoExame.setDataAlteracao(new Date());
		descontoProdutoInfoExame.setDesconto(new BigDecimal(50));
		descontoProdutoInfoExame.setDistribuidor(distribuidor);
		descontoProdutoInfoExame.setProdutoEdicao(produtoEdicaoInfoExame);
		descontoProdutoInfoExame.setUsuario(usuario);
		
		save(produtoInfoExame, produtoEdicaoInfoExame, descontoProdutoInfoExame);
		
		/* Produto de código 4 */
		Produto produtoCapricho = Fixture.produtoCapricho(tipoProduto);
		
		ProdutoEdicao produtoEdicaoCapricho = Fixture.produtoEdicao(
			"123", 2L, 10, 10, new Long(100), new BigDecimal(10), 
			new BigDecimal(10), "55201", 1L, produtoCapricho, new BigDecimal(10), false
		);
		
		DescontoProduto descontoProdutoCapricho = new DescontoProduto();
		descontoProdutoCapricho.setDataAlteracao(new Date());
		descontoProdutoCapricho.setDesconto(new BigDecimal(50));
		descontoProdutoCapricho.setDistribuidor(distribuidor);
		descontoProdutoCapricho.setProdutoEdicao(produtoEdicaoCapricho);
		descontoProdutoCapricho.setUsuario(usuario);
		
		save(produtoCapricho, produtoEdicaoCapricho, descontoProdutoCapricho);
		
		/* Produto de código 5 */
		Produto produtoSuperInteressante = Fixture.produtoSuperInteressante(tipoProduto);
		
		ProdutoEdicao produtoEdicaoSuperInteressante = Fixture.produtoEdicao(
			"123", 2L, 10, 10, new Long(100), new BigDecimal(10), 
			new BigDecimal(10), "55201", 1L, produtoSuperInteressante, new BigDecimal(10), false
		);
		
		DescontoProduto descontoProdutoSuperInteressante = new DescontoProduto();
		descontoProdutoSuperInteressante.setDataAlteracao(new Date());
		descontoProdutoSuperInteressante.setDesconto(new BigDecimal(50));
		descontoProdutoSuperInteressante.setDistribuidor(distribuidor);
		descontoProdutoSuperInteressante.setProdutoEdicao(produtoEdicaoSuperInteressante);
		descontoProdutoSuperInteressante.setUsuario(usuario);
		
		save(produtoSuperInteressante, produtoEdicaoSuperInteressante, descontoProdutoSuperInteressante);
	}

	@Test
	public void buscarTipoDescontoProdutoSucesso() {
		
		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
		
		filtro.setCodigoProduto("1");

		List<TipoDescontoProdutoDTO> listaTipoDescontoProduto =
				this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
		
		Assert.assertNotNull(listaTipoDescontoProduto);

		int expectedSizeList = 1;
		int actualSizeList = listaTipoDescontoProduto.size();
		
		Assert.assertEquals(expectedSizeList, actualSizeList);
	}
	
	@Test
	public void buscarTipoDescontoProdutoPaginadoOrdenadoSucesso() {
		
		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO(1, 3, "asc");
		
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.PRODUTO);

		List<TipoDescontoProdutoDTO> listaTipoDescontoProduto =
				this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
		
		Assert.assertNotNull(listaTipoDescontoProduto);

		int expectedSizeList = 3;
		int actualSizeList = listaTipoDescontoProduto.size();
		
		Assert.assertEquals(expectedSizeList, actualSizeList);
		
		String nomeProdutoAnterior = "";
		
		for (int i = 0; i < listaTipoDescontoProduto.size(); i++) {

			TipoDescontoProdutoDTO tipoDescontoProduto = listaTipoDescontoProduto.get(i);

			Assert.assertTrue(nomeProdutoAnterior.compareTo(tipoDescontoProduto.getNomeProduto()) < 0);

			nomeProdutoAnterior = tipoDescontoProduto.getNomeProduto();
		}
	}
	
	@Test
	public void buscarTipoDescontoProdutoPaginadoOrdenadoPorCodigoSucesso() {
		
		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO(1, 3, "desc");
		
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.CODIGO_PRODUTO);

		List<TipoDescontoProdutoDTO> listaTipoDescontoProduto =
				this.descontoProdutoRepository.buscarTipoDescontoProduto(filtro);
		
		Assert.assertNotNull(listaTipoDescontoProduto);

		int expectedSizeList = 3;
		int actualSizeList = listaTipoDescontoProduto.size();
		
		Assert.assertEquals(expectedSizeList, actualSizeList);
		
		String codigoProdutoAnterior = "6";
		
		for (int i = 0; i < listaTipoDescontoProduto.size(); i++) {

			TipoDescontoProdutoDTO tipoDescontoProduto = listaTipoDescontoProduto.get(i);

			int compareResult = codigoProdutoAnterior.compareTo(tipoDescontoProduto.getCodigoProduto());
			
			Assert.assertTrue(compareResult > 0);

			codigoProdutoAnterior = tipoDescontoProduto.getCodigoProduto();
		}
	}
	
	@Test
	public void buscarQuantidadeTipoDescontoProduto() {

		Integer quantidadeTiposDescontoProduto = 
				this.descontoProdutoRepository.buscarQuantidadeTipoDescontoProduto(null);
		
		Assert.assertNotNull(quantidadeTiposDescontoProduto);
		
		int expected = 5;
		
		Assert.assertEquals(expected, quantidadeTiposDescontoProduto.intValue());
	}
	
	@Test
	public void obterCotasDoTipoDescontoProduto() {
		
		Ordenacao ordenacao = Ordenacao.DESC;
		
		List<CotaDescontoProdutoDTO> cotas = 
				this.descontoProdutoRepository.obterCotasDoTipoDescontoProduto(this.descontoProdutoVeja.getId(), ordenacao);

		int expectedSize = 4;
		int actualSize = cotas.size();
		
		Assert.assertNotNull(cotas);
		Assert.assertEquals(expectedSize, actualSize);
	}
	
	@Test
	public void obterTipoDescontoProdutoPorCota() {
		
		List<TipoDescontoProdutoDTO> descontosProduto = 
				this.descontoProdutoRepository.obterTiposDescontoProdutoPorCota(cota.getId(),"desc","dataAlteracao");

		int expectedSize = 1;
		int actualSize = descontosProduto.size();
		
		Assert.assertNotNull(descontosProduto);
		Assert.assertEquals(expectedSize, actualSize);
	}
}
