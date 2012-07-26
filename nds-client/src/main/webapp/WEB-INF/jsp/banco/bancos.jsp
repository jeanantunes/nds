<head>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.price_format.1.7.js"></script>

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
				display : 'Apelido',
				name : 'apelido',
				width : 80,
				sortable : true,
				align : 'left'
			},{
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
	
	$(function() {
		$("#numero").numeric();	
		$("#cedente").numeric();	
		
		$("#newNumero").numeric();	
		$("#newCodigoCedente").numeric();	
		$("#newAgencia").numeric();	
		$("#newConta").numeric();	
		$("#newDigito").numeric();	
		$("#newJuros").numeric();	
		$("#newMulta").numeric();
		$("#newVrMulta").numeric();
		
		$("#alterNumero").numeric();	
		$("#alterCodigoCedente").numeric();	
		$("#alterAgencia").numeric();	
		$("#alterConta").numeric();	
		$("#alterDigito").numeric();	
		$("#alterJuros").numeric();	
		$("#alterMulta").numeric();
		$("#alterVrMulta").numeric();
    }); 

    function popup() {
	
		limparTelaCadastroBanco();
		
		$( "#dialog-novo" ).dialog({
			resizable: false,
			height:350,
			width:655,
			modal: true,
			buttons: {
				"Confirmar": function() {
					novoBanco();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
			}
		});
	};
	
	function popup_alterar() {
	
		$( "#dialog-alterar" ).dialog({
			resizable: false,
			height:350,
			width:655,
			modal: true,
			buttons: {
				"Confirmar": function() {
					alterarBanco();
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			},
			beforeClose: function() {
				clearMessageDialogTimeout();
		    }
		});	
		      
	};
	
	function popup_excluir(idBanco) {
	
		$( "#dialog-excluir" ).dialog({
			resizable: false,
			height:170,
			width:380,
			modal: true,

			buttons:[ 
			          {
				           id:"bt_confirmar",
				           text:"Confirmar", 
				           click: function() {
					           desativarBanco(idBanco);
				           }
			           },
			           {
				           id:"bt_cancelar",
				           text:"Cancelar", 
				           click: function() {
				        	   $( this ).dialog( "close" );
				           }
			           }
	        ],
	        
			beforeClose: function() {
				clearMessageDialogTimeout();
		    }	
			
		});
	};
	
    function mostrarGridConsulta() {
    	
    	$("#ativo").val(0);
    	if (document.formularioFiltro.ativo.checked){
    		$("#ativo").val(1);
		}
    	
		/*PASSAGEM DE PARAMETROS*/
		$(".bancosGrid").flexOptions({
			/*METODO QUE RECEBERA OS PARAMETROS*/
			url: "<c:url value='/banco/consultaBancos' />",
			params: [
			         {name:'nome', value:$("#nome").val()},
			         {name:'numero', value:$("#numero").val()},
			         {name:'cedente', value:$("#cedente").val()},
			         {name:'ativo', value:$("#ativo").val() }
			        ] ,
			        newp: 1
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
                                      '<img src="${pageContext.request.contextPath}/images/ico_editar.gif" hspace="5" border="0px" title="Altera banco" />' +
	                                  '</a>';			
				
			         var linkExcluir =    '<a href="javascript:;" onclick="popup_excluir(' + row.cell[0] + ');" style="cursor:pointer">' +
			                              '<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0px" title="Exclui banco" />' +
						                  '</a>';		 					 
									
				     row.cell[9] = linkEditar + linkExcluir;

		         }
		);
		
		return dadosPesquisa;
	}
	
	function fecharDialogs() {
		$( "#dialog-novo" ).dialog( "close" );
	    $( "#dialog-alterar" ).dialog( "close" );
	    $( "#dialog-excluir" ).dialog( "close" );
	}
	
    function novoBanco() {
		
    	var numero     = $("#newNumero").val();
		var nome       = $("#newNome").val();
		var cedente    = $("#newCodigoCedente").val();
		var agencia    = $("#newAgencia").val();
		var conta      = $("#newConta").val();
		var digito     = $("#newDigito").val();
		var apelido    = $("#newApelido").val();
		var carteira   = $("#newCarteira").val();
		var juros      = $("#newJuros").val();
		
		$("#newAtivo").val(0);
    	if (document.formularioNovoBanco.newAtivo.checked){
    		$("#newAtivo").val(1);
		}
		var ativo      = $("#newAtivo").val();
		
		var multa      = $("#newMulta").val();
		var vrMulta    = $("#newVrMulta").val();
		var instrucoes = $("#newInstrucoes").val();

		$.postJSON("<c:url value='/banco/novoBanco'/>",
				   "numero="+numero+
				   "&nome="+ nome +
				   "&codigoCedente="+ cedente+
				   "&agencia="+ agencia +
				   "&conta="+ conta+
				   "&digito="+ digito+
				   "&apelido="+ apelido+
				   "&codigoCarteira="+ carteira+
				   "&juros="+ juros+
				   "&ativo="+ ativo+
				   "&multa="+ multa+
				   "&vrMulta="+ vrMulta+
				   "&instrucoes="+ instrucoes,
				   function(result) {
			           fecharDialogs();
					   var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
	                   mostrarGridConsulta();
	               },
				   null,
				   true);
	}
	
	function alterarBanco() {
		
		var idBanco    = $("#idBanco").val();
    	var numero     = $("#alterNumero").val();
		var nome       = $("#alterNome").val();
		var cedente    = $("#alterCodigoCedente").val();
		var agencia    = $("#alterAgencia").val();
		var conta      = $("#alterConta").val();
		var digito     = $("#alterDigito").val();
		var apelido    = $("#alterApelido").val();
		var carteira   = $("#alterCarteira").val();
		var juros      = $("#alterJuros").val();
		
		$("#alterAtivo").val(0);
    	if (document.formularioAlteraBanco.alterAtivo.checked){
    		$("#alterAtivo").val(1);
		}
		var ativo      = $("#alterAtivo").val();
		
		var multa      = $("#alterMulta").val();
		var vrMulta    = $("#alterVrMulta").val();
		var instrucoes = $("#alterInstrucoes").val();

		$.postJSON("<c:url value='/banco/alteraBanco'/>",
				   "idBanco="+idBanco+
				   "&numero="+numero+
				   "&nome="+ nome +
				   "&codigoCedente="+ cedente+
				   "&agencia="+ agencia +
				   "&conta="+ conta+
				   "&digito="+ digito+
				   "&apelido="+ apelido+
				   "&codigoCarteira="+ carteira+
				   "&juros="+ juros+
				   "&ativo="+ ativo+
				   "&multa="+ multa+
				   "&vrMulta="+ vrMulta+
				   "&instrucoes="+ instrucoes,
				   function(result) {
					   fecharDialogs();
				       var tipoMensagem = result.tipoMensagem;
					   var listaMensagens = result.listaMensagens;
					   if (tipoMensagem && listaMensagens) {
					       exibirMensagem(tipoMensagem, listaMensagens);
				       }
			           mostrarGridConsulta();
			       },
				   null,
				   true);
	}
	
	function editarBanco(idBanco){
		var data = [{name: 'idBanco', value: idBanco}];
		$.postJSON("<c:url value='/banco/buscaBanco' />",
				   data,
				   sucessCallbackCadastroBanco, 
				   fecharDialogs);
	}
	
	function sucessCallbackCadastroBanco(resultado) {
		
		$("#idBanco").val(resultado.idBanco);
		$("#alterNumero").val(resultado.numero);
		$("#alterNome").val(resultado.nome);
		$("#alterCodigoCedente").val(resultado.codigoCedente);
		$("#alterAgencia").val(resultado.agencia);
		$("#alterConta").val(resultado.conta);
		$("#alterDigito").val(resultado.digito);
		$("#alterApelido").val(resultado.apelido);
		$("#alterCarteira").val(resultado.codigoCarteira);
		$("#alterJuros").val(resultado.juros);
		
		$("#alterAtivo").val(resultado.ativo);
		document.formularioAlteraBanco.alterAtivo.checked = resultado.ativo;
		
		$("#alterMulta").val(resultado.multa);
		$("#alterVrMulta").val(resultado.vrMulta);
		$("#alterInstrucoes").val(resultado.instrucoes);
		
		popup_alterar();
	}
	
    function desativarBanco(idBanco) {
    	var data = [{name: 'idBanco', value: idBanco}];
		$.postJSON("<c:url value='/banco/desativaBanco'/>",
				   data,
				   function(result) {
					   fecharDialogs();
			           mostrarGridConsulta();
			       },
			       function(result) {
					   fecharDialogs();
			           mostrarGridConsulta();
			       });
	}
	
    function limparTelaCadastroBanco() {
		$("#newNumero").val("");
		$("#newNome").val("");
		$("#newCodigoCedente").val("");
		$("#newAgencia").val("");
		$("#newConta").val("");
		$("#newDigito").val("");
		$("#newApelido").val("");
		$("#newCarteira").val("");
		$("#newJuros").val("");
		$("#newAtivo").val("");
		$("#newMulta").val("");
		$("#newVrMulta").val("");
		$("#newInstrucoes").val("");
	}
    
    function limparMulta(){
    	$("#newMulta").val("");
    	$("#alterMulta").val("");
    }
    
    function limparVrMulta(){
    	$("#newVrMulta").val("");
    	$("#alterVrMulta").val("");
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

        <jsp:include page="../messagesDialog.jsp" />

        <form name="formularioNovoBanco" id="formularioNovoBanco"> 

			<label><strong>Dados de Bancos</strong>
			</label>

			<table width="626" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="111">Número Banco:</td>
					<td width="216"><input type="text" name="newNumero" maxlength="17"
						id="newNumero" style="width: 143px;" />
					</td>
					<td width="73">Nome:</td>
					<td width="205"><input type="text" name="newNome" maxlength="100"
						id="newNome" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Código Cedente:</td>
					<td><input type="text" name="newCodigoCedente" id="newCodigoCedente" maxlength="17"
						style="width: 143px;" />
					</td>
					<td>Agência:</td>
					<td><input maxlength="17" type="text" name="newAgencia" id="newAgencia"
						style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Conta / Digito:</td>
					<td><input maxlength="17" type="text" name="newConta" id="newConta"
						style="width: 97px;" /> - <input maxlength="1" type="text" name="newDigito"
						id="newDigito" style="width: 30px;" />
					</td>
					<td>Apelido:</td>
					<td width="205"><input type="text" name="newApelido" maxlength="100"
						id="newApelido" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Carteira:</td>
					<td>
	
					<select name="newCarteira" id="newCarteira" style="width:150px;">
					    <option></option>
	                    <c:forEach varStatus="counter" var="newCarteira" items="${listaCarteiras}">
					       <option value="${newCarteira.key}">${newCarteira.value}</option>
					    </c:forEach>
	                </select>
					
					</td>
					<td>Juros %:</td>
					<td><input maxlength="17" type="text" name="newJuros" id="newJuros"
						style="width: 80px;" />
					</td>
				</tr>
				<tr>
					<td>Status:</td>
					<td><input name="newAtivo" type="checkbox" value=""
						checked="checked" id="newAtivo" /><label for="statusBco">Ativo</label>
					</td>
	
					<td>Multa %:</td>
					<td>
					    <input onchange="limparVrMulta();" maxlength="17" type="text" name="newMulta" id="newMulta" style="width:80px; text-align:right;" />
					    ou R$: <input onchange="limparMulta();" maxlength="17" type="text" name="newVrMulta" id="newVrMulta" style="width:80px; text-align:right;" />
					</td>

				</tr>
				<tr>
					<td>Instruções:</td>
					<td colspan="3"><textarea name="newInstrucoes" id="newInstrucoes" maxlength="200"
							style="width: 490px;"></textarea>
					</td>
				</tr>
			</table>
			
		</form>
		
	</div>
    
    
    <div id="dialog-alterar" title="Alterar Banco">
    
        <jsp:include page="../messagesDialog.jsp" />

	    <form name="formularioAlteraBanco" id="formularioAlteraBanco"> 
	
			<label><strong>Dados de Bancos</strong>
			</label>
	
	        <input type="hidden" id="idBanco" name="idBanco" />
	
			<table width="626" border="0" cellpadding="2" cellspacing="1">
				<tr>
					<td width="111">Número Banco:</td>
					<td width="216"><input type="text" name="alterNumero" maxlength="17"
						id="alterNumero" style="width: 143px;" />
					</td>
					<td width="73">Nome:</td>
					<td width="205"><input type="text" name="alterNome" maxlength="100"
						id="alterNome" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Código Cedente:</td>
					<td><input type="text" name="alterCodigoCedente" id="alterCodigoCedente" maxlength="17"
						style="width: 143px;" />
					</td>
					<td>Agência:</td>
					<td><input maxlength="17" type="text" name="alterAgencia" id="alterAgencia"
						style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Conta / Digito:</td>
					<td><input maxlength="17" type="text" name="alterConta" id="alterConta"
						style="width: 97px;" /> - <input maxlength="1" type="text" name="alterDigito"
						id="alterDigito" style="width: 30px;" />
					</td>
					<td>Apelido:</td>
					<td width="205"><input type="text" name="alterApelido" maxlength="100"
						id="alterApelido" style="width: 215px;" />
					</td>
				</tr>
				<tr>
					<td>Carteira:</td>
					<td>
					
						<select name="alterCarteira" id="alterCarteira" style="width:150px;">
		                    <option></option>
		                    <c:forEach varStatus="counter" var="carteira" items="${listaCarteiras}">
						       <option value="${carteira.key}">${carteira.value}</option>
						    </c:forEach>
		                </select>
		                
					</td>
					<td>Juros %:</td>
					<td><input maxlength="17" type="text" name="alterJuros" id="alterJuros"
						style="width: 80px;" />
					</td>
				</tr>
				<tr>
					<td>Status:</td>
					<td><input name="alterAtivo" type="checkbox" value=""
						checked="checked" id="alterAtivo" /><label for="statusBco">Ativo</label>
					</td>
					
					<td>Multa %:</td>
					<td>
					    <input onchange="limparVrMulta();" maxlength="17" type="text" name="alterMulta" id="alterMulta" style="width:80px; text-align:right;" />
					    ou R$: <input onchange="limparMulta();" maxlength="17" type="text" name="alterVrMulta" id="alterVrMulta" style="width:80px; text-align:right;" />
					</td>
					
				</tr>
				<tr>
					<td>Instruções:</td>
					<td colspan="3"><textarea name="alterInstrucoes" id="alterInstrucoes" maxlength="200"
							style="width: 490px;"></textarea>
					</td>
				</tr>
			</table>
			
		</form>
		
	</div>




	<div id="effect" style="padding: 0 .7em;"
		class="ui-state-highlight ui-corner-all">
		<p>
			<span style="float: left; margin-right: .3em;"
				class="ui-icon ui-icon-info">
		    </span> 
		    <b>
		        Banco < evento > com < status >.
		    </b>
		</p>
	</div>

    <form name="formularioFiltro" id="formularioFiltro"> 

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
	
	</form>
	
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
