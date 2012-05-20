package br.com.abril.nds.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.abril.nds.model.cadastro.Editor;

/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Editor}
 * 
 * @author InfoA2
 */
@Service
public interface EditorService {

	public List<Editor> obterEditores();
	
}
