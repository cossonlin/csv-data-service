package com.govtech.csvdata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	@Column
	public int totalElement;

	public static TaskBuilder builder() {
		return new TaskBuilder();
	}

	public static class TaskBuilder {
		private Long id;
		private int totalElement;

		public TaskBuilder() {
		}

		public TaskBuilder id(long id) {
			this.id = id;
			return this;
		}

		public TaskBuilder totalElement(int totalElement) {
			this.totalElement = totalElement;
			return this;
		}

		public Task build() {
			return new Task(id, totalElement);
		}
	}
}
