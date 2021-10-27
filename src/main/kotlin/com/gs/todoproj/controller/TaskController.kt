package com.gs.todoproj.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.transaction.annotation.Transactional
import org.slf4j.LoggerFactory
import org.slf4j.Logger

import com.gs.todoproj.service.TaskService
import com.gs.todoproj.dto.TaskDto

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val taskService: TaskService) {
  
  val logger: Logger = LoggerFactory.getLogger(javaClass)
 
  @GetMapping("")
  fun findAll() = taskService.findAll()
 
  @PostMapping("")
  @Transactional
  fun create(@RequestParam title: String, @RequestParam isDone: Boolean?)
    = taskService.addNew(TaskDto(null, title, isDone))
 
  @GetMapping("/{id}")
  fun findById(@PathVariable id: Long) = taskService.findById(id)
 
  @PatchMapping("/{id}")
  @Transactional
  fun update(@PathVariable id: Long, @RequestParam title: String?, @RequestParam isDone: Boolean?)
    = taskService.update(TaskDto(id, title, isDone))
}