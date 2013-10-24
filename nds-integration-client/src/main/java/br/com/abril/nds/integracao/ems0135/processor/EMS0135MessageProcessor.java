package br.com.abril.nds.integracao.ems0135.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0135Input;
import br.com.abril.nds.integracao.model.canonic.EMS0135InputItem;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.PeriodicidadeProduto;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoUsuarioNotaFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;

@Component
public class EMS0135MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		tempVar.set(new ArrayList<NotaFiscalEntradaFornecedor>());
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0135Input input = (EMS0135Input) message.getBody();
	
		// Validar código do distribuidor:
//		Distribuidor distribuidor = this.distribuidorService.obter();
//		if(!distribuidor.getCodigoDistribuidorDinap().equals(
//				input.getDistribuidor().toString())){			
//			this.ndsiLoggerFactory.getLogger().logWarning(message,
//					EventoExecucaoEnum.RELACIONAMENTO, 
//					"Código do distribuidor do arquivo não é o mesmo do arquivo.");
//			return;
//		}

		NotaFiscalEntradaFornecedor notafiscalEntrada = null;

		// Atualização por chave de acesso NFE
		if (input.getChaveAcessoNF() != null && !input.getChaveAcessoNF().isEmpty()) {
			
			if (input.getNumeroNotaEnvio() == null || input.getNumeroNotaEnvio().isEmpty()) {
				this.ndsiLoggerFactory.getLogger().logInfo(message, 
						EventoExecucaoEnum.RELACIONAMENTO, 
						String.format("Numero da nota de envio se encontra vazio para chave de acesso " + input.getChaveAcessoNF() + ". Nenhum registro será atualizado ou inserido!"));
				return;
			}
			
			notafiscalEntrada = obterNotaFiscalPorNumeroNotaEnvio(input.getNumeroNotaEnvio());
			
			// Caso encontre a nota fiscal de entrada, atualiza com a nova chave de acesso
			if (notafiscalEntrada != null) {
				String chaveAcessoAntiga = notafiscalEntrada.getChaveAcesso(); 
				notafiscalEntrada.setChaveAcesso(input.getChaveAcessoNF());
				notafiscalEntrada.setNumero(input.getNotaFiscal());
				notafiscalEntrada.setSerie(input.getSerieNotaFiscal());
				this.getSession().merge(notafiscalEntrada);
				this.ndsiLoggerFactory.getLogger().logInfo(message, 
						EventoExecucaoEnum.INF_DADO_ALTERADO, 
						String.format("Nota Fiscal de Entrada " + input.getNumeroNotaEnvio() + " atualizada com chave de acesso NFE de " + chaveAcessoAntiga + " para " + input.getChaveAcessoNF() + " com sucesso!"));
				return;
			}
		}

		notafiscalEntrada = obterNotaFiscal(
				input.getNotaFiscal()
				, input.getSerieNotaFiscal()
				, input.getCnpjEmissor()
				, input.getNumeroNotaEnvio()
			);		
			
		if(notafiscalEntrada == null){
			
			notafiscalEntrada = new NotaFiscalEntradaFornecedor();
			
			notafiscalEntrada = populaNotaFiscalEntrada(notafiscalEntrada, input);						
			notafiscalEntrada = populaItemNotaFiscalEntrada(notafiscalEntrada, input, message);		
			if (null != notafiscalEntrada) {
				notafiscalEntrada = calcularValores(notafiscalEntrada);				
				this.getSession().persist(notafiscalEntrada);
				
				this.ndsiLoggerFactory.getLogger().logInfo(
							message
							, EventoExecucaoEnum.SEM_DOMINIO
							, String.format("Nota Fiscal inserida no sistema: %1$s", input.getNotaFiscal()));
				
			} else {
				
				String msg = "Nota fiscal com produtos não encontrados no sistema";
				if(input.getNotaFiscal() != null && input.getNotaFiscal() > 0){
					msg = String.format("Nota fiscal com produtos não encontrados no sistema, número nota: %1$s", input.getNotaFiscal());
				}
				
				// Validar código do distribuidor:
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.RELACIONAMENTO, msg);
				return;		
			}
			
		} else {
			// Validar código do distribuidor:
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.REGISTRO_JA_EXISTENTE, 
						String.format("Nota Fiscal %1$s já cadastrada com serie %2$s e nota envio %3$s", notafiscalEntrada.getNumero(), notafiscalEntrada.getSerie(), notafiscalEntrada.getNumeroNotaEnvio()));
				return;			
		}		
	}
	
	private NotaFiscalEntradaFornecedor populaNotaFiscalEntrada(NotaFiscalEntradaFornecedor notafiscalEntrada, EMS0135Input input) {
		
		notafiscalEntrada.setDataEmissao(input.getDataEmissao());
		notafiscalEntrada.setNumero( input.getNotaFiscal() != null ? input.getNotaFiscal().longValue() : 0L );
		notafiscalEntrada.setSerie( input.getSerieNotaFiscal() != null && !input.getSerieNotaFiscal().isEmpty() && !"0".equals(input.getSerieNotaFiscal()) ? input.getSerieNotaFiscal() : "0" );		
		notafiscalEntrada.setDataExpedicao(input.getDataEmissao());		
		notafiscalEntrada.setChaveAcesso( input.getChaveAcessoNF() != null && !input.getChaveAcessoNF().isEmpty() && !"0".equals(input.getChaveAcessoNF()) ? input.getChaveAcessoNF() : "0" );		
		notafiscalEntrada.setCfop(obterCFOP());
		notafiscalEntrada.setOrigem(Origem.INTERFACE);
		notafiscalEntrada.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
		notafiscalEntrada.setNumeroNotaEnvio(Long.parseLong(input.getNumeroNotaEnvio()));
		
		notafiscalEntrada.setValorBruto(BigDecimal.ZERO);
		notafiscalEntrada.setValorLiquido(BigDecimal.ZERO);
		notafiscalEntrada.setValorDesconto(BigDecimal.ZERO);
		
		PessoaJuridica emitente = this.obterPessoaJuridica( input.getCnpjEmissor() );
		
// Comentado pelo Cesar "PunkPop" no dia de hoje :-)		
//		PessoaJuridica emitente = this.obterPessoaJuridica( parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.CNPJ_PJ_IMPORTACAO_NRE).getValor() );
		
		notafiscalEntrada.setEmitente(emitente);
		//Alteracao autorizada pelo Eduardo
		//notafiscalEntrada.setTipoNotaFiscal(obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS));		
		notafiscalEntrada.setTipoNotaFiscal(obterTipoNotaFiscal(GrupoNotaFiscal.NF_REMESSA_MERCADORIA_CONSIGNACAO, TipoUsuarioNotaFiscal.TREELOG, TipoUsuarioNotaFiscal.DISTRIBUIDOR));		
		notafiscalEntrada.setEmitida(true);	
				
		return notafiscalEntrada;		
	}
	
	private NotaFiscalEntradaFornecedor populaItemNotaFiscalEntrada(NotaFiscalEntradaFornecedor nfEntrada, EMS0135Input input, Message message) {
		
		
		List<EMS0135InputItem> items =  input.getItems();
//		boolean ItemNotFound = false;

		for(EMS0135InputItem imputItem : items) {
					

			final String codigoProduto = imputItem.getCodigoProduto();
			final Long edicao = imputItem.getEdicao();
			ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto, edicao);
			if (produtoEdicao == null) {
				
				// Validar código do distribuidor:
//				this.ndsiLoggerFactory.getLogger().logError(message,
//						EventoExecucaoEnum.HIERARQUIA, 
//						String.format( "Produto %1$s / edicao %2$s não cadastrado. A nota  %3$s não será Inserida", codigoProduto , edicao.toString(), nfEntrada.getNumero().toString() )
//						) ;
//				ItemNotFound = true;
				
				//Trac 784
				Produto produto = this.produtoRepository.obterProdutoPorCodigo(codigoProduto);
				
				if (produto == null){
					
					TipoProduto tipoProduto = this.tipoProdutoRepository.buscarPorId(1L);
					
					produto = new Produto();
					produto.setCodigo(imputItem.getCodigoProduto());
					produto.setPeriodicidade(PeriodicidadeProduto.MENSAL);
					produto.setNome(imputItem.getNomeProduto());
					produto.setOrigem(Origem.MANUAL);
					produto.setTipoProduto(tipoProduto);
					produto.setPacotePadrao(10);
					produto.setPeb(35);
					produto.setPeso(100L);
					this.getSession().persist(produto);
				}
				
				produtoEdicao = new ProdutoEdicao();
				produtoEdicao.setProduto(produto);
				produtoEdicao.setNumeroEdicao(imputItem.getEdicao());
				produtoEdicao.setPacotePadrao(10);
				produtoEdicao.setPeb(35);
				produtoEdicao.setPeso(100L);
				produtoEdicao.setPossuiBrinde(false);
				produtoEdicao.setPermiteValeDesconto(false);
				produtoEdicao.setParcial(true);
				produtoEdicao.setAtivo(true);
				produtoEdicao.setOrigem(Origem.PRODUTO_SEM_CADASTRO);
				this.getSession().persist(produtoEdicao);
				
				Date dataAtual = new Date();
				Date dataPrevista = DateUtil.adicionarDias(dataAtual, produto.getPeb());
				Lancamento lancamento = new Lancamento();
				lancamento.setDataCriacao(dataAtual);
				lancamento.setDataLancamentoPrevista(dataAtual);
				lancamento.setDataLancamentoDistribuidor(dataAtual);
				lancamento.setDataRecolhimentoPrevista(dataPrevista);
				lancamento.setDataRecolhimentoDistribuidor(dataPrevista);
				lancamento.setProdutoEdicao(produtoEdicao);
				lancamento.setTipoLancamento(TipoLancamento.NORMAL);
				lancamento.setDataStatus(dataAtual);
				lancamento.setStatus(StatusLancamento.CONFIRMADO);
				lancamento.setReparte(new BigInteger(imputItem.getQtdExemplar().toString()));
				this.getSession().persist(lancamento);
			}
			
			ItemNotaFiscalEntrada item = new ItemNotaFiscalEntrada();
	
			item.setQtde(new BigInteger(imputItem.getQtdExemplar().toString()));
			item.setNotaFiscal(nfEntrada);
			item.setProdutoEdicao(produtoEdicao);
			item.setPreco( BigDecimal.valueOf( imputItem.getPreco() ));
			item.setDesconto( BigDecimal.valueOf( imputItem.getDesconto() ));
			
			Lancamento lancamento = obterLancamentoProdutoEdicao(produtoEdicao.getId());
			if (null == lancamento) {
				Calendar cal = Calendar.getInstance();
				
				cal.add(Calendar.DAY_OF_MONTH, 2);					
				item.setDataLancamento(cal.getTime());
				
				cal.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
				item.setDataRecolhimento(cal.getTime());
				
				item.setTipoLancamento(TipoLancamento.LANCAMENTO);

			} else {					
				item.setDataLancamento(lancamento.getDataLancamentoPrevista());
				item.setDataRecolhimento(lancamento.getDataRecolhimentoPrevista());
				item.setTipoLancamento(lancamento.getTipoLancamento());
			}
			nfEntrada.getItens().add(item);
		}
		
//		if (ItemNotFound) {
//			return null;				
//		}
		
		return nfEntrada;
	}
	
	/**
	 * Realiza os cálculos de valores da Nota Fiscal. Após os cálculos, salva
	 * os novos valores. 
	 * 
	 * @param nfEntrada
	 * @param input
	 */
	private NotaFiscalEntradaFornecedor calcularValores(NotaFiscalEntradaFornecedor nfEntrada) {
		
		BigDecimal valorBruto = this.calcularValorBruto(nfEntrada);
		nfEntrada.setValorBruto(valorBruto);
		
		BigDecimal valorLiquido = this.calcularValorLiquido(nfEntrada);
		nfEntrada.setValorLiquido(valorLiquido);
		
		BigDecimal valorDesconto = valorBruto.subtract(valorLiquido);
		nfEntrada.setValorDesconto(valorDesconto);
		
		return nfEntrada;
	}
	
	/**
	 * Método que contém as regras para o cálculo do "Valor Bruto" de uma NF.
	 * 
	 * @param nfEntrada
	 * @param input
	 * @return
	 */
	private BigDecimal calcularValorBruto(NotaFiscalEntradaFornecedor nfEntrada) {
						
		BigDecimal valorBrutoTotal = nfEntrada.getValorBruto();

		for(ItemNotaFiscalEntrada item : nfEntrada.getItens()) {
					
			BigDecimal valorBrutoItem = item.getPreco()
					.multiply( BigDecimal.valueOf( item.getQtde().doubleValue() ));
			
			valorBrutoTotal = valorBrutoTotal.add(valorBrutoItem);
			
		}
				
		return valorBrutoTotal;
	}
	
	/**
	 * Método que contém as regras para o cálculo do "Valor Líquido" de uma NF.
	 * 
	 * @param nfEntrada
	 * @param input
	 * 
	 * @return
	 */
	private BigDecimal calcularValorLiquido(NotaFiscalEntradaFornecedor nfEntrada) {
		
		BigDecimal valorLiquidoTotal = nfEntrada.getValorLiquido();
		
		for(ItemNotaFiscalEntrada item : nfEntrada.getItens()) {
			
			BigDecimal valorDesconto = item.getPreco().multiply( item.getDesconto());
			
			BigDecimal valorLiquidoItem = item.getPreco().subtract(valorDesconto);
			
			BigDecimal valorTotalItem = valorLiquidoItem.multiply( BigDecimal.valueOf( item.getQtde().doubleValue() ));
			
			valorLiquidoTotal = valorLiquidoTotal.add(valorTotalItem);
		
		}
		
		return valorLiquidoTotal;
	}
	
	private BigDecimal calcularValorUnitario() {
		return BigDecimal.ZERO;
	}
	
	
	private Lancamento obterLancamentoProdutoEdicao(Long idProdutoEdicao) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" select lancamento ")
		   .append(" from Lancamento lancamento ")
		   .append(" where lancamento.dataLancamentoDistribuidor = ")
		   .append(" (select max(lancamentoMaxDate.dataLancamentoDistribuidor) ")
		   .append(" from Lancamento lancamentoMaxDate where lancamentoMaxDate.produtoEdicao.id=:idProdutoEdicao ) ")
		   .append(" and lancamento.produtoEdicao.id=:idProdutoEdicao ");
		
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		Object lancamento = query.uniqueResult();
		
		return (lancamento!=null) ? (Lancamento) lancamento : null ;
		
	}

	private NotaFiscalEntradaFornecedor obterNotaFiscal(Long numero, String serie, String cnpjEmissor, String numeroNotaEnvio) {
		StringBuilder hql = new StringBuilder();

		boolean existeNotaFiscal = (numero != null && !numero.equals(0L) && serie != null && !serie.isEmpty() && !serie.equals("0"));
		
		PessoaJuridica emitente = this.obterPessoaJuridica( cnpjEmissor );		
				
		hql.append("from NotaFiscalEntradaFornecedor nf ");
		hql.append("where nf.emitente = :emitente ");

		if (existeNotaFiscal) {
			hql.append("and nf.numero = :numero and nf.serie = :serie");
		} else {
			hql.append("and nf.numeroNotaEnvio = :numeroNotaEnvio ");
		}


		Query query = super.getSession().createQuery(hql.toString());

		query.setParameter("emitente",  emitente);

		if (existeNotaFiscal) {
			if ( numero != null && !numero.equals(0L) ) {
				query.setParameter("numero", numero);
			}
			
			if ( serie != null && !serie.isEmpty() && !serie.equals("0")) {
				query.setParameter("serie", serie);
			}
		} else {
			query.setParameter("numeroNotaEnvio",  Long.parseLong(numeroNotaEnvio) );
		}
		
		return (NotaFiscalEntradaFornecedor) query.uniqueResult();
		
	}

	/**
	 * Obtém a nota fiscal de entrada do fornecedor através do campo numeroNotaEnvio caso exista uma chaveAcesso
	 * @param numeroNotaEnvio
	 * @return
	 */
	private NotaFiscalEntradaFornecedor obterNotaFiscalPorNumeroNotaEnvio(String numeroNotaEnvio) {
		StringBuilder hql = new StringBuilder();

		hql.append("from NotaFiscalEntradaFornecedor nf ")
			.append("where nf.numeroNotaEnvio = :numeroNotaEnvio ");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("numeroNotaEnvio", Long.parseLong(numeroNotaEnvio));
		return (NotaFiscalEntradaFornecedor) query.uniqueResult();
		
	}
	
	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal, TipoUsuarioNotaFiscal emitente, TipoUsuarioNotaFiscal destinatario) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal " +
						"and tipoNotaFiscal.emitente = :emitente and tipoNotaFiscal.destinatario = :destinatario " +
						" group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		query.setParameter("emitente", emitente);
		query.setParameter("destinatario", destinatario);
		
		return (TipoNotaFiscal) query.uniqueResult();
	}

	private PessoaJuridica obterPessoaJuridica(String cnpj) {

		String hql = "from PessoaJuridica where cnpj = :cnpj";

		Query query = getSession().createQuery(hql);

		query.setParameter("cnpj", cnpj);

		return (PessoaJuridica) query.uniqueResult();
	}

	private CFOP obterCFOP() {
		 
		Criteria criteria =  getSession().createCriteria(CFOP.class);	

		criteria.add(Restrictions.eq("codigo", "5102"));
	
		return (CFOP) criteria.uniqueResult();
	}


	/**
	 * Normaliza uma data, para comparações, zerando os valores de hora (hora,
	 * minuto, segundo e milissendo).
	 * 
	 * @param dt
	 * 
	 * @return
	 */
	private Date normalizarDataSemHora(Date dt) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
