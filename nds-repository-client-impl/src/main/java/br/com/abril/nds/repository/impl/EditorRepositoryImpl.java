package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
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
		   .append("(ed.id, p.razaoSocial) from Editor ed join ed.pessoaJuridica p ")
		   .append(" ORDER BY p.razaoSocial ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
}