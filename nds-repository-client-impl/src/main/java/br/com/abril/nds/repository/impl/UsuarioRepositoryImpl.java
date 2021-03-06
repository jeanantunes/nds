package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class UsuarioRepositoryImpl extends AbstractRepositoryModel<Usuario, Long> implements UsuarioRepository {

	public UsuarioRepositoryImpl() {
		super(Usuario.class);
	}
	

    // TODO Definição de Usuario de Importação
	public Usuario getUsuarioImportacao() {
		return getUsuarioByLogin("importacao");		
	}


	private Usuario getUsuarioByLogin(String login) {
		Criteria criteria =  getSession().createCriteria(Usuario.class);		
		criteria.add(Restrictions.eq("login", login));
		return (Usuario) criteria.uniqueResult();
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
		Criteria criteria = getSession().createCriteria(Usuario.class);	
		
		String nome = usuario.getNome();
		
		if( nome != null && !nome.isEmpty() ) {
			LogicalExpression condicao = Restrictions.or(Restrictions.ilike("nome", "%" + nome + "%"), Restrictions.ilike("sobrenome", "%" + nome + "%"));
			criteria.add(Restrictions.or(condicao, Restrictions.ilike("login", "%" + nome + "%")));
		}
		
        // Deve trazer apenas registros diferentes de usuários do sistema.
		criteria.add(Restrictions.eq("sys", false));

		return criteria;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.UsuarioRepository#getUsuarioPorLogin(java.lang.String)
	 */
	@Override
	public String getNomeUsuarioPorLogin(String login) {
		Criteria criteria =  getSession().createCriteria(Usuario.class);
		criteria.setProjection(Projections.groupProperty("nome"));
		criteria.add(Restrictions.eq("login", login));
		return (String) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Usuario getUsuarioLogado(String login) {
		Criteria criteria =  getSession().createCriteria(Usuario.class);
		criteria.add(Restrictions.eq("login", login));
		criteria.setCacheable(false);
		criteria.setCacheMode(CacheMode.IGNORE);
		return (Usuario) criteria.uniqueResult();
	}


	@Override
	public void alterarSenha(Usuario usuario) {
		
		Query query = this.getSession().createQuery("update " + Usuario.class.getCanonicalName() + " set senha = :senha, lembreteSenha = :lembreteSenha  where id = (:id)");
		
		query.setParameter("senha", usuario.getSenha());
		query.setParameter("lembreteSenha", usuario.getLembreteSenha());
		query.setParameter("id", usuario.getId());
		
		query.executeUpdate();
		
	}


	@Override
	public boolean verificarUsuarioSupervisor(String usuario, String senha) {
		
		Query query = 
			this.getSession().createQuery(
				"select count(id) from Usuario where login = :usuario and senha = :senha and supervisor = :indTrue");
		
		query.setParameter("usuario", usuario);
		query.setParameter("senha", senha);
		query.setParameter("indTrue", true);
		
		return (Long)query.uniqueResult() > 0;
	}
    
    @Override
    public Boolean isSupervisor(String login) {
        Criteria criteria = getSession().createCriteria(Usuario.class);
        criteria.add(Restrictions.eq("login", login));
        criteria.add(Restrictions.eq("supervisor", true));
        
        criteria.setProjection(Projections.rowCount());
        return (Long) criteria.uniqueResult() > 0;
    }
    
    @Override
	public Usuario obterUsuarioPeloMovimento(MovimentoFinanceiroCota movimento) {
		
		StringBuilder hql = new StringBuilder("");
		    	
    	hql.append(" select u from MovimentoFinanceiroCota mfc join mfc.usuario u ");
    	hql.append(" where mfc.id =:idMovimento ");
    	
    	Query query = this.getSession().createQuery(hql.toString());

 	    query.setParameter("idMovimento", movimento.getId());

		return ((Usuario) query.uniqueResult());
	}
}
