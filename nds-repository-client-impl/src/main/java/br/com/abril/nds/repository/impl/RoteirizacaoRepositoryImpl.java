package br.com.abril.nds.repository.impl;

import java.math.BigInteger;
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
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoRoteiro;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RoteirizacaoRepository;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RoteirizacaoRepositoryImpl extends AbstractRepositoryModel<Roteirizacao, Long> implements RoteirizacaoRepository {
	
	public RoteirizacaoRepositoryImpl() {
		super(Roteirizacao.class);
	}
	
	public Roteirizacao buscarRoteirizacaoDeCota(Integer numeroCota){
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select roteirizacao from Roteirizacao roteirizacao " )
			.append(" join roteirizacao.roteiros roteiro " )
			.append(" join roteiro.rotas rota ")
			.append(" join rota.rotaPDVs rpdv " )
			.append(" join rpdv.pdv pdv " )
			.append(" join pdv.cota cota " )
			
			.append(" where pdv.caracteristicas.pontoPrincipal=:pontoPrincipal ")
			.append(" and cota.numeroCota=:numeroCota ")
			.append(" and roteiro.tipoRoteiro = :tipoRoteiro");
		
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
		Criteria criteria  = getSession().createCriteria(Roteiro.class);
		criteria.setFetchMode("rotas", FetchMode.JOIN);
		criteria.createAlias("rotas", "rotas") ;
		criteria.add(Restrictions.eq("rotas.id", rotaId));
		criteria.setProjection(Projections.max("ordem"));  
		return (Integer) criteria.uniqueResult();  
	}
	
	
	@SuppressWarnings("unchecked")
	public List<PDV> buscarPdvRoteirizacaoNumeroCota(Integer numeroCota, Long rotaId , Roteiro roteiro ){
		Boolean exibirPontoPrincipal =  Boolean.FALSE;
		DetachedCriteria subquery = DetachedCriteria.forClass(Roteiro.class);
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
	
	
	@SuppressWarnings("unchecked")
	public List<PDV> buscarRoteirizacaoPorEndereco(String CEP, String uf, String cidade, String bairro,  Long rotaId , Roteiro roteiro ){
		Boolean exibirPontoPrincipal =  Boolean.FALSE;
		DetachedCriteria subquery = DetachedCriteria.forClass(Roteiro.class);
		exibirPontoPrincipal = getSubqueyCotasDisponiveis(rotaId, roteiro,
				exibirPontoPrincipal, subquery);
		
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
			
			if (cidade != null && !cidade.equals("") ) {
				criteria.add(Restrictions.eq("endereco.cidade", cidade));
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
		subquery.setFetchMode("rotas", FetchMode.JOIN);
		subquery.createAlias("rotas", "rotas") ;
		subquery.setFetchMode("rotas.rotaPDVs", FetchMode.JOIN);
		subquery.createAlias("rotas.rotaPDVs", "rotaPDVs");
		subquery.createAlias("rotaPDVs.pdv", "pdv");
		subquery.setProjection(Projections.projectionList().add(Projections.property("pdv.id")));
		if(roteiro.getTipoRoteiro().compareTo(TipoRoteiro.NORMAL) == 0 ) {
			subquery.add(Restrictions.eq("tipoRoteiro", TipoRoteiro.NORMAL));
			exibirPontoPrincipal =  Boolean.TRUE;
		} else {
			subquery.add(Restrictions.eq("rotas.id", rotaId));
		}
		return exibirPontoPrincipal;
	}
		

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacaoPorNumeroCota(Integer numeroCota, TipoRoteiro tipoRoteiro, String  orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		Criteria criteria  = getSession().createCriteria(Roteiro.class);
		criteria.setFetchMode("rotas", FetchMode.JOIN);
		criteria.createAlias("rotas", "rotas") ;
		criteria.createAlias("roteirizacao", "roteirizacao") ;
		criteria.createAlias("roteirizacao.box", "box") ;
		criteria.setFetchMode("rotas.rotaPDVs", FetchMode.JOIN);
		criteria.createAlias("rotas.rotaPDVs", "rotaPDVs");
		criteria.createAlias("rotaPDVs.pdv", "pdv");
		criteria.createAlias("pdv.cota", "cota") ;
		criteria.createAlias("cota.pessoa", "pessoa") ;
		
		criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
	    criteria.add(Restrictions.eq("tipoRoteiro", tipoRoteiro));
		criteria.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("box.nome"), "nomeBox")
				.add(Projections.property("descricaoRoteiro"), "descricaoRoteiro")
				.add(Projections.property("rotas.descricaoRota"), "descricaoRota")
				.add(Projections.property("cota.numeroCota"), "numeroCota")
				.add(Projections.property("pessoa.nome"), "nome")));
		
	    if(Ordenacao.ASC ==  ordenacao) {
			criteria.addOrder(Order.asc(orderBy));
		} else if(Ordenacao.DESC ==  ordenacao) {
			criteria.addOrder(Order.desc(orderBy));
		}
		
		criteria.setFirstResult(initialResult);
		criteria.setMaxResults(maxResults);
		criteria.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));
		return criteria.list(); 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsultaRoteirizacaoDTO> buscarRoteirizacao(FiltroConsultaRoteirizacaoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when box is null then 'Especial' else (box.codigo ||' - '|| box.nome) end as nomeBox ," )
			.append(" rota.descricaoRota as descricaoRota , ")
			.append(" roteiro.descricaoRoteiro as descricaoRoteiro , ")
			.append(" box.id as idBox, 			")
			.append(" box.tipoBox as tipobox,   ")
			.append(" rota.id as idRota, 		")
			.append(" roteiro.id as idRoteiro, 	")
			.append(" cota.id as idCota,		")			
			.append(" case pessoa.class when 'F' then pessoa.nome when 'J' then pessoa.razaoSocial end as nome , ")
			.append(" cota.numeroCota as numeroCota, ")
		    .append(" roteirizacao.id as idRoteirizacao ");
			
		hql.append( getHqlWhere(filtro));
	
		hql.append(getOrdenacaoConsulta(filtro));
		
		Query query  = getSession().createQuery(hql.toString());
		
		getParameterConsulta(filtro, query);
		
		query.setResultTransformer(Transformers.aliasToBean(ConsultaRoteirizacaoDTO.class));
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao() != null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
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
				hql.append(" order by box.codigo ");
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
		
		query.setParameter("sitCadastro", SituacaoCadastro.INATIVO);
		
		if(filtro.getIdBox()!= null && filtro.getIdBox() > 0){
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
			.append(" left join roteirizacao.roteiros roteiro " )
			.append(" left join roteirizacao.box box ")
			.append(" join roteiro.rotas rota " )
			.append(" Join rota.rotaPDVs rotaPdv ")
			.append(" Join rotaPdv.pdv pdv ")
			.append(" Join pdv.cota cota ")
			.append(" join cota.pessoa pessoa ")
			.append(" where 1 = 1 ")
			.append(" and cota.situacaoCadastro != :sitCadastro ");
			
		if(filtro.getIdBox() != null) {
			
			if (filtro.getIdBox() < 1) {
				
				hql.append(" and roteirizacao.box IS NULL ");
			} else {
				
				hql.append(" and box.id = :idBox ");
			}
		}
		
		if(filtro.getIdRoteiro() != null){
			
			hql.append(" and roteiro.id = :idRoteiro ");
		}
		
		if(filtro.getIdRota() != null){
			
			hql.append(" and rota.id = :idRota ");
		}
		
		if(filtro.getNumeroCota() != null){
			
			hql.append(" and cota.numeroCota = :numeroCota ");
		}
		
		return hql;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ConsultaRoteirizacaoDTO> obterCotasParaBoxRotaRoteiro(Long idBox, Long idRota, Long idRoteiro,
			String sortname, String sortorder) {
		
		StringBuilder hql = new StringBuilder();
		boolean indWhere = false;
		
		hql.append("select cota.numeroCota as numeroCota, ")
		.append("case pessoa.class when 'F' then pessoa.nome when 'J' then pessoa.razaoSocial end as nome ")
		.append("from Roteirizacao roteirizacao ")
		
		.append("left join roteirizacao.box box ")
		.append("join roteirizacao.roteiros roteiro ")
		.append("join roteiro.rotas rota ")
		.append("join rota.rotaPDVs rotaPdv ")
		.append("join rotaPdv.pdv pdv ")
		.append("join pdv.cota cota ")
		.append("join cota.pessoa pessoa ");
			
		if(idBox!= null){
			
			hql.append(" where box.id = :idBox ");
			indWhere = true;
		}
		
		if(idRoteiro!= null){
			
			if (indWhere){
				
				hql.append(" and ");
			} else {
				
				hql.append(" where ");
				indWhere = true;
			}
			
			hql.append(" roteiro.id = :idRoteiro ");
		}
		
		if(idRota!= null){
			
			if (indWhere){
				
				hql.append(" and ");
			} else {
				
				hql.append(" where ");
				indWhere = true;
			}
			
			hql.append(" rota.id = :idRota ");
		}
		
		hql.append(" order by ");
		
		if (sortname != null && !sortname.isEmpty()){
			
			hql.append(sortname);
		} else {
			
			hql.append(" rotaPdv.ordem ");
		}
		
		if (sortorder != null && !sortorder.isEmpty()){
			
			hql.append(" ").append(sortorder);
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
		
		hql.append(" select case when box is null then 'Especial' else (box.codigo ||' - '|| box.nome) end as nomeBox ," )
			.append(" rota.descricaoRota as descricaoRota, ")
			.append(" roteiro.descricaoRoteiro as descricaoRoteiro , ")
			.append(" box.id as idBox, 			")
			.append(" rota.id as idRota, 		")
			.append(" roteiro.id as idRoteiro, 	")
			.append(" count (cota.numeroCota) as qntCotas ");
			
		hql.append( getHqlWhere(filtro));
	
		hql.append(" group by box.codigo, box.id, rota.descricaoRota, rota.id, roteiro.descricaoRoteiro, roteiro.id ");
		
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
		
		hql.append(" select count(roteirizacao) ");
			
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
		criteria.createAlias("roteiro.roteirizacao","roteirizacao");
		criteria.createAlias("roteirizacao.box","box");
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Roteirizacao obterRoteirizacaoPorBox(Long idBox) {
		Criteria criteria  = getSession().createCriteria(Roteirizacao.class, "roteirizacao");
		
		if (idBox != null && idBox > 0) {
			criteria.createAlias("roteirizacao.box","box");
			criteria.add(Restrictions.eq("box.id", idBox));
		} else {
			criteria.add(Restrictions.isNull("roteirizacao.box"));
		}
		
		criteria.setMaxResults(1);
		
		return (Roteirizacao) criteria.uniqueResult();
	}

	/**
	 * Obtém o Box de um PDV
	 * @param idPdv
	 * @return
	 */
	@Override
	public Box obterBoxDoPDV(Long... idPdv) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select b from Box b "); 
		hql.append(" join b.roteirizacao r ");
		hql.append(" join r.roteiros roteiro ");
		hql.append(" join roteiro.rotas rota ");
		hql.append(" join rota.rotaPDVs rotaPdv ");
		hql.append(" join rotaPdv.pdv pdv ");
		hql.append(" where r.box = b ");
		hql.append(" and   pdv.id IN ( :idPdv ) ");
		
		Query query  = getSession().createQuery(hql.toString());

		query.setParameterList("idPdv",idPdv);
		
		query.setMaxResults(1);
		
		return (Box) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> obterNumerosCotaOrdenadosRoteirizacao() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota from Roteirizacao r "); 
		hql.append(" join r.box b ");
		hql.append(" join r.roteiros roteiro ");
		hql.append(" join roteiro.rotas rota ");
		hql.append(" join rota.rotaPDVs rotaPdv ");
		hql.append(" join rotaPdv.pdv pdv ");
		hql.append(" join pdv.cota cota ");
		hql.append(" order by b.codigo, roteiro.ordem, rota.ordem, rotaPdv.ordem, cota.numeroCota ");
		
		Query query  = getSession().createQuery(hql.toString());
		
		return query.list();
	}
	
	@Override
	public boolean existeRotaParaCota(final Integer numeroCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(rotaPDV.ID) from rota_pdv rotaPDV join pdv pdv on pdv.ID = rotaPDV.PDV_ID ");
		hql.append(" join cota cota on cota.ID = pdv.COTA_ID ");
		hql.append(" where cota.NUMERO_COTA  =:numeroCota ");
		
		Query query  = getSession().createSQLQuery(hql.toString());
		
		query.setParameter("numeroCota",numeroCota);
		
		BigInteger  quantidade = (BigInteger) query.uniqueResult();
		
		return (Util.nvl(quantidade, BigInteger.ZERO).longValue() > 0);
	}
}