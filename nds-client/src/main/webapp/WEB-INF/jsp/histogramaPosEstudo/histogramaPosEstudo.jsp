<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
-<!-- histogramaPosEstudo -->
<script type="text/javascript" src="scripts/flexGridService.js"></script>
<script type="text/javascript" src="scripts/histogramaPosEstudo.js"></script>
<script type="text/javascript">

var histogramaPosEstudo_cotasRepMenorVenda="";
$(function(){
  histogramaPosEstudoController.init();
  
});
</script>

<div id="dialog-divergencia" title="Base de Estudos" style="display:none;">
  <fieldset style="width:300px; float:left;">
    <legend>Base Sugerida</legend>
    <table class="baseSugeridaGrid"></table>

  </fieldset>

  <fieldset style="width:300px;float:left; margin-left:6px;">
    <legend>Base Estudo</legend>
    <table class="baseEstudoGrid"></table>

  </fieldset>

</div>

<div id="popup_confirmar_exclusao_estudo" title="Excluir Estudo" style="display:none">
  <p>Confirma a exclusão do estudo?</p>
</div>

<div id="dialog-alterar-faixa" title="Consulta de Cotas" style="display:none;">
  <fieldset style="width:350px; margin-top:8px;">
    <legend>Alterar Faixa de Reparte</legend>
    <table class="faixasReparteGrid"></table>
  </fieldset>
</div>

<div class="areaBts">
  <div class="area">
    <span class="bt_novos">
      <a href="javascript:;" isEdicao="true" rel='tipsy' id="alterarFaixaReparte" title='Alterar Faixa'>
        <img src="images/ico_editar.gif" alt="Alterar Faixar" hspace="5" border="0" />
      </a>
    </span>
    <span class="bt_novos">
      <a href="javascript:;" rel='tipsy' id="analiseEstudo" title='Análise'>
        <img src="images/ico_redistribuicao_complementar.gif" alt="Análise" hspace="5" border="0" />

      </a>
    </span>
    <span class="bt_novos">
      <a href="javascript:;" isEdicao="true" rel='tipsy' id="recalcularEstudo" title='Recalcular Estudo'>
        <img src="images/ico_add_novo.gif" alt="Recalcular Estudo" hspace="5" border="0" />

      </a>
    </span>

    <span class="bt_novos">
      <a href="javascript:;" isEdicao="true" rel='tipsy' id="baseEstudo" title='Base de estudo'>
        <img src="images/ico_estudo_complementar.gif" alt="Divergências" hspace="5" border="0" />

      </a>
    </span>
    <span class="bt_novos">
      <a href="javascript:;" isEdicao="true" rel='tipsy' id="excluirEstudo" title='Excluir Estudo'>
        <img src="images/ico_excluir.gif" alt="Excluir Estudo" hspace="5" border="0" />

      </a>
    </span>
    <span class="bt_novos">
      <a href="javascript:;" rel='tipsy' title='Voltar' onclick="histogramaPosEstudoController.voltar(true);" id="botaoVoltarMatrizDistribuicao">
        <img src="images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />

      </a>
    </span>

  </div>
</div>
<br clear="all" />
<br />

<div class="corpo">
  <br clear="all"/>
  <br />

  <div class="container">

    <fieldset class="classFieldset">
      <legend>Histograma Pr&eacute;-An&aacute;lise</legend>
      <input type="hidden" id="modoAnalise" value="${modoAnalise}" />
      <table width="950" border="0" cellpadding="2" cellspacing="1">
        <tr>
          <td width="45"> <strong>Código:</strong>
          </td>
          <td width="60">
            <span id="codigoProdutoFs"></span>
          </td>
          <td width="56"> <strong>Produto:</strong>
          </td>
          <td width="178" id="nomeProdutoFs"/>
          <td width="40">
            <strong>Edição:</strong>
          </td>
          <td width="50">
            <span id="edicaoProdutoFs" />
          </td>
          <td width="80">
            <strong>Classificação:</strong>
          </td>
          <td width="98">
            <span id="classificacaoProdutoFs"></td>
            <td width="61">
              <strong>Segmento:</strong>
            </td>
            <td width="98">
              <span id="segmentoFs"></td>
              <td width="45">
                <strong>Estudo:</strong>
              </td>
              <td width="78">
                <span id="codigoEstudoFs"></td>
              </tr>
              <tr>
                <td>
                  <strong>Período:</strong>
                </td>
                <td>
                  <span id="periodoFs"></td>
                  <td>
                    <strong>Liberado:</strong>
                  </td>
                  <td>
                    <img id="estudoLiberadoFs" width="16" height="16" alt="Liberado" src="images/ico_check.gif" style="display:none"/>
                  </td>
                  <td>
                    &nbsp;
                    <input type="hidden" id="parcial" />
                  </td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
                </tr>
              </table>

            </fieldset>
            <div class="linha_separa_fields">&nbsp;</div>
            <div class="grids" style="display:block;">
              <fieldset class="classFieldset">
                <legend>Base de Estudo / Análise</legend>

                <table class="estudosAnaliseGrid"></table>

              </fieldset>
              <div class="linha_separa_fields">&nbsp;</div>
              <fieldset class="classFieldset">
                <legend>Resumo do Estudo</legend>
                <table width="550" border="0" cellspacing="2" cellpadding="2">
                  <tr>
                    <td width="116" style="border-bottom:1px solid #ccc;">Reparte Total:</td>
                    <td width="41" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteTotal"></td>
                    <td width="86" style="border-bottom:1px solid #ccc;">Cota Atual:</td>

                    <!-- quantidade de cotas ativas na base da distribuidora -->
                    <td width="41" style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvAtual"></td>
                    <td style="border-bottom:1px solid #ccc;">&nbsp;</td>
                    <td style="border-bottom:1px solid #ccc;">Sugerida</td>
                    <td style="border-bottom:1px solid #ccc;">Estudo</td>
                  </tr>
                  <tr>
                    <td style="border-bottom:1px solid #ccc;">Saldo:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoRepartePromocional"></td>
                    <td style="border-bottom:1px solid #ccc;">Cota Produto:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvProduto"></td>
                    <td width="73" style="border-bottom:1px solid #ccc;">Rep. Mínimo:</td>
                    <td width="60" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteMinimoSugerida"></td>
                    <td width="60" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteMinimoEstudo"></td>
                  </tr>
                  <tr>
                    <td style="border-bottom:1px solid #ccc;">Sobra:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoReservaTecnica"></td>
                    <td style="border-bottom:1px solid #ccc;">Cota Compl:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvComplementar"></td>
                    <td style="border-bottom:1px solid #ccc;">Abrangência:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoAbrangenciaSugerida"></td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoAbrangenciaEstudo"></td>
                  </tr>
                  <tr>
                    <td height="19" style="border-bottom:1px solid #ccc;">Reparte Distribuido:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteDistribuida"></td>
                    <td style="border-bottom:1px solid #ccc;">Rep. Médio Cota:</td>
                    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteMedioCota"></td>
                    <td colspan="3" style="border-bottom:1px solid #ccc;" id="fieldSetResumoAbrangenciaVendaPercent"></td>
                  </tr>
                </table>
              </fieldset>
            </div>
          </div>
        </div>