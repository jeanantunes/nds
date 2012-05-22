package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.repository.EditorRepository;

@Repository
public class EditorRepositoryImpl extends AbstractRepository<Editor, Long> implements EditorRepository {

	public EditorRepositoryImpl() {
		super(Editor.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Editor> obterEditores() {
		String hql = "from Editor";
		Query query = getSession().createQuery(hql);
		@SuppressWarnings("unchecked")
		List<Editor> editores = query.list();
		return editores;
	}

}
