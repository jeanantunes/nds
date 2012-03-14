<head>

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
					$("#effect").show("highlight", {}, 1000, callback);
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

			$(".enderecosGrid").hide();

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

		$(".enderecosGrid").flexReload();
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
			null,
			true
		);
	}

	function editarEndereco(idEndereco) {

		$.postJSON(
			'<c:url value="/cadastro/endereco/editarEndereco" />',
			{ "idEndereco": idEndereco },
			function(result) {
				$("#idEndereco").val(result.endereco.id);
				$("#tipoEndereco").val(result.tipoEndereco);
				$("#cep").val(result.endereco.cep);
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
			{ "idEndereco" : idEndereco },
			function(result) {
				$(".enderecosGrid").flexAddData({
					page: result.page, total: result.total, rows: result.rows
				});		
			},
			null,
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
		$("#idEndereco").val("");
		$("#tipoEndereco").val("");
		$("#cep").val("");
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
	});
	
	$(document).ready(function() {
		
		$(".enderecosGrid").flexigrid({
			preProcess: processarResultadoConsultaEndereco,
			url : '<c:url value="/cadastro/endereco/pesquisarEnderecos" />',
			dataType : 'json',
			colModel : [  {
				display : 'Tipo Endereço',
				name : 'tipoendereco',
				width : 80,
				sortable : true,
				align : 'left'
			},{
				display : 'Logradouro',
				name : 'logradouro',
				width : 205,
				sortable : true,
				align : 'left'
			}, {
				display : 'Bairro',
				name : 'bairro',
				width : 120,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cep',
				name : 'cep',
				width : 60,
				sortable : true,
				align : 'left'
			}, {
				display : 'Cidade',
				name : 'cidade',
				width : 90,
				sortable : true,
				align : 'left'
			}, {
				display : 'Principal',
				name : 'principal',
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
			singleSelect: true
		});
	});
	
</script>
    

</head>

<div id="manutencaoEnderecos" title="Detalhes da Nota">

<div id="dialog-excluir-end" title="Excluir Endereço">
	<p>Confirma a exclusão desse endereço?</p>
</div>

<div id="effectDialog" 
	 class="ui-state-highlight ui-corner-all" 
	 style="display: none; position: absolute; z-index: 1000; width: 800px;">
<p>
	<span style="float: left;" class="ui-icon ui-icon-info"></span>
	<b id="idTextoMensagemDialog"></b>
</p>
</div>

	<form name="formEnderecos" id="formEnderecos">
		
		<input type="hidden" name="enderecoAssociacao.endereco.id" id="idEndereco">
		
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
						<a href="javascript:;">&nbsp;</a>
					</span></td>
			</tr>
			<tr>
				<td>Logradouro:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.logradouro" id="logradouro" />
				</td>
				<td>N&uacute;mero:</td>
				<td>
					<input type="text" style="width:50px" 
						   name="enderecoAssociacao.endereco.numero" id="numero" />
				</td>
			</tr>
			<tr>
				<td>Complemento:</td>
				<td>
					<input type="text" style="width:250px" 
						   name="enderecoAssociacao.endereco.complemento" id="complemento" />
				</td>
				<td>Bairro:</td>
				<td>
					<input type="text"  style="width:230px" 
						   name="enderecoAssociacao.endereco.bairro" id="bairro" />
				</td>
			</tr>
			<tr>
			<td>Cidade:</td>
			<td>
				<input type="text" style="width:250px" 
					   name="enderecoAssociacao.endereco.cidade" id="cidade" />
			</td>
			<td>UF:</td>
			<td>
				<input type="text" style="width:50px;text-transform:uppercase" 
					   name="enderecoAssociacao.endereco.uf" id="uf"  />
			</td>
			</tr>
			<tr>
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
			  	<span class="bt_add">
			  		<a href="javascript:;" onclick="incluirNovoEndereco();">Incluir Novo</a>
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
