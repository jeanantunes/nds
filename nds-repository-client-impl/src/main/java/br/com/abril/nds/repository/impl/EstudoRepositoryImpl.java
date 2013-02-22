package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.StatusEstudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoRepositoryImpl extends AbstractRepositoryModel<Estudo, Long> implements EstudoRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoRepositoryImpl() {
		
		super(Estudo.class);
	}

	
	@Override
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		String hql = " from Estudo estudo"
				   + " where estudo.dataLancamento = :dataReferencia "
				   + " and estudo.produtoEdicao.id = :idProdutoEdicao "
				   + " order by estudo.dataLancamento ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("dataReferencia", dataReferencia);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setMaxResults(1);
		
		return (Estudo) query.uniqueResult();
	}
	
	@Override
	public void alterarStatusEstudos(List<Long> listIdEstudos, StatusEstudo status) {
		
		StringBuilder hql = new StringBuilder("update Estudo set");
		hql.append(" status = :statusEstudo")
		   .append(" where id in (:listIdEstudos)");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("statusEstudo", status);
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}
	

}
