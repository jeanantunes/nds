<head>
<script language="javascript" type="text/javascript">

/**
 * VERIFICA A EXISTENCIA DE UMA NOTAFISCAL
 * COM OS PARÂMETROS DE PESQUISA
 */
function verificarContaCorrenteCotaExistente() {

	var cota = $("#cota").val();
	
	var dadosPesquisa = 
		"numeroCota=" 			+ cota;			
	
	//limparCampos();
	
	$.postJSON("<c:url value='/financeiro/contaCorrenteCota/verificarContaCorrenteCotaExistente'/>", 
				   dadosPesquisa,
				   confirmaContaCorrenteCotaEncontrada);

}



/**
 * SELECIONA o NOME A PARTIR DA COTA DIGITADO.
 */
function pesquisarPorCotaNome() {
		
	var cota = $("#cota").val();	
	
	if(cota == "") {
		$("#nomeCota").val("");
		return;
	}
	
	$.postJSON("<c:url value='/financeiro/contaCorrenteCota/buscarCota'/>", "numeroCota=" + cota, 
	function(result) {
		$("#nomeCota").val(result.nome);
	});	

}

/**
 * SE A CONTA CORRENTE FOR ENCONTRADA, SERAO PESQUISADOS OS ITEM RELATIVOS A MESMA
 * E POPULADA A GRID
 */
function confirmaContaCorrenteCotaEncontrada(result) {
	
	//jsonResposta = result;
	
	//if(result.tipoMensagem == "SUCCESS") {

		//exibirMensagem(result.tipoMensagem, result.listaMensagens);
		
		pesquisarItemContaCorrenteCota();

	//} else {
		
	//	$(".grids").hide();
		
	//} vai precis
	
}

/**
 * FAZ A PESQUISA DOS ITENS REFERENTES A CONTA CORRENTE COTA.
 */
function pesquisarItemContaCorrenteCota() {
		
	$(".itemContaCorrenteCotaGrid").flexOptions({
		
		url: '<c:url value="/"/>financeiro/contaCorrenteCota/itemContaCorrenteCota',
		preProcess: getDataFromResult,
		dataType : 'json'
	});

	$(".itemContaCorrenteCotaGrid").flexReload();
	
}

/**
 * PREPARA OS DADOS A SEREM APRESENTADOS NA GRID.
 */
function getDataFromResult(data) {
		
	$.each(data.rows, function(index, value) {
		
		var consignado = value.cell[3];
		var encalhe = value.cell[4];
		var vendaEncalhe = value.cell[5];
		var debCred = value.cell[6];
		var encargos = value.cell[7];
		
		//var alteracaoPermitida = value.cell[8];
		
		var lineId = value.id;
		
		var hiddeFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
		
		
		//if(alteracaoPermitida == "S") {
			
			value.cell[3] = '<a href="#"/>'+consignado+'</a>'+hiddeFields;
			value.cell[4] = '<a href="#"/>'+encalhe+'</a>'+hiddeFields;
			value.cell[5] = '<a href="#"/>'+vendaEncalhe+'</a>'+hiddeFields;
			value.cell[6] = '<a href="#"/>'+debCred+'</a>'+hiddeFields;
			value.cell[7] = '<a href="#"/>'+encargos+'</a>'+hiddeFields;
			
		
	});

	//data = destacarValorNegativo(data);
	
	
	$(".grids").show();
	
	return data;

}

/*
 * O JAVASCRIPT ABAIXO E O PRIMEIRO A SER EXECUTADO 
 * QUANDO A PAGINA CARREGA.
 */
$(function() {
	
	carregarItemContaCorrenteCotaGrid();
	
});

/**
 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
 */
function carregarItemContaCorrenteCotaGrid() {
	
	$(".itemContaCorrenteCotaGrid").flexigrid({
		
			preProcess: getDataFromResult,
			dataType : 'json',
			colModel : [
		{
			display : 'Data',
			name : 'data',
			width : 60,
			sortable : false,
			align : 'center'
		}, {
			display : 'Vlr.Postergado',
			name : 'valorPostergado',
			width : 70,
			sortable : false,
			align : 'left'
		}, {
			display : 'NA',
			name : 'NA',
			width : 90,
			sortable : false,
			align : 'center'
		}, {
			display : 'Consignado',
			name : 'precoCapa',
			width : 70,
			sortable : false,
			align : 'center'
		}, {
			display : 'Encalhe',
			name : 'encalhe',
			width : 90,
			sortable : false,
			align : 'center'
		}, {
			display : 'Venda Encalhe',
			name : 'vendaEncalhe',
			width : 90,
			sortable : false,
			align : 'center'
		}, {
			
			display : 'Déb/Cred.',
			name : 'debCred',
			width : 90,
			sortable : false,
			align : 'center'
			
		}, {
			display : 'Encargos',
			name : 'encargos',
			width : 90,
			sortable : false,
			align : 'center'
			
		}, {
			
			display : 'Pendente',
			name : 'pendente',
			width : 90,
			sortable : false,
			align : 'center'
			
		},{
			
			display : 'Total R$',
			name : 'total',
			width : 90,
			sortable : true,
			align : 'center'
		}],
		
	
		showTableToggleBtn : true,
		width : 960,
		height : 180
	});
}














function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:490,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				},
				
			}
		});
	};
	
function popup_encalhe() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encalhe" ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};

function popup_encalhe_2() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encalhe_2" ).dialog({
			resizable: false,
			height:460,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};

		
function popup_contaCorrente() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-conta" ).dialog({
			resizable: false,
			height:340,
			width:660,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};	
function popup_encargos() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-encargos" ).dialog({
			resizable: false,
			height:'auto',
			width:450,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				}
			}
		});
	};	
</script>
<style type="text/css">
  fieldset { width:auto!important; }
  </style>
</head>

<body>

<div class="corpo">
 
     <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Pessoa < evento > com < status >.</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Conta-Corrente</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="32">Cota:</td>
              <td colspan="3"><input type="text" name="cota" id="cota" style="width:80px; float:left; margin-right:5px;"/><span class="classPesquisar">
              		<a href="javascript:;" onclick="pesquisarPorCotaNome();">&nbsp;</a></span>
              </td>
              <td width="36">Nome:</td>
              <td width="263"><input type="text" name="nomeCota" id="nomeCota" style="width:230px;"/></td>
              <td width="72">&nbsp;</td>
              <td width="283">&nbsp;</td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="verificarContaCorrenteCotaExistente();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Conta-Corrente Selecionado</legend>
       
       <div class="grids" style="display: none;">
          <strong> ------------ </strong>
          <br />

       	  <table class="itemContaCorrenteCotaGrid"></table>
        
                	<span class="bt_novos" title="Negociar Divida"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_negociar.png" hspace="5" border="0" />Negociar Divida</a></span>

                    
                    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
</div>
            
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       
    </div>
</div> 
<script>

</script>
</body>
</html>
