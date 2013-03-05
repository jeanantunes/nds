package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseEstudoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class AnaliseEstudoRepositoryImpl extends AbstractRepositoryModel<AnaliseEstudoDTO, Long> implements AnaliseEstudoRepository {
	
	public AnaliseEstudoRepositoryImpl( ) {
		super(AnaliseEstudoDTO.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliseEstudoDTO> buscarEstudos(FiltroAnaliseEstudoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" estudo.id AS numeroEstudo, ");
		hql.append(" produto.codigo AS codigoProduto, ");
		hql.append(" produto.nome AS nomeProduto, ");
		hql.append(" prodEdicao.numeroEdicao AS numeroEdicaoProduto, ");
		hql.append(" tpClassifProduto.descricao AS descicaoTpClassifProd, ");
		hql.append(" periodicidade.codigo AS codPeriodoProd ");
//		hql.append(" produto.periodicidade AS codPeriodoProd ");
		
		hql.append(" FROM Estudo AS estudo ");
		hql.append(" LEFT JOIN estudo.produtoEdicao AS prodEdicao ");
		hql.append(" LEFT JOIN prodEdicao.produto AS produto ");
		hql.append(" LEFT JOIN produto.periodicidade AS periodicidade ");
		hql.append(" LEFT JOIN produto.tipoClassificacaoProduto AS tpClassifProduto ");
		
		hql.append(" WHERE estudo.id = :NUM_ESTUDO ");
//		hql.append(" produto.codigo = :COD_PRODUTO OR ");
//		hql.append(" produto.nome = :NOME_PRODUTO OR ");
//		hql.append(" prodEdicao.numeroEdicao = :NUM_EDICAO_PRODUTO OR ");
//		hql.append(" tpClassifProd.id = :ID_TIPO_CLASS_PRODUTO ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("NUM_ESTUDO", filtro.getNumeroEstudo());
//		query.setParameter("COD_PRODUTO", filtro.getCodigoProduto());
//		query.setParameter("NOME_PRODUTO", filtro.getNome());
//		query.setParameter("NUM_EDICAO_PRODUTO", filtro.getNumeroEdicao());
//		query.setParameter("ID_TIPO_CLASS_PRODUTO", filtro.getIdTipoClassificacaoProduto());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				AnaliseEstudoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
		
	}
	
	private void configurarPaginacao(FiltroAnaliseEstudoDTO filtro, Query query) {

		PaginacaoVO paginacao = filtro.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}

}
