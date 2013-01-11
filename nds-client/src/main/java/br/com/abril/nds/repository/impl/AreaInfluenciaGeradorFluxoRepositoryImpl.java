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
public class AreaInfluenciaGeradorFluxoRepositoryImpl extends
		AbstractRepositoryModel<Cota, Long> implements
		AreaInfluenciaGeradorFluxoRepository {

	private Map<String, Object> parameters;
	
	public AreaInfluenciaGeradorFluxoRepositoryImpl() {
		super(Cota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {
		
		// Total de registros no grid
		Integer totalRegistros = 0;
		
		// Armazenando os parametros
		Integer paramNumeroCota = 0;
		
		
		if (filtro.getNumeroCota() != null) {
			paramNumeroCota = filtro.getNumeroCota();	
		}
		
		String paramNomeCota = filtro.getNomeCota();
		
		StringBuilder hql = criarConsultaHqlPorCota(paramNumeroCota,paramNomeCota);
		
		Query query =  getSession().createQuery(hql.toString());
		
		setParameters(query);
		
		/* Transforma o resultado da query a partir dos alias
		 * onde cada alias é igual ao nome do atributo no DTO 
         */
		query.setResultTransformer(new AliasToBeanResultTransformer(AreaInfluenciaGeradorFluxoDTO.class));
		
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal() == totalRegistros) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AreaInfluenciaGeradorFluxoDTO> buscarPorAreaInfluencia(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {

		int totalRegistros = 0;
		
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
		
		StringBuilder hql = criarHqlPorAreaInfluenciaGeradorFluxo(paramAreaInfluenciaId, paramGeradorFluxoPrincipalId, paramGeradorFluxoSecundarioId, paramCotasAtivas);
		
		Query query =  getSession().createQuery(hql.toString());
		
		setParameters(query);
		
		/* Transforma o resultado da query a partir dos alias
		 * onde cada alias é igual ao nome do atributo no DTO 
         */
		query.setResultTransformer(new AliasToBeanResultTransformer(AreaInfluenciaGeradorFluxoDTO.class));
		
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal() == totalRegistros) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	private StringBuilder criarHqlPorAreaInfluenciaGeradorFluxo(
			Long paramAreaInfluenciaId, 
			Long paramGeradorFluxoPrincipalId,
			Long paramGeradorFluxoSecundarioId,
			boolean paramCotasAtivas) {
		
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = this.criarConsultaHqlPrincipal();
		
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
	private StringBuilder criarConsultaHqlPorCota(Integer paramNumeroCota,
			String paramNomeCota) {
		
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = criarConsultaHqlPrincipal();
		
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
	 * @return
	 */
	private StringBuilder criarConsultaHqlPrincipal() {
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
		hql.append(" cota.situacaoCadastro as statusCota, "); // STATUS COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
		hql.append(" tipoPontoPDV.descricao as tipoPdv, "); // TIPO PDV
		hql.append(" endereco.bairro as bairro, "); // BAIRRO
		hql.append(" endereco.cidade as cidade, "); // CIDADE
		
		// TODO CAMPO FATURAMENTO
		
		hql.append(" areaInfluenciaPDV.descricao as areaInfluencia, "); // AREA DE INFLUÊNCIA
		hql.append(" tipoGeradorFluxoPrincipal.descricao as geradorFluxoPrincipal "); // GERADOR DE FLUXO PRINCIPAL
		hql.append(" tipoGeradorFluxoSecundarioPDV.descricao as geradorFluxoPrincipal "); // GERADOR DE FLUXO SECUNDARIO
		
		// FROM
		
		hql.append(" from Cota as cota ");
		hql.append(" left join cota.enderecos as cotaEndereco ");
		hql.append(" left join cota.pessoa as pessoa ");
		hql.append(" left join cotaEndereco.endereco as endereco ");
		hql.append(" left join cota.pdvs as pdv ");
		hql.append(" left join pdv.segmentacao as segmento ");
		hql.append(" left join pdv.geradorFluxoPDV as geradorFluxoPrincipalPDV ");
		hql.append(" left join geradorFluxoPrincipalPDV.principal as tipoGeradorFluxoPrincipal ");
		hql.append(" left join geradorFluxoPrincipalPDV.secundarios as tipoGeradorFluxoSecundarioPDV ");
		hql.append(" left join segmento.tipoPontoPDV as tipoPontoPDV ");
		hql.append(" left join segmento.areaInfluenciaPDV as areaInfluenciaPDV ");		
		hql.append(" where pdv.caracteristicas.pontoPrincipal = true ");
		hql.append(" and cotaEndereco.principal = true ");
		
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
	
}
