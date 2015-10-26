package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;

@Repository
public class ChamadaEncalheFornecedorRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalheFornecedor, Long> implements
        ChamadaEncalheFornecedorRepository {

    public ChamadaEncalheFornecedorRepositoryImpl() {
        super(ChamadaEncalheFornecedor.class);
    }
 
    public List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select cef ")
    		.append(" from ChamadaEncalheFornecedor as cef ");
    	
    	hql.append(" where cef.numeroSemana = :numeroSemana and cef.anoReferencia = :anoReferencia ");
    	
    	if(filtro.getCodigoDistribuidorFornecdor()!= null) {
    		hql.append(" and cef.codigoDistribuidor = :codigoDistInterface ");
    	}
    	
    	if(filtro.getIdChamadaEncalhe() != null) {
    		hql.append(" and cef.numeroChamadaEncalhe = :numeroChamadaEncalhe ");
    	}
    	hql.append(" order by cef.numeroChamadaEncalhe desc");
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("numeroSemana", filtro.getNumeroSemana());
    	query.setParameter("anoReferencia", filtro.getAnoReferente());
    	
    	if(filtro.getCodigoDistribuidorFornecdor()!= null){
    		query.setParameter("codigoDistInterface", filtro.getCodigoDistribuidorFornecdor().longValue());
    	}
    	if(filtro.getIdChamadaEncalhe() != null) {
    		query.setParameter("numeroChamadaEncalhe", filtro.getIdChamadaEncalhe());
    	}
    	
    	return query.list();
    }
    
    public List<Long> obterIdentificadorFornecedoresChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select distinct cef.codigoDistribuidor ")
    		.append(" from ChamadaEncalheFornecedor as cef ");
	
    	hql.append(" where cef.numeroSemana = :numeroSemana and cef.anoReferencia = :anoReferencia ");
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("numeroSemana", filtro.getNumeroSemana());
    	query.setParameter("anoReferencia", filtro.getAnoReferente());
    	
    	return query.list();
    }

    /**
     * Obtém lista de CE Fornecedor com Diferença(Perda/Ganho) Pendente
     * 
     * @param listaIdCeFornecedor
     * @param statusConfirmacao
     * @return List<ChamadaEncalheFornecedor>
     */
    @SuppressWarnings("unchecked")
	@Override
    public List<ChamadaEncalheFornecedor> obtemCEFornecedorComDiferencaPendente(List<Long> listaIdCeFornecedor, StatusConfirmacao statusConfirmacao) {
		
        StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select cef ")
    	
    	   .append(" from ChamadaEncalheFornecedor as cef ")

    	   .append(" join cef.itens itens ")
    		
    	   .append(" join itens.diferenca dif ")
    	
    	   .append(" where dif.statusConfirmacao = :statusConfirmacao ");

    	if (listaIdCeFornecedor!=null && !listaIdCeFornecedor.isEmpty()){
    		
    		hql.append(" and cef.id in (:listaIdCeFornecedor) ");
    	}

    	Query query = getSession().createQuery(hql.toString());

    	query.setParameter("statusConfirmacao", statusConfirmacao);
    	
    	if(listaIdCeFornecedor != null && !listaIdCeFornecedor.isEmpty()){
    		
    		query.setParameterList("listaIdCeFornecedor", listaIdCeFornecedor);
    	}
    	
    	return query.list();
	}

}