<head>
<script type="text/javascript" src="scripts/pesquisaCota.js"></script>
<script type="text/javascript" src="scripts/classificacaoNaoRecebida.js" ></script>
<script language="javascript" type="text/javascript">

var	pesquisaCota = new PesquisaCota();

$(function() {
	classificacaoNaoRecebidaController.init();
});

function excluirClassificacao() {
	//$( "#dialog:ui-dialog" ).dialog( "destroy" );

	$( "#dialog-excluir" ).dialog({
		resizable: false,
		height:170,
		width:380,
		modal: true,
		buttons: {
			"Confirmar": function() {
				$( this ).dialog( "close" );
				$("#effect").show("highlight", {}, 1000, callback);
			},
			"Cancelar": function() {
				$( this ).dialog( "close" );
			}
		}
	});
};		

function mostraFiltroPorCota(){
	$('.filtroPorCota').show();
	$('.filtroPorClassificacao').hide();
	$('.porClassificacao').hide();
	}
function mostraFiltroPorClassificacao(){
	$('.filtroPorCota').hide();
	$('.filtroPorClassificacao').show();
	$('.porCota').hide();	
}
</script>

</head>

<body>

<div id="dialog-incluirCotaNaClassificacaoNaoRecebida" style="display: none;" title="Incluir cota(s) na Classificação Não Recebida">
	<p>Confirma a inclusão desta cota?</p>
</div>

<div id="dialog-excluirCotaDaClassificacaoNaoRecebida" style="display: none;" title="Excluir cota da Classificação Não Recebida">
	<p>Confirma a exclusão desta cota na classificação não recebida?</p>
</div>

<div id="dialog-incluirClassificacaoNaCota" style="display: none;" title="Incluir classificação(ões) não recebidas na cota">
	<p>Confirma a inclusão desta classificação?</p>
</div>

<div id="dialog-excluirCotaDaClassificacaoNaoRecebida" style="display: none;" title="Excluir Classificação Não Recebida">
	<p>Confirma a exclusão desta classificação?</p>
</div>

<div class="corpo">
 <br clear="all"/>
    <br />
   
    <div class="container">
    
    <!-- <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Classificação Não Recebida < evento > com < status >.</b></p>
	</div-->
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Classificação Não Recebida</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
          <tr>
            <td width="20"><input type="radio" name="radio" id="radio" value="radio" onclick="mostraFiltroPorClassificacao();" /></td>
            <td width="90">Classificação</td>
            <td width="20"><input type="radio" name="radio" id="radio2" value="radio" onclick="mostraFiltroPorCota();" /></td>
            <td width="37">Cota</td>
            <td width="757">
            <form id="filtroPrincipalClassificacao">
           	  <table width="756" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorClassificacao" style="display:none;">
            <tr>
            	<td width="76">Classificação:</td>
                <td width="560"><select name="filtro.idTipoClassificacaoProduto" id="selectClassificacao" style="width:200px;">
                  <option selected="selected">Selecione...</option>
                  <c:forEach items="${listaTipoClassificacao}" var="tipoClassificacao">
                  	<option value="${tipoClassificacao.key}">${tipoClassificacao.value}</option>
                  </c:forEach>
                </select></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisarPorClassificacao" >Pesquisar</a></span></td>
            </tr>
          </table>
          </form>
          <form id="filtroPrincipalPorCota">
          	<table width="756" border="0" cellpadding="2" cellspacing="1" class="filtro filtroPorCota" style="display:none;">
	          <tr>
	            <td width="42">Cota:</td>
	            <td width="122"><input type="text" name="filtro.cotaDto.numeroCota" id="porCota_numeroCota" style="width:80px; float:left; margin-right:5px;"/>
	              <span class="classPesquisar"><a href="javascript:;">&nbsp;</a></span></td>
	            <td width="50">Nome:</td>
	            <td width="412"><input type="text" name="filtro.cotaDto.nomePessoa" id="porCota_nomeCota" style="width:200px;"/></td>
	            <td width="104"><span class="bt_pesquisar"><a href="javascript:;" id="pesquisarPorCota" onclick="porCota();">Pesquisar</a></span></td>
	          </tr>
	        </table>
          </form>
          
            </td>
          </tr>
        </table>
        
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="grids" style="display:block;">
      
      <div class="porClassificacao" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Cotas que Não Recebem</legend>
        
        	<table class="classificaNaoRecebidaGrid"></table>
             <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;" id="porClassificacaoGerarXLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;" id="porClassificacaoGerarPDF"><img src="images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
        
      </fieldset>

      
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Cotas que Recebem</legend>
       	  <table width="275" border="0" cellpadding="2" cellspacing="1" class="filtro">
       	    <tr>
       	      <td width="33">Cota:</td>
       	      <td width="41"><input type="text" id="cotasQueRecebem_numeroCota" style="width:40px;"/></td>
              <td width="41">Nome:</td>
       	      <td width="115"><input type="text" id="cotasQueRecebem_nomeCota" style="width:140px;"/></td>
              <td width="19"><span class="classPesquisar"><a href="javascript:;" id="pesquisarCotaQueRecebeClassificacao">&nbsp;</a></span></td>
   	        </tr>
   	      </table>
       	  <br />
        	<table class="classificacaoGrid"></table>
              <span class="bt_novos" title="Confirmar" style="float:right;"><a href="javascript:;" id="confirmarInclusaoDaCotaNaClassificacaoNaoRecebida"><img src="images/ico_check.gif" hspace="5" border="0" />Confirmar</a></span>
        
      </fieldset>
      </div>
      
      
      <div class="porCota" style="display:none;">
      <fieldset class="classFieldset" style="float:left; width:631px!important; margin-right:10px!important;">
       	  <legend>Classificações Não Recebidas</legend>
        
        	<table class="classificaCotaGrid"></table>
             <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;" id="porCotaGerarXLS"><img src="images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;" id="porCotaGerarPDF"><img src="images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
        
      </fieldset>
     
      <fieldset class="classFieldset" style="float:left; width:300px!important;">
       	  <legend>Classificações Recebidas</legend>
       	  <br />
        	<table class="classificacaoBGrid"></table>
              <span class="bt_novos" title="Confirmar" style="float:right;"><a href="javascript:;" id="confirmarInclusaoDaClassificacaoNaCota"><img src="images/ico_check.gif" hspace="5" border="0" />Confirmar</a></span>
        
      </fieldset>
      </div>
      
      </div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
</body>
