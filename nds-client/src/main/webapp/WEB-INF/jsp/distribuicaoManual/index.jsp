<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/distribuicaoManual.js"></script>
<script type="text/javascript" charset="utf8" src="${pageContext.request.contextPath}/scripts/usuario/usuario.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.form.js"></script>

<script type="text/javascript">
	$(function() {
		distribuicaoManual.init();
	});
</script>

<style type="text/css">
	.inputGridCota {
		width:50px;
		float:left;
		margin: 0px 0 10px 0;
	}
	.textoGridCota {
		text-align: center;
		width: 90px;
		height: 23px;
	}
</style>
</head>

<div id="distribuicaoManualTelaAnalise" />
<div id="distribuicaoManualContent">

<body>
	<div id="dialog-cancelar-estudo" title="Cancelar Estudo" style="display: none;">
		<p>Tem certeza que deseja cancelar este estudo?</p>
	</div>
	<div id="dialog-voltar" title="Voltar" style="display: none;">
		<p>Tem certeza que deseja voltar para a matriz de distribuição e cancelar este estudo?</p>
	</div>
	<div id="dialog-saldo" title="Confirmação de Saldo" style="display: none;">
		<p>Não foram distribuídos todos os exemplares, tem certeza que deseja gerar o estudo mesmo assim?</p>
	</div>
	<div id="dialog-status-suspenso" title="Cota com status suspenso" style="display: none;">
		<p>Cota suspensa, deseja continuar?</p>
		<p>Posteriormente será solicitado senha.</p>
	</div>

	<div id="dialog-cancelar" title="Cancelar Operação"
		style="display: none;">
		<fieldset style="width: 320px !important;">
			<legend>Cancelar Operação</legend>
			<p>Confirma o Cancelamento desta Operação?</p>
		</fieldset>
	</div>
	
	<!-- DIALOG  UPLOAD -->		
	 <form id="formUploadLoteDistbManual"  method="post" enctype="multipart/form-data">
		<div id="modalUploadArquivo-DistbManual" title="Adicionar em Lote" style="display:none;">
			<p>Utilize o modelo de exemplo para fazer upload para o sistema: </p>
			<p ><span class="bt_novos"><a href="${pageContext.request.contextPath}/modelos/modelo_distribuicao_santos.xls" rel="tipsy" title="Download Modelo">
			<img align="center" src="images/ico_excel.png" hspace="5" border="0" /> Modelo de exemplo</a></span></p>
			<br><br><br>
			<hr>
			<p>Selecione um arquivo para upload:</p>
			<br>
			<p align="center"><input type="file" id="excelFileDistbManual" name="excelFileDistbManual" style="width:300px"/></p>
			
			<input type="hidden" name="estudoDTO.produtoEdicaoId" id="produtoEdicaoIdXLS" value=""/>
			<input type="hidden" name="estudoDTO.reparteDistribuir" id="reparteDistribuirXLS" value=""/>
			<input type="hidden" name="estudoDTO.reparteDistribuido" id="reparteDistribuidoXLS" value=""/>
			<input type="hidden" name="estudoDTO.dataLancamento" id="dataLancamentoXLS" value=""/>
			<input type="hidden" name="estudoDTO.lancamentoId" id="lancamentoIdXLS" value=""/>
			<input type="hidden" name="estudoDTO.reparteTotal" id="reparteTotalXLS" value=""/>
			
		</div>		
	  </form>
	
	<!-- DIALOG MSG UPLOAD -->		
	<div id="dialog-msg-upload" title="Adicionar em Lote" style="display:none;">
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
			<fieldset class="classFieldset" >
				<legend>Distribuição Manual</legend>

				<table width="950" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td valign="top">
							<fieldset
								style="width: 380px; margin-top: 5px; margin-left: 20px">
								<input type="hidden" id="idProdutoEdicao" value="${produto.idProdutoEdicao}"/>
								<input type="hidden" id="periodo" value="${produto.periodo}"/>
								<legend>Dados da Publicação </legend>
								<table width="376" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="106"><strong>Estudo:</strong></td>
										<td width="256" id="distrinuicao-manual-estudo">${produto.idEstudo}</td>
									</tr>
									<tr>
										<td><strong>Código:</strong></td>
										<td id="distrinuicao-manual-codigoProduto">${produto.codigoProduto}</td>
									</tr>
									<tr>
										<td><strong>Produto:</strong></td>
										<td id="distrinuicao-manual-nomeProduto">${produto.nomeProduto}</td>
									</tr>
									<tr>
										<td><strong>Edição:</strong></td>
										<td id="distrinuicao-manual-numeroEdicao">${produto.numeroEdicao}</td>
									</tr>
									<tr>
										<td><strong>Classificação:</strong></td>
										<td id="distrinuicao-manual-classificacao">${produto.classificacao}</td>
									</tr>
									<tr>
										<td><strong>Data Distribuição:</strong></td>
										<td id="distrinuicao-manual-dataLancamento">${produto.dataLancto}</td>
									</tr>
								</table>
							</fieldset>

						</td>
						<td style="width: 15px;">&nbsp;</td>
						<td valign="top">
							<fieldset style="width: 450px;" id="fieldDistribuicao-cotas">
								<legend>Distribuição Manual</legend>
								<table class="estudosManuaisGrid"></table>
								
								<input type="hidden" id="reparteInicial" value="${produto.reparte}"/>
								<table width="394" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="109"><strong>Total Distribuido:</strong></td>
										<td width="285"><span id="distrinuicao-manual-totalDistribuido"></span></td>
									</tr>
									<tr>
										<td><strong>Total A Distribuir:</strong></td>
										<td id="distrinuicao-manual-repDistribuir">${produto.reparte}</td>
									</tr>
									<tr>
										<td><strong>Total Geral:</strong></td>
										<td id="distrinuicao-manual-repGeral"></td>
									</tr>
								</table>
							</fieldset>
						</td>
					</tr>
				</table>
				<span class="bt_novos">
					<a href="javascript:;" onclick="distribuicaoManual.voltar();">
						<img src="${pageContext.request.contextPath}/images/seta_voltar.gif"
						alt="Voltar" hspace="5" border="0" />Voltar</a>
				</span>
				<span class="bt_novos">
					<a href="javascript:;" id="distbManual_cancelar" onclick="distribuicaoManual.cancelar();">
						<img src="${pageContext.request.contextPath}/images/ico_excluir.gif"
						alt="Cancelar" hspace="5" border="0" />Cancelar</a>
				</span>
				<span class="bt_novos">
					<a href="javascript:;" id="distbManual_gerarEstudo" onclick="distribuicaoManual.gerarEstudo();">
						<img src="${pageContext.request.contextPath}/images/ico_check.gif"
						alt="Confirmar" hspace="5" border="0" />Gerar Estudo</a>
				</span>
				<span class="bt_novos">
					<a href="javascript:;" onclick="distribuicaoManual.analisar();">
						<img src="${pageContext.request.contextPath}/images/ico_copia_distrib.gif"
						alt="Confirmar" hspace="5" border="0" />Análise</a>
				</span>
				<span class="bt_novos">
					<a href="javascript:;" id="distbManual_importacao" onclick="distribuicaoManual.add_lote();">
						<img src="${pageContext.request.contextPath}/images/ico_excel.png"
						alt="Confirmar" hspace="5" border="0" />Importar distribuição em lote</a>
				</span>
			</fieldset>
		</div>
	</div>
</body></div>
