package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.model.cadastro.TipoTelefone;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TelefoneEntregadorRepository;

/**
 * Repositório de dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TelefoneEntregador}
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class TelefoneEntregadorRepositoryImpl extends
		AbstractRepositoryModel<TelefoneEntregador, Long> implements
		TelefoneEntregadorRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TelefoneEntregadorRepositoryImpl.class);

	/**
	 * Construtor.
	 */
	public TelefoneEntregadorRepositoryImpl() {
		super(TelefoneEntregador.class);
	}

	/**
	 * @see br.com.abril.nds.repository.TelefoneEntregadorRepository#removerTelefoneEntregadorPorIdEntregador(java.lang.Long)
	 */
	@Override
	public void removerTelefoneEntregadorPorIdEntregador(Long idEntregador) {

		StringBuilder hql = new StringBuilder();

		hql.append(" delete from TelefoneEntregador telefoneEntregador ")
		   .append(" where telefoneEntregador.entregador.id = :idEntregador");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idEntregador", idEntregador);

		query.executeUpdate();
	}

	/**
	 * @see br.com.abril.nds.repository.TelefoneEntregadorRepository#removerTelefonesEntregador(java.util.Collection)
	 */
	@Override
	public void removerTelefonesEntregador(Collection<Long> listaTelefonesEntregador) {

		StringBuilder hql = new StringBuilder("delete from TelefoneEntregador ");

		hql.append(" where telefone.id in (:idsEntregador) ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameterList("idsEntregador", listaTelefonesEntregador);

		query.executeUpdate();
	}

	/**
	 * @see br.com.abril.nds.repository.TelefoneEntregadorRepository#buscarTelefonesEntregador(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<TelefoneAssociacaoDTO> buscarTelefonesEntregador(Long idEntregador) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cast(telefoneEntregador.id as integer) as referencia, telefoneEntregador.telefone as telefone, ")
		   .append(" telefoneEntregador.principal as principal, ")
		   .append(" telefoneEntregador.tipoTelefone as tipoTelefone ")
		   .append(" from TelefoneEntregador telefoneEntregador ")
		   .append(" where telefoneEntregador.entregador.id = :idEntregador ");

		Query query = getSession().createQuery(hql.toString());

        ResultTransformer resultTransformer = null;

        try {
            resultTransformer = new AliasToBeanConstructorResultTransformer(
                    TelefoneAssociacaoDTO.class.getConstructor(Integer.class,
                            Telefone.class, boolean.class, TipoTelefone.class));
        } catch (Exception e) {
            String message = "Erro criando result transformer para classe: "
                    + TelefoneAssociacaoDTO.class.getName();
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
        
        query.setResultTransformer(resultTransformer);
		
		query.setParameter("idEntregador", idEntregador);
		
		return query.list();
	}
}
