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

import com.liferay.document.library.kernel.store.DLStoreUtil;

/**
 * touch mising files.
 */
//  maximum rows to process
MAX_ROWS = 5000;

//  SQL select to execute
QUERY = """
select fv.companyId, fv.repositoryId, fv.folderId, fe.name, fv.fileName, fv.version
from dlfileversion fv, dlfileentry fe where fe.fileentryid=fv.fileentryid and fe.companyid=fv.companyid
""";

fTouch = new File("touchFile")
fTouch.write("-")

countMissing = 0

stmt = null
con = DataAccess.getConnection()
rs = null
try {
	stmt = con.createStatement();
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)

	while (rs.next()) {
		companyId = rs.getLong("companyId")
		repositoryId = rs.getLong("repositoryId")
		folderId = rs.getLong("folderId")
		fileName = rs.getString("fileName")
		name = rs.getString("name")
		version = rs.getString("version")
		if (folderId == 0) { folderId = repositoryId }
		if (! DLStoreUtil.hasFile(companyId, folderId, name, version) ) {
			countMissing++;
			println "missing file: $folderId/$name/$version - $fileName"
			DLStoreUtil.updateFile(companyId, folderId, name, "", false, version, fileName, fTouch)
		}
	}
} catch (e) {
	println e.getClass().getName() + " - " + e.getMessage();
} finally {
	DataAccess.cleanUp(con, stmt, rs)
	fTouch.delete()
}

println "${countMissing} missing files were created."
