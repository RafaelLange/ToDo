package todo.views

import org.jetbrains.exposed.sql.transactions.transaction
import todo.controllers.Task
import todo.controllers.TaskController
import tornadofx.*

class TodoList: View() {
    private val controller: TaskController by inject()

    override val root =
        listview(controller.items) {
        cellFragment(TodoFragment::class)
    }
}