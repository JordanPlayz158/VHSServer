package dev.jordanadams.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class LoginDTO(
  @Serializable(with = ZonedDateTimeSerializer::class)
  val dbTimestamp: ZonedDateTime,
  val platformToken: String,
  override val version: Int,
  override val idpk: String,
) : VHSDTO()

object ZonedDateTimeSerializer : KSerializer<ZonedDateTime> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ZonedDateTime", PrimitiveKind.STRING)

  private const val PATTERN = "yyyy.MM.dd-HH.mm.ss"
  private val encodedFormatter = DateTimeFormatter.ofPattern(PATTERN)
  private val decodedFormatter = DateTimeFormatter.ofPattern("$PATTERN zzz")

  override fun serialize(encoder: Encoder, value: ZonedDateTime) = encoder.encodeString(value.format(encodedFormatter))

  override fun deserialize(decoder: Decoder): ZonedDateTime = ZonedDateTime.parse(decoder.decodeString() + " UTC", decodedFormatter)
}