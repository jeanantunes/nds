package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Editor;

public class EditorRepositoryImplTest extends AbstractRepositoryImplTest {

	@Autowired
	private EditorRepositoryImpl editorRepositoryImpl;

	@Test
	public void testarObterEditoresDesc() {

		List<Editor> editores;

		editores = editorRepositoryImpl.obterEditoresDesc();

		Assert.assertNotNull(editores);

	}
	
	@Test
	public void testarObterPorCodigo() {
		
		Editor editor;
		
		Long codigo = 1L;
		
		editor = editorRepositoryImpl.obterPorCodigo(codigo);
		
		Assert.assertNull(editor);
		
	}
	
	
}
