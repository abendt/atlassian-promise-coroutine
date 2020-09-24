package kotlinx.coroutines.atlassian

import io.atlassian.util.concurrent.Promises
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class PromiseSpec : StringSpec({

    "can await resolved Promise" {
        val result = Promises.promise("foo").await()

        result shouldBe "foo"
    }

    "can await rejected Promise" {
        shouldThrow<RuntimeException> {
            Promises.rejected<String>(RuntimeException("failed")).await()
        }
    }

    "can await successful Promise" {
        val promise = Promises.settablePromise<String>()

        val channel = Channel<String>()

        launch {
            channel.send(promise.await())
        }

        promise.set("hello")

        withTimeout(1000) {
            channel.receive() shouldBe "hello"
        }
    }

    "can await failing Promise" {
        val promise = Promises.settablePromise<String>()

        val channel = Channel<RuntimeException>()

        launch {
            try {
                promise.await()
            } catch (e: RuntimeException) {
                channel.send(e)
            }
        }

        promise.exception(RuntimeException("error"))

        withTimeout(1000) {
            channel.receive() shouldBe RuntimeException("error")
        }
    }


})