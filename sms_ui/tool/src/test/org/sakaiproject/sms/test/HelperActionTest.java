/***********************************************************************************
 * HelperActionTest.java
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
package org.sakaiproject.sms.test;

import junit.framework.TestCase;

import org.sakaiproject.sms.beans.ActionResults;
import org.sakaiproject.sms.beans.HelperActionBean;
import org.sakaiproject.sms.hibernate.model.SmsTask;
import org.sakaiproject.sms.otp.SmsTaskLocator;
import org.sakaiproject.sms.test.stubs.SmsCoreStub;
import org.sakaiproject.sms.test.stubs.SmsServiceStub;

import uk.org.ponder.messageutil.TargettedMessageList;

public class HelperActionTest extends TestCase {

	private HelperActionBean helperAction;
	private TargettedMessageList messages;
	private SmsTaskLocator smsTaskLocator;
	private SmsCoreStub smsCore;
	private SmsServiceStub smsService;

	/**
	 * setUp to run at start of every test
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	public void setUp() {
		helperAction = new HelperActionBean();
		messages = new TargettedMessageList();
		smsTaskLocator = new SmsTaskLocator();
		smsCore = new SmsCoreStub();
		smsService = new SmsServiceStub();

		smsTaskLocator.setSmsCore(smsCore);

		helperAction.setMessages(messages);
		helperAction.setSmsCore(smsCore);
		helperAction.setSmsService(smsService);
		helperAction.setSmsTaskLocator(smsTaskLocator);
	}

	/**
	 * Test cancel method
	 */
	public void testCancel() {
		smsTaskLocator.locateBean(SmsTaskLocator.NEW_1);
		assertTrue(smsTaskLocator.containsNew());
		String result = helperAction.cancel();
		assertEquals(ActionResults.CANCEL, result);
		assertFalse(smsTaskLocator.containsNew());
	}

	/**
	 * If no beans on tasklocator
	 */
	public void testDoContinue_invalid() {
		assertFalse(smsTaskLocator.containsNew());
		String result = helperAction.doContinue();
		assertEquals(ActionResults.ERROR, result);
		assertFalse(smsService.calculateCalled);
		assertEquals("sms.errors.unexpected-error", messages.messageAt(0)
				.acquireMessageCode());
	}

	/**
	 * Test doContinue method
	 */
	public void testDoContinue_valid() {
		SmsTask smsTask = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		assertNull(smsTask.getCreditEstimate());
		String result = helperAction.doContinue();
		assertEquals(ActionResults.CONTINUE, result);
		assertTrue(smsService.calculateCalled);
		assertNotNull(smsTask.getCreditEstimate());
	}

	/**
	 * Test send with insufficient credits
	 */
	public void testSend_insufficientCredits() {
		SmsTask task = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		task.setCreditEstimate(2);
		smsService.sufficientCredits = false;
		String result = helperAction.send();
		assertEquals(ActionResults.ERROR, result);
		assertFalse(smsCore.insertTaskCalled);
		assertEquals("sms.errors.insufficient-credits", messages.messageAt(0)
				.acquireMessageCode());
		assertFalse(smsTaskLocator.containsNew());
	}

	/**
	 * If no beans on tasklocator
	 */
	public void testSend_invalid() {
		assertFalse(smsTaskLocator.containsNew());
		String result = helperAction.send();
		assertEquals(ActionResults.ERROR, result);
		assertFalse(smsCore.insertTaskCalled);
		assertEquals("sms.errors.unexpected-error", messages.messageAt(0)
				.acquireMessageCode());
	}

	/**
	 * Test send with sufficient credits
	 */
	public void testSend_sufficientCredits() {
		SmsTask task = (SmsTask) smsTaskLocator
				.locateBean(SmsTaskLocator.NEW_1);
		task.setCreditEstimate(2);
		smsService.sufficientCredits = true;
		String result = helperAction.send();
		assertEquals(ActionResults.SUCCESS, result);
		assertTrue(smsCore.insertTaskCalled);
		assertEquals("sms.helper.task-success", messages.messageAt(0)
				.acquireMessageCode());
		assertFalse(smsTaskLocator.containsNew());
	}

}
