package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository{

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	//TODO UTILIZAR DTO DE FILTRO...
	private String getConsultaListaContagemDevolucao(Object filtro, boolean indBuscaQtd) {
		
		//TODO: param abaixo passado com o filtro remover abaixo pos testes
		List listaIdProdutoDosFornecedores = null;
		
		StringBuffer hql = new StringBuffer("");
		
		if (indBuscaQtd) {

			hql.append(" select count( lancamento.produtoEdicao.id ) " );		
			
		} else {

			hql.append(" select new " + ContagemDevolucaoDTO.class.getCanonicalName() 	);		
			
			hql.append(" ( 	parcial.id,  											");		
			hql.append("  	lancamento.produtoEdicao.produto.codigo,  				");		
			hql.append(" 	lancamento.produtoEdicao.produto.codigo.nome, 			");
			hql.append(" 	lancamento.produtoEdicao.numeroEdicao, 					");
			hql.append(" 	lancamento.produtoEdicao.precoVenda, 					");
			hql.append("  	sum( movimento.qtde ) as qtdDevolucao,	 				");
			hql.append("  	parcial.qtde,	 										");
			hql.append("  	lancamento.dataRecolhimentoDistribuidor,	 			");
			hql.append("  	parcial.dataConfEncalheParcial,	 						");
			hql.append("  	parcial.dataAprovacao,	 								");
			hql.append("  	parcial.statusAprovacao	 )								");
			
		}
		
		hql.append(" from ");		
		
		hql.append(" Lancamento lancamento,					");		
		hql.append(" MovimentoEstoqueCota movimento,		");		
		hql.append(" ConferenciaEncalheParcial parcial		");		
		
		hql.append(" where ");	
		
		hql.append(" lancamento.dataRecolhimentoDistribuidor = :dataRecolhimentoDistribuidor and	");		
		
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			hql.append(" lancamento.produtoEdicao.produto.id in  ( :listaIdProdutoDosFornecedores ) and 	");		
		}
		
		hql.append(" ( ( lancamento.dataRecolhimentoDistribuidor = parcial.dataRecolhimentoDistribuidor	and ");		
		hql.append(" lancamento.produtoEdicao.id = parcial.produtoEdicao.id ) or  	");		

		hql.append(" parcial.id is null ) and ");		
		
		hql.append(" ( ( lancamento.dataRecolhimentoDistribuidor = movimento.dataInclusao	and ");		
		hql.append(" lancamento.produtoEdicao.id = movimento.produtoEdicao.id and movimento.tipoMovimento = :tipoMovimento ) or  	");		

		hql.append("  movimento.id is null ) ");		

		hql.append(" group by ");		

		hql.append("  	parcial.id,  											");		
		hql.append("  	lancamento.produtoEdicao.produto.codigo,  				");		
		hql.append(" 	lancamento.produtoEdicao.produto.codigo.nome, 			");
		hql.append(" 	lancamento.produtoEdicao.numeroEdicao, 					");
		hql.append(" 	lancamento.produtoEdicao.precoVenda, 					");
		hql.append("  	parcial.qtde,	 										");
		hql.append("  	lancamento.dataRecolhimentoDistribuidor,	 			");
		hql.append("  	parcial.dataConfEncalheParcial,	 						");
		hql.append("  	parcial.dataAprovacao,	 								");
		hql.append("  	parcial.statusAprovacao	 								");
		
		return hql.toString();
	}
	
	//TODO: 
	/*
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, Object filtroConsultaListaContagemDevolucao) {
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);
		query.setParameter("tipoMovimento", tipoMovimento);
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			query.setParameterList("listaIdProdutoDosFornecedores", listaIdProdutoDosFornecedores);
		}
		
		
	}*/
	
	 
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(
			Date dataRecolhimentoDistribuidor,
			TipoMovimentoEstoque tipoMovimento,
			List<Long> listaIdProdutoDosFornecedores) {
		
		String hql = getConsultaListaContagemDevolucao(null, false);
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);
		query.setParameter("tipoMovimento", tipoMovimento);
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			query.setParameterList("listaIdProdutoDosFornecedores", listaIdProdutoDosFornecedores);
		}
		
		//FIXME: 
		query.setFirstResult(0);

		//FIXME: 
		query.setMaxResults(1000);
		
		return query.list();
		
	}
	
	public Integer obterQuantidadeContagemDevolucao(
			Date dataRecolhimentoDistribuidor,
			TipoMovimentoEstoque tipoMovimento,
			List<Long> listaIdProdutoDosFornecedores) {
		
		String hql = getConsultaListaContagemDevolucao(null, false);
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoDistribuidor", dataRecolhimentoDistribuidor);
		query.setParameter("tipoMovimento", tipoMovimento);
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			query.setParameterList("listaIdProdutoDosFornecedores", listaIdProdutoDosFornecedores);
		}
		
		return (Integer) query.uniqueResult();
		
	}

	
}
