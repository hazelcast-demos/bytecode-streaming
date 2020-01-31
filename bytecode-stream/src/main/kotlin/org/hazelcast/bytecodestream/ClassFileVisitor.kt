package org.hazelcast.bytecodestream

import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

class ClassFileVisitor : FileVisitor<Path> {

    private val matcher = FileSystems.getDefault().getPathMatcher("glob:**.{class}")
    val classes = mutableListOf<Path>()

    override fun postVisitDirectory(dir: Path, e: IOException?) = FileVisitResult.CONTINUE
    override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes?) = FileVisitResult.CONTINUE

    override fun visitFile(file: Path, attrs: BasicFileAttributes?): FileVisitResult {
        if (matcher.matches(file)) classes.add(file)
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path, e: IOException?): FileVisitResult {
        e?.printStackTrace()
        return FileVisitResult.CONTINUE
    }
}