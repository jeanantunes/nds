package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
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
	public List<Slip> obterSlipsPorCotasData(List<Integer> listaCotas, Date data) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select s from Slip s where s.numeroCota in (:cotas) and date(s.dataConferencia) = :dataConferencia");
		
		Query query = this.getSession().createQuery(hql.toString());
	        
        query.setParameterList("cotas", listaCotas);
        query.setParameter("dataConferencia", data);
        
        return query.list();
	}
}
