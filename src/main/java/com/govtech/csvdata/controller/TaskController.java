package com.govtech.csvdata.controller;

import com.govtech.csvdata.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	private TaskService taskService;

	@GetMapping("/{taskId}")
	public ResponseEntity<Integer> queryTaskProgress(@PathVariable long taskId) {
		return ResponseEntity.ok(taskService.queryTaskProgress(taskId));
	}
}
