package br.com.abril.nds.repository.impl;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.repository.PoliticaCobrancaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.PoliticaCobranca}
 * 
 * @author Discover Technology
 */
@Repository
public class PoliticaCobrancaRepositoryImpl extends AbstractRepository<PoliticaCobranca,Long> implements PoliticaCobrancaRepository {

	
	/**
	 * Construtor padrão
	 */
	public PoliticaCobrancaRepositoryImpl() {
		super(PoliticaCobranca.class);
	}
	
	@Override
	public PoliticaCobranca obterPorTipoCobranca(TipoCobranca tipoCobranca) {
		
		Criteria criteria = super.getSession().createCriteria(PoliticaCobranca.class);
		
		criteria.createAlias("formaCobranca", "formaCobranca");
		
		criteria.add(Restrictions.eq("formaCobranca.tipoCobranca", tipoCobranca));
		
		criteria.setMaxResults(1);
		
		return (PoliticaCobranca) criteria.uniqueResult();
	}

	@Override
	public PoliticaCobranca buscarPoliticaCobrancaPrincipal() {
		Query query = this.getSession().createQuery("select p from PoliticaCobranca p where p.principal=true");
		query.setMaxResults(1);
		
		return (PoliticaCobranca) query.uniqueResult();
	}

	
	/**
	 * Obtém Lista de políticas de cobrança para os parametros
	 * @return List<PoliticaCobranca>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PoliticaCobranca>  obterPoliticasCobranca(FiltroParametrosCobrancaDTO filtro) {

		StringBuilder hql = new StringBuilder();
		hql.append(" from PoliticaCobranca p ");		
		hql.append(" where p.ativo = :ativo ");
		
		if ((filtro.getIdBanco()!=null)){
		    hql.append(" and p.formaCobranca.banco.id = :pIdBanco ");
		}
		if ((filtro.getTipoCobranca()!=null)){
		    hql.append(" and p.formaCobranca.tipoCobranca = :pTipoCobranca ");
		}

		if (filtro.getOrdenacaoColuna() != null) {
			switch (filtro.getOrdenacaoColuna()) {
				case TIPO_COBRANCA:
					hql.append(" order by p.formaCobranca.tipoCobranca ");
					break;
				case NOME_BANCO:
					hql.append(" order by p.formaCobranca.banco ");
					break;
				case VALOR_MINIMO_EMISSAO:
					hql.append(" order by p.formaCobranca.valorMinimoEmissao ");
					break;
				case ACUMULA_DIVIDA:
					hql.append(" order by p.acumulaDivida ");
					break;
				case COBRANCA_UNIFICADA:
					hql.append(" order by p.unificaConbranca ");
					break;
				case FORMA_EMISSAO:
					hql.append(" order by p.formaEmissao ");
					break;
				case ENVIO_EMAIL:
					hql.append(" order by p.formaCobranca.recebeCobrancaEmail ");
					break;
				
				default:
					break;
			}
			if (filtro.getPaginacao().getOrdenacao() != null) {
				hql.append(filtro.getPaginacao().getOrdenacao().toString());
			}	
		}
		
        Query query = super.getSession().createQuery(hql.toString());
		
        query.setParameter("ativo", true);

        
        if ((filtro.getIdBanco()!=null)){
        	query.setParameter("pIdBanco", filtro.getIdBanco());
		}
		if ((filtro.getTipoCobranca()!=null)){
			query.setParameter("pTipoCobranca", filtro.getTipoCobranca());
		}
       
        if (filtro.getPaginacao() != null) {
			if (filtro.getPaginacao().getPosicaoInicial() != null) {
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			}
			
			if (filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
			}
		}
		
		return query.list();
	}
	
	/**
	 * Obtém Quantidade de políticas de cobrança para os parametros
	 * @return int
	 */
	@Override
	public int obterQuantidadePoliticasCobranca(FiltroParametrosCobrancaDTO filtro) {
		long quantidade = 0;
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(p) from PoliticaCobranca p ");		
		hql.append(" where p.ativo = :ativo ");
		
		if ((filtro.getIdBanco()!=null)){
		    hql.append(" and p.formaCobranca.banco.id = :pIdBanco ");
		}
		if ((filtro.getTipoCobranca()!=null)){
		    hql.append(" and p.formaCobranca.tipoCobranca = :pTipoCobranca ");
		}
		
        Query query = super.getSession().createQuery(hql.toString());
        query.setParameter("ativo", true);
        
        if ((filtro.getIdBanco()!=null)){
        	query.setParameter("pIdBanco", filtro.getIdBanco());
		}
		if ((filtro.getTipoCobranca()!=null)){
			query.setParameter("pTipoCobranca", filtro.getTipoCobranca());
		}

        quantidade = (Long) query.uniqueResult();
		return (int) quantidade;
	}

}



