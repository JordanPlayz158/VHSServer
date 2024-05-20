package dev.jordanadams.dto

import kotlinx.serialization.Serializable

@Serializable
data class SetCharacterLoadoutDTO(
  val sessionTicketId: String,
  val characterType: String,
  val loadoutChanges: LoudoutChangesDTO,
  val returnChangesOnly: Boolean,

  override val version: Int,
  override val idpk: String
) : VHSDTO()

@Serializable
data class LoudoutChangesDTO(
  val UIS_TeenEmoteAny: String,
  val UIS_TeenEmote1: String,
  val UIS_TeenEmote2: String,
  val UIS_TeenEmote3: String,
  val UIS_TeenEmote4: String,
  val UIS_TeenEmote5: String,
  val UIS_TeenEmote6: String,
  val UIS_TeenStickerAny: String,
  val UIS_TeenSticker1: String,
  val UIS_TeenSticker2: String,
  val UIS_TeenSticker3: String,
  val UIS_TeenSticker4: String,
  val UIS_TeenSticker5: String,
  val UIS_TeenSticker6: String,
  val UIS_TeenSpectral: String,
  val UIS_EvilSkin: String,
  val UIS_EvilPerk5: String,
  val UIS_EvilEmoteAny: String,
  val UIS_EvilEmote1: String,
  val UIS_EvilEmote2: String,
  val UIS_EvilEmote3: String,
  val UIS_EvilEmote4: String,
  val UIS_EvilEmote5: String,
  val UIS_EvilEmote6: String,
  val UIS_EvilStickerAny: String,
  val UIS_EvilSticker1: String,
  val UIS_EvilSticker2: String,
  val UIS_EvilSticker3: String,
  val UIS_EvilSticker4: String,
  val UIS_EvilSticker5: String,
  val UIS_EvilSticker6: String,
  val UIS_EvilAccessoryRoot: String,
  val UIS_EvilAccessoryUpper: String,
  val UIS_EvilAccessoryMid: String,
  val UIS_EvilAccessoryLower: String,
  val UIS_EvilSpectral: String,
  val UIS_EvilScream: String,
  val UIS_EvilFeast: String,
  val UIS_EvilRage: String
)