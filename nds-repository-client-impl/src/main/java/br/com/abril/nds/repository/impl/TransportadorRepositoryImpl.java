package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.CotaAtendidaTransportadorVO;
import br.com.abril.nds.dto.ConsultaTransportadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaTransportadorDTO;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Transportador;
import br.com.abril.nds.repository.AbstractRepositoryModel;
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
			
			hql.append(" and ( lower( tr.pessoaJuridica.razaoSocial ) like :razaoSocial) ");
		}
		
		if (filtro.getNomeFantasia() != null && !filtro.getNomeFantasia().isEmpty()){
			
			hql.append(" and ( lower( tr.pessoaJuridica.nomeFantasia ) like :nomeFantasia) ");
		}
		
		if (filtro.getCnpj() != null && !filtro.getCnpj().isEmpty()){
			
			hql.append(" and (tr.pessoaJuridica.cnpj like :cnpj) ");
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
			
			query.setParameter("razaoSocial", "%" + filtro.getRazaoSocial().toLowerCase().trim() + "%");
		}
		
		if (filtro.getNomeFantasia() != null && !filtro.getNomeFantasia().isEmpty()){
			
			query.setParameter("nomeFantasia", "%" + filtro.getNomeFantasia().toLowerCase().trim() + "%");
		}
		
		if (filtro.getCnpj() != null && !filtro.getCnpj().isEmpty()){
			
			query.setParameter("cnpj", "%" + filtro.getCnpj().replaceAll("[.-/]", "") + "%");
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
		   .append("(cota.numeroCota, CASE WHEN cota.pessoa.class = 'J' THEN cota.pessoa.razaoSocial ELSE cota.pessoa.nome END, cota.box.nome || '-' || cota.box.codigo, ")
		   .append(" assoc.rota.roteiro.descricaoRoteiro, assoc.rota.descricaoRota, ")
		   .append(" coalesce(parametroCobrancaDistribuicaoCota.taxaFixa, parametroCobrancaDistribuicaoCota.percentualFaturamento,''))")
		   .append(" from AssociacaoVeiculoMotoristaRota assoc ")
		   .append(" join assoc.rota rota ")
		   .append(" join rota.rotaPDVs rotaPDV ")
		   .append(" join rotaPDV.pdv pdv ")
		   .append(" join pdv.cota cota ")
		   .append(" left join cota.parametroCobrancaDistribuicaoCota parametroCobrancaDistribuicaoCota")
		   .append(" where assoc.transportador.id = :idTransportador ");
		
		if ("numeroCota".equals(sortname)){
			
			hql.append(" order by cota.numeroCota ");
		} else if ("nomeCota".equals(sortname)){
			
			hql.append(" order by cota.pessoa.nome ");
		} else if ("box".equals(sortname)){
			
			hql.append(" order by cota.box.nome ");
		} else if ("roteiro".equals(sortname)){
			
			hql.append(" order by assoc.rota.roteiro.descricaoRoteiro ");
		} else if ("rota".equals(sortname)){
			
			hql.append(" order by assoc.rota.descricaoRota ");
		} else {
			
			hql.append(" order by cota.numeroCota ");
		}
		
		if ("desc".equals(sortorder)){
			
			hql.append(" desc ");
		} else {
			
			hql.append(" asc ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTransportador", idTransportador);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterTransportadorPorNome(String nomeTransportador) {
		
		StringBuilder hql = new StringBuilder("select pessoa ");
		hql.append(" from Transportador t join t.pessoaJuridica pessoa ")
		   .append(" where lower(pessoa.razaoSocial) like :nomeTransportador  ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeTransportador", "%" + nomeTransportador.toLowerCase() + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterTransportadorPorNome(String nomeTransportador, Integer qtdMaxResult) {
		
		StringBuilder hql = new StringBuilder("select pessoa ");
		hql.append(" from Transportador t join t.pessoaJuridica pessoa ")
		   .append(" where lower(pessoa.razaoSocial) like :nomeTransportador  ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeTransportador", "%" + nomeTransportador.toLowerCase() + "%");
		query.setMaxResults(qtdMaxResult);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterTransportadorPorNomeFantasia(String nomeFantasia) {
		
		StringBuilder hql = new StringBuilder("select pessoa ");
		hql.append(" from Transportador t join t.pessoaJuridica pessoa ")
		   .append(" where lower(pessoa.nomeFantasia) like :nomeFantasia  ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeFantasia", "%" + nomeFantasia.toLowerCase() + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> obterTransportadorPorNomeFantasia(String nomeFantasia, Integer qtdMaxResult) {
		
		StringBuilder hql = new StringBuilder("select pessoa ");
		hql.append(" from Transportador t join t.pessoaJuridica pessoa ")
		   .append(" where lower(pessoa.nomeFantasia) like :nomeFantasia  ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("nomeFantasia", "%" + nomeFantasia.toLowerCase() + "%");
		query.setMaxResults(qtdMaxResult);
		
		return query.list();
	}
}