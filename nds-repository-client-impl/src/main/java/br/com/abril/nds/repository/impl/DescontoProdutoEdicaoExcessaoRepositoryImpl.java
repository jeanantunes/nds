package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoEditorDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoEditorDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.Desconto;
import br.com.abril.nds.model.cadastro.desconto.DescontoCotaProdutoExcessao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DescontoProdutoEdicaoExcessaoRepository;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto edição
 * 
 * @author Discover Technology
 */

@Repository
public class DescontoProdutoEdicaoExcessaoRepositoryImpl extends AbstractRepositoryModel<DescontoCotaProdutoExcessao, Long> implements DescontoProdutoEdicaoExcessaoRepository {
 
	private static final int QUINHENTOS = 500;
	
	/**
	 * Construtor padrão.
	 */
	public DescontoProdutoEdicaoExcessaoRepositoryImpl() {
		
		super(DescontoCotaProdutoExcessao.class);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DescontoCotaProdutoExcessao buscarDescontoCotaProdutoExcessao(
			TipoDesconto tipoDesconto,
			Desconto desconto,
			Fornecedor fornecedor,
			Editor editor, 
			Cota cota, 
			Long idProduto, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder("select d ");
		hql.append(" from DescontoCotaProdutoExcessao d ")
			.append(" where 1 = 1 ");
		
		if (fornecedor != null) {
			
			hql.append(" and d.fornecedor = :fornecedor ");
		}
		
		if (editor != null) {
			
			hql.append(" and d.editor = :editor ");
		}

		if (cota != null) {
	
		   hql.append(" and d.cota = :cota ");
		}

		if (idProdutoEdicao != null) {
			
			hql.append(" and d.produtoEdicao.id = :idProdutoEdicao ");
		} else {
			
			hql.append(" and d.produtoEdicao is null ");
		}
	
		if (idProduto != null) {
			
			hql.append(" and d.produto.id = :idProduto ");
		}		
		
		if (tipoDesconto != null) {
			
			hql.append(" and d.tipoDesconto = :tipoDesconto ");
		}
		
		if (desconto != null) {
			
			hql.append(" and d.desconto = :desconto ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (fornecedor != null) {
			
			query.setParameter("fornecedor", fornecedor);
		}
		
		if (editor != null) {
			
			query.setParameter("editor", editor);
		}

		if (cota != null) {
			
			query.setParameter("cota", cota);
		}

		if (idProdutoEdicao != null) {
			
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		if (tipoDesconto != null) {
			
			query.setParameter("tipoDesconto", tipoDesconto);
		}
		
		if (desconto != null) {
			
			query.setParameter("desconto", desconto);
		}
		
		if (idProduto != null) {
			
			query.setParameter("idProduto", idProduto);
		}
		
		query.setMaxResults(1);
		
		return (DescontoCotaProdutoExcessao) query.uniqueResult();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor) {

		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, null, null, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, null, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, cota, null,null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontosProdutoEdicao(ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, null, null, produtoEdicao,null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoSemTipoDesconto(tipoDesconto, fornecedor, cota);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(null, fornecedor, cota, null,tipoDesconto);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Fornecedor fornecedor, Cota cota,ProdutoEdicao produtoEdicao) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(desconto, fornecedor, cota, produtoEdicao, tipoDesconto);
	}
	
	@SuppressWarnings("unchecked")
	private Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessaoCotaFornecedor(Desconto desconto, Fornecedor fornecedor, Cota cota, ProdutoEdicao produtoEdicao,TipoDesconto tipoDesconto){
		
		Criteria criteria = getSession().createCriteria(DescontoCotaProdutoExcessao.class);
		
		if(desconto != null) {
			
			criteria.add(Restrictions.eq("desconto", desconto));
		}
		
		if (fornecedor != null) {
			
			criteria.add(Restrictions.eq("fornecedor", fornecedor));
		}
		
		if (cota != null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		if (produtoEdicao != null) {
			
			criteria.add(Restrictions.eq("produtoEdicao", produtoEdicao));
		}
		
		if(tipoDesconto!= null){
			
			criteria.add(Restrictions.eq("tipoDesconto", tipoDesconto));
		}
		
		criteria.setFetchMode("cota", FetchMode.JOIN);
		
		return new HashSet<DescontoCotaProdutoExcessao>(criteria.list());
	}
	
	@SuppressWarnings("unchecked")
	private List<DescontoCotaProdutoExcessao> obterDescontoSemTipoDesconto(TipoDesconto tipoDesconto, Fornecedor fornecedor, Cota cota){
		
		Criteria criteria = getSession().createCriteria(DescontoCotaProdutoExcessao.class);
		
		criteria.add(Restrictions.eq("fornecedor", fornecedor));

		if (cota!= null) {
			
			criteria.add(Restrictions.eq("cota", cota));
		}
		
		criteria.add(Restrictions.not(Restrictions.eq("tipoDesconto", tipoDesconto)));
	
		return criteria.list();
	}

	@Override
	public void salvarListaDescontoProdutoEdicaoExcessao(List<DescontoCotaProdutoExcessao> lista) {
		
		int i = 0;
		
		for (DescontoCotaProdutoExcessao DescontoProdutoEdicaoExcessao : lista) {
			this.merge(DescontoProdutoEdicaoExcessao);
			i++;
			if (i % QUINHENTOS == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
		
		getSession().flush();
		getSession().clear();
		
	}

	@Override
	public DescontoCotaProdutoExcessao buscarDescontoCotaEditorExcessao(TipoDesconto tipoDesconto, Desconto desconto, Cota cota, Editor editor) {
		
		Criteria criteria = getSession().createCriteria(DescontoCotaProdutoExcessao.class);
		
		if (editor == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetro Editor inválido!");
		}
		
		if (cota == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Parâmetro Cota inválido!");
		}
		
		if(desconto != null) {
			criteria.add(Restrictions.eq("desconto", desconto));
		}
		
		criteria.add(Restrictions.eq("editor", editor));
		criteria.add(Restrictions.eq("cota", cota));
		criteria.add(Restrictions.eq("tipoDesconto", TipoDesconto.EDITOR));
		
		criteria.setFetchMode("cota", FetchMode.JOIN);
		
		return (DescontoCotaProdutoExcessao) criteria.uniqueResult();
		
	}

	@Override
	public Set<DescontoCotaProdutoExcessao> obterDescontoProdutoEdicaoExcessao(TipoDesconto tipoDesconto, Desconto desconto, Editor editor, Cota cota) {
		
		return obterDescontoProdutoEdicaoExcessaoCotaFornecedor(desconto, null, cota, null, tipoDesconto);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoEditorDTO> buscarTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT dados.DESCONTO_ID as descontoId, ")
			.append(" d.VALOR as desconto, ")
			.append(" EDITOR_ID as editorId, ")
			.append(" e.CODIGO as codigoEditor, ")
			.append(" coalesce(p.nome_fantasia, p.razao_social, p.nome, '') as nomeEditor, ")
			.append(" dados.USUARIO_ID as usuarioId, ")
			.append(" u.NOME as nomeUsuario, ")
			.append(" d.DATA_ALTERACAO as dataAlteracao, ")
			.append(" coalesce(count(COTA_ID), -1) as qtdCotas ");
		
		queryFromTipoDescontoEditor(hql);
		
		if(filtro.getPaginacao() != null) {
			hql.append("order by "+ filtro.getPaginacao().getSortColumn() +" "+ filtro.getPaginacao().getSortOrder()); 
		}
		
		Query q = getSession().createSQLQuery(hql.toString());

		if (filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			q.setFirstResult( filtro.getPaginacao().getQtdResultadosPorPagina() * ( (filtro.getPaginacao().getPaginaAtual() - 1 )))
			.setMaxResults( filtro.getPaginacao().getQtdResultadosPorPagina() );
		}

		q.setResultTransformer(Transformers.aliasToBean(TipoDescontoEditorDTO.class));
		
		return (List<TipoDescontoEditorDTO>) q.list();
	}

	@Override
	public Integer buscarQuantidadeTipoDescontoEditor(FiltroTipoDescontoEditorDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT COUNT(0) FROM ")
			.append(" ( ")
			.append(" SELECT dados.DESCONTO_ID ");
		
		queryFromTipoDescontoEditor(hql);
		
		hql.append(" ) rs1 ");
		
		Query q = getSession().createSQLQuery(hql.toString());
		
		return ((BigInteger) q.uniqueResult()).intValue();
		
	}
	
	private void queryFromTipoDescontoEditor(StringBuilder hql) {
		
		hql.append("FROM ")
			.append(" ( ")
			.append("	SELECT hdcpe.DESCONTO_ID, hdcpe.EDITOR_ID, hdcpe.USUARIO_ID, hdcpe.COTA_ID ")
			.append("	FROM HISTORICO_DESCONTO_COTA_PRODUTO_EXCESSOES hdcpe ")	 
			.append("	inner join DESCONTO_COTA_PRODUTO_EXCESSOES dcpe on dcpe.DESCONTO_ID = hdcpe.DESCONTO_ID ")
			.append("	where hdcpe.editor_id is not null ")
			.append(" UNION ")
			.append(" 	SELECT DESCONTO_ID, e.ID as EDITOR_ID, 1 as USUARIO_ID, null as COTA_ID ")
			.append(" 	from editor e ")
			.append(" ) as dados ")  
			.append(" JOIN DESCONTO d on (dados.DESCONTO_ID=d.ID) ")
			.append(" JOIN USUARIO u on (dados.USUARIO_ID=u.ID) ")
			.append(" LEFT OUTER JOIN EDITOR e on (dados.EDITOR_ID=e.ID) ")
			.append(" LEFT JOIN PESSOA p on p.id = e.JURIDICA_ID ")
			.append(" GROUP BY dados.DESCONTO_ID ");
				
	}

}
