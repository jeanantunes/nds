package br.com.abril.nds.repository.impl;

import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

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
		
		PDV pdv = Fixture.criarPDVPrincipal("Pdv 1", cotaF,1);
		save(pdv);
		
		Cota cotaJ = Fixture
				.cota(2, pessoaJuridica, SituacaoCadastro.ATIVO, box);
		cotaJ.setSugereSuspensao(true);
		
		save(cotaJ);
		
		roteirizacao = Fixture.criarRoteirizacao(box);
		save(roteirizacao);
		
		roteiro = Fixture.criarRoteiro("Pinheiros",roteirizacao,box,TipoRoteiro.NORMAL);
		save(roteiro);
		
		rota = Fixture.rota("005", "Rota 005",roteiro,Arrays.asList(pdv));
		rota.setRoteiro(roteiro);
		save(rota);
		
		roteiro = Fixture.criarRoteiro("Interlagos",roteirizacao,box,TipoRoteiro.NORMAL);
		save(roteiro);
		
		rota = Fixture.rota("004", "Rota 004",roteiro,Arrays.asList(pdv));
		rota.setRoteiro(roteiro);
		save(rota);
		
		pdv = Fixture.criarPDVPrincipal("Pdv 1", cotaJ,2);
		save(pdv);
		
		roteirizacao = Fixture.criarRoteirizacao(box);
		save(roteirizacao);
		

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

		Assert.assertEquals(quantidade, 100l);

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
	
}
