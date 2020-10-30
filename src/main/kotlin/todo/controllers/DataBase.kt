package todo.controllers

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


object Tasks : IntIdTable("Tasks") {
    val task = varchar("task", 40)
    val isFinished = bool("isFinished")
    val taskDescription = text("taskDescription")
    val created = varchar("created", 28)
    val completed = varchar("completed", 28)
}

class Task(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Task>(Tasks)

    var task by Tasks.task
    var isFinished by Tasks.isFinished
    var taskDescription by Tasks.taskDescription
    var created by Tasks.created
    var completed by Tasks.completed
}
