package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.repository.NotaFiscalRepository;


/**
 * Implementação do repositório de Pessoa
 
 * @author francisco.garcia
 *
 */
@Repository
public class NotaFiscalRepositoryImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalRepository {

	public NotaFiscalRepositoryImpl() {
		super(NotaFiscal.class);
	}
	
	@Override	
	public void inserirNotaFiscal(NotaFiscal notaFiscal){
		this.adicionar(notaFiscal);
		
	}
	@Override	
	public NotaFiscal obterNotaFiscalPorNumero(String numero){
		String hql = "from NotaFiscal nf where nf.numero = :numero ";
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numero", numero);
		return (NotaFiscal) query.uniqueResult();
	}	
}
