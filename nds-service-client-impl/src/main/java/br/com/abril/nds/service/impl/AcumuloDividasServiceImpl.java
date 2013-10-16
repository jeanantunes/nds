package br.com.abril.nds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.abril.nds.model.financeiro.AcumuloDivida;
import br.com.abril.nds.repository.AcumuloDividasRepository;
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorMovimentoPendente(Long idMovimentoPendente) {

		return this.acumuloDividasRepository.obterAcumuloDividaPorMovimentoFinanceiro(idMovimentoPendente);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AcumuloDivida obterAcumuloDividaPorDivida(Long idDivida) {
		
		return this.acumuloDividasRepository.obterAcumuloDividaPorDivida(idDivida);
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
	public AcumuloDivida atualizarAcumuloDivida(AcumuloDivida acumuloDivida) {

		return this.acumuloDividasRepository.merge(acumuloDivida);
	}
}
