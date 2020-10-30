package todo.main

import javafx.geometry.Pos
import tornadofx.*

class Styles: Stylesheet() {
    companion object {
        val topLayout by cssclass()
        val toDoLabel by cssclass()
    }

    init {
        topLayout {
            padding = box(10.px)
            alignment = Pos.CENTER
        }
        toDoLabel {
            fontSize = 40.px
        }

    }
}