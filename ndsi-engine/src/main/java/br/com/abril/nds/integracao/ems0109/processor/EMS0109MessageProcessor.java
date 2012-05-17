package br.com.abril.nds.integracao.ems0109.processor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0109.inbound.EMS0109Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.EventoExecucaoEnum;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;

@Component
public class EMS0109MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Override
	public void processMessage(Message message) {
		
		try { 
			
			EMS0109Input input = (EMS0109Input) message.getBody();
			
			Integer codigoDistribuidor = input.getCodigoDistribuidor();
			Integer codigoPublicacao = input.getCodigoPublicacao();
			
			boolean logUpdate = false;
			
			if (verificarDistribuidor(codigoDistribuidor)) {
				
				Editor editor = this.obterEditorByID(input.getCodigoEditor());
				
				if (editor == null) {
					this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Editor nao encontrato.");
					throw new RuntimeException("Editor nao encontrado.");
				}
				
				Produto produto = this.obterProdutoPorPublicacao(codigoPublicacao);
				
				if (produto != null) {
	
					produto = this.criarProdutoConformeInput(produto, input);
					
					this.entityManager.merge(produto);
					
					logUpdate = true;
					
				} else {
					
					produto = this.criarProdutoConformeInput(produto, input);
					
					this.entityManager.persist(produto);
				}
				
			} else {
	
				this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.RELACIONAMENTO, "Distribuidor nao encontrato.");
				throw new RuntimeException("Distribuidor incorreto.");
			}
			
			if (logUpdate) {
				// TODO: log das informacoes atualizadas.
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Produto criarProdutoConformeInput(Produto produto, EMS0109Input input) {
		
		if (produto == null) {
			produto = new Produto();
		}
		
		produto.setCodigoContexto(input.getContextoPublicacao());
		
		// FIXME: descobrir qual e o codigo do fornecedor.
		
		// FIXME: assim que alterado o campo de string para integer, retirar o toString().
		produto.setCodigo(input.getCodigoPublicacao().toString());
		produto.setDescricao(input.getNomePublicacao());	
		
		// FIXME: campos que nao foram criados ainda.
		// produto.setSlogan(input.getSlogan());
		// produto.setStatus(input.getStatus());
		// produto.setDataDesativacao(input.getDataDesativacao()); 
		// produto.setCodigoEditor(input.getCodigoEditor());
		// produto.setTipoDesconto(input.getTipoDesconto());
				
		// TODO: verificar como sera inserido a periodicidade, pois e um ENUM e do arquivo vem apenas 3 caracteres.		
		String periodicidadeArquivo = input.getPeriodicidade();
		produto.setPeriodicidade(PeriodicidadeProduto.SEMANAL);

		return produto;
	}
	
	private boolean verificarDistribuidor(Integer codigoDistribuidor) {
		
		Distribuidor distribuidor = this.obterDistribuidor();
		
		if (distribuidor != null && distribuidor.getCodigo().equals(codigoDistribuidor)) {
			return true;
		}
		
		return false;
	}
	
	private Produto obterProdutoPorPublicacao(Integer codigoPublicacao) {
		
		String sql = "select produto from Produto produto ";
		sql += " where produto.codigo = :codigoPublicacao ";
		
		try {
		
			Query query = this.entityManager.createQuery(sql);
			
			query.setParameter("codigoPublicacao", codigoPublicacao);
			
			return (Produto) query.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private Distribuidor obterDistribuidor() {
		
		return this.distribuidorService.findDistribuidor();
	}
	
	private Editor obterEditorByID(Long codigoEditor) {
		
		String sql = "select editor from Editor editor ";
		sql += " where editor.codigo = :codigoEditor ";
		
		try {
			
			Query query = this.entityManager.createQuery(sql);
			
			query.setParameter("codigoEdito", codigoEditor);
			
			return (Editor) query.getSingleResult();
			
		} catch (NoResultException e) {	
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
		
	private void logInformacoesAtualizadas(Message message, Produto produto) {

		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Contexto Publicacao");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "Codigo Fornecedor Publicacao");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
		this.ndsiLoggerFactory.getLogger().logError(message, EventoExecucaoEnum.INF_DADO_ALTERADO, "");
	}
}
