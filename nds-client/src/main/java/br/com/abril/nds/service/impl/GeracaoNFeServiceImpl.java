package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.util.Intervalo;

@Service
public class GeracaoNFeServiceImpl implements GeracaoNFeService {
	
	
	//@Autowired
	private NotaFiscalService notaFiscalService;

	@Override
	public List<CotaExemplaresDTO> busca(Intervalo<String> intervaloBox,
			Intervalo<Integer> intervalorCota,
			Intervalo<Date> intervaloDateMovimento,
			List<Long> listIdFornecedor, Long tipoNotaFiscal, String sortname,
			String sortorder, Integer resultsPage, Integer page) {
		
		
		if(resultsPage == null){
			resultsPage = 15;
		}
	
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs = new ArrayList<CotaExemplaresDTO>(resultsPage);
 		
		for(Long i=0L; i < resultsPage; i++){
			CotaExemplaresDTO cotaExemplaresDTO = new CotaExemplaresDTO();			
			cotaExemplaresDTO.setExemplares(i);
			cotaExemplaresDTO.setIdCota(i);
			cotaExemplaresDTO.setInativo(false);
			cotaExemplaresDTO.setNomeCota("Cota " + (i +1));
			cotaExemplaresDTO.setNumeroCota(i.intValue());
			cotaExemplaresDTOs.add(cotaExemplaresDTO);
			
		}
		
		return cotaExemplaresDTOs;
	}
	
	
}
