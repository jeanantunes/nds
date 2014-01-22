package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;

public interface RepartePdvService {

	public RepartePDV obterRepartePorPdv (Long idFixacao, Long idProduto, Long idPdv);
	
	public void salvarRepartesPDV(List<RepartePDVDTO> listaRepartes, String codProduto, Long idFixacao, boolean manterFixa);
	
	public void salvarRepartesPDVMix(List<RepartePDVDTO> listaRepartes, String codProduto, Long idMix);
	
	public RepartePDV obterRepartePorPdvMix (Long idMix, Long idProduto, Long idPdv);
	
}
