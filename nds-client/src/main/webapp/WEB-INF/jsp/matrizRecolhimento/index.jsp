<head>



</head>

<body>
	
	<!-- Filtro de Pesquisa -->
	
	<fieldset class="classFieldset">
	
		<legend>Pesquisar Balanceamento da Matriz de Recolhimento </legend>
		
		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
			<tr>
				<td width="76">Fornecedor:</td>
				<td colspan="3">
					<a href="#" id="selFornecedor">Clique e Selecione o Fornecedor</a>
					<div class="menu_fornecedor" style="display: none;">
						<span class="bt_sellAll">
							<input type="checkbox" id="sel" name="Todos1" onclick="checkAll_fornecedor();" style="float: left;" />
							<label for="sel">Selecionar Todos</label>
						</span>
						<br clear="all" />
						<input id="dinap" name="checkgroup_menu" onclick="verifyCheck_1()" type="checkbox" />
						<label for="dinap">Dinap</label>
						<br clear="all" />
						<input name="checkgroup_menu" onclick="verifyCheck_1()" id="fc" type="checkbox" />
						<label for="fc">FC</label>
					</div>
				</td>
				<td width="53">Semana:</td>
				<td width="107">
					<input type="text" name="textfield8" id="textfield8" style="width: 50px;" />
				</td>
				<td width="33">Data:</td>
				<td width="145">
					<input type="text" name="datepickerDe" id="datepickerDe" style="width: 80px;" />
				</td>
				<td width="164">
					<span class="bt_pesquisar">
						<a href="javascript:;" onclick="mostrar();">Pesquisar</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
	<!--  Resumo do Período -->
	
	<fieldset class="classFieldset" id="resumoPeriodo" style="display: none;">
	
		<legend>Resumo do Período</legend>
		
		<div style="width: 950px; overflow-x: auto;">
			<table width="100%" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td>
						<div class="box_resumo">
							<label>01/12/2011 
								<a href="javascript:;" onclick="mudaGrid();" style="float: right;">
									<img src="<c:url value='images/ico_detalhes.png'/>" width="15" height="15" border="0" title="Visualizar" />
								</a>
							</label>
							<span class="span_1">Qtde. Títulos:</span><span class="span_2">70</span>
							<span class="span_1">Qtde. Exempl.:</span><span class="span_2">250.000</span>
							<span class="span_1">Qtde. Parciais:</span><span class="span_2">7</span>
							<span class="span_1">Peso Total:</span><span class="span_2">250.000</span>
							<span class="span_1">Valor Total:</span><span class="span_2">250.000</span>
						</div>
					</td>
					<td>
						<div class="box_resumo alert">
							<label>01/12/2011 
								<a href="javascript:;" onclick="mudaGrid();" style="float: right;">
									<img src="<c:url value='images/ico_detalhes.png'/>" width="15" height="15" border="0" title="Visualizar" />
								</a>
							</label>
							<span class="span_1">Qtde. Títulos:</span><span class="span_2">70</span>
							<span class="span_1">Qtde. Exempl.:</span><span class="span_2">250.000</span>
							<span class="span_1">Qtde. Parciais:</span><span class="span_2">7</span>
							<span class="span_1">Peso Total:</span><span class="span_2">250.000</span>
							<span class="span_1">Valor Total:</span><span class="span_2">250.000</span>
						</div>
					</td>
				</tr>
			</table>
		</div>
		
		<!-- Botões de Ação -->
		
		<table width="950" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="88">
					<strong>Balancear por:</strong>
				</td>
				<td width="412">
					<span class="bt_confirmar_novo" title="Balancear Editor">
						<a href="javascript:;" onclick="popup_balanceamento();">
							<img border="0" hspace="5" src="<c:url value='images/ico_check.gif'/>">Editor
						</a>
					</span>
					<span class="bt_confirmar_novo" title="Balancear Volume / Valor">
						<a onclick="popup_balanceamento_1();" href="javascript:;">
							<img border="0" hspace="5" src="<c:url value='images/ico_check.gif'/>">Valor
						</a>
					</span>
				</td>
				<td>
					<span class="bt_novos" title="Matriz Fornecedor" style="float: right;">
						<a href="javascript:;" onclick="mostra_matriz();">
							<img border="0" hspace="5" src="<c:url value='images/ico_detalhes.png'/>">Matriz Fornecedor
						</a>
					</span>
				</td>
				<td>
					<span class="bt_configura_inicial">
						<a href="javascript:;">
							<img src="<c:url value='images/bt_devolucao.png'/>" title="Voltar Configuração Inicial" border="0" hspace="5" />
							Voltar Configuração Inicial
						</a>
					</span>
				</td>
			</tr>
		</table>
	</fieldset>
	
	<!-- Balanceamento -->
	
	<fieldset class="classFieldset">
	
		<legend>Balanceamento da Matriz de Recolhimento </legend>
		
		<div class="grids" style="display: none;">

			<span class="bt_novos" id="bt_fechar" title="Fechar" style="float: right; display: none;">
				
				<a href="javascript:;" onclick="fechaGrid();">
					<img src="<c:url value='images/ico_excluir.gif'/>" hspace="5" border="0" />Fechar
				</a>
			</span>

			<!-- GRID -->
			
			<table class="balanceamentoGrid"></table>
			
			<table width="950" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="152">
						<span class="bt_novos" title="Reprogramar">
							<a href="javascript:;" onclick="popup();">
								<img src="<c:url value='images/ico_reprogramar.gif'/>" hspace="5" border="0" />Reprogramar
							</a>
						</span>
					</td>
					<td width="127">
						<span class="bt_confirmar_novo" title="Confirmar balanceamento">
							<a href="javascript:;" onclick="popup_balanceamento();">
								<img border="0" hspace="5" src="<c:url value='images/ico_check.gif'/>">Confirmar
							</a>
						</span>
					</td>
					<td width="46">&nbsp;</td>
					<td width="443">&nbsp;</td>
					<td width="150">
						<span class="bt_sellAll">
							<label for="sel">Selecionar Todos</label>
							<input type="checkbox" name="Todos" id="sel" onclick="checkAll();" style="float: left;" />
						</span>
					</td>
				</tr>
			</table>
		</div>
		
		<!-- GRID -->
		
		<div id="grid_matriz" style="display: none;">
			<table class="balanceamentoGrid"></table>
		</div>
		
	</fieldset>
	
	<div class="linha_separa_fields">&nbsp;</div>
	
</body>