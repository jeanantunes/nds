package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.repository.FecharDiaRepository;

@Repository
public class FecharDiaRepositoryImpl extends AbstractRepository implements FecharDiaRepository {

	@Override
	public boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from Cobranca c where ");		
		hql.append(" c.statusCobranca = :statusCobranca");		
		hql.append(" and c.dataVencimento = :diaDeOperaoMenosUm ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
		query.setParameter("diaDeOperaoMenosUm", diaDeOperaoMenosUm);
		
		return query.list().isEmpty() ? false : true;
	}

	@Override
	public boolean existeNotaFiscalSemRecebimentoFisico() {

		StringBuilder hql = new StringBuilder();

		hql.append(" select notaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");
		
		hql.append("WHERE notaFiscal.statusNotaFiscal = :statusNF ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusNF", StatusNotaFiscalEntrada.RECEBIDA);
		
		return query.list().isEmpty() ? true : false;
	}

}
