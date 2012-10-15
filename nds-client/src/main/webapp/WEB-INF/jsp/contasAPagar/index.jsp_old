<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/followUpSistema.js"></script>
</head>

<body>
  <br clear="all"/>
    <br />
<script language="javascript" type="text/javascript">
function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:490,
			width:890,
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
	function popup_email() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-email" ).dialog({
			resizable: false,
			height:400,
			width:490,
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
	
	function popup_detalhes() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhes-tipo" ).dialog({
			resizable: false,
			height:500,
			width:950,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				},
			 }
		});
	};
	
	
	$(function() {
		$( "#contaDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#contaAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtLancoDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#dtLancoAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#prodDe" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
		$( "#prodAte" ).datepicker({
			showOn: "button",
			buttonImage: "${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif",
			buttonImageOnly: true
		});
	});
	
	
	
function detalheVenda() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-detalhe-venda" ).dialog({
			resizable: false,
			height:420,
			width:650,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
				},
			}
		});
	};
function pesqDistribuidor(){
	$('.distrFornecedor').show();
	$('.filtroFornecedor').show();
	$('.filtroProdutos').hide();
	$('.grids').show();
}

function pesqProduto(){
	$('.distrFornecedor').hide();
	$('.filtroFornecedor').hide();
	$('.filtroProdutos').show();
	$('.grids').show();
}

function gridDistrib(){
	$('.gridDistrib').show();
	$('.gridProduto').hide();
	}
function gridProduto(){
	$('.gridDistrib').hide();
	$('.gridProduto').show();
	}


function popup_consignado() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-consignado" ).dialog({
			resizable: false,
			height:480,
			width:940,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
					
					$(".grids").show();
					
				},
				
			}
		});
	};
	$(document).ready(function(){	
	$("#selFornecedor").click(function() {
		$(".menu_fornecedor").show().fadeIn("fast");
	})

	$(".menu_fornecedor").mouseleave(function() {
		$(".menu_fornecedor").hide();
	});
	
})

$(document).ready(function(){	
	$("#selProdutos").click(function() {
		$(".menu_produtos").show().fadeIn("fast");
	})

	$(".menu_produtos").mouseleave(function() {
		$(".menu_produtos").hide();
	});
	
})
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
function popup_edit_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-edit-produto" ).dialog({
			resizable: false,
			height:360,
			width:500,
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
function popup_num_nota() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-num-nota" ).dialog({
			resizable: false,
			height:'auto',
			width:350,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
function popup_pesq_produto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-pesq-produto" ).dialog({
			resizable: false,
			height:'auto',
			width:600,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};	
	
$(function() {
		var availableTags = [
			"2323 - Veja",
			"1122 - Turma da Mônica",
			"3212 - Cascão",
			"0033 - Chico Bento",
			"0123 - Casa Cláudia"
		];
		$( "#produtos" ).autocomplete({
			source: availableTags
		});
	});
$(function() {
		var availableTags = [
			"2323 - Veja",
			"1122 - Turma da Mônica",
			"3212 - Cascão",
			"0033 - Chico Bento",
			"0123 - Casa Cláudia"
		];
		$( "#produtos2" ).autocomplete({
			source: availableTags
		});
	});
</script>


<body>
<div id="dialog-pesq-produto" title="Nota Fiscal" style="display:none;">
	<fieldset style="width:550px!important;">
	  <legend>Pesquisar Produtos</legend>
        <table width="530" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="96">Código / Produto:</td>
            <td width="311"><input type="text" name="produtos" id="produtos" style="width:290px;" /></td>
            <td width="40">Edição:</td>
            <td width="83" align="right"><input type="text" name="textfield9" id="textfield9" style="width:60px;" /></td>
          </tr>
        </table>
    </fieldset>
	<br clear="all"/>

  <fieldset style="width:550px!important; margin-top:10px;">
    <legend>Pesquisar Produtos</legend>
    <table class="lstProdutosGrid"></table>
    </fieldset>

</div>

<div id="dialog-num-nota" title="Nota Fiscal" style="display:none;">
	<fieldset style="width:300px!important;">
	<legend>Nota Fiscal</legend>
    <p>Digite o Número da Nota Fiscal: <input name="" type="text" style=" width:100px;" />
      <a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_add.gif" alt="Incluir Nova Nota Fiscal" width="16" height="16" border="0" /></a></p>
   </fieldset>
</div>
<div id="dialog-edit-produto" title="Dados do Produto" style="display:none;">
        <table width="412" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="129">Código:</td>
              <td width="272"><input name="textfield2" type="text" id="textfield2" style="width:80px;" value="3456789" disabled="disabled" /></td>
            </tr>
            <tr>
              <td>Produto:</td>
              <td><input name="textfield7" type="text" id="textfield7" style="width:250px;" value="Mônica" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Edição:</td>
              <td><input name="textfield" type="text" id="textfield" style="width:80px;" value="4556" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Preço Capa R$:</td>
              <td><input name="textfield3" type="text" id="textfield3" style="width:80px; text-align:right" value="4,85" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Fornecedor:</td>
              <td><input name="textfield4" type="text" id="textfield4" style="width:250px;" value="Dinap" disabled="disabled"/></td>
            </tr>
            <tr>
              <td>Data Lançamento:</td>
              <td><input type="text" id="dtLancoDe" style="width:80px;" /></td>
            </tr>
            <tr>
              <td>Data Recolhimento:</td>
              <td><input type="text" id="dtLancoAte" style="width:80px;" /></td>
            </tr>
          </table>
</div>
<div id="dialog-consignado" title="Consignados" style="display:none;">
     <fieldset style="width:895px!important;">
	  <legend>Pesquisar Produtos</legend>
        <table width="530" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="96">Código / Produto:</td>
            <td width="311"><input type="text" name="produtos2" id="produtos2" style="width:290px;" /></td>
            <td width="40">Edição:</td>
            <td width="83" align="right"><input type="text" name="textfield9" id="textfield9" style="width:60px;" /></td>
          </tr>
        </table>
    </fieldset>
	<fieldset style="width:895px!important; margin-top:10px;">
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="consignadoPopGrid"></table>
        
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>

            
                   
        

    </fieldset>

</div>
<div id="dialog-detalhe-venda" title="Parciais de Venda" style="display:none;">
	<fieldset style="width:600px!important;">
	<legend>Parciais de Venda</legend>
    <table class="parciaisVendaGrid"></table>
   
    <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>

</fieldset>
</div>
<div id="dialog-detalhes-tipo" title="Parcial" style="display:none;">
<fieldset>
	<legend>Dados da Parcial</legend>
    <table width="740" border="0" cellpadding="2" cellspacing="1">
            <tr>
              <td width="81"><strong>Código:</strong></td>
              <td width="94">44433</td>
              <td width="115"><strong>Produto:</strong></td>
              <td width="211"> Turma da Mônica</td>
              <td width="68"><strong>Edição:</strong></td>
              <td width="140">4345</td>
            </tr>
            <tr>
              <td><strong>Fornecedor:</strong></td>
              <td>FC</td>
              <td><strong>Data Lançamento:</strong></td>
              <td>12/02/2012</td>
              <td><strong>Data Final:</strong></td>
              <td>10/04/2012</td>
            </tr>
          </table>
          
</fieldset>
<br />
<br clear="all" />
<br />

<fieldset>
	<legend>Parciais</legend>
    <table class="parciaispopGrid"></table>
</fieldset>
<br clear="all" />


<br />
<span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>



</div>
<div id="dialog-encargos" title="Detalhes do Encargo">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        <br />

        <label><strong>Juros R$:</strong></label>  <label>999,99</label>
        <br />
        <br clear="all" />

        <label><strong>Multa R$:</strong></label>  <label>999,99</label>

        

    </fieldset>

</div>


<div id="dialog-conta" title="Detalhe Tipo de Movimento">
	<fieldset>
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhes_3Grid"></table>
        <div style="float:right; margin-right:30px;">Total: <strong>R$ 999.999,99</strong></div>
        <br clear="all" />
        <span class="bt_arquivo"><a href="javascript:;" onclick="popup();">Arquivo</a></span>
            
            <span class="bt_imprimir"><a href="javascript:;" onclick="popup();">Imprimir</a></span>

    </fieldset>

</div>


<div id="dialog-encalhe_2" title="Venda de Encalhe">
	<fieldset style="width:800px!important;">
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhes_2Grid"></table>
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="290" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>


    </fieldset>

</div>


<div id="dialog-encalhe" title="Encalhe da Cota">
	<fieldset style="width:810px!important;">
    	<legend>14/12/2011 - Cota: 26 1335 - CGB Distribuidora de Jornais e Revista</legend>
        
        <table class="encalhesGrid"></table>
        
        <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>
<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span>
       <table width="200" border="0" cellspacing="2" cellpadding="2"  style="float:right; margin-top: 7px;">
              <tr>
                <td width="109"><strong>Total R$:</strong></td>
                <td width="53"><strong>Dinap:</strong></td>
                <td width="92" align="right">999.999,99</td>
                <td width="10">&nbsp;</td>
              </tr>
              <tr>
                <td height="23" align="right"></td>
                <td><strong>FC:</strong></td>
                <td align="right">999.999,99</td>
                <td>&nbsp;</td>
              </tr>
            </table>

    </fieldset>

</div>





<div class="corpo">
 <div class="container">
    
     <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>E-mail enviado com sucesso!</b></p>
	</div>
    	
      <fieldset class="classFieldset">
   	    <legend> Pesquisar Contas a Pagar</legend>
        <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
            <tr>
              <td width="20" align="right"><input type="radio" name="radio" id="radio" value="radio" onchange="pesqDistribuidor();" /></td>
              <td width="69">Distribuidor</td>
              <td width="20"><input type="radio" name="radio" id="radio3" value="radio" onchange="pesqProduto();" /></td>
              <td width="47">Produto</td>
              <td width="195"><div class="filtroFornecedor" style="display:none;"><a href="#" id="selFornecedor">Clique e Selecione o Distribuidor</a>
              <div class="menu_fornecedor" style="display:none;">
                	 <span class="bt_sellAll"><input type="checkbox" id="sel" name="Todos1" onclick="checkAll_fornecedor();" style="float:left;"/><label for="sel">Selecionar Todos</label></span>
                    <br clear="all" />

                    <input id="dinap" name="checkgroup_menu" onclick="verifyCheck_1()" type="checkbox"/>
                    <label for="dinap">Dinap</label>
                    <br clear="all" />
                    <input name="checkgroup_menu" onclick="verifyCheck_1()" id="fc" type="checkbox"/>
                    <label for="fc">FC</label>
              </div>
            </div>
            
            <div class="filtroProdutos" style="display:none;">
            
            <a href="javascript:;" onclick="popup_pesq_produto();">Clique e Selecione o Produto</a>
              
            </div>
            </td>
              <td width="46">Período:</td>
              <td width="92"><input type="text" name="contaDe" id="contaDe" style="width:60px;"/></td>
              <td width="28">Até:</td>
              <td width="87"><input type="text" name="contaAte" id="contaAte" style="width:60px;"/></td>
              <td width="67">Semana CE:</td>
              <td width="71"><input type="text" name="textfield5" id="textfield6" style="width:50px;"/></td>
              <td width="147"><span class="bt_pesquisar filtroFornecedor" style="display:none;"><a href="javascript:;" onclick="gridDistrib();">Pesquisar</a></span>
              <span class="bt_pesquisar filtroProdutos" style="display:none;"><a href="javascript:;" onclick="gridProduto();">Pesquisar</a></span></td>
            </tr>
          </table>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Contas a Pagar </legend>
        <div class="gridDistrib" style="display:none;">
          <div class="distrFornecedor">
       	  	<table class="distrFornecedorGrid"></table>
            <table width="950" border="0" cellspacing="1" cellpadding="1">
              <tr>
                <td width="277"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span></td>
                <td width="220" align="right"><strong>Total Bruto R$: 999.999,99</strong></td>
                <td width="243" align="right"><strong>Total Desconto R$: 999.999,99</strong></td>
                <td width="197" align="right"><strong>Saldo a Pagar R$: 999.999,99</strong></td>
              </tr>
            </table>
          </div>
 
</div>
            
         <div class="gridProduto" style="display:none;">
         <div class="porProdutos">
       	  	<table class="porProdutosGrid"></table>
            <table width="950" border="0" cellspacing="1" cellpadding="1">
              <tr>
                <td width="241"><span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />Arquivo</a></span>

<span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />Imprimir</a></span></td>
                <td width="226" align="right"><strong>Total Pagto R$: 999.999,99</strong></td>
                <td width="216" align="right"><strong>Total Desconto R$: 999.999,99</strong></td>
                <td width="254" align="right"><strong>Valor Líquido a Pagar R$: 999.999,99</strong></td>
              </tr>
            </table>
            
            
          </div>
         </div>   
           
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>
       

        

    
    </div>
</div>
<script>

	$(".lstProdutosGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/lstProduto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
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
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 100,
				sortable : true,
				align : 'left',
			}, {
				display : 'Editor',
				name : 'editor',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : '',
				name : 'sel',
				width : 20,
				sortable : true,
				align : 'center'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 550,
			height : 200
		});
	
	$(".consignadoPopGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/consignado_1-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
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
				name : 'desconto',
				width : 60,
				sortable : true,
				align : 'right',
			}, {
				display : 'Reparte Sug.',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Final',
				name : 'reparteFinal',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Dif.',
				name : 'diferenca',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Motivo',
				name : 'motivo',
				width : 40,
				sortable : true,
				align : 'left'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'vlr',
				width : 40,
				sortable : true,
				align : 'right'
			}, {
				display : 'Valor c/Desc R$',
				name : 'vlrDesc',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'N° NF-e',
				name : 'numNfe',
				width : 57,
				sortable : true,
				align : 'left'
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 895,
			height : 200
		});
	$(".parciaisVendaGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/parciais-venda-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Cota',
				name : 'cota',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Nome',
				name : 'nome',
				width : 200,
				sortable : true,
				align : 'left'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Juramentada',
				name : 'vendaJuramentada',
				width : 100,
				sortable : true,
				align : 'center'
			}],
			sortname : "cota",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 595,
			height : 200
		});
		$(".parciaispopGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/parciais-popB-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Lcto',
				name : 'dtLancamento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Rclt',
				name : 'dtRecolhimento',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Suplementação',
				name : 'suplementacao',
				width : 90,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda',
				name : 'venda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda',
				name : 'percVenda',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda CE',
				name : 'vendaCE',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte Acum.',
				name : 'reparteAcumulado',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Venda Acum.',
				name : 'vendaAcumulada',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : '% Venda Acum.',
				name : 'percVendaAcumulada',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'N° NF-e',
				name : 'numNfe',
				width : 50,
				sortable : true,
				align : 'left'
			}],
			sortname : "dtLancamento",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 900,
			height : 200
		});
		
		$(".porProdutosGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/porProduto-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Rclt',
				name : 'data',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Código',
				name : 'codigo',
				width : 45,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 50,
				sortable : true,
				align : 'left',
			}, {
				display : 'Tipo',
				name : 'tipo',
				width : 40,
				sortable : true,
				align : 'center',
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : 'Suplementação',
				name : 'suplementacao',
				width : 80,
				sortable : true,
				align : 'center'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 60,
				sortable : true,
				align : 'center',
			}, {
				display : 'Venda',
				name : 'venda',
				width : 40,
				sortable : true,
				align : 'center',
			}, {
				display : 'Faltas/Sobras',
				name : 'faltasSobras',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Deb/Cred.',
				name : 'debitoCredito',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Saldo a Pagar R$',
				name : 'saldoPagar',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			sortname : "data",
			sortorder : "asc",
			width : 960,
			height : 255
		});
		
		
		$(".distrFornecedorGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/distrFornecedor-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Consignado R$',
				name : 'consignado',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Suplementação R$',
				name : 'suplementacao',
				width : 100,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe R$',
				name : 'encalhe',
				width : 110,
				sortable : true,
				align : 'right',
			}, {
				display : 'Venda R$',
				name : 'venda',
				width : 100,
				sortable : true,
				align : 'right',
			}, {
				display : 'Faltas Sobras R$',
				name : 'faltasSobras',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Deb/Cred R$',
				name : 'debCredito',
				width : 110,
				sortable : true,
				align : 'right'
			}, {
				display : 'Saldo a Pagar R$',
				name : 'saldoPagar',
				width : 100,
				sortable : true,
				align : 'right'
			}],
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			sortname : "data",
			sortorder : "asc",
			width : 960,
			height : 255
		});
		
		$(".encalhesGrid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/encalhes-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 50,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 130,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'valor',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'desconto',
				width : 95,
				sortable : true,
				align : 'right'
			}, {
				display : 'Encalhe',
				name : 'encalhe',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 100,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'total',
				width : 75,
				sortable : true,
				align : 'right',
			}],
			sortname : "Nome",
			sortorder : "asc",
			width : 800,
			height : 200
		});
		
		$(".encalhes_2Grid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/encalhes_2-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Código',
				name : 'codigo',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Produto',
				name : 'produto',
				width : 150,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'edicao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço de Capa R$',
				name : 'valor',
				width : 90,
				sortable : true,
				align : 'right'
			}, {
				display : 'Preço c/ Desc. R$',
				name : 'desconto',
				width : 60,
				sortable : true,
				align : 'right'
			}, {
				display : 'Box',
				name : 'box',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Exemplares',
				name : 'exemplares',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'fornecedor',
				width : 80,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right',
			}],
			sortname : "codigo",
			sortorder : "asc",
			width : 800,
			height : 200
		});
		
		$(".encalhes_3Grid").flexigrid({
			url : '${pageContext.request.contextPath}/xml/encalhes_3-xml.xml',
			dataType : 'xml',
			colModel : [ {
				display : 'Data',
				name : 'data',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Tipo Movimento',
				name : 'tipoMovimento',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Valor R$',
				name : 'valor',
				width : 70,
				sortable : true,
				align : 'right'
			}, {
				display : 'Observação',
				name : 'Observacao',
				width : 290,
				sortable : true,
				align : 'left'
			}],
			width : 600,
			height : 120
		});
</script>
</body>    
</body>
</html>