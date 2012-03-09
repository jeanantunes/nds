
<head>

<script type="text/javascript">

var linhasDestacadas = new Array();

function pesquisar(){
	$(".grids").show();
	$("#lancamentosProgramadosGrid").flexReload();
	$("#resumoPeriodo").show();
	linhasDestacadas = new Array();
}

function popup() {
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
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
			buttonImageOnly: true
		});
		$( "#datepickerDe_1" ).datepicker({
			showOn: "button",
			dateFormat: 'dd/mm/yy',
			buttonImage: "<c:url value='scripts/jquery-ui-1.8.16.custom/development-bundle/demos/datepicker/images/calendar.gif'/>",
			buttonImageOnly: true
		});
		
		$("#datepickerDe").mask("99/99/9999");
		$("#datepickerDe_1").mask("99/99/9999");
		
	});

</script>
<style>

.ui-datepicker { z-index: 1000 !important; }
.ui-datepicker-today a { display:block !important; }

.gridLinhaDestacada {
  background:#F00; 
  font-weight:bold; 
  color:#fff;
}

.gridLinhaDestacada:hover {
   color:#000;
}

.gridLinhaDestacada a {
   color:#fff;
}

.gridLinhaDestacada a:hover {
   color:#000;
}

</style>
</head>

<body>
<form action="" method="get" id="form1" name="form1">
<div id="dialog-reprogramar" title="Reprogramar Lançamento">
	<p><strong>Nova Data Matriz/Distrib:</strong> 
      <input name="datepickerDe_1" type="text" style="width:80px;" id="datepickerDe_1" />
    </p>
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
   	        <td width="228">
            <a href="#" id="selFornecedor" onclick="return false;">Clique e Selecione o Fornecedor</a>
              <div class="menu_fornecedor" style="display:none;">
                	<span class="bt_sellAll">
						<input type="checkbox" id="selTodos1" name="selTodos1" onclick="checkAll(this, 'checkgroup_menu');" style="float:left;"/>
					<label for="selTodos1">Selecionar Todos</label></span>
                    <br clear="all" />
                    <c:forEach items="${fornecedores}" var="fornecedor">
                      <input id="fornecedor_${fornecedor.id}" value="${fornecedor.id}"  name="checkgroup_menu" onclick="verifyCheck($('#selTodos1'));" type="checkbox"/>
                      <label for="fornecedor_${fornecedor.id}">${fornecedor.juridica.nomeFantasia}</label>
                      <br clear="all" />
                   </c:forEach> 
              </div>
            
            </td>
   	        <td colspan="3">Data de Lançamento Matriz/Distribuidor:</td>
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
       	   <table id="lancamentosProgramadosGrid" class="lancamentosProgramadosGrid"></table>
          
            <span class="bt_novos" title="Gerar Arquivo"><a href="javascript:;"><img src="<c:url value='images/ico_excel.png'/>" hspace="5" border="0" />Arquivo</a></span>
              <span class="bt_novos" title="Imprimir"><a href="javascript:;"><img src="<c:url value='images/ico_impressora.gif'/>" alt="Imprimir" hspace="5" border="0" />Imprimir</a></span>
             
              <span class="bt_novos" title="Reprogramar"><a href="javascript:;" onclick="popup_reprogramar();"><img src="<c:url value='images/ico_reprogramar.gif'/>"  hspace="5" border="0" />Reprogramar</a></span>
         	  <div style="margin-top:15px; margin-left:30px; float:left;"><strong>Valor Total R$: <span id="valorTotal"></span></strong></div>
          
              <span class="bt_sellAll" style="float:right; margin-right:60px;"><label for="selRep">Selecionar Todos</label><input type="checkbox" id="selRep" name="Todos" onclick="checkAll(this, 'checkgroup');"/></span>
        </div>
      </fieldset>
      <div class="linha_separa_fields">&nbsp;</div>      
      <fieldset class="classFieldset" id="resumoPeriodo"; style="display:none;" >
      	<legend>Resumo do Período</legend>
        <table width="100%" border="0" cellspacing="2" cellpadding="2" id="tableResumoPeriodo">
        </table>
      </fieldset>
    </div>
</div>
</form>

<script>
	
	$("#lancamentosProgramadosGrid").flexigrid({
			url : '<c:url value="/matrizLancamento/matrizLancamento"/>',
			dataType : 'json',
			autoload: false,
			singleSelect: true,
			onSuccess: buscarResumoPeriodo,
			preProcess : processarColunasLancamentos,
			onSubmit : function(){
				var parametros = new Array();
				parametros.push({name:'data', value: $("#datepickerDe").val()});
				$("input[name='checkgroup_menu']:checked").each(function(i) {
					parametros.push({name:'idsFornecedores', value: $(this).val()});
				});
				$("#lancamentosProgramadosGrid").flexOptions({params: parametros});
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
				width : 40,
				sortable : true,
				align : 'center'
			}, {
				display : 'Preço Capa R$',
				name : 'preco',
				width : 75,
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
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Físico',
				name : 'fisico',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Estudo Gerado',
				name : 'estudoGerado',
				width : 75,
				sortable : true,
				align : 'center'
			},{
				display : 'Lançamento',
				name : 'lancamento',
				width : 65,
				sortable : true,
				align : 'left'
			}, {
				display : 'Recolhimento',
				name : 'dataRecolhimento',
				width : 75,
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
				width : 100,
				sortable : true,
				align : 'center'
			},{
				display : 'Total R$',
				name : 'total',
				width : 60,
				sortable : true,
				align : 'right'
			},{
				display : 'Reprogramar',
				name : 'reprogramar',
				width : 65,
				sortable : false,
				align : 'center'
			}],
			sortname : "dataMatrizDistrib",
			sortorder : "asc",
			usepager : true,
			useRp : true,
			rp : 15,
			showTableToggleBtn : true,
			width : 960,
			height : 180,
			disableSelect : true
		});

		function processarColunasLancamentos(data) {
			$("#tableResumoPeriodo").clear();
			$("#valorTotal").clear();
			if (data.mensagens) {
				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				return data;
			}
			linhasDestacadas = new Array();
			$("#valorTotal").html(data[1]);
			$.each(data[0].rows, function(i, row){
				row.cell.reprogramar='';
				var emEstudoExpedido = row.cell.estudoFechado || row.cell.expedido;
				if (!emEstudoExpedido) {
					var dataDistrib = '<input type="text" name="datepickerDe10" id="datepickerDe10" style="width:70px; float:left;" value="'+row.cell.dataMatrizDistrib+'"/>';
					dataDistrib+='<span class="bt_atualizarIco" title="Atualizar Datas">';
					dataDistrib+='<a href="javascript:;">&nbsp;</a></span>';
					row.cell.dataMatrizDistrib = dataDistrib;
					row.cell.reprogramar='<input type="checkbox" name="checkgroup" onclick="verifyCheck($(\'#selRep\'));" />';
				}
				if (row.cell.semFisico || row.cell.cancelamentoGD || row.cell.furo ) {
					linhasDestacadas.push(i+1);
				}
			});
			return data[0];
		}
		
		function buscarResumoPeriodo() {
			var parametros = new Array();
			parametros.push({name:'dataInicial', value: $("#datepickerDe").val()});
			$("input[name='checkgroup_menu']:checked").each(function(i) {
				parametros.push({name:'idsFornecedores', value: $(this).val()});
			});
			var data = parametros;
			$.ajax({
				type:"GET",
				url:'<c:url value="/matrizLancamento/resumoPeriodo"/>',
				data: data,
				cache: false,
				dataType: "json",
				success: function(data) {
					popularResumoPeriodo(data);
				}
			});
		}
		
		function popularResumoPeriodo(data) {
			if (data.mensagens) {
				exibirMensagem(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);
				return data;
			}
			
			var rows='<tr>';
			$.each(data, function(index, resumo){
				  rows+='<td>';
				  rows+='<div class="box_resumo">';
				  rows+='<label>'+ resumo.dataFormatada +'</label>';
				  rows+='<span class="span_1">Qtde. Títulos:</span>';	 
				  rows+='<span class="span_2">'+ resumo.qtdeTitulos +'</span>';	
				  rows+='<span class="span_1">Qtde. Exempl.:</span>';	
				  rows+='<span class="span_2">'+ resumo.qtdeExemplaresFormatada +'</span>';	
				  rows+='<span class="span_1">Peso Total:</span>';
				  rows+='<span class="span_2">'+ resumo.pesoTotalFormatado +'</span>';
				  rows+='<span class="span_1">Valor Total:</span>';
				  rows+='<span class="span_2">'+ resumo.valorTotalFormatado +'</span>'
				  rows+='</div>';
				  rows+='</td>';					  
		    });	
		    rows+="</tr>";
		    $("#tableResumoPeriodo").append(rows);
		    
		    $(".lancamentosProgramadosGrid tr").each(function(i){
		    	if($.inArray((i+1), linhasDestacadas) > -1) {
		    		 $(this).removeClass("erow").addClass("gridLinhaDestacada");
					 $(this).children("td").removeClass("sorted");
		    	}
		   	});
		}
		
	
</script>
</body>

