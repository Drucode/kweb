package kw.server

import java.net.*
import java.util.LinkedList
import java.util.concurrent.Executors
import org.jboss.netty.bootstrap.ServerBootstrap
import org.jboss.netty.buffer.ChannelBuffers
import org.jboss.netty.channel.*
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory
import org.jboss.netty.handler.codec.http.*
import org.jboss.netty.handler.stream.ChunkedWriteHandler
import org.jboss.netty.util.CharsetUtil
import java.awt.Desktop

// This is workaround for compiler bug
fun <T> T.bind(op: T.()->Any?) = { op() }

fun HttpResponse.set(header: String, value: Any?): Unit {
    setHeader(header, value)
}

var HttpResponse.content: Any?
    get() = throw UnsupportedOperationException()
    set(c: Any?) {
        val buffer = ChannelBuffers.copiedBuffer(c.toString(), CharsetUtil.UTF_8).sure()
        setContent(buffer)
        setHeader("Content-Length", buffer.readableBytes())
    }

fun ServerBootstrap.configPipeline(config: ChannelPipeline.() -> Unit) = setPipelineFactory(object: ChannelPipelineFactory {
    override fun getPipeline(): ChannelPipeline {
        val pipeline = DefaultChannelPipeline()
        pipeline.bind(config) ()
        return pipeline
    }
})
