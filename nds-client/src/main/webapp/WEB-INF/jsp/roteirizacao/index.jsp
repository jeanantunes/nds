<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/roteirizacao.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script language="javascript" type="text/javascript">

$(function() {
		
		inicializar();
	});
	
function inicializar() {
	iniciarGrid();
	roteirizacao.iniciaRotasGrid();
	roteirizacao.iniciaCotasRotaGrid();
}

function iniciarGrid() {
		
		
		$(".rotaRoteirosGrid").flexigrid({
		preProcess: executarPreProcessamento,
			dataType : 'json',
			colModel : [ {
				display : 'Box',
				name : 'id',
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
			height : 255,
			singleSelect : true
		});
	}

	function executarPreProcessamento(data) {
		
		if (data.mensagens) {

			exibirMensagem(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return data;
		}
		$.each(data.rows, function(index, value) {
        	value.cell.roteiro = value.cell.roteiro.descricaoRoteiro;
			value.cell.rota = value.cell.rota.descricaoRota;
		});
		
		$(".grids").show();
		
		return data;
	}
function pesquisar() {
		
		$(".rotaRoteirosGrid").flexOptions({
			"url" : contextPath + '/cadastro/roteirizacao/pesquisar',
			params : [{
				name : "idBox",
				value : 0
			}, {
				name : "idRoteiro",
				value : 0
			}, {
				name : "idRota",
				value : 0
			}],
			newp:1
		});
		
		$(".rotaRoteirosGrid").flexReload();
	};
	
	
	

function popup_roteirizacao() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	    roteirizacao.limparTelaRoteirizacao();
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
        <input name="roteiroTranferenciaNome" type="text" id="roteiroTranferenciaNome" onkeyup="roteirizacao.autoCompletarRoteiroPorNome('#roteiroTranferenciaNome',roteirizacao.selecionaRoteiroTranferencia)" style="width:300px; float:left; margin-bottom:5px;" />
        <input name="roteiroTranferenciaSelecionadoId" type="hidden" id="roteiroTranferenciaSelecionado"  />
        <input name="roteiroTranferenciaSelecionadoNome" type="hidden" id="roteiroTranferenciaSelecionadoNome"  />
        <a href="javascript:;" onclick="roteirizacao.exibiRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a>
        <br clear="all" />
        <div class="roteiroNovo" style="display:none;">
        <a href="javascript:;" onclick="roteirizacao.escondeRoteiroNovoTranferencia();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Fechar" border="0" align="right" /></a>
        <br clear="all" />
        <label>Box:</label>
        <select name="boxRoteiroTranferencia" id="boxRoteiroTranferencia" style="width:230px;  float:left; margin-bottom:5px;">
            <option>Selecione...</option>
           <c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
        </select>
        <br clear="all" />

        <label>Ordem:</label>
        <input name="ordemRoteiroTranferencia" id="ordemRoteiroTranferencia" type="text" style="width:225px; float:left; margin-bottom:5px;" />    
        <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiroTranferencia" value="Especial" id="tipoRoteiroTranferencia"  />
        <br clear="all" />  
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

<div id="dialog-excluir-rotas" title="Rotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclusão destas Rotas deste Roteiro</p>
    </fieldset>
</div>

<div id="dialog-excluir-cotas" title="Cotas" style="display:none;">
	<fieldset>
    	<legend>Excluir</legend>
        <p>Confirma a exclusão destas Cotas desta Rota?</p>
    </fieldset>
</div>

<div id="dialog-rota" title="Rota" style="display:none;">
	<fieldset>
    	<legend>Nova Rota</legend>
        <label>Ordem:</label>
        <input name="ordemRotaInclusao"  id="ordemRotaInclusao"  type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeRotaInclusao" id="nomeRotaInclusao" type="text" style="width:200px; float:left;" />       
        <br clear="all" />
        

    </fieldset>
</div>



<div id="dialog-roteiro" title="Roteiro" style="display:none;">
<jsp:include page="../messagesDialog.jsp" /> 
	<fieldset>
    	<legend>Novo Roteiro</legend>
        <label>Box:</label>
        <select name="boxInclusaoRoteiro" id="boxInclusaoRoteiro" style="width:200px;  float:left; margin-bottom:5px;">
			<option value="" selected="selected">Selecione...</option>
			<c:forEach var="box" items="${listaBox}">
				<option value="${box.key}">${box.value}</option>
			</c:forEach>
 		</select>
        
        <br clear="all" />
        <label>Ordem:</label>
        <input name="ordemInclusaoRoteiro" id="ordemInclusaoRoteiro" type="text" style="width:200px; float:left; margin-bottom:5px;" />       
        <br clear="all" />
        
        <label>Nome:</label>
        <input name="nomeInclusaoRoteiro" id="nomeInclusaoRoteiro"  type="text" style="width:200px; float:left;" />
         <br clear="all" />
        <label>Roteiro Especial:</label>
        <input type="checkbox" name="tipoRoteiro" value="Especial" id="tipoRoteiro"  />        
        <br clear="all" />
        

    </fieldset>
</div>

<div id="dialog-roteirizacao" title="Nova Roteiriza&ccedil;&atilde;o" style="display:none;">
	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="dialogRoteirizacao" name="messageDialog"/>
	</jsp:include> 
     <fieldset style="width:895px; float:left; margin-bottom:10px;">
		<legend>Roteiros</legend>
        <input name="lstRoteiros" type="text" id="lstRoteiros" style="width:240px; float:left;"  onkeyup="roteirizacao.autoCompletarRoteiroPorNome('#lstRoteiros',roteirizacao.populaDadosRoteiro)" onblur="roteirizacao.buscaRoteiroPorNome('#lstRoteiros')" />
	<span class="bt_novos" title="Nova Roteiro"><a href="javascript:;" onclick="roteirizacao.abrirTelaRoteiro();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Novo Roteiro</a></span>
    <span style="float:right; margin-top:12px; margin-left:20px;" id="spanDadosRoteiro"><strong>Roteiro Selecionado:</strong>&nbsp;&nbsp; <strong>Box: </strong>&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;</span>
   <input type="hidden" id="idRoteiroSelecionado" name="idRoteiroSelecionado"  >   
    </fieldset>
	<br clear="all" />
	<fieldset style="width:270px; float:left;">
		<legend>Rotas</legend>
        

        <input name="nomeRota" type="text" id="nomeRota" style="width:240px; float:left; margin-bottom:5px;" onkeyup="roteirizacao.filtroGridRotasPorNome()"  />
        
        
        <a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Adicionar Rota" width="16" height="16" border="0" style="float:left; margin-left:5px; margin-top:5px;" /></a> <br />
        <table class="rotasGrid"></table>
		<span class="bt_novos" title="Nova Rota"><a href="javascript:;" onclick="roteirizacao.abrirTelaRota();"><img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0"/>Nova</a></span>
        <span class="bt_novos" title="Transferência de Roteiro"><a href="javascript:;" onclick="roteirizacao.popupTransferirRota();"><img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0"/>Transferir</a></span>
        <span class="bt_novos" title="Excluir Rota"><a href="javascript:;" onclick="roteirizacao.popupExcluirRotas();"><img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0"/>Excluir</a></span>
    </fieldset>
    
    
    
    <fieldset style="width:596px; float:left; margin-left:3px; overflow:hidden;">
		<legend>Cotas da Rota</legend>
		
        <span style="float:left; margin-bottom:10px; margin-left:3px; margin-top:5px;" id="spanDadosRota"><strong>Rota Selecionada:</strong>&nbsp;&nbsp;&nbsp;&nbsp; <strong>Ordem: </strong>&nbsp;</span>
        <input name="rotaSelecionada" type="hidden"  id="rotaSelecionada" style="width:240px; float:left; margin-bottom:5px;"  />
        
		<table class="cotasRotaGrid"></table>
        <span class="bt_novos" title="Cotas Ausentes"><a href="javascript:;" onclick="roteirizacao.abrirTelaCotas();"><img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0"/>Adicionar</a></span>
        
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
              <td width="170"><input name="numeroCotaPesquisa" type="text" id="numeroCotaPesquisa" style="width:80px; float:left; margin-right:5px;" onkeypress="mostraCota();"/>
              <span class="classPesquisar"><a href="javascript:;" onclick="roteirizacao.buscarPvsPorCota();">&nbsp;</a></span></td>
              <td width="41">Nome:</td>
              <td colspan="4"><span class="dadosFiltro">CGB Distribuidora de Jorn e Rev</span></td>
            </tr>
            <tr>
              <td>UF:</td>
              <td><select name="comboUf" id="comboUf" onchange="roteirizacao.buscalistaMunicipio()" style="width:100px;">
                
              </select></td>
              <td>Munic.</td>
              <td><select name="comboMunicipio" id="comboMunicipio" onchange="roteirizacao.buscalistaBairro()" style="width:150px;">
                <option>Todos</option>
              </select></td>
              <td>Bairro:</td>
              <td width="168"><select name="comboBairro" id="comboBairro" style="width:150px;">
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
                <td width="277">
                	<select name="roteiro" id="roteiro" style="width: 200px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="roteiro" items="${listaRoteiro}">
							<option value="${roteiro.key}">${roteiro.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
                <td width="27">Rota:</td>
                <td width="296">
					<select name="rota" id="rota" style="width: 190px;">
						<option value="" selected="selected">Selecione...</option>
						<c:forEach var="rota" items="${listaRota}">
							<option value="${rota.key}">${rota.value}</option>
						</c:forEach>
				    </select>
                 
                </td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>
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
       <span class="bt_novos" title="Roteiriza&ccedil;&atilde;o"><a href="javascript:;" onclick="popup_roteirizacao();"><img src="${pageContext.request.contextPath}/images/bt_expedicao.png" hspace="5" border="0"/>Roteiriza&ccedil;&atilde;o</a></span>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div> 
<script>
	
	

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
