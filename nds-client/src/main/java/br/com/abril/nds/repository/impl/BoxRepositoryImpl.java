package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.TipoBox;
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
	public List<Box> obterListaBox(String nomeBox, TipoBox tipoBox) {
		
		Criteria criteria = addRestrictions(null,tipoBox);
		if (!StringUtil.isEmpty(nomeBox)) {
		    criteria.add(Restrictions.ilike("nome", nomeBox, MatchMode.START));
		}
		
		return criteria.list();
		
		
	}
	
    /**
     * {@inheritDoc} 
     */
	@Override
    public List<Box> obterListaBox(TipoBox tipo) {
        return obterListaBox(null, tipo);
    }
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#obterBoxUsuario(java.lang.Long, br.com.abril.nds.model.cadastro.TipoBox)
	 */
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
	public List<Box> busca(Integer codigoBox,TipoBox tipoBox, String  orderBy , Ordenacao ordenacao, Integer initialResult, Integer maxResults){
		
		Criteria criteria = addRestrictions(codigoBox, tipoBox);
		
		if(Ordenacao.ASC ==  ordenacao){
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
	public Long quantidade(Integer codigoBox,TipoBox tipoBox ){
		Criteria criteria = addRestrictions(codigoBox, tipoBox);
		criteria.setProjection(Projections.rowCount());
		
		
		return (Long)criteria.list().get(0);
	}
	
	/**
	 * Adiciona as restricoes a consulta.
	 * @param codigoBox Codigo do box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @return
	 */
	private Criteria addRestrictions(Integer codigoBox, TipoBox tipoBox ) {
		Criteria criteria =  getSession().createCriteria(Box.class);	
		
		if( codigoBox != null ){
			criteria.add(Restrictions.eq("codigo", codigoBox));
		}
		
		if(tipoBox != null){
			criteria.add(Restrictions.eq("tipoBox", tipoBox));
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
	@Override
	public List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long idBox){
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
				
		hql.append(" cota.numeroCota as numeroCota,");
		
		hql.append(" case pessoa.class ");
		
		hql.append("       when 'F' then pessoa.nome ");
				
		hql.append("       when 'J' then pessoa.razaoSocial end  as nomeCota,");
		
		hql.append(" rota.descricaoRota as rota,");
		
		hql.append(" roteiro.descricaoRoteiro as roteiro ");

		hql.append(" from Roteiro roteiro  " );
				
		hql.append(" join roteiro.rotas rota " );

		hql.append(" join roteiro.roteirizacao roteirizacao ");
		
		hql.append(" join roteirizacao.box box ");
		
		hql.append(" join box.cotas cota ");
		
		hql.append(" join cota.pessoa pessoa ");

		hql.append(" where box.id = :id");
		
		hql.append(" order by roteiro asc, rota asc, numeroCota asc");
		
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
        
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct cota from Box b  ");
		
		if(idRoteiro!=null && idRoteiro>0){
			
		    hql.append(", Roteirizacao roteirizacao ");
		    hql.append(" join roteirizacao.roteiros roteiro ");
		    hql.append(" join roteirizacao.box box ");
		    
		    if (idRota!=null && idRota>0){
		    	
				hql.append("join roteiro.rotas rota ");
			}
		}  
		
		hql.append(" join b.cotas cota ");
		
		hql.append(" where b.id = :idBox");
		
		if(idRoteiro!=null && idRoteiro>0){
			
			hql.append(" and box.id = b.id ");
		    hql.append(" and roteiro.id = :idRoteiro ");
		    
		    if (idRota!=null && idRota>0){
		    	
				hql.append(" and rota.id = :idRota ");
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idBox", idBox);
		
		if (idRoteiro!=null && idRoteiro>0){
			
			query.setParameter("idRoteiro", idRoteiro);
			
			if(idRota!=null && idRota>0){
				
			    query.setParameter("idRota", idRota);
			}
		}
		
		return query.list();
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
        
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(cota) from Box b  ");
		
		if(idRoteiro!=null && idRoteiro>0){
			
		    hql.append(", Roteirizacao roteirizacao ");
		    hql.append(" join roteirizacao.roteiros roteiro ");
		    hql.append(" join roteirizacao.box box ");
		    
		    if (idRota!=null && idRota>0){
		    	
				hql.append("join roteiro.rotas rota ");
			}
		}  
		
		hql.append(" join b.cotas cota ");
		
		hql.append(" where b.id = :idBox");
		
		if(idRoteiro!=null && idRoteiro>0){
			
			hql.append(" and box.id = b.id ");
		    hql.append(" and roteiro.id = :idRoteiro ");
		    
		    if (idRota!=null && idRota>0){
		    	
				hql.append(" and rota.id = :idRota ");
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idBox", idBox);
		
		if (idRoteiro!=null && idRoteiro>0){
			
			query.setParameter("idRoteiro", idRoteiro);
			
			if(idRota!=null && idRota>0){
				
			    query.setParameter("idRota", idRota);
			}
		}
		
		return (Long) query.uniqueResult();
	}




}
