<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>K-PACs from set</title>
	<link rel="stylesheet" href="//cdn.dhtmlx.com/suite/edge/suite.css" type="text/css" />
	<link rel="stylesheet" href="https://snippet.dhtmlx.com/codebase/assets/css/auxiliary_controls.css">
	<script src="//cdn.dhtmlx.com/suite/edge/suite.js"></script>
</head>
<body>
<style>
	<%@include file='/WEB-INF/views/css/design.css'%>
</style>
<section class="dhx_sample-container" style="height: 90%; width: 36.9%">
	<div style="height: 90%; width: 100%" id="grid"></div>
</section>
<script>
	const grid = new dhx.Grid("grid", {
		css: "dhx_demo-grid",
		columns: [
			{ width: 60, id: "id", header: [{ text: "Id" }] },
			{ width: 120, id: "title", header: [{ text: "Title"}] },
			{ width: 525, id: "description", header: [{ text: "Description", align: "center"}] },
		],
		selection: "row",
		editable: false,
	});

	const data = [
		<c:forEach items="${kpacList}" var="kpac" varStatus="status">
		{
			id: "${kpac.id}",
			title: "${kpac.title}",
			description: "${kpac.description}",
		}<c:if test="${not status.last}">, </c:if>
		</c:forEach>
	]
	grid.data.parse(data);
</script>
</body>
</html>