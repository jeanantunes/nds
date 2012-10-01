package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.client.vo.FooterTotalFornecedorVO;
import br.com.abril.nds.dto.ConsignadoCotaDTO;
import br.com.abril.nds.dto.ConsultaVendaEncalheDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.FiltroConsolidadoConsignadoCotaDTO;
import br.com.abril.nds.dto.InfoTotalFornecedorDTO;
import br.com.abril.nds.dto.ResultadosContaCorrenteConsignadoDTO;
import br.com.abril.nds.dto.ResultadosContaCorrenteEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoVendaCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.ContaCorrenteCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EmailService;
import br.com.abril.nds.service.exception.AutenticacaoEmailException;
import br.com.abril.nds.util.AnexoEmail;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/contaCorrenteCota")
public class ContaCorrenteCotaController {

	@Autowired
	private Result result;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private ConsolidadoFinanceiroService consolidadoFinanceiroService;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private ContaCorrenteCotaService contaCorrenteCotaService;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private DistribuidorService distribuidorService;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroContaCorrente";

	private static final String FILTRO_SESSION_ATTRIBUTE_ENCALHE = "filtroContaCorrenteEncalhe";
	
	private static final String FILTRO_SESSION_ATTRIBUTE_CONSIGNADO = "filtroContaCorrenteConsignado";

	@Autowired
	private HttpServletRequest request;

	private static final String ITENS_CONTA_CORRENTE = "itensContaCorrente";
	
	public ContaCorrenteCotaController() {
	}

	@Rules(Permissao.ROLE_FINANCEIRO_CONTA_CORRENTE)
	public void index() {
	}

	/**
	 * Método que consulta a conta corrente da Cota selecionada
	 * 
	 * @param filtroViewContaCorrenteCotaDTO
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 */
	public void consultarContaCorrenteCota(
			FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,
			String sortname, String sortorder, int rp, int page) {

		this.validarDadosEntradaPesquisa(filtroViewContaCorrenteCotaDTO
				.getNumeroCota());

		prepararFiltro(filtroViewContaCorrenteCotaDTO, sortorder, sortname,
				page, rp);

		tratarFiltro(filtroViewContaCorrenteCotaDTO);

		List<ViewContaCorrenteCota> listaItensContaCorrenteCota = contaCorrenteCotaService
				.obterListaConsolidadoPorCota(filtroViewContaCorrenteCotaDTO);

		if (listaItensContaCorrenteCota == null
				|| listaItensContaCorrenteCota.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,
					"Nenhum registro encontrado.");
		}

		request.getSession().setAttribute(ITENS_CONTA_CORRENTE,
				listaItensContaCorrenteCota);

		TableModel<CellModel> tableModel = obterTableModelParaListItensContaCorrenteCota(listaItensContaCorrenteCota);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive()
				.serialize();
	}

	/**
	 * Consulta os encalhes da cota em uma determinada data
	 * 
	 * @param filtroConsolidadoEncalheDTO
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 */
	public void consultarEncalheCota(
			FiltroConsolidadoEncalheCotaDTO filtroConsolidadoEncalheDTO,
			String sortname, String sortorder, int rp, int page) {

		ViewContaCorrenteCota contaCorrente = obterListaContaCorrenteSessao(filtroConsolidadoEncalheDTO.getLineId());

		filtroConsolidadoEncalheDTO.setDataConsolidado(contaCorrente
				.getDataConsolidado());
		request.getSession().setAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE,
				filtroConsolidadoEncalheDTO);

		List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService
				.obterMovimentoEstoqueCotaEncalhe(filtroConsolidadoEncalheDTO);

		Collection<InfoTotalFornecedorDTO> listaInfoTotalFornecedor = mostrarInfoTotalForncedores(listaEncalheCota);

		if (listaEncalheCota != null) {

			TableModel<CellModelKeyValue<EncalheCotaDTO>> tableModel = new TableModel<CellModelKeyValue<EncalheCotaDTO>>();
			
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaEncalheCota));
			tableModel.setPage(1);
			tableModel.setTotal(listaEncalheCota.size());

			ResultadosContaCorrenteEncalheDTO resultado = new ResultadosContaCorrenteEncalheDTO(
					tableModel,
					contaCorrente.getDataConsolidado().toString(),
					listaInfoTotalFornecedor );
			
			boolean temMaisQueUm = listaInfoTotalFornecedor.size() > 1;
					
			Object[] dados = new Object[2];
			dados[0] = temMaisQueUm;
			dados[1] = resultado;		
						
			result.use(Results.json()).from(dados, "result").recursive().serialize();
		}else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados do Encalhe não encontrado.");
		}

	}
	
	/**
	 * Consulta Consignados da cota em uma determinada data
	 * @param filtroConsolidadoConsignadoCotaDTO
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 */
	@SuppressWarnings("unused")
	public void consultarConsignadoCota(FiltroConsolidadoConsignadoCotaDTO filtroConsolidadoConsignadoCotaDTO, String sortname, String sortorder, int rp, 
			int page){
		
		ViewContaCorrenteCota contaCorrente = obterListaContaCorrenteSessao(filtroConsolidadoConsignadoCotaDTO.getLineId());

		filtroConsolidadoConsignadoCotaDTO.setDataConsolidado(contaCorrente
				.getDataConsolidado());
		request.getSession().setAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO,
				filtroConsolidadoConsignadoCotaDTO);
		
		List<ConsignadoCotaDTO> listaConsignadoCota = consolidadoFinanceiroService
				.obterMovimentoEstoqueCotaConsignado(filtroConsolidadoConsignadoCotaDTO);
		
		Collection<InfoTotalFornecedorDTO> listaInfoTotalFornecedor = mostrarInfoTotalForncedoresConsignado(listaConsignadoCota);
		
		TableModel<CellModelKeyValue<ConsignadoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<ConsignadoCotaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaConsignadoCota));
		tableModel.setPage(1);
		tableModel.setTotal(listaConsignadoCota.size());
		
		if(listaConsignadoCota != null){
			
			
			ResultadosContaCorrenteConsignadoDTO resultado = new ResultadosContaCorrenteConsignadoDTO(
					tableModel,
					contaCorrente.getDataConsolidado().toString(),
					listaInfoTotalFornecedor );
			
			boolean temMaisQueUm = listaInfoTotalFornecedor.size() > 1;
									
			Object[] dados = new Object[2];
			dados[0] = temMaisQueUm;
			dados[1] = resultado;		
						
			result.use(Results.json()).from(dados, "result").recursive().serialize();
						
		} else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados do Consolidado não encontrado.");
		}
		
	}
	
	
	
	
	
		
	/**
	 * Método que armazena informações para exibição do nome fornecedor e o total por fornecedor
	 * @param listaEncalheCota
	 */
	private Collection<InfoTotalFornecedorDTO> mostrarInfoTotalForncedores(
			List<EncalheCotaDTO> listaEncalheCota) {

		Map<String, InfoTotalFornecedorDTO> mapFornecedores = new HashMap<String, InfoTotalFornecedorDTO>();

		String key;
		BigDecimal valor;

		for (EncalheCotaDTO encalhe : listaEncalheCota) {
			 key = encalhe.getNomeFornecedor();
			 valor = encalhe.getTotal();
			if(mapFornecedores.containsKey(key)){				
				valor = mapFornecedores.get(key).getValorTotal().add(valor);				
			}
			
			mapFornecedores.put(key,new InfoTotalFornecedorDTO(key, valor));
			
		}
		

		List<InfoTotalFornecedorDTO> infoTotalFornecedorDTOs = new ArrayList<InfoTotalFornecedorDTO>();
		infoTotalFornecedorDTOs.addAll( mapFornecedores.values() );
		
		return infoTotalFornecedorDTOs;

	}
	
	/**
	 * Método que armazena informações para exibição do nome fornecedor e o total por fornecedor
	 * @param listaConsignadoCota
	 */
	private Collection<InfoTotalFornecedorDTO> mostrarInfoTotalForncedoresConsignado(
			List<ConsignadoCotaDTO> listaConsignadoCota) {

		
		Map<String, InfoTotalFornecedorDTO> mapFornecedores = new HashMap<String, InfoTotalFornecedorDTO>();

		String key;
		BigDecimal valor;

		for (ConsignadoCotaDTO consignado : listaConsignadoCota) {
			 key = consignado.getNomeFornecedor();
			 valor = consignado.getTotal();
			if(mapFornecedores.containsKey(key)){				
				valor = mapFornecedores.get(key).getValorTotal().add(valor);				
			}
			
			mapFornecedores.put(key,new InfoTotalFornecedorDTO(key, valor));
			
		}
		List<InfoTotalFornecedorDTO> infoTotalFornecedorDTOs = new ArrayList<InfoTotalFornecedorDTO>();
		infoTotalFornecedorDTOs.addAll( mapFornecedores.values() );
		
		return infoTotalFornecedorDTOs;

	}

	/**
	 * Obtém lista de conta corrente da sessão para localizar data selecionada
	 * 
	 * @param lineId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ViewContaCorrenteCota obterListaContaCorrenteSessao(Long lineId) {
		List<ViewContaCorrenteCota> listaContaCorrente = (List<ViewContaCorrenteCota>) request
				.getSession().getAttribute(ITENS_CONTA_CORRENTE);

		if (listaContaCorrente != null) {
			for (ViewContaCorrenteCota contaCorrente : listaContaCorrente) {

				if (contaCorrente.getId().equals(lineId)) {
					return contaCorrente;
				}
			}
		}
		return null;

	}
	
	public void exportarEncalhe(FileType fileType) throws IOException {
		FiltroConsolidadoEncalheCotaDTO filtro = this
				.obterFiltroExportacaoEncalhe();

		List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService
				.obterMovimentoEstoqueCotaEncalhe(filtro);
		
		
		String nomeCota = cotaService.obterNomeResponsavelPorNumeroDaCota(filtro.getNumeroCota());
		String cota = filtro.getNumeroCota() + " - " + nomeCota;
		filtro.setCota(cota);
		
		HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
		
		for(EncalheCotaDTO encalheCotaDTO: listaEncalheCota){
			String key = encalheCotaDTO.getNomeFornecedor();
			if(totais.containsKey(key)){				
				totais.put(key, totais.get(key).add(encalheCotaDTO.getTotal()));				
			}else{
				totais.put(key, encalheCotaDTO.getTotal());
			}
		}
		

		FileExporter.to("encalhe-cota", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, new FooterTotalFornecedorVO(totais), listaEncalheCota,
				EncalheCotaDTO.class, this.httpServletResponse);

		result.use(Results.nothing());

	}

	private FiltroConsolidadoEncalheCotaDTO obterFiltroExportacaoEncalhe() {

		FiltroConsolidadoEncalheCotaDTO filtro = (FiltroConsolidadoEncalheCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE);

		if (filtro != null) {

			if (filtro.getPaginacao() != null) {

				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}

		}

		return filtro;
	}

	public void exportar(FileType fileType) throws IOException {

		FiltroViewContaCorrenteCotaDTO filtro = this.obterFiltroExportacao();

		List<ViewContaCorrenteCota> listaItensContaCorrenteCota = contaCorrenteCotaService
				.obterListaConsolidadoPorCota(filtro);

		List<ContaCorrenteCotaVO> listaItensContaCorrenteCotaVO = new ArrayList<ContaCorrenteCotaVO>();

		for (ViewContaCorrenteCota contaCorrenteCota : listaItensContaCorrenteCota) {

			ContaCorrenteCotaVO contaCorrenteCotaVO = new ContaCorrenteCotaVO();

			contaCorrenteCotaVO.setConsignado(MathUtil
					.defaultValue(contaCorrenteCota.getConsignado()));
			contaCorrenteCotaVO.setDataConsolidado(contaCorrenteCota
					.getDataConsolidado());
			contaCorrenteCotaVO.setDebitoCredito(MathUtil
					.defaultValue(contaCorrenteCota.getDebitoCredito()));
			contaCorrenteCotaVO.setEncalhe(MathUtil
					.defaultValue(contaCorrenteCota.getEncalhe()));
			contaCorrenteCotaVO.setEncargos(MathUtil
					.defaultValue(contaCorrenteCota.getEncargos()));
			contaCorrenteCotaVO.setNumerosAtrasados(MathUtil
					.defaultValue(contaCorrenteCota.getNumeroAtrasados()));
			contaCorrenteCotaVO.setPendente(MathUtil
					.defaultValue(contaCorrenteCota.getPendente()));
			contaCorrenteCotaVO.setTotal(MathUtil
					.defaultValue(contaCorrenteCota.getTotal()));
			contaCorrenteCotaVO.setValorPostergado(MathUtil
					.defaultValue(contaCorrenteCota.getValorPostergado()));
			contaCorrenteCotaVO.setVendaEncalhe(MathUtil
					.defaultValue(contaCorrenteCota.getVendaEncalhe()));

			listaItensContaCorrenteCotaVO.add(contaCorrenteCotaVO);
		}

		FileExporter.to("conta-corrente-cota", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, null,
				listaItensContaCorrenteCotaVO, ContaCorrenteCotaVO.class,
				this.httpServletResponse);
	}

	private FiltroViewContaCorrenteCotaDTO obterFiltroExportacao() {

		FiltroViewContaCorrenteCotaDTO filtro = (FiltroViewContaCorrenteCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		if (filtro != null) {

			if (filtro.getPaginacao() != null) {

				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}

			if (filtro.getNumeroCota() != null) {

				Cota cota = this.cotaService.obterPorNumeroDaCota(filtro
						.getNumeroCota());

				if (cota != null) {

					Pessoa pessoa = cota.getPessoa();

					if (pessoa instanceof PessoaFisica) {

						filtro.setNomeCota(((PessoaFisica) pessoa).getNome());

					} else if (pessoa instanceof PessoaJuridica) {

						filtro.setNomeCota(((PessoaJuridica) pessoa)
								.getRazaoSocial());
					}
				}
			}
		}

		return filtro;
	}

	private void prepararFiltro(
			FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,
			String sortorder, String sortname, int page, int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroViewContaCorrenteCotaDTO.setPaginacao(paginacao);

		paginacao.setPaginaAtual(page);

		filtroViewContaCorrenteCotaDTO
				.setColunaOrdenacao(Util.getEnumByStringValue(
						FiltroViewContaCorrenteCotaDTO.ColunaOrdenacao.values(),
						sortname));
	}

	/**
	 * Executa tratamento de paginação em função de alteração do filtro de
	 * pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(
			FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO) {

		FiltroViewContaCorrenteCotaDTO filtroContaCorrenteSession = (FiltroViewContaCorrenteCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		if (filtroContaCorrenteSession != null
				&& !filtroContaCorrenteSession
						.equals(filtroViewContaCorrenteCotaDTO)) {

			filtroViewContaCorrenteCotaDTO.getPaginacao().setPaginaAtual(1);
		}

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE,
				filtroViewContaCorrenteCotaDTO);
	}

	/**
	 * Obtem uma lista de Conta Corrente cota e prepara o Grid para receber os
	 * valores
	 * 
	 * @param itensContaCorrenteCota
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaListItensContaCorrenteCota(
			List<ViewContaCorrenteCota> itensContaCorrenteCota) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();

		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();

		// /int counter = 1;

		Integer codCota = null;

		for (ViewContaCorrenteCota dto : itensContaCorrenteCota) {

			codCota = dto.getNumeroCota();
			String data = dto.getDataConsolidado().toString();
			String valorPostergado = (dto.getValorPostergado() == null) ? "0.0"
					: dto.getValorPostergado().toString();
			String NA = (dto.getNumeroAtrasados() == null) ? "0.0" : dto
					.getNumeroAtrasados().toString();
			String consignado = (dto.getConsignado() == null) ? "0.0" : dto
					.getConsignado().toString();
			String encalhe = (dto.getEncalhe() == null) ? "0.0" : dto
					.getEncalhe().toString();
			String vendaEncalhe = (dto.getVendaEncalhe() == null) ? "0.0" : dto
					.getVendaEncalhe().toString();
			String debCred = (dto.getDebitoCredito() == null) ? "0.0" : dto
					.getDebitoCredito().toString();
			String encargos = (dto.getEncargos() == null) ? "0.0" : dto
					.getEncargos().toString();
			String pendente = (dto.getPendente() == null) ? "0.0" : dto
					.getPendente().toString();
			String total = (dto.getTotal() == null) ? "0.0" : dto.getTotal()
					.toString();

			listaModeloGenerico.add(new CellModel(dto.getId().intValue(), data,
					valorPostergado, NA, consignado, encalhe, vendaEncalhe,
					debCred, encargos, pendente, total, dto.getId()));

			// counter++;
		}

		Cota cota = cotaService.obterPorNumeroDaCota(codCota);

		result.include("cotaNome",
				cota.getNumeroCota() + " " + cota.getPessoa());

		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);

		return tableModel;

	}
	
	private void validarDadosEntradaPesquisa(Integer numeroCota) {
		List<String> listaMensagemValidacao = new ArrayList<String>();

		if (numeroCota == null) {
			listaMensagemValidacao
					.add("O Preenchimento do campo Cota é obrigatório.");
		} else {
			if (!Util.isNumeric(numeroCota.toString())) {
				listaMensagemValidacao
						.add("A Cota permite apenas valores números.");
			}
		}

		if (!listaMensagemValidacao.isEmpty()) {
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING,
					listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
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

			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica()
					.getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica()
					.getCnpj());
		}

		ndsFileHeader.setData(new Date());

		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());

		return ndsFileHeader;
	}

	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();

		usuario.setId(1L);

		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}

	public void obterMovimentoVendaEncalhe(Long idConsolidado, String sortname, String sortorder, int rp, int page) {
		
		
		FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();
		
		filtro.setIdConsolidado(idConsolidado);		
		
		filtro.setOrdenacaoColuna(FiltroConsolidadoVendaCotaDTO.OrdenacaoColuna.valueOf(sortname));
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
		
		List<ConsultaVendaEncalheDTO> encalheDTOs = consolidadoFinanceiroService
				.obterMovimentoVendaEncalhe(filtro);

		TableModel<CellModelKeyValue<ConsultaVendaEncalheDTO>> tableModel = new TableModel<CellModelKeyValue<ConsultaVendaEncalheDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(encalheDTOs));
		tableModel.setPage(1);
		tableModel.setTotal(encalheDTOs.size());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive()
				.serialize();

	}
	
	public void exportarVendaEncalhe(FileType fileType,Long idConsolidado) throws IOException{
		
		ConsolidadoFinanceiroCota  consolidado =  consolidadoFinanceiroService.buscarPorId(idConsolidado);
		FiltroConsolidadoVendaCotaDTO filtro = new FiltroConsolidadoVendaCotaDTO();
		filtro.setDataConsolidado(consolidado.getDataConsolidado());
		
		filtro.setIdConsolidado(idConsolidado);			
		String cota = consolidado.getCota().getNumeroCota() + " - " + PessoaUtil.obterNomeExibicaoPeloTipo(consolidado.getCota().getPessoa());
		filtro.setCota(cota);
		
		List<ConsultaVendaEncalheDTO> encalheDTOs = consolidadoFinanceiroService
				.obterMovimentoVendaEncalhe(filtro);
		
		
		HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
		
		for(ConsultaVendaEncalheDTO encalheDTO: encalheDTOs){
			String key = encalheDTO.getNomeFornecedor();
			if(totais.containsKey(key)){				
				totais.put(key, totais.get(key).add(encalheDTO.getTotal()));				
			}else{
				totais.put(key, encalheDTO.getTotal());
			}
		}
		FileExporter.to("venda-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, new FooterTotalFornecedorVO(totais),
				encalheDTOs, ConsultaVendaEncalheDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}	
	

	public void exportarConsignadoCota(FileType fileType) throws IOException{
		FiltroConsolidadoConsignadoCotaDTO filtro = (FiltroConsolidadoConsignadoCotaDTO) request.getSession().getAttribute(FILTRO_SESSION_ATTRIBUTE_CONSIGNADO);
		String nomeCota = cotaService.obterNomeResponsavelPorNumeroDaCota(filtro.getNumeroCota());
		String cota = filtro.getNumeroCota() + " - " + nomeCota;
		filtro.setCota(cota);
				
		List<ConsignadoCotaDTO> listConsignadoCotaDTO = consolidadoFinanceiroService.obterMovimentoEstoqueCotaConsignado(filtro);
		HashMap<String, BigDecimal> totais = new HashMap<String, BigDecimal>();
		
		for(ConsignadoCotaDTO consignadoDTO: listConsignadoCotaDTO){
			String key = consignadoDTO.getNomeFornecedor();
			if(totais.containsKey(key)){				
				totais.put(key, totais.get(key).add(consignadoDTO.getTotal()));				
			}else{
				totais.put(key, consignadoDTO.getTotal());
			}
		}
		
		FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), filtro, new FooterTotalFornecedorVO(totais),
				listConsignadoCotaDTO, ConsignadoCotaDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	public void enviarEmail(String assunto, String mensagem, String[]destinatarios) throws AutenticacaoEmailException{
		
		List<AnexoEmail> anexos = new ArrayList<AnexoEmail>();
		emailService.enviar(assunto, mensagem, destinatarios, anexos);
		
	}
	
		
}