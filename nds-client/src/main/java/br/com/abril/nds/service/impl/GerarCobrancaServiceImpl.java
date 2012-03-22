package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.repository.ControleConferenciaEncalheRepository;
import br.com.abril.nds.repository.MovimentoFinanceiroCotaRepository;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.util.TipoMensagem;

@Service
public class GerarCobrancaServiceImpl implements GerarCobrancaService {

	@Autowired
	private MovimentoFinanceiroCotaRepository movimentoFinanceiroCotaRepository;
	
	@Autowired
	private ControleConferenciaEncalheRepository controleConferenciaEncalheRepository;
	
	@Override
	@Transactional
	public void gerarCobranca(Long idCota) {
		
		// verificar se a operação de conferencia ja foi concluida
		StatusOperacao statusOperacao = this.controleConferenciaEncalheRepository.obterStatusConferenciaDataOperacao();
		
		if (statusOperacao != null && statusOperacao.equals(StatusOperacao.CONCLUIDO)){
		
			// buscar movimentos financeiros da cota para a data de operação em andamento
			List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
					this.movimentoFinanceiroCotaRepository.obterMovimentoFinanceiroCotaDataOperacao(idCota);
			
			if (listaMovimentoFinanceiroCota != null &&
					!listaMovimentoFinanceiroCota.isEmpty()){
				
			}
		} else {
			throw new ValidacaoException(TipoMensagem.ERROR, "A conferência de box de encalhe deve ser concluída antes de gerar dívidas.");
		}
	}

}
