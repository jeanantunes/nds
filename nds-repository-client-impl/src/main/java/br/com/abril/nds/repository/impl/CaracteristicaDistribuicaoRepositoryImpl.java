package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CaracteristicaDistribuicaoDTO;
import br.com.abril.nds.dto.CaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaCaracteristicaDistribuicaoSimplesDTO;
import br.com.abril.nds.repository.CaracteristicaDistribuicaoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class CaracteristicaDistribuicaoRepositoryImpl   implements
CaracteristicaDistribuicaoRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CaracteristicaDistribuicaoRepositoryImpl.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    
    protected Session getSession() {
        try {
            return sessionFactory.getCurrentSession();
        } catch (final Exception e) {
            LOGGER.debug(e.getMessage(), e);
        }
        
        return sessionFactory.openSession();
    }
    
    private void configurarPaginacaoPesquisaSimples(final FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro, final Query query) {
        
        final PaginacaoVO paginacao = filtro.getPaginacao();
        
        if(paginacao!=null){
            if (paginacao.getQtdResultadosTotal() == 0) {
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
    
	@Override
	@SuppressWarnings("unchecked")
    public List<CaracteristicaDistribuicaoDTO> obterCaracteristicaDistribuicaoDetalhe(
            final FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder("");
        sql.append(" select distinct ")
        .append(" pro.codigo as 'codigoProduto', ")
        .append(" pro.nome as 'nomeProduto', ")
        .append(" pes2.RAZAO_SOCIAL as 'nomeEditor', ")
        .append(" ped.CHAMADA_CAPA as 'chamadaCapa', ")
        .append(" ped.NUMERO_EDICAO as 'numeroEdicao', ")
        .append(" coalesce(tipoclas.descricao, '') as 'classificacao', ")
        .append(" coalesce(ped.PRECO_VENDA, 0) as 'precoCapa', ")
        
       .append(" cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else -mecReparte.QTDE end) as unsigned int) AS reparte,             ")
       .append("                                                                                                              ")
       .append("     case when lan.STATUS IN ('FECHADO',                                                                      ")
       .append("                             'RECOLHIDO',                                                                     ")
       .append("                             'EM_RECOLHIMENTO') then                                                          ")
       .append("                                                                                                              ")
       .append("       cast(sum(case when tipo.OPERACAO_ESTOQUE = 'ENTRADA' then mecReparte.QTDE else -mecReparte.QTDE end) - (                                ")
       .append("           select sum(mecEncalhe.qtde)                                                                        ")
       .append("           from lancamento lanc                                                                               ")
       .append("           LEFT JOIN chamada_encalhe_lancamento cel on cel.LANCAMENTO_ID = lanc.ID                            ")
       .append("           LEFT JOIN chamada_encalhe ce on ce.id = cel.CHAMADA_ENCALHE_ID                                     ")
       .append("           LEFT JOIN chamada_encalhe_cota cec on cec.CHAMADA_ENCALHE_ID = ce.ID                               ")
       .append("           LEFT JOIN conferencia_encalhe confEnc on confEnc.CHAMADA_ENCALHE_COTA_ID = cec.ID                  ")
       .append("           LEFT JOIN movimento_estoque_cota mecEncalhe on mecEncalhe.id = confEnc.MOVIMENTO_ESTOQUE_COTA_ID   ")
       .append("           WHERE lanc.id = lan.id                                                                             ")
       .append("       ) as unsigned int)                                                                                     ")
       .append("                                                                                                              ")
       .append("     else null end as venda, ")
        
        
        .append(" lan.DATA_LCTO_DISTRIBUIDOR  as 'dataLancamento', 				")
        .append(" lan.DATA_REC_DISTRIB as 'dataRecolhimento', 					")
        .append(" tiposeg.DESCRICAO as 'segmento' 								")
        
        .append(" from produto pro ")
        .append(" join produto_edicao ped on pro.ID = ped.PRODUTO_ID ")
        .append(" left join tipo_segmento_produto tiposeg ON tiposeg.ID = pro.TIPO_SEGMENTO_PRODUTO_ID ")
        .append(" left join tipo_classificacao_produto tipoclas ON tipoclas.ID = ped.tipo_classificacao_produto_id ")
        .append(" left join editor edi on edi.id = pro.editor_id ")
        .append(" join pessoa pes2 on pes2.id = edi.JURIDICA_ID ")
        .append(" left join lancamento lan on lan.PRODUTO_EDICAO_ID = ped.ID  ")
        .append(" left join estoque_produto est on est.PRODUTO_EDICAO_ID = ped.ID  ")
        .append(" left join movimento_estoque_cota mecReparte ON mecReparte.LANCAMENTO_ID = lan.ID ")
        .append(" left join tipo_movimento tipo ON mecReparte.TIPO_MOVIMENTO_ID = tipo.ID  ")
        
        .append(" where lan.DATA_LCTO_DISTRIBUIDOR <> '3000-01-01' ")
        .append(" and lan.STATUS not in ('CONFIRMADO', 'PLANEJADO', 'FURO', 'BALANCEADO', 'EM_BALANCEAMENTO') ")
        .append(" and tipo.GRUPO_MOVIMENTO_ESTOQUE not in ('ENVIO_ENCALHE') ");
        
        if(filtro.getCodigoProduto() != null && filtro.getCodigoProduto() != "") {
            sql.append(" and pro.codigo_icd = :codigoProduto ");
        }
        
        if(filtro.getClassificacaoProduto() != null && filtro.getClassificacaoProduto() != "") {
            sql.append(" and upper(tipoclas.descricao) = upper(:classificacaoProduto) ");
        }
        
        if(filtro.getSegmento() != null && filtro.getSegmento() != "") {
            sql.append(" and upper(tiposeg.DESCRICAO) = upper(:segmento) ");
        }
        
        if(filtro.isBrinde()) {
            sql.append(" and ped.POSSUI_BRINDE is true ");
        }else{
        	sql.append(" and ped.POSSUI_BRINDE is false ");
        }
        
        if(filtro.getFaixaPrecoDe()!=null && filtro.getFaixaPrecoDe()!="") {
            sql.append(" and ped.PRECO_VENDA >= :faixaPrecoDe ");
        }
        
        if(filtro.getFaixaPrecoAte()!=null && filtro.getFaixaPrecoAte()!="") {
            sql.append(" and ped.PRECO_VENDA <= :faixaPrecoAte ");
        }
        
        //tipo pesquisa publicacao
        if(filtro.getNomeProduto() != null && filtro.getNomeProduto().trim() != "") {
            if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
                sql.append(" and upper(pro.nome) = upper(:nomeProduto) ");//exato
            } else {
                //contem
                sql.append(" and upper(pro.nome) like upper(:nomeProduto) ");//contem
            }
        }
        //tipo pesquisa editor
        if(filtro.getNomeEditor()!=null && filtro.getNomeEditor()!="") {
            if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
                sql.append(" and upper(pes2.RAZAO_SOCIAL) = upper(:nomeEditor) ");
            } else {
                //contem
                sql.append(" and upper(pes2.RAZAO_SOCIAL) like upper(:nomeEditor) ");
            }
        }
        
        //chamada de capa
        if(filtro.getChamadaCapa() != null && filtro.getChamadaCapa() != "") {
            if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
                sql.append(" and upper(ped.CHAMADA_CAPA) = upper(:chamadaCapa) ");
            } else {
                //contem
                sql.append(" and upper(ped.CHAMADA_CAPA) like upper(:chamadaCapa) ");
            }
        }
        
        sql.append(" group by ped.id ");
        sql.append(this.ordenarConsultaCaracteristicaDistribuicaoDetalhe(filtro));
        
        final Query  query = getSession().createSQLQuery(sql.toString());
        
        if(filtro.getCodigoProduto() != null && filtro.getCodigoProduto() != "") {
            query.setParameter("codigoProduto", filtro.getCodigoProduto().trim());
        }
        
        if(filtro.getClassificacaoProduto() != null && filtro.getClassificacaoProduto() != "") {
        	query.setParameter("classificacaoProduto", filtro.getClassificacaoProduto().trim());
        }
        
        if(filtro.getSegmento() != null && filtro.getSegmento() != "") {
        	query.setParameter("segmento", filtro.getSegmento().trim());
        }
        
        if(filtro.getFaixaPrecoDe() != null && filtro.getFaixaPrecoDe() != "") {
        	query.setParameter("faixaPrecoDe", filtro.getFaixaPrecoDe().trim());
        }
        
        if(filtro.getFaixaPrecoAte() != null && filtro.getFaixaPrecoAte() != "") {
        	query.setParameter("faixaPrecoAte", filtro.getFaixaPrecoAte().trim());
        }
        
        //tipo pesquisa publicacao
        if(filtro.getNomeProduto() != null && filtro.getNomeProduto().trim() != "") {
        	if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
        		query.setParameter("nomeProduto", filtro.getNomeProduto().trim());
            } else {
            	query.setParameter("nomeProduto", "%"+ filtro.getNomeProduto().trim() +"%");
            }
        }
        //tipo pesquisa editor
        if(filtro.getNomeEditor() != null && filtro.getNomeEditor() != "")	{
        	if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
        		query.setParameter("nomeEditor", filtro.getNomeEditor().trim());
            } else {
            	query.setParameter("nomeEditor", "%"+ filtro.getNomeEditor().trim() +"%");
            }
        }
        
        //chamada de capa
        if(filtro.getChamadaCapa() != null && filtro.getChamadaCapa() != "") {
        	if(filtro.getOpcaoFiltroPublicacao()) {
                //exato
        		query.setParameter("chamadaCapa", filtro.getChamadaCapa().trim());
            } else {
            	query.setParameter("chamadaCapa", "%"+ filtro.getChamadaCapa().trim() +"%");
            }
        }
        
        query.setResultTransformer(new AliasToBeanResultTransformer(CaracteristicaDistribuicaoDTO.class));
        configurarPaginacaoPesquisaDetalhe(filtro, query);
        
        return query.list();
    }
    
    private String ordenarConsultaCaracteristicaDistribuicaoDetalhe(final FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder();
        
        if (filtro.getOrdemColuna() != null) {
            
            switch (filtro.getOrdemColuna()) {
            
            
            case CODIGO:
                sql.append(" ORDER BY codigoProduto ");
                break;
                
            case PRODUTO:
                sql.append(" ORDER BY nomeProduto ");
                break;
                
            case EDITOR:
                sql.append(" ORDER BY nomeEditor ");
                break;
                
            case EDICAO:
                sql.append(" ORDER BY numeroEdicao ");
                break;
                
            case PRECO_CAPA:
                sql.append(" ORDER BY precoCapa ");
                break;
                
            case CLASSIFICACAO:
                sql.append(" ORDER BY classificacao ");
                break;
                
            case SEGMENTO:
                sql.append(" ORDER BY segmento ");
                break;
                
            case DT_LANCAMENTO:
                sql.append(" ORDER BY dataLancamento ");
                break;
                
            case DT_RECOLHIMENTO:
                sql.append(" ORDER BY dataRecolhimento ");
                break;
                
            case REPARTE:
                sql.append(" ORDER BY reparte ");
                break;
                
            case VENDA:
                sql.append(" ORDER BY venda ");
                break;
                
            case CHAMADA_CAPA:
                sql.append(" ORDER BY chamadaCapa ");
                break;
                
            default:
                sql.append(" ORDER BY dataLancamento desc ");
                break;
            }
            
            if (filtro.getPaginacao() != null && filtro.getPaginacao().getOrdenacao() != null) {
                sql.append(filtro.getPaginacao().getOrdenacao().toString());
            }
            
        }
        
        return sql.toString();
    }
    
    private void configurarPaginacaoPesquisaDetalhe(final FiltroConsultaCaracteristicaDistribuicaoDetalheDTO dto, final Query query) {
    	
        final PaginacaoVO paginacao = dto.getPaginacao();
        
        if(paginacao != null) {
        	
            if (paginacao.getQtdResultadosTotal() == 0) {
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
    
    @Override
    public List<CaracteristicaDistribuicaoSimplesDTO> obterCaracteristicaDistribuicaoSimples(final FiltroConsultaCaracteristicaDistribuicaoSimplesDTO filtro) {
        
        final StringBuilder sql = new StringBuilder("");
        sql.append(" select distinct ")
        .append(" pro.codigo as 'codigoProduto', ")
        .append(" pro.nome as 'nomeProduto', ")
        .append(" coalesce(pes2.NOME_FANTASIA, pes2.RAZAO_SOCIAL) as 'nomeEditor' ")
        .append(" from produto pro ")
        
        .append(" left join produto_edicao ped on pro.ID = ped.PRODUTO_ID ")
        .append(" left join editor edi on edi.id = pro.editor_id ")
        .append(" left join pessoa pes2 on pes2.id = edi.JURIDICA_ID ")
        .append(" where  1=1 ");
        
        if(filtro.getCodigoProduto() !=null && filtro.getCodigoProduto() != ""){
            
            if(filtro.getCodigoProduto().length() == 6){
                sql.append(" and pro.codigo_icd = " ).append(filtro.getCodigoProduto());
            }else{
                sql.append(" and pro.codigo = " ).append(filtro.getCodigoProduto());
            }
            
        }
        
        //tipo pesquisa publicacao
        if(filtro.getNomeProduto() !=null && filtro.getNomeProduto()!=""){
            if(filtro.isOpcaoFiltroPublicacao()){
                //exato
                sql.append(" and upper(pro.nome) = ").append(" upper ('").append(filtro.getNomeProduto()).append("')");//exato
            }else{
                //contem
                sql.append(" and upper(pro.nome) like ").append(" upper ('%").append(filtro.getNomeProduto()).append("%')");//contem
            }
            
        }
        //tipo pesquisa editor
        if(filtro.getNomeEditor()!=null && filtro.getNomeEditor()!="")	{
            if(filtro.isOpcaoFiltroPublicacao()){
                //exato
                sql.append(" and upper(pes2.RAZAO_SOCIAL) =").append(" upper('").append(filtro.getNomeEditor()).append("')");
            }else{
                //contem
                sql.append(" and upper(pes2.RAZAO_SOCIAL) like").append(" upper('%").append(filtro.getNomeEditor()).append("%')");
            }
            
        }
        
        final Query  query = getSession().createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(CaracteristicaDistribuicaoSimplesDTO.class));
        configurarPaginacaoPesquisaSimples(filtro,query);
        return query.list();
    }
    
    
}
