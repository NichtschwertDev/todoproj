package com.gs.todoproj.model

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import java.time.Instant
 
@Entity
class Task(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = -1,
	var title: String,
	var isDone: Boolean,
    val created: Instant,
    var lastModified: Instant) {
}