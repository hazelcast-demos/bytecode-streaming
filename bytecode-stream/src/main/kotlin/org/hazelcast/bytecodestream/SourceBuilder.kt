package org.hazelcast.bytecodestream

import com.hazelcast.jet.core.Processor.Context
import com.hazelcast.function.BiConsumerEx
import com.hazelcast.function.FunctionEx
import com.hazelcast.jet.pipeline.SourceBuilder
import com.hazelcast.jet.pipeline.SourceBuilder.SourceBuffer
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

fun classes() = SourceBuilder
    .stream("classes", TargetPathContext())
    .fillBufferFn(ClassPathReader())
    .build()

class TargetPathContext : FunctionEx<Context, ContextHolder> {
    override fun applyEx(ctx: Context) = ContextHolder()
}

class ClassPathReader : BiConsumerEx<ContextHolder, SourceBuffer<Pair<String, ByteArray>>> {

    override fun acceptEx(ctx: ContextHolder, buffer: SourceBuffer<Pair<String, ByteArray>>) {
        val toClassName = PathToClassName(ctx.classesDirectory)
        val visitor = ClassFileVisitor()
        Files.walkFileTree(ctx.classesDirectory, visitor)
        visitor.classes.forEach {
            val name = toClassName.applyEx(it)
            val content = Files.readAllBytes(it)
            if (ctx.classes[name] == null || !content.contentEquals(ctx.classes[name]!!)) {
                ctx.classes[name] = content
                buffer.add(name to content)
            }
        }
    }
}

class PathToClassName(private val root: Path) : FunctionEx<Path, String> {

    override fun applyEx(t: Path) = root
        .relativize(t)
        .toString()
        .replace('/', '.')
        .dropLast(".class".length)

}

class ContextHolder(val classesDirectory: Path = Paths.get(System.getProperty("target"))) {
    var classes: MutableMap<String, ByteArray> = mutableMapOf()
}