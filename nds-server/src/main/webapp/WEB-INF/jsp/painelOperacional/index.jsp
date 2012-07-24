<head>
	
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/painelOperacional.css" />
	<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/painelOperacional.js"></script>

</head>

<body>
	<div class="corpo">
		<div class="container">
			
			<jsp:include page="../messagesDialog.jsp" />

			<fieldset class="classFieldset">
				<legend> Consultar Painel Operacional</legend>
				<table width="950" border="0" cellpadding="2" cellspacing="1"
					class="filtro">
					<tr>
						<td width="31">Data:</td>
						<td colspan="3">${dataHora}hs</td>
						<td width="17">
							<img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Encerrado" />
						</td>
						<td width="88">Encerrado</td>
						<td width="17">
							<img src="${pageContext.request.contextPath}/images/ico_fechamento.png" alt="Fechamento" />
						</td>
						<td width="90">Fechamento</td>
						<td width="17">
							<img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" />
						</td>
						<td width="253">Operando</td>
						<td width="21">
							<span class="bt_atualizarIco">
								<a href="javascript:;" title="Atualizar Painel">&nbsp;</a>
							</span>
						</td>
						<td width="97">
							<a href="${pageContext.request.contextPath}/painelOperacional/">Atualizar Painel</a>
						</td>
					</tr>
				</table>

			</fieldset>
			
			<div class="linha_separa_fields">&nbsp;</div>
			
			<fieldset class="classFieldset">
				<legend> Painel Operacional</legend>
				<ul id="map">
					<li id="crs" estado="rs">
						<a href="javascript:;" onmouseover="mostrarIndicadores('RS');">
							<div id="rs" class="rsActive" title="RS">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="RS" border="0" />
							</div>
						</a>
					</li>
					<li id="csc" estado="sc">
						<a href="javascript:;" onmouseover="mostrarIndicadores('SC');">
							<div id="sc" class="scActive" title="SC">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="SC" border="0" />
							</div>
						</a>
					</li>
					<li id="cpr" estado="pr">
						<a href="javascript:;" onmouseover="mostrarIndicadores('PR');">
							<div id="pr" title="PR" class="prActive">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="PR" border="0" />
							</div>
						</a>
					</li>
					<li id="csp" estado="sp">
						<a href="javascript:;" onmouseover="mostrarIndicadores('SP');">
							<div id="sp" title="SP">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="SP" />
							</div>
						</a>
					</li>
					<li id="cms" estado="ms">
						<a href="javascript:;" onmouseover="mostrarIndicadores('MS');">
							<div id="ms" title="MS">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="MS" />
							</div>
						</a>
					</li>
					<li id="crj" estado="rj">
						<a href="javascript:;" onmouseover="mostrarIndicadores('RJ');">
							<div id="rj" title="RJ">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="RJ" />
							</div>
						</a>
					</li>
					<li id="ces" estado="es">
						<a href="javascript:;" onmouseover="mostrarIndicadores('ES');">
							<div id="es" title="ES">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="ES" />
							</div>
						</a>
					</li>
					<li id="cmg" estado="mg">
						<a href="javascript:;" onmouseover="mostrarIndicadores('MG');">
							<div id="mg" title="MG">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="MG" />
							</div>
						</a>
					</li>
					<li id="cgo" estado="go">
						<a href="javascript:;" onmouseover="mostrarIndicadores('GO');">
							<div id="go" title="GO">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="GO" />
							</div>
						</a>
					</li>
					<li id="cba" estado="ba">
						<a href="javascript:;" onmouseover="mostrarIndicadores('BA');">
							<div id="ba" title="BA">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="BA" />
							</div>
						</a>
					</li>
					<li id="cmt" estado="mt">
						<a href="javascript:;" onmouseover="mostrarIndicadores('MT');">
							<div id="mt" title="MT" class="mtActive">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="MT" border="0" />
							</div>
						</a>
					</li>
					<li id="cro" estado="ro">
						<a href="javascript:;" onmouseover="mostrarIndicadores('RO');">
							<div id="ro" title="RO">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="RO" />
							</div>
						</a>
					</li>
					<li id="cac" estado="ac">
						<a href="javascript:;" onmouseover="mostrarIndicadores('AC');">
							<div id="ac" title="AC">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="AC" />
							</div>
						</a>
					</li>
					<li id="cam" estado="am">
						<a href="javascript:;" onmouseover="mostrarIndicadores('AM');">
							<div id="am" title="AM">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="AM" />
							</div>
						</a>
					</li>
					<li id="crr" estado="rr">
						<a href="javascript:;" onmouseover="mostrarIndicadores('RR');">
							<div id="rr" title="RR">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="RR" />
							</div>
						</a>
					</li>
					<li id="cpa" estado="pa">
						<a href="javascript:;" onmouseover="mostrarIndicadores('PA');">
							<div id="pa" title="PA">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="PA" />
							</div>
						</a>
					</li>
					<li id="cap" estado="ap">
						<a href="javascript:;" onmouseover="mostrarIndicadores('AP');">
							<div id="ap" title="AP">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="AP" />
							</div>
						</a>
					</li>
					<li id="cma" estado="ma">
						<a href="javascript:;" onmouseover="mostrarIndicadores('MA');">
							<div id="ma" title="MA">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="MA" />
							</div>
						</a>
					</li>
					<li id="cto" estado="to">
						<a href="javascript:;" onmouseover="mostrarIndicadores('TO');">
							<div id="to" title="TO">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="TO" />
							</div>
						</a>
					</li>
					<li id="cse" estado="se">
						<a href="javascript:;" onmouseover="mostrarIndicadores('SE');">
							<div id="se" title="SE">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="SE" />
							</div>
						</a>
					</li>
					<li id="cal" estado="al">
						<a href="javascript:;" onmouseover="mostrarIndicadores('AL');">
							<div id="al" title="AL">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="AL" />
							</div>
						</a>
					</li>
					<li id="cpe" estado="pe">
						<a href="javascript:;" onmouseover="mostrarIndicadores('PE');">
							<div id="pe" title="PE">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="PE" />
							</div>
						</a>
					</li>
					<li id="cpb" estado="pb">
						<a href="javascript:;" onmouseover="mostrarIndicadores('PB');">
							<div id="pb" title="PB">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="PB" />
							</div>
						</a>
					</li>
					<li id="crn" estado="rn">
						<a href="javascript:;" onmouseover="mostrarIndicadores('RN');">
							<div id="rn" title="RN">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="RN" />
							</div>
						</a>
					</li>
					<li id="cce" estado="ce">
						<a href="javascript:;" onmouseover="mostrarIndicadores('CE');">
							<div id="ce" title="CE">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="CE" />
							</div>
						</a>
					</li>
					<li id="cpi" estado="pi">
						<a href="javascript:;" onmouseover="mostrarIndicadores('PI');">
							<div id="pi" title="PI">
								<img src="${pageContext.request.contextPath}/mapa/img/null.gif" alt="PI" />
							</div>
						</a>
					</li>
				</ul>

				<div class="infosPainel">
					<fieldset style="width: 367px;">
						<legend>Status Painel</legend>
						<br />
						
						<c:set var="ultimoEstado"></c:set>
						<c:set var="fecharDivEstado" value="false"></c:set>
						<c:set var="ultimoDistribuidor"></c:set>
						
						<c:forEach items="${distribuidores}" var="distribuidor">
						
							<c:if test="${distribuidor.endereco.uf != ultimoEstado}">
								<div id="div_${distribuidor.endereco.uf}" style="display: none;" class="grupoIndicadorEstado">
									<label>${distribuidor.endereco.uf}</label>
									<br clear="all" />
									
									<ul>
									<li>
										<c:choose>
											<c:when test="${distribuidor.statusOperacao.status == 'ENCERRADO'}">
												<img src="${pageContext.request.contextPath}/images/ico_encerrado.png" alt="Encerrado" hspace="2" align="left" />
											</c:when>
											<c:when test="${distribuidor.statusOperacao.status == 'FECHAMENTO'}">
												<img src="${pageContext.request.contextPath}/images/ico_fechamento.png" alt="Fechamento" hspace="2" align="left" />
											</c:when>
											<c:when test="${distribuidor.statusOperacao.status == 'OPERANDO'}">
												<img src="${pageContext.request.contextPath}/images/ico_operando.png" alt="Operando" hspace="2" align="left" />
											</c:when>
										</c:choose>
										
										<div>
											<a href="javascript:;" onclick="openDistrib(${distribuidor.idDistribuidorInterface});">
												${distribuidor.nome}
											</a>
										</div>

										<div class="detalhesPainel" id="detalhe_${distribuidor.idDistribuidorInterface}" style="display: none; width: 340px;">
											<a href="javascript:;" onclick="closeDistrib(${distribuidor.idDistribuidorInterface});">
												<img src="${pageContext.request.contextPath}/images/ico_excluir.gif" 
												     alt="Fechar" border="0" style="float: right;" />
											</a>
											<br clear="all" />
									
									<c:set var="ultimoEstado" value="${distribuidor.endereco.uf}"></c:set>
									<c:set var="fecharDivEstado" value="true"></c:set>
							</c:if>
							
							<div class="accordion">
							
							<c:forEach items="${distribuidor.indicadores}" var="indicador">
										
								<c:if test="${indicador.grupoIndicador.descricao != ultimoGrupo}">
									
									<c:if test="${ultimoGrupo != null}">
										</div>
									</c:if>
									
									<div>
										<a href="#${indicador.grupoIndicador.descricao}">${indicador.grupoIndicador.descricao}</a>
									</div>
									<div>
									
									<c:set var="ultimoGrupo" value="${indicador.grupoIndicador.descricao}"></c:set>
								</c:if>
								
								<span>${indicador.tipoIndicador.descricao}: <strong>${indicador.valor}</strong></span>
								<br clear="all" />
								
							</c:forEach>
										
										<br />
									</div>
								</li>
							</ul>
							
							<c:if test="${fecharDivEstado}">
								</div>
								<c:set var="fecharDivEstado" value="false"></c:set>
							</c:if>
						</c:forEach>
					</fieldset>
				</div>
			</fieldset>
			<div class="linha_separa_fields">&nbsp;</div>
		</div>
	</div>
</body>