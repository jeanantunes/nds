package br.com.abril.nds.integracao.ems0119.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0119Input;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.FormaComercializacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0119MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;

	private String codigoDinap;

	public EMS0119MessageProcessor() {

	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		codigoDinap =distribuidorService.codigoDistribuidorDinap();
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
			
			if (produto.getFormaComercializacao()==null) {
				produto.setFormaComercializacao(FormaComercializacao.CONSIGNADO);
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Forma de Comercialização para: "
								+ FormaComercializacao.CONSIGNADO.name());
			}
			
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
					.equals( PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()) )) {
				produto.setPeriodicidade(
						PeriodicidadeProduto.getByOrdem(input.getPeriodicidade())
								);
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao da Periodicidade para: "
								+ PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()) );
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
			
			if(produto.getEditor() == null) {
				
				Editor ed = this.getEditor(input.getCodigoDoEditor());
				
				if(ed != null) {
					produto.setEditor(ed);
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualização do Codigo do Editor para "
									+ input.getCodigoDoEditor());
				}
				
			} else if (produto.getEditor().getCodigo() != input
					.getCodigoDoEditor()) {
				
				Editor ed = this.getEditor(input.getCodigoDoEditor());
						
				if (null == ed) {
					ndsiLoggerFactory.getLogger().logWarning(
							message,
							EventoExecucaoEnum.RELACIONAMENTO,
							String.format( "Editor %1$s não encontrado para o Produto %2$s ", input.getCodigoDoEditor().toString(), input.getCodigoDaPublicacao() )
						);
				} else {
					produto.setEditor(ed);
					ndsiLoggerFactory.getLogger().logInfo(
							message,
							EventoExecucaoEnum.INF_DADO_ALTERADO,
							"Atualização do Codigo do Editor para "
									+ input.getCodigoDoEditor());
					
				}				
			}
			if (produto.getPacotePadrao() != input.getPacotePadrao()) {
				produto.setPacotePadrao(input.getPacotePadrao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualização do Pacote Padrão para "
								+ input.getPacotePadrao());

			}

			if (produto.getNomeComercial() != input.getNomeComercial()) {
				produto.setNomeComercial(input.getNomeComercial());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualização do Nome Comercial para "
								+ input.getNomeComercial());
			}

			if (produto.isAtivo() != input.getStatusDaPublicacao()) {
				produto.setAtivo(input.getStatusDaPublicacao());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualização do Status para "
								+ input.getPacotePadrao());

			}
			
			produto.setTipoSegmentoProduto(new TipoSegmentoProduto(9l));
			
			this.getSession().merge(produto);

		} else {
			
			produto = new Produto();
			produto.setCodigo(input.getCodigoDaPublicacao());
			produto.setNome(input.getNomeDaPublicacao());
			produto.setPeriodicidade(PeriodicidadeProduto.getByOrdem(input.getPeriodicidade()));
			produto.setPacotePadrao(input.getPacotePadrao());
			produto.setNomeComercial(input.getNomeComercial());
			produto.setAtivo(input.getStatusDaPublicacao());			
			//Default data
			produto.setPeso(0l);
			produto.setOrigem(Origem.MANUAL);
			produto.setFormaComercializacao(FormaComercializacao.CONSIGNADO);

			
			TipoProduto tp =  this.getTipoProduto(input.getTipoDePublicacao());
									
			if (null == tp) {
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						String.format( "Tipo de Produto %1$s não encontrado para o Produto %2$s ", input.getTipoDePublicacao().toString(), input.getCodigoDaPublicacao() )
					);
			} else {
				produto.setTipoProduto(tp);	
			}
			
			Editor ed = this.getEditor(input.getCodigoDoEditor());			
			if (null == ed) {
				ndsiLoggerFactory.getLogger().logWarning(
						message,
						EventoExecucaoEnum.RELACIONAMENTO,
						String.format( "Editor %1$s não encontrado para o Produto %2$s ", input.getCodigoDoEditor().toString(), input.getCodigoDaPublicacao() )
					);
			} else {
				produto.setEditor(ed);	
			}
			
			//Fornecedor fornecedor = this.findFornecedor(Integer.valueOf( input.getCodigoFornecedorPublic()));
			Fornecedor fornecedor = this.findFornecedor(Integer.valueOf(codigoDinap));
			
			if (fornecedor != null) {

				produto.addFornecedor(fornecedor);
			}

			//Tipo Segmento Default. ID Fixo.
			produto.setTipoSegmentoProduto(new TipoSegmentoProduto(9l));
			
			if ((null != tp) && (null != ed)) {
				this.getSession().persist(produto);
			}						
			
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
