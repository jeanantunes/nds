package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;

public interface ConsultaConsignadoCotaService {
	
	List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, boolean limitar);
	
	List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, boolean limitar);
	
	BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro);
	
	List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro);
	
	BigDecimal buscarTotalGeralAvistaCota(FiltroConsultaConsignadoCotaDTO filtro);
	
	BigDecimal buscarTotalGeralConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro);

}
