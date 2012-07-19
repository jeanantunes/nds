package br.com.abril.nds.controllers.administracao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CalendarioFeriadoDTO;
import br.com.abril.nds.model.cadastro.TipoFeriado;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/administracao/cadastroCalendario")
public class CadastroCalendarioController {
	
	@Autowired
	private CalendarioService calendarioService;
	
	private Result result;
	
	public CadastroCalendarioController() {
		
	}
	
	@Path("/")
	public void index(){
		
	}
	
	@Post
	public void cadastrarNovoFeriado(
			
			Date dtFeriado, 
			TipoFeriado tipoFeriado, 
			String descricao,
			Long idLocalidade,
			String ufSigla,
			boolean indOpera, 
			boolean indEfetuaCobranca,
			boolean indRepeteAnualmente
			
			){
		
		CalendarioFeriadoDTO calendarioFeriado = new CalendarioFeriadoDTO();
		
		calendarioFeriado.setDataFeriado(dtFeriado);
		calendarioFeriado.setTipoFeriado(tipoFeriado);
		calendarioFeriado.setDescricaoFeriado(descricao);
		
		
		calendarioFeriado.setIndOpera(indOpera);
		calendarioFeriado.setIndEfetuaCobranca(indEfetuaCobranca);
		calendarioFeriado.setIndRepeteAnualmente(indRepeteAnualmente);

		calendarioFeriado.setIdLocalidade(idLocalidade);
		calendarioFeriado.setUfSigla(ufSigla);
		
		calendarioService.cadastrarFeriado(calendarioFeriado);
		
		
	}
	
	public void obterFeriados(int anoVigencia) {
		
		//TODO obter parametros
		anoVigencia = 2012;
		
		Map<Date, String> mapaFeriados = calendarioService.obterListaDataFeriado(anoVigencia);
		
		result.use(CustomJson.class).from(mapaFeriados).serialize();
	
	}
	
	
	
	

}
