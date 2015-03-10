package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.estoque.EstoqueProdutoCota;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.financeiro.StatusInadimplencia;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoCotaRepository;

@Repository
public class EstoqueProdutoCotaRepositoryImpl extends AbstractRepositoryModel<EstoqueProdutoCota, Long> implements EstoqueProdutoCotaRepository{

	public EstoqueProdutoCotaRepositoryImpl() {
		super(EstoqueProdutoCota.class);
	}

	@Override
	public EstoqueProdutoCota buscarEstoquePorProdutoECota(Long idProdutoEdicao, Long idCota) {

		if(	idProdutoEdicao == null ) {
			throw new NullPointerException();
		}
		
		StringBuilder hql = new StringBuilder("select estoque ");
		hql.append(" from EstoqueProdutoCota estoque    ")
		   .append(" join  estoque.produtoEdicao produtoEdicao ")
		   .append(" join estoque.cota cota")
		   .append(" where produtoEdicao.id =:idProdutoEdicao ")
		   .append(" and cota.id            =:idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (EstoqueProdutoCota) query.uniqueResult();
	}
	
	public EstoqueProdutoCota buscarEstoquePorProdutEdicaoECota(Long idProdutoEdicao, Long idCota) {
		StringBuilder hql = new StringBuilder("select estoqueProdutoCota ");
		hql.append(" from EstoqueProdutoCota estoqueProdutoCota, ProdutoEdicao produtoEdicao, Cota cota ")
		   .append(" where estoqueProdutoCota.produtoEdicao.id = produtoEdicao.id ")
		   .append(" and estoqueProdutoCota.cota.id            = cota.id ")
		   .append(" and produtoEdicao.id                      = :idProdutoEdicao ")
		   .append(" and cota.id                               = :idCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (EstoqueProdutoCota) query.uniqueResult();
	}
	
	public BigInteger obterTotalEmEstoqueProdutoCota(Long idCota, Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select sum(estoque.qtdeRecebida - estoque.qtdeDevolvida) 	");
		hql.append(" from EstoqueProdutoCota estoque  							");
		hql.append(" where estoque.cota.id = :idCota and 						");
		hql.append(" estoque.produtoEdicao.id = :idProdutoEdicao  				");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return (BigInteger) query.uniqueResult();
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoCota> buscarListaEstoqueProdutoCota(Long idLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoCota ")
		   .append(" from EstudoCota estudoCota ")
		   .append(" join estudoCota.estudo estudo ")
		   .append(" join estudo.lancamentos lancamento, EstoqueProdutoCota estoqueProdutoCota ")
		   
		   .append(" where estoqueProdutoCota.produtoEdicao = estudo.produtoEdicao ")
		   .append(" and estoqueProdutoCota.cota = estudoCota.cota")
		   .append(" and lancamento.id = :idLancamento ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idLancamento", idLancamento);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<EstoqueProdutoCota> buscarEstoqueProdutoCotaCompraSuplementar(Long idLancamento){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoCota ")
		   .append(" from EstoqueProdutoCota estoqueProdutoCota ")
		   .append(" join estoqueProdutoCota.movimentos mec ")
		   .append(" join mec.tipoMovimento tm ")
		   .append(" join mec.lancamento l ")
		   .append(" where l.id = :idLancamento ")
		   .append(" and tm.grupoMovimentoEstoque in ( :gruposMovimento ) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idLancamento", idLancamento);
		query.setParameterList("gruposMovimento", Arrays.asList(GrupoMovimentoEstoque.COMPRA_SUPLEMENTAR));
		
		return query.list();
	}
	
	public BigDecimal buscarQuantidadeEstoqueProdutoEdicao(Long numeroEdicao, String codigoProduto ,Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select (estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) ")
			.append(" from EstoqueProdutoCota estoqueProdutoCota " )
			.append(" join estoqueProdutoCota.cota cota ")
			.append(" join estoqueProdutoCota.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ")
			.append(" where produtoEdicao.numeroEdicao = :numeroEdicao ")
			.append(" and produto.codigo =:codigoProduto ")
			.append(" and cota.numeroCota =:numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroEdicao", numeroEdicao);
		query.setParameter("codigoProduto", codigoProduto);
		query.setParameter("numeroCota", numeroCota);
		query.setMaxResults(1);
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public BigDecimal obterConsignado(boolean cotaInadimplente){
		
		StringBuilder hql = new StringBuilder("select  ");
		hql.append(" (sum(es.qtdeRecebida) * sum(es.produtoEdicao.precoVenda)) - (sum(es.qtdeDevolvida) * sum(es.produtoEdicao.precoVenda)) ")
		   .append(" from EstoqueProdutoCota es ");
		
		if (cotaInadimplente){
			
			hql.append(" where es.cota.id not in ( ")
			   .append(" select distinct hist.divida.cota.id ")
			   .append(" from AcumuloDivida hist ")
			   .append(" where hist.status != :quitada) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (cotaInadimplente){
			
			query.setParameter("quitada", StatusInadimplencia.QUITADA);
		}
		
		return (BigDecimal) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<FixacaoReparteDTO> obterHistoricoEdicaoPorProduto(Produto produto, String classificacaoProduto, Integer numeroCota){
		
		StringBuilder sql = new StringBuilder("");

		sql.append(" select ");
		sql.append("    produto_edicao.NUMERO_EDICAO as edicao, ");
		sql.append("    produto.codigo_icd as codigoProduto, ");
		sql.append("    ifnull(tcp.descricao, '') as classificacaoProduto, ");
		sql.append("    ifnull(estoque_produto_cota.QTDE_RECEBIDA, 0) as reparte,");
		sql.append("  	ifnull(estoque_produto_cota.QTDE_RECEBIDA - estoque_produto_cota.QTDE_DEVOLVIDA, 0 ) as venda, ");
		sql.append("  	lancamento.STATUS as status, ");
		sql.append("  	lancamento.DATA_LCTO_PREVISTA as dataLancamento, ");
		sql.append(" 	lancamento.DATA_REC_PREVISTA as dataRecolhimento");
		sql.append(" from produto_edicao ");
		sql.append("	left join estoque_produto_cota on estoque_produto_cota.PRODUTO_EDICAO_ID = produto_edicao.id ");
		sql.append("	join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID ");
		sql.append("	join produto on produto.ID = produto_edicao.PRODUTO_ID");
		sql.append("	left join tipo_classificacao_produto tcp on tcp.ID = produto_edicao.TIPO_CLASSIFICACAO_PRODUTO_ID");
		sql.append(" where produto.codigo_icd = :produtoBusca ");
		sql.append(" and estoque_produto_cota.COTA_ID = (select c.ID from cota c where c.NUMERO_COTA = :numeroCota ) ");
		
				
        if (!classificacaoProduto.equalsIgnoreCase("-1")) {
            sql.append(" and tcp.id = :classificacaoProduto ");
        }
		sql.append("    group by produto_edicao.NUMERO_EDICAO ");
		sql.append("    order by produto_edicao.NUMERO_EDICAO desc ");
		sql.append("    limit 6");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("produtoBusca", produto.getCodigoICD());
		query.setParameter("numeroCota", numeroCota);
		
        if (!classificacaoProduto.equalsIgnoreCase("-1")) {
            query.setParameter("classificacaoProduto", classificacaoProduto);
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
		
		return query.list();
	}
		
	@SuppressWarnings("unchecked")
	public List<FixacaoReparteDTO> obterHistoricoEdicaoPorCota(Cota cota, String codigoProduto, String classificacaoProduto){
			
			StringBuilder sql = new StringBuilder("");
	
			sql.append(" select  produto.codigo_icd as codigoProduto, ");
			sql.append("    ifnull(tcp.descricao, '') as classificacaoProduto, ");
			sql.append("    produto_edicao.NUMERO_EDICAO as edicao, ");
			sql.append("  	estoque_produto_cota.QTDE_RECEBIDA as reparte,");
			sql.append("  	ifnull(estoque_produto_cota.QTDE_RECEBIDA - estoque_produto_cota.QTDE_DEVOLVIDA, 0 ) as venda, ");
			sql.append("  	lancamento.STATUS as status, ");
			sql.append("  	lancamento.DATA_LCTO_PREVISTA as dataLancamento, ");
			sql.append(" 	lancamento.DATA_REC_PREVISTA as dataRecolhimento");
			sql.append("  		 from estoque_produto_cota  ");
			sql.append(" 			join produto_edicao on estoque_produto_cota.PRODUTO_EDICAO_ID = produto_edicao.id ");
			sql.append("  			join lancamento on lancamento.PRODUTO_EDICAO_ID = produto_edicao.ID ");
			sql.append(" 			join produto on produto.ID = produto_edicao.PRODUTO_ID");
			sql.append(" 			left join tipo_classificacao_produto tcp on tcp.ID = produto_edicao.TIPO_CLASSIFICACAO_PRODUTO_ID");
            sql.append(" where cota_id = :cotaBusca ");
            sql.append(" and produto.CODIGO_ICD = :produtoBusca ");

            if (!classificacaoProduto.equalsIgnoreCase("-1")) {
                sql.append(" and tcp.ID = :classificacaoProduto");
            }

            sql.append("	group by edicao order by edicao desc limit 6");

			SQLQuery query = getSession().createSQLQuery(sql.toString());
			
			query.setParameter("cotaBusca", cota.getId());
			query.setParameter("produtoBusca", codigoProduto);

            if (!classificacaoProduto.equalsIgnoreCase("-1")) {
                query.setParameter("classificacaoProduto", classificacaoProduto);
            }

            query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
			
			return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ItemDTO<Long, BigInteger> loadIdAndQtdDevolvidaByIdProdutoEdicaoAndIdCota(final Long idProdutoEdicao, final Long idCota){
		

		final StringBuilder hql = new StringBuilder("select estoque.qtdeDevolvida as value , estoque.id as key ");
		hql.append("from EstoqueProdutoCota estoque    ")
		.append("join estoque.produtoEdicao produtoEdicao ")
		.append("join estoque.cota cota ")
		.append("where produtoEdicao.id = :idProdutoEdicao ")
		.append("and cota.id = :idCota ");

		final Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setParameter("idCota", idCota);
		query.setMaxResults(1);

		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));

		return (ItemDTO<Long, BigInteger>) query.uniqueResult();
		
		
	}
	
	@Override
	public void updateById(Long id, BigInteger qtdeDevolvida){
		final String jpql = "UPDATE EstoqueProdutoCota o SET o.qtdeDevolvida = :qtdeDevolvida WHERE o.id = :id";
		
		super.getSession().createQuery(jpql)
			.setParameter("qtdeDevolvida", qtdeDevolvida)
			.setParameter("id", id)
			.executeUpdate();
	}
	
	@Override
	public BigInteger obterVendaBaseadoNoEstoque(Long idProdutoEdicao){
		
		final StringBuilder sql = new StringBuilder("");
		
		sql.append("select sum(epc.QTDE_RECEBIDA)-sum(epc.QTDE_DEVOLVIDA) as venda from estoque_produto_cota epc where epc.PRODUTO_EDICAO_ID = :idProdutoEdicao");
		
		final Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("idProdutoEdicao", idProdutoEdicao);

		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));

		return (BigInteger) query.uniqueResult();
		
	}
	
}
