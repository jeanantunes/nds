package br.com.abril.nds.controllers.devolucao;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * 
 * Classe responsável por controlas as ações da pagina de  Informe de Recolhimentos.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/informeEncalhe")
public class ConsultaInformeEncalheController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private LancamentoService lancamentoService;	
	
	@Get
	public void index(){}	
	

	@Path("/busca.json")
	@Post
	public void busca(Long idFornecedor,Integer semanaRecolhimento,Calendar dataRecolhimento, String sortname, String sortorder, int rp, int page){
		Calendar dataInicioRecolhimento,dataFimRecolhimento;
		
		if (semanaRecolhimento != null) {
			dataInicioRecolhimento = Calendar.getInstance();
			dataInicioRecolhimento.set(Calendar.WEEK_OF_YEAR, semanaRecolhimento);
			dataFimRecolhimento =(Calendar) dataInicioRecolhimento.clone();
			dataFimRecolhimento.add(Calendar.DAY_OF_MONTH, 7);
		}else{
			dataInicioRecolhimento = dataRecolhimento;
			dataFimRecolhimento = dataRecolhimento;
		}
		Long quantidade=lancamentoService.quantidadeLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento);
		List<InformeEncalheDTO> informeEncalheDTOs = lancamentoService.obterLancamentoInformeRecolhimento(idFornecedor, dataInicioRecolhimento, dataFimRecolhimento, 
				sortname, Ordenacao.valueOf(sortorder.toUpperCase()), page*rp - rp , rp);
		
		result.use(FlexiGridJson.class).from(informeEncalheDTOs).total(quantidade.intValue()).page(page).serialize();
	}
	
	

}
