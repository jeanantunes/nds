package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.criterion.MatchMode;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class RoteiroRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private RoteiroRepository roteiroRepository;

	@Test
	public void buscarRoteiroASC() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiro("ordem",
				Ordenacao.ASC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteiroDESC() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiro("ordem",
				Ordenacao.DESC);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteiroPorDescricao() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroPorDescricao(
				"teste", MatchMode.START);

		Assert.assertNotNull(lista);
	}

	@Test
	public void atualizaOrdenacao() {
		roteiroRepository.atualizaOrdenacao(new Roteiro());

	}

	@Test
	public void buscarRoteiroDeBoxPorIdBox() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroDeBox(1L);

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteiroDeBoxPorIdBoxEDescricao() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroDeBox(1L, "Teste");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteiroDeBoxDescricaoVazia() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroDeBox(1L, "");

		Assert.assertNotNull(lista);
	}

	@Test
	public void buscarRoteiroDeBoxIdNulo() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroDeBox(null);

		Assert.assertNotNull(lista);
	}

	@SuppressWarnings("unused")
	@Test
	public void buscarMaiorOrdemRoteiro() {
		Integer ordemRoteiro = roteiroRepository.buscarMaiorOrdemRoteiro();

	}
	
	@Test
	public void buscarRoteiroEspecial() {
		List<Roteiro> lista = roteiroRepository.buscarRoteiroEspecial();

		Assert.assertNotNull(lista);
	}

	@Test
	public void obterRoteirosPorCota() {
		List<Roteiro> lista = roteiroRepository.obterRoteirosPorCota(null);

		Assert.assertNotNull(lista);
	}

	@Test
	public void obterRoteirosPorCotaPorNumeroConta() {
		List<Roteiro> lista = roteiroRepository.obterRoteirosPorCota(1);

		Assert.assertNotNull(lista);
	}
	
}
