package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EditorRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * @author infoA2
 */
@Repository
public class EditorRepositoryImpl extends AbstractRepositoryModel<Editor, Long> implements EditorRepository {

	public EditorRepositoryImpl() {
		super(Editor.class);
	}

	/**
	 * Obtém editor por código
	 * @param codigo
	 * @return Editor
	 */
	@Override
	public Editor obterPorCodigo(Long codigo) {
        String hql = " from Editor e where e.codigo = :codigo";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigo", codigo);
		
		query.setMaxResults(1);
		
		return (Editor) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Editor> obterEditoresDesc() {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(Editor.class.getCanonicalName())
		   .append("(ed.id, concat(ed.codigo, ' - ', p.razaoSocial)) from Editor ed join ed.pessoaJuridica p ")
		   .append(" ORDER BY p.razaoSocial ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}

    @Override
    public Editor obterEditorPorFornecedor(Long idFornecedor) {
       
        String sql = "Select e.ID as id, e.NOME_CONTATO as nomeContato  from PESSOA p " +
        		" join FORNECEDOR f on (f.JURIDICA_ID=p.ID) " +
        		" join EDITOR e on (e.JURIDICA_ID=p.ID) " +
        		" where f.ID = :idFornecedor";
        
        final Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("id",StandardBasicTypes.LONG)
                .addScalar("nomeContato",StandardBasicTypes.STRING);
        
        query.setParameter("idFornecedor", idFornecedor);
        
        query.setMaxResults(1);
        
        query.setResultTransformer(Transformers.aliasToBean(Editor.class));
        
        return (Editor) query.uniqueResult();
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<Editor> obterEditoresPorNomePessoa(String nomeEditor) {
		
		final Criteria criteria = super.getSession().createCriteria(Editor.class);
        
        criteria.createAlias("pessoaJuridica", "pessoa");
        
        criteria.add(Restrictions.or(Restrictions.ilike("pessoa.razaoSocial", nomeEditor, MatchMode.ANYWHERE), 
        		Restrictions.ilike("pessoa.nomeFantasia", nomeEditor, MatchMode.ANYWHERE)));
        
        return criteria.list();
	}
}