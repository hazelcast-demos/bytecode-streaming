package org.hazelcast.bytecodestream

import com.hazelcast.client.config.ClientConfig
import com.hazelcast.core.EntryEvent
import com.hazelcast.jet.Jet
import com.hazelcast.map.listener.EntryUpdatedListener
import java.lang.instrument.ClassDefinition
import java.lang.instrument.Instrumentation

fun agentmain(args: String?, instrumentation: Instrumentation) {
    val clientConfig = ClientConfig().apply {
        clusterName = "jet"
    }
    Jet.newJetClient(clientConfig).hazelcastInstance
        .getMap<String, ByteArray>("bytecode")
        .addEntryListener(BytecodeChangeListener(instrumentation), true)
}

class BytecodeChangeListener(private val instrumentation: Instrumentation) : EntryUpdatedListener<String, ByteArray> {

    override fun entryUpdated(event: EntryEvent<String, ByteArray>) {
        try {
            val clazz = Class.forName(event.key)
            instrumentation.redefineClasses(ClassDefinition(clazz, event.value))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}