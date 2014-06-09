package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.BoletoEmail;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.BoletoEmailRepository;

@Repository
public class BoletoEmailRepositoryImpl extends AbstractRepositoryModel<BoletoEmail,Long> implements BoletoEmailRepository  {

	/**
	 * Construtor padrão
	 */
	public BoletoEmailRepositoryImpl() {
		super(BoletoEmail.class);
	}
	
	/**
	 * Obtem controle de envio de boletos por email por cobranca
	 * @param cobrancaId
	 * @return BoletoEmail
	 */
	@Override
    public BoletoEmail obterBoletoEmailPorCobranca(Long cobrancaId){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select be from BoletoEmail be where be.cobranca.id = :cobrancaId");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("cobrancaId", cobrancaId);
		
		return (BoletoEmail) query.uniqueResult();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<BoletoEmail> buscarTodosOrdenados() {
        
        return this.getSession().createQuery(
                "select b from BoletoEmail b order by b.cobranca.cota.numeroCota").list();
    }
}