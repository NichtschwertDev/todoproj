package com.gs.todoproj.service

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito
import java.time.Instant
import java.util.Optional
import java.lang.NullPointerException

import com.gs.todoproj.repository.TaskRepository
import com.gs.todoproj.dto.TaskDto
import com.gs.todoproj.TaskNotFoundException
import com.gs.todoproj.RequiredPropertyException
import com.gs.todoproj.model.Task

@ExtendWith(SpringExtension::class)
class TaskServiceImplTest {

  @TestConfiguration
  class TaskServiceImplTestContextConfiguration {
 
    @Bean
    fun taskService(repository: TaskRepository): TaskService {
      return TaskServiceImpl(repository)
    }
  }

  @Autowired
  private lateinit var taskService: TaskService;
  
  @MockBean
  private lateinit var taskRepository: TaskRepository
  
  private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
  
  private val originalCreationInstant = Instant.now()
  
  fun mockSaveTask(task: Task): Task {
    /*if (task.id < 0) {
      task.id = 2
    }*/
    if (task.id < 0) {
      return Task(
            id = 2,
            title = task.title,
            isDone = task.isDone,
            lastModified = task.lastModified,
            created = task.created)
    }
    return task
  }

  @BeforeEach
  fun setUp() {
    
    val existingTask = Task(
            id = 1,
            title = "test task",
            isDone = false,
            lastModified = originalCreationInstant,
            created = originalCreationInstant)
    
    Mockito
      .`when`(taskRepository.findAll())
      .thenReturn(listOf(existingTask))
    
    Mockito
      .`when`(taskRepository.findById(1))
      .thenReturn(Optional.of(existingTask))
    
    Mockito
      .`when`(taskRepository.findById(3))
      .thenReturn(Optional.empty())
    
    Mockito
      .`when`(taskRepository.saveAndFlush(any(Task::class.java)))
      .thenAnswer({it -> mockSaveTask(it.getArguments()[0] as Task)})
    
    Mockito
      .`when`(taskRepository.save(any(Task::class.java)))
      .thenAnswer({it -> mockSaveTask(it.getArguments()[0] as Task)})
  }

  @Test
  fun findAllShouldReturnListWithOneTask() {
    val result = taskService.findAll();
    
    Assertions.assertEquals(result.count(), 1)
    
    val resultTaskDto = result.first()
    
    Assertions.assertEquals(1, resultTaskDto.id)
    Assertions.assertEquals("test task", resultTaskDto.title)
    Assertions.assertFalse(resultTaskDto.isDone?:throw NullPointerException())
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.created)
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.lastModified)
  }

  @Test
  fun findById1ShouldReturnTask() {
    val resultTaskDto = taskService.findById(1);
    
    Assertions.assertEquals(1, resultTaskDto.id)
    Assertions.assertEquals("test task", resultTaskDto.title)
    Assertions.assertFalse(resultTaskDto.isDone?:throw NullPointerException())
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.created)
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.lastModified)
  }

  @Test
  fun addNewShouldReturnTask() {
    val taskDto = TaskDto(id = null, title = "new task", isDone = false)
    
    val resultTaskDto = taskService.addNew(taskDto);
    
    Assertions.assertEquals(2, resultTaskDto.id)
    Assertions.assertEquals("new task", resultTaskDto.title)
    Assertions.assertFalse(resultTaskDto.isDone?:throw NullPointerException())
    Assertions.assertEquals(resultTaskDto.lastModified, resultTaskDto.created)
  }

  @Test
  fun updateShouldReturnTask() {
    val taskDto = TaskDto(id = 1, title = "updated task", isDone = true)
    
    val resultTaskDto = taskService.update(taskDto);
    
    Assertions.assertEquals(1, resultTaskDto.id)
    Assertions.assertEquals("updated task", resultTaskDto.title)
    Assertions.assertTrue(resultTaskDto.isDone?:throw NullPointerException())
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.created)
    Assertions.assertNotEquals(originalCreationInstant, resultTaskDto.lastModified)
  }

  @Test
  fun updateWithoutTitleShouldReturnTask() {
    val taskDto = TaskDto(id = 1, title = null, isDone = true)
    
    val resultTaskDto = taskService.update(taskDto);
    
    Assertions.assertEquals(1, resultTaskDto.id)
    Assertions.assertEquals("test task", resultTaskDto.title)
    Assertions.assertTrue(resultTaskDto.isDone?:throw NullPointerException())
    Assertions.assertEquals(originalCreationInstant, resultTaskDto.created)
    Assertions.assertNotEquals(originalCreationInstant, resultTaskDto.lastModified)
  }

  @Test
  fun findById3ShouldThrowError() {
    Assertions.assertThrows(TaskNotFoundException::class.java, { -> taskService.findById(3)})
  }

  @Test
  fun addNewWithoutTitleShouldThrowError() {
    val taskDto = TaskDto(id = null, title = null, isDone = false)
    Assertions.assertThrows(RequiredPropertyException::class.java, { -> taskService.addNew(taskDto)})
  }

  @Test
  fun updateWithoutIdShouldThrowError() {
    val taskDto = TaskDto(id = null, title = "updated task", isDone = true)
    Assertions.assertThrows(RequiredPropertyException::class.java, { -> taskService.update(taskDto)})
  }
}