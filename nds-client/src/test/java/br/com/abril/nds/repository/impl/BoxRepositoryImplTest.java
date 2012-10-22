package br.com.abril.nds.repository.impl;

import java.util.List;

import junit.framework.Assert;

import org.hibernate.Criteria;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class BoxRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private BoxRepository boxRepository;

	private static final String[] campos = { "codigo", "tipoBox", "nome" };

	private Box box;
	private Roteiro roteiro;
	private Rota rota;
	private Roteirizacao roteirizacao;

	@Before
	public void setUp() throws Exception {

		for (int i = 0; i < 100; i++) {
			box = Fixture.criarBox(i, "BX-" + i, TipoBox.LANCAMENTO);
			save(box);
		}
		
		for (int i = 0; i < 3; i++) {
			box = Fixture.criarBox(i+100, "RECBX-" + i, TipoBox.ENCALHE);
			save(box);
		}
		
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("FC",
				"01.001.001/001-00", "000.000.000.00", "fc@mail.com", "99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39",
				"joao@gmail.com", "João da Silva");
		save(pessoaFisica);

		Cota cotaF = Fixture
				.cota(1, pessoaFisica, SituacaoCadastro.ATIVO, box);
		cotaF.setSugereSuspensao(true);
		save(cotaF);
		
		PDV pdv = Fixture.criarPDVPrincipal("Pdv 1", cotaF);
		save(pdv);
		
		Cota cotaJ = Fixture
				.cota(2, pessoaJuridica, SituacaoCadastro.ATIVO, box);
		cotaJ.setSugereSuspensao(true);
		
		save(cotaJ);
		
		Box boxLancamento = Fixture.criarBox(9999, "Box 9999", TipoBox.LANCAMENTO);
		save(boxLancamento);
		
		roteirizacao = Fixture.criarRoteirizacao(boxLancamento);
		save(roteirizacao);
		
		roteiro = Fixture.criarRoteiro("Pinheiros",roteirizacao,TipoRoteiro.NORMAL);
		save(roteiro);
		
		
		rota = Fixture.rota("Rota 005",roteiro);
		rota.setRoteiro(roteiro);
		rota.addPDV(pdv, 1);
		save(rota);
		
		roteiro = Fixture.criarRoteiro("Interlagos",roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro);
		
		rota = Fixture.rota("Rota 004", roteiro);
		rota.setRoteiro(roteiro);
		rota.addPDV(pdv, 1);
		save(rota);
		
		pdv = Fixture.criarPDVPrincipal("Pdv 1", cotaJ);
		save(pdv);
	}

	@Test
	public void testBusca() {
		List<Box> boxs = boxRepository.busca(null, null, null, null, 0, 10);

		boxs = boxRepository.busca(null, null, null, null, 1, 10);

		Assert.assertTrue("Tamanho da pagina errado", boxs.size() == 10);

		for (String campo : campos) {
			boxRepository.busca(null, null, campo, Ordenacao.ASC, 0, 10);
			boxRepository
					.busca(null, null, campo, Ordenacao.DESC, 0, 10);
			boxRepository.busca(null, null, campo, null, 0, 10);
		}

		boxs = boxRepository.busca(1, TipoBox.LANCAMENTO, null, null,
				0, 10);
		Assert.assertTrue("Encontrado mais de um box", boxs.size() == 1);
	}

	@Test
	public void testQuantidade() {
		long quantidade = boxRepository.quantidade(null, TipoBox.LANCAMENTO);

		Assert.assertEquals(101, quantidade);

	}

	@Test
	public void testObtemCotaRotaRoteiro() {
		List<CotaRotaRoteiroDTO> list = boxRepository.obtemCotaRotaRoteiro(box.getId());
		
		Assert.assertEquals(list.size(), 4);
	}

	@Test
	public void testObterListaBox() {
	
		List<Box> listaBoxRecolhimento =  boxRepository.obterListaBox(TipoBox.ENCALHE);
		
		Assert.assertEquals(3, listaBoxRecolhimento.size());
		
		
	}
	
	@Test
	public void testObterCotasPorBoxRoteiroRota() {
	    List<Cota> cotas = boxRepository.obterCotasPorBoxRoteiroRota(box.getId(), roteiro.getId(), rota.getId());
	    Assert.assertEquals(cotas.size(), 2);
	    Assert.assertEquals(cotas.get(0).getNumeroCota(), new Integer(1));
	}
	
	
//	TESTES SEM USO DE MASSA ----------------------------------------------------------------------------------------
	
	
	@Test
	public void testarObterBoxUsuario() {
		
		List<Box> boxes;		
		Long idUsuario = 1L;
		
		boxes = boxRepository.obterBoxUsuario(idUsuario, TipoBox.ENCALHE);
		
		Assert.assertNotNull(boxes);		
		
	}
	
	@Test
	public void testarObterBoxProduto() {
		
		List<Box> boxes;		
		String codigoProduto = "123";
		
		boxes = boxRepository.obterBoxPorProduto(codigoProduto);
		
		Assert.assertNotNull(boxes);
	}
	
	@Test
	public void testarHasCodigo() {
		
		Integer codigoBox = 1;
		Long id = 2L;
		
		boolean hasCodigo =  boxRepository.hasCodigo(codigoBox, id);
		
		Assert.assertTrue(hasCodigo);
		
	}
	
	@Test
	public void testarObterCodigoBoxPadraoUsuario() {
		Integer codigoPadrao;
		Long idUsuario = 1L;
		
		codigoPadrao = boxRepository.obterCodigoBoxPadraoUsuario(idUsuario);
		
//		Assert.assertNotNull(codigoPadrao);
		
	}
	
	@Test
	public void testarHasRoteirosVinculados() {
		
		Long idBox = 1L;
		
		boolean hasRoteirosVinculados = boxRepository.hasRoteirosVinculados(idBox);
		
		Assert.assertFalse(hasRoteirosVinculados);
		
	}
	
	@Test
	public void testarHasCotasVinculadas() {
		
		Long idBox = 1L;
		
		boolean hasCotasVinculadas = boxRepository.hasCotasVinculadas(idBox);
		
		Assert.assertFalse(hasCotasVinculadas);		
		
	}
	
	@Test
	public void testarObterCostasPorBoxRoteiroRota() {
		
		List<Cota> cotas;
		
		Long idBox = 1L;
		Long idRoteiro = 2L;
		Long idRota = 3L;
		
		cotas = boxRepository.obterCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
		
		Assert.assertNotNull(cotas);		
		
	}
	
	@Test
	public void testarObterQuantidadeCotasPorBoxRoteiroRota() {
		
		long quantidadeCotas;
		
		Long idBox = 1L;
		Long idRoteiro = 2L;
		Long idRota = 3L;
		
		quantidadeCotas = boxRepository.obterQuantidadeCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
		
		Assert.assertNotNull(quantidadeCotas);
		
	}

	
	
	
}
