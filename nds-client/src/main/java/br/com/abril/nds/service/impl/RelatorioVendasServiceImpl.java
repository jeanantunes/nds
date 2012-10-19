package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCDistribuidorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCDistribuidor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCDistribuidorDTO.TipoConsultaCurvaABC;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.repository.RelatorioVendasRepository;
import br.com.abril.nds.service.RelatorioVendasService;
@Service
public class RelatorioVendasServiceImpl implements RelatorioVendasService {

	@Autowired
	private RelatorioVendasRepository relatorioVendasRepository;
	
	@Autowired
	private RankingRepository rankingRepository;

	@Override
	@Transactional
	public List<RegistroCurvaABCDistribuidorVO> obterCurvaABCDistribuidor(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		
		List<RegistroCurvaABCDistribuidorVO> lista = relatorioVendasRepository.obterCurvaABCDistribuidor(filtroCurvaABCDistribuidorDTO);
		
		if(!lista.isEmpty()){
			
			for(RegistroCurvaABCDistribuidorVO item : lista){
				
				if(TipoConsultaCurvaABC.PRODUTO.equals(filtroCurvaABCDistribuidorDTO.getTipoConsultaCurvaABC())){
					
					//Obtem o ranking do produto 
					item.setRkProduto(rankingRepository.obterRankingProduto(item.getIdProduto()));
					
					//Obtem o ranking da cota em relação ao produto 
					item.setRkCota(rankingRepository.obterRankingCota(item.getIdProduto(), item.getIdCota()));
				}
				else{
					
					//Obtem o ranking da cota no distribuidor
					item.setRkCota(rankingRepository.obterRankingCotaDistribuidor(item.getIdCota()));
				}
			}
		}
	
		return lista;
	}

	@Override
	@Transactional
	public ResultadoCurvaABCDistribuidor obterCurvaABCDistribuidorTotal(FiltroCurvaABCDistribuidorDTO filtroCurvaABCDistribuidorDTO) {
		return relatorioVendasRepository.obterCurvaABCDistribuidorTotal(filtroCurvaABCDistribuidorDTO);
	}

}
