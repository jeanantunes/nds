package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public interface ConsolidadoFinanceiroService {
	
	List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro);
	
	List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro);

	public abstract List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(FiltroConsolidadoVendaCotaDTO filtro);

	public abstract ConsolidadoFinanceiroCota buscarPorId(Long id);

}
