package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoEditor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.HistoricoDescontoEditorRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do distribuidor
 * 
 * @author Discover Technology
 */
@Repository
public class HistoricoDescontoEditorRepositoryImpl extends AbstractRepositoryModel<HistoricoDescontoEditor, Long> implements HistoricoDescontoEditorRepository {

	public HistoricoDescontoEditorRepositoryImpl() {
		super(HistoricoDescontoEditor.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select 0L as sequencial ");
		hql.append(", 1L as idTipoDesconto ");
		hql.append(", 'Anonimo' as usuario ");
		hql.append(", f.desconto as desconto ");
		hql.append(", f.juridica.razaoSocial as fornecedor ");
		hql.append(", 'Geral' as descTipoDesconto ");
		hql.append("from Editor e ");
		hql.append("where 1 = 1 ");
		
		if(filtro.getIdFornecedores() != null && !filtro.getIdFornecedores().isEmpty()) {
			hql.append("and f.id in (:idsEditores) ");
		}
		
		hql.append(getOrdenacao(filtro));
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setResultTransformer(Transformers.aliasToBean(TipoDescontoDTO.class));
		
		if(filtro.getIdsEditores() !=null && !filtro.getIdsEditores().isEmpty()) {
			query.setParameterList("idsEditores", filtro.getIdsEditores());
		}
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();

	}
	
	@Override
	public HistoricoDescontoEditor buscarUltimoDescontoValido(Editor editor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select hde ")
			.append("from HistoricoDescontoEditor hde ")
			.append("where hde.dataAlteracao=(select max(hdfSub.dataAlteracao) from HistoricoDescontoEditor hdfSub where  hdfSub.editor.id = :editorId) ")
			.append("and hde.editor.id = :editorId ");
		
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("editorId", editor.getId());
		
		query.setMaxResults(1);
		
		Object resultado = query.uniqueResult();
		
		return resultado==null? null : (HistoricoDescontoEditor) query.uniqueResult();
		
	}
	
	private String getOrdenacao(FiltroTipoDescontoDTO filtro){
		
		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return "";
		}

		StringBuilder hql = new StringBuilder();

		switch (filtro.getOrdenacaoColuna()) {
			
			case DATA_ALTERACAO:
				hql.append(" order by dataAlteracao ");
				break;
				
			case DESCONTO:
				hql.append(" order by desconto  ");
				break;
					
			case SEQUENCIAL:
				hql.append(" order by sequencial  ");
				break;
			
			case USUARIO:
				hql.append(" order by usuario ");
				break;
				
			case FORNECEDORES:
				hql.append(" order by fornecedor ");
				break;	
				
			case TIPO_DESCONTO:
				hql.append(" order by descTipoDesconto ");
				break;		
				
			default:
				hql.append(" order by sequencial ");
		}

		if (filtro.getPaginacao()!= null && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}

		return hql.toString();
	}

	@Override
	public HistoricoDescontoEditor buscarHistoricoDescontoEditorPor(Desconto desconto, Editor editor) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select hde ")
			.append("from HistoricoDescontoEditor hde ")
			.append("where hde.desconto.id = :descontoId ")
			.append("and hde.editor.id = :editorId ");
		
		
		Query query  = getSession().createQuery(hql.toString());
		
		query.setParameter("descontoId", desconto.getId());
		query.setParameter("editorId", editor.getId());
		
		return (HistoricoDescontoEditor) query.uniqueResult();
	}
		
}