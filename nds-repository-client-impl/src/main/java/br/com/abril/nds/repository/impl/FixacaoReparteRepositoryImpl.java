package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;
import br.com.abril.nds.model.distribuicao.FixacaoReparte;
import br.com.abril.nds.model.distribuicao.FixacaoRepartePdv;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FixacaoRepartePdvRepository;
import br.com.abril.nds.repository.FixacaoReparteRepository;
import br.com.abril.nds.repository.TipoClassificacaoProdutoRepository;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * 
 * FixacaoReparte
 */

@Repository
public class FixacaoReparteRepositoryImpl extends  AbstractRepositoryModel<FixacaoReparte, Long> implements FixacaoReparteRepository {
    
    @Autowired
    private FixacaoRepartePdvRepository fixacaoRepartePdvRepository;
    
    @Autowired
    private TipoClassificacaoProdutoRepository tipoClassificacaoProdutoRepository;
    
    public FixacaoReparteRepositoryImpl() {
        super(FixacaoReparte.class);
    }
    
    @Override
    public FixacaoReparte buscarPorId(final Long id){
        return super.buscarPorId(id);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FixacaoReparteDTO> obterFixacoesRepartePorProduto(final FiltroConsultaFixacaoProdutoDTO produto) {
        
        final boolean isClassificacaoPreenchida = NumberUtils.toInt(produto.getClassificacaoProduto()) > 0;
        
        final StringBuilder sql = new StringBuilder("");
        
        sql.append(" select ")
        .append(" f.id as id, ")
        .append(" f.qtdeExemplares as qtdeExemplares,")
        .append(" f.qtdeEdicoes as qtdeEdicoes,")
        .append(" f.dataHora as dataHora,")
        .append(" f.edicaoInicial as edicaoInicial,")
        .append(" f.edicaoFinal as edicaoFinal,")
        .append(" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas,")
        .append(" f.cotaFixada.numeroCota as cotaFixada,")
        .append(" f.cotaFixada.id as cotaFixadaId,")
        .append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '')  as nomeCota,")
        .append(" coalesce(classificacao.descricao, '') as classificacaoProduto,")
        .append(" classificacao.id as classificacaoProdutoId,")
        .append(" f.usuario.login as usuario,")
        .append(" f.codigoICD as codigoProduto,")
        .append(" count(pdv.id) as qtdPdv")
        
        .append(" from ")
        .append(" FixacaoReparte f ")
        .append(" left join f.classificacaoProdutoEdicao as classificacao ")
        .append(" left join f.cotaFixada.pessoa as pessoa ")
        .append(" left join f.cotaFixada.pdvs as pdv ")
        
        .append(" where ");
        
        sql.append(" f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
        
        if(isClassificacaoPreenchida){
            sql.append(" and f.classificacaoProdutoEdicao.id = :classificacaoProduto");
        }
        
        sql.append(	" and f.codigoICD = :codigoICD ");
        
        sql.append(" GROUP BY f.id ");
        sql.append(" order by f.cotaFixada.numeroCota asc ");
        
        final Query query = getSession().createQuery(sql.toString());
        query.setParameter("codigoICD", produto.getCodigoProduto());
        
        if(isClassificacaoPreenchida){
            query.setParameter("classificacaoProduto", Long.parseLong(produto.getClassificacaoProduto()));
        }
        query.setParameter("tipoCota", TipoDistribuicaoCota.CONVENCIONAL);
        
        query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
        configurarPaginacao(produto,query);
        return query.list();
        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<FixacaoReparteDTO> obterFixacoesRepartePorCota(final FiltroConsultaFixacaoCotaDTO cota) {
        final boolean isNumeroCotaPreenchido = cota != null && cota.getCota() != null;
        final boolean isNomeCotaPreenchido = cota != null && StringUtils.isNotEmpty(cota.getNomeCota());
        
        final StringBuilder sql = new StringBuilder("");
        
        sql.append(" select ")
        .append(" f.id as id, ")
        .append(" f.qtdeExemplares as qtdeExemplares, ")
        .append(" f.qtdeEdicoes as qtdeEdicoes, ")
        .append(" f.edicoesAtendidas as edicoesAtendidas, ")
        .append(" f.edicaoInicial as edicaoInicial, ")
        .append(" f.edicaoFinal as edicaoFinal, ")
        .append(" f.dataHora as dataHora , ")
        .append(" f.edicaoFinal - f.edicaoInicial as edicoesAtendidas, ")
        .append(" f.cotaFixada.numeroCota as cotaFixada, ")
        .append(" f.cotaFixada.id as cotaFixadaId, ")
        .append(" f.codigoICD as codigoProduto, ")
        .append(" (select p.nome from Produto p where p.codigo = (select min(p1.codigo) from Produto p1 where p1.codigoICD = f.codigoICD)) as nomeProduto, ")
        .append(" coalesce(classificacao.descricao,'') as classificacaoProduto, ")
        .append(" classificacao.id as classificacaoProdutoId, ")
        .append(" f.usuario.login as usuario, ")
        .append(" f.manterFixa as manterFixa, ")
        .append(" count(pdv.id) as qtdPdv ")
        
        .append(" from ")
        .append(" FixacaoReparte f ")
        .append(" left join f.classificacaoProdutoEdicao as classificacao  ")
        .append(" inner join f.cotaFixada as cota ")
        .append(" inner join cota.pdvs as pdv ")
        .append(" where ");
        
        if(isNomeCotaPreenchido){
            sql.append(" (upper(cota.pessoa.nome) like  upper(:nomeCota) ")
            .append(" or upper(cota.pessoa.nomeFantasia) like  upper(:nomeCota) ")
            .append(" or upper(cota.pessoa.razaoSocial) like  upper(:nomeCota) ) and ");
            
        }
        
        sql.append(" f.cotaFixada.tipoDistribuicaoCota = :tipoCota ");
        
        if(isNumeroCotaPreenchido ){
            sql.append(	" and cota.numeroCota = :numeroCota ");
        }
        
        sql.append(" GROUP BY f.id ");
        sql.append(" order by nomeProduto asc ");
        
        final Query query = getSession().createQuery(sql.toString());
        
        if(isNomeCotaPreenchido){
            query.setParameter("nomeCota", "%"+ cota.getNomeCota() +"%");
        }
        
        if(isNumeroCotaPreenchido ){
            query.setParameter("numeroCota", Integer.valueOf(cota.getCota()));
        }
        query.setParameter("tipoCota", TipoDistribuicaoCota.CONVENCIONAL);
        query.setResultTransformer(new AliasToBeanResultTransformer(FixacaoReparteDTO.class));
        configurarPaginacao(cota, query);
        return query.list();
    }
    
    private void configurarPaginacao(final FiltroDTO dto, final Query query) {
        
        final PaginacaoVO paginacao = dto.getPaginacao();
        
        if(paginacao==null) {
            return;
        }
        
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
    
    @Override
    public FixacaoReparte buscarPorProdutoCotaClassificacao(final Cota cota, final String codigoICD, final TipoClassificacaoProduto classificacaoProduto) {
        
        final StringBuilder sql = new StringBuilder();
        
        sql.append(" from ");
        sql.append(" FixacaoReparte f ");
        sql.append(" where f.cotaFixada = :cotaSelecionada ");
        sql.append(" and f.codigoICD = :codigoICD  ");
        if (classificacaoProduto != null) {
            sql.append(" and f.classificacaoProdutoEdicao = :classificacaoProduto ");
        }
        
        final Query query  = getSession().createQuery(sql.toString());
        query.setParameter("cotaSelecionada",  cota);
        query.setParameter("codigoICD", codigoICD);
        if (classificacaoProduto != null) {
            query.setParameter("classificacaoProduto", classificacaoProduto);
        }
        
        return (FixacaoReparte)query.uniqueResult();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<FixacaoReparte> buscarPorCota(final Cota cota){
        final StringBuilder sql = new StringBuilder("");
        
        sql.append(" from ");
        sql.append(" FixacaoReparte f ");
        sql.append(" where f.cotaFixada = :cotaSelecionada ");
        
        final Query query  = getSession().createQuery(sql.toString());
        query.setParameter("cotaSelecionada",  cota);
        
        
        return query.list();
    }
    
    @Override
    public void removerPorCota(final Cota cota){
        final StringBuilder sql = new StringBuilder("");
        
        sql.append("delete from ");
        
        sql.append(" FixacaoReparte f ");
        
        sql.append(" where f.cotaFixada = :cotaSelecionada ");
        
        final Query query  = getSession().createQuery(sql.toString());
        query.setParameter("cotaSelecionada",  cota);
        
    }
    
    @Override
    public boolean isFixacaoExistente(final FixacaoReparteDTO fixacaoReparteDTO) {
        final StringBuilder sql = new StringBuilder("");
        
        sql.append(" from ")
        .append(" FixacaoReparte f ")
        .append(" where f.cotaFixada.numeroCota = :cotaSelecionada ")
        .append(" and f.codigoICD = :produtoSelecionado  ");
        
        final Query query  = getSession().createQuery(sql.toString());
        query.setParameter("cotaSelecionada",  fixacaoReparteDTO.getCotaFixada());
        query.setParameter("produtoSelecionado", fixacaoReparteDTO.getProdutoFixado());
        
        return query.list().size() > 0;
    }
    
    @Override
    public void gerarCopiaPorCotaFixacaoReparte(final List<FixacaoReparteDTO> fixacaoCotaOrigemList, final Usuario usuarioLogado) {
        
        final StringBuilder hql = new StringBuilder("");
        
        hql.append(" INSERT INTO fixacao_reparte ")
        .append(" (DATA_HORA, ED_FINAL, ED_INICIAL, QTDE_EDICOES, QTDE_EXEMPLARES, ID_COTA, CODIGO_ICD, ID_CLASSIFICACAO_EDICAO, ID_USUARIO, MANTER_FIXA) VALUES ") ;
        
        final List<String> valuesAppendList = new ArrayList<String>();
        for (final FixacaoReparteDTO frDTO : fixacaoCotaOrigemList) {
            valuesAppendList.add(" (now(), "
                    + frDTO.getEdicaoFinal() + ", "
                    + frDTO.getEdicaoInicial() + ", "
                    + frDTO.getQtdeEdicoes() + ", "
                    + frDTO.getQtdeExemplares() + ", "
                    + frDTO.getCotaFixadaId() + ", "
                    + frDTO.getCodigoProduto() + ", "
                    + frDTO.getClassificacaoProdutoId() + ", "
                    + usuarioLogado.getId() + ", "
                    + frDTO.isManterFixa() + ") ");
        }
        
        hql.append(StringUtils.join(valuesAppendList,","));
        
        final Query query = getSession().createSQLQuery(hql.toString());
        query.executeUpdate();
    }
    
    @Override
    public void gerarCopiaPorProdutoFixacaoReparte(final List<FixacaoReparteDTO> fixacaoProdutoOrigem, final Usuario usuarioLogado) {
        
        for (final FixacaoReparteDTO frDTO : fixacaoProdutoOrigem) {
            if(frDTO.getCotaFixadaId()==null){
                throw new RuntimeException("Erro na consulta de Fixação de Repartes por produto");
            }
        }
        
        
        for (final FixacaoReparteDTO frDTO : fixacaoProdutoOrigem) {
            final FixacaoReparte fr = new FixacaoReparte();
            fr.setRepartesPDV(new ArrayList<FixacaoRepartePdv>());
            fr.setDataHora(GregorianCalendar.getInstance().getTime());
            
            fr.setEdicaoFinal(frDTO.getEdicaoFinal());
            fr.setEdicaoInicial(frDTO.getEdicaoInicial());
            fr.setQtdeEdicoes(frDTO.getQtdeEdicoes());
            fr.setManterFixa(frDTO.isManterFixa());
            fr.setQtdeExemplares(frDTO.getQtdeExemplares());
            
            final Cota cotaFixada = new Cota();
            
            cotaFixada.setId(frDTO.getCotaFixadaId());
            
            fr.setCodigoICD(frDTO.getCodigoProduto());
            fr.setClassificacaoProdutoEdicao(getClassificacaoProduto(frDTO.getClassificacaoProdutoId()));
            fr.setCotaFixada(cotaFixada);
            fr.setUsuario(usuarioLogado);
            
            final List<FixacaoRepartePdv> fixacaoRepartePdvList = fixacaoRepartePdvRepository.obterFixacaoRepartePdvPorFixacaoReparteId(frDTO.getId());
            
            for (final FixacaoRepartePdv fixacaoRepartePdv : fixacaoRepartePdvList) {
                
                final FixacaoRepartePdv newFixacaoRepartePdv = new FixacaoRepartePdv();
                newFixacaoRepartePdv.setFixacaoReparte(fr);
                
                newFixacaoRepartePdv.setPdv(fixacaoRepartePdv.getPdv());
                newFixacaoRepartePdv.setRepartePdv(fixacaoRepartePdv.getRepartePdv());
                fr.getRepartesPDV().add(newFixacaoRepartePdv);
                
            }
            
            getSession().save(fr);
        }
        
    }
    
    private TipoClassificacaoProduto getClassificacaoProduto(final Long classificacaoProdutoId) {
        if (classificacaoProdutoId != null) {
            return tipoClassificacaoProdutoRepository.buscarPorId(classificacaoProdutoId);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void execucaoQuartz() {
        
        final Query q = getSession().createQuery("from FixacaoReparte where datediff(now(),dataHora)> 365 and manterFixa = 0 ");
        final List<FixacaoReparte> listaParaApagar = q.list();
        for (final FixacaoReparte fixacaoReparte : listaParaApagar) {
            getSession().delete(fixacaoReparte);
        }
        
    }
    
}
