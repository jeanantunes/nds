package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class UsuarioRepositoryImpl extends AbstractRepositoryModel<Usuario, Long> implements UsuarioRepository {

	public UsuarioRepositoryImpl() {
		super(Usuario.class);
	}
	

	//TODO Definição de Usuario de Importação
	@Transactional
	public Usuario getUsuarioImportacao() {
				
		Usuario usuario = new Usuario();
		usuario.setLogin("usuarioImportacao");
		usuario.setNome("Usuário de Importação");
		usuario.setSenha("usuarioImportacao");
		
		return merge(usuario);
	}


	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.UsuarioRepository#hasUsuarioPorGrupoPermissao(java.lang.Long)
	 */
	@Override
	public boolean hasUsuarioPorGrupoPermissao(Long idGrupoPermissao) {
		
		Criteria criteria =  getSession().createCriteria(Usuario.class, "usuario");
		criteria.createCriteria("usuario.gruposPermissoes", "grupoPermissao", Criteria.LEFT_JOIN);
		criteria.add(Restrictions.eq("grupoPermissao.id", idGrupoPermissao));
		
		if (!criteria.list().isEmpty()) {
			return true;
		}
		return false;
	}


	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.UsuarioRepository#listar(br.com.abril.nds.model.seguranca.Usuario, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	public List<Usuario> listar(Usuario usuario, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		
		Criteria criteria = addRestrictions(usuario);

		if(Ordenacao.ASC ==  ordenacao) {
			criteria.addOrder(Order.asc(orderBy));
		} else if(Ordenacao.DESC ==  ordenacao) {
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setMaxResults(maxResults);
		criteria.setFirstResult(initialResult);
		
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.UsuarioRepository#quantidade(br.com.abril.nds.model.seguranca.Usuario)
	 */
	@Override
	public Long quantidade(Usuario usuario) {
		Criteria criteria = addRestrictions(usuario);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.list().get(0);
	}

	/**
	 * @param usuario
	 * @return
	 */
	private Criteria addRestrictions(Usuario usuario) {
		Criteria criteria =  getSession().createCriteria(Usuario.class);	
		
		String nome = usuario.getNome();
		
		if( nome != null && !nome.isEmpty() ) {
			LogicalExpression condicao = Restrictions.or(Restrictions.ilike("nome", "%" + nome + "%"), Restrictions.ilike("sobrenome", "%" + nome + "%"));
			criteria.add(Restrictions.or(condicao, Restrictions.ilike("login", "%" + nome + "%")));
		}

		return criteria;
	}
	
}
