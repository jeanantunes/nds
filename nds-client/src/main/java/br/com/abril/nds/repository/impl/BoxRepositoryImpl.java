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
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Usuario;
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
public class BoxRepositoryImpl extends AbstractRepository<Box,Long> implements BoxRepository {

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
		
		hql.append("select rotaRoteiro.cota.numeroCota as numeroCota,");
		hql.append("case rotaRoteiro.cota.pessoa.class when 'F' then rotaRoteiro.cota.pessoa.nome when 'J' then rotaRoteiro.cota.pessoa.razaoSocial end  as nomeCota,");
		hql.append("rotaRoteiro.rota.codigoRota as rota,");
		hql.append("rotaRoteiro.roteiro.descricaoRoteiro as roteiro");
		
		hql.append(" from RotaRoteiroOperacao rotaRoteiro");
		
		hql.append(" where rotaRoteiro.cota.box.id = :id");
		
		hql.append(" order by roteiro asc, rota asc, numeroCota asc");
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setLong("id", id);
		query.setResultTransformer(new AliasToBeanResultTransformer(
				CotaRotaRoteiroDTO.class));
		
		
		return query.list();
	}

}
