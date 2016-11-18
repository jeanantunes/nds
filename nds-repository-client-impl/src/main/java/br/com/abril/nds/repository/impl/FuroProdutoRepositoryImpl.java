package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaFuroDTO;
import br.com.abril.nds.dto.CotaResumoDTO;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.movimentacao.FuroProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.FuroProdutoRepository;

@Repository
public class FuroProdutoRepositoryImpl extends AbstractRepositoryModel<FuroProduto, Long>
		implements FuroProdutoRepository {

	public FuroProdutoRepositoryImpl() {
		super(FuroProduto.class);
	}

	@Override
	public FuroProduto obterFuroProdutoPor(Long lancamentoId, Long produtoEdicaoId) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select fp ")
		   .append(" from FuroProduto fp ")
		   .append(" where fp.lancamento.id = :lancamentoId ")
		   .append(" and fp.produtoEdicao.id = :produtoEdicaoId ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("lancamentoId", lancamentoId);
		query.setParameter("produtoEdicaoId", produtoEdicaoId);
		
		return (FuroProduto) query.uniqueResult();		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CotaFuroDTO> obterCobrancaRealizadaParaCotaVista(Long idProdutoEdicao, Date dataFuro, Long idLancamento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numero_cota as numeroCota, p.nome as nome, c.valor as valor, c.NOSSO_NUMERO as nossoNumero ")
		.append(" from CONSOLIDADO_FINANCEIRO_COTA consolidado  ")
		.append(" inner join CONSOLIDADO_MVTO_FINANCEIRO_COTA cmfc  on consolidado.ID=cmfc.CONSOLIDADO_FINANCEIRO_ID  ")
		.append(" inner join MOVIMENTO_FINANCEIRO_COTA mfc on cmfc.MVTO_FINANCEIRO_COTA_ID=mfc.ID ")
		.append(" inner join movimento_estoque_cota mec on mec.MOVIMENTO_FINANCEIRO_COTA_ID = mfc.id ")
		.append(" inner join COTA cota on mfc.COTA_ID = cota.id ")
		.append(" inner join pessoa p on cota.PESSOA_ID = p.id ")
		.append(" left outer join DIVIDA_CONSOLIDADO dc on dc.CONSOLIDADO_ID = consolidado.ID ")
		.append(" left outer join divida d on dc.DIVIDA_ID = d.id ")
		.append(" left outer join cobranca c on d.id = c.DIVIDA_ID ")
		.append(" where consolidado.COTA_ID=cota.ID ")
		.append(" and mec.STATUS_ESTOQUE_FINANCEIRO = 'FINANCEIRO_PROCESSADO' ")
		.append(" and cota.TIPO_COTA = :tipoCota ")
		.append(" and c.DT_EMISSAO = :dataFuro ")
		.append(" and mec.LANCAMENTO_ID = :idLancamento ")
		.append(" and mec.PRODUTO_EDICAO_ID = :idProdutoEdicao ");
		
		SQLQuery query = this.getSession().createSQLQuery(hql.toString());
		
		query.setParameter("tipoCota", TipoCota.A_VISTA.name());
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		query.setParameter("dataFuro", dataFuro);
		query.setParameter("idLancamento", idLancamento);
		
		query.addScalar("numeroCota", StandardBasicTypes.STRING);
    	query.addScalar("nome", StandardBasicTypes.STRING);
    	query.addScalar("valor", StandardBasicTypes.BIG_DECIMAL);       
    	query.addScalar("nossoNumero",  StandardBasicTypes.STRING);   
		
		query.setResultTransformer(new AliasToBeanResultTransformer(CotaFuroDTO.class));
		
		return query.list();
		
	}
}
