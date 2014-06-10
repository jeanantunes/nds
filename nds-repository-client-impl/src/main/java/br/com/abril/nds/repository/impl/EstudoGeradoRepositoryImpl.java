package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoGeradoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.planejamento.EstudoGerado}.
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class EstudoGeradoRepositoryImpl extends AbstractRepositoryModel<EstudoGerado, Long> implements EstudoGeradoRepository {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(EstudoCotaGeradoRepositoryImpl.class);

	@Autowired
	private DataSource dataSource;
	
	/**
	 * Construtor.
	 */
	public EstudoGeradoRepositoryImpl() {
		
		super(EstudoGerado.class);
	}
	
	@Override
	public void liberarEstudo(List<Long> listIdEstudos, boolean liberado) {
		
		StringBuilder hql = new StringBuilder("update EstudoGerado set");
		hql.append(" liberado = :statusEstudo")
		   .append(" where id in (:listIdEstudos)");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("statusEstudo", liberado);
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}
	
	@Override
	public EstudoGerado obterEstudoECotasPorIdEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select estudoCota.estudo from EstudoCotaGerado estudoCota");
		hql.append(" where estudoCota.estudo.id = :estudo");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("estudo", idEstudo);
		
		EstudoGerado estudo = (EstudoGerado) query.uniqueResult();
		
		return estudo;
	}

	@Override
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append("   qtdReparteDistribuidor, ");
		sql.append("   (qtdReparteDistribuidor - qtdReparteADistribuir) as qtdSobraEstudo, ");
		sql.append("   (qtdReparteADistribuir - qtdReparteDistribuidoEstudo) as saldo, ");
		sql.append("   qtdReparteDistribuidoEstudo, ");
		sql.append("   qtdCotasAtivas, ");
		sql.append("   qtdCotasRecebemReparte, ");
		sql.append("   qtdCotasAdicionadasPelaComplementarAutomatica, ");
		sql.append("   CAST(qtdReparteMinimoSugerido as UNSIGNED INTEGER) as qtdReparteMinimoSugerido, ");
		sql.append("   (qtdReparteDistribuidoEstudo / qtdCotasRecebemReparte) as reparteMedioCota, ");
		sql.append("   abrangenciaSugerida, ");
		sql.append("   CAST(qtdReparteMinimoEstudo as UNSIGNED INTEGER) as qtdReparteMinimoEstudo, ");
		sql.append("   ( qtdCotasRecebemReparte / qtdCotasAtivas ) * 100 AS abrangenciaEstudo, ");
		sql.append("   ( qtdCotasQueVenderam  / qtdCotasAtivas ) * 100 AS abrangenciaDeVenda ");

		sql.append("   FROM ");
		sql.append("   ( ");
		sql.append("     SELECT ");
		
		sql.append("       (SELECT case when (estp.QTDE is null or estp.QTDE=0) then case when plp.NUMERO_PERIODO=1 then ((lc.REPARTE)-lc.REPARTE_PROMOCIONAL) else CASE WHEN lc.REPARTE=0 THEN eg.QTDE_REPARTE ELSE lc.REPARTE END end else estp.qtde end as rprte ");
		sql.append("       			From estudo_gerado eg JOIN lancamento lc ON lc.ID = eg.LANCAMENTO_ID LEFT JOIN periodo_lancamento_parcial plp ON plp.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append("       			LEFT JOIN estoque_produto estp ON estp.PRODUTO_EDICAO_ID = eg.PRODUTO_EDICAO_ID where eg.ID = :estudoId ) AS qtdReparteDistribuidor, ");
		
		sql.append("       (SELECT qtde_reparte FROM estudo_gerado where id = :estudoId) AS qtdReparteADistribuir, ");
		sql.append("       (SELECT sum(reparte) FROM estudo_cota_gerado WHERE estudo_id = :estudoId ) AS qtdReparteDistribuidoEstudo, ");
		sql.append("       (SELECT count(id) FROM cota WHERE SITUACAO_CADASTRO = 'ATIVO') AS qtdCotasAtivas, ");
		sql.append("       (SELECT count(DISTINCT estudo_cota_gerado.cota_id) FROM estudo_cota_gerado"); 
		sql.append(" 				WHERE ESTUDO_ID = :estudoId AND reparte IS NOT NULL) AS qtdCotasRecebemReparte, ");
		sql.append("       (SELECT COUNT(id) FROM estudo_cota_gerado WHERE classificacao IN ('CP') and estudo_id = :estudoId ) AS qtdCotasAdicionadasPelaComplementarAutomatica, ");
		sql.append(" 	   IFNULL((SELECT estg.reparte_minimo as repMin FROM estudo_gerado estg WHERE id = :estudoId ),0) AS qtdReparteMinimoEstudo, ");
		sql.append("	   (SELECT estrat.reparte_minimo FROM estrategia estrat JOIN estudo_gerado estudo ON estudo.PRODUTO_EDICAO_ID = estrat.PRODUTO_EDICAO_ID WHERE estudo.ID = :estudoId) AS qtdReparteMinimoSugerido, ");
		sql.append("	   (SELECT abrangencia FROM estrategia JOIN estudo_gerado estudo ON estudo.PRODUTO_EDICAO_ID = estrategia.PRODUTO_EDICAO_ID WHERE estudo.ID = :estudoId) AS abrangenciaSugerida, ");
		sql.append(" 	   (SELECT COUNT( DISTINCT (CASE WHEN qtde_recebida - qtde_devolvida > 0 THEN cota_id ELSE null END)) FROM estoque_produto_cota");
		sql.append(" 		 	WHERE estoque_produto_cota.produto_edicao_id IN (SELECT produto_edicao_id FROM estudo_produto_edicao_base WHERE estudo_id = :estudoId)");
		sql.append(" 		 	AND estoque_produto_cota.cota_id IN (SELECT cota_id FROM estudo_cota_gerado WHERE estudo_id = :estudoId)) AS qtdCotasQueVenderam");
		sql.append("   ) AS base ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ResumoEstudoHistogramaPosAnaliseDTO.class));
		
		ResumoEstudoHistogramaPosAnaliseDTO uniqueResult = (ResumoEstudoHistogramaPosAnaliseDTO) query.uniqueResult();
		
		return uniqueResult;
	}
	
	@Override
    @Transactional
	public void remover(EstudoGerado estudo) {
		
		Long idEstudo = estudo.getId();

        SQLQuery queryPdvs = getSession().createSQLQuery("delete from estudo_pdv where estudo_id = :estudo_id");
        queryPdvs.setLong("estudo_id", idEstudo);
        queryPdvs.executeUpdate();

        Query queryBases = getSession().createSQLQuery("DELETE FROM ESTUDO_PRODUTO_EDICAO_BASE WHERE ESTUDO_ID = :ESTUDO_ID ");
		queryBases.setLong("ESTUDO_ID", estudo.getId());
		queryBases.executeUpdate();
		
		Query queryProdutos = getSession().createSQLQuery("DELETE FROM ESTUDO_PRODUTO_EDICAO WHERE ESTUDO_ID = :ESTUDO_ID ");
		queryProdutos.setLong("ESTUDO_ID", estudo.getId());
		queryProdutos.executeUpdate();
		
		super.remover(estudo);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoGerado> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from EstudoGerado");
		hql.append(" where dataCadastro between :dateStart and :dateEnd ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("dateStart", dataStart);
		query.setParameter("dateEnd", dataEnd);
		
		return (List<EstudoGerado>) query.list();
	}
	
    @Override
    public EstudoGerado obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

		StringBuilder hql = new StringBuilder();
	
		hql.append(" from EstudoGerado estudo ");
		hql.append(" where estudo.id = :numeroEstudoOriginal ");

		Query query = getSession().createQuery(hql.toString());
	
		query.setParameter("numeroEstudoOriginal", divisaoEstudoVO.getNumeroEstudoOriginal());

		EstudoGerado estudo = (EstudoGerado) query.uniqueResult();
	
		return estudo;
    }

	@Override
	public void setIdLancamentoNoEstudo(Long idLancamento, Long idEstudo) {

		Query query = this.getSession().createQuery("update EstudoGerado set LANCAMENTO_ID = :idLancamento where id = :idEstudo ");
		
		query.setParameter("idLancamento", idLancamento);
		query.setParameter("idEstudo", idEstudo);
		
		query.executeUpdate();
	}
	
	@Override
	public Long countDeCotasEntreEstudos(Long estudoBase, Long estudoSomado) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(*) from estudo_cota_gerado ec where ec.ESTUDO_ID=")
				.append( estudoBase.toString()) 
				.append(" and ec.reparte>0 and ec.COTA_ID in (  ")
			.append(" 	select ec2.COTA_ID from estudo_cota_gerado ec2 where ec2.ESTUDO_ID = ")
					.append( estudoSomado.toString())	 
					.append( " AND ec.reparte>0)" );
		
		Query query =	this.getSession().createSQLQuery(hql.toString());
		
		BigInteger count = (BigInteger) query.uniqueResult();
		
		return count.longValue();
	}

    @Override
    @Transactional(readOnly = true)
    public int obterCotasComRepartePorIdEstudo(Long estudoId) {
        return ((Number) this.getSession().createCriteria(EstudoCotaGerado.class)
                .add(Restrictions.eq("estudo.id", estudoId))
                .add(Restrictions.gt("reparte", BigInteger.ZERO))
                .setProjection(Projections.rowCount())
                .uniqueResult()).intValue();
    }

	@Override
	public BigDecimal reparteFisicoOuPrevistoLancamento(Long idEstudo) {

		StringBuilder sql = new StringBuilder();
		
		sql.append(" SELECT ");
		sql.append(" case when estp.QTDE is null ");
		sql.append(" 	then ");
		sql.append("  		case when plp.NUMERO_PERIODO=1 ");
		sql.append(" 			 then ");
		sql.append(" 				((lc.REPARTE)-lc.REPARTE_PROMOCIONAL)  ");
		sql.append("			else lc.REPARTE ");
		sql.append(" 		end ");
		sql.append(" 	else estp.qtde ");
		sql.append(" end ");
		
		sql.append(" From estudo_gerado eg ");
		
		sql.append(" JOIN lancamento lc ON lc.ID = eg.LANCAMENTO_ID ");
		sql.append(" LEFT JOIN periodo_lancamento_parcial plp ON plp.ID = lc.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append(" LEFT JOIN estoque_produto estp ON estp.PRODUTO_EDICAO_ID = eg.PRODUTO_EDICAO_ID  ");
		
		sql.append(" where eg.ID = :estudoId ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", idEstudo);

		return (BigDecimal) query.uniqueResult();
	}
	
	@Override
	public EstudoGerado obterParaAtualizar(Long id) {
		
		Query query = 
			this.getSession().createQuery(
				" select eg from EstudoGerado eg where eg.id = :id ");
		
		query.setLockOptions(LockOptions.UPGRADE);
		
		query.setParameter("id", id);
		
		return (EstudoGerado) query.uniqueResult();
	}
	
}
