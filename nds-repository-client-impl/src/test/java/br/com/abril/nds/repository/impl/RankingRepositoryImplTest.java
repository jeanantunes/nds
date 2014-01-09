package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.RankingDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.fixture.Fixture;
import br.com.abril.nds.repository.RankingRepository;

public class RankingRepositoryImplTest extends AbstractRepositoryImplTest {
	
	@Autowired
	private RankingRepository rankingRepository;
	
	FiltroCurvaABCDistribuidorDTO filtro;
	
	private void validarValoresDeRankingObtidos(List<RankingDTO> rankings) {
		
		Collections.sort(rankings);
		
		int count = 0;
		
		Long rankingAnterior = null;
		
		BigDecimal valorAnterior = null;
		
		BigDecimal valorAcumuladoAnterior = null;
		
		for(RankingDTO r : rankings) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println(" rnkg " + r.getRanking());
			System.out.println(" valor " + r.getValor());
			System.out.println(" valorAcumulado " + r.getValorAcumulado());
			
			if(rankingAnterior == null) {
				rankingAnterior = r.getRanking();
			} else {
				Assert.assertTrue("Ranking não ordenado de forma crescente", rankingAnterior <= r.getRanking());
				rankingAnterior = r.getRanking();
			}
			
			if(valorAnterior == null) {
				valorAnterior = r.getValor();
			} else {
				Assert.assertTrue("Valor não ordenado de forma crescente", valorAnterior.compareTo(r.getValor()) >= 0 );
				valorAnterior = r.getValor();
			}
			
			if(valorAcumuladoAnterior == null) {
				valorAcumuladoAnterior = r.getValorAcumulado();
			} else {
				Assert.assertTrue("Valor Acumulado não ordenado de forma crescente", valorAcumuladoAnterior.compareTo(r.getValorAcumulado()) <= 0 );
				valorAcumuladoAnterior = r.getValorAcumulado();
			}
			
			
		}
		
		
	}
	
	@Test
	public void obterRankingCota() {
		
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingCota(filtro);
		
		validarValoresDeRankingObtidos(new ArrayList<>(mapaRnkg.values()));
	
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingCotaPorProduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Long idProduto = 1L;
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingCotaPorProduto(filtro, idProduto);
		
		validarValoresDeRankingObtidos(new ArrayList<>(mapaRnkg.values()));
		
		Assert.assertNotNull(mapaRnkg);
		
	}

	@Test
	public void obterRankingEditor(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingEditor(filtro);
		
		validarValoresDeRankingObtidos(new ArrayList<>(mapaRnkg.values()));
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingProdutoPorCota(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Long idCota = 1L;
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingProdutoPorCota(filtro, idCota);
		
		validarValoresDeRankingObtidos(new ArrayList<>(mapaRnkg.values()));
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingProdutoPorProduto() { 
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingProdutoPorProduto(filtro);

		validarValoresDeRankingObtidos(new ArrayList<>(mapaRnkg.values()));
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	
}
