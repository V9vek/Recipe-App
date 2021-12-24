package com.vivek.recipeapp.interactors.app

import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

object DoesNetworkHaveInternet {

    // Execute on background thread
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            println("DoesNetworkHaveInternet  -> execute: PINGING GOOGLE")
            // val socket = Socket()
            val socket = socketFactory.createSocket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            println("DoesNetworkHaveInternet -> execute: PING SUCCESS")
            true
        } catch (e: IOException) {
            println("DoesNetworkHaveInternet -> execute: No Internet Connection $e")
            false
        }
    }
}