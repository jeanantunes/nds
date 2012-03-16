package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository {

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	private String getConsultaListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, List<Long> listaIdProdutoDosFornecedores, boolean indBuscaQtd) {
		
		StringBuffer hql = new StringBuffer("");
		
		if (indBuscaQtd) {

			hql.append(" select count( lancamento.produtoEdicao.id ) " );		
			
		} else {

			hql.append(" select new " + ContagemDevolucaoDTO.class.getCanonicalName() 	);		
			
			hql.append(" ( 	parcial.id,  																");		
			hql.append("  	lancamento.produtoEdicao.produto.codigo,  									");		
			hql.append(" 	lancamento.produtoEdicao.produto.codigo.nome, 								");
			hql.append(" 	lancamento.produtoEdicao.numeroEdicao, 										");
			hql.append(" 	lancamento.produtoEdicao.precoVenda, 										");
			hql.append("  	sum( movimento.qtde ) as qtdDevolucao,	 									");
			hql.append("  	sum( lancamento.produtoEdicao.precoVenda * movimento.qtde ) as valorTotal,	");
			hql.append("  	parcial.qtde,	 															");
			hql.append("  	sum( movimento.qtde  - parcial.qtde  ) as diferenca,						");
			hql.append("  	lancamento.dataRecolhimentoDistribuidor,	 								");
			hql.append("  	parcial.dataConfEncalheParcial,	 											");
			hql.append("  	parcial.dataAprovacao,	 													");
			hql.append("  	parcial.statusAprovacao	 )													");
			
		}
		
		hql.append(" from ");		
		
		hql.append(" Lancamento lancamento,					");		
		hql.append(" MovimentoEstoqueCota movimento,		");		
		hql.append(" ConferenciaEncalheParcial parcial		");		
		
		hql.append(" where ");	
		
		hql.append(" ( lancamento.dataRecolhimentoDistribuidor between :dataRecolhimentoDistribuidorInicial and :dataRecolhimentoDistribuidorFinal ) and ");		
		
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			hql.append(" lancamento.produtoEdicao.produto.id in  ( :listaIdProdutoDosFornecedores ) and 	");		
		}
		
		hql.append(" ( ( lancamento.dataRecolhimentoDistribuidor = parcial.dataRecolhimentoDistribuidor	and ");		
		hql.append(" lancamento.produtoEdicao.id = parcial.produtoEdicao.id ) or  	");		

		hql.append(" parcial.id is null ) and ");		
		
		hql.append(" ( ( lancamento.dataRecolhimentoDistribuidor = movimento.dataInclusao	and ");		
		hql.append(" lancamento.produtoEdicao.id = movimento.produtoEdicao.id and movimento.tipoMovimento = :tipoMovimentoEstoque ) or  ");		

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
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (filtro.getOrdenacaoColuna() != null) {

			hql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case CODIGO_PRODUTO:
						orderByColumn = " lancamento.produtoEdicao.produto.codigo ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " lancamento.produtoEdicao.produto.codigo.nome ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " lancamento.produtoEdicao.numeroEdicao ";
						break;
					case PRECO_CAPA:
						orderByColumn = " lancamento.produtoEdicao.precoVenda ";
						break;
					case QTD_DEVOLUCAO:
						orderByColumn = " qtdDevolucao ";
						break;
					case VALOR_TOTAL:
						orderByColumn = " valorTotal ";
						break;
					case QTD_NOTA:
						orderByColumn = " parcial.qtde ";
						break;
					case DIFERENCA:
						orderByColumn = " diferenca ";
						break;
						
					default:
						break;
				}
			
			hql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
				
			}
			
		}
		
		return hql.toString();
	}
	
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque, List<Long> listaIdProdutoDosFornecedores) {
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataRecolhimentoDistribuidorInicial", filtro.getPeriodo().getDataInicial());

		query.setParameter("dataRecolhimentoDistribuidorFinal", filtro.getPeriodo().getDataFinal());
		
		query.setParameter("tipoMovimentoEstoque", tipoMovimentoEstoque);
		
		if(listaIdProdutoDosFornecedores != null && !listaIdProdutoDosFornecedores.isEmpty()) {
			
			query.setParameterList("listaIdProdutoDosFornecedores", listaIdProdutoDosFornecedores);
			
		}
	
		return query;
		
	}
	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(FiltroDigitacaoContagemDevolucaoDTO filtro, TipoMovimentoEstoque tipoMovimentoEstoque, List<Long> listaIdProdutoDosFornecedores) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, listaIdProdutoDosFornecedores, false);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, listaIdProdutoDosFornecedores);
		
		query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());

		query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
		
	}
	
	
	public Integer obterQuantidadeContagemDevolucao( 
			FiltroDigitacaoContagemDevolucaoDTO filtro, 
			TipoMovimentoEstoque tipoMovimentoEstoque, 
			List<Long> listaIdProdutoDosFornecedores) {
		
		String hql = getConsultaListaContagemDevolucao(filtro, listaIdProdutoDosFornecedores, true);
		
		Query query = criarQueryComParametrosObterListaContagemDevolucao(hql, filtro, tipoMovimentoEstoque, listaIdProdutoDosFornecedores);
		
		return (Integer) query.uniqueResult();
		
	}

	
}
