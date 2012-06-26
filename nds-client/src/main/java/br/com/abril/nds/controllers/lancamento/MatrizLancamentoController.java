package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.DetalheProdutoLancamentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizLancamentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.LancamentoVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
public class MatrizLancamentoController {

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private MatrizLancamentoService matrizLancamentoService;
		
	@Autowired 
	private Localization localization;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FORMATO_DATA = "dd/MM/yyyy";
	
	private static final String CAMPO_REQUERIDO_KEY = "required_field";
	private static final String CAMPO_MAIOR_IGUAL_KEY = "validator.must.be.greaterEquals";

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizBalanceamento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO = "balanceamento";
	
	@Path("/matrizLancamento")
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarData(new Date(), FORMATO_DATA);
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}
	
	@Post
	public void obterMatrizLancamento(Date dataLancamento, List<Long> idsFornecedores) {
		
		validarDadosPesquisa(dataLancamento, idsFornecedores);
		
		FiltroLancamentoDTO filtro = configurarFiltropesquisa(dataLancamento, idsFornecedores);
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				this.obterBalanceamentoRecolhimento(filtro);
				
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoLancamento);
						
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	
	
	@Post
	public void obterGridMatrizLancamento(String sortorder, String sortname, int page, int rp) {
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao();
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		filtro.setTotalRegistrosEncontrados(listaProdutoBalanceamento.size());
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			List<LancamentoVO> listaProdutoBalanceamentoVO = processarBalanceamento(listaProdutoBalanceamento,filtro.getPaginacao());
			
			Double valorTotal = 0.0;
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoBalanceamento) {
				
				valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
				
			}
			
			TableModel<CellModelKeyValue<LancamentoVO>> tm = new TableModel<CellModelKeyValue<LancamentoVO>>();
			List<CellModelKeyValue<LancamentoVO>> cells = CellModelKeyValue
					.toCellModelKeyValue(listaProdutoBalanceamentoVO);
			
			tm.setRows(cells);
			tm.setPage(page);
			tm.setTotal(filtro.getTotalRegistrosEncontrados());
			
			Object[] resultado = {tm, CurrencyUtil.formatarValor(valorTotal)};
			result.use(Results.json()).withoutRoot().from(resultado).serialize();
			
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
		
	}
	
	public List<ProdutoLancamentoDTO> getProdutoLancamentoDTOFromMatrizSessao() {
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = 
				(BalanceamentoLancamentoDTO) session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO);
		
		if (balanceamentoLancamento == null
				|| balanceamentoLancamento.getMatrizLancamento() == null
				|| balanceamentoLancamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}		

		List<ProdutoLancamentoDTO> listaProdutoBalanceamento =
			new ArrayList<ProdutoLancamentoDTO>();
		
		
		
		for (Map.Entry<Date,List<ProdutoLancamentoDTO>> entry :
			balanceamentoLancamento.getMatrizLancamento().entrySet()) {
		
			listaProdutoBalanceamento.addAll(entry.getValue());
		}
		
		return listaProdutoBalanceamento;
	}
	
	@Post
	public void confirmarMatrizLancamento() {
		
		// TODO: obter a matriz da sessão
		
		// TODO: chamar a service de confirmação
	}
	
	@Post
	public void voltarConfiguracaoOriginal() {
		
		// TODO: montar a matriz inicial e setar na sessão
	}
	
	@Post
	public void reprogramarLancamentosSelecionados(List<ProdutoLancamentoDTO> listaProdutoLancamento,
												   String novaDataFormatada, String dataAntigaFormatada) {
		
		// TODO: reprogramar os lançamentos selecionados
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}
	
	@Post
	public void reprogramarLancamentoUnico(ProdutoLancamentoDTO produtoLancamento,
										   String dataAntigaFormatada) {
		
		// TODO: reprogramar o lançamento informado
		
		// TODO: atualizar a matriz q estava na sessão
		
		// TODO: setar a matriz na sessão
	}	
	
	/**
	 * Obtém o resumo do período de balanceamento de lançamento.
	 */
	private ResultadoResumoBalanceamentoVO obterResumoBalanceamentoLancamento(
											BalanceamentoLancamentoDTO balanceamentoLancamento) {
		
		// TODO: montar o resumo de balanceamento de lançamento
		
		return null;
	}
	
	private List<LancamentoVO> processarBalanceamento(List<ProdutoLancamentoDTO> listaProdutoLancamento,
										PaginacaoVO paginacao) {
		
		List<LancamentoVO> listaProdutoBalanceamentoVO =
				new LinkedList<LancamentoVO>();
			
		for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoLancamento) {

			listaProdutoBalanceamentoVO.add(getVoProdutoBalanceamento(produtoLancamentoDTO));
		}
		
		listaProdutoBalanceamentoVO =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoBalanceamentoVO, paginacao, paginacao.getSortColumn());
					
		return listaProdutoBalanceamentoVO;
	}
	
	
	private LancamentoVO getVoProdutoBalanceamento(
			ProdutoLancamentoDTO produtoLancamentoDTO) {

		
		LancamentoVO produtoBalanceamentoVO = new LancamentoVO();
		
		produtoBalanceamentoVO.setCodigoProduto(produtoLancamentoDTO.getCodigoProduto());
		
		produtoBalanceamentoVO.setNovaData(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getNovaDataLancamento()));
		
		produtoBalanceamentoVO.setDataPrevisto(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataLancamentoPrevista()));
		
		produtoBalanceamentoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoLancamentoDTO.getDataRecolhimentoPrevista()));
		
		produtoBalanceamentoVO.setId(produtoLancamentoDTO.getIdLancamento());
		
		produtoBalanceamentoVO.setNomeFornecedor(produtoLancamentoDTO.getFornecedor());
		
		if(produtoLancamentoDTO.getParcial() == null)
			produtoBalanceamentoVO.setLancamento("Lancamento");
		else
			produtoBalanceamentoVO.setLancamento(produtoLancamentoDTO.getParcial().getDescricao());
		
		produtoBalanceamentoVO.setNomeProduto(produtoLancamentoDTO.getNomeProduto());
		produtoBalanceamentoVO.setNumEdicao(produtoLancamentoDTO.getNumeroEdicao());
		
		produtoBalanceamentoVO.setPacotePadrao(produtoLancamentoDTO.getPacotePadrao());
		
		produtoBalanceamentoVO.setPreco(CurrencyUtil.formatarValor(produtoLancamentoDTO.getPrecoVenda()));
		
		produtoBalanceamentoVO.setReparte(produtoLancamentoDTO.getRepartePrevisto().toString());
		
		produtoBalanceamentoVO.setTotal(CurrencyUtil.formatarValor(produtoLancamentoDTO.getValorTotal()));
		
		if(produtoLancamentoDTO.getReparteFisico()==null)
			produtoBalanceamentoVO.setFisico(0);
		else
			produtoBalanceamentoVO.setFisico(produtoLancamentoDTO.getReparteFisico().intValue());
		
		produtoBalanceamentoVO.setCancelamentoGD(produtoLancamentoDTO.getStatusLancamento().equals(StatusLancamento.CANCELADO_GD));
		
		if(produtoLancamentoDTO.getNumeroReprogramacoes() == null)
			produtoBalanceamentoVO.setReprogramacoesExcedidas(false);
		else
			produtoBalanceamentoVO.setReprogramacoesExcedidas(produtoLancamentoDTO.getNumeroReprogramacoes() >= Constantes.NUMERO_REPROGRAMACOES_LIMITE);
				
		produtoBalanceamentoVO.setEstudoFechado(produtoLancamentoDTO.isPossuiEstudo());
		
		produtoBalanceamentoVO.setSemFisico(!produtoLancamentoDTO.isPossuiRecebimentoFisico());
		
		//TODO - Pendente
		produtoBalanceamentoVO.setDistribuicao("");
		
		return produtoBalanceamentoVO;
	}

	//----------------------------------------------------------------------------
	

	/**
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	@Exportable
	public class RodapeDTO {
		@Export(label="Valor Total R$:")
		private String total;
		
		public RodapeDTO(String total) {
			this.total = total;
		}
		
		public String getTotal() {
			return total;
		}
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroLancamentoDTO filtro = obterFiltroSessao();
		
		List<ProdutoLancamentoDTO> listaProdutoBalanceamento = getProdutoLancamentoDTOFromMatrizSessao();
		
		if (listaProdutoBalanceamento != null && !listaProdutoBalanceamento.isEmpty()) {	
			
			List<LancamentoVO> listaProdutoBalanceamentoVO = processarBalanceamento(listaProdutoBalanceamento,filtro.getPaginacao());
			
			Double valorTotal = 0.0;
			
			for (ProdutoLancamentoDTO produtoLancamentoDTO : listaProdutoBalanceamento) {
				
				valorTotal += produtoLancamentoDTO.getValorTotal().doubleValue();
			}
						
			RodapeDTO rodape = new RodapeDTO(CurrencyUtil.formatarValor(valorTotal));
			
			FileExporter.to("matriz_balanceamento", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, rodape, 
					listaProdutoBalanceamentoVO, LancamentoVO.class, this.httpResponse);
		}
	
		
		result.nothing();
	}
	
	/**
	 * Método que obtém o usuário logado
	 * 
	 * @return usuário logado
	 */
	public Usuario getUsuario() {
		//TODO getUsuario
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		return usuario;
	}

	/**
	 * Obtém a matriz de balanceamento de balanceamento.
	 * 
	 * @param dataBalanceamento - data de balanceamento
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * @return - objeto contendo as informações do balanceamento
	 */
	private BalanceamentoLancamentoDTO obterBalanceamentoRecolhimento(FiltroLancamentoDTO filtro) {
		
		/*
		 * TODO: quando o método obterMatrizLancamento for chamado através do botão "Voltar configuração inicial",
		 * deve ser passada a flag " configuracaoInicial" como true
		 */
		
		boolean configuracaoInicial = false;
		
		BalanceamentoLancamentoDTO balanceamento =
			this.matrizLancamentoService.obterMatrizLancamento(filtro, configuracaoInicial);
					
		this.session.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO, balanceamento);
		
		
		if (balanceamento == null
				|| balanceamento.getMatrizLancamento() == null
				|| balanceamento.getMatrizLancamento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamento;
	}
	
	
	/**
	 * Configura o filtro informado na tela e o armazena na sessão.
	 * 
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de identificadores de fornecedores
	 */
	private FiltroLancamentoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		FiltroLancamentoDTO filtro =
			new FiltroLancamentoDTO(dataPesquisa, listaIdsFornecedores);
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE,filtro);
		
		return filtro;
	}
	
	
	/**
	 * Valida os dados da pesquisa.
	 *  
	 * @param numeroSemana - número da semana
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 */
	private void validarDadosPesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");
			
		}
		
		if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	
	/**
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private ResultadoResumoBalanceamentoVO obterResultadoResumoBalanceamento(
											BalanceamentoLancamentoDTO balanceamentoBalanceamento) {
		
		if (balanceamentoBalanceamento == null
				|| balanceamentoBalanceamento.getMatrizLancamento() == null
				|| balanceamentoBalanceamento.getMatrizLancamento().isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoVO>();
		
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoBalanceamento.getMatrizLancamento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = Long.valueOf(listaProdutosRecolhimento.size());
				Long qtdeTitulosParciais = 0L;
				
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
					
					if (produtoBalanceamento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoBalanceamento.getPeso() != null) {
						
						pesoTotal = pesoTotal.add(produtoBalanceamento.getPeso());
					}
					
					if (produtoBalanceamento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoBalanceamento.getValorTotal());
					}
					
					if (produtoBalanceamento.getRepartePrevisto() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoBalanceamento.getRepartePrevisto());
					}
				}
				
				boolean excedeCapacidadeDistribuidor = false;
				
				if (balanceamentoBalanceamento.getCapacidadeDistribuicao() != null) {
				
					excedeCapacidadeDistribuidor =
						(balanceamentoBalanceamento.getCapacidadeDistribuicao()
							.compareTo(qtdeExemplares) == -1);
				}
				
				itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(
					excedeCapacidadeDistribuidor);
				
				itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
				itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
				itemResumoPeriodoBalanceamento.setQtdeExemplares(MathUtil.round(qtdeExemplares, 2));
				itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
				
				itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
				
				itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
			}
			
			resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
		
		resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
		
		resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(
			balanceamentoBalanceamento.getCapacidadeDistribuicao());
		
		return resultadoResumoBalanceamento;
	}
	
	/**
	 * Obtém o filtro para pesquisa da sessão.
	 * 
	 * @return filtro
	 */
	private FiltroLancamentoDTO obterFiltroSessao() {
		
		FiltroLancamentoDTO filtro = (FiltroLancamentoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Filtro para a pesquisa não encontrado!");
		}
		
		return filtro;
	}
	
	@Get
	public void resumoPeriodo(Date dataInicial, List<Long> idsFornecedores) {
		
		verificarCamposObrigatorios(dataInicial, idsFornecedores);
		List<ResumoPeriodoBalanceamentoDTO> dtos = matrizLancamentoService
				.obterResumoPeriodo(dataInicial, idsFornecedores);
		result.use(Results.json()).withoutRoot().from(dtos).serialize();
	}

	private void verificarCamposObrigatorios(Date data,
			List<Long> idsFornecedores) {
		Date atual = DateUtil.removerTimestamp(new Date());
		List<String> mensagens = new ArrayList<String>();
		if (idsFornecedores == null || idsFornecedores.isEmpty()) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Fornecedor"));
		}
		if (data == null) {
			mensagens.add(localization.getMessage(CAMPO_REQUERIDO_KEY,
					"Data de Lançamento Matriz/Distribuidor"));
		} else if (data.before(atual)) {
			mensagens.add(localization.getMessage(CAMPO_MAIOR_IGUAL_KEY,
					"Data de Lançamento Matriz/Distribuidor", DateUtil.formatarDataPTBR(atual)));
		}
		if (!mensagens.isEmpty()) {
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, mensagens);
			throw new ValidacaoException(validacao);
		}
	}

	
	/**
	 * Obtem detalhes de produto edição
	 * @param codigoProduto
	 */
	@Post
	public void obterDetalheProduto(Long codigoProduto){
		
		DetalheProdutoLancamentoVO produtoLancamentoVO = null;
		
		BalanceamentoLancamentoDTO balanceamentoLancamento = (BalanceamentoLancamentoDTO) this.session.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO);
	
		for (Map.Entry<Date, List<ProdutoLancamentoDTO>> entry : balanceamentoLancamento.getMatrizLancamento().entrySet()) {
			
            List<ProdutoLancamentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				for (ProdutoLancamentoDTO produtoBalanceamento : listaProdutosRecolhimento) {
				    
					if(produtoBalanceamento.getCodigoProduto().equals(codigoProduto.toString())){
					    
						produtoLancamentoVO = new DetalheProdutoLancamentoVO(produtoBalanceamento.getIdProdutoEdicao(),
										                                     produtoBalanceamento.getNomeProduto(),
										                                     produtoBalanceamento.getCodigoProduto(),
										                                     produtoBalanceamento.getPrecoVenda(),
										                                     produtoBalanceamento.getPrecoComDesconto(),
										                                     produtoBalanceamento.getFornecedor(),
										                                     produtoBalanceamento.getCodigoEditor(),
										                                     produtoBalanceamento.getNomeEditor(),
										                                     produtoBalanceamento.getChamadaCapa(),
										                                     (produtoBalanceamento.isPossuiBrinde()?"Sim":"Não"),
										                                     produtoBalanceamento.getPacotePadrao()
										                                     );

					    break;
				    }
	
				} 
			}	
		}
		if (produtoLancamentoVO!=null){
		    this.result.use(Results.json()).from(produtoLancamentoVO, "result").recursive().serialize();
		}
		else{
			result.nothing();
		}
	}

}

