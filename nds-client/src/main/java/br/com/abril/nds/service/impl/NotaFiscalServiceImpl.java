package br.com.abril.nds.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ConsultaLoteNotaFiscalDTO;
import br.com.abril.nds.dto.RetornoNFEDTO;
import br.com.abril.nds.model.fiscal.nota.IdentificacaoEmitente;
import br.com.abril.nds.model.fiscal.nota.InformacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.fiscal.nota.RetornoComunicacaoEletronica;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.repository.NotaFiscalRepository;
import br.com.abril.nds.service.NotaFiscalService;


/**
 * Classe de implementação de serviços referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.nota.NotaFiscal}  
 * 
 * @author Discover Technology
 *
 */
@Service
public class NotaFiscalServiceImpl implements NotaFiscalService {
	
	@Autowired
	private NotaFiscalRepository notaFiscalDAO;
	
	@Override
	public Map<Long, Integer> obterTotalItensNotaFiscalPorCotaEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<NotaFiscal> gerarDadosNotaFicalEmLote(
			ConsultaLoteNotaFiscalDTO dadosConsultaLoteNotaFiscal) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#processarRetornoNotaFiscal(br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public List<RetornoNFEDTO> processarRetornoNotaFiscal(
			List<RetornoNFEDTO> listaDadosRetornoNFE) {

		List<RetornoNFEDTO> listaDadosRetornoNFEProcessados = new ArrayList<RetornoNFEDTO>();

		for (RetornoNFEDTO dadosRetornoNFE : listaDadosRetornoNFE) {

			NotaFiscal notaFiscal = this.notaFiscalDAO
					.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());

			if (notaFiscal != null) {

				IdentificacaoEmitente emitente = notaFiscal
						.getIdentificacaoEmitente();

				String cpfCnpjEmitente = emitente.getPessoaEmitente()
						.getDocumento();

				InformacaoEletronica informacaoEletronica = notaFiscal
						.getInformacaoEletronica();

				if (cpfCnpjEmitente.equals(dadosRetornoNFE.getCpfCnpj())) {

					if (StatusProcessamentoInterno.ENVIADA.equals(notaFiscal
							.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(dadosRetornoNFE
								.getStatus())) {

							listaDadosRetornoNFEProcessados
									.add(dadosRetornoNFE);

						} else if (Status.USO_DENEGADO.equals(dadosRetornoNFE
								.getStatus())) {

							listaDadosRetornoNFEProcessados
									.add(dadosRetornoNFE);
						}

					} else if (StatusProcessamentoInterno.RETORNADA
							.equals(notaFiscal.getStatusProcessamentoInterno())) {

						if (Status.AUTORIZADO.equals(informacaoEletronica
								.getRetornoComunicacaoEletronica().getStatus())
								&& Status.CANCELAMENTO_HOMOLOGADO
										.equals(dadosRetornoNFE.getStatus())) {

							listaDadosRetornoNFEProcessados
									.add(dadosRetornoNFE);
						}
					}
				}
			}
		}
		
		return listaDadosRetornoNFEProcessados;
	}

	@Override
	public void cancelarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void denegarNotaFiscal(Long id) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.NotaFiscalService#autorizarNotaFiscal(br.com.abril.nds.dto.RetornoNFEDTO)
	 */
	@Override
	@Transactional
	public void autorizarNotaFiscal(RetornoNFEDTO dadosRetornoNFE) {
		
		NotaFiscal notaFiscal = this.notaFiscalDAO.buscarPorId(dadosRetornoNFE.getIdNotaFiscal());
		
		InformacaoEletronica informacaoEletronica = notaFiscal.getInformacaoEletronica();
		
		informacaoEletronica.setChaveAcesso(dadosRetornoNFE.getChaveAcesso());

		RetornoComunicacaoEletronica retornoComunicacaoEletronica = new RetornoComunicacaoEletronica();
		retornoComunicacaoEletronica.setDataRecebimento(dadosRetornoNFE.getDataRecebimento());
		retornoComunicacaoEletronica.setMotivo(dadosRetornoNFE.getMotivo());
		retornoComunicacaoEletronica.setProtocolo(dadosRetornoNFE.getProtocolo());
		retornoComunicacaoEletronica.setStatus(dadosRetornoNFE.getStatus());

		informacaoEletronica.setRetornoComunicacaoEletronica(retornoComunicacaoEletronica);

		notaFiscal.setInformacaoEletronica(informacaoEletronica);
		notaFiscal.setStatusProcessamentoInterno(StatusProcessamentoInterno.RETORNADA);

		this.notaFiscalDAO.merge(notaFiscal);
		
	}
}
