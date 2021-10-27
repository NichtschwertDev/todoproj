package com.gs.todoproj.service

import com.gs.todoproj.dto.TaskDto

interface TaskService {
  fun findAll(): Iterable<TaskDto>
  fun findById(id: Long): TaskDto
  fun addNew(taskDto: TaskDto): TaskDto
  fun update(taskDto: TaskDto): TaskDto
}