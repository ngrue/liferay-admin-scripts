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

/**
 * sql_update.groovy
 *
 * Execute a SQL update on the Liferay Database and display affected row count.
 */

//  SQL update to execute (this can be an INSERT, UPDATE or DELETE statement)
UPDATE = """
	UPDATE User_ 
	SET firstName = 'Alan', lastName = 'Turing' 
	WHERE screenName = 'test'
""";

stmt = null;
con = DataAccess.getConnection();
try {
	con.setAutoCommit(true);
	stmt = con.createStatement();
	count = stmt.executeUpdate(UPDATE);
	println count + " row(s) affected";
} catch (e) {
	println e.getClass().getName() + " - " + e.getMessage();
} finally {
	DataAccess.cleanUp(con, stmt)
}
