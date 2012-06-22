package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public interface ConsultaConsignadoCotaRepository extends Repository<MovimentoEstoqueCota, Long> {
	
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, String limitar);
	
	public Integer buscarTodasMovimentacoesPorCota(FiltroConsultaConsignadoCotaDTO filtro, String limitar);
	
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, String limitar);

}
