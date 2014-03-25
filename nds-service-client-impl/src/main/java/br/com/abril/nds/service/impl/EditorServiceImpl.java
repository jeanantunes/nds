package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.repository.RankingRepository;
import br.com.abril.nds.service.EditorService;

@Service
public class EditorServiceImpl implements EditorService {

	@Autowired
	private EditorRepository editorRepository;
	
	@Autowired
	private RankingRepository rankingRepository;
	
	@Override
	@Transactional(readOnly=true)
	public Editor obterEditorPorId(Long idEditor) {
		return editorRepository.buscarPorId(idEditor);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Editor> obterEditoresDesc() {
		
		return this.editorRepository.obterEditoresDesc();
	}
}