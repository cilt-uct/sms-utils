/***********************************************************************************
 * SmsConfigLogic.java
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

package org.sakaiproject.sms.hibernate.logic;

import java.util.List;
import java.util.Set;

import org.sakaiproject.sms.hibernate.model.*;

/**
 * The data service will handle all sms config database transactions for the sms tool in
 * Sakai.
 * 
 * @author julian@psybergate.com
 * @version 1.0
 * @created 25-Nov-2008 08:12:41 AM
 */
public interface SmsConfigLogic {

	public SmsConfig getSmsConfig(Long smsConfigId);
	
	public void persistSmsConfig(SmsConfig smsConfig);
	
	public Set<SmsConfig> getSmsConfigs();
	
	public void deleteSmsCongif(SmsConfig smsConfig);

}