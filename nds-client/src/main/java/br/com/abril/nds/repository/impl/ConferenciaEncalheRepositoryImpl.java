package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.estoque.ConferenciaEncalhe;
import br.com.abril.nds.repository.ConferenciaEncalheRepository;

@Repository
public class ConferenciaEncalheRepositoryImpl extends
		AbstractRepository<ConferenciaEncalhe, Long> implements
		ConferenciaEncalheRepository {

	public ConferenciaEncalheRepositoryImpl() {
		super(ConferenciaEncalhe.class);
	}
	
	
	public List<ConferenciaEncalheDTO> obterListaConferenciaEncalheDTO(Long idControleConferenciaEncalheCota, Long idDistribuidor) {
		
		String subQueryConsultaValorComissionamento = getSubQueryConsultaValorComissionamento();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		
		hql.append(" conferenciaEncalhe.id as idConferenciaEncalhe, ");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.qtde as qtdExemplar,  ");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.id as idProdutoEdicao,			 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoDeBarras as codigoDeBarras, 	");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.codigoSM as codigoSM, 				");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.codigo as codigo, 			");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.produto.nome as nomeProduto, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.numeroEdicao as numeroEdicao, 		");
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda as precoCapa, 			");

		hql.append( subQueryConsultaValorComissionamento );
		
		hql.append(" as desconto, ");
		
		hql.append(" ( ");
		
		hql.append(" conferenciaEncalhe.movimentoEstoqueCota.qtde * ");
		
		hql.append(" 	( ");
		
		hql.append(" 		conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda -  ");

		hql.append(" 		( ");

		hql.append(" 			conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.precoVenda * ");
		
		hql.append(" ( ");
		
		hql.append( subQueryConsultaValorComissionamento );

		hql.append(" / 100 ) ");
		
		hql.append(" 		) ");
		
		hql.append(" 	) ");
		
		hql.append(" ) as valorTotal, ");
		
		hql.append(" conferenciaEncalhe.observacao as observacao, ");
		
		hql.append(" conferenciaEncalhe.juramentada as juramentada ");
		
		hql.append(" from ConferenciaEncalhe conferenciaEncalhe ");
		
		hql.append(" where ");
		
		hql.append(" conferenciaEncalhe.controleConferenciaEncalheCota.id = :idControleConferenciaEncalheCota ");
		
		Query query =  this.getSession().createQuery(hql.toString()).setResultTransformer(new AliasToBeanResultTransformer(ConferenciaEncalheDTO.class));
		
		query.setParameter("idControleConferenciaEncalheCota", idControleConferenciaEncalheCota);
		
		query.setParameter("idDistribuidor", idDistribuidor);
		
		return query.list();
		
		
	}

	/**
	 * Retorna String referente a uma subquery que obtém o valor comissionamento 
	 * (percentual de desconto) para determinado produtoEdicao a partir de idCota e idDistribuidor. 
	 * 
	 * @return String
	 */
	private static String getSubQueryConsultaValorComissionamento() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ( select case when ( pe.desconto is not null ) then pe.desconto else ");
		
		hql.append(" ( case when ( ct.fatorDesconto is not null ) then ct.fatorDesconto  else  ");
		
		hql.append(" ( case when ( distribuidor.fatorDesconto is not null ) then distribuidor.fatorDesconto else 0 end ) end  ");
		
		hql.append(" ) end ");
		
		hql.append(" from ProdutoEdicao pe, Cota ct, Distribuidor distribuidor ");
		
		hql.append(" where ");
		
		hql.append(" ct.id = conferenciaEncalhe.movimentoEstoqueCota.cota.id and ");

		hql.append(" pe.id = conferenciaEncalhe.movimentoEstoqueCota.produtoEdicao.id and ");

		hql.append(" distribuidor.id = :idDistribuidor ) ");
		
		return hql.toString();
		
	}
	
}
