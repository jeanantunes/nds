package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DigitacaoContagemDevolucaoVO;
import br.com.abril.nds.client.vo.ResultadoDigitacaoContagemDevolucaoVO;
import br.com.abril.nds.dto.ContagemDevolucaoDTO;
import br.com.abril.nds.dto.InfoContagemDevolucaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroDigitacaoContagemDevolucaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ContagemDevolucaoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PeriodoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlas as ações da pagina de Digitação de Contagem de Devolução.
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path(value="/devolucao/digitacao/contagem")
public class DigitacaoContagemDevolucaoController  {
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ContagemDevolucaoService contagemDevolucaoService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisaDigitacaoContagemDevolucao";
	
	private static final String USUARIO_PERFIL_OPERADOR = "userProfileOperador";

	
	@Autowired
	private DistribuidorService distribuidorService;
	
	
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_DIGICACAO_CONTAGEM_DEVOLUCAO)
	public void index(){
		
		/**
		 * FIXE Alterar o códgo abaixo quando, for definido a implementação de Perfil de Usuário
		 */
		result.include(USUARIO_PERFIL_OPERADOR, isPerfilUsuarioEncarregado());
		
		carregarComboFornecedores();
	}
	
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(String dataDe, String dataAte, Long idFornecedor, Integer semanaConferenciaEncalhe, String sortorder, String sortname, int page, int rp){
		
		if(idFornecedor == null || idFornecedor < 0) {
			idFornecedor = null;
		}
		
		PeriodoVO periodo =  obterPeriodoValidado(dataDe, dataAte);
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = new FiltroDigitacaoContagemDevolucaoDTO(periodo,idFornecedor, semanaConferenciaEncalhe);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarPesquisa(filtro);
	}
	
	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroDigitacaoContagemDevolucaoDTO obterFiltroExportacao() {
		
		FiltroDigitacaoContagemDevolucaoDTO filtro = 
				(FiltroDigitacaoContagemDevolucaoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtro.getIdFornecedor() != null && filtro.getIdFornecedor()>0) {
				
				Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(filtro.getIdFornecedor());
				
				if (fornecedor != null) {
					
					PessoaJuridica juridica = fornecedor.getJuridica();
					
					if(juridica!=null) {
						
						filtro.setNomeFornecedor(juridica.getRazaoSocial());
						
					}
												
				}
				
			} else {
				
				filtro.setNomeFornecedor("TODOS");
				
			}
		}
		
		return filtro;
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

		FiltroDigitacaoContagemDevolucaoDTO filtro = obterFiltroExportacao();

		InfoContagemDevolucaoDTO info = contagemDevolucaoService
				.obterInfoContagemDevolucao(filtro, isPerfilUsuarioEncarregado());

		FileExporter.to("digitacao-contagem-devolucao", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, info, info.getListaContagemDevolucao(),
				ContagemDevolucaoDTO.class, this.httpResponse);
		
	}
	
	/*
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
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private boolean isPerfilUsuarioEncarregado() {
		return true;
	}
	
	/**
	 * Executa a pesquisa de digitação de contagem de devolução 
	 * @param filtro
	 */
	private void efetuarPesquisa(FiltroDigitacaoContagemDevolucaoDTO filtro){
		
		InfoContagemDevolucaoDTO infoContagem = contagemDevolucaoService.obterInfoContagemDevolucao(filtro, isPerfilUsuarioEncarregado());
		
		List<ContagemDevolucaoDTO> listaResultados = infoContagem.getListaContagemDevolucao();
		
		if (listaResultados == null || listaResultados.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long quantidadeRegistros = infoContagem.getQtdTotalRegistro().longValue(); 
		
		List<DigitacaoContagemDevolucaoVO> listaResultadosVO = getListaDigitacaoContagemDevolucaoVO(listaResultados);
		
		TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>> tableModel = new TableModel<CellModelKeyValue<DigitacaoContagemDevolucaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));

		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalFormatado = "";
		
		if(	infoContagem!=null && infoContagem.getValorTotalGeral()!=null ) {
			
			valorTotalFormatado = CurrencyUtil.formatarValor(infoContagem.getValorTotalGeral());
		}
		
		ResultadoDigitacaoContagemDevolucaoVO resultadoPesquisa = new ResultadoDigitacaoContagemDevolucaoVO(tableModel,valorTotalFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	
	@Post
	@Path("/salvar")
	public void salvar(List<DigitacaoContagemDevolucaoVO> listaDigitacaoContagemDevolucao) {
		
		if (listaDigitacaoContagemDevolucao == null || listaDigitacaoContagemDevolucao.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Preencha os dados para contagem de devolução!");
		}
		
		List<ContagemDevolucaoDTO> listaContagemDevolucaoDTO = getListaContagemDevolucaoDTO(listaDigitacaoContagemDevolucao);
		
		contagemDevolucaoService.inserirListaContagemDevolucao(listaContagemDevolucaoDTO, getUsuario(), isPerfilUsuarioEncarregado());
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
										Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	@Post
	@Path("/confirmar")
	public void confirmar(List<DigitacaoContagemDevolucaoVO> listaDigitacaoContagemDevolucao) {
		
		if (listaDigitacaoContagemDevolucao == null 
				|| listaDigitacaoContagemDevolucao.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Preencha os dados para contagem de devolução!");
		}
		
		List<ContagemDevolucaoDTO> listaContagemDevolucaoDTO = getListaContagemDevolucaoDTO(listaDigitacaoContagemDevolucao);
		
		contagemDevolucaoService.confirmarContagemDevolucao(listaContagemDevolucaoDTO, getUsuario());
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."),
										Constantes.PARAM_MSGS).recursive().serialize();
		
	}
	
	/**
	 * Retorna uma lista de objetos ContagemDevolucaoDTO convertidos em DigitacaoContagemDevolucaoVO, 
	 * para renderização das informações no grid da tela.
	 * @param listaContagemDevolucaoDto
	 * @return List<DigitacaoContagemDevolucaoVO>
	 */
	private List<DigitacaoContagemDevolucaoVO>getListaDigitacaoContagemDevolucaoVO(List<ContagemDevolucaoDTO> listaContagemDevolucaoDto){
		
		List<DigitacaoContagemDevolucaoVO> listaResultadosVO = new ArrayList<DigitacaoContagemDevolucaoVO>();
		
		DigitacaoContagemDevolucaoVO digitacaoContagemDevolucaoVO = null;
		
		for(ContagemDevolucaoDTO dto: listaContagemDevolucaoDto){
			
			digitacaoContagemDevolucaoVO = new DigitacaoContagemDevolucaoVO();
			
			digitacaoContagemDevolucaoVO.setCodigoProduto(dto.getCodigoProduto());
			digitacaoContagemDevolucaoVO.setNomeProduto(dto.getNomeProduto());
			digitacaoContagemDevolucaoVO.setNumeroEdicao(String.valueOf(dto.getNumeroEdicao()));
			digitacaoContagemDevolucaoVO.setPrecoVenda(CurrencyUtil.formatarValor(dto.getPrecoVenda()));
			digitacaoContagemDevolucaoVO.setQtdDevolucao(String.valueOf( (dto.getQtdDevolucao()==null)?BigDecimal.ZERO.intValue():dto.getQtdDevolucao().intValue()));
			digitacaoContagemDevolucaoVO.setDesconto(String.valueOf((dto.getDesconto()==null)?BigDecimal.ZERO.intValue():dto.getDesconto().intValue()));
			digitacaoContagemDevolucaoVO.setQtdNota( (dto.getQtdNota()==null)?"":String.valueOf(dto.getQtdNota().intValue()));
			
			if(dto.getQtdNota()==null) {
				digitacaoContagemDevolucaoVO.setDiferenca("");
			} else {
				digitacaoContagemDevolucaoVO.setDiferenca(String.valueOf( (dto.getDiferenca() == null)?BigDecimal.ZERO.intValue():dto.getDiferenca().intValue()));
			}
			
			digitacaoContagemDevolucaoVO.setValorTotal( dto.getValorTotal()==null? "" : (CurrencyUtil.formatarValor(dto.getValorTotal())) );
			digitacaoContagemDevolucaoVO.setValorTotalComDesconto(dto.getTotalComDesconto()==null ? "" : (CurrencyUtil.formatarValor(dto.getTotalComDesconto())));
			
			digitacaoContagemDevolucaoVO.setDataRecolhimentoDistribuidor(DateUtil.formatarDataPTBR((dto.getDataMovimento())));
			
			listaResultadosVO.add(digitacaoContagemDevolucaoVO);
		}
		
		return listaResultadosVO;
	}
	
	/**
	 * Retorna uma lista de objetos ContagemDevolucaoDTO para execução de ações nos componentes de negócio.
	 * @param listaContagemDevolucaoVOs
	 * @return List<ContagemDevolucaoDTO>
	 */
	private List<ContagemDevolucaoDTO> getListaContagemDevolucaoDTO(List<DigitacaoContagemDevolucaoVO> listaContagemDevolucaoVOs){
		
		ContagemDevolucaoDTO contagemDevolucaoDTO = null;
		List<ContagemDevolucaoDTO> listaResultadosDto = new ArrayList<ContagemDevolucaoDTO>();
		
		for(DigitacaoContagemDevolucaoVO vo: listaContagemDevolucaoVOs){
			
			contagemDevolucaoDTO = new ContagemDevolucaoDTO();
			
			contagemDevolucaoDTO.setCodigoProduto(vo.getCodigoProduto());
			contagemDevolucaoDTO.setNumeroEdicao(Long.parseLong(vo.getNumeroEdicao()));
			contagemDevolucaoDTO.setQtdNota(new BigInteger(vo.getQtdNota()));
			contagemDevolucaoDTO.setDataMovimento( ( vo.getDataRecolhimentoDistribuidor() == null ) ? null : DateUtil.parseData(vo.getDataRecolhimentoDistribuidor(),"dd/MM/yyyy"));
			
			listaResultadosDto.add(contagemDevolucaoDTO);
		}
		
		return listaResultadosDto;
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroDigitacaoContagemDevolucaoDTO filtro) {

		FiltroDigitacaoContagemDevolucaoDTO filtroResumoSession = 
				(FiltroDigitacaoContagemDevolucaoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroResumoSession != null && !filtroResumoSession.equals(filtro)) {

			filtroResumoSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Configura paginação da lista de resultados
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroDigitacaoContagemDevolucaoDTO filtro, 
											String sortorder, String sortname,
											int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroDigitacaoContagemDevolucaoDTO.OrdenacaoColuna.values(),sortname)));
		}
	}
	
	
	/**
	 * Valida o periodo da consulta e retorna um objeto com os valores. 
	 * @param dataInicial
	 * @param dataFinal
	 * @return PeriodoVO
	 */
	private PeriodoVO obterPeriodoValidado(String dataInicial, String dataFinal) {
				
		tratarErroDatas(validarPreenchimentoObrigatorio(dataInicial, dataFinal));

		tratarErroDatas(validarFormatoDatas(dataInicial, dataFinal));		
		
		validarPeriodo(dataInicial, dataFinal);		
		
		PeriodoVO periodo = new PeriodoVO(DateUtil.parseData(dataInicial, "dd/MM/yyyy"), 
										  DateUtil.parseData(dataFinal, "dd/MM/yyyy"));

		return periodo; 
	}
	
	/**
	 * Trata mensagens de erro, caso tenha mensagem lança exceção de erro.
	 * @param mensagensErro
	 */
	private void tratarErroDatas(List<String> mensagensErro){
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		if(!mensagensErro.isEmpty()){
			
			validacao.setListaMensagens(mensagensErro);
			throw new ValidacaoException(validacao);
		}
	}
	
	/**
	 * Valida o período informado para consulta 
	 * @param dataInicial
	 * @param dataFinal
	 */
	private void validarPeriodo(String dataInicial,String dataFinal){

		if (DateUtil.isDataInicialMaiorDataFinal(DateUtil.parseDataPTBR(dataInicial),
				 								 DateUtil.parseDataPTBR(dataFinal))) {

			throw new ValidacaoException(TipoMensagem.ERROR, "O campo Período de não pode ser maior que o campo Até!");
		}
	}
	
	/**
	 * Valida o formato  das datas informadas  em um determinado período.
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<String>
	 */
	private List<String> validarFormatoDatas(String dataInicial,String dataFinal){
		
		List<String> mensagens = new ArrayList<String>();
		
		if (!DateUtil.isValidDate(dataInicial, "dd/MM/yyyy")) {
			
			mensagens.add("O campo Período de é inválido");
		} 
		
		if (!DateUtil.isValidDate(dataFinal, "dd/MM/yyyy")) {
			
			mensagens.add ("O campo Até é inválido");
		}
		
		return mensagens;
	}
	
	/**
	 * Valida o preenchimento obrigatório do período informado.
	 * @param dataInicial
	 * @param dataFinal
	 * @return List<String>
	 */
	private List<String> validarPreenchimentoObrigatorio(String dataInicial,String dataFinal){
		
		 List<String> mensagens = new ArrayList<String>();
		
		if (dataInicial == null || dataInicial.isEmpty()) {
			
			mensagens.add("O preenchimento do campo Período [De] é obrigatório");
		} 
		
		if (dataFinal == null || dataFinal.isEmpty()) {
			
			mensagens.add("O preenchimento do campo [Até] é obrigatório");
		} 
		
		return mensagens;
	}
}
