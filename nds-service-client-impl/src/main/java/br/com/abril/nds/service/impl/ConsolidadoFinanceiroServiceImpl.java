package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.FornecedorService;

@Service
public class ConsolidadoFinanceiroServiceImpl implements ConsolidadoFinanceiroService {
	
	@Autowired
	ConsolidadoFinanceiroRepository consolidadoFinanceiroRepository;
	
	@Autowired
	FornecedorService fornecedorService;
	
	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Transactional(readOnly=true)
	public List<EncalheCotaDTO> obterMovimentoEstoqueCotaEncalhe(FiltroConsolidadoEncalheCotaDTO filtro){
		
		return consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaEncalhe(filtro);		
	}
	
	@Transactional(readOnly=true)
	public List<ConsultaVendaEncalheDTO> obterMovimentoVendaEncalhe(FiltroConsolidadoVendaCotaDTO filtro) {
		return consolidadoFinanceiroRepository.obterMovimentoVendaEncalhe(filtro);
	}
	
	@Override
	@Transactional(readOnly=true)
	public ConsolidadoFinanceiroCota buscarPorId(Long id) {
		return consolidadoFinanceiroRepository.buscarPorId(id);
	}
	
	@Transactional(readOnly=true)
	public List<ConsignadoCotaDTO> obterMovimentoEstoqueCotaConsignado(FiltroConsolidadoConsignadoCotaDTO filtro){
		
		List<ConsignadoCotaDTO> consignadoDTO;
		
		Cota cota = this.cotaRepository.obterPorNumeroDaCota(filtro.getNumeroCota());
		
		if (cota.getTipoCota().equals(TipoCota.CONSIGNADO)){
			
			consignadoDTO = consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaConsignado(filtro);
		}
		else{
			
			consignadoDTO = consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaAVistaConsignado(filtro);
		}
		
		return consignadoDTO;
	}

	@Override
	@Transactional(readOnly=true)
	public Date buscarUltimaDividaGeradaDia(Date dataOperacao) {
		return consolidadoFinanceiroRepository.buscarUltimaDividaGeradaDia(dataOperacao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Date buscarDiaUltimaDividaGerada() {
		return consolidadoFinanceiroRepository.buscarDiaUltimaDividaGerada();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ContaCorrenteCotaVO> obterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro){
		
		List<Long> tiposMovimentoCredito = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
					Arrays.asList(
						GrupoMovimentoFinaceiro.CREDITO,
						GrupoMovimentoFinaceiro.CREDITO_SOBRE_FATURAMENTO,
						GrupoMovimentoFinaceiro.RESGATE_CAUCAO_LIQUIDA
					)
				);
		
		List<Long> tiposMovimentoDebito =
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
					Arrays.asList(	
						GrupoMovimentoFinaceiro.DEBITO,
						GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO,
						GrupoMovimentoFinaceiro.POSTERGADO_NEGOCIACAO,
						GrupoMovimentoFinaceiro.VENDA_TOTAL,
						GrupoMovimentoFinaceiro.COMPRA_NUMEROS_ATRAZADOS,
						GrupoMovimentoFinaceiro.LANCAMENTO_CAUCAO_LIQUIDA,
						GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO,
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_ENTREGADOR,
						GrupoMovimentoFinaceiro.DEBITO_COTA_TAXA_DE_ENTREGA_TRANSPORTADOR,
						GrupoMovimentoFinaceiro.TAXA_EXTRA
					)
				);
		
		List<Long> tipoMovimentoEncalhe = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
					Arrays.asList(
						GrupoMovimentoFinaceiro.ENVIO_ENCALHE
					)
				);
		
		List<Long> tiposMovimentoEncargos =
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.JUROS, GrupoMovimentoFinaceiro.MULTA));
		
		List<Long> tiposMovimentoPostergadoCredito =
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.POSTERGADO_CREDITO));
		
		List<Long> tiposMovimentoPostergadoDebito = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.POSTERGADO_DEBITO));
		
		List<Long> tipoMovimentoVendaEncalhe = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.COMPRA_ENCALHE_SUPLEMENTAR));
		
		List<Long> tipoMovimentoVendaPendente = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.PENDENTE));
		
		List<Long> tiposMovimentoConsignado = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
					Arrays.asList(
						GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE
					)
				);
		
		List<Long> tiposMovimentoNegociacaoComissao = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
					Arrays.asList(
						GrupoMovimentoFinaceiro.NEGOCIACAO_COMISSAO
					)
				);
		
		return this.consolidadoFinanceiroRepository.obterContaCorrente(filtro, 
				tiposMovimentoCredito, tiposMovimentoDebito, tipoMovimentoEncalhe, 
				tiposMovimentoEncargos, tiposMovimentoPostergadoCredito, 
				tiposMovimentoPostergadoDebito, tipoMovimentoVendaEncalhe,
				tiposMovimentoConsignado, tipoMovimentoVendaPendente,
				tiposMovimentoNegociacaoComissao);
	}
	
	@Override
	@Transactional(readOnly=true)
	public BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro){
		return this.consolidadoFinanceiroRepository.countObterContaCorrente(filtro);
	}
}
