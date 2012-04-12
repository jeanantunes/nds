<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/cota.js"></script>
<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

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
 * FAZ A PESQUISA DOS ITENS REFERENTES A CONTA CORRENTE COTA.
 */
function pesquisarItemContaCorrenteCota() {
	
	var cota = $("#cota").val();

	var parametroPesquisa = [{name:'filtroViewContaCorrenteCotaDTO.numeroCota', value:cota }];

	$(".itemContaCorrenteCotaGrid").flexOptions({
		
		url : '<c:url value="/financeiro/contaCorrenteCota/consultarContaCorrenteCota" />', params: parametroPesquisa
				
	});

	$(".itemContaCorrenteCotaGrid").flexReload();
	
}

/**
 * FAZ A PESQUISA DE ENCALHE DA COTA EM UMA DETERMINADA DATA
 */
function pesquisarEncalheCota(lineId){
		
	var numeroCota = $("#cota").val();		
	
	var parametroPesquisa = [{name:'filtroConsolidadoEncalheDTO.numeroCota', value:numeroCota },
	                         {name:'filtroConsolidadoEncalheDTO.lineId', value:lineId }];
	
	carregarEncalheCotaGrid();
	
	$(".encalheCotaGrid").flexOptions({
		
		url : '<c:url value="/financeiro/contaCorrenteCota/consultarEncalheCota" />', params: parametroPesquisa
				
	});
	
	//$("#datacotanome").html($dataescolhida+" Cota: "+$("#cota").val()+" - "+$("#nomeCota").val());
	
	$(".encalheCotaGrid").flexReload();
	popup_encalhe();
	
	
	
	
	
}

/**
 * PREPARA OS DADOS A SEREM APRESENTADOS NA GRID.
 */
function getDataFromResult(data) {
	
	if(typeof data.mensagens == "object") {
		
		$(".grids").hide();
	
		exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
		
	}else{
		
		$.each(data.rows, function(index, value) {
		
		var consignado = value.cell[3];
		var encalhe = value.cell[4];
		var vendaEncalhe = value.cell[5];
		var debCred = value.cell[6];
		var encargos = value.cell[7];
				
		var lineId = value.id;
		
		var hiddeFields = '<input type="hidden" name="lineId" value="'+lineId+'"/>';
		
			value.cell[3] = '<a href="#"/>'+consignado+'</a>'+hiddeFields;
			value.cell[4] = '<a href="javascript:;" onclick="pesquisarEncalheCota('+[lineId]+');"/>'+encalhe+'</a>'+hiddeFields;
			value.cell[5] = '<a href="#"/>'+vendaEncalhe+'</a>'+hiddeFields;
			value.cell[6] = '<a href="#"/>'+debCred+'</a>'+hiddeFields;
			value.cell[7] = '<a href="#"/>'+encargos+'</a>'+hiddeFields;
					
		});
		
	
		$("#cotanome").html($("#cota").val()+" "+$("#nomeCota").val());
		$(".grids").show();
		
		return data;
	}	

}

function getDataFromResult2(data) {
	
	if(typeof data.mensagens == "object") {
		
		$(".gridsEncalhe").hide();
	
		exibirMensagem(data.mensagens.tipoMensagem, data.mensagens.listaMensagens);
		
	}else{
		
		$(".gridsEncalhe").show();
		
		return data;
	}	

}

/*
 * O JAVASCRIPT ABAIXO E O PRIMEIRO A SER EXECUTADO 
 * QUANDO A PAGINA CARREGA.
 */
$(function() {
	
	$("#cota").numeric();
	
	$("#nomeCota").autocomplete({source: ""});
	
	carregarItemContaCorrenteCotaGrid();	
	
});

/**
 * ESTRUTURA DE COLUNAS DA GRID DE RESULTADO.
 */
function carregarItemContaCorrenteCotaGrid() {
	
	$(".itemContaCorrenteCotaGrid").flexigrid({
		preProcess: getDataFromResult,
		dataType : 'json',
		colModel : [ {
			display : 'Data',
			name : 'data',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Vlr. Postergado',
			name : 'vlrpostergado',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'NA',
			name : 'na',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Consignado',
			name : 'consignadoaVencer',
			width : 90,
			sortable : true,
			align : 'right'
		}, {
			display : 'Encalhe',
			name : 'encalhe	',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Venda Encalhe',
			name : 'vendaEncalhe',
			width : 90,
			sortable : true,
			align : 'right',
		}, {
			display : 'Déb/Cred.',
			name : 'debCred',
			width : 80,
			sortable : true,
			align : 'right'
		}, {
			display : 'Encargos',
			name : 'encargos',
			width : 80,
			sortable : true,
			align : 'right'
		}, {
			display : 'Pendente',
			name : 'pendente',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 105,
			sortable : true,
			align : 'right'
		}],
		sortname : "data",
		sortorder : "desc",
		usepager : true,
		useRp : true,
		rp : 15,
		showTableToggleBtn : true,
		width : 960,
		height : 255
	});
}

/**
 * ESTRUTURA DE COLUNAS DA GRID ENCALHE COTA.
 */
function carregarEncalheCotaGrid() {
	
	$(".encalheCotaGrid").flexigrid({
		preProcess: getDataFromResult2,
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigoProduto',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Produto',
			name : 'nomeProduto',
			width : 130,
			sortable : true,
			align : 'right'
		}, {
			display : 'Edição',
			name : 'numeroEdicao',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Preço Capa R$',
			name : 'precoVenda',
			width : 95,
			sortable : true,
			align : 'right'
		}, {
			display : 'Preço c/ Desc. R$',
			name : 'precoComDesconto',
			width : 70,
			sortable : true,
			align : 'right'
		}, {
			display : 'Encalhe',
			name : 'encalhe',
			width : 70,
			sortable : true,
			align : 'right',
		}, {
			display : 'Fornecedor',
			name : 'nomeFornecedor',
			width : 100,
			sortable : false,
			align : 'right'
		}, {
			display : 'Total R$',
			name : 'total',
			width : 75,
			sortable : true,
			align : 'right'
		}],
		sortname : "Nome",
		sortorder : "asc",
		width : 800,
		height : 200

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
					
					$(".gridsEncalhe").show();
					
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

<div id="dialog-encalhe" title="Encalhe da Cota">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
    	 <strong><span id="datacotanome"></span></strong>
        
        <div class="gridsEncalhe" style="display: none;">      
        
	        <table class="encalheCotaGrid"></table>
	        
	        <span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarEncalhe?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarEncalhe?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir
				</a>
			</span>       	
	    </div>   
    </fieldset>

</div>

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
              <td colspan="3"><input type="text" name="filtroViewContaCorrenteCota.numeroCota" id="cota" onchange="cota.pesquisarPorNumeroCota('#cota', '#nomeCota');" style="width:80px; float:left; margin-right:5px;"/>
              </td>
              <td width="36">Nome:</td>
              <td width="263"><input type="text" name="nomeCota" id="nomeCota" onkeyup="cota.autoCompletarPorNome('#nomeCota');" onblur="cota.pesquisarPorNomeCota('#cota', '#nomeCota');" style="width:230px;"/></td>
              <td width="72">&nbsp;</td>
              <td width="283">&nbsp;</td>
              <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisarItemContaCorrenteCota();">Pesquisar</a></span></td>
            </tr>
          </table>

      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Conta-Corrente Selecionado</legend>
       
       <div class="grids" style="display: none;">
      
          <strong><span id="cotanome"></span></strong>
          <br />
			
       	  <table class="itemContaCorrenteCotaGrid"></table>
        
			<span class="bt_novos" title="Negociar Divida">
				<a href="javascript:;">
					<img src="${pageContext.request.contextPath}/images/ico_negociar.png" hspace="5" border="0" />
					Negociar Divida
				</a>
			</span>
			
			<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportar?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportar?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir
				</a>
			</span>
		</div>
            
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       
    </div>
</div> 
<script>

</script>
</body>
</html>
