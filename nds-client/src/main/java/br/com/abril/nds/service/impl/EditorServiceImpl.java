package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.service.EditorService;

@Service
public class EditorServiceImpl implements EditorService {

	@Autowired
	private EditorRepository editorRepository;
	
	@Override
	@Transactional
	public List<Editor> obterEditores() {
		return editorRepository.obterEditores();
	}

}
