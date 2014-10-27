package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.AjusteReparteDTO;
import br.com.abril.nds.model.distribuicao.AjusteReparte;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.AjusteReparteRepository;
import br.com.abril.nds.vo.PaginacaoVO;

@Repository
public class AjusteReparteRepositoryImpl extends AbstractRepositoryModel<AjusteReparte, Long> implements AjusteReparteRepository {
	
	public AjusteReparteRepositoryImpl( ) {
		super(AjusteReparte.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AjusteReparteDTO> buscarTodasCotas(AjusteReparteDTO dto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" ajuste.id as idAjusteReparte, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as statusCota, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, ");
		hql.append(" pdv.nome as nomePDV, ");
		hql.append(" ajuste.formaAjuste as formaAjusteAplicado, ");
		hql.append(" ajuste.ajusteAplicado as ajusteAplicado, ");
		hql.append(" ajuste.dataInicio as dataInicio, ");
		hql.append(" ajuste.dataFim as dataFim, ");
		hql.append(" ajuste.motivo as motivoAjusteAplicado, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" ajuste.dataAlteracao as dataAlteracao, ");
		hql.append(" TIME(ajuste.dataAlteracao) as hora, ");
		hql.append(" tipoSegmento.id as idSegmento ");
		
		hql.append(" FROM AjusteReparte AS ajuste ");
		hql.append(" LEFT JOIN ajuste.cota AS cota ");
		hql.append(" LEFT JOIN cota.pdvs AS pdv ");
		hql.append(" LEFT JOIN pdv.caracteristicas AS caracteristicasPdv ");
		hql.append(" LEFT JOIN cota.pessoa AS pessoa ");
		hql.append(" LEFT JOIN ajuste.usuario  AS usuario ");
		hql.append(" LEFT JOIN ajuste.tipoSegmentoAjuste AS tipoSegmento ");
		
		hql.append(" WHERE caracteristicasPdv.pontoPrincipal in (true) ");
		
		hql.append(" GROUP BY cota.numeroCota ");
		
		if ((dto.getPaginacao() != null) && (dto.getPaginacao().getSortColumn() != null)) {
			
			if(dto.getPaginacao().getSortColumn().equalsIgnoreCase("ACAO")){
				hql.append(" ORDER BY numeroCota ");
			}else{
				hql.append(" ORDER BY ");
				hql.append(" "+dto.getPaginacao().getSortColumn());
			}
			
			hql.append(" "+dto.getPaginacao().getSortOrder());
			
		} else {
			hql.append(" ORDER BY numeroCota ");
		}
		

		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(AjusteReparteDTO.class));
		
		if (dto != null){
			configurarPaginacao(dto, query);
		}
		
		return query.list();
		
	}
	
	@Override
	public AjusteReparteDTO buscarPorIdAjuste(Long id) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" ajuste.id as idAjusteReparte, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as status, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, ");
		hql.append(" pdv.nome as nomePDV, ");
		hql.append(" ajuste.formaAjuste as formaAjuste, ");
		hql.append(" ajuste.ajusteAplicado as ajusteAplicado, ");
		hql.append(" ajuste.dataInicio as dataInicio, ");
		hql.append(" ajuste.dataFim as dataFim, ");
		hql.append(" ajuste.motivo as motivoAjuste, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" ajuste.dataAlteracao as dataAlteracao, ");
		hql.append(" tipoSegmento.id as idSegmento ");
		
		hql.append(" FROM AjusteReparte AS ajuste ");
		hql.append(" LEFT JOIN ajuste.cota as cota ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		hql.append(" LEFT JOIN pdv.caracteristicas AS caracteristicasPdv ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN ajuste.usuario  as usuario ");
		hql.append(" LEFT JOIN ajuste.tipoSegmentoAjuste AS tipoSegmento ");
		hql.append(" WHERE ajuste.id = :ID_AJUSTE ");
		hql.append(" AND caracteristicasPdv.pontoPrincipal in (true) ");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("ID_AJUSTE", id);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				AjusteReparteDTO.class));
		
		return (AjusteReparteDTO) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<AjusteReparteDTO>  buscarPorIdCota(Long idCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT ");
		hql.append(" ajuste.id as idAjusteReparte, ");
		hql.append(" cota.numeroCota as numeroCota, ");
		hql.append(" cota.situacaoCadastro as status, ");
		hql.append(" coalesce(pessoa.nomeFantasia, pessoa.razaoSocial, pessoa.nome, '') as nomeCota, ");
		hql.append(" pdv.nome as nomePDV, ");
		hql.append(" ajuste.formaAjuste as formaAjuste, ");
		hql.append(" ajuste.ajusteAplicado as ajusteAplicado, ");
		hql.append(" ajuste.dataInicio as dataInicio, ");
		hql.append(" ajuste.dataFim as dataFim, ");
		hql.append(" ajuste.motivo as motivoAjuste, ");
		hql.append(" usuario.nome as nomeUsuario, ");
		hql.append(" ajuste.dataAlteracao as dataAlteracao, ");
		hql.append(" tipoSegmento.id as idSegmento ");
		
		hql.append(" FROM AjusteReparte AS ajuste ");
		hql.append(" LEFT JOIN ajuste.cota as cota ");
		hql.append(" LEFT JOIN cota.pdvs as pdv ");
		hql.append(" LEFT JOIN cota.pessoa as pessoa ");
		hql.append(" LEFT JOIN ajuste.usuario  as usuario ");
		hql.append(" LEFT JOIN ajuste.tipoSegmentoAjuste AS tipoSegmento ");
		
		hql.append(" WHERE cota.id = :ID_COTA ");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("ID_COTA", idCota);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				AjusteReparteDTO.class));
		
		return query.list();
	}
	
	private void configurarPaginacao(AjusteReparteDTO dto, Query query) {

		PaginacaoVO paginacao = dto.getPaginacao();

		if (paginacao.getQtdResultadosTotal().equals(0)) {
			paginacao.setQtdResultadosTotal(query.list().size());
		}

		if(paginacao.getQtdResultadosPorPagina() != null) {
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}

		if (paginacao.getPosicaoInicial() != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
		}
	}
	
	@Override
	public void execucaoQuartz (){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" delete from ajuste_reparte WHERE datediff(data_fim,data_inicio)>180;");
		SQLQuery createSQLQuery = this.getSession().createSQLQuery(hql.toString());
		
		createSQLQuery.executeUpdate();
		
	}

	@Override
	public int qtdAjusteSegmento(Long idCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT COUNT(*) ");
		hql.append(" FROM AjusteReparte AS ajuste ");
		hql.append(" LEFT JOIN ajuste.cota AS cota ");
		hql.append(" WHERE cota.id = :ID_COTA ");
		hql.append(" AND ajuste.tipoSegmentoAjuste > 0 ");

		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("ID_COTA", idCota);
		
		if(((Long)query.uniqueResult()) > 0){
			return ((Long)query.uniqueResult()).intValue();
		}else{
			return 0;
		}
	}

	@Override
	public Integer vendaMedia() {
		
		StringBuilder sql = new StringBuilder();

		sql.append("select VENDA_MEDIA_MAIS from DISTRIBUIDOR_GRID_DISTRIBUICAO ");
		
		Query query = super.getSession().createSQLQuery(sql.toString());
		
		return (Integer)query.uniqueResult();
		
	}
}
