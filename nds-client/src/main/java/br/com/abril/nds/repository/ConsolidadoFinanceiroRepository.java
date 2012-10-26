package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public interface ConsolidadoFinanceiroRepository extends Repository<ConsolidadoFinanceiroCota, Long> {
	
	List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer idCota);
	
	boolean verificarConsodidadoCotaPorDataOperacao(Long idCota);
	
	List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro);
	
	public abstract List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(FiltroConsolidadoVendaCotaDTO filtro);
	
	List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro);
	
	ConsolidadoFinanceiroCota obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro);
	
	Date buscarUltimaDividaGeradaDia(Date dataOperacao);
	
	Date buscarDiaUltimaDividaGerada();

	Long obterQuantidadeDividasGeradasData(Date data);

	ConsolidadoFinanceiroCota buscarPorCotaEData(Cota cota, java.sql.Date data);
}