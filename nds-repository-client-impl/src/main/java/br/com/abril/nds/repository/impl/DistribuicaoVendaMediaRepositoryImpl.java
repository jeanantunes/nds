package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoEdicaoVendaMediaDTO;
import br.com.abril.nds.dto.filtro.FiltroEdicaoBaseDistribuicaoVendaMedia;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DistribuicaoVendaMediaRepository;

@Repository
public class DistribuicaoVendaMediaRepositoryImpl extends AbstractRepositoryModel<ProdutoEdicao, Long> implements DistribuicaoVendaMediaRepository {

    public DistribuicaoVendaMediaRepositoryImpl() {
	super(ProdutoEdicao.class);
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao) {
    	
    	return pesquisar(new FiltroEdicaoBaseDistribuicaoVendaMedia(codigoProduto, nomeProduto, edicao, idClassificacao, null));
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(String codigoProduto, String nomeProduto, Long edicao, Long idClassificacao, boolean usarICD) {
        return pesquisar(new FiltroEdicaoBaseDistribuicaoVendaMedia(codigoProduto, nomeProduto, edicao, idClassificacao, null), usarICD);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean usarICD) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT straight_join ");
        sql.append("     pe.id, ");
        sql.append("     pe.numero_edicao numeroEdicao, "); 
        sql.append("     p.id idProduto, ");
        sql.append("     p.codigo codigoProduto, ");
        sql.append("     p.nome nome, ");
        sql.append("     l.id idLancamento, ");
        
        if (!filtro.isConsolidado()) {
            
            sql.append("     plp.numero_periodo periodo, ");
            
            sql.append("     CASE ");
            sql.append("         WHEN plp.id IS NULL THEN 0 ");
            sql.append("         ELSE 1 ");
            sql.append("     END parcial, ");
        }
        
        sql.append("     l.data_lcto_distribuidor dataLancamento, ");
        sql.append("     l.status status, ");
        sql.append("     tcp.id idClassificacao, ");
        sql.append("     coalesce(tcp.descricao, '') classificacao, ");
    
        sql.append(" cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) else if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) end ) as unsigned int) AS reparte, ");
        
        sql.append("     case when l.STATUS IN (:statusLancFechadoRecolhido) then ");
        
        sql.append("     cast(sum( case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) else if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) end ) - ( ");
        sql.append("        select sum(mecEncalhe.qtde) ");
        sql.append("        from lancamento lanc ");
        sql.append("        LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID ");
        sql.append("        LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID ");
        sql.append("        LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
        sql.append("        LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
        sql.append("        LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
        sql.append("        WHERE lanc.id = l.id ");
        sql.append("     ) as unsigned int) ");
        
        sql.append(" else null end as venda ");
		
        sql.append(" FROM lancamento l ");
        sql.append("     JOIN produto_edicao pe ON pe.id = l.produto_edicao_id ");
        sql.append("     LEFT JOIN periodo_lancamento_parcial plp ON plp.id = l.periodo_lancamento_parcial_id ");
        
        sql.append("     JOIN (select p.* from produto p  ");
        sql.append("           	where p.codigo_icd = (select codigo_icd from produto p where p.codigo = :codigo_produto)  ");
        sql.append("            union  ");
        sql.append("           select p.* from produto p  ");
        sql.append("           	where p.codigo_icd = :codigo_produto ");
        sql.append("          ) p ON p.id = pe.produto_id   ");
        
        
        
        sql.append("     LEFT JOIN tipo_classificacao_produto tcp ON tcp.id = pe.tipo_classificacao_produto_id ");
        sql.append("     LEFT JOIN movimento_estoque_cota mecReparte on mecReparte.LANCAMENTO_ID = l.id ");
        sql.append("     LEFT JOIN tipo_movimento tipo ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");

        sql.append(" where l.status in (:statusLancamento) ");
//        sql.append(" and l.TIPO_LANCAMENTO = :tipoLancamento "); PSAN-139
        
        sql.append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
		
		if (filtro.getEdicao() != null) {
		    sql.append("   and pe.numero_edicao = :numero_edicao ");
		}
		
		/*
		if (filtro.getCodigo() != null) {
            if (usarICD) {
                sql.append("   and ((p.codigo_icd = :codigo_produto) ");
                sql.append("   or p.codigo in (select p.codigo from produto p where p.codigo_icd = (select codigo_icd from produto p where p.codigo = :codigo_produto))) ");
            } else {
                sql.append("   and p.codigo = :codigo_produto ");
            }
        }
		*/
		
        if (filtro.getClassificacao() != null && filtro.getClassificacao() > 0) {
			sql.append("   and tcp.id = :classificacao ");
		}
		
		sql.append(" group by pe.numero_edicao, pe.id");
		
		if (!filtro.isConsolidado()) {
		    
		    sql.append(" , plp.numero_periodo ");
		}
		
		sql.append(ordenarConsulta(filtro));
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		if (filtro.getEdicao()  != null) {
		    query.setLong("numero_edicao", filtro.getEdicao());
		}
		if (filtro.getCodigo() != null) {			
			query.setString("codigo_produto", filtro.getCodigo());
			
		}
		if (filtro.getClassificacao() != null && filtro.getClassificacao() > 0) {
		    query.setLong("classificacao", filtro.getClassificacao());
		}
		
		List<String> statusLancamento = new ArrayList<>();
        statusLancamento.add(StatusLancamento.EXPEDIDO.name());
        statusLancamento.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.EM_RECOLHIMENTO.name());
        statusLancamento.add(StatusLancamento.RECOLHIDO.name());
        statusLancamento.add(StatusLancamento.FECHADO.name());
        
        query.setParameterList("statusLancamento", statusLancamento);
        
        query.setParameterList(
                "statusLancFechadoRecolhido", 
                Arrays.asList(StatusLancamento.FECHADO.name(), StatusLancamento.RECOLHIDO.name(), StatusLancamento.EM_RECOLHIMENTO.name()));
        
//        query.setParameter("tipoLancamento", TipoLancamento.LANCAMENTO.name());
		
		query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
		return query.list();
    }

    @Override
    public List<ProdutoEdicaoVendaMediaDTO> pesquisar(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {
        return pesquisar(filtro, true); //por default usar o codigo ICD
    }
    
    @SuppressWarnings("unchecked")
   	@Override
   	public List<ProdutoEdicaoVendaMediaDTO> pesquisarEdicoesBasesParaLancamentoParcial(FiltroEdicaoBaseDistribuicaoVendaMedia filtro, boolean isFindByICD) {

    	StringBuilder sql = new StringBuilder();

    	sql.append(" Select                                                               ");
    	sql.append(" T.id, T.lancId as idLancamento, T.numeroEdicao, T.idProduto, T.codigoProduto, T.nome, ");
    	sql.append(" MAX(T.periodo) as periodo,										      ");
    	sql.append(" T.parcial,                                                           ");
    	sql.append(" MAX(T.dataLancamento) as dataLancamento,                             ");
    	sql.append(" T.STATUS as status,                                                  ");
    	sql.append(" T.idClassificacao,                                                   ");
    	sql.append(" T.classificacao,                                                     ");
//    	sql.append(" if(T.periodo>1, true, false) as consolidado,                         ");
    	sql.append(" true as isParcialConsolidado,                  				       ");
    	sql.append(" SUM(T.REPARTE) as reparte,                                           ");
    	sql.append(" SUM(T.venda) as venda                                                ");
		
    	sql.append(" FROM (SELECT straight_join  ");
		sql.append("     pe.id, ");
		sql.append("     l.id lancId, ");
		sql.append("     pe.numero_edicao numeroEdicao, ");
		sql.append("     p.id idProduto, ");
		sql.append("     p.codigo codigoProduto, ");
		sql.append("     p.nome nome, ");
		sql.append("     plp.numero_periodo periodo, ");
		sql.append("     CASE ");
		sql.append("         WHEN plp.id IS NULL THEN 0 ");
		sql.append("         ELSE 1 ");
		sql.append("     END parcial, ");
		sql.append("     l.data_lcto_distribuidor dataLancamento, ");
		sql.append("     l.status status, ");
		sql.append("     tcp.id idClassificacao, ");
		sql.append("     coalesce(tcp.descricao, '') classificacao, ");
		
	    sql.append("     cast(sum(if(tipo.OPERACAO_ESTOQUE = 'ENTRADA', mecReparte.QTDE, 0)) as unsigned int) AS reparte, ");
		
		sql.append("     case when l.STATUS IN (:statusLancFechadoRecolhido) then ");
		
		sql.append("     cast(sum(if(tipo.OPERACAO_ESTOQUE = 'ENTRADA', mecReparte.QTDE, 0)) - COALESCE(( ");
		sql.append("        select sum(mecEncalhe.qtde) ");
		sql.append("        from lancamento lanc ");
		sql.append("        LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID ");
		sql.append("        LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID ");
		sql.append("        LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("        LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
		sql.append("        straight_join movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("        WHERE lanc.id = l.id ");
		sql.append("     ),0) as unsigned int) ");
		
		sql.append(" else CAST(sum(if(tipo.OPERACAO_ESTOQUE = 'ENTRADA',mecReparte.QTDE,0)) AS UNSIGNED INT) ");
		sql.append(" end as venda ");
		
		sql.append(" FROM lancamento l ");
		sql.append("     JOIN produto_edicao pe ON pe.id = l.produto_edicao_id ");
		sql.append("     LEFT JOIN periodo_lancamento_parcial plp ON plp.id = l.periodo_lancamento_parcial_id ");
		sql.append("     JOIN produto p ON p.id = pe.produto_id ");
		sql.append("     LEFT JOIN tipo_classificacao_produto tcp ON tcp.id = pe.tipo_classificacao_produto_id ");
		sql.append("     LEFT JOIN movimento_estoque_cota mecReparte on mecReparte.LANCAMENTO_ID = l.id ");
		sql.append("     LEFT JOIN tipo_movimento tipo ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
		
		sql.append(" where l.status in (:statusLancamento) ");
		sql.append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
        sql.append(" and mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
   		
   		if (filtro.getEdicao() != null) {
   		    sql.append("   and pe.numero_edicao = :numero_edicao ");
   		}
           
   		if (filtro.getCodigo() != null) {
   			if (isFindByICD) {
   				sql.append(" and ((p.codigo_icd = :codigo_produto) "); 
		        sql.append("      or p.codigo in (select p.codigo from produto p where p.codigo_icd = (select codigo_icd from produto p where p.codigo = :codigo_produto))) ");
   				
   			} else {
   				sql.append("   and p.codigo = :codigo_produto ");
   			}
   		}
       
   		if ((filtro.getClassificacao() != null) && (filtro.getClassificacao() > 0)) {
   			sql.append("   and tcp.id = :classificacao ");
		}
   		
   		sql.append(" group by pe.id, plp.numero_periodo ");
   		sql.append(" ) T GROUP BY T.id ");
   		sql.append(ordenarConsulta(filtro));
   		
   		SQLQuery query = getSession().createSQLQuery(sql.toString());
   		
   		if (filtro.getEdicao()  != null) {
   		    query.setLong("numero_edicao", filtro.getEdicao());
   		}
   		
   		
   		if (filtro.getCodigo() != null) {			
   			query.setString("codigo_produto", filtro.getCodigo());
   			
   		}
   		
   		if ((filtro.getClassificacao() != null) && (filtro.getClassificacao() > 0)) {
   		    query.setLong("classificacao", filtro.getClassificacao());
   		}
   		
   		List<String> statusLancamento = new ArrayList<>();
           statusLancamento.add(StatusLancamento.EXPEDIDO.name());
           statusLancamento.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name());
           statusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO.name());
           statusLancamento.add(StatusLancamento.EM_RECOLHIMENTO.name());
           statusLancamento.add(StatusLancamento.RECOLHIDO.name());
           statusLancamento.add(StatusLancamento.FECHADO.name());
	        
		query.setParameterList("statusLancamento", statusLancamento);
		
		query.setParameterList("statusLancFechadoRecolhido", Arrays.asList(StatusLancamento.FECHADO.name(), StatusLancamento.RECOLHIDO.name(), StatusLancamento.EM_RECOLHIMENTO.name()));
		
		query.setResultTransformer(Transformers.aliasToBean(ProdutoEdicaoVendaMediaDTO.class));
		
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("id", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("numeroEdicao", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("idProduto", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nome", StandardBasicTypes.STRING);
		query.addScalar("periodo", StandardBasicTypes.INTEGER);
		query.addScalar("parcial", StandardBasicTypes.INTEGER);
		query.addScalar("isParcialConsolidado", StandardBasicTypes.BOOLEAN);
		query.addScalar("dataLancamento", StandardBasicTypes.DATE);
		query.addScalar("idLancamento", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("status", StandardBasicTypes.STRING);
		query.addScalar("idClassificacao", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("classificacao", StandardBasicTypes.STRING);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);

	return query.list();
   }
    
    
    @Override
	public Date obterDataLancamentoValidaParaParcialConsolidada(BigInteger idProduto, BigInteger numeroEdicao) {
    	
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select l.DATA_LCTO_DISTRIBUIDOR   ");
		sql.append(" from lancamento l   ");
		sql.append(" where PRODUTO_EDICAO_ID = (select pe.id from produto_edicao pe where pe.NUMERO_EDICAO = :numeroEdicao and pe.PRODUTO_ID = :idProd)  ");
		sql.append(" and l.DATA_LCTO_DISTRIBUIDOR <> '3000/01/01'  ");
		sql.append(" order by l.DATA_LCTO_DISTRIBUIDOR desc limit 1  ");
		
    	SQLQuery query = getSession().createSQLQuery(sql.toString());
    	
    	query.setParameter("idProd", idProduto);
    	query.setParameter("numeroEdicao", numeroEdicao);
    	
    	return (Date) query.uniqueResult();
	}
    

    private String ordenarConsulta(FiltroEdicaoBaseDistribuicaoVendaMedia filtro) {

		StringBuilder hql = new StringBuilder();

        if (filtro.getOrdemColuna() != null) {

            switch (filtro.getOrdemColuna()) {

                case CODIGO:
                    hql.append(" ORDER BY codigoProduto ");
                    break;

                case EDICAO:
                    hql.append(" ORDER BY numeroEdicao ");
                    break;

                case DATA_LANCAMENTO:
                    hql.append(" ORDER BY dataLancamento ");
                    break;

                case REPARTE:
                    hql.append(" ORDER BY reparte ");
                    break;

                case PERIODO:
                    hql.append(" ORDER BY periodo ");
                    break;

                case CLASSIFICACAO:
                    hql.append(" ORDER BY classificacao ");
                    break;

                case STATUS:
                    hql.append(" ORDER BY status ");
                    break;

                case VENDA:
                    hql.append("ORDER BY venda ");
                    break;

                default:
                    hql.append(" ORDER BY dataLancamento desc ");
            }

            if (filtro.getPaginacao().getOrdenacao() != null) {
                hql.append(filtro.getPaginacao().getOrdenacao().toString());
            }

        } else {
            hql.append(" ORDER BY dataLancamento desc ");
        }

		return hql.toString();
	}
    
}
