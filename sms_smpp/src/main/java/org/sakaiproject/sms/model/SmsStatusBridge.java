/***********************************************************************************
 * SmsStatusBridge.java
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
package org.sakaiproject.sms.model;

import java.util.HashMap;

import org.jsmpp.util.DeliveryReceiptState;
import org.sakaiproject.sms.hibernate.model.constants.SmsConst_SmscDeliveryStatus;

public class SmsStatusBridge {

	public static boolean isSmsDeliveryStatusPopulated;

	public static HashMap<DeliveryReceiptState, Integer> smsDeliveryStatus = null;

	public static void setupHashMap() {

		smsDeliveryStatus = new HashMap<DeliveryReceiptState, Integer>();

		smsDeliveryStatus.put(DeliveryReceiptState.ACCEPTD,
				SmsConst_SmscDeliveryStatus.ACCEPTED);
		smsDeliveryStatus.put(DeliveryReceiptState.DELETED,
				SmsConst_SmscDeliveryStatus.DELETED);
		smsDeliveryStatus.put(DeliveryReceiptState.DELIVRD,
				SmsConst_SmscDeliveryStatus.DELIVERED);
		smsDeliveryStatus.put(DeliveryReceiptState.EXPIRED,
				SmsConst_SmscDeliveryStatus.EXPIRED);
		smsDeliveryStatus.put(DeliveryReceiptState.UNDELIV,
				SmsConst_SmscDeliveryStatus.UNDELIVERA);
		smsDeliveryStatus.put(DeliveryReceiptState.UNKNOWN,
				SmsConst_SmscDeliveryStatus.UNKNOWN);
		smsDeliveryStatus.put(DeliveryReceiptState.REJECTD,
				SmsConst_SmscDeliveryStatus.REJECTED);
		isSmsDeliveryStatusPopulated = true;
	}

	public static int getSmsDeliveryStatus(
			DeliveryReceiptState deliveryReceiptState) {
		if (!isSmsDeliveryStatusPopulated) {
			setupHashMap();
		}
		return smsDeliveryStatus.get(deliveryReceiptState);
	}

}
