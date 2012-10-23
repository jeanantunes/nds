package br.com.abril.nds.controllers.devolucao;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.EmissaoBandeiraVO;
import br.com.abril.nds.client.vo.ImpressaoBandeiraVO;
import br.com.abril.nds.dto.EmissaoBandeiraDTO;
import br.com.abril.nds.dto.SlipVendaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroVendaEncalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.TipoSlip;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.estoque.TipoVendaEncalhe;
import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.ByteArrayDownload;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("devolucao/emissaoBandeira")
public class EmissaoBandeiraController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletResponse response;
	

	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_EMISSAO_BANDEIRA)
	public void index() {
	
	}
	
	@Path("/pesquisar")
	public void pesquisar(Integer semana, String sortname, String sortorder, int rp, int page) {
		
		List<EmissaoBandeiraDTO> listaEmissaoBandeiraDTO = getListaEmissaoBandeira();
		
		List<EmissaoBandeiraVO> listaEmissaoBandeiraVO =  new ArrayList<EmissaoBandeiraVO>();
		
		for (EmissaoBandeiraDTO dto :listaEmissaoBandeiraDTO ){
			listaEmissaoBandeiraVO.add(new EmissaoBandeiraVO (dto));
		}
		
		if (!listaEmissaoBandeiraVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			this.result.use(FlexiGridJson.class).from(listaEmissaoBandeiraVO).total(listaEmissaoBandeiraVO.size()).page(page).serialize();
		}
	}

	private List<EmissaoBandeiraDTO> getListaEmissaoBandeira() {
		List<EmissaoBandeiraDTO> listaEmissaoBandeiraDTO = new ArrayList<EmissaoBandeiraDTO>();
		EmissaoBandeiraDTO emissaoBandeiraDTO = new EmissaoBandeiraDTO();
		emissaoBandeiraDTO.setCodigoProduto("1");
		emissaoBandeiraDTO.setNomeProduto("EPOCA");
		emissaoBandeiraDTO.setEdicao(1000l);
		emissaoBandeiraDTO.setPacotePadrao(32);
		emissaoBandeiraDTO.setData(new Date());
		emissaoBandeiraDTO.setDestino("SERVICE MAIL");
		emissaoBandeiraDTO.setPrioridade(1);
		listaEmissaoBandeiraDTO.add(emissaoBandeiraDTO);
		
		
		emissaoBandeiraDTO = new EmissaoBandeiraDTO();
		emissaoBandeiraDTO.setCodigoProduto("2");
		emissaoBandeiraDTO.setNomeProduto("VEJA");
		emissaoBandeiraDTO.setEdicao(1000l);
		emissaoBandeiraDTO.setPacotePadrao(32);
		emissaoBandeiraDTO.setData(new Date());
		emissaoBandeiraDTO.setDestino("SERVICE MAIL");
		emissaoBandeiraDTO.setPrioridade(1);
		
		listaEmissaoBandeiraDTO.add(emissaoBandeiraDTO);
		return listaEmissaoBandeiraDTO;
	}
	
	@Get
	@Path("/imprimirArquivo")
	public void imprimirArquivo(Integer semana,	String sortname, String sortorder, int rp, int page, FileType fileType) {
	
		List<EmissaoBandeiraDTO> listaEmissaoBandeiraDTO = getListaEmissaoBandeira();
		
		List<EmissaoBandeiraVO> listaEmissaoBandeiraVO =  new ArrayList<EmissaoBandeiraVO>();
		
		for (EmissaoBandeiraDTO dto :listaEmissaoBandeiraDTO ){
			listaEmissaoBandeiraVO.add(new EmissaoBandeiraVO (dto));
		}
		
		
		if (listaEmissaoBandeiraVO != null && !listaEmissaoBandeiraVO.isEmpty()) {
			try {
				
				FileExporter.to("emissao-bandeira", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, listaEmissaoBandeiraVO,EmissaoBandeiraVO.class, this.response);
				
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Erro ao gerar o arquivo!"));
			}
		}
		
		this.result.use(Results.nothing());
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

	@Get("/imprimirBandeira")
	public Download imprimirBandeira(Integer semana, Integer numeroPallets  ) throws Exception{
		
		byte[] comprovate = this.gerarDocumentoIreport(semana,numeroPallets);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeira.pdf", true);
	}
	
	

	
	/**
	 * Gera um relatório à partir de um Objeto com atributos e listas definidas
	 * 
	 * @param list
	 * @param pathJasper
	 * @return Array de bytes do relatório gerado
	 * @throws JRException
	 * @throws URISyntaxException
	 */
	private byte[] gerarDocumentoIreport(Integer semana, Integer numeroPallets ) throws JRException, URISyntaxException {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource( getListaImpressaoBandeiraVO(semana,numeroPallets)); 
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		String path = url.toURI().getPath();
		return JasperRunManager.runReportToPdf(path, parameters,ds);
	}

	

	private List<ImpressaoBandeiraVO> getListaImpressaoBandeiraVO(Integer semana, Integer numeroPallets ) {
		List<ImpressaoBandeiraVO> listaImpressaoBandeiraVO = new ArrayList<ImpressaoBandeiraVO>();
		
		for(int i=1; i <= numeroPallets;i++){
			ImpressaoBandeiraVO impressaoBandeiraVO = new ImpressaoBandeiraVO();
			
			if (i%2 == 0){
				impressaoBandeiraVO.setTipoOperacao("FC");
			} else {
				impressaoBandeiraVO.setTipoOperacao("DINAP");
			}
			
			impressaoBandeiraVO.setSemana(semana);
			impressaoBandeiraVO.setCodigoPracaProcon("148018"+i);
			impressaoBandeiraVO.setPraca("FORTALEZA");
			impressaoBandeiraVO.setDestino("ENCALHE");
			impressaoBandeiraVO.setCanal("BANCAS");
			listaImpressaoBandeiraVO.add(impressaoBandeiraVO);
		}
		
		
		return listaImpressaoBandeiraVO;
	}
	
	
	
	
	

	@Path("/bandeiraManual")
	public void bandeiraManual() {
		
	}
	
	@Get("/imprimirBandeiraManual")
	public Download imprimirBandeiraManual(Integer semana, Integer numeroPallets,String tipoOperacao, String codigoPracaProcon, String praca, String destino, String canal ) throws Exception{
		
		byte[] comprovate = this.gerarDocumentoIreport(semana,  numeroPallets, tipoOperacao,  codigoPracaProcon,  praca,  destino,  canal);
		
		return new ByteArrayDownload(comprovate,"application/pdf", "imprimirBandeira.pdf", true);
	}
	
	
	
	
	private List<ImpressaoBandeiraVO> getListaImpressaoBandeiraVO(Integer semana, Integer numeroPallets,String tipoOperacao, String codigoPracaProcon, String praca, String destino, String canal ) {
		List<ImpressaoBandeiraVO> listaImpressaoBandeiraVO = new ArrayList<ImpressaoBandeiraVO>();
		
		for(int i=1; i <= numeroPallets;i++){
			ImpressaoBandeiraVO impressaoBandeiraVO = new ImpressaoBandeiraVO();
			impressaoBandeiraVO.setTipoOperacao(tipoOperacao);
			
			impressaoBandeiraVO.setSemana(semana);
			impressaoBandeiraVO.setCodigoPracaProcon(codigoPracaProcon);
			impressaoBandeiraVO.setPraca(praca);
			impressaoBandeiraVO.setDestino(destino);
			impressaoBandeiraVO.setCanal(canal);
			listaImpressaoBandeiraVO.add(impressaoBandeiraVO);
		}
		
		
		return listaImpressaoBandeiraVO;
	}
	
	
	private byte[] gerarDocumentoIreport(Integer semana, Integer numeroPallets,String tipoOperacao, String codigoPracaProcon, String praca, String destino, String canal ) throws JRException, URISyntaxException {
		Map<String, Object> parameters = new HashMap<String, Object>();
	    JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource( getListaImpressaoBandeiraVO( semana,  numeroPallets, tipoOperacao,  codigoPracaProcon,  praca,  destino,  canal )); 
		URL url = Thread.currentThread().getContextClassLoader().getResource("/reports/emissao_bandeira.jasper");
		String path = url.toURI().getPath();
		return JasperRunManager.runReportToPdf(path, parameters,ds);
	}

	
}
