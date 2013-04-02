package br.com.abril.nds.repository.impl;
<<<<<<< HEAD
=======

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.ItemNotaEnvioRepository;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoRepositoryImpl extends AbstractRepositoryModel<Estudo, Long> implements EstudoRepository {
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;
	
	@Autowired
	private ItemNotaEnvioRepository itemNotaEnvioRepository;
	
	/**
	 * Construtor.
	 */
	public EstudoRepositoryImpl() {
		
		super(Estudo.class);
	}

	
	@Override
	public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {
		
		String hql = " from Estudo estudo"
				   + " where estudo.dataLancamento = :dataReferencia "
				   + " and estudo.produtoEdicao.id = :idProdutoEdicao "
				   + " order by estudo.dataLancamento ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("dataReferencia", dataReferencia);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setMaxResults(1);
		
		return (Estudo) query.uniqueResult();
	}
	
	@Override
	public void liberarEstudo(List<Long> listIdEstudos, boolean liberado) {
		
		StringBuilder hql = new StringBuilder("update Estudo set");
		hql.append(" status = :statusEstudo")
		   .append(" where id in (:listIdEstudos)");
		
		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("statusEstudo", (liberado)?1:0);
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}
	
	@Override
	public Estudo obterEstudoECotasPorIdEstudo(Long idEstudo) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" select estudoCota.estudo from EstudoCota estudoCota");
		hql.append(" where estudoCota.estudo.id = :estudo");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("estudo", idEstudo);
		
		Estudo estudo = (Estudo)query.uniqueResult();
		
		return estudo;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Estudo> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from Estudo");
		hql.append(" where dataCadastro between :dateStart and :dateEnd ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("dateStart", dataStart);
		query.setParameter("dateEnd", dataEnd);
		
		return (List<Estudo>)query.list();
	}
>>>>>>> refs/remotes/DGBTi/fase2

<<<<<<< HEAD
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.DivisaoEstudoDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade {@link br.com.abril.nds.model.planejamento.Estudo}.
 * 
 * @author Discover Technology
 * 
 */
@Repository
public class EstudoRepositoryImpl extends AbstractRepositoryModel<Estudo, Long> implements EstudoRepository {

    /**
     * Construtor.
     */
    public EstudoRepositoryImpl() {

	super(Estudo.class);
    }

    @Override
    public Estudo obterEstudoDoLancamentoPorDataProdutoEdicao(Date dataReferencia, Long idProdutoEdicao) {

	String hql = " from Estudo estudo" + " where estudo.dataLancamento = :dataReferencia " + " and estudo.produtoEdicao.id = :idProdutoEdicao "
		+ " order by estudo.dataLancamento ";

	Query query = super.getSession().createQuery(hql);

	query.setParameter("dataReferencia", dataReferencia);

	query.setParameter("idProdutoEdicao", idProdutoEdicao);

	query.setMaxResults(1);

	return (Estudo) query.uniqueResult();
    }

    @Override
    public void liberarEstudo(List<Long> listIdEstudos, boolean liberado) {

	StringBuilder hql = new StringBuilder("update Estudo set");
	hql.append(" status = :statusEstudo").append(" where id in (:listIdEstudos)");

	Query query = this.getSession().createQuery(hql.toString());

	query.setParameter("statusEstudo", (liberado) ? 1 : 0);

	query.setParameterList("listIdEstudos", listIdEstudos);

	query.executeUpdate();
    }

    @Override
    public Estudo obterEstudoECotasPorIdEstudo(Long idEstudo) {

	StringBuilder hql = new StringBuilder();
	hql.append(" select estudoCota.estudo from EstudoCota estudoCota");
	hql.append(" where estudoCota.estudo.id = :estudo");

	Query query = getSession().createQuery(hql.toString());
	query.setParameter("estudo", idEstudo);

	Estudo estudo = (Estudo) query.uniqueResult();

	return estudo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Estudo> obterEstudosPorIntervaloData(Date dataStart, Date dataEnd) {

	StringBuilder hql = new StringBuilder();
	hql.append(" from Estudo");
	hql.append(" where dataCadastro between :dateStart and :dateEnd ");

	Query query = getSession().createQuery(hql.toString());
	query.setParameter("dateStart", dataStart);
	query.setParameter("dateEnd", dataEnd);

	return (List<Estudo>) query.list();
    }

    @Override
    public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
	StringBuilder sql = new StringBuilder();

	sql.append(" select ");
	sql.append("   qtdCotasAtivas, ");
	sql.append("   qtdCotasRecebemReparte, ");
	sql.append("   qtdCotasAdicionadasPelaComplementarAutomatica ");
	sql.append("   qtdReparteMinimoSugerido, ");
	sql.append("   qtdReparteMinimoEstudo, ");
	sql.append("   ( qtdCotasRecebemReparte / qtdCotasAtivas ) * 100 as abrangenciaEstudo, ");
	sql.append("   ( qtdCotasQueVenderam  / qtdCotasRecebemReparte ) * 100 as abrangenciaDeVenda ");
	sql.append("   FROM ");
	sql.append("   ( ");
	sql.append("     select ");
	sql.append("       (select count(id) from cota where SITUACAO_CADASTRO = 'ATIVO') AS qtdCotasAtivas, ");
	sql.append("       (select count(estudo_cota.cota_id) from estudo_cota");
	sql.append("        	inner join movimento_estoque_cota ON movimento_estoque_cota.ESTUDO_COTA_ID = estudo_cota.ID ");
	sql.append(" 				where ESTUDO_ID = :estudoId) AS qtdCotasRecebemReparte, ");
	sql.append("       (select count(id) from estudo_cota where CLASSIFICACAO in ('CP') and estudo_id = :estudoId ) as qtdCotasAdicionadasPelaComplementarAutomatica, ");
	sql.append(" 		ifnull((select distinct estudo.REPARTE_MINIMO_SUGERIDO from estudo where id = :estudoId ),0) as qtdReparteMinimoSugerido, ");
	sql.append(" 		ifnull((select min(REPARTE_MINIMO) from estudo_cota where estudo_id = :estudoId ),0) as qtdReparteMinimoEstudo, ");
	sql.append(" 		(select sum(case when qtde_recebida - qtde_devolvida > 0 then 1 else 0 end) from estoque_produto_cota ");
	sql.append(" 		 inner join produto_edicao on estoque_produto_cota.produto_edicao_id = produto_edicao.id");
	sql.append(" 		 where estoque_produto_cota.produto_edicao_id = (select produto_edicao_id from estudo where id = :estudoId)");
	sql.append(" 		 and estoque_produto_cota.COTA_ID in (select cota_id from estudo_cota where estudo_id = :estudoId)) as qtdCotasQueVenderam");
	sql.append("   ) as base ");

	SQLQuery query = this.getSession().createSQLQuery(sql.toString());

	query.setParameter("estudoId", estudoId);

	query.setResultTransformer(new AliasToBeanResultTransformer(ResumoEstudoHistogramaPosAnaliseDTO.class));

	return (ResumoEstudoHistogramaPosAnaliseDTO) query.uniqueResult();
    }

    @Override
    public Estudo obterEstudoByEstudoOriginalFromDivisaoEstudo(DivisaoEstudoDTO divisaoEstudoVO) {

	StringBuilder hql = new StringBuilder();

	hql.append(" from Estudo estudo ");
	hql.append(" where estudo.id = :numeroEstudoOriginal ");
	// hql.append(" and estudo.produtoEdicao.produto.codigo = :codigoProduto ");
	// hql.append(" and estudo.produtoEdicao.produto.nome = :nomeProduto ");
	// hql.append(" and estudo.produtoEdicao.numeroEdicao = :numeroEdicao ");
	// hql.append(" and estudo.dataLancamento = :dataDistribuicao ");

	Query query = getSession().createQuery(hql.toString());

	query.setParameter("numeroEstudoOriginal", divisaoEstudoVO.getNumeroEstudoOriginal());
	// query.setParameter("codigoProduto", divisaoEstudoVO.getCodigoProduto());
	// query.setParameter("nomeProduto", divisaoEstudoVO.getNomeProduto());
	// query.setParameter("numeroEdicao", divisaoEstudoVO.getEdicaoProduto());
	// query.setParameter("dataDistribuicao", divisaoEstudoVO.getDataDistribuicao());

	Estudo estudo = (Estudo) query.uniqueResult();
	
	return estudo;
    }

    @Override
    public Long obterMaxId() {

	StringBuilder hql = new StringBuilder();
	hql.append(" select max(estudo.id) from Estudo estudo ");

	Query query = getSession().createQuery(hql.toString());

	Long maxId = (Long) query.uniqueResult();

	return maxId;
    }
}
=======

	@Override
	public ResumoEstudoHistogramaPosAnaliseDTO obterResumoEstudo(Long estudoId) {
		StringBuilder sql = new StringBuilder();
		
		sql.append(" select ");
		sql.append("   qtdCotasAtivas, ");
		sql.append("   qtdCotasRecebemReparte, ");
		sql.append("   qtdCotasAdicionadasPelaComplementarAutomatica ");
		sql.append("   qtdReparteMinimoSugerido, ");
		sql.append("   qtdReparteMinimoEstudo, ");
		sql.append("   ( qtdCotasRecebemReparte / qtdCotasAtivas ) * 100 as abrangenciaEstudo, ");
		sql.append("   ( qtdCotasQueVenderam  / qtdCotasRecebemReparte ) * 100 as abrangenciaDeVenda ");
		sql.append("   FROM ");
		sql.append("   ( ");
		sql.append("     select ");
		sql.append("       (select count(id) from cota where SITUACAO_CADASTRO = 'ATIVO') AS qtdCotasAtivas, ");
		sql.append("       (select count(estudo_cota.cota_id) from estudo_cota"); 
		sql.append("        	inner join movimento_estoque_cota ON movimento_estoque_cota.ESTUDO_COTA_ID = estudo_cota.ID ");
		sql.append(" 				where ESTUDO_ID = :estudoId) AS qtdCotasRecebemReparte, ");
		sql.append("       (select count(id) from estudo_cota where CLASSIFICACAO in ('CP') and estudo_id = :estudoId ) as qtdCotasAdicionadasPelaComplementarAutomatica, ");
		sql.append(" 		ifnull((select distinct estudo.REPARTE_MINIMO_SUGERIDO from estudo where id = :estudoId ),0) as qtdReparteMinimoSugerido, ");
		sql.append(" 		ifnull((select min(REPARTE_MINIMO) from estudo_cota where estudo_id = :estudoId ),0) as qtdReparteMinimoEstudo, ");
		sql.append(" 		(select sum(case when qtde_recebida - qtde_devolvida > 0 then 1 else 0 end) from estoque_produto_cota ");
		sql.append(" 		 inner join produto_edicao on estoque_produto_cota.produto_edicao_id = produto_edicao.id");
		sql.append(" 		 where estoque_produto_cota.produto_edicao_id = (select produto_edicao_id from estudo where id = :estudoId)");
		sql.append(" 		 and estoque_produto_cota.COTA_ID in (select cota_id from estudo_cota where estudo_id = :estudoId)) as qtdCotasQueVenderam");
		sql.append("   ) as base ");
		
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("estudoId", estudoId);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ResumoEstudoHistogramaPosAnaliseDTO.class));

		return (ResumoEstudoHistogramaPosAnaliseDTO) query.uniqueResult();
	}
	
	
	@Override
	public void remover(Estudo estudo) {
		
		Long idEstudo = estudo.getId();
		
		movimentoEstoqueCotaRepository.removerMovimentoEstoqueCotaPorEstudo(idEstudo);
		itemNotaEnvioRepository.removerItemNotaEnvioPorEstudo(idEstudo);
		estudoCotaRepository.removerEstudoCotaPorEstudo(idEstudo);
		
		super.remover(estudo);
	}
}












>>>>>>> refs/remotes/DGBTi/fase2
