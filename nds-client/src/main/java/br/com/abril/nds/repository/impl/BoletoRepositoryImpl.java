package br.com.abril.nds.repository.impl;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.repository.BoletoRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.Boleto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BoletoRepositoryImpl extends AbstractRepository<Boleto,Long> implements BoletoRepository {

	
	/**
	 * Construtor padrão
	 */
	public BoletoRepositoryImpl() {
		super(Boleto.class);
	}

	
	@Override
	public long obterQuantidadeBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro){
		long quantidade = 0;
		StringBuilder hql = new StringBuilder();
		hql.append(" select count (b) from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		if (filtro.getStatusCobranca()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setParameter("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setParameter("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatusCobranca()!=null){	
		    query.setParameter("status", filtro.getStatusCobranca());
		}
		
		quantidade = (Long) query.uniqueResult();
		return quantidade;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Boleto> obterBoletosPorCota(FiltroConsultaBoletosCotaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" from Boleto b where ");		
		hql.append(" b.cota.numeroCota = :ncota ");
		
		if (filtro.getDataVencimentoDe()!=null){
		    hql.append(" and b.dataVencimento >= :vctode ");
		}
		if (filtro.getDataVencimentoAte()!=null){
		    hql.append(" and b.dataVencimento <= :vctoate ");
		}
		
		if (filtro.getStatusCobranca()!=null){	  
			hql.append(" and b.statusCobranca = :status");
		}
		
		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case NOSSO_NUMERO:
					hql.append(" order by b.cota.numeroCota ");
					break;
				case DATA_EMISSAO:
					hql.append(" order by b.dataEmissao ");
					break;
				case DATA_VENCIMENTO:
					hql.append(" order by dataVencimento ");
					break;
				case DATA_PAGAMENTO:
					hql.append(" order by dataPagamento ");
					break;
				case ENCARGOS:
					hql.append(" order by encargos ");
					break;
				case VALOR:
					hql.append(" order by valor ");
					break;
				case TIPO_BAIXA:
					hql.append(" order by tipoBaixa ");
					break;
				case STATUS:
					hql.append(" order by status ");
					break;
				case ACAO:
					hql.append(" order by acao ");
					break;	
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("ncota", filtro.getNumeroCota());
		
		if (filtro.getDataVencimentoDe()!=null){
		    query.setParameter("vctode", filtro.getDataVencimentoDe());
		}
		if (filtro.getDataVencimentoAte()!=null){
		    query.setParameter("vctoate", filtro.getDataVencimentoAte());
		}
		if (filtro.getStatusCobranca()!=null){	
		    query.setParameter("status", filtro.getStatusCobranca());
		}

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
	
	public Boleto obterPorNossoNumero(String nossoNumero) {
		
		Criteria criteria = super.getSession().createCriteria(Boleto.class);
		
		criteria.add(Restrictions.eq("nossoNumero", nossoNumero));
		
		criteria.setMaxResults(1);
		
		return (Boleto) criteria.uniqueResult();
	}

}



