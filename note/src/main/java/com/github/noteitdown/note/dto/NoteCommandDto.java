package com.github.noteitdown.note.dto;

import java.util.List;

public class NoteCommandDto {

	private String transactionId;

	private List<NoteOperationDto> operations;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public List<NoteOperationDto> getOperations() {
		return operations;
	}

	public void setOperations(List<NoteOperationDto> operations) {
		this.operations = operations;
	}

	public static class NoteOperationDto {

		private String noteId;

		private String parentNoteId;

		private Boolean completed;

		private String type;

		private String content;

		public String getNoteId() {
			return noteId;
		}

		public void setNoteId(String noteId) {
			this.noteId = noteId;
		}

		public String getParentNoteId() {
			return parentNoteId;
		}

		public void setParentNoteId(String parentNoteId) {
			this.parentNoteId = parentNoteId;
		}

		public Boolean getCompleted() {
			return completed;
		}

		public void setCompleted(Boolean completed) {
			this.completed = completed;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
