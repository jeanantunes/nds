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
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.BoxRoteirizacaoDTO;
import br.com.abril.nds.dto.ConsultaRoteirizacaoDTO;
import br.com.abril.nds.dto.RotaRoteirizacaoDTO;
import br.com.abril.nds.dto.RoteiroRoteirizacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaRoteirizacaoDTO;
import br.com.abril.nds.model.LogBairro;
import br.com.abril.nds.model.LogLocalidade;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RoteirizacaoRepositoryImpl extends AbstractRepositoryModel<Roteirizacao, Long> implements RoteirizacaoRepository {
	
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

		StringBuilder hql = new StringBuilder();
		hql.append(" select roteiro.roteirizacao from Roteiro roteiro " )
			.append(" join roteiro.rotas rota " )
			.append(" where rota.id=:idRota ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idRota", rotaId);
		
		return query.list();
	}
	
	@Override
	public Integer buscarMaiorOrdem(Long rotaId){
		Criteria criteria  = getSession().createCriteria(Roteirizacao.class);
		criteria.add(Restrictions.eq("rota.id", rotaId));
		criteria.setProjection(Projections.max("ordem"));  
		return (Integer) criteria.uniqueResult();  
	}
	
	
	public List<PDV> buscarPdvRoteirizacaoNumeroCota(Integer numeroCota, Long rotaId , Roteiro roteiro ){
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
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		Criteria criteria  = getSession().createCriteria(Roteirizacao.class);
		criteria.createAlias("rota", "rota") ;
		criteria.createAlias("rota.roteiro", "roteiro") ;
		criteria.createAlias("roteiro.box", "box") ;
		criteria.createAlias("pdv", "pdv") ;
		criteria.createAlias("pdv.cota", "cota") ;
		criteria.createAlias("cota.pessoa", "pessoa") ;
		
		criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
	    criteria.add(Restrictions.eq("roteiro.tipoRoteiro", tipoRoteiro));
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("box.nome"), "nomeBox")
				.add(Projections.property("roteiro.descricaoRoteiro"), "descricaoRoteiro")
				.add(Projections.property("rota.descricaoRota"), "descricaoRota")
				.add(Projections.property("cota.numeroCota"), "numeroCota")
				.add(Projections.property("pessoa.nome"), "nome")));
		
	    if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(orderBy));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setFirstResult(initialResult);
		criteria.setMaxResults(maxResults);
		criteria.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));
		return criteria.list(); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select box.codigo ||' - '|| box.nome as nomeBox ," )
			.append(" rota.codigoRota || ' - ' || rota.descricaoRota as descricaoRota , ")
			.append(" roteiro.descricaoRoteiro as descricaoRoteiro , ")
			.append(" box.id as idBox, 			")
			.append(" rota.id as idRota, 		")
			.append(" roteiro.id as idRoteiro, 	")
			.append(" cota.id as idCota,		")			
			.append(" case pessoa.class when 'F' then pessoa.nome when 'J' then pessoa.razaoSocial end as nome , ")
			.append(" cota.numeroCota as numeroCota ");
			
		hql.append( getHqlWhere(filtro));
	
		hql.append(getOrdenacaoConsulta(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		getParameterConsulta(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();  
	}

	private StringBuilder getOrdenacaoConsulta(FiltroConsultaRoteirizacaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		if (filtro == null || filtro.getOrdenacaoColuna() == null) {
			return hql; 
		}

		switch (filtro.getOrdenacaoColuna()) {
			
			case BOX:
				hql.append(" order by nomeBox ");
				break;
				
			case NOME_COTA:
				hql.append(" order by nome  ");
				break;
					
			case NUMERO_COTA:
				hql.append(" order by numeroCota ");
				break;
			
			case ROTA:
				hql.append(" order by descricaoRota ");
				break;
				
			case ROTEIRO:
				hql.append(" order by descricaoRoteiro ");
				break;	
				
			case QNT_COTA:
				hql.append(" order by qntCotas ");
				break;		
				
			default:
				hql.append(" order by nomeBox ");
		}

		if (filtro.getPaginacao()!= null && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append(filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql;
	}

	private void getParameterConsulta(FiltroConsultaRoteirizacaoDTO filtro, Query query) {
		
		if(filtro.getIdBox()!= null){
			query.setParameter("idBox", filtro.getIdBox());
		}
		
		if(filtro.getIdRoteiro()!= null){
			query.setParameter("idRoteiro",filtro.getIdRoteiro());
		}
		
		if(filtro.getIdRota()!= null){
			query.setParameter("idRota",  filtro.getIdRota());
		}
		
		if(filtro.getNumeroCota()!= null){
			query.setParameter("numeroCota",filtro.getNumeroCota());
		}
	}

	private StringBuilder getHqlWhere(FiltroConsultaRoteirizacaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("from Roteirizacao roteirizacao ")
			.append(" join roteirizacao.roteiros roteiro " )
			.append(" join roteiro.box box ")
			.append(" join roteiro.rotas rota " )
			.append(" Join rota.pdvs pdv ")
			.append(" Join pdv.cota cota ")
			.append(" where roteiro.box.id = box.id "); 
			
		if(filtro.getIdBox()!= null){
			hql.append(" and box.id =:idBox ");
		}
		
		if(filtro.getIdRoteiro()!= null){
			hql.append(" and roteiro.id =:idRoteiro ");
		}
		
		if(filtro.getIdRota()!= null){
			hql.append(" and rota.id =:idRota ");
		}
		
		if(filtro.getNumeroCota()!= null){
			hql.append(" and cota.numeroCota =:numeroCota ");
		}
		return hql;
	}
	
	
	public List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota as numeroCota, ")
		.append(" case pessoa.class when 'F' then pessoa.nome when 'J' then pessoa.razaoSocial end as nome ")
		.append(" from Roteirizacao roteirizacao 	")
		
		.append(" Join roteirizacao.box box			")
		.append(" Join box.cotas cota   			")
		.append(" Join cota.pessoa pessoa 			")
		.append(" join cota.pdvs pdv 		        ")
		.append(" join pdv.rotas rota 				")
		.append(" join rota.roteiro roteiro 		")
		
		.append(" where roteiro.box.id = box.id 	"); 
			
		if(idBox!= null){
			hql.append(" and box.id =:idBox ");
		}
		
		if(idRoteiro!= null){
			hql.append(" and roteiro.id =:idRoteiro ");
		}
		
		if(idRota!= null){
			hql.append(" and rota.id =:idRota ");
		}

		Query query = getSession().createQuery(hql.toString());

		query.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));

		
		if(idBox!= null){
			query.setParameter("idBox", idBox);
		}
		
		if(idRota!= null){
			query.setParameter("idRota",idRota);
		}
		
		if(idRoteiro!= null){
			query.setParameter("idRoteiro",  idRoteiro);
		}
		
		return query.list();
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select box.codigo ||' - '|| box.nome as nomeBox ," )
			.append(" rota.codigoRota || ' - ' || rota.descricaoRota as descricaoRota , ")
			.append(" roteiro.descricaoRoteiro as descricaoRoteiro , ")
			.append(" box.id as idBox, 			")
			.append(" rota.id as idRota, 		")
			.append(" roteiro.id as idRoteiro, 	")
			.append(" count (cota.numeroCota) as qntCotas ");
			
		hql.append( getHqlWhere(filtro));
	
		hql.append(" group by box.codigo, box.id, rota.codigoRota, rota.id, roteiro.descricaoRoteiro, roteiro.id ");
		
		hql.append(getOrdenacaoConsulta(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		getParameterConsulta(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list(); 
	}
	
	@Override
	public Integer buscarQuantidadeRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(box.codigo) ");
			
		hql.append( getHqlWhere(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		getParameterConsulta(filtro, query);
		
		return ((Long) query.uniqueResult()).intValue();
	}
	
	@Override
	public Integer buscarQuantidadeRoteirizacaoSumarizadoPorCota(FiltroConsultaRoteirizacaoDTO filtro) {
	
		filtro.setPaginacao(null);
		
		return buscarRoteirizacaoSumarizadoPorCota(filtro).size(); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BoxRoteirizacaoDTO> obterBoxesPorNome(String nome) {
		
		Criteria criteria  = getSession().createCriteria(Box.class, "box");
		criteria.add(Restrictions.ilike("box.nome", "%" + nome.toLowerCase() + "%"));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("box.id"), "id")
				.add(Projections.property("box.nome"), "nome")
		//		.add(Projections.property("box.ordem"), "ordem")
				);
		
		//criteria.addOrder(Order.asc("box.ordem"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(BoxRoteirizacaoDTO.class));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoteiroRoteirizacaoDTO> obterRoteirosPorNomeEBoxes(String nome,
			List<Long> idsBoxes) {
		
		Criteria criteria  = getSession().createCriteria(Roteiro.class, "roteiro");
		criteria.createAlias("roteiro.box","box");
		criteria.add(Restrictions.ilike("roteiro.descricaoRoteiro", "%" + nome.toLowerCase() + "%"));
		criteria.add(Restrictions.in("box.id", idsBoxes));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("roteiro.id"), "id")
				.add(Projections.property("roteiro.descricaoRoteiro"), "nome")
				.add(Projections.property("roteiro.ordem"), "ordem")
				);
		
		criteria.addOrder(Order.asc("roteiro.ordem"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(RoteiroRoteirizacaoDTO.class));
		
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RotaRoteirizacaoDTO> obterRotasPorNomeERoteiros(String nome,
			List<Long> idsRoteiros) {
		
		Criteria criteria  = getSession().createCriteria(Rota.class, "rota");
		criteria.createAlias("rota.roteiro","roteiro");
		criteria.add(Restrictions.ilike("rota.descricaoRota", "%" + nome.toLowerCase() + "%"));
		criteria.add(Restrictions.in("roteiro.id", idsRoteiros));
		
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("rota.id"), "id")
				.add(Projections.property("rota.descricaoRota"), "nome")
				.add(Projections.property("rota.ordem"), "ordem")
				);
		
		criteria.addOrder(Order.asc("rota.ordem"));
		
		criteria.setResultTransformer(Transformers.aliasToBean(RotaRoteirizacaoDTO.class));
		
		return criteria.list();
	}

}

