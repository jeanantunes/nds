<head>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.numeric.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/pesquisaCota.js"></script>

<script type="text/javascript" src="scripts/regiao.js"></script>

<script language="javascript" type="text/javascript">

var pesquisaCota = new PesquisaCota();

$(function(){
	regiaoController.init();
});

</script>

<style type="text/css">
.porCep,.porNMaiores,.porSegmento {
	display: none;
}

.gridfaixaCep,.gridNMaiores,.gridsegmentos {
	display: none;
}

#row5 {
	display: none;
}
</style>
</head>

<body>

<div class="corpo">
		<br clear="all" /> <br />

		<div class="container">
		
	<!-- 
	<form action="/produto" id="excluir_form">
	<div id="dialog-excluir " title="Excluir Região">
		<p>Confirma a exclusão desta Região?</p>
	</div>
	</form>
	 -->
	
	<form action="/produto" id="excluir_form">
	<div id="dialog-confirmacao" title="Inserir cota" style="display: none;">
		<p>Confirma a inserção destas cotas na região?</p>
	</div>
	</form>
	
	<div id="dialog-pesqCotas" title="Selecionar Cotas"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Pesquisar Cotas</legend>
			<table width="550" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="31">Cota:</td>
					<td width="99"><input type="text" name="textfield"
						id="textfield" style="width: 80px;" />
					</td>
					
					<td width="32">Nome:</td>
					<td width="250"><input type="text" name="textfield5"
						id="textfield6" style="width: 200px;" />
					</td>
					
					<td width="106"><span class="bt_pesquisar"><a
							href="javascript:;">Pesquisar</a> </span>
					</td>
					
				</tr>
			</table>
		</fieldset>

		<fieldset style="width: 600px !important; margin-top: 10px !important;">
			<legend>Selecionar Cotas</legend>
			<table class="lstCotasGrid"></table>
			<span class="bt_sellAll" style="float: right;">
				<label	for="sel">Selecionar Todos</label>
					<input type="checkbox" id="selCEP"
					name="checkAllCEP" onclick="regiaoController.checkAll();"
					style="float: left; margin-right: 25px;" /> 
			</span>
		</fieldset>
		
	</div>

	<!-- 
	<div id="dialog-detalhes" title="Visualizando Produto">
		<img src="../capas/revista-nautica-11.jpg" width="235" height="314" />
	</div>
	 -->
	
	<!-- ADD POR N-MAIORES -->
	
	<div id="dialog-addNMaiores" title="Adicionar Produtos"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Pesquisar Produtos</legend>
			<table width="588" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="36">Código:</td>
					<td width="77"><input type="text" name="textfield"
						id="textfield" style="width: 60px;" />
					</td>
					<td width="40">Produto:</td>
					<td width="129"><input type="text" name="textfield5"
						id="textfield6" style="width: 120px;" />
					</td>
					<td width="68">Classificação:</td>
					<td width="163"><select name="select" id="select"
						style="width: 140px;">
							<option selected="selected">Selecione...</option>
							<option>Classificaçao 1</option>
							<option>Classificação 2</option>
							<option>Classificação 3</option>
							<option>Classificação 4</option>
					</select>
					</td>
					<td width="31"><span title="Pesquisar Produto"
						class="classPesquisar"><a href="javascript:;">&nbsp;</a> </span>
					</td>
				</tr>
			</table>
		</fieldset>

		<fieldset
			style="width: 600px !important; margin-top: 10px !important;">
			<legend>Regiao</legend>
			<table class="lstProdutosGrid"></table>
			<span class="bt_sellAll" style="float: right;"><label
				for="sel">Selecionar Todos</label><input type="checkbox" id="sel"
				name="Todos" onclick="regiaoController.checkAll();"
				style="float: left; margin-right: 25px;" /> </span>
		</fieldset>

	</div>
	
	<!-- REGIÃO AUTOMÁTICA -->
	<div id="dialog-cotas" title="Montagem de Regão Automática"
		style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Montar Região por:</legend>

			<table width="550" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="20"><input type="radio" name="radio" id="radio"
						value="radio" onclick="regiaoController.filtroPorCep();" />
					</td>
					<td width="40">CEP</td>
					<td width="20"><input type="radio" name="radio" id="radio2"
						value="radio" onclick="regiaoController.filtroPorNMaiores();" />
					</td>
					<td width="74">N Maiores</td>
					<td width="20"><input type="radio" name="radio" id="radio3"
						value="radio" onclick="regiaoController.filtroPorSegmento();" />
					</td>
					<td width="336">Segmento</td>
				</tr>
			</table>
			
			<!-- REGIAO AUTOMÁTICA - POR CEP -->

			<div class="porCep">
				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="86">Faixa de CEP:</td>
						<td width="133">
							<input type="text" name="textfield" id="cepInicialPart1" size="5" maxlength="5" style="width: 50px;" /> 
							<input type="text" name="textfield3" id="cepInicialPart2" size="3" maxlength="3" style="width: 30px;" />
						</td>
						<td width="26">Até:</td>
						<td width="186">
							<input type="text" name="textfield4" id="cepFinalPart1" size="5" maxlength="5" style="width: 50px;" /> 
							<input type="text" name="textfield4" id="cepFinalPart2" size="3" maxlength="3" style="width: 30px;" /></td>
						<td width="87">
							<span class="bt_pesquisar">
								<a href="javascript:;" onclick="regiaoController.mostrarPorCep();">Pesquisar</a>
						</span>
						</td>
					</tr>
				</table>
			</div>

			<!-- REGIAO AUTOMÁTICA - POR SEGMENTO -->

			<div class="porSegmento">
				<table width="550" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td width="51">Segmento:</td>
						<td width="205">
													
							<!-- 
							onchange="$('.grids').toggle(); regiaoController.carregarSegmento()">
							 -->
													
							<select name="comboSegmento" id="comboSegmento" style="width: 180px;">
									<option option selected="selected">Selecione...</option>
										<c:forEach items="${listaSegmento}" var="segmento">
											<option value="${segmento.key}">${segmento.value}</option>
										</c:forEach>	
							</select>
							
							
						</td>
						<td width="92">Qtde de Cotas:</td>
						<td width="83"><input type="text" name="qtdCotas"
							id="qtdCotas" style="width: 80px;" />
						</td>
						<td width="87"><span class="bt_pesquisar"><a
								href="javascript:;"
								onclick="regiaoController.mostrarPorSegmento();">Pesquisar</a> </span>
						</td>
					</tr>
				</table>
			</div>
		</fieldset>

		<!-- <br clear="all" /> -->

		<!-- REGIAO AUTOMÁTICA - POR CEP - GRID -->

		<fieldset style="width: 600px !important; margin-top: 10px;" class="gridfaixaCep">
			<legend>Faixa de Cep</legend>
			<table class="faixaGrid" id="faixaGrid"></table>
			
				<span class="bt_sellAll" style="float: right;">
					<label for="todos">Selecionar Todos</label>
					<input type="checkbox" id="todos" name="todos" onclick="regiaoController.checkAll();" style="float: left; margin-right: 25px;" checked/>					 
				</span> 
				
				<span class="bt_novos">
					<a	href="javascript:;" onclick="regiaoController.add_cotas();">
					<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir</a> 
				</span>
				
				<!-- 
				<span class="bt_novos"><a href="javascript:;" >
					<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0" />Cancelar</a> 
				</span>
				 -->
		</fieldset>

		<!-- REGIAO AUTOMÁTICA - POR N-MAIORES -->

		<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridNMaiores">
			<legend>N Maiores</legend>
			<table class="nMaioresGrid"></table>

			<span class="bt_novos"><a href="javascript:;"
				onclick="regiaoController.add_produtos();"><img
					src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Incluir</a> </span> <span
				style="float: right; margin-top: 5px; margin-right: 50px;">Qtde
				de Cotas:&nbsp;&nbsp; <input name="" type="text"
				style="width: 60px;" /> <a href="javascript:;"
				onclick="regiaoController.add_cotas();"><img
					src="${pageContext.request.contextPath}/images/ico_check.gif" border="0" /> </a> </span>
		</fieldset>

		<!-- REGIAO AUTOMÁTICA - POR SEGMENTO -->

		<fieldset style="width: 600px !important; margin-top: 10px;"
			class="gridsegmentos">
			<legend>Segmento</legend>
			<table class="segmentosGrid" id="segmentosGrid"></table>
			
			<span class="bt_sellAll" style="float: right;">
				<label for="sel">Selecionar Todos</label>
					<input type="checkbox" id="todosSegmento"	name="todosSegmento" onchange='regiaoController.checkAllSegmento();'
						   style="float: left; margin-right: 25px;" checked /> 
			</span> 
			
			<span class="bt_novos">
				<a href="javascript:;" onclick="regiaoController.add_cotas_Segmento();">
					<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
				
				Incluir
				
				</a> 
			</span> 
			
			<!-- 
			<span class="bt_novos">
				<a href="javascript:;">
					<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" hspace="5" border="0" />
				
				Cancelar
				
				</a> 
			</span>
			 -->
			
		</fieldset>
	</div>
	
	
	<!-- ADICIONAR EM LOTE -->

	<div id="dialog-lote" title="Adicionar em Lote" style="display: none;">
		<fieldset style="width: 225px;">
			<legend>Adicionar em Lote</legend>
			<table width="200" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="44">N° cota: <textarea rows="4" cols="30" > </textarea> 
					<!-- <input name="numCota" id="numCota" type="text" style="width: 100px;" /> --> 
					</td>
				</tr>
			</table>
			<div id="example2grid" class="dataTable" style="background: #FFF;"></div>
		</fieldset>
	</div>

	<!-- REGIÃO AUTOMÁTICA - MONTAGEM DA REGIAO -->

	<div id="dialog-regiaoAutomatica" title="Montagem Região Automática"
		style="display: none;">
		<fieldset style="width: 400px !important;">
			<legend>Adicionar Cotas</legend>
			<table class="regioesCadastradasGrid"></table>
		</fieldset>

		<fieldset style="width: 400px !important; margin-top: 5px;">
			<legend>Selecionar Cotas</legend>
			<table class="addCotasGrid"></table>
			<span class="bt_novos"><a href="javascript:;"
				onclick="regiaoController.addNovaRegiao();"><img
					src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />Nova Região</a> </span>
		</fieldset>
	</div>


	<!-- REGIAO -->
	
	<div id="dialog-novo" title="Regiões Cadastradas" style="display: none;">
		<fieldset style="width: 600px !important;">
			<legend>Regiões Cadastradas</legend>
			<table class="regioesCadastradasGrid">
			<!-- <span class="bt_novos"><a href="javascript:;" onclick="addNovaRegiao();"><img src="../images/ico_add.gif" hspace="5" border="0" />Nova Região</a></span>-->
			</table>
		</fieldset>
	</div>

	<!-- NOVA REGIAO -->

	<div id="dialog-addRegiao" title="Nova Região" style="display: none;">
		<fieldset style="width: 300px !important;">
			<legend>Região</legend>
			<table width="270" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="44">Nome:</td>
					<td width="212"><input name="nomeRegiao" id="nomeRegiao"
						type="text" style="width: 200px;" />
					</td>
				</tr>
				<tr>
					<td align="right"><input name="regiaoIsFixa" id="regiaoIsFixa"
						type="checkbox" value="checked" /></td>
					<td>Região Fixa</td>
				</tr>
			</table>
		</fieldset>
	</div>
	<form id="cadastroCotaNaRegiao" >
		<div id="dialog-addCota" title="Nova Cota" style="display: none;">
			<table width="500" border="0" cellpadding="2" cellspacing="1" class="filtro" id="idCotas"></table>
		<div class="linha_separa_fields">&nbsp;</div>
	</form>

		<!-- 
		<fieldset>
			<legend> Dados </legend>
			<table width="270" border="0" cellspacing="2" cellpadding="2">
				<tr>
					<td width="44"> <b>Tipo PDV: </b></b></td>
					
					<td width="44"><b>Status: </b></td>
					
				</tr>
				<tr>
					<td width="44"><b>Bairro: </b></td>
					
					<td width="44"><b>Cidade: </b></td>
				</tr>
				
				<tr>
					<td width="44"><b>Faturamento:  </b></td>
					
					<td width="44"><b>Usário: </b></td>
				</tr>
				
				<tr>
					<td width="44"><b>Data: </b></td>
					
					<td width="44"><b>Hora: </b></td>
				</tr>
			</table>
		</fieldset>
		 -->
	</div>

	<!-- EXCLUIR REGIAO -->

	<div id="dialog-excluir" title="Excluir Região">
		<p>Confirma a exclusão desta Região?</p>
	</div>
	
	<div id="dialog-excluirCota" title="Excluir Cota" style="display:none;">
		<p>Confirma a exclusão desta Cota?</p>
	</div>
	
	<div id="dialog-alterarRegiao" title="Alterar Região" style="display:none;">
		<p>Confirma a alteração desta Região?</p>
	</div>
			<!-- 
			<div id="effect" style="padding: 0 .7em;"
				class="ui-state-highlight ui-corner-all">
				<p>
					<span style="float: left; margin-right: .3em;"
						class="ui-icon ui-icon-info">
					</span> 
						<b>Região < evento > com < status >.</b>
				</p>
			</div>
			 -->
			
	<!-- EDITAR REGIAO -->

	<div id="dialog-editar" title="Editar região">
	</div>

			<!-- COMBO DE REGIÕES -->
			
			<fieldset class="classFieldset">
				<legend> Pesquisar Região</legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
					<tr>
						<td width="48">Região:</td>
						<td width="752">
							<select name="comboRegioes" id="comboRegioes" style="width: 250px;"
									onchange="$('.grids').toggle(); regiaoController.carregarRegiao()">
									<option selected="selected">Selecione...</option>
									<c:forEach items="${listaRegiao}" var="regiao">
										<option value="${regiao.key}">${regiao.value}</option>
									</c:forEach>
							</select>
						</td>
						
						<td width="134"><span class="bt_novos" title="Nova Região"><a
								href="javascript:;" onclick="regiaoController.addNovaRegiao();"><img
									src="${pageContext.request.contextPath}/images/ico_salvar.gif" hspace="5" border="0" />Nova	Região</a> </span>
						</td>
					</tr>
				</table>
			</fieldset>
			

		<!-- COTAS DA REGIAO -->			
			
			<div class="linha_separa_fields">&nbsp;</div>
			<div class="grids" style="display: none;">
				<fieldset class="classFieldset">
					<legend>Cotas por Região</legend>

					<table class="cotasRegiaoGrid"></table>
					
						<!-- BOTÕES GRID PRINCIPAL -->
						
					<span class="bt_novos" title="Arquivo">
						<a href="${pageContext.request.contextPath}/distribuicao/regiao/exportar?fileType=XLS">
							<img src="${pageContext.request.contextPath}/images/ico_excel.png" hspace="5" border="0" />
							
							Arquivo
							
						</a> 
					</span> 
					
					<span class="bt_novos" title="Imprimir">
						<a href="${pageContext.request.contextPath}/distribuicao/regiao/exportar?fileType=PDF">
							<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" alt="Imprimir" hspace="5" border="0" />
							
							Imprimir
							
						</a> 
					</span>

					<!--<span class="bt_novos"><a href="javascript:;" onclick="addCotas();"><img src="../images/ico_add.gif" alt="Adicionar Cotas" hspace="5" border="0" />Adicionar Cotas</a></span>-->
					
					<span class="bt_novos" title="Região Automática">
						<a href="javascript:;" onclick="regiaoController.addCotasRegAutomatica();">
							<img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0" />
						
						Região	Automática
						
						</a> 
					</span> 
					
					<span class="bt_novos" title="Adicionar em Lote">
						<a href="javascript:;" onclick="regiaoController.add_lote();">
							<img src="${pageContext.request.contextPath}/images/ico_integrar.png" hspace="5" border="0" />
						
						Adicionar em Lote
						
						</a> 
					</span> 
					
					<span class="bt_novos" title="Manutenção">
						<a href="javascript:;" onclick="regiaoController.manutencaoRegiao();">
							<img src="${pageContext.request.contextPath}/images/bt_administracao.png" hspace="5" border="0" />
						
						Manutenção
						
						</a>
					</span> 
					
					<span class="bt_novos" title="Adicionar Cota">
						<a href="javascript:;" onclick="regiaoController.popupAddCotaRegiao();">
							<img src="${pageContext.request.contextPath}/images/ico_add.gif" hspace="5" border="0" />
							
						Adicionar Cota
							
						</a> 
					</span>
					
				</fieldset>
			</div>
		</div>
</body>

