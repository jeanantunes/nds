package br.com.abril.nds.integracao.ems0106.processor;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0106.inbound.EMS0106Input;
import br.com.abril.nds.integracao.ems0107.inbound.EMS0107Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0106MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@SuppressWarnings("unchecked")
	@Override
	public void processMessage(Message message) {
		EMS0106Input input = (EMS0106Input) message.getBody();

		// Obter a cota
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM EMS0107Input e JOIN FETCH e.cota ");
		sql.append("WHERE ");
		sql.append("     e.codigoPublicacao = :codigoProduto ");
		sql.append("     AND e.edicao = :numeroEdicao ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("codigoProduto", input.getCodigoPublicacao());
		query.setParameter("numeroEdicao", input.getEdicao());

		List<EMS0107Input> ems0107Inputs = query.list();
		// SE NAO TIVER COTA, NAO GERA ESTUDO
		if (!ems0107Inputs.isEmpty()) {
			sql = new StringBuilder();
			sql.append("SELECT lcto FROM Lancamento lcto ");
			sql.append("			JOIN FETCH lcto.produtoEdicao pe ");
			sql.append("			JOIN FETCH pe.produto p ");
			sql.append("WHERE ");
			sql.append("	pe.numeroEdicao = :numeroEdicao ");
			sql.append("	AND p.codigo = :codigoProduto ");
			sql.append("	AND lcto.dataLancamentoPrevista >= current_date() ");
			sql.append("ORDER BY lcto.dataLancamentoPrevista DESC");

			query = getSession().createQuery(sql.toString());

			// LIMITA PARA PEGAR APENAS UMA LINHA
			query.setMaxResults(1);

			query.setParameter("numeroEdicao", input.getEdicao());
			query.setParameter("codigoProduto", input.getCodigoPublicacao());

			// INSERE Estudo (HEADER)
			Lancamento lancamento = (Lancamento) query.uniqueResult();

			sql = new StringBuilder();
			sql.append("SELECT e ");
			sql.append("FROM Estudo e ");
			sql.append("	JOIN FETCH e.produtoEdicao pe ");
			sql.append("WHERE ");
			sql.append("	pe.id = :produtoEdicaoId ");
			sql.append("	AND e.dataLancamento = :dataLancamento ");

			query = getSession().createQuery(sql.toString());

			query.setParameter("produtoEdicaoId", lancamento.getProdutoEdicao()
					.getId());
			query.setParameter("dataLancamento",
					lancamento.getDataLancamentoPrevista());

			List<Estudo> estudosSalvos = query.list();

			if (estudosSalvos.isEmpty()) {
				Estudo estudo = new Estudo();

				estudo.setProdutoEdicao(lancamento.getProdutoEdicao());
				estudo.setDataLancamento(lancamento.getDataLancamentoPrevista());
				estudo.setQtdeReparte(input.getReparteDistribuir());

				getSession().persist(estudo);

				// INSERE EstudoCota (DETAIL)
				for (EMS0107Input ems0107 : ems0107Inputs) {
					// Inserir EstudoCota
					EstudoCota estudoCota = new EstudoCota();
					estudoCota.setCota(ems0107.getCota());
					estudoCota.setEstudo(estudo);
					estudoCota.setQtdeEfetiva(ems0107.getQuantidadeReparte());
					estudoCota.setQtdePrevista(ems0107.getQuantidadeReparte());

					getSession().persist(estudoCota);
				}
			}
		} else {
			ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.HIERARQUIA,
					"NAO ENCONTROU ProdutoEdicao OU Lancamento");
		}
	}
}