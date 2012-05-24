<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>NDS - Novo Distrib</title>
<link rel="stylesheet" type="text/css" href="../css/NDS.css" />
<link rel="stylesheet" type="text/css" href="../css/menu_superior.css" />
<link rel="stylesheet" href="../scripts/jquery-ui-1.8.16.custom/development-bundle/themes/redmond/jquery.ui.all.css" />
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.core.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.effects.highlight.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.position.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.accordion.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/NDS.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.dialog.js"></script>
<script src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">


//para usar o tooltip 
//$("#feriados .ui-datepicker-calendar tbody tr td a").tip("blablabla")


	var dates = [];

	function popup(date, dates) {		
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:580,
			width:700,
			modal: true,
			buttons: {
				"Confirmar": function() {
					novoFeriado();
					D = date.match(/\d+/g);
					date = new Date(+D[2], D[1]-1, +D[0]);
					dates.push(date);
					highlightDays(date);
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 3000, callback);
					console.log(dates);					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
		$( "#dtFeriado" ).val(date);
		
	};
	
	function novoFeriado(){
		
		var dtFeriado = $("#dtFeriado").val();
		var tipoFeriado = $("#novoTipoFeriado").val();
		var descricao = $("#novoDescricao").val();
		var opera = $("#novoOpera").is(":checked");novoOpera
		var efetuaCobranca = $("#novoEfetuaCobranca").is(":checked");
		var repeteAnualmente = $("#novoRepeteAnualmente").is(":checked");
		
		
		$.postJSON("<c:url value='/administracao/cadastroCalendario/novoFeriado'/>",
				   "dtFeriado="+dtFeriado+
				   "&tipoFeriado="+ tipoFeriado,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
					   //pesquisar();
	               },
				   null,
				   true);
	}
	
	function fecharDialogs() {
		$( "#dialog-novo" ).dialog( "close" );
	}
	
	function popup_bt() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-editar" ).dialog({
			resizable: false,
			height:370,
			width:430,
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
	
	
	function popup_excluir() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:'auto',
			width:300,
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
	
	
	
	function highlightDays(date) {
        for (var i = 0; i < dates.length; i++) {
                if (dates[i] == date) {
                        //$("#feriados .ui-datepicker-calendar tbody tr td a").addClass('ui-state-active');
                }
        }
	}
	
	$(document).ready(function() {

		
		$( "#feriados" ).datepicker({
			numberOfMonths: 10,
			showButtonPanel: false,
			altField: "#alternate",
			dateFormat: "dd/mm/yy",
			dayNames: ['Domingo','Segunda','Terça','Quarta','Quinta','Sexta','Sábado','Domingo'],
            dayNamesMin: ['D','S','T','Q','Q','S','S','D'],
            dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sáb','Dom'],
            monthNames: ['<a href="index.htm">Janeiro</a>','<a href="index.htm">Fevereiro</a>','<a href="index.htm">Março</a>','<a href="index.htm">Abril</a>',
                         '<a href="index.htm">Maio</a>','<a href="index.htm">Junho</a>','<a href="index.htm">Julho</a>','<a href="index.htm">Agosto</a>',
                         '<a href="index.htm">Setembro</a>','<a href="index.htm">Outubro</a>','<a href="index.htm">Novembro</a>','<a href="index.htm">Dezembro</a>'],
            monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun','Jul','Ago','Set', 'Out','Nov','Dez'],

			onSelect: function(dateText, inst) {
				popup(dateText, dates);
			}
		});

		$( "#dtFeriado" ).datepicker({
			showOn: "button",
			dateFormat: "dd/mm/yy",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtFeriado1" ).datepicker({
			showOn: "button",
			dateFormat: "dd/mm/yy",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		
		
	});


</script>
<style>
label{ vertical-align:super;}
.ui-datepicker-inline{width:950px!important;}
.ui-datepicker-group{margin-left:4px;}
.ui-state-default{font-size:13px!important;}
.ui-datepicker-group{margin:0px!important; padding:5px!important;}
</style> 
</head>

<body>
<input type="hidden" id="alternate" /> 

<div id="dialog-excluir" title="Excluir Feriado">
	<p>Confirma a exclusão deste Feriado?</p>
</div>


<div id="dialog-editar" title="Editar Feriado">
	 <fieldset style="width:380px;">
		<legend>Dados do Feriado</legend>
    
    	<table width="365" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="114">Data:</td>
              <td width="240"><input type="text" name="dtFeriado1" id="dtFeriado1" style="width:110px;"/></td>
            </tr>
            <tr>
              <td>Tipo:</td>              
              <td><select name="select" id="select" style="width:150px;">
                <option selected="selected">Selecione...</option>
                <option>Estadual</option>
                <option>Municipal</option>
                <option>Nacional</option>
              </select></td>
            </tr>
            <tr>
              <td>Cidade:</td>
              <td><select name="select2" id="select2" style="width:150px;">
                <option selected="selected">Selecione...</option>
                <option>São Paulo</option>
                <option>Rio de Janeiro</option>
              </select></td>
            </tr>
            <tr>
              <td>Descrição:</td>
              <td><input type="text" name="textfield33" id="textfield34" style="width:230px;"/></td>
            </tr>
            <tr>
              <td>Opera?</td>
              <td><input name="opera" type="checkbox" value="" id="opera" /></td>
            </tr>
            <tr>
              <td>Efetua Cobrança?</td>
              <td><input name="opera2" type="checkbox" value="" id="opera2" /></td>
            </tr>
            <tr>
              <td>Repete Anualmente?</td>
              <td><input name="opera2" type="checkbox" value="" id="opera2" /></td>
            </tr>
          </table>
          </fieldset>
    </div>




<div id="dialog-novo" title="Incluir Novo Feriado">
	
     <fieldset style="width:650px;">
		<legend>Dados do Feriado</legend>
    
    	<table width="365" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="114">Data:</td>
              <td width="240"><input type="text" name="dtFeriado" id="dtFeriado" style="width:110px;"/></td>
            </tr>
            <tr>
              <td>Tipo:</td>              
              <td><select name="tipoFeriado" id="novoTipoFeriado" style="width:180px;">
                		<option selected="selected"> </option>
	                    <option value="FEDERAL">Federal</option>
	                    <option value="ESTADUAL">Estadual</option>
	                    <option value="MUNICIPAL">Municipal</option>
              </select></td>
            </tr>
            <tr>
              <td>Cidade:</td>
              <td><select name="select2" id="select2" style="width:150px;">
                <option selected="selected">Selecione...</option>
                <option>São Paulo</option>
                <option>Rio de Janeiro</option>
              </select></td>
            </tr>
            <tr>
              <td>Descrição:</td>
              <td><input type="text" name="textfield33" id="novoDescricao" style="width:230px;"/></td>
            </tr>
            <tr>
              <td>Opera?</td>
              <td><input name="novoOpera" type="checkbox" value="" id="novoOpera" /></td>
            </tr>
            <tr>
              <td>Efetua Cobrança?</td>
              <td><input name="novoEfetuaCobranca" type="checkbox" value="" id="novoEfetuaCobranca" /></td>
            </tr>
            <tr>
              <td>Repete Anualmente?</td>
              <td><input name="novoRepeteAnualmente" type="checkbox" value="" id="novoRepeteAnualmente" /></td>
            </tr>
          </table>
          </fieldset>
          <fieldset style="width:650px; margin-top:10px;">
		<legend>Dados dos Feriados</legend>
    
    	<table class="diaFeriadoGrid"></table>
      </fieldset>

    </div>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em; z-index:1;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Feriado < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Calendário de Feriados</legend>
        
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_bt();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
        <span class="bt_novos" title="Imprimir Feriados"><a href="imprimir_feriados.htm" target="_blank"><img src="../images/ico_impressora.gif" hspace="5" border="0"/>Imprimir Feriados</a></span>
        <br clear="all" />
        <br />


        <div id="feriados"></div>
        
        
        
        <div class="linha_separa_fields">&nbsp;</div>
      <div class="linha_separa_fields">&nbsp;</div>
       

        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup_bt();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>
        <span class="bt_novos" title="Imprimir Feriados"><a href="imprimir_feriados.htm" target="_blank"><img src="../images/ico_impressora.gif" hspace="5" border="0"/>Imprimir Feriados</a></span>
		<div class="linha_separa_fields">&nbsp;</div>
        
        
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="linha_separa_fields">&nbsp;</div>
      <div class="linha_separa_fields">&nbsp;</div>
    
    </div>
   
    
</div> 
<script>
	$(".diaFeriadoGrid").flexigrid({
			url : '../xml/diasFeriado-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Dia',
				name : 'dia',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Tipo Feriado',
				name : 'tipo',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'cidade',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Descrição',
				name : 'descricao',
				width : 180,
				sortable : true,
				align : 'left',
			}, {
				display : 'Opera',
				name : 'opera',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Cobrança',
				name : 'efetuaCobranca',
				width : 45,
				sortable : true,
				align : 'center'
			}, {
				display : 'Anual',
				name : 'repeteAnual',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			width : 650,
			height : 120
		});
</script>
</body>
</html>
