package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.AcumuloDividasRepository;
import br.com.abril.nds.repository.CobrancaRepository;
import br.com.abril.nds.service.AcumuloDividasService;
import br.com.abril.nds.util.TipoBaixaCobranca;

/**
 * Implementação do serviço responsável pelo acumulo de dívidas.
 * 
 * @author Discover Technology
 * 
 */
@Service
@Transactional(readOnly = true)
public class AcumuloDividasServiceImpl implements AcumuloDividasService {

	@Autowired
	private AcumuloDividasRepository acumuloDividasRepository;
	
	@Autowired
	private CobrancaRepository cobrancaRepository;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorMovimentoPendente(Long idMovimentoPendente) {

		return this.acumuloDividasRepository.obterAcumuloDividaPorMovimentoFinanceiroPendente(idMovimentoPendente);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override

	public void criarAcumuloDivida(AcumuloDivida acumuloDivida) {

		this.acumuloDividasRepository.adicionar(acumuloDivida);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    @Transactional(readOnly = false)
	public AcumuloDivida atualizarAcumuloDivida(AcumuloDivida acumuloDivida) {

		return this.acumuloDividasRepository.merge(acumuloDivida);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroMaximoAcumuloCota(Long idCota) {

		BigInteger numeroAcumulo = this.acumuloDividasRepository.obterNumeroMaximoAcumuloCota(idCota);
		
		return numeroAcumulo == null ? BigInteger.ZERO : numeroAcumulo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
    @Transactional(readOnly = false)
	public void quitarDividasAcumuladas(Date dataPagamento, Divida dividaAtual, TipoBaixaCobranca tipoBaixa) {
		
		if (dividaAtual == null) 			
			return;
		

		List<AcumuloDivida> acumuloDividas =
			this.acumuloDividasRepository.obterAcumuloDividaPorDivida(dividaAtual.getId());
		
		if (acumuloDividas.isEmpty()) 			
			return;
				
		for (AcumuloDivida acumuloDivida : acumuloDividas) {
			
			acumuloDivida.setStatus(StatusInadimplencia.QUITADA);
			
			this.acumuloDividasRepository.alterar(acumuloDivida);
			
			dividaAtual = acumuloDivida.getDividaAnterior();
					
			dividaAtual.getCobranca().setDataPagamento(dataPagamento);
			
			dividaAtual.getCobranca().setTipoBaixa(tipoBaixa);
				
			this.cobrancaRepository.alterar(dividaAtual.getCobranca());
			
			this.quitarDividasAcumuladas(dataPagamento, dividaAtual,tipoBaixa);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigInteger obterNumeroDeAcumulosDivida(Long idConsolidadoFinanceiroCota) {
		
		BigInteger numeroDeAcumulosDivida =
			this.acumuloDividasRepository.obterNumeroDeAcumulosDivida(idConsolidadoFinanceiroCota);
		
		if (numeroDeAcumulosDivida == null) {
			
			return BigInteger.ZERO;
		}
		
		return numeroDeAcumulosDivida; 
	}
	
}
