<head>

<style>
</style>

<script type="text/javascript" src='scripts/dividirEstudo.js'></script>

<script>
    $(function() {
	$("#dataDistribuicao").datepicker({
	    showOn : "button",
	    buttonImage : "${pageContext.request.contextPath}/images/calendar.gif",
	    buttonImageOnly : true,
	    dateFormat : 'dd/mm/yy'
	});
	$("#dataLancamentoSegundoEstudo").datepicker({
	    showOn : "button",
	    buttonImage : "${pageContext.request.contextPath}/images/calendar.gif",
	    buttonImageOnly : true,
	    dateFormat : 'dd/mm/yy'
	});
    });
</script>
</head>

<body>
	<div id="dialog-cancelar" title="Cancelar Opera&ccedil;&atilde;o"
		style="display: none;">
		<fieldset style="width: 320px !important;">
			<legend>Cancelar Opera&ccedil;&atilde;o</legend>
			<p>Confirma o Cancelamento desta Opera&ccedil;&atilde;o?</p>
		</fieldset>
	</div>

	<div id="dialog-novo" title="Nova Base">
		<fieldset style="width: 510px !important;">
			<legend>Pesquisa de Bases</legend>
			<table width="500" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="37">C&oacute;digo:</td>
					<td width="64"><input type="text" name="textfield"
						id="textfield" style="width: 60px; text-align: center;" /></td>
					<td width="41">Produto:</td>
					<td width="178"><input type="text" name="textfield2"
						id="textfield2" style="width: 160px; text-align: center;" /></td>
					<td width="41">Edi&ccedil;&atilde;o:</td>
					<td width="101"><input type="text" name="textfield"
						id="textfield" style="width: 60px; text-align: center;" /></td>
				</tr>
			</table>
		</fieldset>
	</div>

	<br clear="all" />
	<br />

	<div class="corpo">
		<fieldset class="classFieldset">
			<legend>Dividir Estudos</legend>

			<table width="950" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td valign="top">
						<fieldset style="width: 380px; margin-top: 5px; margin-left: 20px">
							<legend>Estudo Original</legend>
							<table width="376" border="0" cellspacing="2" cellpadding="2"
								class="filtro">
								<tr>
									<td width="97">Estudo:</td>
									<td width="257"><input type="text"
										name="numeroEstudoOriginal" id="numeroEstudoOriginal"
										style="width: 100px;" /></td>
								</tr>
								<tr>
									<td>C&oacute;digo:</td>
									<td><input type="text" name="codigoProduto"
										id="codigoProduto" style="width: 100px;" /></td>
								</tr>
								<tr>
									<td>Produto:</td>
									<td><input type="text" name="nomeProduto" id="nomeProduto"
										style="width: 200px;" /></td>
								</tr>
								<tr>
									<td>Edi&ccedil;&atilde;o:</td>
									<td><input type="text" name="edicaoProduto"
										id="edicaoProduto" style="width: 100px;" /></td>
								</tr>
								<%--
								<tr>
									<td>Classifica&ccedil;&atilde;o:</td>
									<td><input type="text" name="classificacao" id="classificacao"
										style="width: 200px;" /></td>
								</tr>
								 --%>
								<tr>
									<td>Data Distribui&ccedil;&atilde;o:</td>
									<td><input type="text" name="dataDistribuicao"
										id="dataDistribuicao" style="width: 80px;" /></td>
								</tr>
								<tr>
									<td colspan="2"><table width="100%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td width="48%">Percentuais da Divis&atilde;o:</td>
												<td width="16%"><input type="text"
													name="percentualDivisaoPrimeiroEstudo"
													id="percentualDivisaoPrimeiroEstudo" style="width: 40px;" /></td>
												<td width="5%">%</td>
												<td width="31%"><input type="text"
													name="percentualDivisaoSegundoEstudo"
													id="percentualDivisaoSegundoEstudo" style="width: 40px;"
													onchange="dividirEstudo.gerarDivisao();" /></td>
											</tr>
										</table></td>
								</tr>
								<tr>
									<td colspan="2"><table width="99%" border="0"
											cellspacing="0" cellpadding="0">
											<tr>
												<td width="49%">N&atilde;o dividir reparte menor que:</td>
												<td width="51%"><input type="text"
													name="quantidadeReparte" id="quantidadeReparte"
													style="width: 40px;"
													onchange="dividirEstudo.gerarDivisao();" /> exs</td>
											</tr>
										</table></td>
								</tr>
							</table>
						</fieldset>

					</td>
					<td style="width: 20px;">&nbsp;</td>
					<td valign="top">
						<fieldset style="width: 430px; margin-left: 10px;">
							<legend>Estudos a serem Divididos</legend>
							<table width="420" border="0" cellspacing="2" cellpadding="2"
								class="filtro">
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>Dt. Lan&ccedil;amento:</td>
								</tr>
								<tr>
									<td width="84">Estudo 1:</td>
									<td width="102"><input type="text" style="width: 80px;"
										name="numeroPrimeiroEstudo" id="numeroPrimeiroEstudo" /></td>
									<td width="46">Reparte:</td>
									<td width="50"><input type="text"
										name="repartePrimeiroEstudo" id="repartePrimeiroEstudo"
										style="width: 50px;" /></td>
									<td width="106">10/05/2012</td>
								</tr>
								<tr>
									<td>Estudo 2:</td>
									<td><input type="text" style="width: 80px;"
										name="numeroSegundoEstudo" id="numeroSegundoEstudo" /></td>
									<td>Reparte:</td>
									<td><input type="text" name="reparteSegundoEstudo"
										id="reparteSegundoEstudo" style="width: 50px;" /></td>
									<td><input type="text" name="dataLancamentoSegundoEstudo"
										id="dataLancamentoSegundoEstudo" style="width: 80px;" /></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
							</table>
						</fieldset>
					</td>
				</tr>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>

			<div class="linha_separa_fields">&nbsp;</div>

			<span class="bt_novos"><a
				href="javascript:$('#workspace').tabs('remove', $('#workspace').tabs('option', 'selected'));"><img
					src="${pageContext.request.contextPath}/images/seta_voltar.gif"
					alt="Voltar" hspace="5" border="0" />Voltar</a></span> <span class="bt_novos"><a
				href="javascript:history.back(-1);"><img
					src="${pageContext.request.contextPath}/images/ico_excluir.gif"
					alt="Cancelar" hspace="5" border="0" />Cancelar</a></span> <span
				class="bt_novos"><a href="#"
				onclick="dividirEstudo.confirmar();"><img
					src="${pageContext.request.contextPath}/images/ico_check.gif"
					alt="Confirmar" hspace="5" border="0" />Confirmar</a></span> <span
				class="bt_novos"><a href="analise_2.htm"><img
					src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif"
					alt="An&aacute;lise" hspace="5" border="0" />An&aacute;lise</a></span>

		</fieldset>

	</div>


</body>
</html>
