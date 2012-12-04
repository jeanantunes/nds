package br.com.abril.nds.repository.impl;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.repository.PdvRepository;

/**
 * 
 * Classe de implementação  das ações a acesso a dados referente a classe
 * {@link br.com.abril.nds.model.cadastro.pdv.PDV}  
 * 
 * @author Discover Technology
 *
 */

@Repository
public class PdvRepositoryImpl extends AbstractRepositoryModel<PDV, Long> implements PdvRepository {

	public PdvRepositoryImpl() {
		super(PDV.class);
	}
	
	@Override
	public Long obterQntPDV(Long idCota, Long idPdvIgnorar){
		
		Criteria criteria  = getSession().createCriteria(PDV.class);
		
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		if (idPdvIgnorar != null){
			
			criteria.add(Restrictions.not(Restrictions.eq("id", idPdvIgnorar)));
		}
		
		return (Long) criteria.uniqueResult();
	}
	
	public Boolean existePDVPrincipal(Long idCota, Long idPdvIgnorar){
		
		Criteria criteria  = getSession().createCriteria(PDV.class);
		
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", Boolean.TRUE));
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		if (idPdvIgnorar != null){
			
			criteria.add(Restrictions.not(Restrictions.eq("id", idPdvIgnorar)));
		}
		
		criteria.setProjection(Projections.rowCount());
		
		Long quantidade = (Long) criteria.uniqueResult(); 
		
		return (quantidade > 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT pdv.nome as nomePDV, " )
				.append("  tipoPontoPDV.descricao as descricaoTipoPontoPDV ,")
				.append("  pdv.contato as contato,")
				.append("  endereco.logradouro || ',' || endereco.numero || '-' || endereco.bairro || '-' || endereco.cidade as  endereco , ")
				.append("  telefone.ddd || '-'|| telefone.numero as telefone ,")
				.append("  pdv.caracteristicas.pontoPrincipal as principal,")
				.append("  pdv.status as statusPDV ,")
				.append("  pdv.porcentagemFaturamento as porcentagemFaturamento ,")
				.append("  pdv.id as id , ")
				.append("  cota.id as idCota ")
		.append(" FROM PDV pdv ")
		.append(" JOIN pdv.cota cota ")
		.append(" LEFT JOIN pdv.enderecos enderecoPdv ")
		.append(" LEFT JOIN enderecoPdv.endereco endereco ")
		.append(" LEFT JOIN pdv.telefones telefonePdv ")
		.append(" LEFT JOIN telefonePdv.telefone telefone ")
		.append(" LEFT JOIN pdv.segmentacao.tipoPontoPDV tipoPontoPDV ")
		.append(" WHERE cota.id = :idCota ")
		.append(" and (enderecoPdv is null or enderecoPdv.principal =:principal )")
		.append(" and (telefonePdv is null or telefonePdv.principal =:principal) ");
		
		hql.append(getOrdenacaoPDV(filtro));
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", filtro.getIdCota());
		query.setParameter("principal",true);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(PdvDTO.class));
		
		if (filtro.getPaginacao() != null) {
			
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
	
		return query.list();
		
	}
	
	/**
	 * Monta strinf hql de ordenação da consulta de pdvs
	 * 
	 * @param filtro - filtro com as opções de ordenação
	 * 
	 * @return String
	 */
	private String getOrdenacaoPDV(FiltroPdvDTO filtro){
		
		if(filtro == null || filtro.getColunaOrdenacao() == null){
			return "";
		}
		
		StringBuilder hql = new StringBuilder();
		
		switch (filtro.getColunaOrdenacao()) {
			case CONTATO:
				hql.append(" order by contato ");
				break;
			case FATURAMENTO:
				hql.append(" order by porcentagemFaturamento ");
				break;	
			case NOME_PDV:
				hql.append(" order by  nomePDV ");
				break;
			case STATUS:
				hql.append(" order by  statusPDV ");
				break;
			case TIPO_PONTO:
				hql.append(" order by  tipoPontoPDV ");
				break;
			case ENDERECO:
				hql.append(" order by  endereco ");
				break;
			case TELEFONE:
				hql.append(" order by  telefone ");
				break;
			default:
				hql.append(" order by  nomePDV ");
		}
		
		if (filtro.getPaginacao()!= null && filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}
	
	public PDV obterPDV(Long idCota,Long idPDV){
		
		Criteria criteria = getSession().createCriteria(PDV.class);
		
		criteria.add(Restrictions.eq("id", idPDV));
		criteria.add(Restrictions.eq("cota.id", idCota));
		
		return (PDV) criteria.uniqueResult();
	}

	public void setarPDVPrincipal(boolean principal, Long idCota){
		
		Query query = 
				this.getSession().createQuery("update PDV p set p.caracteristicas.pontoPrincipal = :principal where p.cota.id = :idCota");
		
		query.setParameter("principal", principal);
		query.setParameter("idCota", idCota);
		
		query.executeUpdate();
	}
	
	public PDV obterPDVPrincipal(Long idCota) {
		
		Criteria criteria = getSession().createCriteria(PDV.class);
		criteria.createAlias("cota", "cota");
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", true));
		criteria.setMaxResults(1);
		return (PDV) criteria.uniqueResult();
	}
	
	/**
	 * Obtém PDV's por Rota
	 * @param idRota
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PDV> obterPDVPorRota(Long idRota) {
		
		Criteria criteria = getSession().createCriteria(RotaPDV.class);
		criteria.createAlias("rota", "rota");
		criteria.add(Restrictions.eq("rota.id", idRota));
		return criteria.list();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<HistoricoTitularidadeCotaPDV> obterPDVsHistoricoTitularidade(FiltroPdvDTO filtro) {
        Validate.notNull(filtro.getIdCota(), "Identificador da cota não deve ser nulo!");
        Validate.notNull(filtro.getIdHistorico(), "Identificador do histórico não deve ser nulo!");
	    
        StringBuilder hql = new StringBuilder("select pdv from HistoricoTitularidadeCotaPDV pdv ");
        hql.append("left join pdv.enderecos as endereco with endereco.principal is true  ");
        hql.append("left join pdv.telefones as telefone with telefone.principal is true  ");
	    hql.append("where pdv.historicoTitularidadeCota.cota.id = :idCota ");
	    hql.append("and pdv.historicoTitularidadeCota.id = :idHistorico ");
	    
        switch (filtro.getColunaOrdenacao()) {
        case CONTATO:
            hql.append(" order by pdv.contato ");
            break;
        case FATURAMENTO:
            hql.append(" order by pdv.porcentagemFaturamento ");
            break;
        case NOME_PDV:
            hql.append(" order by  pdv.nome ");
            break;
        case STATUS:
            hql.append(" order by  pdv.status ");
            break;
        case PRINCIPAL:
            hql.append(" order by  pdv.caracteristicas.pontoPrincipal ");
            break;
        case TIPO_PONTO:
            hql.append(" order by  pdv.tipoPonto.descricao ");
            break;
        case ENDERECO:
            hql.append(" order by  (endereco.logradouro || ',' || endereco.numero || '-' || endereco.bairro || '-' || endereco.cidade) ");
            break;
        case TELEFONE:
            hql.append(" order by  (telefone.ddd || '-'|| telefone.numero) ");
            break;
        default:
            hql.append(" order by  pdv.nome ");
        }
        
        if (filtro.getPaginacao() != null
                && filtro.getPaginacao().getOrdenacao() != null) {
            hql.append(filtro.getPaginacao().getOrdenacao().toString());
        }
        
	    Query query = getSession().createQuery(hql.toString());
	    query.setParameter("idCota", filtro.getIdCota());
	    query.setParameter("idHistorico", filtro.getIdHistorico());

	    if (filtro.getPaginacao() != null) {
	        if (filtro.getPaginacao().getPosicaoInicial() != null) {
	            query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
	        }
	        
	        if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
	            query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
	        }
	    }
	    return query.list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HistoricoTitularidadeCotaPDV obterPDVHistoricoTitularidade(Long idPdv) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        String hql = "from HistoricoTitularidadeCotaPDV pdv where pdv.id = :idPdv ";
        Query query = getSession().createQuery(hql);
        query.setParameter("idPdv", idPdv);
        return (HistoricoTitularidadeCotaPDV) query.uniqueResult();
    }

    /**
     * Obtem PDV's por Cota e informações de Endereço
     * @param numCota
     * @param municipio
     * @param uf
     * @param bairro
     * @param cep
     * @return List<PDV>
     */
	@SuppressWarnings("unchecked")
	@Override
	public List<PDV> obterPDVPorCotaEEndereco(Integer numCota, String municipio, String uf, String bairro, String cep) {

		Criteria criteria  = getSession().createCriteria(PDV.class,"pdv" );
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.createAlias("cota", "cota") ;
		criteria.createAlias("enderecos", "enderecos") ;
		criteria.createAlias("enderecos.endereco", "endereco") ;
		
		if (numCota != null && !numCota.equals("") ) {
			criteria.add(Restrictions.eq("cota.numeroCota", numCota));
		}
		
		if (cep != null && !cep.equals("") ) {
			criteria.add(Restrictions.eq("endereco.cep", cep));
		} else {
			if (uf != null && !uf.equals("") ) {
				criteria.add(Restrictions.eq("endereco.uf", uf));
			}
			
			if (municipio != null && !municipio.equals("") ) {
				criteria.add(Restrictions.eq("endereco.cidade", municipio));
			}
			
			if (bairro != null && !bairro.equals("") ) {
				criteria.add(Restrictions.eq("endereco.bairro", bairro));
			}
			
		}

		return  (List<PDV>)criteria.list();  
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PDV> obterPDVsPrincipaisPor(Integer numCota, String municipio,
			String uf, String bairro, String cep) {

		Criteria criteria  = getSession().createCriteria(PDV.class,"pdv" );
		criteria.setFetchMode("cota", FetchMode.JOIN);
		criteria.createAlias("cota", "cota");
		criteria.createAlias("enderecos", "enderecos") ;
		criteria.createAlias("enderecos.endereco", "endereco");
		
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", true));
		
		if (numCota != null && !numCota.equals("") ) {
			criteria.add(Restrictions.eq("cota.numeroCota", numCota));
		}
		
		if (cep != null && !cep.equals("") ) {
			criteria.add(Restrictions.eq("endereco.cep", cep));
		} else {
			if (uf != null && !uf.equals("") ) {
				criteria.add(Restrictions.eq("endereco.uf", uf));
			}
			
			if (municipio != null && !municipio.equals("") ) {
				criteria.add(Restrictions.eq("endereco.cidade", municipio));
			}
			
			if (bairro != null && !bairro.equals("") ) {
				criteria.add(Restrictions.eq("endereco.bairro", bairro));
			}
			
		}
		
		return  (List<PDV>)criteria.list();  
	}
}
