package org.hazelcast.bytecodestream

import com.hazelcast.jet.Util.entry
import com.hazelcast.jet.pipeline.Pipeline
import com.hazelcast.jet.pipeline.Sinks

fun pipeline() = Pipeline.create().apply {
    readFrom(classes())
        .withIngestionTimestamps()
        .peek { it.first }
        .map { entry(it.first, it.second) }
        .writeTo(Sinks.map("bytecode"))
}