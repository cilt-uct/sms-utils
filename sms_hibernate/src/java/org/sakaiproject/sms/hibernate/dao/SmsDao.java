/***********************************************************************************
 * SmsDao.java - created by Sakai App Builder -AZ
 * Copyright (c) 2008 Sakai Project/Sakai Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *      http://www.osedu.org/licenses/ECL-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *
 **********************************************************************************/
package org.sakaiproject.sms.hibernate.dao;

import java.sql.Timestamp;

/*import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;*/
import org.apache.log4j.Level;


/**
 * Implementations of any specialized DAO methods from the specialized DAO that allows the developer to extend the functionality of the
 * generic dao package
 *
 * @author Sakai App Builder -AZ
 */
public class SmsDao extends BaseDao{

    //protected static Log log = LogFactory.getLog(SmsDao.class);
	//protected static org.apache.log4j.Logger log = org.apache.log4j.Logger
	//.getLogger(SmsDao.class);
	protected Log log = new Log();
	protected class Log {//THIS IS A TEMP SOLUTION TILL I GET A FREE MIN TO SORT OUT LOG4J
		public void debug(String msg) {
			System.out.println(msg);
		}
	}
    
    protected Timestamp getTimestampCurrent() {
    	return new Timestamp(System.currentTimeMillis());
    }

    public void init() {
    	//log.setLevel(Level.ALL);
        log.debug("init");
    }
    
    /**
	 * Enables or disables the debug Information
	 * 
	 * @param debug
	 */
	public void enableDebugInformation(boolean debug) {
		if (debug) {
			//log.setLevel(Level.ALL);
		} else {
			//log.setLevel(Level.OFF);
		}
	}
}
