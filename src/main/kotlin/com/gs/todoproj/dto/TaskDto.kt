package com.gs.todoproj.dto

import java.time.Instant

class TaskDto(
    val id: Long? = -1,
    val title: String?,
    val isDone: Boolean?,
    val created: Instant?,
    val lastModified: Instant?) {
  constructor(id: Long?, title: String?, isDone: Boolean?) : this(id, title, isDone, null, null)
}