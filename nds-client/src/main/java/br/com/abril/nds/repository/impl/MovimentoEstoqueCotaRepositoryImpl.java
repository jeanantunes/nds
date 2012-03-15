package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;

@Repository
public class MovimentoEstoqueCotaRepositoryImpl extends AbstractRepository<MovimentoEstoqueCota, Long> implements MovimentoEstoqueCotaRepository{

	public MovimentoEstoqueCotaRepositoryImpl() {
		super(MovimentoEstoqueCota.class);
	}

	
	public List<ContagemDevolucaoDTO> obterListaContagemDevolucao(Date dataOperacao, List<Long> listaIdProdutoDosFornecedores) {
		
		StringBuilder hql = new StringBuilder("");
		
		hql.append(" select new " + ContagemDevolucaoDTO.class.getCanonicalName() 	);		
		
		hql.append(" ( 	lancamento.produtoEdicao.produto.codigo,  				");		
		hql.append(" 	lancamento.produtoEdicao.produto.codigo.nome, 			");
		hql.append(" 	lancamento.produtoEdicao.numeroEdicao, 					");
		hql.append(" 	lancamento.produtoEdicao.precoCapa, 					");
		hql.append("  	sum( movimento.qtde ) as qtdDevolucao	) 				");
		
		hql.append(" from Lancamento lancamento, MovimentoEstoqueCota movimento	");		
		
		hql.append(" where ");	
		
		hql.append(" lancamento.dataLancamentoDistribuidor = :dataLancamentoDistribuidor and	");		
		hql.append(" lancamento.produtoEdicao.produto.id in :listaIdProdutoDosFornecedores and 	");		
		
		hql.append(" lancamento.dataLancamentoDistribuidor = m.dataInclusao	and ");		
		hql.append(" lancamento.produtoEdicao.id = m.produtoEdicao.id	");		
		
		
		hql.append(" group by m.dataInclusao asc	");		

		hql.append(" lancamento.produtoEdicao.produto.codigo,		");		
		hql.append(" lancamento.produtoEdicao.produto.codigo.nome,	");
		hql.append(" lancamento.produtoEdicao.numeroEdicao,			");
		hql.append(" lancamento.produtoEdicao.precoCapa				");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("listaIdProdutoDosFornecedores", listaIdProdutoDosFornecedores);
		
		return query.list();
		
	}

	
}
