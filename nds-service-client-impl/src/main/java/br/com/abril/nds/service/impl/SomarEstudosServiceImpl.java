package br.com.abril.nds.service.impl;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.EstudoCotaGerado;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.planejamento.TipoGeracaoEstudo;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaGeradoRepository;
import br.com.abril.nds.repository.EstudoGeradoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.SomarEstudosService;
import br.com.abril.nds.util.BigIntegerUtil;

@Service
public class SomarEstudosServiceImpl implements SomarEstudosService {
	
	@Autowired
	private EstudoGeradoRepository estudoGeradoRepository;
	
	@Autowired
	private EstudoCotaGeradoRepository estudoCotaGeradoRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private DistribuicaoRepository distribuicaoRepository;
	
	@Autowired
	private EstudoService estudoService;


	@Override
	@Transactional
	public void somarEstudos(Long idEstudoBase, ProdutoDistribuicaoVO distribuicaoVO) {
		
		if (!produtoEdicaoRepository.estudoPodeSerSomado(idEstudoBase, distribuicaoVO.getCodigoProduto())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Estudo não pode ser somado.");
		}
		
		Long idEstudo = distribuicaoVO.getIdEstudo().longValue();
		EstudoGerado estudoBase = estudoGeradoRepository.buscarPorId(idEstudoBase);
		
		Map<Long,EstudoCotaGerado> mapEstudoCotaBase = new HashMap<Long,EstudoCotaGerado>();
		
		if (estudoBase.getEstudoCotas() != null && !estudoBase.getEstudoCotas().isEmpty()) {
			
			for (EstudoCotaGerado estudoCotaBase : estudoBase.getEstudoCotas()) {
				
				final Cota cota = estudoCotaBase.getCota();
				
				if((cota != null) && (BigIntegerUtil.isMaiorQueZero(estudoCotaBase.getReparte()))) {
					
					mapEstudoCotaBase.put(cota.getId(), estudoCotaBase);
				}
			}
		}
		
		if (mapEstudoCotaBase.isEmpty()) {
			
			return; 
		}
		
		EstudoGerado estudo = estudoGeradoRepository.buscarPorId(idEstudo);
		
		if (estudo.getEstudoCotas() != null && !estudo.getEstudoCotas().isEmpty()) {
			
			for (EstudoCotaGerado estudoCota : estudo.getEstudoCotas()) {
				
				if (estudoCota.getCota() != null) {
					
					Long idCota = estudoCota.getCota().getId();
					
					if (mapEstudoCotaBase.containsKey(idCota)) {
					  	
						EstudoCotaGerado estudoCotaBase = mapEstudoCotaBase.get(idCota);
						
						if (estudoCotaBase.getReparte() != null && estudoCota.getReparte() != null) {
							estudoCota.setReparte(estudoCota.getReparte().add(estudoCotaBase.getReparte()));
							mapEstudoCotaBase.remove(idCota);
						}
					}
				}
			}
		
			for (EstudoCotaGerado estudoCota : mapEstudoCotaBase.values()) {
				
				estudoCota.setEstudo(estudo);
				estudoCota.setQtdeEfetiva(estudoCota.getReparte());
				estudoCota.setQtdePrevista(estudoCota.getReparte());
				estudoCotaGeradoRepository.alterar(estudoCota);
				
				estudoBase.getEstudoCotas().remove(estudoCota);
			}
			
			estudo.setQtdeReparte((estudo.getQtdeReparte()!=null ? estudo.getQtdeReparte() : BigInteger.ZERO).add(estudoBase.getQtdeReparte()!=null ? estudoBase.getQtdeReparte() : BigInteger.ZERO));
			
			estudo.setSobra((estudo.getSobra()!=null ? estudo.getSobra() : BigInteger.ZERO).add(estudoBase.getSobra()!=null ? estudoBase.getSobra() : BigInteger.ZERO));
			
			estudo.setTipoGeracaoEstudo(TipoGeracaoEstudo.SOMA);
			
			estudoGeradoRepository.alterar(estudo);
			estudoGeradoRepository.remover(estudoBase);
			
			estudoService.criarRepartePorPDV(estudo.getId());
			
		}
		
	}

	@Override
	@Transactional
	public Boolean verificarCoincidenciaEntreCotas(Long estudoBase,Long estudoSomado) {
		
		Long count = estudoGeradoRepository.countDeCotasEntreEstudos(estudoBase,estudoSomado);
		
		return (count>0);
	}
	
}