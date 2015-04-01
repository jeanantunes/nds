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
	
	@Override
	public RepartePDV buscarPorId(Long id){
		return super.buscarPorId(id);
	}
	
	 @SuppressWarnings("unchecked")
	public List<RepartePDVDTO> obterRepartePdvMixPorCota(Long idCota){
		 StringBuilder hql = new StringBuilder();
		hql.append(" SELECT  pdv.nome as nomePDV, rep.reparte as reparte, " )
			.append(" coalesce(mix.cota.pessoa.nomeFantasia, mix.cota.pessoa.razaoSocial, mix.cota.pessoa.nome, '')  as nomeCota, ")
//			.append("  mix.produto.tipoClassificacaoProduto.descricao as classificacaoProduto, ") //FIXME tipoClassificacaoProduto é atributo de produtoEdicao
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

	@Override
	public List<RepartePDV> buscarPorIdFixacao(Long id) {
		return this.buscarPorIdTipoReferencia("fixacaoReparte",id);
	}
	
	@Override
	public List<RepartePDV> buscarPorIdMix(Long id) {
		return this.buscarPorIdTipoReferencia("mixCotaProduto",id);
	}
	
	@SuppressWarnings("unchecked")
	public List<RepartePDV> buscarPorCota(Long idCota) {
		
		Query query = this.getSession().createQuery(" from RepartePDV rep WHERE rep.mixCotaProduto.cota.id = :idCota ");
		
		query.setParameter("idCota", idCota);
        
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<RepartePDV> buscarPorProduto(Long produtoId) {
		
		Query query = this.getSession().createQuery(" from RepartePDV rep WHERE rep.produto.id = :produtoId ");
		
		query.setParameter("produtoId", produtoId);
        
        return query.list();
	}
	
	@SuppressWarnings("unchecked")
	private List<RepartePDV> buscarPorIdTipoReferencia(String type,Long id) {
		Query q = getSession().createQuery(" from RepartePDV rep WHERE rep."+type+".id = :idTipo ");
        q.setParameter("idTipo", id);
        return (List<RepartePDV>)q.list();
		
	}

    @Override
    public boolean verificarRepartePdv(final Integer numeroCota, final String codigoProduto) {
        
        final Query query = 
                this.getSession().createQuery(
                        "select count(rpdv.id) from RepartePDV rpdv " +
                        " join rpdv.pdv pd " +
                        " join pd.cota c " +
                        " join rpdv.produto p " +
                        " where c.numeroCota = :numeroCota " +
                        " and p.codigo = :codigoProduto ");
        
        query.setParameter("numeroCota", numeroCota);
        query.setParameter("codigoProduto", codigoProduto);
        
        return ((Long)query.uniqueResult() > 0);
    }
}
