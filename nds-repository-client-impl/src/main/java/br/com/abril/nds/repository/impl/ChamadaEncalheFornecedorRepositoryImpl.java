package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroFechamentoCEIntegracaoDTO;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.integracao.StatusIntegracaoNFE;
import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ChamadaEncalheFornecedorRepository;
import br.com.abril.nds.util.Intervalo;

@Repository
public class ChamadaEncalheFornecedorRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalheFornecedor, Long> implements
        ChamadaEncalheFornecedorRepository {

    public ChamadaEncalheFornecedorRepositoryImpl() {
        super(ChamadaEncalheFornecedor.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(Long idFornecedor, Integer numeroSemana, Intervalo<Date> periodo) {
        StringBuilder builder = new StringBuilder("select distinct(cef) from ChamadaEncalheFornecedor as cef ");
        builder.append("join cef.itens as item ");
        builder.append("join item.produtoEdicao as pe ");
        builder.append("join pe.produto as p ");
        builder.append("join p.fornecedores as f ");
        Map<String, Object> parametros = new HashMap<String, Object>();
        if (numeroSemana != null) {
            builder.append("where cef.numeroSemana = :numeroSemana ");
            parametros.put("numeroSemana", numeroSemana);
        } else {
            builder.append("where item.dataRecolhimento between :dataInicio and :dataFim ");
            parametros.put("dataInicio", periodo.getDe());
            parametros.put("dataFim", periodo.getAte());
            
        }
        if (idFornecedor != null) {
            builder.append("and f.id = :idFornecedor ");
            parametros.put("idFornecedor", idFornecedor);
        }
        Query query = getSession().createQuery(builder.toString());
        for (Entry<String, Object> entry : parametros.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
    }
    
    public List<ChamadaEncalheFornecedor> obterChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select cef ")
    		.append(" from ChamadaEncalheFornecedor as cef ")
    		.append(" join cef.fornecedor f ");
    	
    	hql.append(" where cef.numeroSemana = :numeroSemana and cef.anoReferencia = :anoReferencia ");
    	
    	if(filtro.getIdFornecedor()!= null) {
    		hql.append(" and f.id = :idFornecedor ");
    	}
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("numeroSemana", filtro.getNumeroSemana());
    	query.setParameter("anoReferencia", filtro.getAnoReferente());
    	
    	if(filtro.getIdFornecedor()!= null){
    		query.setParameter("idFornecedor", filtro.getIdFornecedor());
    	}
    	
    	return query.list();
    }
    
    public List<Long> obterIdentificadorFornecedoresChamadasEncalheFornecedor(FiltroFechamentoCEIntegracaoDTO filtro){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select distinct f.id ")
    		.append(" from ChamadaEncalheFornecedor as cef ")
			.append(" join cef.fornecedor f ");
	
    	hql.append(" where cef.numeroSemana = :numeroSemana and cef.anoReferencia = :anoReferencia ");
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("numeroSemana", filtro.getNumeroSemana());
    	query.setParameter("anoReferencia", filtro.getAnoReferente());
    	
    	return query.list();
    }
    
    public Long obterQuantidadeItensIntegracaoNFE(FiltroFechamentoCEIntegracaoDTO filtro, StatusIntegracaoNFE... statusIntegracaoNFE){
    	
    	StringBuilder hql = new StringBuilder();
    	
    	hql.append(" select count(cef.id) ")
    		.append(" from ChamadaEncalheFornecedor as cef ");
    	
    	if(filtro.getIdFornecedor()!= null) {
    		hql.append(" join fetch cef.itens itens ")
    			.append(" join fetch itens.produtoEdicao pe ")
    			.append(" join fetch pe.produto p ")
    			.append(" join fetch p.fornecedores f ");
    	}
    	
    	hql.append(" where cef.numeroSemana = :numeroSemana and cef.anoReferencia = :anoReferencia ");
    	
    	hql.append(" and cef.statusIntegracaoNFE IN(:statusIntegracaoNFE) ");
    	
    	if(filtro.getIdFornecedor()!= null) {
    		hql.append(" and f.id = :idFornecedor ");
    	}
    	
    	Query query = getSession().createQuery(hql.toString());
    	
    	query.setParameter("numeroSemana", filtro.getNumeroSemana());
    	query.setParameter("anoReferencia", filtro.getAnoReferente());
    	query.setParameterList("statusIntegracaoNFE", statusIntegracaoNFE);
    	
    	if(filtro.getIdFornecedor()!= null){
    		query.setParameter("idFornecedor", filtro.getIdFornecedor());
    	}
    	
    	return (Long) query.uniqueResult();
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