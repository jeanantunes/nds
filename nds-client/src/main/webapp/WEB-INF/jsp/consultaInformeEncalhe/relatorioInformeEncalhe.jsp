<html>
	<head>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/NDS.css" />
		<style type="text/css">
			body{
				font-size:12px!important;
			}
			
			h1{
				font-size:35px;
			}
			
			h2{
				font-size:25px;
			}
			
			p{
				margin:0px; padding:0px; font-size:11px;
			}
			
			.capas tr{
				border:1px solid #000;
			}
			
			.relatorioHeader{
				border-bottom:1px solid #000; border-right:1px solid #000; background-color: #E0E0E0;
				white-space:nowrap; overflow: hidden;
			}
			
			.relatorio td{
				border-bottom:1px solid #000; border-right:1px solid #000;
				white-space:nowrap; overflow: hidden;
			}
			
			.pulaFolha{
				page-break-after: always;
				
			}
		</style>
		<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/scripts/jquery-ui-1.8.16.custom/development-bundle/jquery-1.6.2.js"></script>
		<script type="text/javascript">
		$(function() {
			
			$(".header").remove();
			$(".menu_superior").remove();
		});
		</script>
	</head>
	<body>
		<header>
			<table name="tab1" align="center" width="1000" border="0" align="center" cellpadding="0" cellspacing="0">
				
				<tr>
					<td>&nbsp;</td>
					<td>
						<span id="btImpressaoInforme" class="bt_novos" title="Imprimir">
							<a href="javascript:;" onclick="$('#btImpressaoInforme').hide();window.print();$('#btImpressao').show();">
								<img src="${pageContext.request.contextPath}/images/ico_impressora.gif" 
								hspace="5" border="0" />Imprimir</a>
						</span>
					</td>
					<td align="right">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
			</table>
		</header>
		
		
					<table name="tab3" width="1000" border="0" align="center" cellpadding="0" cellspacing="0" style="border: 1px solid #000;">
						<tr>
							<td width="794" align="center" valign="middle" rowspan="2"><h2>Informe de Recolhimento</h2></td>
							<td height="23" colspan="2" align="center" style="border-left: 1px solid #000; border-bottom: 1px solid #000;">
								<strong>Recolhimento</strong>
							</td>
						</tr>
						<tr>
							<td width="98" align="center" style="border-left: 1px solid #000; border-bottom: 1px solid #000; font-size:14px!important;">
								<strong>${diaMesInicioRecolhimento}º DIA</strong>
							</td>
							<td width="71" rowspan="2" align="center" style="border-left: 1px solid #000; border-bottom: 1px solid #000;font-size:14px!important;">
								<strong>${dataInicioRecolhimento}</strong>
							</td>
						</tr>
						<tr>
							<td width="794" align="center" valign="middle" rowspan="3">
								<h2>${nomeDistribuidor}</h2>
							</td>
							<td align="center" style="border-left: 1px solid #000; border-bottom: 1px solid #000;font-size:14px!important;">${diaSemanaInicioRecolhimento}</td>
						</tr>
						
						<tr>
							<td align="center" style="border-left: 1px solid #000; border-bottom: 1px solid #000;font-size:14px!important;">
								<strong>${diaMesFimRecolhimento}º DIA</strong>
							</td>
							<td rowspan="2" align="center" style="border-left: 1px solid #000;font-size:14px!important;">
								<strong>${dataFimRecolhimento}</strong>
							</td>
						</tr>
		
						<tr>
							<td align="center" style="border-left: 1px solid #000;font-size:14px!important;">${diaSemanaFimRecolhimento}</td>
						</tr>
					</table>
		
		
					<table name="tab4" class="relatorio pulaFolha" align="center" width="1000" border="0" align="center" cellpadding="2" cellspacing="0" 
						style="border:1px solid #000; margin-top:5px;" >
					<tr>
						<c:forEach items="${colunas}" var="coluna_">
							<td width="${coluna_.largura}%" height="31" align="center" class="relatorioHeader">
								<strong>${coluna_.nome}</strong>
							</td>
						</c:forEach>
					</tr>
					
					<c:set var="tableFechada" value="false"></c:set>
					<c:set var="indexImg" value="0"></c:set>
					
					<c:forEach items="${dados}" var="dado" varStatus="index">
			            
			            <c:choose>
			           		<c:when test="${!dado.imagem}">
			           			<c:if test="${tableImagemAberta && tableFechada}">
				            		</tr>
									</table>
								
			            		</c:if>
				           		
				           		<c:if test="${dado != null && tableFechada}">
				           			
				           			<table name="tab5"  class="relatorio pulaFolha" align="center" width="1000" border="0" align="center" cellpadding="2" cellspacing="0" 
						style="border:1px solid #000; margin-top:5px;">
									<tr>
										<c:forEach items="${colunas}" var="coluna_">
											<td width="${coluna_.largura}%" height="31" align="center" class="relatorioHeader">
												<strong>${coluna_.nome}</strong>
											</td>
										</c:forEach>
									</tr>
									
									<c:set var="tableFechada" value="false"></c:set>
									<c:set var="indexImg" value="0"></c:set>
					            </c:if>
					            
					            <c:if test="${dado == null}">
					            	<c:set var="tableFechada" value="false"></c:set>
					            	<table><tr><td></td></tr></table>
					            </c:if>
					            
					            <c:choose>
					            	<c:when test="${dado.idProdutoEdicao != null}">
							            <tr class="${index.count % 2 == 0 ? 'class_linha_1' : 'class_linha_2'}">
					           		
					           				<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'sequenciaMatriz'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													${dado.sequenciaMatriz}
												</td>
											</c:if>					           		
					           		
							           		<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'codigoProduto'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													${dado.codigoProduto}
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'nomeProduto'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td>
													<div style="white-space:nowrap; overflow: hidden;font-size:14px!important;">
														<strong>${dado.nomeProduto}</strong>
													</div>
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'numeroEdicao'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center" style="font-size:14px!important;">
													<strong>${dado.numeroEdicao}</strong>
												</td>
											</c:if>
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'chamadaCapa'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													<div style="white-space:nowrap; overflow: hidden; font-size:14px!important">
														<strong>${dado.chamadaCapa}</strong>
													</div>
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'codigoDeBarras'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td>
													${dado.codigoDeBarras}
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'precoVenda'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="right">
													${dado.precoVendaFormatado}
												</td>
											</c:if>
											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'nomeEditor'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												
													<td style="overflow: hidden;">
														<div style="white-space: nowrap;">
														${dado.nomeEditor}
														</div>
													</td>
											</c:if>
											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'brinde'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													${dado.brinde ? "Sim" : "Não"}
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'dataLancamento'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													<fmt:formatDate value="${dado.dataLancamento}" pattern="dd/MM/yy"/>
												</td>
											</c:if>											
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'dataRecolhimento'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													<fmt:formatDate value="${dado.dataRecolhimento}" pattern="dd/MM/yy"/>
												</td>
											</c:if>
																						
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'tipoLancamentoParcial'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													${dado.tipoLancamentoParcial}
												</td>
											</c:if>
											
											<c:set var="contains" value="false" />
											<c:forEach var="coluna" items="${colunas}">
											  <c:if test="${coluna.param eq 'pacotePadrao'}">
											    <c:set var="contains" value="true" />
											  </c:if>
											</c:forEach>
											
											<c:if test="${contains}">
												<td align="center">
													${dado.pacotePadrao}
												</td>
											</c:if>
										</tr>
									</c:when>
									<c:otherwise>
										
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								
								<c:if test="${!tableFechada}">
									</table>
									
						           	<c:set var="tableFechada" value="true"></c:set>
						           	
						           	<c:if test="${dado.idProdutoEdicao != null}">
						           	
							           	<table name="tab6" class="pulaFolha" style="border:1px solid #000; margin-top:5px; border-left: 1px solid; border-right: 1px solid; border-bottom: 1px solid;" 
							           			align="center" width="1000" border="0" align="center" cellpadding="2" 
							           			cellspacing="0">
							        	<tr>
							        	
							        	<c:set value="true" var="tableImagemAberta"></c:set>
							        </c:if>
						        </c:if>
						        
				        		<c:if test="${dado.idProdutoEdicao != null}">
									<td align="center">
										<div align="center"><strong>${dado.sequenciaMatriz == null ? '-' : dado.sequenciaMatriz}</strong></div>
										<img src="<c:url value='/capa/tratarNoImage/${dado.idProdutoEdicao}'></c:url>" width="110" height="143"></img>
									<td>
								</c:if>
								
								<c:set var="indexImg" value="${indexImg + 1}"></c:set>
								
								<c:if test="${indexImg % 5 == 0 && dado.idProdutoEdicao != null}">
									</tr><tr>
								</c:if>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					
					
					<c:if test="!tableFechada">
						</table>
					</c:if>
				</td>
			</tr>
		</table>
	</body>
</html>