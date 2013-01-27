package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FechamentoCEIntegracaoConsolidadoDTO;
import br.com.abril.nds.dto.FechamentoCEIntegracaoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.FechamentoEncalhe;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.pk.FechamentoEncalhePK;
import br.com.abril.nds.model.movimentacao.StatusOperacao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.model.planejamento.fornecedor.StatusCeNDS;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.repository.FechamentoCEIntegracaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class FechamentoCEIntegracaoRepositoryImpl extends AbstractRepositoryModel<FechamentoEncalhe, FechamentoEncalhePK> implements
		FechamentoCEIntegracaoRepository {
	
	@Autowired
	private ChamadaEncalheFornecedorRepository chamadaEncalheFornecedorRepository;
	
	public FechamentoCEIntegracaoRepositoryImpl() {
		super(FechamentoEncalhe.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<FechamentoCEIntegracaoDTO> buscarConferenciaEncalhe(FiltroFechamentoCEIntegracaoDTO filtro) {
		
		String sql = this.getConsultaListaContagemDevolucao(filtro, false);
		Query query = this.criarQueryComParametrosObterListaContagemDevolucao(sql, filtro, false);
		
		if(filtro.getPaginacao()!=null) {
			
			if(filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
			
		}
				
		return query.list();
	}

	@Override
	public FechamentoCEIntegracaoConsolidadoDTO buscarConferenciaEncalheTotal(FiltroFechamentoCEIntegracaoDTO filtro) {
		String sql = this.getConsultaListaContagemDevolucao(filtro, true);
		Query query = this.criarQueryComParametrosObterListaContagemDevolucao(sql, filtro, true);
		return (FechamentoCEIntegracaoConsolidadoDTO) query.uniqueResult();
	}
	
	/**
	 * Obtém hql para pesquisa de ContagemDevolucao.
	 * 
	 * @param filtro
	 * @param indBuscaTotalParcial
	 * @param indBuscaQtd
	 * 
	 * @return String - hql
	 */
	private String getConsultaListaContagemDevolucao(FiltroFechamentoCEIntegracaoDTO filtro, boolean indBuscaQtd) {
		
		StringBuffer sql = new StringBuffer("");
		
		sql.append(" SELECT "); 

		if(indBuscaQtd) {

			sql.append("SUM(PROD_EDICAO.PRECO_VENDA*VENDA_PRODUTO.QNT_PRODUTO) AS totalBruto,");
			sql.append("SUM((PROD_EDICAO.PRECO_VENDA*VENDA_PRODUTO.QNT_PRODUTO)-VENDA_PRODUTO.VALOR_TOTAL_VENDA) AS totalDesconto,");
			sql.append("SUM(VENDA_PRODUTO.VALOR_TOTAL_VENDA) AS totalLiquido");
			
		} else {

			sql.append(" PROD_EDICAO.ID AS idProdutoEdicao, ");
			sql.append(" ITEM_CH_ENC_FORNECEDOR.NUEMRO_ITEM AS sequencial, ");
			sql.append(" PROD.CODIGO as codigoProduto,  				");		
			sql.append(" PROD.NOME as nomeProduto, 						");
			sql.append(" PROD_EDICAO.NUMERO_EDICAO as numeroEdicao, 	");
			sql.append(" ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA as reparte,");
			sql.append(" VENDA_PRODUTO.QNT_PRODUTO  as venda, 			");
			sql.append(" PROD_EDICAO.PRECO_VENDA as precoCapa,  		");
			sql.append(" VENDA_PRODUTO.VALOR_TOTAL_VENDA  as valorVenda,");

			sql.append(" (CASE WHEN ").append( this.getSomaQtdeParcial() ).append(" IS NULL THEN true ELSE false END) AS tipo, ");
			
			sql.append(" COALESCE( ").append(" (" ).append(this.getSomaQtdeParcial()).append("), 0) AS encalhe ");
		}

		sql.append(" FROM ");

		sql.append(" ( ");

		sql.append(" SELECT ");
	
		sql.append(" SUM(MOV_ESTOQUE_COTA.QTDE) AS QTDE_ENCALHE, ");
	
		sql.append(" MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID AS PRODUTO_EDICAO_ID ");
	
		sql.append(" FROM ");

		sql.append(" MOVIMENTO_ESTOQUE_COTA MOV_ESTOQUE_COTA ");

		sql.append(" INNER JOIN CONFERENCIA_ENCALHE ON (	");
		sql.append(" CONFERENCIA_ENCALHE.movimento_estoque_cota_id = MOV_ESTOQUE_COTA.ID ");
		sql.append(" )	"); 
		
		sql.append(" LEFT JOIN CONTROLE_CONTAGEM_DEVOLUCAO ON ( ");
		sql.append(" CONTROLE_CONTAGEM_DEVOLUCAO.PRODUTO_EDICAO_ID 	=  MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID AND ");
	    sql.append(" CONTROLE_CONTAGEM_DEVOLUCAO.DATA     			=  MOV_ESTOQUE_COTA.DATA AND ");
	    sql.append(" CONTROLE_CONTAGEM_DEVOLUCAO.STATUS = :statusOperacao ) ");
	
		sql.append(" WHERE ");

		sql.append(" MOV_ESTOQUE_COTA.DATA BETWEEN :dataInicial AND :dataFinal AND	");
	
		sql.append(" CONTROLE_CONTAGEM_DEVOLUCAO.ID IS NULL  ");
		
		sql.append(" GROUP BY MOV_ESTOQUE_COTA.PRODUTO_EDICAO_ID	");

		sql.append(" ) AS CONFERENCIAS 	");

		sql.append(" RIGHT OUTER JOIN ITEM_CHAMADA_ENCALHE_FORNECEDOR ITEM_CH_ENC_FORNECEDOR ON ( ");
		sql.append(" 	CONFERENCIAS.produto_edicao_id = ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID ");
		sql.append(" ) ");
		
		sql.append(" INNER JOIN PRODUTO_EDICAO PROD_EDICAO ON ( 					");
		sql.append(" 	ITEM_CH_ENC_FORNECEDOR.PRODUTO_EDICAO_ID = PROD_EDICAO.ID 	");
		sql.append(" ) 	");
		
		sql.append(" INNER JOIN PRODUTO PROD ON (			");
		sql.append(" 	PROD_EDICAO.PRODUTO_ID = PROD.ID	");
		sql.append(" ) 	");
		
		sql.append(" INNER JOIN PRODUTO_FORNECEDOR PROD_FORNEC ON (	");
		sql.append(" 	PROD.ID = PROD_FORNEC.PRODUTO_ID 			");
		sql.append(" ) ");

		sql.append(" INNER JOIN VENDA_PRODUTO VENDA_PRODUTO ON (			");
		sql.append(" 	VENDA_PRODUTO.ID_PRODUTO_EDICAO = PROD_EDICAO.ID 	");
		sql.append(" ) ");
		
		sql.append(" WHERE ");

		sql.append(" ITEM_CH_ENC_FORNECEDOR.DATA_RECOLHIMENTO BETWEEN :dataInicial AND :dataFinal ");

		if( filtro.getIdFornecedor() != -1L ) {
			sql.append(" AND PROD_FORNEC.FORNECEDORES_ID = :idFornecedor ");		
		}

		sql.append(" AND VENDA_PRODUTO.TIPO_VENDA_ENCALHE = :tipoVendaEncalhe ");
		
		if(!indBuscaQtd){
    	
    		sql.append(" GROUP BY ");
    		sql.append(" PROD_EDICAO.ID,			");		
    		sql.append(" PROD.CODIGO,  				");		
    		sql.append(" PROD.NOME, 				");
    		sql.append(" PROD_EDICAO.NUMERO_EDICAO, ");
    		sql.append(" PROD_EDICAO.PRECO_VENDA, 	");
    		sql.append(" PROD_EDICAO.DESCONTO       ");
    		
        }
		
		PaginacaoVO paginacao = filtro.getPaginacao();

		if (!indBuscaQtd && filtro.getOrdenacaoColuna() != null) {

			sql.append(" order by ");
			
			String orderByColumn = "";
			
				switch (filtro.getOrdenacaoColuna()) {
				
					case SEQUENCIAL:
						orderByColumn = " ITEM_CH_ENC_FORNECEDOR.NUEMRO_ITEM ";
						break;
					case CODIGO_PRODUTO:
						orderByColumn = " PROD.CODIGO ";
						break;
					case NOME_PRODUTO:
						orderByColumn = " PROD.NOME  ";
						break;
					case ENCALHE:
						orderByColumn = " COALESCE((" + this.getSomaQtdeParcial() + "), 0) ";
						break;
					case PRECO_CAPA:
						orderByColumn = " PROD_EDICAO.PRECO_VENDA ";
						break;
					case NUMERO_EDICAO:
						orderByColumn = " PROD_EDICAO.NUMERO_EDICAO ";
						break;
					case REPARTE:
						orderByColumn = " ITEM_CH_ENC_FORNECEDOR.QTDE_ENVIADA ";
						break;
					case TIPO:
						orderByColumn = " (CASE WHEN " + this.getSomaQtdeParcial() + " IS NULL THEN true ELSE false END) ";
						break;
					case VALOR_VENDA:
						orderByColumn = " VENDA_PRODUTO.VALOR_TOTAL_VENDA ";
						break;
					case VENDA:
						orderByColumn = " VENDA_PRODUTO.QNT_PRODUTO ";
						break;
						
					default:
						break;
				}
			
			sql.append(orderByColumn);
			
			if (paginacao.getOrdenacao() != null) {
				
				sql.append(paginacao.getOrdenacao().toString());
				
			}
			
		}
		
		return sql.toString();
		
	}

	public String getSomaQtdeParcial() {
		String sqlSoma = " (SELECT SUM(PARCIAL.QTDE)"
		+ " FROM CONFERENCIA_ENC_PARCIAL PARCIAL "
		+ " WHERE PARCIAL.DATA_MOVIMENTO BETWEEN :dataInicial AND :dataFinal AND "  
		+ " PROD_EDICAO.ID = PARCIAL.PRODUTOEDICAO_ID AND  "
		+ " PARCIAL.STATUS_APROVACAO = :statusAprovacao) ";
		return sqlSoma; 
	}
	
	/**
	 * Carrega os parâmetros da pesquisa de ContagemDevolucao e retorna
	 * o objeto Query.
	 * 
	 * @param hql
	 * @param filtro
	 * @param tipoMovimentoEstoque
	 * @param indBuscaTotalParcial
	 * @param indBuscaQtd
	 * 
	 * @return Query
	 */
	private Query criarQueryComParametrosObterListaContagemDevolucao(String hql, FiltroFechamentoCEIntegracaoDTO filtro, boolean indBuscaQtd) {
		
		Query query = null;
		
		if(indBuscaQtd) {
		
			query = getSession().createSQLQuery(hql.toString())
								.addScalar("totalBruto", StandardBasicTypes.BIG_DECIMAL)
								.addScalar("totalDesconto", StandardBasicTypes.BIG_DECIMAL)
								.addScalar("totalLiquido", StandardBasicTypes.BIG_DECIMAL)
								.setResultTransformer(Transformers.aliasToBean(FechamentoCEIntegracaoConsolidadoDTO.class));
		
		} else {
			
			query = getSession().createSQLQuery(hql.toString())
					.addScalar("idProdutoEdicao", StandardBasicTypes.LONG)
					.addScalar("sequencial", StandardBasicTypes.LONG)
					.addScalar("codigoProduto", StandardBasicTypes.STRING)
					.addScalar("nomeProduto", StandardBasicTypes.STRING)
					.addScalar("numeroEdicao", StandardBasicTypes.LONG)
					.addScalar("reparte", StandardBasicTypes.BIG_INTEGER)
					.addScalar("tipo", StandardBasicTypes.BOOLEAN)
					.addScalar("venda", StandardBasicTypes.BIG_INTEGER)
					.addScalar("precoCapa", StandardBasicTypes.BIG_DECIMAL)
					.addScalar("valorVenda", StandardBasicTypes.BIG_DECIMAL)
					.addScalar("encalhe", StandardBasicTypes.BIG_INTEGER)
					.setResultTransformer(Transformers.aliasToBean(FechamentoCEIntegracaoDTO.class));
			
		}
		
		query.setParameter("dataInicial", filtro.getPeriodoRecolhimento().getDe());
		
		query.setParameter("dataFinal", filtro.getPeriodoRecolhimento().getAte());
		
		query.setParameter("statusOperacao", StatusOperacao.CONCLUIDO);
		
		query.setParameter("tipoVendaEncalhe", TipoVendaEncalhe.ENCALHE.name());
		
		if (!indBuscaQtd) {
			query.setParameter("statusAprovacao", StatusAprovacao.PENDENTE.name());
		}
		
		if(filtro.getIdFornecedor() != -1L) {
			query.setParameter("idFornecedor", filtro.getIdFornecedor());
		}
		
		return query;
		
	}
	
	@Override
	public void fecharCE(Long encalhe, ProdutoEdicao produtoEdicao, Long idFornecedor, Integer numeroSemana) {
		 FechamentoEncalhe fe = new FechamentoEncalhe();
		 fe.setQuantidade(encalhe);
		 FechamentoEncalhePK pk = new FechamentoEncalhePK();
		 pk.setProdutoEdicao(produtoEdicao);
		 pk.setDataEncalhe(new Date());
		 fe.setFechamentoEncalhePK(pk);
		 
		 for (ChamadaEncalheFornecedor chamadaEncalheFornecedor : chamadaEncalheFornecedorRepository.obterChamadasEncalheFornecedor(idFornecedor, numeroSemana, null)) {
			 chamadaEncalheFornecedor.setStatusCeNDS(StatusCeNDS.FECHADO);
			 this.getSession().save(chamadaEncalheFornecedor);
		 }
		 
		 this.getSession().save(fe);
		
	}

	@Override
	public boolean verificarStatusSemana(FiltroFechamentoCEIntegracaoDTO filtro) {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT cef FROM ChamadaEncalheFornecedor cef WHERE" +
				   " numeroSemana = :numeroSemana" +
				   " and cef.statusCeNDS = :statusCeNDS");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("numeroSemana", new Integer( filtro.getSemana().toString()) );
		query.setParameter("statusCeNDS", StatusCeNDS.FECHADO);
		
		return (query.uniqueResult() == null) ? false : true;
	}

}
