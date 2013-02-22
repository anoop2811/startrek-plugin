package com.cquent.startrek.recognition;

public enum RecognitionType {
	CREATE_CLASS ("computer create java class"),
	CREATE_INTERFACE ("computer create java interface"),
	CREATE_JAVA_PROJECT ("computer create project"),
	ADD_SPRING_NATURE ("computer add spring nature"),
	CREATE_PACKAGE ("computer create package"),
	UNRECOGNIZED_COMMAND("command not recognized");

	private String value;
	
	RecognitionType(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}	
	
	public static RecognitionType fromString(String cValue) {
	    if (cValue != null) {
	      for (RecognitionType rType : RecognitionType.values()) {
	        if (cValue.equalsIgnoreCase(rType.value)) {
	          return rType;
	        }
	      }
	    }
		return UNRECOGNIZED_COMMAND;
	}
}
