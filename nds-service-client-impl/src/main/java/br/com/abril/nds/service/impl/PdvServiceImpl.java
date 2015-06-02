package br.com.abril.nds.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.assembler.HistoricoTitularidadeCotaDTOAssembler;
import br.com.abril.nds.dto.CaracteristicaDTO;
import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.dto.EnderecoDTO;
import br.com.abril.nds.dto.GeradorFluxoDTO;
import br.com.abril.nds.dto.MaterialPromocionalDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.PeriodoFuncionamentoDTO;
import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.dto.TelefoneDTO;
import br.com.abril.nds.dto.TipoEstabelecimentoAssociacaoPDVDTO;
import br.com.abril.nds.dto.TipoLicencaMunicipalDTO;
import br.com.abril.nds.dto.TipoPontoPDVDTO;
import br.com.abril.nds.dto.filtro.FiltroPdvDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.LicencaMunicipal;
import br.com.abril.nds.model.cadastro.MaterialPromocional;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TipoLicencaMunicipal;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.EnderecoPDV;
import br.com.abril.nds.model.cadastro.pdv.GeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.PDV;
import br.com.abril.nds.model.cadastro.pdv.PeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.model.cadastro.pdv.SegmentacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TelefonePDV;
import br.com.abril.nds.model.cadastro.pdv.TipoEstabelecimentoAssociacaoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPeriodoFuncionamentoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaCodigoDescricao;
import br.com.abril.nds.model.titularidade.HistoricoTitularidadeCotaPDV;
import br.com.abril.nds.repository.AreaInfluenciaPDVRepository;
import br.com.abril.nds.repository.CotaRepository;
import br.com.abril.nds.repository.EnderecoPDVRepository;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.repository.GeradorFluxoPDVRepository;
import br.com.abril.nds.repository.MaterialPromocionalRepository;
import br.com.abril.nds.repository.ParametroSistemaRepository;
import br.com.abril.nds.repository.PdvRepository;
import br.com.abril.nds.repository.PeriodoFuncionamentoPDVRepository;
import br.com.abril.nds.repository.TelefonePdvRepository;
import br.com.abril.nds.repository.TipoGeradorFluxoPDVRepsitory;
import br.com.abril.nds.repository.TipoLicencaMunicipalRepository;
import br.com.abril.nds.repository.TipoPontoPDVRepository;
import br.com.abril.nds.repository.TiposEstabelecimentoRepository;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.TelefoneService;
import br.com.abril.nds.util.DateUtil;

@Service
public class PdvServiceImpl implements PdvService {

    @Autowired
    private PdvRepository pdvRepository;

    @Autowired
    private CotaRepository cotaRepository;

    @Autowired
    private GeradorFluxoPDVRepository fluxoPDVRepository;

    @Autowired
    private TipoGeradorFluxoPDVRepsitory tipoGeradorFluxoPDVRepsitory;

    @Autowired
    private MaterialPromocionalRepository materialPromocionalRepository;

    @Autowired
    private PeriodoFuncionamentoPDVRepository periodoFuncionamentoPDVRepository;

    @Autowired
    private AreaInfluenciaPDVRepository areaInfluenciaPDVRepository;

    @Autowired
    private TipoPontoPDVRepository tipoPontoPDVRepository;

    @Autowired
    private TipoLicencaMunicipalRepository tipoLicencaMunicipalRepository;

    @Autowired
    private TiposEstabelecimentoRepository tiposEstabelecimentoRepository;

    @Autowired
    private EnderecoPDVRepository enderecoPDVRepository;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;

    @Autowired
    private TelefonePdvRepository telefonePdvRepository;

    @Autowired
    private TelefoneService telefoneService;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private EnderecoPDVRepository enderecoPdvRepository;
    
    @Autowired
    private GeradorFluxoPDVRepository geradorFluxoPDVRepository;

    @Transactional(readOnly = true)
    @Override
    public List<TipoPontoPDV> obterTiposPontoPDV() {

        return tipoPontoPDVRepository.buscarTodos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TipoPontoPDV> obterTiposPontoPDVPrincipal() {

        return tipoPontoPDVRepository.buscarTodosPdvPrincipal();
    }

    @Transactional(readOnly = true)
    @Override
    public TipoPontoPDV obterTipoPontoPDVPrincipal(Long codigoPontoPDV) {

        return tipoPontoPDVRepository
                .buscarTipoPontoPdvPrincipal(codigoPontoPDV);
    }

    @Transactional(readOnly = true)
    @Override
    public Endereco buscarMunicipioPdvPrincipal(Integer codigoCidadeIBGE) {

        return enderecoPDVRepository
                .buscarMunicipioPdvPrincipal(codigoCidadeIBGE);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<AreaInfluenciaPDV> obterTipoAreaInfluencia() {

        return areaInfluenciaPDVRepository.obterTodasAreaInfluenciaPDV();
    }

    @Transactional(readOnly = true)
    @Override
	public List<TipoGeradorFluxoPDV> obterTipoGeradorDeFluxo() {
		// TODO Auto-generated method stub
    	return tipoGeradorFluxoPDVRepsitory.obterTodosTiposGeradorFluxo();
	}
    
    @Transactional(readOnly = true)
    @Override
    public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxo(Long... codigos) {

        if (codigos.length == 0) {

            return tipoGeradorFluxoPDVRepsitory.buscarTodos();
        }

        return tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxo(codigos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TipoGeradorFluxoPDV> obterTiposGeradorFluxoNotIn(
            Long... codigos) {

        if (codigos.length > 0) {

            return tipoGeradorFluxoPDVRepsitory
                    .obterTiposGeradorFluxoNotIn(codigos);
        }

        return new ArrayList<TipoGeradorFluxoPDV>();
    }

    @Transactional(readOnly = true)
    @Override
    public List<MaterialPromocional> obterMateriaisPromocionalPDV(
            Long... codigos) {

        if (codigos.length == 0) {

            return materialPromocionalRepository.buscarTodos();
        }

        return materialPromocionalRepository.obterMateriaisPromocional(codigos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MaterialPromocional> obterMateriaisPromocionalPDVNotIn(
            Long... codigos) {

        if (codigos.length > 0) {
            return materialPromocionalRepository
                    .obterMateriaisPromocionalNotIn(codigos);
        }

        return new ArrayList<MaterialPromocional>();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TipoEstabelecimentoAssociacaoPDV> obterTipoEstabelecimentoAssociacaoPDV() {

        return tiposEstabelecimentoRepository.buscarTodos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TipoLicencaMunicipal> obterTipoLicencaMunicipal() {

        return tipoLicencaMunicipalRepository.buscarTodos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PdvDTO> obterPDVsPorCota(FiltroPdvDTO filtro) {

        return pdvRepository.obterPDVsPorCota(filtro);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EnderecoAssociacaoDTO> buscarEnderecosPDV(Long idPDV,
            Long idCota) {

        if (idCota == null)
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "IdCota é obrigatório");

        Cota cota = cotaRepository.buscarPorId(idCota);

        if (cota == null)
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "IdCota é obrigatório");

        Long idPessoa = cota.getPessoa().getId();

        Set<Long> endRemover = new HashSet<Long>();

        List<EnderecoAssociacaoDTO> listRetorno = new ArrayList<EnderecoAssociacaoDTO>();

        List<EnderecoAssociacaoDTO> listaEnderecolAssoc = null;
        
        if (idPDV != null) {

            listaEnderecolAssoc = this.enderecoPDVRepository
                    .buscaEnderecosPDV(idPDV, null);

            if (listaEnderecolAssoc != null && !listaEnderecolAssoc.isEmpty()) {

                listRetorno.addAll(listaEnderecolAssoc);

                for (EnderecoAssociacaoDTO dto : listaEnderecolAssoc) {

                    endRemover.add(dto.getEndereco().getId());
                }
            }
        }

        if(listaEnderecolAssoc == null || listaEnderecolAssoc.isEmpty()){
        
	        List<EnderecoAssociacaoDTO> lista = this.enderecoService
	                .buscarEnderecosPorPessoaCotaPDVs(idPessoa, endRemover);
	
	        if (lista != null && !lista.isEmpty()) {
	
	            listRetorno.addAll(lista);
	        }
        }
        
        return listRetorno;
    }

    @Transactional(readOnly = true)
    @Override
    public PdvDTO obterPDV(Long idCota, Long idPdv) {

        PDV pdv = pdvRepository.obterPDV(idCota, idPdv);

        PdvDTO pdvDTO = new PdvDTO();

        if (pdv != null) {

            atribuirValorDadosBasico(pdv, pdvDTO);
            atribuirValorCaracteristica(pdv, pdvDTO);
            atribuirValorGeradorFluxo(pdv, pdvDTO);
            atribuirValorMaterialPromocional(pdv, pdvDTO);
        }

        return pdvDTO;
    }
    
    /**
     * Obtem PDV por id
     * @param idPdv
     * @return PDV
     */
    @Transactional(readOnly = true)
    @Override
    public PDV obterPDVporId(Long idPdv) {

        PDV pdv = pdvRepository.buscarPorId(idPdv);

        return pdv;
    }

    @Transactional
    @Override
    public void excluirPDV(Long idPdv) {

        PDV pdv = this.pdvRepository.buscarPorId(idPdv);

        this.pdvRepository.removeCotaPDVbyPDV(idPdv);
        
        if (pdv.getCaracteristicas() != null
                && pdv.getCaracteristicas().isPontoPrincipal()) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "PDV não pode ser excluído! Pelo menos um PDV deve ser um Ponto Principal.");
        }

        if (pdv != null) {
        	
       		this.geradorFluxoPDVRepository.removerGeradorFluxoPDV(pdv.getId());

       		this.pdvRepository.remover(pdv);
        }
    }

    @Transactional
    @Override
    public void salvar(PdvDTO pdvDTO) {

        if (pdvDTO == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Parâmetro PDV inválido");
        }

        if (pdvDTO.getNomePDV() == null || pdvDTO.getNomePDV().isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Nome do PDV deve ser informado!");
        }

        if (pdvDTO.getIdCota() == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Parâmetro Cota PDV inválido");
        }

        Cota cota = obterCotaPDV(pdvDTO);

        if (cota == null) {
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Não foi encontrado Cota para inclusão do PDV.");
        }

        salvarPDV(pdvDTO, cota);
    }

    private void salvarPDV(PdvDTO pdvDTO, Cota cota) {

        PDV pdv = null;

        if (pdvDTO.getId() != null) {
            pdv = pdvRepository.buscarPorId(pdvDTO.getId());
        }

        if (pdv == null) {
            pdv = new PDV();
            pdv.setDataInclusao(new Date());
        }

        if (pdvDTO.getCaracteristicaDTO() != null
                && pdvDTO.getCaracteristicaDTO().isPontoPrincipal()) {

            PDV principalAtual = this.pdvRepository.obterPDVPrincipal(cota.getNumeroCota());
            
            if(principalAtual != null) {
            	
            	List<RotaPDV> rotasPrincipal = new ArrayList<RotaPDV>(principalAtual.getRotas());
            	
            	for (RotaPDV rotaPDV : rotasPrincipal) {
            		
            		if (rotaPDV.getPdv().getCaracteristicas().isPontoPrincipal()) {
            			
            			rotaPDV.setPdv(pdv);
            		}
            	}
            	
            	pdv.setRotas(rotasPrincipal);
            	
            	principalAtual.getCaracteristicas().setPontoPrincipal(false);
            	
            	principalAtual.setRotas(null);
            	
            	this.pdvRepository.merge(principalAtual);
            }
            
            
        } else if (pdvDTO.getCaracteristicaDTO() != null
                && !pdvDTO.getCaracteristicaDTO().isPontoPrincipal()) {
            if (!pdvRepository.existePDVPrincipal(cota.getId(), pdv.getId())) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "É obrigatório ter um PDV principal.");
            }
        }

        if (pdvRepository.obterQntPDV(cota.getId(), pdv.getId()) == 0
                && pdvDTO.getCaracteristicaDTO() != null
                && !pdvDTO.getCaracteristicaDTO().isPontoPrincipal()) {

            pdvDTO.getCaracteristicaDTO().setPontoPrincipal(Boolean.TRUE);
        }

        if (pdvDTO.getCaracteristicaDTO().isPontoPrincipal()) {

            if (pdv.getCaracteristicas() != null
                    && !pdv.getCaracteristicas().isPontoPrincipal()) {

                if (pdvRepository.existePDVPrincipal(cota.getId(), pdv.getId())) {
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "PDV não pode der incluído! Já existe PDV incluído como principal.");
                }
            }
        }

        tratarDadosParaInclusao(pdvDTO, pdv, cota);

        pdv = pdvRepository.merge(pdv);

        salvarPeriodoFuncionamentoPDV(pdvDTO, pdv);

        salvarGeradorFluxo(pdvDTO, pdv);

        if (pdvDTO.getImagem() != null)
            atualizaImagemPDV(pdvDTO.getImagem(), pdv.getId(),
                    pdvDTO.getPathAplicacao());

        salvarEndereco(pdvDTO, pdv);
        processarTelefones(pdvDTO, pdv);
    }

    private void tratarDadosParaInclusao(PdvDTO pdvDTO, PDV pdv, Cota cota) {

        pdv.setCota(cota);
        pdv.setNome(pdvDTO.getNomePDV());
        pdv.setContato(pdvDTO.getContato());
        pdv.setPontoReferencia(pdvDTO.getPontoReferencia());
        pdv.setEmail(pdvDTO.getEmail());
        pdv.setPorcentagemFaturamento(pdvDTO.getPorcentagemFaturamento());
        pdv.setPossuiSistemaIPV(pdvDTO.isSistemaIPV());
        pdv.setQtdeFuncionarios(pdvDTO.getQtdeFuncionarios());
        pdv.setSite(pdvDTO.getSite());
        pdv.setStatus(pdvDTO.getStatusPDV());
        pdv.setTamanhoPDV(pdvDTO.getTamanhoPDV());
        pdv.setDentroOutroEstabelecimento(pdvDTO.isDentroOutroEstabelecimento());
        pdv.setLicencaMunicipal(obterLicencaMunicipalPDV(pdvDTO, pdv));
        pdv.setCaracteristicas(obterCaracteristicaPDV(pdvDTO, pdv));
        pdv.setMateriais(obterMateriaisPDV(pdvDTO));
        pdv.setSegmentacao(obterSegmentacaoPDV(pdvDTO, pdv));
        pdv.setExpositor(pdvDTO.isExpositor());
        pdv.setTipoExpositor(pdvDTO.getTipoExpositor());

        if (pdvDTO.isDentroOutroEstabelecimento()
                && pdvDTO.getTipoEstabelecimentoAssociacaoPDV() != null) {

            Long codigo = pdvDTO.getTipoEstabelecimentoAssociacaoPDV()
                    .getCodigo();

            TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoAssociacaoPDV = tiposEstabelecimentoRepository
                    .obterTipoEstabelecimentoAssociacaoPDV(codigo);

            if (tipoEstabelecimentoAssociacaoPDV != null) {
                pdv.setTipoEstabelecimentoPDV(tipoEstabelecimentoAssociacaoPDV);
            }
        }
    }

    @Transactional
    public void atualizaImagemPDV(FileInputStream foto, Long idPdv,
            String pathAplicacao) {

        ParametroSistema pathPDV = this.parametroSistemaRepository
                .buscarParametroPorTipoParametro(TipoParametroSistema.PATH_IMAGENS_PDV);

        File fileDir = new File((pathAplicacao + pathPDV.getValor()).replace(
                "\\", "/"));

        fileDir.mkdirs();

        String nomeArquivo = "pdv_" + idPdv + ".jpeg";

        File fileArquivo = new File(fileDir, nomeArquivo);

        if (fileArquivo.exists())
            fileArquivo.delete();

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(fileArquivo);

            IOUtils.copyLarge(foto, fos);

        } catch (Exception e) {

            throw new ValidacaoException(TipoMensagem.ERROR,
                    "Falha ao gravar o arquivo em disco!");

        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                throw new ValidacaoException(TipoMensagem.ERROR,
                        "Falha ao gravar o arquivo em disco!");
            }
        }
    }

    private void salvarEndereco(PdvDTO pdvDTO, PDV pdv) {

        processarEnderecos(pdvDTO, pdv);
    }

    @Transactional
    private void processarTelefones(PdvDTO pdvDTO, PDV pdv) {

        if (pdvDTO.getTelefonesAdicionar() != null
                && !pdvDTO.getTelefonesAdicionar().isEmpty()) {

            this.validarInclusaoEdicaoTelefonePrincipal(pdvDTO
                    .getTelefonesAdicionar());

        }

        if (pdvDTO.getTelefonesRemover() != null
                && !pdvDTO.getTelefonesRemover().isEmpty()) {

            this.validarExcluirTelefonePrincipal(pdv,
                    pdvDTO.getTelefonesRemover(),
                    pdvDTO.getTelefonesAdicionar());

        }

        Pessoa pessoa = pdv.getCota().getPessoa();

        List<Telefone> listaTelefones = new ArrayList<Telefone>();

        if (pdvDTO.getTelefonesAdicionar() != null) {
            for (TelefoneAssociacaoDTO telefoneAssociacaoDTO : pdvDTO
                    .getTelefonesAdicionar()) {
                TelefoneDTO dto = telefoneAssociacaoDTO.getTelefone();
                Telefone telefone = new Telefone(dto.getId(), dto.getNumero(),
                        dto.getRamal(), dto.getDdd(), pessoa);
                listaTelefones.add(telefone);
            }
        }

        if (!listaTelefones.isEmpty()) {
            pessoa.setTelefones(listaTelefones);
        }

        this.salvarTelefonesPdv(pdv, pdvDTO.getTelefonesAdicionar());

        this.removerTelefonesPdv(pdvDTO.getTelefonesRemover());
    }

    private void salvarTelefonesPdv(PDV pdv,
            List<TelefoneAssociacaoDTO> listaTelefones) {

		Pessoa pessoa = pdv.getCota().getPessoa();
		
        if (listaTelefones != null) {

            for (TelefoneAssociacaoDTO dto : listaTelefones) {

                TelefoneDTO telefoneDTO = dto.getTelefone();
                
                this.telefoneService.validarTelefone(telefoneDTO, dto.getTipoTelefone());
                
                TelefonePDV telefonePdv = this.telefonePdvRepository
                        .obterTelefonePorTelefonePdv(telefoneDTO.getId(),
                                pdv.getId());

                if (telefonePdv == null) {
                    telefonePdv = new TelefonePDV();

                    telefonePdv.setPdv(pdv);
                    telefonePdv.setPrincipal(dto.isPrincipal());
                    Telefone telefone = new Telefone(telefoneDTO.getId(),
                            telefoneDTO.getNumero(), telefoneDTO.getRamal(),
                            telefoneDTO.getDdd(), pessoa);
                    telefonePdv.setTelefone(telefone);
                    telefonePdv.setTipoTelefone(dto.getTipoTelefone());

                    this.telefonePdvRepository.adicionar(telefonePdv);
                } else {

                    Telefone telefone = telefonePdv.getTelefone();
                    telefone.setDdd(telefoneDTO.getDdd());
                    telefone.setNumero(telefoneDTO.getNumero());
                    telefone.setRamal(telefoneDTO.getRamal());
                    telefonePdv.setPrincipal(dto.isPrincipal());
                    telefonePdv.setTipoTelefone(dto.getTipoTelefone());

                    this.telefonePdvRepository.alterar(telefonePdv);
                }
            }
        }
    }

    private void removerTelefonesPdv(Collection<Long> listaTelefones) {

        if (listaTelefones != null && !listaTelefones.isEmpty()) {
            this.telefonePdvRepository.removerTelefonesPdv(listaTelefones);

            this.telefoneService.removerTelefones(listaTelefones);
        }
    }

    private void salvarGeradorFluxo(PdvDTO pdvDTO, PDV pdv) {

        GeradorFluxoPDV geradorFluxoPDV = obterGeradorFluxoPDV(pdvDTO, pdv);

        if (geradorFluxoPDV != null) {
            fluxoPDVRepository.merge(geradorFluxoPDV);
        }
    }

    private void salvarPeriodoFuncionamentoPDV(PdvDTO pdvDTO, PDV pdv) {

        if (pdv.getPeriodos() != null && !pdv.getPeriodos().isEmpty()) {

            for (PeriodoFuncionamentoPDV periodo : pdv.getPeriodos()) {
                periodoFuncionamentoPDVRepository.remover(periodo);
            }
        }

        if (pdvDTO.getPeriodosFuncionamentoDTO() != null) {

            PeriodoFuncionamentoPDV periodo = null;

            for (PeriodoFuncionamentoDTO periodoDTO : pdvDTO
                    .getPeriodosFuncionamentoDTO()) {

                periodo = new PeriodoFuncionamentoPDV();
                periodo.setHorarioFim(DateUtil.parseData(periodoDTO.getFim(),
                        "HH:mm"));
                periodo.setHorarioInicio(DateUtil.parseData(
                        periodoDTO.getInicio(), "HH:mm"));
                periodo.setTipoPeriodoFuncionamentoPDV(periodoDTO
                        .getTipoPeriodoFuncionamentoPDV());
                periodo.setPdv(pdv);

                periodoFuncionamentoPDVRepository.adicionar(periodo);
            }
        }
    }

    private SegmentacaoPDV obterSegmentacaoPDV(PdvDTO pdvDTO, PDV pdv) {

        SegmentacaoPDV segmaSegmentacaoPDV = pdv.getSegmentacao();

        if (segmaSegmentacaoPDV == null) {
            segmaSegmentacaoPDV = new SegmentacaoPDV();
        }

        CaracteristicaDTO caracteristicaDTO = pdvDTO.getCaracteristicaDTO();
        AreaInfluenciaPDV areaInfluenciaPDV = null;
        TipoPontoPDV tipoPontoPDV = null;

        if (pdvDTO.getCaracteristicaDTO() != null) {

            if (caracteristicaDTO.getAreaInfluencia() != null) {
                areaInfluenciaPDV = areaInfluenciaPDVRepository
                        .buscarPorId(caracteristicaDTO.getAreaInfluencia());
            }

            if (caracteristicaDTO.getTipoPonto() != null) {
                tipoPontoPDV = tipoPontoPDVRepository
                        .buscarPorId(caracteristicaDTO.getTipoPonto());
            }
        }

        segmaSegmentacaoPDV.setAreaInfluenciaPDV(areaInfluenciaPDV);
        segmaSegmentacaoPDV.setTipoCaracteristica(caracteristicaDTO
                .getTipoCaracteristicaSegmentacaoPDV());
        segmaSegmentacaoPDV.setTipoPontoPDV(tipoPontoPDV);

        return segmaSegmentacaoPDV;
    }

    private Set<MaterialPromocional> obterMateriaisPDV(PdvDTO pdvDTO) {

        Set<MaterialPromocional> materialPromocional = new HashSet<MaterialPromocional>();

        if (pdvDTO.getMaps() != null && !pdvDTO.getMaps().isEmpty()) {

            materialPromocional.addAll(materialPromocionalRepository
                    .obterMateriaisPromocional((pdvDTO.getMaps()
                            .toArray(new Long[] {}))));

        }

        return materialPromocional;
    }

    private LicencaMunicipal obterLicencaMunicipalPDV(PdvDTO pdvDTO, PDV pdv) {

        LicencaMunicipal licencaMunicipal = pdv.getLicencaMunicipal();

        if (licencaMunicipal == null) {
            licencaMunicipal = new LicencaMunicipal();
        }

        TipoLicencaMunicipal tipoLicencaMunicipal = null;

        if (pdvDTO.getTipoLicencaMunicipal() != null) {
            tipoLicencaMunicipal = tipoLicencaMunicipalRepository
                    .obterTipoLicencaMunicipal(pdvDTO.getTipoLicencaMunicipal()
                            .getId());
        }

        licencaMunicipal.setNomeLicenca(pdvDTO.getNomeLicenca());
        licencaMunicipal.setNumeroLicenca(pdvDTO.getNumeroLicenca());
        licencaMunicipal.setTipoLicencaMunicipal(tipoLicencaMunicipal);

        return licencaMunicipal;
    }

    private GeradorFluxoPDV obterGeradorFluxoPDV(PdvDTO pdvDTO, PDV pdv) {

        TipoGeradorFluxoPDV fluxoPrincipal = null;

        if (pdvDTO.getGeradorFluxoPrincipal() != null) {
            fluxoPrincipal = tipoGeradorFluxoPDVRepsitory.buscarPorId(pdvDTO
                    .getGeradorFluxoPrincipal());
        }

        Set<TipoGeradorFluxoPDV> fluxoSecundario = null;

        if (pdvDTO.getGeradorFluxoSecundario() != null && !pdvDTO.getGeradorFluxoSecundario().isEmpty()) {

            fluxoSecundario = new HashSet<TipoGeradorFluxoPDV>();
            fluxoSecundario.addAll(tipoGeradorFluxoPDVRepsitory
                    .obterTiposGeradorFluxo(pdvDTO.getGeradorFluxoSecundario()
                            .toArray(new Long[] {})));
        }

        if (fluxoPrincipal == null && fluxoSecundario == null) {
            return null;
        }

        GeradorFluxoPDV fluxoPDV = pdv.getGeradorFluxoPDV();

        if (fluxoPDV == null) {
            fluxoPDV = new GeradorFluxoPDV();
        }

        fluxoPDV.setPrincipal(fluxoPrincipal);
        fluxoPDV.setSecundarios(fluxoSecundario);
        fluxoPDV.setPdv(pdv);

        return fluxoPDV;
    }

    private Cota obterCotaPDV(PdvDTO pdvDTO) {

        return cotaRepository.buscarPorId(pdvDTO.getIdCota());
    }

    private CaracteristicasPDV obterCaracteristicaPDV(PdvDTO pdvDTO, PDV pdv) {

        CaracteristicaDTO carct = pdvDTO.getCaracteristicaDTO();

        CaracteristicasPDV caracteristicasPDV = pdv.getCaracteristicas();

        if (caracteristicasPDV == null) {
            caracteristicasPDV = new CaracteristicasPDV();
        }

        caracteristicasPDV.setBalcaoCentral(carct.isBalcaoCentral());
        caracteristicasPDV.setPontoPrincipal(carct.isPontoPrincipal());
        caracteristicasPDV.setPossuiComputador(carct.isTemComputador());
        caracteristicasPDV.setPossuiLuminoso(carct.isLuminoso());
        caracteristicasPDV.setPossuiCartaoCredito(carct.isPossuiCartao());

        if (carct.isLuminoso()) {
            caracteristicasPDV.setTextoLuminoso(carct.getTextoLuminoso());
        } else {
            caracteristicasPDV.setTextoLuminoso(null);
        }

        return caracteristicasPDV;
    }

    private void atribuirValorDadosBasico(PDV pdv, PdvDTO pdvDTO) {

        pdvDTO.setId(pdv.getId());
        pdvDTO.setStatusPDV(pdv.getStatus());
        pdvDTO.setNomePDV(pdv.getNome());
        pdvDTO.setContato(pdv.getContato());
        pdvDTO.setDataInicio(pdv.getDataInclusao());
        pdvDTO.setSite(pdv.getSite());
        pdvDTO.setEmail(pdv.getEmail());
        pdvDTO.setPontoReferencia(pdv.getPontoReferencia());
        pdvDTO.setDentroOutroEstabelecimento(pdv.isDentroOutroEstabelecimento());
        pdvDTO.setArrendatario(pdv.isArrendatario());
        
        TipoEstabelecimentoAssociacaoPDV tipoEstabelecimentoPDV = pdv.getTipoEstabelecimentoPDV();
        
        if(pdv.getSegmentacao() != null && pdv.getSegmentacao().getTipoPontoPDV() != null) {
            
        	TipoPontoPDVDTO tppDTO = new TipoPontoPDVDTO();
            
        	tppDTO.setCodigo(pdv.getSegmentacao().getTipoPontoPDV().getCodigo());
            tppDTO.setDescricao(pdv.getSegmentacao().getTipoPontoPDV().getDescricao());
            tppDTO.setId(pdv.getSegmentacao().getTipoPontoPDV().getId());
            
            pdvDTO.setTipoPontoPDV(tppDTO);
        }
        
        if (tipoEstabelecimentoPDV != null) {
            pdvDTO.setTipoEstabelecimentoAssociacaoPDV(new TipoEstabelecimentoAssociacaoPDVDTO(
                    tipoEstabelecimentoPDV.getId(), tipoEstabelecimentoPDV
                            .getCodigo(), tipoEstabelecimentoPDV.getDescricao()));
        }
        
        pdvDTO.setTamanhoPDV(pdv.getTamanhoPDV());
        pdvDTO.setSistemaIPV(pdv.isPossuiSistemaIPV());
        pdvDTO.setQtdeFuncionarios(pdv.getQtdeFuncionarios());
        pdvDTO.setPorcentagemFaturamento(pdv.getPorcentagemFaturamento());

        LicencaMunicipal licencaMunicipal = pdv.getLicencaMunicipal();

        if (licencaMunicipal != null) {
            pdvDTO.setNumeroLicenca(licencaMunicipal.getNumeroLicenca());
            pdvDTO.setNomeLicenca(licencaMunicipal.getNomeLicenca());
            TipoLicencaMunicipal tipoLicencaMunicipal = licencaMunicipal
                    .getTipoLicencaMunicipal();
            TipoLicencaMunicipalDTO tipoLicencaDTO = new TipoLicencaMunicipalDTO(
                    tipoLicencaMunicipal.getId(),
                    tipoLicencaMunicipal.getCodigo(),
                    tipoLicencaMunicipal.getDescricao());
            pdvDTO.setTipoLicencaMunicipal(tipoLicencaDTO);
        }

        Set<PeriodoFuncionamentoPDV> periodos = pdv.getPeriodos();

        List<PeriodoFuncionamentoDTO> listaPeriodos = new ArrayList<PeriodoFuncionamentoDTO>();

        if (periodos != null && !periodos.isEmpty()) {

            for (PeriodoFuncionamentoPDV periodo : periodos) {

                listaPeriodos
                        .add(new PeriodoFuncionamentoDTO(periodo
                                .getTipoPeriodoFuncionamentoPDV(), DateUtil
                                .formatarData(periodo.getHorarioInicio(),
                                        "HH:mm"), DateUtil.formatarData(
                                periodo.getHorarioFim(), "HH:mm")));
            }
        }

        pdvDTO.setPeriodosFuncionamentoDTO(listaPeriodos);
    }

    private void atribuirValorCaracteristica(PDV pdv, PdvDTO pdvDTO) {

        CaracteristicaDTO caracteristicaDTO = new CaracteristicaDTO();

        if (pdv.getSegmentacao() != null) {

            caracteristicaDTO.setAreaInfluencia((pdv.getSegmentacao()
                    .getAreaInfluenciaPDV() != null) ? pdv.getSegmentacao()
                    .getAreaInfluenciaPDV().getCodigo() : null);
            caracteristicaDTO.setTipoCaracteristicaSegmentacaoPDV(pdv
                    .getSegmentacao().getTipoCaracteristica());
            caracteristicaDTO.setTipoPonto((pdv.getSegmentacao()
                    .getTipoPontoPDV() != null) ? pdv.getSegmentacao()
                    .getTipoPontoPDV().getCodigo() : null);
        }

        if (pdv.getCaracteristicas() != null) {

            caracteristicaDTO.setBalcaoCentral(pdv.getCaracteristicas()
                    .isBalcaoCentral());
            caracteristicaDTO.setLuminoso(pdv.getCaracteristicas()
                    .isPossuiLuminoso());
            caracteristicaDTO.setPontoPrincipal(pdv.getCaracteristicas()
                    .isPontoPrincipal());
            caracteristicaDTO.setTemComputador(pdv.getCaracteristicas()
                    .isPossuiComputador());
            caracteristicaDTO.setTextoLuminoso(pdv.getCaracteristicas()
                    .getTextoLuminoso());
            caracteristicaDTO.setPossuiCartao(pdv.getCaracteristicas()
                    .isPossuiCartaoCredito());
        }

        pdvDTO.setCaracteristicaDTO(caracteristicaDTO);
    }

    private void atribuirValorGeradorFluxo(PDV pdv, PdvDTO pdvDTO) {

        pdvDTO.setGeradorFluxoSecundario(new ArrayList<Long>());

        if (pdv.getGeradorFluxoPDV() != null) {

            if (pdv.getGeradorFluxoPDV().getPrincipal() != null) {
                pdvDTO.setGeradorFluxoPrincipal(pdv.getGeradorFluxoPDV()
                        .getPrincipal().getCodigo());
            }

            if (pdv.getGeradorFluxoPDV().getSecundarios() != null
                    && !pdv.getGeradorFluxoPDV().getSecundarios().isEmpty()) {

                for (TipoGeradorFluxoPDV flx : pdv.getGeradorFluxoPDV()
                        .getSecundarios()) {
                    pdvDTO.getGeradorFluxoSecundario().add(flx.getCodigo());
                }
            }
        }
    }

    private void atribuirValorMaterialPromocional(PDV pdv, PdvDTO pdvDTO) {

        pdvDTO.setMaps(new ArrayList<Long>());

        if (pdv.getMateriais() != null && !pdv.getMateriais().isEmpty()) {

            for (MaterialPromocional mat : pdv.getMateriais()) {
                pdvDTO.getMaps().add(mat.getCodigo());
            }
        }

        pdvDTO.setExpositor((pdv.isExpositor() == null) ? false : pdv
                .isExpositor());
        pdvDTO.setTipoExpositor(pdv.getTipoExpositor());
    }

    /**
     * Obtém lista com os possíveis peridos a serem selecionados
     * 
     * @param selecionados
     *            - Periodos já selecionados
     * @return - períodos que ainda podem ser selecionados
     */
    public List<TipoPeriodoFuncionamentoPDV> getPeriodosPossiveis(List<PeriodoFuncionamentoDTO> selecionados) {

        List<TipoPeriodoFuncionamentoPDV> possiveis = new ArrayList<TipoPeriodoFuncionamentoPDV>();

        for (TipoPeriodoFuncionamentoPDV periodo : TipoPeriodoFuncionamentoPDV.values()) {

            try {
                selecionados.add(new PeriodoFuncionamentoDTO(periodo, null, null));
                validarPeriodos(selecionados);
                selecionados.remove(selecionados.size() - 1);

                possiveis.add(periodo);
            } catch (ValidacaoException e) {
                selecionados.remove(selecionados.size() - 1);
            }
        }
        return possiveis;
    }

    /**
     * Valida se uma lista de períodos é valida, de acordo com as regras
     * definidas na EMS 0159
     * 
     * @param listaTipos
     * @throws Exception
     */
    public void validarPeriodos(List<PeriodoFuncionamentoDTO> periodos) {

        List<TipoPeriodoFuncionamentoPDV> listaTipos = new ArrayList<TipoPeriodoFuncionamentoPDV>();

        for (PeriodoFuncionamentoDTO p : periodos) {
            listaTipos.add(p.getTipoPeriodoFuncionamentoPDV());
        }

        validarDuplicidadeDePeriodo(listaTipos);

        if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.DIARIA)) {

        	
        	if (listaTipos.size() == 2 && listaTipos.contains(TipoPeriodoFuncionamentoPDV.FERIADOS)) {
        		
        	} else if (listaTipos.size() > 1) {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Ao selecionar "
                                + TipoPeriodoFuncionamentoPDV.DIARIA
                                        .getDescricao()
                                + ", nenhum outro item deve ser incluído.");
            }

        }

        if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS)) {

            if (listaTipos.size() > 1) {

                throw new ValidacaoException(
                        TipoMensagem.WARNING,
                        "Ao selecionar "
                                + TipoPeriodoFuncionamentoPDV.VINTE_QUATRO_HORAS
                                        .getDescricao()
                                + ", nenhum outro item deve ser incluído.");
            }

        }

        if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA)) {

            if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SEGUNDA_FEIRA)
                    || listaTipos
                            .contains(TipoPeriodoFuncionamentoPDV.TERCA_FEIRA)
                    || listaTipos
                            .contains(TipoPeriodoFuncionamentoPDV.QUARTA_FEIRA)
                    || listaTipos
                            .contains(TipoPeriodoFuncionamentoPDV.QUINTA_FEIRA)
                    || listaTipos
                            .contains(TipoPeriodoFuncionamentoPDV.SEXTA_FEIRA)) {

                throw new ValidacaoException(
                        TipoMensagem.WARNING,
                        "Ao selecionar o período de '"
                                + TipoPeriodoFuncionamentoPDV.SEGUNDA_SEXTA
                                        .getDescricao()
                                + "', não é permitido a selecao específica de um dia da semana.");
            }
        }

        if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA)) {

            if (listaTipos.contains(TipoPeriodoFuncionamentoPDV.SABADO)
                    || listaTipos.contains(TipoPeriodoFuncionamentoPDV.DOMINGO)) {

                throw new ValidacaoException(
                        TipoMensagem.WARNING,
                        "Ao selecionar o período de '"
                                + TipoPeriodoFuncionamentoPDV.FINAIS_SEMANA
                                        .getDescricao()
                                + "', não é permitido a definição específíca para sábado ou domingo.");
            }
        }
    }

    /**
     * Valida duplicidade de período
     * 
     * @param periodos
     *            - periodos
     * @throws Exception
     *             - Exceção ao encontrar registro duplicado.
     */
	private void validarDuplicidadeDePeriodo(List<TipoPeriodoFuncionamentoPDV> periodos) {

		for (TipoPeriodoFuncionamentoPDV item : periodos) {
			int count = 0;
			for (TipoPeriodoFuncionamentoPDV itemComparado : periodos) {
				if (item.equals(itemComparado)) {
					count++;
					if (count > 1) {
						throw new ValidacaoException(TipoMensagem.WARNING, "O período " + item.getDescricao()+ " foi incluido a lista mais de uma vez.");
					}
				}
			}
		}

	}

    /**
     * ENDERECO
     * 
     * @param pdvDTO
     * @param pdv
     */
    private void processarEnderecos(PdvDTO pdvDTO, PDV pdv) {

        List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar = pdvDTO
                .getEnderecosAdicionar();

        if (listaEnderecoAssociacaoSalvar != null
                && !listaEnderecoAssociacaoSalvar.isEmpty()) {

            this.validarInclusaoEdicaoEnderecoPrincipal(
                    listaEnderecoAssociacaoSalvar, pdv);

            this.salvarEnderecosPDV(pdv, listaEnderecoAssociacaoSalvar);
        }

        if (pdvDTO.getEnderecosRemover() != null
                && !pdvDTO.getEnderecosRemover().isEmpty()) {

            this.validarExcluirEnderecoPrincipal(pdv,
                    pdvDTO.getEnderecosRemover(), listaEnderecoAssociacaoSalvar);

            this.removerEnderecosPDV(pdv, pdvDTO.getEnderecosRemover());
        }  
    }
    
    /**
     * ENDERECO
     * 
     * @param pdv
     * @param listaEnderecoAssociacao
     * @param listaEnderecoAssociacaoSalvar
     */
    private void validarExcluirEnderecoPrincipal(PDV pdv,
            Set<Long> listaEnderecoAssociacao,
            List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar) {

        if (this.existeEnderecoPrincipal(listaEnderecoAssociacaoSalvar)) {
            return;
        }

        if (pdv.getEnderecos() != null && !pdv.getEnderecos().isEmpty()
                && pdv.getEnderecos().size() > 1) {

            if (pdv.getEnderecos().size() == listaEnderecoAssociacao.size()) {
                return;
            }

            this.validarExclusaoEnderecoPrincipal(pdv, listaEnderecoAssociacao);
        }
    }

    /**
	 * ENDERECO
	 * 
	 * Retorna um Endereco à ser editado ou cadastrado
	 * @param enderecoDTO
	 * @param pessoa
	 * @param novo
	 * @return Endereco
	 */
	private Endereco obterEndereco(EnderecoDTO enderecoDTO, Pessoa pessoa, boolean novo){
		
		Endereco endereco = new Endereco();
		
		if (!novo){
			
			endereco = this.enderecoRepository.buscarPorId(enderecoDTO.getId());
		}

		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setCep(enderecoDTO.getCep());
		endereco.setCodigoCidadeIBGE(enderecoDTO.getCodigoCidadeIBGE());
		endereco.setCidade(enderecoDTO.getCidade());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
		endereco.setLogradouro(enderecoDTO.getLogradouro());
		endereco.setNumero(enderecoDTO.getNumero());
		endereco.setUf(enderecoDTO.getUf());
		endereco.setCodigoUf(enderecoDTO.getCodigoUf());
		endereco.setPessoa(pessoa);
		
	    return endereco;
	}
	
	/**
	 * ENDERECO
	 * 
	 * Persiste EnderecoPdv e Endereco
	 * Valida apenas endereços vinculados ao pdv
	 * @param pdv
	 * @param listaEnderecoAssociacao
	 */
	private void salvarEnderecosPDV(PDV pdv, List<EnderecoAssociacaoDTO> listaEnderecoAssociacao) {
		
		Pessoa pessoa = pdv.getCota().getPessoa();
		
		for (EnderecoAssociacaoDTO enderecoAssociacao : listaEnderecoAssociacao) {
			
			if (enderecoAssociacao.getTipoEndereco() == null){
				
				continue;
			}
            
			EnderecoDTO enderecoDTO = enderecoAssociacao.getEndereco();
			    
			this.enderecoService.validarEndereco(enderecoDTO, enderecoAssociacao.getTipoEndereco());
			
			EnderecoPDV enderecoPdv = this.enderecoPdvRepository.buscarPorId(enderecoAssociacao.getId());
			
			Endereco endereco = null;
			
			boolean novoEnderecoPdv = false;
			
			if (enderecoPdv == null) {

				novoEnderecoPdv = true;
				
				enderecoPdv = new EnderecoPDV();

				enderecoPdv.setPdv(pdv);
			}
			
			boolean novoEndereco = (novoEnderecoPdv && !enderecoAssociacao.isEnderecoPessoa());
			
			endereco = this.obterEndereco(enderecoDTO, pessoa, novoEndereco);

			enderecoPdv.setEndereco(endereco);

			enderecoPdv.setPrincipal(enderecoAssociacao.isEnderecoPrincipal());

			enderecoPdv.setTipoEndereco(enderecoAssociacao.getTipoEndereco());
				
			this.enderecoPdvRepository.merge(enderecoPdv);
		}
	}

	/**
	 * ENDERECO
	 * 
	 * Remove lista de EnderecoPdv
	 * @param pdv
	 * @param listaEnderecoAssociacao
	 */
	private void removerEnderecosPDV(PDV pdv,
									 Set<Long> enderecosId) {
		
		List<Long> idsEndereco = new ArrayList<Long>();

		for (Long enderecoAssociacaoId : enderecosId) {
			
			if(enderecoAssociacaoId!= null){

				EnderecoPDV enderecoPdv = this.enderecoPdvRepository.buscarPorId(enderecoAssociacaoId);
				
				if(enderecoPdv!= null){
	
					idsEndereco.add(enderecoPdv.getEndereco().getId());
	
					this.enderecoPdvRepository.remover(enderecoPdv);
				}
			}
		}
		
		if(!idsEndereco.isEmpty()){
			
			this.enderecoService.removerEnderecos(idsEndereco);
		}
	}
	
	/**
     * ENDERECO
     * 
     * @param pdv
     * @param listaEnderecoAssociacao
     */
    private void validarExclusaoEnderecoPrincipal(PDV pdv,
            Set<Long> listaEnderecoAssociacao) {

        for (Long idEndereco : listaEnderecoAssociacao) {

            for (EnderecoPDV end : pdv.getEnderecos()) {

                if (end.getId().equals(idEndereco)
                        && end.isPrincipal()) {
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Pelo menos um endereço associado ao PDV deve ser principal!");
                }
            }
        }
    }

    /**
     * ENDERECO
     * 
     * @param listaEnderecoAssociacaoSalvar
     * @param pdv
     */
    private void validarInclusaoEdicaoEnderecoPrincipal(
            List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar, PDV pdv) {

        if (!this.existeEnderecoPrincipal(listaEnderecoAssociacaoSalvar)) {

            if (pdv.getEnderecos() != null && !pdv.getEnderecos().isEmpty()) {

                for (EnderecoPDV endereco : pdv.getEnderecos()) {

                    for (EnderecoAssociacaoDTO end : listaEnderecoAssociacaoSalvar) {

                        if (end.getId().equals(endereco.getEndereco().getId())
                                && endereco.isPrincipal()) {

                            throw new ValidacaoException(TipoMensagem.WARNING,
                                    "Pelo menos um endereço associado ao PDV deve ser principal!");
                        }
                    }
                }
            } else {
                throw new ValidacaoException(TipoMensagem.WARNING,
                        "Pelo menos um endereço associado ao PDV deve ser principal!");
            }
        }
    }

    /**
     * ENDERECO
     * 
     * @param listaEnderecoAssociacaoSalvar
     * @return
     */
    private boolean existeEnderecoPrincipal(
            List<EnderecoAssociacaoDTO> listaEnderecoAssociacaoSalvar) {

        if (listaEnderecoAssociacaoSalvar != null) {

            for (EnderecoAssociacaoDTO end : listaEnderecoAssociacaoSalvar) {

                if (end.isEnderecoPrincipal()) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }
    
    /**
     * TELEFONE
     * 
     */
    @Transactional
    @Override
    public List<TelefoneAssociacaoDTO> buscarTelefonesPdv(Long idPdv,
            Long idCota) {

        if (idCota == null)
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "IdCota é obrigatório");

        Cota cota = cotaRepository.buscarPorId(idCota);

        if (cota == null)
            throw new ValidacaoException(TipoMensagem.ERROR,
                    "IdCota é obrigatório");

        Long idPessoa = cota.getPessoa().getId();

        Set<Long> telRemover = null;

        List<TelefoneAssociacaoDTO> listaTelAssoc = null;

        if (idPdv != null) {

            listaTelAssoc = this.telefonePdvRepository.buscarTelefonesPdv(
                    idPdv, null);

            telRemover = new HashSet<Long>();

            for (TelefoneAssociacaoDTO dto : listaTelAssoc) {

                telRemover.add(dto.getTelefone().getId());
            }
        }

        List<TelefoneAssociacaoDTO> lista = this.telefoneService
                .buscarTelefonesPorIdPessoa(idPessoa, telRemover);

        if (lista != null && !lista.isEmpty()) {

            if (listaTelAssoc == null)
                listaTelAssoc = new ArrayList<TelefoneAssociacaoDTO>();

            listaTelAssoc.addAll(lista);
        }

        return listaTelAssoc;
    }
    
	/**
	 * TELEFONE
	 * 
	 * @param pdv
	 * @param listaTelefoneAssociacao
	 * @param listaTelefoneAssociacaoSalvar
	 */
    private void validarExcluirTelefonePrincipal(PDV pdv,
            Set<Long> listaTelefoneAssociacao,
            List<TelefoneAssociacaoDTO> listaTelefoneAssociacaoSalvar) {

        if (this.existeTelefonePrincipal(listaTelefoneAssociacaoSalvar)) {
            return;
        }

        if (pdv.getTelefones() != null && !pdv.getTelefones().isEmpty()
                && pdv.getTelefones().size() > 1) {

            if (pdv.getTelefones().size() == listaTelefoneAssociacao.size()) {
                return;
            }

            this.validarExclusaoTelefonePrincipal(pdv, listaTelefoneAssociacao);
        }
    }

    /**
     * TELEFONE
     * 
     * @param pdv
     * @param listaTelefoneAssociacao
     */
    private void validarExclusaoTelefonePrincipal(PDV pdv,
            Set<Long> listaTelefoneAssociacao) {

        for (Long idTelefone : listaTelefoneAssociacao) {

            for (TelefonePDV tel : pdv.getTelefones()) {

                if (tel.getTelefone().getId().equals(idTelefone)
                        && tel.isPrincipal()) {
                    throw new ValidacaoException(TipoMensagem.WARNING,
                            "Pelo menos um telefone associado ao PDV deve ser principal!");
                }
            }
        }
    }

    /**
     * TELEFONE
     * 
     * @param listaTelefoneAssociacaoSalvar
     */
    private void validarInclusaoEdicaoTelefonePrincipal(
            List<TelefoneAssociacaoDTO> listaTelefoneAssociacaoSalvar) {

        if (!this.existeTelefonePrincipal(listaTelefoneAssociacaoSalvar)) {

            throw new ValidacaoException(TipoMensagem.WARNING,
                    "Pelo menos um telefone associado ao PDV deve ser principal!");
        }
    }

    /**
     * TELEFONE
     * 
     * @param listaTelefoneAssociacaoSalvar
     * @return
     */
    private boolean existeTelefonePrincipal(
            List<TelefoneAssociacaoDTO> listaTelefoneAssociacaoSalvar) {

        if (listaTelefoneAssociacaoSalvar != null) {

            for (TelefoneAssociacaoDTO end : listaTelefoneAssociacaoSalvar) {

                if (end.isPrincipal()) {
                    return Boolean.TRUE;
                }
            }
        }

        return Boolean.FALSE;
    }

    @Transactional(readOnly = true)
    public boolean existePDVPrincipal(Long idCota, Long idPdv) {

        return pdvRepository.existePDVPrincipal(idCota, idPdv);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<PdvDTO> obterPdvsHistoricoTitularidade(FiltroPdvDTO filtro) {
        List<HistoricoTitularidadeCotaPDV> pdvs = pdvRepository.obterPDVsHistoricoTitularidade(filtro);
        return new ArrayList<PdvDTO>(HistoricoTitularidadeCotaDTOAssembler.toPdvDTOCollection(pdvs));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PdvDTO obterPdvHistoricoTitularidade(Long idPdv) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        return HistoricoTitularidadeCotaDTOAssembler.toPdvDTO(pdv);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<EnderecoAssociacaoDTO> obterEnderecosHistoricoTitularidadePDV(Long idPdv) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        return new ArrayList<EnderecoAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toEnderecoAssociacaoDTOCollection(pdv.getEnderecos()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TelefoneAssociacaoDTO> obterTelefonesHistoricoTitularidadePDV(Long idPdv) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        return new ArrayList<TelefoneAssociacaoDTO>(HistoricoTitularidadeCotaDTOAssembler.toTelefoneAssociacaoDTOCollection(pdv.getTelefones()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<GeradorFluxoDTO> obterGeradoresFluxoHistoricoTitularidadePDV(Long idPdv, Set<Long> codigos) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        
        List<HistoricoTitularidadeCotaCodigoDescricao> geradores = new ArrayList<HistoricoTitularidadeCotaCodigoDescricao>();
        HistoricoTitularidadeCotaCodigoDescricao geradorFluxoPrincipal = pdv.getGeradorFluxoPrincipal();
        if (geradorFluxoPrincipal != null) {
            geradores.add(geradorFluxoPrincipal);
        }
        
        Collection<HistoricoTitularidadeCotaCodigoDescricao> geradoresFluxoSecundarios = pdv.getGeradoresFluxoSecundarios();
        if (geradoresFluxoSecundarios != null) {
            geradores.addAll(geradoresFluxoSecundarios);
        }
        
        List<GeradorFluxoDTO> dtos = new ArrayList<GeradorFluxoDTO>(geradores.size());
        
        for (HistoricoTitularidadeCotaCodigoDescricao gerador : geradores) {
            if (codigos.contains(gerador.getCodigo())) {
                dtos.add(new GeradorFluxoDTO(gerador.getCodigo(), gerador.getDescricao()));
            }
        }
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<MaterialPromocionalDTO> obterMateriaisPromocionaisHistoricoTitularidadePDV(Long idPdv, Set<Long> codigos) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        List<MaterialPromocionalDTO> dtos = new ArrayList<MaterialPromocionalDTO>();
        
        for (HistoricoTitularidadeCotaCodigoDescricao material : pdv.getMateriais()) {
            if (codigos.contains(material.getCodigo())) {
                dtos.add(new MaterialPromocionalDTO(material.getCodigo(), material.getDescricao()));
            }
        }
        return dtos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] obterImagemHistoricoTitularidadePDV(Long idPdv) {
        Validate.notNull(idPdv, "Identificador do PDV não deve ser nulo!");
        HistoricoTitularidadeCotaPDV pdv = pdvRepository.obterPDVHistoricoTitularidade(idPdv);
        return pdv.getImagem();
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<AreaInfluenciaPDV> obterAreasInfluenciaPDV() {

        return areaInfluenciaPDVRepository.buscarTodos();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TipoGeradorFluxoPDV> obterTodosTiposGeradorFluxo() {

            return tipoGeradorFluxoPDVRepsitory.buscarTodos();
    }

    @Transactional(readOnly = true)
	@Override
	public List<PdvDTO> obterPDVs(Integer numeroCota) {
		return this.pdvRepository.obterPDVs(numeroCota);
	}

    @Transactional
    @Override
	public List<TipoGeradorFluxoPDV> obterTodosTiposGeradorFluxoOrdenado() {
		return this.tipoGeradorFluxoPDVRepsitory.obterTiposGeradorFluxoOrdenado();
	}

    @Override
    @Transactional(readOnly=true)
    public Long obterQtdPdvPorCota(Integer numeroCota) {
        
        return this.pdvRepository.obterQtdPdvPorCota(numeroCota);
    }

}
