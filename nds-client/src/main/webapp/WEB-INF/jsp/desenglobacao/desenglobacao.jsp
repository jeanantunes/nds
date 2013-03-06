<head>

<script type="text/javascript" src="scripts/desenglobacao.js" ></script>
	<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
	<script type="text/javascript">

	var	pesquisaCota = new PesquisaCota();

	$(function() {
		desenglobacaoController.init();
	});


function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:500,
			width:650,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	

</script>
<style type="text/css">
.ui-tabs .ui-tabs-panel {
    padding: 0.45em 0.4em!important;
}
</style>
</head>

<body>
<div id="dialog-novo" title="Englobar">    
  <fieldset style="float:left; width:600px!important;">
    	<legend>Desenglobar de:</legend>
		<table width="400" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="40">Cota:</td>
    <td width="67"><input type="text" name="textfield4" id="textfield4" style="width:60px;" /></td>
    <td width="42">Nome:</td>
    <td width="202"><input type="text" name="textfield" id="textfield" style="width:200px;" /></td>
    <td width="17"><span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
  </tr>
</table>
    </fieldset>
  <fieldset style="float:left; width:600px!important;margin-top:10px;">
    	<legend>Englobar em:</legend>
		<table class="desenglobadosGrid"></table>
    </fieldset>
    </div>
<div class="corpo">
   <br clear="all"/>
    <br />
   
    <div class="container">
    
     <!--  <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
                <span class="ui-state-default ui-corner-all" style="float:right;">
                <a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a></span>
				<b>Englobação < evento > com < status >.</b></p>
	</div-->
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Cota</legend>
   	    <form id="filtroPrincipal">
    	 <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="32">Cota:</td>
                <td width="111"><input type="text" name="filtro.cotaDto.numeroCota" id="filtroPrincipalNumeroCota" style="width:100px;" /></td>
                <td width="44">Nome:</td>
                <td width="270"><input type="text" name="filtro.cotaDto.nomePessoa" id="filtroPrincipalNomePessoa" style="width:250px;" /></td>
                <td width="48">Status:</td>
                <td width="305"><input name="statusCota" id="statusCota" type="text" style="width:160px;" disabled="disabled" /></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisaPorCota">Pesquisar</a></span></td>
            </tr>
         </table>
		</form>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:none;">
      <fieldset class="classFieldset">
       	  <legend>Cotas Desenglobadas</legend>
        
        	<table class="englobadosGrid"></table>
            
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
    <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
       

           <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
   
      </fieldset>
       </div>
      <div class="linha_separa_fields">&nbsp;</div>
    </div>
</div> 
</body>
