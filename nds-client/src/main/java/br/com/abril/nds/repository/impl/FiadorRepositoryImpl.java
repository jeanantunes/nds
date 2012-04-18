package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fiador;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.repository.FiadorRepository;

@Repository
public class FiadorRepositoryImpl extends AbstractRepository<Fiador, Long> implements FiadorRepository {

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

	@SuppressWarnings("unchecked")
	@Override
	public ConsultaFiadorDTO obterFiadoresCpfCnpj(FiltroConsultaFiadorDTO filtroConsultaFiadorDTO) {
		
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ");
		
		if (filtroConsultaFiadorDTO.getCpfCnpj() != null &&
				!filtroConsultaFiadorDTO.getCpfCnpj().isEmpty()){
			
			hql.append(" and ");
			
			hql.append(" (p.cpf = :cpfCnpj or p.cnpj = :cpfCnpj) ");
		}
		
		if (filtroConsultaFiadorDTO.getNome() != null &&
				!filtroConsultaFiadorDTO.getNome().isEmpty()){
			
			hql.append(" and ");
			
			hql.append(" (p.nome like :nome or p.razaoSocial like :nome or p.nomeFantasia like :nome) ");
		}
		
		Long qtdRegistros = 
				this.obterQuantidadeRegistros(hql.toString(), filtroConsultaFiadorDTO);
		
		ConsultaFiadorDTO consultaFiadorDTO = new ConsultaFiadorDTO();
		
		long qtdPaginas = 
				(qtdRegistros / filtroConsultaFiadorDTO.getPaginacaoVO().getQtdResultadosPorPagina());
		
		consultaFiadorDTO.setQuantidadePaginas(qtdPaginas == 0 ? 1 : qtdPaginas);
		
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
		}
		
		hql.append(filtroConsultaFiadorDTO.getPaginacaoVO().getOrdenacao());
		
		Query query = this.getSession().createQuery(hql.toString());
		if (filtroConsultaFiadorDTO.getCpfCnpj() != null &&
				!filtroConsultaFiadorDTO.getCpfCnpj().isEmpty()){
			
			query.setParameter("cpfCnpj", filtroConsultaFiadorDTO.getCpfCnpj());
		}
		
		if (filtroConsultaFiadorDTO.getNome() != null &&
				!filtroConsultaFiadorDTO.getNome().isEmpty()){
			
			query.setParameter("nome", "%" + filtroConsultaFiadorDTO.getNome() + "%");
		}
		
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
		String sqlAux = sql.replace("select f", "select count(f.id)");
		//sqlAux = sqlAux.substring(0, sqlAux.indexOf(" order "));
		
		Query query = this.getSession().createQuery(sqlAux);
		if (filtroConsultaFiadorDTO.getCpfCnpj() != null &&
				!filtroConsultaFiadorDTO.getCpfCnpj().isEmpty()){
			
			query.setParameter("cpfCnpj", filtroConsultaFiadorDTO.getCpfCnpj());
		}
		
		if (filtroConsultaFiadorDTO.getNome() != null &&
				!filtroConsultaFiadorDTO.getNome().isEmpty()){
			
			query.setParameter("nome", "%" + filtroConsultaFiadorDTO.getNome() + "%");
		}
		
		return (Long) query.uniqueResult();
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
	public List<Cota> obterCotasAssociadaFiador(Long idFiador){
		
		StringBuilder hql = new StringBuilder("select f.cotasAssociadas ");
		hql.append(" from Fiador f ")
		   .append(" where f.id = :idFiador ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return query.list();
	}

	@Override
	public boolean verificarAssociacaoFiadorCota(Long idFiador,	Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select count (c.id) ");
		hql.append(" from Fiador f, Cota c ")
		   .append(" where c.fiador.id = f.id ")
		   .append(" and   f.id = :idFiador ")
		   .append(" and   c.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		query.setParameter("numeroCota", numeroCota);
		
		return ((Long)query.uniqueResult()) > 0;
	}
}