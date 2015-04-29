package br.com.abril.nds.integracao.ems0109.processor;

import java.util.HashSet;
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
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.util.DateUtil;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

@Component
public class EMS0109MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
    private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private EmailService emailService;

	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
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

			this.atualizaProdutoConformeInput(produto, editor, tipoProduto, message);
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

		sql.append(" select editor from Editor editor ");
		sql.append(" where editor.codigo = :codigoEditor ");
		sql.append(" and editor.ativo = true ");

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

	private TipoProduto findTipoProduto(Message message) {
		EMS0109Input input = (EMS0109Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigo", input.getCategoria());

		TipoProduto tipoProduto =(TipoProduto) query.uniqueResult();

		if (tipoProduto==null) {

			sql = new StringBuilder();

			sql.append("SELECT tp FROM TipoProduto tp ");
			sql.append("WHERE  tp.descricao = :descricao ");

		    query = this.getSession().createQuery(sql.toString());
			query.setParameter("descricao","Outros" );

			tipoProduto =(TipoProduto) query.uniqueResult();

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
	
    private TipoSegmentoProduto findTipoSegmentoProdutoPorId(Long id) {
	    Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
	    criteria.add(Restrictions.idEq(id));
	    //FIXME remover assim que as arquivos pub vierem com a descricao do segmento completa
	    criteria.setMaxResults(1);
	    
	    return (TipoSegmentoProduto) criteria.uniqueResult();
	}
    
    //Deprecated
    
    private TipoSegmentoProduto findTipoSegmentoProdutoPorNome(String nome) {
	    Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
	    criteria.add(Restrictions.like("descricao", "%"+nome+"%"));
	    //FIXME remover assim que as arquivos pub vierem com a descricao do segmento completa
	    criteria.setMaxResults(1);
	    
	    return (TipoSegmentoProduto) criteria.uniqueResult();
	}
	
	private TipoSegmentoProduto criarNovoSegmento(String nome) {
	    TipoSegmentoProduto tipoSegmentoProduto = new TipoSegmentoProduto();
	    tipoSegmentoProduto.setDescricao(nome);
	    return (TipoSegmentoProduto) this.getSession().merge(tipoSegmentoProduto);
	}
	
	//Deprecated
	private TipoSegmentoProduto getTipoSegmento(Long id) {
	    TipoSegmentoProduto tipoSegmentoProduto = null;
	   
	    tipoSegmentoProduto = findTipoSegmentoProdutoPorId(id);
        
	    return tipoSegmentoProduto;
	}
	
	//Deprecated
	
	private TipoSegmentoProduto getTipoSegmento(String nome) {
	    TipoSegmentoProduto tipoSegmentoProduto = null;
	   
	    if(!Strings.isNullOrEmpty(nome)) {
	    
	        tipoSegmentoProduto = findTipoSegmentoProdutoPorNome(nome);
            
            if(tipoSegmentoProduto == null) {
                //tipoSegmentoProduto = criarNovoSegmento(nome);
            	tipoSegmentoProduto = findTipoSegmentoProdutoPorNome("OUTROS");
            }
        }
	    return tipoSegmentoProduto;
	}
	
	private void criarProdutoConformeInput(Message message, Editor editor, TipoProduto tipoProduto) {
		
		EMS0109Input input = (EMS0109Input) message.getBody();

		Produto produto = new Produto();


        String codigoDistribuidor =  message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
        
        Fornecedor fornecedor = this.findFornecedor(Integer.parseInt(codigoDistribuidor));

        if (fornecedor == null) {
        
            ndsiLoggerFactory.getLogger().logError(
                    message,
                    EventoExecucaoEnum.HIERARQUIA,
                    String.format( "Fornecedor nulo. Produto %1$s .", input.getCodigoPublicacao() )
                );
            return ;
        }
        
		validarTipoDesconto(message, input.getTipoDesconto(), input.getCodigoPublicacao());
		
		int tipoDescontoInt = Integer.parseInt( input.getTipoDesconto());

		DescontoLogistica descontoLogistica = this.descontoLogisticaService.obterDescontoLogisticaVigente(tipoDescontoInt,
		                                                                    fornecedor.getId(),
		                                                                    input.getDataGeracaoArquivo());

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
					String.format( "Periodicidade %2$s não conhecida. Produto %1$s", input.getCodigoPublicacao(), input.getPeriodicidade() )
				);
			return;
		}
		
		if(!Strings.isNullOrEmpty(input.getCodigoICD())) {
			
		    produto.setCodigoICD(new Integer(input.getCodigoICD()).toString());
		    
		} else {
			
			if(input.getCodigoPublicacao().substring(0,1).equals("0")) {
				produto.setCodigoICD(input.getCodigoPublicacao().substring(2,8));
			} else {
				produto.setCodigoICD(input.getCodigoPublicacao().substring(0,6));
			}
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
		
		if(input.getSegmento()!=null && !input.getSegmento().trim().equals("")) {
			try {
				produto.setTipoSegmentoProduto(getTipoSegmento(new Long(input.getSegmento())));
			} catch(NumberFormatException ex) {
				produto.setTipoSegmentoProduto(getTipoSegmento(input.getSegmento()));
			}
		} else{
			produto.setTipoSegmentoProduto(getTipoSegmento(new Long(9)));	
		}
		
		String codigoSituacaoTributaria = input.getCodigoSituacaoTributaria();
		produto.setTributacaoFiscal(this.getTributacaoFiscal(codigoSituacaoTributaria));

		produto.setOrigem(Origem.INTERFACE);
		
		produto.addFornecedor(fornecedor);

		if (descontoLogistica != null) {

			produto.setDescontoLogistica(descontoLogistica);

		} else {
			validarDescontoLogistico(message, input.getCodigoPublicacao(), tipoDescontoInt);
		}

		this.getSession().persist(produto);

	}

	private void validarDescontoLogistico(Message message, String codigoPublicacao, int tipoDescontoInt) {
        String assunto = "Erro na Interface 109 PUB - Tipo Desconto não cadastrado na DescontoLogistico";
        String msg = "Tipo Desconto " + tipoDescontoInt
            + " não cadastrado na tabela Desconto Logístico arquivo .PUB vindo PRODIN, Código de Publicação  "
                + codigoPublicacao;
		sendEmailInterface(assunto, msg, message);
        this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, msg);
	}

	public void validarTipoDesconto(Message message, String tipoDesconto, String codigoPublicacao){
		if(tipoDesconto == null || tipoDesconto == "00" || tipoDesconto == "" || tipoDesconto == "0"){
            String assunto = "Erro na Interface 109 PUB - Tipo Desconto não consta no arquivo";
            String msg = "Tipo Desconto "+tipoDesconto+" não consta no arquivo .PUB de publicações vindo PRODIN, Código de Publicação  "
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
	
	
	private void atualizaProdutoConformeInput(Produto produto, Editor editor, TipoProduto tipoProduto, Message message) {

		EMS0109Input input = (EMS0109Input) message.getBody();

		String codigoDistribuidor = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
		
		Fornecedor fornecedor = this.findFornecedor(Integer.parseInt(codigoDistribuidor));
		
        if (fornecedor == null || fornecedor.getId()==null) {
            
            ndsiLoggerFactory.getLogger().logError(
                    message,
                    EventoExecucaoEnum.HIERARQUIA,
                    String.format( "Fornecedor nulo. Produto %1$s .", input.getCodigoPublicacao() )
                );
            return ;
        }
        
		validarTipoDesconto(message, input.getTipoDesconto(), input.getCodigoPublicacao());
		
		if(input.getTipoDesconto()==null){
			
            this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					"Tipo Desconto Nulo");
			 
		}
		
		int tipoDescontoInt = Integer.parseInt( input.getTipoDesconto());

		DescontoLogistica descontoLogistica =
		        this.descontoLogisticaService.obterDescontoLogisticaVigente(tipoDescontoInt,fornecedor.getId(),input.getDataGeracaoArquivo());
		
        if(descontoLogistica == null) {
			
            this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					"Desconto Logística não encontrado. "+tipoDescontoInt);
			 
		} else {
			produto.setDescontoLogistica(descontoLogistica);
			
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Desconto Logística"
					+" de "+ produto.getDescontoLogistica()
					+" para "+ descontoLogistica.getTipoDesconto().intValue()
					+ " Produto "+produto.getCodigo());
		}

		
		produto.setOrigem(Origem.INTERFACE);
		
		if (tipoProduto!=null &&  !produto.getTipoProduto().equals( tipoProduto )) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Tipo de Publicação"
					+" de "+ produto.getTipoProduto().getDescricao()
					+" para "+ tipoProduto.getDescricao()
					+ " Produto "+produto.getCodigo());
            
            produto.setTipoProduto(tipoProduto);
		}
		if (input.getNomePublicacao()!=null && !input.getNomePublicacao().trim().equals("") && !Objects.equal(produto.getNome(), input.getNomePublicacao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Nome do Produto de "+ produto.getNome()
					+ " para "+input.getNomePublicacao()
					+" Produto " + produto.getCodigo());
            
            produto.setNome(input.getNomePublicacao());
		}
		if (input.getContextoPublicacao()!=null && input.getContextoPublicacao().longValue()!=0 && !Objects.equal(produto.getCodigoContexto(), input.getContextoPublicacao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualizacao do Contexto do Produto de "+ produto.getCodigoContexto()
					+ " para "+input.getContextoPublicacao()
					+" Produto " + produto.getCodigo());
            
            produto.setCodigoContexto(input.getContextoPublicacao());
            
		}
		if(produto.getNomeComercial()==null && input.getNomePublicacao()!=null){
			
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Descrição de "+ "Nulo"
					+ " para "+input.getNomePublicacao()
					+" Produto " + produto.getCodigo());
            
            produto.setNomeComercial(input.getNomePublicacao());
            
	    }else if (input.getNomePublicacao()!=null && !input.getNomePublicacao().trim().equals("") && !Objects.equal(produto.getNomeComercial(), input.getNomePublicacao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Descrição de "+ produto.getNomeComercial()
					+ " para "+input.getNomePublicacao()
					+" Produto " + produto.getCodigo());
            
            produto.setNomeComercial(input.getNomePublicacao());
		}
		
		if(produto.getEditor()==null || produto.getEditor().getCodigo()==null){
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Editor"
					+ " de " + " Nulo "
					+ " para " + editor.getPessoaJuridica().getNome()
					+" Produto " + produto.getCodigo());
			
			produto.setEditor(editor);
			
		} else if (input.getCodigoEditor()!=null 
				&& input.getCodigoEditor().intValue()!=0 
				&& !Objects.equal(input.getCodigoEditor(), produto.getEditor().getCodigo())) {

		 if(produto.getEditor()!=null && produto.getEditor().getPessoaJuridica()!=null){
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Editor"
					+ " de " + produto.getEditor().getPessoaJuridica().getNome()
					+ " para " + editor.getPessoaJuridica().getNome()
					+" Produto " + produto.getCodigo());
		 }else{
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Editor"
					+ " de " + "Nulo"
					+ " para " + editor.getPessoaJuridica().getNome()
					+" Produto " + produto.getCodigo());
            
		 }
		 produto.setEditor(editor);
		}
		
		if(produto.getPeriodicidade()==null || produto.getPeriodicidade()==null){
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Periodicidade de "+ "Nulo"
					+ " para "+PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())
					+" Produto " + produto.getCodigo());
            
            produto.setPeriodicidade(PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
            
	    } else if (input.getPeriodicidade()!=null && input.getPeriodicidade().intValue()!=0 && !Objects.equal(produto.getPeriodicidade(), PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())) ) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Periodicidade de "+ produto.getPeriodicidade()
					+ " para "+PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())
					+" Produto " + produto.getCodigo());
            
            produto.setPeriodicidade(PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
		}
		if (input.getSlogan()!=null && !input.getSlogan().trim().equals("") && !Objects.equal(produto.getSlogan(), input.getSlogan())) {

			
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Slogan de " + produto.getSlogan()
					+ " para "+input.getSlogan()
					+" Produto " + produto.getCodigo());
            
            produto.setSlogan(input.getSlogan());
		}		
		if (input.getPeso()!=null && input.getPeso().longValue()!=0 && !Objects.equal(produto.getPeso(),input.getPeso())) {

            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Peso de " + produto.getPeso()
					+ " para "+input.getPeso()
					+" Produto " + produto.getCodigo());
            
            produto.setPeso(input.getPeso());
		}
		if (input.getCodigoPublicacao()!=null && !input.getCodigoPublicacao().trim().equals("")  && !Objects.equal(produto.getCodigo(), input.getCodigoPublicacao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Codigo da Publicação de "
					+ produto.getCodigo()
					+ " para " +input.getCodigoPublicacao()
					+" Produto " + produto.getCodigo());
            
            produto.setCodigo(input.getCodigoPublicacao());
            
		}
		if (produto.isAtivo() != input.isStatus()) {

            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Status de " + produto.isAtivo()
					+" para "+input.isStatus()
					+" Produto " + produto.getCodigo());
            
            produto.setAtivo(input.isStatus());
		}
		if (input.getDataDesativacao()!=null && !Objects.equal(produto.getDataDesativacao(), input.getDataDesativacao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Data de Desativação de "
					+ DateUtil.formatarDataPTBR(produto.getDataDesativacao())
					+ " para "+DateUtil.formatarDataPTBR(input.getDataDesativacao())
					+" Produto " + produto.getCodigo());
            
            produto.setDataDesativacao(input.getDataDesativacao());
		}
		if (input.getPeb()!=null && input.getPeb().intValue()!=0 && !Objects.equal(Integer.valueOf(produto.getPeb()), input.getPeb())) {

            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do PEB de " + produto.getPeb()
					+" para "+ input.getPeb()
					+" Produto " + produto.getCodigo());
            
            produto.setPeb(input.getPeb());
		}
		if (input.getPacotePadrao()!=null && input.getPacotePadrao().intValue()!=0 && !Objects.equal(Integer.valueOf(produto.getPacotePadrao()), input.getPacotePadrao())) {

            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Pacote Padrão de "
							+ produto.getPacotePadrao()
					+" para "+ input.getPacotePadrao()
					+" Produto " + produto.getCodigo());
            
            produto.setPacotePadrao(input.getPacotePadrao());
		}
		if (input.getPeso()!=null && input.getPeso().intValue()!=0 && !Objects.equal(produto.getPeso(), input.getPeso())) {

            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Peso de " + produto.getPeso()
            		+" para "+ input.getPeso()
            		+" Produto " + produto.getCodigo());
            
            produto.setPeso(input.getPeso());
		}
		
		if (input.getCodigoICD()!=null && 
		   !input.getCodigoICD().trim().equals("") && 
		   !input.getCodigoICD().trim().equals("0") && 
		   !input.getCodigoICD().trim().equals("000000") && 
		   !Strings.isNullOrEmpty(input.getCodigoICD()) 
		        && (produto.getCodigoICD()== null ||!Objects.equal(produto.getCodigoICD(), input.getCodigoICD()))) {
		   
		    this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração do Código ICD de " + produto.getCodigoICD()
		    		+ " para "+ new Integer(input.getCodigoICD()).toString()
		    		+" Produto " + produto.getCodigo());
		    
		    produto.setCodigoICD(new Integer(input.getCodigoICD()).toString());
		}
		
		TipoSegmentoProduto tipoSegmentoProduto = produto.getTipoSegmentoProduto();
		String segmento ="";
		
		if(tipoSegmentoProduto!=null){
			segmento = tipoSegmentoProduto.getDescricao();
		}
		
		if (input.getSegmento() != null && !input.getSegmento().trim().equals("") && !Objects.equal(segmento, input.getSegmento())) {
            
            this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração da Segmentação"
                    + " de "+ segmento
                    +" para " + input.getSegmento()
                    +" Produto " + produto.getCodigo()
                    );
        
            try {
            	produto.setTipoSegmentoProduto(getTipoSegmento(new Long(input.getSegmento())));
     	    } catch(NumberFormatException ex) {
     	    	produto.setTipoSegmentoProduto(getTipoSegmento(input.getSegmento()));
     	    }
		}
		
		Fornecedor produtoFornecedor = produto.getFornecedor();
		
		if(produtoFornecedor == null  && fornecedor!=null && fornecedor.getResponsavel()!=null && !fornecedor.getResponsavel().trim().equals("")){
			
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração de Fornecedor"
			        +" de Vazio" 
			        +" para " + fornecedor.getResponsavel()
			        +" Produto " + produto.getCodigo()
			        );
            
            	produto.setFornecedores(new HashSet<Fornecedor>());
		    
            	produto.addFornecedor(fornecedor);
			
		} else if (!Objects.equal(produtoFornecedor.getCodigoInterface(), fornecedor.getCodigoInterface()))  {

                this.ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração de Fornecedor"
				        +" de " + produto.getFornecedor().getResponsavel()
				        +" para " + fornecedor.getResponsavel()
				        +" Produto " + produto.getCodigo()
				        );
                
                	produto.setFornecedores(new HashSet<Fornecedor>());
			    
                	produto.addFornecedor(fornecedor);
		}
		

		if (descontoLogistica == null) {
			validarDescontoLogistico(message, input.getCodigoPublicacao(), tipoDescontoInt);
        } else {

			if (descontoLogistica != null && descontoLogistica.getDescricao()!=null && !descontoLogistica.getDescricao().trim().equals("") && !produto.getDescontoLogistica().equals(descontoLogistica)) {

				

                this.ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.INF_DADO_ALTERADO,
                        "Alteração do Tipo Desconto"
                        + " de "+ descontoLogistica.getTipoDesconto()
                        +" para " + descontoLogistica.getDescricao()
                        +" Produto " + produto.getCodigo()
                );
                
                produto.setDescontoLogistica(descontoLogistica);
				
			}
		}

		if(input.getCodigoSituacaoTributaria()!=null){
			
		
		 TributacaoFiscal tributacaoFiscal = getTributacaoFiscal(input.getCodigoSituacaoTributaria());
		
		 if (tributacaoFiscal!=null && produto.getTributacaoFiscal() != tributacaoFiscal) {
			
            this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração da Tributação Fiscal para "
            		+ " de "+ tributacaoFiscal.getValue()
            		+" para " + tributacaoFiscal
            		+" Produto " + produto.getCodigo()
            		);
            
            produto.setTributacaoFiscal(tributacaoFiscal);
		 }
		}
		FormaComercializacao formaComercializacaoInput = input.getFormaComercializacao().equals("CON") ? 
		        FormaComercializacao.CONSIGNADO : FormaComercializacao.CONTA_FIRME;

		if (formaComercializacaoInput!=null && produto.getFormaComercializacao() != formaComercializacaoInput ) {
				
            this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração da Forma de Comercialização"
    					+ " de "+ formaComercializacaoInput.getValue()
    					+" para " + formaComercializacaoInput
    					+" Produto " + produto.getCodigo()
    					);
            
            produto.setFormaComercializacao(formaComercializacaoInput);
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
