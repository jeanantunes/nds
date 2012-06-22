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
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.tabs.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.datepicker.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/jquery-ui-1.8.16.custom/development-bundle/ui/jquery.ui.autocomplete.js"></script>
<script language="javascript" type="text/javascript" src="../scripts/flexigrid-1.1/js/flexigrid.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../scripts/flexigrid-1.1/css/flexigrid.pack.css" />
<script language="javascript" type="text/javascript">
	function gridCota(){
		$('.pesqCota').show();
		$('.pesqTodos').hide();
		$('.dados').show();
	}
	
	function gridTotal(){
		$('.pesqCota').hide();
		$('.pesqTodos').show();
		$('.dados').hide();
	}
	
	function pesquisar(){
		
		var cota = $('#codigoCota').val();
		
		if(cota == null){
			
			$(".consignadosCotaGrid").flexOptions({
				url: "<c:url value='/financeiro/consultaConsignadoCota/pesquisarConsignadoCota'/>",
				dataType : 'json',
				params: [
							{name:'filtro.idCota', value:$('#codigoCota').val()},
							{name:'filtro.idFornecedor', value:$('#idFornecedor').val()}
							]
			});
			
			$(".consignadosCotaGrid").flexReload();
			gridCota();			
		}else{
			
			$(".consignadosGrid").flexOptions({
				url: "<c:url value='/financeiro/consultaConsignadoCota/pesquisarMovimentoCotaPeloFornecedor'/>",
				dataType : 'json',
				params: [
							{name:'filtro.idCota', value:$('#codigoCota').val()},
							{name:'filtro.idFornecedor', value:$('#idFornecedor').val()}
							]
			});
			
			$(".consignadosGrid").flexReload();
			gridTotal();	
			
		}
	}
	
	function mostra_detalhes() {
	
		$( "#dialog-detalhes" ).dialog({
			resizable: false,
			height:370,
			width:860,
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
	
	function executarPreProcessamento(resultado) {
		
		if (resultado.mensagens) {

			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			
			$(".grids").hide();

			return resultado;
		}
		
		//$(".grids").show();
		
		return resultado;
	}
	
	function pesquisarCota() {
 		
		numeroCota = $("#codigoCota").val();
 		
 		$.postJSON(
			'<c:url value="/financeiro/consultaConsignadoCota/buscarCotaPorNumero" />',
			{ "numeroCota": numeroCota },
			function(result) {
				$("#nomeCota").html(result);				
			},
			null,
			true
		);
 	}
	
	function detalharTodos(opcao) {
		var detalhar = document.getElementById("detalhes");
		
		switch (opcao) {   
			case '-1':   
				detalhar.style.display = ""; 
			  
			break;		
			default:   
				detalhar.style.display = "none";			
			break;   
		}   
	}
	
</script>
<style type="text/css">

#detalhes input{float:left;}
#detalhes label, #dialog-detalhes label{width:auto !important; line-height:30px ; margin-bottom:0px!important;}
#dialog-detalhes fieldset{width:800px!important;}
</style>
</head>

<body>

<div id="dialog-detalhes" title="Detalhes" style="display:none;">
	<fieldset>
    	<legend>Dados da Cota: 4444 - Alberto José da Silva</legend>
        
		<table class="consignadosCotaDetalhesGrid"></table>
	</fieldset>
</div>

<div class="corpo">  
    <br clear="all"/>
    <br />
   
    <div class="container">	
     <fieldset class="classFieldset">
   	    <legend>Pesquisar Consignados Cota
        </legend><table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
  <tr>
    <td width="30">Cota:</td>
    <td width="96"><input type="text" name="codigoCota" id="codigoCota" style="width:60px; float:left; margin-right:5px;" onblur="pesquisarCota();" />
      <span class="classPesquisar"><a href="javascript:;" onclick="gridCota();">&nbsp;</a></span></td>
    <td width="39">Nome:</td>    
    <td width="245"><span name="nomeCota" id="nomeCota"></span></td>
    <td width="67">Fornecedor:</td>
    <td width="159">    	
    	<select id="idFornecedor" name="idFornecedor" style="width:200px;" onchange="detalharTodos(this.value);">
		    <option value="0" selected="selected">Selecione</option>
		    <option value="-1">Todos</option>
		    <c:forEach items="${listaFornecedores}" var="fornecedor">
		      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
		    </c:forEach>
		</select>
    </td>
    <td width="169">
	    <div id="detalhes" style="display:none;">
	    <label><input name="" type="checkbox" value="" />Detalhar</label></div>
	</td>
    <td width="104"><span class="bt_pesquisar"><a href="javascript:;"  onclick="pesquisar();">Pesquisar</a></span></td>
  </tr>
  </table>
      </fieldset>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
      	<div class="pesqCota" style="display:none;">
       	  <legend>Consignados da Cota: 4444 - Alberto José da Silva</legend>
        <div class="grids">
       	  <table class="consignadosCotaGrid"></table>
			<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
			<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
          <table width="190" border="0" cellspacing="1" cellpadding="1" align="right">
              <tr>
                <td width="71"><strong>Total:</strong></td>
                <td width="49"><strong>FC</strong></td>
                <td width="60" align="right"><strong>936,00</strong></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td><strong>DGB</strong></td>
                <td align="right"><strong>785,20</strong></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td><strong>GB</strong></td>
                <td align="right"><strong>1.100,00</strong></td>
              </tr>
              <tr>
                <td style="border-top:1px solid #000;"><strong>Total Geral:</strong></td>
                <td style="border-top:1px solid #000;">&nbsp;</td>
                <td style="border-top:1px solid #000;" align="right"><strong>2.821,20</strong></td>
              </tr>
            </table>
         </div>
         </div>
         <div class="pesqTodos" style="display:none;">
       	  <legend>Consignados</legend>
        <div class="grids" style="display:noneA;">
       	  <table class="consignadosGrid"></table>
         </div>
          <br />
         
          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="../images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
          <table width="190" border="0" cellspacing="1" cellpadding="1" align="right">
              <tr>
                <td width="71"><strong>Total:</strong></td>
                <td width="49"><strong>FC</strong></td>
                <td width="60" align="right"><strong>936,00</strong></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td><strong>DGB</strong></td>
                <td align="right"><strong>785,20</strong></td>
              </tr>
              <tr>
                <td>&nbsp;</td>
                <td><strong>GB</strong></td>
                <td align="right"><strong>1.100,00</strong></td>
              </tr>
              <tr>
                <td style="border-top:1px solid #000;"><strong>Total Geral:</strong></td>
                <td style="border-top:1px solid #000;">&nbsp;</td>
                <td style="border-top:1px solid #000;" align="right"><strong>2.821,20</strong></td>
              </tr>
            </table>

 </div>


      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      
    </div>
</div> 
<script>
	$(".consignadosCotaDetalhesGrid").flexigrid({
			url : '../xml/consignado-cota-detalhes-xml.xml',
			dataType : 'xml',
			colModel : [  {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 110,
				sortable : true,
				align : 'left'
			}, {
				display : 'Dt. Lancto',
				name : 'dtLancto',
				width : 70,
				sortable : true,
				align : 'center'
			},{
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desc R$',
				name : 'precoDesc',
				width : 70,
				sortable : true,
				align : 'right'
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total $',
				name : 'total',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. $',
				name : 'totalDesc',
				width : 60,
				sortable : true,
				align : 'right'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 800,
			height : 180
		});
	
	$(".consignadosCotaGrid").flexigrid({
		preProcess: executarPreProcessamento,
		dataType : 'json',
			colModel : [  {
				display : 'Código',
				name : 'codigoProduto',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 135,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numeroEdicao',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Dt. Lancto',
				name : 'dataLancamento',
				width : 80,
				sortable : true,
				align : 'center'
			},{
				display : 'Preço Capa R$',
				name : 'precoCapa',
				width : 80,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço Desc R$',
				name : 'precoDesconto',
				width : 80,
				sortable : true,
				align : 'right'
			},{
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total $',
				name : 'total',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. $',
				name : 'totalDesconto',
				width : 70,
				sortable : true,
				align : 'right'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});

$(".consignadosGrid").flexigrid({
		preProcess: executarPreProcessamento,
		dataType : 'json',
			colModel : [  {
				display : 'Cota',
				name : 'numeroCota',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nomeCota',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte Total',
				name : 'reparte',
				width : 140,
				sortable : true,
				align : 'center'
			}, {
				display : 'Total R$',
				name : 'total',
				width : 120,
				sortable : true,
				align : 'right'
			}, {
				display : 'Total Desc. R$',
				name : 'totalDesconto',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 40,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
</script>
</body>
</html>
