package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;
import br.com.abril.nds.dto.DataLancamentoPeriodoEdicoesBasesDTO;
import br.com.abril.nds.dto.DetalhesEdicoesBasesAnaliseEstudoDTO;
import br.com.abril.nds.dto.EdicoesProdutosDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.helper.LancamentoHelper;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AnaliseParcialRepository;
import br.com.abril.nds.util.Util;

@Repository
public class AnaliseParcialRepositoryImpl extends AbstractRepositoryModel<EstudoCotaGerado, Long> implements AnaliseParcialRepository {

    public AnaliseParcialRepositoryImpl() {
        super(EstudoCotaGerado.class);
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(AnaliseParcialQueryDTO queryDTO) {
    	
    	List<String> statusLancamento = Arrays.asList(StatusLancamento.EXPEDIDO.name(), 
    			StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO.name(),
    			StatusLancamento.EM_RECOLHIMENTO.name(),
    			StatusLancamento.RECOLHIDO.name(),
    			StatusLancamento.BALANCEADO_RECOLHIMENTO.name(),
    			StatusLancamento.FECHADO.name());
    	
    	StringBuilder sql = new StringBuilder();
    	
        sql.append("select distinct ");
        
        sql.append("	c.numero_cota cota, ");
        sql.append("	c.ID cotaId, ");
        sql.append("	case when pdv_qtd.quantidade > 1 then ( ");
        sql.append("	case when count(case when pdv_fx.ID is not null then 1 ");
        sql.append("	                when pdv_mx.ID is not null then 1 ");
        sql.append("	                when pdv_default.ID is not null then 1 ");
        sql.append("	                else 0 end) > 1 then 1 else 0 end) ");
        sql.append("	else 0 end as contemRepartePorPDV, ");
        
        sql.append("	fx.ID as fixacaoID, mx.ID as mixID, ");        
        
        sql.append("    c.classificacao_espectativa_faturamento classificacao, ");
        
        sql.append("    coalesce(pes.nome, pes.razao_social, pes.nome_fantasia, '') nome, ");
        
        sql.append("    pdv_qtd.quantidade npdv, ");
        
        sql.append("    ec.qtde_efetiva reparteEstudo, ");
        
        sql.append("    ec.venda_media mediaVenda, ");
        
        sql.append("    coalesce(ecg_origem.reparte,0) reparteEstudoOrigemCopia, ");
        
        sql.append("    coalesce(ec.reparte,0) reparteSugerido, ");
        
        sql.append("    ec.classificacao leg, ");

        sql.append("    (select count(cbc.ID) ");
        sql.append("     from cota_base_cota cbc ");
        sql.append("     join cota_base cb on cbC.COTA_BASE_ID = cb.ID ");
        sql.append("     where cb.COTA_ID = c.ID and cbc.DT_FIM_VIGENCIA > data_distrib.DATA_OPERACAO) > 0 cotaNova, ");
        
        sql.append("    (select coalesce(sum(mec.qtde), 0) ");
        sql.append("     from movimento_estoque_cota mec ");
        sql.append("     where mec.tipo_movimento_id = 32 ");
        sql.append("     and mec.cota_id = c.id ");
        sql.append("     and mec.produto_edicao_id = pe.id) juramento, ");

/*      ## Comentário necessário, pois pediram pra deixar a solução antiga em background
 * 		sql.append("    (select epc.qtde_recebida ");
		sql.append("     from lancamento l ");
		sql.append("     inner join produto_edicao _ped on l.produto_edicao_id = _ped.id ");
		sql.append("     inner join estoque_produto_cota epc on epc.produto_edicao_id = _ped.id ");
		sql.append("     inner join cota _c on _c.id = epc.cota_id ");
		sql.append("     inner join produto _p on _p.id = _ped.produto_id ");
		sql.append("     where _p.codigo = p.codigo ");
		sql.append("     and (epc.cota_id = c.id) ");
		sql.append("     and l.data_lcto_distribuidor = (select max(_ul.data_lcto_distribuidor) ");
		sql.append("                                     from lancamento _ul ");
		sql.append("                                     where _ul.produto_edicao_id = _ped.id and _ul.status in (:statusLancamento)) ");
		sql.append("                                     order by l.data_lcto_distribuidor desc limit 1) ultimoReparte, ");*/
        
        sql.append(" (select epc.QTDE_RECEBIDA ");
        sql.append(" from estoque_produto_cota epc ");
        sql.append(" where (epc.cota_id = c.id) "); 
        sql.append(" 		AND epc.PRODUTO_EDICAO_ID = (select lct.PRODUTO_EDICAO_ID from lancamento lct ");
        sql.append("                                   join produto_edicao pe ON lct.PRODUTO_EDICAO_ID = pe.ID ");
        sql.append("                                   join produto pd ON pe.PRODUTO_ID = pd.ID ");
        sql.append("                                where pd.codigo = p.codigo ");
        sql.append("                                and lct.STATUS in (:statusLancamento)");
        sql.append("                                order by lct.DATA_LCTO_DISTRIBUIDOR desc limit 1)) ultimoReparte,");
        
        sql.append("     (coalesce(ec.reparte_inicial,0) <> coalesce(ec.reparte,0)) ajustado, ");
        
        sql.append("     (coalesce(ec.reparte_inicial,0) - coalesce(ec.reparte,0)) quantidadeAjuste ");

        sql.append("  from estudo_cota_gerado ec ");
        
        sql.append("  inner join cota c on (c.id = ec.cota_id) ");
        
        sql.append("  inner join pessoa pes on (c.pessoa_id = pes.id) ");
        
        sql.append("  inner join estudo_gerado e on (e.id = ec.estudo_id) ");
        
        sql.append("  inner join produto_edicao pe on (pe.id = e.produto_edicao_id) ");
        
        sql.append("  inner join produto p on (p.id = pe.produto_id) ");

        sql.append("  left join fixacao_reparte fx on fx.codigo_icd=p.codigo_icd and fx.id_cota=c.id and fx.id_classificacao_edicao=pe.tipo_classificacao_produto_id ");
        
        sql.append("  left join fixacao_reparte_pdv pdv_fx on pdv_fx.ID_FIXACAO_REPARTE=fx.ID ");

        sql.append("  left join mix_cota_produto mx on mx.codigo_icd=p.codigo_icd and mx.id_cota=c.id and mx.tipo_classificacao_produto_id=pe.tipo_classificacao_produto_id ");
        
        sql.append("  left join reparte_pdv pdv_mx on pdv_mx.MIX_COTA_PRODUTO_ID=mx.ID ");

        sql.append("  left join pdv pdv_default on pdv_default.COTA_ID=c.ID ");

        sql.append("  left join (select cota_id cota_id, count(*) quantidade ");
        sql.append("             from pdv as pdvs ");
        sql.append("             group by cota_id) pdv_qtd on pdv_qtd.cota_id = c.id ");
        
        sql.append(" left join estudo_cota_gerado ecg_origem on ecg_origem.estudo_id = e.estudo_origem_copia and ecg_origem.COTA_ID = ec.cota_id ");
        
        sql.append("  cross join (select DATA_OPERACAO from distribuidor) data_distrib ");
        
        if (queryDTO.possuiOrdenacaoPlusFiltro()) {

            if (queryDTO.possuiOrdenacaoNMaiores() || queryDTO.possuiOrdenacaoRanking()){

            	sql.append(" left join ranking_segmento ranking on (ranking.cota_id = c.id and p.tipo_segmento_produto_id = ranking.tipo_segmento_produto_id) ");
            }

            if (queryDTO.possuiPercentualDeVenda()) {
            	
                sql.append(" left join estoque_produto_cota epc on (epc.cota_id = ec.cota_id and epc.produto_edicao_id = e.produto_edicao_id) ");
            }
        }
        
        if (queryDTO.possuiElemento()) {
        	
            if (queryDTO.elementoIsTipoPontoVenda()) {
            	
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1 and pdv.tipo_ponto_pdv_id = :tipoPontoVenda ) ");
            }
            
            if (queryDTO.elementoIsGeradoorDeFluxo()) {
            	
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                
                sql.append(" join gerador_fluxo_pdv gfp on (gfp.pdv_id = pdv.id and gfp.tipo_gerador_fluxo_id = :geraFluxo) ");
            }
            
            if (queryDTO.elementoIsBairro()) {
            	
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id");
                
                sql.append(" join endereco on (endereco.id = endereco_pdv.endereco_id and endereco.bairro = :bairro ");
            }
            if (queryDTO.elementoIsRegiao()) {
            	
                sql.append(" join registro_cota_regiao as rcr on (rcr.cota_id = c.id and rcr.regiao_id = :regiao) ");
            }
            if (queryDTO.elementoIsAreaDeInfluencia()) {
            	
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1 and pdv.area_influencia_pdv_id = :areaDeInfluencia) ");
            }
            if (queryDTO.elementoIsDistrito()) {
            	
                sql.append(" join pdv on (pdv.cota_id = c.id and pdv.PONTO_PRINCIPAL = 1) ");
                
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id ");
                
                sql.append(" join endereco on (endereco.id = endereco_pdv.endereco_id and endereco.uf = :distrito ");
            }
        }   

        sql.append("  where ec.ESTUDO_ID = :estudoId ");
            
        sql.append("  and  ec.reparte is not null and ec.reparte >= 0  ");
        
        if (queryDTO.possuiOrdenacaoPlusFiltro()) {
        	
            if (queryDTO.possuiOrdenacaoReparte() || queryDTO.possuiOrdenacaoRanking()) {
            	
                sql.append(" and case when ec.classificacao = 'S' then coalesce(ec.reparte, 0) else ec.reparte end between :filterSortFrom and :filterSortTo ");
            }
            
            if (queryDTO.possuiPercentualDeVenda()) {

                sql.append(" and (((epc.qtde_recebida - epc.qtde_devolvida)*100)/epc.qtde_recebida) between :filterSortFrom and :filterSortTo ");
            }
        }    
  
        if (queryDTO.possuiElemento()) {
            
            if (queryDTO.elementoIsCotasNovas()) {
            	
                sql.append(" and ec.classificacao "+ (queryDTO.getValorElemento().equals("1") ? "=" : "<>") +" 'S' ");
            }
            
            if (queryDTO.elementoTipoDistribuicaoCota()) {
            	
            	sql.append(" and c.TIPO_DISTRIBUICAO_COTA = :tipoDistribuicaoCota ");
            }
            
            if (queryDTO.elementoIsCotasAVista()) {
            	
                sql.append(" and c.tipo_cota = upper(:cotasAVista) ");
            }
        }

        if (StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())) {
        	
            sql.append(" and c.numero_cota in (:numeroCota) ");
        }

        if (queryDTO.getFaixaDe() != null) {
        	
            sql.append(" and ec.reparte >= :faixaDe ");
        }
        
        if (queryDTO.getFaixaAte() != null) {
        	
            sql.append(" and ec.reparte <= :faixaAte ");
        }
        
        sql.append(" group by c.ID ");

        if (queryDTO.possuiOrdenacaoNMaiores()) {
        	
            sql.append(" order by ").append(" ranking.qtde desc ").append(" limit ").append(queryDTO.getFilterSortFrom().intValue()>0?queryDTO.getFilterSortFrom().intValue() - 1:0)
            
               .append(" , ").append((queryDTO.getFilterSortTo().intValue() - queryDTO.getFilterSortFrom().intValue()) + 1);
            
        } else if (queryDTO.possuiOrdenacaoRanking()) {
        	
        	 sql.append(" order by ").append(" ranking.qtde desc ");
        	 
        } else if (queryDTO.possuiOrderBy()) {
        	
            sql.append(" order by ").append(queryDTO.getSortName()).append(" ").append(queryDTO.getSortOrder());
        }
        
        Query query = getSession().createSQLQuery(sql.toString());
        
        query.setParameter("estudoId", queryDTO.getEstudoId());
        
        query.setParameterList("statusLancamento", statusLancamento);
        
        if (queryDTO.possuiOrdenacaoPlusFiltro()) {
        	
            if (queryDTO.possuiOrdenacaoReparte() || queryDTO.possuiOrdenacaoRanking() || queryDTO.possuiPercentualDeVenda()) {
            	
            	query.setParameter("filterSortFrom", queryDTO.getFilterSortFrom());
            	
            	query.setParameter("filterSortTo", queryDTO.getFilterSortTo());
            }
        }    
        
        if (queryDTO.possuiElemento()) {
        	
        	if (queryDTO.elementoIsTipoPontoVenda()) {
        		
        		query.setParameter("tipoPontoVenda", queryDTO.getValorElemento());        		
        	}
        	
        	if (queryDTO.elementoIsGeradoorDeFluxo()) {
        		
        		query.setParameter("geraFluxo", queryDTO.getValorElemento());
        	}
        	
        	if (queryDTO.elementoIsBairro()) {
        		
        		query.setParameter("bairro", queryDTO.getValorElemento());
        	}
        	
        	if (queryDTO.elementoIsRegiao()) {
        		
        		query.setParameter("regiao", queryDTO.getValorElemento());
        	}
        	
        	if (queryDTO.elementoIsAreaDeInfluencia()) {
        		
        		query.setParameter("areaDeInfluencia", queryDTO.getValorElemento());
        	}
        	
        	if (queryDTO.elementoIsDistrito()) {
        		
        		query.setParameter("distrito", queryDTO.getValorElemento());
        	}
        	
        	if (queryDTO.elementoIsCotasAVista()) {
        		
        		query.setParameter("cotasAVista", queryDTO.getValorElemento().replaceAll("-","_"));
        	}
        	
            if (queryDTO.elementoTipoDistribuicaoCota()) {
            	
             	query.setParameter("tipoDistribuicaoCota", queryDTO.getValorElemento().toUpperCase());
            }
        }
        
        if (queryDTO.getFaixaDe() != null) {
        	
            query.setParameter("faixaDe", queryDTO.getFaixaDe());
        }
        
        if (queryDTO.getFaixaAte() != null) {

        	query.setParameter("faixaAte", queryDTO.getFaixaAte());
        }

        if (StringUtils.isNotEmpty(queryDTO.getNumeroCotaStr())) {
        	
            query.setParameterList("numeroCota", queryDTO.getNumeroCotas());
        }
        
        ((SQLQuery) query).addScalar("cota", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("cotaId", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("classificacao", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("nome", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("npdv", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("reparteEstudo", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("mediaVenda", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("reparteEstudoOrigemCopia", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("reparteSugerido", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("leg", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("cotaNova", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("juramento", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("ultimoReparte", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("ajustado", StandardBasicTypes.BOOLEAN);
        ((SQLQuery) query).addScalar("quantidadeAjuste", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("mixID", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("fixacaoID", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("contemRepartePorPDV", StandardBasicTypes.BOOLEAN);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseParcialDTO.class));
        
        return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudo(Long estudoId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ");
        sql.append("       pe.id produtoEdicaoId, ");
        sql.append("       p.codigo codigoProduto, ");
        sql.append("       p.nome nomeProduto, ");
        sql.append("       pe.numero_edicao edicao, ");
        sql.append("       epe.periodo_parcial periodo, ");
        sql.append("       tcp.id idTipoClassificacao, ");
        sql.append("       (case when l.tipo_lancamento = 'PARCIAL' then 1 else 0 end) parcial, ");
        sql.append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta, ");
        sql.append("       l.data_lcto_distribuidor as dtLancamento ");
        sql.append("  from estudo_produto_edicao_base epe ");
        sql.append("  join produto_edicao pe on pe.id = epe.produto_edicao_id ");
        sql.append("  join produto p on p.id = pe.produto_id ");
        sql.append("  join lancamento l on l.produto_edicao_id = pe.id ");
        sql.append("  left join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
        sql.append(" where epe.estudo_id = :estudoId ");
        sql.append("  group by epe.periodo_parcial, pe.ID, pe.NUMERO_EDICAO ");
        sql.append("  order by l.data_lcto_distribuidor desc ");
        sql.append("  , pe.numero_edicao desc, epe.periodo_parcial desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("idTipoClassificacao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("dtLancamento", StandardBasicTypes.DATE)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN);
        
        query.setParameter("estudoId", estudoId);
//        query.setParameter("dataLancamento", date);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    
	@SuppressWarnings("unchecked")
	@Override
    public List<EdicoesProdutosDTO> carregarPeriodosAnterioresParcial(Long estudoId, Boolean isTrazerEdicoesAbertas) {

        StringBuilder sql = new StringBuilder();
        
        sql.append(" SELECT  ");
        sql.append("   pe.id produtoEdicaoId, ");
        sql.append("   p.codigo codigoProduto, ");
        sql.append("   p.nome nomeProduto, ");
        sql.append("   pe.numero_edicao edicao, ");
        sql.append("   plp.NUMERO_PERIODO periodo, ");
        sql.append("   pe.TIPO_CLASSIFICACAO_PRODUTO_ID idTipoClassificacao, ");
        sql.append("   l.data_lcto_distribuidor as dtLancamento    ");
        sql.append("    ");
        sql.append(" FROM estudo_gerado eg  ");
        sql.append("   join produto_edicao pe  ");
        sql.append("     ON eg.PRODUTO_EDICAO_ID = pe.ID ");
        sql.append("   join produto p ");
        sql.append("     ON pe.PRODUTO_ID = p.ID ");
        sql.append("   join lancamento l ");
        sql.append("     ON l.PRODUTO_EDICAO_ID = pe.ID ");
        sql.append("   join periodo_lancamento_parcial plp ");
        sql.append("     ON plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
        sql.append(" WHERE eg.id = :estudoId ");
        sql.append("   and l.DATA_LCTO_DISTRIBUIDOR < eg.DATA_LANCAMENTO ");
        
        if(!isTrazerEdicoesAbertas){
        	sql.append("   and l.status in ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') ");
        }
        
        sql.append(" group by plp.NUMERO_PERIODO ");
        sql.append(" order by plp.NUMERO_PERIODO desc ");
        sql.append(" limit 3 ");
        
        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("idTipoClassificacao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("dtLancamento", StandardBasicTypes.DATE);
        
        query.setParameter("estudoId", estudoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }    
    
    @SuppressWarnings("unchecked")
    @Override
    public List<EdicoesProdutosDTO> carregarEdicoesBaseEstudoParcial(Long estudoId, Integer numeroPeriodoBase, boolean parcialComRedistribuicao) {

        StringBuilder sql = new StringBuilder();
        
        sql.append(" select    pe.id produtoEdicaoId, ");
        sql.append("        p.codigo codigoProduto, ");
        sql.append("        p.nome nomeProduto, ");
        sql.append("        pe.numero_edicao edicao, ");
        sql.append("        plp.numero_periodo periodo, ");
        sql.append("        l.data_lcto_distribuidor dataLancamento, ");
        sql.append("        tcp.id idTipoClassificacao, ");
        sql.append("        (case when plp.id is not null then 1 else 0 end) parcial, ");
        
        
        sql.append("        (case when l.status IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO')");
        sql.append("            then ");
        
        sql.append("              		cast(sum( ");
		sql.append("                       CASE ");
		sql.append("                           WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                           THEN ");
		sql.append("                             if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0)  ");
		sql.append("                           ELSE ");
		sql.append("                             if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
		sql.append("                       END) ");
        
        sql.append("                    - (select sum(mecEncalhe.qtde) ");
        sql.append("                        from lancamento lanc ");
        sql.append("                        LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID ");
        sql.append("                        LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID ");
        sql.append("                        LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID ");
        sql.append("                        LEFT JOIN cota cota on cota.id = cec.COTA_ID ");
        sql.append("                        LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
        sql.append("                        LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
        sql.append("                        WHERE lanc.id = l.id and cota.id = c.id) AS UNSIGNED INT) ");
        sql.append("            else ");
        sql.append("                null ");
        sql.append("        end) as venda, ");
        
        sql.append(" cast(sum(                                                                   ");
        sql.append("         CASE                                                                ");
        sql.append("             WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0)    ");
        sql.append("             ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
        sql.append("         END) AS UNSIGNED INT)  as reparte,                                  ");
        
        sql.append("        (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta, ");
        sql.append("        c.numero_cota numeroCota ");
        sql.append(" from estudo_gerado eg ");
        sql.append("   join estudo_cota_gerado ecg on ecg.estudo_id = eg.id ");
        sql.append("   join cota c on c.id = ecg.cota_id ");
        sql.append("   join produto_edicao pe on pe.id = eg.produto_edicao_id ");
        sql.append("   join produto p on p.id = pe.produto_id ");
        sql.append("   join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID ");
        sql.append("   join periodo_lancamento_parcial plp on plp.ID = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
        sql.append("   left join tipo_classificacao_produto tcp on tcp.id = pe.tipo_classificacao_produto_id ");
        sql.append("   left join movimento_estoque_cota mecReparte on mecReparte.lancamento_id = l.id and mecReparte.COTA_ID = c.ID");
        sql.append("   LEFT JOIN tipo_movimento tipo ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID");
        sql.append(" where eg.id = :estudoId ");
        sql.append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
        sql.append( (parcialComRedistribuicao) ? " and plp.NUMERO_PERIODO <= :numeroPeriodoBase " : " and plp.NUMERO_PERIODO < :numeroPeriodoBase ");
        sql.append(" group by plp.NUMERO_PERIODO, ecg.cota_id ");
        sql.append(" order by l.data_lcto_distribuidor desc, ");
        sql.append("   pe.numero_edicao desc, ");
        sql.append("   plp.numero_periodo desc ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("idTipoClassificacao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("numeroCota", StandardBasicTypes.INTEGER);
        
        query.setParameter("estudoId", estudoId);
        query.setParameter("numeroPeriodoBase", numeroPeriodoBase);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    
    public boolean verificarRedistribuicaoNoPeriodoParcial(final Long estudoId,final Integer numeroPeriodoBase){
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select  count(distinct plp.ID) as QUANTIDADE ");
    	sql.append(" from estudo_gerado eg "); 
    	sql.append(" join estudo_cota_gerado ecg on ecg.estudo_id = eg.id "); 
    	sql.append(" join cota c on c.id = ecg.cota_id  ");
    	sql.append(" join produto_edicao pe on pe.id = eg.produto_edicao_id "); 
    	sql.append(" join produto p on p.id = pe.produto_id "); 
    	sql.append(" join lancamento l on l.PRODUTO_EDICAO_ID = pe.ID "); 
    	sql.append(" join periodo_lancamento_parcial plp on plp.ID = l.PERIODO_LANCAMENTO_PARCIAL_ID "); 
    	sql.append(" where eg.id =:estudoId  ");
    	sql.append(" and plp.NUMERO_PERIODO =:numeroPeriodoBase ");
    	sql.append(" and l.NUMERO_LANCAMENTO > 1 ");
    	
    	Query query = getSession().createSQLQuery(sql.toString()).addScalar("QUANTIDADE", StandardBasicTypes.LONG);
    	
    	query.setParameter("estudoId", estudoId);
        query.setParameter("numeroPeriodoBase", numeroPeriodoBase);
    	
        Long count = (Long) Util.nvl(query.uniqueResult(),0);
        
    	return (count > 0 );
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<EdicoesProdutosDTO> carregarPublicacaoDoEstudo(Long estudoId) {

        StringBuilder sql = new StringBuilder();
        sql.append("select distinct ")
        .append("       pe.id produtoEdicaoId, ")
        .append("       p.codigo codigoProduto, ")
        .append("       p.nome nomeProduto, ")
        .append("       pe.numero_edicao edicao, ")
        .append("       '' as periodo, ")
        .append("       (case when l.tipo_lancamento = 'PARCIAL' then 1 else 0 end) parcial, ")
        .append("       (case when l.status = 'FECHADO' or l.status = 'RECOLHIDO' then 0 else 1 end) edicaoAberta ")
        .append("  from estudo_gerado e ")
        .append("  join produto_edicao pe on pe.id = e.produto_edicao_id ")
        .append("  join produto p on p.id = pe.produto_id ")
        .append("  join lancamento l on l.produto_edicao_id = pe.id ")
        .append(" where e.id = :estudoId ")
        .append("  order by l.data_lcto_distribuidor desc , pe.numero_edicao desc");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("codigoProduto", StandardBasicTypes.STRING)
                .addScalar("nomeProduto", StandardBasicTypes.STRING)
                .addScalar("edicao", StandardBasicTypes.BIG_INTEGER)
                .addScalar("periodo", StandardBasicTypes.STRING)
                .addScalar("parcial", StandardBasicTypes.BOOLEAN)
                .addScalar("edicaoAberta", StandardBasicTypes.BOOLEAN);
        query.setParameter("estudoId", estudoId);
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }
    

    @SuppressWarnings("unchecked")
	@Override
    public List<EdicoesProdutosDTO> buscaHistoricoDeVendaParaCota(Long numeroCota, List<Long> listProdutoEdicaoId) {
        
    	StringBuilder sql = new StringBuilder();
                                                                                                                                        
    	sql.append(" select ");
    	sql.append(" mec.produto_edicao_id produtoEdicaoId, ");
    	
    	sql.append(" cast(sum( case when tm.OPERACAO_ESTOQUE = 'ENTRADA' then mec.QTDE else -mec.QTDE end ) as unsigned int) AS reparte,  ");
    	
    	sql.append("          (case when l.status IN (:statusLancFechadoRecolhido)  ");
    	sql.append("            then   ");
    	sql.append("             cast(sum(   ");
    	sql.append("               CASE   ");
    	sql.append("                   WHEN tm.OPERACAO_ESTOQUE = 'ENTRADA'   ");
    	sql.append("                   THEN   ");
    	sql.append("                     mec.QTDE   ");
    	sql.append("                   ELSE   ");
    	sql.append("                     -mec.QTDE   ");
    	sql.append("               END)   ");
    	sql.append("               - (select sum(mecEncalhe.qtde)   ");
    	sql.append("                   from lancamento lanc   ");
    	sql.append("                   LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID   ");
    	sql.append("                   LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID   ");
    	sql.append("                   LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID   ");
    	sql.append("                   LEFT JOIN cota cota on cota.id = cec.COTA_ID   ");
    	sql.append("                   LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID   ");
    	sql.append("                   LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID   ");
    	sql.append(" 	              WHERE lanc.id = l.id and cota.id = c.id) AS UNSIGNED INT)   ");
    	sql.append(" 	        else   ");
    	sql.append("             null   ");
    	sql.append("             end) as venda ");
    	
    	sql.append(" from movimento_estoque_cota mec force index (NDX_PRODUTO_EDICAO) ");
    	sql.append(" inner join tipo_movimento tm on tm.id = mec.tipo_movimento_id ");
    	sql.append(" inner join cota c on c.id = :numeroCota ");
    	sql.append(" inner join lancamento l on mec.lancamento_id = l.id ");
    	sql.append(" left outer join periodo_lancamento_parcial plp on plp.id = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
    	sql.append(" where 1 = 1");
    	sql.append(" and c.id = :numeroCota");
    	sql.append(" and mec.PRODUTO_EDICAO_ID in (:produtoEdicaoId) and mec.cota_id = c.id  and mec.cota_id = :numeroCota");
    	sql.append(" and l.STATUS IN (:lancamentosPosExpedicao)");
    	sql.append(" and tm.GRUPO_MOVIMENTO_ESTOQUE  <> 'ENVIO_ENCALHE' ");
    	sql.append(" and mec.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null ");
    	sql.append(" group by mec.PRODUTO_EDICAO_ID");
    	
        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
                .addScalar("reparte", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("venda", StandardBasicTypes.BIG_DECIMAL);

        query.setParameter("numeroCota", numeroCota);
        query.setParameterList("produtoEdicaoId", listProdutoEdicaoId);
        query.setParameterList("lancamentosPosExpedicao", LancamentoHelper.getStatusLancamentosPosExpedicaoString());
        query.setParameterList("statusLancFechadoRecolhido", Arrays.asList(StatusLancamento.FECHADO.name(), StatusLancamento.RECOLHIDO.name(), StatusLancamento.EM_RECOLHIMENTO.name()));
        
        query.setResultTransformer(new AliasToBeanResultTransformer(EdicoesProdutosDTO.class));

        return query.list();
    }

    @Override
    public void atualizaReparteCota(Long estudoId, Integer numeroCota, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("  update estudo_cota_gerado ec ");
        sql.append("  left join cota cota on cota.id = ec.cota_id ");
        sql.append("  set ec.reparte = coalesce(ec.reparte,0) + ?, ");
        sql.append("      ec.qtde_efetiva = coalesce(ec.qtde_efetiva,0) + ? ");
        sql.append("  where ec.estudo_id = ? ");
        sql.append("  and cota.numero_cota = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, reparteSubtraido);
        query.setLong(2, estudoId);
        query.setInteger(3, numeroCota);
        query.executeUpdate();
    }

    @Override
    public void atualizaClassificacaoCota(Long estudoId, Integer numeroCota, String classificacaoCota) {

        StringBuilder sql = new StringBuilder();
        
        sql.append("update estudo_cota_gerado ec ");
        sql.append("  left join cota cota on cota.id = ec.cota_id ");
        sql.append("   set ec.classificacao = ? ");
        sql.append(" where ec.estudo_id = ? ");
        sql.append("   and cota.numero_cota = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setString(0, classificacaoCota);
        query.setLong(1, estudoId);
        query.setInteger(2, numeroCota);
        query.executeUpdate();
    }

    @Override
    public void atualizaReparteEstudo(Long estudoId, Long reparteSubtraido) {

        StringBuilder sql = new StringBuilder();
        sql.append("update estudo_gerado ");
        sql.append("   set sobra = coalesce(sobra, 0) - ? ");
        sql.append(" where id = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteSubtraido);
        query.setLong(1, estudoId);
        query.executeUpdate();
    }

    @Override
    public void liberar(Long id) {
        SQLQuery query = getSession().createSQLQuery("update estudo_gerado set liberado = 1 where id = ? ");
        query.setLong(0, id);
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<PdvDTO> carregarDetalhesPdv(Integer numeroCota, Long idEstudo) {
       
    	StringBuilder sql = new StringBuilder();
        sql.append("select pdv.id, ");
        sql.append("       t.descricao descricaoTipoPontoPDV, ");
        sql.append("       pdv.nome nomePDV,");
        sql.append("       pdv.porcentagem_faturamento porcentagemFaturamento,");
        sql.append("       ifnull(pdv.ponto_principal, false) principal, ");
        sql.append("       est.reparte,");
        sql.append("       concat(e.logradouro, ', ', e.numero, ' - ', e.bairro, ' - ', e.cep, ' - ', e.cidade, ' - ', e.uf) endereco ");
        sql.append("  from pdv ");
        sql.append("  left join cota c on c.id = pdv.cota_id ");
        sql.append("  left join endereco_pdv ep on ep.pdv_id = pdv.id and ep.principal = true");
        sql.append("  left join endereco e on e.id = ep.endereco_id ");
        sql.append("  left join tipo_ponto_pdv t on t.id = pdv.tipo_ponto_pdv_id ");
        sql.append("  left join estudo_pdv est on est.pdv_id = pdv.id and est.estudo_id = :idEstudo ");
        sql.append(" where c.numero_cota = :numeroCota GROUP BY pdv.id ");

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("descricaoTipoPontoPDV", StandardBasicTypes.STRING)
                .addScalar("nomePDV", StandardBasicTypes.STRING)
                .addScalar("porcentagemFaturamento", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("principal", StandardBasicTypes.BOOLEAN)
                .addScalar("reparte", StandardBasicTypes.INTEGER)
                .addScalar("endereco", StandardBasicTypes.STRING);
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("idEstudo", idEstudo);
        query.setResultTransformer(new AliasToBeanResultTransformer(PdvDTO.class));

        return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<CotaQueNaoEntrouNoEstudoDTO> buscarCotasQueNaoEntraramNoEstudo(CotasQueNaoEntraramNoEstudoQueryDTO queryDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cota.numero_cota numeroCota, ");
        sql.append("       coalesce(pe.nome, pe.razao_social, pe.nome_fantasia, '') nomeCota, ");
        sql.append("       ec.reparte quantidade, ");
        sql.append("       ec.venda_media vendaMedia, ");
        sql.append("       cast(cota.situacao_cadastro as char) as situacaoCota, ");
        sql.append("       ec.classificacao motivo ");
        sql.append("  from estudo_cota_gerado ec ");
        sql.append("  join cota on cota.id = ec.cota_id ");
        sql.append("  join pessoa pe on pe.id = cota.pessoa_id ");
        sql.append("  left join ranking_segmento rks on rks.cota_id = cota.id ");
        sql.append("     and rks.tipo_segmento_produto_id = ? ");
        sql.append("     AND DATE_FORMAT(rks.data_geracao_rank,'%d/%m/%Y') = (SELECT  DATE_FORMAT(max(data_geracao_rank),'%d/%m/%Y') FROM ranking_segmento) ");

        StringBuilder where = new StringBuilder();
        List<Object> params = new ArrayList<>();
        List<Object> paramsWhere = new ArrayList<>();

        params.add(queryDTO.getTipoSegmentoProduto());

        if (queryDTO.possuiElemento()) {
            if (queryDTO.elementoIsTipoPontoVenda()) {
                sql.append(" join pdv on pdv.cota_id = cota.id and pdv.tipo_ponto_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsGeradoorDeFluxo()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join gerador_fluxo_pdv gfp on gfp.pdv_id = pdv.id and gfp.tipo_gerador_fluxo_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsBairro()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.bairro = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("' ");
            }
            if (queryDTO.elementoIsRegiao()) {
                sql.append(" join registro_cota_regiao as rcr on rcr.cota_id = cota.id and rcr.regiao_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsAreaDeInfluencia()) {
                sql.append(" join pdv on pdv.cota_id = cota.id and pdv.area_influencia_pdv_id = ? ");
                params.add(queryDTO.getValorElemento());
            }
            if (queryDTO.elementoIsDistrito()) {
                sql.append(" join pdv on pdv.cota_id = cota.id ");
                sql.append(" join endereco_pdv on endereco_pdv.pdv_id = pdv.id ");
                sql.append(" join endereco on endereco.id = endereco_pdv.endereco_id and endereco.uf = '");
                sql.append(queryDTO.getValorElemento());
                sql.append("' ");
            }
            if (queryDTO.elementoIsCotasAVista()) {
            	
                sql.append(" join parametro_cobranca_cota pcc on pcc.id = cota.parametro_cobranca_id ");
                sql.append(" and pcc.tipo_cota = upper('");
                sql.append(queryDTO.getValorElemento().replaceAll("-","_"));
                sql.append("') ");
            }
        }

        where.append(" and ec.estudo_id = ? ");
        where.append("   and (ec.reparte is null) and (ec.qtde_efetiva is null or ec.qtde_efetiva = 0) ");
        where.append("   and ec.classificacao <> 'S' ");
        paramsWhere.add(queryDTO.getEstudo());

        where.append(" and (cota.situacao_cadastro = 'ATIVO' ");
        
		where.append(" or (cota.SITUACAO_CADASTRO = 'SUSPENSO' and "); 
		where.append(" 	(select max(h.DATA_INICIO_VALIDADE) from historico_situacao_cota h where h.COTA_ID=cota.ID) >= ");
		where.append(" 	date_sub((select data_operacao from distribuidor), interval 90 day) ");
		where.append(" )) ");
    
        if (queryDTO.possuiCota()) {
            where.append(" and cota.numero_cota = ? ");
            paramsWhere.add(queryDTO.getCota());
        }

        if (queryDTO.possuiNome()) {
            where.append(" and (pe.nome like upper('").append(queryDTO.getNome()).append("') ");
            where.append("  or pe.razao_social like upper('").append(queryDTO.getNome()).append("') ");
            where.append("  or pe.nome_fantasia like upper('").append(queryDTO.getNome()).append("')) ");
        }

        if (queryDTO.getMotivo() != null && !queryDTO.getMotivo().equals("TODOS")) {
            where.append(" and ec.classificacao = '");
            where.append(queryDTO.getMotivo());
            where.append("' ");
        }

        sql.append(" where 1 = 1 ").append(where);
        sql.append(" order by ");
        
        if(queryDTO.getPaginacao() != null && queryDTO.getPaginacao().getOrdenacao() != null) {
        
        	sql.append(queryDTO.getPaginacao().getOrderByClause());
        
        } else {
        	
        	sql.append(" rks.QTDE desc ");
        }

        Query query = getSession().createSQLQuery(sql.toString())
                .addScalar("numeroCota", StandardBasicTypes.INTEGER)
                .addScalar("nomeCota", StandardBasicTypes.STRING)
                .addScalar("quantidade", StandardBasicTypes.BIG_INTEGER)
                .addScalar("vendaMedia", StandardBasicTypes.BIG_DECIMAL)
                .addScalar("situacaoCota", StandardBasicTypes.STRING)
                .addScalar("motivo", StandardBasicTypes.STRING);

        for (int i = 0; i < params.size(); i++) {
            query.setParameter(i, params.get(i));
        }
        int j = 0;
        for (int i = params.size(); i < params.size() + paramsWhere.size(); i++) {
            query.setParameter(i, paramsWhere.get(j++));
        }

        query.setResultTransformer(new AliasToBeanResultTransformer(CotaQueNaoEntrouNoEstudoDTO.class));

        return query.list();
    }
    
	@Override
	public AnaliseParcialDTO buscarReparteDoEstudo(Long estudoOrigem,Integer numeroCota) {
		String sql = "select estudo_cota.REPARTE as ultimoReparte from estudo_cota_gerado estudo_cota join cota ON estudo_cota.COTA_ID = cota.ID where estudo_id = :estudoID and cota.numero_cota= :numeroCota";
		
		  SQLQuery query = getSession().createSQLQuery(sql);
	        query.setParameter("estudoID", estudoOrigem);
	        query.setParameter("numeroCota", numeroCota);
	        query.addScalar("ultimoReparte", StandardBasicTypes.BIG_DECIMAL);
	        
	        query.setResultTransformer(new AliasToBeanResultTransformer(AnaliseParcialDTO.class));

	        return (AnaliseParcialDTO) query.uniqueResult();
		
	}
    @Override
    public Integer[] buscarCotasPorTipoDistribuicao(TipoDistribuicaoCota tipo) {
        ScrollableResults results = getSession().createCriteria(Cota.class).add(Restrictions.eq("tipoDistribuicaoCota", tipo)).scroll();

        List<Integer> listNumeroCota = new ArrayList<>();
        while (results.next()) {
            Cota cota = (Cota) results.get()[0];
            listNumeroCota.add(cota.getNumeroCota());
        }
        return listNumeroCota.toArray(new Integer[listNumeroCota.size()-1]);
    }

    @Override
    public void atualizaReparteTotalESaldo(Long idEstudo, Integer reparteTotal) {
        StringBuilder sql = new StringBuilder();
        sql.append("update estudo_gerado ");
        sql.append("   set sobra = coalesce(sobra, 0) - (qtde_reparte - ?), ");
        sql.append("       qtde_reparte = ? ");
        sql.append(" where id = ? ");

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setLong(0, reparteTotal);
        query.setLong(1, reparteTotal);
        query.setLong(2, idEstudo);
        query.executeUpdate();
    }

	@Override
	public DetalhesEdicoesBasesAnaliseEstudoDTO buscarReparteVendaTotalPorEdicao(String codigoProduto, Long edicao, Long idTipoClassificacao, Integer numPeriodoParcial) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT  straight_join ");
		sql.append("	   p.CODIGO as codigoProduto, ");
		sql.append("       p.NOME_COMERCIAL as nomeProduto, ");
		sql.append("       pe.NUMERO_EDICAO as edicao, ");
		sql.append("       l.DATA_LCTO_DISTRIBUIDOR as dataLancamento, ");
		sql.append("        cast(sum( ");
		sql.append("                 CASE ");
		sql.append("                     WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                         THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                		 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
		sql.append("                 END) AS UNSIGNED INT) AS reparte, ");
		sql.append("        CASE ");
		sql.append("           WHEN l.STATUS IN ('FECHADO', 'RECOLHIDO', 'EM_RECOLHIMENTO') ");
		sql.append("           THEN ");
		sql.append("              cast(sum( ");
		sql.append("                       CASE ");
		sql.append("                           WHEN tipo.OPERACAO_ESTOQUE = 'ENTRADA' ");
		sql.append("                         THEN if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, mecReparte.QTDE, 0) ");
		sql.append("                		 ELSE if(mecReparte.MOVIMENTO_ESTOQUE_COTA_FURO_ID is null, - mecReparte.QTDE, 0) ");
		sql.append("                       END) ");
		sql.append("                 - (SELECT sum(mecEncalhe.qtde) ");
		sql.append("                      FROM lancamento lanc ");
		sql.append("                           LEFT JOIN chamada_encalhe_lancamento cel ");
		sql.append("                              ON cel.LANCAMENTO_ID = lanc.ID ");
		sql.append("                           LEFT JOIN chamada_encalhe ce ");
		sql.append("                              ON ce.id = cel.CHAMADA_ENCALHE_ID ");
		sql.append("                           LEFT JOIN chamada_encalhe_cota cec ");
		sql.append("                              ON cec.CHAMADA_ENCALHE_ID = ce.ID ");
		sql.append("                           LEFT JOIN conferencia_encalhe confEnc ");
		sql.append("                              ON confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID ");
		sql.append("                           LEFT JOIN movimento_estoque_cota mecEncalhe ");
		sql.append("                              ON mecEncalhe.id = ");
		sql.append("                                    confEnc.MOVIMENTO_ESTOQUE_COTA_ID ");
		sql.append("                     WHERE lanc.id = l.id) AS UNSIGNED INT) ");
		sql.append("           ELSE ");
		sql.append("              NULL ");
		sql.append("        END ");
		sql.append("           AS venda ");
		sql.append("   FROM lancamento l ");
		sql.append("        JOIN produto_edicao pe ");
		sql.append("           ON pe.id = l.produto_edicao_id ");
		sql.append("        LEFT JOIN periodo_lancamento_parcial plp ");
		sql.append("           ON plp.id = l.periodo_lancamento_parcial_id ");
		sql.append("        JOIN (SELECT p.* ");
		sql.append("                FROM produto p ");
		sql.append("               WHERE p.codigo_icd = (SELECT codigo_icd ");
		sql.append("                                       FROM produto p ");
		sql.append("                                      WHERE p.codigo = :codProduto) ");
		sql.append("              UNION ");
		sql.append("              SELECT p.* ");
		sql.append("                FROM produto p ");
		sql.append("               WHERE p.codigo_icd = :codProduto) p ");
		sql.append("           ON p.id = pe.produto_id ");
		sql.append("        LEFT JOIN tipo_classificacao_produto tcp ");
		sql.append("           ON tcp.id = pe.tipo_classificacao_produto_id ");
		sql.append("        LEFT JOIN movimento_estoque_cota mecReparte ");
		sql.append("           ON mecReparte.LANCAMENTO_ID = l.id ");
		sql.append("        LEFT JOIN tipo_movimento tipo ");
		sql.append("           ON tipo.id = mecReparte.TIPO_MOVIMENTO_ID ");
		sql.append("  WHERE l.status IN ");
		sql.append("                   ('EXPEDIDO', ");
		sql.append("                   'EM_BALANCEAMENTO_RECOLHIMENTO', ");
		sql.append("                   'BALANCEADO_RECOLHIMENTO', ");
		sql.append("                   'EM_RECOLHIMENTO', ");
		sql.append("                   'RECOLHIDO', ");
		sql.append("                   'FECHADO') ");
		sql.append("        AND tipo.GRUPO_MOVIMENTO_ESTOQUE <> 'ENVIO_ENCALHE' ");
		sql.append("        AND pe.NUMERO_EDICAO = :edicao");
		sql.append("        AND tcp.ID = :tipClassif ");
		
		if(numPeriodoParcial != null){
			sql.append("    AND plp.NUMERO_PERIODO = :numPeriodo ");
		}
		
		sql.append(" GROUP BY pe.numero_edicao, pe.id ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("codProduto", codigoProduto);
		query.setParameter("edicao", edicao);
		query.setParameter("tipClassif", idTipoClassificacao);
		
		if(numPeriodoParcial != null){
			query.setParameter("numPeriodo", numPeriodoParcial);
		}
		
		query.addScalar("codigoProduto", StandardBasicTypes.STRING);
		query.addScalar("nomeProduto", StandardBasicTypes.STRING);
		query.addScalar("edicao", StandardBasicTypes.LONG);
		query.addScalar("dataLancamento", StandardBasicTypes.DATE);
		query.addScalar("reparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("venda", StandardBasicTypes.BIG_INTEGER);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DetalhesEdicoesBasesAnaliseEstudoDTO.class));
				
		return (DetalhesEdicoesBasesAnaliseEstudoDTO) query.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DataLancamentoPeriodoEdicoesBasesDTO> obterDataDeLacmtoPeriodoParcial (Long idEstudo, Long idProdutoEdicao){
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		sql.append("   lc.DATA_LCTO_DISTRIBUIDOR as dataLancamento,  ");
		sql.append("   plp.NUMERO_PERIODO as numPeriodo  ");
		sql.append(" from lancamento lc  ");
		sql.append(" join periodo_lancamento_parcial plp  ");
		sql.append("   on plp.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" join estudo_produto_edicao_base epe  ");
		sql.append("   on epe.ESTUDO_ID = :estudoId ");
		sql.append(" where lc.PRODUTO_EDICAO_ID = :produtoEdicaoId  ");
		sql.append("     and plp.NUMERO_PERIODO = epe.PERIODO_PARCIAL  ");
		sql.append(" order by plp.NUMERO_PERIODO desc limit 3; ");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", idEstudo);
		query.setParameter("produtoEdicaoId", idProdutoEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(DataLancamentoPeriodoEdicoesBasesDTO.class));
		
		return query.list(); 
	}
	
	@Override
	public Integer qtdBasesNoEstudo (Long idEstudo, boolean isBaseParcial){
		
		StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select  ");
    	sql.append("   COUNT(peb.ID) qtdBasesParciais ");
    	sql.append(" from estudo_gerado eg  ");
    	sql.append("   join estudo_produto_edicao_base peb ");
    	sql.append("     on peb.ESTUDO_ID = eg.ID ");
    	sql.append(" where eg.ID = :estudoId and parcial = :isParcial ");
    	
    	Query query = getSession().createSQLQuery(sql.toString()).addScalar("qtdBasesParciais", StandardBasicTypes.INTEGER);;
    	
    	query.setParameter("estudoId", idEstudo);
        query.setParameter("isParcial", isBaseParcial);
    	
    	return (Integer) query.uniqueResult();
	}
	
}
