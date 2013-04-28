package br.com.abril.nds.controllers.lancamento;


import static br.com.caelum.vraptor.view.Results.json;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.client.vo.estudocomplementar.BaseEstudoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoComplementarService;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/lancamento")
public class EstudoComplementarController  extends BaseController{
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private EstudoComplementarService estudoComplementarService;
	

	
	@Path("/estudoComplementar")
	public void index() {
		
		String data = DateUtil.formatarDataPTBR(new Date());
		result.include("data", data);
	}
	
	
	
	@Path("/pesquisaEstudoBase/{estudoBase.id}")
	public void pesquisaEstudoBase(EstudoCotaDTO estudoBase){
	
		EstudoComplementarDTO estudo = estudoComplementarService.obterEstudoComplementarPorIdEstudoBase(estudoBase.getId());
		BaseEstudoVO baseEstudo = new BaseEstudoVO();
		
		baseEstudo.setIdEstudo(estudo.getIdEstudo());
		baseEstudo.setIdEstudoComplementar(estudo.getIdEstudoComplementar());
		baseEstudo.setIdProduto(estudo.getIdProduto());
		baseEstudo.setNomeProduto(estudo.getNomeProduto());
		
		baseEstudo.setIdEdicao(estudo.getIdEdicao());
		baseEstudo.setNomeClassificacao(estudo.getNomeClassificacao());
		baseEstudo.setIdPublicacao(estudo.getIdPublicacao());
		baseEstudo.setIdPEB(estudo.getIdPEB());
		baseEstudo.setNomeFornecedor(estudo.getNomeFornecedor());
		baseEstudo.setDataLncto(estudo.getDataLncto());
		baseEstudo.setDataRclto(estudo.getDataRclto());
		baseEstudo.setReparteLancamento(estudo.getQtdeReparte());

		
		
		result.use(json()).from(baseEstudo).serialize();		
	}

	@Path("/gerarEstudo")
	@Post
	public void gerarEstudo(EstudoComplementarVO parametros){
		System.out.println( "----->" + parametros.getCodigoEstudo());
		System.out.println( "----->" + parametros.getReparteCota());
		System.out.println( "----->" + parametros.getTipoSelecao());
		if(estudoComplementarService.gerarEstudoComplementar(parametros))
			
		result.nothing();
		
	}
	
}

