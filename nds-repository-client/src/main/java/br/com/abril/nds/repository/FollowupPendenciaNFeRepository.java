package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupPendenciaNFeDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupPendenciaNFeDTO;
import br.com.abril.nds.model.fiscal.NotaFiscalEntrada;

public interface FollowupPendenciaNFeRepository extends Repository<NotaFiscalEntrada,Long> {
    List<ConsultaFollowupPendenciaNFeDTO> consultaPendenciaNFEEncalhe(FiltroFollowupPendenciaNFeDTO filtro);

	Long qtdeRegistrosPendencias(FiltroFollowupPendenciaNFeDTO filtro);
}
