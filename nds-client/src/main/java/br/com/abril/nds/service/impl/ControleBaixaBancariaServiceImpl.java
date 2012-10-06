package br.com.abril.nds.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.financeiro.ControleBaixaBancaria;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.BancoRepository;
import br.com.abril.nds.repository.ControleBaixaBancariaRepository;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.util.TipoMensagem;

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
	
	@Autowired
	private BancoRepository bancoRepository;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void alterarControleBaixa(StatusControle statusControle, Date dataOperacao,
									 Usuario usuario, Banco banco) {
		
		if (banco == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, 
				"Banco não informado!");
		}
		
		ControleBaixaBancaria controleBaixa =
			this.controleBaixaRepository.obterControleBaixaBancaria(dataOperacao, banco);
		
		if (controleBaixa == null) {
			
			controleBaixa = new ControleBaixaBancaria();
			
			controleBaixa.setData(dataOperacao);
			controleBaixa.setResponsavel(usuario);
			controleBaixa.setBanco(banco);
		}
		
		controleBaixa.setStatus(statusControle);
		
		this.controleBaixaRepository.merge(controleBaixa);
	}
	
}
