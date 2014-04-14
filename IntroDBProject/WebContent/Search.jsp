<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="Header.jsp" %>

<h1>Search</h1>

<h2>Search Cases</h2>
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

<h2>Search Persons of Interest</h2>
<hr />
<form method="get" action="Pois">
<div>
	<input type="hidden" name="filter" value="name" />
	Search By Person of Interest Name:
	<input type="text" name="nameTerm" />
	<input type="submit" value="Search" title="Search by Name" />
</div>
</form>

<hr/>

<form method="get" action="Pois">
<div>
	<input type="hidden" name="filter" value="date" />
	Search By Conviction Date:
	<input type="text" name="dateTerm" />
	<input type="submit" value="Search" title="Search by Date" />
</div>
</form>

<hr/>

<form method="get" action="Pois">
<div>
	<input type="hidden" name="filter" value="type" />
	Search By Conviction Type:
	<input type="text" name="typeTerm" />
	<input type="submit" value="Search" title="Search by Type" />
</div>
</form>

<hr/>

<%@ include file="Footer.jsp" %>