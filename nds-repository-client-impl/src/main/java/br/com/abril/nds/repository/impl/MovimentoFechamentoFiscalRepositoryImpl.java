package br.com.abril.nds.repository.impl;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscal;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalCota;
import br.com.abril.nds.model.fiscal.MovimentoFechamentoFiscalFornecedor;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
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
	public List<MovimentoFechamentoFiscalCota> buscarPorChamadaEncalheCota(ChamadaEncalheCota chamadaEncalheCota) {
		
		Criteria criteria = getSession().createCriteria(MovimentoFechamentoFiscalCota.class);
		criteria.add(Restrictions.eq("chamadaEncalheCota", chamadaEncalheCota));
		
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
    
}