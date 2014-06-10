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
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
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
    public List<CaracteristicaDistribuicaoDTO> obterCaracteristicaDistribuicaoDetalhe(
            final FiltroConsultaCaracteristicaDistribuicaoDetalheDTO filtro) {
        
        final StringBuilder sql = new StringBuilder("");
        sql.append(" select distinct ")
        .append(" pro.codigo as 'codigoProduto', ")
        .append(" pro.nome as 'nomeProduto', ")
        .append(" pes2.NOME_FANTASIA as 'nomeEditor', ")
        .append(" ped.CHAMADA_CAPA as 'chamadaCapa', ")
        .append(" ped.NUMERO_EDICAO as 'numeroEdicao', ")
        .append(" coalesce(tipoclas.descricao, '') as 'classificacao', ")
        .append(" coalesce(ped.PRECO_VENDA, 0) as 'precoCapa', ")
        .append(" coalesce(lan.REPARTE,0) - ")
        .append(" 	(select sum(me.QTDE) from movimento_estoque me join tipo_movimento tm on tm.ID = me.TIPO_MOVIMENTO_ID ")
        .append(" 	where me.PRODUTO_EDICAO_ID=ped.ID and tm.GRUPO_MOVIMENTO_ESTOQUE = :grupoEncalhe) ")
        .append(" as 'venda', ")
        .append(" coalesce(lan.REPARTE,0) as 'reparte', ")
        .append(" lan.DATA_LCTO_DISTRIBUIDOR  as 'dataLancamento', ")
        .append(" lan.DATA_REC_DISTRIB as 'dataRecolhimento', ")
        .append(" tiposeg.DESCRICAO as 'segmento' ")
        
        .append(" from produto pro ")
        .append("  join produto_edicao ped on pro.ID = ped.PRODUTO_ID ")
        .append(" left join tipo_segmento_produto tiposeg ON tiposeg.ID = pro.TIPO_SEGMENTO_PRODUTO_ID ")
        .append(" left join tipo_classificacao_produto tipoclas ON tipoclas.ID = ped.tipo_classificacao_produto_id ")
        .append(" left join brinde bri ON bri.ID = ped.BRINDE_ID  ")
        .append(" left join editor edi on edi.id = pro.editor_id ")
        .append(" join pessoa pes2 on pes2.id = edi.JURIDICA_ID ")
        .append(" left join lancamento lan on lan.PRODUTO_EDICAO_ID = ped.ID  ")
        .append(" left join estoque_produto est on est.PRODUTO_EDICAO_ID = ped.ID  ")
        .append(" where  1=1 ");
        
        if(filtro.getCodigoProduto() !=null && filtro.getCodigoProduto() != ""){
            sql.append(" and pro.codigo_icd = " ).append(filtro.getCodigoProduto().trim());
        }
        
        if(filtro.getClassificacaoProduto()!=null && filtro.getClassificacaoProduto()!=""){
            sql.append(" and upper(tipoclas.descricao) = upper('").append(filtro.getClassificacaoProduto().trim()).append("')");
        }
        
        if(filtro.getSegmento()!=null && filtro.getSegmento()!=""){
            sql.append(" and upper(tiposeg.DESCRICAO) = upper('").append(filtro.getSegmento().trim()).append("')");
        }
        
        if(filtro.getBrinde()!=null && filtro.getBrinde()!=""){
            sql.append(" and upper(bri.DESCRICAO_BRINDE) = upper('").append(filtro.getBrinde().trim()).append("')");
        }
        
        if(filtro.getFaixaPrecoDe()!=null && filtro.getFaixaPrecoDe()!=""){
            sql.append(" and ped.PRECO_VENDA >=" ).append(filtro.getFaixaPrecoDe().trim()).append("");
        }
        
        if(filtro.getFaixaPrecoAte()!=null && filtro.getFaixaPrecoAte()!=""){
            sql.append(" and ped.PRECO_VENDA <=" ).append(filtro.getFaixaPrecoAte().trim()).append("");
        }
        
        
        //tipo pesquisa publicacao
        if(filtro.getNomeProduto() !=null && filtro.getNomeProduto().trim()!=""){
            if(filtro.getOpcaoFiltroPublicacao()){
                //exato
                sql.append(" and upper(pro.nome) = ").append(" upper ('").append(filtro.getNomeProduto()).append("')");//exato
            }else{
                //contem
                sql.append(" and upper(pro.nome) like ").append(" upper ('%").append(filtro.getNomeProduto()).append("%')");//contem
            }
            
        }
        //tipo pesquisa editor
        if(filtro.getNomeEditor()!=null && filtro.getNomeEditor()!="")	{
            if(filtro.getOpcaoFiltroPublicacao()){
                //exato
                sql.append(" and upper(pes2.NOME_FANTASIA) =").append(" upper('").append(filtro.getNomeEditor()).append("')");
            }else{
                //contem
                sql.append(" and upper(pes2.NOME_FANTASIA) like").append(" upper('%").append(filtro.getNomeEditor()).append("%')");
            }
            
        }
        
        //chamada de capa
        if(filtro.getChamadaCapa()!=null && filtro.getChamadaCapa()!=""){
            if(filtro.getOpcaoFiltroPublicacao()){
                //exato
                sql.append(" and upper(ped.CHAMADA_CAPA) =").append(" upper('").append(filtro.getChamadaCapa()).append("')");
            }else{
                //contem
                sql.append(" and upper(ped.CHAMADA_CAPA) like").append(" upper('%").append(filtro.getChamadaCapa()).append("%')");
            }
            
        }
        
        sql.append(this.ordenarConsultaCaracteristicaDistribuicaoDetalhe(filtro));
        
        final Query  query = getSession().createSQLQuery(sql.toString());
        query.setParameter("grupoEncalhe", GrupoMovimentoEstoque.RECEBIMENTO_ENCALHE.name());
        query.setResultTransformer(new AliasToBeanResultTransformer(CaracteristicaDistribuicaoDTO.class));
        configurarPaginacaoPesquisaDetalhe(filtro,query);
        
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
    
    private void configurarPaginacaoPesquisaDetalhe(final FiltroConsultaCaracteristicaDistribuicaoDetalheDTO dto,final Query query) {
        final PaginacaoVO paginacao = dto.getPaginacao();
        
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
                sql.append(" and upper(pes2.NOME_FANTASIA) =").append(" upper('").append(filtro.getNomeEditor()).append("')");
            }else{
                //contem
                sql.append(" and upper(pes2.NOME_FANTASIA) like").append(" upper('%").append(filtro.getNomeEditor()).append("%')");
            }
            
        }
        
        final Query  query = getSession().createSQLQuery(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(CaracteristicaDistribuicaoSimplesDTO.class));
        configurarPaginacaoPesquisaSimples(filtro,query);
        return query.list();
    }
    
    
}
