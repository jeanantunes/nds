package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.EstudoGeradoPreAnaliseDTO;
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
	
	/**
	 * Construtor.
	 */
	public EstudoGeradoRepositoryImpl() {
		
		super(EstudoGerado.class);
	}
	
	@Transactional(readOnly = true)
	@Override
	public EstudoGeradoPreAnaliseDTO obterEstudoPreAnalise(Long id) {
		
		Criteria c = this.getSession().createCriteria(EstudoGerado.class);
		c.createAlias("produtoEdicao", "produtoEdicao");
		c.createAlias("produtoEdicao.produto", "produto");
		c.createAlias("produto.tipoSegmentoProduto", "tipoSegmentoProduto");

		c.add(Restrictions.eq("id", id));
		
		c.setProjection(
			Projections.projectionList().add(
				Projections.alias(Projections.property("produtoEdicao.parcial"), "parcial")
			).add(
				Projections.alias(Projections.property("produto.periodicidade"), "periodicidade")
			).add(
				Projections.alias(Projections.property("tipoSegmentoProduto.id"), "idTipoSegmentoProduto")
			).add(
				Projections.alias(Projections.property("tipoSegmentoProduto.descricao"), "descricaoTipoSegmentoProduto")
			).add(
				Projections.alias(Projections.property("liberado"), "liberado")
			).add(
				Projections.alias(Projections.property("produtoEdicao.id"), "idProdutoEdicao")
			)
		);
		
		c.setResultTransformer(Transformers.aliasToBean(EstudoGeradoPreAnaliseDTO.class));
		
		return (EstudoGeradoPreAnaliseDTO) c.uniqueResult();
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
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId, boolean isEdicoesBaseEspecificas) {

		StringBuilder sql = new StringBuilder();

		sql.append("   select  qtdReparteDistribuidor, ");
		sql.append("   (qtdReparteDistribuidor - qtdReparteADistribuir) AS qtdSobraEstudo, ");
		sql.append("   (qtdReparteDistribuidor - qtdReparteDistribuidoEstudo) AS saldo, ");
		sql.append("   qtdReparteDistribuidoEstudo, ");
		sql.append("   CAST(qtdCotasAtivas AS UNSIGNED INTEGER) as qtdCotasAtivas, ");
		sql.append("   CAST(qtdCotasRecebemReparte AS UNSIGNED INTEGER) as qtdCotasRecebemReparte, ");
		sql.append("   CAST(qtdCotasAdicionadasPelaComplementarAutomatica AS UNSIGNED INTEGER) as qtdCotasAdicionadasPelaComplementarAutomatica, ");
		sql.append("   CAST(qtdReparteMinimoSugerido AS UNSIGNED INTEGER) AS qtdReparteMinimoSugerido, ");
		sql.append("   (qtdReparteDistribuidoEstudo / qtdCotasRecebemReparte) AS reparteMedioCota, ");
		sql.append("   abrangenciaSugerida, ");
		sql.append("   CAST(qtdReparteMinimoEstudo AS UNSIGNED INTEGER) AS qtdReparteMinimoEstudo, ");
		sql.append("   abrangencia AS abrangenciaEstudo, ");
		sql.append("   (qtdCotasQueVenderam / qtdCotasAtivasParaCalculo) * 100 AS abrangenciaDeVenda ");
       
		sql.append("   from ( ");

		sql.append("   select "); 
		sql.append("   eg.QTDE_REPARTE as qtdReparteADistribuir, ");
		sql.append("   eg.ABRANGENCIA as abrangencia, ");
		sql.append("   coalesce(eg.REPARTE_MINIMO, 0) as qtdReparteMinimoEstudo, ");
		sql.append("   sum(ecg.REPARTE) * count(distinct ecg.ID)/count(ecg.ID)  as qtdReparteDistribuidoEstudo, ");
		sql.append("   sum(case when ecg.CLASSIFICACAO='CP' then 1 else 0 end)*count(distinct ecg.ID) / count(ecg.ID) as qtdCotasAdicionadasPelaComplementarAutomatica, ");
		sql.append("   sum(case when c.SITUACAO_CADASTRO='ATIVO' then 1 else 0 end)*count(distinct c.ID) / count(c.ID) as qtdCotasAtivasParaCalculo, ");
		sql.append("   (select count(*) from COTA where SITUACAO_CADASTRO='ATIVO') as qtdCotasAtivas, ");
		sql.append("   sum(case when ecg.REPARTE > 0 then 1 else 0 end)*count(distinct ecg.ID) / count(ecg.ID) as qtdCotasRecebemReparte, ");
		sql.append("   COUNT(DISTINCT (CASE WHEN epc.qtde_recebida - epc.qtde_devolvida > 0 THEN epc.cota_id ELSE NULL END)) as qtdCotasQueVenderam, ");

//		sql.append("   CASE WHEN eg.LIBERADO = 1 THEN  				");
//		sql.append("   CASE WHEN (estp.QTDE IS NULL OR estp.QTDE=0) ");
//		sql.append("   THEN CASE WHEN plp.NUMERO_PERIODO=1  		");
//		sql.append("   THEN ((l.REPARTE)-l.REPARTE_PROMOCIONAL)  	");
//		sql.append("   ELSE CASE WHEN l.REPARTE=0  	  ");
//		sql.append("   THEN eg.QTDE_REPARTE  		  ");
//		sql.append("   ELSE l.REPARTE END END 		  ");
//		sql.append("   ELSE estp.qtde END	 		  ");
//		sql.append("   ELSE 0				 		  ");
//		sql.append("   END AS qtdReparteDistribuidor, ");
		
		sql.append("   eg.QTDE_REPARTE AS qtdReparteDistribuidor, ");
		

		sql.append("   est.ABRANGENCIA AS abrangenciaSugerida, ");
		sql.append("   est.REPARTE_MINIMO as qtdReparteMinimoSugerido ");

		sql.append("   from estudo_gerado eg "); 
		sql.append("   join lancamento l on l.ID=eg.LANCAMENTO_ID ");
		sql.append("   left join periodo_lancamento_parcial plp ON plp.ID = l.PERIODO_LANCAMENTO_PARCIAL_ID ");
		sql.append("   left join estudo_cota_gerado ecg on ecg.ESTUDO_ID=eg.ID ");
		sql.append("   left join estudo_produto_edicao_base epeb on epeb.ESTUDO_ID=eg.ID ");
		sql.append("   left join estoque_produto_cota epc on epc.PRODUTO_EDICAO_ID=epeb.PRODUTO_EDICAO_ID and epc.COTA_ID=ecg.COTA_ID ");
		sql.append("   left join estoque_produto estp on estp.PRODUTO_EDICAO_ID=eg.PRODUTO_EDICAO_ID ");
		sql.append("   left join cota c on c.ID=ecg.COTA_ID ");
		sql.append("   left join estrategia est on eg.PRODUTO_EDICAO_ID=est.PRODUTO_EDICAO_ID ");
		sql.append("   where eg.ID=:estudoId ");

		sql.append("   ) as base; ");

		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.addScalar("qtdReparteDistribuidor", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("qtdSobraEstudo", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("saldo", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("qtdReparteDistribuidoEstudo", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("qtdCotasAtivas", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdCotasRecebemReparte", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdCotasAdicionadasPelaComplementarAutomatica", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("qtdReparteMinimoSugerido", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("reparteMedioCota", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("abrangenciaSugerida", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("qtdReparteMinimoEstudo", StandardBasicTypes.BIG_INTEGER);
		query.addScalar("abrangenciaEstudo", StandardBasicTypes.BIG_DECIMAL);
		query.addScalar("abrangenciaDeVenda", StandardBasicTypes.BIG_DECIMAL);
		
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
				.append(" and ec.reparte!=0 and ec.COTA_ID in (  ")
			.append(" 	select ec2.COTA_ID from estudo_cota_gerado ec2 where ec2.ESTUDO_ID = ")
					.append( estudoSomado.toString())	 
					.append( " AND ec.reparte != 0)" );
		
		Query query =	this.getSession().createSQLQuery(hql.toString());
		
		BigInteger count = (BigInteger) query.uniqueResult();
		
		return count.longValue();
	}

    @Override
    @Transactional(readOnly = true)
    public int obterCotasComRepartePorIdEstudo(Long estudoId) {
        return ((Number) this.getSession().createCriteria(EstudoCotaGerado.class)
                .add(Restrictions.eq("estudo.id", estudoId))
                .add(Restrictions.ne("reparte", BigInteger.ZERO))
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
		sql.append(" 	else (estp.QTDE + estp.QTDE_SUPLEMENTAR + estp.QTDE_DEVOLUCAO_ENCALHE) ");
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
		
		query.setLockOptions(LockOptions.UPGRADE.setTimeOut(60000));
		
		query.setParameter("id", id);
		
		return (EstudoGerado) query.uniqueResult();
	}
	
	@Override
	public Long countEstudoGeradoParaLancamento(Long idLancamento, Date dataLancamento) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select count(*) from estudo_gerado where LANCAMENTO_ID = :idLanc and DATA_LANCAMENTO = :dtLancamento ");
		
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("idLanc", idLancamento);
		query.setParameter("dtLancamento", dataLancamento);
		
		BigInteger count = (BigInteger) query.uniqueResult();
		
		return count.longValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoGerado> obterPorLancamentoId(Long idLancamento) {
		
		StringBuilder hql = new StringBuilder();
		hql.append("from EstudoGerado eg ")
		.append(" where eg.lancamentoID = :idLancamento ");
		
		Query q = getSession().createQuery(hql.toString());
        q.setParameter("idLancamento", idLancamento);
        return (List<EstudoGerado>) q.list();		
	}
	
	
	public EstudoGerado obterEstudoSql(Long id) {
		
		StringBuilder sql = new StringBuilder();
		
		
		sql.append(" SELECT  ");
		sql.append(" ID as id,");
		sql.append(" data_alteracao as dataAlteracao,");
		sql.append(" data_cadastro as dataCadastro,");
		sql.append(" DATA_LANCAMENTO as dataLancamento,");
		sql.append(" DISTRIBUICAO_POR_MULTIPLOS as distribuicaoPorMultiplos,");
		sql.append(" ESTUDO_ORIGEM_COPIA as idEstudoOrigemCopia,");
		sql.append(" LANCAMENTO_ID as lancamentoID,");
		sql.append(" LIBERADO as liberado,");
		sql.append(" PACOTE_PADRAO as pacotePadrao,");
		sql.append(" QTDE_REPARTE as qtdeReparte,");
		sql.append(" REPARTE_DISTRIBUIR as reparteDistribuir,");
	    sql.append(" SOBRA as sobra,");
	//	sql.append(" STATUS as status,");
		sql.append(" PRODUTO_EDICAO_ID as produtoEdicaoId,");
	//	sql.append(" USUARIO_ID as usuarioId,");
		sql.append(" DADOS_VENDA_MEDIA as dadosVendaMedia,");
		sql.append(" REPARTE_MINIMO as reparteMinimo,");
	//	sql.append(" tipo_geracao_estudo as tipoGeracaoEstudo,");
		sql.append(" ABRANGENCIA as abrangencia,");
		sql.append(" REPARTE_TOTAL as reparteTotal ");
	//	sql.append(" USED_MIN_MAX_MIX as usedMinMaxMix ");
		sql.append(" From estudo_gerado eg ");
		sql.append(" where eg.id = :estudoId ");
		
	
		
		
		Query query = getSession().createSQLQuery(sql.toString())	
		.addScalar("id", StandardBasicTypes.LONG)
		.addScalar("dataAlteracao", StandardBasicTypes.TIMESTAMP)
		.addScalar("dataCadastro", StandardBasicTypes.TIMESTAMP)
		.addScalar("dataLancamento", StandardBasicTypes.TIMESTAMP)
		.addScalar("distribuicaoPorMultiplos", StandardBasicTypes.INTEGER)
		.addScalar("idEstudoOrigemCopia", StandardBasicTypes.LONG)
		.addScalar("lancamentoID", StandardBasicTypes.LONG)
		.addScalar("liberado", StandardBasicTypes.BOOLEAN)
		.addScalar("pacotePadrao", StandardBasicTypes.BIG_INTEGER)
		.addScalar("qtdeReparte", StandardBasicTypes.BIG_INTEGER)
		.addScalar("reparteDistribuir", StandardBasicTypes.BIG_INTEGER)
	    .addScalar("sobra", StandardBasicTypes.BIG_INTEGER)
		//.addScalar("status", StandardBasicTypes.STRING)
		.addScalar("produtoEdicaoId", StandardBasicTypes.LONG)
		//.addScalar("usuarioId", StandardBasicTypes.LONG)
		.addScalar("dadosVendaMedia", StandardBasicTypes.STRING)
		.addScalar("reparteMinimo", StandardBasicTypes.BIG_INTEGER)
	//	.addScalar("tipoGeracaoEstudo", StandardBasicTypes.STRING)
		.addScalar("abrangencia", StandardBasicTypes.BIG_DECIMAL)
		.addScalar("reparteTotal", StandardBasicTypes.BIG_INTEGER);
		//.addScalar("usedMinMaxMix", StandardBasicTypes.BOOLEAN);
		
       
		 query.setParameter("estudoId", id);
		query.setResultTransformer(new AliasToBeanResultTransformer(EstudoGerado.class));
		EstudoGerado estudoGerado = (EstudoGerado) query.uniqueResult();
		return estudoGerado;
	}
	
}