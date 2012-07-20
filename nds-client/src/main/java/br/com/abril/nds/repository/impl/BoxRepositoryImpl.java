package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaRotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Roteiro;
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

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#obterListaBox(br.com.abril.nds.model.cadastro.TipoBox)
	 */
	public List<Box> obterListaBox(TipoBox tipoBox) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select box from Box box ");
		
		hql.append(" where ");
		
		hql.append(" box.tipoBox = :tipoBox ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoBox", tipoBox);
		
		return query.list();
		
		
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
	public List<Box> busca(String codigoBox,TipoBox tipoBox, boolean postoAvancado , String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults){
		
		Criteria criteria = addRestrictions(codigoBox, tipoBox, postoAvancado);
		
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setMaxResults(maxResults);
		criteria.setFirstResult(initialResult);
		
		return criteria.list();
		
	}
	
	
	@Override
	public Long quantidade(String codigoBox,TipoBox tipoBox, boolean postoAvancado ){
		Criteria criteria = addRestrictions(codigoBox, tipoBox, postoAvancado);
		criteria.setProjection(Projections.rowCount());
		
		
		return (Long)criteria.list().get(0);
	}
	
	/**
	 * Adiciona as restricoes a consulta.
	 * @param codigoBox Codigo do box
	 * @param tipoBox Tipo do Box {@link TipoBox}
	 * @param postoAvancado Inidica se o Box é um posto avançado.
	 * @return
	 */
	private Criteria addRestrictions(String codigoBox, TipoBox tipoBox,
			boolean postoAvancado) {
		Criteria criteria =  getSession().createCriteria(Box.class);	
		
		if(!StringUtil.isEmpty(codigoBox)){
			criteria.add(Restrictions.ilike("codigo", codigoBox));
		}
		
		if(tipoBox != null){
			criteria.add(Restrictions.eq("tipoBox", tipoBox));
		}
		
		if(postoAvancado){
			criteria.add(Restrictions.eq("postoAvancado", true));
		}
		return criteria;
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#hasCodigo(java.lang.String, java.lang.Long)
	 */
	@Override
	public boolean hasCodigo(String codigoBox, Long id){
		Criteria criteria =  getSession().createCriteria(Box.class);	
		
		criteria.add(Restrictions.ilike("codigo", codigoBox));
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
	public List<CotaRotaRoteiroDTO> obtemCotaRotaRoteiro(long id){
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select roteirizacao.pdv.cota.numeroCota as numeroCota,");
		hql.append("case roteirizacao.pdv.cota.pessoa.class when 'F' then roteirizacao.pdv.cota.pessoa.nome when 'J' then roteirizacao.pdv.cota.pessoa.razaoSocial end  as nomeCota,");
		hql.append("rota.codigoRota ||' - '|| rota.descricaoRota as rota,");
		hql.append("roteiro.descricaoRoteiro as roteiro");
		
		hql.append(" from Roteiro roteiro  join roteiro.rotas rota join rota.roteirizacao roteirizacao ");
		
		hql.append(" where roteiro.box.id = :id");
		
		hql.append(" order by roteiro asc, rota asc, numeroCota asc");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setLong("id", id);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaRotaRoteiroDTO.class));
		
		
		return query.list();
	}

	@Override
	public String obterCodigoBoxPadraoUsuario(Long idUsuario) {
		
		StringBuilder hql = new StringBuilder("select p.box.codigo ");
		hql.append(" from ParametroUsuarioBox p ")
		   .append(" where p.usuario.id = :idUsuario");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idUsuario", idUsuario);
		
		return (String) query.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.BoxRepository#hasRoteiros(long)
	 */
	@Override
	public boolean hasRoteirosVinculados(long idBox){
		Criteria criteria = getSession().createCriteria(Roteiro.class);
		
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
		
		hql.append(" select distinct c from Cota c");
		
		if(idRoteiro!=null && idRoteiro>0){
		    hql.append(", Roteiro rr");
		    if (idRota!=null && idRota>0){
				hql.append(", Roteirizacao r");
			}
		}  
		
		hql.append(" where c.box.id = :idBox");
		
		if(idRoteiro!=null && idRoteiro>0){
		    hql.append(" and rr.box = c.box and rr.id = :idRoteiro ");
		    if (idRota!=null && idRota>0){
		    	hql.append(" and r.rota.roteiro = rr and r.rota.id = :idRota and r.pdv.cota = c");
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
	public int obterQuantidadeCotasPorBoxRoteiroRota(Long idBox, Long idRoteiro, Long idRota) {
        
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct c from Cota c");
		
		if(idRoteiro!=null && idRoteiro>0){
		    hql.append(", Roteiro rr");
		    if (idRota!=null && idRota>0){
				hql.append(", Roteirizacao r");
			}
		}  
		
		hql.append(" where c.box.id = :idBox");
		
		if(idRoteiro!=null && idRoteiro>0){
		    hql.append(" and rr.box = c.box and rr.id = :idRoteiro ");
		    if (idRota!=null && idRota>0){
				hql.append(" and r.rota.roteiro = rr and r.rota.id = :idRota and r.pdv.cota = c");
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
		
		return query.list().size();
	}


}
