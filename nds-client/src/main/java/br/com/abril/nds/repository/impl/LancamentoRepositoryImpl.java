package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ResumoPeriodoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO.ColunaOrdenacao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.LancamentoRepository;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class LancamentoRepositoryImpl extends
		AbstractRepository<Lancamento, Long> implements LancamentoRepository {

	public LancamentoRepositoryImpl() {
		super(Lancamento.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Lancamento> obterBalanceamentoMatrizLancamentos(FiltroLancamentoDTO filtro) {
		StringBuilder hql = new StringBuilder("select lancamento  from Lancamento lancamento ");
		hql.append("join fetch lancamento.produtoEdicao produtoEdicao ");
		hql.append("join fetch produtoEdicao.produto produto ");
		hql.append("join fetch produto.fornecedores fornecedor ");
		hql.append("left join fetch lancamento.recebimentos recebimento ");
		hql.append("left join fetch lancamento.estudos estudo ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		if (filtro.getData() != null) {
			hql.append("and lancamento.dataLancamentoPrevista = :data ");
		}
		if (filtro.filtraFornecedores()) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
		}
		PaginacaoVO paginacao = filtro.getPaginacao();
		ColunaOrdenacao colunaOrdenacao = filtro.getColunaOrdenacao();
		if (colunaOrdenacao != null) {
			if (ColunaOrdenacao.CODIGO_PRODUTO == colunaOrdenacao) {
				hql.append("order by produto.codigo ");
			} else if (ColunaOrdenacao.NOME_PRODUTO == colunaOrdenacao) {
				hql.append("order by produto.nome ");
			} else if (ColunaOrdenacao.NUMERO_EDICAO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.numeroEdicao ");
			} else if (ColunaOrdenacao.PRECO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.precoVenda ");
			} else if (ColunaOrdenacao.PACOTE_PADRAO == colunaOrdenacao) {
				hql.append("order by produtoEdicao.pacotePadrao ");
			} else if (ColunaOrdenacao.REPARTE == colunaOrdenacao) {
				hql.append("order by lancamento.reparte ");
			} else if (ColunaOrdenacao.FISICO == colunaOrdenacao) {
				hql.append("order by recebimento.qtdeFisico ");
			} else if (ColunaOrdenacao.ESTUDO_GERADO == colunaOrdenacao) {
					hql.append("order by estudo.qtdeReparte ");
			} else if (ColunaOrdenacao.LANCAMENTO == colunaOrdenacao) {
				hql.append("order by lancamento.tipoLancamento ");
			} else if (ColunaOrdenacao.RECOLHIMENTO == colunaOrdenacao) {
				hql.append("order by lancamento.dataRecolhimentoPrevista ");
			} else if (ColunaOrdenacao.FORNECEDOR == colunaOrdenacao) {
				hql.append("order by fornecedor.juridica.nomeFantasia ");
			} else if (ColunaOrdenacao.DATA_LANC_DISTRIB == colunaOrdenacao) {
				hql.append("order by lancamento.dataLancamentoDistribuidor ");
			} else if (ColunaOrdenacao.DATA_LANC_PREVISTO == colunaOrdenacao) {
				hql.append("order by lancamento.dataLancamentoPrevista ");
			} else if (ColunaOrdenacao.TOTAL == colunaOrdenacao) {
				hql.append("order by (lancamento.reparte * produtoEdicao.precoVenda) ");
			}
			String ordenacao = "asc";
			if (paginacao != null) {
				if (paginacao.getOrdenacao().equals(Ordenacao.DESC)) {
					ordenacao = "desc";
				}
			}
			hql.append(ordenacao);
		}

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", true);
		if (filtro.getData() != null) {
			query.setParameter("data", filtro.getData());
		}
		if (filtro.filtraFornecedores()) {
			query.setParameterList("idsFornecedores", filtro.getIdsFornecedores());
		}

		if (paginacao != null) {
			query.setFirstResult(paginacao.getPosicaoInicial());
			query.setMaxResults(paginacao.getQtdResultadosPorPagina());
		}
		return query.list();
	}

	@Override
	public long totalBalanceamentoMatrizLancamentos(Date data, List<Long> idsFornecedores) {
		StringBuilder hql = new StringBuilder("select count(lancamento) from Lancamento lancamento ");
		hql.append("join lancamento.produtoEdicao produtoEdicao ");
		hql.append("join produtoEdicao.produto produto ");
		hql.append("join produto.fornecedores fornecedor ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		if (data != null) {
			hql.append("and lancamento.dataLancamentoPrevista = :data ");
		}
		boolean filtraFornecedores = idsFornecedores != null && !idsFornecedores.isEmpty();
		if (filtraFornecedores) {
			hql.append("and fornecedor.id in (:idsFornecedores) ");
		}
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", true);
		if (data != null) {
			query.setParameter("data", data);
		}
		if (filtraFornecedores) {
			query.setParameterList("idsFornecedores", idsFornecedores);
		}
		return (Long) query.uniqueResult();
	}

	@Override
	public void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista) {
		StringBuilder hql = new StringBuilder("update Lancamento set ");
		hql.append(" dataLancamentoPrevista = :novaDataLancamentoPrevista, ")
		   .append(" reparte = 0 ")
		   .append(" where id = :id");
		
		Query query = 
				this.getSession().createQuery(hql.toString());
		
		query.setParameter("novaDataLancamentoPrevista", novaDataLancamentoPrevista);
		query.setParameter("id", idLancamento);
		
		query.executeUpdate();
	}

	@Override
	public List<ResumoPeriodoLancamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores) {
		//TODO: implementar
		return null;
	}


}
