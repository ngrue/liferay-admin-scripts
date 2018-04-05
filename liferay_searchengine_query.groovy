/**
 * Copyright (c) 2016 Nicolas Grué, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.search.*;
import com.liferay.portal.kernel.search.generic.*;

// UPDATE AT YOUR CONVENIENCE
NB_RESULTS = 10;
QUERY = "+entryClassName:com.liferay.portal.kernel.model.User +lastName:Test*";
COMPANYID = PortalUtil.getDefaultCompanyId();
OUTPUT_CSV=false; // true will output CSV, false will output HTML

// Log
logger = LogFactoryUtil.getLog("com.edf.add_category_generalites")
def log = { m -> logger.info(m); println m }


hits = SearchEngineUtil.search(SearchEngineUtil.getDefaultSearchEngineId(), COMPANYID, new StringQuery( QUERY
	), 0, NB_RESULTS);

log(hits.getLength()+" results:");

if (hits.getLength()>0) {
	ligne = "";
	if (OUTPUT_CSV) {
		println "<textarea>";
	} else {
		println "<table border=1><tr>"
		
	}
	fields = hits.getDocs()[0].getFields().keySet().toArray();
	for (field in fields) {
		if (OUTPUT_CSV) {
			ligne = ligne+ field+";";
		} else {
			ligne = ligne+ "<th>"+field+"</th>";
		}
	}
	if (OUTPUT_CSV) {
		log(ligne);
	} else {
		println ligne;
		println "</tr><tr>";
	}
	docVal = "";
	for (doc in hits.getDocs()) {
		ligne = "";
		for (field in fields) {
			if ((doc.getValues(field) != null) && (doc.getValues(field).length > 1)) {
				docVal = doc.getValues(field);
			} else {
				docVal = doc.get(field);
			}
			if (OUTPUT_CSV) {
				ligne = ligne+ docVal+";";
			} else {
				ligne = ligne+ "<td>"+docVal+"</td>";
			}
		}
		if (OUTPUT_CSV) {
			log(ligne);
		} else {
			println ligne;
			println "</tr><tr>";
		}
	}
	if (OUTPUT_CSV) {
		println "</textarea>";
	} else {
		println "</tr></table>";
	}

}
