package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.EstudoComplementarRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.InformacoesProdutoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.EstudoComplementarService;


@Service
public class EstudoComplementarServiceImpl implements EstudoComplementarService {

    @Autowired
    EstudoRepository estudoRepository;

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoEdicaoRepository produtoEdicaoRepository;


    @Autowired
    EstudoCotaRepository estudoCotaRepository;

    @Autowired
    EstudoComplementarRepository estudoComplementarRepository;

    @Autowired
    InformacoesProdutoRepository informacoesProdutoRepository;

    @Override
    @Transactional(readOnly = true)
    public EstudoComplementarDTO obterEstudoComplementarPorIdEstudoBase(
	    long idEstudoBase) {
    	EstudoComplementarDTO estudoComplDto = null;
		Estudo estudo = estudoRepository.buscarPorId(idEstudoBase);
	
		if (estudo == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Estudo " + idEstudoBase + " não encontrado.");
		}
		
		ProdutoEdicao  pe = produtoEdicaoRepository.buscarPorId(estudo.getProdutoEdicao().getId());
	
		FiltroInformacoesProdutoDTO dto = new FiltroInformacoesProdutoDTO();
		dto.setCodProduto(pe.getProduto().getCodigo());
		dto.setNumeroEdicao(pe.getNumeroEdicao());
		dto.setNomeProduto(pe.getProduto().getNome());
	
		List<InformacoesProdutoDTO> buscarProdutos = informacoesProdutoRepository.buscarProdutos(dto);
	
		estudoComplDto = new EstudoComplementarDTO();
	
		estudoComplDto.setIdEstudo(estudo.getId());
		estudoComplDto.setIdEstudoComplementar(estudoComplementarRepository.gerarNumeroEstudoComplementar());
		estudoComplDto.setIdProduto(pe.getProduto().getId());
		estudoComplDto.setNomeProduto(pe.getProduto().getNome());
		estudoComplDto.setIdEdicao(pe.getId());
		estudoComplDto.setNumeroEdicao(pe.getNumeroEdicao());
		estudoComplDto.setCodigoProduto(pe.getProduto().getCodigo());
		estudoComplDto.setNomeClassificacao(pe.getProduto().getTipoClassificacaoProduto().getDescricao()==null?"":pe.getProduto().getTipoClassificacaoProduto().getDescricao()); 
		estudoComplDto.setIdPublicacao(pe.getNumeroEdicao());
		estudoComplDto.setIdPEB(pe.getProduto().getPeb());
		estudoComplDto.setNomeFornecedor( pe.getProduto().getFornecedor().getJuridica().getNomeFantasia()==null?"":pe.getProduto().getFornecedor().getJuridica().getNomeFantasia());
	
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
		if(buscarProdutos!=null && !buscarProdutos.isEmpty()){
		    estudoComplDto.setDataRclto(buscarProdutos.get(0).getDataRcto());
		}
		
		return estudoComplDto;
    }

    @Transactional
    @Override
    public boolean gerarEstudoComplementar(EstudoComplementarVO estudoComplementarVO) {
		List<EstudoCota> estudoCotas =  selecionarBancas(estudoComplementarVO);
	
		if (estudoCotas.isEmpty()){
		    throw new ValidacaoException(TipoMensagem.ERROR, "Nenhuma cota foi encontrada nos parâmetros para gerar o estudo complementar.");
		}
	
		BigInteger qtdReparte = BigInteger.valueOf(estudoComplementarVO.getReparteCota());
		BigInteger qtdDistribuido = BigInteger.valueOf(estudoComplementarVO.getReparteDistribuicao());
	
	
		Estudo estudo = estudoRepository.buscarPorId(estudoComplementarVO.getCodigoEstudo());
	
		Estudo estudo1 = new Estudo();
		estudo1.setDataAlteracao(estudo.getDataAlteracao());
		estudo1.setDataCadastro(estudo.getDataCadastro());
		estudo1.setDataLancamento(estudo.getDataLancamento());
		estudo1.setProdutoEdicao(estudo.getProdutoEdicao());
		estudo1.setLiberado(false);
		estudo1.setQtdeReparte(estudo.getQtdeReparte());
		estudo1.setStatus(estudo.getStatus());
	
		// Gera Novo Estudo
		long NumeroEstudo = estudoRepository.adicionar(estudo1);
		estudo1.setId(NumeroEstudo);
	
		List<EstudoCota> estudoCotaNovo = new ArrayList<EstudoCota>();
		for(EstudoCota estudoCota: estudoCotas){
		    EstudoCota ec = new EstudoCota();
		    ec.setClassificacao(estudoCota.getClassificacao());
		    ec.setCota(estudoCota.getCota());
		    ec.setQtdeEfetiva(estudoCota.getQtdeEfetiva());
		    ec.setQtdePrevista(estudoCota.getQtdeEfetiva());
		    ec.setReparte(qtdReparte);
		    estudoCotaNovo.add(ec);
	
		}
	
		boolean primeiraVez=true;
		while (qtdDistribuido.compareTo(BigInteger.ZERO)>0 ){
		    for(int i=0; i<estudoCotaNovo.size();i++ ){
	
			EstudoCota estudoCota = estudoCotaNovo.get(i);
			if(primeiraVez){
			    estudoCota.setQtdeEfetiva(BigInteger.ZERO);
			}
	
			estudoCota.setQtdeEfetiva(qtdReparte.add(estudoCota.getQtdeEfetiva()));
	
			estudoCota.setClassificacao("CP");
			estudoCotaNovo.set(i, estudoCota);
			qtdDistribuido = qtdDistribuido.subtract(qtdReparte);
	
			if(qtdDistribuido.compareTo(BigInteger.ZERO)<=0 ){
			    break;
			}
	
		    }
		    primeiraVez=false;
		}
	
		for(EstudoCota estcota: estudoCotaNovo){
		    EstudoCota ec1 =new EstudoCota();
		    String classificacao = estcota.getClassificacao();
		    ec1.setClassificacao(classificacao);
		    ec1.setCota(estcota.getCota());
		    ec1.setEstudo(estudo1);
		    ec1.setReparte(qtdReparte);
		    ec1.setQtdeEfetiva(estcota.getQtdeEfetiva());
		    ec1.setQtdePrevista(BigInteger.TEN);
	
		    estudoCotaRepository.adicionar(ec1);	
		}
	
		return true;

    }

    private List<EstudoCota> selecionarBancas(EstudoComplementarVO estudoComplementarVO) {
    	return estudoComplementarRepository.selecionarBancas(estudoComplementarVO);
    }

    @Override
    public Long gerarNumeroEstudoComplementar() {
    	return estudoComplementarRepository.gerarNumeroEstudoComplementar();
    }
}
