package dev.jordanadams.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement

@Serializable
data class UploadPlayerSettingsDTO(
  val sessionTicketId: String,
  // They wrapped it in a string, interesting (you can just embed objects in json just fine and they did so in other areas)
  val playerSettingsData: String, // PlayerSettingsDTO
  override val version: Int,
  override val idpk: String
) : VHSDTO()

@Serializable
data class PlayerSettingsDTO(
  val mAutomaticRegion: Boolean,
  // Could probably turn most of the strings into enums if had more data
  val mMatchmakingRegion: MatchmakingRegion,
  val mLanguage: String,
  val mSelectedGameMode: String,
  val mbShowReticle: Boolean,
  val mbShowScoreFeed: Boolean,
  val mbShowGameHUD: Boolean,
  val mbHintsToggle: Int,
  val mDangerSenseType: DangerSenseType,
  // 0 - 1.0 corresponds to 0-100
  val mDangerSenseSize: Float,
  val mDangerSenseColor: DangerSenseColor,
  val mGraphicsColorBlindMode: ColorBlindMode,
  // 0 - 1.0 corresponds to 0-100
  val mGraphicsColorBlindIntensity: Float,
  val mbAudioGameAudioMuteOnUnfocused: Boolean,
  val mbVoiceChatEnabled: Boolean,
  val mVoiceChatMode: VoiceChatMode,
  val mVoiceChatChannel: String,
  val mbVoiceChatMuteOnUnfocused: Boolean,
  val mbMouseInvertY: Boolean,
  val mbKbdSprintToggle: Boolean,
  val mbKbdCrouchToggle: Boolean,
  val mKeyToGameActions: JsonElement,
  val mbControllerInvertY: Boolean,
  val mbControllerSprintToggle: Boolean,
  val mbControllerCrouchToggle: Boolean,
  val mbShowSocialNotifications: Boolean,
  val mbNotifyFriendsWhenPlaying: Boolean,
  val mbAutoMutePlayersNotInParty: Boolean,
  val mbHideDisplayName: Boolean,
  val mbHideOtherPlayerNames: Boolean,
  val mbMatchmakingDelay: Boolean,
  val mbAutoMutePrevMutedPlayers: Boolean,
  val mStoredMatchmakingType: String,
  // If I knew all the internal names, would definitely make an enum
  val mLastStoredCharacterType: String,
  val mStoredTeenCharacterType: String,
  val mStoredEvilCharacterType: String,
  // Might be an array of ints or strings or could be class, confirm later
  //  Need to set to String rather than Any (which is more accurate as idk
  //  to satisfy kotlinx-serialization
  val mStoredHintHistory: Array<String>,
  val mStoredGuideHistory: Array<String>,
  val mLastSeenHint: String,
  val mTipsHistory: Array<String>,
  val mNewsAnnouncements: Array<String>,
  val mbShowBetaAnnouncement: Boolean,
  val mbShowAnnouncement1: Boolean,
  val mbShowAnnouncement2: Boolean,
  val mHighestSeasonAnnouncement: Int
)

enum class MatchmakingRegion(name: String) {
  MR_US_East1("US-EAST"),
  MR_US_West2("US-WEST"),
  MR_EU_Central1("EUROPE")
}

enum class CharacterType {
  TEEN,
  MONSTER
}

enum class Character(private val type: CharacterType, name: String) {
  CT_Cheerleader(CharacterType.TEEN, "Gloria"),



  CT_Toad(CharacterType.MONSTER, "W.A.R.T"),

  // Deobfuscation
  CT_Jock(CharacterType.TEEN, "Brett"),
  CT_Outsider(CharacterType.TEEN, "Jess"),
  CT_Punk(CharacterType.TEEN, "Leo"),
  CT_Virgin(CharacterType.TEEN, "Faith"),
  CT_Nerd(CharacterType.TEEN, "Reggie"),

  CT_Eradicator(CharacterType.MONSTER, "Deathwire"),
  CT_Werewolf(CharacterType.MONSTER, "Werewolf"),
  CT_Anomaly(CharacterType.MONSTER, "Anomaly"),
  CT_DollMaster(CharacterType.MONSTER, "Doll Master");

  fun teens(): Array<Character> {
    return entries.filter { it.type == CharacterType.TEEN }.toTypedArray()
  }

  fun monsters(): Array<Character> {
    return entries.filter { it.type == CharacterType.MONSTER }.toTypedArray()
  }
}

enum class HintsToggle(val value: Int) {
  NONE(1),

  // They use same concept of chmod, not sure
  //  the exact name of it, but it uses the values
  //  and adds them up to determine the set ones
  //  ex.
  //  2 + 4     = 6 = BASIC + INTERMEDIATE
  //  2 + 4 + 8 = 14 = BASIC + INTERMEDIATE + ADVANCED
  BASIC(2),
  INTERMEDIATE(4),
  ADVANCED(8),
  MISCELLANEOUS(16),

  ALL(62),
}

fun intToHints(flags: Int): List<HintsToggle> {
  // Method parameters are immutable
  var flagsMutable = flags

  val hints = arrayListOf<HintsToggle>()
  for (hint in HintsToggle.entries.reversed()) {
    val hintValue = hint.value
    if (flagsMutable >= hintValue) {
      hints.add(hint)
      flagsMutable -= hintValue
    }
  }

  return hints
}

fun intToHintsBitwise(flags: Int) {
  // 0 1 1 1 1 1 = 62
  // 0 1 = 2
  // ---
  // 0 0 1 1 1 1 = 60
  //
  // Bit at position 2 = 0
  //
  // Think of & and | bitwise bits as booleans
  // 0 = false
  // 0 = false
  //
  // | = false || false = false
  // & = false && false = false
  if (flags and HintsToggle.BASIC.value == 0) {

  }
}

enum class HintsToggleHex(val value: Int) {
  NONE(0x01),

  // They use same concept of chmod, not sure
  //  the exact name of it, but it uses the values
  //  and adds them up to determine the set ones
  //  ex.
  //  2 + 4     = 6 = BASIC + INTERMEDIATE
  //  2 + 4 + 8 = 14 = BASIC + INTERMEDIATE + ADVANCED
  BASIC(0x02),
  INTERMEDIATE(0x04),
  ADVANCED(0x08),
  MISCELLANEOUS(0xF + 0x01),

  ALL(0xF + 0xF + 0xF + 0xF + 0x02),
}

// https://stackoverflow.com/a/75644603/12005894
open class EnumAsIntSerializer<T:Enum<*>>(
  serialName: String,
  val serialize: (v: T) -> Int,
  val deserialize: (v: Int) -> T
) : KSerializer<T> {
  override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(serialName, PrimitiveKind.INT)

  override fun serialize(encoder: Encoder, value: T) = encoder.encodeInt(serialize(value))

  override fun deserialize(decoder: Decoder): T {
    val v = decoder.decodeInt()
    return deserialize(v)
  }
}

// Order/Ordinal matches client response
@Serializable(with = DangerSenseTypeSerializer::class)
enum class DangerSenseType {
  STANDARD,
  DISABLED
}

private class DangerSenseTypeSerializer: EnumAsIntSerializer<DangerSenseType>("DangerSenseType", {it.ordinal}, {DangerSenseType.entries[it]})

// Ordinal
@Serializable(with = DangerSenseColorSerializer::class)
enum class DangerSenseColor {
  WHITE,
  BLUE,
  MAGENTA,
  YELLOW,
  CYAN,
  RED,
  GREEN
}

private class DangerSenseColorSerializer: EnumAsIntSerializer<DangerSenseColor>("DangerSenseColor", {it.ordinal}, {DangerSenseColor.entries[it]})

enum class ColorBlindMode {
  NormalVision,
  Deuteranope,
  Protanope,
  Tritanope,
}

enum class VoiceChatMode {
  VC_VOICEDETECT,
  VC_PUSHTOTALK,
  VC_MUTED
}

// From Deobfuscation
enum class MatchModType {
  MM_NONE,
  MM_MAPVOTE_HSPB_01,
  MM_MAPVOTE_FATY_01,
  MM_MAPVOTE_HOTE_01,
  MM_MAPVOTE_ARBS_01,
  MM_MAPVOTE_GASO_01,
  MM_MAPVOTE_GRAV_01,
  MM_MAPVOTE_LAIR_01,
  MM_MAPVOTE_SHIP_01,
  MM_MAPVOTE_LICH_01,
  MM_MAPVOTE_TEMA_01,
  MM_MAPVOTE_UPSD_01,
  MM_MAPVOTE_HSPB_HALLOWEEN_01,
  MM_MAPVOTE_HOTE_HOLIDAY_01,
  MM_PICKUPSTART_PILLS_01,
  MM_PICKUPSTART_ADRENALINE_01,
  MM_PICKUPSTART_WALKIE_01,
  MM_PICKUPSTART_NOISEMAKER_01,
  MM_PICKUPSTART_SMOKEBOMB_01,
  MM_PICKUPSTART_PILLS_HOLIDAY_01,
  // Ghidra..... IDK
  //MM_PICKUPSTART_ADRENALINE_HOLI...
  //MM_PICKUPSTART_NOISEMAKER_HOLI...
  MM_PICKUPSTART_TREAT_HOLIDAY_01,
  MM_PICKUPSTASH_PILLS_01,
  MM_PICKUPSTASH_ADRENALINE_01,
  MM_PICKUPSTASH_WALKIE_01,
  MM_PICKUPSTASH_NOISEMAKER_01,
  MM_PICKUPSTASH_RESURRECT_01,
  MM_PICKUPSTASH_SMOKEBOMB_01,
  MM_PICKUPSTASH_PILLS_HOLIDAY_01,
  //MM_PICKUPSTASH_ADRENALINE_HOLI...
  //MM_PICKUPSTASH_NOISEMAKER_HOLI...
}

// From Deobfuscation
// EStoreSKUDataType
enum class StoreSKUType {
  // ESSDT_None
  None,
  SteamMicroTxn,
  SteamDLC,
  EpicOffer,
}