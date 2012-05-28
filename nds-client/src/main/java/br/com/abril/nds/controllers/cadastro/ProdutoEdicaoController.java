package br.com.abril.nds.controllers.cadastro;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/cadastro/edicao")
public class ProdutoEdicaoController {

	@Autowired
	private ProdutoEdicaoService peService;
	
	/** Traz a página inicial. */
	@Get
	@Path("/")
	public void index() { }
	
	@Post
	@Path("/pesquisarEdicoes.json")
	public void pesquisarEdicoes(String codigoProduto, String nomeProduto,
			Date dataLancamento, String situacaoLancamento,
			String codigoDeBarras, boolean brinde,
            String sortorder, String sortname, int page, int rp) {

		
		ProdutoEdicaoDTO dto = new ProdutoEdicaoDTO();
		dto.setCodigoProduto(codigoProduto);
		dto.setNomeProduto(nomeProduto);
		dto.setDataLancamento(dataLancamento);
		dto.setCodigoDeBarras(codigoDeBarras);
		dto.setPossuiBrinde(brinde);
		
		dto.setSituacaoLancamento(null);
		for (StatusLancamento status : StatusLancamento.values()) {
			if (status.getDescricao().equals(situacaoLancamento)) {
				dto.setSituacaoLancamento(status);
			}
		}
		
		
		List<ProdutoEdicaoDTO> lst = peService.pesquisarEdicoes(dto, sortorder, sortname, page, rp);
		
	}
	
}
