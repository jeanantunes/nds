package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ChamadaoVO;
import br.com.abril.nds.client.vo.ResultadoChamadaoVO;
import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO.OrdenacaoColunaChamadao;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ChamadaoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/devolucao/chamadao")
public class ChamadaoController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private ChamadaoService chamadaoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaService cotaService;
	
	private static final String FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE = "filtroPesquisaConsignados";
	
	private static final String QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaConsignados";
	
	@Get
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CHAMADAO)
	public void index() {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			this.carregarComboFornecedores(null);
			
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
	@Get
	@Path("/popularGridFollowUp")
	public void popularGridFollowUp(Integer numeroCota, String data) {			
		result.include("numeroCotaFollowUp", numeroCota);
		result.include("dataCotaFollowUp", data);
		result.forwardTo(ChamadaoController.class).index();
	}
	
	
	@Get
	public void exportar(FileType fileType) throws IOException {
				
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroChamadaoDTO filtroSessao = this.obterFiltroParaExportacao();
		
		ConsultaChamadaoDTO consultaChamadaoDTO = 
				this.chamadaoService.obterConsignados(filtroSessao);
		
		List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO =
				consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO();
		
		ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao = 
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao();
		
		List<ChamadaoVO> listaChamadao = new LinkedList<ChamadaoVO>();
		
		ChamadaoVO chamadaoVO = null;
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaConsignadoCotaChamadaoDTO) {
			
			chamadaoVO = new ChamadaoVO();
			
			chamadaoVO.setCodigo(consignadoCotaChamadao.getCodigoProduto());
			chamadaoVO.setProduto(consignadoCotaChamadao.getNomeProduto());
			chamadaoVO.setEdicao(consignadoCotaChamadao.getNumeroEdicao().toString());
			
			chamadaoVO.setPrecoVenda(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getPrecoVenda()));
			
			chamadaoVO.setPrecoDesconto(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getPrecoDesconto()));
			
			chamadaoVO.setReparte(String.valueOf(consignadoCotaChamadao.getReparte().longValue()));
			chamadaoVO.setFornecedor(consignadoCotaChamadao.getNomeFornecedor());
			
			chamadaoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(consignadoCotaChamadao.getDataRecolhimento()));
			
			chamadaoVO.setValorTotal(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getValorTotal()));
			
			listaChamadao.add(chamadaoVO);
		}
		
		ResultadoChamadaoVO resultadoChamadao = null;
		
		if (resumoConsignadoCotaChamadao != null) {
			
			String valorTotalFormatado = null;
			
			if (resumoConsignadoCotaChamadao.getValorTotal() != null) {
			
				valorTotalFormatado =
					CurrencyUtil.formatarValor(resumoConsignadoCotaChamadao.getValorTotal());
			}
			
			resumoConsignadoCotaChamadao.setQtdProdutosTotal(
				(long) listaConsignadoCotaChamadaoDTO.size());
			
			resultadoChamadao =
				new ResultadoChamadaoVO(null, resumoConsignadoCotaChamadao.getQtdProdutosTotal(),
										resumoConsignadoCotaChamadao.getQtdExemplaresTotal(),
										valorTotalFormatado);
			
		}
		
		FileExporter.to("chamadao", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoChamadao, 
					listaChamadao, ChamadaoVO.class, this.response);
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO obterFiltroParaExportacao() {
		
		FiltroChamadaoDTO filtroSessao =
			(FiltroChamadaoDTO)
				this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {
				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtroSessao.getNumeroCota() != null) {
		
				Cota cota = this.cotaService.obterPorNumeroDaCota(filtroSessao.getNumeroCota());
				
				if (cota != null) {
					
					String nomeCota = null;
					
					if (cota.getPessoa() instanceof PessoaJuridica) {
						
						PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
						
						nomeCota = pessoaJuridica.getRazaoSocial();
					
					} else if (cota.getPessoa() instanceof PessoaFisica) {
						
						PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
						
						nomeCota = pessoaFisica.getNome();
					}
					
					filtroSessao.setNomeCota(nomeCota);
				}
			}
			
			if (filtroSessao.getIdFornecedor() != null) {
				
				Fornecedor fornecedor =
					this.fornecedorService.obterFornecedorPorId(filtroSessao.getIdFornecedor());
				
				if (fornecedor != null) {
					
					filtroSessao.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
				}
			}
		}
		
		return filtroSessao;
	}
	
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
		
		ndsFileHeader.setNomeUsuario(this.obterUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(String codigoProduto) {
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(codigoProduto, null);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial())
			);
		}
			
		return listaFornecedoresCombo;
	}
	
	@Post
	@Path("/pesquisarConsignados")
	public void pesquisarConsignados(Integer numeroCota, String dataChamadaoFormatada, Long idFornecedor,
									 String sortorder, String sortname, int page, int rp) {
		
		this.validarEntradaDadosPesquisa(numeroCota, dataChamadaoFormatada);
		
		Date dataChamadao = DateUtil.parseDataPTBR(dataChamadaoFormatada);
		
		FiltroChamadaoDTO filtro  = 
			this.carregarFiltroPesquisa(numeroCota, dataChamadao, idFornecedor,
										sortorder, sortname, page, rp);
		
		ConsultaChamadaoDTO consultaChamadaoDTO = 
			this.chamadaoService.obterConsignados(filtro);
		
		if (consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO() == null || 
				consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO().isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			PaginacaoUtil.armazenarQtdRegistrosPesquisa(
				this.session,
				QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
				consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO().size());
						
			this.processarConsignados(consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO(), 
									  consultaChamadaoDTO.getResumoConsignadoCotaChamadao(),
									  filtro,
									  consultaChamadaoDTO.getQuantidadeTotalConsignados().intValue());
		}
		
		result.use(Results.json());
	}
	
	@Post
	@Path("/confirmarChamadao")
	public void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao, boolean chamarTodos) {
		
		if (!chamarTodos && listaChamadao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar pelo menos um produto para realizar o chamadão!");
		}
		
		FiltroChamadaoDTO filtroSessao =
			(FiltroChamadaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		FiltroChamadaoDTO filtro  = 
			this.carregarFiltroConfirmacao(filtroSessao.getNumeroCota(),
										   filtroSessao.getDataChamadao(),
										   filtroSessao.getIdFornecedor());
		
		chamadaoService.confirmarChamadao(listaChamadao, filtro, chamarTodos, obterUsuario());
		
		result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Chamadão realizado com sucesso!"),
							"result").recursive().serialize();
	}
	
	/**
	 * Processa o resultado dos consignados da cota.
	 *  
	 * @param listaConsignadoCotaChamadaoDTO - lista de consignados da cota
	 * @param resumoConsignadoCotaChamadao - resumo dos consignados da cota
	 * @param filtro - filtro da pesquisa
	 * @param qtdeTotalRegistros - quantidade total de registros
	 */
	private void processarConsignados(List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO,
									  ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao,
									  FiltroChamadaoDTO filtro,
									  Integer qtdeTotalRegistros) {
		
		List<ChamadaoVO> listaChamadao = new LinkedList<ChamadaoVO>();
		
		ChamadaoVO chamadaoVO = null;
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaConsignadoCotaChamadaoDTO) {
			
			chamadaoVO = new ChamadaoVO();
			
			chamadaoVO.setCodigo(consignadoCotaChamadao.getCodigoProduto());
			chamadaoVO.setProduto(consignadoCotaChamadao.getNomeProduto());
			chamadaoVO.setEdicao(consignadoCotaChamadao.getNumeroEdicao().toString());
			
			chamadaoVO.setPrecoVenda(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getPrecoVenda()));
			
			if (consignadoCotaChamadao.getPrecoDesconto() != null) {
				
				chamadaoVO.setPrecoDesconto(
					CurrencyUtil.formatarValor(consignadoCotaChamadao.getPrecoDesconto()));
				
			} else {
				chamadaoVO.setPrecoDesconto("");
			}
			
			chamadaoVO.setReparte(String.valueOf(consignadoCotaChamadao.getReparte().longValue()));
			chamadaoVO.setFornecedor(consignadoCotaChamadao.getNomeFornecedor());
			
			chamadaoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(consignadoCotaChamadao.getDataRecolhimento()));
			
			chamadaoVO.setValorTotal(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getValorTotal()));
			
			chamadaoVO.setIdLancamento(consignadoCotaChamadao.getIdLancamento().toString());
			
			listaChamadao.add(chamadaoVO);
		}
		
		TableModel<CellModelKeyValue<ChamadaoVO>> tableModel =
			new TableModel<CellModelKeyValue<ChamadaoVO>>();
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);
		
		List<CellModelKeyValue<ChamadaoVO>> listaCellModel = new ArrayList<CellModelKeyValue<ChamadaoVO>>();
		
		for (ChamadaoVO vo : listaChamadao) {
			
			CellModelKeyValue<ChamadaoVO> cell =
				new CellModelKeyValue<ChamadaoVO>(Integer.valueOf(vo.getIdLancamento()), vo);
			
			listaCellModel.add(cell);
		}
		
		tableModel.setRows(listaCellModel);
		
		Long qtdProdutosTotal = null;
		
		BigInteger qtdExemplaresTotal = null;
		
		String valorTotalFormatado = null;
		
		if (resumoConsignadoCotaChamadao != null) {
			
			qtdProdutosTotal = resumoConsignadoCotaChamadao.getQtdProdutosTotal();
			
			qtdExemplaresTotal = resumoConsignadoCotaChamadao.getQtdExemplaresTotal();
			
			if (resumoConsignadoCotaChamadao.getValorTotal() != null) {
			
				valorTotalFormatado =
					CurrencyUtil.formatarValor(resumoConsignadoCotaChamadao.getValorTotal());	
			}
		}
		
		ResultadoChamadaoVO resultadoChamadao =
			new ResultadoChamadaoVO(tableModel, qtdProdutosTotal,
									qtdExemplaresTotal, valorTotalFormatado);
		
		result.use(Results.json()).withoutRoot().from(resultadoChamadao).recursive().serialize();
	}
	
	/**
	 * Valida a entrada de dados para pesquisa de consignados da cota.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadaoFormatada - data do chamadão
	 */
	private void validarEntradaDadosPesquisa(Integer numeroCota, String dataChamadaoFormatada) {
		
		if (numeroCota == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento da cota é obrigatório!");
		}
		
		if (dataChamadaoFormatada == null 
				|| dataChamadaoFormatada.trim().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "O preenchimento do campo [Data Chamadão] é obrigatório!");
		}
		
		Date dataChamadao = DateUtil.parseDataPTBR(dataChamadaoFormatada);
		
		if (dataChamadao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida");
		}
		
		Date dataAtual = DateUtil.removerTimestamp(new Date());
		
		if (dataChamadao.compareTo(dataAtual) < 0) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A Data do Chamadão deve ser maior ou igual a data do dia!");
		}
	}
	
	/**
	 * Carrega o filtro da pesquisa de consignados da cota.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param idFornecedor - id do fornecedor
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO carregarFiltroPesquisa(Integer numeroCota,
													 Date dataChamadao,
													 Long idFornecedor,
													 String sortorder, 
													 String sortname, 
													 int page, 
													 int rp) {
		
		FiltroChamadaoDTO filtroAtual =
			new FiltroChamadaoDTO(numeroCota, dataChamadao, idFornecedor);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroChamadaoDTO filtroSessao =
			(FiltroChamadaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		PaginacaoUtil.calcularPaginaAtual(
			this.session,
			QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
			FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
			filtroAtual, filtroSessao);
		
		return filtroAtual;
	}
	
	/**
	 * Carrega o filtro da pesquisa de consignados da cota para confirmar o chamadão.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param idFornecedor - id do fornecedor
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO carregarFiltroConfirmacao(Integer numeroCota,
													 	Date dataChamadao,
													 	Long idFornecedor) {
		
		FiltroChamadaoDTO filtroAtual =
			new FiltroChamadaoDTO(numeroCota, dataChamadao, idFornecedor);
		
		return filtroAtual;
	}
	
	/**
	 * Configura a paginação do filtro de pesquisa de consignados da cota.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroChamadaoDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(
				Util.getEnumByStringValue(OrdenacaoColunaChamadao.values(), sortname));
		}
	}
	
	/**
	 * Obtém usuário logado.
	 * 
	 * @return usuário logado
	 */
	private Usuario obterUsuario() {
		
		//TODO: Aguardando definição de como será obtido o usuário logado
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		usuario.setNome("Usuário da Silva");
		
		return usuario;
	}
	
}
