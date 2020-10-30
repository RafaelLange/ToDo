package todo.models

import javafx.beans.property.ObjectProperty
import todo.controllers.Task
import tornadofx.ItemViewModel

class TaskModel(item: ObjectProperty<Task>): ItemViewModel<Task>(itemProperty = item){
    val task = bind(Task::task)
    val isFinished = bind(Task::isFinished)
    val taskDescription = bind(Task::taskDescription)
    val created = bind(Task::created)
    val completed = bind(Task::completed)
}
