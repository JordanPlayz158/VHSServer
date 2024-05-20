package dev.jordanadams

import dev.jordanadams.dto.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.*

fun main() {
  embeddedServer(CIO, port = 8443) {
    install(ContentNegotiation) {
      json()
    }

    install(DoubleReceive)

    routing {
      route("/metagame/THEEND_Game/Client/") {

        post("Login/") {
          val guid = call.parameters["guid"]
          val loginDto = grabAndPrint<LoginDTO>(call)

          val loginDetails = mapOf("playerSettingsData" to JsonPrimitive("4389789165"))
          //val loginDetails = mapOf<String, JsonElement>()

          call.respond(JsonObject(loginDetails))
        }
        post("Discover/") {
          val guid = call.parameters["guid"]
          val discoverDto = grabAndPrint<DiscoverDTO>(call)

          val discoverDetails = mapOf("playerSettingsData" to JsonPrimitive("4389789165"))


//          if (discoverDto != null) {
//            call.respond(ServerDiscoverDTO(
//              2, 4, 8,
//              16, 32, 64, 128, 256,
//              512, 1024, 2048, 4096, 8192, 16384,
//              2, 2, 2, 2, 2,
//              2, 2, discoverDto.version, discoverDto.idpk
//            ))
//            return@post
//          }

          call.respond(JsonObject(discoverDetails))
        }
        post("UploadPlayerSettings/") {
          val guid = call.parameters["guid"]
          val uploadPlayerSettingsDTO = grabAndPrint<UploadPlayerSettingsDTO>(call)

          if (uploadPlayerSettingsDTO !== null) {
            val playerSettingsDTO = grabAndPrint<PlayerSettingsDTO>(uploadPlayerSettingsDTO.playerSettingsData)
          }

          call.respondText("{}")
        }
        post("UseCustomLobby/") {
          val guid = call.parameters["guid"]
          val (removedAction, data) = popKey(call.receive<JsonObject>(), "action")

          when (LobbyAction.entries.first { it.key == removedAction }) {
            LobbyAction.CREATE_LOBBY -> {
              val createLobbyDTO = grabAndPrint<CreateLobbyDTO>(data.toString())
            }
            LobbyAction.CLOSE_LOBBY -> {
              val closeLobbyDTO = grabAndPrint<CloseLobbyDTO>(data.toString())
            }
            else -> {
              printPadded(data.toString())
            }
          }

          call.respondText("{}")
        }
        post("SetCharacterLoadout/") {
          val guid = call.parameters["guid"]
          // UIS_TeenCostume key is missing
          // enable perk and back out of money to reproduce
          val setCharacterLoadoutDTO = grabAndPrint<SetCharacterLoadoutDTO>(call)
          call.respondText("{}")
        }
        post("RedeemProductCode/") {
          val guid = call.parameters["guid"]
          val redeemProductCodeDTO = grabAndPrint<RedeemProductCodeDTO>(call)
          call.respondText("{}")
        }
      }

    }

    install(Intercept)
  }.start(wait = true)
}


const val prefix = "/metagame/THEEND_Game/Client/"
val Intercept = createApplicationPlugin(name = "Intercept All") {
  onCall { call ->
    val url = call.request.uri
    if (url.startsWith(prefix)) {
      val subUrl = url.substring(prefix.length)

      if (subUrl.startsWith("Login")
        || subUrl.startsWith("Discover")
        || subUrl.startsWith("UploadPlayerSettings")
        || subUrl.startsWith("UseCustomLobby")
        || subUrl.startsWith("SetCharacterLoadout")
        || subUrl.startsWith("RedeemProductCode")) return@onCall
    }


    println("URL: $url")
    println("Method: " + call.request.httpMethod)
    println("Data:" + call.receiveText())
  }
}

fun printPadded(data: Any) {
  println("\n$data\n")
}

inline fun <reified T : Any> grabAndPrint(string: String): T? {
  var data: T? = null

  try {
    data = Json.decodeFromString<T>(string)
    printPadded(data)
  } catch (e: Throwable) {
    printPadded(string)
  }

  return data
}

suspend inline fun <reified T : Any> grabAndPrint(call: ApplicationCall): T? {
  return grabAndPrint<T>(call.receiveText())
}

fun removeKey(data: JsonObject, key: String): JsonObject {
  return JsonObject(data.toMutableMap().apply { remove(key) })
}

fun popKey(data: JsonObject, key: String): Pair<String, JsonObject> {
  lateinit var value: String
  val newData = data.toMutableMap().apply { value = remove(key)!!.jsonPrimitive.content }
  return value to JsonObject(newData)
}







//  val protocols = arrayOf("TLSv1.2")
//  // All ciphers from VHS Client Hello because apparently just grabbing one from the list
//  //  still resulted in inappropriate ciphers error
//  val cipherSuites = arrayOf(
//    "TLS_AES_256_GCM_SHA384",
//    "TLS_CHACHA20_POLY1305_SHA256",
//    "TLS_AES_128_GCM_SHA256",
//    "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384",
//    "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384",
//    "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384",
//    "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384",
//    "TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256",
//    "TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
//    "TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256",
//    // Commented out ciphers not recognized
//    //"TLS_ECDHE_ECDSA_WITH_AES_256_CCM_8",
//    //"TLS_ECDHE_ECDSA_WITH_AES_256_CCM",
//    //"TLS_DHE_RSA_WITH_AES_256_CCM_8",
//    //"TLS_DHE_RSA_WITH_AES_256_CCM",
//    //"TLS_ECDHE_ECDSA_WITH_ARIA_256_GCM_SHA384",
//    //"TLS_ECDHE_RSA_WITH_ARIA_256_GCM_SHA384",
//    //"TLS_DHE_DSS_WITH_ARIA_256_GCM_SHA384",
//    //"TLS_DHE_RSA_WITH_ARIA_256_GCM_SHA384",
//    "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384",
//    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384",
//    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256",
//    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA256",
//    //"TLS_ECDHE_ECDSA_WITH_CAMELLIA_256_CBC_SHA384",
//    //"TLS_ECDHE_RSA_WITH_CAMELLIA_256_CBC_SHA384",
//    //"TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA256",
//    //"TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA256",
//    "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
//    "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
//    "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
//    "TLS_DHE_DSS_WITH_AES_256_CBC_SHA",
//    //"TLS_DHE_RSA_WITH_CAMELLIA_256_CBC_SHA",
//    //"TLS_DHE_DSS_WITH_CAMELLIA_256_CBC_SHA",
//    "TLS_RSA_WITH_AES_256_GCM_SHA384",
//    //"TLS_RSA_WITH_AES_256_CCM_8",
//    //"TLS_RSA_WITH_AES_256_CCM",
//    //"TLS_RSA_WITH_ARIA_256_GCM_SHA384",
//    "TLS_RSA_WITH_AES_256_CBC_SHA256",
//    //"TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256",
//    "TLS_RSA_WITH_AES_256_CBC_SHA",
//    //"TLS_RSA_WITH_CAMELLIA_256_CBC_SHA",
//    "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256",
//    "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
//    "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256",
//    "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256",
//    //"TLS_ECDHE_ECDSA_WITH_AES_128_CCM_8",
//    //"TLS_ECDHE_ECDSA_WITH_AES_128_CCM",
//    //"TLS_DHE_RSA_WITH_AES_128_CCM_8",
//    //"TLS_DHE_RSA_WITH_AES_128_CCM",
//    //"TLS_ECDHE_ECDSA_WITH_ARIA_128_GCM_SHA256",
//    //"TLS_ECDHE_RSA_WITH_ARIA_128_GCM_SHA256",
//    //"TLS_DHE_DSS_WITH_ARIA_128_GCM_SHA256",
//    //"TLS_DHE_RSA_WITH_ARIA_128_GCM_SHA256",
//    "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
//    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256",
//    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA256",
//    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA256",
//    //"TLS_ECDHE_ECDSA_WITH_CAMELLIA_128_CBC_SHA256",
//    //"TLS_ECDHE_RSA_WITH_CAMELLIA_128_CBC_SHA256",
//    //"TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA256",
//    //"TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA256",
//    "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
//    "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
//    "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
//    "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
//    //"TLS_DHE_RSA_WITH_SEED_CBC_SHA",
//    //"TLS_DHE_DSS_WITH_SEED_CBC_SHA",
//    //"TLS_DHE_RSA_WITH_CAMELLIA_128_CBC_SHA",
//    //"TLS_DHE_DSS_WITH_CAMELLIA_128_CBC_SHA",
//    "TLS_RSA_WITH_AES_128_GCM_SHA256",
//    //"TLS_RSA_WITH_AES_128_CCM_8",
//    //"TLS_RSA_WITH_AES_128_CCM",
//    //"TLS_RSA_WITH_ARIA_128_GCM_SHA256",
//    "TLS_RSA_WITH_AES_128_CBC_SHA256",
//    //"TLS_RSA_WITH_CAMELLIA_128_CBC_SHA256",
//    "TLS_RSA_WITH_AES_128_CBC_SHA",
//    //"TLS_RSA_WITH_SEED_CBC_SHA",
//    //"TLS_RSA_WITH_CAMELLIA_128_CBC_SHA",
//    //"TLS_RSA_WITH_IDEA_CBC_SHA",
//    "TLS_EMPTY_RENEGOTIATION_INFO_SCSV"
//  )
//
//val factory = SSLServerSocketFactory.getDefault()
//
//factory.createServerSocket(443).use { socket ->
//  val serverSocket = socket as SSLServerSocket
//
//  serverSocket.enabledProtocols = protocols
//  serverSocket.enabledCipherSuites = cipherSuites
//
//  val inputStream = serverSocket.accept().getInputStream().reader().buffered()
//
//  var request = inputStream.readLine()
//  while (request != null) {
//    println("Request: $request")
//    request = inputStream.readLine()
//  }
//}