package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;

@Repository
public class SegmentoNaoRecebidoRepositoryImpl extends AbstractRepositoryModel<SegmentoNaoRecebido, Long> implements SegmentoNaoRecebidoRepository {

	public SegmentoNaoRecebidoRepositoryImpl() {
		super(SegmentoNaoRecebido.class);
	}

	@Override
	public List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		// FIELDS
		hql.append(" SELECT ");
		hql.append(" segmentoNaoRecebido.id as segmentoNaoRecebidoId, "); // Id do Segmento
		hql.append(" cota.numeroCota as numeroCota, "); // NÚMERO DA COTA
		hql.append(" cota.situacaoCadastro as statusCota, "); // STATUS DA COTA
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota,"); // NOME DA PESSOA
		hql.append(" usuario.nome as nomeUsuario, "); // NOME DO USUÁRIO
		hql.append(" segmentoNaoRecebido.dataAlteracao as dataAlteracao "); // DATA ALTERAÇÃO

		// FROM
		hql.append(" FROM ");
		hql.append(" SegmentoNaoRecebido as segmentoNaoRecebido");
		hql.append(" join segmentoNaoRecebido.cota as cota ");
		hql.append(" join cota.pessoa as pessoa ");
		hql.append(" join segmentoNaoRecebido.usuario as usuario ");
		
		// WHERE
		if (!filtro.getTipoSegmentoProdutoId().equals(0)) {
			hql.append(" where segmentoNaoRecebido.tipoSegmentoProduto.id = " + filtro.getTipoSegmentoProdutoId());	
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaNaoRecebeSegmentoDTO.class));
		
		// Configuração da query
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal().equals(0)) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	@Override
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebemCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
		
		// FIELDS
		hql.append(" SELECT ");
		hql.append(" tipoSegmentoProduto.descricao as nomeSegmento, "); // Nome Segmento
		hql.append(" usuario.nome as nomeUsuario, "); // NOME DO USUÁRIO
		hql.append(" segmentoNaoRecebido.dataAlteracao as dataAlteracao "); // DATA ALTERAÇÃO

		// FROM
		hql.append(" FROM ");
		hql.append(" SegmentoNaoRecebido as segmentoNaoRecebido");
		hql.append(" join segmentoNaoRecebido.tipoSegmentoProduto as tipoSegmentoProduto ");
		hql.append(" join segmentoNaoRecebido.usuario as usuario ");
		hql.append(" WHERE ");
		hql.append(" segmentoNaoRecebido.cota.numeroCota = : ");
		
		
		
		// WHERE
		if (!filtro.getTipoSegmentoProdutoId().equals(0)) {
			hql.append(" where segmentoNaoRecebido.tipoSegmentoProduto.id = " + filtro.getTipoSegmentoProdutoId());	
		}
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaNaoRecebeSegmentoDTO.class));
		
		// Configuração da query
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal().equals(0)) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}

	@Override
	public List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" cota.id as idCota, ");
		hql.append(" cota.numeroCota as numeroCota, "); 
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" From ");
		hql.append(" Cota as cota ");
		hql.append(" join cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");
		hql.append(" cota.id not in (select seg.cota.id FROM SegmentoNaoRecebido as seg) ");
		
		if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append(" and coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') like :nomePessoa) ");
			parameters.put("nomePessoa", "%" + filtro.getNomeCota() + "%");
		}
		
		if (filtro.getNumeroCota() != null && filtro.getNumeroCota() > 0) {
			hql.append(" and cota.numeroCota like :numeroCota) ");
			parameters.put("numeroCota", filtro.getNumeroCota());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	/**
	 * @param filtro
	 * @param query
	 */
//	private void configurandoPaginacao(
//			FiltroAreaInfluenciaGeradorFluxoDTO filtro, Query query) {
//		
//		if (filtro.getPaginacao().getPosicaoInicial() != null) {
//			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
//		}
//		
//		if (filtro.getPaginacao().getQtdResultadosTotal().equals(0)) {
//			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
//		}
//		
//		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
//			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
//	}
	
}
