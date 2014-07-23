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

@SuppressWarnings("rawtypes")
@Repository
public class AnaliseEstudoRepositoryImpl extends AbstractRepositoryModel implements AnaliseEstudoRepository {
	
	@SuppressWarnings("unchecked")
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
		hql.append(" coalesce(tpClassifProduto.descricao, '') AS descicaoTpClassifProd, ");
		
		hql.append(" (case when(periodoLancamentoParcial.numeroPeriodo > 1) then true else false end) as permiteParcial, ");
//		hql.append(" estudo.status AS statusEstudo, ");
		hql.append(" estudo.liberado AS statusGeradoOuLiberado, ");
		hql.append(" lancamento.dataLancamentoDistribuidor AS dataLancamento, ");
		hql.append(" periodoLancamentoParcial.numeroPeriodo AS codPeriodoProd ");
		
		hql.append(" FROM EstudoGerado estudo, Lancamento lancamento ");
		hql.append(" JOIN estudo.produtoEdicao as prodEdicao ");
		hql.append(" JOIN prodEdicao.produto as produto ");
		hql.append(" left JOIN prodEdicao.tipoClassificacaoProduto as tpClassifProduto ");
		hql.append(" left JOIN lancamento.periodoLancamentoParcial as periodoLancamentoParcial ");
		
		hql.append(" WHERE ");
		
		hql.append(" datediff(current_date(), ");
		
		hql.append(" CASE WHEN estudo.dataLancamento is not null THEN estudo.dataLancamento ELSE estudo.dataCadastro END ");
		
		hql.append(" )<365 ");
		
		hql.append(this.getSqlWhereBuscarEstudos(filtro));
		
		hql.append(" group by estudo.id ");
		
		hql.append(this.ordenarConsultaAnaliseEstudo(filtro));
		
		Query query = super.getSession().createQuery(hql.toString());
		
		this.paramsDinamicos(query, filtro);

		query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseEstudoDTO.class));
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		if(paginacao != null) {
			configurarPaginacao(paginacao, query);
		}
		
		return query.list();
		
	}

	private String getSqlWhereBuscarEstudos(FiltroAnaliseEstudoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" AND estudo.lancamentoID = lancamento.id");
		
		if(filtro.getNumEstudo() !=null && filtro.getNumEstudo() > 0){
			hql.append(" AND estudo.id = :NUM_ESTUDO ");
		}
		if(filtro.getCodigoProduto() != null && !filtro.getCodigoProduto().isEmpty()){
			hql.append(" AND produto.codigoICD = :COD_PRODUTO ");
			
		}
		if(filtro.getNumeroEdicao() !=null && filtro.getNumeroEdicao() > 0){
			hql.append(" AND prodEdicao.numeroEdicao = :NUM_EDICAO_PRODUTO ");
		}
		
		if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
			hql.append(" AND tpClassifProduto.id = :ID_TIPO_CLASS_PRODUTO ");
		}
		if(filtro.getDataLancamento() != null){
			hql.append(" AND lancamento.dataLancamentoDistribuidor = :DATA_LANCAMENTO ");
		}
		
		return hql.toString();
	}
	
	private String ordenarConsultaAnaliseEstudo(FiltroAnaliseEstudoDTO filtro) {

		StringBuilder hql = new StringBuilder();

		if (filtro.getOrdemColuna() != null) {

		    switch (filtro.getOrdemColuna()) {

		    case ESTUDO:
			hql.append(" ORDER BY numeroEstudo ");
			break;

		    case EDICAO:
			hql.append(" ORDER BY numeroEdicaoProduto ");
			break;

		    case CODIGO:
			hql.append(" ORDER BY codigoProduto ");
			break;

		    case PRODUTO:
			hql.append(" ORDER BY nomeProduto ");
			break;

		    case PERIODO:
			hql.append(" ORDER BY periodoProduto ");
			break;

		    case CLASSIFICACAO:
			hql.append(" ORDER BY descicaoTpClassifProd ");
			break;

		    case STATUS:
			hql.append(" ORDER BY statusLiberadoOuGerado ");
			break;
			
		    case DT_NASCIMENTO:
			hql.append("ORDER BY dataLancamento ");
			break;

		    default:
			hql.append(" ORDER BY numeroEstudo desc ");
		    }

		    if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
		    	hql.append(filtro.getPaginacao().getOrdenacao().toString());
		    }

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
		if(filtro.getNumeroEdicao() !=null && filtro.getNumeroEdicao() > 0){
			query.setParameter("NUM_EDICAO_PRODUTO", filtro.getNumeroEdicao());
		}
		
		if(filtro.getIdTipoClassificacaoProduto() !=null && filtro.getIdTipoClassificacaoProduto() > 0){
			query.setParameter("ID_TIPO_CLASS_PRODUTO", filtro.getIdTipoClassificacaoProduto());
		}
		if(filtro.getDataLancamento() !=null){
			query.setParameter("DATA_LANCAMENTO", filtro.getDataLancamento());
		}
	}
	
	private void configurarPaginacao(PaginacaoVO paginacao, Query query) {

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
