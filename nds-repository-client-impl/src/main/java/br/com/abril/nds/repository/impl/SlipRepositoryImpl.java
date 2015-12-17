package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.movimentacao.Slip;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.SlipRepository;

@Repository
public class SlipRepositoryImpl extends AbstractRepositoryModel<Slip, Long> implements SlipRepository {
    
    public SlipRepositoryImpl() {
        super(Slip.class);
    }

    @Override
    public Slip obterPorNumeroCotaData(Integer numeroCota, Date dataConferencia) {
        
        Query query = 
                this.getSession().createQuery(
                        "select s from Slip s where s.numeroCota = :numeroCota and date(s.dataConferencia) = :dataConferencia");
        
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("dataConferencia", dataConferencia);
        
        return (Slip) query.uniqueResult();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<DebitoCreditoCota> obterComposicaoSlip(Long idSlip, boolean composicao){
        
        Query query = 
                this.getSession().createQuery(
                        "select d from DebitoCreditoCota d where d.slip.id = :idSlip and d.composicaoCobranca = :composicao ");
        
        query.setParameter("idSlip", idSlip);
        query.setParameter("composicao", composicao);
        
        return query.list();
    }

	@Override
	@SuppressWarnings("unchecked")
	public Map<Integer, List<Slip>> obterSlipsPorCotasData(List<Integer> listaCotas, Date dataDe, Date dataAte) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select s from Slip s where s.numeroCota in (:cotas)");
		
		if(dataAte != null){
			hql.append("  and date(s.dataConferencia) BETWEEN :dataConferenciaDe AND :dataConferenciaAte ");
		}else{
			hql.append("  and date(s.dataConferencia) = :dataConferenciaDe ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
	        
        query.setParameterList("cotas", listaCotas);
        query.setParameter("dataConferenciaDe", dataDe);
        
        if(dataAte != null){
        	query.setParameter("dataConferenciaAte", dataAte);
        }
        
       List<Slip> listaSlips = query.list();
       
       Map<Integer, List<Slip>> mapComSlips = new HashMap<>();
       
       for (Slip slip : listaSlips) {
    	   
    	   if(mapComSlips.get(slip.getNumeroCota()) == null){
    		   
    		   List<Slip> slips = new ArrayList<>();
    		   slips.add(slip);
    		   
    		   mapComSlips.put(slip.getNumeroCota(), slips);
    	   }else{
    		   List<Slip> slips = mapComSlips.get(slip.getNumeroCota());
    		   slips.add(slip);
    		   mapComSlips.put(slip.getNumeroCota(), slips);
    	   }
       }
       
       return mapComSlips;

	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Long> obterIdsSlipsPorCotasDataOrdenados(List<Integer> listaCotas, Date dataDe, Date dataAte) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		
		sql.append("  s.id as id  ");
		
		sql.append(" from Slip s  ");
		
		sql.append("   join cota c  ");
		sql.append("     on c.NUMERO_COTA = s.NUMERO_COTA ");
		sql.append("   join pdv  ");
		sql.append("     on pdv.COTA_ID = c.ID and PDV.PONTO_PRINCIPAL = true ");
		sql.append("   join rota_pdv rPdv ");
		sql.append("     on rPdv.PDV_ID = pdv.ID ");
		sql.append("   join rota  ");
		sql.append("     ON rPdv.ROTA_ID = rota.ID ");
		sql.append("   join roteiro  ");
		sql.append("     ON rota.ROTEIRO_ID = roteiro.ID ");
		sql.append("   join box  ");
		sql.append("     ON c.BOX_ID = box.ID ");
		sql.append(" where  ");
		sql.append("   s.NUMERO_COTA in (:cotas) and  ");
		
		if(dataAte != null){
			sql.append("   date(s.DATA_CONFERENCIA) BETWEEN :dataConferenciaDe AND :dataConferenciaAte ");
		}else{
			sql.append("   date(s.DATA_CONFERENCIA) = :dataConferenciaDe ");
		}
		
		sql.append("   group by s.ID ");
		sql.append("   order by BOX.CODIGO, ROTEIRO.ORDEM, ROTA.ORDEM, rPdv.ORDEM ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
	        
        query.setParameterList("cotas", listaCotas);
        query.setParameter("dataConferenciaDe", dataDe);
        
        if(dataAte != null){
        	query.setParameter("dataConferenciaAte", dataAte);
        }
        
        ((SQLQuery) query).addScalar("id", StandardBasicTypes.LONG);
		
        return (List<Long>)query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Integer> obterCotasRoteirizadas(List<Integer> listaCotas) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		
		sql.append(" 	c.numero_cota numCota ");
		
		sql.append(" from cota c ");
		sql.append("   join pdv  ");
		sql.append("     on pdv.COTA_ID = c.ID ");
		sql.append("     and PDV.PONTO_PRINCIPAL = true ");
		sql.append("   join rota_pdv ");
		sql.append("     on rota_pdv.PDV_ID = pdv.ID ");
		sql.append("   join rota  ");
		sql.append("     on rota_pdv.ROTA_ID = rota.ID ");
		sql.append("   join roteiro  ");
		sql.append("     ON rota.ROTEIRO_ID = roteiro.ID   ");
		sql.append("   join roteirizacao  ");
		sql.append("     ON roteiro.ROTEIRIZACAO_ID = roteirizacao.ID ");
		sql.append("   join box  ");
		sql.append("     ON roteirizacao.BOX_ID = box.ID ");
		
		sql.append(" where c.NUMERO_COTA in (:cotas) ");
		sql.append(" and box.tipo_box <> 'ESPECIAL' ");
		sql.append(" group by BOX.CODIGO, c.NUMERO_COTA ");
		sql.append(" order by BOX.CODIGO, ROTEIRO.ORDEM, ROTA.ORDEM, rota_pdv.ORDEM ");
		
		
		Query query = this.getSession().createSQLQuery(sql.toString());
	        
        query.setParameterList("cotas", listaCotas);
        
        ((SQLQuery) query).addScalar("numCota", StandardBasicTypes.INTEGER);
		
        return (List<Integer>)query.list();
	}
}
