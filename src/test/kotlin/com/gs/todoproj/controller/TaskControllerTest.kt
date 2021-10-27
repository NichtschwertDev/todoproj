package com.gs.todoproj.controller

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.Mockito
import java.time.Instant

import com.gs.todoproj.dto.TaskDto
import com.gs.todoproj.TaskNotFoundException
import com.gs.todoproj.service.TaskServiceImpl

import com.gs.todoproj.service.TaskService

@WebMvcTest(TaskController::class)
class TaskControllerTest {

  @Autowired
  private lateinit var mvc: MockMvc;
  
  @MockBean
  private lateinit var taskService: TaskService
  
  private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)

  @BeforeEach
  fun setUp() {
    
    val existingTaskDto = TaskDto(
            id = 1,
            title = "test task",
            isDone = false,
            lastModified = Instant.now(),
            created = Instant.now())
    
    val newTaskDto = TaskDto(
            id = 2,
            title = "new task",
            isDone = false,
            lastModified = Instant.now(),
            created = Instant.now())
    
    val updatedTaskDto = TaskDto(
            id = 1,
            title = "updated task",
            isDone = true,
            lastModified = Instant.now(),
            created = Instant.now())
    
    Mockito
      .`when`(taskService.findAll())
      .thenReturn(sequenceOf(existingTaskDto).asIterable())
    
    Mockito
      .`when`(taskService.findById(3))
      .thenThrow(TaskNotFoundException("No Task could be found for id"))
    
    Mockito
      .`when`(taskService.findById(1))
      .thenReturn(existingTaskDto)
    
    Mockito
      .`when`(taskService.addNew(any(TaskDto::class.java)))
      .thenReturn(newTaskDto)
    
    Mockito
      .`when`(taskService.update(any(TaskDto::class.java)))
      .thenReturn(updatedTaskDto)
    
  }
  
  @Test
  fun getAllShouldReturnListWithOneTask() {
    this.mvc
        .perform(MockMvcRequestBuilders.get("/api/v1/tasks"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("test task"))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].isDone").value(false));
  }
  
  @Test
  fun getSingleShouldReturnTask() {
    this.mvc
        .perform(MockMvcRequestBuilders.get("/api/v1/tasks/1"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test task"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(false));
  }
  
  @Test
  fun postShouldReturnTask() {
    this.mvc
        .perform(MockMvcRequestBuilders.post("/api/v1/tasks?title=new task&isDone=false"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("new task"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(false));
  }
  
  @Test
  fun patchShouldReturnTask() {
    this.mvc
        .perform(MockMvcRequestBuilders.patch("/api/v1/tasks/1?title=updated task&isDone=true"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("updated task"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.isDone").value(true));
  }
  
  @Test
  fun getInvalidShouldReturnError() {
    this.mvc
        .perform(MockMvcRequestBuilders.get("/api/v1/tasks/3"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isDone").doesNotExist());
  }
  
  @Test
  fun postToIdShouldReturnError() {
    this.mvc
        .perform(MockMvcRequestBuilders.post("/api/v1/tasks/3"))
        .andExpect(MockMvcResultMatchers.status().isMethodNotAllowed())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").doesNotExist())
        .andExpect(MockMvcResultMatchers.jsonPath("$.isDone").doesNotExist());
  }
}