package com.gs.todoproj.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

import com.gs.todoproj.model.Task

@Repository
interface TaskRepository : JpaRepository<Task, Long>