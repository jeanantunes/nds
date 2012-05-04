package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TransportadorRepository;

@Repository
public class TransportadorRepositoryImpl extends
		AbstractRepository<Transportador, Long> implements TransportadorRepository {

	public TransportadorRepositoryImpl() {
		super(Transportador.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConsultaTransportadorDTO pesquisarTransportadoras(FiltroConsultaTransportadorDTO filtro) {
		
		ConsultaTransportadorDTO consultaTransportadorDTO = new ConsultaTransportadorDTO();
		
		StringBuilder hql = new StringBuilder("select distinct tr from Transportador tr, PessoaJuridica j ");
		hql.append(" left join tr.telefonesTransportador telefoneTransportador ")
		   .append(" left join telefoneTransportador.telefone telefone ")
		   .append(" where tr.pessoaJuridica.id = j.id ");
		
		if (filtro.getRazaoSocial() != null && !filtro.getRazaoSocial().isEmpty()){
			
			hql.append(" and (tr.pessoaJuridica.razaoSocial like :razaoSocial) ");
		}
		
		if (filtro.getNomeFantasia() != null && !filtro.getNomeFantasia().isEmpty()){
			
			hql.append(" and (tr.pessoaJuridica.nomeFantasia like :nomeFantasia) ");
		}
		
		if (filtro.getCnpj() != null && !filtro.getCnpj().isEmpty()){
			
			hql.append(" and (tr.pessoaJuridica.cnpj = :cnpj) ");
		}
		
		consultaTransportadorDTO.setQuantidadeRegistros(this.obterQuantidadeRegistros(hql.toString(), filtro));
		
		switch (filtro.getOrdenacaoColunaTransportador()){
			case CODIGO:
				hql.append(" order by tr.id ");
			break;
			case CNPJ:
				hql.append(" order by j.cnpj ");
			break;
			case EMAIL:
				hql.append(" order by j.email ");
			break;
			case RAZAO_SOCIAL:
				hql.append(" order by j.razaoSocial ");
			break;
			case RESPONSAVEL:
				hql.append(" order by tr.responsavel ");
			break;
			case TELEFONE:
				hql.append(" order by telefone.numero ");
			break;
		}
	
		hql.append(filtro.getPaginacaoVO().getOrdenacao());
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.setarParametros(query, filtro);
		
		query.setMaxResults(filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		
		query.setFirstResult(
				(filtro.getPaginacaoVO().getPaginaAtual() - 1) * filtro.getPaginacaoVO().getQtdResultadosPorPagina());
		
		List<Transportador> transportador = query.list();
		
		consultaTransportadorDTO.setTransportadores(transportador);
		
		return consultaTransportadorDTO;
	}
	
	private Long obterQuantidadeRegistros(String sql, FiltroConsultaTransportadorDTO filtro){
		
		String sqlAux = sql.replace("select distinct tr", "select count(distinct tr.id)");
		
		Query query = this.getSession().createQuery(sqlAux);
		
		this.setarParametros(query, filtro);
		
		return (Long) query.uniqueResult();
	}
	
	private void setarParametros(Query query, FiltroConsultaTransportadorDTO filtro){
		
		if (filtro.getRazaoSocial() != null && !filtro.getRazaoSocial().isEmpty()){
			
			query.setParameter("razaoSocial", "%" + filtro.getRazaoSocial() + "%");
		}
		
		if (filtro.getNomeFantasia() != null && !filtro.getNomeFantasia().isEmpty()){
			
			query.setParameter("nomeFantasia", "%" + filtro.getNomeFantasia() + "%");
		}
		
		if (filtro.getCnpj() != null && !filtro.getCnpj().isEmpty()){
			
			query.setParameter("cnpj", filtro.getCnpj());
		}
	}
	
	@Override
	public Transportador buscarTransportadorPorCNPJ(String cnpj){
		
		StringBuilder hql = new StringBuilder("select t ");
		hql.append(" from Transportador t ")
		   .append(" where t.pessoaJuridica.cnpj = :cnpj ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cnpj", cnpj);
		
		return (Transportador) query.uniqueResult();
	}
}