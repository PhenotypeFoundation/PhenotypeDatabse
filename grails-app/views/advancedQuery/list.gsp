<%@ page import="dbnp.query.*" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<meta name="layout" content="main"/>
	<title>Previous queries</title>
	<link rel="stylesheet" href="<g:resource dir="css" file="advancedQuery.css" />" type="text/css"/>
	<g:javascript src="advancedQueryResults.js" />
	<script type="text/javascript">
		function searchWithinResults( form ) {
			submitForm( form, '/advancedQuery/searchIn' );
		}
		function discardResults( form ) {
			submitForm( form, '/advancedQuery/discard' );
		}	
		function combineResults( form ) {
			submitForm( form, '/advancedQuery/combine' );
		}				
	</script>
	
</head>
<body>

<h1>Previous queries</h1>

<g:if test="${flash.error}">
	<div class="errormessage">
		${flash.error.toString().encodeAsHTML()}
	</div>
</g:if>
<g:if test="${flash.message}">
	<div class="message">
		${flash.message.toString().encodeAsHTML()}
	</div>
</g:if>

<g:if test="${searches.size() > 0}">
	<form id="searchform" method="post">
	<table id="searchresults" class="paginate">
		<thead>
			<tr>
				<th class="nonsortable"></th>
				<th>#</th>
				<th>Type</th>
				<th>Criteria</th>
				<th># results</th>
				<th>time</th>
				<th class="nonsortable"></th>
				<th class="nonsortable"></th>
			</tr>
		</thead>
		<g:each in="${searches}" var="search">
			<tr>
				<td><g:checkBox name="id" value="${search.id}" checked="${false}" /></td>
				<td>${search.id}</td>
				<td>${search.entity}</td>
				<td>
					<g:each in="${search.getCriteria()}" var="criterion" status="j">
						<g:if test="${j > 0}">, </g:if>
						<span class="entityfield">${criterion.entityField()}</span>
						<span class="operator">${criterion.operator}</span>
						<span class="value">
							<g:if test="${criterion.value instanceof Search}">
								<g:link action="show" id="${criterion.value.id}">${criterion.value}</g:link>
							</g:if>
							<g:else>
								${criterion.value}
							</g:else>
						</span>
					</g:each>
				</td>
				<td>${search.getNumResults()}</td>
				<td><g:formatDate date="${search.executionDate}" format="HH:mm" /></td>
				<td><g:link action="show" id="${search.id}">Show</g:link>
				<td><g:link action="discard" id="${search.id}">Discard</g:link>
			</tr>
		</g:each>
	</table>
	</form>	
</g:if>

<p class="options">
	<a href="#" class="combine" onClick="combineResults( $( '#searchform' ) ); return false;">Combine results</a><br />
	<a href="#" class="searchIn" onClick="searchWithinResults( $( '#searchform' ) ); return false;">Search within results</a><br />
	<g:link class="search" action="index">Search again</g:link><br />
	<a href="#" class="discard" onClick="discardResults( $( '#searchform' ) ); return false;">Discard results</a><br />
</p>

</body>
</html>