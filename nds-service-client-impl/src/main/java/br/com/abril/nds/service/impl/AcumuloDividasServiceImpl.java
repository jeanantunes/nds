package br.com.abril.nds.service.impl;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.StatusDivida;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.AcumuloDividasRepository;
import br.com.abril.nds.repository.DividaRepository;
import br.com.abril.nds.service.AcumuloDividasService;

/**
 * Implementação do serviço responsável pelo acumulo de dívidas.
 * 
 * @author Discover Technology
 *
 */
@Service
public class AcumuloDividasServiceImpl implements AcumuloDividasService {

	@Autowired
	private AcumuloDividasRepository acumuloDividasRepository;
	
	@Autowired
	private DividaRepository dividaRepository;
	
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
	@Transactional(readOnly=true)
	public AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida) {
		
		return this.acumuloDividasRepository.obterAcumuloDividaPorDivida(idDivida);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void criarAcumuloDivida(AcumuloDivida acumuloDivida) {

		this.acumuloDividasRepository.adicionar(acumuloDivida);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public AcumuloDivida atualizarAcumuloDivida(AcumuloDivida acumuloDivida) {

		return this.acumuloDividasRepository.merge(acumuloDivida);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional(readOnly=true)
	public BigInteger obterNumeroMaximoAcumuloCota(Long idCota) {

		BigInteger numeroAcumulo = this.acumuloDividasRepository.obterNumeroMaximoAcumuloCota(idCota);
		
		return numeroAcumulo == null ? BigInteger.ZERO : numeroAcumulo;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void quitarDividasAcumuladas(Divida dividaAtual) {

		AcumuloDivida acumuloDivida = this.acumuloDividasRepository.obterAcumuloDividaPorDivida(dividaAtual.getId());
		
		if (acumuloDivida == null) {
			
			return;
		}
		
		acumuloDivida.setStatus(StatusInadimplencia.QUITADA);
		
		this.acumuloDividasRepository.alterar(acumuloDivida);
		
		dividaAtual.setStatus(StatusDivida.QUITADA);
		
		dividaAtual.getCobranca().setStatusCobranca(StatusCobranca.PAGO);
		
		this.dividaRepository.alterar(dividaAtual);
		
		for (MovimentoFinanceiroCota movimento : dividaAtual.getConsolidado().getMovimentos()) {
			
			AcumuloDivida acumuloDividaAnterior = this.acumuloDividasRepository.obterAcumuloDividaPorMovimentoFinanceiroPendente(movimento.getId());
			
			if (acumuloDividaAnterior != null) {
				
				quitarDividasAcumuladas(acumuloDividaAnterior.getDividaAnterior());
				
				break;
			}
		}
	}
}
