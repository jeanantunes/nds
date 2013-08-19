package br.com.abril.nds.repository.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.RegistroHistoricoEditorVO;
import br.com.abril.nds.dto.filtro.FiltroPesquisarHistoricoEditorDTO;
import br.com.abril.nds.model.cadastro.Editor;

public class EditorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EditorRepositoryImpl editorRepositoryImpl;

	@Test
	public void testarObterEditores() {

		List<Editor> editores;

		editores = editorRepositoryImpl.obterEditores();

		Assert.assertNotNull(editores);

	}
	
	@Test
	public void testarObterHistoricoEditor() { // MÉTODO COM PROBLEMAS -----------------------
		
		List<RegistroHistoricoEditorVO> historicoEditor;
		
		Calendar d = Calendar.getInstance();
		Date dataDe = d.getTime();
		Date dataAte = d.getTime();
		
		String numeroEditor = "123";
		
		FiltroPesquisarHistoricoEditorDTO filtro = new FiltroPesquisarHistoricoEditorDTO(dataDe, dataAte, numeroEditor);
		
		historicoEditor = editorRepositoryImpl.obterHistoricoEditor(filtro);
		
		Assert.assertNotNull(historicoEditor);
		
	}
	
	@Test
	public void testarObterPorCodigo() {
		
		Editor editor;
		
		Long codigo = 1L;
		
		editor = editorRepositoryImpl.obterPorCodigo(codigo);
		
		Assert.assertNull(editor);
		
	}
	
	
}
