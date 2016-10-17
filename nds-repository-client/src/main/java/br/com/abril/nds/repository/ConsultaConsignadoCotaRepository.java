package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ConsultaConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaConsignadoCotaPeloFornecedorDTO;
import br.com.abril.nds.dto.TotalConsultaConsignadoCotaDetalhado;
import br.com.abril.nds.dto.filtro.FiltroConsultaConsignadoCotaDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;

public interface ConsultaConsignadoCotaRepository extends Repository<MovimentoEstoqueCota, Long> {
	
	public List<ConsultaConsignadoCotaDTO> buscarConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro, boolean limitar);
	
	public List<ConsultaConsignadoCotaPeloFornecedorDTO> buscarMovimentosCotaPeloFornecedor(FiltroConsultaConsignadoCotaDTO filtro, boolean limitar);
	
	public BigDecimal buscarTotalGeralDaCota(FiltroConsultaConsignadoCotaDTO filtro);
	
	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhado(FiltroConsultaConsignadoCotaDTO filtro);

    public BigDecimal buscarTotalDetalhadoSomado(FiltroConsultaConsignadoCotaDTO filtro);

	public List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhadoPorCota(FiltroConsultaConsignadoCotaDTO filtro);

	public BigDecimal buscarTotalGeralAvistaCota(FiltroConsultaConsignadoCotaDTO filtro);
	
	List<TotalConsultaConsignadoCotaDetalhado> buscarTotalDetalhadoPorCotaAvista(FiltroConsultaConsignadoCotaDTO filtro);
	
	BigDecimal buscarTotalGeralConsignadoCota(FiltroConsultaConsignadoCotaDTO filtro);
	
}
