
<head>

<script type="text/javascript">
function pesquisar(){
	$(".lancamentosProgramadosGrid").populate();
	$("#resumoPeriodo").show();
}

function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:370,
			width:410,
			modal: true,
			buttons: {
				"Fechar": function() {
					$( this ).dialog( "close" );
				}
			}
		});
	};
	
	function popup_reprogramar() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-reprogramar" ).dialog({
			resizable: false,
			height:160,
			width:320,
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
	
	
	function popup_volume_valor() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-volume-valor" ).dialog({
			resizable: false,
			height:'auto',
			width:320,
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
	
	function popup_peso() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
		$( "#dialog-peso" ).dialog({
			resizable: false,
			height:'auto',
			width:320,
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
	
	function popup_num_lancto() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-num-lancto" ).dialog({
			resizable: false,
			height:'auto',
			width:320,
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
	
	
	$(function() {
		$( "#datepickerDe" ).datepicker({
			dateFormat: "dd/mm/yy",
			showOn: "button",
			buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
			buttonImageOnly: true
		});
		$( "#datepickerDe_1" ).datepicker({
			dateFormat: "dd/mm/yy",
			showOn: "button",
			buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
			buttonImageOnly: true
		});
		
	});

</script>

</head>

<body>
<form action="" method="get" id="form1" name="form1">
<div id="dialog-reprogramar" title="Reprogramar Lançamento">
	<p><strong>Nova Data Matriz/Distrib:</strong> <input name="datepickerDe_1" type="text" style="width:80px;" id="datepickerDe_1" /></p>
</div>

<div id="dialog-volume-valor" title="Balanceamento por Volume Valor">
     <p>Confirma Balanceamento por Volume Valor?</p>
</div>


<div id="dialog-peso" title="Balanceamento por Peso">
     <p>Confirma Balanceamento por Peso?</p>
</div>

<div id="dialog-num-lancto" title="Número de Lançamentos">
     <p>Confirma Balanceamento por Número de Lançamentos?</p>
</div>
<div id="dialog-novo" title="Consulta de Lançamentos Programados">
     <fieldset style="width:365px;">
     	<legend>988989 - Nome do Fornecedor</legend>
        <table class="lancamentoProgFornecedorGrid"></table>
     
     </fieldset>
</div>

<div class="corpo">
   
    <div class="container">	
    <div id="effect" style="padding: 0 .7em;" class="ui-state-highlight ui-corner-all"> 
				<p><span style="float: left; margin-right: .3em;" class="ui-icon ui-icon-info"></span>
				<b>Lançamento Programado < evento > com < status >.</b></p>
	</div>
      <fieldset class="classFieldset">
   	    <legend>Pesquisar Balanceamento da Matriz de Lançamento
        </legend>
   	    <table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
   	      <tr>
   	        <td width="68">Fornecedor:</td>
   	        <td width="221">
            <a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
              <div class="menu_fornecedor" style="display:none;">
                 <span class="bt_sellAll"><input type="checkbox" id="sel" name="Todos" onclick="checkAll_fornecedor();" style="float:left;"/><label for="sel">Selecionar Todos</label></span>
                   <br clear="all" />
                   <c:forEach items="${fornecedores}" var="fornecedor">
                      <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="checkgroup_menu" onclick="verifyCheck_1()" type="checkbox"/>
                      <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
                      <br clear="all" />
                   </c:forEach> 
              </div>
           </td>
   	        <td colspan="3">Período de Lançamento Matriz/Distribuidor:</td>
   	        <td width="109"><input type="text" name="datepickerDe" id="datepickerDe" style="width:80px;" value="${data}" /></td>
   	        <td width="47" align="center">&nbsp;</td>
   	        <td width="112">&nbsp;</td>
   	        <td width="104"><span class="bt_pesquisar"><a href="javascript:;" onclick="pesquisar();">Pesquisar</a></span></td>

          </tr>
        </table>
      </fieldset>
          <div class="linha_separa_fields">&nbsp;</div>
      <fieldset class="classFieldset">
       	  <legend>Balanceamento da Matriz de Lançamento Cadastrados</legend>
        <div class="grids" style="display:none;">
        	<span class="bt_configura_inicial"><a href="javascript:;"><img src="<c:url value='images/bt_devolucao.png'/>" title="Voltar Configuração Inicial" border="0" hspace="5" />Voltar Configuração Inicial</a></span>
           <br clear="all" />
       	   <table class="lancamentosProgramadosGrid"></table>
          <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="<c:url value='images/ico_excel.png'/>" hspace="5" border="0" />Arquivo</a></span>
          <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="<c:url value='images/ico_impressora.gif'/>" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
          <span class="bt_novos" title="Reprogramar"><a href="javascript:;" onclick="popup_reprogramar();"><img src="<c:url value='images/ico_reprogramar.gif'/>"  hspace="5" border="0" />Reprogramar</a></span>
          <span class="bt_sellAll" style="float:right; margin-right:10px;"><label for="selRep">Selecionar Todos</label><input type="checkbox" id="selRep" name="Todos" onclick="checkAll();"/></span>
        </div>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>      
      <fieldset class="classFieldset" id="resumoPeriodo"; style="display:none;" >
        <legend>Resumo do Período</legend>
        <table width="100%" border="0" cellspacing="2" cellpadding="2">
          
        </table>
      </fieldset>
     </div>
</div>
</form>
<script>
	$(".lancamentosProgramadosGrid").flexigrid({
			url : '<c:url value="/matrizLancamento/matrizLancamento"/>',
			dataType : 'json',
			autoload: false,
			onSuccess: popularResumoPeriodo,
			preProcess : processaColunasLancamentos,
			onSubmit : function(){
				var idsFornecedores = new Array();
				$("input[name='checkgroup_menu']:checked").each(function(i) {
					idsFornecedores.push($(this).val());
				});
				$(".lancamentosProgramadosGrid").flexOptions({params: [{name:'data', value: $("#datepickerDe").val()}, 
		                                      {name:'idsFornecedores', value: idsFornecedores}]});
		        return true;
		    },
			colModel : [  {
				display : 'Código',
				name : 'codigoProduto',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Produto',
				name : 'nomeProduto',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Edição',
				name : 'numEdicao',
				width : 30,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'preco',
				width : 50,
				sortable : true,
				align : 'right'
			}, {
				display : 'Pcte Padrão',
				name : 'pacotePadrao',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Reparte',
				name : 'reparte',
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Lançamento',
				name : 'lancamento',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 70,
				sortable : true,
				align : 'center'
			}, {
				display : 'Fornecedor',
				name : 'nomeFornecedor',
				width : 70,
				sortable : true,
				align : 'left'
			}, {
				display : 'Previsto',
				name : 'dataPrevisto',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Matriz/Distrib.',
				name : 'dataMatrizDistrib',
				width : 105,
				sortable : true,
				align : 'center'
			},{
				display : 'Total R$',
				name : 'total',
				width : 40,
				sortable : true,
				align : 'right'
			},{
				display : 'Reprogramar',
				name : 'reprogramar',
				width : 70,
				sortable : true,
				align : 'center'
			}],
			sortname : "Nome",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180
		});
		
		$(".lancamentoProgFornecedorGrid").flexigrid({
			url : '',
			dataType : 'json',
			colModel : [ {
				display : 'Data Interface',
				name : 'dataInterface',
				width : 90,
				sortable : true,
				align : 'center'
			},{
				display : 'PEB',
				name : 'peb',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Físico',
				name : 'fisico',
				width : 60,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estudo Gerado',
				name : 'estudoGerado',
				width : 80,
				sortable : true,
				align : 'center'
			}],

			width : 360,
			height : 180
		});
		
		function processaColunasLancamentos(data) {
			$.each(data.rows, function(i, row){
				var inputDataDistrib = '<input type="text" name="datepickerDe10" id="datepickerDe10" style="width:70px; float:left;" value="'+row.cell.dataMatrizDistrib+'"/>';
				inputDataDistrib+='<span class="bt_atualizarIco" title="Atualizar Datas">';
				inputDataDistrib+='<a href="javascript:;">&nbsp;</a></span>';
				row.cell.dataMatrizDistrib = inputDataDistrib;
				row.cell.reprogramar='<input type="checkbox" name="checkgroup" onclick="verifyCheck()" />'
			});
			return data;
		}
		
		function popularResumoPeriodo() {
			return alert("Resumo Período!");
		}
		
</script>
</body>

