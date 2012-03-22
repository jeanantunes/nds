
<head>
<script language="javascript" type="text/javascript">
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
		$( "#datepickerDe" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#datepickerDe1" ).datepicker({
			showOn: "button",
			buttonImage: "../scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		
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

<div id="dialog-suplementar" title="Redistribuição">
<table width="555" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="226" valign="top">
    <fieldset>
    	<legend>Nota de Envio</legend>
        <table width="350" border="0" cellspacing="2" cellpadding="2">
      <tr>
        <td width="49" bgcolor="#F5F5F5"><strong>Código</strong></td>
        <td width="112" bgcolor="#F5F5F5"><strong>Produto</strong></td>
        <td width="61" bgcolor="#F5F5F5"><strong>Edição</strong></td>
        <td width="62" align="center" bgcolor="#F5F5F5"><strong>Reparte</strong></td>
        <td width="34" bgcolor="#F5F5F5">&nbsp;</td>
      </tr>
      <tr style="background:#F8F8F8;">
        <td>9999</td>
        <td>Veja</td>
        <td>2345</td>
        <td align="center">322</td>
        <td><a href="javascript:;" onclick="mostra_grid();"><img src="../images/ico_negociar.png" border="0" /></a></td>
      </tr>
      <tr>
        <td>9999</td>
        <td>Viajar</td>
        <td>2345</td>
        <td align="center">322</td>
        <td><a href="javascript:;"><img src="../images/ico_negociar.png" border="0" /></a></td>
      </tr>
    </table>
    
    </fieldset>
    
    
    </td>
    <td width="315" valign="top">
    <fieldset>
    <legend>Redistribuição</legend>
    
    <table border="0" cellspacing="1" cellpadding="1" style="display:none; width:350px;" id="grid_1">
      <tr>
        <td bgcolor="#F5F5F5"><strong>Cota</strong></td>
        <td bgcolor="#F5F5F5"><strong>Nome</strong></td>
        <td align="center" bgcolor="#F5F5F5"><strong>Quantidade</strong></td>
      </tr>
      <tr>
        <td><input type="text" name="textfield2" id="textfield2" style="width:60px;" /></td>
        <td><input type="text" name="textfield3" id="textfield3" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield4" id="textfield4" style="width:60px; text-align:center;" onblur="abre_linha_1();"/></td>
      </tr>
      <tr class="linha_1">
        <td><input type="text" name="textfield5" id="textfield5" style="width:60px;" /></td>
        <td><input type="text" name="textfield6" id="textfield6" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield7" id="textfield7" style="width:60px; text-align:center;" onblur="abre_linha_2();" /></td>
      </tr>
      <tr class="linha_2">
        <td><input type="text" name="textfield8" id="textfield8" style="width:60px;" /></td>
        <td><input type="text" name="textfield7" id="textfield9" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield10" id="textfield10" style="width:60px; text-align:center;" onblur="abre_linha_3();" /></td>
      </tr>
      <tr class="linha_3">
        <td><input type="text" name="textfield11" id="textfield11" style="width:60px;" /></td>
        <td><input type="text" name="textfield4" id="textfield12" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield13" id="textfield13" style="width:60px; text-align:center;" onblur="abre_linha_4();" /></td>
      </tr>
      <tr class="linha_4">
        <td><input type="text" name="textfield14" id="textfield14" style="width:60px;" /></td>
        <td><input type="text" name="textfield15" id="textfield15" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield16" id="textfield16" style="width:60px; text-align:center;" onblur="abre_linha_5();" /></td>
      </tr>
      <tr class="linha_5">
        <td><input type="text" name="textfield17" id="textfield17" style="width:60px;" /></td>
        <td><input type="text" name="textfield18" id="textfield18" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield19" id="textfield19" style="width:60px; text-align:center;" onblur="abre_linha_6();" /></td>
      </tr>
      <tr class="linha_6">
        <td><input type="text" name="textfield20" id="textfield20" style="width:60px;" /></td>
        <td><input type="text" name="textfield" id="textfield" style="width:180px;" /></td>
        <td align="center"><input type="text" name="textfield" id="textfield2" style="width:60px; text-align:center;" /></td>
      </tr>
    </table>
    
    </fieldset>
    </td>
  </tr>
</table>
    <!--<table class="lanctoFaltasSobras_1Grid"></table>
    <br />
    <table width="406" border="0" cellspacing="2" cellpadding="2">
  <tr>
    <td width="293"><strong>Total:</strong></td>
    <td width="99">999.999</td>
  </tr>
</table>-->
    
    
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
              <td colspan="3"><input type="text" name="datepickerDe" id="datepickerDe" style="width:80px;" /></td>
                <td width="38">Cota:</td>
                <td width="123"><input type="text" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar"><a href="javascript:;" onclick="popup_suplementar();">&nbsp;</a></span></td>
                <td width="40">Nome:</td>
                <td width="296"><input type="text" class="nome_jornaleiro" style="width:280px;"/></td>
                <td width="27">Box:</td>
                <td width="111"><input type="text" name="textfield" id="textfield" style="width:80px;"/></td>
              <td width="114"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Cotas Ausentes Cadastradas</legend>
        <div class="grids" style="display:none;">
       	  <table class="ausentesGrid"></table>
          <br />
          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

        </div>
        <span class="bt_novos" title="Novo"><a href="javascript:;" onclick="popup();"><img src="../images/ico_salvar.gif" hspace="5" border="0"/>Novo</a></span>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	$(".ausentesGrid").flexigrid({
			url : '../xml/ausentes-xml.xml',
			dataType : 'xml',
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
		});
		$(".lanctoFaltasSobras_1Grid").flexigrid({
			url : '../xml/lancto_faltas_sobras-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 240,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Quantidade',
				name : 'quantidade',
				width : 90,
				sortable : true,
				align : 'center'
			}],
			width : 575,
			height : 200
		});
</script>
</body>
