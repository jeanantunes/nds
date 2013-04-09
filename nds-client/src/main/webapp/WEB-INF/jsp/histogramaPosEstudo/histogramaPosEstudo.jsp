
<script type="text/javascript" src="scripts/flexGridService.js"></script>
<script type="text/javascript" src="scripts/histogramaPosEstudo.js"></script>
<script type="text/javascript">
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

<div id="dialog-alterar-faixa" title="Consulta de Cotas do Histograma de Venda" style="display:none;">
<fieldset style="width:350px; margin-top:8px;">
  <legend>Alterar Faixa de Reparte</legend>
  <table class="faixasReparteGrid"></table>
</fieldset>
</div>

<div class="corpo">
      <br clear="all"/>
    <br />
   
    <div class="container">
    
       <fieldset class="classFieldset">
   	    <legend> Histograma Pré-Análise</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="45"><strong>Código:</strong></td>
              <td width="60"><span id="codigoProdutoFs"></span></td>
              <td width="56"><strong>Produto:</strong></td>
              <td width="178" id="nomeProdutoFs"/>
              <td width="40"><strong>Edição:</strong></td>
              <td width="50"><span id="edicaoProdutoFs" /></td>
              <td width="80"><strong>Classificação:</strong></td>
              <td width="98"><span id="classificacaoProdutoFs"></td>
              <td width="61"><strong>Segmento:</strong></td>
              <td width="98"><span id="segmentoFs"></td>
              <td width="45"><strong>Estudo:</strong></td>
              <td width="78"><span id="codigoEstudoFs"></td>
            </tr>
            <tr>
              <td><strong>Período:</strong></td>
              <td><span id="periodoFs"></td>
              <td><strong>Liberado:</strong></td>
              <td><img id="estudoLiberadoFs" width="16" height="16" alt="Liberado" /></td>
              <td>&nbsp;</td>
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
	            <table width="950" border="0" cellspacing="2" cellpadding="2">
				      <tr>
				        <td width="423" rowspan="4" valign="top"><!--<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>-->
				          <!-- <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>-->
				          <span class="bt_novos"><a href="javascript:;" id="alterarFaixaReparte"><img src="images/ico_editar.gif" alt="Alterar Faixar" hspace="5" border="0" />Alterar Faixa</a></span>
				          <span class="bt_novos"><a href="javascript:;" id="analiseEstudo"><img src="images/ico_redistribuicao_complementar.gif" alt="Análise" hspace="5" border="0" />Análise</a></span>
				          <span class="bt_novos"><a href="javascript:;" id="recalcularEstudo"><img src="images/ico_add_novo.gif" alt="Recalcular Estudo" hspace="5" border="0" />Recalcular Estudo</a></span>
				          <!-- <span class="bt_novos"><a href="javascript:;"><img src="images/ico_negociar.png" alt="Reabrir" hspace="5" border="0" />Reabrir</a></span>-->
				          <span class="bt_novos"><a href="javascript:;" id="baseEstudo"><img src="images/ico_estudo_complementar.gif" alt="Divergências" hspace="5" border="0" />Base de estudo</a></span>
				          <span class="bt_novos"><a href="javascript:;" id="excluirEstudo"><img src="images/ico_excluir.gif" alt="Excluir Estudo" hspace="5" border="0" />Excluir Estudo</a></span>
				          <span class="bt_novos"><a href="javascript:;" id="botaoVoltarMatrizDistribuicao"><img src="images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span><br clear="all" />
				
				        </td>
				        <td width="116" style="border-bottom:1px solid #ccc;">Reparte Total:</td>
				        <td width="41" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteTotal"></td>
				        <td width="86" style="border-bottom:1px solid #ccc;">NPDV Atual:</td>
				        
				        <!-- quantidade de cotas ativas na base da distribuidora -->
				        <td width="41" style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvAtual"></td>
				        <td style="border-bottom:1px solid #ccc;">&nbsp;</td>
				        <td style="border-bottom:1px solid #ccc;">Sugerida</td>
				        <td style="border-bottom:1px solid #ccc;">Estudo</td>
				        </tr>
				  <tr>
				    <td style="border-bottom:1px solid #ccc;">Reparte Promocional:</td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoRepartePromocional"></td>
				    <td style="border-bottom:1px solid #ccc;">NPDV Produto:</td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvProduto"></td>
				    <td width="73" style="border-bottom:1px solid #ccc;">Rep. Mínimo:</td>
				    <td width="60" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteMinimoSugerida"></td>
				    <td width="60" style="border-bottom:1px solid #ccc;" id="fieldSetResumoReparteMinimoEstudo"></td>
				    </tr>
				  <tr>
				    <td style="border-bottom:1px solid #ccc;">Reserva Técnica:</td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoReservaTecnica">200</td>
				    <td style="border-bottom:1px solid #ccc;">NPDV Compl:</td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoNpdvComplementar"></td>
				    <td style="border-bottom:1px solid #ccc;">Abrangência:</td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoAbrangenciaSugerida"></td>
				    <td style="border-bottom:1px solid #ccc;" id="fieldSetResumoAbrangenciaEstudo"></td>
				    </tr>
				  <tr>
				    <td height="19" style="border-bottom:1px solid #ccc;">Reparte Distribuida:</td>
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
