package org.ricardo.school_system.assemblers;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DegreeSubjectBundle {

	private String degreeName;
	private List<Integer> subjectIds;
	
	@JsonCreator
	public DegreeSubjectBundle(@JsonProperty("degreeName") String degreeName, 
				               @JsonProperty("subjectIds") List<Integer> subjectIds) {
		this.subjectIds = subjectIds;
		this.degreeName = degreeName;
	}

	public List<Integer> getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(List<Integer> subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getDegreeName() {
		return degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	@Override
	public String toString() {
		return "DegreeSubjectBundle [degreeName=" + degreeName + ", subjectIds=" + subjectIds + "]";
	}
	
}
