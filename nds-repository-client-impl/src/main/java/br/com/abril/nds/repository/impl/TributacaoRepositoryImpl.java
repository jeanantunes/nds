package br.com.abril.nds.repository.impl;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.le;
import static org.hibernate.criterion.Restrictions.or;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.CST;
import br.com.abril.nds.model.fiscal.nota.NotaFiscalTributacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TributacaoRepository;

/**
 * 
 * @author Diego Fernandes
 * 
 */
@Repository
public class TributacaoRepositoryImpl extends
		AbstractRepositoryModel<NotaFiscalTributacao, Long> implements
		TributacaoRepository {

	public TributacaoRepositoryImpl() {
		super(NotaFiscalTributacao.class);
	}

	@Override
	public NotaFiscalTributacao buscar(String codigoEmpresa, TipoOperacao tipoOperacao,
			String ufOrigem, String ufDestino, int naturezaOperacao,
			String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia,
			String cstICMS) {

		Criteria criteria = getSession().createCriteria(NotaFiscalTributacao.class);

		criteria.add(eq("codigoEmpresa", codigoEmpresa))
				.add(eq("tipoOperacao", tipoOperacao.getSimpleValue()))				
				.add(eq("naturezaOperacao", naturezaOperacao))
				.add(eq("codigoNaturezaOperacao", codigoNaturezaOperacao))
				.add(eq("codigoNBM", codigoNBM))
				.add(le("dataVigencia", dataVigencia))
				.add(eq("cstICMS", cstICMS))
				.add(eq("ufDestino", ufDestino))
				.add(eq("ufOrigem", ufOrigem));

		criteria.setMaxResults(1);

		criteria.addOrder(Order.desc("dataVigencia"));
		return (NotaFiscalTributacao) criteria.uniqueResult();

	}
	
	
	
	@Override
	public NotaFiscalTributacao buscar(String codigoEmpresa, TipoOperacao tipoOperacao,
			List<String> ufs, int naturezaOperacao,
			String codigoNaturezaOperacao, String codigoNBM, Date dataVigencia,
			String cstICMS) {

		Criteria criteria = getSession().createCriteria(NotaFiscalTributacao.class);

		criteria.add(eq("codigoEmpresa", codigoEmpresa))
				.add(eq("tipoOperacao", tipoOperacao.getSimpleValue()))				
				.add(eq("naturezaOperacao", naturezaOperacao))
				.add(eq("codigoNaturezaOperacao", codigoNaturezaOperacao))
				.add(eq("codigoNBM", codigoNBM))
				.add(le("dataVigencia", dataVigencia))
				.add(eq("cstICMS", cstICMS))
				.add(or(in("ufDestino", ufs),in("ufOrigem", ufs)));
		

		criteria.setMaxResults(1);

		criteria.addOrder(Order.desc("dataVigencia"));
		return (NotaFiscalTributacao) criteria.uniqueResult();

	}

	@Override
	public NotaFiscalTributacao tributacaoDefault(String codigoEmpresa,
			TipoOperacao tipoOperacao, String ufOrigem, String ufDestino,
			int naturezaOperacao, String codigoNaturezaOperacao,
			String codigoNBM, Date dataVigencia) {

		NotaFiscalTributacao tributacao = new NotaFiscalTributacao();
		tributacao.setCodigoEmpresa(codigoEmpresa);
		tributacao.setTipoOperacao(tipoOperacao.getSimpleValue());
		tributacao.setUfDestino(ufDestino);
		tributacao.setUfOrigem(ufOrigem);
		tributacao.setNaturezaOperacao(naturezaOperacao);
		tributacao.setCodigoNaturezaOperacao(codigoNaturezaOperacao);

		tributacao.setCstICMS(CST.ICMS_NAO_TRIBUTADA);
		tributacao.setTributacaoICMS("2");
		tributacao.setAliquotaICMS(BigDecimal.ZERO);
		tributacao.setTipoBaseCalculoICMS("3");

		tributacao.setTributacaoIPI("3");
		tributacao.setCstIPI(CST.IPI_OUTRAS_SAIDAS);
		tributacao.setAliquotaIPI(BigDecimal.ZERO);

		tributacao.setTributacaoPIS("3");
		tributacao
				.setCstPIS(tipoOperacao == TipoOperacao.SAIDA ? CST.PIS_OUTRAS_OPERACOES_SAIDA
						: CST.PIS_OUTRAS_OPERACOES_ENTRADA);
		tributacao.setAliquotaPIS(BigDecimal.ZERO);

		tributacao.setTributacaoCOFINS("3");
		tributacao
				.setCstCOFINS(tipoOperacao == TipoOperacao.SAIDA ? CST.COFINS_OUTRAS_OPERACOES_SAIDA
						: CST.COFINS_OUTRAS_OPERACOES_ENTRADA);
		tributacao.setAliquotaCOFINS(BigDecimal.ZERO);

		return tributacao;
	}

}
