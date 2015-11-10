package br.com.abril.nds.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.NotaFiscalDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.estoque.MovimentoEstoque;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.OrigemItem;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscal;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoque;
import br.com.abril.nds.model.fiscal.OrigemItemNotaFiscalMovimentoEstoqueCota;
import br.com.abril.nds.model.fiscal.nota.DetalheNotaFiscal;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamento;
import br.com.abril.nds.repository.EstornoNFERepository;
import br.com.abril.nds.repository.UsuarioRepository;
import br.com.abril.nds.service.EstornoNFEService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;

@Service
public class EstornoNFEServiceImpl implements EstornoNFEService {
	
    @Autowired
    private ParametroSistemaService parametroSistemaService;
    
    @Autowired
    private DistribuidorService distribuidorService;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private EstornoNFERepository estornoNFERepository;
    
    private NFeService nFeService;
	
	@Override
	@Transactional
	public void estornoNotaFiscal(Long id) {
		
		NotaFiscal notaFiscal = new NotaFiscal();
		notaFiscal.setId(id);
		notaFiscal = nFeService.obterNotaFiscalPorId(notaFiscal);
		
		if(notaFiscal != null && notaFiscal.getNotaFiscalInformacoes() != null && notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal() != null) {
			
			for(DetalheNotaFiscal detNF : notaFiscal.getNotaFiscalInformacoes().getDetalhesNotaFiscal()) {
				
				for(OrigemItemNotaFiscal origemItem : detNF.getProdutoServico().getOrigemItemNotaFiscal()) {
					
					if(origemItem.getOrigem().equals(OrigemItem.MOVIMENTO_ESTOQUE_COTA)) {
						
						MovimentoEstoqueCota mec = ((OrigemItemNotaFiscalMovimentoEstoqueCota) origemItem).getMovimentoEstoqueCota();
						mec.setNotaFiscalEmitida(false);
					}
					
					if(origemItem.getOrigem().equals(OrigemItem.MOVIMENTO_ESTOQUE)) {
						
						MovimentoEstoque me = ((OrigemItemNotaFiscalMovimentoEstoque) origemItem).getMovimentoEstoque();
						me.setNotaFiscalEmitida(false);
					}
					
				}
				
			}
			
		} else {
			throw new ValidacaoException(TipoMensagem.WARNING, "Problema essa nota não pode ser estornada!");
		}
		
		notaFiscal.getNotaFiscalInformacoes().getIdentificacao().setJustificativaEntradaContigencia("Nota estornada com problema Produto / Cota!");
		
		notaFiscal.getNotaFiscalInformacoes().setStatusProcessamento(StatusProcessamento.NAO_TRANSMITIDA);
		
		this.nFeService.mergeNotaFiscal(notaFiscal);
	}
	
	@Override
	@Transactional
	public Long quantidade(FiltroMonitorNfeDTO filtro) {
		
		return estornoNFERepository.quantidade(filtro);
	}
	
	@Override
	@Transactional
	public List<NotaFiscalDTO> pesquisar(FiltroMonitorNfeDTO filtro) {
		
		return estornoNFERepository.obterListaNotasFiscaisEstorno(filtro);
		
	}
}