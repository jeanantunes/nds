package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;
import br.com.abril.nds.model.movimentacao.Slip;


public interface SlipRepository extends Repository<Slip, Long>{
    
    Slip obterPorNumeroCotaData(Integer numeroCota, Date dataConferencia);

    List<DebitoCreditoCota> obterComposicaoSlip(Long idSlip, boolean composicao);

	List<Slip> obterSlipsPorCotasData(List<Integer> listaCotas, Date data);
}
