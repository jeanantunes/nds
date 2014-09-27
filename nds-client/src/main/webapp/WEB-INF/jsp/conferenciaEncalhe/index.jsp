<input id="permissaoAlteracao" type="hidden" value="${permissaoAlteracao}">

<head>

	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<title>Conferencia Encalhe</title>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>
	
	<script type="text/javascript" src='<c:url value="/"/>/scripts/jquery.justLetter.js'></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/produtoEdicao.js"></script>
	
	<script type="text/javascript" src='<c:url value="/"/>/scripts/scriptConferenciaEncalhe.js'></script>

	<script type="text/javascript" src='<c:url value="/"/>/scripts/pesquisaConferenciaEncalhe.js'></script>
	
	
	
	<style type="text/css">
		._dados:hover{
			background: none repeat scroll 0 0 #D9EBF5;
		}
		
		.td {
			font-size: 14px;
		}
	</style>
	
	<script type="text/javascript">

		var VEIO_DO_BT_BOX_ENCALHE = false;
	
		$(function(){
			
			ConferenciaEncalhe.init();
			
			bloquearItensEdicao(ConferenciaEncalhe.workspace);
			
		});
		
	</script>
	
</head>

<body>

	<jsp:include page="../messagesDialog.jsp">
		<jsp:param value="idTelaConferenciaEncalhe" name="messageDialog"/>
	</jsp:include>

	<jsp:include page="dialog.jsp" />
		
		<div class="areaBts" style="height:38px">
			<div class="area">
				<fieldset class="classFieldset" style="height:25px">
					<table width="950" border="0" cellspacing="1" cellpadding="1">
						<tr>
							<td width="20">
								<span class="bt_novos">
									<a href="javascript:;" onclick="ConferenciaEncalhe.irParaContigencia()" rel="tipsy" title="Contingência">
										<img border="0" src="${pageContext.request.contextPath}/images/ico_expedicao_box.gif" />
									</a>
								</span>
							</td>			
							<td width="80">
								<span class="bt_novos">
									<a href="javascript:;" onclick="ConferenciaEncalhe.abrirModalLogadoDoBotao()" rel="tipsy" title="Alterar BOX Encalhe">
										<img border="0" width="40" height="16" src="${pageContext.request.contextPath}/images/bt_operacao_box.png" />
									</a>
								</span>
							</td>	
																
							<td width="169">&nbsp;</td>
							<td class="atalhosCE" width="60" align="center" bgcolor="#F4F4F4"><strong>Atalhos:</strong></td>
							<td class="atalhosCE" width="119" bgcolor="#F8F8F8"><strong>F6</strong>-Nova Nota Fiscal</td>
							<td class="atalhosCE" width="62" bgcolor="#F8F8F8"><strong>F8</strong>-Salvar</td>
							<td class="atalhosCE" width="145" bgcolor="#F8F8F8"><strong>F9</strong>-Finalizar Conferência</td>
							<td class="atalhosCE" width="150" bgcolor="#F8F8F8"><strong>F10</strong>-Oredenar Conferência por SM</td>
						</tr>
					</table>
				</fieldset>
			</div>
		</div>
		<fieldset class="fieldFiltro fieldFiltroItensNaoBloqueados">

			<legend> Pesquisar Encalhe</legend>

			<table width="950" border="0" cellspacing="2" cellpadding="2" class="filtro">
				<tr>
					<td width="40">Cota:</td>
					<td width="121">
						<input type="text" id="numeroCota" style="width: 80px; float: left; margin-right: 5px;" maxlength="6"/>
						<span class="classPesquisar">
							<a href="javascript:;" onclick="ConferenciaEncalhe.pesquisarCota();">&nbsp;</a>
						</span>
					</td>

					<td colspan="2">
						<span class="dadosFiltro" id="nomeCota"></span>
					</td>
					
					<td width="44"><span class="dadosFiltro">Status:</span></td>
					
					<td width="91"><span class="dadosFiltro" id="statusCota"></span></td>
					
					<td width="144">

						<span class="dadosFiltro">
							<c:choose>
								<c:when test="${tipoContabilizacaoCE eq 'VALOR'}">
									Valor CE Jornaleiro R$:
								</c:when>
								<c:when test="${tipoContabilizacaoCE eq 'EXEMPLARES'}">
									Qtde CE Jornaleiro:
								</c:when>
							</c:choose>
						</span>
						
					</td>
					
					<td width="100">
						
						<span class="dadosFiltro">

							<c:choose>
								<c:when test="${tipoContabilizacaoCE eq 'VALOR'}">
							<input isEdicao="true" type="text" name="vlrCE" id="vlrCE" style="width: 100px; text-align: right;" maxlength="255" />
								</c:when>
								<c:when test="${tipoContabilizacaoCE eq 'EXEMPLARES'}">
							<input isEdicao="true" type="text" name="qtdCE" id="qtdCE" style="width: 100px; text-align: right;" maxlength="255" />
								</c:when>
							</c:choose>
							
						</span>
						
					</td>
					
				</tr>
			</table>
			
		</fieldset>
		




		<fieldset class="fieldGrid">

			<legend>Encalhe</legend>

			<table width="950" border="0" cellspacing="1" cellpadding="2">
				<tr class="header_table">
					<td width="65" align="center" style="border-left: 1px solid #666; border-top: 1px solid #666;">Qtde</td>
					<td width="168" align="center" style="border-top: 1px solid #666;">Código de Barras</td>
					<td width="42" align="center" style="border-top: 1px solid #666;">SM</td>
					<td width="107" align="center" style="border-top: 1px solid #666; border-right: 1px solid #666;">Código</td>
					<td width="158">Produto</td>
					<td width="69" align="center">Edição</td>
					<td width="93">Preço Capa R$</td>
					<td width="79" align="center">Desc. R$</td>
					<td width="87" align="center">Valor Total R$</td>
					<td width="31">&nbsp;</td>
				</tr>
				<tr>
					<td class="class_linha_1" align="center" style="border-left: 1px solid #666; border-bottom: 1px solid #666;">
						<input name="qtdeExemplar" type="text" id="qtdeExemplar" class="input-numericPacotePadrao" style="width: 60px; text-align: center;" maxlength="255"/>
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666;">
						<input name="cod_barras" type="text" id="cod_barras_conf_encalhe" style="width: 160px;" maxlength="255"/>
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666;">
						<input name="sm" type="text" id="sm" style="width: 40px;" maxlength="255"/>
					</td>
					<td class="class_linha_1" align="center" style="border-bottom: 1px solid #666; border-right: 1px solid #666;">
						<input name="codProduto" type="text" id="codProduto" style="width: 100px;" />
					</td>
					<td class="class_linha_2" id="nomeProduto"></td>
					<td class="class_linha_2" align="center" id="edicaoProduto"></td>
					<td class="class_linha_2" align="center" id="precoCapa"></td>
					<td class="class_linha_2" align="center" id="desconto"></td>
					<td class="class_linha_2" align="center" id="valorTotal"></td>
					<td align="center">
						
						<input name="idProdutoEdicaoHidden" type="hidden" id="idProdutoEdicaoHidden" />
						
						<a isEdicao="true" href="javascript:;" class="ok_filtro" onclick="ConferenciaEncalhe.buscarProdutoConferencia();" >
							<img src="${pageContext.request.contextPath}/images/bt_check.gif" alt="Incluir" width="22" height="22" border="0" />
						</a>
					</td>
				</tr>
			</table>
			
			<div class="grids" style="display: block; clear: left; margin-top: 10px;">
				
				<div style="overflow: auto; height: 250px; border: 1px #EEEEEE solid;">
					<table class="conferenciaEncalheGrid" style="width: 941px;" id="dadosGridConferenciaEncalhe">
						<tr class="header_table">
							<td style="width: 65px; text-align: left;">Exemplares</td>
							<td style="width: 145px; text-align: left;">Código de Barras</td>
							<td style="width: 40px; text-align: center;">SM</td>
							<td style="width: 65px; text-align: left;">Código</td>
							<td style="width: 70px; text-align: left;">Produto</td>
							<td style="width: 40px; text-align: left;">Edição</td>
							<td style="width: 80px; text-align: right;" nowrap="nowrap">Preço Capa R$</td>
							<td style="width: 70px; text-align: right;" nowrap="nowrap">Desconto R$</td>
							<td style="width: 50px; text-align: right;">Total R$</td>
							<td style="width: 20px; text-align: center;">Dia</td>
							<td style="width: 70px; text-align: center;" id="colunaJuramentada">Juramentada</td>
							<td style="width: 45px; text-align: center;">Detalhe</td>
							<td style="width: 30px; text-align: center;">Ação</td>
						</tr>
					</table>
				</div>
				
				<br clear="all" />

				<table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="51"><strong>Reparte:</strong></td>
						<td width="85" id="totalReparte"></td>
						<td width="83"><strong> ( - ) Encalhe:</strong></td>
						<td width="87" id="totalEncalhe"></td>
						<td width="126" align="center" bgcolor="#EFEFEF" style="border: 1px solid #000;">
							<strong>( = )Valor Venda Dia:</strong>
						</td>
						<td width="80" align="center" bgcolor="#EFEFEF"	style="border: 1px solid #000;" id="valorVendaDia"></td>
						<td width="130">&nbsp;&nbsp;
							<strong>
								<a href="javascript:;" onclick="ConferenciaEncalhe.popup_outros_valores();"> ( + )Outros valores </a>:
							</strong>
						</td>
						<td width="68" id="totalOutrosValores"></td>
						<td width="125"><strong>( = )Valor a pagar R$:</strong></td>
						<td width="77" id="valorAPagar"></td>
						<td width="17">&nbsp;</td>
					</tr>
				</table>
			</div>
		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>
	
		<fieldset class="classFieldset">
			<table width="950" height="32" border="0" cellpadding="1" cellspacing="1">
				<tr>
					<td width="380">&nbsp;</td>
					<td width="120" align="center" style="float:left"><strong>Data de Operação:</strong></td>
					<td width="70" style="float:left">${dataOperacao}</td>
					<td width="128" style="float:left"><strong>Total de devolução:</strong></td>
					<td width="62" style="float:left"><span id="totalExemplaresFooter"></span></td>
				</tr>
			</table>
		</fieldset>
		
		<div id="idImpressaoFinalizacaoApplet1" style="display: none;">&nbsp;
			<div id="replaceAppletFinal1"></div>
		</div>	

		<div id="idImpressaoFinalizacaoApplet2" style="display: none;">&nbsp;
			<div id="replaceAppletFinal2"></div>
		</div>	
		
</body>