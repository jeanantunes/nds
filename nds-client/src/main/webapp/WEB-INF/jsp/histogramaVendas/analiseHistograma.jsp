
<div id="analiseHistogramaContent">

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

<div id="dialog-alterar-faixa" title="Consulta de Cotas do Histograma de Venda" style="display:none;">
<fieldset style="width:350px; margin-top:8px;">
  <legend>Alterar Faixa de Reparte</legend>
  <table class="faixasVendaGrid" id="faixasReparteGrid"></table>
</fieldset>
</div>


    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Faixa de Reparte < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar </legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="66"><strong>Código:</strong></td>
              <td width="146">${codigoLabel}</td>
              <td width="75"><strong>Produto:</strong></td>
              <td width="160">${nomeProduto}</td>
              <td width="58"><strong>Edições:</strong></td>
              <td colspan="3">${listaEdicoes}</td>
            </tr>
            <tr>
              <td><strong>Segmento:</strong></td>
              <td>${segmentoLabel}</td>
              <td><strong>Classificação:</strong></td>
              <td>${classificacaoLabel}</td>
              <td><strong>Elemento:</strong></td>
              <td width="213">${labelElemento}</td>
              <td width="78"><strong>Componente:</strong></td>
              <td width="113">${labelComponente}</td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
      <div class="grids" style="display:block;">
      <fieldset class="classFieldset">
       	  <legend>Base de Estudo / Análise</legend>
        
        	<table class="estudosAnaliseHistGrid" id="estudosAnaliseHistGrid"></table>
            
   		
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
   		<legend>Resumo do Estudo</legend>
            <table width="950" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td width="426" rowspan="5" valign="top">
          <span class="bt_novos" title="Gerar Arquivo">
          	<a id="a1" href="#" data-href="${pageContext.request.contextPath}/distribuicao/histogramaVendas/exportar">
          	<img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
          </span>
          <span class="bt_novos"><a href="javascript:;" isEdicao="true" id="alterarFaixaReparte" ><img src="images/ico_editar.gif" alt="Alterar Faixar" hspace="5" border="0" />Alterar Faixa</a></span><!-- <span class="bt_novos"><a href="javascript:;"><img src="../images/ico_negociar.png" alt="Reabrir" hspace="5" border="0" />Reabrir</a></span>-->
          <span class="bt_novos"><a href="javascript:anaLiseHistogramaController.voltarFiltro();"><img src="images/seta_voltar.gif" alt="Voltar" hspace="5" border="0" />Voltar</a></span><br clear="all" />

          </td>
        <td width="98" style="border-bottom:1px solid #ccc;">Cotas Ativas:</td>
        <td width="45" style="border:1px solid #ccc;" id="cotasAtivasCell"> </td>
        <td width="117" style="border-bottom:1px solid #ccc;" >Reparte Total:</td>
        <td width="45" style="border:1px solid #ccc;">${reparteTotalDistribuidor}  </td>
        <td style="border-bottom:1px solid #ccc;">Abrangência Distribuição:</td>
        <td style="border:1px solid #ccc;" id="abrangenciaDistribuicaoCell"> </td>
        </tr>
  <tr>
    <td style="border-bottom:1px solid #ccc;">Cotas Produto:</td>
    <td style="border:1px solid #ccc;" id="cotasProdutoCell"> </td>
    <td style="border-bottom:1px solid #ccc;">Reparte Distribuido:</td>
    <td style="border:1px solid #ccc;" id="reparteDistribuidoCell"> </td>
    <td width="130" style="border-bottom:1px solid #ccc;">Abrangência Venda:</td>
    <td width="45" style="border:1px solid #ccc;" id="abrangenciaVendaCell"> </td>
    </tr>
  <tr>
    <td style="border-bottom:1px solid #ccc;">Cotas Esmagadas:</td>
    <td style="border:1px solid #ccc;" id="cotasEsmagadasCell"> </td>
    <td style="border-bottom:1px solid #ccc;">Venda:</td>
    <td style="border:1px solid #ccc;" id="vdaTotalCell"></td>
    <td style="border-bottom:1px solid #ccc;">Reparte Médio:</td>
    <td style="border:1px solid #ccc;" id="repMedioCell"> </td>
    </tr>
  <tr>
    <td height="19" style="border-bottom:1px solid #ccc;">Venda Esmagada:</td>
    <td style="border:1px solid #ccc;" id="vendaEsmagadasCell"> </td>
    <td style="border-bottom:1px solid #ccc;">Eficiência de Venda:</td>
    <td style="border:1px solid #ccc;" id="eficienciaDeVendaCell"> </td>
    <td style="border-bottom:1px solid #ccc;">Venda Média:</td>
    <td style="border:1px solid #ccc;" id="vdaMedioCell"> </td>
    </tr>
  <tr>
    <td height="19" style="border-bottom:1px solid #ccc;">&nbsp;</td>
    <td style="border-bottom:1px solid #ccc;">&nbsp;</td>
    <td style="border-bottom:1px solid #ccc;">&nbsp;</td>
    <td style="border-bottom:1px solid #ccc;">&nbsp;</td>
    <td style="border-bottom:1px solid #ccc;">Encalhe Médio:</td>
    <td style="border:1px solid #ccc;" id="encalheMedioCell"></td>
  </tr>
        </table>
       </fieldset>
</div>
        

    
    </div>
</div> 
