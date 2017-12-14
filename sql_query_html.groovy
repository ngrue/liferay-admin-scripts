/*
 Copyright (c) 2017 Nicolas Grué, All rights reserved.
 This library is free software; you can redistribute it and/or modify it under
 the terms of the GNU Lesser General Public License as published by the Free
 Software Foundation; either version 2.1 of the License, or (at your option)
 any later version.
 This library is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 details.
*/

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.util.HtmlUtil;

/**
 * sql_query_html.groovy
 *
 * Execute a SQL select on the Liferay Database and display results in HTML.
 */

//  SQL select to execute
QUERY = """
	SELECT * FROM PortletPreferences
""";

//  maximum rows to display
MAX_ROWS = 100;

stmt = null
con = DataAccess.getConnection()
rs = null
try {
	stmt = con.createStatement();
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)
	md = rs.getMetaData()
	cc = md.getColumnCount()
	
	println QUERY+"</pre>"
	println """
		<div class="lfr-search-container">
		<div class="results-grid">
		<table class="taglib-search-iterator">
		<thead>
		<tr class="portlet-section-header results-header">
	""";
	for (column=1; column<=cc; column++) {
		println "<th>" + md.getColumnLabel(column) + "</th>"
	}
	println """
		</tr>
		</thead>
		<tbody>
	""";

	alt = false
	while (rs.next()) {
		line = "<tr class=\"portlet-section-alternate results-row " + (alt ? "alt" : "") + "\">" ;
		for (column=1; column<=cc; column++) {
			value =  rs.getObject(column)
			typeCol = md.getColumnTypeName(column)
			if (typeCol == "CLOB") {
				clob = rs.getClob(column);
				if (clob == null) {
					clob = "(null)"
				} else {
					clob = clob.getSubString(1 as long, clob.length() as int)
				}
				line = line + "<td>" + HtmlUtil.escape(clob) + "</td>"
			} else {
				line = line + "<td>" + HtmlUtil.escape(value==null? "(null)": value.toString()) + "</td>"
			}
		}	
		println line+"</tr>";
		alt = !alt
	}
	
	println """
		</tbody>
		</table>
		</div>
		</div>
		<pre>
	"""
} catch (e) {
	println e.getClass().getName() + " - " + e.getMessage();
} finally {
	DataAccess.cleanUp(con, stmt, rs)
}