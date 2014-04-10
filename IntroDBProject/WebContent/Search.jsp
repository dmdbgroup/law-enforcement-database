<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Search</h1>

<hr/>

<form method="get" action="Cases">
<div>
	<input type="hidden" name="filter" value="title" />
	Search By Title:
	<input type="text" name="titleTerm" />
	<input type="submit" value="Search" title="Search by Title" />
</div>
</form>

<hr/>

<form method="get" action="Cases">
<div>
	<input type="hidden" name="filter" value="description" />
	Search By Description:
	<input type="text" name="description" />
	<input type="submit" value="Search" title="Search by Description" />
</div>
</form>

<hr/>

<form method="get" action="Cases">
<div>
	<input type="hidden" name="filter" value="category" />
	Search By Category:
	<input type="text" name="categoryTerm" />
	<input type="submit" value="Search" title="Search by Category" />
</div>
</form>

<hr/>

<%@ include file="Footer.jsp" %>