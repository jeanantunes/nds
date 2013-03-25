<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicaoManual.js"></script>

<script type="text/javascript">
	$(function() {
		distribuicaoManualController.init();
	});
</script>

<style type="text/css">
	.inputGridCota {
		width:50px;
		float:left;
		margin: 2px 0 10px 0;
	}
	.textoGridCota {
		text-align: left;
		width: 90px;
		height: 23px;
	}
</style>
</head>

<body>
	<div id="dialog-cancelar" title="Cancelar Operação"
		style="display: none;">
		<fieldset style="width: 320px !important;">
			<legend>Cancelar Operação</legend>
			<p>Confirma o Cancelamento desta Operação?</p>
		</fieldset>
	</div>

	<div class="corpo">
		<br clear="all" /> <br />

		<div class="container">

			<div id="effect" style="padding: 0 .7em;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info"></span> <span
						class="ui-state-default ui-corner-all" style="float: right;">
						<a href="javascript:;" class="ui-icon ui-icon-close">&nbsp;</a>
					</span> <b>Box < evento > com < status >.</b>
				</p>
			</div>
			<fieldset class="classFieldset">
				<legend>Distribuição Manual</legend>

				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td valign="top">
							<fieldset
								style="width: 380px; margin-top: 5px; margin-left: 20px">
								<legend>Dados da Publicação </legend>
								<table width="376" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="106"><strong>Estudo:</strong></td>
										<td width="256">${produto.idEstudo}</td>
									</tr>
									<tr>
										<td><strong>Código:</strong></td>
										<td>${produto.codigoProduto}</td>
									</tr>
									<tr>
										<td><strong>Produto:</strong></td>
										<td>${produto.nomeProduto}</td>
									</tr>
									<tr>
										<td><strong>Edição:</strong></td>
										<td>${produto.numeroEdicao}</td>
									</tr>
									<tr>
										<td><strong>Classificação:</strong></td>
										<td>${produto.classificacao}</td>
									</tr>
									<tr>
										<td><strong>Data Distribuição:</strong></td>
										<td>${produto.dataLancto}</td>
									</tr>
								</table>
							</fieldset>

						</td>
						<td style="width: 20px;">&nbsp;</td>
						<td valign="top">
							<fieldset style="width: 400px;">
								<legend>Distribuição Manual</legend>
								<table class="estudosManuaisGrid"></table>

								<table width="394" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="109"><strong>Total Distribuido:</strong></td>
										<td width="285"><span id="totalDistribuido"></span></td>
									</tr>
									<tr>
										<td><strong>Total A Distribuir:</strong></td>
										<td>${produto.reparte}</td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
				<span class="bt_novos">
					<a href="#" onclick="distribuicaoManualController.voltar();">
						<img src="${pageContext.request.contextPath}/images/seta_voltar.gif"
						alt="Voltar" hspace="5" border="0" />Voltar</a>
				</span>
				<span class="bt_novos">
					<a href="#" onclick="distribuicaoManualController.voltar();">
						<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
						alt="Cancelar" hspace="5" border="0" />Cancelar</a>
				</span>
				<span class="bt_novos">
					<a href="../Distribuicao/matriz_distribuicao.htm">
						<img src="${pageContext.request.contextPath}/images/ico_check.gif"
						alt="Confirmar" hspace="5" border="0" />Gerar Estudo</a>
				</span>
				<span class="bt_novos"><a href="analise_2.htm">
					<img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif"
						alt="Confirmar" hspace="5" border="0" />Análise</a>
				</span>
			</fieldset>
		</div>
	</div>
</body>