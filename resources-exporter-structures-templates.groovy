/*
 Copyright (c) 2018 Nicolas Grué, All rights reserved.
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

//  SQL select to execute
QUERY = """
select s.structureid, s.structurekey, s.definition, t.templateid, t.templatekey, t.script, t.language
from ddmstructure s, ddmtemplate t
where s.classnameid=(select classnameid from classname_ where value='com.liferay.journal.model.JournalArticle') 
and s.groupid= (select groupid from group_ where friendlyurl='/global')
and t.classnameid=(select classnameid from classname_ where value='com.liferay.dynamic.data.mapping.model.DDMStructure') 
and t.classpk=s.structureid
order by s.structureid, t.templatekey
""";
//  maximum rows
MAX_ROWS = 500;

// path to export folder
PATH_EXPORT = System.getProperty("user.home")+'/resources-export/'

stmt = null
con = DataAccess.getConnection()
rs = null
try {
	stmt = con.createStatement();
	stmt.setMaxRows(MAX_ROWS)
	rs = stmt.executeQuery(QUERY)
	oldStructId = -1;
	String structKey, structDef, tplKey, tplLang, tplScript, pathStruct, pathTpl;
	while (rs.next()) {
		structId = rs.getLong("structureid");
		if (oldStructId != structId) {
			structKey = rs.getString("structurekey");
			structDef = rs.getString("definition");
			pathStruct = PATH_EXPORT + 'structures/';
			new File(pathStruct).mkdirs()
			new File(pathStruct + structKey + '.json') << structDef
			oldStructId = structId
		}
		tplKey = rs.getString("templatekey");
		tplLang = rs.getString("language");
		tplScript = rs.getString("script");
		pathTpl = PATH_EXPORT + 'templates/' + structKey+ '/'
		new File(pathTpl).mkdirs()
		new File(pathTpl + tplKey + '.' + tplLang) << tplScript
	}
} catch (e) {
	println e.getClass().getName() + " - " + e.getMessage();
} finally {
	DataAccess.cleanUp(con, stmt, rs)
}
