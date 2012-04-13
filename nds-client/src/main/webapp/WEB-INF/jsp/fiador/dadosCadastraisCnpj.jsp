<script>
	function cadastrarFiadorCnpj(janela){
		
		fecharModalCadastroFiador = true;
		
		var data = "fiador.razaoSocial=" + $("#razaoSocialFiador").val() + "&" +
		           "fiador.nomeFantasia=" + $("#nomeFantasiaFiador").val() + "&" +
		           "fiador.inscricaoEstadual=" + $("#inscricaoEstadualFiador").val() + "&" +
		           "fiador.cnpj=" + $("#cnpjFiador").val() + "&" +
		           "fiador.email=" + $("#emailFiadorCnpj").val();
		
		$.postJSON("<c:url value='/cadastro/fiador/cadastrarFiadorCnpj' />", data, 
			function(result){
		
				if (result[0].tipoMensagem){
					exibirMensagem(result[0].tipoMensagem, result[0].listaMensagens);
				}
				
				if (result[1] != ""){
					$(".pessoasGrid").flexAddData({
						page: result[1].page, total: result[1].total, rows: result[1].rows
					});
					
					$("#gridFiadoresCadastrados").show();
				} else {
					$("#gridFiadoresCadastrados").hide();
				}
				
				if (result[0].tipoMensagem == "SUCCESS"){
					$(janela).dialog("close");
				}
			},
			null,
			true
		);
	}
	
	function limparDadosCadastraisCNPJ(){
		$("#razaoSocialFiador").val("");
		$("#nomeFantasiaFiador").val("");
		$("#inscricaoEstadualFiador").val("");
		$("#cnpjFiador").val("");
		$("#emailFiadorCnpj").val("");
	}
	
	$(function(){
		$("#cnpjFiador").mask("99.999.999/9999-99");
	});
</script>
<table width="754" cellpadding="2" cellspacing="2" style="text-align:left;">
	<tr>
		<td nowrap="nowrap"><strong>Início de Atividade:</strong></td>
		<td>${dataAtual}</td>
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td width="120">Razão Social:</td>
		<td width="240"><input type="text" style="width:230px " id="razaoSocialFiador" /></td>
		<td width="136">Nome Fantasia:</td>
		<td width="230"><input type="text" style="width:230px " id="nomeFantasiaFiador" /></td>
	</tr>
	<tr>
		<td>Inscrição Estadual:</td>
		<td><input type="text" style="width:230px " id="inscricaoEstadualFiador" /></td>
		<td>CNPJ:</td>
		<td><input type="text" style="width:230px " id="cnpjFiador" /></td>
	</tr>
	<tr>
		<td>E-mail:</td>
		<td><input type="text" style="width:230px" id="emailFiadorCnpj" /></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>