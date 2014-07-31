package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EstoqueProdutoFilaDTO;
import br.com.abril.nds.model.estoque.EstoqueProdutoFila;
import br.com.abril.nds.model.estoque.OperacaoEstoque;
import br.com.abril.nds.model.estoque.TipoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstoqueProdutoFilaRepository;

@Repository
public class EstoqueProdutoFilaRepositoryImpl extends AbstractRepositoryModel<EstoqueProdutoFila, Long> implements EstoqueProdutoFilaRepository {

	public EstoqueProdutoFilaRepositoryImpl() {
		super(EstoqueProdutoFila.class);
	}


	@Override
	public List<EstoqueProdutoFilaDTO> buscarTodosEstoqueProdutoFila() {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");

		hql.append(" estoqueProdutoFila.id as id,  					");
		hql.append(" produtoEdicao.id as idProdutoEdicao, 			");
		hql.append(" cota.id as idCota, 							");
		hql.append(" estoqueProdutoFila.qtde as qtde, 				");
		hql.append(" estoqueProdutoFila.tipoEstoque as tipoEstoque, ");
		hql.append(" estoqueProdutoFila.operacaoEstoque as operacaoEstoque ");
		
		hql.append(" from EstoqueProdutoFila estoqueProdutoFila ");
		
		hql.append(" join estoqueProdutoFila.cota cota			");
		hql.append(" join estoqueProdutoFila.produtoEdicao produtoEdicao ");
		
		hql.append(" group by estoqueProdutoFila.id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EstoqueProdutoFilaDTO.class));
		
		return query.list();
		
	}

	
	@Override
	public List<EstoqueProdutoFila> buscarEstoqueProdutoFilaDaCota(Long idCota) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoFila ");
		hql.append(" from EstoqueProdutoFila estoqueProdutoFila ");
		hql.append(" join estoqueProdutoFila.cota cota			");
		hql.append(" join estoqueProdutoFila.produtoEdicao produtoEdicao ");
		hql.append(" where cota.id = :idCota 					");
		hql.append(" order by produtoEdicao.id desc 			");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		return query.list();
		
	}

	@Override
	public List<EstoqueProdutoFila> buscarEstoqueProdutoFilaNumeroCota(Integer numeroCota) {
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select estoqueProdutoFila ");
		hql.append(" from EstoqueProdutoFila estoqueProdutoFila ");
		hql.append(" join estoqueProdutoFila.cota cota			");
		hql.append(" join estoqueProdutoFila.produtoEdicao produtoEdicao  ");
		hql.append(" where cota.numeroCota = :numeroCota 				  ");
		hql.append(" order by produtoEdicao.id desc 			");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return query.list();
		
	}
	
	@Override
	public void insert(Long idCota,
    		Long idProdutoEdicao, 
    		TipoEstoque tipoEstoque,
    		OperacaoEstoque operacaoEstoque,
    		BigInteger qtde){
		final StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO ESTOQUE_PRODUTO_FILA ");
		sql.append("(TIPO_ESTOQUE,COTA_ID,PRODUTO_EDICAO_ID,QTDE,OPERACAO_ESTOQUE) ");
		sql.append("VALUES (:tipoEstoque,:idCota,:idProdutoEdicao,:qtde,:operacaoEstoque)");
		
		super.getSession().createSQLQuery(sql.toString())
		.setParameter("tipoEstoque", tipoEstoque.name())
		.setParameter("idCota", idCota)
		.setParameter("idProdutoEdicao", idProdutoEdicao)
		.setParameter("qtde", qtde)
		.setParameter("operacaoEstoque", operacaoEstoque.name())
		.executeUpdate();
		
	}
	
}