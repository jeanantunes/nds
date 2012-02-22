package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.service.EstudoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.planejamento.Estudo}  
 * 
 * @author Discover Technology
 *
 */
public class EstudoServiceImpl implements EstudoService {
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Override
	public Estudo obterEstudoDoLancamentoMaisProximo(Date dataReferencia, String codigoProduto, Long numeroEdicao) {
		
		return this.estudoRepository.obterEstudoDoLancamentoMaisProximo(dataReferencia, codigoProduto, numeroEdicao);
	}

}
