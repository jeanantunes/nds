package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ConsultaFiadorDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaFiadorDTO;
import br.com.abril.nds.model.cadastro.Fiador;
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
		boolean andUsado = false;
		
		StringBuilder hql = new StringBuilder("select f from Fiador f, Pessoa p ");
		hql.append(" where f.pessoa.id = p.id ");
		
		if (filtroConsultaFiadorDTO.getCpfCnpj() != null &&
				!filtroConsultaFiadorDTO.getCpfCnpj().isEmpty()){
			
			if (!andUsado){
				hql.append(" and ");
				andUsado = true;
			}
			
			hql.append(" (p.cpf = :cpfCnpj or p.cnpj = :cpfCnpj) ");
		}
		
		if (filtroConsultaFiadorDTO.getNome() != null &&
				!filtroConsultaFiadorDTO.getNome().isEmpty()){
			
			if (!andUsado){
				hql.append(" and ");
				andUsado = true;
			}
			
			hql.append(" (p.nome like :nome or p.razaoSocial like :nome or p.nomeFantasia like :nome) ");
		}
		
		ConsultaFiadorDTO consultaFiadorDTO = new ConsultaFiadorDTO();
		consultaFiadorDTO.setQuantidadePaginas(
				(this.obterQuantidadeRegistros(hql.toString(), filtroConsultaFiadorDTO) / filtroConsultaFiadorDTO.getPaginacaoVO().getQtdResultadosPorPagina()) + 1);
		
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
		query.setFirstResult(filtroConsultaFiadorDTO.getPaginacaoVO().getPaginaAtual());
		
		consultaFiadorDTO.setListaFiadores(query.list());
		
		return consultaFiadorDTO;
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
}