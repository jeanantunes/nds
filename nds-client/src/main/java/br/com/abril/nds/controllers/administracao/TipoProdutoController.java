package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.TipoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.TipoProdutoService;
import br.com.abril.nds.service.exception.UniqueConstraintViolationException;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/tipoProduto")
public class TipoProdutoController {

	@Autowired
	private Result resutl;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private TipoProdutoService tipoProdutoService;
	
	@Path("/")
	public void index() {
		
	}
	
	@Post("/busca.json")
	public void busca(String descricao, Long codigo, 
			String codigoNCM, String codigoNBM, String sortname, String sortorder, int rp, int page ) {
		
		List<TipoProduto> listaTipoProdutos = this.tipoProdutoService.busca(descricao, codigo, codigoNCM, codigoNBM,
				sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp, rp);
		
		Long quantidade = this.tipoProdutoService.quantidade(descricao, codigo, codigoNCM, codigoNBM);
		
		this.resutl.use(FlexiGridJson.class).from(listaTipoProdutos)
			.total(quantidade.intValue()).page(page).serialize();
	}
	
	@Post("/getById.json")
	public void buscaPorId(Long id) {
		
		TipoProduto tipoProduto = this.tipoProdutoService.buscaPorId(id);
		
		this.resutl.use(Results.json()).from(tipoProduto, "tipoProduto").serialize();
	}
	
	@Post("/salva.json")
	public void salvar(TipoProduto tipoProduto) {
		
		this.valida(tipoProduto);
			
		this.tipoProdutoService.merge(tipoProduto);
		
		this.resutl.use(Results.json()).from(new ValidacaoVO
				(TipoMensagem.SUCCESS, "Tipo de Produto salvo com Sucesso."), "result").recursive().serialize();
	}
	
	@Post("/remove.json")
	public void remove(long id) {
		
		try {
			this.tipoProdutoService.remover(id);
		} catch (UniqueConstraintViolationException e) {
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		}
				
		this.resutl.use(Results.json()).from("OK").serialize();
	}
	
	@Post("/getCodigoSugerido.json")
	public void getCodigoSugerido() {
		String codigo = this.tipoProdutoService.getCodigoSugerido();
		
		this.resutl.use(Results.json()).from(codigo, "codigo").serialize();
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	public void exportar(FileType fileType) throws IOException {
		
		FiltroTipoProdutoDTO filtro = new FiltroTipoProdutoDTO();
		
		List<TipoProdutoDTO> listaTipoProduto = this.toDTO(this.tipoProdutoService.obterTodosTiposProduto());
		
		FileExporter.to("tipo-produto", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaTipoProduto, TipoProdutoDTO.class, this.response);
	}
	
	private void valida(TipoProduto tipoProduto) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (tipoProduto == null || StringUtil.isEmpty(tipoProduto.getDescricao())) {
			listaMensagens.add("O preenchimento do campo [Tipo de Produto] é obrigatório.");
		}
		
		if (tipoProduto == null || tipoProduto.getCodigo() == null ) {
			listaMensagens.add("O preenchimento do campo [Código] é obrigatório.");
		}
		
		if (!listaMensagens.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,
					listaMensagens));
		}
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
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: obter usuario do sistema de autenticação.
	private Usuario getUsuario() {
			
			Usuario usuario = new Usuario();
			
			usuario.setId(1L);
			
			usuario.setNome("Jornaleiro da Silva");
			
			return usuario;
	}
	
	private List<TipoProdutoDTO> toDTO(List<TipoProduto> listaTipoProduto) {
		
		List<TipoProdutoDTO> lista = new ArrayList<TipoProdutoDTO>();
		
		if (listaTipoProduto != null) {
			for (TipoProduto tipoProduto : listaTipoProduto) {
			
				TipoProdutoDTO tipoProdutoDTO = new TipoProdutoDTO();
			
				tipoProdutoDTO.setCodigo(tipoProduto.getCodigo());
				tipoProdutoDTO.setCodigoNBM(tipoProduto.getCodigoNBM());
				tipoProdutoDTO.setCodigoNCM(tipoProduto.getCodigoNCM());
				tipoProdutoDTO.setDescricao(tipoProduto.getDescricao());
			
				lista.add(tipoProdutoDTO);
			}
		}
		return lista;
	}
}
