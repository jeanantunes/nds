package br.com.abril.nds.controllers.administracao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.service.TipoDescontoCotaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/tipoDescontoCota")
public class TipoDescontoCotaController {
	
	private Result result;
	
	@Autowired
	private TipoDescontoCotaService tipoDescontoCotaService;
	
	@SuppressWarnings("unused")
	private HttpSession httpSession;
	
	public TipoDescontoCotaController(Result result, HttpSession httpSession) {
		this.result = result; 
		this.httpSession = httpSession;
	}
	
	@Path("/")
	public void index() {		
		
	}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(String desconto, String dataAlteracao, String usuario){		
		
		try {
			TipoDescontoCota tipoDescontoCota = new TipoDescontoCota();
			Long parseDesconto = Long.parseLong(desconto);
			tipoDescontoCota.setDesconto(parseDesconto);
			SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
			Date dataFormatada;
			dataFormatada = sdf.parse(dataAlteracao);
			tipoDescontoCota.setDataAlteracao(dataFormatada);
			tipoDescontoCota.setUsuario(usuario);
			
			this.tipoDescontoCotaService.incluirDescontoGeral(tipoDescontoCota);
			
			atualizarDistribuidor(parseDesconto);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
		
	}
	
	private void atualizarDistribuidor(Long desconto) {
		this.tipoDescontoCotaService.atualizarDistribuidos(desconto);

	}
	
	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, Long descontoEspecifico, Date dataAlteracaoEspecifico, String usuarioEspecifico){



		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();

	}
	
	

}
