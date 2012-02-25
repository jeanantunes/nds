package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.StatusNotaFiscal;
import br.com.abril.nds.repository.NotaFiscalDAO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.filtro.FiltroConsultaNotaFiscalVO;

@Repository
public class NotaFiscalDAOImpl extends AbstractRepository<NotaFiscal, Long> implements
		NotaFiscalDAO {

	public NotaFiscalDAOImpl() {
		super(NotaFiscal.class);
	}
	
	public Integer obterQuantidadeNotasFicaisCadastradas(FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(notaFiscal) from NotaFiscal notaFiscal, Fornecedor fornecedor ")
		   .append(" join notaFiscal.tipoNotaFiscal ")
		   .append(" join notaFiscal.juridica ")
		   .append(" where notaFiscal.dataEmissao between :dataInicio and :dataFim ")
		   .append(" and notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ")
		   .append(" and fornecedor.id = :idFornecedor")
		   .append(" and fornecedor.juridica.id = notaFiscal.juridica.id ");
	
		if (filtroConsultaNotaFiscal.isNotaRecebida() != null) {
			
			hql.append(" and notaFiscal.statusNotaFiscal = :statusNotaFiscal ");
		}
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql.toString(), filtroConsultaNotaFiscal);
		
		return ((Long) query.uniqueResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NotaFiscal> obterNotasFiscaisCadastradas(FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select notaFiscal from NotaFiscal notaFiscal, Fornecedor fornecedor ")
		   .append(" join notaFiscal.tipoNotaFiscal ")
		   .append(" join notaFiscal.juridica ")
		   .append(" where notaFiscal.dataEmissao between :dataInicio and :dataFim ")
		   .append(" and notaFiscal.tipoNotaFiscal.id = :idTipoNotaFiscal ")
		   .append(" and fornecedor.id = :idFornecedor ")
		   .append(" and fornecedor.juridica.id = notaFiscal.juridica.id ");

		if (filtroConsultaNotaFiscal.isNotaRecebida() != null) {
			
			hql.append(" and notaFiscal.statusNotaFiscal = :statusNotaFiscal ");
		}
		
		PaginacaoVO paginacao = filtroConsultaNotaFiscal.getPaginacao();

		if (filtroConsultaNotaFiscal.getColunaOrdenacao() != null) {
				
			hql.append(" order by ");
			
			switch (filtroConsultaNotaFiscal.getColunaOrdenacao()) {
	
				case DATA_EMISSAO:
					hql.append(" notaFiscal.dataEmissao ");
					break;
				case DATA_EXPEDICAO:
					hql.append(" notaFiscal.dataExpedicao ");
					break;
				case FORNECEDOR:
					hql.append(" notaFiscal.juridica.razaoSocial ");
					break;
				case NOTA_RECEBIDA:
					hql.append(" notaFiscal.statusNotaFiscal ");
					break;
				case NUMERO_NOTA:
					hql.append(" notaFiscal.numero ");
					break;
				case TIPO_NOTA:
					hql.append(" notaFiscal.tipoNotaFiscal.descricao ");
					break;
				default:
					break;
			}
			
			if (paginacao.getOrdenacao() != null) {
				
				hql.append(paginacao.getOrdenacao().toString());
			}
		}
		
		Query query = criarQueryComParametrosObterNotasFiscaisCadastradas(hql.toString(), filtroConsultaNotaFiscal);
		
		query.setFirstResult(paginacao.getPosicaoInicial());
		query.setMaxResults(paginacao.getQtdResultadosPorPagina());

		return query.list();
	}

	private Query criarQueryComParametrosObterNotasFiscaisCadastradas(String hql, FiltroConsultaNotaFiscalVO filtroConsultaNotaFiscal) {

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataInicio", filtroConsultaNotaFiscal.getPeriodo().getDataInicial());
		query.setParameter("dataFim", filtroConsultaNotaFiscal.getPeriodo().getDataFinal());
		query.setParameter("idTipoNotaFiscal", filtroConsultaNotaFiscal.getIdTipoNotaFiscal());
		query.setParameter("idFornecedor", filtroConsultaNotaFiscal.getIdFornecedor());
		
		if (filtroConsultaNotaFiscal.isNotaRecebida() != null) {

			StatusNotaFiscal statusNotaFiscal = 
					filtroConsultaNotaFiscal.isNotaRecebida() ? 
							StatusNotaFiscal.RECEBIDA : StatusNotaFiscal.NAO_RECEBIDA; 

			query.setParameter("statusNotaFiscal", statusNotaFiscal);
		}

		return query;
	}
}

