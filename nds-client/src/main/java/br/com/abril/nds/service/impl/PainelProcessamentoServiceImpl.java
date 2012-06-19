package br.com.abril.nds.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.service.PainelProcessamentoService;

/**
 * Classe de implementação de serviços referentes a entidade
 * @author infoA2
 */
@Service
public class PainelProcessamentoServiceImpl implements PainelProcessamentoService {

	@Override
	@Transactional(readOnly = true)
	public void obterInterfaces() {
		
	}

	@Override
	@Transactional(readOnly = true)
	public void obterProcessos() {
		
	}

}
