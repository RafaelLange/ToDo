package todo.views

import todo.controllers.TaskController
import tornadofx.*

class MainView : View() {
    val controller: TaskController by inject()

    init{
        controller.openConnection()
        controller.createTable()
        controller.loadTasks()
    }

    override val root = borderpane {
        top(TopView::class)
        center(TodoList::class)
        bottom(BottomView::class)
    }
}