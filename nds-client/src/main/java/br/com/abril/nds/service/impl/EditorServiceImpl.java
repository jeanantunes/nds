package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.RegistroCurvaABCEditorVO;
import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.client.vo.ResultadoCurvaABCEditor;
import br.com.abril.nds.dto.filtro.FiltroCurvaABCEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.repository.EditorRepository;
import br.com.abril.nds.service.EditorService;

@Service
public class EditorServiceImpl implements EditorService {

	@Autowired
	private EditorRepository editorRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Editor> obterEditores() {
		return editorRepository.obterEditores();
	}
	
	@Override
	@Transactional(readOnly=true)
	public Editor obterEditorPorId(Long idEditor) {
		return editorRepository.buscarPorId(idEditor);
	}

	@Override
	@Transactional
	public List<RegistroCurvaABCEditorVO> obterCurvaABCEditor(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		return editorRepository.obterCurvaABCEditor(filtroCurvaABCEditorDTO);
	}

	@Override
	@Transactional
	public ResultadoCurvaABCEditor obterCurvaABCEditorTotal(FiltroCurvaABCEditorDTO filtroCurvaABCEditorDTO) {
		return editorRepository.obterCurvaABCEditorTotal(filtroCurvaABCEditorDTO);
	}

	@Override
	@Transactional
	public List<RegistroHistoricoEditorVO> obterHistoricoEditor(FiltroPesquisarHistoricoEditorDTO filtro) {
		return editorRepository.obterHistoricoEditor(filtro);
	}

}
