package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.EstudoComplementarVO;
import br.com.abril.nds.dto.EstudoComplementarDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.EstudoComplementarRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.service.EstudoComplementarService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;

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

    
	@Override
	@Transactional(readOnly = true)
	public EstudoComplementarDTO obterEstudoComplementarPorIdEstudoBase(
			long idEstudoBase) {
		// TODO Auto-generated method stub
		
		Estudo estudo = estudoRepository.buscarPorId(idEstudoBase);
		estudo.getDataLancamento();
		estudo.getLancamento();
		estudo.getProdutoEdicao();
		
		ProdutoEdicao  pe = produtoEdicaoRepository.buscarPorId(estudo.getProdutoEdicao().getId());
		
		
	    Produto produto = produtoRepository.buscarPorId(pe.getId());
	    
	   
	    
		EstudoComplementarDTO estudoComplDto = new EstudoComplementarDTO();
		
		
		
		estudoComplDto.setIdEstudo(estudo.getId());
		estudoComplDto.setIdEstudoComplementar(estudoComplementarRepository.gerarNumeroEstudoComplementar());
		estudoComplDto.setIdProduto(pe.getProduto().getId());
		estudoComplDto.setNomeProduto(pe.getProduto().getNome());
		estudoComplDto.setIdEdicao(pe.getId());
		estudoComplDto.setNomeClassificacao(pe.getProduto().getTipoClassificacaoProduto().getDescricao()==null?"":pe.getProduto().getTipoClassificacaoProduto().getDescricao()); 
		estudoComplDto.setIdPublicacao(pe.getNumeroEdicao());
		estudoComplDto.setIdPEB(pe.getProduto().getPeb());
		estudoComplDto.setNomeFornecedor( pe.getProduto().getFornecedor().getJuridica().getNomeFantasia()==null?"":pe.getProduto().getFornecedor().getJuridica().getNomeFantasia());
		
	    pe.getLancamentos().iterator().next().getDataRecolhimentoDistribuidor();
		
	   
	    estudoComplDto.setQtdeReparte (estudo.getQtdeReparte());
		String dataLancamento = new SimpleDateFormat("dd/MM/yyyy").format(estudo.getDataLancamento());  
		estudoComplDto.setDataLncto(dataLancamento);
		
		estudoComplDto.setDataRclto(estudo.getLancamento().getDataRecolhimentoDistribuidor().toString());

		
		return estudoComplDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean gerarEstudoComplementar(EstudoComplementarVO estudoComplementarVO) {
		
		
		
		
		List<EstudoCota> estudoCotas =  selecionarBancas(estudoComplementarVO);
		
		if (estudoCotas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.ERROR, "Nenhum registro encontrado.");
		}
			
		
		BigInteger qtdReparte = BigInteger.valueOf(estudoComplementarVO.getReparteCota());
		BigInteger qtdDistribuido = BigInteger.valueOf(estudoComplementarVO.getReparteDistribuicao());

		//gerar Numero de estudo
		Estudo estudo = new Estudo();
		
		
		
		//Estudo estudo = gerarNumeroEstudo(estudoComplementarVO.getCodigoEstudo());
		
		boolean primeiraVez=true;
		while (qtdDistribuido.compareTo(BigInteger.ZERO)>0 ){
			for(int i=0; i<estudoCotas.size();i++ ){
				
				
				
				EstudoCota estudoCota = estudoCotas.get(i);
				if(primeiraVez){
					estudoCota.setQtdeEfetiva(BigInteger.ZERO);
					estudoCota.setEstudo(null);
					estudoCota.setId(null);
					
					
				}
				
				estudoCota.setQtdeEfetiva(qtdReparte.and(estudoCota.getQtdeEfetiva()));
				estudoCota.setClassificacao("CP");
				estudoCota.setEstudo(estudo);
				
				estudoCotas.set(i, estudoCota);
				qtdDistribuido = qtdDistribuido.subtract(qtdReparte);
				
				if(qtdDistribuido.compareTo(BigInteger.ZERO)<=0 ){
					break;
				}
				
			}
			primeiraVez=false;
		}
		
		estudo.setEstudoCotas((Set<EstudoCota>) estudoCotas);
		estudo.setId(null);
		estudoRepository.adicionar(estudo);
		return false;
	}

	private List<EstudoCota> selecionarBancas(EstudoComplementarVO estudoComplementarVO) {
		return estudoComplementarRepository.selecionarBancas(estudoComplementarVO);
		
	}


	@Override
	public Long gerarNumeroEstudoComplementar() {
		
		return estudoComplementarRepository.gerarNumeroEstudoComplementar();
	}


}
