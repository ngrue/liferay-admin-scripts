
/*
 Copyright (c) 2020 Nicolas Grué, All rights reserved.
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

import com.liferay.journal.service.JournalArticleLocalServiceUtil;

/**
 * clean journal article intermediate versions.
 * we keep 1.0 version in all cases.
 * we will make sure that a newer, approved & non-expireable version exists before removing it.
 */
//  maximum rows to process
MAX_ROWS = 5000;

//  force delete for versions in error (recommended: false)
IS_FORCE_DELETE_WHEN_ERRORS = false;


//  SQL select to execute
QUERY = """
select ja.id_
from journalarticle ja 
where ja.version>1.0 and exists (
  select 1 from journalarticle ja2 
  where ja2.resourceprimkey=ja.resourceprimkey 
  and ja2.articleid=ja.articleid
  and ja2.companyid=ja.companyid
  and ja2.groupid=ja.groupid
  and ja2.version>ja.version
  and ja2.status=0 and ja2.expirationdate is null)
""";

notDeletedIds = []
countDeleted = 0

stmt = null
con = DataAccess.getConnection()
rs = null
try {
	stmt = con.createStatement();
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)

	while (rs.next()) {
		journalArticleId = rs.getLong(1)
		try {
			article = JournalArticleLocalServiceUtil.fetchArticle(journalArticleId)
			//println article
			JournalArticleLocalServiceUtil.deleteArticle(article)
			countDeleted++;
		} catch (e) {
			notDeletedIds.add(journalArticleId)
			println e.getClass().getName() + " - " + e.getMessage();
		}
	}
} catch (e) {
	println e.getClass().getName() + " - " + e.getMessage();
} finally {
	DataAccess.cleanUp(con, stmt, rs)
}

println "${countDeleted} article versions deleted."
if (notDeletedIds.size() > 0) {
	allIds = notDeletedIds.join(",");
	println "IDs not deleted: ${allIds}"
	if (IS_FORCE_DELETE_WHEN_ERRORS) {
		println "Force delete is ON"
		stmt = null
		con = DataAccess.getConnection()
		try {
			stmt = con.createStatement();
			QUERY = "delete from journalarticle where id_ IN (${allIds})"
			println QUERY
			countDeleted = stmt.executeUpdate(QUERY)

			println "${countDeleted} FORCED article versions deleted."
		} catch (e) {
			println e.getClass().getName() + " - " + e.getMessage();
		} finally {
			DataAccess.cleanUp(con, stmt)
		}
	}
}