
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>

<script language="javascript" type="text/javascript">

function cliquePesquisar() {
	
	var dataAusencia = $('#idData').attr('value');
	var numcota = $('#idCota').attr('value');
	var nomeCota = $('#idNomeCota').attr('value');
	var box = $('#idBox').attr('value');
		
	$(".ausentesGrid").flexOptions({			
		url : '<c:url value="/cotaAusente/pesquisarCotasAusentes"/>',
		dataType : 'json',
		preProcess:processaRetornoPesquisa,
		params:[{name:'dataAusencia',value:dataAusencia},
		        {name:'numCota',value:numcota},
		        {name:'nomeCota',value:nomeCota},
		        {name:'box',value:box}]		
	});
	
	$(".ausentesGrid").flexReload();
}

function processaRetornoPesquisa(data) {
	alert("_" + data[0]+data[1]+data[2]);	
}

function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:'auto',
			width:540,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					popup_confirm();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	
	
	function popup_suplementar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-suplementar" ).dialog({
			resizable: false,
			height:450,
			width:800,
			modal: true,
			buttons: {
				"Suplementar": function() {
					$( this ).dialog( "close" );
					$("#effect").show("highlight", {}, 1000, callback);
					$(".grids").show();
					
				},
				"Redistribuir": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_confirm() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-confirm" ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Sim": function() {
					$( this ).dialog( "close" );
					$(".grids").show();
					
					
				},
				"Não": function() {
					$( this ).dialog( "close" );
					popup_suplementar();
				}
			}
		});
	};
	
function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:220,
			width:540,
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
					$(".grids").show();
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};	


$(function() {
		$( "#idData" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#idData" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
		$( "#idData" ).datepicker( "option", "dateFormat", "dd/mm/yy" );
		$("#idData").mask("99/99/9999");
		
	});		
$(function() {
		$( "#tabs-pop" ).tabs();
	});

</script>
<script>
function abre_linha_1(){
	$( '.linha_1' ).show();
	textfield5.focus();
}
function abre_linha_2(){
	$( '.linha_2' ).show();
	textfield8.focus();
}
function abre_linha_3(){
	$( '.linha_3' ).show();
	textfield11.focus();
}
function abre_linha_4(){
	$( '.linha_4' ).show();
	textfield11.focus();
}
function abre_linha_4(){
	$( '.linha_5' ).show();
	textfield17.focus();
}
function abre_linha_5(){
	$( '.linha_6' ).show();
	textfield20.focus();
}
function abre_linha_21(){
	$( '.linha_21' ).show();
	textfield24.focus();
}
function abre_linha_22(){
	$( '.linha_22' ).show();
	textfield27.focus();
}

function abre_linha_31(){
	$( '.linha_31' ).show();
	textfield34.focus();
}
function abre_linha_32(){
	$( '.linha_32' ).show();
	textfield37.focus();
}


function mostra_grid(){
	$( '#grid_1' ).show();
}


</script>
<style>
.linha_1, .linha_2, .linha_3, .linha_4, .linha_5, .linha_6, .linha_21, .linha_22, .linha_31, .linha_32 {display:none;}
#dialog-suplementar fieldset{width:350px!important;}
#dialog-suplementar .linha_separa_fields{width:350px!important;}

</style>
</head>

<body>


<div id="dialog-confirm" title="Suplementar">
	<p>Confirma Suplementar?</p>
</div>

<div id="dialog-excluir" title="Cota Ausente">
	<p>Confirma a exclusão desse Cota Ausente?</p>
</div>



<div id="dialog-novo" title="Incluir Cota Ausente"> 
    <table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td>Cota:</td>
              <td width="446" colspan="3"><input type="text" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar">&nbsp;</span><label style="margin-left:10px;">Nome:</label><input type="text" class="nome_jornaleiro" style="width:280px; margin-left:5px;"/></td>
            </tr>
          </table>
    </div>
   
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Ausente < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Cotas Ausentes</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="35">Data:</td>
              <td colspan="3">
<!--DATA-->
<input id="idData" type="text" name="datepickerDe" style="width:80px;" /></td>
              
                <td width="38">Cota:</td>
                <td width="123">
<!-- COTA -->                
<input id="idCota" type="text" style="width:80px; float:left; margin-right:5px;" 
	onchange="cota.limparCamposPesquisa('#idNomeCota');"
	onblur="cota.pesquisarPorNumeroCota('#idCota', '#idNomeCota');"/>

<!-- PESQUISAR NOME COTA -->
<span class="classPesquisar"><a href="javascript:;" onclick="cota.pesquisarPorNumeroCota('#idCota', '#idNomeCota');">&nbsp;</a></span></td>
                <td width="40">Nome:</td>
                <td width="296">
<!-- NOME -->            
<input id="idNomeCota" type="text" class="nome_jornaleiro" style="width:280px;" 
	onkeyup="cota.autoCompletarPorNome('#idNomeCota');" 
	onblur="cota.pesquisarPorNomeCota('#idCota', '#idNomeCota');"
	/>
				</td>
                <td width="27">Box:</td>
                <td width="111">
<!-- BOX -->
<input id="idBox" type="text" name="textfield" id="textfield" style="width:80px;"/></td>
              <td width="114"><span class="bt_pesquisar">
<!-- PESQUISAR -->
<a href="javascript:;" onclick="cliquePesquisar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Cotas Ausentes Cadastradas</legend>
        <div class="grids" style="display:none;">
       	  <table class="ausentesGrid"></table>
          <br />
          <span class="bt_novos" title="Gerar Arquivo">
<!-- ARQUIVO -->
<a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

	<span class="bt_novos" title="Imprimir">
<!-- IMPRIMIR -->	
<a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

        </div>
        <span class="bt_novos" title="Novo">
<!-- NOVO -->
<a href="javascript:;" onclick="popup();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 

<script type="text/javascript">
	
	$("#idCota").mask("?99999999999999999999", {placeholder:""});
	
	$(function() {	
		
		$(".ausentesGrid").flexigrid($.extend({},{
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 480,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor NE R$',
				name : 'vlrNE',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : true,
				align : 'center'
			}],
			sortname : "data",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		})); 	
		
		$(".grids").show();		
	});
	

</script>

<jsp:include page="modalRateio.jsp"/>
</body>
