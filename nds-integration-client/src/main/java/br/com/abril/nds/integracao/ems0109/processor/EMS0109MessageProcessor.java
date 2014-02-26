package br.com.abril.nds.integracao.ems0109.processor;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0109Input;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.EmailService;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

@Component
public class EMS0109MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private EmailService emailService;

	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void processMessage(Message message) {

		Editor editor = this.findEditorByID(message);

		TipoProduto tipoProduto = this.findTipoProduto(message);

		Produto produto = this.findProduto(message);

		if (produto == null) {

			this.criarProdutoConformeInput(message, editor, tipoProduto);

		} else {

			this.atualizaProdutoConformeInput(produto, editor, tipoProduto,
					message);
		}
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

            this.ndsiLoggerFactory.getLogger()
                    .logWarning(
                            message,
					EventoExecucaoEnum.SEM_DOMINIO,
                            "Editor " + input.getCodigoEditor() + " nao encontrado, publicação: "
                                    + input.getCodigoPublicacao());

//			throw new RuntimeException("Editor " + input.getCodigoEditor() + " nao encontrado.");
		}
		
		return editor;
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
		List<TipoProduto> tiposProduto = query.list();

		TipoProduto tipoProduto = null;

		if (!tiposProduto.isEmpty()) {

			tipoProduto = tiposProduto.get(0);

			return tipoProduto;

		} else {

            this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Produto REVISTA nao encontrado.");


//			throw new RuntimeException("Tipo Produto nao encontrado.");
		}
		return tipoProduto;
	}

	private Fornecedor findFornecedor(Integer codigoInterface) {
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT f FROM Fornecedor f ");
		sql.append("WHERE  f.codigoInterface = :codigoInterface ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoInterface", codigoInterface);

		return (Fornecedor) query.uniqueResult();

	}
	
    private TipoSegmentoProduto findTipoSegmentoProdutoPorNome(String nome) {
	    Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
	    criteria.add(Restrictions.like("descricao", nome));
	    return (TipoSegmentoProduto) criteria.uniqueResult();
	}
	
	private TipoSegmentoProduto criarNovoSegmento(String nome) {
	    TipoSegmentoProduto tipoSegmentoProduto = new TipoSegmentoProduto();
	    tipoSegmentoProduto.setDescricao(nome);
	    return (TipoSegmentoProduto) this.getSession().merge(tipoSegmentoProduto);
	}
	
	private TipoSegmentoProduto getTipoSegmento(String nome) {
	    TipoSegmentoProduto tipoSegmentoProduto = null;
	   
	    if(!Strings.isNullOrEmpty(nome)) {
	    
	        tipoSegmentoProduto = findTipoSegmentoProdutoPorNome(nome);
            
            if(tipoSegmentoProduto == null) {
                tipoSegmentoProduto = criarNovoSegmento(nome);
            }
        }
	    return tipoSegmentoProduto;
	}
	
	private void criarProdutoConformeInput(Message message, Editor editor,
			TipoProduto tipoProduto) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		Produto produto = new Produto();


        String codigoDistribuidor = 
                message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
        
        Fornecedor fornecedor = this
                .findFornecedor(Integer.parseInt(codigoDistribuidor));
				
		validarTipoDesconto(message, input.getTipoDesconto(), input.getCodigoPublicacao());
		
		int tipoDescontoInt = Integer.parseInt( input.getTipoDesconto());
		DescontoLogistica descontoLogistica = this.findDescontoLogisticaByTipoDesconto( tipoDescontoInt );

		produto.setTipoProduto(tipoProduto);
		produto.setNome(input.getNomePublicacao());
		produto.setCodigoContexto(input.getContextoPublicacao());
		produto.setNomeComercial(input.getNomePublicacao());
		produto.setEditor(editor);
		if (null != PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())) {
			produto.setPeriodicidade(PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
		} else {
            ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.SEM_DOMINIO,
					String.format( "Produto %1$s com periodicidade nao conhecida %2$s", input.getCodigoPublicacao(), input.getPeriodicidade() )
				);
			return;
		}
		
		if(!Strings.isNullOrEmpty(input.getCodigoICD())) {
		    produto.setCodigoICD(input.getCodigoICD());
		}
		
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
		
		
		
		produto.setTipoSegmentoProduto(getTipoSegmento(input.getSegmento()));
		
		String codigoSituacaoTributaria = input.getCodigoSituacaoTributaria();
		produto.setTributacaoFiscal(this.getTributacaoFiscal(codigoSituacaoTributaria));

		produto.setOrigem(Origem.INTERFACE);
		
		if (fornecedor != null) {

			produto.addFornecedor(fornecedor);
		} else {
            ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Fornecedor nulo para o produto: %1$s .", input.getCodigoPublicacao() )
				);
			return ;
		}

		if (descontoLogistica != null) {

			produto.setDescontoLogistica(descontoLogistica);

		}else{
			validarDescontoLogistico(message, input.getCodigoPublicacao(), tipoDescontoInt);
		}

		this.getSession().persist(produto);

	}

	private void validarDescontoLogistico(Message message, String codigoPublicacao, int tipoDescontoInt) {
        String assunto = "Erro na Interface 109 PUB - TipoDesconto não cadastrado na DescontoLogistico";
        String msg = "TipoDesconto: " + tipoDescontoInt
            + " não cadastrado na tabela DescontoLogistico arquivo .PUB vindo PRODIN, código de publicação:  "
                + codigoPublicacao;
		sendEmailInterface(assunto, msg, message);
        this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, msg);
	}

	public void validarTipoDesconto(Message message, String tipoDesconto, String codigoPublicacao){
		if(tipoDesconto == null || tipoDesconto == "00" || tipoDesconto == "" || tipoDesconto == "0"){
            String assunto = "Erro na Interface 109 PUB - TipoDesconto não consta no arquivo";
            String msg = "TipoDesconto não consta no arquivo .PUB de publicações vindo PRODIN, código de publicação:  "
                    + codigoPublicacao;
			sendEmailInterface(assunto, msg, message);
            this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.HIERARQUIA, msg);
		}
	}
	
	private void sendEmailInterface(String assunto, String mensagem, Message message){
		//Descomentar p/ enviar e-mail quando resolver questoes de rede e conta de e-mail
//		try {
//			emailService.enviar(assunto, mensagem, Constantes.MAILS_RECEBIMENTO_INTERFACE);
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage(), e);
        // ndsiLoggerFactory.getLogger().logWarning(message,
        // EventoExecucaoEnum.HIERARQUIA,
        // String.format("Erro ao tentar enviar e-mail Interface"));
//		}
	}
	
	
	private void atualizaProdutoConformeInput(Produto produto, Editor editor,
			TipoProduto tipoProduto, Message message) {

		EMS0109Input input = (EMS0109Input) message.getBody();

		String codigoDistribuidor = 
	            message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
		
		Fornecedor fornecedor = this
				.findFornecedor(Integer.parseInt(codigoDistribuidor));
		
		validarTipoDesconto(message, input.getTipoDesconto(), input.getCodigoPublicacao());
		
		
		int tipoDescontoInt = Integer.parseInt( input.getTipoDesconto());
		DescontoLogistica descontoLogistica = this.findDescontoLogisticaByTipoDesconto(tipoDescontoInt);
		
		produto.setOrigem(Origem.INTERFACE);
		
		if (!produto.getTipoProduto().equals( tipoProduto )) {

			produto.setTipoProduto(tipoProduto);
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Tipo de Publicacao para: "
							+ tipoProduto.getDescricao());
		}
		if (!Objects.equal(produto.getNome(), input.getNomePublicacao())) {

			produto.setNome(input.getNomePublicacao());
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Nome da Publicacao para: "
							+ input.getNomePublicacao());
		}
		if (!Objects.equal(produto.getCodigoContexto(), input.getContextoPublicacao())) {

			produto.setCodigoContexto(input.getContextoPublicacao());
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Contexto Publicacao para: "
							+ input.getContextoPublicacao());
		}
		if (!Objects.equal(produto.getNomeComercial(), input.getNomePublicacao())) {

			produto.setNomeComercial(input.getNomePublicacao());
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Descricao para: "
							+ input.getNomePublicacao());
		}
		if (!Objects.equal(input.getCodigoEditor(), produto.getEditor().getCodigo())) {

			produto.setEditor(editor);
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Editor para: " + editor.getPessoaJuridica().getNome());
		}
		if (!Objects.equal(produto.getPeriodicidade(), PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())) ) {

			produto.setPeriodicidade(PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Periodicidade para: "
							+ PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
		}
		if (!Objects.equal(produto.getSlogan(), input.getSlogan())) {

			produto.setSlogan(input.getSlogan());
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Slogan para: " + input.getSlogan());
		}		
		if (!Objects.equal(produto.getPeso(),input.getPeso())) {

			produto.setPeso(input.getPeso());
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Peso para: " + input.getPeso());
		}
		if (!Objects.equal(produto.getCodigo(), input.getCodigoPublicacao())) {

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
		if (!Objects.equal(produto.getDataDesativacao(), input.getDataDesativacao())) {

			produto.setDataDesativacao(input.getDataDesativacao());
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao da Data de Desativacao para: "
							+ input.getDataDesativacao());
		}
		if (!Objects.equal(Integer.valueOf(produto.getPeb()), input.getPeb())) {

			produto.setPeb(input.getPeb());
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do PEB para: " + input.getPeb());
		}
		if (!Objects.equal(Integer.valueOf(produto.getPacotePadrao()), input.getPacotePadrao())) {

			produto.setPacotePadrao(input.getPacotePadrao());
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Pacote Padrao para: "
							+ input.getPacotePadrao());
		}
		if (!Objects.equal(produto.getPeso(), input.getPeso())) {

			produto.setPeso(input.getPeso());
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Peso para: " + input.getPeso());
		}
		
		if (!Strings.isNullOrEmpty(input.getCodigoICD()) 
		        && !Objects.equal(produto.getCodigoICD(), input.getCodigoICD())) {
		    produto.setCodigoICD(input.getCodigoICD());
		    this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Atualizacao do CodigoICD para: " + input.getCodigoICD());
		}
		
		TipoSegmentoProduto tipoSegmentoProduto = produto.getTipoSegmentoProduto();
		
		if ((tipoSegmentoProduto == null && input.getSegmento() != null) || 
		        (input.getSegmento() != null && !Objects.equal(
		                tipoSegmentoProduto.getDescricao(), input.getSegmento()))) {
		    
            produto.setTipoSegmentoProduto(getTipoSegmento(input.getSegmento()));
            this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Atualizacao do Tipo de Segmento do Produto para: " + input.getSegmento());
        }
		
		Fornecedor produtoFornecedor = produto.getFornecedor();
		if ((produtoFornecedor == null) || 
		        !Objects.equal(produtoFornecedor.getCodigoInterface(), fornecedor.getCodigoInterface()))  {

			    produto.setFornecedores(new HashSet<Fornecedor>());
			    
				produto.addFornecedor(fornecedor);

                this.ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao de Fornecedor para Fornecedor: "
								+ fornecedor.getResponsavel());
		}
		

		if (descontoLogistica == null) {
			validarDescontoLogistico(message, input.getCodigoPublicacao(), tipoDescontoInt);
        } else {

			if (produto.getDescontoLogistica() == null || !produto.getDescontoLogistica().equals(descontoLogistica)) {

				produto.setDescontoLogistica(descontoLogistica);

                this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO,
                        "Atualizacao do Tipo Desconto para: " + descontoLogistica.getTipoDesconto());
				
			}
		}

		TributacaoFiscal tributacaoFiscal = getTributacaoFiscal(input.getCodigoSituacaoTributaria());
		
		if (produto.getTributacaoFiscal() != tributacaoFiscal) {
			produto.setTributacaoFiscal(tributacaoFiscal);
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Atualizacao da Tributação Fiscal para: " + tributacaoFiscal.getValue());
		}
		FormaComercializacao formaComercializacaoInput = input.getFormaComercializacao().equals("CON") ? 
		        FormaComercializacao.CONSIGNADO : FormaComercializacao.CONTA_FIRME;

		if ( produto.getFormaComercializacao() != formaComercializacaoInput ) {

		    produto.setFormaComercializacao(formaComercializacaoInput);
				
            this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao da Forma de Comercializacao para: " + formaComercializacaoInput.getValue());
		}
		
		this.getSession().update(produto);
	}

	                        /**
     * Retorna o enum TributacaoFiscal (codigoSituacaoTributaria) baseado na
     * posição 220 retornada na EMS0109Input.java
     * 
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
