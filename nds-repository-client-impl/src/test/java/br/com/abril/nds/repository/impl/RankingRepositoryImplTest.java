package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	
	
	@Test
	public void obterRankingCota() {
		
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingCota(filtro);
		
		List<RankingDTO> rankings = new ArrayList<>(mapaRnkg.values());
		
		Collections.sort(rankings);
		
		int count = 0;
		
		for(RankingDTO r : rankings) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println(" rnkg " + r.getRanking());
			System.out.println(" valor " + r.getValor());
			System.out.println(" valorAcumulado " + r.getValorAcumulado());
			
		}
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingCotaPorProduto(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Long idProduto = 1L;
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingCotaPorProduto(filtro, idProduto);
		
		Set<Long> chaves = mapaRnkg.keySet();
		
		int count = 0;
		
		for(Long c : chaves) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println("Chave" + c);
			System.out.println(" rnkg " + mapaRnkg.get(c).getRanking());
			System.out.println(" valor " + mapaRnkg.get(c).getValor());
			System.out.println(" valorAcumulado " + mapaRnkg.get(c).getValorAcumulado());
			
		}
		
		Assert.assertNotNull(mapaRnkg);
		
	}

	@Test
	public void obterRankingEditor(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Long idProduto = 1L;
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingEditor(filtro);
		
		Set<Long> chaves = mapaRnkg.keySet();
		
		int count = 0;
		
		for(Long c : chaves) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println("Chave" + c);
			System.out.println(" rnkg " + mapaRnkg.get(c).getRanking());
			System.out.println(" valor " + mapaRnkg.get(c).getValor());
			System.out.println(" valorAcumulado " + mapaRnkg.get(c).getValorAcumulado());
			
		}
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingProdutoPorCota(){
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Long idCota = 1L;
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingProdutoPorCota(filtro, idCota);
		Set<Long> chaves = mapaRnkg.keySet();
		
		int count = 0;
		
		for(Long c : chaves) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println("Chave" + c);
			System.out.println(" rnkg " + mapaRnkg.get(c).getRanking());
			System.out.println(" valor " + mapaRnkg.get(c).getValor());
			System.out.println(" valorAcumulado " + mapaRnkg.get(c).getValorAcumulado());
			
		}
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	@Test
	public void obterRankingProdutoPorProduto() { 
		filtro = new FiltroCurvaABCDistribuidorDTO(); 
		
		filtro.setDataDe(Fixture.criarData(1, Calendar.OCTOBER, 2013));
		filtro.setDataAte(Fixture.criarData(30, Calendar.OCTOBER, 2013));
		
		Map<Long, RankingDTO> mapaRnkg = rankingRepository.obterRankingProdutoPorProduto(filtro);
		Set<Long> chaves = mapaRnkg.keySet();
		
		int count = 0;
		
		for(Long c : chaves) {
		
			if(count++ > 10) {
				break;
			}
			
			System.out.println("Chave" + c);
			System.out.println(" rnkg " + mapaRnkg.get(c).getRanking());
			System.out.println(" valor " + mapaRnkg.get(c).getValor());
			System.out.println(" valorAcumulado " + mapaRnkg.get(c).getValorAcumulado());
			
		}
		
		Assert.assertNotNull(mapaRnkg);
		
	}
	
	
}
