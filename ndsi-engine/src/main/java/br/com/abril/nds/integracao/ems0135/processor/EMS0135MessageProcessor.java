package br.com.abril.nds.integracao.ems0135.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
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
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0135Input;
import br.com.abril.nds.integracao.model.canonic.EMS0135InputItem;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.integracao.service.ParametroSistemaService;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoParametroSistema;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.ItemNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0135MessageProcessor extends AbstractRepository implements MessageProcessor {
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;

	@Autowired
	private FornecedorRepository fornecedorRepository;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		tempVar.set(new ArrayList<NotaFiscalEntradaFornecedor>());
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0135Input input = (EMS0135Input) message.getBody();
	
		
		// Validar código do distribuidor:
		Distribuidor distribuidor = this.distribuidorService.obter();
		if(!distribuidor.getCodigoDistribuidorDinap().equals(
				input.getDistribuidor().toString())){			
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO, 
					"Código do distribuidor do arquivo não é o mesmo do arquivo.");
			return;
		}
				

		
		NotaFiscalEntradaFornecedor notafiscalEntrada = obterNotaFiscal(
				input.getNotaFiscal()
				, input.getSerieNotaFiscal()
				);		
		
		if(notafiscalEntrada == null){
			
			notafiscalEntrada = new NotaFiscalEntradaFornecedor();
			
			notafiscalEntrada = populaNotaFiscalEntrada(notafiscalEntrada, input);						
			notafiscalEntrada = populaItemNotaFiscalEntrada(notafiscalEntrada, input, message);			
			notafiscalEntrada = calcularValores(notafiscalEntrada);
			
			if (null != notafiscalEntrada) {
				this.getSession().persist(notafiscalEntrada);
			} 
			
		}else{
			// Validar código do distribuidor:
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.REGISTRO_JA_EXISTENTE, 
						String.format("Nota Fiscal %1$s já cadastrada", notafiscalEntrada.getNumero()));
				return;			
		}		
	}
	
	private NotaFiscalEntradaFornecedor populaNotaFiscalEntrada(NotaFiscalEntradaFornecedor notafiscalEntrada, EMS0135Input input) {
		
		notafiscalEntrada.setDataEmissao(input.getDataEmissao());
		notafiscalEntrada.setNumero(input.getNotaFiscal().longValue());
		notafiscalEntrada.setSerie(input.getSerieNotaFiscal());		
		notafiscalEntrada.setDataExpedicao(input.getDataEmissao());		
		notafiscalEntrada.setChaveAcesso(input.getChaveAcessoNF());		
		notafiscalEntrada.setCfop(obterCFOP());
		notafiscalEntrada.setOrigem(Origem.INTERFACE);
		notafiscalEntrada.setStatusNotaFiscal(StatusNotaFiscalEntrada.NAO_RECEBIDA);
		
		notafiscalEntrada.setValorBruto(BigDecimal.ZERO);
		notafiscalEntrada.setValorLiquido(BigDecimal.ZERO);
		notafiscalEntrada.setValorDesconto(BigDecimal.ZERO);
		
		List<Fornecedor> listafornecedores = fornecedorRepository.obterFornecedoresPorIdPessoa(Long.valueOf( parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.ID_PJ_IMPORTACAO_NRE).getValor()));
		
		Fornecedor fornecedor = null;
		if (listafornecedores != null && !listafornecedores.isEmpty()) {
			fornecedor = listafornecedores.get(0);
		}
		
		notafiscalEntrada.setFornecedor(fornecedor);		
		notafiscalEntrada.setTipoNotaFiscal(obterTipoNotaFiscal(GrupoNotaFiscal.RECEBIMENTO_MERCADORIAS));		
		notafiscalEntrada.setEmitida(true);	
				
		return notafiscalEntrada;		
	}
	
	private NotaFiscalEntradaFornecedor populaItemNotaFiscalEntrada(NotaFiscalEntradaFornecedor nfEntrada, EMS0135Input input, Message message) {
		
		
		List<EMS0135InputItem> items =  input.getItems();
		boolean ItemNotFound = false;

		for(EMS0135InputItem imputItem : items) {
					

			final String codigoProduto = imputItem.getCodigoProduto();
			final Long edicao = imputItem.getEdicao();
			ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
					edicao);
			if (produtoEdicao == null) {
				
				// Validar código do distribuidor:
				this.ndsiLoggerFactory.getLogger().logWarning(message,
						EventoExecucaoEnum.HIERARQUIA, 
						String.format( "Produto %1$s / edicao %2$s não cadastrado. A nota  %3$s não será Inserida", codigoProduto , edicao.toString(), nfEntrada.getNumero().toString() )
						) ;
				ItemNotFound = true;
			}	else {					
			
				ItemNotaFiscalEntrada item = new ItemNotaFiscalEntrada();
		
				item.setQtde(new BigInteger(imputItem.getQtdExemplar().toString()));
				item.setNotaFiscal(nfEntrada);
				item.setProdutoEdicao(produtoEdicao);
				item.setPreco( BigDecimal.valueOf( imputItem.getPreco() ));
				item.setDesconto( BigDecimal.valueOf( imputItem.getDesconto() ));
				
				Lancamento lancamento = obterLancamentoProdutoEdicao(produtoEdicao.getId());
				item.setDataLancamento(lancamento.getDataLancamentoPrevista());
				item.setDataRecolhimento(lancamento.getDataRecolhimentoPrevista());
				nfEntrada.getItens().add(item);
			}
		}
		
		if (ItemNotFound) {
			return null;				
		}
		
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

	private NotaFiscalEntradaFornecedor obterNotaFiscal(Long numero, String serie) {
		StringBuilder hql = new StringBuilder();

		PessoaJuridica emitente = new PessoaJuridica();
		emitente.setId(	Long.valueOf( parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.ID_PJ_IMPORTACAO_NRE).getValor() ) );
		
		hql.append("from NotaFiscalEntradaFornecedor nf ")
			.append("where nf.numero = :numero ")
			.append("and nf.serie = :serie ")
			.append("and nf.emitente = :emitente ");
		
		Query query = super.getSession().createQuery(hql.toString());
		query.setParameter("numero", numero);
		query.setParameter("serie", serie);
		query.setParameter("emitente",  emitente);
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

	private TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal group by tipoNotaFiscal.id  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
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
