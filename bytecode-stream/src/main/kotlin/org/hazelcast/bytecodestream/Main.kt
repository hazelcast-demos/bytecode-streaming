package org.hazelcast.bytecodestream

import com.hazelcast.jet.Jet
import com.hazelcast.jet.JetInstance
import com.hazelcast.jet.config.JobConfig
import java.io.Closeable

fun main() {
    Jet.newJetInstance().withCloseable().use {
        with(jobConfig) {
            it.newJob(pipeline(), this).join()
        }
    }
}

private class CloseableJet(private val instance: JetInstance) : Closeable, JetInstance by instance {
    override fun close() {
        shutdown()
    }
}

private fun JetInstance.withCloseable() = CloseableJet(this)

private val jobConfig = JobConfig()
    .addClass(
        TargetPathContext::class.java,
        ClassPathReader::class.java
    )