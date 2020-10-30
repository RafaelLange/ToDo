package todo.main

import javafx.application.Application
import javafx.stage.Stage
import todo.views.MainView
import tornadofx.App

class MainApp: App(MainView::class, Styles::class) {

    override fun start(stage: Stage) {
        with(stage) {
            minWidth = 400.0
            minHeight = 500.0
            isResizable = false
            super.start(this)
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(MainApp::class.java, *args)
}