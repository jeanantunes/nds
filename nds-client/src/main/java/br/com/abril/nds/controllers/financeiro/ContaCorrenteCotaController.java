package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ContaCorrenteCotaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.EncalheCotaDTO;
import br.com.abril.nds.dto.InfoTotalFornecedorDTO;
import br.com.abril.nds.dto.ResultadosEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsolidadoEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroViewContaCorrenteCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.ViewContaCorrenteCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.ContaCorrenteCotaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
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
	private DistribuidorService distribuidorService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroContaCorrente";
	
	private static final String FILTRO_SESSION_ATTRIBUTE_ENCALHE = "filtroContaCorrenteEncalhe";
	
	@Autowired
	private HttpServletRequest request;
	
	private static final String ITENS_ENCALHE = "itensEncalhe";
	
		
	public ContaCorrenteCotaController(){		
	}
	
	public void index() {	
	}
					
	/**
	 * Método que consulta a conta corrente da Cota selecionada
	 * @param filtroViewContaCorrenteCotaDTO
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 */
	public void consultarContaCorrenteCota( FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,String sortname, String sortorder, int rp, int page) {
			
				
		this.validarDadosEntradaPesquisa(filtroViewContaCorrenteCotaDTO.getNumeroCota());
		
		prepararFiltro(filtroViewContaCorrenteCotaDTO, sortorder, sortname, page, rp);
		
		tratarFiltro(filtroViewContaCorrenteCotaDTO);
		
	
		
		List<ViewContaCorrenteCota> listaItensContaCorrenteCota = contaCorrenteCotaService.obterListaConsolidadoPorCota(filtroViewContaCorrenteCotaDTO);
							
		if (listaItensContaCorrenteCota == null || listaItensContaCorrenteCota.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		request.getSession().setAttribute(ITENS_ENCALHE, listaItensContaCorrenteCota);
		
		TableModel<CellModel> tableModel =  obterTableModelParaListItensContaCorrenteCota(listaItensContaCorrenteCota);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Consulta os encalhes da cota em uma determinada data
	 * @param filtroConsolidadoEncalheDTO
	 * @param sortname
	 * @param sortorder
	 * @param rp
	 * @param page
	 */
	public void consultarEncalheCota(FiltroConsolidadoEncalheCotaDTO filtroConsolidadoEncalheDTO, String sortname, String sortorder, int rp, int page ){
				
		
		ViewContaCorrenteCota contaCorrente = obterListaEncalheSessao(filtroConsolidadoEncalheDTO.getLineId());
		
		filtroConsolidadoEncalheDTO.setDataConsolidado(contaCorrente.getDataConsolidado());	
		request.getSession().setAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE, filtroConsolidadoEncalheDTO);
						
		List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtroConsolidadoEncalheDTO);		
		
		List<InfoTotalFornecedorDTO> listaInfoTotalFornecedor = mostrarInfoTotalForncedores(listaEncalheCota);
				
		if(listaEncalheCota != null){
						
			TableModel<CellModel> tableModel =  obterTableModelParaEncalheCota(listaEncalheCota);
			
			ResultadosEncalheCotaDTO resultado = new ResultadosEncalheCotaDTO(
					tableModel,
					contaCorrente.getDataConsolidado().toString(),
					listaInfoTotalFornecedor );
						
			result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		}else{
			throw new ValidacaoException(TipoMensagem.WARNING, "Dados do Encalhe não encontrado.");
		}
		
	}
	
		
	/**
	 * Método que armazena informações para exibição do nome fornecedor e o total por fornecedor
	 * @param listaEncalheCota
	 */
	private List<InfoTotalFornecedorDTO> mostrarInfoTotalForncedores(List<EncalheCotaDTO> listaEncalheCota){
		
		List<InfoTotalFornecedorDTO> listaInfoFornecedores = new ArrayList<InfoTotalFornecedorDTO>();
				
		String nomeFornecedor ="";
		BigDecimal total = new BigDecimal(0);
		InfoTotalFornecedorDTO info = new InfoTotalFornecedorDTO();
		int count = 1;
		
		for(EncalheCotaDTO encalhe : listaEncalheCota){
			
			if(nomeFornecedor.equals("")){
				nomeFornecedor = encalhe.getNomeFornecedor();
			}
			
			if(encalhe.getNomeFornecedor().equals(nomeFornecedor)){				
				total = total.add(encalhe.getTotal());
				
			}else{
				info = new InfoTotalFornecedorDTO();
				info.setNomeFornecedor(nomeFornecedor);
				info.setValorTotal(total.toString());
				listaInfoFornecedores.add(info);				
				nomeFornecedor = encalhe.getNomeFornecedor();
				total = new BigDecimal(0);
				
			}
			
			if(count == listaEncalheCota.size()){
				info = new InfoTotalFornecedorDTO();
				info.setNomeFornecedor(encalhe.getNomeFornecedor());				
				info.setValorTotal(total.add(encalhe.getTotal()).toString());	
				listaInfoFornecedores.add(info);
			}
						
			count++;
		}
		
		return listaInfoFornecedores;
		
	}

	/**
	 * Obtém lista de conta corrente da sessão para localizar data selecionada
	 * @param lineId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ViewContaCorrenteCota obterListaEncalheSessao(Long lineId) {
		List<ViewContaCorrenteCota> listaContaCorrente =  (List<ViewContaCorrenteCota>) request.getSession().getAttribute(ITENS_ENCALHE);
		
		if(listaContaCorrente != null){
			for(ViewContaCorrenteCota contaCorrente : listaContaCorrente){
				
				if(contaCorrente.getId().equals(lineId)){
					return contaCorrente;
				}
			}
		}		
		return null;
		
	}
	
	
	public void exportarEncalhe(FileType fileType) throws IOException{
		
		FiltroConsolidadoEncalheCotaDTO filtro = this.obterFiltroExportacaoEncalhe();
		
		
		List<EncalheCotaDTO> listaEncalheCota = consolidadoFinanceiroService.obterMovimentoEstoqueCotaEncalhe(filtro);		
		
		
		FileExporter.to("encalhe-cota", fileType)
		.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaEncalheCota, EncalheCotaDTO.class, this.httpServletResponse);
		
		result.use(Results.nothing());
		
	}
	
	
	
	private FiltroConsolidadoEncalheCotaDTO obterFiltroExportacaoEncalhe() {
		
		FiltroConsolidadoEncalheCotaDTO filtro = 
			(FiltroConsolidadoEncalheCotaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE_ENCALHE);
		
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
		
		List<ViewContaCorrenteCota> listaItensContaCorrenteCota =
			contaCorrenteCotaService.obterListaConsolidadoPorCota(filtro);
		
		List<ContaCorrenteCotaVO> listaItensContaCorrenteCotaVO = new ArrayList<ContaCorrenteCotaVO>();
		
		for (ViewContaCorrenteCota contaCorrenteCota : listaItensContaCorrenteCota) {
			
			ContaCorrenteCotaVO contaCorrenteCotaVO = new ContaCorrenteCotaVO();
			
			contaCorrenteCotaVO.setConsignado(MathUtil.defaultValue(contaCorrenteCota.getConsignado()));
			contaCorrenteCotaVO.setDataConsolidado(contaCorrenteCota.getDataConsolidado());
			contaCorrenteCotaVO.setDebitoCredito(MathUtil.defaultValue(contaCorrenteCota.getDebitoCredito()));
			contaCorrenteCotaVO.setEncalhe(MathUtil.defaultValue(contaCorrenteCota.getEncalhe()));
			contaCorrenteCotaVO.setEncargos(MathUtil.defaultValue(contaCorrenteCota.getEncargos()));
			contaCorrenteCotaVO.setNumerosAtrasados(MathUtil.defaultValue(contaCorrenteCota.getNumeroAtrasados()));
			contaCorrenteCotaVO.setPendente(MathUtil.defaultValue(contaCorrenteCota.getPendente()));
			contaCorrenteCotaVO.setTotal(MathUtil.defaultValue(contaCorrenteCota.getTotal()));
			contaCorrenteCotaVO.setValorPostergado(MathUtil.defaultValue(contaCorrenteCota.getValorPostergado()));
			contaCorrenteCotaVO.setVendaEncalhe(MathUtil.defaultValue(contaCorrenteCota.getVendaEncalhe()));
			
			listaItensContaCorrenteCotaVO.add(contaCorrenteCotaVO);
		}
		
		FileExporter.to("conta-corrente-cota", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaItensContaCorrenteCotaVO, ContaCorrenteCotaVO.class, this.httpServletResponse);
	}
	
	private FiltroViewContaCorrenteCotaDTO obterFiltroExportacao() {
		
		FiltroViewContaCorrenteCotaDTO filtro = 
			(FiltroViewContaCorrenteCotaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtro.getNumeroCota() != null) {
				
				Cota cota =
					this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
				
				if (cota != null) {
					
					Pessoa pessoa = cota.getPessoa();
					
					if (pessoa instanceof PessoaFisica) {
						
						filtro.setNomeCota(((PessoaFisica) pessoa).getNome());
												
					} else if (pessoa instanceof PessoaJuridica) {
						
						filtro.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
					}
				}
			}
		}
		
		return filtro;
	}
	
	private void prepararFiltro(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO,	String sortorder, String sortname, int page, int rp) {
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		filtroViewContaCorrenteCotaDTO.setPaginacao(paginacao);
		
		paginacao.setPaginaAtual(page);
		
		filtroViewContaCorrenteCotaDTO.setColunaOrdenacao(Util.getEnumByStringValue(FiltroViewContaCorrenteCotaDTO.ColunaOrdenacao.values(), sortname));
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroViewContaCorrenteCotaDTO filtroViewContaCorrenteCotaDTO) {

		FiltroViewContaCorrenteCotaDTO filtroContaCorrenteSession = (FiltroViewContaCorrenteCotaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroContaCorrenteSession != null
				&& !filtroContaCorrenteSession.equals(filtroViewContaCorrenteCotaDTO)) {

			filtroViewContaCorrenteCotaDTO.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroViewContaCorrenteCotaDTO);
	}
	
	
	/**
	 * Obtem uma lista de Conta Corrente cota e prepara o Grid para receber os valores
	 * @param itensContaCorrenteCota
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaListItensContaCorrenteCota(List<ViewContaCorrenteCota> itensContaCorrenteCota) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		///int counter = 1;
		
		Integer codCota = null;
		
		for(ViewContaCorrenteCota dto : itensContaCorrenteCota) {
			
			
			
			codCota = dto.getNumeroCota();
			String data 		     	 = dto.getDataConsolidado().toString();
			String valorPostergado	     = (dto.getValorPostergado() 	== null) 	? "0.0" : dto.getValorPostergado().toString();
			String NA 		     	     = (dto.getNumeroAtrasados()    == null) 	? "0.0" : dto.getNumeroAtrasados().toString();
			String consignado 	     	 = (dto.getConsignado()			== null) 	? "0.0" : dto.getConsignado().toString();
			String encalhe 	 	         = (dto.getEncalhe()        	== null) 	? "0.0" : dto.getEncalhe().toString();
			String vendaEncalhe		 	 = (dto.getVendaEncalhe()		== null) 	? "0.0" : dto.getVendaEncalhe().toString();
			String debCred		 	     = (dto.getDebitoCredito() 		== null) 	? "0.0" : dto.getDebitoCredito().toString();
			String encargos		 	     = (dto.getEncargos()			== null) 	? "0.0" : dto.getEncargos().toString() ;
			String pendente				 = (dto.getPendente() 			== null) 	? "0.0" : dto.getPendente().toString();
			String total                 = (dto.getTotal() 			    == null) 	? "0.0" : dto.getTotal().toString();
			
			listaModeloGenerico.add(
					new CellModel( 	
							dto.getId().intValue(), 
							data, 
							valorPostergado, 
							NA, 
							consignado, 
							encalhe,
							vendaEncalhe,
							debCred,
							encargos,
							pendente,
							total
					));
			
			//counter++;
		}
		
		Cota cota = cotaService.obterPorNumeroDaCota(codCota);
		
		result.include("cotaNome",cota.getNumeroCota()+" "+cota.getPessoa() );
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
	/**
	 * Obtem uma lista de Encalhe da cota no dia selecionado e prepara o Grid para receber os valores
	 * @param itensContaCorrenteCota
	 * @return
	 */
	private TableModel<CellModel> obterTableModelParaEncalheCota(List<EncalheCotaDTO> listaEncalheCota) {
					
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();		
			
		int counter = 1;
		
		for(EncalheCotaDTO dto : listaEncalheCota) {		
			
			String codigoProduto 		 = dto.getCodigoProduto().toString();
			String nomeProduto	     	 = (dto.getNomeProduto() 	    == null) 	? "0.0" : dto.getNomeProduto();
			String numeroEdicao 		 = (dto.getNumeroEdicao()       == null) 	? "0.0" : dto.getNumeroEdicao().toString();
			String precoCapa 	     	 = (dto.getPrecoCapa()			== null) 	? "0.0" : dto.getPrecoCapa().toString();
			String precoComDesconto      = (dto.getPrecoComDesconto()  	== null) 	? "0.0" : dto.getPrecoComDesconto().toString();
			String encalhe		 	 	 = (dto.getEncalhe()		    == null) 	? "0.0" : dto.getEncalhe().toString();
			String nomeFornecedor	 	 = (dto.getNomeFornecedor() 	== null) 	? "0.0" : dto.getNomeFornecedor();
			String total		 	     = (dto.getTotal()			    == null) 	? "0.0" : dto.getTotal().toString() ;
					
			listaModeloGenerico.add(
					new CellModel( 	
							counter, 
							codigoProduto, 
							nomeProduto, 
							numeroEdicao, 
							precoCapa, 
							precoComDesconto,
							encalhe,
							nomeFornecedor,
							total
					));
			
			counter++;
		}
						
		//result.include("cotaNome",cota.getNumeroCota()+" "+cota.getPessoa() );
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;		
	}
			
	private void validarDadosEntradaPesquisa(Integer numeroCota) {
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (numeroCota == null){
			listaMensagemValidacao.add("O Preenchimento do campo Cota é obrigatório.");
		}else{
			if(!Util.isNumeric(numeroCota.toString())){
				listaMensagemValidacao.add("A Cota permite apenas valores números.");
			}
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.WARNING, listaMensagemValidacao);
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
	
}
