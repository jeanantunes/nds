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
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.repository.SegmentoNaoRecebidoRepository;

@Repository
public class SegmentoNaoRecebidoRepositoryImpl extends AbstractRepositoryModel<SegmentoNaoRecebido, Long> implements SegmentoNaoRecebidoRepository {

	private Map<String, Object> parameters;
	
	public SegmentoNaoRecebidoRepositoryImpl() {
		super(SegmentoNaoRecebido.class);
	}

	@Override
	public List<CotaNaoRecebeSegmentoDTO> obterCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		parameters = new HashMap<String, Object>();
		
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
		hql.append(" WHERE ");
		
		// WHERE
		if (!filtro.getTipoSegmentoProdutoId().equals(0)) {
			hql.append(" segmentoNaoRecebido.tipoSegmentoProduto.id =  :tipoSegmentoProdutoId");
			parameters.put("tipoSegmentoProdutoId", filtro.getTipoSegmentoProdutoId());
		}
		
		if (filtro.isCotasAtivas()) {
			hql.append(" and cota.situacaoCadastro = :statusCota");
			parameters.put("statusCota", SituacaoCadastro.ATIVO);
		}
		
		Query query =  getSession().createQuery(hql.toString());

		setParameters(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaNaoRecebeSegmentoDTO.class));
		
		configurandoPaginacao(filtro, query);
		
		return query.list();
	}

	@Override
	public List<SegmentoNaoRecebeCotaDTO> obterSegmentosNaoRecebidosCadastradosNaCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		// FIELDS
		hql.append(" SELECT ");
		hql.append(" segmentoNaoRecebido.id as segmentoNaoRecebidoId, "); // Id Segmento
		hql.append(" tipoSegmentoProduto.descricao as nomeSegmento, "); // Nome Segmento
		hql.append(" usuario.nome as nomeUsuario, "); // NOME DO USUÁRIO
		hql.append(" segmentoNaoRecebido.dataAlteracao as dataAlteracao "); // DATA ALTERAÇÃO

		// FROM
		hql.append(" FROM ");
		hql.append(" SegmentoNaoRecebido as segmentoNaoRecebido ");
		hql.append(" inner join segmentoNaoRecebido.cota as cota ");
		hql.append(" inner join cota.pessoa as pessoa ");
		hql.append(" inner join segmentoNaoRecebido.tipoSegmentoProduto as tipoSegmentoProduto ");
		hql.append(" inner join segmentoNaoRecebido.usuario as usuario ");

		// WHERE
		hql.append(" WHERE ");

		if (filtro.getNumeroCota() != null && !filtro.getNumeroCota().equals(0)) {
			hql.append(" cota.numeroCota = :numeroCota ");
			parameters.put("numeroCota", filtro.getNumeroCota());
		}
		else if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa )");
			parameters.put("nomePessoa", filtro.getNomeCota());
		}
		
		Query query =  getSession().createQuery(hql.toString());

		setParameters(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(SegmentoNaoRecebeCotaDTO.class));
		
		configurandoPaginacao(filtro, query);
		
		return query.list();
	}

	@Override
	public List<CotaDTO> obterCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" cota.id as idCota, ");
		hql.append(" cota.numeroCota as numeroCota, "); 
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomePessoa");
		
		hql.append(" From ");
		hql.append(" Cota as cota ");
		hql.append(" join cota.pessoa as pessoa ");
		
		hql.append(" WHERE ");

		if (filtro.getNumeroCota() != null && !filtro.getNumeroCota().equals(0)) {
			hql.append(" cota.numeroCota = :numeroCota ");
			parameters.put("numeroCota", filtro.getNumeroCota());
		}
		else if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') like :nomePessoa ");
			parameters.put("nomePessoa", "%" + filtro.getNomeCota() + "%");
		}
		
		hql.append(" and ");
		hql.append(" cota.id not in ");
		hql.append(" ( SELECT "); // inicio da query
		hql.append(" seg.cota.id ");
		hql.append(" FROM ");
		hql.append(" SegmentoNaoRecebido as seg ");
		hql.append(" WHERE ");
		hql.append(" seg.tipoSegmentoProduto.id = :tipoSegmentoProdutoId ) "); // finaliza a query
		
		if (filtro.getTipoSegmentoProdutoId() != null && !filtro.getTipoSegmentoProdutoId().equals(0)) {
			parameters.put("tipoSegmentoProdutoId", filtro.getTipoSegmentoProdutoId());
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		setParameters(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaDTO.class));
		
		return query.list();
	}

	@Override
	public List<TipoSegmentoProduto> obterSegmentosElegiveisParaInclusaoNaCota(FiltroSegmentoNaoRecebidoDTO filtro) {
		parameters = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ");
		hql.append(" tipoSegmentoProduto.id as id, ");
		hql.append(" tipoSegmentoProduto.descricao as descricao ");
		
		hql.append(" From ");
		hql.append(" TipoSegmentoProduto as tipoSegmentoProduto ");

		hql.append(" WHERE ");
		hql.append(" tipoSegmentoProduto.id not in ");
		hql.append(" ( SELECT ");
		hql.append(" segmentoNaoRecebido.tipoSegmentoProduto.id ");
		hql.append(" FROM ");
		hql.append(" SegmentoNaoRecebido as segmentoNaoRecebido ");
		hql.append(" inner join segmentoNaoRecebido.cota as cota ");
		hql.append(" inner join cota.pessoa as pessoa ");
		hql.append(" inner join segmentoNaoRecebido.tipoSegmentoProduto as tipoSegmentoProduto ");
		
		if (filtro.getNumeroCota() != null || filtro.getNomeCota() != null || filtro.getNomeSegmento() != null) {
			hql.append(" WHERE ");
		}
		
		if (filtro.getNumeroCota() != null && !filtro.getNumeroCota().equals(0)) {
			hql.append(" cota.numeroCota = :numeroCota )");
			parameters.put("numeroCota", filtro.getNumeroCota());
		}
		else if (filtro.getNomeCota() != null && !filtro.getNomeCota().isEmpty()) {
			hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome,'') = :nomePessoa )");
			parameters.put("nomePessoa", filtro.getNomeCota());
		}
		
		if (filtro.getNomeSegmento() != null && !filtro.getNomeSegmento().isEmpty()) {
			if (filtro.isAutoComplete()) {
				hql.append(" and tipoSegmentoProduto.descricao = :tipoSegmentoProduto )");
				parameters.put("tipoSegmentoProduto", filtro.getNomeSegmento());
			}else {
				hql.append(" and tipoSegmentoProduto.descricao like :tipoSegmentoProduto )");
				parameters.put("tipoSegmentoProduto", filtro.getNomeSegmento() + "%");
			}
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		setParameters(query);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(TipoSegmentoProduto.class));
		
		return query.list();
	}
	
	/**
	 * @param filtro
	 * @param query
	 */
	private void configurandoPaginacao(
			FiltroSegmentoNaoRecebidoDTO filtro, Query query) {
		
		if (filtro.getPaginacao().getPosicaoInicial() != null) {
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		}
		
		if (filtro.getPaginacao().getQtdResultadosTotal().equals(0)) {
			filtro.getPaginacao().setQtdResultadosTotal(query.list().size());
		}
		
		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) {
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
	}

	private void setParameters(Query query) {
		for (String key : parameters.keySet()) {
			query.setParameter(key, parameters.get(key));
		}
	}

}
