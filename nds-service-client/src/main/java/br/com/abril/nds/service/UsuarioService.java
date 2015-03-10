package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.seguranca.GrupoPermissao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * @author InfoA2
 */
public interface UsuarioService {

	public List<Usuario> listar(Usuario usuario, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	public int quantidade(Usuario usuario);

	public void salvar(Usuario usuario);
	
	public void excluir(Long codigoUsuario);

	public Usuario buscar(Long codigoUsuario);

	public boolean validarSenha(Long idUsuario, String senha);

	public List<GrupoPermissao> buscarGrupoPermissoes(Long codigoUsuario);

	public List<Permissao> buscarPermissoes(Long codigoUsuario);

	public String getNomeUsuarioLogado();

	public Usuario getUsuarioLogado();

	public boolean existeUsuario(String login);

	public void alterarSenha(Usuario usuario);

	public boolean verificarUsuarioSupervisor(String usuario, String senha);

    /**
     * @deprecated Use {@link #isNotSupervisor()} instead
     */
    public abstract Boolean isSupervisor(String login);

    public abstract Boolean isSupervisor();

    public abstract Boolean isNotSupervisor();
    
    String obterNomeUsuarioPorLogin(String login);
    
}