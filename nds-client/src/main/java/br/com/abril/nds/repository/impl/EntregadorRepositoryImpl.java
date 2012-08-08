package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoPaginacaoVO;
import br.com.abril.nds.client.vo.EntregadorCotaProcuracaoVO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEntregadorDTO;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.ProcuracaoEntregador;
import br.com.abril.nds.repository.EntregadorRepository;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Repositório referente a entidade 
 * {@link br.com.abril.nds.model.cadastro.Entregador}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EntregadorRepositoryImpl extends AbstractRepositoryModel<Entregador, Long> 
									  implements EntregadorRepository {

	/**
	 * Construtor.
	 */
	public EntregadorRepositoryImpl() {

		super(Entregador.class);
	}

	/**
	 * @see br.com.abril.nds.repository.EntregadorRepository#obterEntregadoresPorFiltro(br.com.abril.nds.dto.filtro.FiltroEntregadorDTO)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Entregador> obterEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador) {

		String hql = getConsultaEntregadoresPorFiltro(filtroEntregador, false);
	    
	    Query query = getSession().createQuery(hql);
	    
	    if (filtroEntregador.getNomeRazaoSocial() != null 
	    		&& !filtroEntregador.getNomeRazaoSocial().isEmpty()) {
	    	
	    	query.setParameter("nome", "%" + filtroEntregador.getNomeRazaoSocial() + "%");
	    }
	    
	    if (filtroEntregador.getCpfCnpj() != null 
	    		&& !filtroEntregador.getCpfCnpj().isEmpty()) {

	    	String documentoSemMascara = filtroEntregador.getCpfCnpj().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");
	    	
	    	query.setParameter("documentoSemMascara", "%" + documentoSemMascara + "%");

	    	query.setParameter("documento", "%" + filtroEntregador.getCpfCnpj() + "%");
	    }
	 
	    if (filtroEntregador.getApelidoNomeFantasia() != null 
	    		&& !filtroEntregador.getApelidoNomeFantasia().isEmpty()) {
	    	
	    	query.setParameter("apelido", "%" + filtroEntregador.getApelidoNomeFantasia() + "%");
	    }

	    if (filtroEntregador.getPaginacao() != null 
				&& filtroEntregador.getPaginacao().getPosicaoInicial() != null) {

			query.setFirstResult(filtroEntregador.getPaginacao().getPosicaoInicial());

			query.setMaxResults(filtroEntregador.getPaginacao().getQtdResultadosPorPagina());
		}

	    return query.list();
	}
	
	/**
	 * @see br.com.abril.nds.repository.EntregadorRepository#obterContagemEntregadoresPorFiltro(br.com.abril.nds.dto.filtro.FiltroEntregadorDTO)
	 */
	@Override
	public Long obterContagemEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador) {

		String hql = getConsultaEntregadoresPorFiltro(filtroEntregador, true);
	    
	    Query query = getSession().createQuery(hql);
		
	    if (filtroEntregador.getNomeRazaoSocial() != null 
	    		&& !filtroEntregador.getNomeRazaoSocial().isEmpty()) {
	    	
	    	query.setParameter("nome", "%" + filtroEntregador.getNomeRazaoSocial() + "%");
	    }
	    
	    if (filtroEntregador.getCpfCnpj() != null 
	    		&& !filtroEntregador.getCpfCnpj().isEmpty()) {

	    	String documentoSemMascara = filtroEntregador.getCpfCnpj().replaceAll("\\.", "").replaceAll("-", "").replaceAll("/", "");

	    	query.setParameter("documentoSemMascara", "%" + documentoSemMascara + "%");
	    	
	    	query.setParameter("documento", "%" + filtroEntregador.getCpfCnpj() + "%");
	    }
	 
	    if (filtroEntregador.getApelidoNomeFantasia() != null 
	    		&& !filtroEntregador.getApelidoNomeFantasia().isEmpty()) {
	    	
	    	query.setParameter("apelido", "%" + filtroEntregador.getApelidoNomeFantasia() + "%");
	    }
	    
	    return (Long) query.uniqueResult();
	}

	/*
	 * Método que retorna a query utilizada na consulta para obter Entregadores.
	 */
	private String getConsultaEntregadoresPorFiltro(FiltroEntregadorDTO filtroEntregador, boolean isCountQuery) {

		StringBuilder builder = new StringBuilder();
		
		if (isCountQuery) {
			
			builder.append(" select count(entregador) from Entregador entregador ");
			
		} else {
			
			builder.append(" select entregador from Entregador entregador left join fetch entregador.telefones ");
		}
		
		String condition = "";
	    
	    if (filtroEntregador.getNomeRazaoSocial() != null 
	    		&& !filtroEntregador.getNomeRazaoSocial().isEmpty()) { 
	    
	    	condition = condition == "" ? " where " : " and ";
	    	
	    	builder.append(condition);
	    	builder.append(" (entregador.pessoa.nome like :nome ");
	    	builder.append(" or entregador.pessoa.razaoSocial like :nome )");
	    }

	    if (filtroEntregador.getCpfCnpj() != null && !filtroEntregador.getCpfCnpj().isEmpty()) {

	    	condition = condition == "" ? " where " : " and ";

	    	builder.append(condition);
	    	builder.append(" ((entregador.pessoa.cpf like :documento or entregador.pessoa.cpf like :documentoSemMascara) ");
	    	builder.append(" or (entregador.pessoa.cnpj like :documento or entregador.pessoa.cnpj like :documentoSemMascara))");
	    }
	    
	    if (filtroEntregador.getApelidoNomeFantasia() != null && !filtroEntregador.getApelidoNomeFantasia().isEmpty()) {

	    	condition = condition == "" ? " where " : " and ";
	    	
	    	builder.append(condition);
	    	builder.append(" (entregador.pessoa.apelido like :apelido ");
	    	builder.append(" or entregador.pessoa.nomeFantasia like :apelido )");
	    }
	    
	    builder.append(getOrdenacao(filtroEntregador));

	    return builder.toString();
	}
	
	/*
	 * Método que retorna a cláusula Order By da consulta de entregadores, através do filtro.
	 */
	private String getOrdenacao(FiltroEntregadorDTO filtroEntregador) {
		
		StringBuilder builder = new StringBuilder();

		builder.append(" order by ");
		
		switch(filtroEntregador.getOrdenacaoColunaEntregador()) {

		case CODIGO:
			builder.append(" entregador.codigo ");
			break;
			
		case NOME_RAZAO_SOCIAL:
			builder.append(" concat(case when entregador.pessoa.razaoSocial is null "); 
			builder.append(" then '' else entregador.pessoa.razaoSocial end, "); 
			builder.append(" case when entregador.pessoa.nome is null ");
			builder.append(" then '' else entregador.pessoa.nome end) ");
			break;
			
		case CPF_CNPJ:
			builder.append(" concat(case when entregador.pessoa.cpf is null "); 
			builder.append(" then '' else entregador.pessoa.cpf end, "); 
			builder.append(" case when entregador.pessoa.cnpj is null ");
			builder.append(" then '' else entregador.pessoa.cnpj end) ");
			break; 
		
		case APELIDO_NOME_FANTASIA:
			builder.append(" concat(case when entregador.pessoa.nomeFantasia is null "); 
			builder.append(" then '' else entregador.pessoa.nomeFantasia end, "); 
			builder.append(" case when entregador.pessoa.apelido is null ");
			builder.append(" then '' else entregador.pessoa.apelido end) ");
			break;

		case TELEFONE:

			return "";

		case EMAIL:
			builder.append(" entregador.pessoa.email ");
			break;

		}
		
		builder.append(
				filtroEntregador.getPaginacao() != null && 
				Ordenacao.DESC == filtroEntregador.getPaginacao().getOrdenacao() ? 
						" desc" : " asc ");

		return builder.toString();
	}

	/**
	 * @see br.com.abril.nds.repository.EntregadorRepository#obterEnderecosPorIdEntregador(java.lang.Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosPorIdEntregador(Long idEntregador) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select enderecoEntregador.id as id, enderecoEntregador.endereco as endereco, ")
		   .append(" enderecoEntregador.principal as enderecoPrincipal, ")
		   .append(" enderecoEntregador.tipoEndereco as tipoEndereco ")
		   .append(" from EnderecoEntregador enderecoEntregador ")
		   .append(" where enderecoEntregador.entregador.id = :idEntregador ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(EnderecoAssociacaoDTO.class);
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("idEntregador", idEntregador);
		
		return query.list();
	}

	/**
	 * @see br.com.abril.nds.repository.EntregadorRepository#obterProcuracaoEntregadorPorIdEntregador(java.lang.Long)
	 */
	@Override
	public ProcuracaoEntregador obterProcuracaoEntregadorPorIdEntregador(Long idEntregador) {

		Criteria criteria = getSession().createCriteria(ProcuracaoEntregador.class);
		
		criteria.createAlias("entregador", "entregador");
		
		criteria.add(Restrictions.eq("entregador.id", idEntregador));
		
		return (ProcuracaoEntregador) criteria.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer obterQuantidadeEntregadoresPorIdPessoa(Long idPessoa, Long idEntregador) {

		Criteria criteria = getSession().createCriteria(Entregador.class);
		
		criteria.add(Restrictions.eq("pessoa.id", idPessoa));
		
		if (idEntregador != null) {
		
			criteria.add(Restrictions.ne("id", idEntregador));
		}
		
		criteria.setProjection(Projections.rowCount());

		return ((Long) criteria.list().get(0)).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntregadorCotaProcuracaoPaginacaoVO buscarCotasAtendidas(
			Long idEntregador, int pagina, int resultadosPorPagina,
			String sortname, String sortorder) {
		
		EntregadorCotaProcuracaoPaginacaoVO retorno = new EntregadorCotaProcuracaoPaginacaoVO();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select new ").append(EntregadorCotaProcuracaoVO.class.getCanonicalName()).append("(")
		   .append(" cota.numeroCota, cota.pessoa.nome, cota.parametroDistribuicao.procuracaoAssinada ")
		   .append(") ")
		   .append(" from Entregador e join e.rota.roteiro.box.cotas cota ")
		   .append(" where e.id = :idEntregador ");
		
		retorno.setTotalRegistros(obterQtdRegistrosCotaAtendidaPaginacao(hql.toString(), idEntregador).intValue());
		
		hql.append(" order by ");
		
		if ("numeroCota".equals(sortname)){
			
			hql.append(" cota.numeroCota ");
		} else {
			
			hql.append(" cota.pessoa.nome ");
		}
		
		if ("asc".equals(sortorder)){
			
			hql.append(" asc ");
		} else {
			
			hql.append(" desc ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEntregador", idEntregador);
		
		query.setFirstResult(pagina > 0 ? (pagina - 1) * resultadosPorPagina : pagina * resultadosPorPagina);
		query.setMaxResults(resultadosPorPagina);
		
		retorno.setListaVO(query.list());
		
		return retorno;
	}
	
	private Long obterQtdRegistrosCotaAtendidaPaginacao(String hql, Long idEntregador){
		
		String _hql = hql.substring(hql.indexOf("from"), hql.length());
		
		Query query = this.getSession().createQuery("select count (cota.id) " + _hql);
		query.setParameter("idEntregador", idEntregador);
		
		return (Long) query.uniqueResult();
	}
}