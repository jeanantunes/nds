package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.VendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaProdutoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.VendaProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class VendaProdutoRepositoryImpl extends AbstractRepositoryModel<MovimentoEstoque, Long> implements VendaProdutoRepository {

	public VendaProdutoRepositoryImpl() {
		super(MovimentoEstoque.class);
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<VendaProdutoDTO> buscarVendaPorProduto(FiltroVendaProdutoDTO filtro) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		sql.append("             T.numEdicao as numEdicao, ");
		sql.append("             T.statusLancamento as statusLancamento, ");
		sql.append("             T.idProdutoEdicao AS idProdutoEdicao, ");
		sql.append("             T.precoCapa AS precoCapa, ");
		sql.append("             T.chamadaCapa AS chamadaCapa, ");
		sql.append("             T.parcial AS parcial, "); 

		if(filtro.getEdicao() !=null){
			sql.append("         T.periodo AS periodoFormatado, "); 
			sql.append("         T.numEstudo AS numeroEstudo, ");
			sql.append("         T.dataLancamento as dataLancamento, ");
			sql.append("         T.dataRecolhimento as dataRecolhimento, ");
		}else{
			sql.append("         MAX(T.periodo) AS periodoFormatado, "); 
			sql.append("         MAX(T.numEstudo) AS numeroEstudo, ");
			sql.append("         MAX(T.dataLancamento) as dataLancamento, ");
			sql.append("         MAX(T.dataRecolhimento) as dataRecolhimento, ");
		}
		
		sql.append("             T.codigoProduto AS codigoProduto, ");
		
		sql.append("             SUM(T.repartePrevisto) as reparte, ");
		sql.append("             SUM(T.qtdeVendas) as venda, ");

		sql.append("             ((SUM(T.qtdeVendas)*100)/SUM(T.repartePrevisto)) as percentualVenda, ");
		sql.append("             (SUM(T.qtdeVendas)*T.precoCapa) as total ");

		sql.append(" FROM  (SELECT ");
		sql.append("             pe.NUMERO_EDICAO as numEdicao, ");
		sql.append("             lancamento.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		sql.append("             lancamento.DATA_REC_DISTRIB as dataRecolhimento, ");
		sql.append("             lancamento.STATUS as statusLancamento, ");
		sql.append("             pe.id AS idProdutoEdicao, ");
		sql.append("             pe.PRECO_VENDA AS precoCapa, ");
		sql.append("             pe.CHAMADA_CAPA AS chamadaCapa, ");
		sql.append("             pe.PARCIAL AS parcial, ");
		sql.append("         	 est.id AS numEstudo, ");
		sql.append("         	 plp.NUMERO_PERIODO AS periodo, ");
		sql.append("             produto.CODIGO AS codigoProduto, ");

		sql.append("            round(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else -mecReparte.QTDE end), 0) AS repartePrevisto,    ");
		sql.append(" 			      ");
		sql.append("            coalesce(round(case  ");
		sql.append(" 			         when lancamento.STATUS IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO')                                              ");
		sql.append(" 			           then sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else -mecReparte.QTDE end) - (       ");
		sql.append(" 			                     select sum(mecEncalhe.qtde) ");
		sql.append(" 			                     from ");
		sql.append(" 			                         lancamento lanc                                                                                    ");
		sql.append(" 			                     LEFT JOIN                                                                                              ");
		sql.append(" 			                         chamada_encalhe_lancamento cel                                                                     ");
		sql.append(" 			                             on cel.LANCAMENTO_ID = lanc.ID                                                                 ");
		sql.append(" 			                     LEFT JOIN                                                                                              ");
		sql.append(" 			                         chamada_encalhe ce                                                                                 ");
		sql.append(" 			                             on ce.id = cel.CHAMADA_ENCALHE_ID                                                              ");
		sql.append(" 			                     LEFT JOIN                                                                                              ");
		sql.append(" 			                         chamada_encalhe_cota cec                                                                           ");
		sql.append(" 			                             on cec.CHAMADA_ENCALHE_ID = ce.ID                                                              ");
		sql.append(" 			                     LEFT JOIN                                                                                              ");
		sql.append(" 			                         conferencia_encalhe confEnc                                                                        ");
		sql.append(" 			                             on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                                                    ");
		sql.append(" 			                     LEFT JOIN                                                                                              ");
		sql.append(" 			                         movimento_estoque_cota mecEncalhe                                                                  ");
		sql.append(" 			                             on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID                                           ");
		sql.append(" 			                     WHERE                                                                                                  ");
		sql.append(" 			                         lanc.id = lancamento.id)                                                                           ");
		sql.append(" 			         else null                                                                                                          ");
		sql.append(" 			     end, 0), 0) as qtdeVendas                                                                                                      ");
		sql.append(" 	                                                                                                                                    ");
		sql.append(" 		     FROM lancamento lancamento                                                                                                 ");

		sql.append(" 		     JOIN                                                                                                                       ");
		sql.append(" 		         produto_edicao pe                                                                                           ");
		sql.append(" 		           ON pe.id = lancamento.produto_edicao_id                                                                   ");
		sql.append(" 		     LEFT JOIN                                                                                                                  ");
		sql.append(" 		         periodo_lancamento_parcial plp                                                                                         ");
		sql.append(" 		           ON plp.id = lancamento.periodo_lancamento_parcial_id                                                                 ");
		sql.append(" 		     JOIN                                                                                                                       ");
		sql.append(" 		         produto produto                                                                                                        ");
		sql.append(" 		           ON produto.id = pe.produto_id                                                                             ");
		sql.append(" 		     JOIN produto_fornecedor pf      ");
		sql.append(" 		         ON pf.PRODUTO_ID = produto.ID     ");
		sql.append(" 		     JOIN fornecedor  forn");
		sql.append(" 		         ON pf.fornecedores_ID = forn.ID   ");
		sql.append(" 		     LEFT JOIN                                                                                                                  ");
		sql.append(" 		         tipo_classificacao_produto tipoClassificacaoProduto                                                                    ");
		sql.append(" 		           ON tipoClassificacaoProduto.id = pe.tipo_classificacao_produto_id                                         ");
		sql.append(" 		     JOIN                                                                                                                  ");
		sql.append(" 		         movimento_estoque_cota mecReparte                                                                                      ");
		sql.append(" 		           on mecReparte.LANCAMENTO_ID = lancamento.id                                                                          ");
		sql.append(" 		     LEFT JOIN                                                                                                                  ");
		sql.append(" 		         tipo_movimento tipo                                                                                                    ");
		sql.append(" 		           ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID       ");
		sql.append(" 		     LEFT JOIN                                                                                                                  ");
		sql.append(" 		         estudo est ");
		sql.append(" 		           ON lancamento.ESTUDO_ID = est.ID  ");

		sql.append("                where tipo.GRUPO_MOVIMENTO_ESTOQUE not in ('ENVIO_ENCALHE') ");
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()) { 
			sql.append(" AND produto.CODIGO = :codigo ");
		}
		
		if(filtro.getEdicao() !=null){
			sql.append(" AND pe.NUMERO_EDICAO = :edicao ");
		}
		
		if(filtro.getIdFornecedor() !=null && filtro.getIdFornecedor() != -1){
			sql.append(" AND forn.ID = :idFornecedor ");
		}
		
		if(filtro.getIdClassificacaoProduto() !=null && filtro.getIdClassificacaoProduto() != -1){
			sql.append(" AND pe.TIPO_CLASSIFICACAO_PRODUTO_ID = :idClassificacao ");
		}
		
		sql.append("    GROUP BY pe.numero_Edicao, produto.codigo, plp.NUMERO_PERIODO) T ");
		
		if(filtro.getEdicao() !=null){
			sql.append(" group by T.periodo ");
		}else{
			sql.append(" group by T.numEdicao ");
		}
		
		sql.append(this.getOrderByVenda(filtro));
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.addScalar("numEdicao", StandardBasicTypes.LONG);
		query.addScalar("dataLancamento");
		query.addScalar("dataRecolhimento");
		query.addScalar("statusLancamento", StandardBasicTypes.STRING);
		query.addScalar("idProdutoEdicao", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("percentualVenda");
		query.addScalar("precoCapa");
		query.addScalar("chamadaCapa");
		query.addScalar("total");
		query.addScalar("parcial", StandardBasicTypes.BOOLEAN);
		query.addScalar("periodoFormatado", StandardBasicTypes.INTEGER);
		query.addScalar("numeroEstudo", StandardBasicTypes.LONG);
		query.addScalar("codigoProduto");
		
		HashMap<String, Object> param = this.buscarParametrosVendaProduto(filtro);
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(VendaProdutoDTO.class));
		
		this.configurarPaginacao(filtro, query);
				
		return query.list();
	}
	
	private void configurarPaginacao(FiltroVendaProdutoDTO filtro, Query query) {
		
		PaginacaoVO paginacao = filtro.getPaginacao();
		
		if(paginacao==null){
			return;
		}
		
		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}
		
		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		
		if (paginacao.getPosicaoInicial() != null) {
			if(paginacao.getPosicaoInicial() > paginacao.getQtdResultadosTotal()){
				query.setFirstResult(1);
				paginacao.setPaginaAtual(1);
			}else{
				query.setFirstResult(paginacao.getPosicaoInicial());
			}
		}
	}
	
	private String getOrderByVenda(FiltroVendaProdutoDTO filtro) {
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" ORDER BY " + coluna);
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(" " + filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	private HashMap<String,Object> buscarParametrosVendaProduto(FiltroVendaProdutoDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().isEmpty()){ 
			param.put("codigo", filtro.getCodigo());
		}
		
		if(filtro.getEdicao() != null){ 
			param.put("edicao", filtro.getEdicao());
		}
		
		if(filtro.getIdFornecedor() != null && filtro.getIdFornecedor() != -1){ 
			param.put("idFornecedor", filtro.getIdFornecedor());
		}
		
		if(filtro.getIdClassificacaoProduto() !=null && filtro.getIdClassificacaoProduto() != -1){
			param.put("idClassificacao", filtro.getIdClassificacaoProduto());
		}
		
		return param;
	}

}
