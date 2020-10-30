package todo.controllers

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import todo.models.TaskModel
import tornadofx.*
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TaskController : Controller() {
    private val dbFile = "c:/Temp/ToDo.db"
    val items = SortedFilteredList<Task>()
    lateinit var currentTask: TaskModel

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    val currentTimeProperty = SimpleStringProperty(dateFormatter.format(LocalDateTime.now()))
    var currentTime: String by currentTimeProperty

    val searchEnableProperty = SimpleBooleanProperty(false)
    var searchEnable: Boolean by searchEnableProperty


    init {
        val timeline = Timeline(KeyFrame(1.seconds, {
            currentTime = dateFormatter.format(LocalDateTime.now())
        }))
        timeline.cycleCount = Animation.INDEFINITE
        timeline.play()
    }

    fun openConnection() {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:sqlite:$dbFile"
        config.validate()
        val ds = HikariDataSource(config)
        Database.connect(ds)
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    fun createTable() {
        transaction {
            SchemaUtils.create(Tasks)
        }
    }

    fun loadTasks() {
        items.clear()
        transaction {
            items.addAll(Task.all().toList())
        }
    }

    fun createTask(newTask: String) {
        transaction {
            val task = Task.new {
                task = newTask
                isFinished = false
                taskDescription = ""
                created = dateFormatter.format(LocalDateTime.now())
                completed = ""
            }
            items.add(task)
        }
    }

    fun removeTask(task: Task) {
        transaction { task.delete() }
        try {
            items.remove(task)
        }
        catch (e: NullPointerException) {}
    }

    fun commitTask(task: TaskModel) {
        transaction {
            task.commit()
        }
        items.refilter()
    }

    fun completeTask(task: TaskModel) {
        transaction {
            task.completed.value = dateFormatter.format(LocalDateTime.now())
            task.commit()
        }
        items.refilter()
    }

    fun searchTask(taskText: String) {
        items.predicate = {it.task.contains(taskText, true)}
    }

    fun filterBy(status: FilterState) {
        if (searchEnable) {
            return
        }
        when (status) {
            FilterState.Pending -> items.predicate = {it.isFinished.not()}
            FilterState.Completed -> items.predicate = {it.isFinished}
            FilterState.All -> items.predicate = { true }
        }

    }
}

enum class FilterState { All, Pending, Completed }
