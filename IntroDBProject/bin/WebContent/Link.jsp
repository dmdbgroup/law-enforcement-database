<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Links</h1>

<hr/>
<form action="Link" method="get">
	
	<input type="hidden" name="case_id" value="<%=session.getAttribute("case_id")%>" />
	<input type="hidden" name="id" value="<%=session.getAttribute("case_id")%>" />
	<h2>Person of Interest</h2>
	<%= session.getAttribute("poisTable") %>
	<br/>
	<h2>Type of Crime</h2>
	<%= session.getAttribute("categoriesTable") %>
	<input type="radio" name="category" value="insertnewcategory">new type:</input>
	<input type="text" name="newcategory" />
	<br />
	<br />
	<input type="submit" value="Addlink" />
	
	<input type="hidden" name="action" value="addlink" />
</form>

<hr/>

<%@ include file="Footer.jsp" %>