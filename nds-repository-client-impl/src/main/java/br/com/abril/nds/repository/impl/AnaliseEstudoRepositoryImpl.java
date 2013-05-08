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
		hql.append(" prodEdicao.parcial as permiteParcial, ");
		hql.append(" estudo.liberado AS statusLiberadoOuGerado ");
		
		hql.append(" FROM Estudo estudo, Lancamento lancamento");
		hql.append(" JOIN estudo.produtoEdicao as prodEdicao ");
		hql.append(" JOIN prodEdicao.produto as produto ");
		hql.append(" JOIN produto.tipoClassificacaoProduto as tpClassifProduto ");
		
		hql.append(" WHERE estudo.produtoEdicao.id = lancamento.produtoEdicao.id ");
		
		hql.append(this.getSqlWhereBuscarEstudos(filtro));
		hql.append(" ORDER BY prodEdicao.numeroEdicao desc ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.paramsDinamicos(query, filtro);

		query.setParameter("RECOLHIDO", StatusLancamento.RECOLHIDO);
		query.setParameter("EXPEDIDO", StatusLancamento.EXPEDIDO);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseEstudoDTO.class));
		
		configurarPaginacao(filtro, query);
		
		return query.list();
		
	}

	private String getSqlWhereBuscarEstudos(FiltroAnaliseEstudoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		if(filtro.getNumEstudo() !=null && filtro.getNumEstudo() > 0){
			hql.append(" AND estudo.id = :NUM_ESTUDO ");
		}
		if(filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()){
			hql.append(" AND produto.codigo = :COD_PRODUTO ");
		}
		if(filtro.getNome() !=null && !filtro.getNome().isEmpty()){
			hql.append(" AND produto.nome = :NOME_PRODUTO ");
		}
		if(filtro.getNumeroEdicao() !=null && filtro.getNumeroEdicao() > 0){
			hql.append(" AND prodEdicao.numeroEdicao = :NUM_EDICAO_PRODUTO ");
		}
		
		if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
			hql.append(" AND tpClassifProduto.id = :ID_TIPO_CLASS_PRODUTO ");
		}
		
		return hql.toString();
	}
	
	private void paramsDinamicos (Query query, FiltroAnaliseEstudoDTO filtro){
		
		if(filtro.getNumEstudo() !=null && filtro.getNumEstudo() > 0){
			query.setParameter("NUM_ESTUDO", filtro.getNumEstudo());
		}
		if(filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()){
			query.setParameter("COD_PRODUTO", filtro.getCodigoProduto());
		}
		if(filtro.getNome() !=null && !filtro.getNome().isEmpty()){
			query.setParameter("NOME_PRODUTO", filtro.getNome());
		}
		if(filtro.getNumeroEdicao() !=null && filtro.getNumeroEdicao() > 0){
			query.setParameter("NUM_EDICAO_PRODUTO", filtro.getNumeroEdicao());
		}
		
		if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
			query.setParameter("ID_TIPO_CLASS_PRODUTO", filtro.getIdTipoClassificacaoProduto());
		}
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
