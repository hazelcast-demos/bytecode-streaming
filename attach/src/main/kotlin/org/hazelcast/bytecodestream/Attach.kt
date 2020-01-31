package org.hazelcast.bytecodestream

import com.sun.tools.attach.VirtualMachine

fun main(args: Array<String>) {
    with(VirtualMachine.attach(getMainPid().toString())) {
        try {
            loadAgent(args[0])
        } finally {
            detach()
        }
    }
}

private fun getMainPid() = ProcessHandle
    .allProcesses()
    .filter { it.info().commandLine().isPresent }
    .filter { it.info().commandLine().get().endsWith("org.hazelcast.bytecodestream.Main") }
    .findAny()
    .get()
    .pid()