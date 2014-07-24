package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoSegmentoProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class TipoSegmentoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoSegmentoProduto, Long> implements
		TipoSegmentoProdutoRepository {

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoSegmentoProduto> obterTipoSegmentoProdutoOrdenados(Ordenacao ordem) {

		Criteria criteria = getSession().createCriteria(TipoSegmentoProduto.class);
		
		ordem = ordem == null ? Ordenacao.ASC : ordem;
		
		String columnToOrder = "descricao";
		
		switch(ordem) {
		case DESC:
			criteria.addOrder(Order.desc(columnToOrder));
		default:
			criteria.addOrder(Order.asc(columnToOrder));
		}
		
		return criteria.list();
	}

	public TipoSegmentoProdutoRepositoryImpl(){
		super(TipoSegmentoProduto.class);
	}
	
}
