package br.com.abril.nds.integracao.ems0110.processor;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0110FilialInput;
import br.com.abril.nds.integracao.model.canonic.EMS0110Input;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Brinde;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Dimensao;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.cadastro.TributacaoFiscal;
import br.com.abril.nds.model.cadastro.desconto.DescontoProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.DescontoProdutoEdicaoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.util.DateUtil;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

@Component
public class EMS0110MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DescontoProdutoEdicaoRepository descontoProdutoEdicaoRepository;
		
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private LancamentoService lancamentoService;

	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
	private static final String ZEROS_NBM = "000000000";
	
	private static final String FORMATO_DATA = "yyyyMMdd";
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

	}

	@Override
	public void processMessage(Message message) {

		if (verificarDistribuidor(message)) {

			ProdutoEdicao edicao = this.findProdutoEdicao(message);

			if (edicao == null) {

				Produto produto = this.findProduto(message);
				
				if (produto == null) {
					produto = this.criarProdutoComInputDeEdicao(message);
				}
					
				edicao = this.criarProdutoEdicaoConformeInput(produto, message);
				
				if (edicao != null) {
				    
				    this.criarLancamento(edicao, message, produto.getFormaComercializacao());				    
				}

			} else {

				this.atualizaProdutoEdicaoConformeInput(edicao, message);
			}

		} else {

			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor não encontrato. "+message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.toString()));
//			throw new RuntimeException("Distribuidor nao encontrado.");
		}
	}
	
	private void criarLancamento(ProdutoEdicao edicao, Message message, FormaComercializacao formaComercializacaoDoProduto) {

	    EMS0110FilialInput input = (EMS0110FilialInput) message.getBody();
	    
	    Lancamento lancamento = new Lancamento();
	    
	    Date dataGeracaoArq = DateUtil.parseData(input.getDataGeracaoArq(), FORMATO_DATA);
	    
	    Long idFornecedor = edicao.getProduto().getFornecedor().getId();
	    
	    Date dataLancamento = lancamentoService.obterDataLancamentoValido(dataGeracaoArq, idFornecedor);
	    
	    Date dataRecolhimento = DateUtil.adicionarDias(dataLancamento, edicao.getPeb());
	    
        lancamento.setDataCriacao(dataGeracaoArq);
        lancamento.setNumeroLancamento(1);
        lancamento.setDataLancamentoPrevista(dataLancamento);
        lancamento.setDataLancamentoDistribuidor(dataLancamento);
        
        //Se a forma de recolhimento do produto for Conta Firme a data de recolhimento deve se igual a data de lançamento,
        //pois esse produto não vai ter recolhimento, e não ira no processo de consignação.
        if(FormaComercializacao.CONTA_FIRME.equals(formaComercializacaoDoProduto)){
        	lancamento.setDataRecolhimentoPrevista(dataLancamento);
            lancamento.setDataRecolhimentoDistribuidor(dataLancamento);
        }
        else{
        	lancamento.setDataRecolhimentoPrevista(dataRecolhimento);
            lancamento.setDataRecolhimentoDistribuidor(dataRecolhimento);
        }
        
        lancamento.setProdutoEdicao(edicao);
        lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
        lancamento.setDataStatus(new Date());
        lancamento.setStatus(StatusLancamento.CONFIRMADO);
        lancamento.setReparte(BigInteger.ZERO);
        lancamento.setRepartePromocional(BigInteger.ZERO);
        
        this.getSession().persist(lancamento);
    }

    private Editor findEditorByID(Long codigoEditor) {

		StringBuilder sql = new StringBuilder();

		sql.append("select editor from Editor editor ");
		sql.append("where editor.codigo = :codigoEditor ");
		sql.append("where editor.ativo = :true ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigoEditor", codigoEditor);
		query.setParameter("true", true);

		return (Editor) query.uniqueResult();		
	}
	
	private TipoProduto findTipoProduto(Long codigoCategoria) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT tp FROM TipoProduto tp ");
		sql.append("WHERE  tp.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());
		query.setParameter("codigo", codigoCategoria);

		@SuppressWarnings("unchecked")
		List<TipoProduto> tiposProduto = query.list();

		TipoProduto tipoProduto = null;

		if (!tiposProduto.isEmpty()) {

			tipoProduto = tiposProduto.get(0);

			

		} 
		return tipoProduto;
	}
	
	
	private TipoClassificacaoProduto findTipoClassificacaoProdutoPorNome(String nome) {
		
		Criteria criteria = this.getSession().createCriteria(TipoClassificacaoProduto.class);
        criteria.add(Restrictions.eq("descricao", nome));
        
        //FIXME Romover assim que vier a descricao completa de segmento no arquivo . prd
        criteria.setMaxResults(1);
        
        return (TipoClassificacaoProduto) criteria.uniqueResult();
	}
	
	private TipoClassificacaoProduto findTipoClassificacaoProdutoPorId(Long id) {
        Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
        criteria.add(Restrictions.idEq(id));
   
        
        return (TipoClassificacaoProduto) criteria.uniqueResult();
	}
	
	private TipoSegmentoProduto findTipoSegmentoProdutoPorId(Long id) {
        Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
        criteria.add(Restrictions.idEq(id));
   
        
        return (TipoSegmentoProduto) criteria.uniqueResult();
	}
	
	// Deprecated
	private TipoSegmentoProduto findTipoSegmentoProdutoPorNome(String nome) {
        Criteria criteria = this.getSession().createCriteria(TipoSegmentoProduto.class);
        criteria.add(Restrictions.like("descricao", nome));
        
        //FIXME Romover assim que vier a descricao completa de segmento no arquivo . prd
        criteria.setMaxResults(1);
        
        return (TipoSegmentoProduto) criteria.uniqueResult();
	}

    private TipoSegmentoProduto criarNovoSegmento(String nome) {
        TipoSegmentoProduto tipoSegmentoProduto = new TipoSegmentoProduto();
        tipoSegmentoProduto.setDescricao(nome);
        return (TipoSegmentoProduto) this.getSession().merge(tipoSegmentoProduto);
    }

   
    private TipoClassificacaoProduto getTipoClassificacaoProduto(String nome) {
        TipoClassificacaoProduto tipoClassificacaoProduto = null;
       
        if(!Strings.isNullOrEmpty(nome)) {
            
        	tipoClassificacaoProduto = findTipoClassificacaoProdutoPorNome(nome.trim());
            
            if(tipoClassificacaoProduto == null) {
            	//FIXME
            	//tipoClassificacaoProduto = findTipoClassificacaoProdutoPorNome("NORMAL");
            	tipoClassificacaoProduto = findTipoClassificacaoProdutoPorId(new Long(16));
            }
        }
        return tipoClassificacaoProduto;
        
    }
    
    private TipoSegmentoProduto getTipoSegmento(Long id) {

    	if(id==null||id.intValue()==0){
    		//Caso venha 0 do arquivo troca para OUTROS - 9
    		id = new Long(9); 
    	}
       
        
        return findTipoSegmentoProdutoPorId(id);
        
    }
    
    //Deprecated
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
	
    private TipoClassificacaoProduto findTipoClassificacaoProduto(String classificacao) {
        
    	TipoClassificacaoProduto tipoClassificacaoProdutoAux;
    	Criteria criteria = this.getSession().createCriteria(TipoClassificacaoProduto.class);
        criteria.add(Restrictions.eq("descricao", classificacao.trim().toUpperCase()));
        tipoClassificacaoProdutoAux = (TipoClassificacaoProduto) criteria.uniqueResult();
        
        if(tipoClassificacaoProdutoAux==null){
         criteria.add(Restrictions.ge("descricao", classificacao.trim().toUpperCase()));
         tipoClassificacaoProdutoAux = (TipoClassificacaoProduto) criteria.list().get(0);
        }
        
        return tipoClassificacaoProdutoAux;
    }
    
    private TipoClassificacaoProduto criarNovoTipoClassificacaoProduto(String classificacao) {
        TipoClassificacaoProduto tipoClassificacaoProduto = new TipoClassificacaoProduto();
        tipoClassificacaoProduto.setDescricao(classificacao);
        return (TipoClassificacaoProduto) this.getSession().merge(tipoClassificacaoProduto);
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
	
	private Produto criarProdutoComInputDeEdicao(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		Produto produto = new Produto();
		
		Fornecedor fornecedor = this.obterFornecedor(message);
		
		if (fornecedor == null) {

            ndsiLoggerFactory.getLogger().logError(
                    message,
                    EventoExecucaoEnum.HIERARQUIA,
                    String.format( "Fornecedor Nulo. Produto  %1$s"+" Edição "+input.getEdicaoProd(), input.getCodProd() )
                );
            
            return null;
        }
		
		DescontoLogistica descontoLogistica =
		        this.descontoLogisticaService.obterDescontoLogisticaVigente(Integer.parseInt( input.getTipoDesconto()),
		                                                                    fornecedor.getId(),
		                                                                    DateUtil.parseData(input.getDataGeracaoArq(), FORMATO_DATA));
		
		if(descontoLogistica == null) {
			
			if(input.getTipoDesconto()!=null && !input.getTipoDesconto().trim().equals("") 
					&& !input.getTipoDesconto().trim().equals("0")
					&& !input.getTipoDesconto().trim().equals("00")) {
				
				this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					"Desconto Logística não encontrado. "+Integer.parseInt( input.getTipoDesconto())
					+ " Produto "+produto.getCodigo());
			}
			 
		} else {
			
			produto.setDescontoLogistica(descontoLogistica);
			
            this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Desconto Logística"
					+" de "+ produto.getDescontoLogistica().getTipoDesconto()
					+" para "+ descontoLogistica.getTipoDesconto().intValue()
					+ " Produto "+produto.getCodigo());
		}
		
		Editor editor = this.findEditorByID(input.getCodEditor());

		if (null == editor) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Editor " + input.getCodEditor() + " não encontrado. Produto " + input.getCodProd() +" Edição "+input.getEdicaoProd());

//			throw new RuntimeException("Editor " + input.getCodEditor() + " nao encontrado. Código do produto: " + input.getCodProd() +  " - Nome do Produto: " + input.getNomeProd());
		}
		
		TipoProduto tipoProduto = this.findTipoProduto(input.getCodCategoria());

		if (null == tipoProduto) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Tipo Produto "+input.getCodCategoria()+" não encontrado. Produto "+input.getCodProd()+" Edição "+input.getEdicaoProd());

//			throw new RuntimeException("Tipo Produto nao encontrado.");
		}
				
		this.ndsiLoggerFactory.getLogger().logError(message,
				EventoExecucaoEnum.SEM_DOMINIO,
				"Publicação Cadastrada através do Produto Edição, Código ICD não será Preenchido. Produto "+input.getCodProd()+" Edição "+input.getEdicaoProd());

		
		produto.setTipoProduto(tipoProduto);
		produto.setNome(input.getNomeProd());
		produto.setCodigoContexto(input.getContextoPublicacao());
		produto.setNomeComercial(input.getNomeComercial());
		produto.setEditor(editor);
		produto.setPeriodicidade(PeriodicidadeProduto.INDEFINIDO); 	// default definito por Eduardo e Paulo em 13/12/2012
		produto.setSlogan("");										// default definito por Eduardo e Paulo em 13/12/2012
		produto.setPeb(input.getPeb());
		produto.setPeso(input.getPesoUni());
		produto.setPacotePadrao(input.getPactPadrao());
		produto.setCodigo(input.getCodProd());
		produto.setAtivo(input.getStatusProd());
		produto.setDataDesativacao(input.getDataDesativacao());
		produto.setFormaComercializacao(
				(input.getFormaComercializacao().equals("CON") 
						? FormaComercializacao.CONSIGNADO 
						: FormaComercializacao.CONTA_FIRME
				) 
		);
		
		if (input.getSegmento() == null || input.getSegmento().trim().equals("")) {
			
			if(produto.getTipoSegmentoProduto()==null || produto.getTipoSegmentoProduto().getId().intValue()==0){
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Produto com Segmento INDEFINIDO, Será atribuido 'OUTROS'. Produto "+input.getCodProd()+" Edição "+input.getEdicaoProd(), input.getCodProd(), input.getEdicaoProd() )
				);
			
			//FIXME Não deveria vir Segmento como nulo
			produto.setTipoSegmentoProduto(getTipoSegmento(new Long(9)));
			}
			
			
		}else{
			produto.setTipoSegmentoProduto(getTipoSegmento(new Long(input.getSegmento())));
		}

		
		String codigoSituacaoTributaria = input.getCodSitTributaria();
		produto.setTributacaoFiscal(this.getTributacaoFiscal(codigoSituacaoTributaria));

		produto.setOrigem(Origem.INTERFACE);
		
		produto.addFornecedor(fornecedor);
		
		if (descontoLogistica != null && descontoLogistica.getPercentualDesconto() != null) {

			produto.setDescontoLogistica(descontoLogistica);

		} else {
			
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Produto sem Desconto-Logística:  %1$s - %1$s", input.getCodProd(), input.getEdicaoProd() )
				);
			
		}

		this.getSession().persist(produto);
		
		this.ndsiLoggerFactory.getLogger().logWarning(message,
				EventoExecucaoEnum.SEM_DOMINIO,
				"Produto Inserido com Periodicidade INDEFINIDA. Produto "+ produto.getCodigo()+" Edição "+input.getEdicaoProd());	
		return produto;
	}

    private Fornecedor obterFornecedor(Message message) {
        String codigoDistribuidor = 
                message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
		
		Fornecedor fornecedor = this.fornecedorRepository.obterFornecedorPorCodigoInterface(Integer.parseInt(codigoDistribuidor));
        return fornecedor;
    }

	private boolean verificarDistribuidor(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		String codigoDistribuidorSistema = message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.toString()).toString();
		
		String codigoDistribuidorArquivo = input.getCodDistrib();

		if (codigoDistribuidorSistema.equals(codigoDistribuidorArquivo)) {

			return true;
		}

		return false;
	}

	private ProdutoEdicao findProdutoEdicao(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT pe FROM ProdutoEdicao pe JOIN FETCH pe.produto p ");
		sql.append("WHERE pe.numeroEdicao = :numeroEdicao ");
		sql.append(" AND  p.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("numeroEdicao", input.getEdicaoProd());
		query.setParameter("codigo", input.getCodProd());

		return (ProdutoEdicao) query.uniqueResult();
	}

	private Produto findProduto(Message message) {
		EMS0110Input input = (EMS0110Input) message.getBody();

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT p FROM Produto p ");
		sql.append("WHERE p.codigo = :codigo ");

		Query query = this.getSession().createQuery(sql.toString());

		query.setParameter("codigo", input.getCodProd());

		return (Produto) query.uniqueResult();
	}
	
	private ProdutoEdicao criarProdutoEdicaoConformeInput(Produto produto, Message message) {
		
		EMS0110FilialInput input = (EMS0110FilialInput) message.getBody();

		ProdutoEdicao edicao = new ProdutoEdicao();
		Dimensao dimensao = new Dimensao();
		
		Brinde brinde = null;
		
		if (input.isContemBrinde()){
			
			brinde = new Brinde();
			brinde.setDescricao(input.getDescBrinde());
			brinde.setPermiteVendaSeparada(input.getCondVendeSeparado());
		}
		
		Fornecedor fornecedor = this.obterFornecedor(message);
		
		//FIXME
		DescontoLogistica descontoLogistica;
		
		if(input.getTipoDesconto()==null || input.getTipoDesconto().trim().equals("")) {
			descontoLogistica = this.descontoLogisticaService.obterDescontoLogisticaVigente(Integer.parseInt("1"),
                    fornecedor.getId(),
                    DateUtil.parseData(input.getDataGeracaoArq(), FORMATO_DATA));
		} else {
			descontoLogistica = this.descontoLogisticaService.obterDescontoLogisticaVigente(Integer.parseInt( input.getTipoDesconto()),
                    fornecedor.getId(),
                    DateUtil.parseData(input.getDataGeracaoArq(), FORMATO_DATA));
		}
		
		if (!Objects.equal(produto.getCodigoContexto(), input.getContextoProd())) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código Contexto do Produto "+input.getCodProd()
					+" de "+ produto.getCodigoContexto()
					+" para "+ input.getContextoProd()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			produto.setCodigoContexto(input.getContextoProd());
		}
		
		/*
		if (null != produto.getTipoProduto()) {

			if(null != produto.getTipoProduto().getCodigoNBM() && (input.getCodNBM() != null && !input.getCodNBM().equals(ZEROS_NBM))) {
				
				if(!produto.getTipoProduto().getCodigoNBM().equals(input.getCodNBM())) {
					
				  this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do Código NBM"
						+" de "+ produto.getTipoProduto().getCodigoNBM()
						+" para "+ input.getCodNBM()
						+" Produto "+ input.getCodProd()
						+" Edição "+ input.getEdicaoProd());
				}
				
			} else {
				
				this.ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração do Código NBM"
						+" de Nulo "
						+" para "+ input.getCodNBM()
						+" Produto "+ input.getCodProd()
						+" Edição "+ input.getEdicaoProd());
				
			}
			
			produto.getTipoProduto().setCodigoNBM(input.getCodNBM());
		}
		*/

		if (input.getNomeComercial()!=null && !input.getNomeComercial().trim().equals("") 
				&& !Objects.equal(produto.getNomeComercial(), input.getNomeComercial()) 
		        && !input.getNomeComercial().isEmpty()) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Nome Comercial"
					+" de "+ produto.getNomeComercial()
					+" para "+ input.getNomeComercial()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			produto.setNomeComercial(input.getNomeComercial());
		}
		
		TipoSegmentoProduto tipoSegmentoProduto = produto.getTipoSegmentoProduto();
        if ((tipoSegmentoProduto == null && input.getSegmento() != null) || 
                (input.getSegmento() != null && !input.getSegmento().trim().equals("") && !Objects.equal(
                        tipoSegmentoProduto.getDescricao(), input.getSegmento()))) {
            
        	if(produto != null && produto.getTipoSegmentoProduto() != null 
        			&& produto.getTipoSegmentoProduto().getDescricao() != null) {
        		
	        	if((produto.getTipoSegmentoProduto().getId()==0 || produto.getTipoSegmentoProduto().getId()==9 ) &&
	        		(new Long(input.getSegmento()).intValue()!=0 && new Long(input.getSegmento()).intValue()!=9)) {
	        		
	        		this.ndsiLoggerFactory.getLogger().logInfo(message,
	                    EventoExecucaoEnum.INF_DADO_ALTERADO,
	                    "Alteração do Tipo de Segmento"
	                    +" de " + produto.getTipoSegmentoProduto().getDescricao()
	                    +" para " + getTipoSegmento(new Long(input.getSegmento())).getDescricao()
	                    +" Produto "+ input.getCodProd()
						+" Edição "+ input.getEdicaoProd());
	            
	               produto.setTipoSegmentoProduto(getTipoSegmento(new Long(input.getSegmento())));
	        	}
        	} else {
        		
        		this.ndsiLoggerFactory.getLogger().logInfo(message,
                        EventoExecucaoEnum.INF_DADO_ALTERADO,
                        "Alteração do Tipo de Segmento"
                        +" de Nulo "
                        +" para " + getTipoSegmento(new Long(9)).getDescricao()
                        +" Produto "+ input.getCodProd()
    					+" Edição "+ input.getEdicaoProd());
        		
        		produto.setTipoSegmentoProduto(getTipoSegmento(new Long(9)));
        		
        	}
            
            
        }
        
		if (descontoLogistica != null 
				&& descontoLogistica.getPercentualDesconto() != null 
				&& descontoLogistica.getPercentualDesconto().longValue() > 0) {

			edicao.setDescontoLogistica(descontoLogistica);

		} else {
			
			StringBuilder sb = new StringBuilder("Desconto Logística ("+input.getTipoDesconto()+") Não encontrado. Produto "+input.getCodProd()+" Edição "+input.getEdicaoProd());
			
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					sb.toString());
			
			return null;
		}
		
		// FIX input.getCodIcd
		
		edicao.setProduto(produto);

		dimensao.setLargura(input.getLargura());
		dimensao.setComprimento(input.getComprimento());
		dimensao.setEspessura(input.getExpessura());
		edicao.setDimensao(dimensao);
		
		edicao.setBrinde(brinde);

		String codigoDeBarras = input.getCodBarra();
		boolean validarCodigoBarras = validarCodigoBarras(produto.getCodigo(), input.getEdicaoProd(), message, codigoDeBarras);
		if(validarCodigoBarras) {
			edicao.setCodigoDeBarras(codigoDeBarras);
		}
		
		edicao.setNumeroEdicao(input.getEdicaoProd());
		edicao.setPacotePadrao(input.getPactPadrao());
		edicao.setPeb(input.getPeb());
		edicao.setPeso(input.getPesoUni());
		edicao.setPossuiBrinde(input.isContemBrinde());
		edicao.setDataDesativacao(input.getDataDesativacao());
		edicao.setChamadaCapa(input.getChamadaCapa());
		edicao.setOrigem(Origem.INTERFACE);
		edicao.setNomeComercial(input.getNomeComercial());
		
		TipoClassificacaoProduto tpclassificacao = getTipoClassificacaoProduto(input.getClassificacao());
		
		if(tpclassificacao!=null){
		  
			edicao.setTipoClassificacaoProduto(tpclassificacao);
		
		} else { // ---
			
			ndsiLoggerFactory.getLogger().logError(
	                message,
	                EventoExecucaoEnum.HIERARQUIA,
	                "-Classificação Nula não existe. Produto "+input.getCodProd()+" Edição "+ input.getEdicaoProd());
			
			//FIXME Classificação não deveria vir como nula.
			edicao.setTipoClassificacaoProduto(getTipoClassificacaoProduto("NORMAL"));
		}
		
		boolean isParcial = false;
		
		if(input.getRegimeRecolhimento()!= null){
			isParcial = ("P".equals(input.getRegimeRecolhimento().toUpperCase()));
		}
		
		edicao.setParcial(isParcial);
		
		edicao.setCodigoDeBarraCorporativo(input.getCodigoBarrasCorporativo());
		
		if(null != edicao.getCodigoNBM() && (input.getCodNBM() != null && !input.getCodNBM().equals(ZEROS_NBM))) {
				
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código NBM"
					+" de "+ edicao.getCodigoNBM()
					+" para "+ input.getCodNBM()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
		} else {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código NBM"
					+" de Nulo "
					+" para "+ input.getCodNBM()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
		}
		
		edicao.setCodigoNBM(input.getCodNBM());
		
		this.getSession().persist(edicao);
		
		//inserirDescontoProdutoEdicao(edicao, produto);
		
		return edicao;
	}

	private boolean validarCodigoBarras(String codigo, Long edicao, Message message,	String codigoDeBarras) {
		 
		if (codigoDeBarras == null || "".equals(codigoDeBarras) || new BigInteger(codigoDeBarras).compareTo(BigInteger.ZERO) <= 0){
			this.ndsiLoggerFactory.getLogger().logWarning(message, EventoExecucaoEnum.RELACIONAMENTO, "Não Atualização do Código de Barras Vazio. Produto "+codigo +" Edição "+edicao);
			return false;
		}
		return true;
	}

	/**
	 * Insere os dados de desconto relativos ao produto edição em questão.
	 * 
	 * @param produtoEdicao
	 * @param indNovoProdutoEdicao
	 */
	private void inserirDescontoProdutoEdicao(ProdutoEdicao produtoEdicao, Produto produto) {
		
		GrupoProduto grupoProduto = produto.getTipoProduto().getGrupoProduto() ;
		
		if(GrupoProduto.OUTROS.equals(grupoProduto)) {
			return;
		}
		
		List<Fornecedor> fornecedores = fornecedorRepository.obterFornecedoresDeProduto(produto.getCodigo(), null);
		
		if(fornecedores == null || fornecedores.isEmpty()) {
			throw new IllegalStateException("Não há Fornecedor associado. Produto " + produto.getCodigo() + " Edição " + produtoEdicao.getNumeroEdicao());
		}

		if(fornecedores.size()!=1) {
			throw new IllegalStateException("Mais de um Fornecedor associado. Produto " + produto.getCodigo() + " Edição " + produtoEdicao.getNumeroEdicao());
		}
		
		Set<Fornecedor> conjuntoFornecedor = new HashSet<Fornecedor>();
		
		Fornecedor fornecedor = fornecedores.get(0);
		
		conjuntoFornecedor.add(fornecedor);
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoEspecifico = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.ESPECIFICO, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoEspecifico!=null && !conjuntoDescontoProdutoEdicaoEspecifico.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoEspecifico : conjuntoDescontoProdutoEdicaoEspecifico) {
				
				Cota cota = descontoEspecifico.getCota();
				
				descontoService.processarDescontoCota(cota, conjuntoFornecedor, descontoEspecifico.getDesconto());
				
			}
			
			
		}
		
		Set<DescontoProdutoEdicao> conjuntoDescontoProdutoEdicaoGeral = 
				descontoProdutoEdicaoRepository.obterDescontoProdutoEdicao(TipoDesconto.GERAL, fornecedor, null);
		
		if(conjuntoDescontoProdutoEdicaoGeral!=null && !conjuntoDescontoProdutoEdicaoGeral.isEmpty()) {
			
			
			for(DescontoProdutoEdicao descontoGeral : conjuntoDescontoProdutoEdicaoGeral) {
				
				descontoService.processarDescontoDistribuidor(conjuntoFornecedor, descontoGeral.getDesconto());
				
			}
			
			
		}
		
	}

	
	private void atualizaProdutoEdicaoConformeInput(ProdutoEdicao edicao, Message message) {
		
		EMS0110FilialInput input = (EMS0110FilialInput) message.getBody();

		edicao.setOrigem(Origem.INTERFACE);
		Produto produto = edicao.getProduto();
		
		if (!Objects.equal(produto.getCodigoContexto(),input.getContextoProd())) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código Contexto"
					+" de "+ edicao.getProduto().getCodigoContexto()
					+" para "+ input.getContextoProd()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.getProduto().setCodigoContexto(input.getContextoProd());
		}
		
		if (!Objects.equal(produto.getNomeComercial(), input.getNomeComercial()) 
		        && !input.getNomeComercial().isEmpty()) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Nome Comercial"
					+" de "+ produto.getNomeComercial()
					+" para "+ input.getNomeComercial()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			produto.setNomeComercial(input.getNomeComercial());
		}
		
		/*
		if ( null != produto.getTipoProduto() && null != produto.getTipoProduto().getCodigoNBM() 
				&& !produto.getTipoProduto().getCodigoNBM().equals(input.getCodNBM()) 
				&& !input.getCodNBM().equals(ZEROS_NBM)) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código NBM"
					+" de " + produto.getTipoProduto().getCodigoNBM()
					+" para " + input.getCodNBM()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			produto.getTipoProduto().setCodigoNBM(input.getCodNBM());
		}
		*/
		
		TipoSegmentoProduto tipoSegmentoProduto = produto.getTipoSegmentoProduto();
        
        if ((tipoSegmentoProduto == null && input.getSegmento() != null && !input.getSegmento().trim().equals("")) 
        		|| (input.getSegmento() != null && !input.getSegmento().trim().equals("") 
                	&& !Objects.equal(tipoSegmentoProduto.getId(), Long.valueOf(input.getSegmento())))) {
            
            this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração de Segmentação"
                    +" de " + produto.getTipoSegmentoProduto().getDescricao()
                    +" para " + getTipoSegmento(new Long (input.getSegmento()))
                    +" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
            
            produto.setTipoSegmentoProduto(getTipoSegmento(new Long (input.getSegmento())));
        } else if(tipoSegmentoProduto == null) {
        	
        	this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração de Segmentação"
                    +" de INDEFINIDO" 
                    +" para " + getTipoSegmento(new Long (input.getSegmento())).getDescricao()
                    +" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
            
            produto.setTipoSegmentoProduto(getTipoSegmento(new Long (input.getSegmento())));
        	
        }
		
		if (!Objects.equal(edicao.getNomeComercial(), input.getNomeComercial()) 
		        && !input.getNomeComercial().isEmpty()) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Nome Comercial"
					+" de " + edicao.getNomeComercial()
					+" para " + input.getNomeComercial()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setNomeComercial(input.getNomeComercial());
		}
		
		
		Dimensao dimensaoEdicao = edicao.getDimensao();
		
		if (null != dimensaoEdicao && null != dimensaoEdicao.getLargura() && input.getLargura()!=null && !dimensaoEdicao.getLargura().equals(input.getLargura())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Largura"
					+" de " + dimensaoEdicao.getLargura()
					+" para " + input.getLargura()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			dimensaoEdicao.setLargura(input.getLargura());
		}
		
		if (null != dimensaoEdicao && null != dimensaoEdicao.getComprimento() && !dimensaoEdicao.getComprimento()
				.equals(input.getComprimento())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Comprimento"
					+" de " + dimensaoEdicao.getComprimento()
					+" para " + input.getComprimento()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			dimensaoEdicao.setComprimento(input.getComprimento());
		}
		
		if (null != dimensaoEdicao && null != dimensaoEdicao.getEspessura() && !dimensaoEdicao.getEspessura().equals(input.getExpessura())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Espessura"
					+" de " + dimensaoEdicao.getEspessura()
					+" para " + input.getExpessura()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			dimensaoEdicao.setEspessura(input.getExpessura());
		}
		
		Brinde brindeEdicao = edicao.getBrinde();
		if (null != brindeEdicao && null != brindeEdicao.getPermiteVendaSeparada() && brindeEdicao.getPermiteVendaSeparada() != input
				.getCondVendeSeparado()) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Permite Venda Separada"
					+" de "+ brindeEdicao.getPermiteVendaSeparada()
					+" para "+ input.getCondVendeSeparado()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			brindeEdicao.setPermiteVendaSeparada(input.getCondVendeSeparado());
		}
		if (null != brindeEdicao && null != brindeEdicao.getDescricao() && !brindeEdicao.getDescricao().equals(input.getDescBrinde())) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração Descricao Brinde"
					+" de "+ brindeEdicao.getDescricao()
					+" para "+ input.getDescBrinde()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			brindeEdicao.setDescricao(input.getDescBrinde());
		}

		if (!"".equals(input.getChamadaCapa().trim()) && 
		        !Objects.equal(edicao.getChamadaCapa(), input.getChamadaCapa())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Chamada de Capa"
					+" de " + edicao.getChamadaCapa()
					+" para " + input.getChamadaCapa()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setChamadaCapa(input.getChamadaCapa());
		}
		
		TipoClassificacaoProduto tipoClassificacaoProduto = edicao.getTipoClassificacaoProduto();
		
        if (tipoClassificacaoProduto == null && input.getClassificacao() != null){
            
            this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração do Tipo de Classificação"
                    +" de Nulo"
                    +" para " + input.getClassificacao()
                    +" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
            
            edicao.setTipoClassificacaoProduto(getTipoClassificacaoProduto(input.getClassificacao()));
        
        }else if (input.getClassificacao() != null && !Objects.equal(
                        tipoClassificacaoProduto.getDescricao(), input.getClassificacao())) {
            
            this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Alteração do Tipo de Classificação"
                    +" de " + tipoClassificacaoProduto.getDescricao()
                    +" para " + input.getClassificacao()
                    +" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
            
            edicao.setTipoClassificacaoProduto(getTipoClassificacaoProduto(input.getClassificacao()));
        } else if (input.getClassificacao() == null){
        	
        	this.ndsiLoggerFactory.getLogger().logInfo(message,
                    EventoExecucaoEnum.INF_DADO_ALTERADO,
                    "Tipo de Classificação Nula."
                    +" Produto "+ input.getCodProd()
        			+" Edição "+ input.getEdicaoProd());
        	
        	edicao.setTipoClassificacaoProduto(getTipoClassificacaoProduto("NORMAL"));
        }
        
		if (edicao.isPossuiBrinde() != input.isContemBrinde()) {

			this.ndsiLoggerFactory.getLogger()
					.logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Alteração Possui Brinde"
							+" de "+ edicao.isPossuiBrinde()
							+" para "+ input.isContemBrinde()
							+" Produto "+ input.getCodProd()
							+" Edição "+ input.getEdicaoProd());
			
			edicao.setPossuiBrinde(input.isContemBrinde());
		}

		if (null != edicao.getCodigoDeBarras() && !Objects.equal(edicao.getCodigoDeBarras(), input.getCodBarra())) {

			String codigoDeBarras = input.getCodBarra();
			boolean validarCodigoBarras = validarCodigoBarras(produto.getCodigo(), edicao.getNumeroEdicao(), message, codigoDeBarras);
			
			if(validarCodigoBarras) {
				
				if(edicao != null && edicao.getCodigoDeBarras() != null 
						&& input.getCodBarra() != null 
						&& new Long(edicao.getCodigoDeBarras()).longValue() != new Long(input.getCodBarra()).longValue()) {
					
					this.ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Alteração do Código de Barras"
									+" de " + edicao.getCodigoDeBarras()
									+" para " + input.getCodBarra()
									+" Produto "+ input.getCodProd()
									+" Edição "+ input.getEdicaoProd());
				}
				
				edicao.setCodigoDeBarras(codigoDeBarras);
			}
		}

		if (!Objects.equal(edicao.getDataDesativacao(), input.getDataDesativacao())) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração da Data de Desativação"
					+" de "+ DateUtil.formatarDataPTBR(edicao.getDataDesativacao())
					+" para "+ DateUtil.formatarDataPTBR(input.getDataDesativacao())
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setDataDesativacao(input.getDataDesativacao());
		}
		
		if (edicao.getPeb() != input.getPeb()) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do PEB"
					+" de " + edicao.getPeb()
					+" para " + input.getPeb()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setPeb(input.getPeb());
		}
		
		if (edicao.getPacotePadrao() != input.getPactPadrao()) {

			this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Pacote Padrao"
					+" de "+ edicao.getPacotePadrao()
					+" para "+ input.getPactPadrao()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setPacotePadrao(input.getPactPadrao());
		}
		
		if (!Objects.equal(edicao.getPeso(),input.getPesoUni())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Peso"
					+" de " + edicao.getPeso()
					+" para " + input.getPesoUni()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setPeso(input.getPesoUni());
		}
		
		if (!Objects.equal(edicao.getCodigoDeBarraCorporativo(),input.getCodigoBarrasCorporativo())) {

			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código de Barras Corporativo"+input.getCodProd()
					+" de " + edicao.getCodigoDeBarraCorporativo()
					+" para " + input.getCodigoBarrasCorporativo()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setCodigoDeBarraCorporativo(input.getCodigoBarrasCorporativo());
		}
		
		Long idFornecedor = edicao.getProduto().getFornecedor().getId();
		 
		DescontoLogistica descontoLogistica = this.descontoLogisticaService.obterDescontoLogisticaVigente(
				Integer.parseInt( input.getTipoDesconto()),idFornecedor,
		        DateUtil.parseData(input.getDataGeracaoArq(), FORMATO_DATA));
		
		if(descontoLogistica == null) {
			
			if(input.getTipoDesconto() != null && !input.getTipoDesconto().trim().equals("") 
					&& !input.getTipoDesconto().trim().equals("0") && !input.getTipoDesconto().trim().equals("00")) {
				
            this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.ERRO_INFRA,
					"Desconto Logística não encontrado. "+Integer.parseInt( input.getTipoDesconto()));
			}
			 
		} else if(produto.getDescontoLogistica() != null 
				&& !produto.getDescontoLogistica().getTipoDesconto().equals(descontoLogistica.getTipoDesconto())) {
			
			edicao.setDescontoLogistica(descontoLogistica);
			
            this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Desconto Logística"
					+" de "+ produto.getDescontoLogistica().getTipoDesconto()
					+" para "+ descontoLogistica.getTipoDesconto().intValue()
					+" Produto "+ produto.getCodigo()
					+" Edição "+ input.getEdicaoProd());
		} else if(produto.getDescontoLogistica() == null) {
			
			edicao.setDescontoLogistica(descontoLogistica);
			
            this.ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Desconto Logística"
					+" de nulo/vazio"
					+" para "+ descontoLogistica.getTipoDesconto().intValue()
					+" Produto "+ produto.getCodigo()
					+" Edição "+ input.getEdicaoProd());
		}
		
		if ( null != edicao.getCodigoNBM() && !edicao.getCodigoNBM().equals(input.getCodNBM()) && !input.getCodNBM().equals(ZEROS_NBM)) {
			
			this.ndsiLoggerFactory.getLogger().logInfo(message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Alteração do Código NBM"
					+" de " + edicao.getCodigoNBM()
					+" para " + input.getCodNBM()
					+" Produto "+ input.getCodProd()
					+" Edição "+ input.getEdicaoProd());
			
			edicao.setCodigoNBM(input.getCodNBM());
		}
		
		//this.getSession().merge(produto);
		this.getSession().merge(edicao);
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}