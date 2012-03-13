package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalFornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/recebimentoFisico")
public class RecebimentoFisicoController {
	
	private Result result;

	private HttpServletRequest request;

	private static final String CABECALHO_NOTA_FISCAL = "cabecalhoNotaFiscal";
	
	private static final String ITENS_NOTA_FISCAL = "itensNotaFiscal";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;

	public RecebimentoFisicoController(
			Result result, 
			HttpServletRequest request,
			Validator validator) {
		this.result = result;
		this.request = request;
	}
	
	/**
	 * Direciona para a página de recebimento físico.
	 */
	public void index() {

		preencherCombos();
		
		preencherDataEmissao();
	
	}
	
	/**
	 * Inclúi a data atual para a view.
	 */
	private void preencherDataEmissao() {
		
		Date data = new Date(System.currentTimeMillis());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		result.include("dataAtual", sdf.format(data));

	}
	
	/**
	 * Método para prencher combo fornecedor com o cnpj informado.
	 * 
	 * @param cnpj
	 */
	@Post
	public void buscaCnpj(String cnpj){
		
		PessoaJuridica pessoaJuridica = pessoaJuridicaService.buscarPorCnpj(cnpj);
		
		if(pessoaJuridica != null){
			result.use(Results.json()).from(pessoaJuridica, "result").serialize();			 
		}else{
			
			throw new ValidacaoException(TipoMensagem.ERROR,"CNPJ não encontrado!");
			
		}
	}
	
	/**
	 * Serializa a listaItemRecebimentoFisico que esta em session recalculando os campos
	 * valorTotal e valorDiferenca.
	 */
	@Post
	public void refreshListaItemRecebimentoFisico() {
		
		if( getItensRecebimentoFisicoFromSession() == null) {
			setItensRecebimentoFisicoToSession(new LinkedList<RecebimentoFisicoDTO>());
		}
		
		recarregarValoresCalculados(getItensRecebimentoFisicoFromSession());
		
		TableModel<CellModel> tableModel =  obterTableModelParaListItensNotaRecebimento(getItensRecebimentoFisicoFromSession());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	/**
	 * Recalcula os campos valorTotal e valorDiferenca.
	 * 
	 * @param itensRecebimento
	 */
	private void recarregarValoresCalculados(List<RecebimentoFisicoDTO> itensRecebimento) {
		
		for(RecebimentoFisicoDTO item : itensRecebimento) {
			
			carregarValorTotal(item);
			
			carregarValorDiferenca(item);
			
		}
		
	}
	
		
	/**
	 * Faz a busca na base de dados da listaItemRecebimentoFisico e a serializa.
	 */
	@Post
	public void obterListaItemRecebimentoFisico() {
		
		NotaFiscal notaFiscal = getNotaFiscalFromSession();
		
		Long idNotaFiscal = notaFiscal.getId();
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico = recebimentoFisicoService.obterListaItemRecebimentoFisico(idNotaFiscal);
		
		if(itensRecebimentoFisico == null) {
			itensRecebimentoFisico = new LinkedList<RecebimentoFisicoDTO>();
		}
		
		recarregarValoresCalculados(itensRecebimentoFisico);
		
		setItensRecebimentoFisicoToSession(itensRecebimentoFisico);
		
		TableModel<CellModel> tableModel =  obterTableModelParaListItensNotaRecebimento(getItensRecebimentoFisicoFromSession());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	/**
	 * Valida os dados da nova Nota Fiscal.
	 * 
	 * @param cnpj
	 * @param numeroNotaFiscal
	 * @param serie
	 */
	private void validarDadosNotaFiscal(String cnpj, String numeroNotaFiscal, String serie) {
		
		List<String> msgs = new ArrayList<String>();
		
		if(cnpj == null || cnpj.isEmpty()) {
			msgs.add("O campo CNPJ é obrigatório");
		} else if(cnpj.length()<12) {
			msgs.add("O campo cnpj esta inválido");
		}
		
		if(numeroNotaFiscal == null || numeroNotaFiscal.isEmpty()) {
			msgs.add("O campo Nota Fiscal é obrigatório");
		}

		if(serie == null || serie.isEmpty()) {
			msgs.add("O campo Série é obrigatório");
		}

		if(!msgs.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
	}

	/**
	 * Serializa os dados do produtoEdicao pesquisado.
	 * 
	 * @param codigo
	 * @param edicao
	 */
	public void obterProdutoEdicao(String codigo, String edicao) {
		
		if(codigo!=null && !codigo.trim().isEmpty() && edicao != null) {
			
			ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigo, edicao);
		
			if(produtoEdicao!=null) {
				result.use(Results.json()).from(produtoEdicao, "result").serialize();
			}
			
		}
		
		result.use(Results.nothing());
		
	}
	
	/**
	 * Inclui um novo item nota fiscal e dados de recebimento físico.
	 * 
	 * @param itemRecebimento
	 * @param numeroEdicao
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param itensRecebimento
	 */
	@Post 
	public void incluirItemNotaFiscal(
			RecebimentoFisicoDTO itemRecebimento,
			String numeroEdicao,
			String dataLancamento, 
			String dataRecolhimento, 
			List<RecebimentoFisicoDTO> itensRecebimento) {
		
		atualizarItensRecebimentoEmSession(itensRecebimento);
		
		validarNovoItemRecebimentoFisico(
				itemRecebimento, 
				numeroEdicao,
				dataLancamento, 
				dataRecolhimento);
		try {
			itemRecebimento.setDataLancamento(sdf.parse(dataLancamento));
			itemRecebimento.setDataRecolhimento(sdf.parse(dataRecolhimento));
		} catch(ParseException e) {
			itemRecebimento.setDataLancamento(null);
			itemRecebimento.setDataRecolhimento(null);
		}
		
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(itemRecebimento.getCodigoProduto(), numeroEdicao);
		
		if(produtoEdicao == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Produto edição não existe.");
		}
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico =  getItensRecebimentoFisicoFromSession();
		
		if(itensRecebimentoFisico == null) {
			itensRecebimentoFisico = new LinkedList<RecebimentoFisicoDTO>();
			setItensRecebimentoFisicoToSession(itensRecebimentoFisico);
		}
		
		itemRecebimento.setOrigemItemNota(Origem.MANUAL);
		itemRecebimento.setEdicao(produtoEdicao.getNumeroEdicao());
		itemRecebimento.setIdProdutoEdicao(produtoEdicao.getId());
		
		validarProdutoEdicaoExistente(itemRecebimento, itensRecebimentoFisico);
		
		itensRecebimentoFisico.add(itemRecebimento);
		
		List<String> msgs = new ArrayList<String>();
		
		msgs.add("Item Nota fiscal adicionado com sucesso.");
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
	}

	/**
	 * Verifica se o novo item ja esta adicionado a nota fiscal que esta sendo editada. 
	 * 
	 * @param novoItemRecebimento
	 * @param itensRecebimentoAdicionadosANota
	 */
	private void validarProdutoEdicaoExistente(RecebimentoFisicoDTO novoItemRecebimento, List<RecebimentoFisicoDTO> itensRecebimentoAdicionadosANota) {
	
		for(RecebimentoFisicoDTO itemJaAdicionado : itensRecebimentoAdicionadosANota) {
			
			if(novoItemRecebimento.getIdProdutoEdicao().equals(itemJaAdicionado.getIdProdutoEdicao())) {
			
				throw new ValidacaoException(TipoMensagem.WARNING, "Produto edição já existente nesta nota fiscal.");
				
			}
			
		}
		
		
	}
	
	/**
	 * Recalcula o campo valorTotal do itemRecebimentoFisico.
	 * 
	 * @param itemRecebimento
	 */
	private void carregarValorTotal(RecebimentoFisicoDTO itemRecebimento) {
		
		BigDecimal qtdRepartePrevisto = itemRecebimento.getRepartePrevisto();
		
		BigDecimal precoCapa = itemRecebimento.getPrecoCapa();
		
		BigDecimal valorTotal = new BigDecimal(0.0D);
		
		if(qtdRepartePrevisto != null && precoCapa != null) {
			
			valorTotal = qtdRepartePrevisto.multiply(precoCapa);
		
		}
		
		itemRecebimento.setValorTotal(valorTotal);
		
	}
	
	/**
	 * Recalcula o campo valorDiferenca do itemRecebimentoFisico.
	 * 
	 * @param itemRecebimento
	 */
	private void carregarValorDiferenca(RecebimentoFisicoDTO itemRecebimento) {
		
		if(itemRecebimento.getRepartePrevisto() == null) {
			itemRecebimento.setRepartePrevisto(new BigDecimal("0.0"));
		}

		if(itemRecebimento.getQtdFisico() == null) {
			itemRecebimento.setQtdFisico(new BigDecimal("0.0"));
		}

		BigDecimal qtdRepartePrevisto = itemRecebimento.getRepartePrevisto();
		
		BigDecimal qtdFisico = itemRecebimento.getQtdFisico();
		
		BigDecimal valorDiferenca = qtdRepartePrevisto.subtract(qtdFisico);
		
		itemRecebimento.setDiferenca(valorDiferenca);
		
	}
	
	/**
	 * Exclui um item de nota fiscal da lista em session.
	 * 
	 * @param lineId
	 * @param itensRecebimento
	 */
	@Post
	public void excluirItemNotaFiscal(int lineId, List<RecebimentoFisicoDTO> itensRecebimento) {
		
		atualizarItensRecebimentoEmSession(itensRecebimento);
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico =  getItensRecebimentoFisicoFromSession();

		RecebimentoFisicoDTO apagarReceb = null;
		
		for(RecebimentoFisicoDTO recebimento : itensRecebimentoFisico) {
			
			if(recebimento.getLineId() == lineId) {
				apagarReceb = recebimento;
				break;
			}
			
		}
		
		itensRecebimentoFisico.remove(apagarReceb);
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Item Nota fiscal removido com sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
	}
	
	/**
	 * Atualiza o campo qtdFisico da lista de itemRecebimentoFisico que se encontra em session.
	 * 
	 * @param itensRecebimento
	 */
	private void atualizarItensRecebimentoEmSession(List<RecebimentoFisicoDTO> itensRecebimento) {
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisicoFromSession = getItensRecebimentoFisicoFromSession();
		
		if(itensRecebimentoFisicoFromSession == null) {
			return;
		}
		
		if(itensRecebimento != null) {
			
			for(RecebimentoFisicoDTO itemFromForm : itensRecebimento) {

				for(RecebimentoFisicoDTO itemFromSession : itensRecebimentoFisicoFromSession) {
					
					if(itemFromForm.equals(itemFromSession)) {
						
						itemFromSession.setQtdFisico(itemFromForm.getQtdFisico());
						
						break;
						
					}
					
				}
				
			}
			
		}		
	}
	
	/**
	 * Salva as alterações de recebimento físico realizadas em uma nota existente ou nota
	 * que esteja sendo criada.
	 *  
	 * @param itensRecebimento
	 */
	@Post
	public void salvarDadosItensDaNotaFiscal(List<RecebimentoFisicoDTO> itensRecebimento) {
		
		atualizarItensRecebimentoEmSession(itensRecebimento);
	
		//TODO: capturar usuario logado
		Usuario usuarioLogado = new Usuario();
		usuarioLogado.setId(1L);
		
		recebimentoFisicoService.inserirDadosRecebimentoFisico(usuarioLogado, getNotaFiscalFromSession(), getItensRecebimentoFisicoFromSession(), new Date());
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Itens salvos com sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
	}
	
	/**
	 * Limpa os dados mantidos em session.
	 */
	private void limparDadosDaSession() {
		
		setItensRecebimentoFisicoToSession(null);
		
		setNotaFiscalToSession(null);
		
	}
	
	/**
	 * Faz a pesquisa de uma nota fiscal através dos parâmetros de 
	 * CNPJ, numero da nota, série e chave de acesso caso a mesma
	 * seja uma nota fiscal eletrônica. Caso a nota seja encontrada
	 * o id interno da mesma será colocado na session.
	 * 
	 * @param cnpj
	 * @param numeroNotaFiscal
	 * @param serie
	 * @param chaveAcesso
	 */
	@Post
	public void verificarNotaFiscalExistente(String cnpj, String numeroNotaFiscal, String serie, String chaveAcesso) {

		validarDadosNotaFiscal(cnpj, numeroNotaFiscal, serie);
		
		if(chaveAcesso == null || chaveAcesso.trim().isEmpty()) {
			chaveAcesso = null;
		}
		
		limparDadosDaSession();
		
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		filtro.setCnpj(cnpj);
		filtro.setNumeroNota(numeroNotaFiscal);
		filtro.setSerie(serie);
		filtro.setChave(chaveAcesso);
		
		List<NotaFiscal> listaNotaFiscal = notaFiscalService.obterNotaFiscalPorNumeroSerieCnpj(filtro);
		
			
		if(listaNotaFiscal != null && listaNotaFiscal.size()>1) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Mais de uma nota fiscal cadastrada com estes valores.");
		} 
		
		NotaFiscal notaFiscal = null;
		
		if(listaNotaFiscal != null && !listaNotaFiscal.isEmpty()) {
			notaFiscal = listaNotaFiscal.get(0);
		} 
		
		if(notaFiscal == null){	

			List<String> msgs = new ArrayList<String>();
			
			msgs.add("Nota fiscal não encontrada");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, msgs);
			
			result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
		} else {
			
			setNotaFiscalToSession(notaFiscal);
			
			List<String> msgs = new ArrayList<String>();
			
			msgs.add("Nota fiscal encontrada com sucesso");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
			
			result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();

		}
				
	}
	
	
	/**
	 * Obtem um tableModel com os dados da lista de itens de recebimento fisico.
	 * 
	 * @param listaExtratoEdicao
	 * 
	 * @return TableModel.
	 */
	private TableModel<CellModel> obterTableModelParaListItensNotaRecebimento(List<RecebimentoFisicoDTO> itensRecebimentoFisico) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		int counter = 0;
		
		for(RecebimentoFisicoDTO dto : itensRecebimentoFisico) {
			
			dto.setLineId(counter++);
			
			String codigo 		     	 = dto.getCodigoProduto();
			String nomeProduto 	     	 = dto.getNomeProduto();
			String edicao 		     	 = (dto.getEdicao() 				== null) 	? "" 	: dto.getEdicao().toString();
			String precoCapa 	     	 = (dto.getPrecoCapa() 			== null) 	? "0.0" : dto.getPrecoCapa().toString();
			String repartePrevisto 	 	 = (dto.getRepartePrevisto() 	== null) 	? "0.0" : dto.getRepartePrevisto().toString();
			String qtdeFisica		 	 = (dto.getQtdFisico() 			== null) 	? "0.0" : dto.getQtdFisico().toString();
			String diferenca		 	 = (dto.getDiferenca() 			== null) 	? "0.0" : dto.getDiferenca().toString();
			String valorTotal		 	 = (dto.getValorTotal() 			== null) 	? "0.0" : dto.getValorTotal().toString() ;
			String alteracaoPermitida	 = (Origem.MANUAL.equals(dto.getOrigemItemNota())) ? "S" : "N";
			String destacarValorNegativo = (dto.getDiferenca() != null && dto.getDiferenca().doubleValue() < 0.0D) ? "S" : "N";
			
			listaModeloGenerico.add(
					new CellModel( 	
							dto.getLineId(), 
							codigo, 
							nomeProduto, 
							edicao, 
							precoCapa, 
							repartePrevisto,
							qtdeFisica,
							diferenca,
							valorTotal,
							alteracaoPermitida,
							destacarValorNegativo
					));
			
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
	/**
	 * Inclui na view os dados do combo de CFOP.
	 */
	private void carregarComboCfop() {
		
		List<CFOP> listaCFOP = recebimentoFisicoService.obterListaCFOP();
		
		if (listaCFOP != null) {
			result.include("listacfop", listaCFOP);
		}
		
	}

	/**
	 * Inclui na view os dados do combo de TipoNotaFiscal.
	 */
	private void carregarComboTipoNotaFiscal() {
		
		List<TipoNotaFiscal> listaTipoNotaFiscal = recebimentoFisicoService.obterTiposNotasFiscais(TipoOperacao.ENTRADA);
		
		if (listaTipoNotaFiscal != null) {
			result.include("listaTipoNotaFiscal", listaTipoNotaFiscal);
		}
		
	}
	
	/**
	 * Inclui na view os dados do combo de Fornecedor.
	 */
	private void carregarComboFornecedor() {
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedoresAtivos();
		
		if (fornecedores != null) {
			result.include("listafornecedores", fornecedores);
		}
		
	}
	
	/**
	 * Inclui na view os dados do combo TipoLancamento.
	 */
	private void carregarComboTipoLancamento() {
		
		List<String> listaTipoLancamento = new ArrayList<String>();
		
		for(TipoLancamento obj: TipoLancamento.values()){
			
			listaTipoLancamento.add(obj.name());
			
		}
		
		result.include("listaTipoLancamento",listaTipoLancamento);
		
	}
	
	/**
	 * Inclui na view dados dos combos.
	 */
	public void preencherCombos() {
		
		carregarComboFornecedor();
		
		carregarComboCfop();
		
		carregarComboTipoNotaFiscal();
		
		carregarComboTipoLancamento();
		
	}

	/**
	 * Valida os dados de uma nota fiscal.
	 * 
	 * @param notaFiscalFornecedor
	 * @param dataEmissao
	 * @param dataEntrada
	 * @param valorLiquido
	 * @param valorBruto
	 * @param valoDesconto
	 * 
	 * @throws ValidacaoException
	 */
	private void validarNovaNotaFiscal(
			NotaFiscalFornecedor notaFiscalFornecedor, String dataEmissao,
			String dataEntrada, String valorLiquido, String valorBruto,
			String valorDesconto) throws ValidacaoException {

		List<String> msgs = new ArrayList<String>();

		if(notaFiscalFornecedor == null) {
			
			msgs.add("Os campos da Nota Fiscal devem ser informados");
			
		} else {
			
			if (	notaFiscalFornecedor.getEmitente() == null || 
					notaFiscalFornecedor.getEmitente().getCnpj() == null || 
					notaFiscalFornecedor.getEmitente().getCnpj().isEmpty()) {
				
				msgs.add("O campo Emitente dever ser informado");
				
			}

			if (	notaFiscalFornecedor.getNumero() == null || 
					notaFiscalFornecedor.getNumero().isEmpty()) {
				
				msgs.add("O campo Nota Fiscal dever ser informado");
				
			}

			if (	notaFiscalFornecedor.getSerie() == null || 
					notaFiscalFornecedor.getSerie().isEmpty()) {
				msgs.add("O campo Série dever ser informado");
			}

			validarCampoMonetario("Valor Bruto", valorBruto, msgs);
			
			validarCampoMonetario("Valor Líquido", valorLiquido, msgs);

			validarCampoMonetario("Valor Desconto", valorDesconto, msgs);

			validarCampoData("Data Emissão", dataEmissao, msgs);

			validarCampoData("Data Entrada", dataEntrada, msgs);			
		}

		if(!msgs.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
	}
	
	private void validarNovoItemRecebimentoFisico(RecebimentoFisicoDTO itemRecebimento,
			String numeroEdicao,
			String dataLancamento, 
			String dataRecolhimento) {
		

		List<String> msgs = new ArrayList<String>();

		if(itemRecebimento == null) {
			
			msgs.add("Os campos do Novo Item devem ser informados.");
			
		} else {
			
			if (itemRecebimento.getCodigoProduto() == null) {
				msgs.add("O campo Código dever ser informado.");
			}

			if (numeroEdicao == null) {
				msgs.add("O campo Edição dever ser informado.");
			}

			if (itemRecebimento.getRepartePrevisto() == null) {
				msgs.add("O campo Reparte Previsto dever ser informado.");
			}
			
			if (itemRecebimento.getTipoLancamento() == null) {
				msgs.add("O campo Tipo Lançamento dever ser informado.");
			}
			
			validarCampoData("Data Lançamento", dataLancamento, msgs);

			validarCampoData("Data Recolhimento", dataRecolhimento, msgs);
			
		}
		
		if(!msgs.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, msgs));
		}
		
	}
	
	private void validarCampoData(String label, String valor, List<String> msgs) {
		
		if (valor == null || valor.isEmpty()) {
			
			msgs.add("O campo " + label + "dever ser informado");
			
		} else {
			
			try {
				
				sdf.parse(valor);
				
			} catch (ParseException e) {
				
				msgs.add("O campo " + label + " é invalido");
				
			}
			
		}
		
	}
	
	/**
	 * Valida valores monitotarios.
	 * 
	 * @param label
	 * @param valor
	 * @param msgs
	 */
	private void validarCampoMonetario(String label, String valor, List<String> msgs) {
		
		if (valor == null || valor.isEmpty()) {
			
			msgs.add("O campo " + label + " dever ser informado");
			
		} else {
			
			try {
				
				Double.parseDouble(valor.replace(".", "").replace(",", "."));
				
			} catch (NumberFormatException e) {
				
				msgs.add("O campo " + label + " é invalido");
				
			}
			
		}
		
	}
	
	/**
	 * Inclui os dados de uma nova nota fiscal em session.
	 * 
	 * @param notaFiscalFornecedor
	 * @param dataEmissao
	 * @param dataEntrada
	 * @param valorLiquido
	 * @param valorBruto
	 * @param valorDesconto
	 */
	@Post
	public void incluirNovaNotaFiscal(NotaFiscalFornecedor notaFiscalFornecedor, 
			String dataEmissao,
			String dataEntrada,
			String valorLiquido,
			String valorBruto,
			String valorDesconto)  {
		
		validarNovaNotaFiscal(
				notaFiscalFornecedor, 
				dataEmissao,
				dataEntrada,
				valorLiquido,
				valorBruto,
				valorDesconto);
	
		if(notaFiscalFornecedor.getChaveAcesso() == null || notaFiscalFornecedor.getChaveAcesso().trim().isEmpty()) {
			notaFiscalFornecedor.setChaveAcesso(null);
		}
		
		try {
			notaFiscalFornecedor.setDataEmissao(sdf.parse(dataEmissao));
			notaFiscalFornecedor.setDataExpedicao(sdf.parse(dataEntrada));
		} catch(ParseException e) {
			notaFiscalFornecedor.setDataEmissao(null);
			notaFiscalFornecedor.setDataExpedicao(null);
		}
		
		notaFiscalFornecedor.setValorLiquido(getBigDecimalFromValor(valorLiquido));
		notaFiscalFornecedor.setValorBruto(getBigDecimalFromValor(valorBruto));
		notaFiscalFornecedor.setValorDesconto(getBigDecimalFromValor(valorDesconto));
		
		setNotaFiscalToSession(notaFiscalFornecedor);
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Nova nota fiscal cadastrada com sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
	
	}
	
		
	/**
	 * confirmaçao de recebimento fisico
	 * @param notaFiscal
	 * @param itensRecebimento
	 */
	@Post
	public void confirmarRecebimentoFisico(List<RecebimentoFisicoDTO> itensRecebimento){
		
		atualizarItensRecebimentoEmSession(itensRecebimento);
		
		//TODO: capturar usuario logado
		Usuario usuarioLogado = new Usuario();
		usuarioLogado.setId(1L);
		
		recebimentoFisicoService.confirmarRecebimentoFisico(usuarioLogado, getNotaFiscalFromSession(), getItensRecebimentoFisicoFromSession(), new Date());
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Itens Confirmados com Sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
		
		
	}
	
	/**
	 * Retorna valor BigDecimal.
	 * 
	 * @param valor
	 * 
	 * @return BigDecimal
	 */
	private BigDecimal getBigDecimalFromValor(String valor) {
		return new BigDecimal(valor.replace(".", "").replace(",", "."));
	}
	
	public NotaFiscal getNotaFiscalFromSession() {
		return (NotaFiscal) request.getSession().getAttribute(CABECALHO_NOTA_FISCAL);
	}

	public void setNotaFiscalToSession(NotaFiscal notaFiscal) {
		request.getSession().setAttribute(CABECALHO_NOTA_FISCAL, notaFiscal);
	}

	@SuppressWarnings("unchecked")
	public List<RecebimentoFisicoDTO> getItensRecebimentoFisicoFromSession() {
		return (List<RecebimentoFisicoDTO>) request.getSession().getAttribute(ITENS_NOTA_FISCAL);
	}

	public void setItensRecebimentoFisicoToSession(List<RecebimentoFisicoDTO> itensRecebimentoFisico) {
		request.getSession().setAttribute(ITENS_NOTA_FISCAL, itensRecebimentoFisico);
	}

	

}
