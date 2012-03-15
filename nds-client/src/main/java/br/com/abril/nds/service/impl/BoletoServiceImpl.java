package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.DistribuidorService;

@Service
public class BoletoServiceImpl implements BoletoService {

	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private PoliticaCobrancaRepository politicaCobrancaRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ControleBaixaBancariaService controleBaixaService;
	
	@Transactional
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	@Transactional
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	@Override
	@Transactional
	public void baixarBoletos(ArquivoPagamentoBancoDTO arquivoPagamento, Usuario usuario) {
		
		Date dataOperacao = obterDataOperacao();
		
		//TODO: verificar se já foi feita uma baixa automática no dia
		
		PoliticaCobranca politicaCobranca =
			politicaCobrancaRepository.obterPorTipoCobranca(TipoCobranca.BOLETO);
		
		//TODO: politica de cobrança
		
		controleBaixaService.alterarControleBaixa(StatusControle.INICIADO,
												  dataOperacao, usuario);
		
		try {
			if (arquivoPagamento != null && arquivoPagamento.getListaPagemento() != null) {
				
				for (PagamentoDTO pagamento : arquivoPagamento.getListaPagemento()) {
				
					baixarBoleto(pagamento, dataOperacao);
				}
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO,
						  								  dataOperacao, usuario);
				
			} else {
				
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
						  								  dataOperacao, usuario);
			}
		} catch (Exception e) {
			
			controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS,
													  dataOperacao, usuario);
		}
	}
	
	@Transactional
	public void baixarBoleto(PagamentoDTO pagamento, Date dataOperacao) {
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamento.getNossoNumero());
		
		if (boleto != null) {
			
			if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
				
				gerarLogPagamento(TipoBaixaLog.PAGO_ANTERIORMENTE);
			}
			
			if (boleto.getDataVencimento().compareTo(dataOperacao) > 0) {
				
				gerarLogPagamento(TipoBaixaLog.FORA_DATA_PAGAMENTO);
			}
			
			if (!pagamento.getValorPagamento().equals(boleto.getValor())) {
				//TODO: pagar com divergência, dependendo do parâmetro
			}
			
			boleto.setDataPagamento(dataOperacao);
			boleto.setStatusCobranca(StatusCobranca.PAGO);
			
			boletoRepository.alterar(boleto);
			
			gerarLogPagamento(TipoBaixaLog.PAGO_SUCESSO);
			
		} else {
			
			gerarLogPagamento(TipoBaixaLog.NAO_ENCONTRADO);
		}
	}
	
	private void gerarLogPagamento(TipoBaixaLog tipoBaixaLog) {
		
		switch (tipoBaixaLog) {
		
			case PAGO_ANTERIORMENTE:
				
				break;
				
			case FORA_DATA_PAGAMENTO:
				
				break;
				
			case NAO_ENCONTRADO:
				
				break;
				
			case PAGO_SUCESSO:
				
				break;
				
			default:
				break;
		}
	}
	
	private Date obterDataOperacao() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		return distribuidor.getDataOperacao();
	}
	
	private enum TipoBaixaLog {
		
		PAGO_ANTERIORMENTE,
		FORA_DATA_PAGAMENTO,
		NAO_ENCONTRADO,
		PAGO_SUCESSO;
	}

}
