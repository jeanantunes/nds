package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.EstudoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Service
public class EstudoServiceImpl implements EstudoService {
	
	@Autowired
	private EstudoRepository estudoRepository;

	@Transactional(readOnly = true)
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		return this.estudoRepository.obterEstudoDoLancamentoPorDataProdutoEdicao(dataReferencia, idProdutoEdicao);
	}

	@Transactional(readOnly = true)
	@Override
	public Estudo obterEstudo(Long id) {
		return this.estudoRepository.buscarPorId(id);
	}

}
