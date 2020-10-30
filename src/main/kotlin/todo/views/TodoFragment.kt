package todo.views

import javafx.beans.property.SimpleStringProperty
import javafx.scene.layout.Priority
import todo.controllers.Task
import todo.controllers.TaskController
import todo.models.TaskModel
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class TodoFragment : ListCellFragment<Task>() {
    private val controller: TaskController by inject()
    private val taskModel = TaskModel(itemProperty)
    private val maskedCreatedProperty = SimpleStringProperty()
    var maskedCreate: String by maskedCreatedProperty
    private val task: Task? by itemProperty

    override val root = hbox {
        checkbox(property = taskModel.isFinished) {
            action {
                controller.completeTask(taskModel)
            }
        }
        label(taskModel.task) {
            hgrow = Priority.ALWAYS
            useMaxSize = true
        }
        label(maskedCreatedProperty)

        onDoubleClick {
            controller.currentTask = taskModel
            find<TaskConfig>().openModal()
        }

        maskedCreate = maskTime()
        controller.currentTimeProperty.onChange {
            maskedCreate = maskTime()
        }
    }

    private fun maskTime(): String {
        if (task == null) {
            return ""
        }
        val createdDate = LocalDateTime.parse(task!!.created, controller.dateFormatter)
        val currentDate = LocalDateTime.parse(controller.currentTime, controller.dateFormatter)
        val timeDiff = ChronoUnit.MILLIS.between(createdDate, currentDate).millis
        if (timeDiff < 1.minutes) {
            return "Now"
        }
        if (timeDiff < 1.hours) {
            val minutes = timeDiff.toMinutes().toInt()
            return "$minutes minute${minutes.plural} ago"
        }
        if (timeDiff < 24.hours) {
            val hours = timeDiff.toHours().toInt()
            return "$hours hour${hours.plural} ago"
        }

        return createdDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    private val Int.plural: String get() = if (this == 1) "" else "s"
}