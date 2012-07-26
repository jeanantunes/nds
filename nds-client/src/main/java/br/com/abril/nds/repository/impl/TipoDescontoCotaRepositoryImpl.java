package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.repository.TipoDescontoCotaRepository;

@Repository
public class TipoDescontoCotaRepositoryImpl extends AbstractRepositoryModel<TipoDescontoCota,Long> implements TipoDescontoCotaRepository {

	public TipoDescontoCotaRepositoryImpl() {
		super(TipoDescontoCota.class);		 
	}

//	@Override
//	public int obterSequencial() {
//		StringBuilder hql = new StringBuilder();
//
//		hql.append(
//				" SELECT MAX(tipo.sequencial) FROM TipoDescontoCota as tipo");
//
//		Query query = getSession().createQuery(hql.toString());
//
//		return (Integer) query.uniqueResult();
//	}

	@Override
	public Integer buscarTotalDescontoPorCota() {
		 
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(desconto) FROM TipoDescontoCota as desconto  ");		
		
		Query query =  getSession().createQuery(hql.toString());
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

	@Override
	public void incluirDesconto(TipoDescontoCota tipoDescontoCota) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void excluirDesconto(TipoDescontoCota tipoDescontoCota) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TipoDescontoCota obterTipoDescontoCotaPorId(long idDesconto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer buscarTotalDescontosPorCota() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDescontoCotaDTO> obterTipoDescontosCota(Cota cota) {

		StringBuilder hql = new StringBuilder();

		hql.append("SELECT cota.numeroCota as numeroCota, ");
		hql.append("pessoa.nome as nomeCota, ");
		hql.append("desconto.desconto as desconto, ");
		hql.append("desconto.dataAlteracao as dataAlteracao, ");
		hql.append("usuario.nome as nomeUsuario ");
		
		hql.append(" from TipoDescontoCota as desconto ");
		hql.append(" LEFT JOIN desconto.cota as cota ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN desconto.usuario as usuario ");
		
		if(cota != null){
			hql.append(" where cota.id = :idCota ");
		}

		Query query = getSession().createQuery(hql.toString());
		
		if(cota != null){
			query.setParameter("idCota", cota.getId());
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(TipoDescontoCotaDTO.class));
		
		return query.list();
	}

	

}
