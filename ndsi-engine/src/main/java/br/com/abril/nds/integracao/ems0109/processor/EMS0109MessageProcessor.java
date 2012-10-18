package br.com.abril.nds.integracao.ems0109.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0109MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {

		// Distribuidor unico para todo sistema
		if (verificarDistribuidor(message)) {

			Editor editor = this.findEditorByID(message);

			TipoProduto tipoProduto = this.findTipoProduto(message);

			Produto produto = this.findProduto(message);

			if (produto == null) {

				this.criarProdutoConformeInput(message, editor, tipoProduto);

			} else {

				this.atualizaProdutoConformeInput(produto, editor, tipoProduto,
						message);
			}

		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor nao encontrato.");

			throw new RuntimeException("Distribuidor incorreto.");
		}
	}

	private boolean verificarDistribuidor(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		Long codigoDistribuidorSistema = (Long) message.getHeader().get(
				MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue());
		Long codigoDistribuidorArquivo = Long.parseLong(input
				.getCodigoDistribuidor());

		if (codigoDistribuidorSistema != null
				&& codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {

			return true;
		}

		return false;
	}

	private Produto findProduto(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("select p from Produto p ");
		sql.append(" where p.codigo = :codigoPublicacao ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoPublicacao", input.getCodigoPublicacao());

		return (Produto) query.uniqueResult();

	}	

	private Editor findEditorByID(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("select editor from Editor editor ");
		sql.append(" where editor.codigo = :codigoEditor ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoEditor", input.getCodigoEditor());

		Editor editor = (Editor) query.uniqueResult();
		if (null != editor) {
			return editor;
		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Editor " + input.getCodigoEditor() + " nao encontrado.");

			throw new RuntimeException("Editor " + input.getCodigoEditor() + " nao encontrado.");
		}
	}

	private DescontoLogistica findDescontoLogisticaByTipoDesconto(
			Integer codigoTipoDesconto) {
		StringBuilder sql = new StringBuilder();

		sql.append("select d from DescontoLogistica d ");
		sql.append(" where d.tipoDesconto = :codigoTipoDesconto ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoTipoDesconto", codigoTipoDesconto);

		return (DescontoLogistica) query.uniqueResult();

	}

	private TipoProduto findTipoProduto(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigo", input.getCategoria());

		@SuppressWarnings("unchecked")
		List<TipoProduto> tiposProduto = (List<TipoProduto>) query.list();

		TipoProduto tipoProduto = null;

		if (!tiposProduto.isEmpty()) {

			tipoProduto = tiposProduto.get(0);

			return tipoProduto;

		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Produto REVISTA nao encontrado.");

			throw new RuntimeException("Tipo Produto nao encontrado.");
		}
	}

	private Fornecedor findFornecedor(Integer codigoInterface) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT f FROM Fornecedor f ");
		sql.append("WHERE  f.codigoInterface = :codigoInterface ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoInterface", codigoInterface);

		return (Fornecedor) query.uniqueResult();

	}

	private void criarProdutoConformeInput(Message message, Editor editor,
			TipoProduto tipoProduto) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		Produto produto = new Produto();

		Fornecedor fornecedor = this
				.findFornecedor(input.getCodigoFornecedor());
		DescontoLogistica descontoLogistica = this
				.findDescontoLogisticaByTipoDesconto( Integer.parseInt( input.getTipoDesconto()) );

		produto.setTipoProduto(tipoProduto);
		produto.setNome(input.getNomePublicacao());
		produto.setCodigoContexto(input.getContextoPublicacao());
		produto.setDescricao(input.getNomePublicacao());
		produto.setEditor(editor);
		produto.setPeriodicidade(PeriodicidadeProduto.values()[input.getPeriodicidade()]);
		produto.setSlogan(input.getSlogan());
		produto.setPeb(input.getPeb());
		produto.setPeso(input.getPeso());
		produto.setPacotePadrao(input.getPacotePadrao());
		produto.setCodigo(input.getCodigoPublicacao());
		produto.setAtivo(input.isStatus());
		produto.setDataDesativacao(input.getDataDesativacao());
		produto.setFormaComercializacao(
				(input.getFormaComercializacao().equals("CON") 
						? FormaComercializacao.CONSIGNADO 
						: FormaComercializacao.CONTA_FIRME
				) 
		);

		String codigoSituacaoTributaria = input.getCodigoSituacaoTributaria();
		produto.setTributacaoFiscal(this.getTributacaoFiscal(codigoSituacaoTributaria));

		if (fornecedor != null) {

			produto.addFornecedor(fornecedor);
		}

		if (descontoLogistica != null) {

			produto.setDescontoLogistica(descontoLogistica);

		}

		this.getSession().persist(produto);

	}

	private void atualizaProdutoConformeInput(Produto produto, Editor editor,
			TipoProduto tipoProduto, Message message) {

		EMS0109Input input = (EMS0109Input) message.getBody();

		
		Fornecedor fornecedor = this
				.findFornecedor(input.getCodigoFornecedor());
		DescontoLogistica descontoLogistica = this
				.findDescontoLogisticaByTipoDesconto(Integer.parseInt( input.getTipoDesconto()) );

		if (produto.getTipoProduto() != tipoProduto) {

			produto.setTipoProduto(tipoProduto);
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Tipo de Publicacao para: "
							+ tipoProduto.getDescricao());
		}
		if (!produto.getNome().equals(input.getNomePublicacao())) {

			produto.setNome(input.getNomePublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Nome da Publicacao para: "
							+ input.getNomePublicacao());
		}
		if (produto.getCodigoContexto() != input.getContextoPublicacao()) {

			produto.setCodigoContexto(input.getContextoPublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Contexto Publicacao para: "
							+ input.getContextoPublicacao());
		}
		if (!produto.getDescricao().equals(input.getNomePublicacao())) {

			produto.setDescricao(input.getNomePublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Descricao para: "
							+ input.getNomePublicacao());
		}
		if (!produto.getEditor().getCodigo().equals(input.getCodigoEditor())) {

			produto.setEditor(editor);
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Editor para: " + editor.getPessoaJuridica().getNome());
		}
		if (produto.getPeriodicidade() != PeriodicidadeProduto.values()[input.getPeriodicidade()]) {

			produto.setPeriodicidade(PeriodicidadeProduto.values()[input.getPeriodicidade()]);
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Periodicidade para: "
							+ PeriodicidadeProduto.values()[input.getPeriodicidade()]);
		}
		if (!produto.getSlogan().equals(input.getSlogan())) {

			produto.setSlogan(input.getSlogan());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Slogan para: " + input.getSlogan());
		}
		if (produto.getPeb() != input.getPeb()) {

			produto.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if (produto.getPeso().equals(input.getPeso())) {

			produto.setPeso(input.getPeso());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Peso para: " + input.getPeso());
		}
		if (!produto.getCodigo().equals(input.getCodigoPublicacao())) {

			produto.setCodigo(input.getCodigoPublicacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Codigo da Publicacao para: "
							+ input.getCodigoPublicacao());
		}
		if (produto.isAtivo() != input.isStatus()) {

			produto.setAtivo(input.isStatus());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Status para: " + input.isStatus());
		}
		if (!produto.getDataDesativacao().equals(input.getDataDesativacao())) {

			produto.setDataDesativacao(input.getDataDesativacao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Data de Desativacao para: "
							+ input.getDataDesativacao());
		}
		if (produto.getPeb() != input.getPeb()) {

			produto.setPeb(input.getPeb());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if (produto.getPacotePadrao() != input.getPacotePadrao()) {

			produto.setPacotePadrao(input.getPacotePadrao());
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Pacote Padrao para: "
							+ input.getPacotePadrao());
		}
		if (!produto.getPeso().equals(input.getPeso())) {

			produto.setPeso(input.getPeso());
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Peso para: " + input.getPeso());
		}

		if (fornecedor != null) {

			if (produto.getFornecedor().getCodigoInterface() != fornecedor
					.getCodigoInterface()) {

				produto.addFornecedor(fornecedor);

				this.ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao de Fornecedor para Fornecedor: "
								+ fornecedor.getResponsavel());
			}
		}

		if (descontoLogistica != null) {

			if (!produto.getDescontoLogistica().equals(descontoLogistica)) {

				produto.setDescontoLogistica(descontoLogistica);

				this.ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Tipo Desconto para: "
								+ descontoLogistica.getTipoDesconto());

			}
		}

		TributacaoFiscal tributacaoFiscal = getTributacaoFiscal(input.getCodigoSituacaoTributaria());
		if (produto.getTributacaoFiscal() != tributacaoFiscal) {
			produto.setTributacaoFiscal(tributacaoFiscal);
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Tributação Fiscal para: " + tributacaoFiscal);
		}

	}

	/**
	 * Retorna o enum TributacaoFiscal (codigoSituacaoTributaria) baseado na posição 220 retornada na EMS0109Input.java
	 * @return
	 */
	private TributacaoFiscal getTributacaoFiscal(String codigoSituacaoTributaria) {
		if ("A".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.TRIBUTADO;
		} else if ("B".equalsIgnoreCase(codigoSituacaoTributaria)) {
			return TributacaoFiscal.ISENTO;
		} else {
			return TributacaoFiscal.OUTROS;
		}
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
