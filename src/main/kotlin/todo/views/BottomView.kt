package todo.views

import javafx.scene.layout.Priority
import todo.controllers.FilterState
import todo.controllers.TaskController
import tornadofx.*

class BottomView: View() {
    val controller: TaskController by inject()
    val countItems = integerBinding(controller.items.items) { count { it.isFinished.not() } }

    override val root = borderpane {
        paddingAll = 10
        left {
            label(stringBinding(countItems) { "$value item${value.plural} left" }) {
                hgrow = Priority.ALWAYS
            }
        }
        right {
            hbox {
                spacing = 10.0
                togglegroup {
                    for (status in FilterState.values()) {
                        togglebutton(status.name) { action { controller.filterBy(status) } }
                    }
                }
            }
        }
    }

    private val Int.plural: String get() = if (this == 1) "" else "s"
}