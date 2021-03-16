package dev.herod.kmpp.files

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class KFileNativeTest : KFileTest {

    @Test
    override fun readLinesTest() {
        val file = file(absolutePath = "/etc/hosts")
        assertTrue {
            runBlocking {
                "127.0.0.1\tlocalhost" in file.readLines().toList()
            }
        }
    }

    @Test
    override fun sizeTest() {
        val file = file(absolutePath = "/etc/hosts")
        assertTrue { file.size() > 200 }
    }

    @Test
    override fun listFilesTest() {
        val file = file(absolutePath = "/etc/")
        val flow = file.listFiles()
        val files = runBlocking {
            flow.toList()
        }
        assertTrue {
            files.size > 1 && file !in files
        }
    }

    @Test
    override fun isFileTest() {
        val file = file(absolutePath = "/etc/hosts")
        assertTrue(file.isFile())
    }

    override fun isDirectoryTest() {
        val file = file(absolutePath = "/etc")
        assertTrue(file.isDirectory())
    }
}
