package br.com.abril.nds.repository.impl;

import static org.apache.commons.lang.StringUtils.left;
import static org.apache.commons.lang.StringUtils.leftPad;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaProdutoDTO;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ProdutoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Produto}
 * 
 * @author Discover Technology
 */
@Repository
public class ProdutoRepositoryImpl extends AbstractRepositoryModel<Produto, Long> implements ProdutoRepository {

	/**
	 * Construtor padrão.
	 */
	public ProdutoRepositoryImpl() {
		super(Produto.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> obterProdutoLikeNome(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) like upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", "%" + nome + "%");
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> obterProdutoLikeNome(String nome, Integer qtdMaxRegRetorno) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) like upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", "%" + nome + "%");
		query.setMaxResults(qtdMaxRegRetorno);
		
		return query.list();
	}
	
	@Override
	public Produto obterProdutoPorNome(String nome) {
		String hql = "from Produto produto "
				   + " where upper(produto.nome) = upper(:nome) order by produto.nome";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome);
		
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
	}
	
	@Override
	public Produto obterProdutoPorCodigoProdin(String codigoProduto) {
		
		String hql = "from Produto produto "
				   + " where produto.codigo = :codigo ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigo", leftPad(codigoProduto, 8, "0"));
		
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
	}
	
	@Override
	public Produto obterProdutoBalanceadosPorCodigo(String codigoProduto, Date dataLancamento) {
		
		String hql = " select produto "
				+ " from Lancamento lancamento "
				+ " join lancamento.produtoEdicao produtoEdicao "
				+ " join produtoEdicao.produto produto "
				+ " where lancamento.status in (:status) "
				+ " and lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor "
				+ " and produto.codigo = :codigoProduto ";
		
		Query query = super.getSession().createQuery(hql);
		

		query.setParameterList("status", Arrays.asList(StatusLancamento.BALANCEADO,StatusLancamento.EXPEDIDO));
		query.setParameter("dataLancamentoDistribuidor", dataLancamento);
		query.setParameter("codigoProduto", codigoProduto);
		
		return (Produto) query.uniqueResult();
	}

	@Override
	public String obterNomeProdutoPorCodigo(String codigoProduto) {
		Query query = this.getSession().createQuery("select p.nome from Produto p where p.codigo = :codigoProduto");
		query.setParameter("codigoProduto", codigoProduto);
		
		return (String) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ConsultaProdutoDTO> pesquisarProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto,
			String sortorder, String sortname, int page, int rp, Boolean isGeracaoAutomatica) {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select  ");
		
		hql.append(" produto.id as id, 								");
		hql.append(" produto.codigo as codigo, 						");
		hql.append(" produto.nome as produtoDescricao, 				");
		hql.append(" tipoProduto.descricao as tipoProdutoDescricao, ");
		
		hql.append(" ( select case when editor.id is null then '' 	");
		hql.append(" else editor.pessoaJuridica.razaoSocial end		");
		hql.append(" from Produto p left join p.editor as editor 	");
		hql.append(" where p.id = produto.id ) as nomeEditor, 		");
		
		hql.append(" ( select "); 
		hql.append(" case when count(f.id)>1 then 'Diversos' 				");
		hql.append(" else f.juridica.razaoSocial end 						");
		hql.append(" from Produto p join p.fornecedores f 					");
		hql.append(" where p.id = produto.id ) as tipoContratoFornecedor, 	");
		
		hql.append(" produto.peb as peb,  						");
		hql.append(" produto.pacotePadrao as pacotePadrao, 		");
		hql.append(" coalesce(descontoLogistica.percentualDesconto, produto.desconto, 0) as percentualDesconto, ");
		hql.append(" produto.periodicidade as periodicidade, 	");
		hql.append(" produto.isSemCeIntegracao as isSemCeIntegracao 	");
		
		try {
			
			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, sortname, sortorder, false, isGeracaoAutomatica);
			
			query.setResultTransformer(
				new AliasToBeanResultTransformer(
					ConsultaProdutoDTO.class));
			
			query.setMaxResults(rp);
			query.setFirstResult(page);
			
			return query.list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Integer pesquisarCountProdutos(String codigo, String produto,
			String fornecedor, String editor, Long codigoTipoProduto, Boolean isGeracaoAutomatica) {
		
		StringBuffer hql = new StringBuffer(" select count(distinct produto.id) ");
		
		try {

			Query query = 
				this.getQueryBuscaProdutos(
					hql, codigo, produto, fornecedor, 
					editor, codigoTipoProduto, null, null, true, isGeracaoAutomatica);
			
			return ((Long) query.uniqueResult()).intValue();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Query getQueryBuscaProdutos(StringBuffer hql, String codigo, String nome,
			String fornecedor, String editor, Long codigoTipoProduto, String sortname, String sortorder, boolean isCount, Boolean isGeracaoAutomatica) {
		
		hql.append(" from ");
		hql.append(" Produto produto ");
		hql.append(" join produto.tipoProduto tipoProduto 					");
		hql.append(" left join produto.descontoLogistica descontoLogistica 	");

		if (fornecedor != null && !fornecedor.isEmpty()) {
			hql.append(" join produto.fornecedores fornecedorProd ");
		}
		
		if (editor != null && !editor.isEmpty()) {
			hql.append(" join produto.editor editorProd ");
		}
		
		String auxHql = " where ";
		
		if (codigo != null && !codigo.isEmpty()) {
			hql.append(auxHql).append(" upper(produto.codigoICD) = :codigo ");
			auxHql = " and ";
		}
		
		if (nome != null && !nome.isEmpty()) {
			hql.append(auxHql).append(" lower( produto.nome ) like :nome ");
			auxHql = " and ";
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			
			hql.append(auxHql);
			hql.append(" lower( fornecedorProd.juridica.nomeFantasia ) like :fornecedor ");
			auxHql = " and ";
		}
		
		if (editor != null && !editor.isEmpty()) {
			
			hql.append(auxHql);
			hql.append(" lower( editorProd.pessoaJuridica.razaoSocial ) like :nomeEditor ");
			auxHql = " and ";
		}
		
		if (isGeracaoAutomatica != null && isGeracaoAutomatica == true) {
			
			hql.append(auxHql);
			hql.append(" produto.isGeracaoAutomatica = true " );
			auxHql = " and ";
		}
		
		if (isGeracaoAutomatica != null && isGeracaoAutomatica == false) {
			
			hql.append(auxHql);
			hql.append(" produto.isGeracaoAutomatica = false " );
			auxHql = " and ";
		}

		if (codigoTipoProduto != null && codigoTipoProduto > 0) {
			hql.append(auxHql).append(" tipoProduto.id = :codigoTipoProduto ");
		}

		if(!isCount) {
		
			hql.append(" group by  			");
			hql.append(" produto.id, 		");
			hql.append(" produto.codigo, 	");
			hql.append(" produto.nome,      ");
			hql.append(" tipoProduto.descricao, ");
			hql.append(" col_4_0_, 				");
			hql.append(" col_5_0_,				");
			hql.append(" produto.peb,			");
			hql.append(" produto.pacotePadrao,  ");
			hql.append(" col_8_0_, 				");
			hql.append(" produto.periodicidade  ");
			
		}

		
		if (sortname != null && sortorder != null) {
			hql.append(" order by ").append(sortname).append(" ").append(sortorder);
		}
		
		Query query = super.getSession().createQuery(hql.toString());

		if (codigo != null && !codigo.isEmpty()) {
			query.setParameter("codigo", codigo.toUpperCase());
		}
		
		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toLowerCase().trim() + "%");
		}
		
		if (fornecedor != null && !fornecedor.isEmpty()) {
			query.setParameter("fornecedor", "%" + fornecedor.toLowerCase().trim() + "%");
		}
		
		if (editor != null && !editor.isEmpty()) {
			query.setParameter("nomeEditor", "%" + editor.toLowerCase().trim() + "%");
		}
		
		if (codigoTipoProduto != null && codigoTipoProduto > 0) {
			query.setParameter("codigoTipoProduto", codigoTipoProduto);
		}
		
		return query;
	}

	@Override
	public Produto obterProdutoPorID(Long id) {
		
		Criteria criteria = super.getSession().createCriteria(Produto.class);
		
		criteria.add(Restrictions.eq("id", id))
			.setFetchMode("fornecedores", FetchMode.JOIN)
			.setFetchMode("descontoLogistica", FetchMode.JOIN);
		
		return (Produto) criteria.uniqueResult();
	}
	
	/**
	 * Obtem produto por nome ou codigo
	 * @param nome
	 * @param codigo
	 * @return Produto
	 */
	@Override
	public Produto obterProdutoPorNomeProdutoOuCodigo(String nome, String codigo) {
		String hql = "from Produto produto "
				   + " where ((upper(produto.nome) = upper(:nome)) or (produto.codigo = :codigo)) ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("nome", nome);
		
		query.setParameter("codigo", codigo);
		
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
	}
	

	public GrupoProduto obterGrupoProduto(String codigoProduto) {

		StringBuffer hql = new StringBuffer();

		hql.append(" select p.tipoProduto.grupoProduto from Produto p where p.codigo = :codigoProduto ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("codigoProduto", codigoProduto);

		return (GrupoProduto) query.uniqueResult();

	}

	@SuppressWarnings("unchecked")
	public List<Produto> buscarProdutosBalanceadosOrdenadosNome(Date dataLancamento) {
		
		String hql = " select produto "
				+ " from Lancamento lancamento "
				+ " join lancamento.produtoEdicao produtoEdicao "
				+ " join produtoEdicao.produto produto "
				+ " where lancamento.status in (:status) "
				+ " and lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor "
				+ " group by produto.id "
				+ " order by produto.nome ";
		
		Query query = super.getSession().createQuery(hql);
	
		query.setParameterList("status", Arrays.asList(StatusLancamento.BALANCEADO,StatusLancamento.EXPEDIDO));
		
		query.setParameter("dataLancamentoDistribuidor", dataLancamento);
		
		return query.list();
	}
	
	@Override
	public BigDecimal obterDescontoLogistica(Long idProduto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p.descontoLogistica.percentualDesconto from Produto p ");
		hql.append(" where p.id = :idProduto ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("idProduto",idProduto);
		
		return (BigDecimal) query.uniqueResult();
	
	}

	@Override
	public String obterUltimoCodigoProdutoRegional() {
		
		Query query = 
			this.getSession().createQuery("select max(codigo) from Produto where length(codigo) = 10 and codigo like '10%'");
		
		return (String)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Produto> obterProdutoLikeCodigo(String codigo) {
		String hql = "from Produto produto "
				   + " where upper(produto.codigo) like upper(:codigo) order by produto.codigo";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("codigo", "%" + codigo + "%");
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> verificarProdutoExiste(String... codigoProduto) {
		StringBuilder hql = new StringBuilder("select codigo from produto where produto.codigo in (:codigoProdutoList)");
		
		SQLQuery query = super.getSession().createSQLQuery(hql.toString());
		query.setParameterList("codigoProdutoList", codigoProduto);
		
		return query.list();
	}

    @Override
    public Produto obterProdutoPorCodigoICD(String codigoProduto) {
        List list = getSession().createCriteria(Produto.class).add(Restrictions.eq("codigoICD", codigoProduto)).addOrder(Order.asc("nome")).list();
        return list.isEmpty() ? null : (Produto) list.get(0);
    }

    @Override
    public Produto obterProdutoPorCodigoICDLike(String codigoProduto) {
        List list = getSession().createCriteria(Produto.class).add(Restrictions.like("codigoICD", codigoProduto + "%")).addOrder(Order.asc("nome")).list();
        return list.isEmpty() ? null : (Produto) list.get(0);
    }

    @Override
    public Produto obterProdutoPorCodigoProdinLike(String codigoProduto) {
        String codigoProdutoLike = leftPad(left(codigoProduto, 6), 6, "0") + "%";
        List list = getSession().createCriteria(Produto.class).add(Restrictions.like("codigo", codigoProdutoLike)).addOrder(Order.asc("nome")).list();
        return list.isEmpty() ? null : (Produto) list.get(0);
    }

	@Override
	public boolean existeProdutoRegional(String codigo) {
		
		Query query = this.getSession().createQuery("select count(codigo) from Produto where codigo = :codigo");
		
		query.setParameter("codigo", codigo);
		
		return ((Long)query.uniqueResult()) > 0;
	}

	@Override
	public Produto obterProdutoPorICDBaseadoNoPrimeiroBarra(String codigo_icd) {
		
		String hql = "from Produto pd where pd.codigoICD = :cod_icd order by pd.codigo asc ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("cod_icd", codigo_icd);
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
		
	}
	
	@Override
	public Produto obterProdutoPorICDSegmentoNotNull(String codigo_icd) {
		
		String hql = "from Produto pd where pd.codigoICD = :cod_icd and pd.tipoSegmentoProduto is not null order by pd.codigo asc ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("cod_icd", codigo_icd);
		query.setMaxResults(1);
		
		return (Produto) query.uniqueResult();
		
	}
	

	
	@Override
	public List<Produto> obterProdutosPorDescontoLogistica(DescontoLogistica descontoLogistica) {
		
		List<Produto> list = getSession().createCriteria(Produto.class)
				.add(Restrictions.eq("descontoLogistica", descontoLogistica))
				.addOrder(Order.asc("nome"))
				//.setMaxResults(1)
				.list();
		
        return list;
	}
}
