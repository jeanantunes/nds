package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RepartePDVRepository;
/**
 * Classe de implementação referente ao acesso a dados da entidade
 *
 * FixacaoReparte
 */

@Repository
public class RepartePDVRepositoryImpl extends  AbstractRepositoryModel<RepartePDV, Long> implements RepartePDVRepository {
 
	public RepartePDVRepositoryImpl() {
		super(RepartePDV.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public RepartePDV buscarPorId(Long id){
		return super.buscarPorId(id);
	}
	
	 public List<RepartePDVDTO> obterRepartePdvMixPorCota(Long idCota){
		 StringBuilder hql = new StringBuilder();
		hql.append(" SELECT  pdv.nome as nomePDV, rep.reparte as reparte, " )
			.append(" coalesce(mix.cota.pessoa.nomeFantasia, mix.cota.pessoa.razaoSocial, mix.cota.pessoa.nome, '')  as nomeCota, ")
			.append("  mix.produto.tipoClassificacaoProduto.descricao as classificacaoProduto, ")
			.append("  endereco.logradouro  as  endereco ")
			.append(" FROM RepartePDV rep, MixCotaProduto mix ")
			.append(" RIGHT JOIN rep.pdv pdv ")
			.append(" JOIN pdv.cota cota ")
			.append(" LEFT JOIN pdv.enderecos enderecoPdv ")
			.append(" LEFT JOIN enderecoPdv.endereco endereco ")
			.append(" LEFT JOIN pdv.telefones telefonePdv ")
			.append(" LEFT JOIN telefonePdv.telefone telefone ")
			.append(" WHERE cota.numeroCota = :numeroCota ");
			
			Query q = getSession().createQuery(hql.toString());
	        q.setParameter("numeroCota", idCota);
	        q.setResultTransformer(new AliasToBeanResultTransformer(PdvDTO.class));
	        return q.list();
			
	 }
	 
	@Override
	public RepartePDV obterRepartePorPdv(Long idFixacao, Long idProduto, Long idPdv){
		 StringBuilder hql = new StringBuilder();

		hql.append(" from RepartePDV rep " )
			.append(" WHERE rep.pdv.id = :idPdv ")
			.append(" and  rep.produto.id = :idProduto ")
			.append(" and  rep.fixacaoReparte.id = :idFixacao ");
	        	
		Query q = getSession().createQuery(hql.toString());
		 q.setParameter("idFixacao", idFixacao);
        q.setParameter("idProduto", idProduto);
        q.setParameter("idPdv", idPdv);
        return (RepartePDV) q.uniqueResult();

	        		
	}
	
	public RepartePDV obterRepartePdvMix(Long idMix, Long idProduto, Long idPdv){
		 StringBuilder hql = new StringBuilder();

			hql.append(" from RepartePDV rep " )
				.append(" WHERE rep.pdv.id = :idPdv ")
				.append(" and  rep.produto.id = :idProduto ")
				.append(" and  rep.mixCotaProduto.id = :idMix ");
		        	
			Query q = getSession().createQuery(hql.toString());
	        q.setParameter("idMix", idMix);
	        q.setParameter("idProduto", idProduto);
	        q.setParameter("idPdv", idPdv);
	        return (RepartePDV) q.uniqueResult();
		
	}
	
	
}



