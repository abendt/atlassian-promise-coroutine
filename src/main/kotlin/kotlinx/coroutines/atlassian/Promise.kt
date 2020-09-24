package kotlinx.coroutines.atlassian

import io.atlassian.util.concurrent.Promise
import io.atlassian.util.concurrent.Promises
import kotlinx.coroutines.future.await

suspend fun <T> Promise<T>.await(): T =
        Promises.toCompletableFuture(this).await()
