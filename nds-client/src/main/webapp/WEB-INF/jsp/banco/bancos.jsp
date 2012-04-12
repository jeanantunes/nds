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
				sortable : false,
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
					novoBanco();
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
	
		$( "#dialog-alterar" ).dialog({
			resizable: false,
			height:350,
			width:655,
			modal: true,
			buttons: {
				"Confirmar": function() {
					alterarBanco();
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

					 var linkEditar = '<a href="javascript:;" onclick="editarBanco(' + row.cell[0] + ');" style="cursor:pointer">' +
                                      '<img src="${pageContext.request.contextPath}/images/bt_email.png" hspace="5" border="0px" title="Altera banco" />' +
	                                  '</a>';			
				
			         var linkExcluir =    '<a href="javascript:;" onclick="desativarBanco(' + row.cell[0] + ');" style="cursor:pointer">' +
			                              '<img src="${pageContext.request.contextPath}/images/bt_email.png" hspace="5" border="0px" title="Exclui banco" />' +
						                  '</a>';		 					 
									
				     row.cell[9] = linkEditar + linkExcluir;

		         }
		);
		
		return dadosPesquisa;
	}
	
	
	
    function novoBanco() {
		
    	var numero     = $("#newNumero").val();
		var nome       = $("#newNome").val();
		var cedente    = $("#newCodigoCedente").val();
		var agencia    = $("#newAgencia").val();
		var conta      = $("#newConta").val();
		var digito     = $("#newDigito").val();
		var moeda      = $("#newMoeda").val();
		var carteira   = $("#newCarteira").val();
		var juros      = $("#newJuros").val();
		var ativo      = $("#newAtivo").val();
		var multa      = $("#newMulta").val();
		var instrucoes = $("#newInstrucoes").val();
		
		$.postJSON("<c:url value='/banco/novoBanco'/>",
				   "numero="+numero+
				   "&nome="+ nome +
				   "&codigoCedente="+ cedente+
				   "&agencia="+ agencia +
				   "&conta="+ conta+
				   "&digito="+ digito+
				   "&moeda="+ moeda+
				   "&carteira="+ carteira+
				   "&juros="+ juros+
				   "&ativo="+ ativo+
				   "&multa="+ multa+
				   "&instrucoes="+ instrucoes,
				   function() {mostrarGridConsulta();}, errorCallbackCadastroBanco);
	}
	
    
    
	function alterarBanco() {
		
		var idBanco    = $("#idBanco").val();
    	var numero     = $("#alterNumero").val();
		var nome       = $("#alterNome").val();
		var cedente    = $("#alterCodigoCedente").val();
		var agencia    = $("#alterAgencia").val();
		var conta      = $("#alterConta").val();
		var digito     = $("#alterDigito").val();
		var moeda      = $("#alterMoeda").val();
		var carteira   = $("#alterCarteira").val();
		var juros      = $("#alterJuros").val();
		var ativo      = $("#alterAtivo").val();
		var multa      = $("#alterMulta").val();
		var instrucoes = $("#alterInstrucoes").val();

		$.postJSON("<c:url value='/banco/alteraBanco'/>",
				   "idBanco="+idBanco+
				   "&numero="+numero+
				   "&nome="+ nome +
				   "&codigoCedente="+ cedente+
				   "&agencia="+ agencia +
				   "&conta="+ conta+
				   "&digito="+ digito+
				   "&moeda="+ moeda+
				   "&carteira="+ carteira+
				   "&juros="+ juros+
				   "&ativo="+ ativo+
				   "&multa="+ multa+
				   "&instrucoes="+ instrucoes,
				   function() {mostrarGridConsulta();}, errorCallbackCadastroBanco);
	}
	
	
	
	function editarBanco(idBanco){
		var data = [{name: 'idBanco', value: idBanco}];
		$.postJSON("<c:url value='/banco/buscaBanco' />",
				   data,
				   sucessCallbackCadastroBanco, errorCallbackCadastroBanco);

	}
	
	
	
	function sucessCallbackCadastroBanco(resultado) {
		
		$("#idBanco").val(resultado.idBanco);
		$("#alterNumero").val(resultado.numero);
		$("#alterNome").val(resultado.nome);
		$("#alterCodigoCedente").val(resultado.codigoCedente);
		$("#alterAgencia").val(resultado.agencia);
		$("#alterConta").val(resultado.conta);
		$("#alterDigito").val(resultado.digito);
		$("#alterMoeda").val(resultado.moeda);
		$("#alterCarteira").val(resultado.carteira);
		$("#alterJuros").val(resultado.juros);
		$("#alterAtivo").val(resultado.ativo);
		$("#alterMulta").val(resultado.multa);
		$("#alterInstrucoes").val(resultado.instrucoes);
		
		popup_alterar();
	}
	
	
	
	function errorCallbackCadastroBanco() {
		$('#dialog-novo').hide();
		$('#dialog-alterar').hide();
	}
	
	
	
    function desativarBanco(idBanco) {
    	var data = [{name: 'idBanco', value: idBanco}];
		$.postJSON("<c:url value='/banco/desativaBanco'/>",
				   data,
				   function() {mostrarGridConsulta();});
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
				<td width="216"><input type="text" name="newNumero"
					id="newNumero" style="width: 150px;" />
				</td>
				<td width="73">Nome:</td>
				<td width="205"><input type="text" name="newNome"
					id="newNome" style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Código Cedente:</td>
				<td><input type="text" name="newCodigoCedente" id="newCodigoCedente"
					style="width: 150px;" />
				</td>
				<td>Agência:</td>
				<td><input type="text" name="newAgencia" id="newAgencia"
					style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Conta / Digito:</td>
				<td><input type="text" name="newConta" id="newConta"
					style="width: 97px;" /> - <input type="text" name="newDigito"
					id="newDigito" style="width: 37px;" />
				</td>
				<td>Moeda:</td>
				<td><input type="text" name="newMoeda" id="newMoeda"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Carteira:</td>
				<td><input type="text" name="newCarteira" id="newCarteira"
					style="width: 150px;" />
				</td>
				<td>Juros %:</td>
				<td><input type="text" name="newJuros" id="newJuros"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Status:</td>
				<td><input name="newAtivo" type="checkbox" value=""
					checked="checked" id="newAtivo" /><label for="statusBco">Ativo</label>
				</td>
				<td>Multa %:</td>
				<td><input type="text" name="newMulta" id="newMulta"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Instruções:</td>
				<td colspan="3"><textarea name="newInstrucoes" id="newInstrucoes"
						style="width: 477px;"></textarea>
				</td>
			</tr>
		</table>
	</div>


    <div id="dialog-alterar" title="Alterar Banco">

		<label><strong>Dados de Bancos</strong>
		</label>

        <input type="hidden" id="idBanco" name="idBanco" />

		<table width="626" border="0" cellpadding="2" cellspacing="1">
			<tr>
				<td width="111">Número Banco:</td>
				<td width="216"><input type="text" name="alterNumero"
					id="alterNumero" style="width: 150px;" />
				</td>
				<td width="73">Nome:</td>
				<td width="205"><input type="text" name="alterNome"
					id="alterNome" style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Código Cedente:</td>
				<td><input type="text" name="alterCodigoCedente" id="alterCodigoCedente"
					style="width: 150px;" />
				</td>
				<td>Agência:</td>
				<td><input type="text" name="alterAgencia" id="alterAgencia"
					style="width: 177px;" />
				</td>
			</tr>
			<tr>
				<td>Conta / Digito:</td>
				<td><input type="text" name="alterConta" id="alterConta"
					style="width: 97px;" /> - <input type="text" name="alterDigito"
					id="alterDigito" style="width: 37px;" />
				</td>
				<td>Moeda:</td>
				<td><input type="text" name="alterMoeda" id="alterMoeda"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Carteira:</td>
				<td><input type="text" name="alterCarteira" id="alterCarteira"
					style="width: 150px;" />
				</td>
				<td>Juros %:</td>
				<td><input type="text" name="alterJuros" id="alterJuros"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Status:</td>
				<td><input name="alterAtivo" type="checkbox" value=""
					checked="checked" id="alterAtivo" /><label for="statusBco">Ativo</label>
				</td>
				<td>Multa %:</td>
				<td><input type="text" name="alterMulta" id="alterMulta"
					style="width: 80px;" />
				</td>
			</tr>
			<tr>
				<td>Instruções:</td>
				<td colspan="3"><textarea name="alterInstrucoes" id="alterInstrucoes"
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
