<head>
<script language="javascript" type="text/javascript">
function popup_roteirizacao() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-roteirizacao" ).dialog({
			resizable: false,
			height:590,
			width:955,
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
	
function popup_cotas_ausentes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-cotas-disponiveis" ).dialog({
			resizable: false,
			height:490,
			width:870,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	


	function popup_alterar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:350,
			width:655,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
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
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	
	function popup_tranferir_cota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-transfere-cotas" ).dialog({
			resizable: false,
			height:220,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_tranferir_rota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-transfere-rota" ).dialog({
			resizable: false,
			height:300,
			width:400,
			modal: true,
			buttons: {
				"Confirmar": function() {
					$( this ).dialog( "close" );
					$("#effect").hide("highlight", {}, 1000, callback);
					
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}
		});	
		      
	};
	
	function popup_roteiro() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-roteiro" ).dialog({
			resizable: false,
			height:230,
			width:420,
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
	function popup_rota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-rota" ).dialog({
			resizable: false,
			height:210,
			width:420,
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

	
	function mostraCota(){
		$( '.dadosFiltro' ).show();
		$( '.grids' ).show();
		}
	
	$(function() {
		var availableTags = [
			"Comprador",
			"Entrega"
		];
		$( "#lstRoteiros" ).autocomplete({
			source: availableTags
		});
		$( "#lstRoteiros_1" ).autocomplete({
			source: availableTags
		});
		
	});
	
	$(function() {
		var availableTags = [
			"11-Santana 01",
			"12-Santana 02 ",
			"13-Santana 03 ",
			"14-Centro 01 "
		];
		$( "#lstRota" ).autocomplete({
			source: availableTags
		});
		$( "#lstRota_1" ).autocomplete({
			source: availableTags
		});
		
	});

	function roteiroNovo(){
		$('.roteiroNovo').show();
		}
	
	function esconde_dados(){
		$('.roteiroNovo').hide();
		}
		
</script>
<style>
label{ vertical-align:super;}
.linha_separa_fields{width:880px!important;}
#dialog-roteirizacao fieldset{margin-right:0px!important; margin-bottom:2px;}
#dialog-roteiro fieldset, #dialog-rota fieldset, #dialog-excluir fieldset, #dialog-transfere-cotas fieldset, #dialog-transfere-rota fieldset{width:350px!important;}
</style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">

<div id="dialog-transfere-rota" title="Transferir Rotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Rotas para:</legend>
        <p>Pesquise ou Digite o nome de um Novo Roteiro para estas Rotas.</p>
        <input name="lstRoteiros_1" type="text" id="lstRoteiros_1" style="width:300px; float:left; margin-bottom:5px;" />
        <a href="javascript:;" onclick="roteiroNovo();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <br clear="all" />
        <div class="roteiroNovo" style="display:none;">
        <a href="javascript:;" onclick="esconde_dados();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
        <br clear="all" />

        <label>Box:</label>
        <select name="select" id="select" style="width:230px;  float:left; margin-bottom:5px;">
            <option>Selecione...</option>
            <option>Box 202</option>
            <option>Box 204</option>
            <option>Box 400</option>
            <option>Box 509</option>
        </select>
        <br clear="all" />

        <label>Ordem:</label>
        <input name="" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
</div>
    </fieldset>
</div>

<div id="dialog-transfere-cotas" title="Transferir Cotas" style="display:none;">
	<fieldset>
    	<legend>Transferir Cotas para:</legend>
        <p>Pesquise ou Digite o nome de uma Nova Rota para estas Cotas.</p>
        <input name="lstRota_1" type="text" id="lstRota_1" style="width:300px; float:left; margin-bottom:5px;" />
        <a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
    </fieldset>
</div>

<div id="dialog-excluir" title="Cota / Rotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclusão destas Cotas/Rotas desta Rota?</p>
    </fieldset>
</div>

<div id="dialog-rota" title="Rota" style="display:none;">
	<fieldset>
    	<legend>Nova Rota</legend>
        <label>Ordem:</label>
        <input name="" type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="" type="text" style="width:200px; float:left;" />       
        <br clear="all" />
        

    </fieldset>
</div>



<div id="dialog-roteiro" title="Roteiro" style="display:none;">
	<fieldset>
    	<legend>Novo Roteiro</legend>
        <label>Box:</label>
        <select name="select" id="select" style="width:200px;  float:left; margin-bottom:5px;">
            <option>Selecione...</option>
            <option>Box 202</option>
            <option>Box 204</option>
            <option>Box 400</option>
            <option>Box 509</option>
        </select>
        <br clear="all" />

        <label>Ordem:</label>
        <input name="" type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="" type="text" style="width:200px; float:left;" />       
        <br clear="all" />
        

    </fieldset>
</div>

<div id="dialog-roteirizacao" title="Nova Roteirização" style="display:none;">
     <fieldset style="width:895px; float:left; margin-bottom:10px;">
		<legend>Roteiros</legend>
        <input name="lstRoteiros" type="text" id="lstRoteiros" style="width:240px; float:left;" />
	<span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_roteiro();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo Roteiro</a></span>
    <span style="float:right; margin-top:12px; margin-left:20px;"><strong>Roteiro Selecionado:</strong> Comprador - <strong>Box: </strong>230 - <strong>Ordem: </strong>2</span>
        
    </fieldset>
	<br clear="all" />
	<fieldset style="width:270px; float:left;">
		<legend>Rotas</legend>
        

        <input name="lstRota" type="text" id="lstRota" style="width:240px; float:left; margin-bottom:5px;" />
        <a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a> <br />
        <table class="rotasGrid"></table>
		<span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_rota();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Nova</a></span>
        <span class="bt_novos" title="Transferência de Roteiro"><a href="javascript:;" onclick="popup_tranferir_rota();"><img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a></span>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_excluir();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a></span>
    </fieldset>
    
    
    
    <fieldset style="width:596px; float:left; margin-left:5px; overflow:hidden;">
		<legend>Cotas da Rota</legend>
        <span style="float:left; margin-bottom:10px; margin-left:3px; margin-top:5px;"><strong>Roteiro Selecionado:</strong> Comprador - <strong>Box: </strong>230 - <strong>Ordem: </strong>2</span>
        
        
		<table class="cotasRotaGrid"></table>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_cotas_ausentes();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/>Adicionar</a></span>
        
         <span class="bt_novos" title="Transferência de Roteiro"><a href="javascript:;" onclick="popup_tranferir_cota();"><img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a></span>
         <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_excluir();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a></span>
	</fieldset>
    
    
    
    
	<div class="linha_separa_fields">&nbsp;</div>
</div>



<!--<div id="dialog-roteirizacao" title="Nova Roteirização" style="display:none;">
     <fieldset style="width:430px; float:left;">
		<legend>Roteiros</legend>
		<table class="roteirosGrid"></table>
        <span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_roteiro();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo Roteiro</a></span>
        
	</fieldset>

	<fieldset style="width:430px; float:left; margin-left:15px;">
		<legend>Rotas</legend>
        <table class="rotasGrid"></table>
		<span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="popup_rota();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Nova Rota</a></span>
    </fieldset>
    
    
    
    <fieldset style="width:895px; float:left; margin-top:5px;">
		<legend>Cotas da Rota</legend>
		<table class="cotasRotaGrid"></table>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="popup_cotas_ausentes();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Incluir Cota</a></span>
        
	</fieldset>
    
	<div class="linha_separa_fields">&nbsp;</div>
</div>-->

	<div id="dialog-cotas-disponiveis" title="Cotas Disponíveis" style="display:none;">
    
    <fieldset style="width:800px; float:left;">
		<legend>Pesquisar Cotas</legend>
		<table width="800" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="31">Tipo:</td>
              <td width="100"><select name="select" id="select" style="width:100px;">
                <option>Selecione...</option>
                <option>Cota</option>
              </select></td>
              <td width="42">Cota:</td>
              <td width="170"><input name="pesq_cota" type="text" id="pesq_cota" style="width:80px; float:left; margin-right:5px;" onkeypress="mostraCota();"/>
              <span class="classPesquisar"><a href="javascript:;" onclick="mostraCota();">&nbsp;</a></span></td>
              <td width="41">Nome:</td>
              <td colspan="4"><span class="dadosFiltro">CGB Distribuidora de Jorn e Rev</span></td>
            </tr>
            <tr>
              <td>UF:</td>
              <td><select name="select2" id="select2" style="width:100px;">
                <option selected="selected">Selecione...</option>
                <option>AL</option>
                <option>BA</option>
                <option>RJ</option>
                <option>SP</option>
              </select></td>
              <td>Munic.</td>
              <td><select name="select3" id="select3" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td>Bairro:</td>
              <td width="168"><select name="select4" id="select4" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td width="36">CEP:</td>
              <td width="87"><input name="pesq_cota2" type="text" id="pesq_cota2" style="width:80px;" /></td>
              <td width="79"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
          </table>
	</fieldset>
    
    <fieldset style="width:800px; float:left; margin-top:5px;">
		<legend>Cotas Disponíveis</legend>
		<table class="cotasDisponiveisGrid"></table>
        <span class="bt_sellAll" style="float:right; margin-right:25px;"><label for="sel">Selecionar Todos</label><input type="checkbox" name="Todos" id="sel" onclick="checkAll();" style="float:left;"/></span>
        
	</fieldset>
	<br clear="all" />
	</div>




<div class="corpo">
  <div class="header">
    <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Roteirização < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Rota / Roteiro</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="34">Box:</td>
              <td width="121">
               <select name="box" id="box" style="width: 100px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="box" items="${listaBox}">
							<option value="${box.key}">${box.value}</option>
						</c:forEach>
			  </select>
              </td>
                <td width="55">Roteiro:</td>
                <td width="277"><select name="select2" id="select2" style="width:200px;">
                  <option selected="selected">Selecione...</option>
                  <option>Entrega</option>
                  <option>Comprador</option>
                  <option>Leopoldina - Individ.</option>
                 
                </select></td>
                <td width="27">Rota:</td>
                <td width="296"><select name="select3" id="select3" style="width:190px;">
                  <option selected="selected">Selecione...</option>
                  <option>11-Santana 01</option>
                  <option>12-Santana 02</option>
                  <option>13-Santana 03</option>
                  <option>14-Centro 01</option>
                  <option>15-Centro 02</option>
                </select></td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrar();">Pesquisar</a></span></td>
            </tr>
            <tr>
              <td>Cota:</td>
              <td><input name="pesq_cota" type="text" id="pesq_cota" style="width:80px; float:left; margin-right:5px;" onkeypress="mostraCota();"/>
              <span class="classPesquisar"><a href="javascript:;" onclick="mostraCota();">&nbsp;</a></span></td>
              <td>Nome:</td>
              <td><span class="dadosFiltro">CGB Distribuidora de Jorn e Rev</span></td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Rotas / Roteiros Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<table class="rotaRoteirosGrid"></table>
        </div>
		
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
    <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
       <span class="bt_novos" title="Roteirização"><a href="javascript:;" onclick="popup_roteirizacao();"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0"/>Roteiriza&ccedil;&atilde;</a></span>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	$(".cotasDisponiveisGrid").flexigrid({
			url : '../xml/cotasDisponiveis-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Pto. Venda',
				name : 'ptoVenda',
				width : 95,
				sortable : true,
				align : 'left'
			}, {
				display : 'Orig. End',
				name : 'origemEndereco',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 270,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ordem',
				name : 'ordem',
				width : 55,
				sortable : true,
				align : 'center'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			width : 800,
			height : 200
		});
	

	$(".cotasRotaGrid").flexigrid({
			url : '../xml/cotasRotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : '',
				name : 'ordem',
				width : 15,
				sortable : true,
				align : 'center'
			},{
				display : 'Ordem',
				name : 'ordem',
				width : 35,
				sortable : true,
				align : 'left'
			}, {
				display : 'Pto. Venda',
				name : 'ptoVenda',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Orig.',
				name : 'origemEndereco',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Endereço',
				name : 'endereco',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 30,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 95,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ordernar',
				name : 'ordenar',
				width : 50,
				sortable : true,
				align : 'right'
			}],
			sortname : "ordem",
			width : 590,
			height : 284
		});
	
	
	
	$(".rotasGrid").flexigrid({
			url : '../xml/rotas-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : '',
				name : 'sel',
				width : 15,
				sortable : true,
				align : 'center'
			},{
				display : 'Ordem',
				name : 'ordem',
				width : 35,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'detalhes',
				width : 15,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			width : 270,
			height : 280
		});


	$(".roteirosGrid").flexigrid({
			url : '../xml/roteiros-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cód.',
				name : 'codigo',
				width : 35,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 250,
				sortable : true,
				align : 'left'
			}, {
				display : 'Box',
				name : 'box',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'detalhes',
				width : 15,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			width : 430,
			height : 130
		});


	$(".rotaRoteirosGrid").flexigrid({
			url : '../xml/rota_roteiros-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Box',
				name : 'box',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Roteiro',
				name : 'roteiro',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Rota',
				name : 'rota',
				width : 180,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cota',
				name : 'cota',
				width : 60,
				sortable : true,
				align : 'left',
			}, {
				display : 'Nome',
				name : 'nome',
				width : 360,
				sortable : true,
				align : 'left'
			}],
			sortname : "box",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 255
		});
</script>
</form>

</body>
