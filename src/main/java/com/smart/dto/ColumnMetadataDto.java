package com.smart.dto;

public class ColumnMetadataDto {

	private String originalName;
	private String renamedName;

	public ColumnMetadataDto() {
		super();
	}

	public ColumnMetadataDto(String originalName, String renamedName) {
		super();
		this.originalName = originalName;
		this.renamedName = renamedName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getRenamedName() {
		return renamedName;
	}

	public void setRenamedName(String renamedName) {
		this.renamedName = renamedName;
	}

}
