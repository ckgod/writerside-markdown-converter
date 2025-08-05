package com.github.ckgod.markdownconverter.task

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class BackgroundTask<Request, Response>(
    project: Project,
    title: String,
    canBeCancelled: Boolean,
    private val request: Request,
    private val backgroundTask: (Request, ProgressIndicator) -> Response,
    private val onSuccess: (Response) -> Unit,
    private val onCancel: () -> Unit,
    private val onFailure: (Throwable) -> Unit
) : Task.Backgroundable(project, title, canBeCancelled) {

    private var response: Response? = null
    private var error: Throwable? = null

    override fun run(indicator: ProgressIndicator) {
        try {
            response = backgroundTask(request, indicator)
        } catch (e: Throwable) {
            error = e
        }
    }

    override fun onSuccess() {
        response?.let(onSuccess)
    }

    override fun onCancel() {
        onCancel.invoke()
    }

    override fun onThrowable(error: Throwable) {
        onFailure(this.error ?: error)
    }
}

class BackgroundTaskBuilder<Request, Response>() {
    var backgroundTask: (Request, ProgressIndicator) -> Response = { _, _ -> throw IllegalStateException("backgroundTask is not defined.") }
    var onSuccess: (Response) -> Unit = {}
    var onCancel: () -> Unit = {}
    var onFailure: (Throwable) -> Unit = {}

    fun backgroundTask(block: (Request, ProgressIndicator) -> Response) {
        this.backgroundTask = block
    }

    fun onSuccess(block: (Response) -> Unit) {
        this.onSuccess = block
    }

    fun onCancel(block: () -> Unit) {
        this.onCancel = block
    }

    fun onFailure(block: (Throwable) -> Unit) {
        this.onFailure = block
    }
}

fun <Request, Response> runBackgroundTask(
    project: Project,
    request: Request,
    title: String,
    canBeCancelled: Boolean = true,
    dsl: BackgroundTaskBuilder<Request, Response>.() -> Unit
) {
    val builder = BackgroundTaskBuilder<Request, Response>().apply(dsl)

    val task = BackgroundTask(
        project = project,
        title = title,
        canBeCancelled = canBeCancelled,
        request = request,
        backgroundTask = builder.backgroundTask,
        onSuccess = builder.onSuccess,
        onCancel = builder.onCancel,
        onFailure = builder.onFailure
    )

    task.queue()
}