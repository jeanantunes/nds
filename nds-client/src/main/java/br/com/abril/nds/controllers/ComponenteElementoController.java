package br.com.abril.nds.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ComponenteElementoDTO;
import br.com.abril.nds.service.ComponenteElementoService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/componentes")
public class ComponenteElementoController {

    @Autowired
    private ComponenteElementoService componenteElementoService;

    private Result result;

    public ComponenteElementoController(Result result) {
	this.result = result;
    }

    @Path("/elementos")
    public void buscaElementos(String tipo, Long estudo) {
	List<ComponenteElementoDTO> retorno = componenteElementoService.buscaElementos(tipo, estudo);
	result.use(Results.json()).withoutRoot().from(retorno).recursive().serialize();
    }
}
