<head>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">

	function confirmarExclusaoEndereco(idEndereco) {
	
		$( "#dialog-excluir-end" ).dialog({
			resizable: false,
			height:'auto',
			width:380,
			modal: true,
			buttons: {
				"Confirmar": function() {
					removerEndereco(idEndereco);
					$( this ).dialog( "close" );
				},
				"Cancelar": function() {
					$( this ).dialog( "close" );
				}
			}			
		});
	}

	function processarResultadoConsultaEndereco(data) {
                                                                                                                                                                                                                                                                                                                                                                  
		if (data.mensagens) {

			exibirMensagemDialog(
				data.mensagens.tipoMensagem, 
				data.mensagens.listaMensagens
			);

			return;
		}
		
		var i;

		for (i = 0 ; i < data.rows.length; i++) {

			var lastIndex = data.rows[i].cell.length;

			data.rows[i].cell[lastIndex - 1] =					
				data.rows[i].cell[lastIndex - 1] == "true" 
						? '<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0px"/>'
						: '&nbsp;';

			data.rows[i].cell[lastIndex] = getAction(data.rows[i].id);
		}

		if ($(".enderecosGrid").css('display') == 'none') {

			$(".enderecosGrid").show();
		}

		return data;
	}

	function getAction(idEndereco) {

		return '<a href="javascript:;" onclick="editarEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
				'</a>' +
				'<a href="javascript:;" onclick="confirmarExclusaoEndereco(' + idEndereco + ')" ' +
				' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
				'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>' +
				'</a>';
	}

	function popularGridEnderecos() {
		
		$.postJSON(
			'<c:url value="/cadastro/endereco/pesquisarEnderecos" />',
			null,
			function(result) {
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				limparFormEndereco();
				
				$("#tipoEndereco").focus();
			},
			function(result) {
				
				processarResultadoConsultaEndereco(result);
			},
			true
		);
	}		
	
	function incluirNovoEndereco() {

		var formData = $("#formEnderecos").serializeArray();

		$.postJSON(
			'<c:url value="/cadastro/endereco/incluirNovoEndereco" />',
			formData,
			function(result) {
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});	
				
				limparFormEndereco();
				
				$("#tipoEndereco").focus();
			},
			function(result) {
				
				processarResultadoConsultaEndereco(result);
			},
			true
		);
	}

	function editarEndereco(idEndereco) {

		$("#linkIncluirNovoEndereco").html("<img src='${pageContext.request.contextPath}/images/ico_salvar.gif' hspace='5' border='0' /> Salvar");
		$("#btnIncluirNovoEndereco").removeClass();
		$("#btnIncluirNovoEndereco").addClass("bt_novos");

		$.postJSON(
			'<c:url value="/cadastro/endereco/editarEndereco" />',
			{ "idEnderecoAssociacao": idEndereco },
			function(result) {
				$("#idEndereco").val(result.id);
				$("#tipoEndereco").val(result.tipoEndereco);
				$("#cep").val(result.endereco.cep);
				$("#tipoLogradouro").val(result.endereco.tipoLogradouro);
				$("#logradouro").val(result.endereco.logradouro);
				$("#numero").val(result.endereco.numero);
				$("#complemento").val(result.endereco.complemento);
				$("#bairro").val(result.endereco.bairro);
				$("#cidade").val(result.endereco.cidade);
				$("#uf").val(result.endereco.uf);
				$("#principal").attr("checked", result.enderecoPrincipal);
			},
			null, 
			true
		);
	}

	function removerEndereco(idEndereco) {
		
		$.postJSON(
			'<c:url value="/cadastro/endereco/removerEndereco" />',
			{ "idEnderecoAssociacao" : idEndereco },
			function(result) {
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});		
			},
			function(result) {
				
				processarResultadoConsultaEndereco(result);
			},
			true
		);
	}

	function popup() {

		$("#manutencaoEnderecos").dialog({
			resizable: false,
			height:640,
			width:840,
			modal : true,
			buttons : {
				"Fechar" : function() {
					$(this).dialog("close");
				},
			}
		});
	}
	
	function limparFormEndereco() {

		$("#linkIncluirNovoEndereco").html("Incluir Novo");
		$("#btnIncluirNovoEndereco").removeClass();
		$("#btnIncluirNovoEndereco").addClass("bt_add");

		$("#idEndereco").val("");
		$("#tipoEndereco").val("");
		$("#cep").val("");
		$("#tipoLogradouro").val("");
		$("#logradouro").val("");
		$("#numero").val("");
		$("#complemento").val("");
		$("#bairro").val("");
		$("#cidade").val("");
		$("#uf").val("");
		$("#principal").attr("checked", false);
	}
	
	$(function() {

		$("#cep").mask("99999-999");
		$("#uf").mask("aa");
		$("#numero").numeric();
		
		$("#linkIncluirNovoEndereco").keypress(function() {
			
			var keynum = 0;
	          
	        if(window.event) {

	            keynum = event.keyCode;
	        
	        } else if(event.which) {   

	        	keynum = event.which;
	        }

			if (keynum == 13) {
				incluirNovoEndereco();
			}
		});
	});
	
	function pesquisarEnderecoPorCep() {
	
		var cep =  $("#cep").val();
		
		$.postJSON(
			'<c:url value="/cadastro/endereco/obterEnderecoPorCep" />',
			{ "cep": cep },
			function(result) {
				$("#idEndereco").val(result.id);
				$("#tipoEndereco").val(result.tipoEndereco);
				$("#cep").val(result.endereco.cep);
				$("#tipoLogradouro").val(result.endereco.tipoLogradouro);
				$("#logradouro").val(result.endereco.logradouro);
				$("#numero").val(result.endereco.numero);
				$("#complemento").val(result.endereco.complemento);
				$("#bairro").val(result.endereco.bairro);
				$("#cidade").val(result.endereco.cidade);
				$("#uf").val(result.endereco.uf);
				$("#principal").attr("checked", result.enderecoPrincipal);
			},
			null, 
			true
		);
	}
	
	$(document).ready(function() {
		
		$(".enderecosGrid").flexigrid({
			preProcess: processarResultadoConsultaEndereco,
			dataType : 'json',
			colModel : [  {
				display : 'Tipo Endereço',
				name : 'tipoEndereco',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Logradouro',
				name : 'endereco.logradouro',
				width : 205,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'endereco.bairro',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cep',
				name : 'endereco.cep',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'endereco.cidade',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'enderecoPrincipal',
				width : 50,
				sortable : true,
				align : 'center'
			}, {
				display : 'Ação',
				name : 'acao',
				width : 60,
				sortable : false,
				align : 'center'
			}],
			width : 770,
			height : 150,
			sortorder: "asc",
			sortname: "endereco.logradouro",
			singleSelect: true
		});
	});
	
</script>
    

</head>

<div id="manutencaoEnderecos">

	<div id="dialog-excluir-end" title="Excluir Endereço">
		<p>Confirma a exclusão desse endereço?</p>
	</div>

	<form name="formEnderecos" id="formEnderecos">
		
		<input type="hidden" name="enderecoAssociacao.id" id="idEndereco"/>
		
		<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
			<tr>
				
				<td width="99">Tipo Endereço:</td>
				<td width="310">
					<select  style="width:157px" 
							 name="enderecoAssociacao.tipoEndereco" 
							 id="tipoEndereco">
						<option value="COMERCIAL">Comercial</option>
						<option value="LOCAL_ENTREGA">Local de Entrega</option>
						<option value="RESIDENCIAL">Residencial</option>
						<option value="COBRANCA">Cobrança</option>
					</select>
				</td>
				<td width="76">CEP:</td>
				<td width="241">
				<input type="text" style="float:left; margin-right:5px;" 
					   name="enderecoAssociacao.endereco.cep" id="cep" />

					<span class="classPesquisar" title="Pesquisar Cep.">
						<a href="javascript:;" onclick="pesquisarEnderecoPorCep();">&nbsp;</a>
					</span></td>
			</tr>
			<tr>
				<td>Tipo Logradouro:</td>
				<td>
					<input type="text" style="width:230px" 
						   name="enderecoAssociacao.endereco.tipoLogradouro" id="tipoLogradouro" />
				</td>
				<td>Logradouro:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.logradouro" id="logradouro" />
				</td>
			</tr>
			<tr>			
				<td>N&uacute;mero:</td>
				<td>
					<input type="text" style="width:50px" 
						   name="enderecoAssociacao.endereco.numero" id="numero" maxlength="9" />
				</td>
				<td>Complemento:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.complemento" id="complemento" />
				</td>
			</tr>
			<tr>
			
				<td>Bairro:</td>
				<td>
					<input type="text"  style="width:230px" 
						   name="enderecoAssociacao.endereco.bairro" id="bairro" />
				</td>
				<td>Cidade:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.cidade" id="cidade" />
				</td>
				
			</tr>
			<tr>
				<td>UF:</td>
				<td>
					<input type="text" style="width:50px;text-transform:uppercase" 
						   name="enderecoAssociacao.endereco.uf" id="uf"  />
				</td>
			    <td>Principal:</td>
			   	<td>
				  	<input type="checkbox" id="principal" 
				  		   name="enderecoAssociacao.enderecoPrincipal"/>
			  	</td>
			    <td>&nbsp;</td>
			    <td>&nbsp;</td>
		  </tr>
			<tr>
			  <td>&nbsp;</td>
			  <td>
			  	<span class="bt_add" id="btnIncluirNovoEndereco">
			  		<a href="javascript:;" onclick="incluirNovoEndereco();" id="linkIncluirNovoEndereco">Incluir Novo</a>
			  	</span>
			  </td>
			  <td>&nbsp;</td>
			  <td>&nbsp;</td>
		  </tr>		
		</table>
	</form>
    <br />
    <label>
    	<strong>Endereços Cadastrados</strong>
    </label>
    <br />

    <table class="enderecosGrid"></table>
    
</div>
