package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;

@Service
public class DebitoCreditoCotaServiceImpl implements DebitoCreditoCotaService {

	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;

	@Autowired
	private CotaRepository cotaRepository;
	
	@Autowired
	private BoxRepository boxRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Override
	@Transactional
	public MovimentoFinanceiroCotaDTO gerarMovimentoFinanceiroCotaDTO(
			DebitoCreditoDTO debitoCredito) {

		Long idMovimento = debitoCredito.getId();

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = new MovimentoFinanceiroCotaDTO();
		
		movimentoFinanceiroCotaDTO.setIdMovimentoFinanceiroCota(idMovimento);
		
		movimentoFinanceiroCotaDTO.setDataCriacao(DateUtil.removerTimestamp(new Date()));

		Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
		
		movimentoFinanceiroCotaDTO.setDataVencimento(dataVencimento);

		movimentoFinanceiroCotaDTO.setValor(new BigDecimal(debitoCredito.getValor()));

		movimentoFinanceiroCotaDTO.setObservacao(debitoCredito.getObservacao());

		TipoMovimentoFinanceiro tipoMovimentoFinanceiro =
				this.tipoMovimentoFinanceiroRepository.buscarPorId(debitoCredito.getTipoMovimentoFinanceiro().getId());

		movimentoFinanceiroCotaDTO.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);
		
		Cota cota = this.cotaRepository.obterPorNumerDaCota(debitoCredito.getNumeroCota());
		
		movimentoFinanceiroCotaDTO.setCota(cota);

		Usuario usuario = this.usuarioRepository.buscarPorId(debitoCredito.getIdUsuario());
		
		movimentoFinanceiroCotaDTO.setUsuario(usuario);
		
		movimentoFinanceiroCotaDTO.setLancamentoManual(true);

		return movimentoFinanceiroCotaDTO;
	}

	
	/**
	 * Obtém dados pré-configurados com informações das Cotas do Box, Rota e Roteiro. Para lançamentos de débitos e/ou créditos
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return List<DebitoCreditoDTO>
	 */
	@Override
	@Transactional(readOnly=true)
	public List<DebitoCreditoDTO> obterDadosLancamentoPorBoxRoteiroRota(Long idBox,Long idRoteiro,Long idRota,BigDecimal percentual,BaseCalculo baseCalculo,Date dataPeriodoInicial,Date dataPeriodoFinal) {
		
		List<DebitoCreditoDTO> listaDC = new ArrayList<DebitoCreditoDTO>();
		
		List<Cota> cotas = this.boxRepository.obterCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
		
		BigDecimal percFat;
		
		BigDecimal percTotal = new BigDecimal(100);
		
		Map<Long,BigDecimal> cotasFaturamentos = null;
		
		Long indice=0l;
		for (Cota itemCota:cotas){

			percFat = null;
			
			indice++;

			if((percentual!=null)&&( baseCalculo!=null)&&( dataPeriodoInicial!=null)&&(dataPeriodoFinal!=null)){
				
				cotasFaturamentos = this.movimentoFinanceiroCotaService.obterFaturamentoCotasPeriodo(cotas, baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
				
				if (cotasFaturamentos!=null && cotasFaturamentos.get(itemCota.getId())!=null){
					
					if(cotasFaturamentos.get(itemCota.getId()).doubleValue() > 0 ){
				        percFat = ((cotasFaturamentos.get(itemCota.getId()).divide(percTotal)).multiply(percentual));
					}
					
				}
			}
			
			listaDC.add(new DebitoCreditoDTO(indice,null,null,itemCota.getNumeroCota(),itemCota.getPessoa().getNome(),null,null,(percFat!=null?CurrencyUtil.formatarValor(percFat.doubleValue()):null),null));	
		}
		return listaDC;
	}


	/**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	@Override
	@Transactional(readOnly=true)
	public int obterQuantidadeCotasPorBoxRoteiroRota(Long idBox,
			Long idRoteiro, Long idRota) {
		return (int) this.boxRepository.obterQuantidadeCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
	}


	/**
	 * Obtém dados pré-configurados com informações da Cota para lançamento de débito e/ou crédito
	 * @param numeroCota
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 * @return DebitoCreditoDTO
	 */
	@Override
	@Transactional(readOnly=true)
	public DebitoCreditoDTO obterDadosLancamentoPorCota(Integer numeroCota,
			BigDecimal percentual, BaseCalculo baseCalculo,
			Date dataPeriodoInicial, Date dataPeriodoFinal, Long index) {
		
		DebitoCreditoDTO dc = new DebitoCreditoDTO();
		Long indice=index;
		Cota cota = null;
		
		if (numeroCota!=null){
		
			cota = this.cotaRepository.obterPorNumerDaCota(numeroCota);
			
			if (cota!=null){
			
				BigDecimal percFat = null;
				
				BigDecimal percTotal = new BigDecimal(100);
				
				Map<Long,BigDecimal> cotasFaturamentos = null;
		
				if((percentual!=null)&&( baseCalculo!=null)&&( dataPeriodoInicial!=null)&&(dataPeriodoFinal!=null)){
					
					cotasFaturamentos = this.movimentoFinanceiroCotaService.obterFaturamentoCotasPeriodo(Arrays.asList(cota), baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
					
					if (cotasFaturamentos!=null && cotasFaturamentos.get(cota.getId())!=null){
						
						if(cotasFaturamentos.get(cota.getId()).doubleValue() > 0 ){
					        percFat = ((cotasFaturamentos.get(cota.getId()).divide(percTotal)).multiply(percentual));
						}
						
					}
				}
				
				dc = new DebitoCreditoDTO(indice,null,null,cota.getNumeroCota(),cota.getPessoa().getNome(),null,null,(percFat!=null?CurrencyUtil.formatarValor(percFat.doubleValue()):null),null);
			}
		}

		return dc;
	}
	
}
