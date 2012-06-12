package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;


import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.RoteirizacaoRepository;

@Repository
public class RoteirizacaoRepositoryImpl extends AbstractRepository<Roteirizacao, Long> implements RoteirizacaoRepository {
	
	public RoteirizacaoRepositoryImpl() {
		super(Roteirizacao.class);
	}
	
	public Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select roteirizacao from Roteirizacao roteirizacao " )
			.append(" join roteirizacao.pdv pdv " )
			.append(" join pdv.cota cota " )
			.append(" join roteirizacao.rota rota " )
			.append(" join rota.roteiro roteiro ")
			.append(" where pdv.caracteristicas.pontoPrincipal=:pontoPrincipal ")
			.append(" and cota.numeroCota=:numeroCota ")
			.append(" and roteiro.tipoRoteiro=:tipoRoteiro");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("pontoPrincipal", Boolean.TRUE);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("tipoRoteiro", TipoRoteiro.NORMAL);
		
		query.setMaxResults(1);
		
		return (Roteirizacao)query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Roteirizacao> buscarRoterizacaoPorRota(Long rotaId) {
		Criteria criteria = getSession().createCriteria(Roteirizacao.class);
		criteria.add(Restrictions.eq("rota.id", rotaId));
		criteria.addOrder(Order.asc("ordem"));
		return criteria.list();
	}
	
	@Override
	public Integer buscarMaiorOrdem(Long rotaId){
		Criteria criteria  = getSession().createCriteria(Roteirizacao.class);
		criteria.add(Restrictions.eq("rota.id", rotaId));
		criteria.setProjection(Projections.max("ordem"));  
		return (Integer) criteria.uniqueResult();  
	}
	
	
	public List<PDV> buscarRoteirizacaoNumeroCota(Integer numeroCota, Long rotaId , Roteiro roteiro ){
		Boolean exibirPontoPrincipal =  Boolean.FALSE;
		DetachedCriteria subquery = DetachedCriteria.forClass(Roteirizacao.class);
		exibirPontoPrincipal = getSubqueyCotasDisponiveis(rotaId, roteiro,
				exibirPontoPrincipal, subquery);

		Criteria criteria  = getSession().createCriteria(PDV.class,"pdv" );
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.createAlias("cota", "cota") ;
		criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", exibirPontoPrincipal));
		criteria.add(Property.forName("pdv.id").notIn(subquery));
		return  (List<PDV>)criteria.list();  
	}
	
	
	public List<PDV> buscarRoteirizacaoPorEndereco(String CEP, String uf, String municipio, String bairro,  Long rotaId , Roteiro roteiro ){
		Boolean exibirPontoPrincipal =  Boolean.FALSE;
		DetachedCriteria subquery = DetachedCriteria.forClass(Roteirizacao.class);
		exibirPontoPrincipal = getSubqueyCotasDisponiveis(rotaId, roteiro,
				exibirPontoPrincipal, subquery);
		
		EnderecoPDV pdv = new EnderecoPDV();
		Criteria criteria  = getSession().createCriteria(PDV.class,"pdv" );
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.createAlias("cota", "cota") ;
		criteria.createAlias("enderecos", "enderecos") ;
		criteria.createAlias("enderecos.endereco", "endereco") ;
		if (CEP != null && !CEP.equals("") ) {
			criteria.add(Restrictions.eq("endereco.cep", CEP));
		} else {
			if (uf != null && !uf.equals("") ) {
				criteria.add(Restrictions.eq("endereco.uf", uf));
			}
			
			if (municipio != null && !municipio.equals("") ) {
				criteria.add(Restrictions.eq("endereco.municipio", municipio));
			}
			
			if (bairro != null && !bairro.equals("") ) {
				criteria.add(Restrictions.eq("endereco.bairro", bairro));
			}
			
			
		}
		criteria.add(Restrictions.eq("enderecos.principal", true));
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", exibirPontoPrincipal));
		criteria.add(Property.forName("pdv.id").notIn(subquery));
		return  (List<PDV>)criteria.list();  
	}

	private Boolean getSubqueyCotasDisponiveis(Long rotaId, Roteiro roteiro,
			Boolean exibirPontoPrincipal, DetachedCriteria subquery) {
		subquery.createAlias("rota", "rota") ;
		subquery.createAlias("pdv", "pdv") ;
		subquery.createAlias("rota.roteiro", "roteiro") ;
		subquery.setProjection(Projections.projectionList().add(Projections.property("pdv.id")));
		if ( roteiro.getTipoRoteiro().compareTo(TipoRoteiro.NORMAL) == 0 ){
			subquery.add(Restrictions.eq("roteiro.tipoRoteiro", TipoRoteiro.NORMAL));
			exibirPontoPrincipal =  Boolean.TRUE;
		} else {
			subquery.add(Restrictions.eq("rota.id", rotaId));
		}
		return exibirPontoPrincipal;
	}
	
	

	@Override
	public List<String> buscarUF(){

		Criteria criteria  = getSession().createCriteria(LogLocalidade.class);
		criteria.setProjection(Projections.property("ufeSg"));
		criteria.setProjection(Projections.distinct(Projections.property("ufeSg")));
		criteria.addOrder(Order.asc("ufeSg"));
		return criteria.list();  

	}
	
	@Override
	public List<LogLocalidade> buscarMunicipioPorUf(String uf){
		Criteria criteria  = getSession().createCriteria(LogLocalidade.class);
		criteria.add(Restrictions.eq("ufeSg", uf));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("locNo"));
		return criteria.list();  
	}
	
	@Override
	public List<LogBairro> buscarBairroPorMunicipio(Long municipio, String uf){
		Criteria criteria  = getSession().createCriteria(LogBairro.class);
		criteria.add(Restrictions.eq("logLocalidade.locNu", municipio));
		criteria.add(Restrictions.eq("ufeSg", uf));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("baiNo"));
		return criteria.list();  
	}
	
	@Override
	public List<Roteirizacao> buscarRoteirizacao(Long boxId, Long roteiroId, Long rotaId, TipoRoteiro tipoRoteiro){
		Criteria criteria  = getSession().createCriteria(Roteirizacao.class);
		criteria.createAlias("rota", "rota") ;
		criteria.createAlias("rota.roteiro", "roteiro") ;
		criteria.createAlias("roteiro.box", "box") ;
		if (boxId != null) { 
			criteria.add(Restrictions.eq("roteiro.box.id", boxId));
		}
		
		if (roteiroId != null) { 
			criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		}
		
		if (rotaId != null) { 
			criteria.add(Restrictions.eq("rota.id", rotaId));
		}
		
		criteria.add(Restrictions.eq("roteiro.tipoRoteiro", tipoRoteiro));
		criteria.addOrder(Order.asc("box.nome"));

		
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();  
	}

}

