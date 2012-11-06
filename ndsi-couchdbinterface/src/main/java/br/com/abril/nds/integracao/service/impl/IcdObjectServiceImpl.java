package br.com.abril.nds.integracao.service.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ancientprogramming.fixedformat4j.format.impl.StringFormatter;

import br.com.abril.nds.integracao.dto.SolicitacaoDTO;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra;
import br.com.abril.nds.integracao.icd.model.DetalheFaltaSobra.DfsPK;
import br.com.abril.nds.integracao.icd.model.MotivoSituacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.MotivoSituacaoFaltaSobra.MfsPK;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra;
import br.com.abril.nds.integracao.icd.model.SolicitacaoFaltaSobra.SfsPK;
import br.com.abril.nds.integracao.model.canonic.EMS0128Input;
import br.com.abril.nds.integracao.model.canonic.EMS0128InputItem;
import br.com.abril.nds.integracao.repository.SolicitacaoFaltasSobrasRepository;
import br.com.abril.nds.integracao.service.IcdObjectService;


@Service
public class IcdObjectServiceImpl implements IcdObjectService {
	
	@Autowired
	private SolicitacaoFaltasSobrasRepository solicitacaoFaltasSobrasRepository;

	public IcdObjectServiceImpl() {
		
	}
	
	@Override
	public Set<Integer> recuperaSolicitacoesSolicitadas(Long distribuidor) {

		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesSolicitadas(distribuidor) ;
	
	}

	@Override
	public Set<Integer> recuperaSolicitacoesAcertadas(Long distribuidor) {
	
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoesAcertadas(distribuidor);

	}

	@Override
	public List<SolicitacaoDTO> recuperaSolicitacoes(Long distribuidor, EMS0128Input doc) {
		return solicitacaoFaltasSobrasRepository.recuperaSolicitacoes(distribuidor, doc.getDataSolicitacao(), DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss") );
	}

	@Override
	public void insereSolicitacao(EMS0128Input doc) {
		SolicitacaoFaltaSobra sfs = new SolicitacaoFaltaSobra();

		SfsPK sfsPK = sfs.new SfsPK();		
		sfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
		sfsPK.setDataSolicitacao(doc.getDataSolicitacao());
		sfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss") );
		sfs.setSfsPK(sfsPK);
		
		sfs.setCodigoForma(doc.getFormaSolicitacao());
		sfs.setCodigoSituacao(doc.getSituacaoSolicitacao());


				
		for (EMS0128InputItem item : doc.getItems()) {
			DetalheFaltaSobra dfs = new DetalheFaltaSobra();
			
			//pk
			DfsPK dfsPK = dfs.new DfsPK();
			dfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
			dfsPK.setDataSolicitacao(doc.getDataSolicitacao());
			dfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss") );
			dfsPK.setNumeroSequencia(item.getNumSequenciaDetalhe());
			dfs.setDfsPK(dfsPK);
						
			dfs.setCodigoTipoAcerto(item.getTipoAcerto());
			dfs.setCodigoAcerto(doc.getSituacaoSolicitacao());
			dfs.setCodigoPublicacaoAdabas(item.getNumeroEdicao());			
			dfs.setNumeroEdicao(item.getNumeroEdicao());			
			dfs.setQtdSolicitada(item.getQtd().longValue());
			dfs.setValorUnitario(item.getPrecoCapa().doubleValue());
			dfs.setPctDesconto(item.getPercentualDesconto().doubleValue());			
			dfs.setNumeroDocumentoOrigem(null);
			dfs.setNumeroDocumentoAcerto(null);
			dfs.setDataEmissaoDocumentoAcerto(null);
			dfs.setCodigoUsuarioAcerto(null);
			
			
			MotivoSituacaoFaltaSobra mfs = new MotivoSituacaoFaltaSobra();
			
			MfsPK mfsPK = mfs.new MfsPK();
			mfsPK.setCodigoDistribuidor( Long.valueOf( doc.getCodigoDistribuidor() ) );
			mfsPK.setDataSolicitacao(doc.getDataSolicitacao());
			mfsPK.setHoraSolicitacao(DateFormatUtils.format(doc.getHoraDeCriacao(), "hh:mm:ss")  );
			mfsPK.setNumeroSequencia(item.getNumSequenciaDetalhe());
			mfs.setMfsPK(mfsPK);			
			
			dfs.setMotivoSituacaoFaltaSobra(mfs);
			
			sfs.getItens().add(dfs);			
		}		
		
		solicitacaoFaltasSobrasRepository.save(sfs);
		
	}
	

}
