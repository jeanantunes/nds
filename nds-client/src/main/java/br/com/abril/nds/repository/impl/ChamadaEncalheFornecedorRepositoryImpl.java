package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.fornecedor.ChamadaEncalheFornecedor;
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
        Map<String, Object> parametros = new java.util.HashMap<>();
        if (numeroSemana != null) {
            builder.append("where cef.numeroSemana = :numeroSemana ");
            parametros.put("numeroSemana", numeroSemana);
        } else {
            builder.append("where item.dataRecolhimento between :dataInicio and :dataFim ");
            parametros.put("dataInicio", periodo.getDe());
            parametros.put("dataFim", periodo.getAte());
            
        }
        if (idFornecedor != null) {
            builder.append("and cef.fornecedor.id = :idFornecedor ");
            parametros.put("idFornecedor", idFornecedor);
        }
        Query query = getSession().createQuery(builder.toString());
        for (Entry<String, Object> entry : parametros.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.list();
    }

}