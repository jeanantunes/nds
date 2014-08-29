package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.MovimentoFechamentoFiscalRepository;

@Repository
public class MovimentoFechamentoFiscalRepositoryImpl extends AbstractRepositoryModel<MovimentoFechamentoFiscal, Long> implements MovimentoFechamentoFiscalRepository {
    
    @Autowired
    private DataSource dataSource;
    
    public MovimentoFechamentoFiscalRepositoryImpl() {
        super(MovimentoFechamentoFiscal.class);
    }

	@Override
	@SuppressWarnings("unchecked")
	public List<MovimentoFechamentoFiscalCota> buscarPorChamadaEncalheCota(Long chamadaEncalheCotaId) {
		
		Criteria criteria = getSession().createCriteria(MovimentoFechamentoFiscalCota.class);
		criteria.createAlias("chamadaEncalheCota", "chamadaEncalheCota");
		criteria.add(Restrictions.eq("chamadaEncalheCota.id", chamadaEncalheCotaId));
		
		return (List<MovimentoFechamentoFiscalCota>) criteria.list();
	}

	@Override
	public MovimentoFechamentoFiscalFornecedor buscarPorProdutoEdicaoTipoMovimentoEstoque(ProdutoEdicao produtoEdicao, TipoMovimentoEstoque tipoMovimentoEstoque) {
		
		Criteria criteria = getSession().createCriteria(MovimentoFechamentoFiscalFornecedor.class);
		criteria.createAlias("origemMovimentoFechamentoFiscal", "origemMovimentoFechamentoFiscal");
		criteria.createAlias("origemMovimentoFechamentoFiscal.movimento", "movimento");
		criteria.add(Restrictions.eq("movimento.tipoMovimento", tipoMovimentoEstoque));
		criteria.add(Restrictions.eq("movimento.produtoEdicao", produtoEdicao));
		criteria.add(Restrictions.eq("notaFiscalDevolucaoSimbolicaEmitida", false));
		criteria.add(Restrictions.eq("notaFiscalLiberadaEmissao", true));
		
		return (MovimentoFechamentoFiscalFornecedor) criteria.uniqueResult();		
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Long> obterMECIdsPelosMovFechamentosFiscaisCota(List<Long> idsMFFC) {
		
		StringBuilder sql = new StringBuilder(" SELECT ")
			.append(" MOVIMENTO_ID as mecId ")
			.append(" from MOVIMENTO_FECHAMENTO_FISCAL_ORIGEM_ITEM mffoi ")
			.append(" where MOVIMENTO_FECHAMENTO_FISCAL_ID in (:idsMFFC)");
		
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameterList("idsMFFC", idsMFFC);
		
		query.addScalar("mecId", StandardBasicTypes.LONG);
		
		return query.list();		
	}

	@Override
	public void atualizarMovimentosFechamentosFiscaisPorLancamento(long lancamentoId, boolean desobrigaEmissaoDevolucaoSimbolica, boolean desobrigaEmissaoVendaConsignado) {
		
		StringBuilder sql = new StringBuilder("update MOVIMENTO_FECHAMENTO_FISCAL_COTA mffc ")
			.append(" inner join CHAMADA_ENCALHE_COTA cec on mffc.CHAMADA_ENCALHE_COTA_ID=cec.ID ")
			.append(" inner join CHAMADA_ENCALHE ce on cec.CHAMADA_ENCALHE_ID=ce.ID ")
			.append(" inner join CHAMADA_ENCALHE_LANCAMENTO cel on cel.CHAMADA_ENCALHE_ID = ce.id ")
			.append(" inner join LANCAMENTO l on l.id = cel.LANCAMENTO_ID ")
			.append(" inner join COTA c on cec.COTA_ID=c.ID ") 
			.append(" set mffc.NOTA_FISCAL_LIBERADA_EMISSAO = true ")
			.append(" 	, mffc.DESOBRIGA_NOTA_FISCAL_DEVOLUCAO_SIMBOLICA = false ")
			.append(" 	, mffc.NOTA_FISCAL_VENDA_EMITIDA = false ")
			.append(" 	, mffc.DESOBRIGA_NOTA_FISCAL_VENDA = :desobrigaEmissaoVendaConsignado ")
			.append(" 	, mffc.NOTA_FISCAL_DEVOLUCAO_SIMBOLICA_EMITIDA = case when (c.CONTRIBUINTE_ICMS = true) then true else :desobrigaEmissaoDevolucaoSimbolica end ")
			.append(" where l.id = :lancamentoId ");
	
		SQLQuery query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("lancamentoId", lancamentoId);
		query.setParameter("desobrigaEmissaoDevolucaoSimbolica", desobrigaEmissaoDevolucaoSimbolica);
		query.setParameter("desobrigaEmissaoVendaConsignado", desobrigaEmissaoVendaConsignado);
		
		query.executeUpdate();	
		
	}
    
}