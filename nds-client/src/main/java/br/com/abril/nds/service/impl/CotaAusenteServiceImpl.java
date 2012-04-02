package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.movimentacao.CotaAusente;
import br.com.abril.nds.model.movimentacao.RateioCotaAusente;
import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.service.CotaAusenteService;
import br.com.abril.nds.service.MovimentoEstoqueService;

public class CotaAusenteServiceImpl implements CotaAusenteService{
	
	@Autowired
	CotaAusenteRepository cotaAusenterepository;
	
	@Autowired
	MovimentoEstoqueService movimentoEstoqueService;
	
	@Autowired
	MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Transactional
	public void declararCotaAusente(Long idCota, Date data, List<RateioCotaAusente> listaDeRateio){

		CotaAusente cotaAusente = new CotaAusente();
		Cota cota = new Cota();
		
		cota.setId(idCota);
		cotaAusente.setAtivo(true);
		cotaAusente.setCota(cota);
		cotaAusente.setData(data);
		
		cotaAusenterepository.adicionar(cotaAusente);
		
		 List<MovimentoEstoqueCota> movimentoCota = movimentoEstoqueCotaRepository.obterMovimentoCotaPorTipoMovimento(data, cota.getId(), GrupoMovimentoEstoque.ENVIO_JORNALEIRO);
		
		 movimentoEstoqueService.enviarSuplementarCotaAusente(data, cota.getId(), movimentoCota);	
	}
	
	@Transactional
	public List<CotaAusenteDTO> obterCotasAusentes(Date data, Long idCota, CotaAusenteDTO cotaAusenteDTO){
		return cotaAusenterepository.obterCotasAusentes(data, idCota, cotaAusenteDTO);
	}

}
