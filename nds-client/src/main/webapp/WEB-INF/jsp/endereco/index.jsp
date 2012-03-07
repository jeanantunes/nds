<head>
	<script>
	
		function processarResultadoConsultaEndereco(data) {
			
			if (data.mensagens) {

				exibirMensagemDialog(
					data.mensagens.tipoMensagem, 
					data.mensagens.listaMensagens
				);

				$(".grids").hide();

				return;
			}
			
			var i;

			for (i = 0 ; i < data.rows.length; i++) {

				var lastIndex = data.rows[i].cell.length;
				
				data.rows[i].cell[lastIndex - 1] =					
					data.rows[i].cell[lastIndex - 1] ? '<img src="${pageContext.request.contextPath}/images/ico_check.gif" border="0px"/>'
													 : '&nbsp;';

				data.rows[i].cell[lastIndex] = getAction(data.rows[i].id);
			}

			if ($(".grids").css('display') == 'none') {
					
				$(".grids").show();
			}

			return data;
		}

		function getAction(idEndereco) {

			return '<a href="javascript:;" onclick="editarEndereco(' + idEndereco + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Editar endereço">' +
					'<img src="${pageContext.request.contextPath}/images/ico_editar.gif" border="0px"/>' +
					'</a>' +
					'<a href="javascript:;" onclick="pesquisarDetalhesNota(' + idEndereco + ')" ' +
					' style="cursor:pointer;border:0px;margin:5px" title="Excluir endereço">' +
					'<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" border="0px"/>' +
					'</a>';
		}
	
		function popularGrid() {
		
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
					sortable : true,
					align : 'center'
				}],
				width : 770,
				height : 150,
				singleSelect: true
			});
		}
		
		function editarEndereco(idEndereco) {
			
			alert(idEndereco);
		}
		
		function incluirNovoEndereco() {

			var formData = $("#formEnderecos").serializeArray();
			
			formData.push({
				name: "endereco.id",
				value: 5
			});

			$(".enderecosGrid").flexOptions({
				url : '<c:url value="/cadastro/endereco/incluirNovoEndereco" />', 
				params: formData
			});
			
			$(".enderecosGrid").flexReload();
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
		};
	</script>
</head>

<body>
	
	<div class="container" >


		<div id="manutencaoEnderecos" title="Detalhes da Nota" style="display:none">
		
		<form name="formEnderecos" id="formEnderecos">
			
			<table width="754" cellpadding="2" cellspacing="2" style="text-align:left ">
				<tr>
					<td width="99">Tipo Endereço:</td>
					<td width="310">
						<select  style="width:157px" name="endereco.tipoEndereco">
							<option>COMERCIAL</option>
							<option>Local de Entrega</option>
							<option>Residencial</option>
							<option>Cobrança</option>
						</select>
					</td>
					<td width="76">CEP:</td>
					<td width="241">
					<input type="text" style="float:left; margin-right:5px;" name="endereco.cep" />
						<span class="classPesquisar" title="Pesquisar Cep.">
							<a href="javascript:;">&nbsp;</a>
						</span></td>
				</tr>
				<tr>
					<td>Logradouro:</td>
					<td>
						<input type="text" style="width:250px" name="endereco.logradouro" />
					</td>
					<td>N&uacute;mero:</td>
					<td>
						<input type="text" style="width:50px" name="endereco.numero" />
					</td>
				</tr>
				<tr>
					<td>Complemento:</td>
					<td>
						<input type="text" style="width:250px" name="endereco.complemento" />
					</td>
					<td>Bairro:</td>
					<td>
						<input type="text"  style="width:230px" name="endereco.bairro" />
					</td>
				</tr>
				<tr>
					<td>Cidade:</td>
					<td>
						<input type="text" style="width:250px" name="endereco.cidade" />
					</td>
					<td>UF:</td>
					<td>
						<input type="text" style="width:50px" name="endereco.uf" />
					</td>
					</tr>
					<tr>
					  <td>Principal:</td>
					  <td>
					  	<input type="checkbox" name="checkbox" id="checkbox" name="isEnderecoPrincipal"/>
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

	<input type="button" value="abrir modal" onclick="popup();"/>

	</div>
	<script>
		
		$(document).ready(function() {
			popularGrid();	
		});
		
	</script>
</body>