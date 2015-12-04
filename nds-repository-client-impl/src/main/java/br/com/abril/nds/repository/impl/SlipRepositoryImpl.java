package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
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
	public List<Slip> obterSlipsPorCotasData(List<Integer> listaCotas, Date dataDe, Date dataAte) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select  ");
		
		sql.append("  s.id as id,  ");
		sql.append("  s.ce_jornaleiro ceJornaleiro,  ");
		sql.append("  s.codigo_box codigoBox,  ");
		sql.append("  s.data_conferencia dataConferencia,  ");
		sql.append("  s.descricao_rota descricaoRota,  ");
		sql.append("  s.descricao_roteiro descricaoRoteiro,  ");
		sql.append("  s.nome_cota nomeCota,  ");
		sql.append("  s.num_slip numSlip,  ");
		sql.append("  s.numero_cota numeroCota,  ");
		sql.append("  s.pagamento_pendente pagamentoPendente,  ");
		sql.append("  s.total_produto_dia totalProdutoDia,  ");
		sql.append("  s.total_produtos totalProdutos,  ");
		sql.append("  s.valor_credito_dif valorCreditoDif,  ");
		sql.append("  s.valor_devido valorDevido,  ");
		sql.append("  s.valor_encalhe_dia valorEncalheDia,  ");
		sql.append("  s.valor_liquido_devido valorLiquidoDevido,  ");
		sql.append("  s.valor_slip valorSlip,  ");
		sql.append("  s.valor_total_desconto valorTotalDesconto,  ");
		sql.append("  s.valor_total_encalhe valorTotalEncalhe,  ");
		sql.append("  s.valor_total_pagar valorTotalPagar,  ");
		sql.append("  s.valor_total_reparte valorTotalReparte,  ");
		sql.append("  s.valor_total_sem_desconto valorTotalSemDesconto ");
		
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
        ((SQLQuery) query).addScalar("ceJornaleiro", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("codigoBox", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("dataConferencia", StandardBasicTypes.DATE);
        ((SQLQuery) query).addScalar("descricaoRota", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("descricaoRoteiro", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("nomeCota", StandardBasicTypes.STRING);
        ((SQLQuery) query).addScalar("numSlip", StandardBasicTypes.LONG);
        ((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
        ((SQLQuery) query).addScalar("pagamentoPendente", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("totalProdutoDia", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("totalProdutos", StandardBasicTypes.BIG_INTEGER);
        ((SQLQuery) query).addScalar("valorCreditoDif", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorDevido", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorEncalheDia", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorLiquidoDevido", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorSlip", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorTotalDesconto", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorTotalEncalhe", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorTotalPagar", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorTotalReparte", StandardBasicTypes.BIG_DECIMAL);
        ((SQLQuery) query).addScalar("valorTotalSemDesconto", StandardBasicTypes.BIG_DECIMAL);
		
        query.setResultTransformer(new AliasToBeanResultTransformer(Slip.class));
        
        return query.list();
	}
}
