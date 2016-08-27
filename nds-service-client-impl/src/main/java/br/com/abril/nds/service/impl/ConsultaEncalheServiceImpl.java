package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoArquivo;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.AnexoEmail.TipoAnexo;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.PDFUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.DebitoCreditoCotaVO;

@Service
public class ConsultaEncalheServiceImpl implements ConsultaEncalheService {

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private ControleConferenciaEncalheCotaRepository controleConferenciaEncalheCotaRepository;
	
	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;
	
	@Autowired
	private DocumentoCobrancaService documentoCobrancaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EmailService emailService;

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional(readOnly = true)
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		Integer qtdeConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdeConsultaEncalhe(filtro);
		
		if (qtdeConsultaEncalhe == null || qtdeConsultaEncalhe.equals(0)) {
			
			return info;
		}
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		BigDecimal valorTotalReparte = BigDecimal.ZERO;
		
		BigDecimal valorTotalEncalhe = BigDecimal.ZERO;
		
		BigDecimal valorVendaDia = BigDecimal.ZERO;

		if(listaConsultaEncalhe != null && !listaConsultaEncalhe.isEmpty()) {

			ConsultaEncalheDTO totalReparteEncalhe = movimentoEstoqueCotaRepository.obterValorTotalReparteEncalheDataCotaFornecedor(filtro);
			
			valorTotalReparte = totalReparteEncalhe.getReparte();
			
			valorTotalEncalhe = totalReparteEncalhe.getEncalhe();
			
			valorVendaDia = (valorTotalReparte==null?BigDecimal.ZERO:valorTotalReparte).subtract(valorTotalEncalhe==null?BigDecimal.ZERO:valorTotalEncalhe);
		}
		
		List<DebitoCreditoCota> listaDebitoCreditoCotaDTO = new ArrayList<DebitoCreditoCota>();
		
		List<Long> listaIdControleConfEncalheCota = controleConferenciaEncalheCotaRepository.obterListaIdControleConferenciaEncalheCota(filtro);
		
		if(listaIdControleConfEncalheCota != null && !listaIdControleConfEncalheCota.isEmpty()) {

			for(Long idControleConfEncalheCota : listaIdControleConfEncalheCota) {
				
				ControleConferenciaEncalheCota controleConfEncalheCota = controleConferenciaEncalheCotaRepository.buscarPorId(idControleConfEncalheCota);
				
				if(controleConfEncalheCota != null) {
					
					List<DebitoCreditoCota> listDebCred = 
					        conferenciaEncalheService.obterDebitoCreditoDeCobrancaPorOperacaoEncalhe(
					                controleConfEncalheCota,
					                filtro.getIdFornecedor());
				
					if(listDebCred!= null && !listDebCred.isEmpty()) {
						listaDebitoCreditoCotaDTO.addAll(listDebCred);
					}
				}
			}
			
		}
		
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		
		if (listaDebitoCreditoCotaDTO != null) {
			
			for (DebitoCreditoCota debitoCreditoCotaDTO: listaDebitoCreditoCotaDTO) {
				
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
		
		info.setValorVendaDia(valorVendaDia.setScale(2, RoundingMode.HALF_UP));
		
		info.setValorDebitoCredito(valorDebitoCredito.setScale(2, RoundingMode.HALF_UP));
		
		info.setValorPagar(valorPagar.setScale(2, RoundingMode.HALF_UP));
		
		info.setValorReparte(valorTotalReparte.setScale(2, RoundingMode.HALF_UP));
		
		info.setValorEncalhe(valorTotalEncalhe.setScale(2, RoundingMode.HALF_UP));
		
		return info;
	}
	
	
	@Transactional(readOnly = true)
	public InfoConsultaEncalheDTO pesquisarReparte(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaReparte(filtro);
		
		
		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		
		
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
		
		Date dataOperacao = filtro.getDataRecolhimento();
		
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

		List<Integer> listaCotas = controleConferenciaEncalheCotaRepository.obterListaNumCotaConferenciaEncalheCota(filtro);
		
		if (listaCotas != null && !listaCotas.isEmpty() ) {

			List<byte[]> arquivos = new ArrayList<byte[]>();
			
			List<Date> datas = new ArrayList<>();
			
			datas.add(filtro.getDataRecolhimentoInicial());
			
			List<Integer> numeroCotasExclusao = new ArrayList<>();
			
			for (Integer numCota : listaCotas) {
				
				Cota cota = cotaService.obterPorNumeroDaCota(numCota);
				
				if(filtro.getNumCota() == null){
					if(cota.getParametroDistribuicao().getUtilizaDocsParametrosDistribuidor()){
						if(filtro.isDistribEnviaEmail()){
							enviarSlipPorEmail(filtro, cota);
						}
					}else{
						if(!Util.validarBoolean(cota.getParametroDistribuicao().getSlipImpresso())){
							numeroCotasExclusao.add(numCota);
						}else{
							if(Util.validarBoolean(cota.getParametroDistribuicao().getSlipEmail())){
								enviarSlipPorEmail(filtro, cota);
							}
						}
					}
				}
			}
			
			if(!numeroCotasExclusao.isEmpty()){
				listaCotas.removeAll(numeroCotasExclusao);
			}
			
			this.documentoCobrancaService.gerarSlipCobranca(arquivos, listaCotas, filtro.getDataRecolhimentoInicial(), filtro.getDataRecolhimentoFinal(), false, TipoArquivo.PDF);
			
			if (arquivos.size() == 1) {
			
				retorno = arquivos.get(0);
			
			} else if (arquivos.size() > 1) {

				retorno = PDFUtil.mergePDFs(arquivos);
			}
		}

		return retorno;
	}


	private void enviarSlipPorEmail(FiltroConsultaEncalheDTO filtro, Cota cota){
		
		List<byte[]> arquivosCota = new ArrayList<byte[]>();
		
		List<Integer> numeroCotaList = new ArrayList<>();
		numeroCotaList.add(cota.getNumeroCota());
		
		this.documentoCobrancaService.gerarSlipCobranca(arquivosCota, numeroCotaList, filtro.getDataRecolhimentoInicial(), filtro.getDataRecolhimentoFinal(), false, TipoArquivo.PDF);
		
		byte[] retornoArquivoCota = null; 
		
		retornoArquivoCota = arquivosCota.size() > 1 ? retornoArquivoCota = PDFUtil.mergePDFs(arquivosCota) : arquivosCota.get(0);
		
		String[] listaDeDestinatarios = {cota.getPessoa().getEmail()};
		AnexoEmail anexoPDF = new AnexoEmail("SLIP – COTA "+cota.getNumeroCota(), retornoArquivoCota, TipoAnexo.PDF);
		
		try {
			emailService.enviar("[NDS] - Emissão SLIP", "Olá, segue em anexo o slip emitido pela consulta de encalhe.", listaDeDestinatarios, anexoPDF);
			System.out.println("Envio EMAIL SLIP COTA - "+cota.getNumeroCota());
		} catch (AutenticacaoEmailException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Coverte um objeto List<DebitoCreditoCotaDTO> em List<DebitoCreditoCotaVO>.
	 * 
	 * @param listaDebitoCreditoCotaDTO
	 * @return
	 */
	private List<DebitoCreditoCotaVO> carregaDebitoCreditoCotaVO(List<DebitoCreditoCota> listaDebitoCreditoCotaDTO) {
		List<DebitoCreditoCotaVO> listaDebitoCreditoCotaVO = new ArrayList<DebitoCreditoCotaVO>();
		DebitoCreditoCotaVO debitoCreditoCotaVO;
		
		if(listaDebitoCreditoCotaDTO != null) {
			for(DebitoCreditoCota debitoCreditoCotaDTO: listaDebitoCreditoCotaDTO) {
				
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
