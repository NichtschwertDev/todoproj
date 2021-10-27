package com.gs.todoproj.service

import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Instant

import com.gs.todoproj.TaskNotFoundException
import com.gs.todoproj.RequiredPropertyException
import com.gs.todoproj.dto.TaskDto
import com.gs.todoproj.dto.TaskMapper
import com.gs.todoproj.repository.TaskRepository
import com.gs.todoproj.model.Task

@Component
class TaskServiceImpl (private val repository:TaskRepository) : TaskService {
  
  val logger: Logger = LoggerFactory.getLogger(javaClass)
  
  override fun findAll(): Iterable<TaskDto> {
    val tasks = repository.findAll()
    return tasks.map{TaskMapper.toTaskDto(it)}
  }
  override fun findById(id: Long): TaskDto {
    val task: Task = repository.findById(id)
      .orElseThrow({TaskNotFoundException("No Task could be found for id ${id}")})
    return TaskMapper.toTaskDto(task)
  }
  override fun addNew(taskDto: TaskDto): TaskDto {
    val task = Task(
      title = taskDto.title?: throw RequiredPropertyException("missing property: title"),
      isDone = taskDto.isDone?: false,
      lastModified = Instant.now(),
      created = Instant.now())
    val result = repository.saveAndFlush(task)
    return TaskMapper.toTaskDto(result)
  }
  override fun update(taskDto: TaskDto): TaskDto {
    val id: Long = taskDto.id?: throw RequiredPropertyException("missing property: id")
    val task: Task = repository.findById(id)
      .orElseThrow({TaskNotFoundException("No Task could be found for id ${id}")})
    task.title = taskDto.title?: task.title;
    task.isDone = taskDto.isDone?: task.isDone;
    task.lastModified = Instant.now();
    val result = repository.save(task);
    
    return TaskMapper.toTaskDto(result);
  }
}