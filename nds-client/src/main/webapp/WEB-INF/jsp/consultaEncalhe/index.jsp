<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Consulta Encalhe</title>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>scripts/produto.js'></script>

<script language="javascript" type="text/javascript" src='<c:url value="/"/>/scripts/jquery.numeric.js'></script>

<script type="text/javascript">
	
</script>

</head>

<body>
	<div class="container">

		<fieldset class="classFieldset">

			<legend> Consulta Encalhe </legend>

			<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			
				<tr>
				
					<td width="30">Data:</td>
					
					<td colspan="3"><input name="datepickerDe" type="text" 
						id="datepickerDe"
						style="width: 80px; float: left; margin-right: 5px;" />
					</td>
					
					<td width="68">Fornecedor:</td>
					
					<td width="264">
					
						<select name="idFornecedor" id="idFornecedor" style="width:260px;">
						    <option value="-1"  selected="selected">Todos</option>
						    <c:forEach items="${listaFornecedores}" var="fornecedor">
						      		<option value="${fornecedor.key}">${fornecedor.value}</option>	
						    </c:forEach>
					    </select>
					
					</td>
					
					<td width="30">Cota:</td>
					
					<td width="90">
						<input type="text" style="width: 60px; float: left; margin-right: 5px;" /> 
					<span class="classPesquisar">
						<a href="javascript:;">&nbsp;</a>
					</span>
					
					</td>
					
					<td width="36">Nome:</td>
					
					<td width="160">
						<input type="text" style="width: 160px;" />
					</td>
					
					<td width="104">
						<span class="bt_pesquisar">
							<a href="javascript:;" onclick="mostrar();">Pesquisar</a>
						</span>
					</td>
				</tr>
			</table>

		</fieldset>
		
		<div class="linha_separa_fields">&nbsp;</div>

		<fieldset class="classFieldset">
		
			<legend> Encalhe</legend>
			
			<div class="grids" style="display: none;">
				<table class="edicoesChamadaGrid"></table>
				<table width="950" border="0" cellspacing="1" cellpadding="1">
					<tr>
						<td width="280" valign="top"><span class="bt_novos"
							title="Gerar Arquivo"><a href="javascript:;"><img
									src="../images/ico_excel.png" hspace="5" border="0" />Arquivo</a>
						</span> <span class="bt_novos" title="Imprimir"><a
								href="javascript:;"><img src="../images/ico_impressora.gif"
									hspace="5" border="0" />Imprimir</a>
						</span></td>
						<td width="190">&nbsp;&nbsp;&nbsp;&nbsp;</td>
						<td width="226">

							<fieldset class="box_field">
								<legend>Primeiro Recolhimento</legend>
								<table width="200" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="8">&nbsp;</td>
										<td width="178">
											<div class="box_resumo">
												<table width="150" border="0" cellspacing="1"
													cellpadding="1">
													<tr>
														<td width="83" height="23"><strong>Produtos:</strong>
														</td>
														<td width="60"><input type="text"
															style="width: 60px;" />
														</td>
													</tr>
													<tr>
														<td><strong>Exemplares:</strong>
														</td>
														<td><input type="text" style="width: 60px;" />
														</td>
													</tr>
												</table>
											</div></td>
									</tr>
								</table>



							</fieldset></td>
						<td width="10">&nbsp;&nbsp;</td>
						<td width="228">
							<fieldset class="box_field">
								<legend>Demais Recolhimento</legend>
								<table width="200" border="0" cellspacing="2" cellpadding="2">
									<tr>
										<td width="8">&nbsp;</td>
										<td width="178">
											<div class="box_resumo">
												<table width="150" border="0" cellspacing="1"
													cellpadding="1">
													<tr>
														<td width="83" height="23"><strong>Produtos:</strong>
														</td>
														<td width="60"><input type="text"
															style="width: 60px;" />
														</td>
													</tr>
													<tr>
														<td><strong>Exemplares:</strong>
														</td>
														<td><input type="text" style="width: 60px;" />
														</td>
													</tr>
												</table>
											</div></td>
									</tr>
								</table>



							</fieldset>
						</td>
					</tr>
				</table>



			</div>

		</fieldset>

		<div class="linha_separa_fields">&nbsp;</div>

	</div>
</body>