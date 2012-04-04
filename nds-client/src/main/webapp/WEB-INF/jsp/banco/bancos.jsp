<head>
<script language="javascript" type="text/javascript">

$(function() {
	$(".bancosGrid").flexigrid({
		preProcess: getDataFromResult,
		dataType : 'json',
		colModel : [ {
			display : 'Código',
			name : 'codigo',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Banco',
			name : 'banco',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Nome',
			name : 'nome',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Agência / Dígito',
			name : 'agencia',
			width : 90,
			sortable : true,
			align : 'left',
		}, {
			display : 'Conta-Corrente / Dígito',
			name : 'contaCorrente',
			width : 130,
			sortable : true,
			align : 'left'
		}, {
			display : 'Cedente',
			name : 'cedente',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Moeda',
			name : 'moeda',
			width : 50,
			sortable : true,
			align : 'left'
		}, {
			display : 'Carteira',
			name : 'carteira',
			width : 130,
			sortable : true,
			align : 'left'
		}, {
			display : 'Status',
			name : 'status',
			width : 70,
			sortable : true,
			align : 'left'
		}, {
			display : 'Ação',
			name : 'acao',
			width : 60,
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
		height : 255
	});
});	

function popup() {
		//$( "#dialog:ui-dialog" ).dialog( "destroy" );
	
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:350,
			width:655,
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
					$( "#abaPdv" ).show( );
					
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
			height:170,
			width:380,
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
	
	
	
	
	
	
	
function mostrarGridConsulta() {
		
		/*PASSAGEM DE PARAMETROS*/
		$(".bancosGrid").flexOptions({
			
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/banco/consultaBancos' />",
			params: [
			         {name:'nome', value:$("#nome").val()},
			         {name:'numero', value:$("#numero").val()},
			         {name:'cedente', value:$("#cedente").val()},
			         {name:'ativo', value:$("#ativo").val()}
			        ] ,
		});
		
		/*RECARREGA GRID CONFORME A EXECUCAO DO METODO COM OS PARAMETROS PASSADOS*/
		$(".bancosGrid").flexReload();
		
		$(".grids").show();
	}	
	
	
    
	function getDataFromResult(resultado) {
		
		//TRATAMENTO NA FLEXGRID PARA EXIBIR MENSAGENS DE VALIDACAO
		if (resultado.mensagens) {
			exibirMensagem(
				resultado.mensagens.tipoMensagem, 
				resultado.mensagens.listaMensagens
			);
			$(".grids").hide();
			return resultado.tableModel;
		}
		
		
		
		var dadosPesquisa;
		$.each(resultado, function(index, value) {
			  if(value[0] == "TblModelBancos") {
				  dadosPesquisa = value[1];
			  }
	    });
		
		
		
		$.each(dadosPesquisa.rows, 
				function(index, row) {

					 var linkImpressao = '<a href="${pageContext.request.contextPath}/banco/editarBanco?codigo=' + row.cell[9] + '" style="cursor:pointer">' +
					 					 '<img src="${pageContext.request.contextPath}/images/bt_impressao.png" hspace="5" border="0px" title="Imprime boleto" />' +
					 					 '</a>';			
				
			         var linkEmail =     '<a href="javascript:;" onclick="excluirBanco(' + row.cell[9] + ');" style="cursor:pointer">' +
			                             '<img src="${pageContext.request.contextPath}/images/bt_email.png" hspace="5" border="0px" title="Envia boleto por e-mail" />' +
 					                     '</a>';		 					 
									
				     row.cell[8] = linkImpressao + linkEmail;

		         }
		);
		
		
		return dadosPesquisa;
	}
	
	
	
	
	
	
	
</script>
<style>
label {
	vertical-align: super;
}
</style>
</head>

<body>

	<div id="dialog-excluir" title="Excluir Banco">
		<p>Confirma a exclusão deste Banco?</p>
	</div>





	<div id="dialog-novo" title="Incluir Novo Banco">

		<label><strong>Dados de Bancos</strong>
		</label>

		<table width="626" border="0" cellpadding="2" cellspacing="1">
			<tr>
				<td width="111">Número Banco:</td>
				<td width="216"><input type="text" name="textfield31"
					id="textfield32" style="width: 150px;" />
				</td>
				<td width="73">Nome:</td>
				<td width="205"><input type="text" name="textfield29"
					id="textfield30" style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Código Cedente:</td>
				<td><input type="text" name="textfield30" id="textfield31"
					style="width: 150px;" />
				</td>
				<td>Agência:</td>
				<td><input type="text" name="textfield32" id="textfield33"
					style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Conta / Digito:</td>
				<td><input type="text" name="textfield28" id="textfield28"
					style="width: 97px;" /> - <input type="text" name="textfield28"
					id="textfield29" style="width: 37px;" />
				</td>
				<td>Moeda:</td>
				<td><input type="text" name="textfield15" id="textfield15"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Carteira:</td>
				<td><input type="text" name="textfield33" id="textfield34"
					style="width: 150px;" />
				</td>
				<td>Juros %:</td>
				<td><input type="text" name="textfield16" id="textfield16"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Status:</td>
				<td><input name="statusBco" type="checkbox" value=""
					checked="checked" id="statusBco" /><label for="statusBco">Ativo</label>
				</td>
				<td>Multa %:</td>
				<td><input type="text" name="textfield17" id="textfield17"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Instruções:</td>
				<td colspan="3"><textarea name="textfield18" id="textfield18"
						style="width: 477px;"></textarea>
				</td>
			</tr>
		</table>
	</div>







	<div id="effect" style="padding: 0 .7em;"
		class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info"></span> <b>Banco < evento > com <
				status >.</b>
		</p>
	</div>

	<fieldset class="classFieldset">
		<legend> Pesquisar Bancos</legend>
		<table width="950" border="0" cellpadding="2" cellspacing="1"
			class="filtro">
			<tr>
				<td width="42">Nome:</td>
				<td colspan="3"><input type="text" name="nome"
					id="nome" style="width: 180px;" />
				</td>
				<td width="55">Número:</td>
				<td width="146"><input type="text" name="numero"
					id="numero" style="width: 130px;" />
				</td>
				<td width="57">Cedente:</td>
				<td width="163"><input type="text" name="cedente"
					id="cedente" style="width: 150px;" />
				</td>
				<td width="158"><input name="ativo" type="checkbox"
					id="ativo" style="float: left;" value="" checked="checked" /><label
					for="ativo">Ativo</label>
				</td>
				<td width="108"><span class="bt_pesquisar"><a href="javascript:;" onclick="mostrarGridConsulta();">Pesquisar</a>
				</span>
				</td>
			</tr>
		</table>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>
	<fieldset class="classFieldset">
		<legend>Bancos Cadastrados</legend>
		<div class="grids" style="display: none;">
			<table class="bancosGrid"></table>
		</div>

		<span class="bt_novo"><a href="javascript:;" onclick="popup();">Novo</a>
		</span>

	</fieldset>
	<div class="linha_separa_fields">&nbsp;</div>


</body>
