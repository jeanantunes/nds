<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.multiselect.css" />
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/romaneios.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.multiselect.br.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery.fileDownload.js"></script>
	<script language="javascript" type="text/javascript">
	$(function(){
		romaneiosController.init();
	});
	</script>
	
	<style type="text/css">
	  .dados, .dadosFiltro{display:none;}
	</style>
</head>

<body>
	    		
    <div class="areaBts">
		<div class="area">
		
			<span class="bt_novos" style="display: none;">
				<a href="${pageContext.request.contextPath}/romaneio/gerarRomaneio?fileType=PDF" rel="tipsy" title="Gerar Romaneio">
					<img src="${pageContext.request.contextPath}/images/bt_expedicao.png" border="0" />
				</a>
			</span>
		
			<span class="bt_arq" style="display: none;">
				<a href="${pageContext.request.contextPath}/romaneio/exportar?fileType=XLS" rel="tipsy" title="Gerar Arquivo">
					<img src="${pageContext.request.contextPath}/images/ico_excel.png" border="0" />
				</a>
			</span>
			
			<span class="bt_arq" style="display: none;">
				<a href="${pageContext.request.contextPath}/romaneio/exportar?fileType=PDF" rel="tipsy" title="Imprimir">
					<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" border="0" />
				</a>
			</span>
			
			<span class="bt_arq" style="display: none;">
				<a href="javascript:;" id="romaneio-btnExtracao" title="Imprimir" onclick="romaneiosController.gerarArquivo('PDF');">
					<img src="${pageContext.request.contextPath}/images/ico_soma_estudos.gif" alt="Imprimir" hspace="5" border="0" />
				</a>				
			</span>
		</div>
	</div>
	
	<div class="corpo">
	    <br clear="all"/>
	    
	    <div class="container">
	    
			<br />
			
			<fieldset class="classFieldset fieldFiltroItensNaoBloqueados">
				<legend>Romaneios de Entrega</legend>
	        		<table width="950" border="0" cellpadding="2" cellspacing="1" class="filtro">
	         			<tr>
	         				<td width="54">Data:</td>
    						<td width="104">
    							<input type="text" id="romaneio-dataLancamento" style="width:70px;" value="${dataAtual}" 
    								   onchange="romaneiosController.pesquisarProdutos();" />
    						</td>
	              			<td width="24">Box:</td>
								<td width="176">
							  		<select name="codigoBox" id="codigoBox" style="width: 100px;"
							  				onchange="romaneiosController.recarregarComboRoteiroRotas(this.value)">
										 <option selected="selected" value="">Todos</option>
										<c:forEach var="box" items="${listaBox}">
											<option value="${box.key}">${box.value}</option>
										</c:forEach>
									</select>
								</td>
	                		<td width="49" align="right">Roteiro:</td>
	                		<td width="260">
	                			<select name="idRoteiro" id="idRoteiro" multiple="multiple" style="width: 200px;" >
									<c:forEach var="roteiro" items="${listaRoteiro}">
										<option value="${roteiro.key}">${roteiro.value}</option>
									</c:forEach>
					    		</select>
					    		
	                		</td>
	                		<td width="32" align="right">Rota:</td>
	                		<td colspan="2">
								<select name="idRota" id="idRota" style="width: 200px;">
									 <option selected="selected" value="">Todos</option>
									<c:forEach var="rota" items="${listaRota}">
										<option value="${rota.key}">${rota.value}</option>
									</c:forEach>
					    		</select>
	                		</td>
						</tr>
						<tr>
							<td>Produtos:</td>
							<td colspan="5">
								<select id="selectProdutos" multiple="multiple" style="width:200px">
									<c:forEach items="${produtos}" var="produto">
										<option value="${produto.key}">${produto.value}</option>
									</c:forEach>
								</select>
							</td>
							<td></td>
							<td width="208">
	              				<span class="bt_pesquisar">
	              					<a href="javascript:;" onclick="romaneiosController.pesquisar();">Pesquisar</a>
	              				</span>
	              			</td>
						</tr>
					</table>
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
			
			<fieldset class="classFieldset">
			
				<legend> Roteiros / Rotas</legend>
				
				<div class="grids" style="display:none;">
				
					<table class="romaneiosGrid"></table>
					
		    		<span style="float:right; margin-top:10px; margin-right:50px;">
		    			<strong>Total de Entregas:</strong>&nbsp;<span id="totalCotas"></span>
		    		</span>
		    		
				</div>	
					
			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
			
		</div>
	</div>
</body>