package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.CotaAusenteEncalheDTO;
import br.com.abril.nds.dto.FechamentoFisicoLogicoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;

public interface FechamentoEncalheService {

	List<FechamentoFisicoLogicoDTO> buscarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, int page, int rp);
	
	List<FechamentoFisicoLogicoDTO> salvarFechamentoEncalhe(FiltroFechamentoEncalheDTO filtro, String sortorder, String sortname, int page, int rp);
	
	List<CotaAusenteEncalheDTO> buscarCotasAusentes(Date dataEncalhe, String sortorder, String sortname, int page, int rp);

	Integer buscarTotalCotasAusentes(Date dataEncalhe);

	void postergarCotas(Date dataPostergacao, List<Long> idsCotas);
	
	void cobrarCotas(Date dataEncalhe, List<Long> idsCotas);
	
}
