package br.com.abril.nds.repository.impl;

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

	private static final String[] campos = { "codigo", "tipoBox", "nome",
			"postoAvancado" };

	private Box box;

	@Before
	public void setUp() throws Exception {

		for (int i = 0; i < 100; i++) {
			box = Fixture.criarBox("Box-" + i, "BX-" + i, TipoBox.LANCAMENTO, false);
			save(box);
		}
		
		for (int i = 0; i < 3; i++) {
			box = Fixture.criarBox("RecBox-" + i, "RECBX-" + i, TipoBox.RECOLHIMENTO, false);
			save(box);
		}
		
		
		PessoaJuridica pessoaJuridica = Fixture.pessoaJuridica("FC",
				"01.001.001/001-00", "000.000.000.00", "fc@mail.com", "99.999-9");

		save(pessoaJuridica);

		PessoaFisica pessoaFisica = Fixture.pessoaFisica("100.955.356-39",
				"joao@gmail.com", "João da Silva");
		save(pessoaFisica);

		Cota cota = Fixture
				.cota(1, pessoaFisica, SituacaoCadastro.ATIVO, box);
		cota.setSugereSuspensao(true);
		save(cota);
		
		Roteiro roteiro = Fixture.criarRoteiro("Pinheiros", box,TipoRoteiro.NORMAL);
		save(roteiro);
		
		Rota rota = Fixture.rota("005", "Rota 005");
		rota.setRoteiro(roteiro);
		save(rota);
		
		PDV pdv = Fixture.criarPDVPrincipal("Pdv 1", cota);
		save(pdv);
		
		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(pdv, rota, 1);
		save(roteirizacao);
		
		cota = Fixture
				.cota(2, pessoaJuridica, SituacaoCadastro.ATIVO, box);
		cota.setSugereSuspensao(true);
		
		save(cota);
		
		roteiro = Fixture.criarRoteiro("Interlagos",box,TipoRoteiro.NORMAL);
		save(roteiro);
		
		rota = Fixture.rota("004", "Rota 004");
		rota.setRoteiro(roteiro);
		save(rota);
		
		pdv = Fixture.criarPDVPrincipal("Pdv 1", cota);
		save(pdv);
		
		roteirizacao = Fixture.criarRoteirizacao(pdv, rota, 1);
		save(roteirizacao);
		

	}

	@Test
	public void testBusca() {
		List<Box> boxs = boxRepository.busca(null, null, true, null, null, 0,
				10);
		Assert.assertTrue("Encontrado postos avançados", boxs.isEmpty());

		boxs = boxRepository.busca(null, null, false, null, null, 1, 10);

		Assert.assertTrue("Tamanho da pagina errado", boxs.size() == 10);

		for (String campo : campos) {
			boxRepository.busca(null, null, false, campo, Ordenacao.ASC, 0, 10);
			boxRepository
					.busca(null, null, false, campo, Ordenacao.DESC, 0, 10);
			boxRepository.busca(null, null, false, campo, null, 0, 10);
		}

		boxs = boxRepository.busca("Box-1", TipoBox.LANCAMENTO, false, null,
				null, 0, 10);
		Assert.assertTrue("Encontrado mais de um box", boxs.size() == 1);
	}

	@Test
	public void testQuantidade() {
		long quantidade = boxRepository.quantidade(null, TipoBox.LANCAMENTO,
				false);

		Assert.assertEquals(quantidade, 100l);

	}

	@Test
	public void testObtemCotaRotaRoteiro() {
		List<CotaRotaRoteiroDTO> list = boxRepository.obtemCotaRotaRoteiro(box.getId());
		
		Assert.assertEquals(list.size(), 2);
	}

	@Test
	public void testObterListaBox() {
	
		List<Box> listaBoxRecolhimento =  boxRepository.obterListaBox(TipoBox.RECOLHIMENTO);
		
		Assert.assertEquals(3, listaBoxRecolhimento.size());
		
		
	}

	
	
}
