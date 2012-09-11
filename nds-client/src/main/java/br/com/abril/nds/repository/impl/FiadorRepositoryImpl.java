package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.repository.FiadorRepository;

@Repository
public class FiadorRepositoryImpl extends AbstractRepositoryModel<Fiador, Long> implements FiadorRepository {

	public FiadorRepositoryImpl() {
		super(Fiador.class);
	}

	@Override
	public Fiador obterFiadorPorCpf(String cpf) {
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ")
		   .append(" and p.cpf = :cpf ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cpf", cpf);
		
		return (Fiador) query.uniqueResult();
	}

	@Override
	public Fiador obterFiadorPorCnpj(String cnpj) {
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ")
		   .append(" and p.cnpj = :cnpj ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("cnpj", cnpj);
		
		return (Fiador) query.uniqueResult();
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.FiadorRepository#obterPorCpfCnpj(java.lang.String)
	 */
	@Override
	public Fiador obterPorCpfCnpj(String doc){
		StringBuilder hql = new StringBuilder();
		
		hql.append("from Fiador fiador ")
		   .append("where fiador.pessoa.cpf = :doc or fiador.pessoa.cnpj = :doc");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("doc", doc);
		return (Fiador) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public ConsultaFiadorDTO obterFiadoresCpfCnpj(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO) {
		
		StringBuilder hql = new StringBuilder("select distinct f from Fiador f, Pessoa p ");
		hql.append(" left join f.telefonesFiador telefonefiador ")
		   .append(" left join telefonefiador.telefone telefone")
		   .append(" where f.pessoa.id = p.id ");
		
		if (filtroConsultaFiadorDTO.getCpfCnpj() != null &&
				!filtroConsultaFiadorDTO.getCpfCnpj().isEmpty()){
			
			hql.append(" and (p.cpf = :cpfCnpj or p.cnpj = :cpfCnpj) ");
		}
		
		if (filtroConsultaFiadorDTO.getNome() != null &&
				!filtroConsultaFiadorDTO.getNome().isEmpty()){
			
			hql.append(" and (p.nome like :nome or p.razaoSocial like :nome or p.nomeFantasia like :nome) ");
		}
		
		ConsultaFiadorDTO consultaFiadorDTO = new ConsultaFiadorDTO();
		
		consultaFiadorDTO.setQuantidadeRegistros(this.obterQuantidadeRegistros(hql.toString(), filtroConsultaFiadorDTO));
		
		switch (filtroConsultaFiadorDTO.getOrdenacaoColunaFiador()){
			case CODIGO:
				hql.append(" order by f.id ");
			break;
			case CPF_CNPJ:
				hql.append(" order by p.cpf, p.cnpj ");
			break;
			case EMAIL:
				hql.append(" order by p.email ");
			break;
			case NOME:
				hql.append(" order by p.nome, p.razaoSocial, p.nomeFantasia ");
			break;
			case RG_INSCRICAO:
				hql.append(" order by p.rg, p.inscricaoEstadual ");
			break;
			case TELEFONE:
				hql.append(" order by telefone.numero ");
			break;
		}
		
		hql.append(filtroConsultaFiadorDTO.getPaginacaoVO().getOrdenacao());
		
		Query query = this.getSession().createQuery(hql.toString());
		
		this.setarParametros(query, filtroConsultaFiadorDTO);
		
		query.setMaxResults(filtroConsultaFiadorDTO.getPaginacaoVO().getQtdResultadosPorPagina());
		
		query.setFirstResult(
				(filtroConsultaFiadorDTO.getPaginacaoVO().getPaginaAtual() -1) * filtroConsultaFiadorDTO.getPaginacaoVO().getQtdResultadosPorPagina());
		
		consultaFiadorDTO.setListaFiadores(query.list());
		
		return consultaFiadorDTO;
	}
	
	@Override
	public Pessoa buscarPessoaFiadorPorId(Long idFiador){
		
		StringBuilder hql = new StringBuilder("select f.pessoa from Fiador f ");
		hql.append(" where f.id = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return (Pessoa) query.uniqueResult();
	}
	
	private Long obterQuantidadeRegistros(String sql, FiltroConsultaFiadorDTO filtroConsultaFiadorDTO){
		String sqlAux = sql.replace("select distinct f", "select count(distinct f.id)");
		
		Query query = this.getSession().createQuery(sqlAux);
		
		this.setarParametros(query, filtroConsultaFiadorDTO);
		
		return (Long) query.uniqueResult();
	}
	
	private void setarParametros(Query query, FiltroConsultaFiadorDTO filtro){
		
		if (filtro.getCpfCnpj() != null &&
				!filtro.getCpfCnpj().isEmpty()){
			
			query.setParameter("cpfCnpj", filtro.getCpfCnpj());
		}
		
		if (filtro.getNome() != null &&
				!filtro.getNome().isEmpty()){
			
			query.setParameter("nome", "%" + filtro.getNome() + "%");
		}
	}
	
	@Override
	public Long buscarIdPessoaFiador(Long idFiador){
		
		StringBuilder hql = new StringBuilder("select f.pessoa.id from Fiador f");
		hql.append(" where f.id = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return (Long) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Pessoa> buscarSociosFiador(Long idFiador){
		
		StringBuilder hql = new StringBuilder("select f.socios from Fiador f ");
		hql.append(" where f.id = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return query.list();
	}

	@Override
	public Date buscarDataInicioAtividadeFiadorPorId(Long id) {
		
		StringBuilder hql = new StringBuilder("select f.inicioAtividade ");
		hql.append(" from Fiador f ")
		   .append(" where f.id = :id ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("id", id);
		
		return (Date) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Cota> obterCotasAssociadaFiador(Long idFiador, Set<Long> cotasIgnorar){
		
		StringBuilder hql = new StringBuilder("select c ");
		hql.append(" from Cota c ")
		   .append(" where c.fiador.id = :idFiador ");
		
		if (cotasIgnorar != null && !cotasIgnorar.isEmpty()){
			hql.append(" and c.id not in (:cotasIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		if (cotasIgnorar != null && !cotasIgnorar.isEmpty()){
			query.setParameterList("cotasIgnorar", cotasIgnorar);
		}
		
		return query.list();
	}

	@Override
	public boolean verificarAssociacaoFiadorCota(Long idFiador,	Integer numeroCota, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select count (c.id) ");
		hql.append(" from Fiador f, Cota c ")
		   .append(" where c.fiador.id = f.id ")
		   .append(" and   f.id = :idFiador ")
		   .append(" and   c.numeroCota = :numeroCota ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			hql.append(" and c.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		query.setParameter("numeroCota", numeroCota);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return ((Long)query.uniqueResult()) > 0;
	}

	@Override
	public PessoaFisica buscarSocioFiadorPorCPF(Long idFiador, String cpf) {
		
		StringBuilder hql = new StringBuilder("select p ");
		hql.append(" from Pessoa p, Fiador f ")
		   .append(" where p.cpf = :cpf ")
		   .append(" and   f.id = :idFiador ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		query.setParameter("cpf", cpf);
		
		query.setMaxResults(1);
		
		return (PessoaFisica) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ItemDTO<Long, String>> buscaFiador(String nome, int maxResults){
		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT fiador.id as key, ")
			.append("CASE fiador.pessoa.class WHEN 'F' THEN fiador.pessoa.nome WHEN 'J' THEN fiador.pessoa.razaoSocial END  as value ")
			.append("FROM Fiador fiador ")
			.append("WHERE ")
			.append("lower(fiador.pessoa.nome) like :nome or lower(fiador.pessoa.razaoSocial) like :nome");
		
		Query query = getSession().createQuery(hql.toString());
		
		
		query.setString("nome", "%" + nome.toLowerCase() + "%");
		query.setMaxResults(maxResults);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ItemDTO.class));
		return query.list();
		
	}
}