package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * @author InfoA2
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.UsuarioService#listar(br.com.abril.nds.model.seguranca.Usuario, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Usuario> listar(Usuario usuario, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		return usuarioRepository.listar(usuario, orderBy, ordenacao, initialResult, maxResults);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.UsuarioService#quantidade(br.com.abril.nds.model.seguranca.Usuario)
	 */
	@Override
	@Transactional(readOnly=true)
	public int quantidade(Usuario usuario) {
		return Integer.valueOf(usuarioRepository.quantidade(usuario).toString());
	}

	@Override
	@Transactional
	public void salvar(Usuario usuario) {
		if (usuario.getId() == null || usuario.getId() == 0) {
			usuarioRepository.adicionar(usuario);
		}
		usuarioRepository.alterar(usuario);
	}

	@Override
	@Transactional
	public void excluir(Long codigoUsuario) {
		Usuario usuario = usuarioRepository.buscarPorId(codigoUsuario);
		usuario.setPermissoes(new HashSet<Permissao>());
		usuarioRepository.alterar(usuario);
		usuarioRepository.remover(usuario);
	}

	@Override
	@Transactional
	public Usuario buscar(Long codigoUsuario) {
		Usuario usuario = usuarioRepository.buscarPorId(codigoUsuario);
		return usuario;
	}

	@Override
	@Transactional
	public boolean validarSenha(Long idUsuario, String senha) {
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		if (usuario.getSenha().equals(senha)) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public List<GrupoPermissao> buscarGrupoPermissoes(Long codigoUsuario) {
		return new ArrayList(usuarioRepository.buscarPorId(codigoUsuario).getGruposPermissoes());
	}

	@Override
	@Transactional
	public List<Permissao> buscarPermissoes(Long codigoUsuario) {
		return new ArrayList(usuarioRepository.buscarPorId(codigoUsuario).getPermissoes());
	}

	@Override
	@Transactional(readOnly=true)
	public String getNomeUsuarioLogado() {
		String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		return usuarioRepository.getNomeUsuarioPorLogin(loginUsuario);
	}

}
