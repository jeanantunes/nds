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
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.repository.ConsolidadoFinanceiroRepository;
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
		return consolidadoFinanceiroRepository.obterMovimentoEstoqueCotaConsignado(filtro);
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
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiroPorOperacaoFinanceira(
						OperacaoFinaceira.CREDITO,
						null);
		
		List<Long> tiposMovimentoDebito =
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiroPorOperacaoFinanceira(
						OperacaoFinaceira.DEBITO,
						Arrays.asList(
								this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
										GrupoMovimentoFinaceiro.MULTA),
								this.tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(
										GrupoMovimentoFinaceiro.JUROS)));
		
		List<Long> tipoMovimentoEncalhe = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.ENVIO_ENCALHE));
		
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
		
		List<Long> tiposMovimentoConsignado = 
				this.tipoMovimentoFinanceiroRepository.buscarIdsTiposMovimentoFinanceiro(
						Arrays.asList(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE));
		
		return this.consolidadoFinanceiroRepository.obterContaCorrente(filtro, 
				tiposMovimentoCredito, tiposMovimentoDebito, tipoMovimentoEncalhe, 
				tiposMovimentoEncargos, tiposMovimentoPostergadoCredito, 
				tiposMovimentoPostergadoDebito, tipoMovimentoVendaEncalhe,
				tiposMovimentoConsignado);
	}
	
	@Override
	@Transactional(readOnly=true)
	public BigInteger countObterContaCorrente(FiltroViewContaCorrenteCotaDTO filtro){
		return this.consolidadoFinanceiroRepository.countObterContaCorrente(filtro);
	}
}
