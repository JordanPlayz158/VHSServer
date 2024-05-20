package dev.jordanadams.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class CreateLobbyDTO(
  val connectionString: String,
  override val version: Int,
  override val idpk: String
) : VHSDTO()

@Serializable(with = LobbyActionsSerializer::class)
enum class LobbyAction(val key: String) {
  CREATE_LOBBY("createLobby"),
  CLOSE_LOBBY("closeLobby")
}

object LobbyActionsSerializer : KSerializer<LobbyAction> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LobbyActions", PrimitiveKind.STRING)
  override fun serialize(encoder: Encoder, value: LobbyAction) = encoder.encodeString(value.key)
  override fun deserialize(decoder: Decoder): LobbyAction = LobbyAction.entries.first { it.key == decoder.decodeString() }
}

@Serializable
data class CloseLobbyDTO(
  val lobbyData: LobbyDataDTO,
  val matchSettings: MatchSettingsDTO,
  override val version: Int,
  override val idpk: String
) : VHSDTO()


@Serializable
data class LobbyDataDTO(
  val lobbyName: String,
  val numTeens: Int,
  val numEvils: Int,
  val levelType: Int,
  val beaconPort: Int,
  // Probably an Enum
  val closeReason: String
)

@Serializable
data class MatchSettingsDTO(
  // Enum
  val selectedMap: Map
)

enum class Map(name: String) {
  // MDA - Map(?) ? ?
  // HSPB - HighSchool Public Beta (4 letter description of map?)
  // Default|Seasonal - Type
  MDA_HSPB_Default("HighSchool"),
  MDA_HSPB_Halloween("Hallways of Horror (HighSchool)"),
  MDA_HOTE_Default("Hotel"),
  // Holiday = Christmas?
  MDA_HOTE_Holiday("Ho Ho Hotel (Hotel)"),
  MDA_FATY_Default("Facility"),
  MDA_GASO_Default("The Eviscerator"),
  MDA_ARBS_Default("Outpost K"),
  MDA_GRAV_Default("Graveyard")
}

enum class CloseReason() {
  // LCR - Lobby Close Reason
  LCR_NONE
}