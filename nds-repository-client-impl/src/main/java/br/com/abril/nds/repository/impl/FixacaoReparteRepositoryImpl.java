package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.FixacaoReparteDTO;
import br.com.abril.nds.dto.verificadorFixacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFixacaoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
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
        .append(" f.dataHora as data,")
        .append(" TIME(f.dataHora) as hora,")
        .append(" f.edicaoInicial as edicaoInicial,")
        .append(" f.edicaoFinal as edicaoFinal, ")
        .append(" f.edicoesAtendidas as edicoesAtendidas, ")
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
        
		if ((produto.getPaginacao() != null) && (produto.getPaginacao().getSortColumn() != null)) {
			
			if(produto.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO")){
				sql.append(" ORDER BY cotaFixada ");
			}else{
				sql.append(" ORDER BY ");
				sql.append(" "+produto.getPaginacao().getSortColumn());
			}
			
			sql.append(" "+produto.getPaginacao().getSortOrder());
			
		} else {
			sql.append(" ORDER BY cotaFixada ");
		}
        
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
        .append(" f.dataHora as data, ")
        .append(" TIME(f.dataHora) as hora , ")
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
        
        if((cota.getPaginacao()!=null) && (cota.getPaginacao().getSortColumn()!=null)){
        	
        	if(cota.getPaginacao().getSortColumn().equalsIgnoreCase("CODIGOPRODUTO")){
				sql.append(" ORDER BY CAST(f.codigoICD as int) ");
        	}else{
	        	if((cota.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO"))){
	        		sql.append(" ORDER BY nomeProduto ");
	        	}else{
	        		sql.append(" ORDER BY ");
	        		sql.append(" "+cota.getPaginacao().getSortColumn());
	        	}
        	}
        	
        	sql.append(" "+cota.getPaginacao().getSortOrder());
        	
        }else{
        	sql.append(" ORDER BY nomeProduto ");
        }
        
        
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
       
    try {
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
    } catch ( org.hibernate.NonUniqueResultException nu ) {
    	  throw new ValidacaoException(TipoMensagem.ERROR, "Fixacao reparte duplicado para cota="+cota.getNumeroCota() +
    			     " e CODIGO_ICD="+codigoICD);
    	
    }
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
        
        query.executeUpdate();
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> historicoDeEdicoesParaCalcularEdicoesAtendidas(Long lancamentoID, String codigoICD, Integer qtdEdicoes, Long idClassificacao) {
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" SELECT ");
		sql.append(" 	PE.NUMERO_EDICAO ");
		sql.append(" FROM PRODUTO_EDICAO PE ");
		sql.append(" 	JOIN produto P ON PE.PRODUTO_ID = P.ID  ");
		sql.append(" 	JOIN lancamento l ON PE.ID = l.PRODUTO_EDICAO_ID  ");
		sql.append(" WHERE P.codigo_icd = :codigoICD  ");
		sql.append(" 	   AND l.STATUS in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', 'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO') ");
		sql.append(" 	   AND (l.DATA_LCTO_DISTRIBUIDOR>(select lct.DATA_LCTO_DISTRIBUIDOR from lancamento lct where id = :LancamentoID)) ");
		sql.append(" 	   AND PE.TIPO_CLASSIFICACAO_PRODUTO_ID = :classificacao");
		sql.append(" group by PE.ID ");
		sql.append(" order by l.DATA_LCTO_DISTRIBUIDOR asc ");
		sql.append(" limit :limite ");
		
		final Query query  = getSession().createSQLQuery(sql.toString());
        
		query.setParameter("codigoICD",  codigoICD);
        query.setParameter("LancamentoID",  lancamentoID);
        query.setParameter("classificacao", idClassificacao);
        query.setParameter("limite", qtdEdicoes);
        
        
        
        return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<verificadorFixacaoDTO> obterLcmtsDosProdutosQuePossueFixacao(List<BigInteger> lancamentosDoDia) {

		StringBuilder sql = new StringBuilder("");
		
		sql.append(" select ");
		sql.append(" 	pd.codigo_icd as codICDFixacao, ");
		sql.append(" 	lc.id as idLancamento ");
		sql.append(" from lancamento lc  ");
		sql.append(" 	join produto_edicao pe on lc.PRODUTO_EDICAO_ID = pe.ID  ");
		sql.append(" 	join produto pd on pe.PRODUTO_ID = pd.ID  ");
		sql.append(" 	join fixacao_reparte fr on fr.codigo_icd = pd.codigo_icd  ");
		sql.append(" where lc.ID in (:lancamentosDoDia)  ");
		sql.append(" and lc.STATUS in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', 'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO') ");
		sql.append(" group by lc.ID ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameterList("lancamentosDoDia", lancamentosDoDia);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(verificadorFixacaoDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FixacaoReparte> obterFixacoesParaOsProdutosDosLancamentos(List<String> listaCodICDsLacamentos) {
		
			StringBuilder hql = new StringBuilder("");

			hql.append(" select fixacao ")
					.append(" from FixacaoReparte fixacao ")
					.append(" where fixacao.codigoICD in (:listaCodICD) order by edicaoFinal, edicoesAtendidas ");

			Query query = getSession().createQuery(hql.toString());

			query.setParameterList("listaCodICD", listaCodICDsLacamentos);
			
		return (List<FixacaoReparte>) query.list();
	}		

	@Override
	public BigInteger obterNumeroEdicaoPeloLancamentoID(Long idLancamento) {
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" select  ");
		sql.append(" 	pe.NUMERO_EDICAO  ");
		sql.append(" from lancamento lc  ");
		sql.append(" join produto_edicao pe on lc.PRODUTO_EDICAO_ID = pe.ID  ");
		sql.append(" where lc.ID in (:idLancamento)  ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idLancamento", idLancamento);
		
		return (BigInteger)query.uniqueResult();
	}

	@Override
	public BigInteger qntdEdicoesPosterioresAolancamento(String codigoICD, Date dataFixaCadastroFixacao) { 

		StringBuilder sql = new StringBuilder("");
		
		sql.append("  SELECT COUNT(T.lcId)  ");
		sql.append("  	FROM (  ");
		sql.append("  			select ");
		sql.append("  				lc.id AS lcId ");
		sql.append("  			from lancamento lc ");
		sql.append(" 				join produto_edicao pe on lc.PRODUTO_EDICAO_ID = pe.ID  ");
		sql.append(" 				join produto pd on pe.PRODUTO_ID = pd.ID  ");
		sql.append(" 			where  ");
		sql.append(" 				pd.codigo_icd = (:codigoICD)  ");
		sql.append(" 				and lc.DATA_LCTO_DISTRIBUIDOR>=:dataFixa and lc.DATA_LCTO_DISTRIBUIDOR<:dataCabalistica  ");
		sql.append(" 			  	and lc.STATUS in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', 'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO') ");
		sql.append(" 			group by pe.ID  ");
		sql.append(" 			order by lc.DATA_LCTO_DISTRIBUIDOR	  ");
		sql.append(" 		) T  ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataFixa", dataFixaCadastroFixacao);
		query.setParameter("codigoICD", codigoICD);
		query.setParameter("dataCabalistica", "3000/01/01");
	
		return (BigInteger) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BigInteger> obterListaLancamentos(Date dataOperacao) {
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" select lc.ID from lancamento lc join expedicao ex on lc.EXPEDICAO_ID = ex.ID where Date(ex.DATA_EXPEDICAO)= :data ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("data", dataOperacao);
		
		return (List<BigInteger>) query.list();
	}

	@Override
	public BigInteger obterQtdDeEdicoesNoRanger(String codigoICD, Integer edInicial, Integer edFinal) {
		
		StringBuilder sql = new StringBuilder("");
		
		sql.append(" SELECT ");
		sql.append(" 	count(pe.NUMERO_EDICAO) ");
		sql.append(" FROM produto_edicao pe ");
		sql.append(" 	JOIN produto pd on pe.PRODUTO_ID = pd.ID ");
		sql.append(" 	JOIN lancamento lc on lc.PRODUTO_EDICAO_ID = pe.ID ");
		sql.append(" WHERE pd.codigo_icd = :icd ");
		sql.append(" 	   AND pe.NUMERO_EDICAO >= :edInicial ");
		sql.append(" 	   AND pe.NUMERO_EDICAO < :edFinal ");
		sql.append(" 	   AND lc.STATUS in ('EXPEDIDO', 'EM_BALANCEAMENTO_RECOLHIMENTO', 'BALANCEADO_RECOLHIMENTO', 'EM_RECOLHIMENTO', 'RECOLHIDO', 'FECHADO') ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("icd", codigoICD);
		query.setParameter("edInicial", edInicial);
		query.setParameter("edFinal", edFinal);
		
		return (BigInteger) query.uniqueResult();
	}

	@Override
	public void atualizarQtdeExemplares(Long fixacaoID, Integer qtdeExemplares, Date data, Usuario usuario) {
		
		StringBuilder statement = new StringBuilder();
		
		statement.append(" update fixacao_reparte ")
				 .append(" set qtde_exemplares = :qtdeExemplares, ")
				 .append(" data_hora = :dataHora, ")
				 .append(" id_usuario = :idUsuario ")
				 .append(" where id = :fixacaoID ");
		
		SQLQuery query = this.getSession().createSQLQuery(statement.toString());
		
		query.setParameter("qtdeExemplares", qtdeExemplares);
		query.setParameter("dataHora", data);
		query.setParameter("idUsuario", usuario.getId());
		query.setParameter("fixacaoID", fixacaoID);
		
		query.executeUpdate();
	}
}
