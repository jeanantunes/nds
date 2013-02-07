package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;

public interface ConsolidadoFinanceiroRepository extends Repository<ConsolidadoFinanceiroCota, Long> {
	
	List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer idCota);
	
	List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro);
	
	public abstract List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(FiltroConsolidadoVendaCotaDTO filtro);
	
	List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro);
	
	List<ConsolidadoFinanceiroCota> obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro);
	
	Date buscarUltimaDividaGeradaDia(Date dataOperacao);
	
	Date buscarDiaUltimaDividaGerada();

	Long obterQuantidadeDividasGeradasData(List<Long> idsCota);

	ConsolidadoFinanceiroCota buscarPorCotaEData(Cota cota, java.sql.Date data);

	List<ConsolidadoFinanceiroCota> obterConsolidadosDataOperacao(Long idCota);

	List<ContaCorrenteCotaVO> obterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro,
			List<Long> tiposMovimentoCredito, List<Long> tiposMovimentoDebito,
			List<Long> tipoMovimentoEncalhe, List<Long> tiposMovimentoEncargos,
			List<Long> tiposMovimentoPostergadoCredito, List<Long> tiposMovimentoPostergadoDebito,
			List<Long> tipoMovimentoVendaEncalhe);

	BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro);
}