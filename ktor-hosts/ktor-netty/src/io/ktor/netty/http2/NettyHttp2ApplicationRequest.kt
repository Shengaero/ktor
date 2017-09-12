package io.ktor.netty.http2

import io.netty.channel.*
import io.netty.handler.codec.http2.*
import io.ktor.application.*
import io.ktor.cio.*
import io.ktor.content.*
import io.ktor.host.*
import io.ktor.http.*
import io.ktor.pipeline.*
import io.ktor.request.*
import io.ktor.util.*
import java.net.*

internal class NettyHttp2ApplicationRequest(
        call: ApplicationCall,
        val context: ChannelHandlerContext,
        val nettyHeaders: Http2Headers) : BaseApplicationRequest(call) {

    override val headers: ValuesMap by lazy { ValuesMap.build(caseInsensitiveKey = true) { nettyHeaders.forEach { append(it.key.toString(), it.value.toString()) } } }

    val contentQueue = SuspendQueue<Http2DataFrame>(10)
    private val contentChannel: NettyHttp2ReadChannel = NettyHttp2ReadChannel(contentQueue)

    override val queryParameters: ValuesMap by lazy {
        header(":path")?.let { path ->
            parseQueryString(path.substringAfter("?", ""))
        } ?: ValuesMap.Empty
    }

    override val local = object : RequestConnectionPoint {
        override val method: HttpMethod = nettyHeaders.method()?.let { HttpMethod.parse(it.toString()) } ?: HttpMethod.Get

        override val scheme: String
            get() = nettyHeaders.scheme()?.toString() ?: "http"

        override val version: String
            get() = "HTTP/2"

        override val uri: String
            get() = nettyHeaders.path()?.toString() ?: "/"

        override val host: String
            get() = nettyHeaders.authority()?.toString() ?: "localhost"

        override val port: Int
            get() = nettyHeaders.authority()?.toString()?.substringAfter(":")?.toInt()
                    ?: (context.channel().localAddress() as? InetSocketAddress)?.port
                    ?: 80

        override val remoteHost: String
            get() = "unknown" // TODO
    }

    override val cookies: RequestCookies
        get() = throw UnsupportedOperationException()

    override fun receiveContent() = NettyHttp2IncomingContent(this)

    class NettyHttp2IncomingContent internal constructor(override val request: NettyHttp2ApplicationRequest) : IncomingContent {
        override fun readChannel(): ReadChannel = request.contentChannel
        override fun multiPartData(): MultiPartData {
            throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}
