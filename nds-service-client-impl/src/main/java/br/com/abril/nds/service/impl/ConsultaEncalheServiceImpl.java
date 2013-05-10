package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.vo.DebitoCreditoCotaVO;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private TipoMovimentoFinanceiroRepository tipoMovimentoFinanceiroRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private ChamadaEncalheCotaRepository chamadaEncalheCotaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	private void carregarDiaRecolhimento(List<ConsultaEncalheDTO> listaConsultaEncalhe) {
		
		if(listaConsultaEncalhe == null || listaConsultaEncalhe.isEmpty()) {
			return;
		}
		
		for(ConsultaEncalheDTO consultaEncalhe : listaConsultaEncalhe) {
			
			Date dataConferencia = consultaEncalhe.getDataMovimento();
			
			Date dataRecolhimento = consultaEncalhe.getDataDoRecolhimentoDistribuidor();
			
			if(dataRecolhimento != null && dataConferencia != null) {

				Integer dia = distribuidorService.obterDiaDeRecolhimentoDaData(dataConferencia, dataRecolhimento, consultaEncalhe.getIdProdutoEdicao());
				
				consultaEncalhe.setRecolhimento(dia);				
			} else {
				
				consultaEncalhe.setRecolhimento(0);
			}
			
		}
		
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional(readOnly = true)
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		carregarDiaRecolhimento(listaConsultaEncalhe);
		
		Integer qtdeConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
		BigDecimal valorTotalReparte = BigDecimal.ZERO;
		
		BigDecimal valorTotalEncalhe = BigDecimal.ZERO;

		valorTotalReparte = 
				chamadaEncalheCotaRepository.obterReparteDaChamaEncalheCotaNoPeriodo(
						filtro.getIdCota(), 
						filtro.getDataRecolhimentoInicial(), 
						filtro.getDataRecolhimentoFinal(), false);
		
		valorTotalEncalhe = movimentoEstoqueCotaRepository.obterValorTotalEncalhe(filtro);
		
		BigDecimal valorVendaDia = valorTotalReparte.subtract(valorTotalEncalhe); 
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaDTO = new ArrayList<DebitoCreditoCotaDTO>();
		
		List<Long> listaIdControleConfEncalheCota = 
				controleConferenciaEncalheCotaRepository.obterListaIdControleConferenciaEncalheCota(filtro);
		
		if(listaIdControleConfEncalheCota != null && !listaIdControleConfEncalheCota.isEmpty()) {

			for(Long idControleConfEncalheCota : listaIdControleConfEncalheCota) {
				
				ControleConferenciaEncalheCota controleConfEncalheCota = controleConferenciaEncalheCotaRepository.buscarPorId(idControleConfEncalheCota);
				
				if(controleConfEncalheCota != null) {
					
					List<DebitoCreditoCotaDTO> listDebCred = conferenciaEncalheService.obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(controleConfEncalheCota);
				
					if(listDebCred!= null && !listDebCred.isEmpty()) {
						listaDebitoCreditoCotaDTO.addAll(listDebCred);
					}
				}
			}
			
		}
		
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		
		if (listaDebitoCreditoCotaDTO != null) {
			
			for (DebitoCreditoCotaDTO debitoCreditoCotaDTO: listaDebitoCreditoCotaDTO) {
				
				if (OperacaoFinaceira.DEBITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
					
					valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
					
				} else if (OperacaoFinaceira.CREDITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
					
					valorDebitoCredito = valorDebitoCredito.subtract(debitoCreditoCotaDTO.getValor());
				}
			}
		}
		
		BigDecimal valorPagar = valorVendaDia.add(valorDebitoCredito);
		
		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setQtdeConsultaEncalhe(qtdeConsultaEncalhe);
		
		info.setListaDebitoCreditoCota(carregaDebitoCreditoCotaVO(listaDebitoCreditoCotaDTO));
		
		info.setValorVendaDia(valorVendaDia);
		
		info.setValorDebitoCredito(valorDebitoCredito);
		
		info.setValorPagar(valorPagar);
		
		info.setValorReparte(valorTotalReparte);
		
		info.setValorEncalhe(valorTotalEncalhe);
		
		return info;
	}
	
	@Transactional
	public ConsultaEncalheRodapeDTO obterResultadosConsultaEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		return this.movimentoEstoqueCotaRepository.obterValoresTotais(filtro);
	}

	@Transactional
	public InfoConsultaEncalheDetalheDTO pesquisarEncalheDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {
		
		InfoConsultaEncalheDetalheDTO info = new InfoConsultaEncalheDetalheDTO();
		
		List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalheDetalhe(filtro);
		
		Integer qtdeRegistrosConsultaEncalheDetalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalheDetalhe(filtro);
		
		Date dataOperacao = filtro.getDataMovimento();
		
		ProdutoEdicao produtoEdicao = produtoEdicaoRepository.buscarPorId(filtro.getIdProdutoEdicao());
		
		info.setListaConsultaEncalheDetalhe(listaConsultaEncalheDetalhe);
		
		info.setQtdeConsultaEncalheDetalhe(qtdeRegistrosConsultaEncalheDetalhe);
		
		info.setDataOperacao(dataOperacao);
		
		info.setProdutoEdicao(produtoEdicao);
		
		return info;
	}
	
	@Transactional
	public byte[] gerarDocumentosConferenciaEncalhe(FiltroConsultaEncalheDTO filtro) {
		byte[] retorno = null; 
		byte[] arquivo; 
	
		List<Long> listaConferenciaEncalheCotas = 
				controleConferenciaEncalheCotaRepository.obterListaIdControleConferenciaEncalheCota(filtro);
		
		if (listaConferenciaEncalheCotas != null) {
			
			List<byte[]> arquivos = new ArrayList<byte[]>();
			
			for(Long idControleConferenciaEncalheCota : listaConferenciaEncalheCotas) {
			
				arquivo = conferenciaEncalheService.gerarSlip(idControleConferenciaEncalheCota, false, TipoArquivo.PDF);
				
				arquivos.add(arquivo);
			
			}

			if (arquivos.size() == 1) {
			
				retorno = arquivos.get(0);
			
			} else if (arquivos.size() > 1) {

				retorno = PDFUtil.mergePDFs(arquivos);
			}
		}

		return retorno;
	}
	
	/**
	 * Coverte um objeto List<DebitoCreditoCotaDTO> em List<DebitoCreditoCotaVO>.
	 * 
	 * @param listaDebitoCreditoCotaDTO
	 * @return
	 */
	private List<DebitoCreditoCotaVO> carregaDebitoCreditoCotaVO(List<DebitoCreditoCotaDTO> listaDebitoCreditoCotaDTO) {
		List<DebitoCreditoCotaVO> listaDebitoCreditoCotaVO = new ArrayList<DebitoCreditoCotaVO>();
		DebitoCreditoCotaVO debitoCreditoCotaVO;
		
		if(listaDebitoCreditoCotaDTO != null) {
			for(DebitoCreditoCotaDTO debitoCreditoCotaDTO: listaDebitoCreditoCotaDTO) {
				
				String tipoLancamento	= (debitoCreditoCotaDTO.getTipoLancamento() != null) ? debitoCreditoCotaDTO.getTipoLancamento().toString() : ""; 
				String dataLancamento	= (debitoCreditoCotaDTO.getDataLancamento() != null) ? DateUtil.formatarDataPTBR(debitoCreditoCotaDTO.getDataLancamento()) : "";
				String observacoes		= (debitoCreditoCotaDTO.getObservacoes() != null) ? debitoCreditoCotaDTO.getObservacoes() : "";
				String valor			= (debitoCreditoCotaDTO.getValor() != null) ? CurrencyUtil.formatarValor(debitoCreditoCotaDTO.getValor()) : "";
				
				debitoCreditoCotaVO = new DebitoCreditoCotaVO();
				
				debitoCreditoCotaVO.setTipoLancamento(tipoLancamento);
				debitoCreditoCotaVO.setDataLancamento(dataLancamento);
				debitoCreditoCotaVO.setObservacoes(observacoes);
				debitoCreditoCotaVO.setValor(valor);
				
				listaDebitoCreditoCotaVO.add(debitoCreditoCotaVO);				
			}
		}
		
		return listaDebitoCreditoCotaVO;
	}
	
}
