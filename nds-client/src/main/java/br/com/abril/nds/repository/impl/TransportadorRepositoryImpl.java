package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.TransportadorRepository;

@Repository
public class TransportadorRepositoryImpl extends
		AbstractRepositoryModel<Transportador, Long> implements TransportadorRepository {

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
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CotaAtendidaTransportadorVO> buscarCotasAtendidadas(
			Long idTransportador, String sortorder,	String sortname){
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(CotaAtendidaTransportadorVO.class.getCanonicalName())
		   .append("(roteirizacao.pdv.cota.numeroCota, roteirizacao.pdv.cota.pessoa.nome, roteirizacao.pdv.cota.box.nome || '-' || roteirizacao.pdv.cota.box.codigo, ")
		   .append(" assoc.rota.roteiro.descricaoRoteiro, assoc.rota.codigoRota || '-' || assoc.rota.descricaoRota, ")
		   .append(" coalesce(roteirizacao.pdv.cota.parametroDistribuicao.taxaFixa, roteirizacao.pdv.cota.parametroDistribuicao.percentualFaturamento || '%'))")
		   .append(" from AssociacaoVeiculoMotoristaRota assoc ")
		   .append(" join assoc.rota.roteirizacao roteirizacao ")
		   .append(" where assoc.transportador.id = :idTransportador ");
		
		if ("numeroCota".equals(sortname)){
			
			hql.append(" order by roteirizacao.pdv.cota.numeroCota ");
		} else if ("nomeCota".equals(sortname)){
			
			hql.append(" order by roteirizacao.pdv.cota.pessoa.nome ");
		} else if ("box".equals(sortname)){
			
			hql.append(" order by roteirizacao.pdv.cota.box.nome ");
		} else if ("roteiro".equals(sortname)){
			
			hql.append(" order by assoc.rota.roteiro.descricaoRoteiro ");
		} else if ("rota".equals(sortname)){
			
			hql.append(" order by assoc.rota.codigoRota ");
		} else {
			
			hql.append(" order by roteirizacao.pdv.cota.numeroCota ");
		}
		
		if ("asc".equals(sortorder)){
			
			hql.append(" asc ");
		} else {
			
			hql.append(" desc ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTransportador", idTransportador);
		
		return query.list();
	}
}