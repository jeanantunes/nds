<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/jquery.numeric.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/scripts/vendaEncalheCota.js'></script>

<script language="javascript" type="text/javascript">

var pesquisaCotaContaCorrentCota = new PesquisaCota();

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
	
	$.postJSON(
			'<c:url value="/financeiro/contaCorrenteCota/consultarEncalheCota" />', 
			parametroPesquisa,
			function(result){
				
				$(".encalheCotaGrid").flexToggleCol(7,result[0]);			
				
				$(".encalheCotaGrid").flexAddData({
					page: 1, total: 1, rows: result[1].tableModelEncalhe.rows
				});
				
				
				/////////////////////////////////////
				
				var data = result[1];
				
				$("#datacotanome").html(data.dataEscolhida+" Cota: "+$("#cota").val()+" - "+$("#nomeCota").val());
		
				var conteudoSpan = $("#listaInfoEncalhe").html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
			 	 	
			      conteudoSpan = $("#listaInfoEncalhe").html();
			 	 	
			
			      $("#listaInfoEncalhe").html(conteudoSpan + value.nomeFornecedor+":      "+value.valorTotal+"<br><br>");
			    });
				
				
				////////////////////////////////////
				
				$(".encalheCotaGrid").show();
				
							
				$(".gridsEncalhe").show();
				
			});	
			
	popup_encalhe();
	
}

/**
 * FAZ A PESQUISA DE CONSIGNADO DA COTA EM UMA DETERMINADA DATA
 */
function pesquisarConsignadoCota(lineId){
		
	var numeroCota = $("#cota").val();		
	
	var parametroPesquisa = [{name:'filtroConsolidadoConsignadoCotaDTO.numeroCota', value:numeroCota },
	                         {name:'filtroConsolidadoConsignadoCotaDTO.lineId', value:lineId }];
	
	$.postJSON(
			'<c:url value="/financeiro/contaCorrenteCota/consultarConsignadoCota" />', 
			parametroPesquisa,
			function(result){
				
				
				$(".consignadoCotaGrid").flexToggleCol(10,result[0]);
							
				$(".consignadoCotaGrid").flexAddData({
					page: 1, total: 1, rows: result[1].tableModelConsignado.rows
				});
				
				
				/////////////////////////////////////
				
				var data = result[1];
				
				$("#datacotanome_consignado").html(data.dataEscolhida+" Cota: "+$("#cota").val()+" - "+$("#nomeCota").val());
		
				var conteudoSpan = $("#listaInfoConsignado").html("");
				
				$.each(data.listaInfoFornecedores, function(index, value) {
			 	 	
			      conteudoSpan = $("#listaInfoConsignado").html();
			 	 	
			      $("#listaInfoConsignado").html(conteudoSpan + value.nomeFornecedor+":      "+value.valorTotal+"<br><br>");
			    });
				
				
				////////////////////////////////////
				
				$(".consignadoCotaGrid").show();
				
							
				$(".gridsConsignado").show();
				
			});	
			
	popup_consignado();
	
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
		
			value.cell[3] = '<a href="javascript:;" onclick="pesquisarConsignadoCota('+[lineId]+');"/>'+consignado+'</a>'+hiddeFields;
			value.cell[4] = '<a href="javascript:;" onclick="pesquisarEncalheCota('+[lineId]+');"/>'+encalhe+'</a>'+hiddeFields;
			value.cell[5] = '<a href="javascript:;" onclick="vendaEncalhe.showDialog('+value.cell[10]+',\''+value.cell[0]+'\')"/>'+vendaEncalhe+'</a>'+hiddeFields;
			value.cell[6] = '<a href="javascript:;"/>'+debCred+'</a>'+hiddeFields;
			value.cell[7] = '<a href="javascript:;"/>'+encargos+'</a>'+hiddeFields;
					
		});
		
	
		$("#cotanome").html($("#cota").val()+" "+$("#nomeCota").val());
		$(".grids").show();
		
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
	montarColunaConsignado();
	montarColunaEncalheCota();
	
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




function montarColunaEncalheCota(){
	
	$(".encalheCotaGrid").flexigrid({		
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
			name : 'precoCapa',
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
			align : 'right',		
		}, {
			display : 'Total R$',
			name : 'total',
			width : 75,
			sortable : true,
			align : 'right'
		}],
		sortname : "codigoProduto",
		sortorder : "asc",
		width : 800,
		height : 200
	});
}

function montarColunaConsignado(){

$(".consignadoCotaGrid").flexigrid({
	preProcess : function(data) {
		$.each(data.rows, function(index, value) {			
			if(!value.cell.motivo){
				value.cell.motivo = "";
			}				
			});
			return data;
		
	},
	dataType : 'json',	
	colModel : [ {
		display : 'Código',
		name : 'codigoProduto',
		width : 40,
		sortable : true,
		align : 'left'
	}, {
		display : 'Produto',
		name : 'nomeProduto',
		width : 90,
		sortable : true,
		align : 'left'
	}, {
		display : 'Edição',
		name : 'numeroEdicao',
		width : 40,
		sortable : true,
		align : 'center'
	}, {
		display : 'Preço Capa R$',
		name : 'precoCapa',
		width : 80,
		sortable : true,
		align : 'right',
	}, {
		display : 'Preço c/ Desc. R$',
		name : 'precoComDesconto',
		width : 60,
		sortable : true,
		align : 'right',
	}, {
		display : 'Reparte Sugerido',
		name : 'reparteSugerido',
		width : 82,
		sortable : true,
		align : 'center'
	}, {
		display : 'Reparte Final',
		name : 'reparteFinal',
		width : 70,
		sortable : true,
		align : 'center'
	}, {
		display : 'Diferença',
		name : 'diferenca',
		width : 45,
		sortable : true,
		align : 'center'
	}, {
		display : 'Motivo',
		name : 'motivo',
		width : 80,
		sortable : true,
		align : 'left'
	}, {
		display : 'Fornecedor',
		name : 'nomeFornecedor',
		width : 60,
		sortable : true,
		align : 'left'
	}, {
		display : 'Total R$',
		name : 'total',
		width : 50,
		sortable : true,
		align : 'right'
	}],
	sortname : "codigo",
	sortorder : "asc",
	width : 820,
	height : 200
});

}





function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-consignado" ).dialog({
			resizable: false,
			height:490,
			width:860,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".gridsConsignado").show();
					
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
$(function() {
	vendaEncalhe.url = '${pageContext.request.contextPath}/financeiro/contaCorrenteCota/obterMovimentoVendaEncalhe';
	vendaEncalhe.initGrid(".vendaEncalheGrid");
	vendaEncalhe.dialogId = "#dialog-venda-encalhe";
	
	vendaEncalhe.urlExport = '${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarVendaEncalhe';
});	
</script>
<style type="text/css">
  fieldset { width:auto!important; }
  
  #dialog-venda-encalhe, #dialog-consignado {
  	display: none;
  }
  </style>
</head>

<body>

<div id="dialog-consignado" title="Consignados">
     
	<fieldset style="">
    	<strong><span id="datacotanome_consignado"></span></strong>
    	
    	 <div class="gridsConsignado" style="display: none;">
        
        	<table class="consignadoCotaGrid"></table>
        
        	<span class="bt_novos" title="Gerar Arquivo">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarConsignadoCota?fileType=XLS">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
					Arquivo
				</a>
			</span>
			
			<span class="bt_novos" title="Imprimir">
				<a href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarConsignadoCota?fileType=PDF">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
					Imprimir
				</a>
			</span>
				     
				<div align="right">
								
					<table>
						<tr>
							<td><strong>Total R$:</strong></td>
							<td> </td>
						</tr>				
						<tr>
							<td></td>													
							<td><span id="listaInfoConsignado"></span></td>
						</tr>						
					</table>			
				</div>       	
	    </div>  
    </fieldset>
</div>

<div id="dialog-encalhe" title="Encalhe da Cota">
	<fieldset>
    
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
			
			<div align="right">
								
				<table>
					<tr>
						<td><strong>Total R$:</strong></td>
						<td> </td>
					</tr>				
					<tr>
						<td></td>													
						<td><span id="listaInfoEncalhe"></span></td>
					</tr>						
				</table>
			</div>       	
	    </div>   
    </fieldset>

</div>



<div id="dialog-venda-encalhe" title="Venda de Encalhe">
	<fieldset>
    	<legend><span id="datacotanome-venda-encalhe"></span></legend>
        
        <table class="vendaEncalheGrid"></table>
        <span class="bt_novos" title="Gerar Arquivo">
			<a id="dialog-venda-encalhe-export-xls" href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarVendaEncalhe?fileType=XLS">
				<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
				Arquivo
			</a>
		</span>
			
		<span class="bt_novos" title="Imprimir">
			<a id="dialog-venda-encalhe-export-pdf" href="${pageContext.request.contextPath}/financeiro/contaCorrenteCota/exportarVendaEncalhe?fileType=PDF">
				<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />
				Imprimir
			</a>
		</span>
       	<div align="right">								
				<table id="totaisFornecedores-venda-encalhe" width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;" >
					<tr>
						<td><strong>Total R$:</strong></td>
					</tr>			
				</table>
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
              <td colspan="3"><input type="text" name="filtroViewContaCorrenteCota.numeroCota" id="cota" onchange="pesquisaCotaContaCorrentCota.pesquisarPorNumeroCota('#cota', '#nomeCota');" style="width:80px; float:left; margin-right:5px;"/>
              </td>
              <td width="36">Nome:</td>
              <td width="263"><input type="text" name="nomeCota" id="nomeCota" onkeyup="pesquisaCotaContaCorrentCota.autoCompletarPorNome('#nomeCota');" onblur="pesquisaCotaContaCorrentCota.pesquisarPorNomeCota('#cota', '#nomeCota');" style="width:230px;"/></td>
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
