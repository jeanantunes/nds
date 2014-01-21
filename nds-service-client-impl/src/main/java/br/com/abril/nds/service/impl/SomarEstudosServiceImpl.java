package br.com.abril.nds.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.DistribuicaoRepository;
import br.com.abril.nds.repository.EstudoCotaRepository;
import br.com.abril.nds.repository.EstudoRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.SomarEstudosService;

@Service
public class SomarEstudosServiceImpl implements SomarEstudosService {
	
	@Autowired
	private EstudoRepository estudoRepository;
	
	@Autowired
	private EstudoCotaRepository estudoCotaRepository;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;

	@Autowired
	private DistribuicaoRepository distribuicaoRepository;


	@Override
	@Transactional
	public void somarEstudos(Long idEstudoBase, ProdutoDistribuicaoVO distribuicaoVO) {
		
		if (!produtoEdicaoRepository.estudoPodeSerSomado(idEstudoBase, distribuicaoVO.getCodigoProduto())) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Estudo não pode ser somado.");
		}
		
		Long idEstudo = distribuicaoVO.getIdEstudo().longValue();
		Estudo estudoBase = estudoRepository.buscarPorId(idEstudoBase);
		
		Map<Long,EstudoCota> mapEstudoCota = new HashMap<Long,EstudoCota>();
		
		if (estudoBase.getEstudoCotas() != null && !estudoBase.getEstudoCotas().isEmpty()) {
			
			Iterator<EstudoCota> it =  estudoBase.getEstudoCotas().iterator(); 
			
			while (it.hasNext()) {
				
				EstudoCota estudoCota = it.next();
				Cota cota = estudoCota.getCota();
				
				if(cota != null) {
					
					mapEstudoCota.put(cota.getId(), estudoCota);
				}
			}
		}
		
		if (mapEstudoCota.isEmpty()) {
			
			return; 
		}
		
		
		
		Estudo estudo = estudoRepository.buscarPorId(idEstudo);
		
		if (estudo.getEstudoCotas() != null && !estudo.getEstudoCotas().isEmpty()) {
			
			Iterator<EstudoCota> it =  estudo.getEstudoCotas().iterator(); 
			
			while (it.hasNext()) {
				
				EstudoCota estudoCota = it.next();
				
				if (estudoCota.getCota() != null) {
					
					Long idCota = estudoCota.getCota().getId();
					
					if (mapEstudoCota.containsKey(idCota)) {
					  	
						EstudoCota estudoCotaBase = mapEstudoCota.remove(idCota);
						
						if (estudoCotaBase.getReparte() != null && estudoCota.getReparte() != null) {
							
							estudoCota.setReparte(estudoCota.getReparte().add(estudoCotaBase.getReparte()));
						}
					}
				}
			}
		
			Iterator<Entry<Long,EstudoCota>> itMap = mapEstudoCota.entrySet().iterator();
			
			while (itMap.hasNext()) {
				
				EstudoCota estudoCota = itMap.next().getValue();
				estudoCota.setEstudo(estudo);
				estudoCota.setQtdeEfetiva(estudoCota.getReparte());
				estudoCota.setQtdePrevista(estudoCota.getReparte());
				estudoCotaRepository.alterar(estudoCota);
			}
			
			if(estudo.getQtdeReparte()!=null && estudoBase.getQtdeReparte()!=null){
				estudo.setQtdeReparte(estudo.getQtdeReparte().add(estudoBase.getQtdeReparte()));				
			}
			estudoRepository.alterar(estudo);
			estudoRepository.remover(estudoBase);
		}
		
	}


	@Override
	public Boolean verificarCoincidenciaEntreCotas(Long estudoBase,Long estudoSomado) {
		
		Long count = estudoRepository.countDeCotasEntreEstudos(estudoBase,estudoSomado);
		
		return (count>0);
	}
	
	
	
}
