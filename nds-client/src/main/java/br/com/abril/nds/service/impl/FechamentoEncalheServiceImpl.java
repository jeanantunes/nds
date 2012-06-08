package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.repository.FechamentoEncalheRepository;
import br.com.abril.nds.service.FechamentoEncalheService;

@Service
public class FechamentoEncalheServiceImpl implements FechamentoEncalheService {

	@Autowired
	private FechamentoEncalheRepository fechamentoEncalheRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro,
			String sortorder, String sortname, int page, int rp) {
		
		
		
		
		
		return fechamentoEncalheRepository.buscarFechamentoEncalhe(filtro, sortorder, sortname, page, rp);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe,
			String sortorder, String sortname, int page, int rp) {

		int startSearch = page * rp - rp;
		
		return this.fechamentoEncalheRepository.buscarCotasAusentes(dataEncalhe, sortorder, sortname, startSearch, rp);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer buscarTotalCotasAusentes(Date dataEncalhe) {
		return this.fechamentoEncalheRepository.buscarTotalCotasAusentes(dataEncalhe);
	}

}
