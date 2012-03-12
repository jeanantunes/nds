package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.service.EstudoCotaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstudoCotaServiceImpl implements EstudoCotaService {

	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Transactional(readOnly = true)
	public EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		return this.estudoCotaRepository.obterEstudoCota(numeroCota, dataReferencia);
	}

	@Transactional(readOnly = true)
	public EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, 
																  Long idProdutoEdicao,
																  Integer numeroCota) {
		
		return this.estudoCotaRepository.obterEstudoCotaDeLancamentoComEstudoFechado(dataLancamentoDistribuidor, idProdutoEdicao, numeroCota);
	}

}
