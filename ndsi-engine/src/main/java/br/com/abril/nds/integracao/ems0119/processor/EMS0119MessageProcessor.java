package br.com.abril.nds.integracao.ems0119.processor;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0119MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private PeriodicidadeProdutoService pps;

	public EMS0119MessageProcessor() {

	}

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0119Input input = (EMS0119Input) message.getBody();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM ProdutoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("WHERE p.codigo = :codigoProduto ");
		sql.append("AND pe.numeroEdicao = :numEdicao");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", input.getCodigoDaPublicacao());
		query.setParameter("numEdicao", input.getEdicao());

		ProdutoEdicao produtoEdicao = (ProdutoEdicao) query.uniqueResult();
		if (null != produtoEdicao) {

			if (!produtoEdicao.getProduto().getNome()
					.equals(input.getNomeDaPublicacao())) {
				produtoEdicao.getProduto().setNome(input.getNomeDaPublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Nome da Publicacao para: "
								+ input.getNomeDaPublicacao());
			}
			if (!produtoEdicao
					.getProduto()
					.getPeriodicidade()
					.toString()
					.equals(pps.getPeriodicidadeProdutoAsArchive(input
							.getPeriodicidade()))) {
				produtoEdicao.getProduto().setPeriodicidade(
						pps.getPeriodicidadeProdutoAsArchive(input
								.getPeriodicidade()));
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao da Periodicidade para: "
								+ pps.getPeriodicidadeProdutoAsArchive(input
										.getPeriodicidade()));
			}
			if (produtoEdicao.getProduto().getTipoProduto().getId() != input
					.getTipoDePublicacao()) {
				produtoEdicao.getProduto().getTipoProduto()
						.setId(input.getTipoDePublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Tipo de Publicacao para: "
								+ input.getTipoDePublicacao());
			}
			if (produtoEdicao.getProduto().getEditor().getCodigo() != input
					.getCodigoDoEditor()) {
				produtoEdicao.getProduto().getEditor()
						.setCodigo(input.getCodigoDoEditor());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Codigo do Editor para: "
								+ input.getCodigoDoEditor());
			}
			if (produtoEdicao.getPacotePadrao() != input.getPacotePadrao()) {
				produtoEdicao.setPacotePadrao(input.getPacotePadrao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Pacote Padrao para: "
								+ input.getPacotePadrao());

			}

			if (produtoEdicao.getNomeComercial() != input.getNomeComercial()) {
				produtoEdicao.setNomeComercial(input.getNomeComercial());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Nome Comercial para: "
								+ input.getNomeComercial());
			}

			if (produtoEdicao.isAtivo() != input.getStatusDaPublicacao()) {
				produtoEdicao.setAtivo(input.getStatusDaPublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Status para: "
								+ input.getPacotePadrao());

			}

		}
	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
