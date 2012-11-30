package br.com.abril.nds.repository.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public class UsuarioRepositoryImplTest extends AbstractRepositoryImplTest{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@SuppressWarnings("unused")
	@Test
	public void getUsuarioImportacao(){
		Usuario usuario = usuarioRepository.getUsuarioImportacao();
	}
		
	@SuppressWarnings("unused")
	@Test
	public void hasUsuarioPorGrupoPermissao(){
		boolean has = usuarioRepository.hasUsuarioPorGrupoPermissao(1L);
	}

	@Test
	public void listarOrdenacaoAsc(){
		List<Usuario> lista = usuarioRepository.listar(new Usuario(), "nome", Ordenacao.ASC, 1, 15);
		Assert.assertNotNull(lista);
	}
	
	@Test
	public void listarOrdenacaoDesc(){
		List<Usuario> lista = usuarioRepository.listar(new Usuario(), "nome", Ordenacao.ASC, 1, 15);
		Assert.assertNotNull(lista);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void quantidade(){
		Long qtde = usuarioRepository.quantidade(new Usuario());
	}
	
	@SuppressWarnings("unused")
	@Test
	public void quantidadePorNome(){
		Usuario usuario = new Usuario();
		usuario.setNome("Teste");
		Long qtde = usuarioRepository.quantidade(usuario);
	}
	
	@SuppressWarnings("unused")
	@Test
	public void getNomeUsuarioPorLogin(){
		String nomeUsuario = usuarioRepository.getNomeUsuarioPorLogin("login");
	}
	
	@SuppressWarnings("unused")
	@Test
	public void getUsuarioLogado(){
		Usuario usuario = usuarioRepository.getUsuarioLogado("login");
	}
	
	@Test
	public void alterarSenha(){
		usuarioRepository.alterarSenha(new Usuario());
	}
	
}
