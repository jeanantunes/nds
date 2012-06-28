package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.model.cadastro.pdv.PDV;
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
	public Long obterQntPDV(){
		
		Criteria criteria  = getSession().createCriteria(PDV.class);
		
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.uniqueResult();
	}
	
	public Boolean existePDVPrincipal(){
		
		Criteria criteria  = getSession().createCriteria(PDV.class);
		
		criteria.add(Restrictions.eq("caracteristicas.pontoPrincipal", Boolean.TRUE));
		
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
	
	
}
