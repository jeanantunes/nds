package br.com.abril.nds.repository.impl;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
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
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class RoteirizacaoRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RoteirizacaoRepository roteirizacaoRepository;

	private PDV pdvManoel;
	private Cota cotaManoel;
	private PessoaFisica manoel;

	private static Roteirizacao roteirizacao;

	private Box box;
	private Box box1;
	private Box box2;
	private Box box3;
	private Box box4;
	private Box box5;
	private Box box6;

	private Roteiro roteiro;
	private Roteiro roteiro1;
	private Roteiro roteiro2;
	private Roteiro roteiro3;

	private Rota rota;
	private Rota rota1;
	private Rota rota2;
	private Rota rota3;

	@Before
	public void setup() {

		box = Fixture.criarBox(300, "Box 300", TipoBox.LANCAMENTO);
		save(box);

		manoel = Fixture.pessoaFisica("10732815665", "sys.discover@gmail.com",
				"Manoel da Silva");
		save(manoel);

		cotaManoel = Fixture.cota(123, manoel, SituacaoCadastro.ATIVO, box);
		save(cotaManoel);

		pdvManoel = Fixture.criarPDVPrincipal("PDV MANOEL", cotaManoel);
		save(pdvManoel);

		roteirizacao = Fixture.criarRoteirizacao(box);
		save(roteirizacao);

		box1 = Fixture.criarBox(1, "BOX01", TipoBox.LANCAMENTO);
		save(box1);

		box2 = Fixture.criarBox(2, "BOX02", TipoBox.LANCAMENTO);
		save(box2);

		box3 = Fixture.criarBox(3, "BOX03", TipoBox.LANCAMENTO);
		save(box3);

		box4 = Fixture.criarBox(4, "BX-04", TipoBox.LANCAMENTO);
		save(box4);

		box5 = Fixture.criarBox(5, "BX-05", TipoBox.LANCAMENTO);
		save(box5);

		box6 = Fixture.criarBox(6, "BX-06", TipoBox.LANCAMENTO);
		save(box6);

		roteiro = Fixture
				.criarRoteiro("RT00", roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro);

		roteiro1 = Fixture.criarRoteiro("RT01", roteirizacao,
				TipoRoteiro.NORMAL);
		save(roteiro1);

		roteiro2 = Fixture
				.criarRoteiro("R02", roteirizacao, TipoRoteiro.NORMAL);
		save(roteiro2);

		roteiro3 = Fixture.criarRoteiro("RT03", roteirizacao,
				TipoRoteiro.NORMAL);
		save(roteiro3);

		rota = Fixture.rota("ROTA00", roteiro);
		rota.addPDV(pdvManoel, 1, box);
		save(rota);

		rota1 = Fixture.rota("ROTA01", roteiro);
		rota1.addPDV(pdvManoel, 1, box);
		save(rota1);

		rota2 = Fixture.rota("ROTA02", roteiro);
		rota2.addPDV(pdvManoel, 1, box);
		save(rota2);

		rota3 = Fixture.rota("ROTA03", roteiro1);
		rota3.addPDV(pdvManoel, 1, box);
		rota3.setRoteiro(roteiro1);
		save(rota3);

	}

	@Test
	public void obterBoxesPorNome() {

		List<BoxRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterBoxesPorNome("BOX");

		Assert.assertNotNull(lista);
		
		Assert.assertTrue(lista.get(3) != null);

		Assert.assertEquals(4, lista.size());
	}

	@Test
	public void obterRoteirosPorNomeEBoxes() {

		List<RoteiroRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterRoteirosPorNomeEBoxes("RT",
						Collections.singletonList(box.getId()));
		
		Assert.assertNotNull(lista);

		Assert.assertTrue(lista.get(1) != null);

		Assert.assertEquals(3, lista.size());
	}

	@Test
	public void obterRotasPorNomeERoteiros() {

		List<RotaRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterRotasPorNomeERoteiros("ROTA",
						Collections.singletonList(roteiro.getId()));
		
		Assert.assertNotNull(lista);

		Assert.assertTrue(lista.get(1) != null);

		Assert.assertEquals(lista.size(), 3);
	}

	@SuppressWarnings("unused")
	@Test
	public void obterRoteirizacaoPorBox(){
		Roteirizacao roteirizacao = roteirizacaoRepository.obterRoteirizacaoPorBox(1L);
	}
	
	@Test
	public void buscarRoteirizacao() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(123);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
		Assert.assertEquals(4, resultado.size());

		ConsultaRoteirizacaoDTO resultado1 = resultado.get(0);
		Assert.assertEquals(rota.getDescricaoRota(),
				resultado1.getDescricaoRota());

		ConsultaRoteirizacaoDTO resultado2 = resultado.get(1);
		Assert.assertEquals(rota1.getDescricaoRota(),
				resultado2.getDescricaoRota());

		ConsultaRoteirizacaoDTO resultado3 = resultado.get(2);
		Assert.assertEquals(rota2.getDescricaoRota(),
				resultado3.getDescricaoRota());

		ConsultaRoteirizacaoDTO resultado4 = resultado.get(3);
		Assert.assertEquals(rota3.getDescricaoRota(),
				resultado4.getDescricaoRota());
	}

	@Test
	public void buscarRoteirizacaoPorPaginacaoNula() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdBox(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(null);

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPorPaginacao() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdBox(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(15);
		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPorOrdenacao() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdBox(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);
		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPorIdBox() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdBox(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPorIdRoteiro() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdRoteiro(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPorIdRota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setIdRota(1L);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoPassandoNumeroCota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoOrdenacaoBox() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.BOX);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoOrdenacaoNomeCota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.NOME_COTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoOrdenacaoNumeroCota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.NUMERO_COTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoOrdenacaoRota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTA);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void buscarRoteirizacaoOrdenacaoRoteiro() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setNumeroCota(1);
		filtro.setOrdenacaoColuna(OrdenacaoColunaConsulta.ROTEIRO);
		filtro.setPaginacao(new PaginacaoVO(1, 10, "asc"));

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacao(filtro);
		Assert.assertNotNull(resultado);
	}

	@Test
	public void obterBoxDoPDV() {
		Box box = this.roteirizacaoRepository.obterBoxDoPDV(pdvManoel.getId());
		Assert.assertNotNull(box);
		Assert.assertEquals(box.getNome(), "Box 300");
	}

	@Test
	public void buscarRoteirizacaoDeCota() {

		Roteirizacao roteirizacao = roteirizacaoRepository
				.buscarRoteirizacaoDeCota(123);

		Assert.assertNotNull(roteirizacao);
	}

	@Test
	public void buscarRoterizacaoPorRota() {

		List<Roteirizacao> roteirizacao = roteirizacaoRepository
				.buscarRoterizacaoPorRota(1L);

		Assert.assertNotNull(roteirizacao);
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarMaiorOrdem() {

		Integer maiorOrdem = roteirizacaoRepository.buscarMaiorOrdem(rota
				.getId());
		System.out.println("");
	}

	@Test
	public void buscarPdvRoteirizacaoNumeroCota() {

		List<PDV> lista = roteirizacaoRepository
				.buscarPdvRoteirizacaoNumeroCota(cotaManoel.getNumeroCota(),
						rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEndereco() {
		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "", "", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoTipoRoteiroNormal() {
		Roteiro roteiro = this.roteiro;
		roteiro.setTipoRoteiro(TipoRoteiro.NORMAL);

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "", "", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoTipoRoteiroEspecial() {
		Roteiro roteiro = this.roteiro;
		roteiro.setTipoRoteiro(TipoRoteiro.ESPECIAL);

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "", "", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoPorCep() {

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"13700-000", "", "", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoPorUf() {

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "SP", "", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoPorCidade() {

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "", "Mococa", "", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorEnderecoPorBairro() {

		List<PDV> lista = roteirizacaoRepository.buscarRoteirizacaoPorEndereco(
				"", "", "", "Bairro", rota.getId(), roteiro);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarUF() {

		List<String> lista = roteirizacaoRepository.buscarUF();

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarMunicipioPorUf() {

		List<LogLocalidade> lista = roteirizacaoRepository
				.buscarMunicipioPorUf("Mococa");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarBairroPorMunicipio() {

		List<LogBairro> lista = roteirizacaoRepository
				.buscarBairroPorMunicipio(1L, "Mococa");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorNumeroCota() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.buscarRoteirizacaoPorNumeroCota(cotaManoel.getNumeroCota(),
						TipoRoteiro.NORMAL, "rotas", Ordenacao.ASC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteirizacaoPorNumeroCotaOrdenacaoDesc() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.buscarRoteirizacaoPorNumeroCota(cotaManoel.getNumeroCota(),
						TipoRoteiro.NORMAL, "rotas", Ordenacao.DESC, 1, 15);

		Assert.assertNotNull(lista);
	}

	@Test
	public void obterCotasParaBoxRotaRoteiro() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterCotasParaBoxRotaRoteiro(null, null, null);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterCotasParaBoxRotaRoteiroPorIdBox() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterCotasParaBoxRotaRoteiro(1L, null, null);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterCotasParaBoxRotaRoteiroPorIdRoteiro() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterCotasParaBoxRotaRoteiro(null, 1L, null);

		Assert.assertNotNull(lista);

	}

	@Test
	public void obterCotasParaBoxRotaRoteiroPorIdRota() {

		List<ConsultaRoteirizacaoDTO> lista = roteirizacaoRepository
				.obterCotasParaBoxRotaRoteiro(null, null, 1L);

		Assert.assertNotNull(lista);

	}
	
	@Test
	public void buscarRoteirizacaoSumarizadoPorCotaPaginacaoNula() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setPaginacao(null);
		
		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacaoSumarizadoPorCota(filtro);
		Assert.assertNotNull(resultado);
	}
	
	@Test
	public void buscarRoteirizacaoSumarizadoPorCotaPaginacao() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		
		filtro.setPaginacao(new PaginacaoVO());
		filtro.getPaginacao().setPaginaAtual(1);
		filtro.getPaginacao().setQtdResultadosPorPagina(15);

		List<ConsultaRoteirizacaoDTO> resultado = roteirizacaoRepository
				.buscarRoteirizacaoSumarizadoPorCota(filtro);
		Assert.assertNotNull(resultado);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void buscarQuantidadeRoteirizacao() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();

		Integer resultado = roteirizacaoRepository
				.buscarQuantidadeRoteirizacao(filtro);
		
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarQuantidadeRoteirizacaoSumarizadoPorCota() {
		FiltroConsultaRoteirizacaoDTO filtro = new FiltroConsultaRoteirizacaoDTO();
		filtro.setPaginacao(new PaginacaoVO());
		
		Integer resultado = roteirizacaoRepository
				.buscarQuantidadeRoteirizacaoSumarizadoPorCota(filtro);
	}

	
	
}
