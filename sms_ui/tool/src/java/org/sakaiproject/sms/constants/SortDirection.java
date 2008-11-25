package org.sakaiproject.sms.constants;

public enum SortDirection {

	  ASC("asc"),
	  DESC("desc");
	  
	  private String code;

	  private SortDirection(String code) {
	    this.code = code;
	  }

	  public Integer getOrdinal() {
	    return ordinal();
	  }

	  public String getName() {
	    return name();
	  }

	  public String getCode() {
	    return code;
	  }

	  public static SortDirection findByCode(String code) {
	    for (SortDirection sortDirection : SortDirection.values()) {
	      if (sortDirection.getCode().equalsIgnoreCase(code)) return sortDirection;
	    }
	    return null;
	  }
}
