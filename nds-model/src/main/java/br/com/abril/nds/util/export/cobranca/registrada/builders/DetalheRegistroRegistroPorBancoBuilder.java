package br.com.abril.nds.util.export.cobranca.registrada.builders;

import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistro01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroBradesco01;
import br.com.abril.nds.util.export.cobranca.registrada.CobRegEnvTipoRegistroItau01;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.xml.bind.ValidationException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class DetalheRegistroRegistroPorBancoBuilder implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String CODIGO_INSCRICAO_FISICA = "01";

    private static final String CODIGO_INSCRICAO_JURIDICA = "02";

    public static CobRegEnvTipoRegistro01 detalheRegistroPorBanco(Boleto boleto, Banco banco, int count, CobRegEnvTipoRegistro01 registro01) throws ValidationException {
        registro01.setTipoRegistro("1");
        registro01.setFiller("");
        registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
        registro01.setFiller1("");
        registro01.setContaCliente(String.valueOf(banco.getConta()));
        registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
        registro01.setTaxa(String.valueOf(banco.getJuros().intValue()));
        registro01.setFiller2("");

        // não sei o que setar
        registro01.setNumeroControle("00");
        registro01.setNossoNumero(boleto.getNossoNumero());
        ;
        registro01.setDigitoNossoNumero(boleto.getDigitoNossoNumero());
        registro01.setNumeroContrato("0");
        registro01.setDataSegundoDesconto("0");
        registro01.setValorSegundoDesconto("0");
        ;
        registro01.setFiller3("");
        registro01.setCarteira(String.valueOf(banco.getCarteira()));

        // não sei o que setar
        registro01.setCodigoServico("01");

        registro01.setNumero(boleto.getNossoNumero());

        try {
            registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
            registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
        } catch (Exception e) {
            throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
        }

        registro01.setValorTitulo(CurrencyUtil.formatarValor(boleto.getValor()).replace(".", ""));
        registro01.setNumeroBanco(banco.getNumeroBanco());
        registro01.setAgencia(String.valueOf(banco.getAgencia()));
        registro01.setFiller4(" ");
        registro01.setEspecie("01");
        registro01.setAceite("A");
        registro01.setJurosDia("0");
        ;
        registro01.setDataDesconto("0");
        registro01.setValorDesconto("0");
        registro01.setValorIOC("0");
        ;
        registro01.setValorAbatimento("0");

        registro01.setSequencialRegistro(String.valueOf(count));

        return registro01;
    }

    public static CobRegEnvTipoRegistroItau01 detalheRegistroPorBancoItau(Boleto boleto, Banco banco, int count) throws ValidationException {

        CobRegEnvTipoRegistroItau01 registro01 = new CobRegEnvTipoRegistroItau01();

        registro01.setTipoRegistro("1");
        registro01.setCodigoInscricao("02");

        Pessoa pessoaCedente = banco.getPessoaJuridicaCedente();


        String nomeCedente = "";
        String documentoCedente = "";

        String numeroCota = "";
        StringBuilder usoDaEmpresa = new StringBuilder();

        if (boleto.getCota() != null) {
            if (boleto.getCota().getNumeroCota() != null) {
                numeroCota = boleto.getCota().getNumeroCota().toString();

                if (numeroCota.length() > 5) {
                    numeroCota = numeroCota.substring(0, 5);
                }

            }
        }

        usoDaEmpresa.append(setarDataFomatada());
        usoDaEmpresa.append(numeroCota);

        if (pessoaCedente instanceof PessoaJuridica) {

            nomeCedente = ((PessoaJuridica) pessoaCedente).getRazaoSocial();
            documentoCedente = ((PessoaJuridica) pessoaCedente).getCnpj();

        } else {

            nomeCedente = ((PessoaFisica) pessoaCedente).getNome();
            documentoCedente = ((PessoaFisica) pessoaCedente).getCpf();

        }

        registro01.setNumeroInscricao(documentoCedente.replace(".", "").replace("/", "").replace("-", ""));
        registro01.setAgenciaCedente(String.valueOf(banco.getAgencia()));
        registro01.setZeros("00");
        registro01.setContaCliente(String.valueOf(banco.getConta()));
        registro01.setDigitoConta(String.valueOf(banco.getDvConta()));
        registro01.setBrancos("");
        registro01.setInstrucao("0000");
        registro01.setUsoDaEmpresa(usoDaEmpresa.toString());
        registro01.setNossoNumero(boleto.getNossoNumero());
        registro01.setQtdeMoeda("0");
        registro01.setNumeroCarteira(String.valueOf(banco.getCarteira()));
        registro01.setUsoBanco("");
        registro01.setCarteira("I");
        registro01.setCodOcerrencia("01");
        registro01.setNumeroDocumento(boleto.getNossoNumero());

        try {
            registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
            registro01.setDataDeMora(Util.formataData(boleto.getDataVencimento()));
            registro01.setDataEmissao(Util.formataData(boleto.getDataEmissao()));
        } catch (Exception e) {
            throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
        }

        BigDecimal valorDuasCadasDecimais = CurrencyUtil.truncateDecimal(boleto.getValor(), 2);

        registro01.setValorTitulo(Util.getValorString(valorDuasCadasDecimais.toEngineeringString()));

        registro01.setNumeroBanco(banco.getNumeroBanco());

        registro01.setEspecie("01");

        registro01.setAceite("N");

        registro01.setAgencia(String.valueOf("00000"));

        registro01.setCodigoInstrucao("09");

        registro01.setCodigoInstrucao2("00");

        BigDecimal taxaJurosDiaria = MathUtil.divide(banco.getJuros(), new BigDecimal(30));

        BigDecimal valorJurosCalculado = CurrencyUtil.truncateDecimal((boleto.getValor().multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)))), 2);

        registro01.setJurosDia(Util.getValorString(valorJurosCalculado.toEngineeringString()));

        registro01.setDataDesconto("0");

        registro01.setValorDesconto("0");

        registro01.setValorIOC("0");

        registro01.setValorAbatimento("0");

        registro01.setPrazo("10");

        registro01.setBranco2("");

        PopularSacadoBuilder.popularSacadoCobrana(registro01, boleto);

        registro01.setSequencialRegistro(String.valueOf(count));

        return registro01;
    }

    public static CobRegEnvTipoRegistroBradesco01 detalheRegistroPorBancoBradesco(Boleto boleto, Banco banco, int count) throws ValidationException {

        CobRegEnvTipoRegistroBradesco01 registro01 = new CobRegEnvTipoRegistroBradesco01();

        registro01.setTipoRegistro("1");

        // pagina 17
        registro01.setAgenciaDebito("");
        registro01.setDigitoAgenciaDebito("");
        registro01.setRazaoContaCorrente("");
        registro01.setContaCorrente("");
        // pagina 17

        registro01.setIdentificacaoEmpresaBeneficiaria(montarIdentificacaoEmpresaBeneficiaria(banco));
        registro01.setNumeroControlePaticipante(boleto.getNossoNumero());
        registro01.setCodigoBanco("0");
        registro01.setCampoMulta("0");
        registro01.setPercentualMulta("0");
        registro01.setIdentificacaoTituloBanco(boleto.getNossoNumero());
        registro01.setDigitoConferenciaBancaria(boleto.getDigitoNossoNumero());

        registro01.setDescontoBonificacao("");
        registro01.setCondicaoEmissaoPapeleta("2");
        registro01.setIdentificacaoBoletoDebitoAutomatico("N");
        registro01.setIdentificaOperacaoBanco("");
        registro01.setIndicadorRateiroCredito("");
        registro01.setEnderecamentoAvisoDebitoAutomatico("");

        registro01.setBranco("");
        registro01.setIdentificacaoOcorrencia("01");

        Pessoa pessoaCedente = banco.getPessoaJuridicaCedente();

        String pessoaSacado = boleto.getCota().getPessoa().getDocumento();

        String nomeCedente = "";
        String documentoCedente = "";

        if (pessoaCedente instanceof PessoaJuridica) {

            nomeCedente = ((PessoaJuridica) pessoaCedente).getRazaoSocial();
            documentoCedente = ((PessoaJuridica) pessoaCedente).getCnpj();
//            registro01.setIdentificacaoTipoIncricaoPagador("01");
            registro01.setIdentificacaoTipoIncricaoPagador(CODIGO_INSCRICAO_JURIDICA);

        } else {

            nomeCedente = ((PessoaFisica) pessoaCedente).getNome();
            documentoCedente = ((PessoaFisica) pessoaCedente).getCpf();
//            registro01.setIdentificacaoTipoIncricaoPagador("01");
            registro01.setIdentificacaoTipoIncricaoPagador(CODIGO_INSCRICAO_FISICA);
        }

        registro01.setNumeroDocumento(documentoCedente.replace(".", "").replace("/", "").replace("-", ""));

        try {
            registro01.setDataVencimento(Util.formataData(boleto.getDataVencimento()));
            registro01.setDataLimiteCocessaoDesconto(Util.formataData(boleto.getDataVencimento()));
            registro01.setDataEmissaoTitulo(Util.formataData(boleto.getDataEmissao()));
        } catch (Exception e) {
            throw new ValidationException("Erro ao Formatar a Data Vencimento / Emissão");
        }

        BigDecimal valorDuasCadasDecimais = CurrencyUtil.truncateDecimal(boleto.getValor(), 2);

        registro01.setValorTitulo(Util.getValorString(valorDuasCadasDecimais.toEngineeringString()));

        registro01.setBancoEncarregadoCobranca("0");
        registro01.setAgenciaDepositaria("0");
        registro01.setEspecieTitulo("99");
        registro01.setIdentificacao("N");
        registro01.setInstrucao("0");
        registro01.setInstrucao2("0");

        BigDecimal taxaJurosDiaria = MathUtil.divide(banco.getJuros(), new BigDecimal(30));

        BigDecimal valorJurosCalculado = CurrencyUtil.truncateDecimal((boleto.getValor().multiply(MathUtil.divide(taxaJurosDiaria, new BigDecimal(100)))), 2);

        registro01.setJurosdia(Util.getValorString(valorJurosCalculado.toEngineeringString()));

        registro01.setValorDesconto("0");

        registro01.setValorIOF("0");

        registro01.setValorAbatimento("0");


        registro01.setNumeroIncricaoPagador(pessoaSacado.replace(".", "").replace("/", "").replace("-", ""));

        registro01.setNomePagador("");

        registro01.setMensagemCompleto("");

        PopularSacadoBuilder.popularSacadoCobrana(registro01, boleto);

        registro01.setSequencialBradescoRegistro(String.valueOf(count));

        return registro01;
    }

    private static String montarIdentificacaoEmpresaBeneficiaria(Banco banco) {
        //"0"+ banco.getCarteira()+banco.getAgencia()+banco.getConta()

        StringBuilder sb = new StringBuilder();

        sb.append("0")
                .append(StringUtils.leftPad(banco.getCarteira().toString(), 3, '0'))
                .append(StringUtils.leftPad(banco.getAgencia().toString(), 5, '0'))
                .append(StringUtils.leftPad(banco.getConta().toString(), 7, '0'))
                .append(StringUtils.leftPad(banco.getDvConta().toString(), 1, '0'));

        return sb.toString();
    }

    private static String setarDataFomatada() throws ValidationException {
        try {
            return Util.formataData(new Date());
        } catch (Exception e) {
            throw new ValidationException("Não formatar a data do arquivo");
        }
    }

}
