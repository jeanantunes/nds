package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.dto.InformacoesProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroInformacoesProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.TipoEstudoCota;
import br.com.abril.nds.model.planejamento.TipoGeracaoEstudo;
import br.com.abril.nds.repository.EstudoComplementarRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.EstudoComplementarService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.UsuarioService;

@Service
public class EstudoComplementarServiceImpl implements EstudoComplementarService {

    @Autowired
    private EstudoGeradoRepository estudoGeradoRepository;

    @Autowired
    private ProdutoEdicaoRepository produtoEdicaoRepository;

    @Autowired
    private EstudoCotaGeradoRepository estudoCotaGeradoRepository;

    @Autowired
    private EstudoComplementarRepository estudoComplementarRepository;

    @Autowired
    private InformacoesProdutoRepository informacoesProdutoRepository;
    
    @Autowired
    private LancamentoService lancamentoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;

    @Override
    @Transactional(readOnly = true)
    public EstudoComplementarDTO obterEstudoComplementarPorIdEstudoBase(long idEstudoBase) {
	
	EstudoComplementarDTO estudoComplDto = null;
	EstudoGerado estudo = estudoGeradoRepository.buscarPorId(idEstudoBase);

	if (estudo == null) {
	    throw new ValidacaoException(TipoMensagem.WARNING, "Estudo " + idEstudoBase + " não encontrado.");
	}
	
	if(estudo.getProdutoEdicao().getProduto().getTipoSegmentoProduto() == null){
		throw new ValidacaoException(TipoMensagem.WARNING, "Estudo " + idEstudoBase + " está sem segmento.");
	}

	ProdutoEdicao  pe = produtoEdicaoRepository.buscarPorId(estudo.getProdutoEdicao().getId());

	FiltroInformacoesProdutoDTO dto = new FiltroInformacoesProdutoDTO();
	dto.setCodProduto(pe.getProduto().getCodigo());
	dto.setNumeroEdicao(pe.getNumeroEdicao());
	dto.setNomeProduto(pe.getProduto().getNome());

	List<InformacoesProdutoDTO> buscarProdutos = informacoesProdutoRepository.buscarProdutos(dto);

	estudoComplDto = new EstudoComplementarDTO();

	estudoComplDto.setIdEstudo(estudo.getId());
	estudoComplDto.setIdProduto(pe.getProduto().getId());
	estudoComplDto.setNomeProduto(pe.getProduto().getNome());
	estudoComplDto.setIdEdicao(pe.getId());
	estudoComplDto.setNumeroEdicao(pe.getNumeroEdicao());
	estudoComplDto.setCodigoProduto(pe.getProduto().getCodigo());
	estudoComplDto.setNomeClassificacao((pe.getTipoClassificacaoProduto() == null || pe.getTipoClassificacaoProduto().getDescricao()==null)?"":pe.getTipoClassificacaoProduto().getDescricao());
	estudoComplDto.setIdPublicacao(pe.getNumeroEdicao());
	estudoComplDto.setIdPEB(pe.getProduto().getPeb());
	estudoComplDto.setNomeFornecedor( pe.getProduto().getFornecedor().getJuridica().getNomeFantasia()==null?"":pe.getProduto().getFornecedor().getJuridica().getNomeFantasia());
	estudoComplDto.setTipoSegmentoProduto(estudo.getProdutoEdicao().getProduto().getTipoSegmentoProduto().getDescricao()!=null?estudo.getProdutoEdicao().getProduto().getTipoSegmentoProduto().getDescricao():"");

	Set<Lancamento> lancamentos = pe.getLancamentos();

	if (lancamentos == null || lancamentos.size() == 0) {
	    throw new ValidacaoException(TipoMensagem.WARNING, "Não existem lançamentos para a edição " + pe.toString());
	}

	lancamentos.iterator().next().getDataRecolhimentoDistribuidor();

	estudoComplDto.setQtdeReparte (estudo.getQtdeReparte());

	Date dtLancamento = estudo.getDataLancamento();
	String dataLancamento = "";

	if (dtLancamento != null) {
	    dataLancamento = new SimpleDateFormat("dd/MM/yyyy").format(dtLancamento);
	}

	estudoComplDto.setDataLncto(dataLancamento);
	if(buscarProdutos != null && !buscarProdutos.isEmpty()){
	    estudoComplDto.setDataRclto(buscarProdutos.get(0).getDataRcto());
	}

	return estudoComplDto;
    }

    @Transactional
    @Override
    public Long gerarEstudoComplementar(EstudoComplementarVO estudoComplementarVO) {
        
		List<EstudoCotaGerado> estudoCotas = selecionarBancas(estudoComplementarVO);

        if (estudoCotas.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota foi encontrada nos parâmetros para gerar o estudo complementar.");
        }

        EstudoGerado estudo = estudoGeradoRepository.buscarPorId(estudoComplementarVO.getCodigoEstudo());
        
        validarEdicaoEProduto(estudoComplementarVO, estudo);
        
        BigInteger reparte = BigInteger.valueOf(estudoComplementarVO.getReparteCota());
        BigInteger qtdDistribuido = BigInteger.valueOf(estudoComplementarVO.getReparteDistribuicao());


        EstudoGerado estudo1 = new EstudoGerado();
        BeanUtils.copyProperties(estudo, estudo1, new String[] {"id", "lancamentos", "estudoCotas", "dataAlteracao"});
        estudo1.setLiberado(false);
        estudo1.setProdutoEdicao(new ProdutoEdicao(estudoComplementarVO.getIdProdutoEdicao()));
        estudo1.setQtdeReparte(qtdDistribuido);
        estudo1.setReparteDistribuir(qtdDistribuido);
        estudo1.setSobra(estudo1.getQtdeReparte().subtract(estudo1.getReparteDistribuir()));
        estudo1.setDataCadastro(new Date());
        estudo1.setUsuario(this.usuarioService.getUsuarioLogado());
        estudo1.setTipoGeracaoEstudo(TipoGeracaoEstudo.DIVISAO);

        // Gera Novo Estudo
        Long idEstudo = estudoGeradoRepository.adicionar(estudo1);
        
        List<EstudoCotaGerado> cotas = new ArrayList<>();

        for (EstudoCotaGerado cota : estudoCotas) {
            EstudoCotaGerado nova = new EstudoCotaGerado();
            BeanUtils.copyProperties(cota, nova, new String[] {"id", "reparte", "rateiosDiferenca", "movimentosEstoqueCota", "itemNotaEnvios"});
            nova.setEstudo(estudo1);
            nova.setReparte(reparte);
            nova.setReparteInicial(reparte);
            nova.setClassificacao("CP");
            nova.setTipoEstudo(TipoEstudoCota.NORMAL);
            cotas.add(nova);

            qtdDistribuido = qtdDistribuido.subtract(reparte);

            if (qtdDistribuido.compareTo(reparte) < 0) {
            break;
            }
        }

        // reordenando de acordo com o ranking
        cotas = ordenarCotas(cotas, estudoComplementarVO);
        if (!estudoComplementarVO.isMultiplo()) {
            reparte = BigInteger.ONE;
        } 
        while (qtdDistribuido.compareTo(reparte) > 0) {
            for (EstudoCotaGerado cota : cotas) {
            cota.setReparte(cota.getReparte().add(reparte));
            qtdDistribuido = qtdDistribuido.subtract(reparte);

            if (qtdDistribuido.compareTo(reparte) < 0) {
                break;
            }
            }
        }

        for (EstudoCotaGerado cota : cotas) {
            if (cota.getReparte() != null) {
            cota.setQtdeEfetiva(cota.getReparte());
            cota.setQtdePrevista(cota.getReparte());
            }
            estudoCotaGeradoRepository.adicionar(cota); 
        }

        this.matrizDistribuicaoService.atualizarPercentualAbrangencia(idEstudo);
        
        return idEstudo;
    }

	private void validarEdicaoEProduto(EstudoComplementarVO estudoComplementarVO, EstudoGerado estudo) {
		
		Lancamento lancParaEstudo = lancamentoService.buscarPorId(estudoComplementarVO.getIdLancamento());
        
        if((lancParaEstudo.getProdutoEdicao().getNumeroEdicao() != estudo.getProdutoEdicao().getNumeroEdicao()) || 
        		(lancParaEstudo.getProdutoEdicao().getProduto().getCodigo() != estudo.getProdutoEdicao().getProduto().getCodigo())){
        	
        	throw new ValidacaoException(TipoMensagem.WARNING, "O estudo utilizado como base, não é da mesma edição/produto do estudo a ser criado.");
        }
	}

    private List<EstudoCotaGerado> ordenarCotas(List<EstudoCotaGerado> estudoCotas, EstudoComplementarVO estudoComplementarVO) {
	Map<Long, EstudoCotaGerado> mapCotas = new HashMap<>();
	for (EstudoCotaGerado cota : estudoCotas) {
	    mapCotas.put(cota.getCota().getId(), cota);
	}
	LinkedList<EstudoCotaGerado> cotasOrdenadas = new LinkedList<>(estudoComplementarRepository.getCotasOrdenadas(estudoComplementarVO));
	LinkedList<EstudoCotaGerado> retorno = new LinkedList<>();
	
	for (EstudoCotaGerado cota : cotasOrdenadas) {
	    if (mapCotas.get(cota.getCota().getId()) != null) {
		retorno.add(mapCotas.get(cota.getCota().getId()));
	    }
	}
	return retorno;
    }

    private List<EstudoCotaGerado> selecionarBancas(EstudoComplementarVO estudoComplementarVO) {
	return estudoComplementarRepository.selecionarBancas(estudoComplementarVO);
    }
}
