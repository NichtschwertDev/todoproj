package com.gs.todoproj

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No Task exists for that id.") //404
class TaskNotFoundException : RuntimeException {
  constructor() : super()
  constructor(message: String) : super(message)
}

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Required property is missing") //400
class RequiredPropertyException : RuntimeException {
  constructor() : super()
  constructor(message: String) : super(message)
}