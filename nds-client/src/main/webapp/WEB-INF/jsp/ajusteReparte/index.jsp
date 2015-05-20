<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">
<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="scripts/ajusteReparte.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCota = new PesquisaCota();

$(function(){
	ajusteReparteController.init();
});
</script>

<style>
#effect {
	z-index: 1;
}
</style>

</head>

<body>
<div id="dialog-excluir" title="Excluir ajuste">
	<p>Confirma a exclusão deste ajuste?</p>
</div>

<form action="/ajuste" id="excluir_form">
	<div id="dialog-confirmacao" title="Inserir cota" style="display: none;">
		<p>Confirma a inserção desta cota para ajuste?</p>
	</div>
</form>

<div id="dialog-segmentos" title="Selecionar Segmentos"
		style="display: none;">
	<fieldset style="width: 400px !important;">
		<legend>Lista de Segmentos</legend>

		<table class="lstSegmentosGrid">
			<tr>
				<td>
					<select name="tipoSegmento1" id="tipoSegmento1" style="width: 200px;" onblur="ajusteReparteController.validarTipoSegmento1()" >
						<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
							<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input name="segmento1" id="segmento1"
						onblur="ajusteReparteController.formatarAjusteAplicadoSegmento1(); "
						type="text" style="width: 60px; float: left; margin-right: 5px;" />
				</td>
			</tr>

			<tr>
				<td>
					<select name="tipoSegmento2" id="tipoSegmento2" style="width: 200px;" onblur="ajusteReparteController.validarTipoSegmento2()" >
						<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
							<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input name="segmento2" id="segmento2" onblur="ajusteReparteController.formatarAjusteAplicadoSegmento2();" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
				</td>
			</tr>

			<tr>
				<td>
					<select name="tipoSegmento3" id="tipoSegmento3" style="width: 200px;" onblur="ajusteReparteController.validarTipoSegmento3()" >
						<option selected="selected">Selecione...</option>
						<c:forEach items="${listaSegmentos}" var="segmento">
							<option value="${segmento.id}">${segmento.descricao}</option>
						</c:forEach>
					</select>
				</td>
				<td>
					<input name="segmento3" id="segmento3" onblur="ajusteReparteController.formatarAjusteAplicadoSegmento3();" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
				</td>
			</tr>
		</table>
	</fieldset>
</div>

<div id="dialog-novo-ajusteReparte" title="Ajuste" style="display: none;">
	<fieldset style="width: 585px !important;">
		<legend>Ajuste de Reparte</legend>
		<table width="575" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<td width="25">Cota:</td>
				<td width="99">
					<input name="ajuste-reparte-numeroCota" id="ajuste-reparte-numeroCota" type="text" style="width: 60px; float: left; margin-right: 5px;"/>

					<span class="classPesquisar">
						<a href="javascript:;">&nbsp;</a>
					</span>
				</td>
				<td width="30">Nome:</td>
				<td width="398">
					<input name="ajuste-reparte-nomeCota" id="ajuste-reparte-nomeCota" type="text" style="width: 160px;" 
										   onkeyup="pesquisaCota.autoCompletarPorNome('#ajuste-reparte-nomeCota');" 
		 	   			   				   onblur="pesquisaCota.pesquisarPorNomeCota('#ajuste-reparte-numeroCota', '#ajuste-reparte-nomeCota');"/>
				</td>
			</tr>
		</table>

		<table width="575" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<br>
				<br>
				<td colspan="8"> <strong>Formas de Ajuste</strong>
				</td>
			</tr>
			<tr>
				<td width="20">
					<input type="radio" name="formaAjuste" value="AJUSTE_HISTORICO" id="AJUSTE_HISTORICO"
							   onclick = "ajusteReparteController.filtroPorHistorico();" />
				</td>
				<td width="102">Ajuste Histórico</td>

				<td width="20">
					<input type="radio" name="formaAjuste" value="AJUSTE_VENDA_MEDIA" id="AJUSTE_VENDA_MEDIA"
							   onclick = "ajusteReparteController.filtroPorVenda();" />
				</td>
				<td width="88">Venda Média +</td>

				<td width="20">
					<input type="radio" name="formaAjuste" value="AJUSTE_ENCALHE_MAX" id="AJUSTE_ENCALHE_MAX"
							   onclick = "ajusteReparteController.filtroPorEncalhe();" />
				</td>
				<td width="139">% de Encalhe Máximo</td>

				<td width="20">
					<input type="radio" name="formaAjuste" id="formaAjusteAjusteSegmento" value="AJUSTE_SEGMENTO" />
				</td>
				<td width="125">Ajuste por Segmento</td>
			</tr>
		</table>

		<table width="360" border="0" cellspacing="1" cellpadding="2">
			<tr>
				<td width="10"></td>
				<td width="102">
					<span class="vlrPerc" >
						<input name="ajuste_historico_input" id="AJUSTE_HISTORICO_input" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoHistorico();" type="text" style="width: 50px;" size="5"/>
					</span>
				</td>

				<td width=400></td>
				<td width="100">
					<span class="vdaMedia" >
						<input name="ajuste_venda_media_input" id="AJUSTE_VENDA_MEDIA_input" value="1" style="display: none;" type="text" style="width: 50px;" size="5" />
					</span>
				</td>
				<td width="350"></td>
				<td width="250">
					<span class="encalheMaximo"	>
						<input name="ajuste_encalhe_max_input" id="AJUSTE_ENCALHE_MAX_input" value="1" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoEncalhe();" type="text" style="width: 50px;" size="5" />
					</span>
				</td>
			</tr>
		</table>

		<table id="tableSegmentos" style="display: none;">
			<tr>
				<td width="80"> <strong>Segmentos:</strong>
				</td>

				<tr id="tr_exibirSegmento1">
					<td width="220" id="colSegmento1">Segmento 1:</td>
					<td width="40">
						<input name="exibirSegmento1" id="exibirSegmento1" type="text" style="width: 30px;" align="middle"/>
						<td width="25">
							<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir" border="0"
		           			 onclick = "ajusteReparteController.limparExibicaoSegmento(exibirSegmento1, tipoSegmento1, segmento1, 'tr_exibirSegmento1');" />
						</td>
					</tr>

					<tr id="tr_exibirSegmento2">
						<td width="220" id="colSegmento2">Segmento 2:</td>
						<td width="40">
							<input name="exibirSegmento2" id="exibirSegmento2" type="text" style="width: 30px;" align="middle"/>
							<td width="25">
								<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir" border="0" 
							 onclick = "ajusteReparteController.limparExibicaoSegmento(exibirSegmento2, tipoSegmento2, segmento2, 'tr_exibirSegmento2');" />
							</td>
						</tr>

						<tr id="tr_exibirSegmento3">
							<td width="220" id="colSegmento3">Segmento 3:</td>
							<td width="40">
								<input name="exibirSegmento3" id="exibirSegmento3" type="text" style="width: 30px;" align="middle"/>
								<td width="25">
									<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir" border="0"
                    		 onclick = "ajusteReparteController.limparExibicaoSegmento(exibirSegmento3, tipoSegmento3, segmento3, 'tr_exibirSegmento3');" />
								</td>
							</tr>

						</tr>
					</table>

					<table width="575" border="0" cellpadding="2" cellspacing="1">
						<tr>
							<br>
							<br>
							<td width="89">Motivo do Ajuste:</td>
							<td width="466">
								<select name="motivoAjuste" id="motivoAjuste" style="width: 200px;" >
									<option selected="selected">Selecione...</option>
									<c:forEach items="${listaMotivosStatusCota}" var="motivo">
										<option value="${motivo.key}">${motivo.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>

						<tr>
							<td>Período:</td>
							<tr>
								<td width="100">
									<input name="periodo1" id="dataInicioAjusteReparte" type="text" value="${dataAtual}" style="width: 70px;"/>
									<td width="100">
										Até:
										<input name="periodo2" id="dataFimAjusteReparte" type="text" value="${dataAtual}" style="width: 70px;"/>
									</td>
								</tr>
							</tr>

						</table>
					</fieldset>
				</div>

				<div id="dialog-editar" title="Ajuste" style="display: none;">
					<fieldset style="width: 585px !important;">
						<legend>Editar Ajuste de Reparte</legend>
						<table width="575" border="0" cellspacing="1" cellpadding="2">
							<tr>
								<td width="25">Cota:</td>
								<td width="99">
									<input name="numeroCota" id="numeroCotaEditar" type="text" style="width: 60px; float: left; margin-right: 5px;"/>
									<span class="classPesquisar">
										<a href="javascript:;">&nbsp;</a>
									</span>
								</td>
								<td width="30">Nome:</td>
								<td width="398">
									<input name="nomeCota" id="nomeCotaEditar" type="text" style="width: 160px;"/> 
								</td>
							</tr>
							</table>

							<table width="575" border="0" cellspacing="1" cellpadding="2">
								<tr>
									<br>
									<br>
									<td colspan="8">
										<strong>Formas de Ajuste</strong>
									</td>
								</tr>
								<tr>
									<td width="20">
										<input type="radio" name="formaAjusteEditar" value="AJUSTE_HISTORICO" id="AJUSTE_HISTORICO_editar" 
							   onclick = "ajusteReparteController.filtroPorHistoricoEditar();" />
									</td>
									<td width="102">Ajuste Histórico</td>

									<td width="20">
										<input type="radio" name="formaAjusteEditar" value="AJUSTE_VENDA_MEDIA" id="AJUSTE_VENDA_MEDIA_editar" 
							   onclick = "ajusteReparteController.filtroPorVendaEditar();" />
									</td>
									<td width="88">Venda Média +</td>

									<td width="20">
										<input type="radio" name="formaAjusteEditar" value="AJUSTE_ENCALHE_MAX" id="AJUSTE_ENCALHE_MAX_editar" 
							   onclick = "ajusteReparteController.filtroPorEncalheEditar();" />
									</td>
									<td width="139">% de Encalhe Máximo</td>

									<td width="20">
										<input type="radio" name="formaAjusteEditar" value="AJUSTE_SEGMENTO" id="formaAjusteAjusteSegmento_editar" 
							   onclick = "ajusteReparteController.filtroPorSegmentoEditar();" />
									</td>
									<td width="125">Ajuste por Segmento</td>
								</tr>
							</table>

							<table width="360" border="0" cellspacing="1" cellpadding="2">
								<tr>
									<td width="10"></td>
									<td width="102">
										<span class="vlrPerc" >
											<input name="ajuste_historico_input" id="AJUSTE_HISTORICO_editar_input" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoHistorico();" type="text" style="width: 10px;" size="5" />
										</span>
									</td>

									<td width="400"></td>
									<td width="100">
										<span class="vdaMedia" >
											<input name="ajuste_venda_media_input" id="AJUSTE_VENDA_MEDIA_editar_input" value="1" style="display: none;"  type="text" style="width: 10px;" size="5"/>
										</span>
									</td>
									<td width="350"></td>
									<td width="250">
										<span class="encalheMaximo"	>
											<input name="ajuste_encalhe_max_input" id="AJUSTE_ENCALHE_MAX_editar_input" value="1" style="display: none;" onblur="ajusteReparteController.formatarAjusteAplicadoEncalheEditar();" type="text" style="width: 10px;" size="5"/>
										</span>
									</td>
								</tr>
							</table>

							<table id="tableSegmentosEditar" style="display: none;">

								<tr>
									<td width="80">
										<strong>Segmentos:</strong>
									</td>

									<td width="60" id="colSegmento1Editar">Segmento 1:</td>
									<td width="40">
										<input name="exibirSegmento1" id="exibirSegmento1Editar" type="text" style="width: 30px;" align="middle"/>
										<td width="25">
											<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" alt="Excluir" border="0"
		           			 onclick = "ajusteReparteController.limparExibicaoSegmento(exibirSegmento1, tipoSegmento1, segmento1);" />
										</td>
									</tr>
								</table>

								<table width="575" border="0" cellpadding="2" cellspacing="1">
									<tr>
										<br>
										<br>
										<td width="89">Motivo do Ajuste:</td>
										<td width="466">
											<select name="motivoAjuste" id="motivoAjusteEditar" style="width: 200px;" >
												<option selected="selected">Selecione...</option>
												<c:forEach items="${listaMotivosStatusCota}" var="motivo">
													<option value="${motivo.key}">${motivo.value}</option>
												</c:forEach>
											</select>
										</td>
									</tr>

									<tr>
										<td>Período:</td>
										<tr>
											<td width="100">
												<input name="periodo1" id="dataInicioEditar" type="text" value="${dataAtual}" style="width: 70px;"/>
												<td width="100">
													Até:
													<input name="periodo2" id="dataFimEditar" type="text" value="${dataAtual}" style="width: 70px;"/>
												</td>
											</tr>
										</tr>

									</table>
								</fieldset>
							</div>
							<div class="areaBts">
								<div class="area">
									<span class="bt_novos">
										<a href="javascript:;" isEdicao="true" onclick="ajusteReparteController.incluirAjuste();" rel="tipsy" title="Novo">
											<img src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />

										</a>
									</span>

									<span class="bt_arq" >
										<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
											<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />

										</a>
									</span>

									<span class="bt_arq" >
										<a href="${pageContext.request.contextPath}/distribuicao/ajusteReparte/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
											<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" hspace="5" border="0" />

										</a>
									</span>
								</div>
							</div>

							<div class="linha_separa_fields">&nbsp;</div>

							<div class="grids" style="display: block;">

								<div class="porSegmento" style="display: block;">
									<fieldset class="classFieldset">
										<legend>Cotas em Ajuste Reparte</legend>
										<table class="cotasAjusteGrid"></table>
									</fieldset>
								</div>

							</div>
							<div class="linha_separa_fields">&nbsp;</div>
</body>