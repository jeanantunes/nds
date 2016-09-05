package br.com.abril.nds.repository.impl;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.dto.filtro.FiltroBoletoAvulsoDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BoxRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.Box}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoxRepositoryImpl extends AbstractRepositoryModel<Box,Long> implements BoxRepository {

	/**
	 * Construtor padrão
	 */
	public BoxRepositoryImpl() {
		super(Box.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public List<Box> obterListaBox(String nomeBox, List<TipoBox> tipoBox) {
		
		Criteria criteria = addRestrictions(null, tipoBox);
		if (!StringUtil.isEmpty(nomeBox)) {
		    criteria.add(Restrictions.ilike("nome", nomeBox, MatchMode.START));
		}
		criteria.addOrder(Order.asc("codigo"));
		
		criteria.setCacheable(true);
		
		return criteria.list();
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
    public List<Box> obterListaBox(List<TipoBox> tipo) {
        return obterListaBox(null, tipo);
    }
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#obterBoxUsuario(java.lang.Long, br.com.abril.nds.model.cadastro.TipoBox)
	 */
	@SuppressWarnings("unchecked")
	public List<Box> obterBoxUsuario(Long idUsuario, TipoBox tipoBox) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select parametroUsuarioBox.box from ParametroUsuarioBox parametroUsuarioBox ");
		
		hql.append(" where ");
		
		hql.append(" parametroUsuarioBox.usuario.id = :idUsuario ");
		
		hql.append(" and parametroUsuarioBox.box.tipoBox = :tipoBox ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idUsuario", idUsuario);

		query.setParameter("tipoBox", tipoBox);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> obterBoxPorProduto(String codigoProduto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select box from Box box ")
			.append(" join box.cotas cota ")
			.append(" join cota.estudoCotas estudoCota ")
			.append(" join estudoCota.estudo estudo ")
			.append(" join estudo.produtoEdicao produtoEdicao ")
			.append(" join produtoEdicao.produto produto ");
			
	    if(codigoProduto!= null && !codigoProduto.isEmpty()){
			hql.append(" where ")
	    	.append(" produto.codigo = :codigoProduto");
	    }
	    
		hql.append(" group by box.codigo ")
			.append(" order by box.codigo ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
	    if(codigoProduto!= null && !codigoProduto.isEmpty()){
	    	query.setParameter("codigoProduto", codigoProduto);
	    }
			
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> busca(Integer codigoBox, TipoBox tipoBox, String  orderBy , Ordenacao ordenacao, Integer initialResult, Integer maxResults) {
		
		Criteria criteria = addRestrictions(codigoBox, (tipoBox != null ? Arrays.asList(tipoBox) : null));
		
		if(Ordenacao.ASC ==  ordenacao) {
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		if(initialResult != null) {
			
			criteria.setFirstResult(initialResult);
			
		}
		
		if(maxResults != null) {
			
			criteria.setMaxResults(maxResults);
			
		}
		
		return criteria.list();
	}
	
	
	@Override
	public Long quantidade(Integer codigoBox, TipoBox tipoBox ) {
		
		Criteria criteria = addRestrictions(codigoBox, (tipoBox != null ? Arrays.asList(tipoBox) : null));
		criteria.setProjection(Projections.rowCount());
		
		return (Long)criteria.list().get(0);
	}
	
	/**
	 * Adiciona as restricoes a consulta.
	 * @param codigoBox Codigo do box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @return
	 */
	private Criteria addRestrictions(Integer codigoBox, List<TipoBox> tipoBox ) {
		
		Criteria criteria =  getSession().createCriteria(Box.class);	
		
		if(codigoBox != null ) {
			criteria.add(Restrictions.eq("codigo", codigoBox));
		}
		
		if(tipoBox != null && !tipoBox.isEmpty()) {
			criteria.add(Restrictions.in("tipoBox", tipoBox));
		}
		
		return criteria;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#hasCodigo(java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean hasCodigo(Integer codigoBox, Long id){
		Criteria criteria =  getSession().createCriteria(Box.class);	
		
		criteria.add(Restrictions.eq("codigo", codigoBox));
		if(id != null){
			criteria.add(Restrictions.ne("id", id));
		}
		
		criteria.setProjection(Projections.rowCount());
		Long qtd = (Long)criteria.list().get(0);
		return qtd > 0;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#obtemCotaRotaRoteiro(long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long idBox, String sortname, String sortorder){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
				
		hql.append(" cota.numeroCota as numeroCota,");
		
		hql.append(" case pessoa.class ");
		
		hql.append("       when 'F' then pessoa.nome ");
				
		hql.append("       when 'J' then pessoa.razaoSocial end  as nomeCota,");
		
		hql.append(" rota.descricaoRota as rota,");
		
		hql.append(" roteiro.descricaoRoteiro as roteiro ");

		hql.append(" from Roteirizacao roteirizacao ");
		
		hql.append(" join roteirizacao.box box ");
		
		hql.append(" join roteirizacao.roteiros roteiro ");
		
		hql.append(" join roteiro.rotas rota ");

		hql.append(" join rota.rotaPDVs rotaPDV ");

		hql.append(" join rotaPDV.pdv pdv ");
		
		hql.append(" join pdv.cota cota ");
		
		hql.append(" join cota.pessoa pessoa ");

		hql.append(" where box.id = :id ");
		
		hql.append(" group by roteiro.id, rota.id, cota.id ");
		
		sortorder = sortorder!=null && !sortorder.isEmpty()?sortorder:"asc";
		
		sortname = sortname!=null && !sortname.isEmpty()?sortname:"numeroCota";
		
		hql.append(" order by ");
		
		hql.append(sortname + " " + sortorder);
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setLong("id", idBox);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaRotaRoteiroDTO.class));
		
		return query.list();
	}

	@Override
	public Integer obterCodigoBoxPadraoUsuario(Long idUsuario) {
		
		StringBuilder hql = new StringBuilder("select p.box.codigo ");
		hql.append(" from ParametroUsuarioBox p ")
		   .append(" where p.usuario.id = :idUsuario");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idUsuario", idUsuario);
		
		return (Integer) query.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#hasRoteiros(long)
	 */
	@Override
	public boolean hasRoteirosVinculados(long idBox){
		Criteria criteria = getSession().createCriteria(Roteirizacao.class);
		
		criteria.createCriteria("box").add(Restrictions.idEq(idBox));
		
		criteria.setProjection(Projections.rowCount());
		
		Long qtd = (Long)criteria.list().get(0);
		return qtd > 0;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#hasCotas(long)
	 */
	@Override
	public boolean hasCotasVinculadas(long idBox){
		Criteria criteria = getSession().createCriteria(Cota.class);
		
		criteria.createCriteria("box").add(Restrictions.idEq(idBox));
		
		criteria.setProjection(Projections.rowCount());
		
		Long qtd = (Long)criteria.list().get(0);
		return qtd > 0;
		
	}

	/**
	 * Obtém Lista de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return List<Cota>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota) {
        
	    StringBuilder hql = new StringBuilder("select cota ");
	    
	    Query query = this.getSession().createQuery(
	            this.obterQueryCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota, hql));
		
		this.setParametrosCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota, query);
		
		return query.list();
	}

    private String obterQueryCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota, StringBuilder hql) {
        
	    hql.append(" from PDV pdv ")
	       .append(" join pdv.cota cota ")
	       .append(" join pdv.rotas rotaPDV ")
	       .append(" join rotaPDV.rota rota ")
	       .append(" join rota.roteiro roteiro ")
	       .append(" join roteiro.roteirizacao roteirizacao ")
	       .append(idBox != null && idBox < 1 ? " left" : "")
	       .append(" join roteirizacao.box box ");
	    
	    boolean indWhere = false;
	    
	    if (idRota != null && idRota != 0){
	        
	        hql.append(indWhere ? " and " : " where ")
	           .append(" rota.id = :idRota ");
	        
	        indWhere = true;
	    }

        if (idRoteiro != null && idRoteiro != 0){
            
            hql.append(indWhere ? " and " : " where ")
               .append(" roteiro.id = :idRoteiro ");
            indWhere = true;
        }
        
        if (idBox != null){
            
            hql.append(indWhere ? " and " : " where ");
            
            if (idBox < 1){
                
                hql.append(" roteirizacao.box is null ");
            } else {
                
                hql.append(" box.id = :idBox ");
            }
        }
        
        hql.append("order by box.codigo, roteiro.ordem, rota.ordem, cota.numeroCota ");
    
        return hql.toString();
    }
    
    private void setParametrosCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota, Query query) {
        
        if (idRota != null && idRota != 0){
            
            query.setParameter("idRota", idRota);
        }
        
        if (idRoteiro != null && idRoteiro != 0){
            
            query.setParameter("idRoteiro", idRoteiro);
        }
        
        if (idBox != null && idBox > 0){
            
            query.setParameter("idBox", idBox);
        }
    }

    private void setParametrosCotasBoletoAvulso(FiltroBoletoAvulsoDTO boletoAvulso, Query query) {
        
        if (boletoAvulso.getIdRota() != null && boletoAvulso.getIdRota() != 0){
            query.setParameter("idRota", boletoAvulso.getIdRota());
        }
        
        if (boletoAvulso.getIdRoteiro() != null && boletoAvulso.getIdRoteiro() != 0){
            
            query.setParameter("idRoteiro", boletoAvulso.getIdRoteiro());
        }
        
        if (boletoAvulso.getIdBox() != null && boletoAvulso.getIdBox() > 0){
            
            query.setParameter("idBox", boletoAvulso.getIdBox());
        }
        

        if (boletoAvulso.getIdRegiao() != null && boletoAvulso.getIdRegiao() != 0){
            
            query.setParameter("idRegiao", boletoAvulso.getIdRegiao());
        }
    }
    
    /**
	 * Obtém Quantidade de Cotas por Box, Rota e Roteiro
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @return Número de Cotas encontradas
	 */
	@Override
	public Long obterQuantidadeCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota) {
        
		StringBuilder hql = new StringBuilder("select count(cota.id) ");
		
		Query query = this.getSession().createQuery(this.obterQueryCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota, hql));
		
		this.setParametrosCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota, query);
		
		return (Long) query.uniqueResult();
	}

	@Override
	public Box obterBoxPorRota(Long rotaID) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT box FROM Box box ");
		sql.append(" JOIN box.roteirizacao roteirizacao ");
		sql.append(" JOIN roteirizacao.roteiros roteiro ");
		sql.append(" JOIN roteiro.rotas rota ");
		sql.append(" WHERE rota.id = :rotaID ");
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("rotaID", rotaID);
		query.setMaxResults(1);
		
		return (Box) query.uniqueResult();
	}

	@Override
	public Box obterBoxPorRoteiro(Long roteiroID) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT box FROM Box box ");
		sql.append(" JOIN box.roteirizacao roteirizacao ");
		sql.append(" JOIN roteirizacao.roteiros roteiro ");
		sql.append(" WHERE roteiro.id = :roteiroID ");
		
		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("roteiroID", roteiroID);
		query.setMaxResults(1);
		
		return (Box) query.uniqueResult();
	}

	/**
	 * Obtem lista de Box por intervalo de Código
	 * @param codigoBoxDe
	 * @param codigoBoxAte
	 * @return List<Box>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> obterBoxPorIntervaloCodigo(Integer codigoBoxDe, Integer codigoBoxAte) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT box FROM Box box ");
		
		sql.append(" WHERE box.codigo in (:codigosBox) ");
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameterList("codigosBox", Arrays.asList(codigoBoxDe, codigoBoxAte));
		
		return query.list();
	}

	/**
	 * Busca lista de Box por Rota
	 * @param rotaId
	 * @return List<Box>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> buscarBoxPorRota(Long rotaId) {
		
	    String hql  = " select box from Rota rota ";
	           hql += " join rota.roteiro roteiro ";
	           hql += " join roteiro.roteirizacao roteirizacao ";
	           hql += " join roteirizacao.box box ";
	           hql += " where rota.id = :rotaId ";
	           hql += " group by box.codigo ";
			
		Query query = getSession().createQuery(hql);
		
		query.setParameter("rotaId", rotaId);
		
		return query.list();
	}

	/**
	 * Busca lista de Box por Roteiro
	 * @param roteiroId
	 * @return List<Box>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> buscarBoxPorRoteiro(Long roteiroId) {
		
		String hql  = " select box from Rota rota ";
			   hql += " join rota.roteiro roteiro ";
               hql += " join roteiro.roteirizacao roteirizacao ";
		       hql += " join roteirizacao.box box ";
		       hql += " where roteiro.id = :roteiroId ";
		       hql += " group by box.codigo ";
		
        Query query = getSession().createQuery(hql);
	
	    query.setParameter("roteiroId", roteiroId);
	    
	    query.setCacheable(true);
	    
	    return query.list();
	}

	@Override
	public String obterDescricaoBoxPorCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select concat(b.codigo, ' - ', b.nome) ");
		hql.append(" from Cota c ")
		   .append(" join c.box b ")
		   .append(" where c.numeroCota = :numeroCota");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		return (String) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Box> obterBoxPorCodigo(Integer codigoBox) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT box FROM Box box ");
		
		sql.append(" WHERE box.codigo = :codigosBox ");
		
		Query query = this.getSession().createQuery(sql.toString());
		
		query.setParameter("codigosBox", codigoBox);
		
		return query.list();
	}

	@Override
	public Long obterIdBoxEspecial() {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT id FROM Box box ");
		
		sql.append(" WHERE box.tipo_box = :tipoBox ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("tipoBox", TipoBox.ESPECIAL.name());
		query.addScalar("id", StandardBasicTypes.LONG);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotasParaBoletoAvulso(FiltroBoletoAvulsoDTO boletoAvulso) {
        
	    StringBuilder hql = new StringBuilder("select distinct cota ");
	    
	    Query query = this.getSession().createQuery(this.obterQueryCotasBoletoAvulso(boletoAvulso, hql));
		
		this.setParametrosCotasBoletoAvulso(boletoAvulso, query);
		
		return query.list();
	}
	
	private String obterQueryCotasBoletoAvulso(FiltroBoletoAvulsoDTO boletoAvulso, StringBuilder hql) {
        
	    hql.append(" from PDV pdv ")
	       .append(" join pdv.cota cota ")
	       .append(" join pdv.rotas rotaPDV ")
	       .append(" join rotaPDV.rota rota ")
	       .append(" join rota.roteiro roteiro ")
	       .append(" join roteiro.roteirizacao roteirizacao ")
	       .append(" join roteirizacao.box box ")
	       .append(" where 1=1 "); 
	    
	    if (boletoAvulso.getIdRota() != null && boletoAvulso.getIdRota() != 0){
	    	hql.append(" and rota.id = :idRota ");
	    }

        if (boletoAvulso.getIdRoteiro() != null && boletoAvulso.getIdRoteiro() != 0){
        	hql.append(" and roteiro.id = :idRoteiro ");
        }
        
        if (boletoAvulso.getIdBox() != null){
            if (boletoAvulso.getIdBox() < 1){
                
                hql.append(" and roteirizacao.box is null ");
            } else {
                
                hql.append(" and box.codigo = :idBox ");
            }
        }
        
        if(boletoAvulso.getIdRegiao() != null) {
			hql.append(" AND cota.id in (SELECT ");
			hql.append(" c.id ");
			hql.append(" FROM RegistroCotaRegiao registroCotaRegiao ");
			hql.append(" INNER JOIN registroCotaRegiao.cota c ");
			hql.append(" INNER JOIN registroCotaRegiao.regiao regiao ");
			hql.append(" WHERE regiao.id = :idRegiao) ");
		}
        
        // hql.append(" and cota.numeroCota in ('100', '101', '102', '104') ");
        hql.append("order by box.codigo, roteiro.ordem, rota.ordem, cota.numeroCota ");
    
        return hql.toString();
    }
	
}