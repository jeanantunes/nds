package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.DescontoProduto;
import br.com.abril.nds.repository.DescontoProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente a acesso de dados
 * para as pesquisas de desconto do produto
 * 
 * @author Discover Technology
 */
@Repository
public class DescontoProdutoRepositoryImpl extends AbstractRepositoryModel<DescontoProduto,Long> implements DescontoProdutoRepository {

	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	public DescontoProdutoRepositoryImpl() {
		super(DescontoProduto.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoProdutoDTO> buscarTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT ")
		.append("vdpe.desconto_id as idTipoDesconto ")
		.append(", vdpe.codigo_produto as codigoProduto ")
		.append(", vdpe.nome_produto as nomeProduto ")
		.append(", vdpe.numero_edicao as numeroEdicao ")
		.append(", coalesce(d.valor, vdpe.valor, 0) as desconto ")
		.append(", coalesce(d.data_alteracao, vdpe.data_alteracao, null) as dataAlteracao ")
		.append(", vdpe.nome_usuario as nomeUsuario ")
		.append("FROM VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ")
		.append("LEFT OUTER JOIN HISTORICO_DESCONTO_PRODUTO_EDICOES hdpe ON vdpe.produto_edicao_id = hdpe.produto_edicao_id ") 
		.append("LEFT OUTER JOIN DESCONTO d ON hdpe.desconto_id = d.id ")
		.append("WHERE vdpe.codigo_produto = :codigoProduto ");
		
		hql.append("order by numeroEdicao, dataAlteracao desc");
		
		Query q = getSession().createSQLQuery(hql.toString());

		q.setParameter("codigoProduto", filtro.getCodigoProduto());

		if (filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			q.setFirstResult( filtro.getPaginacao().getQtdResultadosPorPagina() * ( (filtro.getPaginacao().getPaginaAtual() - 1 )))
			.setMaxResults( filtro.getPaginacao().getQtdResultadosPorPagina() );
		}

		q.setResultTransformer(Transformers.aliasToBean(TipoDescontoProdutoDTO.class));
		
		return (List<TipoDescontoProdutoDTO>) q.list();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer buscarQuantidadeTipoDescontoProduto(FiltroTipoDescontoProdutoDTO filtro) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("select ")
		.append("count(*) ")
		.append("from VIEW_DESCONTO_PRODUTOS_EDICOES as vdpe ")
		.append("where vdpe.codigo_produto = :codigoProduto ");
		
		Query q = getSession().createSQLQuery(hql.toString());

		q.setParameter("codigoProduto", filtro.getCodigoProduto());

		if (filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			q.setFirstResult( filtro.getPaginacao().getQtdResultadosPorPagina() * ( (filtro.getPaginacao().getPaginaAtual() - 1 )))
			.setMaxResults( filtro.getPaginacao().getQtdResultadosPorPagina() );
		}

		
		return ((BigInteger) q.uniqueResult()).intValue();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<CotaDescontoProdutoDTO> obterCotasDoTipoDescontoProduto(Long idDescontoProduto, Ordenacao ordenacao) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT PRODUTO_ID ")
			.append("FROM VIEW_DESCONTO_PRODUTOS_EDICOES AS vdpe ")
			.append("WHERE vdpe.DESCONTO_ID = :idDesconto");
		
		Query q1 = getSession().createSQLQuery(hql.toString());
		
		q1.setParameter("idDesconto", idDescontoProduto);
		
		Long produtoId = ((BigInteger) q1.uniqueResult()).longValue();
		
		hql = new StringBuilder();
		hql.append(" select cota.numeroCota as numeroCota, ");
		hql.append(" case when cota.pessoa.nome is not null then ");
		hql.append(" cota.pessoa.nome ");
		hql.append(" else ");
		hql.append(" cota.pessoa.razaoSocial ");
		hql.append(" end ");
		hql.append(" as nome ");
		hql.append(" from Produto as p ");
		hql.append(" inner join p.fornecedores as f ");
		hql.append(" inner join f.cotas as cota ");
		hql.append(" where p.id = :idProduto ");
		hql.append(" order by cota.numeroCota ");
		hql.append(ordenacao.getOrdenacao() == null ? "" : ordenacao.getOrdenacao());

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProduto", produtoId);

		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDescontoProdutoDTO.class));

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDescontoProdutoDTO> obterTiposDescontoProdutoPorCota(Long idCota, String sortorder, String sortname) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select produtoEdicao.produto.codigo as codigoProduto, ");
		hql.append(" produtoEdicao.produto.nome as nomeProduto, ");
		hql.append(" produtoEdicao.numeroEdicao as numeroEdicao, ");
		hql.append(" descontoProduto.desconto as desconto, ");
		hql.append(" descontoProduto.dataAlteracao as dataAlteracao, ");
		hql.append(" usuario.nome as nomeUsuario ");
		hql.append(" from DescontoProduto as descontoProduto ");
		hql.append(" join descontoProduto.cotas as cota ");
		hql.append(" join descontoProduto.usuario as usuario ");
		hql.append(" join descontoProduto.produtoEdicao as produtoEdicao ");

		if (idCota!=null){
			hql.append(" where cota.id = :idCota ");
		}

		if (sortname != null && !sortname.isEmpty()) { 

			hql.append(" order by ");
			hql.append(sortname);
			hql.append(" ");
			hql.append(sortorder != null ? sortorder : "");
		}

		Query query = getSession().createQuery(hql.toString());

		if (idCota!=null){
			query.setParameter("idCota", idCota);
		}

		query.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoProdutoDTO.class));

		return query.list();
	}

	@Override
	public DescontoProduto buscarUltimoDescontoValido(Cota cota,ProdutoEdicao produtoEdicao) {

		return obterDescontoValido(null, cota, produtoEdicao);
	}

	@Override
	public DescontoProduto buscarUltimoDescontoValido(Long idDesconto,Cota cota, ProdutoEdicao produtoEdicao) {

		return obterDescontoValido(idDesconto, cota, produtoEdicao);
	}

	private DescontoProduto obterDescontoValido(Long idDesconto, Cota cota, ProdutoEdicao produtoEdicao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select desconto from DescontoProduto desconto JOIN desconto.produtoEdicao produtoEdicao JOIN desconto.cotas cota  ")
		.append("where desconto.dataAlteracao = ")
		.append(" ( select max(descontoSub.dataAlteracao) from DescontoProduto descontoSub  ")
		.append(" JOIN descontoSub.produtoEdicao produtoEdicaoSub JOIN descontoSub.cotas cotaSub ")
		.append(" where produtoEdicaoSub.id =:idProdutoEdicao ")
		.append(" and cotaSub.id =:idCota ");
		if(idDesconto!= null){
			hql.append(" and descontoSub.id not in (:idUltimoDesconto) ");
		}

		hql.append(" ) ")
		.append(" AND produtoEdicao.id =:idProdutoEdicao ")
		.append(" AND cota.id =:idCota ");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idProdutoEdicao",produtoEdicao.getId());

		if(idDesconto!= null){

			query.setParameter("idUltimoDesconto", idDesconto);
		}

		query.setParameter("idCota", cota.getId());

		query.setMaxResults(1);

		return (DescontoProduto)  query.uniqueResult();
	}
}
