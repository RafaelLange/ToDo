package todo.views

import javafx.geometry.Pos
import todo.controllers.Task
import todo.controllers.TaskController
import tornadofx.*

class TaskConfig: Fragment("Task Info") {
    private val controller : TaskController by inject()
    private val taskModel = controller.currentTask
    private var onTaskDeleted = false

    override val root = vbox {
        form {
            fieldset {
                label("Task:")
                textfield(taskModel.task) {
                        required()
                        validator {
                            if (checkValidation(it)) error("Text length is bigger than 40 characters") else null
                        }
                    }
                label("Description")
                textarea(taskModel.taskDescription)
            }
        }
        hbox {
            paddingAll = 10
            spacing = 50.0
            hbox {
                spacing = 10.0
                label("Created:")
                label(taskModel.created)
            }
            hbox {
                spacing = 10.0
                label("Completed:")
                label(taskModel.completed)
            }
        }

        hbox {
            alignment = Pos.CENTER_RIGHT
            paddingAll = 10
            spacing = 30.0
            button("Save"){
                enableWhen(taskModel.valid)
                action {
                    controller.commitTask(taskModel)
                    close()
                }
            }
            button("Cancel") {
                action {
                    close()
                }
            }
            button("Delete") {
                action {
                    onTaskDeleted = true
                    controller.removeTask(taskModel.item)
                    close()
                }
            }
        }
    }

    private fun checkValidation(text: String?): Boolean {
        if (text == null) {
            return false
        }
        return if (onTaskDeleted) {
            false
        } else {
            text.length > 40
        }
    }
}