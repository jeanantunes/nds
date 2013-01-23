package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.filtro.FiltroAreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.AreaInfluenciaGeradorFluxoRepository;

@Repository
public class AreaInfluenciaGeradorFluxoRepositoryImpl extends AbstractRepositoryModel<Cota, Long> implements AreaInfluenciaGeradorFluxoRepository {

	private Map<String, Object> parameters;
	
	public AreaInfluenciaGeradorFluxoRepositoryImpl() {
		super(Cota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {
		
		// Armazenando os parametros do filtro
		Integer paramNumeroCota = 0;
		
		if (filtro.getNumeroCota() != null) {
			paramNumeroCota = filtro.getNumeroCota();	
		}
		
		String paramNomeCota = filtro.getNomeCota();
		
		StringBuilder hql = montarConsultaHqlPorCota(paramNumeroCota,paramNomeCota);
		
		// Group by na query
		setGroupBy(hql);
		
		Query query =  getSession().createQuery(hql.toString());
		
		// Adicionando os valores dos parametros na query
		setParameters(query);
		
		/* Transforma o resultado da query a partir dos alias
		 * onde cada alias é igual ao nome do atributo no DTO 
         */
		query.setResultTransformer(new AliasToBeanResultTransformer(AreaInfluenciaGeradorFluxoDTO.class));
		
		configurandoPaginacao(filtro, query);
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorAreaInfluencia(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {

		// Armazenando os parametros do filtro
		Long paramAreaInfluenciaId = 0l;
		Long paramGeradorFluxoPrincipalId = 0l;
		Long paramGeradorFluxoSecundarioId = 0l;
		boolean paramCotasAtivas = false;
		
		if (filtro.getAreaInfluenciaId() != null && filtro.getAreaInfluenciaId() > 0) {
			paramAreaInfluenciaId = filtro.getAreaInfluenciaId();
		}
		
		if (filtro.getGeradorFluxoPrincipalId() != null && filtro.getGeradorFluxoPrincipalId() > 0) {
			paramGeradorFluxoPrincipalId = filtro.getGeradorFluxoPrincipalId();
		}
		
		if (filtro.getGeradorFluxoSecundarioId() != null && filtro.getGeradorFluxoSecundarioId() > 0) {
			paramGeradorFluxoSecundarioId = filtro.getGeradorFluxoSecundarioId();
		}
		
		if (filtro.isCotasAtivas()) {
			paramCotasAtivas = filtro.isCotasAtivas();
		}
		
		StringBuilder hql = montarConsultaHqlPorAreaInfluenciaGeradorFluxo(paramAreaInfluenciaId, paramGeradorFluxoPrincipalId, paramGeradorFluxoSecundarioId, paramCotasAtivas);
		
		// Group by
		setGroupBy(hql);
		
		Query query =  getSession().createQuery(hql.toString());
		
		// Adicionando os valores dos parametros na query
		setParameters(query);
		
		/* Transforma o resultado da query a partir dos alias
		 * onde cada alias é igual ao nome do atributo no DTO 
         */
		query.setResultTransformer(new AliasToBeanResultTransformer(AreaInfluenciaGeradorFluxoDTO.class));
		
		configurandoPaginacao(filtro, query);
		
		return query.list();
	}
	
	/**
	 * @return
	 * Ambas consultas utilizam o mesmo corpo de query
	 */
	private StringBuilder montarCorpoHql() {
		StringBuilder hql = new StringBuilder();
		
		// RETURNING FIELDS
		hql.append(" select ");
		hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
		hql.append(" cota.situacaoCadastro as statusCota, "); // STATUS COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
		hql.append(" tipoPontoPDV.descricao as tipoPdv, "); // TIPO PDV
		hql.append(" endereco.bairro as bairro, "); // BAIRRO
		hql.append(" endereco.cidade as cidade, "); // CIDADE
		hql.append(" sum((estoqueProdutoCota.qtdeRecebida - estoqueProdutoCota.qtdeDevolvida) * produtoEdicao.precoVenda) as faturamento, "); // FATURAMENTO
		hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA DE INFLUÊNCIA
		hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorFluxoPrincipal "); // GERADOR DE FLUXO PRINCIPAL
		
		// TODO INFORMAR O CAMPO SECUNDÁRIO GERADOR
		//hql.append(" tipoGeradorFluxoSecundarioPDV.descricao as geradorFluxoSecundario "); // GERADOR DE FLUXO SECUNDARIO
		
		// FROM
		hql.append(" from EstoqueProdutoCota as estoqueProdutoCota");
		hql.append(" left join estoqueProdutoCota.produtoEdicao as produtoEdicao ");
		hql.append(" left join estoqueProdutoCota.cota as cota ");
		hql.append(" left join cota.enderecos as cotaEndereco ");
		hql.append(" left join cota.pessoa as pessoa ");
		hql.append(" left join cotaEndereco.endereco as endereco ");
		hql.append(" left join cota.pdvs as pdv ");
		hql.append(" left join pdv.segmentacao as segmento ");
		hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
		hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
		
		//TODO FAZER O JOIN DO GERADOR SECUNDÁRIO
		//hql.append(" left join geradorFluxoPrincipalPDV.secundarios as tipoGeradorFluxoSecundarioPDV ");
		hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
		hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");
		hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		hql.append(" and cotaEndereco.principal = true ");
		
		return hql;
	}
	
	
	

	private void setGroupBy(StringBuilder hql) {
		hql.append(" group by cota.id ");
	}

	private StringBuilder montarConsultaHqlPorAreaInfluenciaGeradorFluxo(
			Long paramAreaInfluenciaId, 
			Long paramGeradorFluxoPrincipalId,
			Long paramGeradorFluxoSecundarioId,
			boolean paramCotasAtivas) {
		
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = this.montarCorpoHql();
		
		if (paramAreaInfluenciaId > 0) {
			hql.append(" and areaInfluenciaPDV.id = :areaInfluenciaId");
			parameters.put("areaInfluenciaId", paramAreaInfluenciaId);
		}
		
		if (paramGeradorFluxoPrincipalId > 0) {
			hql.append(" and tipoGeradorFluxoPrincipal.id = :geradorFluxoPrincipalId");
			parameters.put("geradorFluxoPrincipalId", paramGeradorFluxoPrincipalId);
		}
		
		if (paramGeradorFluxoSecundarioId > 0) {
			//TODO
			//hql.append(" or geradorFluxoPDV.principal = :geradorFluxoSecundario");
			//parameters.put("geradorFluxoSecundario", paramGeradorFluxoSecundario);
		}
		
		if (paramCotasAtivas) {
			hql.append(" and cota.situacaoCadastro = :statusCota");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		return hql;
	}

	/**
	 * @param paramNumeroCota
	 * @param paramNomeCota
	 * @return
	 */
	private StringBuilder montarConsultaHqlPorCota(Integer paramNumeroCota, String paramNomeCota) {
		
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = montarCorpoHql();
		
		if (paramNumeroCota != 0 ) {
			hql.append(" and cota.numeroCota = :numeroCota ");
			parameters.put("numeroCota", paramNumeroCota);
		}
		else if (paramNomeCota != null && !paramNomeCota.isEmpty()) {
			hql.append(" and pessoa.nome like :nomeCota ");
			parameters.put("nomeCota", "%" + paramNomeCota + "%");
		}
		
		return hql;
	}

	/**
	 * @param paramNumeroCota
	 * @param paramNomeCota
	 * @param query
	 */
	private void setParameters(Query query) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}
	
	/**
	 * @param filtro
	 * @param query
	 */
	private void configurandoPaginacao(
			FiltroAreaInfluenciaGeradorFluxoDTO filtro, Query query) {
		
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal().equals(0)) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
	}
	
}
