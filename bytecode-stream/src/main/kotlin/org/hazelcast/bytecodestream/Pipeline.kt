package org.hazelcast.bytecodestream

import com.hazelcast.function.BiPredicateEx
import com.hazelcast.jet.Util.entry
import com.hazelcast.jet.pipeline.Pipeline
import com.hazelcast.jet.pipeline.ServiceFactories
import com.hazelcast.jet.pipeline.Sinks
import com.hazelcast.map.IMap

fun pipeline() = Pipeline.create().apply {
    val mapName = "bytecode"
    readFrom(classes())
        .withIngestionTimestamps()
        .peek { it.first }
        .filterUsingService(
            ServiceFactories.iMapService(mapName),
            CheckChange()
        )
        .map { entry(it.first, it.second) }
        .writeTo(Sinks.map(mapName))
}

class CheckChange : BiPredicateEx<IMap<String, ByteArray>, Pair<String, ByteArray>> {

    override fun testEx(map: IMap<String, ByteArray>, pair: Pair<String, ByteArray>) =
        map[pair.first] == null || !pair.second.contentEquals(map[pair.first]!!)
}