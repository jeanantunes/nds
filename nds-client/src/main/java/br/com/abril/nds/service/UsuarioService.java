package br.com.abril.nds.service;

import java.util.List;

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
	
}
