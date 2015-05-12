package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RepartePDVDTO;
import br.com.abril.nds.model.cadastro.pdv.RepartePDV;

public interface RepartePDVRepository  extends Repository<RepartePDV, Long> {
	
	public RepartePDV buscarPorId(Long id);
	
	public RepartePDV obterRepartePorPdv(Long idFixacao, Long idProduto, Long idPdv);
	
	public List<RepartePDVDTO> obterRepartePdvMixPorCota(Long idCota);
	
	public RepartePDV obterRepartePdvMix(Long idMix, Long idProduto, Long idPdv);

	public List<RepartePDV> buscarPorIdFixacao(Long id);
	
	public List<RepartePDV> buscarPorIdMix(Long id);
	
	List<RepartePDV> buscarPorCota(Long idCota);
	
	List<RepartePDV> buscarPorProduto(Long produtoId);

    public boolean verificarRepartePdv(Integer numeroCota, String codigoProduto);
	
}