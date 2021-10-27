package com.gs.todoproj.dto

import java.time.Instant
import com.gs.todoproj.model.Task

class TaskMapper {
  companion object {
     fun toTaskDto(task: Task) : TaskDto = TaskDto(
     id = task.id,
     title = task.title,
     isDone = task.isDone,
     created = task.created,
     lastModified = task.lastModified)
  }
}