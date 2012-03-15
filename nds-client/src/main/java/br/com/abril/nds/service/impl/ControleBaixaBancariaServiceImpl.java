package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.service.ControleBaixaBancariaService;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.financeiro.ControleBaixaBancaria}  
 * 
 * @author Discover Technology
 */
@Service
public class ControleBaixaBancariaServiceImpl implements ControleBaixaBancariaService {
	
	@Autowired
	private ControleBaixaBancariaRepository controleBaixaRepository;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ControleBaixaBancaria alterarControleBaixa(StatusControle statusControle,
													  Date dataOperacao, Usuario usuario) {
		
		ControleBaixaBancaria controleBaixa =
			controleBaixaRepository.obterPorData(dataOperacao);
		
		if (controleBaixa == null) {
			controleBaixa = new ControleBaixaBancaria();
			
			controleBaixa.setData(dataOperacao);
			controleBaixa.setResponsavel(usuario);
		}
		
		controleBaixa.setStatus(statusControle);
		
		return controleBaixaRepository.merge(controleBaixa);
	}
	
}
