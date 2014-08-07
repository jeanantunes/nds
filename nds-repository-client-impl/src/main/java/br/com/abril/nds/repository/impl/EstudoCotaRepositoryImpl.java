package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.util.Intervalo;

/**
 * Classe de implementação referente ao acesso a dados da entidade {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoCotaRepositoryImpl extends AbstractRepositoryModel<EstudoCota, Long> implements EstudoCotaRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoCotaRepositoryImpl() {
		super(EstudoCota.class);
	}

	@Override
	public EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		String hql = " from EstudoCota estudoCota"
				   + " where estudoCota.cota.numeroCota = :numeroCota"
				   + " and estudoCota.estudo.dataLancamento >= :dataReferencia";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("numeroCota", numeroCota);
		query.setParameter("dataReferencia", dataReferencia);
		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstudoCota> obterEstudoCota(Long idCota, Date dataDe, Date dataAte) {
		
		String hql = " from EstudoCota estudoCota "
				   + " join estudoCota.estudo estudo "
				   + " join estudoCota.cota cota "
				   + " join estudo.lancamentos lancamento "
				   + " where cota.id = :idCota"
				   + " and lancamento.dataLancamentoDistribuidor between :dataDe AND :dataAte ";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("idCota", idCota);
		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoCotaDTO> obterEstudoCotaPorDataProdutoEdicao(Long idLancamento, Long idProdutoEdicao) {
			
		StringBuilder hql = new StringBuilder(" select estudoCota.id as id, ") 
			.append(" estudoCota.qtdeEfetiva as qtdeEfetiva, ")
			.append(" cota.id as idCota, ")
			.append(" estudoCota.tipoEstudo as tipoEstudo, ")
			.append(" CASE WHEN cota.devolveEncalhe is not null THEN cota.devolveEncalhe ELSE false END as devolveEncalhe, ")
			.append(" CASE WHEN (cota.parametrosCotaNotaFiscalEletronica.contribuinteICMS = true or cota.parametrosCotaNotaFiscalEletronica.exigeNotaFiscalEletronica = true) ")
			.append(" 	THEN true ELSE false END as cotaContribuinteExigeNotaFiscal, ")
			.append(" cota.tipoCota as tipoCota, ")
			.append(" fornecedor.id as idFornecedorPadraoCota ")
			.append(" from EstudoCota estudoCota ")
			.append(" join estudoCota.estudo estudo ")
			.append(" join estudoCota.cota cota ")
			.append(" join estudo.produtoEdicao produtoEdicao ")
			.append(" left join cota.parametroCobranca.fornecedorPadrao fornecedor ")
			.append(" where estudo.lancamentoID = :idLancamento ") 
			.append(" and produtoEdicao.id = :idProdutoEdicao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("idLancamento", idLancamento);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setResultTransformer(Transformers.aliasToBean(EstudoCotaDTO.class));
		
		return query.list();
	}
	
	@Override
	public EstudoCota obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota) {

		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento= :dataLancamento " 
				   + " and estudoCota.estudo.produtoEdicao.id= :idProdutoEdicao " 
				   + " and estudoCota.cota.id = :idCota";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);

		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
    @Override
    public EstudoCota obterEstudoCotaDeLancamentoComEstudoFechado(Date dataLancamentoDistribuidor, Long idProdutoEdicao, Integer numeroCota) {
		
		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento <= :dataLancamentoDistribuidor " 
				   + " and estudoCota.estudo.produtoEdicao.id = :idProdutoEdicao " 
				   + " and estudoCota.cota.numeroCota = :numeroCota "
				   + " order by estudoCota.estudo.dataLancamento desc ";
		
		Query query = super.getSession().createQuery(hql);
		query.setParameter("dataLancamentoDistribuidor", dataLancamentoDistribuidor);
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("numeroCota", numeroCota);
		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<EstudoCota> obterEstudosCotaParaNotaEnvio(List<Long> idCotas, 
														  Intervalo<Date> periodo, 
														  List<Long> listaIdsFornecedores,
														  String exibirNotasEnvio) {
		
		StringBuffer sql = new StringBuffer("SELECT DISTINCT estudoCota ");

		sql.append(" FROM EstudoCota estudoCota ");
		sql.append(" JOIN estudoCota.cota cota ");
		sql.append(" JOIN estudoCota.estudo estudo ");
		sql.append(" JOIN estudo.lancamentos lancamento ");
		sql.append(" JOIN estudo.produtoEdicao produtoEdicao ");
		sql.append(" JOIN produtoEdicao.produto produto ");
		sql.append(" JOIN produto.fornecedores fornecedor ");

		sql.append(" LEFT JOIN estudoCota.itemNotaEnvios itemNotaEnvios ");
		sql.append(" WHERE cota.id IN (:idCotas) ");
		sql.append(" AND lancamento.status NOT IN (:listaExclusaoStatusLancamento) ");
		sql.append(" AND estudoCota.qtdeEfetiva > 0 ");
		
		if("EMITIDAS".equals(exibirNotasEnvio)) {
			sql.append(" AND itemNotaEnvios is not null ");
		} else if("AEMITIR".equals(exibirNotasEnvio)) {
			sql.append(" AND itemNotaEnvios is null ");
		}
		
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			
			sql.append(" AND (fornecedor IS NULL OR fornecedor.id IN (:listaFornecedores)) ");
		}
		
        if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			
			sql.append(" AND lancamento.dataLancamentoDistribuidor BETWEEN :dataInicio AND :dataFim ");
		}	
		
		Query query = getSession().createQuery(sql.toString());
		
		query.setParameterList("idCotas", idCotas);

		query.setParameterList("listaExclusaoStatusLancamento", new StatusLancamento[] {StatusLancamento.FURO,StatusLancamento.PLANEJADO, StatusLancamento.FECHADO, StatusLancamento.CONFIRMADO, StatusLancamento.EM_BALANCEAMENTO, StatusLancamento.CANCELADO});
		
		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {
			query.setParameterList("listaFornecedores", listaIdsFornecedores);
		}
		if (periodo != null && periodo.getDe() != null && periodo.getAte() != null) {
			query.setParameter("dataInicio", periodo.getDe());
			
			query.setParameter("dataFim", periodo.getAte());
		}

		return query.list();
    }

    @SuppressWarnings("unchecked")
	@Override
    public List<EstudoCota> obterEstudosCota(Long idEstudo) {
	String hql = " from EstudoCota estudoCota where estudoCota.estudo.id = :estudo";

	Query query = super.getSession().createQuery(hql);
	query.setParameter("estudo", idEstudo);
		
	return query.list();
	}
    
    @Override
	public void removerEstudosCotaPorEstudos(List<Long> listIdEstudos) {
		
		StringBuilder hql = new StringBuilder(" delete from EstudoCota ec ");
		
		hql.append(" where ec.estudo.id in (:listIdEstudos) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameterList("listIdEstudos", listIdEstudos);
		
		query.executeUpdate();
	}
    
}
