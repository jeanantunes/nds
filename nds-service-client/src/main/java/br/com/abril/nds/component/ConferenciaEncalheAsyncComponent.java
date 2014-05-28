package br.com.abril.nds.component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;

public interface ConferenciaEncalheAsyncComponent {

	public void finalizarConferenciaEncalheAsync(ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			boolean indConferenciaContingencia,
			BigDecimal reparte);
	
	public void salvarConferenciaEncalhe(
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final Set<Long> listaIdConferenciaEncalheParaExclusao,
			final Usuario usuario, 
			final boolean indConferenciaContingencia);
	
}
