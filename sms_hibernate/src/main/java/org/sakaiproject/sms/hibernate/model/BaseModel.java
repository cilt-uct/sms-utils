/***********************************************************************************
 * BaseModel.java
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

package org.sakaiproject.sms.hibernate.model;

import java.io.Serializable;

/**
 * This is the base model class from which all model class should inherit.
 * 
 * It also holds the id field that should be used as the unique identifier for
 * all the model classes.
 * 
 * @author Julian Wyngaard
 * @version 1.0
 * @created 19-Nov-2008
 */
public abstract class BaseModel implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Represent the unique id for the model object.
	 * */
	private Long id;

	/**
	 * Exists.
	 * 
	 * @return true if this entity already exists in the persistent store - we
	 *         determine this if id is allocated.
	 */
	public boolean exists() {
		if (getId() != null)
			return true;
		return false;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 *            the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		/*
		 * final int prime = 31; int result = 1; result = prime result + ((id ==
		 * null) ? 0 : id.hashCode()); return result;
		 */
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		/*
		 * if (this == obj) return true; if (obj == null) return false; if
		 * (!(obj instanceof BaseModel)) return false; BaseModel other =
		 * (BaseModel) obj; if (id == null) { if (other.id != null) return
		 * false; } else if (!id.equals(other.id)) return false; return true;
		 */
		return false;
	}

}
