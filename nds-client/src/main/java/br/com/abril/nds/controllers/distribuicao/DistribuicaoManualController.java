package br.com.abril.nds.controllers.distribuicao;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.EstudoCotaDTO;
import br.com.abril.nds.dto.EstudoDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicaoManual")
@Resource
public class DistribuicaoManualController extends BaseController {

    @Autowired
    private Result result;
    
    @Autowired
    private CotaService cotaService;

    @Autowired
    private EstudoService estudoService;

    @Path("/")
    public void index(ProdutoDistribuicaoVO produto) {
	result.include("produto", produto);
    }
    
    @Post
    @Path("/consultarCotaPorNumero")
    public void consultarCotaPorNumero(Integer numeroCota) throws Exception {
	CotaDTO cotaDTO = new CotaDTO();
	Cota cota = this.cotaService.obterPorNumeroDaCota(numeroCota);
	if (cota == null) {
	    throw new Exception("Não foi encontrada nenhuma cota com este número.");
	} else {
	    cotaDTO.setNumeroCota(cota.getNumeroCota());
	    cotaDTO.setIdCota(cota.getId());
	    cotaDTO.setNomePessoa(cota.getPessoa().getNome());
	    cotaDTO.setStatus(cota.getSituacaoCadastro());
	}
	result.use(Results.json()).from(cotaDTO, "result").recursive().serialize();
    }

    @Post
    @Path("/consultarCotaPorNome")
    public void consultarCotaPorNome(String nomeCota) {
	List<ItemAutoComplete> lista = new ArrayList<>();
	for (CotaDTO cota : this.cotaService.obterPorNomeAutoComplete(nomeCota)) {
	    ItemAutoComplete item = new ItemAutoComplete(cota.getNumeroCota().toString(), cota.getNomePessoa(), cota);
	    lista.add(item);
	}
	this.result.use(Results.json()).from(lista, "result").include("value", "chave").serialize();
    }
    
    @Post
    @Path("/gravarEstudo")
    public void gravarEstudo(EstudoDTO estudoDTO, List<EstudoCotaDTO> estudoCotasDTO) throws Exception {
	Estudo estudo = new Estudo();
	estudo.setProdutoEdicao(new ProdutoEdicao(estudoDTO.getProdutoEdicaoId()));
	estudo.setReparteDistribuir(BigInteger.valueOf(estudoDTO.getReparteDistribuir()));
	estudo.setQtdeReparte(BigInteger.valueOf(estudoDTO.getReparteDistribuir()));
	estudo.setDataCadastro(new Date());
	estudo.setStatus(StatusLancamento.ESTUDO_FECHADO);
	estudo.setLiberado(0);
	try {
	    estudo.setDataLancamento(new SimpleDateFormat("dd/MM/yyyy").parse(estudoDTO.getDataLancamento()));
	} catch (ParseException e) {
	    throw new Exception("Data de lançamento em formato incorreto.");
	}
	for (EstudoCotaDTO cotaDTO : estudoCotasDTO) {
	    EstudoCota estudoCota = new EstudoCota();
	    estudoCota.setCota(new Cota(cotaDTO.getIdCota()));
	    estudoCota.setQtdePrevista(cotaDTO.getQtdeEfetiva());
	    estudoCota.setQtdeEfetiva(cotaDTO.getQtdeEfetiva());
	    estudoCota.setReparte(cotaDTO.getQtdeEfetiva());
	    estudo.getEstudoCotas().add(estudoCota);
	}
	estudoService.gravarEstudo(estudo);
	result.use(Results.json()).from(estudo.getId(), "result").serialize();
    }
}
