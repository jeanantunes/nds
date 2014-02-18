package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * @author InfoA2
 */
@Service
@Transactional(readOnly = true)
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

	public int quantidade(Usuario usuario) {
		return Integer.valueOf(usuarioRepository.quantidade(usuario).toString());
	}

	@Override
    @Transactional(readOnly = false)
	public void salvar(Usuario usuario) {
		if (usuario.getId() == null || usuario.getId() == 0) {
			usuarioRepository.adicionar(usuario);
		}
		usuarioRepository.alterar(usuario);
	}

	@Override
    @Transactional(readOnly = false)
	public void excluir(Long codigoUsuario) {
		Usuario usuario = usuarioRepository.buscarPorId(codigoUsuario);
		usuario.setPermissoes(new HashSet<Permissao>());
		usuarioRepository.alterar(usuario);
		usuarioRepository.remover(usuario);
	}

	@Override
	public Usuario buscar(Long codigoUsuario) {
		Usuario usuario = usuarioRepository.buscarPorId(codigoUsuario);
		return usuario;
	}

	@Override
	public boolean validarSenha(Long idUsuario, String senha) {
		Usuario usuario = usuarioRepository.buscarPorId(idUsuario);
		if (usuario.getSenha().equals(senha)) {
			return true;
		}
		return false;
	}

	@Override
	public List<GrupoPermissao> buscarGrupoPermissoes(Long codigoUsuario) {
		return new ArrayList(usuarioRepository.buscarPorId(codigoUsuario).getGruposPermissoes());
	}

	@Override
	public List<Permissao> buscarPermissoes(Long codigoUsuario) {
		return new ArrayList(usuarioRepository.buscarPorId(codigoUsuario).getPermissoes());
	}

	@Override
	public String getNomeUsuarioLogado() {
        String loginUsuario = getCurrentUserName();
		return usuarioRepository.getNomeUsuarioPorLogin(loginUsuario);
	}

	@Override
	public Usuario getUsuarioLogado() {
        String loginUsuario = getCurrentUserName();
		return usuarioRepository.getUsuarioLogado(loginUsuario);
	}

	
	@Override
	public boolean existeUsuario(String login) {
		if (usuarioRepository.getNomeUsuarioPorLogin(login) != null) {
			return true;
		}
		return false;
	}

	@Override
	public void alterarSenha(Usuario usuario) {
		usuarioRepository.alterarSenha(usuario);
	}

	@Override
	public boolean verificarUsuarioSupervisor(String usuario, String senha) {
		
		try {
			senha = Util.encriptar(senha);
		} catch (Exception e) {
			
			throw new ValidacaoException(
				TipoMensagem.ERROR, "Houve problema com a senha informada. Tente novamente.");
		}
		
		return this.usuarioRepository.verificarUsuarioSupervisor(usuario, senha);
	}
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * br.com.abril.nds.service.UsuarioService#isSupervisor(java.lang.String)
     */
    @Override
    public Boolean isSupervisor(String login) {
        return usuarioRepository.isSupervisor(login);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * br.com.abril.nds.service.UsuarioService#isSupervisor(java.lang.String)
     */
    @Override
    public Boolean isSupervisor() {
        String loginUsuario = getCurrentUserName();
        return this.isSupervisor(loginUsuario);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * br.com.abril.nds.service.UsuarioService#isSupervisor(java.lang.String)
     */
    @Override
    public Boolean isNotSupervisor() {
        
        return !this.isSupervisor();
    }
    
    private String getCurrentUserName() {
        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return loginUsuario;
    }

}
