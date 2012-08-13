package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface UsuarioRepository extends Repository<Usuario, Long> {

	public Usuario getUsuarioImportacao();

	public boolean hasUsuarioPorGrupoPermissao(Long idGrupoPermissao);

	public List<Usuario> listar(Usuario usuario, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);

	public Long quantidade(Usuario usuario);

	public String getNomeUsuarioPorLogin(String login);

	public void alterarSenha(Usuario usuario);
	
}
