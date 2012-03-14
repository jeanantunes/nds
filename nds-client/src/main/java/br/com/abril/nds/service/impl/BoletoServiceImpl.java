package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.DistribuidorService;

@Service
public class BoletoServiceImpl implements BoletoService {

	@Autowired
	private BoletoRepository boletoRepository;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Transactional
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterBoletosPorCota(filtro);
	}
	
	@Transactional
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {
		return this.boletoRepository.obterQuantidadeBoletosPorCota(filtro);
	}
	
	public void baixarBoletos(List<PagamentoDTO> listaPagamento) {
		for (PagamentoDTO pagamento : listaPagamento) {
			baixarBoleto(pagamento);
		}
	}
	
	public void baixarBoleto(PagamentoDTO pagamentoDTO) {
		
		Date dataOperacao = obterDataOperacao();
		
		Boleto boleto = boletoRepository.obterPorNossoNumero(pagamentoDTO.getNossoNumero());
		
		if (boleto != null) {
			
			if (boleto.getStatusCobranca().equals(StatusCobranca.PAGO)) {
				throw new IllegalArgumentException("Boleto já foi pago!");
			}
			
			if (boleto.getDataVencimento().compareTo(dataOperacao) > 0) {
				throw new IllegalArgumentException("Boleto pago fora da data!");
			}
			
			boleto.setDataPagamento(dataOperacao);
			boleto.setStatusCobranca(StatusCobranca.PAGO);
			
			boletoRepository.alterar(boleto);
		} else {
			gerarLogPagamento();
			throw new IllegalArgumentException("Boleto não encontrado!");
		}
	}
	
	private void gerarLogPagamento() {
		
	}
	
	private Date obterDataOperacao() {
		
		Distribuidor distribuidor = distribuidorService.obter();
		
		return distribuidor.getDataOperacao();
	}

}
