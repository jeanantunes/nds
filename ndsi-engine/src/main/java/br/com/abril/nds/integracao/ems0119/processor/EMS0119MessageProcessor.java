package br.com.abril.nds.integracao.ems0119.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.integracao.service.PeriodicidadeProdutoService;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
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
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0119Input input = (EMS0119Input) message.getBody();

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p ");
		sql.append("FROM Produto p ");
		sql.append("WHERE p.codigo = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", input.getCodigoDaPublicacao());

		Produto produto = (Produto) query.uniqueResult();
		if (null != produto) {

			if (!produto.getNome()
					.equals(input.getNomeDaPublicacao())) {
				produto.setNome(input.getNomeDaPublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Nome da Publicacao para: "
								+ input.getNomeDaPublicacao());
			}
			if (!produto
					.getPeriodicidade()
					.toString()
					.equals(pps.getPeriodicidadeProdutoAsArchive(input
							.getPeriodicidade()))) {
				produto.setPeriodicidade(
						pps.getPeriodicidadeProdutoAsArchive(input
								.getPeriodicidade()));
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao da Periodicidade para: "
								+ pps.getPeriodicidadeProdutoAsArchive(input
										.getPeriodicidade()));
			}
			if (produto.getTipoProduto().getId() != input
					.getTipoDePublicacao()) {
				
				TipoProduto tp =  this.getTipoProduto(input.getTipoDePublicacao());
				
				if (null == tp) {
					ndsiLoggerFactory.getLogger().logWarning(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
							String.format( "Tipo de Produto %1$s não encontrado para o produto: %2$s ", input.getTipoDePublicacao().toString(), input.getCodigoDaPublicacao() )
						);
				} else {
					produto.setTipoProduto(tp);
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Tipo de Publicacao para: "
									+ input.getTipoDePublicacao());

				}								
			}
			if (produto.getEditor().getCodigo() != input
					.getCodigoDoEditor()) {
				
				Editor ed = this.getEditor(input.getCodigoDoEditor());			
				if (null == ed) {
					ndsiLoggerFactory.getLogger().logWarning(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
							String.format( "Editor %1$s não encontrado para o produto: %2$s ", input.getCodigoDoEditor().toString(), input.getCodigoDaPublicacao() )
						);
				} else {
					produto.setEditor(ed);
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualizacao do Codigo do Editor para: "
									+ input.getCodigoDoEditor());
					
				}				
			}
			if (produto.getPacotePadrao() != input.getPacotePadrao()) {
				produto.setPacotePadrao(input.getPacotePadrao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Pacote Padrao para: "
								+ input.getPacotePadrao());

			}

			if (produto.getDescricao() != input.getNomeComercial()) {
				produto.setDescricao(input.getNomeComercial());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Nome Comercial para: "
								+ input.getNomeComercial());
			}

			if (produto.isAtivo() != input.getStatusDaPublicacao()) {
				produto.setAtivo(input.getStatusDaPublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Status para: "
								+ input.getPacotePadrao());

			}

		} else {
			
			produto = new Produto();
			produto.setCodigo(input.getCodigoDaPublicacao());
			produto.setNome(input.getNomeDaPublicacao());
			produto.setPeriodicidade(pps.getPeriodicidadeProdutoAsArchive(input.getPeriodicidade()));
			produto.setPacotePadrao(input.getPacotePadrao());
			produto.setDescricao(input.getNomeComercial());
			produto.setAtivo(input.getStatusDaPublicacao());			
			//Default data
			produto.setPeso(0l);
			
			TipoProduto tp =  this.getTipoProduto(input.getTipoDePublicacao());
									
			if (null == tp) {
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						String.format( "Tipo de Produto %1$s não encontrado para o produto: %2$s ", input.getTipoDePublicacao().toString(), input.getCodigoDaPublicacao() )
					);
			} else {
				produto.setTipoProduto(tp);	
			}
			
			Editor ed = this.getEditor(input.getCodigoDoEditor());			
			if (null == ed) {
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						String.format( "Editor %1$s não encontrado para o produto: %2$s ", input.getCodigoDoEditor().toString(), input.getCodigoDaPublicacao() )
					);
			} else {
				produto.setEditor(ed);	
			}
			
			if ((null != tp) && (null != ed)) {
				this.getSession().persist(produto);
			}						
			
		}
	}

	private Editor getEditor(Long codigoDoEditor) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e ");
		sql.append("FROM Editor e ");
		sql.append("WHERE e.codigo = :codigoDoEditor ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoDoEditor", codigoDoEditor);

		return (Editor) query.uniqueResult();
	}

	private TipoProduto getTipoProduto(Long tipoDePublicacao) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tp ");
		sql.append("FROM TipoProduto tp ");
		sql.append("WHERE tp.codigo = :tipoDePublicacao ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("tipoDePublicacao", tipoDePublicacao);

		return (TipoProduto) query.uniqueResult();
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
