/***********************************************************************************
 * FloatEditorTest.java
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

import java.beans.PropertyEditor;

import junit.framework.TestCase;

import org.sakaiproject.sms.util.FloatEditorFactory;
import org.sakaiproject.sms.util.SmsCustomFloatEditor;

/**
 * Tests associated with {@link FloatEditorFactory} and
 * {@link SmsCustomFloatEditor}
 * 
 */
public class FloatEditorTest extends TestCase {
	FloatEditorFactory floatEditorFactory;

	@Override
	protected void setUp() throws Exception {
		floatEditorFactory = new FloatEditorFactory();
	}

	/**
	 * Test PropertyEditor retrieved from FloatEditorFactory
	 */
	public void testFloatEditorFactory() {
		PropertyEditor editor = floatEditorFactory.getPropertyEditor();
		assertNotNull(editor);
		assertTrue(editor instanceof SmsCustomFloatEditor);
	}

	/**
	 * Test empty + whitespace
	 */
	public void testSmsCustomFloatEditor_empty() {
		SmsCustomFloatEditor editor = (SmsCustomFloatEditor) floatEditorFactory
				.getPropertyEditor();
		editor.setAsText("");
		assertNull(editor.getValue());

		editor.setAsText(" ");
		assertNull(editor.getValue());
	}

	/**
	 * Test invalid number
	 */
	public void testSmsCustomFloatEditor_invalid() {
		SmsCustomFloatEditor editor = (SmsCustomFloatEditor) floatEditorFactory
				.getPropertyEditor();
		try {
			editor.setAsText("xXxxX");
			fail("NumberFormatException should be thrown");
		} catch (NumberFormatException nfe) {
			assertNotNull(nfe);
		}

		assertNull(editor.getValue());
	}

	/**
	 * Test null
	 */
	public void testSmsCustomFloatEditor_null() {
		SmsCustomFloatEditor editor = (SmsCustomFloatEditor) floatEditorFactory
				.getPropertyEditor();
		editor.setAsText(null);
		assertNull(editor.getValue());
	}

	/**
	 * Test valid numbers
	 */
	public void testSmsCustomFloatEditor_valid() {
		SmsCustomFloatEditor editor = (SmsCustomFloatEditor) floatEditorFactory
				.getPropertyEditor();

		editor.setAsText("200");
		assertNotNull(editor.getValue());
		assertTrue(editor.getValue() instanceof Float);
		Float value = (Float) editor.getValue();
		assertEquals(200f, value);

		editor.setAsText("123.01");
		value = (Float) editor.getValue();
		assertEquals(123.01f, value);

		editor.setAsText("456.2");
		value = (Float) editor.getValue();
		assertEquals(456.2f, value);
	}
}
