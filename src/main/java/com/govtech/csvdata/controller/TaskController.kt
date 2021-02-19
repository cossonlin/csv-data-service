package com.govtech.csvdata.controller

import com.govtech.csvdata.service.TaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/task")
class TaskController(private val taskService: TaskService) {

    @GetMapping("/{taskId}")
    fun queryTaskProgress(@PathVariable taskId: Long): ResponseEntity<Int>? {
        return ResponseEntity.ok(taskService!!.queryTaskProgress(taskId))
    }
}