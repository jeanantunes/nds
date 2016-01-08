package br.com.abril.nds.unit.test.integration.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.PdvRoteirizacaoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.repository.RoteiroRepository;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.RotaService;

@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(transactionManager="transactionManager")
@ContextConfiguration(locations="file:src/test/resources/spring/applicationContext-ndsi-test.xml")
public class RoteirizacaoTest {

	@Autowired
	private RoteiroRepository roteRepository;
	
	@Autowired
	private RotaRepository rotaRepository;
	
	@Autowired
	private RotaService rotaService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private PdvRepository pdvRepository;
	 
	@Test
	@Transactional
	public void roteiroTest() {
		
		Roteiro roteiro = this.roteRepository.buscarPorId(1L);
		Roteiro roteiroEspecial = popularRoteiroEspecial(roteiro);
		System.out.println(roteiroEspecial.toString());
		roteRepository.merge(roteiroEspecial);
		roteRepository.flush();
		roteRepository.clear();
		assert(true);
	}

	private Roteiro popularRoteiroEspecial(Roteiro roteiro) {
		
		Box box = boxService.buscarPorId(179L);
		
		List<Rota> rotasEpeciais = new ArrayList<Rota>();
		
		Roteiro roteiroEspecial = this.roteRepository.buscarPorId(16L);
		
		for (Rota rota :  roteiro.getRotas()) {
			
			Rota rotaEspecial = new Rota();
			
			rotaEspecial.setAssociacoesVeiculoMotoristaRota(rota.getAssociacoesVeiculoMotoristaRota());
			rotaEspecial.setDescricaoRota(rota.getDescricaoRota());
			rotaEspecial.setOrdem(rota.getOrdem());
			rotaEspecial.setEntregador(rota.getEntregador());
			rotaEspecial.setRoteiro(roteiroEspecial);
			rotaEspecial.setRotaPDVs(rota.getRotaPDVs());
			rotasEpeciais.add(rotaEspecial);
			
			rotaRepository.merge(rotaEspecial);
			
		}
		
		roteiroEspecial.setRotas(rotasEpeciais);
		
		roteiroEspecial.getRotas();
		
		return roteiroEspecial;
	}
}
