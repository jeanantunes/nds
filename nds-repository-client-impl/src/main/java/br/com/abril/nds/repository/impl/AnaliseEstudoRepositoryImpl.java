package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseEstudoDTO;
import br.com.abril.nds.dto.filtro.FiltroAnaliseEstudoDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseEstudoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class AnaliseEstudoRepositoryImpl extends AbstractRepositoryModel implements AnaliseEstudoRepository {
	
	public AnaliseEstudoRepositoryImpl( ) {
		super(Object.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AnaliseEstudoDTO> buscarEstudos(FiltroAnaliseEstudoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" estudo.id AS numeroEstudo, ");
		hql.append(" prodEdicao.numeroEdicao AS numeroEdicaoProduto, ");
		hql.append(" produto.codigo AS codigoProduto, ");
		hql.append(" produto.nome AS nomeProduto, ");
		hql.append(" produto.periodicidade AS periodoProduto, ");
		hql.append(" tpClassifProduto.descricao AS descicaoTpClassifProd, ");
		
		hql.append(" CASE ");
		hql.append(" WHEN lancamento.status = :RECOLHIDO OR lancamento.status = :EXPEDIDO THEN lancamento.status ");
		hql.append(" ELSE null ");
		hql.append(" END AS statusRecolhiOuExpedido, ");
		
		hql.append(" CASE ");
		hql.append(" WHEN estudo.liberado = 1 OR estudo.liberado = 0 THEN estudo.liberado ");
		hql.append(" ELSE null ");
		hql.append(" END AS statusLiberadoOuGerado ");
		
		hql.append(" FROM Estudo estudo, Lancamento lancamento");
		hql.append(" JOIN estudo.produtoEdicao as prodEdicao ");
		hql.append(" JOIN prodEdicao.produto as produto ");
		hql.append(" JOIN produto.tipoClassificacaoProduto as tpClassifProduto ");
		
		hql.append(" WHERE estudo.produtoEdicao.id = lancamento.produtoEdicao.id AND ");
		hql.append(" estudo.id = :NUM_ESTUDO OR ");
//		hql.append(" prodEdicao.numeroEdicao = :NUM_EDICAO_PRODUTO OR ");
//		hql.append(" produto.codigo = :COD_PRODUTO OR ");
		hql.append(" produto.codigo = :COD_PRODUTO ");
//		hql.append(" produto.nome = :NOME_PRODUTO OR ");
				
//		hql.append(" tpClassifProduto.id = :ID_TIPO_CLASS_PRODUTO ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("NUM_ESTUDO", filtro.getNumEstudo());
		query.setParameter("COD_PRODUTO", filtro.getCodigoProduto());
//		query.setParameter("NOME_PRODUTO", filtro.getNome());
//		query.setParameter("NUM_EDICAO_PRODUTO", filtro.getNumeroEdicao());
//		query.setParameter("ID_TIPO_CLASS_PRODUTO", filtro.getIdTipoClassificacaoProduto());
		query.setParameter("RECOLHIDO", StatusLancamento.RECOLHIDO);
		query.setParameter("EXPEDIDO", StatusLancamento.EXPEDIDO);
		
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
