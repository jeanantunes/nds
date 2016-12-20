package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DebitoCreditoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoletoAvulsoDTO;
import br.com.abril.nds.dto.DebitoCreditoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MovimentoFinanceiroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO;
import br.com.abril.nds.dto.filtro.FiltroFechamentoEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroDebitoCreditoDTO.ColunaOrdenacao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.TipoEdicao;
import br.com.abril.nds.model.cadastro.BaseCalculo;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.MovimentoFinanceiroCota;
import br.com.abril.nds.model.financeiro.TipoMovimentoFinanceiro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DebitoCreditoCotaService;
import br.com.abril.nds.service.DocumentoCobrancaService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.MovimentoFinanceiroCotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.TipoMovimentoFinanceiroService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/financeiro/debitoCreditoCota")
@Rules(Permissao.ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA)
public class DebitoCreditoCotaController extends BaseController{

	@Autowired
	private Result result;
	
	@Autowired
	private CotaService cotaService;

	@Autowired
	private TipoMovimentoFinanceiroService tipoMovimentoFinanceiroService;
	
	@Autowired
	private MovimentoFinanceiroCotaService movimentoFinanceiroCotaService;
	
	@Autowired
	private DebitoCreditoCotaService debitoCreditoCotaService;

	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
    @Autowired
    private FechamentoEncalheService fechamentoEncalheService;
	
    @Autowired
    private DocumentoCobrancaService documentoCobrancaService;
    
	private static List<ItemDTO<Long,String>> listaRoteiros =  new ArrayList<ItemDTO<Long,String>>();

	private static List<ItemDTO<Long,String>> listaRotas =  new ArrayList<ItemDTO<Long,String>>();

	private static final String FILTRO_SESSION_ATTRIBUTE = "pesquisaDebitoCreditoCota";

	@Path("/")
	public void index() { 
		preencherComboTipoMovimento();
		preencherComboBaseCalculo();
		preencherComboBox();
	}

	private void preencherComboTipoMovimento() {

		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiro = 
				this.tipoMovimentoFinanceiroService.obterTipoMovimentosFinanceirosCombo(false);
		List<TipoMovimentoFinanceiro> tiposMovimentoFinanceiroMain = 
				this.tipoMovimentoFinanceiroService.obterTipoMovimentosFinanceirosCombo(true);

		this.result.include("tiposMovimentoFinanceiro", tiposMovimentoFinanceiro);
		this.result.include("tiposMovimentoFinanceiroMain", tiposMovimentoFinanceiroMain);
	}

	/**
	 * Obtém lista de Bases de Cálculos para popular combo da view
	 */
	private void preencherComboBaseCalculo() {
		List<BaseCalculo> basesCalculo = new ArrayList<BaseCalculo>();
		for(BaseCalculo item:BaseCalculo.values()){
			basesCalculo.add(item);
		}
		this.result.include("basesCalculo", basesCalculo);
	}
	
	/**
	 * Obtém lista de Boxes para popular combo da view
	 */
	private void preencherComboBox() {
		List<Box> boxes = this.boxService.buscarTodos(null);
		this.result.include("boxes", boxes);
	}

	/**
	 * Obtém lista de Roteiros do Box para popular combo da view
	 * @param idBox
	 */
	@Post
	@Path("/obterRoteirosBox")
	public void obterRoteirosBox(Long idBox){
		listaRoteiros.clear();
		
		List<Roteiro> roteiros = this.roteirizacaoService.buscarRoteiroDeBox(idBox);
		for(Roteiro item:roteiros){
			listaRoteiros.add(new ItemDTO<Long,String>(item.getId(), item.getDescricaoRoteiro()));
		}
		result.use(Results.json()).from(listaRoteiros, "result").recursive().serialize();
	}
	
	/**
	 * Obtém lista de Rotas do Roteiro para popular combo da view
	 * @param idRoteiro
	 */
	@Post
	@Path("/obterRotasRoteiro")
	public void obterRotasRoteiro(Long idRoteiro){
		listaRotas.clear();
		if (idRoteiro!=null){
			List<Rota> rotas = this.roteirizacaoService.buscarRotasPorRoteiro(idRoteiro);
			for(Rota item:rotas){
				listaRotas.add(new ItemDTO<Long,String>(item.getId(), item.getDescricaoRota()));
			} 
		}
		result.use(Results.json()).from(listaRotas, "result").recursive().serialize();
	}
	
	/**
	 * Obtém valor calculado do movimento com base no faturamento da cota, caso o tipo de movimento for por faturamento
	 * @param tipoMovimento
	 */
	@Post
    @Path("/obterGrupoFaturamento")
	public void obterGrupoFaturamento(Long idTipoMovimento){
		TipoMovimentoFinanceiro tipoMovimento = null;
		if (idTipoMovimento!=null){
		    tipoMovimento = this.tipoMovimentoFinanceiroService.obterTipoMovimentoFincanceiroPorId(idTipoMovimento);
		}
		this.result.use(Results.json()).from(tipoMovimento!=null?tipoMovimento.getGrupoMovimentoFinaceiro():"", "result").recursive().serialize();
	}
	
	/**
     * Retorna lançamentos pré-configurados com as cotas e informações referentes à percentual sobre faturamento para preencher a grid da view
	 * @param idBox
	 * @param idRoteiro
	 * @param idRota
	 * @param grupoMovimento
	 * @param percentual
	 * @param baseCalculo
	 * @param dataPeriodoInicial
	 * @param dataPeriodoFinal
	 */
    @Post
	@Path("/obterInformacoesParaLancamento")
	public void obterInformacoesParaLancamento(Long idBox, 
			                                   Long idRoteiro, 
			                                   Long idRota,
			                                   GrupoMovimentoFinaceiro grupoMovimento,
			                                   BigDecimal percentual,
			                                   BaseCalculo baseCalculo,
			                                   Date dataPeriodoInicial,
			                                   Date dataPeriodoFinal){
    	
		if (grupoMovimento == GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO){
    		
			if (percentual == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Percentual].");
			}
			
			if (baseCalculo == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar a [Base de Cálculo].");
			}
			
			if (dataPeriodoInicial == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Período para Cálculo].");
			}
			
			if (dataPeriodoFinal == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Período para Cálculo].");
			}
			
			if (DateUtil.isDataInicialMaiorDataFinal(dataPeriodoInicial, dataPeriodoFinal)) {
				throw new ValidacaoException(TipoMensagem.WARNING, "A [Data Final] deve susceder a [Data Inicial].");
			}
    	}

		List<DebitoCreditoDTO> listaDebitoCredito = this.debitoCreditoCotaService.obterDadosLancamentoPorBoxRoteiroRota(idBox, idRoteiro, idRota, percentual, baseCalculo, dataPeriodoInicial, dataPeriodoFinal);
        
		if (listaDebitoCredito==null || listaDebitoCredito.size()<=0){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma informação encontrada para os filtros escolhidos.");
		}
		
		int qtd = this.debitoCreditoCotaService.obterQuantidadeCotasPorBoxRoteiroRota(idBox, idRoteiro, idRota);
        
		TableModel<CellModelKeyValue<DebitoCreditoDTO>> tableModel =
				new TableModel<CellModelKeyValue<DebitoCreditoDTO>>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCredito));
		tableModel.setTotal(qtd);
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }
    
    /**
     * Retorna dados da cota referentes à percentual sobre faturamento
     * @param numeroCota
     * @param grupoMovimento
     * @param percentual
     * @param baseCalculo
     * @param dataPeriodoInicial
     * @param dataPeriodoFinal
     */
    @Post
	@Path("/obterInformacoesParaLancamentoIndividual")
	public void obterInformacoesParaLancamentoIndividual(Integer numeroCota,
						                                 GrupoMovimentoFinaceiro grupoMovimento,
						                                 BigDecimal percentual,
						                                 BaseCalculo baseCalculo,
						                                 Date dataPeriodoInicial,
						                                 Date dataPeriodoFinal,
						                                 Long index){
    		
		if (grupoMovimento == GrupoMovimentoFinaceiro.DEBITO_SOBRE_FATURAMENTO){
    		
			if (percentual == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Percentual].");
			}
			
			if (baseCalculo == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar a [Base de Cálculo].");
			}
			
			if (dataPeriodoInicial == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Período para Cálculo].");
			}
			
			if (dataPeriodoFinal == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o lançamento baseado no faturamento é obrigatório informar o [Período para Cálculo].");
			}
			
			if (DateUtil.isDataInicialMaiorDataFinal(dataPeriodoInicial, dataPeriodoFinal)) {
				throw new ValidacaoException(TipoMensagem.WARNING, "A [Data Final] deve susceder a [Data Inicial].");
			}
    	}

		DebitoCreditoDTO debitoCredito = this.debitoCreditoCotaService.obterDadosLancamentoPorCota(numeroCota, percentual, baseCalculo, dataPeriodoInicial, dataPeriodoFinal, index);

		this.result.use(Results.json()).from(debitoCredito!=null?debitoCredito:"", "result").recursive().serialize();
 
    }

    
	public void buscarCotaPorNumero(Integer numeroCota) {
		
		Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
		
		if (cota == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Cota inexistente.");
		}

		Pessoa pessoa = cota.getPessoa();

		String nomeCota = null;

		if (pessoa instanceof PessoaFisica) {

			nomeCota = ((PessoaFisica) pessoa).getNome();

		} else if (pessoa instanceof PessoaJuridica) {

			nomeCota = ((PessoaJuridica) pessoa).getRazaoSocial();
		}

		this.result.use(Results.json()).from(nomeCota, "result").recursive().serialize();
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroDebitoCreditoDTO filtro) {

		FiltroDebitoCreditoDTO filtroSession = (FiltroDebitoCreditoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
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
		
		FiltroDebitoCreditoDTO filtro = this.obterFiltroExportacao();
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaService.obterMovimentosFinanceiroCota(filtro);
		
		List<MovimentoFinanceiroCotaDTO> listaExport = obterListaMovimentoFinanceiroCotaExportacao(listaMovimentoFinanceiroCota);
		
		BigDecimal valorSumarizado = 
				this.movimentoFinanceiroCotaService.obterSomatorioValorMovimentosFinanceiroCota(filtro);
		
		DebitoCreditoVO debitoCreditoVO = new DebitoCreditoVO();

		if (valorSumarizado != null) {
		
			debitoCreditoVO.setValorTotal(new DecimalFormat("#,###.00").format(valorSumarizado));
		}

		FileExporter.to("debito-credito-cota", fileType)
			.inHTTPResponse(
					this.getNDSFileHeader(), filtro, debitoCreditoVO, 
					listaExport, MovimentoFinanceiroCotaDTO.class, this.httpResponse);
	}
	
	private List<MovimentoFinanceiroCotaDTO> obterListaMovimentoFinanceiroCotaExportacao(List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota) {
		
		List<MovimentoFinanceiroCotaDTO> listaExport = new LinkedList<MovimentoFinanceiroCotaDTO>();
		
		if (listaMovimentoFinanceiroCota == null || listaMovimentoFinanceiroCota.isEmpty()) {
			
			return listaExport;
		}
		
		for (MovimentoFinanceiroCota movimeto : listaMovimentoFinanceiroCota) {
			
			MovimentoFinanceiroCotaDTO itemExport = new MovimentoFinanceiroCotaDTO();
			
			itemExport.setAprovacaoAutomatica(movimeto.isAprovadoAutomaticamente());
			
			itemExport.setCota(movimeto.getCota());
			
			itemExport.setDataCriacao(movimeto.getDataCriacao());
			
			itemExport.setDataVencimento(movimeto.getData());
			
			itemExport.setLancamentoManual(movimeto.isLancamentoManual());
			
			itemExport.setObservacao(movimeto.getObservacao());
			
			itemExport.setTipoMovimentoFinanceiro((TipoMovimentoFinanceiro) movimeto.getTipoMovimento());
			
			itemExport.setUsuario(movimeto.getUsuario());
			
			itemExport.setValor(movimeto.getValor());
			
			listaExport.add(itemExport);
			
		}
		
		return listaExport;		
	}
	

	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroDebitoCreditoDTO obterFiltroExportacao() {
		
		FiltroDebitoCreditoDTO filtro = 
			(FiltroDebitoCreditoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			carregarDescricoesFiltro(filtro);
			
		}
		
		return filtro;
	}
	
	private void carregarDescricoesFiltro(FiltroDebitoCreditoDTO filtro) {
		
		filtro.setDescricaoTipoMovimento("");
		
		filtro.setNomeCota("");
		
		if (filtro.getIdTipoMovimento() != null) {
			
			TipoMovimentoFinanceiro tipoMovimentoFinac =  
					tipoMovimentoFinanceiroService.
					obterTipoMovimentoFincanceiroPorId(filtro.getIdTipoMovimento());
			
			if(tipoMovimentoFinac!=null) {
				filtro.setDescricaoTipoMovimento(tipoMovimentoFinac.getDescricao());
			}
			
			
		}

		if (filtro.getNumeroCota() != null) {
			
			Cota cota = cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
			
			if(cota!=null) {
				
				String descricao = "";
				
				if(cota.getPessoa() instanceof PessoaJuridica) {
					descricao = ((PessoaJuridica)cota.getPessoa()).getRazaoSocial();
				} else {
					descricao = ((PessoaFisica)cota.getPessoa()).getNome();
				}
				
				filtro.setNomeCota(descricao);
			}
		}
	}
	
	@Post
	public void pesquisarDebitoCredito(FiltroDebitoCreditoDTO filtroDebitoCredito, 
									   String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		ColunaOrdenacao colunaOrdenacao = Util.getEnumByStringValue(ColunaOrdenacao.values(), sortname.trim());

		filtroDebitoCredito.setPaginacao(paginacao);
		filtroDebitoCredito.setColunaOrdenacao(colunaOrdenacao);

		validarFiltroDebitoCredito(filtroDebitoCredito);
		
		List<MovimentoFinanceiroCota> listaMovimentoFinanceiroCota = 
				this.movimentoFinanceiroCotaService.obterMovimentosFinanceiroCotaUsuario(filtroDebitoCredito);
		
		if (listaMovimentoFinanceiroCota == null || listaMovimentoFinanceiroCota.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		Integer quantidadeRegistros =  
				this.movimentoFinanceiroCotaService.obterContagemMovimentosFinanceiroCota(filtroDebitoCredito);

		BigDecimal valorSumarizado = 
				this.movimentoFinanceiroCotaService.obterSomatorioValorMovimentosFinanceiroCota(filtroDebitoCredito);

		tratarFiltro(filtroDebitoCredito);

		TableModel<CellModel> tableModel = getTableModel(listaMovimentoFinanceiroCota);

		tableModel.setPage(page);
		tableModel.setTotal(quantidadeRegistros);

		DebitoCreditoVO debitoCreditoVO = new DebitoCreditoVO();

		debitoCreditoVO.setTableModel(tableModel);
		
		if (valorSumarizado != null) {
			
			debitoCreditoVO.setValorTotal(new DecimalFormat("#,###.00").format(valorSumarizado));
		}

		this.result.use(Results.json()).withoutRoot().from(debitoCreditoVO).recursive().serialize();
	}

	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO)
	public void removerMovimentoFinanceiroCota(Long idMovimento) {

		if (idMovimento == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "ID do movimento inválido.");
		}

		this.movimentoFinanceiroCotaService.removerMovimentoFinanceiroCota(idMovimento);

		List<String> listaMensagens = new ArrayList<String>();
		
		listaMensagens.add("Exclusão realizado com sucesso.");

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, listaMensagens), "result").recursive().serialize();
	}

	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO)
	public void criarMovimentoFincanceiroCota(List<DebitoCreditoDTO> listaNovosDebitoCredito, Long idTipoMovimento) {

		validarPreenchimentoCampos(listaNovosDebitoCredito, idTipoMovimento);
		
		for (DebitoCreditoDTO debito : listaNovosDebitoCredito) {
			
			boolean isFechamentoEncalheGeral = existeFechamentoEncalhePorDataOperacao(DateUtil.parseDataPTBR(debito.getDataVencimento()));
			
			if(isFechamentoEncalheGeral) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Fechamento de encalhe já realizado na data do movimento");
			}
		}
		
		
		Date dataCriacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		for (DebitoCreditoDTO debitoCredito : listaNovosDebitoCredito) {

			TipoMovimentoFinanceiro tipoMovimentoFinanceiro = new TipoMovimentoFinanceiro();
			
			tipoMovimentoFinanceiro.setId(idTipoMovimento);

			debitoCredito.setTipoMovimentoFinanceiro(tipoMovimentoFinanceiro);

			debitoCredito.setValor(getValorSemMascara(debitoCredito.getValor()));

			debitoCredito.setIdUsuario(this.getUsuarioLogado().getId());
			
			debitoCredito.setId(null);
			
			debitoCredito.setDataCriacao(dataCriacao);
			
			boolean existeFechamentoEncalheCota = fechamentoEncalheService.existeFechamentoEncalhePorCota(DateUtil.parseDataPTBR(debitoCredito.getDataVencimento()), debitoCredito.getNumeroCota());
			
			MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = null;
			
			if(existeFechamentoEncalheCota) {
				movimentoFinanceiroCotaDTO = debitoCreditoCotaService.gerarMovimentoFinanceiroCotaCobrancaDTO(debitoCredito);
			} else {
				movimentoFinanceiroCotaDTO = debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCredito);
				movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.INCLUSAO);				
				this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);
			}
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cadastro realizado com sucesso."), "result").recursive().serialize();
	}

	private boolean existeFechamentoEncalhePorDataOperacao(Date dataVencimento) {
		return this.fechamentoEncalheService.existeFechamentoEncalhePorDataOperacao(dataVencimento);
	}
	
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_DEBITOS_CREDITOS_COTA_ALTERACAO)
	public void editarMovimentoFincanceiroCota(DebitoCreditoDTO debitoCredito) {
		
		validarPreenchimentoCamposEdicao(debitoCredito);
		
		existeMovimentoFinanceiroCota(debitoCredito);
		
		debitoCredito.setIdUsuario(this.getIdUsuario());
		
		debitoCredito.setValor(getValorSemMascara(debitoCredito.getValor()));

		MovimentoFinanceiroCotaDTO movimentoFinanceiroCotaDTO = 
				debitoCreditoCotaService.gerarMovimentoFinanceiroCotaDTO(debitoCredito);

		movimentoFinanceiroCotaDTO.setTipoEdicao(TipoEdicao.ALTERACAO);
		
		this.movimentoFinanceiroCotaService.gerarMovimentosFinanceirosDebitoCredito(movimentoFinanceiroCotaDTO);

		List<String> listaMensagens = new ArrayList<String>();
		
		listaMensagens.add("Alteração realizada com sucesso.");

		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, listaMensagens), "result").recursive().serialize();
	}
	
	@Post
	@Path("/novoMovimento")
	public void carregarNovosMovimentos() {

		List<DebitoCreditoDTO> listaDebitoCredito = new ArrayList<DebitoCreditoDTO>();
		
		for (int indice = 0; indice < 50; indice++) {
			
			DebitoCreditoDTO debitoCreditoDTO = new DebitoCreditoDTO();
			
			debitoCreditoDTO.setId(Long.valueOf(indice));
			
			listaDebitoCredito.add(debitoCreditoDTO);
		}
		
		TableModel<CellModelKeyValue<DebitoCreditoDTO>> tableModel =
				new TableModel<CellModelKeyValue<DebitoCreditoDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCredito));
		
		tableModel.setTotal(50);
		
		tableModel.setPage(1);
		
		this.result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void prepararMovimentoFinanceiroCotaParaEdicao(Long idMovimento) {

		if (idMovimento == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "ID do movimento inválido.");
		}

		MovimentoFinanceiroCota movimentoFinanceiroCota = this.movimentoFinanceiroCotaService.obterMovimentoFinanceiroCotaPorId(idMovimento);

		DebitoCreditoDTO debitoCredito = new DebitoCreditoDTO();
		
		debitoCredito.setPermiteAlteracao(this.debitoCreditoCotaService.isMovimentoEditavel(movimentoFinanceiroCota));

		Pessoa pessoa = movimentoFinanceiroCota.getCota().getPessoa();

		String nomeCota = pessoa instanceof PessoaJuridica ? ((PessoaJuridica) pessoa).getRazaoSocial() : ((PessoaFisica) pessoa).getNome();

		debitoCredito.setDataLancamento(formatField(movimentoFinanceiroCota.getDataCriacao()));
		debitoCredito.setTipoMovimentoFinanceiro((TipoMovimentoFinanceiro) movimentoFinanceiroCota.getTipoMovimento());
		debitoCredito.setDataVencimento(formatField(movimentoFinanceiroCota.getData()));
		debitoCredito.setNumeroCota(movimentoFinanceiroCota.getCota().getNumeroCota());
		debitoCredito.setObservacao(movimentoFinanceiroCota.getObservacao());
		debitoCredito.setValor(CurrencyUtil.formatarValor(movimentoFinanceiroCota.getValor()));
		debitoCredito.setId(movimentoFinanceiroCota.getId());
		debitoCredito.setNomeCota(nomeCota);
		debitoCredito.setIdUsuario(movimentoFinanceiroCota.getUsuario().getId());

		this.result.use(Results.json()).from(debitoCredito, "result").recursive().serialize();
	}
	
	private TableModel<CellModel> getTableModel(List<MovimentoFinanceiroCota> listaDebitoCredito) {

		List<CellModel> listaCellModel = new ArrayList<CellModel>();

		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

		for (MovimentoFinanceiroCota movimentoFinanceiroCota : listaDebitoCredito) {

			boolean isEditavel = this.debitoCreditoCotaService.isMovimentoEditavel(movimentoFinanceiroCota);

			String dataLancamento = 
					formatField(movimentoFinanceiroCota.getDataCriacao());
			String dataVencimento = 
					formatField(movimentoFinanceiroCota.getData());
			String numeroCota = 
					formatField(movimentoFinanceiroCota.getCota().getNumeroCota());
			String tipoMovimento = 
					formatField(movimentoFinanceiroCota.getTipoMovimento().getDescricao());
			String valor = 
					movimentoFinanceiroCota.getValor() == null ? 
							"" : decimalFormat.format(movimentoFinanceiroCota.getValor());
			String observacao = 
					formatField(movimentoFinanceiroCota.getObservacao());

			Pessoa pessoa = movimentoFinanceiroCota.getCota().getPessoa();
			
			String usuario = "";
			 
			 if(movimentoFinanceiroCota!=null && movimentoFinanceiroCota.getUsuario()!=null && movimentoFinanceiroCota.getUsuario().getNome() !=null){
				 usuario = movimentoFinanceiroCota.getUsuario().getNome();
			 }
			
			String nomeCota = pessoa instanceof PessoaJuridica ? 
							  ((PessoaJuridica) pessoa).getRazaoSocial() : ((PessoaFisica) pessoa).getNome();

			CellModel cellModel = new CellModel(
				movimentoFinanceiroCota.getId().intValue(),
				dataLancamento,
				dataVencimento,
				numeroCota,
				nomeCota,
				tipoMovimento,
				valor,
				observacao,
				usuario,
				isEditavel
				
			);

			listaCellModel.add(cellModel);
		}

		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		tableModel.setRows(listaCellModel);
		
		tableModel.setTotal(listaCellModel.size());

		return tableModel;
	}

	private String formatField(Object field) {
		
		if (field instanceof Date) {
			
			return field == null ? "" : DateUtil.formatarDataPTBR((Date) field);
		}

		return field == null ? "" : String.valueOf(field);
	}

	private void validarFiltroDebitoCredito(FiltroDebitoCreditoDTO filtroDebitoCredito) {		

		List<String> listaMensagens = new ArrayList<String>();

		if (filtroDebitoCredito.getDataLancamentoInicio() != null 
				&& filtroDebitoCredito.getDataLancamentoFim() != null
				&& DateUtil.isDataInicialMaiorDataFinal(
						filtroDebitoCredito.getDataLancamentoInicio(), 
						filtroDebitoCredito.getDataLancamentoFim())) {

			listaMensagens.add("A data de lançamento inicial deve ser menor que a data de lançamento final.");

		} else if (filtroDebitoCredito.getDataLancamentoInicio() != null 
				&& filtroDebitoCredito.getDataLancamentoFim() == null) {
			
			listaMensagens.add("O preenchimento da data de lançamento final é obrigatória.");
		
		}  else if (filtroDebitoCredito.getDataLancamentoInicio() == null 
				&& filtroDebitoCredito.getDataLancamentoFim() != null) {
			
			listaMensagens.add("O preenchimento da data de lançamento inicial é obrigatória.");
		}

		if (filtroDebitoCredito.getDataVencimentoInicio() != null 
				&& filtroDebitoCredito.getDataVencimentoFim() != null
				&& DateUtil.isDataInicialMaiorDataFinal(
						filtroDebitoCredito.getDataVencimentoInicio(), 
						filtroDebitoCredito.getDataVencimentoFim())) {

			listaMensagens.add("A data de vencimento inicial deve ser menor que a data de vencimento final.");
		
		} else if (filtroDebitoCredito.getDataVencimentoInicio() != null 
				&& filtroDebitoCredito.getDataVencimentoFim() == null) {
			
			listaMensagens.add("O preenchimento da data de vencimento final é obrigatória.");
		
		} else if (filtroDebitoCredito.getDataVencimentoInicio() == null 
				&& filtroDebitoCredito.getDataVencimentoFim() != null) {
			
			listaMensagens.add("O preenchimento da data de vencimento inicial é obrigatória.");
		}

		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}	

	private void validarPreenchimentoCamposEdicao(DebitoCreditoDTO debitoCredito) {
		
		List<String> listaMensagens = new ArrayList<String>();
	
		
		Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
		
		if (debitoCredito.getNumeroCota() == null) {
			
			listaMensagens.add("O preenchimento do campo [Número da Cota] é obrigatório.");
		}
		
		if (dataVencimento == null) {

			listaMensagens.add("O preenchimento do campo [Data de Vencimento] é obrigatório.");
		
		} else if (DateUtil.isDataInicialMaiorDataFinal(
				DateUtil.adicionarDias(
						this.distribuidorService.obterDataOperacaoDistribuidor() ,1), dataVencimento)) {

			listaMensagens.add("A data de vencimento deve suceder a data de operação atual.");
		}

		if (debitoCredito.getValor() == null) {
			
			listaMensagens.add("O preenchimento do campo [Valor] é obrigatório.");
		
		} else {

			try {

				new BigDecimal(getValorSemMascara(debitoCredito.getValor()));

			} catch(NumberFormatException e) {

				listaMensagens.add("O preenchimento do campo [Valor] é inválido.");
			}
		}

		if (debitoCredito.getTipoMovimentoFinanceiro().getId() == null) {

			listaMensagens.add("O preenchimento do campo [Tipo de Movimento] é obrigatório.");
		}

		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, listaMensagens);

			throw new ValidacaoException(validacao);
		}
	}
	
	private void validarPreenchimentoCampos(List<DebitoCreditoDTO> listaNovosDebitoCredito, Long idTipoMovimento) {
		
		if (idTipoMovimento == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "O preenchimento do campo [Tipo Movimento] é obrigatório.");
		}
		
		if (listaNovosDebitoCredito==null || listaNovosDebitoCredito.size()<=0){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há movimentos para serem lançados.");
		}
		
		List<Long> linhasComErro = new ArrayList<Long>();
		
		String msgsErros = "";
		
		for (DebitoCreditoDTO debitoCredito : listaNovosDebitoCredito) {
			
			long linha = (debitoCredito.getId()+1l);
			
			Date dataVencimento = DateUtil.parseDataPTBR(debitoCredito.getDataVencimento());
			
			if (debitoCredito.getNumeroCota() == null) {
				
				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nInforme o [número] da [Cota] na linha ["+linha+"] !");
			}
			
			Date dataDistrib = this.distribuidorService.obterDataOperacaoDistribuidor();
			
			if (dataVencimento == null) {

				linhasComErro.add(debitoCredito.getId());
			
			} else if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.removerTimestamp(DateUtil.adicionarDias(dataDistrib, 0)), dataVencimento)) {

				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nO campo [Data] deve ser maior ou igual a [Data de Operação: "+DateUtil.formatarDataPTBR(dataDistrib)+"] na linha ["+linha+"] !");
			}

			if (debitoCredito.getValor() == null) {
				
				linhasComErro.add(debitoCredito.getId());
				
				msgsErros += ("\nInforme o [Valor] na linha ["+linha+"] !");
			
			} else {

				try {

					new BigDecimal(getValorSemMascara(debitoCredito.getValor()));

				} catch(NumberFormatException e) {

					linhasComErro.add(debitoCredito.getId());
					
					msgsErros += ("\nInforme um [Valor] válido na linha ["+linha+"] !");
				}
			}		
			
			if (debitoCredito.getDataVencimento()==null){
				
                linhasComErro.add(debitoCredito.getId());
				
                msgsErros += ("\nInforme a [Data] na linha ["+linha+"] !");
			}
		}

		if (!linhasComErro.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Existe(m) movimento(s) preenchido(s) incorretamente.\n"+msgsErros);
					
			validacao.setDados(linhasComErro);
			
			throw new ValidacaoException(validacao);
		}
	}

	private void existeMovimentoFinanceiroCota(DebitoCreditoDTO debitoCredito) {
		
		Long id = debitoCredito.getId();
		
		FiltroDebitoCreditoDTO filtroDebitoCredito = new FiltroDebitoCreditoDTO();
		
		filtroDebitoCredito.setDataVencimentoInicio(DateUtil.parseDataPTBR(debitoCredito.getDataVencimento()));
		filtroDebitoCredito.setDataVencimentoFim(DateUtil.parseDataPTBR(debitoCredito.getDataVencimento()));
		filtroDebitoCredito.setNumeroCota(debitoCredito.getNumeroCota());
		filtroDebitoCredito.setGrupoMovimentosFinanceirosDebitosCreditos(
				Arrays.asList( debitoCredito.getTipoMovimentoFinanceiro().getGrupoMovimentoFinaceiro()));
			
		if (this.movimentoFinanceiroCotaService.existeOutrosMovimentosFinanceiroCota(filtroDebitoCredito, id)) {
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, "Já existe um movimento para esta cota, nesta data.");
			throw new ValidacaoException(validacao);
		}
	}
	
	private String getValorSemMascara(String valor) {

		String chr = String.valueOf(valor.charAt(valor.length()-3));
		if (",".equals(chr)){
		    valor = valor.replaceAll("\\.", "");
		    valor = valor.replaceAll(",", "\\.");
		}
		
		if (".".equals(chr)){
		    valor = valor.replaceAll(",", "");
		}

		return valor;
	}
	
	private Long getIdUsuario(){
		
		return this.usuarioService.getUsuarioLogado().getId();
	}
	
	@Path("/imprimirBoleto")
	public void imprimirBoleto(List<DebitoCreditoDTO> listaNovosDebitoCredito, Long idTipoMovimento) {
    	
    	byte[] arquivo = null; //this.documentoCobrancaService.gerarDocumentoCobrancaBoletoAvulso(listaBoletosAvulso);
    	
    	String nomeArquivo = "boleto-debito-credito"; 
    	
    	try {
			
    		this.httpResponse.setContentType("application/pdf");
    		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=" + nomeArquivo + new Date()+ ".pdf");

    		OutputStream output = this.httpResponse.getOutputStream();
    		output.write(arquivo);

    		this.httpResponse.getOutputStream().close();

    		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS," sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	        
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma informação encontrada para os filtros escolhidos.");
		}

    }
}