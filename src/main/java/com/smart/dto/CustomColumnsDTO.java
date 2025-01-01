package com.smart.dto;

import java.util.List;

public class CustomColumnsDTO {
	
	private List<CustomColumn> columnsToSave;
	private List<CustomColumn> columnsToDelete;
	
	
	
	public List<CustomColumn> getColumnsToSave() {
		return columnsToSave;
	}



	public void setColumnsToSave(List<CustomColumn> columnsToSave) {
		this.columnsToSave = columnsToSave;
	}



	public List<CustomColumn> getColumnsToDelete() {
		return columnsToDelete;
	}



	public void setColumnsToDelete(List<CustomColumn> columnsToDelete) {
		this.columnsToDelete = columnsToDelete;
	}



	public static class CustomColumn {
		private String customColumnName;
		
		public String getCustomColumnName() {
			return customColumnName;
		}
		
		public void setCustomColumnName(String customColumnName) {
			this.customColumnName = customColumnName;
		}
	}
	
	

}
