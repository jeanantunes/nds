package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.NFEImportUtil;
import br.com.abril.nds.client.vo.RecebimentoFisicoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CabecalhoNotaDTO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNotaFiscalDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.Origem;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.CFOP;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.fiscal.StatusRecebimento;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NotaFiscalEntradaService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RecebimentoFisicoService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/recebimentoFisico")

@Rules(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO)
public class RecebimentoFisicoController extends BaseController {
	
	private Result result;

	private HttpServletRequest request;

	private static final String CABECALHO_NOTA_FISCAL = "cabecalhoNotaFiscal";
	
	private static final String ITENS_NOTA_FISCAL = "itensNotaFiscal";
	
	private static final String IND_SIM = "S";
	
	private static final String IND_NAO = "N";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private NotaFiscalEntradaService notaFiscalService;
	
	@Autowired
	private RecebimentoFisicoService recebimentoFisicoService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private TipoNotaFiscalService tipoNotaService;
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private DistribuidorService distribuidorService;

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
	@Path("/")
	public void index() {

		preencherCombos();
		
		preencherDataEmissao();
		
		boolean confCega = !usuarioPossuiRule(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO_CONF_CEGA);
		
		boolean permiteVisualizacao = usuarioPossuiRule(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO)  && confCega;

		result.include(
			"permissaoBotaoConfirmacao", 
			usuarioPossuiRule(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO) && confCega);
		
		result.include(
			"permissaoGridColRepartePrevisto", permiteVisualizacao);
		
		result.include(
			"permissaoGridColDiferenca", permiteVisualizacao);
		
		result.include(
			"permissaoColValorTotal", permiteVisualizacao);
		
		result.include(
				"permissaoColValorTotalDesconto", permiteVisualizacao);
		
		result.include("indConferenciaCega", this.distribuidorService.isConferenciaCegaRecebimentoFisico());
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
		
		if(pessoaJuridica == null){

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "CNPJ não foi encontrado!"));
		}
		
		result.use(Results.json()).from(pessoaJuridica, "result").serialize();
		
	}
	
	/**
	 * Replica o valor de reparte previsto para qtdPacote e qtdExemplar, e serializa o objeto 
	 * do tipo RecebimentoFisicoVO pertinente.
	 * 
	 * @param lineId
	 */
	public void replicarValorRepartePrevisto(int lineId) {
		
		RecebimentoFisicoDTO itemRecebimentoFisicoDTO =  obterRecebimentoFisicoDTODaSessaoPorLineId(lineId);
		
		itemRecebimentoFisicoDTO.setQtdFisico(itemRecebimentoFisicoDTO.getRepartePrevisto());
		
		carregarValoresQtdPacoteQtdExemplar(itemRecebimentoFisicoDTO);
		
		obterRecebimentoFisicoVO(lineId);
		
	}
	
	/**
	 * Replica o valor de reparte previsto para qtdPacote e qtdExemplar de todos
	 * os objetos em session.
	 */
	public void replicarTodosValoresRepartePrevisto() {
		
		for(RecebimentoFisicoDTO itemRecebimentoFisicoDTO : getItensRecebimentoFisicoFromSession()) {
			
			itemRecebimentoFisicoDTO.setQtdFisico(itemRecebimentoFisicoDTO.getRepartePrevisto());
			
			carregarValoresQtdPacoteQtdExemplar(itemRecebimentoFisicoDTO);
		}
		
		result.use(Results.json()).from("").serialize();
		
	}

	
	
	/**
	 * Serializa a listaItemRecebimentoFisico que esta em session recalculando os campos
	 * valorTotal e valorDiferenca.
	 */
	@Post
	public void refreshListaItemRecebimentoFisico() {
		
		if( getItensRecebimentoFisicoFromSession() == null ) {
			setItensRecebimentoFisicoToSession(new LinkedList<RecebimentoFisicoDTO>());
		}
		
		NotaFiscalEntrada notaFiscal = getNotaFiscalFromSession();
		
		boolean indRecebimentoFisicoConfirmado = verificarRecebimentoFisicoConfirmado(notaFiscal.getId());
		
		List<CellModelKeyValue<RecebimentoFisicoVO>> rows = obterListaCellModelKeyValue(getItensRecebimentoFisicoFromSession(), indRecebimentoFisicoConfirmado, true);
				
		TableModel<CellModelKeyValue<RecebimentoFisicoVO>> tableModel = new TableModel<CellModelKeyValue<RecebimentoFisicoVO>>();

		tableModel.setRows(rows);
		tableModel.setTotal(rows.size());
		tableModel.setPage(1);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
		
	/**
	 * Faz a busca na base de dados da listaItemRecebimentoFisico e a serializa.
	 */
	@Post
	public void obterListaItemRecebimentoFisico() {
		
		NotaFiscalEntrada notaFiscal = getNotaFiscalFromSession();
		
		Long idNotaFiscal = notaFiscal.getId();
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico = 
				recebimentoFisicoService.obterListaItemRecebimentoFisico(idNotaFiscal);
				
		if(itensRecebimentoFisico == null) {
			itensRecebimentoFisico = new LinkedList<RecebimentoFisicoDTO>();
		}
		
		boolean indRecebimentoFisicoConfirmado = verificarRecebimentoFisicoConfirmado(idNotaFiscal);
		
		List<CellModelKeyValue<RecebimentoFisicoVO>> rows = 
			obterListaCellModelKeyValue(
					itensRecebimentoFisico, indRecebimentoFisicoConfirmado, false);
		
		setItensRecebimentoFisicoToSession(itensRecebimentoFisico);
		
		TableModel<CellModelKeyValue<RecebimentoFisicoVO>> tableModel = 
				new TableModel<CellModelKeyValue<RecebimentoFisicoVO>>();

		tableModel.setRows(rows);
		tableModel.setTotal(rows.size());
		tableModel.setPage(1);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	public void obterInformacoesNota() {
		
		NotaFiscalEntrada notaFiscal = getNotaFiscalFromSession();
		
		String cnpjFornecedor = null;
		
		if( notaFiscal.getEmitente() != null) {
			 cnpjFornecedor = notaFiscal.getEmitente().getCnpj();
		}
		
		Map<String, String> infoNota = new HashMap<String, String>();
		infoNota.put("numero", notaFiscal.getNumero().toString());
		infoNota.put("serie", notaFiscal.getSerie());
		infoNota.put("chaveAcesso", notaFiscal.getChaveAcesso());
		infoNota.put("cnpj", cnpjFornecedor);
		
		result.use(CustomJson.class).put("nota", infoNota).serialize();
	}
	
	private boolean verificarRecebimentoFisicoConfirmado(Long idNotaFiscal) {
		
		if(idNotaFiscal == null){
			return false;
		}
		
		RecebimentoFisico recebimentoFisico = recebimentoFisicoService.obterRecebimentoFisicoPorNotaFiscal(idNotaFiscal);
		
		if(recebimentoFisico == null) {
			return false;
		}
		
		if(recebimentoFisico.getStatusConfirmacao().equals(StatusConfirmacao.CONFIRMADO)){
			return true;
		}
		
		return false;		
	}

	private boolean validarChaveAcesso(FiltroConsultaNotaFiscalDTO filtro, String indNFe, List<String> msgs) {
		
		boolean indChaveAcessoInformada = false;
		
		String regraParaChaveAcesso = "^[0-9]{"+
		NFEImportUtil.QTD_DIGITOS_CHAVE_ACESSO_NFE+","+
		NFEImportUtil.QTD_DIGITOS_CHAVE_ACESSO_NFE+"}$";
		
		if(filtro.getChave() != null && filtro.getChave().trim().isEmpty()) {
			filtro.setChave(null);
		}
		
		if(indNFe.equals("S")) {
			
			indChaveAcessoInformada = true;
			
			if(filtro.getChave() != null && 
				!filtro.getChave().matches(regraParaChaveAcesso)) {
				
				msgs.add("Chave de Acesso deve possuir " + NFEImportUtil.QTD_DIGITOS_CHAVE_ACESSO_NFE +  " dígitos.");
				
			}
			
		}
		
		return indChaveAcessoInformada;
	}
	
	
	private boolean verificaValidationError(String category) {
	
		if(!validator.hasErrors()) {
			return false;
		}
		
		for(Message msg : validator.getErrors()) {
			if(msg.getCategory().contains(category)) {
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * Valida os dados da nova Nota Fiscal.
	 * 
	 */
	private void validarDadosNotaFiscal(FiltroConsultaNotaFiscalDTO filtro, String fornecedor, String indNFe) {
		
		List<String> msgs = new ArrayList<String>();
		
		boolean indChaveAcessoInformada = validarChaveAcesso(filtro, indNFe, msgs);
		
		boolean indTodosFornecedores = fornecedor.equals("-1");
		
		if(!indChaveAcessoInformada) {
			
			if(!indTodosFornecedores){
				if(filtro.getCnpj() == null || filtro.getCnpj().trim().isEmpty()) {
					msgs.add("O campo CNPJ do fornecedor é obrigatório");
				} 
			} else {
				filtro.setCnpj(null);
			}
			
			if(filtro.getNumeroNotaEnvio() != null) {
				return;
			}
			
			if(filtro.getNumeroNota() == null) {
				
				if(verificaValidationError("numeroNotaFiscal")) {
					msgs.add("Valor inválido para o campo Nota Fiscal");
				} else {
					msgs.add("O campo Nota Fiscal é obrigatório");
				}
				
			}

			if(filtro.getSerie() == null || filtro.getSerie().isEmpty()) {
				msgs.add("O campo Série é obrigatório");
			}
			
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
	 * Obtém um item da lista em sessão a partir de seu lineId.
	 * 
	 * @param lineId
	 * 
	 * @return RecebimentoFisicoDTO
	 */
	private RecebimentoFisicoDTO obterRecebimentoFisicoDTODaSessaoPorLineId(int lineId) {
		
		for(RecebimentoFisicoDTO recebimentoFisicoDTO : getItensRecebimentoFisicoFromSession()) {
			if(recebimentoFisicoDTO.getLineId() == lineId) {
				return recebimentoFisicoDTO;
			}
		}
		
		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item de nota encontrado");
		
	}
	
	/**
	 * Serializa um item do tipo RecebimentoFisicoVO obtido da lista de 
	 * itens da nota em sessão cujo lineId seja o mesmo do parâmetro.
	 * 
	 * @param lineId
	 */
	public void obterRecebimentoFisicoVO(int lineId) {
		
		RecebimentoFisicoDTO recebimentoFisicoDTO = obterRecebimentoFisicoDTODaSessaoPorLineId(lineId);
		
		String codigo			= recebimentoFisicoDTO.getCodigoProduto();
		String nomeProduto		= recebimentoFisicoDTO.getNomeProduto();
		String edicao			= (recebimentoFisicoDTO.getEdicao() 		== null) 	? "" 	: recebimentoFisicoDTO.getEdicao().toString();
		String precoItem		= (recebimentoFisicoDTO.getPrecoItem() 		== null) 	? "0.0" : recebimentoFisicoDTO.getPrecoItem().toString();
		String dataLancamento	= (recebimentoFisicoDTO.getDataLancamento() != null) ? DateUtil.formatarDataPTBR(recebimentoFisicoDTO.getDataLancamento()) : "";
		String dataRecolhimento = (recebimentoFisicoDTO.getDataRecolhimento() != null) ?  DateUtil.formatarDataPTBR(recebimentoFisicoDTO.getDataRecolhimento()) : "";
		String repartePrevisto  = (recebimentoFisicoDTO.getRepartePrevisto()!=null) ? recebimentoFisicoDTO.getRepartePrevisto().toString() : "";
		String tipoLancamento   = (recebimentoFisicoDTO.getTipoLancamento()!=null) ? recebimentoFisicoDTO.getTipoLancamento().name() : "";
		String pacotePadrao		= String.valueOf(recebimentoFisicoDTO.getPacotePadrao());
		String peso				= (recebimentoFisicoDTO.getPeso()!=null) ? recebimentoFisicoDTO.getPeso().toString() : ""; 
		String qtdPacote		= (recebimentoFisicoDTO.getQtdPacote()!=null) ? recebimentoFisicoDTO.getQtdPacote().toString() : "";
		String qtdExemplar		= (recebimentoFisicoDTO.getQtdExemplar()!=null) ? recebimentoFisicoDTO.getQtdExemplar().toString() : "";
		
		RecebimentoFisicoVO recebimentoFisicoVO = new RecebimentoFisicoVO();
		
		recebimentoFisicoVO.setCodigo(codigo);
		recebimentoFisicoVO.setNomeProduto(nomeProduto);
		recebimentoFisicoVO.setEdicao(edicao);
		recebimentoFisicoVO.setPrecoCapa(precoItem);
		recebimentoFisicoVO.setDataLancamento(dataLancamento);
		recebimentoFisicoVO.setDataRecolhimento(dataRecolhimento);
		recebimentoFisicoVO.setRepartePrevisto(repartePrevisto);
		recebimentoFisicoVO.setTipoLancamento(tipoLancamento);
		recebimentoFisicoVO.setPacotePadrao(pacotePadrao);
		recebimentoFisicoVO.setPeso(peso);
		recebimentoFisicoVO.setQtdPacote(qtdPacote);
		recebimentoFisicoVO.setQtdExemplar(qtdExemplar);
		
		recebimentoFisicoVO.setValorTotalCapa(
			recebimentoFisicoDTO.getPrecoCapa().multiply(
				new BigDecimal(recebimentoFisicoDTO.getRepartePrevisto())).setScale(2, RoundingMode.HALF_EVEN).toString());
		
		recebimentoFisicoVO.setValorTotalDesconto(
				new BigDecimal(recebimentoFisicoDTO.getPrecoDesconto()).multiply(
					new BigDecimal(recebimentoFisicoDTO.getRepartePrevisto())).setScale(2, RoundingMode.HALF_EVEN).toString());
				
		result.use(Results.json()).withoutRoot().from(recebimentoFisicoVO).serialize();
		
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
		
		boolean indEdicaoItemNota = (itemRecebimento.getLineId() >= 0);
		
		NotaFiscalEntrada notaFiscalEntrada = getNotaFiscalFromSession();
		
		if(Origem.INTERFACE.equals(notaFiscalEntrada.getOrigem())) {
			atualizarItensRecebimentoEmSession(itensRecebimento);
		}
		
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
		
		BigDecimal qtdProduto = 
			new BigDecimal(
				itemRecebimento.getRepartePrevisto().multiply(
					new BigInteger(
							String.valueOf(itemRecebimento.getPacotePadrao())
		)));
		
		if (itemRecebimento.getPrecoItem() == null){
			itemRecebimento.setPrecoItem(produtoEdicao.getPrecoVenda());
		}
		
		if ((itemRecebimento.getPrecoDesconto() == null ||
			itemRecebimento.getPrecoDesconto().isEmpty()) &&
			produtoEdicao.getDesconto() != null){
			
			itemRecebimento.setPrecoDesconto(
				produtoEdicao.getPrecoVenda().subtract(
					produtoEdicao.getPrecoVenda().multiply(
						produtoEdicao.getDesconto())).setScale(
							2, RoundingMode.HALF_EVEN).toString());
			
		} else {
			
			itemRecebimento.setPrecoDesconto(
				produtoEdicao.getPrecoVenda().setScale(
					2, RoundingMode.HALF_EVEN).toString());
		}
		
		if (itemRecebimento.getValorTotal() == null){
			itemRecebimento.setValorTotal(
				itemRecebimento.getPrecoItem().multiply(qtdProduto));
		}
		
		if (itemRecebimento.getValorTotalDesconto() == null &&
			itemRecebimento.getPrecoDesconto() != null){
			
			itemRecebimento.setValorTotalDesconto(
				new BigDecimal(itemRecebimento.getPrecoDesconto()).multiply(qtdProduto));
		} else {
			
			itemRecebimento.setValorTotalDesconto(itemRecebimento.getValorTotal());
		}
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico =  getItensRecebimentoFisicoFromSession();
		
		if(itensRecebimentoFisico == null) {
			itensRecebimentoFisico = new LinkedList<RecebimentoFisicoDTO>();
			setItensRecebimentoFisicoToSession(itensRecebimentoFisico);
		}
		
		itemRecebimento.setOrigemItemNota(Origem.MANUAL);
		itemRecebimento.setEdicao(produtoEdicao.getNumeroEdicao());
		itemRecebimento.setIdProdutoEdicao(produtoEdicao.getId());
		
		if(!Origem.INTERFACE.equals(itemRecebimento.getOrigemItemNota())) {
			itemRecebimento.setQtdFisico(itemRecebimento.getRepartePrevisto());
		}
		
		if(indEdicaoItemNota) {
			
			RecebimentoFisicoDTO itemParaRemocao = obterRecebimentoFisicoDTODaSessaoPorLineId(itemRecebimento.getLineId());
			
			itemRecebimento.setIdItemNota(itemParaRemocao.getIdItemNota());
			itemRecebimento.setIdItemRecebimentoFisico(itemParaRemocao.getIdItemRecebimentoFisico());
			
			itensRecebimentoFisico.remove(itemParaRemocao);
			
		} else {
			
			validarProdutoEdicaoExistente(itemRecebimento, itensRecebimentoFisico);
			
		}
		
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
	private void carregarValorTotal(RecebimentoFisicoDTO itemRecebimento, boolean isRefresh) {
		
		BigInteger qtdeExemplares = itemRecebimento.getRepartePrevisto() == null ? BigInteger.ZERO : itemRecebimento.getRepartePrevisto();
		
		BigDecimal precoItem = itemRecebimento.getPrecoCapa() == null ? BigDecimal.ZERO : itemRecebimento.getPrecoCapa();
				
		BigInteger qtdeTotalItens = null;
		
		if (itemRecebimento.getPacotePadrao() == 0){
			
			qtdeTotalItens = itemRecebimento.getRepartePrevisto() == null ? BigInteger.ZERO : itemRecebimento.getRepartePrevisto();
		} else {
			qtdeTotalItens = qtdeExemplares;
		
		}
		
		BigDecimal valorTotal = precoItem.multiply(new BigDecimal(qtdeTotalItens));
  
		itemRecebimento.setValorTotal(valorTotal);
		
		BigDecimal precoDesc = new BigDecimal(itemRecebimento.getPrecoDesconto());
		
		itemRecebimento.setValorTotalDesconto( precoDesc.multiply( new BigDecimal(qtdeTotalItens)).setScale(2, RoundingMode.HALF_EVEN));
	}
		
	/**
	 * Recalcula o campo valorDiferenca do itemRecebimentoFisico.
	 * 
	 * @param itemRecebimento
	 */
	private void carregarValorDiferenca(RecebimentoFisicoDTO itemRecebimento) {
		
		if (itemRecebimento.getRepartePrevisto() == null) {			
			itemRecebimento.setRepartePrevisto(BigInteger.ZERO);
		}

		BigInteger qtdRepartePrevisto = itemRecebimento.getRepartePrevisto();
		
		BigInteger qtdFisico = itemRecebimento.getQtdFisico();
		
		BigInteger valorDiferenca = BigInteger.ZERO;
		
		if (itemRecebimento.getQtdFisico() != null) {
			
			valorDiferenca = qtdFisico.subtract(qtdRepartePrevisto);
		}
		
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
		
		NotaFiscalEntrada notaFiscalEntrada = getNotaFiscalFromSession();
		
		if(Origem.INTERFACE.equals(notaFiscalEntrada.getOrigem())){
			atualizarItensRecebimentoEmSession(itensRecebimento);
		}
		
		List<RecebimentoFisicoDTO> itensRecebimentoFisico =  getItensRecebimentoFisicoFromSession();

		RecebimentoFisicoDTO apagarReceb = null;
		
		for(RecebimentoFisicoDTO recebimento : itensRecebimentoFisico) {
			
			if(recebimento.getLineId() == lineId) {
				
				apagarReceb = recebimento;
				
				break;
			}
			
		}
				
		itensRecebimentoFisico.remove(apagarReceb);
		
		recebimentoFisicoService.apagarItemRecebimentoItemNota(apagarReceb);
		
		List<String> msgs = new ArrayList<String>();
		
		msgs.add("Item Nota fiscal removido com sucesso.");
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();
		
	}
	


	/**
	 * Atualiza os campos qtdPacote e qtdExemplar da lista de 
	 * itemRecebimentoFisico que se encontra em session.
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
				
				int formItemLineId = itemFromForm.getLineId();
				
				for(RecebimentoFisicoDTO itemFromSession : itensRecebimentoFisicoFromSession) {
					
					int sessionItemLineId = itemFromSession.getLineId();
					
					if(formItemLineId == sessionItemLineId) {
						
						itemFromSession.setQtdPacote(itemFromForm.getQtdPacote());
						
						itemFromSession.setQtdExemplar(itemFromForm.getQtdExemplar());
						
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
		
		NotaFiscalEntrada notaFiscalEntrada = getNotaFiscalFromSession();
		notaFiscalEntrada.setUsuario(getUsuarioLogado());
		
		if(Origem.INTERFACE.equals(notaFiscalEntrada.getOrigem())){
			atualizarItensRecebimentoEmSession(itensRecebimento);
		}
		
		NotaFiscalEntrada notaFiscalFromSession = getNotaFiscalFromSession();
		notaFiscalFromSession.setStatusRecebimento(StatusRecebimento.SALVO);
		
		recebimentoFisicoService.inserirDadosRecebimentoFisico(getUsuarioLogado(), notaFiscalFromSession, getItensRecebimentoFisicoFromSession(), new Date());
		
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
	 * Faz o cancelamento de uma nota fiscal e seu recebimento físico.
	 */
	public void cancelarNotaRecebimentoFisico() {
		
		NotaFiscalEntrada notaFiscal = getNotaFiscalFromSession();
		
		if(notaFiscal == null || notaFiscal.getId() == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Nenhuma Nota Fiscal existente para cancelamento do recebimento físico.");
		}
				
		recebimentoFisicoService.cancelarNotaFiscal(notaFiscal.getId());
		
		setItensRecebimentoFisicoToSession(null);
		
		setNotaFiscalToSession(null);
		
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Recebimento Físico cancelado com sucesso");
		
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
		
	}
	
	/**
	 * Faz a exclusão de uma nota fiscal .
	 */
	public void excluirNotaRecebimentoFisico(Long id) {
	
		recebimentoFisicoService.excluirNota(id);
							
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, "Nota excluida com sucesso");
		
		result.use(Results.json()).from(validacao, "result").include("listaMensagens").serialize();	
		
	}
	
	/**
	 * Faz a pesquisa de uma nota fiscal através dos parâmetros de 
	 * CNPJ, numero da nota, série e chave de acesso ou ainda por 
	 * numero da nota de envio caso a mesma seja uma nota fiscal 
	 * eletrônica. Caso a nota seja encontrada o id interno da mesma 
	 * será colocado na session.
	 * 
	 * @param cnpj
	 * @param numeroNotaFiscal
	 * @param serie
	 * @param indNFe
	 * @param fornecedor
	 * @param chaveAcesso
	 * @param numeroNotaEnvio
	 */
	@Post
	public void verificarNotaFiscalExistente(
			String cnpj, 
			Long numeroNotaFiscal, 
			String serie,
			String indNFe,
			String fornecedor, 
			String chaveAcesso) {
		
		limparDadosDaSession();
		
		FiltroConsultaNotaFiscalDTO filtro = new FiltroConsultaNotaFiscalDTO();
		
		String cnpjSemMascara = Util.removerMascaraCnpj(cnpj);
		
		filtro.setCnpj(cnpjSemMascara);
		
		filtro.setSerie(serie);
		filtro.setChave(chaveAcesso);
		filtro.setNomeFornecedor(fornecedor);
		
		boolean isNE = (numeroNotaFiscal != null && serie == null);
		
		if (isNE) {
			
			filtro.setNumeroNotaEnvio(numeroNotaFiscal);
			
		} else {
			
			filtro.setNumeroNota(numeroNotaFiscal);
		}
		
		validarDadosNotaFiscal(filtro, fornecedor, indNFe);		
		
		List<NotaFiscalEntrada> listaNotaFiscal = notaFiscalService.obterNotaFiscalEntrada(filtro);
		
			
		if(listaNotaFiscal != null && listaNotaFiscal.size()>1) {
			
			if (filtro.getNumeroNota() != null && 
				(filtro.getIdFornecedor() == null || filtro.getIdFornecedor() == -1) && 
				(filtro.getSerie() == null || filtro.getSerie().isEmpty())){
				
				throw new ValidacaoException(
						TipoMensagem.WARNING, 
						"Mais de uma nota fiscal cadastrada com estes valores, especifique um fornecedor.");
			} else {
			
				throw new ValidacaoException(
					TipoMensagem.WARNING, 
					"Mais de uma nota fiscal encontrada com o filtro escolhido.");
			}
		} 
		
		NotaFiscalEntrada notaFiscal = null;
		
		if(listaNotaFiscal != null && !listaNotaFiscal.isEmpty()) {
			notaFiscal = listaNotaFiscal.get(0);
		} 
		
		if (notaFiscal == null){
						
			List<String> msgs = new ArrayList<String>();
			
			if (isNE) {
			
				msgs.add("Nota de Envio não encontrada");
				
			} else {
			
				msgs.add("Nota fiscal não encontrada");
			}
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, msgs);
													
			result.use(Results.json()).from(
				new ResultadoNotaFiscalExistente(
					validacao, false, false ), "result")
						.include("validacao")
						.include("validacao.listaMensagens").serialize();
		
		} else {
			
			boolean indNotaInterface = false;
			
			setNotaFiscalToSession(notaFiscal);
			
			List<String> msgs = new ArrayList<String>();
			
			if(StatusNotaFiscalEntrada.RECEBIDA.equals(notaFiscal.getStatusNotaFiscal()))
				msgs.add("Nota fiscal encontrada com sucesso");
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);	
			
			if(notaFiscal.getOrigem().equals(Origem.INTERFACE)){
				indNotaInterface = true;
			}
			
			boolean indRecebimentoFisicoConfirmado = verificarRecebimentoFisicoConfirmado(notaFiscal.getId());
			
			if (cnpj == null || cnpj.isEmpty()){
				if (notaFiscal instanceof NotaFiscalEntradaFornecedor){
					cnpj = ((NotaFiscalEntradaFornecedor) notaFiscal).getEmitente().getCnpj();
				}
			}
			
			result.use(Results.json()).from(
				new ResultadoNotaFiscalExistente(
					validacao, indNotaInterface, indRecebimentoFisicoConfirmado,
					cnpj, notaFiscal.getNumero(), notaFiscal.getSerie(),
					notaFiscal.getChaveAcesso(), notaFiscal.getId()), "result")
						.include("validacao")
						.include("validacao.listaMensagens").serialize();
		}
				
	}
	
	class ResultadoNotaFiscalExistente {
		
		private ValidacaoVO validacao;
		private boolean indNotaInterface;		
        private boolean indRecebimentoFisicoConfirmado;
        private String cnpj, serieNotaFiscal, chaveAcesso;
        private Long numeroNotaFiscal;
        private Long id;
			
		public ResultadoNotaFiscalExistente(ValidacaoVO validacao,
				boolean indNotaInterface,
				boolean indRecebimentoFisicoConfirmado) {
			super();
			this.validacao = validacao;
			this.indNotaInterface = indNotaInterface;
			this.indRecebimentoFisicoConfirmado = indRecebimentoFisicoConfirmado;
		}
		
		public ResultadoNotaFiscalExistente(ValidacaoVO validacao,
				boolean indNotaInterface,
				boolean indRecebimentoFisicoConfirmado,
				String cnpj, Long numeroNotaFiscal,
				String serieNotaFiscal,
				String chaveAcesso,
				Long id) {
			this(validacao, indNotaInterface, indRecebimentoFisicoConfirmado);
			this.cnpj = Util.adicionarMascaraCNPJ(cnpj);
			this.serieNotaFiscal = serieNotaFiscal;
			this.numeroNotaFiscal = numeroNotaFiscal;
			this.chaveAcesso = chaveAcesso;
			this.id = id;
		}
		
		public ValidacaoVO getValidacao() {
			return validacao;
		}
		
		public void setValidacao(ValidacaoVO validacao) {
			this.validacao = validacao;
		}
		public boolean isIndNotaInterface() {
			return indNotaInterface;
		}
		public void setIndNotaInterface(boolean indNotaInterface) {
			this.indNotaInterface = indNotaInterface;
		}
		public boolean isIndRecebimentoFisicoConfirmado() {
			return indRecebimentoFisicoConfirmado;
		}

		public void setIndRecebimentoFisicoConfirmado(
				boolean indRecebimentoFisicoConfirmado) {
			this.indRecebimentoFisicoConfirmado = indRecebimentoFisicoConfirmado;
		}

		public String getCnpj() {
			return cnpj;
		}

		public void setCnpj(String cnpj) {
			this.cnpj = cnpj;
		}

		public String getSerieNotaFiscal() {
			return serieNotaFiscal;
		}

		public void setSerieNotaFiscal(String serieNotaFiscal) {
			this.serieNotaFiscal = serieNotaFiscal;
		}

		public Long getNumeroNotaFiscal() {
			return numeroNotaFiscal;
		}

		public void setNumeroNotaFiscal(Long numeroNotaFiscal) {
			this.numeroNotaFiscal = numeroNotaFiscal;
		}

		public String getChaveAcesso() {
			return chaveAcesso;
		}

		public void setChaveAcesso(String chaveAcesso) {
			this.chaveAcesso = chaveAcesso;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		
	}
	
	private void carregarValoresQtdPacoteQtdExemplar(RecebimentoFisicoDTO itemRecebimento) {
		
		if(Origem.MANUAL.equals(itemRecebimento.getOrigemItemNota())) {
			itemRecebimento.setQtdFisico(itemRecebimento.getRepartePrevisto());
		}
		
		BigInteger qtdFisico = (itemRecebimento.getQtdFisico() == null) ? BigInteger.ZERO : itemRecebimento.getQtdFisico();
		
		BigInteger pacotePadrao = (itemRecebimento.getPacotePadrao() <= 0) ? BigInteger.ONE :  
							new BigInteger(String.valueOf(itemRecebimento.getPacotePadrao()));
			
		BigInteger[] qtdes = qtdFisico.divideAndRemainder(pacotePadrao);  

		BigInteger qtdPacote = qtdes[0];
		
		BigInteger qtdQuebra = qtdes[1];
		
		itemRecebimento.setQtdPacote(qtdPacote);
		
		itemRecebimento.setQtdExemplar(qtdQuebra);
		
	}

	
	/**
	 * Obtém uma lista do tipo {@link CellModelKeyValue<RecebimentoFisicoVO>}
	 * 
	 * @param itensRecebimentoFisico
	 * @param indNotaInterface
	 * @param indRecebimentoFisicoConfirmado
	 * 
	 * @return List<CellModelKeyValue<RecebimentoFisicoVO>>
	 */
	private List<CellModelKeyValue<RecebimentoFisicoVO>> obterListaCellModelKeyValue(
			List<RecebimentoFisicoDTO> itensRecebimentoFisico, 
			boolean indRecebimentoFisicoConfirmado, boolean isRefresh) {
		
		int counter = 0;
		
		List<CellModelKeyValue<RecebimentoFisicoVO>> rows = new LinkedList<CellModelKeyValue<RecebimentoFisicoVO>>();

		for(RecebimentoFisicoDTO dto : itensRecebimentoFisico) {
			
			counter++;

			dto.setLineId(counter);
		
			carregarValoresQtdPacoteQtdExemplar(dto);
			
			String valorDesconto = dto.getPrecoDesconto();
			
			if (dto.getPrecoDesconto() == null) {
				
				if (dto.getPercentualDesconto() != null) {
					valorDesconto = dto.getPrecoCapa().subtract(
							dto.getPrecoCapa().multiply(
									dto.getPercentualDesconto())).setScale(4, RoundingMode.HALF_EVEN).toString();
				} else {
					
					valorDesconto = dto.getPrecoItem().setScale(4, RoundingMode.HALF_EVEN).toString();
				}
				
			}
			
			dto.setPrecoDesconto(new BigDecimal(valorDesconto).setScale(4, RoundingMode.HALF_EVEN).toString());
			
			carregarValorTotal(dto, isRefresh);
			
			carregarValorDiferenca(dto);
			
			RecebimentoFisicoVO recebFisico = new RecebimentoFisicoVO();
			
			String codigo 		     	 = dto.getCodigoProduto();
			String nomeProduto 	     	 = dto.getNomeProduto();
			String edicao 		     	 = (dto.getEdicao() 			== null) 	? "" 	: dto.getEdicao().toString();
			String precoCapa			 = (dto.getPrecoCapa() 			== null) 	? "0.0" : dto.getPrecoCapa().toString();
			
			String repartePrevisto 	 	 = (dto.getRepartePrevisto() 	== null) 	? "0" : dto.getRepartePrevisto().toString();
			String qtdPacote			 = (dto.getQtdPacote()			== null) 	? "0"	: dto.getQtdPacote().toString(); 
			String qtdExemplar			 = (dto.getQtdExemplar()		== null)	? "0"	: dto.getQtdExemplar().toString();
			String diferenca		 	 = (dto.getDiferenca() 			== null) 	? "0" : dto.getDiferenca().toString();
			String valorTotal		 	 = (dto.getValorTotal() 		== null) 	? "0.0" : dto.getValorTotal().toString();
			String valorTotalDesconto	 = (dto.getValorTotalDesconto()	== null) 	? "0.0" : dto.getValorTotalDesconto().toString();
			String pacotePadrao		 	 = (dto.getValorTotal() 		== null) 	? "0"   : Integer.toString(dto.getPacotePadrao());
			
			String edicaoItemNotaPermitida 		= IND_SIM;
			String edicaoItemRecFisicoPermitida = IND_SIM;
			
			if(indRecebimentoFisicoConfirmado) {
				
				edicaoItemNotaPermitida 		= IND_NAO;
				edicaoItemRecFisicoPermitida 	= IND_NAO;
				
			} else {
				
				if(Origem.MANUAL.equals(dto.getOrigemItemNota())) {

					edicaoItemNotaPermitida 		= IND_SIM;
					edicaoItemRecFisicoPermitida 	= IND_NAO;

					
				} else {
					edicaoItemNotaPermitida 		= IND_NAO;
					edicaoItemRecFisicoPermitida 	= IND_SIM;
				}
			}
			
			String destacarValorNegativo = (dto.getDiferenca() != null && dto.getDiferenca().intValue() < 0) ? IND_SIM : IND_NAO;
			
			recebFisico.setLineId(counter);
			recebFisico.setCodigo(codigo);
			recebFisico.setNomeProduto(nomeProduto);
			recebFisico.setEdicao(edicao);
			recebFisico.setPrecoCapa(precoCapa);
			
			recebFisico.setPrecoDesconto(valorDesconto);
			
			recebFisico.setRepartePrevisto(repartePrevisto);
			recebFisico.setQtdPacote(qtdPacote);
			recebFisico.setQtdExemplar(qtdExemplar);
			recebFisico.setDiferenca(diferenca);
			recebFisico.setValorTotalCapa(valorTotal);
			recebFisico.setValorTotalDesconto(valorTotalDesconto);
			recebFisico.setPacotePadrao(pacotePadrao);
			
			recebFisico.setEdicaoItemNotaPermitida(edicaoItemNotaPermitida);
			recebFisico.setEdicaoItemRecFisicoPermitida(edicaoItemRecFisicoPermitida);
			recebFisico.setDestacarValorNegativo(destacarValorNegativo);
			recebFisico.setProdutoSemCadastro(dto.isProdutoSemCadastro());

			rows.add(new CellModelKeyValue<RecebimentoFisicoVO>(counter, recebFisico));
		}
		
		return rows;
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
		
		List<NaturezaOperacao> listaTipoNotaFiscal = recebimentoFisicoService.obterTiposNotasFiscais(TipoOperacao.ENTRADA);
		
		if (listaTipoNotaFiscal != null) {
			result.include("listaTipoNotaFiscal", listaTipoNotaFiscal);
		}
		
	}
	
	/**
	 * Inclui na view os dados do combo de Fornecedor.
	 */
	private void carregarComboFornecedor() {
		
		List<Fornecedor> fornecedores = 
			this.fornecedorService.obterFornecedoresPorSituacaoEOrigem(
				SituacaoCadastro.ATIVO, null);
		
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
	 * confirmaçao de recebimento fisico
	 * @param notaFiscal
	 * @param itensRecebimento
	 */
	@Post
	@Rules(Permissao.ROLE_ESTOQUE_RECEBIMENTO_FISICO_ALTERACAO)
	public void confirmarRecebimentoFisico(List<RecebimentoFisicoDTO> itensRecebimento){
		
		NotaFiscalEntrada notaFiscalEntrada = getNotaFiscalFromSession();
		notaFiscalEntrada.setUsuario(getUsuarioLogado());
		
		if(Origem.INTERFACE.equals(notaFiscalEntrada.getOrigem())){
			atualizarItensRecebimentoEmSession(itensRecebimento);
		}
		NotaFiscalEntrada notaFiscalFromSession = getNotaFiscalFromSession();
		notaFiscalFromSession.setStatusRecebimento(StatusRecebimento.CONFIRMADO);
		
		recebimentoFisicoService.confirmarRecebimentoFisico(getUsuarioLogado(), notaFiscalFromSession, getItensRecebimentoFisicoFromSession(), new Date(),false);
		
		List<String> msgs = new ArrayList<String>();
		msgs.add("Itens Confirmados com Sucesso.");
		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.SUCCESS, msgs);
		result.use(Results.json()).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();	
		
		
	}
	
	public NotaFiscalEntrada getNotaFiscalFromSession() {
		return (NotaFiscalEntrada) request.getSession().getAttribute(CABECALHO_NOTA_FISCAL);
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
	
	
	
	/*NOVO POPUP DE CADASTRO DE NOTA FISCAL*/
	
	/**
	 * Obtem cnpj do fornecedor
	 * @param idFornecedor
	 */
	@Post
	@Path("/obterCnpjFornecedor")
	public void obterCnpjFornecedor(Long idFornecedor) {
		String cnpj = "";
		if(idFornecedor!=null) {
			
			Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(idFornecedor);
		
			if(fornecedor!=null) {
				cnpj = fornecedor.getJuridica().getCnpj();
			}

		}
		result.use(Results.json()).from(cnpj, "result").serialize();
	}
	
	/**
	 * Obtem dados da edição do produto
	 * @param codigo
	 * @param edicao
	 */
	@Post
	@Path("/obterDadosEdicao")
	public void obterDadosEdicao(String codigo, String edicao) {
		
		if (codigo != null 
				&& !codigo.trim().isEmpty() 
				&& edicao != null) {
			
			codigo = StringUtils.leftPad(codigo, 8, '0');
			
			RecebimentoFisicoDTO recebimentoFisicoDTO = this.recebimentoFisicoService.obterRecebimentoFisicoDTO(codigo, edicao);
			
			this.result.use(Results.json()).from(recebimentoFisicoDTO, "result").serialize();
			
		} else {
		
			this.result.use(Results.nothing());
		}
	}
	
	/**
	 * Carrega linha inicial da grid de inputs
	 */
	@Post
	@Path("/montaGridItemNota")
	public void montaGridItemNota() {

		List<RecebimentoFisicoDTO> itemRecebimentoFisicoDTO = new ArrayList<RecebimentoFisicoDTO>();
		
		for (int indice = 0; indice < 1; indice++) {
			
			RecebimentoFisicoDTO recFisicoDTO = new RecebimentoFisicoDTO();
			
			itemRecebimentoFisicoDTO.add(recFisicoDTO);
		}
		
		TableModel<CellModelKeyValue<RecebimentoFisicoDTO>> tableModel =
				new TableModel<CellModelKeyValue<RecebimentoFisicoDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(itemRecebimentoFisicoDTO));
		
		tableModel.setTotal(1);
		
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Valida dados da Nota
	 * @param nota
	 */
	private void validaCabecalhoNota(CabecalhoNotaDTO nota){
		
		//VALIDAÇÃO CABEÇALHO
		if (nota.getFornecedor()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Fornecedor] é obrigatório!");
		}
		
		if (nota.getCnpj()==null || "".equals(nota.getCnpj())){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Cnpj] é obrigatório!");
		}
		
		if(nota.getNumeroNotaEnvio() == null) {

			if (nota.getNumero()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Nota Fiscal] é obrigatório!");
			}
			
			if (nota.getSerie()==null || "".equals(nota.getSerie())){
				throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Série] é obrigatório!");
			}
			
		}
		
		if (nota.getDataEmissao()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Data Emissão] é obrigatório!");
		}
		
		if (nota.getDataEntrada()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Data Entrada] é obrigatório!");
		}
		
		if (nota.getValorTotal()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Valor Total] é obrigatório!");
		}
	}
	
	/**
	 * Valida Itens da Nota
	 * @param itens
	 */
	private void validaItensNota(List<RecebimentoFisicoDTO> itens){

		//VALIDAÇÃO ITENS
		if (itens!=null && itens.size() > 0){
		
			int linha=0;
			for (RecebimentoFisicoDTO item:itens){
			     
				linha++;
				
				if (item.getCodigoProduto()==null || "".equals(item.getCodigoProduto())){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Codigo] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getNomeProduto()==null || "".equals(item.getNomeProduto())){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Produto] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getEdicao()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Edição] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getQtdFisico()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Qtde. Nota] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getQtdPacote()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Qtde. Pcts] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getQtdExemplar()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Qtde. Exems] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getPrecoDesconto()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Preço Desc.] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getDiferenca()==null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Diferença] do ítem "+linha+" é obrigatório!");
				}
				
				if (item.getValorTotal()==null && item.getValorTotalString() == null){
					throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Valor] do ítem "+linha+" é obrigatório!");
				}
			}
		}
		else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há ítens na nota!");
		}
	}
	
	@Post
	@Path("/validarValorTotalNotaFiscal")
	public void validarValorTotalNotaFiscal(CabecalhoNotaDTO nota, List<RecebimentoFisicoDTO> itens) {
		
		this.validaItensNota(itens);
		
		if(nota != null && nota.getValorTotal() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O valor total da nota deve ser maior que 0.");
		}
		
		if (nota.getNumeroNotaEnvio() != null && nota.getFornecedor() != null && nota.getDataEmissao() != null){
			if (this.notaFiscalService.existeNotaFiscalEntradaFornecedor(nota.getNumeroNotaEnvio(),
					this.pessoaJuridicaService.buscarIdPessoaJuridicaPorIdForncedor(nota.getFornecedor()),
					nota.getDataEmissao())){
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Nota de envio já cadastrada, favor utilizar o recebimento automático.");
			}
		}
		
		BigDecimal valorInformadoNotaFiscal = CurrencyUtil.getBigDecimal(nota.getValorTotal());
				
		BigDecimal totalItem = BigDecimal.ZERO;
		
		for (RecebimentoFisicoDTO recebimento : itens) {
			
			totalItem = totalItem.add(CurrencyUtil.getBigDecimal(recebimento.getValorTotalString()));
			
	    }
		
		if (totalItem.compareTo(valorInformadoNotaFiscal) != 0) {
			
			result.use(CustomJson.class).from(
				new ValidacaoException(
					TipoMensagem.WARNING, 
					"Valor total da [Nota] não confere com o valor total dos [Itens]. Deseja prosseguir?")
			).serialize();
			
		} else {
			
			result.use(Results.json()).from("").serialize();
		}
	}
	
	/**
	 * Inclui nota e itens
	 * @param nota
	 * @param itens
	 */
	@Post
	@Path("/incluirNota")
	public void incluirNota(CabecalhoNotaDTO nota, List<RecebimentoFisicoDTO> itens) {

		this.validaCabecalhoNota(nota);
		this.validaItensNota(itens);
		
		Fornecedor fornecedor = fornecedorService.obterFornecedorPorId(nota.getFornecedor());
		
		NotaFiscalEntradaFornecedor notaFiscal = new NotaFiscalEntradaFornecedor();
		
		notaFiscal.setNumeroNotaEnvio(nota.getNumeroNotaEnvio());
		notaFiscal.setNumero(nota.getNumero());
		notaFiscal.setSerie(nota.getSerie());
		notaFiscal.setDataEmissao(nota.getDataEmissao());
		notaFiscal.setDataExpedicao(nota.getDataEntrada());
		notaFiscal.setValorInformado(CurrencyUtil.converterValor(nota.getValorTotal()));
		notaFiscal.setChaveAcesso(nota.getChaveAcesso());
		
		notaFiscal.setFornecedor(fornecedor);
		
		final long codigoTipoNotaFiscalRemessaMercadoriaConsignacao = 5L;
		
		notaFiscal.setTipoNotaFiscal(
			this.tipoNotaService.obterPorId(codigoTipoNotaFiscalRemessaMercadoriaConsignacao));
		
        notaFiscal.setValorDesconto(BigDecimal.ZERO);
        
        notaFiscal.setStatusNotaFiscal(StatusNotaFiscalEntrada.RECEBIDA);
		
		notaFiscal.setEmitente(fornecedor.getJuridica());
		
		BigDecimal totalItem = BigDecimal.ZERO;
		
		ProdutoEdicao pe = null;
		
		for (RecebimentoFisicoDTO item : itens) {
		    
			pe = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(item.getCodigoProduto(), Long.toString(item.getEdicao()));
		    
			item.setIdProdutoEdicao(pe.getId());
		    
			item.setOrigemItemNota(Origem.MANUAL);
		    
			item.setTipoLancamento(TipoLancamento.LANCAMENTO); 
            
		    totalItem = totalItem.add(item.getValorTotal());
		    
	    }
		
		notaFiscal.setValorLiquido(totalItem);
		
		notaFiscal.setValorBruto(totalItem);
		
		notaFiscal.setOrigem(Origem.MANUAL);
		
		recebimentoFisicoService.validarExisteNotaFiscal(notaFiscal);
	    
		recebimentoFisicoService.confirmarRecebimentoFisico(getUsuarioLogado(), notaFiscal, itens, new Date(), true);

		List<String> listaMensagens = new ArrayList<String>();
		listaMensagens.add("Nota fiscal cadastrada com sucesso.");
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, listaMensagens),"result").recursive().serialize();
	}
	
	
}
