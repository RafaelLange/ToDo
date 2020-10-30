package todo.views

import javafx.scene.Parent
import javafx.scene.layout.Priority
import todo.controllers.TaskController
import todo.main.Styles
import tornadofx.*

class TopView: View() {
    private val controller : TaskController by inject()
    private lateinit var validator: ValidationContext.Validator<String>

    override val root = vbox {
        addClass(Styles.topLayout)
        label {
            addClass(Styles.toDoLabel)
            text = "ToDo"
        }
        hbox {
            val textFieldAddTask = textfield {
                removeWhen { controller.searchEnableProperty }
                hgrow = Priority.ALWAYS
                promptText = "Type a Task and ENTER"
                action {
                    if (validator.validate()) {
                        controller.createTask(text)
                        clear()
                    }
                }
            }

            val textFieldSearchTask = textfield {
                removeWhen { controller.searchEnableProperty.not() }
                hgrow = Priority.ALWAYS
                promptText = "Type something to Search a Task"
                action {
                    controller.searchTask(text)
                }
            }

            val context = ValidationContext()
            validator = context.addValidator(textFieldAddTask, textFieldAddTask.textProperty()) {
                if (it?.length!! > 40) error("Text length is bigger than 40 characters") else null
            }

            togglebutton("Search") {
                isSelected = false
                action {
                    controller.searchEnable = isSelected
                    controller.searchTask("")
                    textFieldAddTask.text = ""
                    textFieldSearchTask.text = ""
                }
            }
        }
    }
}