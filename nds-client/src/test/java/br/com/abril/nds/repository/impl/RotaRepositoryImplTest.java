package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class RotaRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RotaRepository rotaRepository;

	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;

	private Box box1;
	private PDV pdvManoel;
	private Cota cotaManoel;
	private PessoaFisica manoel;
	private Rota rota;
	private Rota rota10;
	private Roteiro roteiro;
	private Roteiro roteiroTCD;

	@Before
	public void setUp() {

		manoel = Fixture.pessoaFisica("10732815665", "sys.discover@gmail.com",
				"Manoel da Silva");
		save(manoel);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box1);
		save(cotaManoel);

		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		save(pdvManoel);

		box1 = Fixture.criarBox(1, "BX-001", TipoBox.LANCAMENTO);
		save(box1);

		Roteirizacao roteirizacao = Fixture.criarRoteirizacao(box1);
		save(roteirizacao);

		roteiro = Fixture.criarRoteiro("Pinheiros", roteirizacao,
				TipoRoteiro.NORMAL);
		save(roteiro);

		rota = Fixture.rota("Rota 005", roteiro);
		rota.addPDV(pdvManoel, 1, box1);
		rota.setRoteiro(roteiro);
		save(rota);

		roteiro = Fixture.criarRoteiro("Interlagos", roteirizacao,
				TipoRoteiro.NORMAL);
		save(roteiro);

		roteiroTCD = Fixture.criarRoteiro("TCD", roteirizacao,
				TipoRoteiro.NORMAL);
		save(roteiroTCD);

		rota10 = Fixture.rota("Rota 001", roteiroTCD);
		rota10.addPDV(pdvManoel, 1, box1);
		rota10.setRoteiro(roteiroTCD);
		save(rota10);

	}

	@Test
	public void buscarRotasRoteiroAssociacaoSortnameDescricaoRota() {
		String sortname = "descricaoRota";
		String sortorder = "asc";

		List<RotaRoteiroDTO> lista = rotaRepository
				.buscarRotasRoteiroAssociacao(sortname, sortorder);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRotasRoteiroAssociacaoSortnameRoteiro() {
		String sortname = "roteiro.descricaoRoteiro";
		String sortorder = "desc";

		List<RotaRoteiroDTO> lista = rotaRepository
				.buscarRotasRoteiroAssociacao(sortname, sortorder);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRotaOrdenacaoASC() {
		String sortname = "descricaoRota";

		List<Rota> lista = rotaRepository.buscarRota(sortname, Ordenacao.ASC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRotaOrdenacaoDESC() {
		String sortname = "descricaoRota";

		List<Rota> lista = rotaRepository.buscarRota(sortname, Ordenacao.DESC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRotaPorRoteiroOrdenacaoASC() {
		String sortname = "descricaoRota";

		List<Rota> lista = rotaRepository.buscarRotaPorRoteiro(1L, sortname,
				Ordenacao.ASC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRotaPorRoteiroOrdenacaoDESC() {
		String sortname = "descricaoRota";

		List<Rota> lista = rotaRepository.buscarRotaPorRoteiro(1L, sortname,
				Ordenacao.DESC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void atualizaOrdenacao() {
		Rota rota = new Rota();
		rotaRepository.atualizaOrdenacao(rota);
	}

	@Test
	public void obterRotasPorCota() {

		List<Rota> rotas = rotaRepository.obterRotasPorCota(cotaManoel
				.getNumeroCota());

		Assert.assertNotNull(rotas);
	}
	
	@Test
	public void buscarRotaPorNome() {

		List<Rota> rotas = rotaRepository.buscarRotaPorNome(1L, "Rota", MatchMode.START);

		Assert.assertNotNull(rotas);
	}
	
	@Test
	public void buscarRotaPorNomeEmpty() {

		List<Rota> rotas = rotaRepository.buscarRotaPorNome(1L, "", MatchMode.START);

		Assert.assertNotNull(rotas);
	}
	
	@Test
	public void buscarRotaDeBox() {

		List<Rota> rotas = rotaRepository.buscarRotaDeBox(1L);

		Assert.assertNotNull(rotas);
	}
	
	@Test
	public void buscarRotaDeRoteiro() {

		List<Rota> rotas = rotaRepository.buscarRotaDeRoteiro("Teste");

		Assert.assertNotNull(rotas);
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarMaiorOrdemRota() {

		Integer maiorOrdemRota = rotaRepository.buscarMaiorOrdemRota(1L);

	}
	
	@SuppressWarnings("unused")
	@Test
	public void obterRotaPorPDV() {

		Rota rota = rotaRepository.obterRotaPorPDV(1L, 1L);

	}
	
	@Test
	public void obterCotasPorBoxRotaRoteiro() {
		List<ConsultaRoteirizacaoDTO> rotas = roteirizacaoRepository
				.obterCotasParaBoxRotaRoteiro(box1.getId(), rota10.getId(),
						roteiroTCD.getId());

		Assert.assertEquals(1, rotas.size());
		ConsultaRoteirizacaoDTO dto = rotas.get(0);
		Assert.assertEquals(cotaManoel.getNumeroCota(), dto.getNumeroCota());
		Assert.assertEquals(cotaManoel.getPessoa().getNome(), dto.getNome());
	}

}
