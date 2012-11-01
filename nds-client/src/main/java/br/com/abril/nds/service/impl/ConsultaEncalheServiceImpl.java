package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.util.PDFUtil;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheRodapeDTO;
import br.com.abril.nds.dto.DebitoCreditoCotaDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.OperacaoFinaceira;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.repository.ControleConferenciaEncalheCotaRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.TipoMovimentoFinanceiroRepository;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
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
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.service.ConsultaEncalheService#pesquisarEncalhe(br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO)
	 */
	@Transactional(readOnly = true)
	public InfoConsultaEncalheDTO pesquisarEncalhe(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO info = new InfoConsultaEncalheDTO();
		
		List<ConsultaEncalheDTO> listaConsultaEncalhe = movimentoEstoqueCotaRepository.obterListaConsultaEncalhe(filtro);
		
		Integer qtdeRegistrosConsultaEncalhe = movimentoEstoqueCotaRepository.obterQtdConsultaEncalhe(filtro);
		
		ConsultaEncalheRodapeDTO consultaEncalheRodapeDTO = movimentoEstoqueCotaRepository.obterValoresTotais(filtro);

		BigDecimal valorVendaDia = consultaEncalheRodapeDTO.getValorReparte().subtract(consultaEncalheRodapeDTO.getValorEncalhe());
		
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroEnvioEncalhe = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.ENVIO_ENCALHE);
		TipoMovimentoFinanceiro tipoMovimentoFinanceiroRecebimentoReparte = tipoMovimentoFinanceiroRepository.buscarTipoMovimentoFinanceiro(GrupoMovimentoFinaceiro.RECEBIMENTO_REPARTE);
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroIgnorados = new ArrayList<TipoMovimentoFinanceiro>();
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroEnvioEncalhe);
		tiposMovimentoFinanceiroIgnorados.add(tipoMovimentoFinanceiroRecebimentoReparte);
		
		List<DebitoCreditoCotaDTO> listaDebitoCreditoCota = movimentoFinanceiroCotaRepository.obterDebitoCreditoPorPeriodoOperacao(filtro, tiposMovimentoFinanceiroIgnorados);
		
		BigDecimal valorDebitoCredito = BigDecimal.ZERO;
		
		if(listaDebitoCreditoCota != null) {
			for(DebitoCreditoCotaDTO debitoCreditoCotaDTO: listaDebitoCreditoCota) {
				if(OperacaoFinaceira.CREDITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
					valorDebitoCredito = valorDebitoCredito.add(debitoCreditoCotaDTO.getValor());
				} else if(OperacaoFinaceira.DEBITO.equals(debitoCreditoCotaDTO.getTipoLancamento())) {
					valorDebitoCredito = valorDebitoCredito.subtract(debitoCreditoCotaDTO.getValor());
				}
			}
		}
		
		BigDecimal valorPagar = valorVendaDia.add(valorDebitoCredito);
		
		info.setListaConsultaEncalhe(listaConsultaEncalhe);
		
		info.setListaDebitoCreditoCota(carregaDebitoCreditoCotaVO(listaDebitoCreditoCota));
		
		info.setQtdeConsultaEncalhe(qtdeRegistrosConsultaEncalhe);
		
		info.setValorReparte(consultaEncalheRodapeDTO.getValorReparte());
		
		info.setValorEncalhe(consultaEncalheRodapeDTO.getValorEncalhe());
		
		info.setValorVendaDia(valorVendaDia);
		
		info.setValorDebitoCredito(valorDebitoCredito);
		
		info.setValorPagar(valorPagar);
		
		return info;
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
		
		List<ControleConferenciaEncalheCota> listaConferenciaEncalheCotas = 
				controleConferenciaEncalheCotaRepository.obterControleConferenciaEncalheCotaPorFiltro(filtro); 
		
		if (listaConferenciaEncalheCotas != null) {
			for(ControleConferenciaEncalheCota conferenciaEncalheCota: listaConferenciaEncalheCotas) {
				arquivo = conferenciaEncalheService.gerarSlip(conferenciaEncalheCota.getId(), false);
				if(retorno == null) {
					retorno = arquivo;
				} else {
					List<byte[]> arquivos = new ArrayList<byte[]>();
					arquivos.add(retorno);
					arquivos.add(arquivo);
					retorno = PDFUtil.mergePDFs(arquivos);
				}
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
