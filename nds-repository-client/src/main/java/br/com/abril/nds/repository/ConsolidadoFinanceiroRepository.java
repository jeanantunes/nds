package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.client.vo.ContaCorrenteVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.ViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteDTO;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;

public interface ConsolidadoFinanceiroRepository extends Repository<ConsolidadoFinanceiroCota, Long> {
	
	List<ViewContaCorrenteCotaDTO> buscarListaDeConsolidado(Integer idCota);
	
	List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro);
	
	public abstract List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(FiltroConsolidadoVendaCotaDTO filtro);
	
	List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro);
	
	List<ConsolidadoFinanceiroCota> obterConsolidadoPorIdMovimentoFinanceiro(Long idMovimentoFinanceiro);
	
	Date buscarUltimaDividaGeradaDia(Date dataOperacao);
	
	Date buscarDiaUltimaDividaGerada();

	Long obterQuantidadeDividasGeradasData(List<Long> idsCota);

	ConsolidadoFinanceiroCota buscarPorCotaEData(Long idCota, Date data);

	List<ConsolidadoFinanceiroCota> obterConsolidadosDataOperacao(List<Long> idConsolidados);

	List<ContaCorrenteCotaVO> obterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro,
			List<Long> tiposMovimentoCredito, List<Long> tiposMovimentoDebito,
			List<Long> tipoMovimentoEncalhe, List<Long> tiposMovimentoEncargos,
			List<Long> tiposMovimentoPostergadoCredito, List<Long> tiposMovimentoPostergadoDebito,
			List<Long> tipoMovimentoVendaEncalhe, List<Long> tiposMovimentoConsignado, 
			List<Long> tiposMovimentoPendete, List<Long> tiposMovimentoNegociacaoComissao);

	List<ContaCorrenteVO> obterContaCorrenteExtracao(FiltroViewContaCorrenteDTO filtro);
			
	BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro);
	
	Long obterQuantidadeDividasGeradasData(Date dataVencimentoDebito, Long... idsCota);
	
	Date obterDataAnteriorImediataPostergacao(ConsolidadoFinanceiroCota consolidadoFinanceiroCota);

	/**
	 * Método que obtem os movimentos de Envio ao Jornaleiro (Consignado) para conta corrente da Cota do tipo À Vista
	 */
	List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaAVistaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro);

    List<DebitoCreditoCota> buscarMovFinanPorCotaEData(
            Long idCota, List<Date> datas, Long idFornecedor);

    List<DebitoCreditoCota> obterConsolidadosDataOperacaoSlip(Long idCota, Date dataOperacao);

	List<Long> obterIdsConsolidadosDataOperacao(Long idCota, Date dataOperacao);
}